package com.uchump.prime.Metatron.Lib.SimpleHttp;

import static java.lang.String.format;
import static com.uchump.prime.Metatron.Lib.SimpleHttp.Url.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.uchump.prime.Metatron.Lib.SimpleHttp.A_I.Headers;



public class Link {

    private final Map<String, URL> links = new HashMap<>();

    public Link(Headers headers) {
        if (!headers.has("link"))
            return;
        StringTokenizer tokenizer = new StringTokenizer(headers.get("link").value(), ",");
        while (tokenizer.hasMoreElements()) {
            String relativeLink = (String) tokenizer.nextElement();
            String href = findHref(relativeLink);
            String relation = findRelation(relativeLink);
            links.put(relation, url(href));
        }
    }

    public URL next() {
        return links.get("next");
    }

    public URL previous() {
        if (!links.containsKey("prev"))
            return links.get("previous");
        return links.get("prev");
    }

    public URL first() {
        return links.get("first");
    }

    public URL last() {
        return links.get("last");
    }

    private String findRelation(String link) {
        try {
            return findRelationInLinkBetween(link, "rel=\"", "\"");
        } catch (Exception e) {
            return findRelationInLinkBetween(link, "rel='", "'");
        }
    }

    private String findRelationInLinkBetween(String link, String startDelimiter, String endDelimiter) {
        int start = link.indexOf(startDelimiter);
        int end = link.indexOf(endDelimiter, start + startDelimiter.length());
        if (start == -1 || end == -1)
            throw new LinkException(link);
        return link.substring(start + startDelimiter.length(), end).toLowerCase();
    }

    private String findHref(String link) {
        return link.substring(link.indexOf('<') + 1, link.indexOf(">;"));
    }

    private static class LinkException extends HttpException {
        public LinkException(String link) {
            super(format("failed to find a 'rel' within %s", link));
        }
    }
}