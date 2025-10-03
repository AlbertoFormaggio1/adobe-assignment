package http.exceptions;

import http.Status;

public class VersionNotSupportedException extends HttpException{
    public VersionNotSupportedException(String message){
        super(Status.VERSION_NOT_SUPPORTED_505, message);
    }
}
