package http.exceptions;

import http.Status;

public abstract class HttpException extends Exception {
    private final Status status;

    protected HttpException(Status status, String message) {
        super(message);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}


