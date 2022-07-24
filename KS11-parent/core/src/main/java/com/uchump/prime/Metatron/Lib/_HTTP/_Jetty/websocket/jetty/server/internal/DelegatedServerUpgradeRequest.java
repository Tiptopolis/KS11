package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.internal;

import java.net.HttpCookie;
import java.net.SocketAddress;
import java.net.URI;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.ExtensionConfig;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.ServerUpgradeRequest;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.common.JettyExtensionConfig;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyServerUpgradeRequest;

public class DelegatedServerUpgradeRequest implements JettyServerUpgradeRequest
{
    private final ServerUpgradeRequest upgradeRequest;

    public DelegatedServerUpgradeRequest(ServerUpgradeRequest request)
    {
        upgradeRequest = request;
    }

    @Override
    public List<HttpCookie> getCookies()
    {
        return upgradeRequest.getCookies();
    }

    @Override
    public List<ExtensionConfig> getExtensions()
    {
        return upgradeRequest.getExtensions().stream()
            .map(JettyExtensionConfig::new)
            .collect(Collectors.toList());
    }

    @Override
    public String getHeader(String name)
    {
        return upgradeRequest.getHeader(name);
    }

    @Override
    public int getHeaderInt(String name)
    {
        return upgradeRequest.getHeaderInt(name);
    }

    @Override
    public Map<String, List<String>> getHeaders()
    {
        return upgradeRequest.getHeadersMap();
    }

    @Override
    public List<String> getHeaders(String name)
    {
        return upgradeRequest.getHeaders(name);
    }

    @Override
    public String getHost()
    {
        return upgradeRequest.getHost();
    }

    @Override
    public String getHttpVersion()
    {
        return upgradeRequest.getHttpVersion();
    }

    @Override
    public String getMethod()
    {
        return upgradeRequest.getMethod();
    }

    @Override
    public String getOrigin()
    {
        return upgradeRequest.getOrigin();
    }

    @Override
    public Map<String, List<String>> getParameterMap()
    {
        return upgradeRequest.getParameterMap();
    }

    @Override
    public String getProtocolVersion()
    {
        return upgradeRequest.getProtocolVersion();
    }

    @Override
    public String getQueryString()
    {
        return upgradeRequest.getQueryString();
    }

    @Override
    public URI getRequestURI()
    {
        return upgradeRequest.getRequestURI();
    }

    @Override
    public HttpSession getSession()
    {
        return upgradeRequest.getSession();
    }

    @Override
    public List<String> getSubProtocols()
    {
        return upgradeRequest.getSubProtocols();
    }

    @Override
    public Principal getUserPrincipal()
    {
        return upgradeRequest.getUserPrincipal();
    }

    @Override
    public boolean hasSubProtocol(String subprotocol)
    {
        return upgradeRequest.hasSubProtocol(subprotocol);
    }

    @Override
    public boolean isSecure()
    {
        return upgradeRequest.isSecure();
    }

    @Override
    public X509Certificate[] getCertificates()
    {
        return upgradeRequest.getCertificates();
    }

    @Override
    public HttpServletRequest getHttpServletRequest()
    {
        return upgradeRequest.getHttpServletRequest();
    }

    @Override
    public Locale getLocale()
    {
        return upgradeRequest.getLocale();
    }

    @Override
    public Enumeration<Locale> getLocales()
    {
        return upgradeRequest.getLocales();
    }

    @Override
    public SocketAddress getLocalSocketAddress()
    {
        return upgradeRequest.getLocalSocketAddress();
    }

    @Override
    public SocketAddress getRemoteSocketAddress()
    {
        return upgradeRequest.getRemoteSocketAddress();
    }

    @Override
    public String getRequestPath()
    {
        return upgradeRequest.getRequestPath();
    }

    @Override
    public Object getServletAttribute(String name)
    {
        return upgradeRequest.getServletAttribute(name);
    }

    @Override
    public Map<String, Object> getServletAttributes()
    {
        return upgradeRequest.getServletAttributes();
    }

    @Override
    public Map<String, List<String>> getServletParameters()
    {
        return upgradeRequest.getServletParameters();
    }

    @Override
    public boolean isUserInRole(String role)
    {
        return upgradeRequest.isUserInRole(role);
    }

    @Override
    public void setServletAttribute(String name, Object value)
    {
        upgradeRequest.setServletAttribute(name, value);
    }
}