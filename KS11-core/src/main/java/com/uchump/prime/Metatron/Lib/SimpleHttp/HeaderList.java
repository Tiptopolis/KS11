package com.uchump.prime.Metatron.Lib.SimpleHttp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Header;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Headers;

import static java.lang.String.format;

public final class HeaderList implements Headers {

    private final List<Header> headers;

    private HeaderList(List<Header> headers) {
        this.headers = new ArrayList<>(headers);
    }

    public static Headers headers(Header header, Header... headers) {
        return new HeaderList(Stream.concat(Stream.of(header), Arrays.stream(headers)).collect(Collectors.toList()));
    }

    @Override
    public Iterator<Header> iterator() {
        return headers.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeaderList headers1 = (HeaderList) o;
        return Objects.equals(headers, headers1.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers);
    }

    @Override
    public String toString() {
        return format("%s{headers=%s}", this.getClass().getSimpleName(), headers);
    }

    @Override
    public boolean has(String key) {
        return !get(key).equals(new NoHeader());
    }

    @Override
    public Header get(String key) {
        for (Header header : headers)
            if (header.name().equalsIgnoreCase(key))
                return header;
        return new NoHeader();
    }
}