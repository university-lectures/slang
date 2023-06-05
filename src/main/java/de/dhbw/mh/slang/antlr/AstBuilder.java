package de.dhbw.mh.slang.antlr;

import de.dhbw.mh.slang.NumericValue;
import de.dhbw.mh.slang.antlr.SlangParser.AdditiveExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.AtomicExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.EqualityExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.ExponentiationContext;
import de.dhbw.mh.slang.antlr.SlangParser.LogicalAndExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.LogicalOrExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.MultiplicativeExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.RelationalExpressionContext;
import de.dhbw.mh.slang.antlr.SlangParser.SignedTermContext;
import de.dhbw.mh.slang.ast.*;
import de.dhbw.mh.slang.craft.parser.CraftedSlangParser;
import static de.dhbw.mh.slang.craft.Token.Type.MINUS;
import static de.dhbw.mh.slang.craft.Token.Type.PLUS;

import de.dhbw.mh.slang.craft.lexer.NumericalEvaluator;
import org.antlr.v4.runtime.tree.TerminalNode;
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
	public AstNode visitEqualityExpression(EqualityExpressionContext ctx) {
		AstNode left = null;
		for (int i = 0; i < ctx.children.size(); i++) {
			if (i == 0) {
				left = visitRelationalExpression(ctx.relationalExpression().get(i));
			}

			if (ctx.children.get(i) instanceof TerminalNodeImpl) {
				String operator = ((TerminalNodeImpl) ctx.children.get(i)).getSymbol().getText();
				switch (((TerminalNodeImpl) ctx.children.get(i)).getSymbol().getType()) {
					case SlangLexer.EQ:
						left = new AstBinaryOperation(null, left, AstBinaryOperation.Operator.COMPARE_EQUAL, visitRelationalExpression(ctx.relationalExpression(i + 1)));
						break;
					case SlangLexer.NE:
						left = new AstBinaryOperation(null, left, AstBinaryOperation.Operator.COMPARE_UNEQUAL, visitRelationalExpression(ctx.relationalExpression(i + 1)));
						break;
					default:
						throw new RuntimeException("Unknown operator");
				}
			}
		}

		return left;
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
		if(ctx.IDENTIFIER() != null){
			return new AstVariable(ctx.getText());
		}
		if(ctx.NUMERIC_LITERAL() != null){
			return new AstLiteral(NumericalEvaluator.parse(ctx.getText()));
		}
		if(ctx.LPAREN() != null){
			return this.visitLogicalOrExpression(ctx.logicalOrExpression());
		}
		return super.visitAtomicExpression( ctx );
	}

}
