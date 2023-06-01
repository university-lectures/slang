package de.dhbw.mh.slang.antlr;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
	AstBuilderLogicalOrExpressionTest.class,
	AstBuilderLogicalAndExpressionTest.class,
	AstBuilderEqualityExpressionTest.class,
	AstBuilderRelationalExpressionTest.class,
	AstBuilderAdditiveExpressionTest.class,
	AstBuilderMultiplicativeExpressionTest.class,
	AstBuilderSignedExpressionTest.class,
	AstBuilderExponentiationTest.class,
	AstBuilderAtomicsTest.class
	})
public class AstBuilderTestSuite {

}
