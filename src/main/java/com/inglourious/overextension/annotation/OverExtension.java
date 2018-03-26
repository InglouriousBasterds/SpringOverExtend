package com.inglourious.overextension.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gbrescia on 26/03/2018.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
//@ComponentScan(nameGenerator = ExtensionAnnotationBeanNameGenerator.class)
public @interface OverExtension {

    String value() default "";

    String extension() default "abstract";

}
