package br.com.dsc.lcdpr.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validation {

    char type() default  'C';
    int size() default 255;
    char required() default  'S';
    String defaultValue() default  "";

}
