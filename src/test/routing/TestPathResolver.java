package test.routing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import http.exceptions.ForbiddenException;

import java.nio.file.Path;

import routing.PathResolver;


public class TestPathResolver {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void resolvesRootToIndexHtml() throws Exception {
        Path docRoot = tmp.newFolder("www").toPath();

        Path actual = PathResolver.resolveSafe(docRoot, "/");

        Path expected = docRoot.resolve("index.html").normalize();
        assertEquals(expected, actual);
    }

    @Test
    public void stripsQueryStringAndLeadingSlash() throws Exception {
        Path docRoot = tmp.newFolder("www").toPath();

        Path actual = PathResolver.resolveSafe(docRoot, "/css/style.css?v=123&x=y");

        Path expected = docRoot.resolve("css/style.css").normalize();
        assertEquals(expected, actual);
    }

    @Test
    public void nullOrEmptyPathDefaultsToIndex() throws Exception {
        Path docRoot = tmp.newFolder("www").toPath();

        Path fromNull  = PathResolver.resolveSafe(docRoot, null);
        Path fromEmpty = PathResolver.resolveSafe(docRoot, "");

        Path expected = docRoot.resolve("index.html").normalize();
        assertEquals(expected, fromNull);
        assertEquals(expected, fromEmpty);
    }

    @Test
    public void blocksTraversalThatEscapesDocRoot() throws Exception {
        Path docRoot = tmp.newFolder("www").toPath();

        assertThrows(ForbiddenException.class, () -> PathResolver.resolveSafe(docRoot, "/../../secret.txt"));
    }

}