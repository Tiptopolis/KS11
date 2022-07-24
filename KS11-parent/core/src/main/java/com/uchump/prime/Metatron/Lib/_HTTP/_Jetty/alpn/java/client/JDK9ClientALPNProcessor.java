package com.uchump.prime.Metatron.Lib._HTTP._Jetty.alpn.java.client;
import java.util.List;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.alpn.client.ALPNClientConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.ALPNProcessor;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.SslConnection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.SslConnection.DecryptedEndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.SslHandshakeListener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.SslHandshakeListener.Event;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.JavaVersion;


public class JDK9ClientALPNProcessor implements ALPNProcessor.Client
{
    private static final Logger LOG = LoggerFactory.getLogger(JDK9ClientALPNProcessor.class);

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
        ALPNClientConnection alpn = (ALPNClientConnection)connection;
        SSLParameters sslParameters = sslEngine.getSSLParameters();
        List<String> protocols = alpn.getProtocols();
        sslParameters.setApplicationProtocols(protocols.toArray(new String[0]));
        sslEngine.setSSLParameters(sslParameters);
        ((DecryptedEndPoint)connection.getEndPoint()).getSslConnection()
            .addHandshakeListener(new ALPNListener(alpn));
    }

    private static final class ALPNListener implements SslHandshakeListener
    {
        private final ALPNClientConnection alpnConnection;

        private ALPNListener(ALPNClientConnection connection)
        {
            alpnConnection = connection;
        }

        @Override
        public void handshakeSucceeded(Event event)
        {
            String protocol = alpnConnection.getSSLEngine().getApplicationProtocol();
            if (LOG.isDebugEnabled())
                LOG.debug("selected protocol '{}'", protocol);
            if (protocol != null && !protocol.isEmpty())
                alpnConnection.selected(protocol);
            else
                alpnConnection.selected(null);
        }
    }
}