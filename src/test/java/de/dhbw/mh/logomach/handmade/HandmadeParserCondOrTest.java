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
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import de.dhbw.mh.logomach.I8;
import de.dhbw.mh.logomach.ast.AstBinaryOperation;
import de.dhbw.mh.logomach.ast.AstLiteral;
import de.dhbw.mh.logomach.ast.AstNode;

class HandmadeParserCondOrTest extends HandmadeParserUtils {
	
	private static AstNode DUMMY_LITERAL1 = new AstLiteral( new I8((byte)42) );
	
	
	private static Stream<Arguments> delegation_errorDetection() {
		return Stream.of(
				Arguments.of( "+",  "0:1:1" ),
				Arguments.of( "-",  "0:1:1" ),
				Arguments.of( "*",  "0:1:1" ),
				Arguments.of( "/",  "0:1:1" ),
				Arguments.of( "%",  "0:1:1" ),
				Arguments.of( "**", "0:2:2" ),
				Arguments.of( "&&", "0:2:2" ),
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
				Arguments.of( "42 || 123",       "(42i8||123i8)" ),
				Arguments.of( "42 || 123 || 45", "((42i8||123i8)||45i8)" )
		);
	}
	
	
	
	
	@ParameterizedTest
	@MethodSource("end_to_end_inputs")
	void e2e_conditionalOr_is_leftAssociative( String input, String expectedTree ){
		HandmadeParserLL1 spy = createSpy( input );
		Mockito.doAnswer( parseNumericLiteral ).when( spy ).conditionalAndExpression( );
		
		AstNode result = spy.conditionalOrExpression( );
		assertThat( result.toString() ).isEqualTo( expectedTree );
	}
	
	@Test
	void conditionalOr_passesResultTo_disjunction( ){
		String input = "";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).conditionalAndExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).disjunction( any() );
		
		AstNode result = spy.conditionalOrExpression( );
		assertThat( result ).isSameAs( DUMMY_AST );
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"|| 42"})
	void disjunction_delegatesTo_disjunction1( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).disjunction1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).disjunction2( any() );
		
		spy.disjunction( previousSubtree );
		verify( spy, times(1) ).disjunction1(any());
		verify( spy, times(0) ).disjunction2(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"", " )"})
	void disjunction_delegatesTo_disjunction2( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).disjunction1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).disjunction2( any() );
		
		spy.disjunction( previousSubtree );
		verify( spy, times(0) ).disjunction1(any());
		verify( spy, times(1) ).disjunction2(any());
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void disjunction_detectsSyntaxErrors( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.disjunction( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'||'", "<EOF>", "')'" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void disjunction1_rejectsOtherThan_LOR( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.disjunction1( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'||'" );
	}
	
	@Test
	void disjunction1_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = "||";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).conditionalAndExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).disjunction( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.disjunction1( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.LOGICAL_OR );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void disjunction2_returns_input_parameter( ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		String input = "the input is not relevant for this function";
		initializeParser( input );
		
		AstNode result = parser.disjunction2( previousSubtree );
		assertThat( result ).isSameAs( previousSubtree );
	}
	
}
