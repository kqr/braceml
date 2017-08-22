package org.xkqr.braceml;

import java.io.IOException;


public class Html implements Document<LazyStringBuilder> {

    public Html() {
        this.content = new LazyStringBuilder();
        this.footer = new LazyStringBuilder();
        this.currentOpenList = OpenList.NONE;
    }

    // TODO: make it possible to "stream render", i.e. some sort of
    // functionality where once all child renderers are rendered,
    // the parent automatically renders? but well designed, and not
    // dumb like that idea...

    public LazyStringBuilder build() {
        this.close(currentOpenList);
        return this.content.append(footer);
    }

    public Html h() {
        return node("h1");
    }
    public Html hh() {
        return node("h1");
    }
    public Html hhh() {
        return node("h3");
    }
    public Html uli() {
        return li(OpenList.UL);
    }
    public Html oli() {
        return li(OpenList.OL);
    }
    public Html quote() {
        return node("blockquote");
    }
    public Html paragraph() {
        return node("p");
    }
    public Html emph() {
        return node("em");
    }
    public Html strong() {
        return node("strong");
    }
    public Html abbr() {
        return node("abbr");
    }
    public Html dfn() {
        return node("dfn");
    }
    public Html footnote() {
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

    public Html href(String url) {
        this.close(currentOpenList);
        LazyStringBuilder contents = new LazyStringBuilder();
        this.content.append("<a href=\"" + url + "\">")
            .append(contents)
            .append("</a>");
        return new Html(contents);
    }

    public void image(String alttext, String url) {
    }
    public void codeblock(String verbatim) {
        Html renderer = node("pre");
        renderer.code(verbatim);
    }
    public void code(String verbatim) {
        Html renderer = node("code");
        renderer.regular(verbatim);
    }
    public void regular(String verbatim) {
        this.close(currentOpenList);
        // TODO: ESCAPE &<> CHARACTERS!!!
        this.content.append(verbatim);
    }

    /*================ PRIVATE ================*/

    
    private enum OpenList {UL, OL, NONE};

    private LazyStringBuilder content;
    private LazyStringBuilder footer;
    private OpenList currentOpenList;

    private Html(LazyStringBuilder content) {
        // TODO: call the other constructor?
        this.content = content;
        this.footer = new LazyStringBuilder();
        this.currentOpenList = OpenList.NONE;
    }

    private Html li(OpenList type) {
        this.open(type);
        return node("li");
    }

    private Html node(String name) {
        return node(name, this.content);
    }

    private Html node(String name, LazyStringBuilder into) {
        this.close(currentOpenList);
        LazyStringBuilder contents = new LazyStringBuilder();
        into.append("<" + name + ">")
            .append(contents)
            .append("</" + name + ">");
        return new Html(contents);
    }
    
    private void close(OpenList type) {
        switch (type) {
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
        currentOpenList = OpenList.NONE;
    }

    private void open(OpenList type) {
        if (currentOpenList != OpenList.NONE && currentOpenList != type) {
            close(currentOpenList);
        }
        switch (type) {
        case UL:
            content.append("<ul>");
            break;
        case OL:
            content.append("<ol>");
            break;
        default:
            // TODO: Handle this case. Should be logged but otherwise ignored.
        }
        currentOpenList = type;
    }
}
