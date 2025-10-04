package test.routing;

import org.junit.Test;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import routing.FileMeta;
import routing.FileService;
import routing.RequestContext;
import routing.StaticFileHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import java.nio.file.Paths;
import java.nio.file.Path;
import http.Status;


public class TestStaticFileHandler {
    static class FakeFileService extends FileService {
        private final FileMeta meta;

        FakeFileService(FileMeta meta) {
            this.meta = meta;
        }

        @Override
        public FileMeta computeHeaders(HttpRequest request, RequestContext context) {
            return meta;
        }
    }


    @Test
    public void handle_getRequest() throws Exception{
        Path path = Paths.get("./files/index.html");
        FileMeta meta = new FileMeta(path, 12L, "text/plain");
        // Using this fake handler to return already all the required values instead of using the fileService
        StaticFileHandler handler = new StaticFileHandler(new FakeFileService(meta));

        Map<String, String> headers = new HashMap<>();
        HttpRequest request = new HttpRequest(HttpMethod.GET, path.toString(), "HTTP/1.1", headers);
        RequestContext context = new RequestContext(Paths.get("./files"));

        HttpResponse response = handler.handle(request, context);

        assertNotNull(response);
        assertEquals(Status.OK_200, response.status());
        assertEquals("HTTP/1.1", response.version());
        assertNotNull(response.body());
    }

    @Test
    public void handle_headRequest() throws Exception{
        Path path = Paths.get("./files/index.html");
        FileMeta meta = new FileMeta(path, 12L, "text/plain");
        // Using this fake handler to return already all the required values instead of using the fileService
        StaticFileHandler handler = new StaticFileHandler(new FakeFileService(meta));

        Map<String, String> headers = new HashMap<>();
        HttpRequest request = new HttpRequest(HttpMethod.HEAD, path.toString(), "HTTP/1.1", headers);
        RequestContext context = new RequestContext(Paths.get("./files"));

        HttpResponse response = handler.handle(request, context);

        assertNotNull(response);
        assertEquals(Status.OK_200, response.status());
        assertEquals("HTTP/1.1", response.version());
        assertNull(response.body());
    }

    @Test
    public void handle_returnsMethodNotAllowedForUnsupportedMethod() {
        Path path = Paths.get("./files/index.html");
        FileMeta meta = new FileMeta(path, 12L, "text/plain");
        // Using this fake handler to return already all the required values instead of using the fileService
        StaticFileHandler handler = new StaticFileHandler(new FakeFileService(meta));

        Map<String, String> headers = new HashMap<>();
        // Setting POST as method
        HttpRequest request = new HttpRequest(HttpMethod.POST, path.toString(), "HTTP/1.1", headers);
        RequestContext context = new RequestContext(Paths.get("./files"));

        HttpResponse response = handler.handle(request, context);

        assertEquals(Status.METHOD_NOT_ALLOWED_405, response.status());
    }

    @Test
    public void handle_returnsVersionNotSupportedForBadHTTPVersion() {
        Path path = Paths.get("./files/index.html");
        FileMeta meta = new FileMeta(path, 12L, "text/plain");
        // Using this fake handler to return already all the required values instead of using the fileService
        StaticFileHandler handler = new StaticFileHandler(new FakeFileService(meta));

        Map<String, String> headers = new HashMap<>();
        // Setting POST as method
        HttpRequest request = new HttpRequest(HttpMethod.POST, path.toString(), "HTTP/2", headers);
        RequestContext context = new RequestContext(Paths.get("./files"));

        HttpResponse response = handler.handle(request, context);

        assertEquals(Status.VERSION_NOT_SUPPORTED_505, response.status());
    }
}
