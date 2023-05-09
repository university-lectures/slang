package de.dhbw.mh.logomach.ast;

import de.dhbw.mh.logomach.handmade.CodeLocation;

public class AstUnaryOperation extends AstNode {
	
	public static enum Operator {
		POSITIVE_SIGN, NEGATIVE_SIGN;
	}
	
	public final AstNode BASE;
	public final Operator OPERATOR;

	public AstUnaryOperation( CodeLocation location, Operator operator, AstNode base ){
		super( );
		OPERATOR = operator;
		BASE = base;
	}

}
