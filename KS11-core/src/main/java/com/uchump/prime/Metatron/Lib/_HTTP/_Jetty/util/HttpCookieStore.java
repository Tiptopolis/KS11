package com.uchump.prime.Metatron.Lib._HTTP._Jetty.util;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link CookieStore} that delegates to an instance created by {@link CookieManager}
 * via {@link CookieManager#getCookieStore()}.
 */
public class HttpCookieStore implements CookieStore
{
    private final CookieStore delegate;

    public HttpCookieStore()
    {
        delegate = new CookieManager().getCookieStore();
    }

    @Override
    public void add(URI uri, HttpCookie cookie)
    {
        delegate.add(uri, cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri)
    {
        return delegate.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies()
    {
        return delegate.getCookies();
    }

    @Override
    public List<URI> getURIs()
    {
        return delegate.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie)
    {
        return delegate.remove(uri, cookie);
    }

    @Override
    public boolean removeAll()
    {
        return delegate.removeAll();
    }

    public static List<HttpCookie> matchPath(URI uri, List<HttpCookie> cookies)
    {
        if (cookies == null || cookies.isEmpty())
            return Collections.emptyList();
        List<HttpCookie> result = new ArrayList<>(4);
        String path = uri.getPath();
        if (path == null || path.trim().isEmpty())
            path = "/";
        for (HttpCookie cookie : cookies)
        {
            String cookiePath = cookie.getPath();
            if (cookiePath == null)
            {
                result.add(cookie);
            }
            else
            {
                // RFC 6265, section 5.1.4, path matching algorithm.
                if (path.equals(cookiePath))
                {
                    result.add(cookie);
                }
                else if (path.startsWith(cookiePath))
                {
                    if (cookiePath.endsWith("/") || path.charAt(cookiePath.length()) == '/')
                        result.add(cookie);
                }
            }
        }
        return result;
    }

    public static class Empty implements CookieStore
    {
        @Override
        public void add(URI uri, HttpCookie cookie)
        {
        }

        @Override
        public List<HttpCookie> get(URI uri)
        {
            return Collections.emptyList();
        }

        @Override
        public List<HttpCookie> getCookies()
        {
            return Collections.emptyList();
        }

        @Override
        public List<URI> getURIs()
        {
            return Collections.emptyList();
        }

        @Override
        public boolean remove(URI uri, HttpCookie cookie)
        {
            return false;
        }

        @Override
        public boolean removeAll()
        {
            return false;
        }
    }
}