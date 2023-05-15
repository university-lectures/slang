package de.dhbw.mh.slang.handmade;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dhbw.mh.slang.F32;
import de.dhbw.mh.slang.F64;
import de.dhbw.mh.slang.I16;
import de.dhbw.mh.slang.I32;
import de.dhbw.mh.slang.I64;
import de.dhbw.mh.slang.I8;
import de.dhbw.mh.slang.NumericValue;

public abstract class NumericalEvaluator {
	
	private static final String OPTIONAL_SIGN = "(\\+|-)?";
	private static final String FRACTIONAL_PART = "(?:\\.(\\d+))?";
	private static final String INTEGER_PART = "([0-9_]+)";
	private static final String TYPE_SUFFIX = "(?:([a-z])(\\d+))?";
	
	private static final Pattern NUMERIC_PATTERN = Pattern.compile(
			"^" + OPTIONAL_SIGN + INTEGER_PART + FRACTIONAL_PART + TYPE_SUFFIX + "$" );

	
	
	public static Info decompose( String lexeme ){
		Matcher matcher = NUMERIC_PATTERN.matcher( lexeme );
		if( !matcher.find() ){
			throw new RuntimeException( String.format("'%s' is not a valid literal.", lexeme) );
		}
		String sign, integers, fractionals, typeLetter, typeSize;
		sign = matcher.group(1);
		integers = matcher.group(2);
		fractionals = matcher.group(3);
		typeLetter = matcher.group(4);
		typeSize = matcher.group(5);
		return new Info( sign, integers, fractionals, typeLetter, typeSize );
	}
	
	
	public static NumericValue parse( String lexeme ){
		Info info = decompose( lexeme );
		if( null != info.TYPE_LETTER ){
			if( info.TYPE_LETTER.equals("i") ){
				return parseInteger( info );
			}
			if( info.TYPE_LETTER.equals("f") ){
				return parseFloat( info );
			}
			throw new RuntimeException( String.format("unknown type suffix '%s%s' in literal '%s'",
					info.TYPE_LETTER, info.TYPE_SIZE, lexeme) );
		}
		if( null != info.FRACTIONAL_PART ){
			return parseFloat( info );
		}
		return parseInteger( info );
	}
	
	
	private static String normalizeLiteral( Info info ){
		StringBuilder builder = new StringBuilder( );
		if( null != info.SIGN && info.SIGN.equals("-") ){
			builder.append( info.SIGN );
		}
		if( null != info.INTEGER_PART ){
			for( int i = 0; i < info.INTEGER_PART.length(); ++i ){
				if( '_' != info.INTEGER_PART.charAt(i) ){
					builder.append( info.INTEGER_PART.charAt(i) );
				}
			}
		}
		if( null != info.FRACTIONAL_PART ){
			builder.append( '.' );
			for( int i = 0; i < info.FRACTIONAL_PART.length(); ++i ){
				builder.append( info.FRACTIONAL_PART.charAt(i) );
			}
		}
		return builder.toString();
	}
	
	
	
	
	private static NumericValue parseInteger( Info info ){
		if( null != info.FRACTIONAL_PART ){
			throw new RuntimeException( "integer data types cannot store decimal places" );
		}
		String normalizedString = normalizeLiteral( info );
		long value = Long.parseLong( normalizedString );
		if( null == info.TYPE_SIZE ){
			if( (byte)value == value ){
				return new I8( (byte)value );
			}
			if( (short)value == value ){
				return new I16( (short)value );
			}
			if( (int)value == value ){
				return new I32( (int)value );
			}
			return new I64( value );
		}
		if( info.TYPE_SIZE.equals("8") ){
			if( (byte)value != value ){
				throw new RuntimeException( String.format( "the value '%s' is outside the range of data type i8", normalizedString ) );
			}
			return new I8( (byte)value );
		}
		if( info.TYPE_SIZE.equals("16") ){
			if( (short)value != value ){
				throw new RuntimeException( String.format( "the value '%s' is outside the range of data type i16", normalizedString ) );
			}
			return new I16( (short)value );
		}
		if( info.TYPE_SIZE.equals("32") ){
			if( (int)value != value ){
				throw new RuntimeException( String.format( "the value '%s' is outside the range of data type i32", normalizedString ) );
			}
			return new I32( (int)value );
		}
		if( info.TYPE_SIZE.equals("64") ){
			return new I64( value );
		}
		throw new RuntimeException( "integer data types exist only in lengths of 8, 16, 32 and 64 bits" );
	}
	
	
	
	private static NumericValue parseFloat( Info info ){
		String normalizedString = normalizeLiteral( info );
		if( null == info.TYPE_SIZE ){
			float value = Float.parseFloat( normalizedString );
			return new F32( value );
		}
		if( info.TYPE_SIZE.equals("32") ){
			float value = Float.parseFloat( normalizedString );
			return new F32( value );
		}
		if( info.TYPE_SIZE.equals("64") ){
			double value = Double.parseDouble( normalizedString );
			return new F64( value );
		}
		throw new RuntimeException( "floating point data types exist only in lengths of 32 and 64 bits" );
	}
	
	
	
	public static class Info {
		public final String SIGN;
		public final String INTEGER_PART;
		public final String FRACTIONAL_PART;
		public final String TYPE_LETTER;
		public final String TYPE_SIZE;
		
		public Info( String sign, String integers, String fractionals, String typeLetter, String typeSize ){
			super( );
			SIGN = sign;
			INTEGER_PART = integers;
			FRACTIONAL_PART = fractionals;
			TYPE_LETTER = typeLetter;
			TYPE_SIZE = typeSize;
		}
		
		@Override
		public String toString( ){
			return String.format( "(%s,%s,%s,%s,%s)",
					SIGN,
					INTEGER_PART,
					FRACTIONAL_PART,
					TYPE_LETTER, TYPE_SIZE );
		}
	}
	
}
