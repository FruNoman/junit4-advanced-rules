package com.github.frunoman.rules.repeat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( {
        java.lang.annotation.ElementType.METHOD
} )
public @interface Repeat {
    public  int times();
}
