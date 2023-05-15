package de.dhbw.mh.slang;

public class I16 extends NumericValue {
	
	public final short VALUE;
	
	public I16( short value ){
		super( );
		VALUE = value;
	}
	
	@Override
	public String toString( ){
		return String.format( "%di16", VALUE );
	}

}
