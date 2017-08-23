package org.xkqr.braceml.documentbuilder;

import java.io.IOException;

import org.xkqr.util.LazyStringBuilder;

public class Html implements DocumentBuilder<LazyStringBuilder> {

    public Html() {
        this(null, new LazyStringBuilder());
    }

    // TODO: make it possible to "stream render", i.e. some sort of
    // functionality where once all child renderers are rendered,
    // the parent automatically renders? but well designed, and not
    // dumb like that idea...

    public LazyStringBuilder build() {
        this.close(currentOpenList);

        LazyStringBuilder rendered = new LazyStringBuilder();
        rendered.append(this.content);
        if (footer().length() > 0) {
            rendered.append("\n\n")
                .append("<footer>\n")
                .append(footer())
                .append("</footer>\n");
        }
        return rendered;
    }

    public Html h() {
        return heading(1);
    }
    public Html hh() {
        return heading(2);
    }
    public Html hhh() {
        return heading(3);
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
        Html n = node("p");
        return n;
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
        footnotes++;
        anchor("back-" + footnotes);
        href("#fn-" + footnotes).regular("[" + footnotes + "]");

        LazyStringBuilder text = new LazyStringBuilder();
        Html footnote = new Html(this, text);
        footnote.anchor("fn-" + footnotes);
        footnote.href("#back-" + footnotes);
        footer().append(text);
        return footnote;
    }

    public Html href(String url) {
        this.close(currentOpenList);
        LazyStringBuilder contents = new LazyStringBuilder();
        this.content.append("<a href=\"" + url + "\">")
            .append(contents)
            .append("</a>");
        return new Html(this, contents);
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

    private Html parent;
    private LazyStringBuilder content;
    private LazyStringBuilder footer;
    private OpenList currentOpenList;
    private int footnotes;
    private int sections;

    private Html(LazyStringBuilder content) {
        this(null, content);
    }

    private Html(Html parent, LazyStringBuilder content) {
        this.parent = parent;
        this.content = content;
        this.footer = new LazyStringBuilder();
        this.currentOpenList = OpenList.NONE;
        this.footnotes = 0;
        this.sections = 0;
    }

    private LazyStringBuilder footer() {
        Html root;
        for (root = this; root.parent != null; root = root.parent);
        return root.footer;
    }

    private void anchor(String name) {
        this.content.append("<a id=\"" + name + "\">").append("</a>");
    }

    private Html li(OpenList type) {
        this.open(type);
        return node("li");
    }

    private Html heading(int level) {
        sections++;
        this.content.append("\n\n");
        anchor("s-" + sections);
        Html inline = node("h" + level);
        this.content.append("\n");
        return inline;
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
        return new Html(this, contents);
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
