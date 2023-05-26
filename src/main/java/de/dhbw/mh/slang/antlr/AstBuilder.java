package de.dhbw.mh.slang.antlr;

import de.dhbw.mh.slang.antlr.SlangParser.AtomicExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.ExponentiationContext;
import de.dhbw.mh.slang.ast.AstNode;

public class AstBuilder extends SlangBaseVisitor<AstNode> {
	
	public AstBuilder( ){
		super( );
	}
	
	@Override
	public AstNode visitExponentiation( ExponentiationContext ctx ){
		// TODO Auto-generated method stub
		return super.visitExponentiation( ctx );
	}
	
	@Override
	public AstNode visitAtomicExpression( AtomicExpressionContext ctx ){
		// TODO Auto-generated method stub
		return super.visitAtomicExpression( ctx );
	}

}
