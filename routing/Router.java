package routing;

import routing.RequestContext;
import routing.Handler;
import http.HttpRequest;
import http.HttpResponse;

public class Router {
    private Handler handler;
    private RequestContext context;

    public Router(Handler handler, RequestContext ctx){
        this.handler = handler;
        this.context = ctx;
    }

    public HttpResponse route(HttpRequest request){
        return handler.handle(request, context);
    }
}
