package com.uchump.prime.Metatron.Lib.SimpleHttp;

public enum CharacterSet {

    // character encodings
    UTF_8("UTF-8"),
    UTF_16("UTF-16"),
    // character sets
    US_ASCII("US-ASCII"),
    ASCII("ASCII"),
    ISO_8859_1("ISO-8859-1"),
    defaultCharacterSet(ISO_8859_1.characterSet);

    private final String characterSet;

    CharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }
    
    public String asString() {
        return characterSet;
    }

}