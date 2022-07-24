package com.uchump.prime.Metatron.Lib.SimpleHttp;
import java.util.Objects;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.MessageContent;

import static java.lang.String.format;

public final class StringMessageContent implements MessageContent {

    private final String content;

    public StringMessageContent(String content) {
        this.content = content;
    }

    @Override
    public String asString() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringMessageContent that = (StringMessageContent) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return format("StringMessageBody{content=%s}", content);
    }
}