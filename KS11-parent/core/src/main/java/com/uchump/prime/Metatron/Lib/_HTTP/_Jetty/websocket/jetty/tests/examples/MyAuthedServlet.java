package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.examples;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServlet;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.server.JettyWebSocketServletFactory;

@SuppressWarnings("serial")
public class MyAuthedServlet extends JettyWebSocketServlet
{
    @Override
    public void configure(JettyWebSocketServletFactory factory)
    {
        factory.setCreator(new MyAuthedCreator());
    }
}