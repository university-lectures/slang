package de.dhbw.mh.logomach.handmade;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

public class CodeLocationConverter extends TypedArgumentConverter<String, CodeLocation> {
	
	protected CodeLocationConverter( ){
		super( String.class, CodeLocation.class );
	}
	
	@Override
	public CodeLocation convert( String source ) throws ArgumentConversionException {
		String[] parts = source.split( ":" );
		int[] locationComponents = new int[3];
		try {
			for( int i = 0; i < locationComponents.length; ++i ){
				locationComponents[i] = Integer.parseInt( parts[i] );
			}
			return new CodeLocation( locationComponents[0], locationComponents[1], locationComponents[2] );
		} catch( NumberFormatException e ){
			throw new ArgumentConversionException( "Cannot convert into a code location", e );
		}
	}

}
