package com.uchump.prime.Metatron.Lib._HTTP._Spark;
import javax.servlet.http.HttpServletResponse;

/**
 * Exception used for stopping the execution
 *
 * @author Per Wendel
 */
public class HaltException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int statusCode = HttpServletResponse.SC_OK;
    private String body = null;

    HaltException() {
        super(null, null, false, false);
    }

    HaltException(int statusCode) {
        this();
        this.statusCode = statusCode;
    }

    HaltException(String body) {
        this();
        this.body = body;
    }

    HaltException(int statusCode, String body) {
        this();
        this.statusCode = statusCode;
        this.body = body;
    }

    /**
     * @return the statusCode
     * @deprecated replaced by {@link #statusCode()}
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @return the statusCode
     */
    public int statusCode() {
        return statusCode;
    }

    /**
     * @return the body
     * @deprecated replaced by {@link #body()}
     */
    public String getBody() {
        return body;
    }

    /**
     * @return the body
     */
    public String body() {
        return body;
    }

}