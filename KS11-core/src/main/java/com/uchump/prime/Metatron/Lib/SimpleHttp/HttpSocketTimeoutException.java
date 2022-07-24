package com.uchump.prime.Metatron.Lib.SimpleHttp;
public class HttpSocketTimeoutException extends HttpException {

    public HttpSocketTimeoutException() {
        super();
    }

    public HttpSocketTimeoutException(String message) {
        super(message);
    }

    public HttpSocketTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpSocketTimeoutException(Throwable cause) {
        super(cause);
    }
}