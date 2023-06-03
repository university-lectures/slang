package de.dhbw.mh.slang.codegen;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.dhbw.mh.slang.Value;
import de.dhbw.mh.slang.ast.AstNode;


class JavaBytecodeGenerator6Test extends JavaBytecodeGeneratorTest {
	
	private static final int GROUP_ID = 6;
	
	
	
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
	 *           RELATIONAL OPERATIONS          *
	 *==========================================*/
	
	private static Stream<Arguments> relationalOperations() {
		return allRelationalOperations( )
				.skip( GROUP_ID - 1 )
				.limit( 1 );
	}
	
	@ParameterizedTest
	@MethodSource("relationalOperations")
	void relationalOperationsSuccessfullyGenerated(
			AstNode node,
			String branch,
			String unfulfilled,
			String label,
			String fulfilled ){
		checkCodeGenerationForRelationalOperations( node, branch, unfulfilled, label, fulfilled );
	}

}
