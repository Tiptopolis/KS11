package com.uchump.prime.Metatron.Lib.SimpleHttp.A_I;
import java.net.URL;

import com.uchump.prime.Metatron.Lib.SimpleHttp.HttpException;

public interface HttpClient {

    HttpResponse get(URL url) throws HttpException;

    HttpResponse get(URL url, Headers headers) throws HttpException;

    HttpResponse post(URL url, HttpPost message) throws HttpException;

    HttpResponse put(URL url, HttpPut message) throws HttpException;

    HttpResponse delete(URL url) throws HttpException;

    HttpResponse options(URL url) throws HttpException;

    void shutdown();
}