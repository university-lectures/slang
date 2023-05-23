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
import de.dhbw.mh.slang.craft.CodeLocation;
import de.dhbw.mh.slang.craft.Token;
import de.dhbw.mh.slang.craft.lexer.CraftedSlangLexer;
import de.dhbw.mh.slang.craft.lexer.NumericalEvaluator;

public class CraftedSlangParser extends AbstractParserLL1 {

	private static final String PARSER_ERROR_MESSAGE = "mismatched input '%s' at %s-%s, expected %s";

	public CraftedSlangParser(CraftedSlangLexer lexer) {
		super(lexer);
	}

	private static Set<Token.Type> setOf(Token.Type... types) {
		return Stream.of(types).collect(Collectors.toCollection(HashSet::new));
	}

	private static Set<Token.Type> setOf(Set<Token.Type> base, Token.Type... types) {
		Set<Token.Type> result;
		result = Stream.of(types).collect(Collectors.toCollection(HashSet::new));
		result.addAll(base);
		return result;
	}

	private static final class Selector {
		private static final Set<Token.Type> DISJUNCTION2 = setOf(EOF, RPAREN);
		private static final Set<Token.Type> DISJUNCTION = setOf(DISJUNCTION2, LOR);
		private static final Set<Token.Type> CONJUNCTION2 = setOf(DISJUNCTION);
		private static final Set<Token.Type> CONJUNCTION = setOf(CONJUNCTION2, LAND);
		private static final Set<Token.Type> EQUALITIES3 = setOf(CONJUNCTION);
		private static final Set<Token.Type> EQUALITIES = setOf(EQUALITIES3, EQUAL, NOT_EQUAL);
		private static final Set<Token.Type> RELATIONS5 = setOf(EQUALITIES);
		private static final Set<Token.Type> RELATIONS = setOf(RELATIONS5, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL);
		private static final Set<Token.Type> SUMMAND3 = setOf(RELATIONS);
		private static final Set<Token.Type> SUMMAND = setOf(SUMMAND3, PLUS, MINUS);
		private static final Set<Token.Type> FACTOR4 = setOf(SUMMAND);
		private static final Set<Token.Type> FACTOR = setOf(FACTOR4, ASTERISK, DIVIDE, MODULO);
		private static final Set<Token.Type> SIGNED_TERM3 = setOf(LPAREN, IDENTIFIER, NUMERIC_LITERAL);
		private static final Set<Token.Type> SIGNED_TERM = setOf(SIGNED_TERM3, PLUS, MINUS);
		private static final Set<Token.Type> EXPONENT2 = setOf(FACTOR);
		private static final Set<Token.Type> EXPONENT = setOf(EXPONENT2, POWER);
		private static final Set<Token.Type> ATOMICS = setOf(LPAREN, IDENTIFIER, NUMERIC_LITERAL);
		private static final Set<Token.Type> LITERAL1 = setOf(TRUE, FALSE);
		private static final Set<Token.Type> LITERAL = setOf(LITERAL1, NUMERIC_LITERAL);
	}

	/*
	 * ===========================================================
	 * conditionalExpression
	 * ===========================================================
	 */

	@Override
	public AstNode conditionalExpression() {
		// TODO Auto-generated method stub
		return super.conditionalExpression();
	}

	/*
	 * ===========================================================
	 * conditionalOrExpression
	 * ===========================================================
	 */

	@Override
	public AstNode conditionalOrExpression() {
		// TODO Auto-generated method stub
		return super.conditionalOrExpression();
	}

	@Override
	AstNode disjunction(AstNode previous) {
		// TODO Auto-generated method stub
		return super.disjunction(previous);
	}

	@Override
	AstNode disjunction1(AstNode previous) {
		// TODO Auto-generated method stub
		return super.disjunction1(previous);
	}

	@Override
	AstNode disjunction2(AstNode previous) {
		// TODO Auto-generated method stub
		return super.disjunction2(previous);
	}

	/*
	 * ===========================================================
	 * conditionalAndExpression
	 * ===========================================================
	 */

	@Override
	public AstNode conditionalAndExpression() {
		if (PLUS == LEXER.lookahead().TYPE || MINUS == LEXER.lookahead().TYPE || LPAREN == LEXER.lookahead().TYPE
				|| IDENTIFIER == LEXER.lookahead().TYPE || Selector.LITERAL.contains(LEXER.lookahead().TYPE)) {
			AstNode res = equation();
			return conjunction(res);

		}

		throw parsingException(Selector.LITERAL);

	}

	@Override
	AstNode conjunction(AstNode previous) {
		// Reject anything except LAND
		if (LEXER.lookahead().TYPE == LAND) {
			LEXER.advance();
			// && one literal or two ??
			return this.conjunction1(previous);
		} else if (LEXER.lookahead().TYPE == EOF || LEXER.lookahead().TYPE == RPAREN || LEXER.lookahead().TYPE == LOR) {
			return conjunction2(previous);
		} else {
			throw this.parsingException(Selector.CONJUNCTION);
		}
		// AstNode res=equation();
		// return conjunction1(res);

	}

	@Override
	AstNode conjunction1(AstNode previous) {
		if (LEXER.lookahead().TYPE != LAND) {
			throw this.parsingException(LAND);
		}else{
			LEXER.advance();
		}
	AstBinaryOberation abo = new AstBinaryOperation(this.LEXER.lookahead().BEGIN, previous, Operator.LOGICAL_AND,
				equation());		
		return conjunction(abo);
	}

	@Override
	AstNode conjunction2(AstNode previous) {
		return previous;
	}

	/*
	 * =========================================================== equation
	 * ===========================================================
	 */

	@Override
	public AstNode equation() {
		// TODO Auto-generated method stub
		return super.equation();
	}

	@Override
	AstNode equalities(AstNode previous) {
		// TODO Auto-generated method stub
		return super.equalities(previous);
	}

	@Override
	AstNode equalities1(AstNode previous) {
		// TODO Auto-generated method stub
		return super.equalities1(previous);
	}

	@Override
	AstNode equalities2(AstNode previous) {
		// TODO Auto-generated method stub
		return super.equalities2(previous);
	}

	@Override
	AstNode equalities3(AstNode previous) {
		// TODO Auto-generated method stub
		return super.equalities3(previous);
	}

	/*
	 * ===========================================================
	 * relationalExpression
	 * ===========================================================
	 */

	@Override
	public AstNode relationalExpression() {
		// TODO Auto-generated method stub
		return super.relationalExpression();
	}

	@Override
	public AstNode relations(AstNode previous) {
		// TODO Auto-generated method stub
		return super.relations(previous);
	}

	@Override
	AstNode relations1(AstNode previous) {
		// TODO Auto-generated method stub
		return super.relations1(previous);
	}

	@Override
	AstNode relations2(AstNode previous) {
		// TODO Auto-generated method stub
		return super.relations2(previous);
	}

	@Override
	AstNode relations3(AstNode previous) {
		// TODO Auto-generated method stub
		return super.relations3(previous);
	}

	@Override
	AstNode relations4(AstNode previous) {
		// TODO Auto-generated method stub
		return super.relations4(previous);
	}

	@Override
	AstNode relations5(AstNode previous) {
		// TODO Auto-generated method stub
		return super.relations5(previous);
	}

	/*
	 * ===========================================================
	 * additiveExpression
	 * ===========================================================
	 */

	@Override
	public AstNode additiveExpression() {
		// TODO Auto-generated method stub
		return super.additiveExpression();
	}

	@Override
	public AstNode summand(AstNode previous) {
		// TODO Auto-generated method stub
		return super.summand(previous);
	}

	@Override
	AstNode summand1(AstNode previous) {
		// TODO Auto-generated method stub
		return super.summand1(previous);
	}

	@Override
	AstNode summand2(AstNode previous) {
		// TODO Auto-generated method stub
		return super.summand2(previous);
	}

	@Override
	AstNode summand3(AstNode previous) {
		// TODO Auto-generated method stub
		return super.summand3(previous);
	}

	/*
	 * ===========================================================
	 * multiplicativeExpression
	 * ===========================================================
	 */

	@Override
	public AstNode multiplicativeExpression() {
		// TODO Auto-generated method stub
		return super.multiplicativeExpression();
	}

	@Override
	AstNode factor(AstNode previous) {
		// TODO Auto-generated method stub
		return super.factor(previous);
	}

	@Override
	AstNode factor1(AstNode previous) {
		// TODO Auto-generated method stub
		return super.factor1(previous);
	}

	@Override
	AstNode factor2(AstNode previous) {
		// TODO Auto-generated method stub
		return super.factor2(previous);
	}

	@Override
	AstNode factor3(AstNode previous) {
		// TODO Auto-generated method stub
		return super.factor3(previous);
	}

	@Override
	AstNode factor4(AstNode previous) {
		// TODO Auto-generated method stub
		return super.factor4(previous);
	}

	/*
	 * =========================================================== signedExpression
	 * ===========================================================
	 */

	@Override
	public AstNode signedTerm() {
		// TODO Auto-generated method stub
		return super.signedTerm();
	}

	@Override
	AstNode signedTerm1() {
		// TODO Auto-generated method stub
		return super.signedTerm1();
	}

	@Override
	AstNode signedTerm2() {
		// TODO Auto-generated method stub
		return super.signedTerm2();
	}

	@Override
	AstNode signedTerm3() {
		// TODO Auto-generated method stub
		return super.signedTerm3();
	}

	/*
	 * =========================================================== exponentiation
	 * ===========================================================
	 */

	@Override
	public AstNode exponentiation() {
		// TODO Auto-generated method stub
		return super.exponentiation();
	}

	@Override
	public AstNode exponent(AstNode previous) {
		// TODO Auto-generated method stub
		return super.exponent(previous);
	}

	@Override
	public AstNode exponent1(AstNode previous) {
		// TODO Auto-generated method stub
		return super.exponent1(previous);
	}

	@Override
	public AstNode exponent2(AstNode previous) {
		// TODO Auto-generated method stub
		return super.exponent2(previous);
	}

	/*
	 * =========================================================== atomicExpression
	 * ===========================================================
	 */

	@Override
	public AstNode atomicExpression() {
		// TODO Auto-generated method stub
		return super.atomicExpression();
	}

	@Override
	public AstNode atomicExpression1() {
		// TODO Auto-generated method stub
		return super.atomicExpression1();
	}

	@Override
	public AstNode atomicExpression2() {
		// TODO Auto-generated method stub
		return super.atomicExpression2();
	}

	@Override
	public AstNode atomicExpression3() {
		// TODO Auto-generated method stub
		return super.atomicExpression3();
	}

	/*
	 * =========================================================== literal
	 * ===========================================================
	 */

	@Override
	public AstNode literal() {
		if (Selector.LITERAL1.contains(LEXER.lookahead().TYPE)) {
			return literal1();
		}
		if (NUMERIC_LITERAL == LEXER.lookahead().TYPE) {
			return literal2();
		}
		throw parsingException(Selector.LITERAL);
	}

	@Override
	AstNode literal1() {
		Token token = LEXER.lookahead();
		if (TRUE == token.TYPE) {
			return new AstLiteral(new Bool(true));
		}
		if (FALSE == token.TYPE) {
			return new AstLiteral(new Bool(false));
		}
		throw parsingException(Selector.LITERAL1);
	}

	@Override
	AstNode literal2() {
		Token token = LEXER.lookahead();
		match(Token.Type.NUMERIC_LITERAL);
		NumericValue value = NumericalEvaluator.parse(token.LEXEME);
		return new AstLiteral(value);
	}

	/*
	 * =========================================================== helpers
	 * ===========================================================
	 */

	public void match(Token.Type expectedType) {
		if (LEXER.lookahead().TYPE != expectedType) {
			throw parsingException(expectedType);
		}
		LEXER.advance();
	}

	private RuntimeException parsingException(Set<Token.Type> selectionSet) {
		CodeLocation begin = LEXER.lookahead().BEGIN;
		CodeLocation end = LEXER.lookahead().END;
		String message = String.format(PARSER_ERROR_MESSAGE, LEXER.lookahead().LEXEME, begin, end, selectionSet);
		return new RuntimeException(message);
	}

	private RuntimeException parsingException(Token.Type selectionSet) {
		CodeLocation begin = LEXER.lookahead().BEGIN;
		CodeLocation end = LEXER.lookahead().END;
		String message = String.format(PARSER_ERROR_MESSAGE, LEXER.lookahead().LEXEME, begin, end,
				"{" + selectionSet + "}");
		return new RuntimeException(message);
	}

}
