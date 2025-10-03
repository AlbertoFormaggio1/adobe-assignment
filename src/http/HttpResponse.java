package http;

import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class HttpResponse {
    private final String version;
    private final Status status;
    private final Map<String, String> headers;
    private final InputStream body; // FileInputStream used to handle also large files without loading in memory
    private final long contentLength; //-1 if unknown

    public HttpResponse(String version, Status status, Map<String, String> headers, InputStream body, long contentLength){
        this.status = status;
        this.headers = headers;
        this.body = body;
        this.contentLength = contentLength;
        this.version = version;
    }

    public static Builder builder(Status status) {
        return new Builder(status);
    }

    // Accessor methods
    public Status status() { return status; }
    public Map<String, String> headers() {return headers;}
    public InputStream body() { return body; }
    public long contentLength() { return contentLength; }
    public String version() { return version; }

    public static class Builder {
        private Status status;
        private Map<String, String> headers;
        private InputStream body; // InputStream used to handle also large files without loading in memory
        private long contentLength; //-1 if unknown
        private String version = "HTTP/1.1";

        public Builder(Status status){
            this.status = status;
            this.headers = new HashMap<>();
        }

        public Builder setHeader(String name, String value){
            /**
             * This function sets the value of an header of the response as name: value
             * @returns: This function returns a builder for additional operations
             */
            headers.put(name, value);
            return this;
        }

        public Builder setBody(InputStream body, long length){
            /**
             * This function sets the body of the response.
             * @returns: This function returns a builder for additional operations
             */
            this.body = body;
            this.contentLength = length;
            return this;
        }

        public Builder setBody(String text, Charset cs) {
            byte[] bytes = text.getBytes(cs);
            return setBody(bytes)
                .setHeader("Content-Type", "text/plain; charset=" + cs.name().toLowerCase());
        }

        public Builder setBody(byte[] bytes) {
            this.body = new ByteArrayInputStream(bytes);
            this.contentLength = bytes.length;
            return this;
        }
        
        public Builder setBody(String text) { // default UTF-8
            return setBody(text, StandardCharsets.UTF_8);
        }

        public HttpResponse build(){
            /**
             * @returns an HttpResponse object with the values previously set in this builder.
             */
            if (contentLength >= 0 && !headers.containsKey("Content-Length")) {
                headers.put("Content-Length", Long.toString(contentLength));
            }
            return new HttpResponse(version, status, headers, body, contentLength);
        }
    }
}
