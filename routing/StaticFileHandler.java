package routing;

import http.exceptions.HttpException;
import http.exceptions.InternalServerErrorException;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import routing.RequestContext;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import http.HttpResponse;
import http.HttpMethod;
import http.Status;
import http.exceptions.BadRequestException;
import http.exceptions.ForbiddenException;
import http.exceptions.NotFoundException;
import http.exceptions.HttpException;
import http.exceptions.MethodNotAllowedException;
import routing.PathResolver;
import routing.Handler;
import routing.FileService;

public class StaticFileHandler implements Handler{
    private final FileService fileService;
    private final Map<HttpMethod, MethodHandler> registry = new EnumMap<>(HttpMethod.class);

    public StaticFileHandler(){
        this(new FileService());
    }

    public StaticFileHandler(FileService fileService){
        this.fileService = fileService;

        registry.put(HttpMethod.GET, new GetMethodHandler());
        registry.put(HttpMethod.HEAD, new HeadMethodHandler());
    }

    @Override
    public HttpResponse handle(HttpRequest request, RequestContext context){
        try{
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
