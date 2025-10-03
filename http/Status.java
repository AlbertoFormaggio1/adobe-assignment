package http;

public enum Status {
    OK_200(200, "OK"),
    NOT_FOUND_404(404, "NOT_FOUND"),
    BAD_REQUEST_400(400, "BAD_REQUEST"),
    METHOD_NOT_ALLOWED_405(405,"METHOD_NOT_ALLOWED"),
    FORBIDDEN_403(403,"FORBIDDEN"),
    INTERNAL_SERVER_ERROR_500(500,"INTERNAL_SERVER_ERROR"),
    VERSION_NOT_SUPPORTED_505(505,"VERSION_NOT_SUPPORTED");

    private final int code;
    private final String description;

    Status(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() { return code; }
    public String description() { return description; }
}
