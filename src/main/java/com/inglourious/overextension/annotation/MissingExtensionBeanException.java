package com.inglourious.overextension.annotation;

import org.springframework.beans.factory.BeanCreationException;

class MissingExtensionBeanException extends BeanCreationException {

    public MissingExtensionBeanException(AnnotatedBean annotatedBean) {
        super("Bean " + annotatedBean.name() + " annotated with OverExtension must extend a superclass");
    }
}
