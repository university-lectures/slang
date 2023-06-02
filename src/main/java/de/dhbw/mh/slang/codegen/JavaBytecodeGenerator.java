package de.dhbw.mh.slang.codegen;

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
	
	public JavaBytecodeGenerator( ){
		super( );
	}

	@Override
	public String visit( AstLiteral literal ){
		if( literal.VALUE instanceof I8 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof I16 ){
			return String.format("sipush %s%n", ((I16) literal.VALUE).VALUE);
		}else if( literal.VALUE instanceof I32 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof I64 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof F32 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof F64 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof Bool ){
			throw new RuntimeException( "not yet implemented" );
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
		String prefix= "";
		switch (node.getDatatype()) {
			case BOOL: prefix = "i";break;
			case I8: prefix = "i";break;
			case I16: prefix = "i";break;
			case I32: prefix = "i";break;
			case I64: prefix = "l";break;
			case F32: prefix = "f";break;
			case F64: prefix = "d";break;		
			default:
				throw new RuntimeException( "unknown type" );
		}
		switch( node.OPERATOR ){
			case ADD:{
				throw new RuntimeException( "not yet implemented" );
			}
			case SUBTRACT:{
				return String.format("%s%s%ssub%n", lhs, rhs, prefix);
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
			case COMPARE_EQUAL:{
				throw new RuntimeException( "not yet implemented" );
			}
			case COMPARE_UNEQUAL:{
				return String.format( "%s%sif_icmpeq #0_eq%niconst_1%ngoto #0_end%n#0_eq:%niconst_0%n#0_end:%n", lhs, rhs);
			}
			case LESS_THAN:{
				throw new RuntimeException("not yet implemented");
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

}
