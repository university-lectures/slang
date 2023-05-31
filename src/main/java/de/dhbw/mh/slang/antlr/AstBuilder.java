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
import de.dhbw.mh.slang.ast.AstBinaryOperation;
import de.dhbw.mh.slang.ast.AstNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

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
        if (ctx.children.size() == 1)
        {
            return visitSignedTerm(ctx.signedTerm(0));
        }

        AstNode left = null;
        for (int i = 0; i < ctx.children.size(); i++)
        {
            if (i == 0)
            {
                left = visitSignedTerm(ctx.signedTerm(i));
            }

            if (ctx.children.get(i) instanceof TerminalNodeImpl)
            {
                switch (((TerminalNodeImpl) ctx.children.get(i)).getSymbol().getType())
                {
                    case SlangLexer.MUL:
                        left = new AstBinaryOperation(null, left, AstBinaryOperation.Operator.MULTIPLY, visitSignedTerm(ctx.signedTerm(i + 1)));
                        break;
                    case SlangLexer.DIV:
                        left = new AstBinaryOperation(null, left, AstBinaryOperation.Operator.DIVIDE, visitSignedTerm(ctx.signedTerm(i + 1)));
                        break;
                    case SlangLexer.MOD:
                        left = new AstBinaryOperation(null, left, AstBinaryOperation.Operator.MODULO, visitSignedTerm(ctx.signedTerm(i + 1)));
                        break;
                    default:
                        throw new RuntimeException("Unknown operator");
                }
            }
        }

        return left;
	}
	
	@Override
	public AstNode visitSignedTerm( SignedTermContext ctx ){
		return super.visitSignedTerm( ctx );
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
