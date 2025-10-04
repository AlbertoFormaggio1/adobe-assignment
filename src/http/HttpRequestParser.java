package http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import http.exceptions.BadRequestException;

/**
 * HttpRequestParser is responsible for parsing raw HTTP requests
 * (read from an InputStream) into structured {@link HttpRequest} objects.
 *
 * It validates the request line and headers, and throws
 * {@link BadRequestException} if the request is malformed.
 */
public class HttpRequestParser {

    /**
     * Internal helper class that represents the first line of an HTTP request:
     * <pre>
     *   METHOD PATH VERSION
     *   e.g. GET /index.html HTTP/1.1
     * </pre>
     */
    private static class RequestLine {
        private HttpMethod method;
        private String version;
        private String path;

        public RequestLine(HttpMethod method, String path, String version){
            this.method = method;
            this.path = path;
            this.version = version;
        }

        public HttpMethod getMethod(){
            return method;
        }

        public String getVersion(){
            return version;
        }

        public String getPath(){
            return path;
        }
    }

    /**
     * Parse an HTTP request from an {@link InputStreamReader}.
     *
     * @param inputReader the reader connected to the client socket input stream
     * @return an {@link HttpRequest} representing the parsed request
     * @throws Exception if the request line or headers are invalid
     */
    public static HttpRequest parse(InputStreamReader inputReader) throws Exception {
        BufferedReader br = new BufferedReader(inputReader);

        // Read and parse the request line
        RequestLine requestLine = readRequestLine(br.readLine());

        // Read headers into a map
        Map<String, String> headers = readHeaders(br);
        
        // Construct and return the HttpRequest object
        return new HttpRequest(requestLine.getMethod(),
                               requestLine.getPath(),
                               requestLine.getVersion(),
                               headers);
    }

    /**
     * Reads and parses all HTTP headers from the buffered reader.
     * Stops when an empty line is encountered (end of headers).
     *
     * @param br the buffered reader on the socket input
     * @return a map of header name → header value
     * @throws Exception if headers are malformed or repeated
     */
    private static Map<String, String> readHeaders(BufferedReader br) throws Exception {
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null) {
            // Empty line marks end of headers
            if (line.isEmpty())
                break;

            // Split "Key: Value" at the first colon
            int idx = line.indexOf(":");
            if(idx == -1)
                throw new BadRequestException("Malformed header");
            String key = line.substring(0, idx).trim();
            String value = line.substring(idx + 1).trim();
            
            // Prevent duplicate headers
            if (headers.get(key) != null)
                throw new BadRequestException("Header repeated");

            headers.put(key, value);
        }

        return headers;
    }

    /**
     * Parse and validate the HTTP request line.
     *
     * The request line must have exactly three parts:
     * <pre>
     *   METHOD PATH VERSION
     * </pre>
     *
     * @param requestLine the raw request line string
     * @return a RequestLine object holding method, path, and version
     * @throws BadRequestException if the request line is missing, malformed, or uses an invalid method
     */
    private static RequestLine readRequestLine(String requestLine) throws Exception {
        // Ensure request line is not empty
        if (requestLine == null || requestLine.isEmpty())
            throw new BadRequestException("Empty request line");

        // Must contain exactly 3 tokens
        String[] parts = requestLine.split("\\s+");
        if (parts.length != 3)
            throw new BadRequestException("Malformed request line");

        // Validate the method
        HttpMethod method = HttpMethod.fromString(parts[0]);
        if (method == null) {
            throw new BadRequestException("Method not valid");
        }

        String path = parts[1];
        String version = parts[2];

        return new RequestLine(method, path, version);
    }
}