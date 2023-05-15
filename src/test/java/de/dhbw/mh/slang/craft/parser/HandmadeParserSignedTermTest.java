package de.dhbw.mh.slang.craft.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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

import de.dhbw.mh.slang.ast.AstNode;
import de.dhbw.mh.slang.ast.AstUnaryOperation;
import de.dhbw.mh.slang.craft.CodeLoc;
import de.dhbw.mh.slang.craft.CodeLocation;

class HandmadeParserSignedTermTest extends HandmadeParserUtils {
	
	private static Stream<Arguments> delegation_errorDetection() {
		return Stream.of(
				Arguments.of( "<",  "0:1:1" ),
				Arguments.of( ">",  "0:1:1" ),
				Arguments.of( "<=", "0:2:2" )
		);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"+"})
	void signedTerm_delegatesTo_signedTerm1( String input ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm1( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm2( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm3( );
		
		spy.signedTerm( );
		verify( spy, times(1) ).signedTerm1( );
		verify( spy, times(0) ).signedTerm2( );
		verify( spy, times(0) ).signedTerm3( );
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"-"})
	void signedTerm_delegatesTo_signedTerm2( String input ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm1( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm2( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm3( );
		
		spy.signedTerm( );
		verify( spy, times(0) ).signedTerm1( );
		verify( spy, times(1) ).signedTerm2( );
		verify( spy, times(0) ).signedTerm3( );
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"(", "93", "tree"})
	void signedTerm_delegatesTo_signedTerm3( String input ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm1( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm2( );
		Mockito.doAnswer( returnDummyAst ).when( spy ).signedTerm3( );
		
		spy.signedTerm( );
		verify( spy, times(0) ).signedTerm1( );
		verify( spy, times(0) ).signedTerm2( );
		verify( spy, times(1) ).signedTerm3( );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	void signedTerm_detectsSyntaxErrors( String input, @CodeLoc CodeLocation location ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).exponentiation( );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			spy.signedTerm( );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'+'", "'-'", "'('", "<ID>", "<NUM_LIT>" );
	}
	
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	@CsvSource({ "-,0:1:1" })
	void signedTerm1_rejectsOtherThan_PLUS( String input, @CodeLoc CodeLocation location ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).exponentiation( );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.signedTerm1( );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'+'" );
	}
	
	@ParameterizedTest
	@MethodSource("delegation_errorDetection")
	@CsvSource({ "+,0:1:1" })
	void signedTerm2_rejectsOtherThan_MINUS( String input, @CodeLoc CodeLocation location ){
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).exponentiation( );
		
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			parser.signedTerm2( );
		}).withMessageStartingWith( "mismatched input '%s' at 0:0:0-%s, expected", input, location )
		.withMessageContainingAll( "'-'" );
	}
	
	@Test
	void signedTerm1_returns_AstUnaryOperation( ){
		String input = "+54";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).exponentiation( );
		
		AstUnaryOperation result = (AstUnaryOperation) spy.signedTerm1( );
		assertThat( result.OPERATOR ).isSameAs( AstUnaryOperation.Operator.POSITIVE_SIGN );
		assertThat( result.BASE ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void signedTerm2_returns_AstUnaryOperation( ){
		String input = "-54";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).exponentiation( );
		
		AstUnaryOperation result = (AstUnaryOperation) spy.signedTerm2( );
		assertThat( result.OPERATOR ).isSameAs( AstUnaryOperation.Operator.NEGATIVE_SIGN );
		assertThat( result.BASE ).isSameAs( DUMMY_AST );
	}
	
	@Test
	void signedTerm3_allows_exponentiation( ){
		String input = "54";
		AbstractParserLL1 spy = createSpy( input );
		Mockito.doAnswer( returnDummyAst ).when( spy ).exponentiation( );
		
		AstNode result = spy.signedTerm3( );
		assertThat( result ).isSameAs( DUMMY_AST );
	}
	
}
