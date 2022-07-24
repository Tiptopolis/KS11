package com.uchump.prime.Metatron.Lib.SimpleHttp.listener;
import java.net.URL;

import org.apache.log4j.Logger;

import com.uchump.prime.Metatron.Lib.SimpleHttp.*;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.*;


public class LoggingHttpClient implements HttpClient {

    private static final String lineSeparator = System.getProperty("line.separator");

    private final HttpClient delegate;
    private final Logger log;

    public LoggingHttpClient(HttpClient delegate, Logger logger) {
        this.log = logger;
        this.delegate = delegate;
    }

    @Override
    public HttpResponse get(final URL url) throws HttpException {
        HttpResponse response = null;
        try {
            response = delegate.get(url);
            return response;
        } finally {
            if (log.isInfoEnabled())
                log.info(message(url, new HttpGetMessage(), response));
        }
    }

    @Override
    public HttpResponse get(URL url, Headers headers) throws HttpException {
        HttpResponse response = null;
        try {
            response = delegate.get(url, headers);
            return response;
        } finally {
            if (log.isInfoEnabled())
                log.info(message(url, new HttpGetMessage(headers), response));
        }
    }

    @Override
    public HttpResponse post(URL url, HttpPost message) throws HttpException {
        HttpResponse response = null;
        try {
            response = delegate.post(url, message);
            return response;
        } finally {
            if (log.isInfoEnabled())
                log.info(message(url, message, response));
        }
    }

    @Override
    public HttpResponse put(URL url, HttpPut message) throws HttpException {
        HttpResponse response = null;
        try {
            response = delegate.put(url, message);
            return response;
        } finally {
            if (log.isInfoEnabled())
                log.info(message(url, message, response));
        }
    }

    @Override
    public HttpResponse delete(URL url) throws HttpException {
        HttpResponse response = null;
        try {
            response = delegate.delete(url);
            return response;
        } finally {
            if (log.isInfoEnabled())
                log.info(message(url, new HttpDeleteMessage(), response));
        }
    }

    @Override
    public HttpResponse options(URL url) throws HttpException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    private String message(URL url, HttpRequest request, HttpResponse response) {
        StringBuilder builder = new StringBuilder().append(request(url, request)).append(lineSeparator);
        if (response != null)
            builder.append(response(response));
        return builder.toString();
    }

    private String request(URL url, final HttpRequest message) {
        RawHttpRequest rawRequest = new RawHttpRequest(url);
        message.accept(rawRequest);
        return rawRequest.asString();
    }
    
    private String response(HttpResponse response) {
        return new StringBuilder()
            .append("HTTP/1.1 ")
            .append(response.getStatusCode())
            .append(" ")
            .append(response.getStatusMessage())
            .append(lineSeparator)
            .append(headers(response.getHeaders()))
            .append(lineSeparator)
            .append(response.getContent().asString())
            .toString();
    }
    
    private static String headers(Headers headers) {
        StringBuilder builder = new StringBuilder();
        for (Header header : headers)
            builder.append(header.name()).append(": ").append(header.value()).append(lineSeparator);
        return builder.toString();
    }

    private static class RawHttpRequest implements HttpRequestVisitor {

        private final URL url;
        private final StringBuilder builder;

        public RawHttpRequest(URL url) {
            this.url = url;
            this.builder = new StringBuilder();
        }

        @Override
        public void visit(HttpGet message) {
            asHttpString(message, "GET");
        }

        @Override
        public void visit(HttpDelete message) {
            asHttpString(message, "DELETE");
        }

        @Override
        public void visit(FormUrlEncodedMessage message) {
            asHttpString(message, "POST");
        }

        @Override
        public void visit(UnencodedStringMessage message) {
            asHttpString(message, "POST");
        }

        @Override
        public void visit(Multipart multipart) {
            asHttpString(multipart, "POST");
        }

        private void asHttpString(HttpMessage message, String method) {
            builder
                .append(method)
                .append(" ")
                .append(url.toExternalForm())
                .append(" HTTP/1.1")
                .append(lineSeparator)
                .append(headers(message.getHeaders()))
                .append(lineSeparator)
                .append(message.getContent().asString());
        }

        public String asString() {
            return builder.toString();
        }
    }
}