package com.uchump.prime.Metatron.Lib.SimpleHttp;

import static java.lang.String.format;
import static com.uchump.prime.Metatron.Lib.SimpleHttp.CharacterSet.defaultCharacterSet;
import static com.uchump.prime.Metatron.Lib.SimpleHttp.EmptyHeaders.emptyHeaders;


import java.util.Objects;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Headers;
import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpPost;
import  com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.HttpRequestVisitor;


import static java.lang.String.format;

public final class FormUrlEncodedMessage implements HttpPost {

    private final Headers headers;
    private final FormParameters content;
    private final CharacterSet characterSet;

    public FormUrlEncodedMessage(FormParameters content) {
        this(content, emptyHeaders());
    }

    public FormUrlEncodedMessage(FormParameters content, Headers headers) {
        this(content, headers, defaultCharacterSet);
    }

    public FormUrlEncodedMessage(FormParameters content, CharacterSet characterSet) {
        this(content, emptyHeaders(), characterSet);
    }

    public FormUrlEncodedMessage(FormParameters content, Headers headers, CharacterSet characterSet) {
        this.headers = headers;
        this.content = content;
        this.characterSet = characterSet;
    }

    @Override
    public Headers getHeaders() {
        // Content-Type and Content-Length are already set in UrlParameters (at least for Apache)
        return headers;
    }

    @Override
    public FormParameters getContent() {
        return content;
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
        FormUrlEncodedMessage that = (FormUrlEncodedMessage) o;
        return Objects.equals(headers, that.headers) &&
            Objects.equals(content, that.content) &&
            characterSet == that.characterSet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers, content, characterSet);
    }

    @Override
    public String toString() {
        return format("%s{content='%s', headers='%s'characterSet='%s'}", this.getClass().getSimpleName(), content, headers, characterSet());
    }
}