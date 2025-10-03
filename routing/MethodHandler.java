package routing;

import http.HttpRequest;
import http.HttpResponse;
import routing.RequestContext;
import routing.FileMeta;

public interface MethodHandler {
    HttpResponse handle(HttpRequest request, RequestContext context, FileMeta metadata) throws Exception;
}
