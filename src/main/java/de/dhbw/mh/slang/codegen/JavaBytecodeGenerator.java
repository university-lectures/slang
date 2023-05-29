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
			return String.format("bipush %s%n", ((I8) literal.VALUE).VALUE);
		}else if( literal.VALUE instanceof I16 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof I32 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof I64 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof F32 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof F64 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof Bool ){
			String appendix = (((Bool) literal.VALUE).VALUE) ? "1" : "0";
			return String.format("iconst_%s%n", appendix);
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
		char datatype = getNodeDatatype(node);

		switch( node.OPERATOR ){
			case ADD:{ // lhs rhs add
				return String.format("%s%s%sadd%n", lhs, rhs, datatype);
			}
			case SUBTRACT:{
				throw new RuntimeException( "not yet implemented" );
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
