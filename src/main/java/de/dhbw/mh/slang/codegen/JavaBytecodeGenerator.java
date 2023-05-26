package de.dhbw.mh.slang.codegen;

import de.dhbw.mh.slang.*;
import de.dhbw.mh.slang.ast.AstBinaryOperation;
import de.dhbw.mh.slang.ast.AstLiteral;
import de.dhbw.mh.slang.ast.AstUnaryOperation;
import de.dhbw.mh.slang.ast.AstVariable;
import de.dhbw.mh.slang.ast.AstVisitor;

import java.util.Formatter;

public class JavaBytecodeGenerator implements AstVisitor<String> {
	
	public JavaBytecodeGenerator( ){
		super( );
	}

	@Override
	public String visit( AstLiteral literal ){
		if( literal.VALUE instanceof I8 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof I16 ){
			I16 shortValue = (I16) literal.VALUE;
			// Pushes the short constant onto the operand stack.
			return String.format("sipush %d%n", shortValue.VALUE);
		}else if( literal.VALUE instanceof I32 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof I64 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof F32 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof F64 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof Bool ){
			//throw new RuntimeException( "not yet implemented" );
			Bool boolValue = (Bool) literal.VALUE;
			// Pushes the int constant <i> (0 or 1 for false or true respectively) onto the operand stack.
			return boolValue.VALUE ? "iconst_1" : "iconst_0";
		}
		throw new RuntimeException( "unhandled branch" );
	}

	@Override
	public String visit( AstVariable variable ){
		throw new RuntimeException( "not yet implemented" );
	}

	@Override
	public String visitPost( AstUnaryOperation node, String base ){
		switch( node.OPERATOR ){
			case NEGATIVE_SIGN:{
				throw new RuntimeException( "not yet implemented" );
			}
			case POSITIVE_SIGN:{
				throw new RuntimeException( "not yet implemented" );
			}
		}
		throw new RuntimeException( "unhandled branch" );
	}

	@Override
	public String visitPost( AstBinaryOperation node, String lhs, String rhs ){
		char resultChar = getBytecodeDatatypeChar(node.getDatatype());
		switch( node.OPERATOR ){
			case ADD:{
				throw new RuntimeException( "not yet implemented" );
			}
			case SUBTRACT: {
				return String.format("%s%s%ssub%n", lhs, rhs, resultChar);
				//return rhs + "\n" + lhs + "\nisub";
			}
			case MULTIPLY:{
				throw new RuntimeException( "not yet implemented" );
			}
			case DIVIDE:{
				throw new RuntimeException( "not yet implemented" );
			}
			case MODULO:{
				throw new RuntimeException( "not yet implemented" );
			}
			case LOGICAL_AND:{
				throw new RuntimeException( "not yet implemented" );
			}
			case LOGICAL_OR:{
				throw new RuntimeException( "not yet implemented" );
			}
			case COMPARE_EQUAL: {
				//String compareEqual = String.format("if_icmpeq equal%niconst_1%ngoto endCompare%nequal:%niconst_0%nendCompare:%n");
				//return String.format("%s%n%s%n%s", lhs.trim(), rhs.trim(), compareEqual);
				throw new RuntimeException( "not yet implemented" );
			}
			case COMPARE_UNEQUAL: {
				String compareAndNegate = String.format("if_icmpeq #0_eq%niconst_1%ngoto #0_end%n#0_eq:%niconst_0%n#0_end:%n");
				return String.format("%s%n%s%n%s", lhs.trim(), rhs.trim(), compareAndNegate);

			}
			case LESS_THAN:{
				throw new RuntimeException( "not yet implemented" );
			}
			case GREATER_OR_EQUAL:{
				throw new RuntimeException( "not yet implemented" );
			}
			case GREATER_THAN:{
				throw new RuntimeException( "not yet implemented" );
			}
			case LESS_OR_EQUAL:{
				throw new RuntimeException( "not yet implemented" );
			}
			case POWER:{
				throw new RuntimeException( "not yet implemented" );
			}
		}
		throw new RuntimeException( "unhandled branch" );
	}
	private char getBytecodeDatatypeChar(Datatype datatype) {
		char result;
		switch (datatype) {
			case BOOL:
			case I8:
			case I16:
			case I32:
				result = 'i';
				break;
			case I64:
				result = 'l';
				break;
			case F32:
				result = 'f';
				break;
			case F64:
				result = 'd';
				break;
			default:
				throw new RuntimeException("unhandled branch");
		}
		return result;
	}
}
