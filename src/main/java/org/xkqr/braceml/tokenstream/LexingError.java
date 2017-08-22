package org.xkqr.braceml.tokenstream;

public class LexingError extends Exception {
    public LexingError(String msg) {
        super(msg);
    }
    public LexingError(Throwable t) {
        super(t);
    }
}
