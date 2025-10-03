package routing;

import java.nio.file.Path;
import util.Utils;

import http.exceptions.ForbiddenException;

public final class PathResolver {
    private PathResolver() {}

    public static Path resolveSafe(Path docRoot, String requestPath) throws ForbiddenException{
        // Check if the request path is valid
        if(requestPath == null || requestPath.isEmpty()) requestPath = "/";

        int q = requestPath.indexOf("?");
        if(q != -1) requestPath = requestPath.substring(0, q);

        // Convert the simple "/" to "/index.html"
        if("/".equals(requestPath)) requestPath = "index.html";

        if(requestPath.startsWith("/")){
            requestPath = requestPath.substring(1);
        }

        Path resolvedPath = docRoot.resolve(requestPath);

        // Check for possible injection that would allow the user to retrieve data it shouldn't have access to
        if(!resolvedPath.startsWith(docRoot))
            throw new ForbiddenException(String.format("The path provided %s is not allowed", requestPath));

        return resolvedPath;
    }
}
