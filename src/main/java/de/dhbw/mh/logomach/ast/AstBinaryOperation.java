package de.dhbw.mh.logomach.ast;

import de.dhbw.mh.logomach.handmade.CodeLocation;

public class AstBinaryOperation extends AstNode {
	
	public static enum Operator {
		ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/"), MODULO("%"), POWER("**"),
		LESS_THAN("<"), GREATER_THAN(">"), LESS_OR_EQUAL("<="), GREATER_OR_EQUAL(">="),
		COMPARE_EQUAL("=="), COMPARE_UNEQUAL("!="),
		LOGICAL_AND("&&"), LOGICAL_OR("||");
		
		private final String OPERATOR;
		
		private Operator( String operator ){
			OPERATOR = operator;
		}
		
		@Override
		public String toString( ){
			return OPERATOR;
		}
	}
	
	public final AstNode FIRST, SECOND;
	public final Operator OPERATOR;

	public AstBinaryOperation( CodeLocation location, AstNode first, Operator operator, AstNode second ){
		super( );
		FIRST = first;
		SECOND = second;
		OPERATOR = operator;
	}
	
	@Override
	public String toString( ){
		return String.format( "(%s%s%s)", FIRST, OPERATOR, SECOND );
	}
	
}
