package de.dhbw.mh.slang.craft.parser;

import de.dhbw.mh.slang.ast.AstNode;
import de.dhbw.mh.slang.craft.Token;
import de.dhbw.mh.slang.craft.Token.Type;
import de.dhbw.mh.slang.craft.lexer.HandmadeLexer;

public abstract class AbstractParserLL1 {
	
	protected final HandmadeLexer LEXER;
	
	public AbstractParserLL1( HandmadeLexer lexer ){
		super( );
		LEXER = lexer;
	}
	
	
	
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * conditionalExpression : conditionalOrExpression ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	public AstNode conditionalExpression( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * conditionalOrExpression : conditionalAndExpression disjunction ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	public AstNode conditionalOrExpression( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * disjunction : '||' conditionalAndExpression disjunction ;
	 * disjunction : ;</pre> using delegation to specialized functions.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode disjunction( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * disjunction : '||' conditionalAndExpression disjunction ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode disjunction1( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * disjunction : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode disjunction2( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * conditionalAndExpression : equation conjunction ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	public AstNode conditionalAndExpression( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * conjunction : '&&' equation conjunction ;
	 * conjunction : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode conjunction( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * conjunction : '&&' equation conjunction ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode conjunction1( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * conjunction : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode conjunction2( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * equation : relationalExpression equalities ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	public AstNode equation( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * equalities : '==' relationalExpression equalities ;
	 * equalities : '!=' relationalExpression equalities ;
	 * equalities : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode equalities( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * equalities : '==' relationalExpression equalities ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode equalities1( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * equalities : '!=' relationalExpression equalities ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode equalities2( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * equalities : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode equalities3( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * relationalExpression : additiveExpression relations ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	public AstNode relationalExpression( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * relations : '<' additiveExpression relations ;
	 * relations : '>' additiveExpression relations ;
	 * relations : '<=' additiveExpression relations ;
	 * relations : '>=' additiveExpression relations ;
	 * relations : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode relations( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * relations : '<' additiveExpression relations ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode relations1( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * relations : '>' additiveExpression relations ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode relations2( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * relations : '<=' additiveExpression relations ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode relations3( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * relations : '>=' additiveExpression relations ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode relations4( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * relations : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode relations5( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * additiveExpression : multiplicativeExpression summand ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	public AstNode additiveExpression( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * summand : '+' multiplicativeExpression summand ;
	 * summand : '-' multiplicativeExpression summand ;
	 * summand : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode summand( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * summand : '+' multiplicativeExpression summand ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode summand1( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * summand : '-' multiplicativeExpression summand ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode summand2( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * summand : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode summand3( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * multiplicativeExpression : signedExpression factor ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	public AstNode multiplicativeExpression( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * factor : '*' signedExpression factor ;
	 * factor : '/' signedExpression factor ;
	 * factor : '%' signedExpression factor ;
	 * factor : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode factor( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * factor : '*' signedExpression factor ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode factor1( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * factor : '/' signedExpression factor ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode factor2( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * factor : '%' signedExpression factor ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode factor3( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rule
	 * <pre>
	 * factor : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode factor4( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * signedTerm : '+' exponentiation ;
	 * signedTerm : '-' exponentiation ;
	 * signedTerm : exponentiation ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	public AstNode signedTerm( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rule
	 * <pre>
	 * signedTerm : '+' exponentiation ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode signedTerm1( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rule
	 * <pre>
	 * signedTerm : '-' exponentiation ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode signedTerm2( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rule
	 * <pre>
	 * signedTerm : exponentiation ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode signedTerm3( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rule
	 * <pre>
	 * exponentiation : atomicExpression exponent ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	public AstNode exponentiation( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * exponent : '**' exponentiation ;
	 * exponent : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	public AstNode exponent( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rule
	 * <pre>
	 * exponent : '**' exponentiation ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode exponent1( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rule
	 * <pre>
	 * exponent : ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode exponent2( AstNode previous ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * atomicExpression : '(' additiveExpression ')' ;
	 * atomicExpression : IDENTIFIER ;
	 * atomicExpression : literal ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	public AstNode atomicExpression( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rule <pre>atomicExpression : '(' additiveExpression ')' ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode atomicExpression1( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rule <pre>atomicExpression : IDENTIFIER ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode atomicExpression2( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rule <pre>atomicExpression : literal ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode atomicExpression3( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rules
	 * <pre>
	 * literal : TRUE | FALSE ;
	 * literal : NUMERIC_LITERAL ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	public AstNode literal( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rule <pre>literal : TRUE | FALSE ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode literal1( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	/**
	 * Implementation of the grammar rule <pre>literal : NUMERIC_LITERAL ;</pre> using recursive descent.
	 * 
	 * @return a subtree of the AST corresponding to the given expression
	 */
	AstNode literal2( ){
		throw new RuntimeException( "not yet implemented" );
	}
	
	
	/**
	 * Advances to the next token in the input stream if the type of the current token matches the specified type.
	 * If the types do not match, an exception of the type ParseError is thrown.
	 * @param expectedType
	 */
	abstract public void match( Token.Type expectedType );

}
