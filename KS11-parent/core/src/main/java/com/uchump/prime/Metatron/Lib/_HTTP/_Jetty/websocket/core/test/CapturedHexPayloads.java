package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.test;

import java.util.ArrayList;
import java.util.List;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.toolchain.Hex;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.Callback;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.Frame;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.core.common.OutgoingFrames;

public class CapturedHexPayloads implements OutgoingFrames
{
    private final List<String> captured = new ArrayList<>();

    @Override
    public void sendFrame(Frame frame, Callback callback, boolean batch)
    {
        String hexPayload = Hex.asHex(frame.getPayload());
        captured.add(hexPayload);
        if (callback != null)
        {
            callback.succeeded();
        }
    }

    public List<String> getCaptured()
    {
        return captured;
    }
}