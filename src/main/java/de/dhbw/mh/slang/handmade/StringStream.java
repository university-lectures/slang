package de.dhbw.mh.slang.handmade;

public class StringStream extends CharacterStream {
	
	private final String STRING;
	private int position;
	
	public StringStream( String string ){
		super( );
		STRING = string;
		position = 0;
	}
	
	@Override
	public char lookahead( ){
		return STRING.charAt( position );
	}

	@Override
	public boolean hasNext( ){
		return position < STRING.length();
	}
	
	@Override
	public Character next( ){
		return STRING.charAt( position++ );
	}

	@Override
	public void resetOn( CodeLocation location ){
		position = location.OFFSET;
	}
}
