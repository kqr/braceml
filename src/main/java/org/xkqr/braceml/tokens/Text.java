package org.xkqr.braceml.tokens;

public class Text implements Token {
    private String contents;

    public Text(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "TEXT(" + this.contents + ")";
    }
}

