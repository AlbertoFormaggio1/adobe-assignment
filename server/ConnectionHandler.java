package server;

import java.net.*;
import java.io.*;
import http.HttpRequestParser;
import http.HttpRequest;

public class ConnectionHandler implements Runnable{
    private final Socket socket;
    
    public ConnectionHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        // Intantiating InputStream and OutputStream as autoclosing at the end of the try-catch regardless of the outcome
        try(InputStream inStream = socket.getInputStream();
        DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());){
            // Parse the client request and generate a HttpRequest data object
            HttpRequest req = HttpRequestParser.parse(new InputStreamReader(inStream));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}