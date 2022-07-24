package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.internal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.BadMessageException;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpField;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.HttpHeader;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.http.QuotedCSV;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.WebSocketNegotiation;

public class RFC6455Negotiation extends WebSocketNegotiation
{
    private boolean successful;
    private String key;

    public RFC6455Negotiation(Request baseRequest, HttpServletRequest request, HttpServletResponse response, WebSocketComponents components) throws BadMessageException
    {
        super(baseRequest, request, response, components);
    }

    @Override
    protected void negotiateHeaders(Request baseRequest)
    {
        super.negotiateHeaders(baseRequest);

        boolean upgrade = false;
        QuotedCSV connectionCSVs = null;
        for (HttpField field : baseRequest.getHttpFields())
        {
            HttpHeader header = field.getHeader();
            if (header != null)
            {
                switch (header)
                {
                    case UPGRADE:
                        upgrade = "websocket".equalsIgnoreCase(field.getValue());
                        break;

                    case CONNECTION:
                        if (connectionCSVs == null)
                            connectionCSVs = new QuotedCSV();
                        connectionCSVs.addValue(field.getValue());
                        break;

                    case SEC_WEBSOCKET_KEY:
                        key = field.getValue();
                        break;

                    default:
                        break;
                }
            }
        }

        successful = upgrade && connectionCSVs != null &&
            connectionCSVs.getValues().stream().anyMatch(s -> s.equalsIgnoreCase("upgrade"));
    }

    @Override
    public boolean validateHeaders()
    {
        return successful;
    }

    public String getKey()
    {
        return key;
    }
}