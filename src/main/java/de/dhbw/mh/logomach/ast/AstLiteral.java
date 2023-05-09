package de.dhbw.mh.logomach.ast;

import de.dhbw.mh.logomach.Value;

public class AstLiteral extends AstNode {
	
	public final Value VALUE;
	
	public AstLiteral( Value value ){
		super( );
		VALUE = value;
	}
	
	@Override
	public String toString( ){
		return VALUE.toString();
	}

}
