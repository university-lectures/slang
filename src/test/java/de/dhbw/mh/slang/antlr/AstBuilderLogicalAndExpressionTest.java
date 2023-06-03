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

@Disabled
class AstBuilderLogicalAndExpressionTest extends AstBuilderUtils {

	private static Stream<Arguments> logicalAndExpressions() {
		return Stream.of(
				Arguments.of( "19i8",                       "19i8" ),
				Arguments.of( "19i8 && 23i8",               "(19i8&&23i8)" ),
				Arguments.of( "19i8 && 23i8 && 37i8",        "((19i8&&23i8)&&37i8)" ),
				Arguments.of( "19i8 && 23i8 && 37i8 && 41i8", "(((19i8&&23i8)&&37i8)&&41i8)" )
		);
	}
	
	@ParameterizedTest
	@MethodSource("logicalAndExpressions")
	void createsSubtreeForLogicalAndExpressions( String input, String expectedTree ){
		ParseTree tree = parse( input ).logicalAndExpression( );
		AstBuilder spy = Mockito.spy( AstBuilder.class );
		Mockito.doReturn( L1 ).doReturn( L2 ).doReturn( L3 ).doReturn( L4 )
				.when( spy ).visitEqualityExpression( any() );
		assertThat( spy.visit(tree).toString() ).isEqualTo( expectedTree );
	}

}
