package routing;

import java.io.FileInputStream;

import http.Status;
import http.HttpRequest;
import http.HttpResponse;

/**
 * Handler for the HTTP GET method.
 *
 * Responsibilities:
 *  - Open the requested file as a stream
 *  - Build an HttpResponse with status 200 OK
 *  - Set headers (Content-Type, Content-Length)
 *  - Attach the file stream as the response body
 */
public class GetMethodHandler implements MethodHandler {

    @Override
    public HttpResponse handle(HttpRequest request,
                               RequestContext context,
                               FileMeta metadata) throws Exception {

        // Open the file as a stream (will be streamed to the client)
        FileInputStream body = new FileInputStream(metadata.path.toString());

        // Build the HTTP response with headers and body
        return HttpResponse.builder(Status.OK_200)
                .setHeader("Content-Type", metadata.contentType)
                .setHeader("Content-Length", String.valueOf(metadata.contentLength))
                .setBody(body, metadata.contentLength) // attach file content
                .build();
    }
}