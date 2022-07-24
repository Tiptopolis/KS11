package com.uchump.prime.Metatron.Lib._HTTP._Jetty.alpn.server;

import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.ConnectionFactory;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Connector;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.NegotiatingServerConnection;

public class ALPNServerConnection extends NegotiatingServerConnection
{
    private static final Logger LOG = LoggerFactory.getLogger(ALPNServerConnection.class);

    public ALPNServerConnection(Connector connector, EndPoint endPoint, SSLEngine engine, List<String> protocols, String defaultProtocol)
    {
        super(connector, endPoint, engine, protocols, defaultProtocol);
    }

    public void unsupported()
    {
        select(Collections.emptyList());
    }

    public void select(List<String> clientProtocols)
    {
        SSLEngine sslEngine = getSSLEngine();
        List<String> serverProtocols = getProtocols();
        SSLSession sslSession = sslEngine.getHandshakeSession();
        if (sslSession == null)
            sslSession = sslEngine.getSession();
        String tlsProtocol = sslSession.getProtocol();
        String tlsCipher = sslSession.getCipherSuite();
        String negotiated = null;

        // RFC 7301 states that the server picks the protocol
        // that it prefers that is also supported by the client.
        for (String serverProtocol : serverProtocols)
        {
            if (clientProtocols.contains(serverProtocol))
            {
                ConnectionFactory factory = getConnector().getConnectionFactory(serverProtocol);
                if (factory instanceof CipherDiscriminator && !((CipherDiscriminator)factory).isAcceptable(serverProtocol, tlsProtocol, tlsCipher))
                {
                    if (LOG.isDebugEnabled())
                        LOG.debug("Protocol {} not acceptable to {} for {}/{} on {}", serverProtocol, factory, tlsProtocol, tlsCipher, getEndPoint());
                    continue;
                }

                negotiated = serverProtocol;
                break;
            }
        }
        if (negotiated == null)
        {
            if (clientProtocols.isEmpty())
            {
                negotiated = getDefaultProtocol();
            }
            else
            {
                if (LOG.isDebugEnabled())
                    LOG.debug("Could not negotiate protocol from client{} and server{} on {}", clientProtocols, serverProtocols, getEndPoint());
                throw new IllegalStateException();
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug("Protocol selected {} from client{} and server{} on {}", negotiated, clientProtocols, serverProtocols, getEndPoint());
        setProtocol(negotiated);
    }
}