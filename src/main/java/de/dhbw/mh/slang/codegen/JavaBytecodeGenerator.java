package de.dhbw.mh.slang.codegen;

import de.dhbw.mh.slang.*;
import de.dhbw.mh.slang.ast.*;

public class JavaBytecodeGenerator implements AstVisitor<String> {
	
	public JavaBytecodeGenerator( ){
		super( );
	}

	@Override
	public String visit( AstLiteral literal ){
		if( literal.VALUE instanceof I8 ){
			return "bipush " + ((I8) literal.VALUE).VALUE + "\r\n";
		}else if( literal.VALUE instanceof I16 ){
			return "bipush " + ((I16) literal.VALUE).VALUE + "\r\n";
		}else if( literal.VALUE instanceof I32 ){
			return "bipush " + ((I32) literal.VALUE).VALUE + "\r\n";
		}else if( literal.VALUE instanceof I64 ){
			return "bipush " + ((I64) literal.VALUE).VALUE + "\r\n";
		}else if( literal.VALUE instanceof F32 ){
			return "bipush " + ((F32) literal.VALUE).VALUE + "\r\n";
		}else if( literal.VALUE instanceof F64 ){
			return "bipush " + ((F64) literal.VALUE).VALUE + "\r\n";
		}else if( literal.VALUE instanceof Bool ){
			String appendix = (((Bool) literal.VALUE).VALUE) ? "1" : "0";
			return "iconst_" + appendix + "\r\n";
		}
		throw new RuntimeException( "unhandled branch" );
	}

	private char getNodeDatatype(AstNode node){
		switch (node.getDatatype()){
			case I8:
			case I16:
			case I32:
			case BOOL:
				return 'i';
			case I64:
				return 'l';
			case F32:
				return 'f';
			case F64:
				return 'd';
		}
		throw new RuntimeException( "unhandled branch" );
	}

	@Override
	public String visit( AstVariable variable ){
		throw new RuntimeException( "not yet implemented" );
	}

	@Override
	public String visitPost( AstUnaryOperation node, String base ){
		char Datatype = getNodeDatatype(node);
		switch( node.OPERATOR ){
			case NEGATIVE_SIGN:{
				return String.format("%sneg%n", Datatype);
			}
			case POSITIVE_SIGN:{
				return "";
			}
		}
		throw new RuntimeException( "unhandled branch" );
	}

	@Override
	public String visitPost( AstBinaryOperation node, String lhs, String rhs ){
		char datatype = getNodeDatatype(node);

		switch( node.OPERATOR ){
			case ADD:{ // lhs rhs add
				return String.format("%s%s%sadd%n", lhs, rhs, datatype);
			}
			case SUBTRACT:{
				return String.format("%s%s%ssub%n", lhs, rhs, datatype);
			}
			case MULTIPLY:{
				return String.format("%s%s%smul%n", lhs, rhs, datatype);
			}
			case DIVIDE:{
				return String.format("%s%s%sdiv%n", lhs, rhs, datatype);
			}
			case MODULO:{
				return String.format("%s%s%srem%n", lhs, rhs, datatype);
			}
			case LOGICAL_AND:{
				throw new RuntimeException( "not yet implemented" );
			}
			case LOGICAL_OR:{
				throw new RuntimeException( "not yet implemented" );
			}
			case COMPARE_EQUAL:{
				return String.format("%s%sif_icmpne #0_ne%niconst_1%ngoto #0_end%n#0_ne:%niconst_0%n#0_end:%n", lhs, rhs);
			}
			case COMPARE_UNEQUAL:{
				return String.format("%s%sif_icmpeq #0_eq%niconst_1%ngoto #0_end%n#0_eq:%niconst_0%n#0_end:%n", lhs, rhs);
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

}
