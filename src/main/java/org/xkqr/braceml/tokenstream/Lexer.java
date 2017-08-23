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

        this.reserved = new HashMap<String, Token>();
        for (Token token : Token.syntax()) {
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
        Token current;
        if (this.next != null) {
            /* If we have a token buffered, use that */
            current = this.next;
            this.next = null;
        } else {
            /* If we have no token buffered, get a new one */
            try {
                current = fetch();
            } catch (IOException e) {
                throw new LexingError(e);
            }
        }


        if (current.type() == Token.Type.NEWLINE) {
            /* If the current token is a newline, increase counters */
            this.line++;
            this.column = 0;

            /* Get one more non-whitespace token, just to see what it is */
            Token following;
            try {
                while ((following = fetch()).type() == Token.Type.WHITESPACE);
            } catch (IOException e) {
                throw new LexingError(e);
            }
            if (following.type() == Token.Type.NEWLINE) {
                /*
                 * If the last two non-whitespace tokens both have been
                 * newlines, chuck them out and replace them with a parabreak
                 *
                 */
                current = token(Token.Type.PARABREAK, "\n\n");

            } else if (this.next != null) {
                // TODO: We are in a weird situation where "current" refers
                // to a newline, "following" refers to a non-syntax token,
                // and "this.next" refers to something follwing that.
                //
                // Since I don't have a good solution, I'm going to smash
                // together the sourcerep for "current" and "following" and
                // pretend that counts as "current".
                //
                // This breaks any grammar that depends on detecting a
                // leading newline following a non-syntax token, but that
                // should hopefully not be a problem. It is a better
                // solution than smashing together "following" and
                // "this.next" because that may lead to failures in
                // detecting paragraph breaks.

                current = token(
                    following.type(),
                    current.sourceRep() + following.sourceRep()
                );

            } else {
                this.next = following;
            }
        }

        System.out.println("[LEXER] " + current);
        return current;
    }

    private Token fetch() throws IOException {
        StringBuilder scanned = new StringBuilder();
        int i;

        while ((i = this.source.read()) != -1) {
            char c = (char) i;
            this.column++;

            Token delimiter = null;
            if (c == '\n') {
                delimiter = token(Token.Type.NEWLINE, "\n");
            } else if (c == '\t' || c == '\r' || c == ' ') {
                delimiter = token(Token.Type.WHITESPACE, "" + c);
            } else {
                scanned.append(c);
            }

            if (reserved(scanned.toString())) {
                return evaluate(scanned.toString());
            } else if (delimiter != null) {
                if (scanned.length() == 0) {
                    return delimiter;
                } else {
                    this.next = delimiter;
                    return evaluate(scanned.toString());
                }
            }
        }

        return token(Token.Type.EOF, "EOF");
    }

    private boolean reserved(String scanned) {
        return this.reserved.containsKey(scanned.toString());
    }

    private Token token(Token.Type type, String sourceRep) {
        return new Token(type, sourceRep)
            .found("input", this.line, this.column);
    }

    private Token evaluate(String scanned) {
        if (this.reserved.containsKey(scanned)) {
            return this.reserved.get(scanned)
                .found("input", this.line, this.column);
        } else {
            return token(Token.Type.REGULAR, scanned);
        }
    }

}
