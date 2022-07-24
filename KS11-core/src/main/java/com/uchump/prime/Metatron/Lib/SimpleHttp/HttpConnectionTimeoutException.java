package com.uchump.prime.Metatron.Lib.SimpleHttp;
public class HttpConnectionTimeoutException extends HttpException {

    public HttpConnectionTimeoutException() {
        super();
    }

    public HttpConnectionTimeoutException(String message) {
        super(message);
    }

    public HttpConnectionTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpConnectionTimeoutException(Throwable cause) {
        super(cause);
    }
}