package routing;

import http.HttpRequest;
import routing.RequestContext;
import http.HttpResponse;

/**
 * A generic handler for HTTP requests.
 *
 * Responsibilities:
 *  - Take an HttpRequest and the current RequestContext
 *  - Produce an HttpResponse
 *
 * Each specific handler (e.g., StaticFileHandler, HealthCheckHandler)
 * will implement this interface.
 */
public interface Handler {

    /**
     * Handle the given HTTP request in the provided context.
     *
     * @param request the parsed HTTP request (method, headers, path, etc.)
     * @param context server context (document root, config, etc.)
     * @return the HTTP response to send back to the client
     */
    HttpResponse handle(HttpRequest request, RequestContext context);
}