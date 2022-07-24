package com.uchump.prime.Metatron.Lib._HTTP._Spark.http.matching;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.CustomErrorPages;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.ExceptionHandlerImpl;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.ExceptionMapper;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.RequestResponseFactory;

/**
 * Modifies the HTTP response and body based on the provided exception and request/response wrappers.
 */
final class GeneralError {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(GeneralError.class);

    /**
     * Modifies the HTTP response and body based on the provided exception.
     */
    static void modify(HttpServletRequest httpRequest,
                       HttpServletResponse httpResponse,
                       Body body,
                       RequestWrapper requestWrapper,
                       ResponseWrapper responseWrapper,
                       ExceptionMapper exceptionMapper,
                       Exception e) {

        ExceptionHandlerImpl handler = exceptionMapper.getHandler(e);

        if (handler != null) {
            handler.handle(e, requestWrapper, responseWrapper);
            String bodyAfterFilter = responseWrapper.getDelegate().body();

            if (bodyAfterFilter != null) {
                body.set(bodyAfterFilter);
            }
        } else {
            LOG.error("", e);

            httpResponse.setStatus(500);

            if (CustomErrorPages.existsFor(500)) {
                requestWrapper.setDelegate(RequestResponseFactory.create(httpRequest));
                responseWrapper.setDelegate(RequestResponseFactory.create(httpResponse));
                body.set(CustomErrorPages.getFor(500, requestWrapper, responseWrapper));
            } else {
                body.set(CustomErrorPages.INTERNAL_ERROR);
            }
        }
    }

}