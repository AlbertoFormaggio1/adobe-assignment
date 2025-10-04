package routing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import http.HttpRequest;
import http.exceptions.NotFoundException;
import http.exceptions.ForbiddenException;

/**
 * FileService: responsible for mapping an HttpRequest to
 * the file system and producing metadata (FileMeta).
 *
 * Responsibilities:
 *  - resolve the requested path against the document root
 *  - validate that the file exists and is not a directory
 *  - determine content length and content type
 */
public class FileService {

    /**
     * Compute headers and file metadata for the requested file.
     *
     * @param request the parsed HTTP request (contains path)
     * @param context request context (contains docRoot, etc.)
     * @return FileMeta object with file path, length, and type
     * @throws NotFoundException if the file does not exist
     * @throws ForbiddenException if the path is a directory
     * @throws Exception for other I/O problems
     */
    public FileMeta computeHeaders(HttpRequest request, RequestContext context) throws Exception {
        // Extract the raw path from the request
        String path = request.getPath();

        // Safely resolve the request path against the document root
        Path file = PathResolver.resolveSafe(context.docRoot(), path);

        // Ensure the file exists
        if (file == null || !Files.exists(file)) {
            throw new NotFoundException("File not found");
        }
        // Forbid directory listing
        if (Files.isDirectory(file)) {
            throw new ForbiddenException("Directories are not listable");
        }

        // File is valid: determine its metadata
        long contentLength = Files.size(file);
        String contentType = resolveContentType(file);

        return new FileMeta(file, contentLength, contentType);
    }

    /**
     * Determine MIME type for a file.
     * First tries system probe; if that fails, fall back to extension mapping.
     */
    private String resolveContentType(Path file) throws IOException {
        // Try to probe using system-specific implementation
        String contentType = Files.probeContentType(file);
        if (contentType != null)
            return contentType;

        // Fallback by file extension
        String name = file.getFileName().toString().toLowerCase();
        if (name.endsWith(".html") || name.endsWith(".htm")) return "text/html";
        if (name.endsWith(".css")) return "text/css; charset=utf-8";
        if (name.endsWith(".js")) return "application/javascript";
        if (name.endsWith(".png")) return "image/png";
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "image/jpeg";
        if (name.endsWith(".gif")) return "image/gif";
        if (name.endsWith(".svg")) return "image/svg+xml";
        if (name.endsWith(".txt")) return "text/plain; charset=utf-8";
        return "application/octet-stream"; // default if unknown
    }
}