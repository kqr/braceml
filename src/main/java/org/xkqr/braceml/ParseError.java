package org.xkqr.braceml;

class ParseError extends Exception {
    public ParseError(String msg) {
        super(msg);
    }
    public ParseError(Throwable t) {
        super(t);
    }
}
