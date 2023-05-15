package de.dhbw.mh.slang;

public class F64 extends NumericValue {
	
	public final double VALUE;
	
	public F64( double value ){
		super( );
		VALUE = value;
	}
	
	@Override
	public String toString( ){
		return String.format( "%sf64", VALUE );
	}

}
