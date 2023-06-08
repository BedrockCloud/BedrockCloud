package com.bedrockcloud.bedrockcloud.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
@author TeriumCloud
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {

    /**
     * 1 - lowest
     * 5 - highest
     *
     * @return The event priority set for this listener
     */
    int priority() default EventPriority.MEDIUM;
}