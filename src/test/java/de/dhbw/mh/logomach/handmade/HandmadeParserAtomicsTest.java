package de.dhbw.mh.logomach.handmade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import de.dhbw.mh.logomach.ast.AstLiteral;
import de.dhbw.mh.logomach.ast.AstNode;
import de.dhbw.mh.logomach.ast.AstVariable;

class HandmadeParserAtomicsTest extends HandmadeParserUtils {
	
	
	private static Stream<Arguments> delegation_errorDetection() {
		return Stream.of(
				Arguments.of( "+",  "0:1:1" ),
				Arguments.of( "-",  "0:1:1" ),
				Arguments.of( "*",  "0:1:1" ),
				Arguments.of( "/",  "0:1:1" ),
				Arguments.of( "%",  "0:1:1" )
		);
	}
	
	private static Stream<Arguments> delegation_errorDetection1() {
		return Stream.of(
				Arguments.of( "+",  "0:1:1" ),
				Arguments.of( "-",  "0:1:1" ),
				Arguments.of( "12", "0:2:2" ),
				Arguments.of( "xy", "0:2:2" )
		);
	}
	
	private static Stream<Arguments> delegation_errorDetection2() {
		return Stream.of(
				Arguments.of( "( 42 +", "+", "0:5:5", "0:6:6" ),
				Arguments.of( "( 42",   "",  "0:4:4", "0:4:4" )
		);
	}
	
	
	
	@ParameterizedTest
	@ValueSource(strings = {"("})
	void atomicExpr_delegatesTo_atomicExpr1( String input ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).atomicExpression1( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).atomicExpression2( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).atomicExpression3( );
		
		spy.atomicExpression( );
		verify( spy, times(1) ).atomicExpression1( );
		verify( spy, times(0) ).atomicExpression2( );
		verify( spy, times(0) ).atomicExpression3( );
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"it"})
	void atomicExpr_delegatesTo_atomicExpr2( String input ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).atomicExpression1( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).atomicExpression2( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).atomicExpression3( );
		
		spy.atomicExpression( );
		verify( spy, times(0) ).atomicExpression1( );
		verify( spy, times(1) ).atomicExpression2( );
		verify( spy, times(0) ).atomicExpression3( );
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"74"})
	void atomicExpr_delegatesTo_atomicExpr3( String input ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).atomicExpression1( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).atomicExpression2( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).atomicExpression3( );
		
		spy.atomicExpression( );
		verify( spy, times(0) ).atomicExpression1( );
		verify( spy, times(0) ).atomicExpression2( );
		verify( spy, times(1) ).atomicExpression3( );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void atomicExpr_detectsSyntaxErrors( String input, @CodeLoc CodeLocation location ){
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.atomicExpression( );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'('", "<ID>", "<NUM_LIT>" );
	}
	
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection1")
	void atomicExpr1_rejectsOtherThan_LPAREN( String input, @CodeLoc CodeLocation location ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( parseNumericLiteral ).when( spy ).conditionalExpression( );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			spy.atomicExpression1( );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'('" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection2")
	void atomicExpr1_rejectsOtherThan_RPAREN( String input, String error, @CodeLoc CodeLocation begin, @CodeLoc CodeLocation end ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( parseNumericLiteral ).when( spy ).conditionalExpression( );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			spy.atomicExpression1( );
		}).withMessageStartingWith( "mismatched input '%s' at %s-%s, expected", error, begin, end )
		.withMessageContainingAll( "')'" );
	}
	
	@Test
	void atomicExpr1_calls_conditionalExpr( ){
		String input = "( 123 )";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( parseNumericLiteral ).when( spy ).conditionalExpression( );
		
		spy.atomicExpression1( );
		verify( spy, times(1) ).conditionalExpression( );
	}
	
	@Test
	void atomicExpr2_returns_AstVariable( ){
		String input = "xy";
		initializeParser( input );
		
		AstNode result = parser.atomicExpression2( );
		assertThat( result ).isInstanceOf( AstVariable.class );
	}
	
	@Test
	void atomicExpr3_returns_literal( ){
		String input = "42";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).literal( );
		
		AstLiteral result = (AstLiteral) spy.atomicExpression3( );
		assertThat( result ).isSameAs( DUMMY_AST );
	}
	
}
