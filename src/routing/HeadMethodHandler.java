package routing;

import http.HttpRequest;
import http.HttpResponse;
import routing.RequestContext;
import http.Status;

/**
 * Handler for the HTTP HEAD method.
 *
 * Responsibilities:
 *  - Return the same headers as GET would
 *  - Do NOT include a body
 *
 * This allows clients to check resource metadata (type, length, etc.)
 * without downloading the whole file.
 */
public class HeadMethodHandler implements MethodHandler {

    @Override
    public HttpResponse handle(HttpRequest request,
                               RequestContext context,
                               FileMeta metadata) throws Exception {

        // HEAD responses include headers but no body
        return HttpResponse.builder(Status.OK_200)
                .setHeader("Content-Type", metadata.contentType)
                .setHeader("Content-Length", Long.toString(metadata.contentLength))
                .build();
    }
}