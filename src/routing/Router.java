package routing;

import http.HttpRequest;
import http.HttpResponse;

/**
 * Router is responsible for directing an HttpRequest to the right Handler.
 *
 * In this simplified version:
 *  - It holds a single Handler (e.g. StaticFileHandler)
 *  - It holds a RequestContext (e.g. document root)
 *  - It always forwards the request to that Handler
 *
 * Later this can be extended to:
 *  - support multiple handlers mapped by path prefix
 *  - choose different handlers based on method or headers
 *  - add middlewares for logging, authentication, etc.
 */
public class Router {
    private final Handler handler;          // the handler to delegate to
    private final RequestContext context;   // context (docRoot, etc.) passed to the handler

    /**
     * Create a new Router.
     *
     * @param handler the handler that processes requests
     * @param ctx     the request context (shared info like docRoot)
     */
    public Router(Handler handler, RequestContext ctx) {
        this.handler = handler;
        this.context = ctx;
    }

    /**
     * Route a request to the configured handler.
     *
     * @param request the parsed HttpRequest
     * @return the HttpResponse produced by the handler
     */
    public HttpResponse route(HttpRequest request) {
        return handler.handle(request, context);
    }
}