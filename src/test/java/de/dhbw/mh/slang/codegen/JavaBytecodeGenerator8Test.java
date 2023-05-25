package de.dhbw.mh.slang.codegen;

import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.dhbw.mh.slang.Value;
import de.dhbw.mh.slang.ast.AstNode;

@Disabled
class JavaBytecodeGenerator8Test extends JavaBytecodeGeneratorTest {
	
	private static final int GROUP_ID = 8;
	
	
	
	/*==========================================*
	 *                 LITERALS                 *
	 *==========================================*/
	
	private static Stream<Arguments> literals() {
		return allLiterals( )
				.skip( GROUP_ID - 1 )
				.limit( 1 );
	}

	@ParameterizedTest
	@MethodSource("literals")
	void literalsSuccessfullyGenerated( Value value, String instruction ){
		checkCodeGenerationForLiterals( value, instruction );
	}
	
	/*==========================================*
	 *            LOGICAL OPERATIONS            *
	 *==========================================*/
	
	private static Stream<Arguments> logicalOperations() {
		return allLogicalOperations( )
				.skip( GROUP_ID - 7 )
				.limit( 1 );
	}
	
	@ParameterizedTest
	@MethodSource("logicalOperations")
	void logicalOperationsSuccessfullyGenerated( AstNode node, String a, String b, String c, String d, String e ){
		checkCodeGenerationForLogicalOperations( node, a, b, c, d, e );
	}

}
