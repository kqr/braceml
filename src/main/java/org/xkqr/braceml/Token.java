package org.xkqr.braceml;

import java.util.Formatter;

public class Token {
    public enum Type {
        /* Single thing tokens */
        NEWLINE, PARABREAK, PIPE, EOF,

        /* Regular text-like tokens */
        REGULAR, HREF, VERBATIM,

        /* Inline tag tokens */
        EMPH_OPEN, EMPH_CLOSE, STRONG_OPEN, STRONG_CLOSE,
        ABBR_OPEN, ABBR_CLOSE, DFN_OPEN, DFN_CLOSE,
        CODE_OPEN, CODE_CLOSE, FN_OPEN, FN_CLOSE,
        HREF_OPEN, HREF_CLOSE,

        /* Block tag tokens */
        H_OPEN, H_CLOSE, HH_OPEN, HH_CLOSE, HHH_OPEN, HHH_CLOSE,
        ULI_OPEN, ULI_CLOSE, OLI_OPEN, OLI_CLOSE,
        CODEBLOCK_OPEN, CODEBLOCK_CLOSE, QUOTE_OPEN, QUOTE_CLOSE,
        IMG_OPEN, IMG_CLOSE;
    }

    private Type type = null;
    private String sourceRep = null;
    // TODO: Add file name as well?
    private int line = 0;
    private int column = 0;

    public Token(Type type, String sourceRep) {
        this.type = type;
        this.sourceRep = sourceRep;
        this.line = this.line;
        this.column = column;
    }

    public Token found(int line, int column) {
        this.line = line;
        this.column = column;
        return this;
    }

    public Type type() {
        return this.type;
    }

    public String sourceRep() {
        return this.sourceRep;
    }

    public int line() {
        return this.line;
    }

    public int column() {
        return this.column;
    }

    @Override
    public String toString() {
        return new Formatter().format(":%d:%d: %s(%s)", this.line, this.column, this.type, this.sourceRep).toString();
    }
}
