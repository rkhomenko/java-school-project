package org.khomenko.project.core.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MultiThreadScheduled {
    int threads() default 1;
    int initDelay() default -1;
    int period() default -1;
    int initDelayVar() default 0;
    int periodVar() default 0;
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
