package de.dhbw.mh.logomach;

public class I32 extends NumericValue {
	
	public final int VALUE;
	
	public I32( int value ){
		super( );
		VALUE = value;
	}
	
	@Override
	public String toString( ){
		return String.format( "%di32", VALUE );
	}

}
