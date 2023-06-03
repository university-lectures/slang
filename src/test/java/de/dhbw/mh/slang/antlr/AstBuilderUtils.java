package de.dhbw.mh.slang.antlr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import de.dhbw.mh.slang.I8;
import de.dhbw.mh.slang.ast.AstLiteral;

public class AstBuilderUtils {
	
	protected static final AstLiteral L1 = new AstLiteral( new I8((byte)19) );
	protected static final AstLiteral L2 = new AstLiteral( new I8((byte)23) );
	protected static final AstLiteral L3 = new AstLiteral( new I8((byte)37) );
	protected static final AstLiteral L4 = new AstLiteral( new I8((byte)41) );
	
	protected static SlangParser parse( String input ){
		CharStream codePointStream = CharStreams.fromString( input );
		SlangLexer lexer = new SlangLexer( codePointStream );
		CommonTokenStream tokenStream = new CommonTokenStream( lexer );
		SlangParser parser = new SlangParser( tokenStream );
		return parser;
	}

}
