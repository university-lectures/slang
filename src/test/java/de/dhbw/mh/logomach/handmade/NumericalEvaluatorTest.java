package de.dhbw.mh.logomach.handmade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.dhbw.mh.logomach.F32;
import de.dhbw.mh.logomach.F64;
import de.dhbw.mh.logomach.I16;
import de.dhbw.mh.logomach.I32;
import de.dhbw.mh.logomach.I64;
import de.dhbw.mh.logomach.I8;
import de.dhbw.mh.logomach.NumericValue;

class NumericalEvaluatorTest {
	
	
	private static Stream<Arguments> correctDecompositions() {
		return Stream.of(
				Arguments.of( "42",           new NumericalEvaluator.Info( null,    "42", null, null, null ) ),
				Arguments.of( "+3",           new NumericalEvaluator.Info(  "+",     "3", null, null, null ) ),
				Arguments.of( "-349",         new NumericalEvaluator.Info(  "-",   "349", null, null, null ) ),
				Arguments.of( "47.11",        new NumericalEvaluator.Info( null,    "47", "11", null, null ) ),
				Arguments.of( "81i1",         new NumericalEvaluator.Info( null,    "81", null,  "i", "1" ) ),
				Arguments.of( "35b7",         new NumericalEvaluator.Info( null,    "35", null,  "b", "7" ) ),
				Arguments.of( "-1393.52f12",  new NumericalEvaluator.Info(  "-",  "1393", "52",  "f", "12" ) ),
				Arguments.of( "-1_393.52f12", new NumericalEvaluator.Info(  "-", "1_393", "52",  "f", "12" ) )
		);
	}
	
	private static Stream<Arguments> incorrectDecompositions() {
		return Stream.of(
				Arguments.of( "a42" ),
				Arguments.of( "+3i" ),
				Arguments.of( "a42" ),
				Arguments.of( " -349" )
		);
	}
	
	private static Stream<Arguments> incorrectLiterals() {
		return Stream.of(
				Arguments.of( "1.23_4",         "'1.23_4' is not a valid literal." ),
				Arguments.of( "35b7",           "unknown type suffix 'b7' in literal '35b7'" ),
				Arguments.of( "81i1",           "integer data types exist only in lengths of 8, 16, 32 and 64 bits" ),
				Arguments.of( "47.11i8",        "integer data types cannot store decimal places" ),
				Arguments.of( "128i8",          "the value '128' is outside the range of data type i8" ),
				Arguments.of( "-129i8",         "the value '-129' is outside the range of data type i8" ),
				Arguments.of( "32768i16",       "the value '32768' is outside the range of data type i16" ),
				Arguments.of( "-32769i16",      "the value '-32769' is outside the range of data type i16" ),
				Arguments.of( "2147483648i32",  "the value '2147483648' is outside the range of data type i32" ),
				Arguments.of( "-2147483649i32", "the value '-2147483649' is outside the range of data type i32" )
//				Arguments.of( "9223372036854775808i64",
//						"the value '9223372036854775808' is outside the range of data type i64" ),
//				Arguments.of( "-9223372036854775809i64",
//						"the value '-9223372036854775809' is outside the range of data type i64" )
		);
	}
	
	private static Stream<Arguments> correct_i8_literals() {
		return Stream.of(
				Arguments.of( "127",    (byte)127 ),
				Arguments.of( "-128",   (byte)-128 ),
				Arguments.of( "0",      (byte)0 ),
				Arguments.of( "17i8",   (byte)17 ),
				Arguments.of( "-53i8",  (byte)-53 ),
				Arguments.of( "1_23",   (byte)123 ),
				Arguments.of( "-2_9",   (byte)-29 )
		);
	}
	
	private static Stream<Arguments> correct_i16_literals() {
		return Stream.of(
				Arguments.of( "32767",   (short)32767 ),
				Arguments.of( "-32768",  (short)-32768 ),
				Arguments.of( "0i16",    (short)0 ),
				Arguments.of( "46i16",   (short)46 ),
				Arguments.of( "-111i16", (short)-111 )
		);
	}
	
	private static Stream<Arguments> correct_i32_literals() {
		return Stream.of(
				Arguments.of( "2147483647",  (int)2147483647 ),
				Arguments.of( "-2147483648", (int)-2147483648 ),
				Arguments.of( "0i32",        (int)0 ),
				Arguments.of( "9i32",        (int)9 ),
				Arguments.of( "-121i32",     (int)-121 )
		);
	}
	
	private static Stream<Arguments> correct_i64_literals() {
		return Stream.of(
				Arguments.of( "9223372036854775807",  9223372036854775807l ),
				Arguments.of( "-9223372036854775808", -9223372036854775808l ),
				Arguments.of( "0i64",                 0l ),
				Arguments.of( "76i64",                76l ),
				Arguments.of( "-56i64",               -56l )
		);
	}
	
	private static Stream<Arguments> correct_f32_literals() {
		return Stream.of(
				Arguments.of( "23f32",   23f ),
				Arguments.of( "23.2",    23.2f ),
				Arguments.of( "0.5",     0.5f ),
				Arguments.of( "1_23.2",  123.2f ),
				Arguments.of( "+2f32",   2f ),
				Arguments.of( "-2f32",  -2f )
		);
	}
	
	private static Stream<Arguments> correct_f64_literals() {
		return Stream.of(
				Arguments.of( "23f64",      23d ),
				Arguments.of( "23.2f64",    23.2d ),
				Arguments.of( "0.5f64",     0.5d ),
				Arguments.of( "1_23.2f64",  123.2d ),
				Arguments.of( "+2f64",      2d ),
				Arguments.of( "-2f64",     -2d )
		);
	}
	
	
	
	@ParameterizedTest
	@MethodSource("correctDecompositions")
	void decomposesCorrectLexemes( String lexeme, NumericalEvaluator.Info expected ){
		NumericalEvaluator.Info info = NumericalEvaluator.decompose( lexeme );
		assertThat( info.SIGN ).isEqualTo( expected.SIGN );
		assertThat( info.INTEGER_PART ).isEqualTo( expected.INTEGER_PART );
		assertThat( info.FRACTIONAL_PART ).isEqualTo( expected.FRACTIONAL_PART );
		assertThat( info.TYPE_LETTER ).isEqualTo( expected.TYPE_LETTER );
		assertThat( info.TYPE_SIZE ).isEqualTo( expected.TYPE_SIZE );
	}
	
	@ParameterizedTest
	@MethodSource("incorrectDecompositions")
	void throwsDecompositionException( String lexeme ){
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			NumericalEvaluator.decompose( lexeme );
		}).withMessage( "'%s' is not a valid literal.", lexeme );
	}
	
	@ParameterizedTest
	@MethodSource("correct_i8_literals")
	void parsesValid_i8( String lexeme, byte expected ){
		NumericValue value = NumericalEvaluator.parse( lexeme );
		assertThat( value ).isInstanceOf( I8.class );
		assertThat( ((I8)value).VALUE ).isEqualTo( expected );
	}
	
	@ParameterizedTest
	@MethodSource("correct_i16_literals")
	void parsesValid_i16( String lexeme, short expected ){
		NumericValue value = NumericalEvaluator.parse( lexeme );
		assertThat( value ).isInstanceOf( I16.class );
		assertThat( ((I16)value).VALUE ).isEqualTo( expected );
	}
	
	@ParameterizedTest
	@MethodSource("correct_i32_literals")
	void parsesValid_i32( String lexeme, int expected ){
		NumericValue value = NumericalEvaluator.parse( lexeme );
		assertThat( value ).isInstanceOf( I32.class );
		assertThat( ((I32)value).VALUE ).isEqualTo( expected );
	}
	
	@ParameterizedTest
	@MethodSource("correct_i64_literals")
	void parsesValid_i64( String lexeme, long expected ){
		NumericValue value = NumericalEvaluator.parse( lexeme );
		assertThat( value ).isInstanceOf( I64.class );
		assertThat( ((I64)value).VALUE ).isEqualTo( expected );
	}
	
	@ParameterizedTest
	@MethodSource("correct_f32_literals")
	void parsesValid_f32( String lexeme, float expectedValue ){
		F32 actual = (F32) NumericalEvaluator.parse( lexeme );
		assertThat( actual.VALUE ).isEqualTo( expectedValue );
	}
	
	@ParameterizedTest
	@MethodSource("correct_f64_literals")
	void parsesValid_f64( String lexeme, double expectedValue ){
		F64 actual = (F64) NumericalEvaluator.parse( lexeme );
		assertThat( actual.VALUE ).isEqualTo( expectedValue );
	}
	
	@ParameterizedTest
	@MethodSource("incorrectLiterals")
	void throwsParsingException( String lexeme, String message ){
		assertThatExceptionOfType( RuntimeException.class ).isThrownBy(()->{
			NumericalEvaluator.parse( lexeme );
		}).withMessage( message );
	}

}
