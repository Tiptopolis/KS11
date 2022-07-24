package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server;

import java.net.Socket;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.Connection.Listener;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.EndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.SocketChannelEndPoint;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.io.ssl.SslConnection.DecryptedEndPoint;

/**
 * A Connection Lister for customization of SocketConnections.
 * <p>
 * Instances of this listener may be added to a {@link Connector} (or
 * {@link ConnectionFactory}) so that they are applied to all connections
 * for that connector (or protocol) and thus allow additional Socket
 * configuration to be applied by implementing {@link #customize(Socket, Class, boolean)}
 */
public class SocketCustomizationListener implements Listener
{
    private final boolean _ssl;

    /**
     * Construct with SSL unwrapping on.
     */
    public SocketCustomizationListener()
    {
        this(true);
    }

    /**
     * @param ssl If True, then a Socket underlying an SSLConnection is unwrapped
     * and notified.
     */
    public SocketCustomizationListener(boolean ssl)
    {
        _ssl = ssl;
    }

    @Override
    public void onOpened(Connection connection)
    {
        EndPoint endPoint = connection.getEndPoint();
        boolean ssl = false;

        if (_ssl && endPoint instanceof DecryptedEndPoint)
        {
            endPoint = ((DecryptedEndPoint)endPoint).getSslConnection().getEndPoint();
            ssl = true;
        }

        if (endPoint instanceof SocketChannelEndPoint)
        {
            Socket socket = ((SocketChannelEndPoint)endPoint).getChannel().socket();
            customize(socket, connection.getClass(), ssl);
        }
    }

    /**
     * This method may be extended to configure a socket on open
     * events.
     *
     * @param socket The Socket to configure
     * @param connection The class of the connection (The socket may be wrapped
     * by an {@link SslConnection} prior to this connection).
     * @param ssl True if the socket is wrapped with an SslConnection
     */
    protected void customize(Socket socket, Class<? extends Connection> connection, boolean ssl)
    {
    }

    @Override
    public void onClosed(Connection connection)
    {
    }
}