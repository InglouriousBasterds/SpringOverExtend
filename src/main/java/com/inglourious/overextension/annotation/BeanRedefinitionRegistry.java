package com.inglourious.overextension.annotation;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import static com.inglourious.overextension.xml.ExtensionBeanDefinitionDecorator.SUFFIX_BEAN_EXTENDED;

public class BeanRedefinitionRegistry {

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public BeanRedefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public void remappingRegistry(ReplacerKeyRegistry replacerKeyRegistry) {

        // rimuoviamo il bean originale dal registry
        beanDefinitionRegistry.removeBeanDefinition(replacerKeyRegistry.parentBeanName());

        //Rendiamo abstract il parent
        ((AbstractBeanDefinition) replacerKeyRegistry.parentBeanDefinition()).setAbstract(true);

        // rinominiamo il bean originale secondo la convenzione definita e lo salviamo nel registry
        String newParentName = buildParentName(replacerKeyRegistry.parentBeanName());
        beanDefinitionRegistry.registerBeanDefinition(newParentName, replacerKeyRegistry.parentBeanDefinition());

        // rimuoviamo il bean originale dal registry
        beanDefinitionRegistry.removeBeanDefinition(replacerKeyRegistry.childBeanName());

        //Rendiamo abstract il parent
        replacerKeyRegistry.childBeanDefinition().setParentName(newParentName);

        beanDefinitionRegistry.registerBeanDefinition(replacerKeyRegistry.parentBeanName(), replacerKeyRegistry.childBeanDefinition());
    }

    private String buildParentName(String parentName) {
        return parentName + "_" + SUFFIX_BEAN_EXTENDED;
    }
}
