package org.xkqr.braceml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xkqr.braceml.tokens.*;

public class Lexer implements AutoCloseable {
    private Reader source;
    private Token next;
    private Map<String, Token> reserved;

    public Lexer(Reader source, Map<String, Token> reserved) throws IOException {
        this.source = source;
        this.reserved = reserved;
    }

    @Override
    public void close() throws IOException {
        this.source.close();
    }

    public Token token() throws IOException {
        StringBuilder scanned = new StringBuilder();
        int i;
        while ((i = source.read()) != -1) {
            char c = (char) i;
            if (c == '\n') {
                next = new Newline();
            }
            if (c == '\n' || c == '\r' || c == ' ' || c == '\t') {
                if (scanned.toString().equals("")) {
                    continue;
                } else {
                    return evaluate(scanned.toString());
                }
            } else {
                scanned.append(c);
                if (reserved.containsKey(scanned.toString())) {
                    return evaluate(scanned.toString());
                }
            }
        }
        return new EOF();
    }

    private Token evaluate(String scanned) {
        if (reserved.containsKey(scanned.toString())) {
            return reserved.get(scanned.toString());
        } else {
            return new Text(scanned);
        }
    }
}
