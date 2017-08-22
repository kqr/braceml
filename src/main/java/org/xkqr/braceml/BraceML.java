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

    public static void main(String[] args) throws IOException {
        // TODO: parse
        
        Reader source = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[0]))));
        Lexer lexer = new Lexer(source);
        Token t;
        while (true) {
            t = lexer.next();
            System.out.println(t);
            if (t.is(Token.Type.EOF)) {
                break;
            }
        }
        
        //System.out.println("");

/*
        Renderer r = new Html();
        r.h().regular("Hello");
        Renderer p = r.paragraph();
        p.regular("World, I ");
        p.strong().regular("really");
        p.regular(" wish to greet ");
        p.href("127.0.0.1").regular("you");
        p.regular(" welcome");
        System.out.println(r.render());
*/
    }

}
