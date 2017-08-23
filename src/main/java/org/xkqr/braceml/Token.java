package org.xkqr.braceml;

import java.util.ArrayList;
import java.util.List;
import java.util.Formatter;

public class Token {
    public enum Type {
        /* Single thing tokens */
        NEWLINE, WHITESPACE, PARABREAK, PIPE, EOF,

        /* Plain text tokens */
        REGULAR,

        /* Inline tag tokens */
        EMPH_OPEN, EMPH_CLOSE, STRONG_OPEN, STRONG_CLOSE,
        ABBR_OPEN, ABBR_CLOSE, DFN_OPEN, DFN_CLOSE,
        CODE_OPEN, CODE_CLOSE, FOOTNOTE_OPEN, FOOTNOTE_CLOSE,
        HREF_OPEN, HREF_CLOSE,

        /* Block tag tokens */
        H_OPEN, H_CLOSE, HH_OPEN, HH_CLOSE, HHH_OPEN, HHH_CLOSE,
        ULI_OPEN, ULI_CLOSE, OLI_OPEN, OLI_CLOSE,
        QUOTE_OPEN, QUOTE_CLOSE, IMG_OPEN, IMG_CLOSE;
    }

    public static List<Token> syntax() {
        List<Token> syntax = new ArrayList<Token>();
        syntax.add(new Token(Type.PIPE, "|"));
        syntax.add(new Token(Type.H_OPEN, "{."));
        syntax.add(new Token(Type.H_CLOSE, ".}"));
        syntax.add(new Token(Type.HH_OPEN, "{.."));
        syntax.add(new Token(Type.HH_CLOSE, "..}"));
        syntax.add(new Token(Type.HHH_OPEN, "{..."));
        syntax.add(new Token(Type.HHH_CLOSE, "...}"));
        syntax.add(new Token(Type.ULI_OPEN, "{-"));
        syntax.add(new Token(Type.ULI_CLOSE, "-}"));
        syntax.add(new Token(Type.OLI_OPEN, "{#"));
        syntax.add(new Token(Type.OLI_CLOSE, "#}"));
        syntax.add(new Token(Type.QUOTE_OPEN, "{\""));
        syntax.add(new Token(Type.QUOTE_CLOSE, "\"}"));
        syntax.add(new Token(Type.EMPH_OPEN, "{/"));
        syntax.add(new Token(Type.EMPH_CLOSE, "/}"));
        syntax.add(new Token(Type.STRONG_OPEN, "{**"));
        syntax.add(new Token(Type.STRONG_CLOSE, "**}"));
        syntax.add(new Token(Type.ABBR_OPEN, "{["));
        syntax.add(new Token(Type.ABBR_CLOSE, "]}"));
        syntax.add(new Token(Type.DFN_OPEN, "{="));
        syntax.add(new Token(Type.DFN_CLOSE, "=}"));
        syntax.add(new Token(Type.FOOTNOTE_OPEN, "{^"));
        syntax.add(new Token(Type.FOOTNOTE_CLOSE, "^}"));
        syntax.add(new Token(Type.HREF_OPEN, "{@"));
        syntax.add(new Token(Type.HREF_CLOSE, "@}"));
        syntax.add(new Token(Type.CODE_OPEN, "{{"));
        syntax.add(new Token(Type.CODE_CLOSE, "}}"));
        return syntax;
    }

    public Token(Type type, String sourceRep) {
        this.type = type;
        this.sourceRep = sourceRep;
        this.line = this.line;
        this.column = column;
    }

    public Token found(String filename, int line, int column) {
        this.filename = filename;
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

    @Override
    public String toString() {
        return new Formatter().format(
            "%s:%d:%d: %s(%s)",
            this.filename, this.line, this.column,
            this.type, this.sourceRep
        ).toString();
    }

    private Type type = null;
    private String sourceRep = null;
    private String filename = "";
    private int line = 0;
    private int column = 0;

}
