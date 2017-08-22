package org.xkqr.braceml;

import java.io.IOException;

public class Parser<Output> {

    private Lexer lexer;
    private Renderer<Output> renderer;

    public Parser(Lexer lexer, Renderer<Output> renderer) {
        this.lexer = lexer;
        this.renderer = renderer;
    }

    public Output document() throws IOException, ParseError {
        Token current = lexer.next();
        while ((current = block(current)) == null) {
            current = lexer.next();
        }
        if (current.type() != Token.Type.EOF) {
            throw new ParseError("Unexpected token " + current + ". Expected block element.");
        }
        return renderer.render();
    }

    private Token block(Token current) throws IOException, ParseError {
        switch (current.type()) {
            case H_OPEN:
                Token leftover = inline(renderer.h());
                expect(Token.Type.H_CLOSE, leftover);
                return null;
            case HH_OPEN:
            case HHH_OPEN:
            case ULI_OPEN:
            case OLI_OPEN:
            case CODEBLOCK_OPEN:
            case QUOTE_OPEN:
            case IMG_OPEN:
            default:
                return paragraph(renderer.paragraph());
        }
    }

    private Token paragraph(Renderer onto) throws IOException, ParseError {
        Token leftover = inline(onto);
        if (leftover.type() == Token.Type.PARABREAK) {
            return null;
        } else {
            return leftover;
        }
    }

    private Token inline(Renderer onto) throws IOException, ParseError {
        Token current = lexer.next();
        while ((current = line(current, onto)) == null) {
            current = lexer.next();
        }
        return current;
    }

    private Token line(Token current, Renderer onto) throws IOException, ParseError {
        switch (current.type()) {
            case EMPH_OPEN:
                Token leftover = inline(onto.emph());
                expect(Token.Type.EMPH_CLOSE, leftover);
                return null;
            case STRONG_OPEN:
            case ABBR_OPEN:
            case DFN_OPEN:
            case CODE_OPEN:
            case FN_OPEN:
            case HREF_OPEN:
            case REGULAR:
                onto.regular(" ");
                onto.regular(current.sourceRep());
                onto.regular(" ");
                return null;
            case NEWLINE:
                onto.regular("\n");
                return null;
            default:
                return current;
        }
    }
    
    private Token ignore(Token.Type type) throws IOException, ParseError {
        Token current = lexer.next();
        if (current.type() == Token.Type.NEWLINE) {
            current = lexer.next();
        }
        return current;
    }

    private void expect(Token.Type type, Token token) throws ParseError {
        if (token.type() == type) {
            return;
        } else {
            throw new ParseError("Unexpected token " + token + ". Expected " + type + ".");
        }
    }

}
