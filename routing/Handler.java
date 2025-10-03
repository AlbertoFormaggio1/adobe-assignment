package routing;

import http.HttpRequest;
import routing.RequestContext;
import http.HttpResponse;

public interface Handler {
    public HttpResponse handle(HttpRequest request, RequestContext context);
}
