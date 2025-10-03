package http;

import http.HttpMethod;
import java.util.Map;

public class HttpRequest {
    private final HttpMethod method;  // Http method
    private final String path; // Path of the required resource
    private final Map<String, String> headers; // Mapping Header -> HeaderContent
    //private final byte[] body;
    private final String version;

    public HttpRequest(HttpMethod method, String path, String version, Map<String, String> headers){
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.version = version;
    }

    public HttpMethod getMethod(){
        return method;
    }

    public String getPath(){
        return path;
    }

    public String getHeaderValue(String headerKey){
        return headers.get(headerKey);
    }
}
