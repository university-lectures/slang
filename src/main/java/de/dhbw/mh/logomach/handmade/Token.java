package de.dhbw.mh.logomach.handmade;

public class Token {
	
	public static enum Type {
		EOF("<EOF>"), WHITESPACE("<WS>"), SEMICOLON("';'"),
		PLUS("'+'"), MINUS("'-'"), ASTERISK("'*'"), DIVIDE("'/'"), MODULO("'%'"), POWER("'**'"),
		LESS("'<'"), GREATER("'>'"), LESS_EQUAL("'<='"), GREATER_EQUAL("'>='"),
		EQUAL("'=='"), NOT_EQUAL("'!='"),
		BITWISE_OR("'|'"), BITWISE_AND("'&'"),
		LAND("'&&'"), LOR("'||'"),
		NEGATION("'!'"), ASSIGN("'='"),
		LPAREN("'('"), RPAREN("')'"),
		IF("'if'"), ELSE("'else'"), TRUE("'true'"), FALSE("'false'"),
		NUMERIC_LITERAL("<NUM_LIT>"), IDENTIFIER("<ID>");
		
		private final String LABEL;
		
		private Type( String label ){
			LABEL = label;
		}
		
		@Override
		public String toString( ){
			return LABEL;
		}
	}
	
	
	public final Type TYPE;
	public final String LEXEME;
	public final CodeLocation BEGIN, END;
	
	public Token( Type type, String lexeme, CodeLocation begin, CodeLocation end ){
		super( );
		TYPE = type;
		LEXEME = lexeme;
		BEGIN = begin;
		END = end;
	}
	
	@Override
	public String toString( ){
		return String.format( "Token(%s,%s,%s-%s)", TYPE, escape(LEXEME), BEGIN, END );
	}
	
	private String escape( String original ){
		String temp = original.replaceAll( "\r", "\\\\r" );
		temp = temp.replaceAll( "\n", "\\\\n" );
		return String.format( "\"%s\"", temp );
	}

}
