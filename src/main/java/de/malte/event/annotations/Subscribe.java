package de.malte.event.annotations;

import de.malte.event.enums.Priority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    Priority value() default Priority.DEFAULT;
    boolean ignoreCancelled() default true;
}
