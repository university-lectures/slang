package de.dhbw.mh.slang.handmade;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.dhbw.mh.slang.handmade.CharacterStream;
import de.dhbw.mh.slang.handmade.CodeLocation;
import de.dhbw.mh.slang.handmade.HandmadeLexer;
import de.dhbw.mh.slang.handmade.Token;

class HandmadeLexerTest {

	private static Stream<Arguments> inputToToken() {
		return Stream.of(
				Arguments.of( "",
						new Token(Token.Type.EOF,           "",     new CodeLocation(0,0,0), new CodeLocation(0,0,0)) ),
				Arguments.of( " ",
						new Token(Token.Type.EOF,           "",     new CodeLocation(0,1,1), new CodeLocation(0,1,1)) ),
				Arguments.of( "\n",
						new Token(Token.Type.EOF,           "",     new CodeLocation(1,0,1), new CodeLocation(1,0,1)) ),
				Arguments.of( "\r",
						new Token(Token.Type.EOF,           "",     new CodeLocation(1,0,1), new CodeLocation(1,0,1)) ),
				Arguments.of( "\r\n",
						new Token(Token.Type.EOF,           "",     new CodeLocation(1,0,2), new CodeLocation(1,0,2)) ),
				Arguments.of( "+",
						new Token(Token.Type.PLUS,          "+",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( "-",
						new Token(Token.Type.MINUS,         "-",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( "*",
						new Token(Token.Type.ASTERISK,      "*",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( "/",
						new Token(Token.Type.DIVIDE,        "/",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( "**",
						new Token(Token.Type.POWER,         "**",   new CodeLocation(0,0,0), new CodeLocation(0,2,2)) ),
				Arguments.of( "%",
						new Token(Token.Type.MODULO,        "%",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( "(",
						new Token(Token.Type.LPAREN,        "(",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( ")",
						new Token(Token.Type.RPAREN,        ")",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( "<",
						new Token(Token.Type.LESS,          "<",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( ">",
						new Token(Token.Type.GREATER,       ">",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( "<=",
						new Token(Token.Type.LESS_EQUAL,    "<=",   new CodeLocation(0,0,0), new CodeLocation(0,2,2)) ),
				Arguments.of( ">=",
						new Token(Token.Type.GREATER_EQUAL, ">=",   new CodeLocation(0,0,0), new CodeLocation(0,2,2)) ),
				Arguments.of( "==",
						new Token(Token.Type.EQUAL,         "==",   new CodeLocation(0,0,0), new CodeLocation(0,2,2)) ),
				Arguments.of( "!=",
						new Token(Token.Type.NOT_EQUAL,     "!=",   new CodeLocation(0,0,0), new CodeLocation(0,2,2)) ),
				Arguments.of( "&",
						new Token(Token.Type.BITWISE_AND,   "&",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( "|",
						new Token(Token.Type.BITWISE_OR,    "|",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( "&&",
						new Token(Token.Type.LAND,          "&&",   new CodeLocation(0,0,0), new CodeLocation(0,2,2)) ),
				Arguments.of( "||",
						new Token(Token.Type.LOR,           "||",   new CodeLocation(0,0,0), new CodeLocation(0,2,2)) ),
				Arguments.of( "a",
						new Token(Token.Type.IDENTIFIER,    "a",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( "a_",
						new Token(Token.Type.IDENTIFIER,    "a_",   new CodeLocation(0,0,0), new CodeLocation(0,2,2)) ),
//				Arguments.of( "_a",
//						new Token(Token.Type.IDENTIFIER,    "_a",   new CodeLocation(0,0,0), new CodeLocation(0,1,1)) )
				Arguments.of( "ß",
						new Token(Token.Type.IDENTIFIER,    "ß",    new CodeLocation(0,0,0), new CodeLocation(0,1,1)) ),
				Arguments.of( "if",
						new Token(Token.Type.IF,            "if",   new CodeLocation(0,0,0), new CodeLocation(0,2,2)) ),
				Arguments.of( "else",
						new Token(Token.Type.ELSE,          "else", new CodeLocation(0,0,0), new CodeLocation(0,4,4)) ),
				Arguments.of( "true",
						new Token(Token.Type.TRUE,          "true", new CodeLocation(0,0,0), new CodeLocation(0,4,4)) ),
				Arguments.of( "false",
						new Token(Token.Type.FALSE,         "false", new CodeLocation(0,0,0), new CodeLocation(0,5,5)) ),
				Arguments.of( "123",
						new Token(Token.Type.NUMERIC_LITERAL, "123",  new CodeLocation(0,0,0), new CodeLocation(0,3,3)) ),
				Arguments.of( "123i8",
						new Token(Token.Type.NUMERIC_LITERAL, "123i8", new CodeLocation(0,0,0), new CodeLocation(0,5,5)) ),
				Arguments.of( "12.3",
						new Token(Token.Type.NUMERIC_LITERAL, "12.3",  new CodeLocation(0,0,0), new CodeLocation(0,4,4)) )
		);
	}
	
	@ParameterizedTest
	@MethodSource("inputToToken")
	void test( String input, Token result ){
		CharacterStream inputStream = CharacterStream.fromString( input );
		HandmadeLexer lexer = HandmadeLexer.on( inputStream );
		lexer.lookahead();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( result.TYPE );
		assertThat( lexer.lookahead().LEXEME ).isEqualTo( result.LEXEME );
		assertThat( lexer.lookahead().BEGIN ).isNotNull();
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( result.BEGIN.LINE );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( result.BEGIN.COLUMN );
		assertThat( lexer.lookahead().END ).isNotNull();
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( result.END.LINE );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( result.END.COLUMN );
	}
	
	
	@Test
	@MethodSource("inputToToken")
	void testMultipleTokens( ){
		CharacterStream inputStream = CharacterStream.fromString( "s = 1/2 * a * t**2\r\n  + v * t\r\n  + s_0;" );
		HandmadeLexer lexer = HandmadeLexer.on( inputStream );
		
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.IDENTIFIER );
		assertThat( lexer.lookahead().LEXEME ).isEqualTo( "s" );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 1 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 1 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.ASSIGN );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 2 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 2 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 3 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 3 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.NUMERIC_LITERAL );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 4 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 4 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 5 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 5 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.DIVIDE );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 5 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 5 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 6 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 6 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.NUMERIC_LITERAL );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 6 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 6 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 7 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 7 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.ASTERISK );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 8 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 8 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 9 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 9 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.IDENTIFIER );
		assertThat( lexer.lookahead().LEXEME ).isEqualTo( "a" );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 10 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 10 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 11 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 11 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.ASTERISK );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 12 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 12 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 13 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 13 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.IDENTIFIER );
		assertThat( lexer.lookahead().LEXEME ).isEqualTo( "t" );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 14 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 14 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 15 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 15 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.POWER );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 15 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 15 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 17 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 17 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.NUMERIC_LITERAL );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 17 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 17 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 18 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 18 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.PLUS );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 1 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 2 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 22 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 1 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 3 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 23 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.IDENTIFIER );
		assertThat( lexer.lookahead().LEXEME ).isEqualTo( "v" );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 1 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 4 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 24 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 1 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 5 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 25 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.ASTERISK );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 1 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 6 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 26 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 1 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 7 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 27 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.IDENTIFIER );
		assertThat( lexer.lookahead().LEXEME ).isEqualTo( "t" );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 1 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 8 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 1 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 9 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 29 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.PLUS );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 2 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 2 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 2 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 3 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 34 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.IDENTIFIER );
		assertThat( lexer.lookahead().LEXEME ).isEqualTo( "s_0" );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 2 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 4 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 2 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 7 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 38 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.SEMICOLON );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 2 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 7 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 2 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 8 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 39 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.EOF );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 2 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 8 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 2 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 8 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 39 );
	}
	
	
	
	@Test
	@MethodSource("inputToToken")
	void testLexerReset( ){
		CharacterStream inputStream = CharacterStream.fromString( "s = 1/2 * a * t**2\r\n  + v * t\r\n  + s_0;" );
		HandmadeLexer lexer = HandmadeLexer.on( inputStream );
		
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.IDENTIFIER );
		assertThat( lexer.lookahead().LEXEME ).isEqualTo( "s" );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 1 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 1 );
		lexer.advance();
		CodeLocation assignLocation = lexer.lookahead().BEGIN;
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.ASSIGN );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 2 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 2 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 3 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 3 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.NUMERIC_LITERAL );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 4 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 4 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 5 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 5 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.DIVIDE );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 5 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 5 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 6 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 6 );
		lexer.resetOn( assignLocation );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.ASSIGN );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 2 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 2 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 3 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 3 );
		lexer.advance();
		assertThat( lexer.lookahead() ).isNotNull();
		assertThat( lexer.lookahead().TYPE ).isSameAs( Token.Type.NUMERIC_LITERAL );
		assertThat( lexer.lookahead().BEGIN.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().BEGIN.COLUMN ).isEqualTo( 4 );
		assertThat( lexer.lookahead().BEGIN.OFFSET ).isEqualTo( 4 );
		assertThat( lexer.lookahead().END.LINE ).isEqualTo( 0 );
		assertThat( lexer.lookahead().END.COLUMN ).isEqualTo( 5 );
		assertThat( lexer.lookahead().END.OFFSET ).isEqualTo( 5 );
	}

}
