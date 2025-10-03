package routing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import http.HttpRequest;
import routing.PathResolver;
import routing.RequestContext;
import http.exceptions.NotFoundException;
import http.exceptions.ForbiddenException;

public final class FileService {

    public FileMeta computeHeaders(HttpRequest request, RequestContext context) throws Exception{
        String path = request.getPath();
        Path file = PathResolver.resolveSafe(context.docRoot(), path);

        if(file == null || !Files.exists(file)){
            throw new NotFoundException("File not found");
        }
        if (Files.isDirectory(file)) {
            throw new ForbiddenException("Directories are not listable");
        }

        long contentLength = Files.size(file);
        String contentType = resolveContentType(file);

        return new FileMeta(file, contentLength, contentType);
    }

    private String resolveContentType(Path file) throws IOException{
        String contentType = Files.probeContentType(file);
        if(contentType != null) 
            return contentType;

        // Fallback in case contentType doesn't work
        String name = file.getFileName().toString().toLowerCase();
        if (name.endsWith(".html") || name.endsWith(".htm")) return "text/html; charset=utf-8";
        if (name.endsWith(".css")) return "text/css; charset=utf-8";
        if (name.endsWith(".js")) return "application/javascript";
        if (name.endsWith(".png")) return "image/png";
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "image/jpeg";
        if (name.endsWith(".gif")) return "image/gif";
        if (name.endsWith(".svg")) return "image/svg+xml";
        if (name.endsWith(".txt")) return "text/plain; charset=utf-8";
        return "application/octet-stream";
    }
}
