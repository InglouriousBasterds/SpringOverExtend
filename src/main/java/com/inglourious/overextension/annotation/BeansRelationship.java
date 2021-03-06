package com.inglourious.overextension.annotation;

import org.springframework.beans.factory.config.BeanDefinition;

/**
 * Created by gbrescia on 27/03/2018.
 */
public class BeansRelationship {

    private AnnotatedBean childBean;
    private ParentBean parentBean;

    public BeansRelationship(AnnotatedBean childBean, ParentBean parentBean) {
        this.childBean = childBean;
        this.parentBean = parentBean;
    }

    public String childBeanName() {
        return childBean.name();
    }

    public BeanDefinition childBeanDefinition() {
        return childBean.definition();
    }

    public String parentBeanName() {
        return parentBean.name();
    }

    public BeanDefinition parentBeanDefinition() {
        return parentBean.definition();
    }
}
