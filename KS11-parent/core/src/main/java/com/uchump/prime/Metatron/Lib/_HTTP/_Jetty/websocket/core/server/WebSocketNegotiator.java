package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server;

import java.io.IOException;
import java.util.function.Function;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Configuration;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.FrameHandler;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.server.internal.CreatorNegotiator;

public interface WebSocketNegotiator extends Configuration.Customizer
{
    FrameHandler negotiate(WebSocketNegotiation negotiation) throws IOException;

    @Override
    default void customize(Configuration configurable)
    {
    }

    static WebSocketNegotiator from(Function<WebSocketNegotiation, FrameHandler> negotiate)
    {
        return from(negotiate, null);
    }

    static WebSocketNegotiator from(Function<WebSocketNegotiation, FrameHandler> negotiate, Configuration.Customizer customizer)
    {
        return new AbstractNegotiator(customizer)
        {
            @Override
            public FrameHandler negotiate(WebSocketNegotiation negotiation)
            {
                return negotiate.apply(negotiation);
            }
        };
    }

    static WebSocketNegotiator from(WebSocketCreator creator, FrameHandlerFactory factory)
    {
        return from(creator, factory, null);
    }

    static WebSocketNegotiator from(WebSocketCreator creator, FrameHandlerFactory factory, Configuration.Customizer customizer)
    {
        return new CreatorNegotiator(creator, factory, customizer);
    }

    abstract class AbstractNegotiator extends Configuration.ConfigurationCustomizer implements WebSocketNegotiator
    {
        final Configuration.Customizer customizer;

        public AbstractNegotiator()
        {
            this(null);
        }

        public AbstractNegotiator(Configuration.Customizer customizer)
        {
            this.customizer = customizer;
        }

        @Override
        public void customize(Configuration configurable)
        {
            if (customizer != null)
                customizer.customize(configurable);
            super.customize(configurable);
        }
    }
}