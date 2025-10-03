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

public class WebServer{
    private final int port;
    private final Router router;
    private ServerSocket serverSocket;
    private boolean running = false;
    private Thread acceptThread;

    public WebServer(int port, Router router){
        this.port = port;
        this.router = router;
    }

    public void start(){
        Utils.log("Starting server...");
        // If the server is already running, do not do anything
        if (running) return;

        try{
            serverSocket = new ServerSocket(port);
        } catch(IOException e){
            Utils.log("Failed to start server: " + e.getMessage());
            return;
        }

        running = true;
        Utils.log("Server running on port: " + serverSocket.getLocalPort());
        
        acceptThread = new Thread(this::acceptLoop, "accept-loop");
        // Start the thread accepting incoming connections
        acceptThread.start();
    }

    public void await() throws InterruptedException {
        if (acceptThread != null) acceptThread.join();
    }

    public void stop(){
        Utils.log("Stopping server...");
        // Stop the server if not closed
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

    private void acceptLoop(){
        while(running){
            try{
                Utils.log("Waiting for incoming connection...");
                // Accept inbound connection
                Socket clientSocket = serverSocket.accept();
                Utils.log("Accepted connection from " + clientSocket.getRemoteSocketAddress());

                // Create connection handler and execute it.
                ConnectionHandler connectionHandler = new ConnectionHandler(clientSocket, router);
                Thread connectionThread = new Thread(connectionHandler);
                connectionThread.start();   
                Utils.log("Finished handling " + clientSocket.getRemoteSocketAddress());

            } catch(Exception e){
                // If running, simply skip the request if exception was thrown
                if(running) Utils.log("Inbound request not accepted");
                // If server not running exit from the loop
                break;
            }
        }
    }
    
}