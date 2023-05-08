package de.dhbw.mh.logomach.handmade;

public class CodeLocation {

	public final int LINE, COLUMN, OFFSET;
	
	public CodeLocation( int line, int column, int offset ){
		super( );
		LINE = line;
		COLUMN = column;
		OFFSET = offset;
	}
	
	@Override
	public String toString( ){
		return String.format( "%d:%d:%d", LINE, COLUMN, OFFSET );
	}
	
}
