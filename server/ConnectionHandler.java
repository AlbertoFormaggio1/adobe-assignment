package server;

import java.net.*;
import java.io.*;
import http.HttpRequestParser;
import http.HttpRequest;
import routing.Router;
import routing.RequestContext;
import java.nio.file.Paths;
import http.HttpResponseWriter;
import http.Status;
import http.exceptions.HttpException;
import http.HttpResponse;

public class ConnectionHandler implements Runnable{
    private final Socket socket;
    private final Router router;
    
    public ConnectionHandler(Socket socket, Router router){
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void run(){
        try (InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream()) {

            HttpResponse response;

            try {
                // Parse the request (prefer specifying charset for headers)
                HttpRequest req = HttpRequestParser.parse(new InputStreamReader(in, java.nio.charset.StandardCharsets.US_ASCII));

                // Route to a handler and get a response
                response = router.route(req);

            } catch (HttpException e) {
                // Known, intentional HTTP error (e.g., 400/404/405)
                response = HttpResponse.builder(e.getStatus())
                        .setBody(e.getMessage())
                        .build();

            } catch (Exception e) {
                // Unexpected error -> 500
                String msg = (e.getMessage() != null ? e.getMessage() : "Internal Server Error");
                response = HttpResponse.builder(Status.INTERNAL_SERVER_ERROR_500)
                        .setBody(msg)
                        .build();
            }
            
            HttpResponseWriter.write(response, out);
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            // Close the socket
            socket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}