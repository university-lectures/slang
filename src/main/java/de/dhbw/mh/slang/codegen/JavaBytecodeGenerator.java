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
	private int counterIDLAND;
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
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof I64 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof F32 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof F64 ){
			throw new RuntimeException( "not yet implemented" );
		}else if( literal.VALUE instanceof Bool ){
			if (((Bool) literal.VALUE).VALUE) {
				return String.format("iconst_1%n");
			} else {
				return String.format("iconst_0%n");
			}
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
				throw new RuntimeException( "not yet implemented" );
			}
			case DIVIDE:{
				throw new RuntimeException( "not yet implemented" );
			}
			case MODULO:{
				throw new RuntimeException( "not yet implemented" );
			}
			case LOGICAL_AND:{
				String template = "%s" +
						"%s%n" +
						"%s" +
						"%s%n" +
						"%s%n" +
						"goto #" + counterIDLAND + "_end%n" +
						"%s%n" +
						"%s%n" +
						"#" + counterIDLAND + "_end:%n";
				String result = String.format( template, lhs, "ifeq #" + counterIDLAND + "_false", rhs, "ifeq #" + counterIDLAND + "_false", "iconst_1", "#" + counterIDLAND + "_false:", "iconst_0" );
				counterIDLAND++;
				return result;
			}
			case LOGICAL_OR:{
				throw new RuntimeException( "not yet implemented" );
			}
			case COMPARE_EQUAL:{
				throw new RuntimeException( "not yet implemented" );
			}
			case COMPARE_UNEQUAL:{
				throw new RuntimeException( "not yet implemented" );
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
