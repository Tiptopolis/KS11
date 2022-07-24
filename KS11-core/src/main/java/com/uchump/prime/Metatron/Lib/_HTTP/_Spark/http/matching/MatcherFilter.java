package com.uchump.prime.Metatron.Lib._HTTP._Spark.http.matching;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Spark.CustomErrorPages;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.ExceptionMapper;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.HaltException;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.RequestResponseFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.embeddedserver.jetty.HttpRequestWrapper;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.route.HttpMethod;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.serialization.SerializerChain;
import com.uchump.prime.Metatron.Lib._HTTP._Spark.staticfiles.StaticFilesConfiguration;


/**
 * Matches Spark routes and filters.
 *
 * @author Per Wendel
 */
public class MatcherFilter implements Filter {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(MatcherFilter.class);

    private static final String ACCEPT_TYPE_REQUEST_MIME_HEADER = "Accept";
    private static final String HTTP_METHOD_OVERRIDE_HEADER = "X-HTTP-Method-Override";

    private final StaticFilesConfiguration staticFiles;

    private com.uchump.prime.Metatron.Lib._HTTP._Spark.route.Routes routeMatcher;
    private SerializerChain serializerChain;
    private ExceptionMapper exceptionMapper;

    private boolean externalContainer;
    private boolean hasOtherHandlers;

    /**
     * Constructor
     *
     * @param routeMatcher      The route matcher
     * @param staticFiles       The static files configuration object
     * @param externalContainer Tells the filter that Spark is run in an external web container.
     *                          If true, chain.doFilter will be invoked if request is not consumed by Spark.
     * @param hasOtherHandlers  If true, do nothing if request is not consumed by Spark in order to let others handlers process the request.
     */
    public MatcherFilter(com.uchump.prime.Metatron.Lib._HTTP._Spark.route.Routes routeMatcher,
                         StaticFilesConfiguration staticFiles,
                         ExceptionMapper exceptionMapper,
                         boolean externalContainer,
                         boolean hasOtherHandlers) {

        this.routeMatcher = routeMatcher;
        this.staticFiles = staticFiles;
        this.exceptionMapper = exceptionMapper;
        this.externalContainer = externalContainer;
        this.hasOtherHandlers = hasOtherHandlers;
        this.serializerChain = new SerializerChain();
    }

    @Override
    public void init(FilterConfig config) {
        //
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // handle static resources
        boolean consumedByStaticFile = staticFiles.consume(httpRequest, httpResponse);

        if (consumedByStaticFile) {
            return;
        }

        String method = getHttpMethodFrom(httpRequest);

        String httpMethodStr = method.toLowerCase();
        String uri = httpRequest.getRequestURI();
        String acceptType = httpRequest.getHeader(ACCEPT_TYPE_REQUEST_MIME_HEADER);

        Body body = Body.create();

        RequestWrapper requestWrapper = RequestWrapper.create();
        ResponseWrapper responseWrapper = ResponseWrapper.create();

        Response response = RequestResponseFactory.create(httpResponse);

        HttpMethod httpMethod = HttpMethod.get(httpMethodStr);

        RouteContext context = RouteContext.create()
                .withMatcher(routeMatcher)
                .withHttpRequest(httpRequest)
                .withUri(uri)
                .withAcceptType(acceptType)
                .withBody(body)
                .withRequestWrapper(requestWrapper)
                .withResponseWrapper(responseWrapper)
                .withResponse(response)
                .withHttpMethod(httpMethod);

        try {
            try {

                BeforeFilters.execute(context);
                Routes.execute(context);
                AfterFilters.execute(context);

            } catch (HaltException halt) {

                Halt.modify(httpResponse, body, halt);

            } catch (Exception generalException) {

                GeneralError.modify(
                        httpRequest,
                        httpResponse,
                        body,
                        requestWrapper,
                        responseWrapper,
                        exceptionMapper,
                        generalException);

            }

            // If redirected and content is null set to empty string to not throw NotConsumedException
            if (body.notSet() && responseWrapper.isRedirected()) {
                body.set("");
            }
            
            if (body.notSet() && hasOtherHandlers) {
                if (servletRequest instanceof HttpRequestWrapper) {
                    ((HttpRequestWrapper) servletRequest).notConsumed(true);
                    return;
                }
            }

            if (body.notSet()) {
                LOG.info("The requested route [{}] has not been mapped in Spark for {}: [{}]",
                         uri, ACCEPT_TYPE_REQUEST_MIME_HEADER, acceptType);
                httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);

                if (CustomErrorPages.existsFor(404)) {
                    requestWrapper.setDelegate(RequestResponseFactory.create(httpRequest));
                    responseWrapper.setDelegate(RequestResponseFactory.create(httpResponse));
                    body.set(CustomErrorPages.getFor(404, requestWrapper, responseWrapper));
                } else {
                    body.set(String.format(CustomErrorPages.NOT_FOUND));
                }
            }
        } finally {
            try {
                AfterAfterFilters.execute(context);
            } catch (Exception generalException) {
                GeneralError.modify(
                        httpRequest,
                        httpResponse,
                        body,
                        requestWrapper,
                        responseWrapper,
                        exceptionMapper,
                        generalException);
            }
        }

        if (body.isSet()) {
            body.serializeTo(httpResponse, serializerChain, httpRequest);
        } else if (chain != null) {
            chain.doFilter(httpRequest, httpResponse);
        }
    }

    private String getHttpMethodFrom(HttpServletRequest httpRequest) {
        String method = httpRequest.getHeader(HTTP_METHOD_OVERRIDE_HEADER);

        if (method == null) {
            method = httpRequest.getMethod();
        }
        return method;
    }

    @Override
    public void destroy() {
    }


}