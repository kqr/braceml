package org.xkqr.braceml;

class LexingError extends Exception {
    public LexingError(String msg) {
        super(msg);
    }
    public LexingError(Throwable t) {
        super(t);
    }
}
