package com.inglourious.overextension.annotation;

import org.springframework.beans.factory.BeanCreationException;

class NotExistingExtendedBeanException extends BeanCreationException {
    public NotExistingExtendedBeanException(AnnotatedBean annotatedBean, String superClassName) {
        super("Bean " + annotatedBean.name() + " must extends a spring bean component or specify extendBeanId , doesn't exist a spring bean for the class " + superClassName + " ");
    }
}
