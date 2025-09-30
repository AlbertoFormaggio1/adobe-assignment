package server;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer{
    public static int parseInputs(String args[]){
        // Check if argument is provided
        if(args.length == 0){
            throw new IllegalArgumentException("No port provided. The app must be executed as `java WebServer <port number>`");
        }

        int port = Integer.parseInt(args[0]);

        return port;
    }


    public static void main(String args[]) throws Exception{
        // Parsing input port
        int port = 0;
        try{
            port = WebServer.parseInputs(args);
        }
        catch(NumberFormatException e){
            System.err.println("The port provided is not valid, please provide an integer.");
            System.exit(1);
        }

        // Instantiate the Server Socket on the specified port
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server running on port: " + serverSocket.getLocalPort());

        //ConnectionHandlerFactory factory = new ConnectionHandlerFactory();

        while(true){
            //Wait for incoming connection from client
            Socket clientSocket = serverSocket.accept();

            ConnectionHandler handler = new ConnectionHandler(clientSocket);

            handler.run();
        }
    }
}