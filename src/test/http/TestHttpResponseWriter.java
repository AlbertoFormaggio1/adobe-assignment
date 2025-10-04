package test.http;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import http.HttpResponse;
import http.HttpResponseWriter;
import http.Status;

import java.io.InputStream;

public class TestHttpResponseWriter{

    private static HttpResponse makeResponse(
                String version,
                Status status,
                Map<String,String> headers,
                String body
        ) {
        InputStream ins = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
        return new HttpResponse(version, status, headers, ins, body.getBytes(StandardCharsets.UTF_8).length);
    }

    @Test
    public void writesStatusLineHeadersAndBody() throws Exception {
        // Use LinkedHashMap to keep insertion order deterministic
        Map<String,String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "text/plain");

        String body = "Hello";
        HttpResponse resp = makeResponse("HTTP/1.1", Status.OK_200, headers, body);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpResponseWriter.write(resp, out);

        String expected =
                "HTTP/1.1 200 " + Status.OK_200.description() + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                body;

        String written = out.toString();
        assertEquals(expected, written);
    }
}