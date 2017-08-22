package org.xkqr.braceml;

public interface Document<Format> {
    public Format build();

    /*
     * Returns a document builder such that anything you build
     * with it will end up nested correctly in the final output.
     *
     */
    public Document<Format> h();
    public Document<Format> hh();
    public Document<Format> hhh();
    public Document<Format> uli();
    public Document<Format> oli();
    public Document<Format> quote();
    public Document<Format> paragraph();
    public Document<Format> emph();
    public Document<Format> strong();
    public Document<Format> abbr();
    public Document<Format> dfn();
    public Document<Format> footnote();

    /* The url should not be rendered, but the link description may. */
    public Document<Format> href(String url);

    /* Verbatim content and regular symbols should not be rendered specially. */
    public void image(String alttext, String url);
    public void codeblock(String verbatim);
    public void code(String verbatim);
    public void regular(String verbatim);
    
}
