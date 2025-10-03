package routing;

import java.nio.file.Path;

public class RequestContext {
    private final Path rootPath;

    public RequestContext(Path rootPath){
        this.rootPath = rootPath;
    }

    public Path docRoot(){
        return rootPath;
    }
}
