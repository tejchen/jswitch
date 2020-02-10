package com.tejchen.switchclient.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SwitchNamespace {
    String value() default "";
}
