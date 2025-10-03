package routing;

import java.io.FileInputStream;

import http.HttpRequest;
import http.HttpResponse;
import routing.MethodHandler;
import routing.RequestContext;
import http.Status;

public class HeadMethodHandler implements MethodHandler {
    @Override
    public HttpResponse handle(HttpRequest request, RequestContext context, FileMeta metadata) throws Exception{
        return HttpResponse.builder(Status.OK_200).setHeader("Content-Type", metadata.contentType).setHeader("Content-Length", Long.toString(metadata.contentLength)).build();
    }
}
