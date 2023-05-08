package de.dhbw.mh.logomach;

public class F32 extends NumericValue {
	
	public final float VALUE;
	
	public F32( float value ){
		super( );
		VALUE = value;
	}
	
	@Override
	public String toString( ){
		return String.format( "%sf32", VALUE );
	}

}
