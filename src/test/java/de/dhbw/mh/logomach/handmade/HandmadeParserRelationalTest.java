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

class HandmadeParserRelationalTest extends HandmadeParserUtils {
	
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
				Arguments.of( "12", "0:2:2" ),
				Arguments.of( "xy", "0:2:2" )
		);
	}
	
	private static Stream<Arguments> end_to_end_inputs() {
		return Stream.of(
				Arguments.of( "42",              "42i8" ),
				Arguments.of( "42 <  123",       "(42i8<123i8)" ),
				Arguments.of( "42 <  123 && 45", "(42i8<123i8)" ),
				Arguments.of( "42 <  123 || 45", "(42i8<123i8)" ),
				Arguments.of( "42 <  123 == 45", "(42i8<123i8)" ),
				Arguments.of( "42 <  123 != 45", "(42i8<123i8)" ),
				Arguments.of( "42 <  123 )",     "(42i8<123i8)" ),
				Arguments.of( "42 <  123 <  45", "((42i8<123i8)<45i8)" ),
				Arguments.of( "42 >  123",       "(42i8>123i8)" ),
				Arguments.of( "42 >  123 && 45", "(42i8>123i8)" ),
				Arguments.of( "42 >  123 || 45", "(42i8>123i8)" ),
				Arguments.of( "42 >  123 == 45", "(42i8>123i8)" ),
				Arguments.of( "42 >  123 != 45", "(42i8>123i8)" ),
				Arguments.of( "42 >  123 )",     "(42i8>123i8)" ),
				Arguments.of( "42 >  123 >  45", "((42i8>123i8)>45i8)" ),
				Arguments.of( "42 <= 123",       "(42i8<=123i8)" ),
				Arguments.of( "42 <= 123 && 45", "(42i8<=123i8)" ),
				Arguments.of( "42 <= 123 || 45", "(42i8<=123i8)" ),
				Arguments.of( "42 <= 123 == 45", "(42i8<=123i8)" ),
				Arguments.of( "42 <= 123 != 45", "(42i8<=123i8)" ),
				Arguments.of( "42 <= 123 )",     "(42i8<=123i8)" ),
				Arguments.of( "42 <= 123 <= 45", "((42i8<=123i8)<=45i8)" ),
				Arguments.of( "42 <  123 >  45", "((42i8<123i8)>45i8)" ),
				Arguments.of( "42 >  123 <  45", "((42i8>123i8)<45i8)" )
		);
	}
	
	
	
	
	@ParameterizedTest
	@MethodSource("end_to_end_inputs")
	void e2e_relationalExpr_is_leftAssociative( String input, String expectedTree ){
		HandmadeParserLL1 spy = createSpy( input );
		Mockito.doAnswer( parseNumericLiteral ).when( spy ).additiveExpression( );
		
		AstNode result = spy.relationalExpression( );
		assertThat( result.toString() ).isEqualTo( expectedTree );
	}
	
	@Test
	void relationalExpr_passesResultTo_relations( ){
		String input = "";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).additiveExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).relations( any() );
		
		AstNode result = spy.relationalExpression( );
		assertThat( result ).isSameAs( DUMMY_AST );
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"<"})
	void relations_delegatesTo_relations1( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations3( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations4( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations5( any() );
		
		spy.relations( previousSubtree );
		verify( spy, times(1) ).relations1(any());
		verify( spy, times(0) ).relations2(any());
		verify( spy, times(0) ).relations3(any());
		verify( spy, times(0) ).relations4(any());
		verify( spy, times(0) ).relations5(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {">"})
	void relations_delegatesTo_relations2( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations3( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations4( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations5( any() );
		
		spy.relations( previousSubtree );
		verify( spy, times(0) ).relations1(any());
		verify( spy, times(1) ).relations2(any());
		verify( spy, times(0) ).relations3(any());
		verify( spy, times(0) ).relations4(any());
		verify( spy, times(0) ).relations5(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"<="})
	void relations_delegatesTo_relations3( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations3( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations4( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations5( any() );
		
		spy.relations( previousSubtree );
		verify( spy, times(0) ).relations1(any());
		verify( spy, times(0) ).relations2(any());
		verify( spy, times(1) ).relations3(any());
		verify( spy, times(0) ).relations4(any());
		verify( spy, times(0) ).relations5(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {">="})
	void relations_delegatesTo_relations4( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations3( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations4( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations5( any() );
		
		spy.relations( previousSubtree );
		verify( spy, times(0) ).relations1(any());
		verify( spy, times(0) ).relations2(any());
		verify( spy, times(0) ).relations3(any());
		verify( spy, times(1) ).relations4(any());
		verify( spy, times(0) ).relations5(any());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"==", "!=", "&&", "||", "", ")"})
	void relations_delegatesTo_relations5( String input ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations1( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations2( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations3( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations4( any() );
		Mockito.doAnswer( returnDummyAst ).when( spy ).relations5( any() );
		
		spy.relations( previousSubtree );
		verify( spy, times(0) ).relations1(any());
		verify( spy, times(0) ).relations2(any());
		verify( spy, times(0) ).relations3(any());
		verify( spy, times(0) ).relations4(any());
		verify( spy, times(1) ).relations5(any());
	}

	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void relations_detectsSyntaxErrors( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.relations( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'<'", "'>'", "'<='", "'>='", "'=='", "'!='", "'&&'", "'||'", "<EOF>", "')'" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void relations1_rejectsOtherThan_LESS( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.relations1( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'<'" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void relations2_rejectsOtherThan_GREATER( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.relations2( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'>'" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void relations3_rejectsOtherThan_LESS_EQUAL( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.relations3( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'<='" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void relations4_rejectsOtherThan_GREATER_EQUAL( String input, @CodeLoc CodeLocation location ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.relations4( previousSubtree );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'>='" );
	}
	
	@Test
	void relations1_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = "<";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).additiveExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).relations( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.relations1( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.LESS_THAN );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void relations2_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = ">";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).additiveExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).relations( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.relations2( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.GREATER_THAN );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void relations3_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = "<=";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).additiveExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).relations( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.relations3( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.LESS_OR_EQUAL );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void relations4_returns_AstBinaryOperation( ){
		AstNode first = new AstLiteral( new I8((byte)45) );
		String input = ">=";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).additiveExpression( );
		Mockito.doAnswer( returnInputParameter ).when( spy ).relations( any() );
		
		AstBinaryOperation result = (AstBinaryOperation) spy.relations4( first );
		assertThat( result.OPERATOR ).isSameAs( AstBinaryOperation.Operator.GREATER_OR_EQUAL );
		assertThat( result.FIRST ).isSameAs( first );
		assertThat( result.SECOND ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void relations5_returns_input_parameter( ){
		AstNode previousSubtree = DUMMY_LITERAL1;
		String input = "the input is not relevant for this function";
		initializeParser( input );
		
		AstNode result = parser.relations5( previousSubtree );
		assertThat( result ).isSameAs( previousSubtree );
	}
	
}
