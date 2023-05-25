package de.dhbw.mh.slang.ast;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import de.dhbw.mh.slang.I32;
import de.dhbw.mh.slang.ast.AstBinaryOperation;
import de.dhbw.mh.slang.ast.AstLiteral;
import de.dhbw.mh.slang.ast.AstUnaryOperation;
import de.dhbw.mh.slang.ast.AstVariable;
import de.dhbw.mh.slang.ast.AstVisitor;

class AstVisitorTest {
	
	@Test
	void traversalLogicIsCorrect( ){
		AstLiteral lit1 = new AstLiteral( new I32(97) );
		AstLiteral lit2 = new AstLiteral( new I32(61) );
		AstLiteral lit3 = new AstLiteral( new I32(53) );
		AstUnaryOperation unary1 = new AstUnaryOperation( null, AstUnaryOperation.Operator.NEGATIVE_SIGN, lit2 );
		AstBinaryOperation binop1 = new AstBinaryOperation( null, lit1, AstBinaryOperation.Operator.SUBTRACT, unary1 );
		AstUnaryOperation unary2 = new AstUnaryOperation( null, AstUnaryOperation.Operator.NEGATIVE_SIGN, binop1 );
		AstBinaryOperation binop2 = new AstBinaryOperation( null, unary2, AstBinaryOperation.Operator.ADD, lit3 );
		
		DummyVisitor spy = Mockito.spy( new DummyVisitor() );
		InOrder chronology = Mockito.inOrder( spy );
		binop2.accept( spy );
		chronology.verify( spy ).visit( binop2 );
		chronology.verify( spy ).visit( unary2 );
		chronology.verify( spy ).visit( binop1 );
		chronology.verify( spy ).visit( lit1 );
		chronology.verify( spy ).visit( unary1 );
		chronology.verify( spy ).visit( lit2 );
		chronology.verify( spy ).visitPost( unary1, null );
		chronology.verify( spy ).visitPost( binop1, null, null );
		chronology.verify( spy ).visitPost( unary2, null );
		chronology.verify( spy ).visit( lit3 );
		chronology.verify( spy ).visitPost( binop2, null, null );
	}
	
	
	
	private static class DummyVisitor implements AstVisitor<Void> {
		
		private DummyVisitor( ){
			super( );
		}

		@Override
		public Void visit( AstLiteral literal ){
			return null;
		}

		@Override
		public Void visit( AstVariable variable ){
			return null;
		}

		@Override
		public Void visitPost( AstUnaryOperation node, Void base ){
			return null;
		}

		@Override
		public Void visitPost( AstBinaryOperation node, Void lhs, Void rhs ){
			return null;
		}
		
	}

}
