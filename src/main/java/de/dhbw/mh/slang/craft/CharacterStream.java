package de.dhbw.mh.slang.craft;

import java.util.Iterator;

public abstract class CharacterStream implements Iterator<Character> {
	
	public static CharacterStream fromString( String input ){
		return new StringStream( input );
	}
	
	abstract public char lookahead( );
	
	abstract public void resetOn( CodeLocation location );
	
}
