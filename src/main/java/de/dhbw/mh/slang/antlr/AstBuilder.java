package de.dhbw.mh.slang.antlr;

import de.dhbw.mh.slang.antlr.SlangParser.AdditiveExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.AtomicExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.EqualityExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.ExponentiationContext;
import de.dhbw.mh.slang.antlr.SlangParser.LogicalAndExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.LogicalOrExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.MultiplicativeExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.RelationalExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.SignedTermContext;
import de.dhbw.mh.slang.ast.AstNode;
import de.dhbw.mh.slang.ast.AstUnaryOperation;
import de.dhbw.mh.slang.craft.parser.CraftedSlangParser;

import static de.dhbw.mh.slang.craft.Token.Type.MINUS;
import static de.dhbw.mh.slang.craft.Token.Type.PLUS;

public class AstBuilder extends SlangBaseVisitor<AstNode> {
	
	public AstBuilder( ){
		super( );
	}
	
	
	@Override
	public AstNode visitLogicalOrExpression( LogicalOrExpressionContext ctx ){
		return super.visitLogicalOrExpression( ctx );
	}
	
	@Override
	public AstNode visitLogicalAndExpression( LogicalAndExpressionContext ctx ){
		return super.visitLogicalAndExpression( ctx );
	}
	
	@Override
	public AstNode visitEqualityExpression( EqualityExpressionContext ctx ){
		return super.visitEqualityExpression( ctx );
	}
	
	@Override
	public AstNode visitRelationalExpression( RelationalExpressionContext ctx ){
		return super.visitRelationalExpression( ctx );
	}
	
	@Override
	public AstNode visitAdditiveExpression( AdditiveExpressionContext ctx ){
		return super.visitAdditiveExpression( ctx );
	}
	
	@Override
	public AstNode visitMultiplicativeExpression( MultiplicativeExpressionContext ctx ){
		return super.visitMultiplicativeExpression( ctx );
	}
	
	@Override
	public AstNode visitSignedTerm( SignedTermContext ctx ){
		if(ctx.PLUS() != null) {
			return new AstUnaryOperation(
					null,
					AstUnaryOperation.Operator.POSITIVE_SIGN,
					this.visit(ctx.exponentiation())
			);
		}
		if(ctx.MINUS() != null) {
			return new AstUnaryOperation(
					null,
					AstUnaryOperation.Operator.NEGATIVE_SIGN,
					this.visit(ctx.exponentiation())
			);
		}

		return this.visit(ctx.exponentiation());
	}
	
	@Override
	public AstNode visitExponentiation( ExponentiationContext ctx ){
		return super.visitExponentiation( ctx );
	}
	
	@Override
	public AstNode visitAtomicExpression( AtomicExpressionContext ctx ){
		return super.visitAtomicExpression( ctx );
	}

}
