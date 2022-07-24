package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.jetty.tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.Session;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.OnWebSocketConnect;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api.annotations.WebSocket;



@SuppressWarnings("unused")
@WebSocket
public class ParamsEndpoint
{
    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException
    {
        Map<String, List<String>> params = session.getUpgradeRequest().getParameterMap();
        StringBuilder msg = new StringBuilder();

        for (String key : params.keySet())
        {
            msg.append("Params[").append(key).append("]=");
            msg.append(params.get(key).stream().collect(Collectors.joining(", ", "[", "]")));
            msg.append("\n");
        }

        session.getRemote().sendString(msg.toString());
    }
}