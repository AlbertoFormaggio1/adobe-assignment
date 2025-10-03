package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import routing.RequestContext;
import routing.Router;
import routing.StaticFileHandler;
import routing.RequestContext;
import util.Utils;

/**
 * A simple multi-threaded web server that accepts incoming TCP connections
 * on a specified port and delegates request handling to a Router.
 *
 * Responsibilities:
 *  - Bind to a TCP port and listen for connections
 *  - Accept incoming client sockets in a dedicated thread
 *  - Spawn a new thread per connection (via ConnectionHandler)
 *  - Provide start/stop lifecycle controls
 */
public class WebServer {
    private final int port;          // Port number where the server listens
    private final Router router;     // Router responsible for handling requests
    private ServerSocket serverSocket; // Underlying TCP listening socket
    private boolean running = false;   // Indicates if the server is running
    private Thread acceptThread;       // Thread dedicated to accepting connections

    /**
     * Create a new WebServer.
     * @param port   Port number to listen on
     * @param router Router used to route incoming HTTP requests
     */
    public WebServer(int port, Router router){
        this.port = port;
        this.router = router;
    }

    /**
     * Starts the web server:
     *  - Opens a ServerSocket bound to the configured port
     *  - Spawns an "accept loop" thread to handle incoming connections
     */
    public void start(){
        Utils.log("Starting server...");
        // Do nothing if already running
        if (running) return;

        try{
            // Bind to port
            serverSocket = new ServerSocket(port);
        } catch(IOException e){
            Utils.log("Failed to start server: " + e.getMessage());
            return;
        }

        running = true;
        Utils.log("Server running on port: " + serverSocket.getLocalPort());
        
        // Start background thread to accept new connections
        acceptThread = new Thread(this::acceptLoop, "accept-loop");
        acceptThread.start();
    }

    /**
     * Blocks until the accept thread has finished.
     * Useful when you want the server to keep running until explicitly stopped.
     */
    public void await() throws InterruptedException {
        if (acceptThread != null) acceptThread.join();
    }

    /**
     * Stops the web server:
     *  - Closes the ServerSocket (interrupts accept())
     *  - Marks running = false so loop exits
     */
    public void stop(){
        Utils.log("Stopping server...");
        if(serverSocket != null && !serverSocket.isClosed()){
            try{
                serverSocket.close();
            } catch (IOException e){
                Utils.log("Error closing server: " + e.getMessage());
                return;
            }
        }
        running = false;
    }

    /**
     * Loop that runs in its own thread:
     *  - Waits for incoming TCP connections
     *  - For each accepted socket, spawns a ConnectionHandler in a new thread
     *  - Continues until the server is stopped or an error occurs
     */
    private void acceptLoop(){
        while(running){
            try{
                Utils.log("Waiting for incoming connection...");
                // Block until a client connects
                Socket clientSocket = serverSocket.accept();
                Utils.log("Accepted connection from " + clientSocket.getRemoteSocketAddress());

                // Create a handler for this connection
                ConnectionHandler connectionHandler = new ConnectionHandler(clientSocket, router);

                // Run the handler in its own thread
                Thread connectionThread = new Thread(connectionHandler);
                connectionThread.start();   
                Utils.log("Finished handling " + clientSocket.getRemoteSocketAddress());

            } catch(Exception e){
                // If still running, skip this request and continue loop
                if(running) Utils.log("Inbound request not accepted");
                // If stop() was called, break the loop
                break;
            }
        }
    }
}