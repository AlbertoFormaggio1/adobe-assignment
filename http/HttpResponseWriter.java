package http;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import http.HttpResponse;

public final class HttpResponseWriter {
    public static void write(HttpResponse response, OutputStream outs) throws Exception{
        String statusLine = String.format("%s %d %s\r\n", response.version(), response.status().code(), response.status().description());
        outs.write(statusLine.getBytes(StandardCharsets.UTF_8));

        // Add the headers one by one
        for(var entry: response.headers().entrySet()){
            String headerLine = String.format("%s: %s\r\n", entry.getKey(), entry.getValue());
            outs.write(headerLine.getBytes(StandardCharsets.UTF_8));
        }

        outs.write("\r\n".getBytes());

        InputStream ins = response.body();
        // Temporary byte buffer for reading from InputStream
        byte[] buffer = new byte[1024];
        int bytes = 0;

        while((bytes = ins.read(buffer)) != -1){
            outs.write(buffer, 0, bytes);
        }
    }
}