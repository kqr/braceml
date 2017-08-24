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

    public Lexer(String inputname, Reader source)
    throws IOException {
        this();
        this.source = source;
        this.inputname = inputname;
    }

    @Override
    public void close() throws IOException {
        this.source.close();
        this.source = null;
        this.line = 0;
        this.column = 0;
    }

    public Token next() throws LexingError {
        Token current;
        if (this.next != null) {
            /* We need this one-token buffer to be able to detect parabreaks */
            current = this.next;
            this.next = null;
        } else {
            try {
                current = fetch();
            } catch (IOException e) {
                throw new LexingError(e);
            }
        }


        if (current.type() == Token.Type.NEWLINE) {
            this.line++;
            this.column = 0;

            /* Get one more token, just to see what it is */
            Token following;
            try {
                following = fetch();
            } catch (IOException e) {
                throw new LexingError(e);
            }
            if (following.type() == Token.Type.NEWLINE) {
                /*
                 * If the last two tokens both have been newlines,
                 * chuck them out and replace them with a parabreak
                 *
                 */
                current = token(Token.Type.PARABREAK, "\n\n");
            } else {
                this.next = following;
            }
        }

        System.out.println("[LEXER] " + current);
        return current;
    }

    /* PRIVATE */

    private Reader source;
    private Token next;
    private StringBuilder scanned;
    private String inputname;
    private int line;
    private int column;
    private Map<String, Token> reserved;

    private Lexer() {
        this.scanned = new StringBuilder();
        this.next = null;
        this.line = 1;
        this.column = 0;

        this.reserved = new HashMap<String, Token>();
        for (Token token : Token.syntax()) {
            this.reserved.put(token.sourceRep(), token);
        }
        /* Further "reserved" sequences, mainly to simplify implementation */
        this.reserved.put("\n", token(Token.Type.NEWLINE, "\n"));
    }

    private Token fetch() throws IOException {
        int i;

        while ((i = this.source.read()) != -1) {
            char c = (char) i;

            this.column++;
            String before = scanned.toString();
            scanned.append(c);
            String after = scanned.toString();

            if (reserved(after) != null) {
                /*
                 * This character completes a reserved sequence: these
                 * always take priority, so terminate further processing
                 * immediately and flush scanned
                 *
                 */
                scanned = new StringBuilder();
                return evaluate(after);
            }

            /* Newlines always terminate all tokens */
            boolean terminates = (c == '\n');
            /* Non-whitespace always terminates whitespace tokens */
            terminates = !whitespace(c) && whitespace(before) || terminates;
            /* Whitespace tokens are terminated by non-whitespace */
            terminates = whitespace(c) && !whitespace(before) || terminates;
            /* Don't terminate if there's nothing to terminate */
            terminates = before.length() > 0 && terminates;

            if (terminates) {
                scanned = new StringBuilder().append(c);
                return evaluate(before);
            }
        }

        return token(Token.Type.EOF, "EOF");
    }

    private Token evaluate(String scanned) {
        Token token = reserved(scanned);
        if (token == null) {
            if (whitespace(scanned)) {
                token = token(Token.Type.WHITESPACE, scanned);
            } else {
                token = token(Token.Type.REGULAR, scanned);
            }
        }
        return token.found(inputname, this.line, this.column);
    }

    private Token reserved(String scanned) {
        return this.reserved.get(scanned);
    }

    private Token token(Token.Type type, String sourceRep) {
        return new Token(type, sourceRep)
            .found(inputname, this.line, this.column);
    }

    private boolean whitespace(String scanned) {
        return scanned.matches("^[\n\r\t ]+$");
    }

    private boolean whitespace(char c) {
        return c == '\n' || c == '\r' || c == '\t' || c == ' ';
    }


}
