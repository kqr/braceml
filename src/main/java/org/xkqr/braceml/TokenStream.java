package org.xkqr.braceml;

public interface TokenStream {
    public Token next() throws LexingError;
}
