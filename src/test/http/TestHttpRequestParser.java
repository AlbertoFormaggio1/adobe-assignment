package test.http;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import http.exceptions.BadRequestException;
import http.HttpRequest;
import http.HttpRequestParser;
import http.HttpMethod;

public class TestHttpRequestParser{

    /* Creates an InputStreamReader with the content provided in the specified string */
    private InputStreamReader readerFrom(String raw) {
        return new InputStreamReader(
                new ByteArrayInputStream(raw.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8
        );
    }

    @Test
    public void parse_validRequest() throws Exception{
        String requestString = 
            "GET index.html HTTP/1.1\r\n"+
            "Host: example.com\r\n";

        HttpRequest req = HttpRequestParser.parse(readerFrom(requestString));

        assertEquals(req.getMethod(), HttpMethod.GET);
        assertEquals(req.getPath(), "index.html");
        assertEquals(req.getVersion(), "HTTP/1.1");
        assertEquals(req.getHeaderValue("Host"), "example.com");
    }

    @Test
    public void parse_trimWhitespace() throws Exception{
        String requestString = 
            "GET    index.html HTTP/1.1\r\n"+
            "Host:        example.com\r\n";

        HttpRequest req = HttpRequestParser.parse(readerFrom(requestString));

        assertEquals(req.getMethod(), HttpMethod.GET);
        assertEquals(req.getPath(), "index.html");
        assertEquals(req.getVersion(), "HTTP/1.1");
        assertEquals(req.getHeaderValue("Host"), "example.com");
    }

    @Test
    public void parse_emptyRequestLine() {
        String requestString = "\r\n"; // first line empty
        assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(readerFrom(requestString)));
    }

    @Test
    public void parse_wrongNumberArgsRequestLine(){
        String requestString = 
            "GET HTTP/1.1\r\n"+
            "Host: example.com\r\n";

        assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(readerFrom(requestString)));
    }

    @Test 
    public void parse_wrongMethod(){
        String requestString = 
            "GOT HTTP/1.1\r\n"+
            "Host: example.com\r\n";

        assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(readerFrom(requestString)));
    }

    @Test
    public void parse_malformedHeaderNoColon() {
        String raw =
                "GET /p HTTP/1.1\r\n" +
                "NoColonHere\r\n" +
                "\r\n";
        assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(readerFrom(raw)));
    }
}