package de.dhbw.mh.slang;

public class I64 extends NumericValue {
	
	public final long VALUE;
	
	public I64( long value ){
		super( );
		VALUE = value;
	}
	
	@Override
	public String toString( ){
		return String.format( "%di64", VALUE );
	}

}
