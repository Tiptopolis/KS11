package com.uchump.prime.Metatron.Lib._HTTP._Spark.http.matching;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.serialization.SerializerChain;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.utils.GzipUtils;

/**
 * Represents the 'body'
 */
final class Body {

    private Object content;

    public static Body create() {
        return new Body();
    }

    private Body() {

    }

    public boolean notSet() {
        return content == null;
    }

    public boolean isSet() {
        return content != null;
    }

    public Object get() {
        return content;
    }

    public void set(Object content) {
        this.content = content;
    }

    public void serializeTo(HttpServletResponse httpResponse,
                            SerializerChain serializerChain,
                            HttpServletRequest httpRequest) throws IOException {

        if (!httpResponse.isCommitted()) {
            if (httpResponse.getContentType() == null) {
                httpResponse.setContentType("text/html; charset=utf-8");
            }

            // Check if GZIP is wanted/accepted and in that case handle that
            OutputStream responseStream = GzipUtils.checkAndWrap(httpRequest, httpResponse, true);

            // Serialize the body to output stream
            serializerChain.process(responseStream, content);

            responseStream.flush(); // needed for GZIP stream. Not sure where the HTTP response actually gets cleaned up
            responseStream.close(); // needed for GZIP
        }
    }


}