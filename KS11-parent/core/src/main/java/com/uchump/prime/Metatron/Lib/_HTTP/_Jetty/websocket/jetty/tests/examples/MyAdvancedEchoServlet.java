package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.examples;

import java.time.Duration;
import javax.servlet.annotation.WebServlet;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServlet;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServletFactory;

@SuppressWarnings("serial")
@WebServlet(name = "MyAdvanced Echo WebSocket Servlet", urlPatterns = {"/advecho"})
public class MyAdvancedEchoServlet extends JettyWebSocketServlet
{
    @Override
    public void configure(JettyWebSocketServletFactory factory)
    {
        // set a 10 second timeout
        factory.setIdleTimeout(Duration.ofSeconds(10));

        // set a custom WebSocket creator
        factory.setCreator(new MyAdvancedEchoCreator());
    }
}