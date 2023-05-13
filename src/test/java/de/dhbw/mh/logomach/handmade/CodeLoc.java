package de.dhbw.mh.logomach.handmade;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.converter.ConvertWith;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention( RetentionPolicy.RUNTIME )
@ConvertWith( CodeLocationConverter.class )
public @interface CodeLoc {

}