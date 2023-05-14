package de.dhbw.mh.logomach.handmade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import de.dhbw.mh.logomach.I8;
import de.dhbw.mh.logomach.ast.AstBinaryOperation;
import de.dhbw.mh.logomach.ast.AstLiteral;
import de.dhbw.mh.logomach.ast.AstNode;

class HandmadeParserMultiplicativeTest extends HandmadeParserUtils {
	
	private static AstNode DUMMY_LITERAL1 = new AstLiteral( new I8((byte)42) );
	
	
	private static Stream<Arguments> delegation_errorDetection() {
		return Stream.of(
				Arguments.of( "**", "0:2:2" ),
				Arguments.of( "(",  "0:1:1" ),
				Arguments.of( "12", "0:2:2" ),
				Arguments.of( "xy", "0:2:2" )
		);
	}
	
	private static Stream<Arguments> end_to_end_inputs() {
		return Stream.of(
				Arguments.of( "42",              "42i8" ),
				Arguments.of( "42 * 123",       "(42i8*123i8)" ),
				Arguments.of( "42 * 123 && 45", "(42i8*123i8)" ),
				Arguments.of( "42 * 123 || 45", "(42i8*123i8)" ),
				Arguments.of( "42 * 123 == 45", "(42i8*123i8)" ),
				Arguments.of( "42 * 123 != 45", "(42i8*123i8)" ),
				Arguments.of( "42 * 123 <  45", "(42i8*123i8)" ),
				Arguments.of( "42 * 123 >  45", "(42i8*123i8)" ),
				Arguments.of( "42 * 123 <= 45", "(42i8*123i8)" ),
				Arguments.of( "42 * 123 >= 45", "(42i8*123i8)" ),
				Arguments.of( "42 * 123 +  45", "(42i8*123i8)" ),
				Arguments.of( "42 * 123 -  45", "(42i8*123i8)" ),
				Arguments.of( "42 * 123 )",     "(42i8*123i8)" ),
				Arguments.of( "42 * 123 * 45",  "((42i8*123i8)*45i8)" ),
				Arguments.of( "42 / 123",       "(42i8/123i8)" ),
				Arguments.of( "42 / 123 && 45", "(42i8/123i8)" ),
				Arguments.of( "42 / 123 || 45", "(42i8/123i8)" ),
				Arguments.of( "42 / 123 == 45", "(42i8/123i8)" ),
				Arguments.of( "42 / 123 != 45", "(42i8/123i8)" ),
				Arguments.of( "42 / 123 <  45", "(42i8/123i8)" ),
				Arguments.of( "42 / 123 >  45", "(42i8/123i8)" ),
				Arguments.of( "42 / 123 <= 45", "(42i8/123i8)" ),
				Arguments.of( "42 / 123 >= 45", "(42i8/123i8)" ),
				Arguments.of( "42 / 123 +  45", "(42i8/123i8)" ),
				Arguments.of( "42 / 123 -  45", "(42i8/123i8)" ),
				Arguments.of( "42 / 123 )",     "(42i8/123i8)" ),
				Arguments.of( "42 / 123 / 45",  "((42i8/123i8)/45i8)" ),
				Arguments.of( "42 * 123 /  45", "((42i8*123i8)/45i8)" ),
				Arguments.of( "42 * 123 %  45", "((42i8*123i8)%45i8)" ),
				Arguments.of( "42 / 123 *  45", "((42i8/123i8)*45i8)" ),
				Arguments.of( "42 / 123 % 45",  "((42i8/123i8)%45i8)" ),
				Arguments.of( "42 % 123 *  45", "((42i8%123i8)*45i8)" ),
				Arguments.of( "42 % 123 /  45", "((42i8%123i8)/45i8)" )
		);
	}
	
	
	
	
	@ParameterizedTest
	@MethodSource("end_to_end_inputs")
	void e2e_multiplicativeExpr_is_leftAssociative( String input, String expectedTree ){
		HandmadeParserLL1 spy = createSpy( input );
		Mockito.doAnswer( parseNumericLiteral ).when( spy ).signedTerm( );
		
		AstNode result = spy.multiplicativeExpression( );
		assertThat( result.toString() ).isEqualTo( expectedTree );
	}
	
	@Test
	void multiplicativeExpr_passesResultTo_factors( ){
		String input = "";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).factor( any() );
		
		AstNode result = spy.multiplicativeExpression( );
		assertThat( result ).isSameAs( DUMMY_AST );
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"*"})
	void factor_delegatesTo_factor1( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor3( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor4( any() );
		
		spy.factor( previousSubtree );
		verify( spy, times(1) ).factor1(any());
		verify( spy, times(0) ).factor2(any());
		verify( spy, times(0) ).factor3(any());
		verify( spy, times(0) ).factor4(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"/"})
	void factor_delegatesTo_factor2( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor3( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor4( any() );
		
		spy.factor( previousSubtree );
		verify( spy, times(0) ).factor1(any());
		verify( spy, times(1) ).factor2(any());
		verify( spy, times(0) ).factor3(any());
		verify( spy, times(0) ).factor4(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"%"})
	void factor_delegatesTo_factor3( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor3( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor4( any() );
		
		spy.factor( previousSubtree );
		verify( spy, times(0) ).factor1(any());
		verify( spy, times(0) ).factor2(any());
		verify( spy, times(1) ).factor3(any());
		verify( spy, times(0) ).factor4(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"+", "-", "<", ">", "<=", ">=", "==", "!=", "&&", "||", "", ")"})
	void factor_delegatesTo_factor4( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor3( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).factor4( any() );
		
		spy.factor( previousSubtree );
		verify( spy, times(0) ).factor1(any());
		verify( spy, times(0) ).factor2(any());
		verify( spy, times(0) ).factor3(any());
		verify( spy, times(1) ).factor4(any());
	}

	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void factor_detectsSyntaxErrors( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.factor( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'*'", "'/'", "'%'", "'+'", "'-'", "'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&&'", "'||'", "<EOF>", "')'" );
	}
	
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	@CsvSource({ "/,0:1:1", "%,0:1:1" })
	void factor1_rejectsOtherThan_ASTERISK( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.factor1( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'*'" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	@CsvSource({ "*,0:1:1", "%,0:1:1" })
	void factor2_rejectsOtherThan_SLASH( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.factor2( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'/'" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	@CsvSource({ "*,0:1:1", "/,0:1:1" })
	void factor3_rejectsOtherThan_PERCENT( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.factor3( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'%'" );
	}
	
	
	@Test
	void factor1_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = "*";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).factor( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.factor1( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.MULTIPLY );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void factor2_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = "/";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).factor( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.factor2( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.DIVIDE );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void factor3_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = "%";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).factor( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.factor3( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.MODULO );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void factor4_returns_input_parameter( ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		String input = "the input is not relevant for this function";
		initializeParser( input );
		
		AstNode result = parser.factor4( previousSubtree );
		assertThat( result ).isSameAs( previousSubtree );
	}
	
}
