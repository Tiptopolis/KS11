package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

class ToStringConsumer implements ContentConsumingStrategy {

    @Override
    public String toString(HttpEntity entity) throws IOException {
        return EntityUtils.toString(entity);
    }
}