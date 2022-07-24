package com.uchump.prime.Metatron.Lib._HTTP._Spark.route;
import java.util.HashMap;

/**
 * @author Per Wendel
 */
public enum HttpMethod {
    get, post, put, patch, delete, head, trace, connect, options, before, after, afterafter, unsupported;

    private static HashMap<String, HttpMethod> methods = new HashMap<>();

    static {
        for (HttpMethod method : values()) {
            methods.put(method.toString(), method);
        }
    }

    /**
     * Gets the HttpMethod corresponding to the provided string. If no corresponding method can be found
     * {@link spark.route.HttpMethod#unsupported} will be returned.
     *
     * @param methodStr The string containing HTTP method name
     * @return          The HttpMethod corresponding to the provided string
     */
    public static HttpMethod get(String methodStr) {
        HttpMethod method = methods.get(methodStr);
        return method != null ? method : unsupported;
    }
}