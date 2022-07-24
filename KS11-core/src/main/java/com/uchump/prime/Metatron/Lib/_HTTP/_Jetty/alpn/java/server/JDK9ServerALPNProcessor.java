package com.uchump.prime.Metatron.Lib._HTTP._Jetty.alpn.java.server;


import java.util.List;
import java.util.function.BiFunction;
import javax.net.ssl.SSLEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.alpn.server.ALPNServerConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.ALPNProcessor;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.SslHandshakeListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.SslConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.JavaVersion;


public class JDK9ServerALPNProcessor implements ALPNProcessor.Server, SslHandshakeListener
{
    private static final Logger LOG = LoggerFactory.getLogger(JDK9ServerALPNProcessor.class);

    @Override
    public void init()
    {
        if (JavaVersion.VERSION.getPlatform() < 9)
            throw new IllegalStateException(this + " not applicable for java " + JavaVersion.VERSION);
    }

    @Override
    public boolean appliesTo(SSLEngine sslEngine)
    {
        Module module = sslEngine.getClass().getModule();
        return module != null && "java.base".equals(module.getName());
    }

    @Override
    public void configure(SSLEngine sslEngine, Connection connection)
    {
        sslEngine.setHandshakeApplicationProtocolSelector(new ALPNCallback((ALPNServerConnection)connection));
    }

    private static final class ALPNCallback implements BiFunction<SSLEngine, List<String>, String>, SslHandshakeListener
    {
        private final ALPNServerConnection alpnConnection;

        private ALPNCallback(ALPNServerConnection connection)
        {
            alpnConnection = connection;
            ((SslConnection.DecryptedEndPoint)alpnConnection.getEndPoint()).getSslConnection().addHandshakeListener(this);
        }

        @Override
        public String apply(SSLEngine engine, List<String> protocols)
        {
            try
            {
                if (LOG.isDebugEnabled())
                    LOG.debug("apply {} {}", alpnConnection, protocols);
                alpnConnection.select(protocols);
                return alpnConnection.getProtocol();
            }
            catch (Throwable x)
            {
                // Cannot negotiate the protocol, return null to have
                // JSSE send Alert.NO_APPLICATION_PROTOCOL to the client.
                return null;
            }
        }

        @Override
        public void handshakeSucceeded(Event event)
        {
            String protocol = alpnConnection.getProtocol();
            if (LOG.isDebugEnabled())
                LOG.debug("TLS handshake succeeded, protocol={} for {}", protocol, alpnConnection);
            if (protocol == null)
                alpnConnection.unsupported();
        }

        @Override
        public void handshakeFailed(Event event, Throwable failure)
        {
            if (LOG.isDebugEnabled())
                LOG.debug("TLS handshake failed {}", alpnConnection, failure);
        }
    }
}