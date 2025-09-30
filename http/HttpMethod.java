package http;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH;

    private static final Map<String, HttpMethod> LOOKUP;

    static {
        Map<String, HttpMethod> map = new HashMap<>();
        for(HttpMehtod method: HttpMethod.values()){
            map.put(method.name(), method);
        }
        LOOKUP = map;
    }

    /**
     * Convert string to HttpMethod.
     * Returns null if unknown.
     */
    public static HttpMethod fromString(String value) {
        if (value == null) return null;
        return LOOKUP.get(value.toUpperCase());
    }
}
