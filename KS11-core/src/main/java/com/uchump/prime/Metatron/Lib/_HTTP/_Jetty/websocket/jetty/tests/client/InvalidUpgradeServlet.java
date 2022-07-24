package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests.client;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal.WebSocketCore;



public class InvalidUpgradeServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    {
        String pathInfo = req.getPathInfo();
        if (pathInfo.contains("only-accept"))
        {
            // Force 200 response, no response body content, incomplete websocket response headers, no actual upgrade for this test
            resp.setStatus(HttpServletResponse.SC_OK);
            String key = req.getHeader(HttpHeader.SEC_WEBSOCKET_KEY.toString());
            resp.setHeader(HttpHeader.SEC_WEBSOCKET_ACCEPT.toString(), WebSocketCore.hashKey(key));
        }
        else if (pathInfo.contains("close-connection"))
        {
            // Force 101 response, with invalid Connection header, invalid handshake
            resp.setStatus(HttpServletResponse.SC_SWITCHING_PROTOCOLS);
            String key = req.getHeader(HttpHeader.SEC_WEBSOCKET_KEY.toString());
            resp.setHeader(HttpHeader.CONNECTION.toString(), "close");
            resp.setHeader(HttpHeader.SEC_WEBSOCKET_ACCEPT.toString(), WebSocketCore.hashKey(key));
        }
        else if (pathInfo.contains("missing-connection"))
        {
            // Force 101 response, with no Connection header, invalid handshake
            resp.setStatus(HttpServletResponse.SC_SWITCHING_PROTOCOLS);
            String key = req.getHeader(HttpHeader.SEC_WEBSOCKET_KEY.toString());
            // Intentionally leave out Connection header
            resp.setHeader(HttpHeader.SEC_WEBSOCKET_ACCEPT.toString(), WebSocketCore.hashKey(key));
        }
        else if (pathInfo.contains("rubbish-accept"))
        {
            // Force 101 response, with no Connection header, invalid handshake
            resp.setStatus(HttpServletResponse.SC_SWITCHING_PROTOCOLS);
            resp.setHeader(HttpHeader.SEC_WEBSOCKET_ACCEPT.toString(), "rubbish");
        }
        else
        {
            resp.setStatus(500);
        }
    }
}