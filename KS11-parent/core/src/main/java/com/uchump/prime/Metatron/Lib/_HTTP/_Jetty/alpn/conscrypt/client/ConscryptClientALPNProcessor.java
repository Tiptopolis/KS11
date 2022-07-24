package com.uchump.prime.Metatron.Lib._HTTP._Jetty.alpn.conscrypt.client;

import java.security.Security;
import javax.net.ssl.SSLEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.alpn.client.ALPNClientConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.ALPNProcessor;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.SslHandshakeListener;

public class ConscryptClientALPNProcessor implements ALPNProcessor.Client
{
    private static final Logger LOG = LoggerFactory.getLogger(ConscryptClientALPNProcessor.class);

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
            ALPNClientConnection alpn = (ALPNClientConnection)connection;
            String[] protocols = alpn.getProtocols().toArray(new String[0]);
            Conscrypt.setApplicationProtocols(sslEngine, protocols);
            ((SslConnection.DecryptedEndPoint)connection.getEndPoint()).getSslConnection()
                .addHandshakeListener(new ALPNListener(alpn));
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

    private final class ALPNListener implements SslHandshakeListener
    {
        private final ALPNClientConnection alpnConnection;

        private ALPNListener(ALPNClientConnection connection)
        {
            alpnConnection = connection;
        }

        @Override
        public void handshakeSucceeded(Event event)
        {
            try
            {
                SSLEngine sslEngine = alpnConnection.getSSLEngine();
                String protocol = Conscrypt.getApplicationProtocol(sslEngine);
                if (LOG.isDebugEnabled())
                    LOG.debug("Selected {} for {}", protocol, alpnConnection);
                alpnConnection.selected(protocol);
            }
            catch (Throwable e)
            {
                LOG.warn("Unable to process Conscrypt ApplicationProtocol for {}", alpnConnection, e);
                alpnConnection.selected(null);
            }
        }
    }
}