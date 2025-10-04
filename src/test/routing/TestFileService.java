package test.routing;

import routing.FileService;
import routing.RequestContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Collections;
import routing.FileMeta;

import org.junit.Test;

import http.HttpMethod;
import http.HttpRequest;
import http.exceptions.ForbiddenException;
import http.exceptions.NotFoundException;

public class TestFileService {
     private static HttpRequest req(String path) {
        // Only path is used by FileService, but use a realistic request anyway
        return new HttpRequest(HttpMethod.GET, path, "HTTP/1.1", Collections.emptyMap());
    }

    @Test
    public void computeHeaders_existingFile_returnsMeta() throws Exception{
        FileService srv = new FileService();
        Path path = Paths.get("files").toAbsolutePath().normalize();
        RequestContext context = new RequestContext(path);

        FileMeta res = srv.computeHeaders(req("index.html"), context);

        assertEquals(res.getPath().toString(), Paths.get("files/index.html").toAbsolutePath().normalize().toString());
        assertEquals(res.getContentType(), "text/html");
    }

    @Test
    public void computeHeaders_directory_forbidden() throws Exception {
        FileService srv = new FileService();
        Path path = Paths.get("files").toAbsolutePath().normalize();
        RequestContext context = new RequestContext(path);

        assertThrows(ForbiddenException.class, () -> srv.computeHeaders(req("folder"), context));
    }

    @Test
    public void computeHeaders_missing_notFound() throws Exception {
        FileService srv = new FileService();
        Path path = Paths.get("files").toAbsolutePath().normalize();
        RequestContext context = new RequestContext(path);

        assertThrows(NotFoundException.class, () -> srv.computeHeaders(req("not_found.html"), context));
    }
}
