package routing;

import java.nio.file.Path;

/**
 * Metadata about a static file to be served.
 * Immutable data class that carries:
 *  - path: absolute path to the file on disk
 *  - contentLength: size in bytes (from Files.size)
 *  - contentType: MIME type (from probeContentType or fallback)
 */
public final class FileMeta {
    // absolute, normalized path of the file
    final Path path;

    // file size in bytes
    final long contentLength;

    // MIME type string, e.g. "text/html" or "application/octet-stream"
    final String contentType;

    public FileMeta(Path path, long contentLength, String contentType) {
        this.path = path;
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

    public Path getPath(){
        return path;
    }

    public long getContentLength(){
        return contentLength;
    }

    public String getContentType(){
        return contentType;
    }
}