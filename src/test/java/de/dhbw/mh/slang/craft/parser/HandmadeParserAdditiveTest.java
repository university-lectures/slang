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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import de.dhbw.mh.slang.I8;
import de.dhbw.mh.slang.ast.AstBinaryOperation;
import de.dhbw.mh.slang.ast.AstLiteral;
import de.dhbw.mh.slang.ast.AstNode;
import de.dhbw.mh.slang.craft.CodeLoc;
import de.dhbw.mh.slang.craft.CodeLocation;

class HandmadeParserAdditiveTest extends HandmadeParserUtils {
	
	private static AstNode DUMMY_LITERAL1 = new AstLiteral( new I8((byte)42) );
	
	
	private static Stream<Arguments> delegation_errorDetection() {
		return Stream.of(
				Arguments.of( "*",  "0:1:1" ),
				Arguments.of( "/",  "0:1:1" ),
				Arguments.of( "%",  "0:1:1" ),
				Arguments.of( "**", "0:2:2" ),
				Arguments.of( "(",  "0:1:1" ),
				Arguments.of( "12", "0:2:2" ),
				Arguments.of( "xy", "0:2:2" )
		);
	}
	
	private static Stream<Arguments> end_to_end_inputs() {
		return Stream.of(
				Arguments.of( "42",              "42i8" ),
				Arguments.of( "42 + 123",       "(42i8+123i8)" ),
				Arguments.of( "42 + 123 && 45", "(42i8+123i8)" ),
				Arguments.of( "42 + 123 || 45", "(42i8+123i8)" ),
				Arguments.of( "42 + 123 == 45", "(42i8+123i8)" ),
				Arguments.of( "42 + 123 != 45", "(42i8+123i8)" ),
				Arguments.of( "42 + 123 <  45", "(42i8+123i8)" ),
				Arguments.of( "42 + 123 >  45", "(42i8+123i8)" ),
				Arguments.of( "42 + 123 <= 45", "(42i8+123i8)" ),
				Arguments.of( "42 + 123 >= 45", "(42i8+123i8)" ),
				Arguments.of( "42 + 123 )",     "(42i8+123i8)" ),
				Arguments.of( "42 + 123 + 45", "((42i8+123i8)+45i8)" ),
				Arguments.of( "42 - 123",       "(42i8-123i8)" ),
				Arguments.of( "42 - 123 && 45", "(42i8-123i8)" ),
				Arguments.of( "42 - 123 || 45", "(42i8-123i8)" ),
				Arguments.of( "42 - 123 == 45", "(42i8-123i8)" ),
				Arguments.of( "42 - 123 != 45", "(42i8-123i8)" ),
				Arguments.of( "42 - 123 <  45", "(42i8-123i8)" ),
				Arguments.of( "42 - 123 >  45", "(42i8-123i8)" ),
				Arguments.of( "42 - 123 <= 45", "(42i8-123i8)" ),
				Arguments.of( "42 - 123 >= 45", "(42i8-123i8)" ),
				Arguments.of( "42 - 123 )",     "(42i8-123i8)" ),
				Arguments.of( "42 - 123 -  45", "((42i8-123i8)-45i8)" ),
				Arguments.of( "42 + 123 -  45", "((42i8+123i8)-45i8)" ),
				Arguments.of( "42 - 123 +  45", "((42i8-123i8)+45i8)" )
		);
	}
	
	
	
	
	@ParameterizedTest
	@MethodSource("end_to_end_inputs")
	void e2e_additiveExpr_is_leftAssociative( String input, String expectedTree ){
		HandmadeParserLL1 spy = createSpy( input );
		Mockito.doAnswer( parseNumericLiteral ).when( spy ).multiplicativeExpression( );
		
		AstNode result = spy.additiveExpression( );
		assertThat( result.toString() ).isEqualTo( expectedTree );
	}
	
	@Test
	void additiveExpr_passesResultTo_summands( ){
		String input = "";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).multiplicativeExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).summand( any() );
		
		AstNode result = spy.additiveExpression( );
		assertThat( result ).isSameAs( DUMMY_AST );
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"+"})
	void summand_delegatesTo_summand1( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).summand1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).summand2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).summand3( any() );
		
		spy.summand( previousSubtree );
		verify( spy, times(1) ).summand1(any());
		verify( spy, times(0) ).summand2(any());
		verify( spy, times(0) ).summand3(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"-"})
	void summand_delegatesTo_summand2( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).summand1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).summand2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).summand3( any() );
		
		spy.summand( previousSubtree );
		verify( spy, times(0) ).summand1(any());
		verify( spy, times(1) ).summand2(any());
		verify( spy, times(0) ).summand3(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"<", ">", "<=", ">=", "==", "!=", "&&", "||", "", ")"})
	void summand_delegatesTo_summand3( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).summand1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).summand2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).summand3( any() );
		
		spy.summand( previousSubtree );
		verify( spy, times(0) ).summand1(any());
		verify( spy, times(0) ).summand2(any());
		verify( spy, times(1) ).summand3(any());
	}

	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void summand_detectsSyntaxErrors( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.summand( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'+'", "'-'", "'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&&'", "'||'", "<EOF>", "')'" );
	}
	
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	@CsvSource({ "-,0:1:1" })
	void summand1_rejectsOtherThan_PLUS( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.summand1( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'+'" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	@CsvSource({ "+,0:1:1" })
	void summand2_rejectsOtherThan_MINUS( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.summand2( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'-'" );
	}
	
	
	@Test
	void summand1_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = "+";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).multiplicativeExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).summand( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.summand1( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.ADD );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void summand2_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = "-";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).multiplicativeExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).summand( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.summand2( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.SUBTRACT );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void summand3_returns_input_parameter( ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		String input = "the input is not relevant for this function";
		initializeParser( input );
		
		AstNode result = parser.summand3( previousSubtree );
		assertThat( result ).isSameAs( previousSubtree );
	}
	
}
