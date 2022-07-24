package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpVersion;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;

/**
 * <p>HttpUpgrader prepares a HTTP request to upgrade from one protocol to another,
 * and implements the upgrade mechanism.</p>
 * <p>The upgrade mechanism can be the
 * <a href="https://tools.ietf.org/html/rfc7230#section-6.7">HTTP/1.1 upgrade mechanism</a>
 * or the
 * <a href="https://tools.ietf.org/html/rfc8441#section-4">HTTP/2 extended CONNECT mechanism</a>.</p>
 * <p>Given the differences among mechanism implementations, a request needs to be
 * prepared before being sent to comply with the mechanism requirements (for example,
 * add required headers, etc.).</p>
 */
public interface HttpUpgrader
{
    /**
     * <p>Prepares the request for the upgrade, for example by setting the HTTP method
     * or by setting HTTP headers required for the upgrade.</p>
     *
     * @param request the request to prepare
     */
    public void prepare(HttpRequest request);

    /**
     * <p>Upgrades the given {@code endPoint} to a different protocol.</p>
     * <p>The success or failure of the upgrade should be communicated via the given {@code callback}.</p>
     * <p>An exception thrown by this method is equivalent to failing the callback.</p>
     *
     * @param response the response with the information about the upgrade
     * @param endPoint the EndPoint to upgrade
     * @param callback a callback to notify of the success or failure of the upgrade
     */
    public void upgrade(HttpResponse response, EndPoint endPoint, Callback callback);

    /**
     * <p>A factory for {@link HttpUpgrader}s.</p>
     * <p>A {@link Request} subclass should implement this interface
     * if it wants to create a specific HttpUpgrader.</p>
     */
    public interface Factory
    {
        public HttpUpgrader newHttpUpgrader(HttpVersion version);
    }
}