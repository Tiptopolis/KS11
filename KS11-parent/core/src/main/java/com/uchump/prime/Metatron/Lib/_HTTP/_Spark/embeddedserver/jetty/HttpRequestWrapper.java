package com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.utils.IOUtils;

/**
 * Http request wrapper. Wraps the request so 'getInputStream()' can be called multiple times.
 * Also has methods for checking if request has been consumed.
 */
public class HttpRequestWrapper extends HttpServletRequestWrapper {
    private byte[] cachedBytes;
    private boolean notConsumed = false;

    public HttpRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public boolean notConsumed() {
        return notConsumed;
    }

    public void notConsumed(boolean notConsumed) {
        this.notConsumed = notConsumed;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        HttpServletRequest request = (HttpServletRequest) super.getRequest();

        // disable stream cache for chunked transfer encoding
        String transferEncoding = request.getHeader("Transfer-Encoding");
        if ("chunked".equals(transferEncoding)) {
            return super.getInputStream();
        }

        // disable stream cache for multipart/form-data file upload
        // -> upload might be very large and might lead to out-of-memory error if we try to cache the bytes
        String contentType = request.getHeader("Content-Type");
        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            return super.getInputStream();
        }

        if (cachedBytes == null) {
            cacheInputStream();
        }
        return new CachedServletInputStream();
    }

    private void cacheInputStream() throws IOException {
        cachedBytes = IOUtils.toByteArray(super.getInputStream());
    }

    private class CachedServletInputStream extends ServletInputStream {
        private ByteArrayInputStream byteArrayInputStream;

        public CachedServletInputStream() {
            byteArrayInputStream = new ByteArrayInputStream(cachedBytes);
        }

        @Override
        public int read() {
            return byteArrayInputStream.read();
        }

        @Override
        public int available() {
            return byteArrayInputStream.available();
        }

        @Override
        public boolean isFinished() {
            return available() <= 0;
        }

        @Override
        public boolean isReady() {
            return available() >= 0;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }
    }
}