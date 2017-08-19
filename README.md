
BraceML
=======

A markup language for my blog posts. The basic syntax is meant to be
something along these lines:


    DOCUMENT     ::= BLOCK DOCUMENT | e
         "A document is a sequence of block elements"
  
    BLOCK        ::= BLOCKTAG | PARAGRAPH
         "A block element starts with a block tag, xor is a paragraph"
  
  
    BLOCKTAG     ::= H | HH | HHH | ULI | OLI | CODEBLOCK | QUOTE | IMG
         "A block tag is either a heading with a level, a list item,
          a code block, a blockquote or an image"
  
    H            ::= {. INLINE .}
    HH           ::= {.. INLINE ..}
    HHH          ::= {... INLINE ...}
    ULI          ::= {- DOCUMENT -}
    OLI          ::= {# DOCUMENT #}
    CODEBLOCK    ::= {{ NEWLINE VERBATIM }}
    QUOTE        ::= {" DOCUMENT "}
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


Examples
--------

I'll put some examples here while I wait for a better idea of where to put
them. (Like a tests directory for the code, perhaps?)

These are mostly meant for my own memory, and not public consumption.

    {# a #}
    {# b 
      {# subnumber
        {- not a number -}#}
      {# subnumber #}#}
    {# c #}
    
    {- a -}
    {- b -}
    {- c
     cont -}
    
    {^ footnote text ^}
    
    {{ code }}
    
    {{
    pre code
    }}
    
    {"
    blockquote
    "}
    
    {@ title | href @}
    {! alt text | url !}
    
    {/ em /}
    
    {** strong **}
    
    {[ abbr ]}
    
    
    {. en huvudrubrik .}
    
    {.. underrubrik ..}
    
    {... tredje nivån ...}
