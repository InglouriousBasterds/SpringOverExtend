package com.inglourious.overextension.annotation;

import org.springframework.beans.factory.BeanCreationException;

import java.util.List;

class InvalidSuperClassBeanException extends BeanCreationException {

    public InvalidSuperClassBeanException(AnnotatedBean annotatedBean,
                                          String superClassName,
                                          List<String> parentBeanNames) {
        super("Bean " + annotatedBean.name() + " must extends a unique spring bean component or specify extendBeanId. " +
                "Invalid superClass " + superClassName + " (" + parentBeanNames.toString() + ")");
    }
}
