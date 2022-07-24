package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.PushBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpField;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpFields;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpMethod;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpURI;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.MetaData;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.StringUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.URIUtil;

/**
 *
 */
public class PushBuilderImpl implements PushBuilder
{
    private static final Logger LOG = LoggerFactory.getLogger(PushBuilderImpl.class);

    private static final HttpField JETTY_PUSH = new HttpField("x-http2-push", "PushBuilder");
    private static EnumSet<HttpMethod> UNSAFE_METHODS = EnumSet.of(
        HttpMethod.POST,
        HttpMethod.PUT,
        HttpMethod.DELETE,
        HttpMethod.CONNECT,
        HttpMethod.OPTIONS,
        HttpMethod.TRACE);

    private final Request _request;
    private final HttpFields.Mutable _fields;
    private String _method;
    private String _queryString;
    private String _sessionId;
    private String _path;
    private String _lastModified;

    public PushBuilderImpl(Request request, HttpFields fields, String method, String queryString, String sessionId)
    {
        super();
        _request = request;
        _fields = HttpFields.build(fields);
        _method = method;
        _queryString = queryString;
        _sessionId = sessionId;
        _fields.add(JETTY_PUSH);
        if (LOG.isDebugEnabled())
            LOG.debug("PushBuilder({} {}?{} s={})", _method, _request.getRequestURI(), _queryString, _sessionId);
    }

    @Override
    public String getMethod()
    {
        return _method;
    }

    @Override
    public PushBuilder method(String method)
    {
        Objects.requireNonNull(method);
        
        if (StringUtil.isBlank(method) || UNSAFE_METHODS.contains(HttpMethod.fromString(method)))
            throw new IllegalArgumentException("Method not allowed for push: " + method);
        _method = method;
        return this;
    }

    @Override
    public String getQueryString()
    {
        return _queryString;
    }

    @Override
    public PushBuilder queryString(String queryString)
    {
        _queryString = queryString;
        return this;
    }

    @Override
    public String getSessionId()
    {
        return _sessionId;
    }

    @Override
    public PushBuilder sessionId(String sessionId)
    {
        _sessionId = sessionId;
        return this;
    }

    @Override
    public Set<String> getHeaderNames()
    {
        return _fields.getFieldNamesCollection();
    }

    @Override
    public String getHeader(String name)
    {
        return _fields.get(name);
    }

    @Override
    public PushBuilder setHeader(String name, String value)
    {
        _fields.put(name, value);
        return this;
    }

    @Override
    public PushBuilder addHeader(String name, String value)
    {
        _fields.add(name, value);
        return this;
    }

    @Override
    public PushBuilder removeHeader(String name)
    {
        _fields.remove(name);
        return this;
    }

    @Override
    public String getPath()
    {
        return _path;
    }

    @Override
    public PushBuilder path(String path)
    {
        _path = path;
        return this;
    }

    @Override
    public void push()
    {
        if (_path == null || _path.length() == 0)
            throw new IllegalStateException("Bad Path " + _path);

        String path = _path;
        String query = _queryString;
        int q = path.indexOf('?');
        if (q >= 0)
        {
            query = (query != null && query.length() > 0) ? (path.substring(q + 1) + '&' + query) : path.substring(q + 1);
            path = path.substring(0, q);
        }

        if (!path.startsWith("/"))
            path = URIUtil.addPaths(_request.getContextPath(), path);

        String param = null;
        if (_sessionId != null)
        {
            if (_request.isRequestedSessionIdFromURL())
                param = "jsessionid=" + _sessionId;
            // TODO else
            //      _rawFields.add("Cookie","JSESSIONID="+_sessionId);
        }

        HttpURI uri = HttpURI.build(_request.getHttpURI(), path, param, query).normalize();
        MetaData.Request push = new MetaData.Request(_method, uri, _request.getHttpVersion(), _fields);

        if (LOG.isDebugEnabled())
            LOG.debug("Push {} {} inm={} ims={}", _method, uri, _fields.get(HttpHeader.IF_NONE_MATCH), _fields.get(HttpHeader.IF_MODIFIED_SINCE));

        _request.getHttpChannel().getHttpTransport().push(push);
        _path = null;
        _lastModified = null;
    }
}