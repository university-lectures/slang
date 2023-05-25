package de.dhbw.mh.slang.ast;

import de.dhbw.mh.slang.Datatype;

public abstract class AstNode {

	protected Datatype inferredType;

	public AstNode( ){
		super( );
	}

	public void setDatatype( Datatype datatype ){
		this.inferredType = datatype;
	}

	public Datatype getDatatype( ){
		return inferredType;
	}

	abstract public <T> T accept( AstVisitor<T> visitor );

}
