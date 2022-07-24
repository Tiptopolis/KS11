package com.uchump.prime.Metatron.Lib.SimpleHttp.apache;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;

import com.uchump.prime.Metatron.Lib.SimpleHttp.EmptyHeaders;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Header;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Headers;

import static com.uchump.prime.Metatron.Lib.SimpleHttp.HeaderList.headers;
import static com.uchump.prime.Metatron.Lib.SimpleHttp.HeaderPair.header;

public class Coercions {

    private Coercions() { }
    
    public static org.apache.http.Header[] asApacheBasicHeader(Headers headers) {
        List<org.apache.http.Header> list = new ArrayList<>();
        for (Header header : headers)
            list.add(new BasicHeader(header.name(), header.value()));
        return list.toArray(new org.apache.http.Header[list.size()]);
    }

    public static Headers asHeaders(org.apache.http.Header[] headers) {
        if (headers.length == 0)
            return EmptyHeaders.emptyHeaders();
        
        List<Header> list = new ArrayList<>();
        for (org.apache.http.Header header : headers)
            list.add(header(header.getName(), header.getValue()));

        Header[] array = list.toArray(new Header[list.size()]);
        return headers(array[0], tail(array));
    }

    private static Header[] tail(Header[] array) {
        return Arrays.copyOfRange(array, 1, array.length);
    }

    public static HttpHost asHttpHost(URL url) {
        return new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
    }
}