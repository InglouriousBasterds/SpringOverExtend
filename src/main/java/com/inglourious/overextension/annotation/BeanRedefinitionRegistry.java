package com.inglourious.overextension.annotation;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import static com.inglourious.overextension.annotation.ExtensionBeanDefinitionDecorator.SUFFIX_BEAN_EXTENDED;

public class BeanRedefinitionRegistry {

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public BeanRedefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void remappingRegistry(ReplacerKeyRegistry replacerKeyRegistry) {

        // rimuoviamo il bean originale dal registry
        beanDefinitionRegistry.removeBeanDefinition(replacerKeyRegistry.getBeanNameOfParent());

        //Rendiamo abstract il parent
        ((AbstractBeanDefinition) replacerKeyRegistry.getBeanParentDefinition()).setAbstract(true);

        // rinominiamo il bean originale secondo la convenzione definita e lo salviamo nel registry
        String newParentName = buildParentName(replacerKeyRegistry.getBeanNameOfParent());
        beanDefinitionRegistry.registerBeanDefinition(newParentName, replacerKeyRegistry.getBeanParentDefinition());

        // rimuoviamo il bean originale dal registry
        beanDefinitionRegistry.removeBeanDefinition(replacerKeyRegistry.getBeanNameOfChildren());

        //Rendiamo abstract il parent
        replacerKeyRegistry.getBeanChildrenResult().setParentName(newParentName);

        beanDefinitionRegistry.registerBeanDefinition(replacerKeyRegistry.getBeanNameOfParent(), replacerKeyRegistry.getBeanChildrenResult());
    }

    private String buildParentName(String parentName) {
        return parentName + "_" + SUFFIX_BEAN_EXTENDED;
    }
}
