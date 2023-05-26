grammar Slang;

exponentiation
  : atomicExpression '**' exponentiation
  | atomicExpression ;

atomicExpression
  : IDENTIFIER
  | NUMERIC_LITERAL ;

IDENTIFIER
  : [a-zA-Z][a-zA-Z0-9]* ;

NUMERIC_LITERAL
  : [0-9][.a-zA-Z0-9]* ;
