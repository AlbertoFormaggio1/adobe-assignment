package http.exceptions;

import http.Status;

public class MethodNotAllowedException extends HttpException{
    public MethodNotAllowedException(String message){
        super(Status.METHOD_NOT_ALLOWED_405, message);
    }
}
