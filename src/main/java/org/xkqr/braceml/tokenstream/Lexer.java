package org.xkqr.braceml.tokenstream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.xkqr.braceml.Token;

public class Lexer implements TokenStream, AutoCloseable {
    private Reader source;
    private Token next;
    private int line;
    private int column;
    private Map<String, Token> reserved;

    public Lexer(Reader source) throws IOException {
        this.source = source;
        this.next = null;
        this.line = 1;
        this.column = 0;

        List<Token> predef = new ArrayList<Token>();
        predef.add(new Token(Token.Type.H_OPEN, "{."));
        predef.add(new Token(Token.Type.H_CLOSE, ".}"));
        predef.add(new Token(Token.Type.HH_OPEN, "{.."));
        predef.add(new Token(Token.Type.HH_CLOSE, "..}"));
        predef.add(new Token(Token.Type.HHH_OPEN, "{..."));
        predef.add(new Token(Token.Type.HHH_CLOSE, "...}"));
        predef.add(new Token(Token.Type.ULI_OPEN, "{-"));
        predef.add(new Token(Token.Type.ULI_CLOSE, "-}"));
        predef.add(new Token(Token.Type.OLI_OPEN, "{#"));
        predef.add(new Token(Token.Type.OLI_CLOSE, "#}"));
        predef.add(new Token(Token.Type.QUOTE_OPEN, "{\""));
        predef.add(new Token(Token.Type.QUOTE_CLOSE, "\"}"));
        predef.add(new Token(Token.Type.EMPH_OPEN, "{/"));
        predef.add(new Token(Token.Type.EMPH_CLOSE, "/}"));
        predef.add(new Token(Token.Type.STRONG_OPEN, "{**"));
        predef.add(new Token(Token.Type.STRONG_CLOSE, "**}"));
        predef.add(new Token(Token.Type.ABBR_OPEN, "{["));
        predef.add(new Token(Token.Type.ABBR_CLOSE, "]}"));
        predef.add(new Token(Token.Type.DFN_OPEN, "{="));
        predef.add(new Token(Token.Type.DFN_CLOSE, "=}"));
        predef.add(new Token(Token.Type.FOOTNOTE_OPEN, "{^"));
        predef.add(new Token(Token.Type.FOOTNOTE_CLOSE, "^}"));

        this.reserved = new HashMap<String, Token>();
        for (Token token : predef) {
            this.reserved.put(token.sourceRep(), token);
        }
    }

    @Override
    public void close() throws IOException {
        this.source.close();
        this.source = null;
        this.line = 0;
        this.column = 0;
    }

    public Token next() throws LexingError {
        if (this.next == null) {
            try {
                return fetch();
            } catch (IOException e) {
                throw new LexingError(e);
            }
        }

        Token current = this.next;
        this.next = null;

        if (current.type() == Token.Type.NEWLINE) {
            this.line++;
            this.column = 0;
            try {
                this.next = fetch();
            } catch (IOException e) {
                throw new LexingError(e);
            }
            if (this.next.type() == Token.Type.NEWLINE) {
                current = token(Token.Type.PARABREAK, "\\n\\n");
            }
        }

        return current;
    }

    private Token fetch() throws IOException {
        StringBuilder scanned = new StringBuilder();
        int i;

        while ((i = this.source.read()) != -1) {
            char c = (char) i;
            this.column++;
            if (c == '\n' || c == '\t' || c == '\r' || c == ' ') {
                if (scanned.length() == 0) {
                    if (c == '\n') {
                        return token(Token.Type.NEWLINE, "\\n");
                    }
                    continue;
                } else {
                    if (c == '\n') {
                        this.next = token(Token.Type.NEWLINE, "\\n");
                    }
                    return evaluate(scanned.toString());
                }
            }

            scanned.append(c);
            if (this.reserved.containsKey(scanned.toString())) {
                return evaluate(scanned.toString());
            }
        }

        return token(Token.Type.EOF, "EOF");
    }

    private Token token(Token.Type type, String sourceRep) {
        return new Token(type, sourceRep).found(this.line, this.column);
    }

    private Token evaluate(String scanned) {
        if (this.reserved.containsKey(scanned)) {
            return this.reserved.get(scanned).found(this.line, this.column);
        } else {
            return token(Token.Type.REGULAR, scanned);
        }
    }

}
