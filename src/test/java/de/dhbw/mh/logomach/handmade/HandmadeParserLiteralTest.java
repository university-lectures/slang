package de.dhbw.mh.logomach.handmade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import de.dhbw.mh.logomach.Bool;
import de.dhbw.mh.logomach.ast.AstLiteral;

class HandmadeParserLiteralTest extends HandmadeParserUtils {
	
	
	private static Stream<Arguments> delegation_errorDetection() {
		return Stream.of(
				Arguments.of( "+",   "0:1:1" ),
				Arguments.of( "-",   "0:1:1" ),
				Arguments.of( "*",   "0:1:1" ),
				Arguments.of( "/",   "0:1:1" ),
				Arguments.of( "%",   "0:1:1" ),
				Arguments.of( "**",  "0:2:2" ),
				Arguments.of( "==",  "0:2:2" ),
				Arguments.of( "!=",  "0:2:2" ),
				Arguments.of( "(",   "0:1:1" ),
				Arguments.of( "<",   "0:1:1" ),
				Arguments.of( ">",   "0:1:1" ),
				Arguments.of( "<=",  "0:2:2" ),
				Arguments.of( ">=",  "0:2:2" ),
				Arguments.of( "nil", "0:3:3" )
		);
	}
	
	private static Stream<Arguments> numericLiterals() {
		return Stream.of(
				Arguments.of( "23",     "23i8" ),
				Arguments.of( "23i8",   "23i8" ),
				Arguments.of( "23i16",  "23i16" ),
				Arguments.of( "23i32",  "23i32" ),
				Arguments.of( "23i64",  "23i64" ),
				Arguments.of( "23f32",  "23.0f32" ),
				Arguments.of( "23f64",  "23.0f64" ),
				Arguments.of( "23.2",   "23.2f32" ),
				Arguments.of( "0.5",    "0.5f32" ),
				Arguments.of( "1_23.2", "123.2f32" )
		);
	}
	
	private static Stream<Arguments> booleanLiterals() {
		return Stream.of(
				Arguments.of( "true",   "true" ),
				Arguments.of( "false",  "false" )
		);
	}
	
	
	@ParameterizedTest
	@ValueSource(strings = {"true", "false"})
	void literal_delegatesTo_literal1( String input ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).literal1( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).literal2( );
		
		spy.literal( );
		verify( spy, times(1) ).literal1( );
		verify( spy, times(0) ).literal2( );
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"38"})
	void literal_delegatesTo_literal2( String input ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).literal1( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).literal2( );
		
		spy.literal( );
		verify( spy, times(0) ).literal1( );
		verify( spy, times(1) ).literal2( );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void literal_detectsSyntaxErrors( String input, @CodeLoc CodeLocation location ){
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.literal( );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'false'", "<NUM_LIT>", "'true'" );
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "3i7", "3i9", "3i15", "3i17", "3i31", "3i33", "3i63", "3i65" })
	void literal_detectsMalformedIntegers( String input ){
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.literal( );
		}).withMessage( "integer data types exist only in lengths of 8, 16, 32 and 64 bits" );
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "3f31", "3f33", "3f63", "3f65" })
	void literal_detectsMalformedFloats( String input ){
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.literal( );
		}).withMessage( "floating point data types exist only in lengths of 32 and 64 bits" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void literal1_rejectsOtherThan_BOOL( String input, @CodeLoc CodeLocation location ){
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.literal1( );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'true'", "'false'" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void literal2_rejectsOtherThan_NUMERIC_LITERAL( String input, @CodeLoc CodeLocation location ){
		initializeParser( input );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.literal2( );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "<NUM_LIT>" );
	}
	
	@ParameterizedTest
	@MethodSource("booleanLiterals")
	void literal1_returnsBooleanLiterals( String input, String expected ){
		initializeParser( input );
		
		AstLiteral literal = (AstLiteral) parser.literal1();
		assertThat( literal.VALUE.toString() ).isEqualTo( expected );
		assertThat( literal.VALUE ).isInstanceOf( Bool.class );
	}
	
	@ParameterizedTest
	@MethodSource("numericLiterals")
	void literal2_returnsNumericLiterals( String input, String expected ){
		initializeParser( input );
		
		AstLiteral literal = (AstLiteral) parser.literal2();
		assertThat( literal.VALUE.toString() ).isEqualTo( expected );
	}
	
}
