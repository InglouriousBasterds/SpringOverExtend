package com.inglourious.overextension.annotation;

import org.springframework.beans.factory.config.BeanDefinition;

/**
 * Created by gbrescia on 27/03/2018.
 */
public class ReplacerKeyRegistry {

    private final String childBeanName;
    private final BeanDefinition childBeanResult;
    private final String parentBeanName;
    private final BeanDefinition parentBeanDefinition;

    public ReplacerKeyRegistry(String childBeanName,
                               BeanDefinition beanChildrenResult,
                               String parentBeanName,
                               BeanDefinition parentBeanDefinition) {
        this.childBeanName = childBeanName;
        this.childBeanResult = beanChildrenResult;
        this.parentBeanName = parentBeanName;
        this.parentBeanDefinition = parentBeanDefinition;
    }

    public String getBeanNameOfChildren() {
        return childBeanName;
    }

    public BeanDefinition getBeanChildrenResult() {
        return childBeanResult;
    }

    public String getBeanNameOfParent() {
        return parentBeanName;
    }

    public BeanDefinition getBeanParentDefinition() {
        return parentBeanDefinition;
    }
}
