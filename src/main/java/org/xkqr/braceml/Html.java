package org.xkqr.braceml;

import java.io.IOException;


public class Html implements Renderer<LazyStringBuilder> {

    public Html() {
        this.content = new LazyStringBuilder();
        this.footer = new LazyStringBuilder();
        this.currentActive = ActiveTag.NONE;
    }

    public LazyStringBuilder render() {
        this.close(currentActive);
        return this.content.append(footer);
    }

    public Renderer<LazyStringBuilder> h() {
        return node("h1");
    }
    public Renderer<LazyStringBuilder> hh() {
        return node("h1");
    }
    public Renderer<LazyStringBuilder> hhh() {
        return node("h3");
    }
    public Renderer<LazyStringBuilder> uli() {
        return li(ActiveTag.UL);
    }
    public Renderer<LazyStringBuilder> oli() {
        return li(ActiveTag.OL);
    }
    public Renderer<LazyStringBuilder> quote() {
        return node("blockquote");
    }
    public Renderer<LazyStringBuilder> paragraph() {
        return node("p");
    }
    public Renderer<LazyStringBuilder> emph() {
        return node("em");
    }
    public Renderer<LazyStringBuilder> strong() {
        return node("strong");
    }
    public Renderer<LazyStringBuilder> abbr() {
        return node("abbr");
    }
    public Renderer<LazyStringBuilder> dfn() {
        return node("dfn");
    }
    public Renderer<LazyStringBuilder> footnote() {
        // TODO: implement
        // 1. sequencing for anchors
        // 2. make anchor to here
        // 3. make note (new Html())
        // 4. make anchor to note
        // 5. make ref to note
        // 6. make backref to here
        // 7. return note

        return this;
    }

    public Renderer<LazyStringBuilder> href(String url) {
        this.close(currentActive);
        LazyStringBuilder contents = new LazyStringBuilder();
        this.content.append("<a href=\"" + url + "\">")
            .append(contents)
            .append("</a>");
        return new Html(contents);
    }

    public void image(String alttext, String url) {
    }
    public void codeblock(String verbatim) {
        Renderer<LazyStringBuilder> renderer = node("pre");
        renderer.code(verbatim);
    }
    public void code(String verbatim) {
        Renderer<LazyStringBuilder> renderer = node("code");
        renderer.regular(verbatim);
    }
    public void regular(String verbatim) {
        this.close(currentActive);
        this.content.append(verbatim);
    }

    /*================ PRIVATE ================*/

    
    private enum ActiveTag {UL, OL, NONE};

    private LazyStringBuilder content;
    private LazyStringBuilder footer;
    private ActiveTag currentActive;

    private Html(LazyStringBuilder content) {
        // TODO: call the other constructor?
        this.content = content;
        this.footer = new LazyStringBuilder();
        this.currentActive = ActiveTag.NONE;
    }

    private Renderer<LazyStringBuilder> li(ActiveTag tag) {
        this.open(tag);
        return node("li");
    }

    private Renderer<LazyStringBuilder> node(String name) {
        return node(name, this.content);
    }

    private Renderer<LazyStringBuilder> node(String name, LazyStringBuilder into) {
        this.close(currentActive);
        LazyStringBuilder contents = new LazyStringBuilder();
        into.append("<" + name + ">")
            .append(contents)
            .append("</" + name + ">");
        return new Html(contents);
    }
    
    private void close(ActiveTag tag) {
        switch (tag) {
        case NONE:
            break;
        case UL:
            content.append("</ul>");
            break;
        case OL:
            content.append("</ol>");
            break;
        default:
            // TODO: Handle this case. Should be logged but otherwise ignored.
        }
        currentActive = ActiveTag.NONE;
    }

    private void open(ActiveTag tag) {
        if (currentActive != ActiveTag.NONE && currentActive != tag) {
            close(currentActive);
        }
        switch (tag) {
        case UL:
            content.append("<ul>");
            break;
        case OL:
            content.append("<ol>");
            break;
        default:
            // TODO: Handle this case. Should be logged but otherwise ignored.
        }
        currentActive = tag;
    }
}
