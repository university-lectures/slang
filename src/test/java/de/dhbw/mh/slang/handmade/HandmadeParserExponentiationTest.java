package de.dhbw.mh.slang.handmade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import de.dhbw.mh.slang.I8;
import de.dhbw.mh.slang.ast.AstBinaryOperation;
import de.dhbw.mh.slang.ast.AstLiteral;
import de.dhbw.mh.slang.ast.AstNode;
import de.dhbw.mh.slang.handmade.AbstractParserLL1;
import de.dhbw.mh.slang.handmade.CodeLocation;
import de.dhbw.mh.slang.handmade.HandmadeParserLL1;

class HandmadeParserExponentiationTest extends HandmadeParserUtils {
	
	private static AstNode DUMMY_LITERAL1 = new AstLiteral( new I8((byte)42) );
	
	
	private static Stream<Arguments> delegation_errorDetection() {
		return Stream.of(
				Arguments.of( "(",  "0:1:1" ),
				Arguments.of( "12", "0:2:2" ),
				Arguments.of( "xy", "0:2:2"  )
		);
	}
	
	private static Stream<Arguments> end_to_end_inputs() {
		return Stream.of(
				Arguments.of( "42",              "42i8" ),
				Arguments.of( "42 ** 123",       "(42i8**123i8)" ),
				Arguments.of( "42 ** 123 && 45", "(42i8**123i8)" ),
				Arguments.of( "42 ** 123 || 45", "(42i8**123i8)" ),
				Arguments.of( "42 ** 123 == 45", "(42i8**123i8)" ),
				Arguments.of( "42 ** 123 != 45", "(42i8**123i8)" ),
				Arguments.of( "42 ** 123 <  45", "(42i8**123i8)" ),
				Arguments.of( "42 ** 123 >  45", "(42i8**123i8)" ),
				Arguments.of( "42 ** 123 <= 45", "(42i8**123i8)" ),
				Arguments.of( "42 ** 123 >= 45", "(42i8**123i8)" ),
				Arguments.of( "42 ** 123 )",     "(42i8**123i8)" ),
				Arguments.of( "42 ** 123 ** 45", "(42i8**(123i8**45i8))" )
		);
	}
	
	
	
	
	@ParameterizedTest
	@MethodSource("end_to_end_inputs")
	void e2e_exponentiation_is_RightAssociative( String input, String expectedTree ){
		HandmadeParserLL1 spy = createSpy( input );
		Mockito.doAnswer( parseNumericLiteral ).when( spy ).atomicExpression( );
		
		AstNode result = spy.exponentiation( );
		assertThat( result.toString() ).isEqualTo( expectedTree );
	}
	
	@Test
	void exponentiation_passesResultTo_exponent( ){
		String input = "";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).atomicExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).exponent( any() );
		
		AstNode result = spy.exponentiation( );
		assertThat( result ).isSameAs( DUMMY_AST );
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"**"})
	void exponent_delegatesTo_exponent1( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).exponent1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).exponent2( any() );
		
		spy.exponent( previousSubtree );
		verify( spy, times(1) ).exponent1(any());
		verify( spy, times(0) ).exponent2(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"+", "-", "<", ">", "<=", ">=", "==", "!=", "&&", "||", "", ")"})
	void exponent_delegatesTo_exponent2( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).exponent1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).exponent2( any() );
		
		spy.exponent( previousSubtree );
		verify( spy, times(0) ).exponent1(any());
		verify( spy, times(1) ).exponent2(any());
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void exponent_detectsSyntaxErrors( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.exponent( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'**'", "'*'", "'/'", "'%'", "'+'", "'-'", "'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&&'", "'||'", "<EOF>", "')'" );
	}
	
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void exponent1_rejectsOtherThan_POWER( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.exponent1( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'**'" );
	}
	
	@Test
	void exponent1_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = "**";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).exponentiation( );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.exponent1( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.POWER );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void exponent2_returns_input_parameter( ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		String input = "+";
		initializeParser( input );
		
		AstNode result = parser.exponent2( previousSubtree );
		assertThat( result ).isSameAs( previousSubtree );
	}
	
}
