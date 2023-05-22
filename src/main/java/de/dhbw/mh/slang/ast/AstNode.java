package de.dhbw.mh.slang.ast;

public abstract class AstNode {

	abstract public <T> T accept( AstVisitor<T> visitor );

}
