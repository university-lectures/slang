package de.dhbw.mh.logomach.handmade;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import de.dhbw.mh.logomach.ast.AstNode;

@Suite
@SelectClasses({
	HandmadeParserCondOrTest.class,
	HandmadeParserCondAndTest.class,
	HandmadeParserEquationTest.class,
	HandmadeParserRelationalTest.class,
	HandmadeParserAdditiveTest.class,
	HandmadeParserMultiplicativeTest.class,
	HandmadeParserSignedTermTest.class,
	HandmadeParserExponentiationTest.class,
	HandmadeParserAtomicsTest.class,
	HandmadeParserLiteralTest.class
	})
public class HandmadeParserTestSuite {
	
	public static final class Environment {
		public final HandmadeLexer LEXER;
		public final AbstractParserLL1 PARSER;
		public Environment( HandmadeLexer lexer, AbstractParserLL1 parser ){
			super( );
			LEXER = lexer;
			PARSER = parser;
		}
	}
	
	public static Environment parserFor( String input ){
		CharacterStream stream = CharacterStream.fromString( input );
		HandmadeLexer  lexer  = HandmadeLexer.on( stream );
		AbstractParserLL1 parser = new HandmadeParserLL1( lexer );
		return new Environment( lexer, parser );
	}
	
	
	private static Stream<Arguments> end_to_end_inputs( ){
		return Stream.of(
				Arguments.of( "42",              "42i8" ),
				Arguments.of( "42 + 3",          "(42i8+3i8)" ),
				Arguments.of( "11 * 5",          "(11i8*5i8)" ),
				Arguments.of( "9 + 23 * 2",      "(9i8+(23i8*2i8))" ),
				Arguments.of( "31 * 7 + 64",     "((31i8*7i8)+64i8)" ),
				Arguments.of( "31 * 7 + 2 ** 6", "((31i8*7i8)+(2i8**6i8))" ),
				Arguments.of( "16 <= x <= 59",   "((16i8<=x)<=59i8)" ),
				Arguments.of( "31 && 7 || 3",    "((31i8&&7i8)||3i8)" ),
				Arguments.of( "31 || 7 && 3",    "(31i8||(7i8&&3i8))" ),
				Arguments.of( "a+3 <= b <= c*5", "(((a+3i8)<=b)<=(c*5i8))" ),
				Arguments.of( "-77",             "-77i8" ),
				Arguments.of( "-( x * y + 63 )", "-((x*y)+63i8)" )
		);
	}
	
	private static Stream<Arguments> shadyCases( ){
		return Stream.of(
				Arguments.of( "31 < 7 == 3",     "((31i8<7i8)==3i8)" ),
				Arguments.of( "3 == z != p % q", "((3i8==z)!=(p%q))" )
		);
	}
	
	
	@ParameterizedTest
	@MethodSource("end_to_end_inputs")
	void inputIsParsedCompletely( String input, String expectedTree ){
		Environment environment = parserFor( input );
		AstNode node = environment.PARSER.conditionalExpression( );
		assertThat( node.toString() ).isEqualTo( expectedTree );
	}
	
	@ParameterizedTest
	@MethodSource("shadyCases")
	void drawAttentionToShadyCases( String input, String expectedTree ){
		Environment environment = parserFor( input );
		AstNode node = environment.PARSER.conditionalExpression( );
		assertThat( node.toString() ).isEqualTo( expectedTree );
	}

}
