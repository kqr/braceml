package org.xkqr.util;

import java.util.ArrayList;
import java.util.List;



/*
 * TODO: This deserves an exposition. Example:
 *
 *        LazyStringBuilder builder = new LazyStringBuilder();
 *        LazyStringBuilder notyet = new LazyStringBuilder();
 *
 *        builder.append("Hello, ").append(notyet).append("!");
 *
 *        assert builder.toString().equals("Hello, !");
 *
 *        notyet.append("world");
 *
 *        assert builder.toString().equals("Hello, world!");
 *
 */

public class LazyStringBuilder implements CharSequence, Appendable {

    public LazyStringBuilder() {
        sources = new ArrayList<>();
    }

    public LazyStringBuilder(String contents) {
        this();
        this.append(contents);
    }

    @Override
    public LazyStringBuilder append(CharSequence csq) {
        sources.add(csq);
        return this;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        StringBuilder output = new StringBuilder();

        for (CharSequence source : sources) {
            if (output.length() >= end) {
                break;
            } else if (output.length() + source.length() >= end) {
                output.append(source.subSequence(0, end - output.length()));
                break;
            } else {
                output.append(source);
            }
        }

        return output.subSequence(start, end);
    }

    @Override
    public int length() {
        return sources.stream().mapToInt(CharSequence::length).sum();
    }


    /*
     * Boring paperwork overrides we need because apparently CharSequence
     * and Appendable do not have default methods even in Java 1.8!
     *
     */

    @Override
    public String toString() {
        return subSequence(0, this.length()).toString();
    }

    @Override
    public char charAt(int index) {
        return subSequence(0, index+1).charAt(index);
    }

    @Override
    public LazyStringBuilder append(char c) {
        this.append(Character.toString(c));
        return this;
    }
    
    @Override
    public LazyStringBuilder append(CharSequence csq, int start, int end) {
        if (csq != null) {
            this.append(csq.subSequence(start, end));
        }
        return this;
    }


    /* PRIVATE */

    private List<CharSequence> sources;

}
