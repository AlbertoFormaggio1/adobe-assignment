package routing;

import http.HttpRequest;
import http.HttpResponse;
import routing.RequestContext;
import routing.FileMeta;

/**
 * A handler specialized for a specific HTTP method (e.g. GET, HEAD).
 *
 * Responsibilities:
 *  - Take an HttpRequest and the current RequestContext
 *  - Use FileMeta (already validated file info) to prepare the response
 *  - Return an HttpResponse that matches the semantics of the method
 *
 * Examples:
 *  - GetMethodHandler: includes headers + body
 *  - HeadMethodHandler: includes headers, no body
 */
public interface MethodHandler {

    /**
     * Handle an HTTP request of a given method.
     *
     * @param request  the parsed HTTP request
     * @param context  server context (doc root, config, etc.)
     * @param metadata file metadata (path, length, type) for the resolved file
     * @return an HttpResponse ready to be written back to the client
     * @throws Exception for I/O errors or unexpected failures
     */
    HttpResponse handle(HttpRequest request,
                        RequestContext context,
                        FileMeta metadata) throws Exception;
}