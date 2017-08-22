package org.xkqr.braceml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BraceML {

    public static void main(String[] args)
    throws IOException, LexingError, ParseError {
        Reader source = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[0]))));
        Parser<LazyStringBuilder> parser = new Parser<>(new Lexer(source), new Html());
        System.out.println(parser.parse());
    }


    private void lexerdump(Reader source)
    throws IOException, LexingError {
        Lexer lexer = new Lexer(source);
        Token t;
        while (true) {
            t = lexer.next();
            System.out.println(t);
            if (t.type() == Token.Type.EOF) {
                break;
            }
        }
    }

}
