package de.dhbw.mh.slang.craft.parser;

import static de.dhbw.mh.slang.craft.Token.Type.ASTERISK;
import static de.dhbw.mh.slang.craft.Token.Type.DIVIDE;
import static de.dhbw.mh.slang.craft.Token.Type.EOF;
import static de.dhbw.mh.slang.craft.Token.Type.EQUAL;
import static de.dhbw.mh.slang.craft.Token.Type.FALSE;
import static de.dhbw.mh.slang.craft.Token.Type.GREATER;
import static de.dhbw.mh.slang.craft.Token.Type.GREATER_EQUAL;
import static de.dhbw.mh.slang.craft.Token.Type.IDENTIFIER;
import static de.dhbw.mh.slang.craft.Token.Type.LAND;
import static de.dhbw.mh.slang.craft.Token.Type.LESS;
import static de.dhbw.mh.slang.craft.Token.Type.LESS_EQUAL;
import static de.dhbw.mh.slang.craft.Token.Type.LOR;
import static de.dhbw.mh.slang.craft.Token.Type.LPAREN;
import static de.dhbw.mh.slang.craft.Token.Type.MINUS;
import static de.dhbw.mh.slang.craft.Token.Type.MODULO;
import static de.dhbw.mh.slang.craft.Token.Type.NOT_EQUAL;
import static de.dhbw.mh.slang.craft.Token.Type.NUMERIC_LITERAL;
import static de.dhbw.mh.slang.craft.Token.Type.PLUS;
import static de.dhbw.mh.slang.craft.Token.Type.POWER;
import static de.dhbw.mh.slang.craft.Token.Type.RPAREN;
import static de.dhbw.mh.slang.craft.Token.Type.TRUE;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.dhbw.mh.slang.Bool;
import de.dhbw.mh.slang.NumericValue;
import de.dhbw.mh.slang.ast.AstBinaryOperation;
import de.dhbw.mh.slang.ast.AstBinaryOperation.Operator;
import de.dhbw.mh.slang.ast.AstLiteral;
import de.dhbw.mh.slang.ast.AstNode;
import de.dhbw.mh.slang.ast.AstUnaryOperation;
import de.dhbw.mh.slang.ast.AstVariable;
import de.dhbw.mh.slang.craft.CodeLocation;
import de.dhbw.mh.slang.craft.Token;
import de.dhbw.mh.slang.craft.Token.Type;
import de.dhbw.mh.slang.craft.lexer.CraftedSlangLexer;
import de.dhbw.mh.slang.craft.lexer.NumericalEvaluator;

public class CraftedSlangParser extends AbstractParserLL1 {

	private static final String PARSER_ERROR_MESSAGE = "mismatched input '%s' at %s-%s, expected %s";

	public CraftedSlangParser( CraftedSlangLexer lexer ){
		super( lexer );
	}


	private static Set<Token.Type> setOf( Token.Type... types ){
		return Stream.of( types ).collect( Collectors.toCollection(HashSet::new) );
	}

	private static Set<Token.Type> setOf( Set<Token.Type> base, Token.Type... types ){
		Set<Token.Type> result;
		result = Stream.of( types ).collect( Collectors.toCollection(HashSet::new) );
		result.addAll( base );
		return result;
	}

	private static final class Selector {
		private static final Set<Token.Type> DISJUNCTION2   = setOf( EOF, RPAREN );
		private static final Set<Token.Type> DISJUNCTION    = setOf( DISJUNCTION2, LOR );
		private static final Set<Token.Type> CONJUNCTION2   = setOf( DISJUNCTION );
		private static final Set<Token.Type> CONJUNCTION    = setOf( CONJUNCTION2, LAND );
		private static final Set<Token.Type> EQUALITIES3    = setOf( CONJUNCTION );
		private static final Set<Token.Type> EQUALITIES     = setOf( EQUALITIES3, EQUAL, NOT_EQUAL );
		private static final Set<Token.Type> RELATIONS5     = setOf( EQUALITIES );
		private static final Set<Token.Type> RELATIONS      = setOf( RELATIONS5, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL );
		private static final Set<Token.Type> SUMMAND3       = setOf( RELATIONS );
		private static final Set<Token.Type> SUMMAND        = setOf( SUMMAND3, PLUS, MINUS );
		private static final Set<Token.Type> FACTOR4        = setOf( SUMMAND );
		private static final Set<Token.Type> FACTOR         = setOf( FACTOR4, ASTERISK, DIVIDE, MODULO );
		private static final Set<Token.Type> SIGNED_TERM3   = setOf( LPAREN, IDENTIFIER, NUMERIC_LITERAL );
		private static final Set<Token.Type> SIGNED_TERM    = setOf( SIGNED_TERM3, PLUS, MINUS );
		private static final Set<Token.Type> EXPONENT2      = setOf( FACTOR );
		private static final Set<Token.Type> EXPONENT       = setOf( EXPONENT2, POWER );
		private static final Set<Token.Type> ATOMICS        = setOf( LPAREN, IDENTIFIER, NUMERIC_LITERAL );
		private static final Set<Token.Type> LITERAL1       = setOf( TRUE, FALSE );
		private static final Set<Token.Type> LITERAL        = setOf( LITERAL1, NUMERIC_LITERAL );
	}



	/*===========================================================
	 * conditionalExpression
	 *===========================================================*/

	@Override
	public AstNode conditionalExpression( ){
		return conditionalOrExpression();
	}

	/*===========================================================
	 * conditionalOrExpression
	 *===========================================================*/

	@Override
	public AstNode conditionalOrExpression( ){
		switch( LEXER.lookahead().TYPE ){
			case PLUS:
			case MINUS:
			case LPAREN:
			case IDENTIFIER:
			case NUMERIC_LITERAL:
				return this.disjunction( conditionalAndExpression() );
			default:
				Set<Token.Type> acceptedTypes = setOf( PLUS, MINUS, LPAREN, IDENTIFIER, NUMERIC_LITERAL );
				throw this.parsingException( acceptedTypes );
		}
	}

	@Override
	AstNode disjunction( AstNode previous ){
		switch( LEXER.lookahead().TYPE ){
			case LOR:
				return this.disjunction1( previous );
			case EOF:
			case RPAREN:
				return this.disjunction2( previous );
			default:
				throw this.parsingException( Selector.DISJUNCTION );
		}
	}

	@Override
	AstNode disjunction1( AstNode previous ){
		if( LEXER.lookahead().TYPE != LOR ){
			throw this.parsingException( LOR );
		} else {
			LEXER.advance();
		}
		AstNode abo = new AstBinaryOperation( this.LEXER.lookahead().BEGIN,
				previous, Operator.LOGICAL_OR, conditionalAndExpression() );
		return this.disjunction( abo );
	}

	@Override
	AstNode disjunction2( AstNode previous ){
		return previous;
	}

	/*===========================================================
	 * conditionalAndExpression
	 *===========================================================*/

	@Override
	public AstNode conditionalAndExpression( ){
		if( PLUS == LEXER.lookahead().TYPE || MINUS == LEXER.lookahead().TYPE || LPAREN == LEXER.lookahead().TYPE
				|| IDENTIFIER == LEXER.lookahead().TYPE || Selector.LITERAL.contains(LEXER.lookahead().TYPE) ){
			AstNode res = equation( );
			return conjunction( res );
		}
		throw parsingException( Selector.LITERAL );
	}

	@Override
	AstNode conjunction( AstNode previous ){
		// Reject anything except LAND
		if( LEXER.lookahead().TYPE == LAND ){
			return this.conjunction1( previous );
		}else if( LEXER.lookahead().TYPE == EOF || LEXER.lookahead().TYPE == RPAREN || LEXER.lookahead().TYPE == LOR ){
			return conjunction2( previous );
		}else{
			throw this.parsingException( Selector.CONJUNCTION );
		}
	}

	@Override
	AstNode conjunction1( AstNode previous ){
		if( LEXER.lookahead().TYPE != LAND ){
			throw this.parsingException( LAND );
		}else{
			LEXER.advance( );
		}
		AstBinaryOperation abo = new AstBinaryOperation( this.LEXER.lookahead().BEGIN,
				previous, Operator.LOGICAL_AND, equation() );
		return conjunction( abo );
	}

	@Override
	AstNode conjunction2( AstNode previous ){
		return previous;
	}

	/*===========================================================
	 * equation
	 *===========================================================*/

	@Override
	public AstNode equation( ){
		if( PLUS == LEXER.lookahead().TYPE ||
				MINUS == LEXER.lookahead().TYPE ||
				LPAREN == LEXER.lookahead().TYPE ||
				IDENTIFIER == LEXER.lookahead().TYPE ||
				NUMERIC_LITERAL == LEXER.lookahead().TYPE ){
			return equalities( relationalExpression() );
		}
		return equalities( relationalExpression() );
	}

	@Override
	AstNode equalities( AstNode previous ){
		if( EQUAL == LEXER.lookahead().TYPE ){
			return equalities1( previous );
		}
		if( NOT_EQUAL == LEXER.lookahead().TYPE ){
			return equalities2( previous );
		}
		if( Selector.EQUALITIES3.contains( LEXER.lookahead().TYPE ) ){
			return equalities3( previous );
		}
		throw parsingException( Selector.EQUALITIES );
	}

	@Override
	AstNode equalities1( AstNode previous ){
		match( Token.Type.EQUAL );
		AstNode next = relationalExpression( );
		AstNode result = new AstBinaryOperation( LEXER.lookahead().BEGIN,
				previous, AstBinaryOperation.Operator.COMPARE_EQUAL, next );
		return equalities( result );
	}

	@Override
	AstNode equalities2( AstNode previous ){
		match( Token.Type.NOT_EQUAL );
		AstNode next = relationalExpression( );
		AstNode result = new AstBinaryOperation( LEXER.lookahead().BEGIN,
				previous, AstBinaryOperation.Operator.COMPARE_UNEQUAL, next );
		return equalities( result );
	}

	@Override
	AstNode equalities3( AstNode previous ){
		return previous;
	}

	/*===========================================================
	 * relationalExpression
	 *===========================================================*/

	@Override
	public AstNode relationalExpression( ){
		Token.Type type = LEXER.lookahead().TYPE;
		if( NUMERIC_LITERAL == type
				|| IDENTIFIER == type
				|| PLUS == type
				|| MINUS == type
				|| LPAREN == type ){
			AstNode previous = additiveExpression( );
			return relations( previous );
		}else{
			throw new RuntimeException( );
		}
	}

	@Override
	public AstNode relations( AstNode previous ){
		switch( LEXER.lookahead().TYPE ){
			case LESS:
				return relations1( previous );
			case GREATER:
				return relations2( previous );
			case LESS_EQUAL:
				return relations3( previous );
			case GREATER_EQUAL:
				return relations4( previous );
			case EOF:
			case RPAREN:
			case EQUAL:
			case NOT_EQUAL:
			case LAND:
			case LOR:
				return relations5( previous );
			default:
				throw parsingException( Selector.RELATIONS );
		}
	}

	@Override
	AstNode relations1( AstNode previous ){
		match( Token.Type.LESS );
		AstNode diese = additiveExpression( );
		AstBinaryOperation middleAst = new AstBinaryOperation( null,
				previous, AstBinaryOperation.Operator.LESS_THAN, diese );
		return relations( middleAst );
	}

	@Override
	AstNode relations2( AstNode previous ){
		match( Token.Type.GREATER );
		AstNode diese = additiveExpression( );
		AstBinaryOperation middleAst = new AstBinaryOperation( null,
				previous, AstBinaryOperation.Operator.GREATER_THAN, diese );
		return relations( middleAst );
	}

	@Override
	AstNode relations3( AstNode previous ){
		match( Token.Type.LESS_EQUAL );
		AstNode diese = additiveExpression( );
		AstBinaryOperation middleAst = new AstBinaryOperation( null,
				previous, AstBinaryOperation.Operator.LESS_OR_EQUAL, diese );
		return relations( middleAst );
	}

	@Override
	AstNode relations4( AstNode previous ){
		match( Token.Type.GREATER_EQUAL );
		AstNode diese = additiveExpression( );
		AstBinaryOperation middleAst = new AstBinaryOperation( null,
				previous, AstBinaryOperation.Operator.GREATER_OR_EQUAL, diese );
		return relations( middleAst );
	}

	@Override
	AstNode relations5( AstNode previous ){
		return previous;
	}

	/*===========================================================
	 * additiveExpression
	 *===========================================================*/

	@Override
	public AstNode additiveExpression( ){
		Set<Type> selectionSet = setOf( PLUS, MINUS, LPAREN, IDENTIFIER, NUMERIC_LITERAL );
		if( selectionSet.contains( LEXER.lookahead().TYPE ) ){
			AstNode previous = multiplicativeExpression( );
			return summand( previous );
		}
		throw parsingException( Selector.SUMMAND );
	}

	@Override
	public AstNode summand( AstNode previous ){
		Token token = LEXER.lookahead( );
		if( Selector.SUMMAND3.contains( token.TYPE ) ){
			return summand3( previous );
		}
		if( PLUS == token.TYPE ){
			return summand1( previous );
		}
		if( MINUS == token.TYPE ){
			return summand2( previous );
		}
		throw parsingException( Selector.SUMMAND );
	}

	@Override
	AstNode summand1( AstNode previous ){
		if( PLUS == LEXER.lookahead().TYPE ){
			CodeLocation location = LEXER.lookahead().BEGIN;
			LEXER.advance( );
			AstNode next = multiplicativeExpression( );
			return summand( new AstBinaryOperation( location, previous, Operator.ADD, next ) );
		}
		throw parsingException( Selector.SUMMAND );
	}

	@Override
	AstNode summand2( AstNode previous ){
		if( MINUS == LEXER.lookahead().TYPE ){
			CodeLocation location = LEXER.lookahead().BEGIN;
			LEXER.advance( );
			AstNode next = multiplicativeExpression( );
			return summand( new AstBinaryOperation( location, previous, Operator.SUBTRACT, next ) );
		}
		throw parsingException( Selector.SUMMAND );
	}

	@Override
	AstNode summand3( AstNode previous ){
		if( Selector.SUMMAND3.contains( LEXER.lookahead().TYPE ) ){
			return previous;
		}
		throw parsingException( Selector.SUMMAND3 );
	}

	/*===========================================================
	 * multiplicativeExpression
	 *===========================================================*/

	@Override
	public AstNode multiplicativeExpression( ){
		switch( LEXER.lookahead().TYPE ){
			case PLUS:
			case MINUS:
			case LPAREN:
			case IDENTIFIER:
			case NUMERIC_LITERAL:
				return factor( signedTerm() );
			default: /* fall-through */
		}
		return factor( signedTerm() );
	}

	@Override
	AstNode factor( AstNode previous ){
		switch( LEXER.lookahead().TYPE ){
			case ASTERISK:
				return factor1( previous );
			case DIVIDE:
				return factor2( previous );
			case MODULO:
				return factor3( previous );
			default: /* fall-through */
		}
		if( Selector.FACTOR4.contains(LEXER.lookahead().TYPE) ){
			return factor4( previous );
		}
		throw parsingException( Selector.FACTOR );
	}

	@Override
	AstNode factor1( AstNode previous ){
		match( ASTERISK );
		AstNode next = signedTerm( );
		AstNode result = new AstBinaryOperation( LEXER.lookahead().BEGIN,
				previous, AstBinaryOperation.Operator.MULTIPLY, next );
		return factor( result );
	}

	@Override
	AstNode factor2( AstNode previous ){
		match( DIVIDE );
		AstNode next = signedTerm( );
		AstNode result = new AstBinaryOperation( LEXER.lookahead().BEGIN,
				previous, AstBinaryOperation.Operator.DIVIDE, next );
		return factor( result );
	}

	@Override
	AstNode factor3( AstNode previous ){
		match( MODULO );
		AstNode next = signedTerm( );
		AstNode result = new AstBinaryOperation( LEXER.lookahead().BEGIN,
				previous, AstBinaryOperation.Operator.MODULO, next );
		return factor( result );
	}

	@Override
	AstNode factor4( AstNode previous ){
		return previous;
	}
	
	
	/*===========================================================
	 * signedExpression
	 *===========================================================*/

	public AstNode signedTerm( ){
		if( PLUS == LEXER.lookahead().TYPE ){
			return signedTerm1( );
		}
		if( MINUS == LEXER.lookahead().TYPE ){
			return signedTerm2( );
		}
		if( Selector.SIGNED_TERM3.contains(LEXER.lookahead().TYPE) ){
			return signedTerm3( );
		}
		throw parsingException( Selector.SIGNED_TERM );
	}

	@Override
	AstNode signedTerm1( ){
		match( PLUS );
		return new AstUnaryOperation( LEXER.lookahead().BEGIN,
				AstUnaryOperation.Operator.POSITIVE_SIGN, exponentiation() );
	}

	@Override
	AstNode signedTerm2( ){
		match( MINUS );
		return new AstUnaryOperation( LEXER.lookahead().BEGIN,
				AstUnaryOperation.Operator.NEGATIVE_SIGN, exponentiation() );
	}

	@Override
	AstNode signedTerm3( ){
		return exponentiation( );
	}

	/*===========================================================
	 * exponentiation
	 *===========================================================*/

	// Initial Rules:
	// Exponentiation: atomicExpression "**" exponentiation
	// Exponentiation: eps

	// There is left-recursion, as the atomicExpression circles over many rules again to Exponentiation
	// This is noticeable in the select set

	// Rules without left recursion
	// Exponentiation: atomicExpression Exponent
	// Exponent: "**" exponentiation
	// Exponent: eps

	@Override
	public AstNode exponentiation( ){
		// Rule: Exponentiation: atomicExpression Exponent
		// Select Set: LPAREN, IDENTIFIER, NUMERIC_LITERAL, EOF
		if( LPAREN == LEXER.lookahead().TYPE
				|| IDENTIFIER == LEXER.lookahead().TYPE
				|| NUMERIC_LITERAL == LEXER.lookahead().TYPE
				|| EOF == LEXER.lookahead().TYPE ){
			return exponent( atomicExpression() );
		}
		Set<Token.Type> acceptedTypes = setOf( LPAREN, IDENTIFIER, NUMERIC_LITERAL, EOF );
		throw this.parsingException( acceptedTypes) ;
	}

	@Override
	public AstNode exponent( AstNode previous ){
		// This function decides which of the both rules for "exponent" should be applied
		// This is determined by the select set
		switch( LEXER.lookahead().TYPE ){
			// Rule: Exponent: "**" Exponentiation
			// Select Set: POWER
			case POWER:
				return exponent1( previous );
			// Rule: Exponent: eps
			// Select Set: +,-,*,/ ..
			case PLUS:
			case MINUS:
			case ASTERISK:
			case DIVIDE:
			case GREATER:
			case LESS:
			case GREATER_EQUAL:
			case LESS_EQUAL:
			case EQUAL:
			case NOT_EQUAL:
			case LAND:
			case LOR:
			case RPAREN:
			case MODULO:
			case EOF:
				return exponent2( previous );
			default:
				Set<Token.Type> acceptedTypes = setOf( POWER, PLUS, MINUS, ASTERISK, DIVIDE, GREATER, LESS,
						GREATER_EQUAL, LESS_EQUAL, EQUAL, NOT_EQUAL, LAND, LOR, RPAREN, MODULO, EOF );
				throw this.parsingException( acceptedTypes );
		}
	}

	@Override
	public AstNode exponent1( AstNode previous ){
		match( POWER );
		return new AstBinaryOperation( LEXER.lookahead().BEGIN,
				previous, AstBinaryOperation.Operator.POWER, exponentiation() );
	}

	@Override
	public AstNode exponent2( AstNode previous ){
		return previous;
	}

	/*===========================================================
	 * atomicExpression
	 *===========================================================*/

	@Override
	public AstNode atomicExpression( ){
		switch( this.LEXER.lookahead().TYPE ){
			case LPAREN:
				return this.atomicExpression1( );
			case IDENTIFIER:
				return this.atomicExpression2( );
			case NUMERIC_LITERAL:
			case TRUE:
			case FALSE:
				return this.atomicExpression3( );
			default:
				throw parsingException( Selector.ATOMICS );
		}
	}

	@Override
	public AstNode atomicExpression1( ){
		match( LPAREN );
		AstNode node = this.conditionalExpression( );
		match( RPAREN );
		return node;
	}

	@Override
	public AstNode atomicExpression2( ){
		if( this.LEXER.lookahead().TYPE != IDENTIFIER ){
			throw this.parsingException( IDENTIFIER );
		}
		AstVariable variable = new AstVariable( this.LEXER.lookahead().LEXEME );
		this.LEXER.advance( );
		return variable;
	}

	@Override
	public AstNode atomicExpression3( ){
		Set<Token.Type> acceptedTypes = setOf( NUMERIC_LITERAL, TRUE, FALSE );
		if( !acceptedTypes.contains( this.LEXER.lookahead().TYPE ) ){
			throw this.parsingException( acceptedTypes );
		}
		AstNode literal = this.literal( );
		return literal;
	}

	/*===========================================================
	 * literal
	 *===========================================================*/

	@Override
	public AstNode literal( ){
		if( Selector.LITERAL1.contains(LEXER.lookahead().TYPE) ){
			return literal1( );
		}
		if( NUMERIC_LITERAL == LEXER.lookahead().TYPE ){
			return literal2( );
		}
		throw parsingException( Selector.LITERAL );
	}

	@Override
	AstNode literal1( ){
		Token token = LEXER.lookahead( );
		if( TRUE == token.TYPE ){
			return new AstLiteral( new Bool(true) );
		}
		if( FALSE == token.TYPE ){
			return new AstLiteral( new Bool(false) );
		}
		throw parsingException( Selector.LITERAL1 );
	}

	@Override
	AstNode literal2( ){
		Token token = LEXER.lookahead( );
		match( Token.Type.NUMERIC_LITERAL );
		NumericValue value = NumericalEvaluator.parse( token.LEXEME );
		return new AstLiteral( value );
	}

	/*===========================================================
	 * helpers
	 *===========================================================*/

	public void match( Token.Type expectedType ){
		if( LEXER.lookahead().TYPE != expectedType ){
			throw parsingException( expectedType );
		}
		LEXER.advance( );
	}

	private RuntimeException parsingException( Set<Token.Type> selectionSet ){
		CodeLocation begin = LEXER.lookahead().BEGIN;
		CodeLocation end = LEXER.lookahead().END;
		String message = String.format( PARSER_ERROR_MESSAGE, LEXER.lookahead().LEXEME, begin, end, selectionSet );
		return new RuntimeException( message );
	}

	private RuntimeException parsingException( Token.Type selectionSet ){
		CodeLocation begin = LEXER.lookahead().BEGIN;
		CodeLocation end = LEXER.lookahead().END;
		String message = String.format( PARSER_ERROR_MESSAGE, LEXER.lookahead().LEXEME, begin, end,
				"{" + selectionSet + "}" );
		return new RuntimeException( message );
	}

}
