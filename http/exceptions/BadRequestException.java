package http.exceptions;

import http.exceptions.HttpException;
import http.Status;

public class BadRequestException extends HttpException{
    public BadRequestException(String message){
        super(Status.BAD_REQUEST_400, message);
    }
}
