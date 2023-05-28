package de.dhbw.mh.slang.codegen;

import de.dhbw.mh.slang.Datatype;
import de.dhbw.mh.slang.Bool;
import de.dhbw.mh.slang.F32;
import de.dhbw.mh.slang.F64;
import de.dhbw.mh.slang.I16;
import de.dhbw.mh.slang.I32;
import de.dhbw.mh.slang.I64;
import de.dhbw.mh.slang.I8;
import de.dhbw.mh.slang.ast.AstBinaryOperation;
import de.dhbw.mh.slang.ast.AstLiteral;
import de.dhbw.mh.slang.ast.AstUnaryOperation;
import de.dhbw.mh.slang.ast.AstVariable;
import de.dhbw.mh.slang.ast.AstVisitor;

public class JavaBytecodeGenerator implements AstVisitor<String> {
	
	private int labelCounter = 0;

	public JavaBytecodeGenerator( ){
		super( );
	}

	@Override
	public String visit( AstLiteral literal ){
		if( literal.VALUE instanceof I8 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof I16 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof I32 ){
            return String.format("ldc %s%n", ((I32) literal.VALUE).VALUE);
		}else if( literal.VALUE instanceof I64 ){
			return String.format( "ldc2_w %s%n",  ((I64) literal.VALUE).VALUE);
		}else if( literal.VALUE instanceof F32 ){
			return String.format("ldc %sf%n", ((F32) literal.VALUE).VALUE);
		}else if( literal.VALUE instanceof F64 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof Bool ){
			if(((Bool) literal.VALUE).VALUE){
				return String.format("iconst_1%n");
			}
			return String.format("iconst_0%n");
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
		switch( node.OPERATOR ){
			case ADD:{
				throw new RuntimeException( "not yet implemented" );
			}
			case SUBTRACT:{
				throw new RuntimeException( "not yet implemented" );
			}
			case MULTIPLY:{
                char resultChar = getBytecodeDatatypeChar(node.getDatatype());
                return String.format("%s%s%cmul%n", lhs, rhs, resultChar);
			}
			case DIVIDE:{
				String pattern = "%s%s%s%n";
				switch ( node.getDatatype() )
				{
					case I64:
						return String.format( pattern, lhs, rhs, "ldiv" );
					case F64:
						return String.format( pattern, lhs, rhs, "ddiv" );
					case F32:
						return String.format( pattern, lhs, rhs, "fdiv" );
					default:
						return String.format( pattern, lhs, rhs, "idiv" );
				}

			}
			case MODULO:{
				StringBuilder builder = new StringBuilder();
				builder.append(lhs);
				builder.append(rhs);
				switch (node.getDatatype()) {
					case BOOL: case I8: case I16: case I32:
						builder.append("i");
						break;
					case I64:
						builder.append("l");
						break;
					case F32:
						builder.append("f");
						break;
					case F64:
						builder.append("d");
						break;
				}
				builder.append(String.format("rem%n"));
				return builder.toString();
			}
			case LOGICAL_AND:{
				throw new RuntimeException( "not yet implemented" );
			}
			case LOGICAL_OR:{
				StringBuilder stringBuilder = new StringBuilder();

				stringBuilder.append(lhs);
				stringBuilder.append("ifne #" + labelCounter + "_true%n");
				stringBuilder.append(rhs);
				stringBuilder.append("ifne #" + labelCounter + "_true%n");
				stringBuilder.append("iconst_0%n");
				stringBuilder.append("goto #" + labelCounter + "_end%n");
				stringBuilder.append("#" + labelCounter + "_true:%n");
				stringBuilder.append("iconst_1%n");
				stringBuilder.append("#" + labelCounter + "_end:%n");

				labelCounter++;
				return String.format(stringBuilder.toString());
			}
			case COMPARE_EQUAL:{
				throw new RuntimeException( "not yet implemented" );
			}
			case COMPARE_UNEQUAL:{
				throw new RuntimeException( "not yet implemented" );
			}
			case LESS_THAN:{
                var sb = new StringBuilder();
                sb.append(lhs);
                sb.append(rhs);
                sb.append("if_icmpge #" + labelCounter + "_ge%n");
                sb.append("iconst_1%n");
                sb.append("goto #" + labelCounter + "_end%n");
                sb.append("#" + labelCounter + "_ge:%n");
                sb.append("iconst_0%n");
                sb.append("#" + labelCounter + "_end:%n");
                return String.format(sb.toString());
            }
			case GREATER_OR_EQUAL:{
				throw new RuntimeException( "not yet implemented" );
			}
			case GREATER_THAN:{
				String string = String.format( "%s%s%s%n", lhs, rhs, "if_icmple #0_le" );
				string += String.format( "%s%n", "iconst_1" );
				string += String.format( "%s%n", "goto #0_end" );
				string += String.format( "%s%n", "#0_le:" );
				string += String.format( "%s%n", "iconst_0" );
				string += String.format( "%s%n", "#0_end:" );
				return string;
			}
			case LESS_OR_EQUAL:{
				StringBuilder builder = new StringBuilder();
				builder.append(lhs);
				builder.append(rhs);
				builder.append(String.format("if_icmpgt #0_gt%n"));
				builder.append(String.format("iconst_1%n"));
				builder.append(String.format("goto #0_end%n"));
				builder.append(String.format("#0_gt:%n"));
				builder.append(String.format("iconst_0%n"));
				builder.append(String.format("#0_end:%n"));
				return builder.toString();
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
