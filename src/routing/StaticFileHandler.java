package routing;

import http.exceptions.HttpException;
import http.exceptions.VersionNotSupportedException;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import java.util.EnumMap;
import java.util.Map;

import http.Status;
import http.exceptions.MethodNotAllowedException;


/**
 * StaticFileHandler handles HTTP requests for static files.
 * It supports GET and HEAD methods, using a FileService to compute file metadata.
 */
public class StaticFileHandler implements Handler{

    /** Service responsible for computing file metadata (size, MIME type, etc.) */
    private final FileService fileService;

    /** Registry that maps supported HTTP methods to their corresponding handlers */
    private final Map<HttpMethod, MethodHandler> registry = new EnumMap<>(HttpMethod.class);

    /**
     * Default constructor: creates a new FileService internally.
     */
    public StaticFileHandler(){
        this(new FileService());
    }

    /**
     * Constructor that allows providing an external FileService
     * (useful for testing or custom configurations).
     *
     * @param fileService the FileService instance to use
     */
    public StaticFileHandler(FileService fileService){
        this.fileService = fileService;

        // Register handlers for GET and HEAD
        registry.put(HttpMethod.GET, new GetMethodHandler());
        registry.put(HttpMethod.HEAD, new HeadMethodHandler());
    }

    /**
     * Handles an HTTP request directed at a static file.
     * If the method is not supported, throws a MethodNotAllowedException.
     * If the file is not found or an error occurs, returns an HttpResponse with the proper status.
     *
     * @param request the HTTP request to process
     * @param context the request context (e.g. document root)
     * @return an HttpResponse representing the result of the request
     */
    @Override
    public HttpResponse handle(HttpRequest request, RequestContext context){
        try{
            if(!request.getVersion().equals("HTTP/1.1")) throw new VersionNotSupportedException(String.format("The only HTTP version accepted is HTTP/1.1. Got: %s.", request.getVersion()));

            MethodHandler methodHandler = registry.get(request.getMethod());
            if (methodHandler == null) throw new MethodNotAllowedException(String.format("The method %s is not allowed", request.getMethod()));

            FileMeta meta = fileService.computeHeaders(request, context);

            return methodHandler.handle(request, context, meta);
        } catch(HttpException e){
            return HttpResponse.builder(e.getStatus()).setBody(e.getMessage()).build();
        }
        catch(Exception e){
            return HttpResponse.builder(Status.INTERNAL_SERVER_ERROR_500).setBody(e.getMessage()).build();
        }        
    }    
}