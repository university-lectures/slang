package de.dhbw.mh.slang.codegen;

import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.dhbw.mh.slang.Datatype;
import de.dhbw.mh.slang.Value;
import de.dhbw.mh.slang.ast.AstNode;


class JavaBytecodeGenerator4Test extends JavaBytecodeGeneratorTest {
	
	private static final int GROUP_ID = 4;
	
	
	
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
	 *             BINARY OPERATIONS            *
	 *==========================================*/
	
	private static Stream<Arguments> binaryOperations() {
		return allBinaryOperations( )
				.skip( GROUP_ID - 1 )
				.limit( 1 );
	}
	
	@ParameterizedTest
	@MethodSource("binaryOperations")
	void bool_BinaryOperationsSuccessfullyGenerated( AstNode node, String instruction ){
		node.setDatatype( Datatype.BOOL );
		String bytecode = String.format( "LHS%nRHS%ni%s%n", instruction );
		checkCodeGenerationForBinaryOperations( node, bytecode );
	}
	
	@ParameterizedTest
	@MethodSource("binaryOperations")
	void i8_BinaryOperationsSuccessfullyGenerated( AstNode node, String instruction ){
		node.setDatatype( Datatype.I8 );
		String bytecode = String.format( "LHS%nRHS%ni%s%n", instruction );
		checkCodeGenerationForBinaryOperations( node, bytecode );
	}
	
	@ParameterizedTest
	@MethodSource("binaryOperations")
	void i16_BinaryOperationsSuccessfullyGenerated( AstNode node, String instruction ){
		node.setDatatype( Datatype.I16 );
		String bytecode = String.format( "LHS%nRHS%ni%s%n", instruction );
		checkCodeGenerationForBinaryOperations( node, bytecode );
	}
	
	@ParameterizedTest
	@MethodSource("binaryOperations")
	void i32_BinaryOperationsSuccessfullyGenerated( AstNode node, String instruction ){
		node.setDatatype( Datatype.I32 );
		String bytecode = String.format( "LHS%nRHS%ni%s%n", instruction );
		checkCodeGenerationForBinaryOperations( node, bytecode );
	}
	
	@ParameterizedTest
	@MethodSource("binaryOperations")
	void i64_BinaryOperationsSuccessfullyGenerated( AstNode node, String instruction ){
		node.setDatatype( Datatype.I64 );
		String bytecode = String.format( "LHS%nRHS%nl%s%n", instruction );
		checkCodeGenerationForBinaryOperations( node, bytecode );
	}
	
	@ParameterizedTest
	@MethodSource("binaryOperations")
	void f32_BinaryOperationsSuccessfullyGenerated( AstNode node, String instruction ){
		node.setDatatype( Datatype.F32 );
		String bytecode = String.format( "LHS%nRHS%nf%s%n", instruction );
		checkCodeGenerationForBinaryOperations( node, bytecode );
	}
	
	@ParameterizedTest
	@MethodSource("binaryOperations")
	void f64_BinaryOperationsSuccessfullyGenerated( AstNode node, String instruction ){
		node.setDatatype( Datatype.F64 );
		String bytecode = String.format( "LHS%nRHS%nd%s%n", instruction );
		checkCodeGenerationForBinaryOperations( node, bytecode );
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
	void relationalOperationsSuccessfullyGenerated( AstNode node, String instruction, String label ){
		checkCodeGenerationForRelationalOperations( node, instruction, label );
	}

}
