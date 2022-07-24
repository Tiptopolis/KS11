package com.uchump.prime.Metatron.Lib.SimpleHttp;


import static java.lang.String.format;

import static com.uchump.prime.Metatron.Lib.SimpleHttp.EmptyHeaders.emptyHeaders;
import static com.uchump.prime.Metatron.Lib.SimpleHttp.CharacterSet.defaultCharacterSet;

import java.util.Objects;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Headers;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpPost;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpPut;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpRequestVisitor;

public final class UnencodedStringMessage implements HttpPut, HttpPost {

    private final String content;
    private final Headers headers;
    private final CharacterSet characterSet;

    public UnencodedStringMessage(String content) {
        this(content, emptyHeaders());
    }

    public UnencodedStringMessage(String content, Headers headers) {
        this(content, headers, defaultCharacterSet);
    }

    public UnencodedStringMessage(String content, CharacterSet characterSet) {
        this(content, emptyHeaders(), characterSet);
    }

    public UnencodedStringMessage(String content, Headers headers, CharacterSet characterSet) {
        this.content = content;
        this.headers = headers;
        this.characterSet = characterSet;
    }

    @Override
    public StringMessageContent getContent() {
        return new StringMessageContent(content);
    }

    @Override
    public Headers getHeaders() {
        return headers;
    }

    public String characterSet() {
        return characterSet.asString();
    }

    @Override
    public void accept(HttpRequestVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnencodedStringMessage that = (UnencodedStringMessage) o;
        return Objects.equals(content, that.content) &&
            Objects.equals(headers, that.headers) &&
            characterSet == that.characterSet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, headers, characterSet);
    }

    @Override
    public String toString() {
        return format("%s{content='%s', headers='%s'}", this.getClass().getSimpleName(), content, headers);
    }
}