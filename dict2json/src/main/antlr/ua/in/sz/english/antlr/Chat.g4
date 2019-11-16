grammar Chat;

@header {
package ua.in.sz.english.antlr;
}

// ====================================================================================================================
// Parser Rules
// ====================================================================================================================

chat : line+ EOF ;

line : name command color message NEWLINE ;

name : WORD WHITESPACE ;

command : (SAYS) ':' WHITESPACE ;

color : ( '/' WORD '/' )? ;

message : ( WORD | WHITESPACE)+ ;

// ====================================================================================================================
// Lexer Rules
// ====================================================================================================================

fragment A : ('A' | 'a') ;
fragment S : ('S' | 's') ;
fragment Y : ('Y' | 'y') ;

fragment LOWERCASE : [a-z] ;
fragment UPPERCASE : [A-Z] ;

SAYS : S A Y S ;

WORD : (LOWERCASE | UPPERCASE | '_')+ ;

WHITESPACE : (' ' | '\t')+ ;

NEWLINE : ('\r'? '\n' | '\r')+ ;

TEXT : ('[' | '(') ~[\])]+ (']' | ')') ;