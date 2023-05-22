package de.dhbw.mh.slang.ast;

import de.dhbw.mh.slang.craft.CodeLocation;

public class AstUnaryOperation extends AstNode {
	
	public static enum Operator {
		POSITIVE_SIGN("+"), NEGATIVE_SIGN("-");
		
		private final String OPERATOR;
		
		private Operator( String operator ){
			OPERATOR = operator;
		}
		
		@Override
		public String toString( ){
			return OPERATOR;
		}
	}
	
	public final AstNode BASE;
	public final Operator OPERATOR;

	public AstUnaryOperation( CodeLocation location, Operator operator, AstNode base ){
		super( );
		OPERATOR = operator;
		BASE = base;
	}
	
	@Override
	public String toString( ){
		return String.format( "%s%s", OPERATOR, BASE );
	}

	@Override
	public <T> T accept( AstVisitor<T> visitor ){
		return visitor.visit( this );
	}

}
