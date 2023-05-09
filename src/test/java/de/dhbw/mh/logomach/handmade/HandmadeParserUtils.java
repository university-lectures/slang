package de.dhbw.mh.logomach.handmade;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.dhbw.mh.logomach.I8;
import de.dhbw.mh.logomach.NumericValue;
import de.dhbw.mh.logomach.ast.AstLiteral;
import de.dhbw.mh.logomach.ast.AstNode;

public class HandmadeParserUtils {
	
	protected HandmadeLexer  lexer;
	protected HandmadeParserLL1 parser = new HandmadeParserLL1( lexer );
	
	/**
	 * The lexer must be reinitialized at the beginning of each test.
	 * However, it is dependent on the test-specific input.
	 * Therefore, the initialization cannot be performed in @BeforeEach.
	 * To ensure that all tests without proper initialization break, the lexer is set to null.
	 */
	@BeforeEach
	void init( ){
		lexer  = null;
		parser = null;
	}
	
	protected void initializeParser( String input ){
		CharacterStream stream = CharacterStream.fromString( input );
		lexer  = HandmadeLexer.on( stream );
		parser = new HandmadeParserLL1( lexer );
	}
	
	protected HandmadeParserLL1 createSpy( String input ){
		initializeParser( input );
		return Mockito.spy( parser );
	}
	
	
	protected Answer<AstNode> returnInputParameter = new Answer<AstNode>(){
		@Override
		public AstNode answer( InvocationOnMock invocation ){
			return (AstNode) invocation.getArguments()[0];
		}
	};
	
	protected static final AstNode DUMMY_AST = new AstLiteral( new I8((byte)45) );
	protected Answer<AstNode> returnDummyAst = new Answer<AstNode>(){
		@Override
		public AstNode answer( InvocationOnMock invocation ){
			return DUMMY_AST;
		}
	};
	
	protected Answer<AstNode> parseNumericLiteral = new Answer<AstNode>(){
		@Override
		public AstNode answer( InvocationOnMock invocation ){
			Token token = lexer.lookahead();
			parser.match( Token.Type.NUMERIC_LITERAL );
			NumericValue value = NumericalEvaluator.parse( token.LEXEME );
			return new AstLiteral( value );
		}
	};

}
