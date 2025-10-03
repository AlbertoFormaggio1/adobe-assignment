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

/**
 * Handles a single client connection.
 * Each instance is run in its own thread (spawned by WebServer).
 *
 * Responsibilities:
 *  - Read and parse an HTTP request from the socket input
 *  - Route the request using the provided Router
 *  - Convert the result (or an error) into an HttpResponse
 *  - Write the response back to the client via the output stream
 *  - Close the socket when done
 */
public class ConnectionHandler implements Runnable {
    private final Socket socket;   // client socket for this connection
    private final Router router;   // router that decides how to handle the request
    
    public ConnectionHandler(Socket socket, Router router) {
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void run() {
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            HttpResponse response;

            try {
                // 1. Parse the raw request text into an HttpRequest object
                //    Headers are ASCII by HTTP/1.1 spec; body may use other charsets
                HttpRequest req = HttpRequestParser.parse(
                        new InputStreamReader(in, java.nio.charset.StandardCharsets.US_ASCII)
                );

                // 2. Route the request to a handler, which returns a response object
                response = router.route(req);

            } catch (HttpException e) {
                // Known/expected error (bad request, not found, etc.)
                // Build a response with the corresponding HTTP status and message
                response = HttpResponse.builder(e.getStatus())
                        .setBody(e.getMessage()) // small text body for error
                        .build();

            } catch (Exception e) {
                // Unexpected/unhandled exception -> return a 500 Internal Server Error
                String msg = (e.getMessage() != null ? e.getMessage() : "Internal Server Error");
                response = HttpResponse.builder(Status.INTERNAL_SERVER_ERROR_500)
                        .setBody(msg)
                        .build();
            }

            // 3. Serialize the response (status line, headers, body) to the client
            HttpResponseWriter.write(response, out);

        } catch (Exception e) {
            // Covers I/O or parsing exceptions not already handled above
            e.printStackTrace();
        }

        // 4. Finally, close the socket (even if errors occurred)
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}