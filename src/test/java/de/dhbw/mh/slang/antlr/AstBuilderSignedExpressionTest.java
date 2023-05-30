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
class AstBuilderSignedExpressionTest extends AstBuilderUtils {

	private static Stream<Arguments> signedExpressions() {
		return Stream.of(
				Arguments.of( "19i8",  "19i8" ),
				Arguments.of( "+19i8", "+19i8" ),
				Arguments.of( "-19i8", "-19i8" )
		);
	}
	
	@ParameterizedTest
	@MethodSource("signedExpressions")
	void createsSubtreeForSignedExpressions( String input, String expectedTree ){
		ParseTree tree = parse( input ).signedTerm( );
		AstBuilder spy = Mockito.spy( AstBuilder.class );
		Mockito.doReturn( L1 ).when( spy ).visitExponentiation( any() );
		assertThat( spy.visit(tree).toString() ).isEqualTo( expectedTree );
	}

}
