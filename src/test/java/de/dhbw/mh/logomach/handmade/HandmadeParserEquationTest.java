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

class HandmadeParserEquationTest extends HandmadeParserUtils {
	
	private static AstNode DUMMY_LITERAL1 = new AstLiteral( new I8((byte)42) );
	
	
	private static Stream<Arguments> delegation_errorDetection() {
		return Stream.of(
				Arguments.of( "+",  "0:1:1" ),
				Arguments.of( "-",  "0:1:1" ),
				Arguments.of( "*",  "0:1:1" ),
				Arguments.of( "/",  "0:1:1" ),
				Arguments.of( "%",  "0:1:1" ),
				Arguments.of( "**", "0:2:2" ),
				Arguments.of( "(",  "0:1:1" ),
				Arguments.of( "<",  "0:1:1" ),
				Arguments.of( ">",  "0:1:1" ),
				Arguments.of( "<=", "0:2:2" ),
				Arguments.of( ">=", "0:2:2" ),
				Arguments.of( "12", "0:2:2" ),
				Arguments.of( "xy", "0:2:2" )
		);
	}
	
	private static Stream<Arguments> end_to_end_inputs() {
		return Stream.of(
				Arguments.of( "42",              "42i8" ),
				Arguments.of( "42 == 123",       "(42i8==123i8)" ),
				Arguments.of( "42 == 123 || 45", "(42i8==123i8)" ),
				Arguments.of( "42 == 123 && 45", "(42i8==123i8)" ),
				Arguments.of( "42 == 123 )",     "(42i8==123i8)" ),
				Arguments.of( "42 == 123 == 45", "((42i8==123i8)==45i8)" ),
				Arguments.of( "42",              "42i8" ),
				Arguments.of( "42 != 123",       "(42i8!=123i8)" ),
				Arguments.of( "42 != 123 || 45", "(42i8!=123i8)" ),
				Arguments.of( "42 != 123 && 45", "(42i8!=123i8)" ),
				Arguments.of( "42 != 123 )",     "(42i8!=123i8)" ),
				Arguments.of( "42 != 123 != 45", "((42i8!=123i8)!=45i8)" ),
				Arguments.of( "42 == 123 != 45", "((42i8==123i8)!=45i8)" ),
				Arguments.of( "42 != 123 == 45", "((42i8!=123i8)==45i8)" )
		);
	}
	
	
	
	
	@ParameterizedTest
	@MethodSource("end_to_end_inputs")
	void e2e_equation_is_leftAssociative( String input, String expectedTree ){
		HandmadeParserLL1 spy = createSpy( input );
		Mockito.doAnswer( parseNumericLiteral ).when( spy ).relationalExpression( );
		
		AstNode result = spy.equation( );
		assertThat( result.toString() ).isEqualTo( expectedTree );
	}
	
	@Test
	void equation_passesResultTo_equalities( ){
		String input = "";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relationalExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).equalities( any() );
		
		AstNode result = spy.equation( );
		assertThat( result ).isSameAs( DUMMY_AST );
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"=="})
	void equalities_delegatesTo_equalities1( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).equalities1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).equalities2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).equalities3( any() );
		
		spy.equalities( previousSubtree );
		verify( spy, times(1) ).equalities1(any());
		verify( spy, times(0) ).equalities2(any());
		verify( spy, times(0) ).equalities3(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"!="})
	void equalities_delegatesTo_equalities2( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).equalities1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).equalities2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).equalities3( any() );
		
		spy.equalities( previousSubtree );
		verify( spy, times(0) ).equalities1(any());
		verify( spy, times(1) ).equalities2(any());
		verify( spy, times(0) ).equalities3(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"&&", "||", "", ")"})
	void equalities_delegatesTo_equalities3( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).equalities1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).equalities2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).equalities3( any() );
		
		spy.equalities( previousSubtree );
		verify( spy, times(0) ).equalities1(any());
		verify( spy, times(0) ).equalities2(any());
		verify( spy, times(1) ).equalities3(any());
	}
	

	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void equalities_detectsSyntaxErrors( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.equalities( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'=='", "'!='", "'&&'", "'||'", "<EOF>", "')'" );
	}
	
	@ParameterizedTest( name = "mismatched input {0} at 0:0:0-{1}, expected ==" )
	@MethodSource("delegation_errorDetection")
	@CsvSource({ "!=,0:2:2" })
	void equalities1_rejectsOtherThan_EQUAL( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.equalities1( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'=='" );
	}
	
	@ParameterizedTest( name = "mismatched input {0} at 0:0:0-{1}, expected !=" )
	@MethodSource("delegation_errorDetection")
	@CsvSource({ "==,0:2:2" })
	void equalities2_rejectsOtherThan_UNEQUAL( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.equalities2( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'!='" );
	}
	
	@Test
	void equalities1_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = "==";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relationalExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).equalities( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.equalities1( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.COMPARE_EQUAL );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void equalities2_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = "!=";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relationalExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).equalities( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.equalities2( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.COMPARE_UNEQUAL );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void equalities3_returns_input_parameter( ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		String input = "the input is not relevant for this function";
		initializeParser( input );
		
		AstNode result = parser.equalities3( previousSubtree );
		assertThat( result ).isSameAs( previousSubtree );
	}
	
}
