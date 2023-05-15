package de.dhbw.mh.slang.craft.parser;

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
import de.dhbw.mh.slang.craft.CodeLoc;
import de.dhbw.mh.slang.craft.CodeLocation;

class HandmadeParserCondAndTest extends HandmadeParserUtils {
	
	private static AstNode DUMMY_LITERAL1 = new AstLiteral( new I8((byte)42) );
	
	
	private static Stream<Arguments> delegation_errorDetection() {
		return Stream.of(
				Arguments.of( "+",  "0:1:1" ),
				Arguments.of( "-",  "0:1:1" ),
				Arguments.of( "*",  "0:1:1" ),
				Arguments.of( "/",  "0:1:1" ),
				Arguments.of( "%",  "0:1:1" ),
				Arguments.of( "**", "0:2:2" ),
				Arguments.of( "==", "0:2:2" ),
				Arguments.of( "!=", "0:2:2" ),
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
				Arguments.of( "42 && 123",       "(42i8&&123i8)" ),
				Arguments.of( "42 && 123 || 45", "(42i8&&123i8)" ),
				Arguments.of( "42 && 123 )",     "(42i8&&123i8)" ),
				Arguments.of( "42 && 123 && 45", "((42i8&&123i8)&&45i8)" )
		);
	}
	
	
	
	
	@ParameterizedTest
	@MethodSource("end_to_end_inputs")
	void e2e_conditionalAnd_is_leftAssociative( String input, String expectedTree ){
		HandmadeParserLL1 spy = createSpy( input );
		Mockito.doAnswer( parseNumericLiteral ).when( spy ).equation( );
		
		AstNode result = spy.conditionalAndExpression( );
		assertThat( result.toString() ).isEqualTo( expectedTree );
	}
	
	@Test
	void conditionalAnd_passesResultTo_conjunction( ){
		String input = "";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).equation( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).conjunction( any() );
		
		AstNode result = spy.conditionalAndExpression( );
		assertThat( result ).isSameAs( DUMMY_AST );
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"&& 42"})
	void conjunction_delegatesTo_conjunction1( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).conjunction1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).conjunction2( any() );
		
		spy.conjunction( previousSubtree );
		verify( spy, times(1) ).conjunction1(any());
		verify( spy, times(0) ).conjunction2(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"||", "", " )"})
	void conjunction_delegatesTo_conjunction2( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).conjunction1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).conjunction2( any() );
		
		spy.conjunction( previousSubtree );
		verify( spy, times(0) ).conjunction1(any());
		verify( spy, times(1) ).conjunction2(any());
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void conjunction_detectsSyntaxErrors( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.conjunction( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'||'", "<EOF>", "')'" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void conjunction1_rejectsOtherThan_LAND( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.conjunction1( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'&&'" );
	}
	
	@Test
	void conjunction1_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = "&&";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).equation( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).conjunction( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.conjunction1( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.LOGICAL_AND );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void conjunction2_returns_input_parameter( ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		String input = "the input is not relevant for this function";
		initializeParser( input );
		
		AstNode result = parser.conjunction2( previousSubtree );
		assertThat( result ).isSameAs( previousSubtree );
	}
	
}
