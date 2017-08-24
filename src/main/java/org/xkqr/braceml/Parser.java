package org.xkqr.braceml;

import org.xkqr.braceml.tokenstream.TokenStream;
import org.xkqr.braceml.tokenstream.LexingError;
import org.xkqr.braceml.documentbuilder.DocumentBuilder;
import org.xkqr.util.LazyStringBuilder;


public class Parser<Format> {

    public Parser(TokenStream lexer, DocumentBuilder<Format> document) {
        this.lexer = lexer;
        this.document = document;
    }

    public Format parse()
    throws LexingError, ParseError {
        Token leftover = document(document);
        if (leftover.type() != Token.Type.EOF) {
            throw new ParseError("Unexpected token " + leftover + ". Expected block element.");
        }
        return document.build();
    }

    private TokenStream lexer;
    private DocumentBuilder<Format> document;

    private Token document(DocumentBuilder<Format> into)
    throws LexingError, ParseError {
        Token current = lexer.next();
        while ((current = block(current, into)) == null) {
            current = lexer.next();
        }
        return current;
    }

    private Token block(Token current, DocumentBuilder<Format> into)
    throws LexingError, ParseError {
        Token leftover;
        switch (current.type()) {
            case NEWLINE:
            case WHITESPACE:
            case PARABREAK:
                return null;
            case H_OPEN:
                leftover = inline(into.h());
                expect(Token.Type.H_CLOSE, leftover);
                return null;
            case HH_OPEN:
                leftover = inline(into.hh());
                expect(Token.Type.HH_CLOSE, leftover);
                return null;
            case HHH_OPEN:
                leftover = inline(into.hhh());
                expect(Token.Type.HHH_CLOSE, leftover);
                return null;
            case HR_OPEN:
                leftover = inline(into.hr());
                expect(Token.Type.HR_CLOSE, leftover);
                return null;
            case ULI_OPEN:
                leftover = document(into.uli());
                expect(Token.Type.ULI_CLOSE, leftover);
                return null;
            case OLI_OPEN:
                leftover = document(into.oli());
                expect(Token.Type.OLI_CLOSE, leftover);
                return null;
            case QUOTE_OPEN:
                leftover = document(into.quote());
                expect(Token.Type.QUOTE_CLOSE, leftover);
                return null;
            case CODE_OPEN:
                return code(into, true);
            case IMG_OPEN:
            default:
                return paragraph(current, into.paragraph());
        }
    }

    private Token paragraph(Token current, DocumentBuilder<Format> into)
    throws LexingError, ParseError {
        Token leftover = inline(current, into);
        if (leftover.type() == Token.Type.PARABREAK) {
            return null;
        } else {
            return leftover;
        }
    }

    private Token inline(DocumentBuilder<Format> into)
    throws LexingError, ParseError {
        return inline(lexer.next(), into);
    }

    private Token inline(Token current, DocumentBuilder<Format> into)
    throws LexingError, ParseError {
        while ((current = line(current, into)) == null) {
            current = lexer.next();
        }
        return current;
    }

    private Token line(Token current, DocumentBuilder<Format> into)
    throws LexingError, ParseError {
        Token leftover;
        switch (current.type()) {
            case EMPH_OPEN:
                leftover = inline(into.emph());
                expect(Token.Type.EMPH_CLOSE, leftover);
                return null;
            case STRONG_OPEN:
                leftover = inline(into.strong());
                expect(Token.Type.STRONG_CLOSE, leftover);
                return null;
            case ABBR_OPEN:
                leftover = inline(into.abbr());
                expect(Token.Type.ABBR_CLOSE, leftover);
                return null;
            case DFN_OPEN:
                leftover = inline(into.dfn());
                expect(Token.Type.DFN_CLOSE, leftover);
                return null;
            case FOOTNOTE_OPEN:
                leftover = document(into.footnote());
                expect(Token.Type.FOOTNOTE_CLOSE, leftover);
                return null;
            case HREF_OPEN:
                LazyStringBuilder url = new LazyStringBuilder();
                leftover = inline(into.href(url));
                expect(Token.Type.PIPE, leftover);
                verbatim(url, Token.Type.HREF_CLOSE);
                return null;
            case CODE_OPEN:
                return code(into, false);
            case NEWLINE:
            case WHITESPACE:
            case REGULAR:
                into.regular(current.sourceRep());
                return null;
            default:
                return current;
        }
    }

    private Token code(DocumentBuilder<Format> into, boolean block)
    throws LexingError, ParseError {
        LazyStringBuilder code = new LazyStringBuilder();
        if (block) {
            into.codeblock(code);
        } else {
            into.code(code);
        }
        Token leftover = verbatim(code, Token.Type.CODE_CLOSE);
        expect(Token.Type.CODE_CLOSE, leftover);
        return null;
    }

    private Token verbatim(LazyStringBuilder text, Token.Type terminator)
    throws LexingError {
        StringBuilder whitespace;
        Token current = lexer.next();
        
        /* Ignores any leading empty lines before the content... */

        /* 
         * This is slightly tricky because if it's an inline thing,
         * then we _do_ want to ignore leading whitespace on the
         * the first line, and otherwise we don't.
         *
         * This algorithm works by signaling "newline discovered"
         * through setting whitespace away from null.
         *
         */
        whitespace = null;
        while (whitespace(current)) {
            if (newline(current)) {
                whitespace = new StringBuilder();
            } else if (whitespace != null) {
                whitespace.append(current.sourceRep());
            }
            current = lexer.next();
        }
        /* ...but preserves leading whitespace on the first line of content */
        if (whitespace != null) {
            text.append(whitespace);
        }

        whitespace = new StringBuilder();
        while (current.type() != terminator) {
            /* Ignore any trailing whitespace after the last content... */
            if (whitespace(current)) {
                whitespace.append(current.sourceRep());
            } else {
                /* But preserve any other whitespace */
                text.append(whitespace);
                whitespace = new StringBuilder();
                text.append(current.sourceRep());
            }

            current = lexer.next();
        }
        return current;
    }

    private boolean newline(Token token) {
        return token.type() == Token.Type.NEWLINE
            || token.type() == Token.Type.PARABREAK;
    }

    private boolean whitespace(Token token) {
        return newline(token) || token.type() == Token.Type.WHITESPACE;
    }
    
    private void expect(Token.Type type, Token token)
    throws ParseError {
        if (token.type() == type) {
            return;
        } else {
            throw new ParseError("Unexpected token " + token + ". Expected " + type + ".");
        }
    }

}
