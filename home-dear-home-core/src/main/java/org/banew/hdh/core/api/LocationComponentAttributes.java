package org.banew.hdh.core.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LocationComponentAttributes {

    String name();
    String description();
    Argument[] generationArguments();
    Argument[] processingArguments();

    @interface Argument {
        String name();
        String desc();
        String format();
    }
}