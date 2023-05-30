package de.dhbw.mh.slang.antlr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.stream.Stream;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class AstBuilderAtomicsTest extends AstBuilderUtils {

	private static Stream<Arguments> atomicExpressions() {
		return Stream.of(
				Arguments.of( "11",   "11i8" ),
				Arguments.of( "353",  "353i16" ),
				Arguments.of( "37i8", "37i8" ),
				Arguments.of( "abc",  "abc" ),
				Arguments.of( "(19)", "19i8" )
		);
	}
	
	@ParameterizedTest
	@MethodSource("atomicExpressions")
	void createsSubtreeForAtomicexpression( String input, String expectedTree ){
		ParseTree tree = parse( input ).atomicExpression( );
		AstBuilder spy = Mockito.spy( AstBuilder.class );
		Mockito.doReturn( L1 ).when( spy ).visitLogicalOrExpression( any() );
		assertThat( spy.visit(tree).toString() ).isEqualTo( expectedTree );
	}

}
