package com.uchump.prime.Metatron.Lib.SimpleHttp;
import java.net.MalformedURLException;

public class MalformedUrlRuntimeException extends HttpException {
    public MalformedUrlRuntimeException(MalformedURLException e) {
        super(e);
    }
}