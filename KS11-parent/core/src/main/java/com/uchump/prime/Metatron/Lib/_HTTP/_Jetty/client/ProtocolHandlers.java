package com.uchump.prime.Metatron.Lib._HTTP._Jetty.client;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.client.api.Response;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.component.Dumpable;

/**
 * <p>A container for {@link ProtocolHandler}s accessible from {@link HttpClient#getProtocolHandlers()}.</p>
 */
public class ProtocolHandlers implements Dumpable
{
    private final Map<String, ProtocolHandler> handlers = new LinkedHashMap<>();

    protected ProtocolHandlers()
    {
    }

    /**
     * <p>Stores the given {@code protocolHandler} in this container.</p>
     * <p>If a protocol handler with the same name exists, it is
     * replaced by the given one, and the existing returned.</p>
     *
     * @param protocolHandler the protocol handler to store
     * @return the existing protocol handler with the same name,
     * or null if no protocol handler with that name was already stored
     * @see #remove(String)
     */
    public ProtocolHandler put(ProtocolHandler protocolHandler)
    {
        return handlers.put(protocolHandler.getName(), protocolHandler);
    }

    /**
     * <p>Removes the protocol handler with the given name.</p>
     *
     * @param name the name of the protocol handler to remove
     * @return the removed protocol handler, or null if no
     * protocol handler with that name was already stored
     * @see #put(ProtocolHandler)
     * @see #clear()
     */
    public ProtocolHandler remove(String name)
    {
        return handlers.remove(name);
    }

    /**
     * <p>Removes all protocol handlers from this container.</p>
     */
    public void clear()
    {
        handlers.clear();
    }

    /**
     * <p>Finds the first protocol handler that
     * {@link ProtocolHandler#accept(Request, Response) accepts}
     * the given request and response.</p>
     *
     * @param request the request to accept
     * @param response the response to accept
     * @return the protocol handler that accepted the request and response,
     * or null if none of the protocol handlers accepted the request and response
     */
    public ProtocolHandler find(Request request, Response response)
    {
        for (ProtocolHandler handler : handlers.values())
        {
            if (handler.accept(request, response))
                return handler;
        }
        return null;
    }

    @Override
    public String dump()
    {
        return Dumpable.dump(this);
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException
    {
        Dumpable.dumpObjects(out, indent, this, handlers);
    }
}