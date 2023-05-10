package de.dhbw.mh.logomach.ast;

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
