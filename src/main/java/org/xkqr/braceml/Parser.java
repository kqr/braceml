package org.xkqr.braceml;

import org.xkqr.braceml.tokenstream.TokenStream;
import org.xkqr.braceml.tokenstream.LexingError;
import org.xkqr.braceml.documentbuilder.DocumentBuilder;


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
            case CODEBLOCK_OPEN:
            case IMG_OPEN:
            default:
                return paragraph(into.paragraph());
        }
    }

    private Token paragraph(DocumentBuilder<Format> into)
    throws LexingError, ParseError {
        Token leftover = inline(into);
        if (leftover.type() == Token.Type.PARABREAK) {
            return null;
        } else {
            return leftover;
        }
    }

    private Token inline(DocumentBuilder<Format> into)
    throws LexingError, ParseError {
        Token current = lexer.next();
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
            case CODE_OPEN:
            case HREF_OPEN:
            case REGULAR:
                into.regular(" ");
                into.regular(current.sourceRep());
                into.regular(" ");
                return null;
            case NEWLINE:
                into.regular("\n");
                return null;
            default:
                return current;
        }
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
