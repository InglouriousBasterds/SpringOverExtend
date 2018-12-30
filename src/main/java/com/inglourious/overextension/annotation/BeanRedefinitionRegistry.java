package com.inglourious.overextension.annotation;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import static com.inglourious.overextension.xml.ExtensionBeanDefinitionDecorator.SUFFIX_BEAN_EXTENDED;

public class BeanRedefinitionRegistry {

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public BeanRedefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void remappingRegistry(BeansRelationship beansRelationship) {

        beanDefinitionRegistry.removeBeanDefinition(beansRelationship.parentBeanName());

        ((AbstractBeanDefinition) beansRelationship.parentBeanDefinition()).setAbstract(true);

        String newParentName = buildParentName(beansRelationship.parentBeanName());
        beanDefinitionRegistry.registerBeanDefinition(newParentName, beansRelationship.parentBeanDefinition());

        beanDefinitionRegistry.removeBeanDefinition(beansRelationship.childBeanName());

        beansRelationship.childBeanDefinition().setParentName(newParentName);

        beanDefinitionRegistry.registerBeanDefinition(beansRelationship.parentBeanName(), beansRelationship.childBeanDefinition());
    }

    private String buildParentName(String parentName) {
        return parentName + "_" + SUFFIX_BEAN_EXTENDED;
    }
}
