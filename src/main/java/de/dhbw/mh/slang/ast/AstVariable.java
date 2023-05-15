package de.dhbw.mh.slang.ast;

public class AstVariable extends AstNode {
	
	public final String NAME;
	
	public AstVariable( String name ){
		super( );
		NAME = name;
	}
	
	@Override
	public String toString( ){
		return NAME;
	}

}
