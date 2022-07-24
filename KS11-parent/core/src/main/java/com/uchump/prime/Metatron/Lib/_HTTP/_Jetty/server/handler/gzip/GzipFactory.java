package com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.handler.gzip;

import com.uchump.prime.Metatron.Lib._HTTP._Jetty.server.Request;
import com.uchump.prime.Metatron.Lib._HTTP._Jetty.util.compression.DeflaterPool;

public interface GzipFactory
{
    DeflaterPool.Entry getDeflaterEntry(Request request, long contentLength);

    boolean isMimeTypeGzipable(String mimetype);
}