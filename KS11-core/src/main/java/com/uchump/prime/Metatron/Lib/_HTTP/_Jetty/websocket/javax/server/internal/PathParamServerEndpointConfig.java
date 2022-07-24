package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.server.internal;


import java.util.HashMap;
import java.util.Map;
import javax.websocket.server.ServerEndpointConfig;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.URIUtil;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.PathParamProvider;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.common.ServerEndpointConfigWrapper;

/**
 * Make {@link javax.websocket.server.PathParam} information from the incoming request available
 * on {@link ServerEndpointConfig}
 */
public class PathParamServerEndpointConfig extends ServerEndpointConfigWrapper implements PathParamProvider
{
    private final Map<String, String> pathParamMap;

    public PathParamServerEndpointConfig(ServerEndpointConfig config, Map<String, String> pathParams)
    {
        super(config);

        pathParamMap = new HashMap<>();
        if (pathParams != null)
            pathParams.forEach((key, value) -> pathParamMap.put(key, URIUtil.decodePath(value)));
    }

    @Override
    public Map<String, String> getPathParams()
    {
        return pathParamMap;
    }
}