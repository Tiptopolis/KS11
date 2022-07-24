package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;
import org.apache.http.HttpEntity;

import java.io.IOException;

interface ContentConsumingStrategy {
    String toString(HttpEntity stream) throws IOException;
}