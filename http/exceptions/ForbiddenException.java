package http.exceptions;

import http.exceptions.HttpException;
import http.Status;

public class ForbiddenException extends HttpException{
    public ForbiddenException(String message){
        super(Status.FORBIDDEN_403, message);
    }
}
