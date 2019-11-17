grammar Chat;

@header {
package ua.in.sz.english.antlr;
}

// ====================================================================================================================
// Parser Rules
// ====================================================================================================================

word_definition : line+ EOF ;

line : single_part_of_speech_word NEWLINE ;

single_part_of_speech_word : EN_WORD WHITESPACE TRANSCRIPTION WHITESPACE PART_OF_SPEECH WHITESPACE MEANING END;

// ====================================================================================================================
// Lexer Rules
// ====================================================================================================================
fragment DASH : '-';
fragment DOT : '.';
fragment NOUN : ('N' | 'n') ;
fragment VERB : ('V' | 'v') ;
fragment EN_LETTER : [a-zA-Z];
fragment RU_LETTER : [\u0400-\u04FF];

NEWLINE : ('\r'? '\n' | '\r')+ ;
WHITESPACE : (' ' | '\t')+ ;

PART_OF_SPEECH : ( NOUN | VERB ) ;

EN_WORD : ( EN_LETTER | DASH )+;
RU_WORD : ( RU_LETTER )+;

MEANING : ( RU_WORD )+ ( WHITESPACE RU_WORD )*;

TRANSCRIPTION : ( '[' ~[\]]+ ']' )+;

END : (DOT)+;