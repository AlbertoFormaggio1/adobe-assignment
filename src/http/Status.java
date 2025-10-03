package http;

/**
 * Enumeration of standard HTTP status codes supported by this server.
 * Each status has a numeric code and a textual description.
 *
 * Examples:
 *  - 200 OK
 *  - 404 Not Found
 *  - 500 Internal Server Error
 */
public enum Status {
    /** 200 OK: the request succeeded */
    OK_200(200, "OK"),

    /** 404 Not Found: the requested resource could not be found */
    NOT_FOUND_404(404, "NOT_FOUND"),

    /** 400 Bad Request: the request was malformed or invalid */
    BAD_REQUEST_400(400, "BAD_REQUEST"),

    /** 405 Method Not Allowed: the request method is not supported */
    METHOD_NOT_ALLOWED_405(405,"METHOD_NOT_ALLOWED"),

    /** 403 Forbidden: the server understood the request but refuses to authorize it */
    FORBIDDEN_403(403,"FORBIDDEN"),

    /** 500 Internal Server Error: an unexpected error occurred on the server */
    INTERNAL_SERVER_ERROR_500(500,"INTERNAL_SERVER_ERROR"),

    /** 505 HTTP Version Not Supported: the server does not support the HTTP version used */
    VERSION_NOT_SUPPORTED_505(505,"VERSION_NOT_SUPPORTED");

    /** Numeric status code (e.g. 200, 404, 500) */
    private final int code;

    /** Textual reason phrase associated with the status */
    private final String description;

    Status(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * @return the numeric HTTP status code
     */
    public int code() { return code; }

    /**
     * @return the textual description / reason phrase
     */
    public String description() { return description; }
}
