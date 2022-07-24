package com.uchump.prime.Metatron.Lib.SimpleHttp;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.MessageContent;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Transform;


public final class FormParameters implements MessageContent {

    private final Map<String, String> parameters = new HashMap<>();

    public static FormParameters params(String... values) {
        return new FormParameters(values);
    }

    private FormParameters(String... values) {
        if (values.length % 2 != 0)
            throw new IllegalArgumentException("should be a even number of arguments, received" + values.length);
        toMap(values);
    }

    private void toMap(String[] values) {
        for (int i = 0; i < values.length; i += 2)
            parameters.put(values[i], values[i + 1]);
    }

    public <T> List<T> transform(Transform<Map.Entry<String, String>, T> transform) {
        List<T> pairs = new ArrayList<>();
        for (Map.Entry<String, String> parameter : parameters.entrySet())
            pairs.add(transform.call(parameter));
        return pairs;
    }

    @Override
    public String asString() {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = parameters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> parameter = iterator.next();
            builder.append(encode(parameter.getKey())).append("=").append(encode(parameter.getValue()));
            if (iterator.hasNext())
                builder.append("&");
        }
        return builder.toString();
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormParameters that = (FormParameters) o;
        return Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameters);
    }

    @Override
    public String toString() {
        return asString();
    }
}