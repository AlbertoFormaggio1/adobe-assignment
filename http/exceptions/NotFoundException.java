package http.exceptions;

import http.exceptions.HttpException;
import http.Status;

public class NotFoundException extends HttpException{
    public NotFoundException(String message){
        super(Status.NOT_FOUND_404, message);
    }
}
