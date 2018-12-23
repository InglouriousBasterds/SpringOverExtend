package com.inglourious.overextension.annotation;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.springframework.util.StringUtils.isEmpty;

class AnnotatedBean {

    private final String name;
    private final AnnotatedBeanDefinition definition;

    AnnotatedBean(String name, AnnotatedBeanDefinition definition) {
        this.name = name;
        this.definition = definition;
    }

    Optional<String> superClassName() {
        String superClassName = definition.getMetadata().getSuperClassName();
        return isEmpty(superClassName) || isObjectClass(superClassName) ? empty() :
                of(superClassName);
    }

    private boolean isObjectClass(String superClassName) {
        return Object.class.getCanonicalName().equalsIgnoreCase(superClassName);
    }


    AnnotationMetadata metadata() {
        return definition.getMetadata();
    }

    AnnotatedBeanDefinition definition() {
        return definition;
    }

    String name() {
        return name;
    }
}
