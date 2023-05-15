package de.dhbw.mh.slang;

public class I8 extends NumericValue {
	
	public final byte VALUE;
	
	public I8( byte value ){
		super( );
		VALUE = value;
	}
	
	@Override
	public String toString( ){
		return String.format( "%di8", VALUE );
	}

}
