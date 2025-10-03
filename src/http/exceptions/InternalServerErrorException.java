package http.exceptions;

import http.Status;

public class InternalServerErrorException extends HttpException{
    public InternalServerErrorException(String message){
        super(Status.INTERNAL_SERVER_ERROR_500, message);
    }
}
