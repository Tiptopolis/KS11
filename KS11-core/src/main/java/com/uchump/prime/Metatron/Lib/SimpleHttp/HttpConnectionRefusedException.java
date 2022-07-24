package com.uchump.prime.Metatron.Lib.SimpleHttp;
public class HttpConnectionRefusedException extends HttpException {

    public HttpConnectionRefusedException() {
        super();
    }

    public HttpConnectionRefusedException(String message) {
        super(message);
    }

    public HttpConnectionRefusedException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpConnectionRefusedException(Throwable cause) {
        super(cause);
    }
}