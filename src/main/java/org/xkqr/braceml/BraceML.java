package org.xkqr.braceml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xkqr.braceml.tokens.Token;
import org.xkqr.braceml.tokens.EOF;


public class BraceML {
/*
 * GRAMMAR (terminals are tokens)
 * ==============================
 *   DOCUMENT     ::= SECTION DOCUMENT | e
 *   SECTION      ::= HEADING CONTENTS
 *   HEADING      ::= H | HH | HHH
 *   CONTENTS     ::= HEADING CONTENTS | BLOCK CONTENTS | e
 *   BLOCK        ::= PARAGRAPH pgbreak | openblock 
 *     PARAGRAPH  ::= INLINE PARAGRAPH | e
 *     ULI        ::=
 *     OLI        ::=
 *     CODEBLOCK  ::= openblock VERBATIM closeblock
 *     QUOTE      ::=
 *
 * TOKENS (whitespace ignored unless otherwise specified)
 * ======================================================
 *   pgbreak    = \n \n
 *   openblock  = {{ \n
 *   closeblock = \n }}
 
 * INLINE ::= EMPH | HREF | STRONG | ABBR | DFN | CODE | FOOTNOTE | REGULAR
 *
 ** EMPH       ::= {/ INLINE /}
 ** STRONG     ::= {** INLINE **}
 ** HREF       ::= {@ INLINE | URL @}
 ** CODE       ::= {{ VERBATIM }}
 ** ABBR       ::= {[ INLINE ]}
 ** DFN        ::= {= INLINE =}
 ** FOOTNOTE   ::= {^ DOCUMENT ^}
 ** 
 *
 * VERBATIM   ::= anything other than }}
 * URL        ::= anything other than @}
 *
 * REGULAR    ::= anything
 */

    public static void main(String[] args) throws IOException {
        System.out.println("Hello, world!");
        // TODO: lex
        // TODO: parse
        // TODO: generate
        Map<String, Token> reserved = new HashMap<>();
        
        Lexer lexer = new Lexer(new File(args[0]), reserved);
        Token t;
        while (true) {
            t = lexer.token();
            System.out.println(t);
        }
        
        //System.out.println("");
    }
    


}
