package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.internal;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.WebSocketConstants;

public final class WebSocketCore
{

    /**
     * Concatenate the provided key with the Magic GUID and return the Base64 encoded form.
     *
     * @param key the key to hash
     * @return the {@code Sec-WebSocket-Accept} header response (per opening handshake spec)
     */
    public static String hashKey(String key)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(key.getBytes(StandardCharsets.UTF_8));
            md.update(WebSocketConstants.MAGIC);
            return Base64.getEncoder().encodeToString(md.digest());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}