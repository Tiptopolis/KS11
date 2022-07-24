package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.javax.tests;

import java.util.Map;

public class UpgradeUtils
{
    public static String generateUpgradeRequest(CharSequence requestPath, Map<String, String> headers)
    {
        StringBuilder upgradeRequest = new StringBuilder();
        upgradeRequest.append("GET ");
        upgradeRequest.append(requestPath == null ? "/" : requestPath);
        upgradeRequest.append(" HTTP/1.1\r\n");
        headers.entrySet().forEach(e ->
            upgradeRequest.append(e.getKey()).append(": ").append(e.getValue()).append("\r\n"));
        upgradeRequest.append("\r\n");
        return upgradeRequest.toString();
    }
}