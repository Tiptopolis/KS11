package com.uchump.prime.Metatron.Lib._HTTP._Jetty.websocket.api;

public class CloseStatus
{
    private static final int MAX_CONTROL_PAYLOAD = 125;
    public static final int MAX_REASON_PHRASE = MAX_CONTROL_PAYLOAD - 2;

    private final int code;
    private final String phrase;

    /**
     * Creates a reason for closing a web socket connection with the given code and reason phrase.
     *
     * @param closeCode the close code
     * @param reasonPhrase the reason phrase
     * @see StatusCode
     */
    public CloseStatus(int closeCode, String reasonPhrase)
    {
        this.code = closeCode;
        this.phrase = reasonPhrase;
        if (reasonPhrase.length() > MAX_REASON_PHRASE)
        {
            throw new IllegalArgumentException("Phrase exceeds maximum length of " + MAX_REASON_PHRASE);
        }
    }

    public int getCode()
    {
        return code;
    }

    public String getPhrase()
    {
        return phrase;
    }
}