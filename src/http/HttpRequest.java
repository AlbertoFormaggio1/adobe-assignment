package http;

import java.util.Map;

/**
 * Represents a parsed HTTP request.
 * Contains method, request path, protocol version, and headers.
 * (Body is omitted in this simplified implementation.)
 */
public class HttpRequest {
    /** HTTP method (e.g. GET, POST) */
    private final HttpMethod method;

    /** Requested path (e.g. "/index.html") */
    private final String path;

    /** Mapping of header names to their values */
    private final Map<String, String> headers;

    /** HTTP version (e.g. "HTTP/1.1") */
    private final String version;

    /**
     * Construct a new HttpRequest object.
     *
     * @param method  the HTTP method
     * @param path    the resource path
     * @param version the HTTP version string
     * @param headers the headers as a Map
     */
    public HttpRequest(HttpMethod method, String path, String version, Map<String, String> headers){
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.version = version;
    }

    /**
     * @return the HTTP version of the request
     */
    public String getVersion(){
        return version;
    }

    /**
     * @return the HTTP method of the request
     */
    public HttpMethod getMethod(){
        return method;
    }

    /**
     * @return the resource path of the request
     */
    public String getPath(){
        return path;
    }

    /**
     * Returns the value of a given header key.
     *
     * @param headerKey the header name
     * @return the header value, or null if not present
     */
    public String getHeaderValue(String headerKey){
        return headers.get(headerKey);
    }
}