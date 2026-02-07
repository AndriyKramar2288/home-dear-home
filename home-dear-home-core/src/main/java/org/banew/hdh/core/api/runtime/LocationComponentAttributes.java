package org.banew.hdh.core.api.runtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LocationComponentAttributes {

    String name();
    String description();
    boolean supportGeneration() default false;
    boolean supportProcessing() default false;
    Argument[] generationArguments() default {};
    Argument[] processingArguments() default {};

    @interface Argument {
        String name();
        String desc();
        String format();
        boolean required() default false;
        String dependsOn() default "";
    }
}