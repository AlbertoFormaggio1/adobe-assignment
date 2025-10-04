package http;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Utility class responsible for serializing an {@link HttpResponse}
 * into raw HTTP format and writing it to an output stream.
 *
 * Responsibilities:
 *  - Write the status line (HTTP version, status code, description)
 *  - Write all response headers
 *  - Write a CRLF line to separate headers from the body
 *  - Stream the response body from an {@link InputStream} to the client
 */
public final class HttpResponseWriter {

    /**
     * Write the given {@link HttpResponse} to an {@link OutputStream}.
     *
     * @param response the HttpResponse to serialize
     * @param outs     the output stream (usually the client socket output)
     * @throws Exception if an I/O error occurs
     */
    public static void write(HttpResponse response, OutputStream outs) throws Exception {
        // Build the status line: e.g. "HTTP/1.1 200 OK"
        String statusLine = String.format("%s %d %s\r\n",
                response.version(),
                response.status().code(),
                response.status().description());
        outs.write(statusLine.getBytes(StandardCharsets.UTF_8));

        // Write each header line
        for (Map.Entry<String, String> entry : response.headers().entrySet()) {
            String headerLine = String.format("%s: %s\r\n", entry.getKey(), entry.getValue());
            outs.write(headerLine.getBytes(StandardCharsets.UTF_8));
        }

        // Blank line to mark the end of headers
        outs.write("\r\n".getBytes());

        // Stream the response body
        InputStream ins = response.body();
        byte[] buffer = new byte[1024];
        int bytes = 0;

        while ((bytes = ins.read(buffer)) != -1) {
            outs.write(buffer, 0, bytes);
        }
    }
}