package com.tejchen.switchclient.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SpringJSwitch {

    String namespace() default "";
}
