package com.uchump.prime.Metatron.Lib._HTTP._Spark.http.matching;

import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.HaltException;

/**
 * Modifies the HTTP response and body based on the provided HaltException.
 */
public class Halt {

    /**
     * Modifies the HTTP response and body based on the provided HaltException.
     *
     * @param httpResponse The HTTP servlet response
     * @param body         The body content
     * @param halt         The halt exception object
     */
    public static void modify(HttpServletResponse httpResponse, Body body, HaltException halt) {

        httpResponse.setStatus(halt.statusCode());

        if (halt.body() != null) {
            body.set(halt.body());
        } else {
            body.set("");
        }
    }
}