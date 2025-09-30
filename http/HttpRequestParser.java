package http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpVersionNotSupportedException;
import java.util.HashMap;

public class HttpRequestParser {
    private class RequestLine{
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

    public static HttpRequest parse(InputStreamReader inputReader) throws IOException{
        BufferedReader br = new BufferedReader(inputReader);

        // Read Request Line
        RequestLine requestLine = readRequestLine(br.readLine());

        // Read headers
        Map<String, String> headers = readHeaders(br);
        
        return new HttpRequest(requestLine.method, requestLine.path, requestLine.version, headers);
    }

    private static Map<String, String> readHeaders(BufferedReader br) throws IOException{
        Map<String, String> headers = new HashMap<>();
        while((line = br.readLine()) != null){
            // Empty line separates headers from content
            if(line.isEmpty())
                break;

            // Get separator between key and value
            int idx = line.indexOf(":");
            String key = line.substring(0, idx).trim();
            // From idx + 1 because you skip colon
            String value = line.substring(idx + 1).trim();
            
            if(headers.get(key) != null)
                throw new BadRequestException("Header repeated");
            headers.put(key, value);
        }

        return headers;
    }

    private static RequestLine readRequestLine(String requestLine){
        // Check if requestLine is valid
        if(requestLine == null || requestLine.isEmpty())
            throw new BadRequestException("Empty request line");

        // The request line must have METHOD PATH VERSION
        String[] parts = requestLine.split("\\s+");
        if(parts.length != 3)
            throw new BadRequestException("Malformed request line");

        // Perform validation of the method, if allowed
        HttpMethod method = HttpMethod.fromString(parts[0]);
        if(method == null){
            throw new BadRequestException("Method not valid");
        }

        String path = parts[1];
        String version = parts[2];

        // Check HTTP version
        if(version != "HTTP/1.1"){
            throw new HttpVersionNotSupportedException("Only HTTP/1.1 is supported");
        }

        return new RequestLine(method, parts[1], parts[2]);
    }
}
