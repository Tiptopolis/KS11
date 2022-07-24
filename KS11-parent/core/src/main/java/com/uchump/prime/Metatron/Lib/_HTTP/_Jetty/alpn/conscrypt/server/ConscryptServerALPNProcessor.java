package com.uchump.prime.Metatron.Lib._HTTP._Jetty.alpn.conscrypt.server;

import java.security.Security;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.alpn.server.ALPNServerConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.ALPNProcessor;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.SslHandshakeListener;

public class ConscryptServerALPNProcessor implements ALPNProcessor.Server
{
    private static final Logger LOG = LoggerFactory.getLogger(ConscryptServerALPNProcessor.class);

    @Override
    public void init()
    {
        if (Security.getProvider("Conscrypt") == null)
        {
            Security.addProvider(new OpenSSLProvider());
            if (LOG.isDebugEnabled())
                LOG.debug("Added Conscrypt provider");
        }
    }

    @Override
    public boolean appliesTo(SSLEngine sslEngine)
    {
        return sslEngine.getClass().getName().startsWith("org.conscrypt.");
    }

    @Override
    public void configure(SSLEngine sslEngine, Connection connection)
    {
        try
        {
            Conscrypt.setApplicationProtocolSelector(sslEngine, new ALPNCallback((ALPNServerConnection)connection));
        }
        catch (RuntimeException x)
        {
            throw x;
        }
        catch (Exception x)
        {
            throw new RuntimeException(x);
        }
    }

    private final class ALPNCallback extends ApplicationProtocolSelector implements SslHandshakeListener
    {
        private final ALPNServerConnection alpnConnection;

        private ALPNCallback(ALPNServerConnection connection)
        {
            alpnConnection = connection;
            ((DecryptedEndPoint)alpnConnection.getEndPoint()).getSslConnection().addHandshakeListener(this);
        }

        @Override
        public String selectApplicationProtocol(SSLEngine engine, List<String> protocols)
        {
            alpnConnection.select(protocols);
            String protocol = alpnConnection.getProtocol();
            if (LOG.isDebugEnabled())
                LOG.debug("Selected {} among {} for {}", protocol, protocols, alpnConnection);
            return protocol;
        }

        @Override
        public String selectApplicationProtocol(SSLSocket socket, List<String> protocols)
        {
            throw new UnsupportedOperationException();
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