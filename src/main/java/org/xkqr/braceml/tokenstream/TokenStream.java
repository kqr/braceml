package org.xkqr.braceml.tokenstream;

import org.xkqr.braceml.Token;

public interface TokenStream {
    public Token next() throws LexingError;
}
