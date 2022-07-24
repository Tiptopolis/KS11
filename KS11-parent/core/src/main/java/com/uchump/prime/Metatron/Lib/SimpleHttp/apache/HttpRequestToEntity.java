package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.*;
import com.uchump.prime.Metatron.Lib.SimpleHttp.*;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;


import static org.apache.http.entity.ContentType.DEFAULT_BINARY;
import static org.apache.http.entity.mime.HttpMultipartMode.BROWSER_COMPATIBLE;



class HttpRequestToEntity implements HttpRequestVisitor {

    private final HttpRequest message;
    private HttpEntity entity;

    public HttpRequestToEntity(HttpRequest message) {
        this.message = message;
    }

    public HttpEntity asHttpEntity() {
        message.accept(this);
        return entity;
    }

    @Override
    public void visit(HttpDelete message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(HttpGet message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void visit(FormUrlEncodedMessage message) {
        try {
            FormParameters content = message.getContent();
            entity = new UrlEncodedFormEntity(content.transform(asApacheNameValuePair()), message.characterSet());
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        }
    }

    @Override
    public void visit(UnencodedStringMessage message) {
        try {
            StringMessageContent content = message.getContent();
            entity = new StringEntity(content.asString(), message.characterSet());
        } catch (UnsupportedCharsetException e) {
            throw new HttpException(e);
        }        
    }

    @Override
    public void visit(Multipart multipart) {
		entity = MultipartEntityBuilder
			.create()
			.setMode(BROWSER_COMPATIBLE)
			.addBinaryBody(multipart.getName(), multipart.getFile(), DEFAULT_BINARY, multipart.getFile().getName())
			.build();
    }

    private Transform<Map.Entry<String, String>, NameValuePair> asApacheNameValuePair() {
        return tuple -> new BasicNameValuePair(tuple.getKey(), tuple.getValue());
    }
}