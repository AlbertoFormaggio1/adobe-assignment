package routing;

import java.nio.file.Path;

public final class FileMeta {
    final Path path;
    final long contentLength;
    final String contentType;

    FileMeta(Path path, long contentLength, String contentType) {
        this.path = path;
        this.contentLength = contentLength;
        this.contentType = contentType;
    }
}