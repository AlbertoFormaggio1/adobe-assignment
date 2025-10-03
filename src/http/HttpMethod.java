package http;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of supported HTTP methods.
 * Includes standard verbs like GET, HEAD, POST, PUT, DELETE, and PATCH.
 *
 * Provides a lookup utility to convert raw strings to enum values.
 */
public enum HttpMethod {
    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    PATCH;

    /** Map used for efficient string-to-enum lookup */
    private static final Map<String, HttpMethod> LOOKUP;

    // Static initializer builds the lookup map at class load time
    static {
        Map<String, HttpMethod> map = new HashMap<>();
        for (HttpMethod method : HttpMethod.values()) {
            map.put(method.name(), method);
        }
        LOOKUP = map;
    }

    /**
     * Convert a string value into an HttpMethod enum.
     * Matching is case-insensitive.
     *
     * @param value the raw method string (e.g. "GET", "post")
     * @return the corresponding HttpMethod enum, or null if not recognized
     */
    public static HttpMethod fromString(String value) {
        if (value == null) return null;
        return LOOKUP.get(value.toUpperCase());
    }
}