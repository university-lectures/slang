grammar Slang;

logicalOrExpression
  : logicalAndExpression ( LOR logicalAndExpression )*
  ;

logicalAndExpression
  : equalityExpression ( LAND equalityExpression )*
  ;

equalityExpression
  : relationalExpression (( EQ | NE ) relationalExpression )*
  ;

relationalExpression
  : additiveExpression (( LT | GT | LE | GE ) additiveExpression )*
  ;

additiveExpression
  : multiplicativeExpression (( PLUS | MINUS ) multiplicativeExpression )*
  ;

multiplicativeExpression
  : signedTerm (( MUL | DIV | MOD ) signedTerm )*
  ;

signedTerm
  : ( PLUS | MINUS )? exponentiation
  ;

exponentiation
  : atomicExpression POW exponentiation
  | atomicExpression
  ;

atomicExpression
  : IDENTIFIER
  | NUMERIC_LITERAL
  | LPAREN logicalOrExpression RPAREN
  ;


LOR    : '||' ;
LAND   : '&&' ;
EQ     : '==' ;
NE     : '!=' ;
LT     : '<' ;
GT     : '>' ;
LE     : '<=' ;
GE     : '>=' ;
PLUS   : '+' ;
MINUS  : '-' ;
MUL    : '*' ;
DIV    : '/' ;
MOD    : '%' ;
POW    : '**' ;
LPAREN : '(' ;
RPAREN : ')' ;

IDENTIFIER
  : [a-zA-Z][a-zA-Z0-9]*
  ;

NUMERIC_LITERAL
  : [0-9][.a-zA-Z0-9]*
  ;

WHITESPACE
  : [\p{Zs}] -> channel(HIDDEN)
  ;

NEWLINE
  : ('\r\n' | [\r\n]) -> channel(HIDDEN)
  ;
