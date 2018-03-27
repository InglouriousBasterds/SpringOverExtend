package com.inglourious.overextension.bean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

/**
 * Created by gbrescia on 27/03/2018.
 */
public class ReplacerKeyRegistry {

    String beanNameOfChildren;
    ScannedGenericBeanDefinition beanChildrenResult;
    String beanNameOfParent;
    BeanDefinition beanParentDefinition;

    public ReplacerKeyRegistry(String beanNameOfChildren, ScannedGenericBeanDefinition beanChildrenResult, String beanNameOfParent, BeanDefinition beanParentDefinition) {
        this.beanNameOfChildren = beanNameOfChildren;
        this.beanChildrenResult = beanChildrenResult;
        this.beanNameOfParent = beanNameOfParent;
        this.beanParentDefinition = beanParentDefinition;
    }

    public String getBeanNameOfChildren() {
        return beanNameOfChildren;
    }

    public ScannedGenericBeanDefinition getBeanChildrenResult() {
        return beanChildrenResult;
    }

    public String getBeanNameOfParent() {
        return beanNameOfParent;
    }

    public BeanDefinition getBeanParentDefinition() {
        return beanParentDefinition;
    }
}
