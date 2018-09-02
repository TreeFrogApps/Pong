package com.treefrogapps.pong.model;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
public @interface RandomSupplier {

    enum Type {
        VELOCITY, ANGLE
    }

    Type value() default Type.VELOCITY;
}
