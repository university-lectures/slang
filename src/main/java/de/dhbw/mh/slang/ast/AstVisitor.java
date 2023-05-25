package de.dhbw.mh.slang.ast;

public interface AstVisitor<T> {

	public T visit( AstLiteral literal );
	public T visit( AstVariable variable );
	
	default public T visit( AstUnaryOperation operation ){
		T operand = operation.BASE.accept( this );
		return visitPost( operation, operand );
	}
	public T visitPost( AstUnaryOperation node, T base );
	
	default public T visit( AstBinaryOperation operation ){
		T first = operation.FIRST.accept( this );
		T second = operation.SECOND.accept( this );
		return visitPost( operation, first, second );
	}
	public T visitPost( AstBinaryOperation node, T base, T second );
	
}
