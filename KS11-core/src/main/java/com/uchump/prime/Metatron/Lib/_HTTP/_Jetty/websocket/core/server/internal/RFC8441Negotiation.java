package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.internal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.BadMessageException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.MetaData;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiation;

public class RFC8441Negotiation extends WebSocketNegotiation
{
    public RFC8441Negotiation(Request baseRequest, HttpServletRequest request, HttpServletResponse response, WebSocketComponents components) throws BadMessageException
    {
        super(baseRequest, request, response, components);
    }

    @Override
    public boolean validateHeaders()
    {
        MetaData.Request metaData = getBaseRequest().getMetaData();
        if (metaData == null)
            return false;
        return "websocket".equals(metaData.getProtocol());
    }
}