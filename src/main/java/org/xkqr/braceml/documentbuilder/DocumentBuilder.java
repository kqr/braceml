package org.xkqr.braceml.documentbuilder;

public interface DocumentBuilder<Format> {
    public Format build();

    /*
     * Returns a document builder such that anything you build
     * with it will end up nested correctly in the final output.
     *
     */
    public DocumentBuilder<Format> h();
    public DocumentBuilder<Format> hh();
    public DocumentBuilder<Format> hhh();
    public DocumentBuilder<Format> uli();
    public DocumentBuilder<Format> oli();
    public DocumentBuilder<Format> quote();
    public DocumentBuilder<Format> paragraph();
    public DocumentBuilder<Format> emph();
    public DocumentBuilder<Format> strong();
    public DocumentBuilder<Format> abbr();
    public DocumentBuilder<Format> dfn();
    public DocumentBuilder<Format> footnote();

    /* The url should not be rendered, but the link description may. */
    public DocumentBuilder<Format> href(String url);

    /* Verbatim content and regular symbols should not be rendered specially. */
    public void image(String alttext, String url);
    public void codeblock(String verbatim);
    public void code(String verbatim);
    public void regular(String verbatim);
    
}
