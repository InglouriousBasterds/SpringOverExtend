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

        // rimuoviamo il bean originale dal registry
        beanDefinitionRegistry.removeBeanDefinition(beansRelationship.parentBeanName());

        //Rendiamo abstract il parent
        ((AbstractBeanDefinition) beansRelationship.parentBeanDefinition()).setAbstract(true);

        // rinominiamo il bean originale secondo la convenzione definita e lo salviamo nel registry
        String newParentName = buildParentName(beansRelationship.parentBeanName());
        beanDefinitionRegistry.registerBeanDefinition(newParentName, beansRelationship.parentBeanDefinition());

        // rimuoviamo il bean originale dal registry
        beanDefinitionRegistry.removeBeanDefinition(beansRelationship.childBeanName());

        //Rendiamo abstract il parent
        beansRelationship.childBeanDefinition().setParentName(newParentName);

        beanDefinitionRegistry.registerBeanDefinition(beansRelationship.parentBeanName(), beansRelationship.childBeanDefinition());
    }

    private String buildParentName(String parentName) {
        return parentName + "_" + SUFFIX_BEAN_EXTENDED;
    }
}
