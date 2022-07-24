package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.QuotedStringTokenizer;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.annotation.ManagedObject;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.AbstractExtension;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.ExtensionConfig;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketComponents;

@ManagedObject("Identity Extension")
public class IdentityExtension extends AbstractExtension
{
    private String id;

    public String getParam(String key)
    {
        return getConfig().getParameter(key, "?");
    }

    @Override
    public String getName()
    {
        return "identity";
    }

    @Override
    public void onFrame(Frame frame, Callback callback)
    {
        // pass through
        nextIncomingFrame(frame, callback);
    }

    @Override
    public void sendFrame(Frame frame, Callback callback, boolean batch)
    {
        // pass through
        nextOutgoingFrame(frame, callback, batch);
    }

    @Override
    public void init(ExtensionConfig config, WebSocketComponents components)
    {
        super.init(config, components);

        StringBuilder s = new StringBuilder();
        s.append(config.getName());
        s.append("@").append(Integer.toHexString(hashCode()));
        s.append("[");
        boolean delim = false;
        for (String param : config.getParameterKeys())
        {
            if (delim)
            {
                s.append(';');
            }
            s.append(param).append('=').append(QuotedStringTokenizer.quoteIfNeeded(config.getParameter(param, ""), ";="));
            delim = true;
        }
        s.append("]");
        id = s.toString();
    }

    @Override
    public String toString()
    {
        return id;
    }
}