package org.xkqr.braceml;

public interface Renderer<Output> {
    public Output render();

    /*
     * Returns a renderer such that anything you render with it will end
     * up nested correctly in the final output.
     *
     */
    public Renderer<Output> h();
    /*
     * We could imagine having a "public void h(String verbatim)" version
     * of this method (and all like it) for convenience when manually
     * writing code to render a document. That's not a priority though.
     *
     */
    public Renderer<Output> hh();
    public Renderer<Output> hhh();
    public Renderer<Output> uli();
    public Renderer<Output> oli();
    public Renderer<Output> quote();
    public Renderer<Output> paragraph();
    public Renderer<Output> emph();
    public Renderer<Output> strong();
    public Renderer<Output> abbr();
    public Renderer<Output> dfn();
    public Renderer<Output> footnote();

    /* The url should not be rendered, but the link description may. */
    public Renderer<Output> href(String url);

    /* Verbatim content and regular symbols should not be rendered specially. */
    public void image(String alttext, String url);
    public void codeblock(String verbatim);
    public void code(String verbatim);
    public void regular(String verbatim);
    
}
