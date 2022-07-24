package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.examples;

import java.time.Duration;

import javax.servlet.annotation.WebServlet;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServlet;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServletFactory;

@SuppressWarnings("serial")
@WebServlet(name = "MyEcho WebSocket Servlet", urlPatterns = {"/echo"})
public class MyEchoServlet extends JettyWebSocketServlet
{
    @Override
    public void configure(JettyWebSocketServletFactory factory)
    {
        // set a 10 second timeout
        factory.setIdleTimeout(Duration.ofSeconds(10));

        // register MyEchoSocket as the WebSocket to create on Upgrade
        factory.register(MyEchoSocket.class);
    }
}