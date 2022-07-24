package com.uchump.prime.Metatron.Lib._HTTP._Jetty.servlet.servlets;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.MimeTypes;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.pathmap.PathSpecSet;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.IncludeExclude;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.StringUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.URIUtil;

/**
 * Include Exclude Based Filter
 * <p>
 * This is an abstract filter which helps with filtering based on include/exclude of paths, mime types, and/or http methods.
 * <p>
 * Use the {@link #shouldFilter(HttpServletRequest, HttpServletResponse)} method to determine if a request/response should be filtered. If mime types are used,
 * it should be called after {@link javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)} since the mime type may not
 * be written until then.
 *
 * Supported init params:
 * <ul>
 * <li><code>includedPaths</code> - CSV of path specs to include</li>
 * <li><code>excludedPaths</code> - CSV of path specs to exclude</li>
 * <li><code>includedMimeTypes</code> - CSV of mime types to include</li>
 * <li><code>excludedMimeTypes</code> - CSV of mime types to exclude</li>
 * <li><code>includedHttpMethods</code> - CSV of http methods to include</li>
 * <li><code>excludedHttpMethods</code> - CSV of http methods to exclude</li>
 * </ul>
 * <p>
 * Path spec rules:
 * <ul>
 * <li>If the spec starts with <code>'^'</code> the spec is assumed to be a regex based path spec and will match with normal Java regex rules.</li>
 * <li>If the spec starts with <code>'/'</code> the spec is assumed to be a Servlet url-pattern rules path spec for either an exact match or prefix based
 * match.</li>
 * <li>If the spec starts with <code>'*.'</code> the spec is assumed to be a Servlet url-pattern rules path spec for a suffix based match.</li>
 * <li>All other syntaxes are unsupported.</li>
 * </ul>
 * <p>
 * CSVs are parsed with {@link StringUtil#csvSplit(String)}
 *
 * @see PathSpecSet
 * @see IncludeExcludeSet
 */
public abstract class IncludeExcludeBasedFilter implements Filter
{	
    private final IncludeExclude<String> _mimeTypes = new IncludeExclude<>();
    private final IncludeExclude<String> _httpMethods = new IncludeExclude<>();
    private final IncludeExclude<String> _paths = new IncludeExclude<>(PathSpecSet.class);
    private static final Logger LOG = LoggerFactory.getLogger(IncludeExcludeBasedFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        final String includedPaths = filterConfig.getInitParameter("includedPaths");
        final String excludedPaths = filterConfig.getInitParameter("excludedPaths");
        final String includedMimeTypes = filterConfig.getInitParameter("includedMimeTypes");
        final String excludedMimeTypes = filterConfig.getInitParameter("excludedMimeTypes");
        final String includedHttpMethods = filterConfig.getInitParameter("includedHttpMethods");
        final String excludedHttpMethods = filterConfig.getInitParameter("excludedHttpMethods");

        if (includedPaths != null)
        {
            _paths.include(StringUtil.csvSplit(includedPaths));
        }
        if (excludedPaths != null)
        {
            _paths.exclude(StringUtil.csvSplit(excludedPaths));
        }
        if (includedMimeTypes != null)
        {
            _mimeTypes.include(StringUtil.csvSplit(includedMimeTypes));
        }
        if (excludedMimeTypes != null)
        {
            _mimeTypes.exclude(StringUtil.csvSplit(excludedMimeTypes));
        }
        if (includedHttpMethods != null)
        {
            _httpMethods.include(StringUtil.csvSplit(includedHttpMethods));
        }
        if (excludedHttpMethods != null)
        {
            _httpMethods.exclude(StringUtil.csvSplit(excludedHttpMethods));
        }
    }

    protected String guessMimeType(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
    {
        String contentType = httpResponse.getContentType();
        LOG.debug("Content Type is: {}", contentType);

        String mimeType = "";
        if (contentType != null)
        {
            mimeType = MimeTypes.getContentTypeWithoutCharset(contentType);
            LOG.debug("Mime Type is: {}", mimeType);
        }
        else
        {
            String requestUrl = httpRequest.getPathInfo();
            mimeType = MimeTypes.getDefaultMimeByExtension(requestUrl);

            if (mimeType == null)
            {
                mimeType = "";
            }

            LOG.debug("Guessed mime type is {}", mimeType);
        }

        return mimeType;
    }

    protected boolean shouldFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
    {
        String httpMethod = httpRequest.getMethod();
        LOG.debug("HTTP method is: {}", httpMethod);
        if (!_httpMethods.test(httpMethod))
        {
            LOG.debug("should not apply filter because HTTP method does not match");
            return false;
        }

        String mimeType = guessMimeType(httpRequest, httpResponse);

        if (!_mimeTypes.test(mimeType))
        {
            LOG.debug("should not apply filter because mime type does not match");
            return false;
        }

        ServletContext context = httpRequest.getServletContext();
        String path = context == null ? httpRequest.getRequestURI() : URIUtil.addPaths(httpRequest.getServletPath(), httpRequest.getPathInfo());
        LOG.debug("Path is: {}", path);
        if (!_paths.test(path))
        {
            LOG.debug("should not apply filter because path does not match");
            return false;
        }

        return true;
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("filter configuration:\n");
        sb.append("paths:\n").append(_paths).append("\n");
        sb.append("mime types:\n").append(_mimeTypes).append("\n");
        sb.append("http methods:\n").append(_httpMethods);
        return sb.toString();
    }
}