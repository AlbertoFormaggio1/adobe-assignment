package routing;

import java.nio.file.Path;

/**
 * RequestContext carries contextual information that
 * handlers may need when processing a request.
 */
public class RequestContext {
    // Absolute path to the document root directory
    private final Path rootPath;

    /**
     * Create a new RequestContext.
     *
     * @param rootPath the directory under which all request paths are resolved
     */
    public RequestContext(Path rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * @return the document root path
     */
    public Path docRoot() {
        return rootPath;
    }
}