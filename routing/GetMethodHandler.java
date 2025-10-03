package routing;

import java.io.FileInputStream;

import http.Status;
import http.HttpRequest;
import http.HttpResponse;
import routing.MethodHandler;
import routing.RequestContext;

public class GetMethodHandler implements MethodHandler {
    @Override
    public HttpResponse handle(HttpRequest request, RequestContext context, FileMeta metadata) throws Exception{
        FileInputStream body = new FileInputStream(metadata.path.toString());
        return HttpResponse.builder(Status.OK_200).setHeader("Content-Type", metadata.contentType).setBody(body, metadata.contentLength).build();
    }
}
