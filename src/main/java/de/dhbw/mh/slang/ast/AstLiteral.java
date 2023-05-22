package de.dhbw.mh.slang.ast;

import de.dhbw.mh.slang.Value;

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

	@Override
	public <T> T accept( AstVisitor<T> visitor ){
		return visitor.visit( this );
	}

}
