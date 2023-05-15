package de.dhbw.mh.slang.craft.lexer;

import java.util.HashMap;
import java.util.Map;

import de.dhbw.mh.slang.craft.CharacterStream;
import de.dhbw.mh.slang.craft.CodeLocation;
import de.dhbw.mh.slang.craft.Token;
import de.dhbw.mh.slang.craft.Token.Type;

public class CraftedSlangLexer {
	
	private static final Map<String, Token.Type> RESERVED_KEYWORDS = new HashMap<>();
	
	static {
		RESERVED_KEYWORDS.put( "if",    Token.Type.IF );
		RESERVED_KEYWORDS.put( "else",  Token.Type.ELSE );
		RESERVED_KEYWORDS.put( "true",  Token.Type.TRUE );
		RESERVED_KEYWORDS.put( "false", Token.Type.FALSE );
	}

	private final CharacterStream INPUT_STREAM;
	
	private int line, previousLine;
	private int column, previousColumn;
	private int offset, previousOffset;
	private Token currentToken;
	private StringBuilder lexeme;
	
	private CraftedSlangLexer( CharacterStream inputStream ){
		super( );
		INPUT_STREAM = inputStream;
		line   = previousLine   = 0;
		column = previousColumn = 0;
		offset = previousOffset = 0;
		currentToken = null;
		lexeme = new StringBuilder( );
		advance( );
	}
	
	public static CraftedSlangLexer on( CharacterStream inputStream ){
		return new CraftedSlangLexer( inputStream );
	}
	
	public Token lookahead( ){
		return currentToken;
	}
	
	private Token createToken( Token.Type type ){
		String lexeme = this.lexeme.toString();
		this.lexeme.delete( 0, this.lexeme.length() );
		return new Token( type, lexeme,
				new CodeLocation( previousLine, previousColumn, previousOffset ),
				new CodeLocation( line, column, offset ));
	}
	
	public void advance( ){
		currentToken = null;
		lexeme.delete( 0, lexeme.length() );
		previousLine = line;
		previousColumn = column;
		previousOffset = offset;
		if( !INPUT_STREAM.hasNext() ){
			currentToken = createToken( Token.Type.EOF );
			return;
		}
		char symbol = INPUT_STREAM.next();
		updateCodeLocation( symbol );
		lexeme.append(symbol);
		Token.Type type = determineTokenType( symbol );
		if( Token.Type.ASTERISK == type ){
			if( INPUT_STREAM.hasNext() && '*' == INPUT_STREAM.lookahead() ){
				symbol = INPUT_STREAM.next();
				updateCodeLocation( symbol );
				lexeme.append(symbol);
				type = Token.Type.POWER;
			}
		}
		if( Token.Type.LESS == type ){
			if( INPUT_STREAM.hasNext() && '=' == INPUT_STREAM.lookahead() ){
				symbol = INPUT_STREAM.next();
				updateCodeLocation( symbol );
				lexeme.append(symbol);
				type = Token.Type.LESS_EQUAL;
			}
		}
		if( Token.Type.GREATER == type ){
			if( INPUT_STREAM.hasNext() && '=' == INPUT_STREAM.lookahead() ){
				symbol = INPUT_STREAM.next();
				updateCodeLocation( symbol );
				lexeme.append(symbol);
				type = Token.Type.GREATER_EQUAL;
			}
		}
		if( Token.Type.BITWISE_AND == type ){
			if( INPUT_STREAM.hasNext() && '&' == INPUT_STREAM.lookahead() ){
				symbol = INPUT_STREAM.next();
				updateCodeLocation( symbol );
				lexeme.append(symbol);
				type = Token.Type.LAND;
			}
		}
		if( Token.Type.BITWISE_OR == type ){
			if( INPUT_STREAM.hasNext() && '|' == INPUT_STREAM.lookahead() ){
				symbol = INPUT_STREAM.next();
				updateCodeLocation( symbol );
				lexeme.append(symbol);
				type = Token.Type.LOR;
			}
		}
		if( Token.Type.IDENTIFIER == type ){
			while( INPUT_STREAM.hasNext() && isIdentifierPart(INPUT_STREAM.lookahead()) ){
				symbol = INPUT_STREAM.next();
				updateCodeLocation( symbol );
				lexeme.append(symbol);
			}
			Token.Type temp = RESERVED_KEYWORDS.get( lexeme.toString() );
			if( null != temp ){
				type = temp;
			}
		}
		if( Token.Type.NUMERIC_LITERAL == type ){
			while( INPUT_STREAM.hasNext() ){
				if( isNumberPart(INPUT_STREAM.lookahead()) ){
					symbol = INPUT_STREAM.next();
					updateCodeLocation( symbol );
					lexeme.append(symbol);
				}else if( isIdentifierPart(INPUT_STREAM.lookahead()) ){
					symbol = INPUT_STREAM.next();
					updateCodeLocation( symbol );
					lexeme.append(symbol);
				}else if( '.' == INPUT_STREAM.lookahead() ){
					symbol = INPUT_STREAM.next();
					updateCodeLocation( symbol );
					lexeme.append(symbol);
				}else{
					break;
				}
			}
		}
		if( Token.Type.WHITESPACE == type ){
			while( INPUT_STREAM.hasNext() && Character.isWhitespace(INPUT_STREAM.lookahead()) ){
				symbol = INPUT_STREAM.next();
				updateCodeLocation( symbol );
				lexeme.append(symbol);
			}
			advance( );
			return;
		}
		if( Token.Type.ASSIGN == type ){
			if( INPUT_STREAM.hasNext() && '=' == INPUT_STREAM.lookahead() ){
				symbol = INPUT_STREAM.next();
				updateCodeLocation( symbol );
				lexeme.append(symbol);
				type = Token.Type.EQUAL;
			}
		}
		if( Token.Type.NEGATION == type ){
			if( INPUT_STREAM.hasNext() && '=' == INPUT_STREAM.lookahead() ){
				symbol = INPUT_STREAM.next();
				updateCodeLocation( symbol );
				lexeme.append(symbol);
				type = Token.Type.NOT_EQUAL;
			}
		}
		currentToken = createToken( type );
	}
	
	private boolean isNumberPart( char symbol ){
		return '0' <= symbol && symbol <= '9';
	}
	
	private boolean isIdentifierPart( char symbol ){
		switch( symbol ){
		case 'ä': case 'ö': case 'ü':
		case 'Ä': case 'Ö': case 'Ü':
		case 'ß':
		case '_':
			return true;
		}
		return ('a' <= symbol && symbol <= 'z') || ('A' <= symbol && symbol <= 'Z') || ('0' <= symbol && symbol <= '9');
	}
	
	private boolean lineFeedSeen = false;
	private void updateCodeLocation( char symbol ){
		++ offset;
		if( lineFeedSeen ){
			lineFeedSeen = false;
			if( '\n' == symbol ){
				return;
			}
		}
		if( '\r' == symbol ){
			lineFeedSeen = true;
			++line;
			column = 0;
		}else if( '\n' == symbol ){
			++line;
			column = 0;
		}else{
			++column;
		}
	}
	
	private Token.Type determineTokenType( char symbol ){
		switch( symbol ){
		case '+':  return Token.Type.PLUS;
		case '-':  return Token.Type.MINUS;
		case '*':  return Token.Type.ASTERISK;
		case '/':  return Token.Type.DIVIDE;
		case '%':  return Token.Type.MODULO;
		case '<':  return Token.Type.LESS;
		case '>':  return Token.Type.GREATER;
		case '&':  return Token.Type.BITWISE_AND;
		case '|':  return Token.Type.BITWISE_OR;
		case '=':  return Token.Type.ASSIGN;
		case '!':  return Token.Type.NEGATION;
		case ';':  return Token.Type.SEMICOLON;
		case '(':  return Token.Type.LPAREN;
		case ')':  return Token.Type.RPAREN;
		case ' ':  return Token.Type.WHITESPACE;
		case '\n': return Token.Type.WHITESPACE;
		case '\r': return Token.Type.WHITESPACE;
		}
		if( Character.isLetter(symbol) ){
			return Token.Type.IDENTIFIER;
		}
		if( '0' <= symbol && symbol <= '9' ){
			return Token.Type.NUMERIC_LITERAL;
		}
		throw new RuntimeException( "token recognition error: '" + symbol + "'" );
	}
	
	public void resetOn( CodeLocation location ){
		INPUT_STREAM.resetOn( location );
		line   = previousLine   = location.LINE;
		column = previousColumn = location.COLUMN;
		offset = previousOffset = location.OFFSET;
	}
	
}
