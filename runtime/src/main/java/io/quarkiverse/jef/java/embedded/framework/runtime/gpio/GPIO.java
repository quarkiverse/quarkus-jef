package io.quarkiverse.jef.java.embedded.framework.runtime.gpio;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.enterprise.util.Nonbinding;
import jakarta.inject.Qualifier;

@Qualifier
@Target({ FIELD, METHOD, PARAMETER })
@Retention(RUNTIME)
public @interface GPIO {
    @Nonbinding
    int number();

    @Nonbinding
    String name();
}
