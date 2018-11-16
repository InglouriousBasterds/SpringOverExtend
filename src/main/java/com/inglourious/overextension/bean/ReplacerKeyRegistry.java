package com.inglourious.overextension.bean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

/**
 * Created by gbrescia on 27/03/2018.
 */
public class ReplacerKeyRegistry {

    private final String childBeanName;
    private final ScannedGenericBeanDefinition childBeanResult;
    private final String parentBeanName;
    private final BeanDefinition parentBeanDefinition;

    public ReplacerKeyRegistry(String childBeanName,
                               ScannedGenericBeanDefinition beanChildrenResult,
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

    public ScannedGenericBeanDefinition getBeanChildrenResult() {
        return childBeanResult;
    }

    public String getBeanNameOfParent() {
        return parentBeanName;
    }

    public BeanDefinition getBeanParentDefinition() {
        return parentBeanDefinition;
    }
}
