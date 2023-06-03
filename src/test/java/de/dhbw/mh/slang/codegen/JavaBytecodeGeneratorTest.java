package de.dhbw.mh.slang.codegen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mockito;

import de.dhbw.mh.slang.Bool;
import de.dhbw.mh.slang.Datatype;
import de.dhbw.mh.slang.F32;
import de.dhbw.mh.slang.F64;
import de.dhbw.mh.slang.I16;
import de.dhbw.mh.slang.I32;
import de.dhbw.mh.slang.I64;
import de.dhbw.mh.slang.I8;
import de.dhbw.mh.slang.Value;
import de.dhbw.mh.slang.ast.AstBinaryOperation;
import de.dhbw.mh.slang.ast.AstLiteral;
import de.dhbw.mh.slang.ast.AstNode;


class JavaBytecodeGeneratorTest {
	
	private static final AstLiteral LHS = new AstLiteral(new I8( (byte)45 ));
	private static final AstLiteral RHS = new AstLiteral(new I8( (byte)97 ));
	
	
	/*==========================================*
	 *                 LITERALS                 *
	 *==========================================*/
	
	protected static Stream<Arguments> allLiterals() {
		return Stream.of(
				Arguments.of( new I8( (byte)45 ),   String.format("bipush 45%n") ),
				Arguments.of( new I16( (short)97 ), String.format("sipush 97%n") ),
				Arguments.of( new I32( (short)31 ), String.format("ldc 31%n") ),
				Arguments.of( new I64( (short)23 ), String.format("ldc2_w 23%n") ),
				Arguments.of( new F32( 3.1415f ),   String.format("ldc 3.1415f%n") ),
				Arguments.of( new F64( 2.718281d ), String.format("ldc2_w 2.718281d%n") ),
				Arguments.of( new Bool( true ),     String.format("iconst_1%n") ),
				Arguments.of( new Bool( false ),    String.format("iconst_0%n") )
		);
	}
	
	protected void checkCodeGenerationForLiterals( Value value, String instruction ){
		AstLiteral literal = new AstLiteral( value );
		JavaBytecodeGenerator generator = new JavaBytecodeGenerator( );
		assertThat( literal.accept(generator) ).isEqualTo( instruction );
	}
	
	
	/*==========================================*
	 *             BINARY OPERATIONS            *
	 *==========================================*/
	
	protected static Stream<Arguments> allBinaryOperations() {
		return Stream.of(
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.ADD, RHS ),
						"add"
				),
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.SUBTRACT, RHS ),
						"sub"
				),
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.MULTIPLY, RHS ),
						"mul"
				),
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.DIVIDE, RHS ),
						"div"
				),
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.MODULO, RHS ),
						"rem"
				)
		);
	}
	
	private static JavaBytecodeGenerator getSpy( ){
		JavaBytecodeGenerator spy = Mockito.spy( JavaBytecodeGenerator.class );
		Mockito.doReturn( String.format("LHS%n") ).when( spy ).visit( LHS );
		Mockito.doReturn( String.format("RHS%n") ).when( spy ).visit( RHS );
		return spy;
	}
	
	protected void checkCodeGenerationForBinaryOperations( AstNode node, String bytecode ){
		JavaBytecodeGenerator spy = getSpy( );
		assertThat( node.accept(spy) ).isEqualTo( bytecode );
		Mockito.verify( spy, times(1) ).visit( LHS );
		Mockito.verify( spy, times(1) ).visit( RHS );
	}
	
	
	/*==========================================*
	 *            LOGICAL OPERATIONS            *
	 *==========================================*/
	
	protected static Stream<Arguments> allLogicalOperations() {
		return Stream.of(
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.LOGICAL_AND, RHS ),
						"ifeq #0_false",
						"ifeq #0_false",
						"iconst_1",
						"#0_false:",
						"iconst_0"
				),
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.LOGICAL_OR, RHS ),
						"ifne #0_true",
						"ifne #0_true",
						"iconst_0",
						"#0_true:",
						"iconst_1"
				)
		);
	}
	
	void checkCodeGenerationForLogicalOperations( AstNode node, String a, String b, String c, String d, String e ){
		String template = "LHS%n%s%nRHS%n%s%n%s%ngoto #0_end%n%s%n%s%n#0_end:%n";
		String bytecode = String.format( template, a, b, c, d, e );
		
		JavaBytecodeGenerator spy = Mockito.spy( JavaBytecodeGenerator.class );
		Mockito.doReturn( String.format("LHS%n") ).when( spy ).visit( LHS );
		Mockito.doReturn( String.format("RHS%n") ).when( spy ).visit( RHS );
		assertThat( node.accept(spy) ).isEqualTo( bytecode );
	}
	
	
	/*==========================================*
	 *           RELATIONAL OPERATIONS          *
	 *==========================================*/
	
	protected static Stream<Arguments> allRelationalOperations() {
		return Stream.of(
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.COMPARE_EQUAL, RHS ),
						"if_icmpne #0_ne",
						"iconst_1",
						"#0_ne:",
						"iconst_0"
				),
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.COMPARE_UNEQUAL, RHS ),
						"if_icmpeq #0_eq",
						"iconst_1",
						"#0_eq:",
						"iconst_0"
				),
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.LESS_THAN, RHS ),
						"if_icmpge #0_ge",
						"iconst_1",
						"#0_ge:",
						"iconst_0"
				),
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.GREATER_THAN, RHS ),
						"if_icmple #0_le",
						"iconst_1",
						"#0_le:",
						"iconst_0"
				),
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.LESS_OR_EQUAL, RHS ),
						"if_icmpgt #0_gt",
						"iconst_1",
						"#0_gt:",
						"iconst_0"
				),
				Arguments.of(
						new AstBinaryOperation( null, LHS, AstBinaryOperation.Operator.GREATER_OR_EQUAL, RHS ),
						"if_icmpge #0_ge",
						"iconst_0",
						"#0_ge:",
						"iconst_1"
				)
		);
	}
	
	void checkCodeGenerationForRelationalOperations(
			AstNode node,
			String branch,
			String unfulfilled,
			String label,
			String fulfilled ){
		node.setDatatype(Datatype.I32);
		String template = "LHS%nRHS%n%s%n%s%ngoto #0_end%n%s%n%s%n#0_end:%n";
		String bytecode = String.format(template, branch, unfulfilled, label, fulfilled);

		JavaBytecodeGenerator spy = Mockito.spy(JavaBytecodeGenerator.class);
		Mockito.doReturn(String.format("LHS%n")).when(spy).visit(LHS);
		Mockito.doReturn(String.format("RHS%n")).when(spy).visit(RHS);
		assertThat(node.accept(spy)).isEqualTo(bytecode);
	}

}
