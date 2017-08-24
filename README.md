
BraceML
=======

A convenient markup language for simple articles. No, I don't think markdown
is quite good enough.


Todo
----

1. Finish lexer

    1. [CHECK] Add all token types
    2. [CHECK] Complete implementation of more complex tokens
    3. [WIP] Create map of "reserved keywords"

2. Create parser

    1. [WIP] Recursive descent based on specification below.
    2. [WIP] Make calls to renderer.
    3. [ ] Escape HTML

3. Finish plumbing

    1. [ ] Read from stdin, barf rendered to stdout?

4. Document

    1. [ ] Write comments
    2. [ ] Make tests
    3. [ ] Blog articles?


### Concepts completely handled

* Heading 1
* Paragraphs
* Emphasis
* Regular text
* Heading 2
* Heading 3
* ULI
* OLI
* Blockquote
* Strong
* Abbreviation
* Definition
* Footnote
* Href
* Code block
* Inline code
* hr


### Concepts that aren't supported anywhere yet

* Images (suggested syntax: {! inline alt-text | url !} )
* sup and sub (are these really necessary?)



Possibilities
-------------

This way of dealing with writing blog posts opens up a lot of possibilities
that were previously unaccessible to me. Here are some:

* Anchors on every heading
* Table of contents
* Hovering over a footnote reveals it without a click?


Specification
-------------

The basic syntax is meant to be something along these lines. This
sketch has some bugs. To see how it actually works, reading the
code gives you accurate results.


    DOCUMENT     ::= BLOCK DOCUMENT | e
         "A document is a sequence of block elements"
  
    BLOCK        ::= BLOCKTAG | PARAGRAPH
         "If the block does not start with a block tag, it must be a paragraph"
  
  
    BLOCKTAG     ::= H | HH | HHH | ULI | OLI | CODEBLOCK | QUOTE | IMG
         "A block tag is one of these things (H = heading, ULI = UL + LI)"
  
    H            ::= {. INLINE .}
    HH           ::= {.. INLINE ..}
    HHH          ::= {... INLINE ...}
    ULI          ::= {- DOCUMENT -}
    OLI          ::= {# DOCUMENT #}
    CODEBLOCK    ::= {{ NEWLINE VERBATIM }}
    QUOTE        ::= {" DOCUMENT "}
    HR           ::= {_ INLINE _}
    IMG          ::= {! VERBATIM | HREF !}
  
  
    PARAGRAPH    ::= INLINE PARAEND
    PARAEND      ::= \n PARAEND | BLOCKTAG | e
  
          "A paragraph contains inline content and ends with either a
           number of newlines greater than 1, or a blocktag, or the end
           of the document."
  
  
    INLINE       ::= NEWLINE LINE | LINE
          "Inline content is either a single newline followed by a line,
           or just the line by itself."
  
    LINE         ::= TEXT INLINE | e
          "A line is text followed by more inline content."
  
    TEXT         ::= INLINETAG | REGULAR
          "Text is either an inline tag or regular text."
  
  
    INLINETAG    ::= EMPH | STRONG | ABBR | DFN | CODE | FOOTNOTE | HREF
  
    EMPH         ::= {/ INLINE /}
    STRONG       ::= {** INLINE **}
    ABBR         ::= {[ INLINE ]}
    DFN          ::= {= INLINE =}
    CODE         ::= {{ VERBATIM }}
    FOOTNOTE     ::= {^ DOCUMENT ^}
    HREF         ::= {@ INLINE | URL @}
  
    REGULAR      ::= anything
    VERBATIM     ::= anything other than }}
    URL          ::= anything other than @}


Example
-------

I'll put an example here while I wait for a better place to put it.

This is mostly meant for my own memory, and not public consumption.

    {. BraceML .}

    A convenient markup language {^ like {[ html ]} ^} for simple articles.

    Includes more than one paragraph! {^ We can even handle multiple
    paragraphs in these footnotes!

    Are you not impressed? ^}


    {.. Todo ..}

    {# Fix stuff

        {# Start by doing the thing #}

        {# Then try the other thing #} #}

    {# This will take two paragraphs.

       In no particular order you should

        {- Something else -}

        {- Yet more things -} #}


    The {{ inline code }} should be easy to put in.

    {{
    Also applies to code blocks!

    Which, importantly, must have nice html generated for them.
    }}

    If you want to quote {@ someone
    | http://en.wikipedia.org/wiki/Weasel_words @} and the "quote is too
    large" to do inline, you can always resort to

    {"
    Block quoting them across the page!!
    "}
    
    You may want to {= insert =} (tricky terminology gets <dfn>-ed!) images:

    {! Images should have an alt text | image.jpg !}
    
    At this point, {/ you /} may think I'm {** crazy **} but that's just
    different degrees of emphasis.
    
    
