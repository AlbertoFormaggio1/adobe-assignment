package routing;

import java.nio.file.Path;
import http.exceptions.ForbiddenException;

/**
 * Utility class for safely resolving request paths
 * against the server's document root.
 *
 * Prevents path traversal attacks (e.g. "../..") that could escape docRoot.
 */
public final class PathResolver {
    // Private constructor: utility class, not meant to be instantiated
    private PathResolver() {}

    /**
     * Resolve a request path against the given document root.
     *
     * @param docRoot      the base directory where static files are served from
     * @param requestPath  the raw path from the HTTP request
     * @return the resolved absolute Path within docRoot
     * @throws ForbiddenException if the resolved path escapes the docRoot
     */
    public static Path resolveSafe(Path docRoot, String requestPath) throws ForbiddenException {
        // Default empty path to "/"
        if (requestPath == null || requestPath.isEmpty()) {
            requestPath = "/";
        }

        // Strip query string if present (everything after '?')
        int q = requestPath.indexOf("?");
        if (q != -1) {
            requestPath = requestPath.substring(0, q);
        }

        // Convert root "/" to index.html (default page)
        if ("/".equals(requestPath)) {
            requestPath = "index.html";
        }

        // Remove leading "/" so it resolves as a relative path under docRoot
        if (requestPath.startsWith("/")) {
            requestPath = requestPath.substring(1);
        }

        // Join docRoot + requestPath (normalized relative path)
        Path resolvedPath = docRoot.resolve(requestPath).normalize();

        // Ensure the resolved path is still inside the docRoot
        // (prevents "../../etc/passwd" style traversal)
        if (!resolvedPath.startsWith(docRoot)) {
            throw new ForbiddenException(
                String.format("The path provided %s is not allowed", requestPath)
            );
        }

        return resolvedPath;
    }
}