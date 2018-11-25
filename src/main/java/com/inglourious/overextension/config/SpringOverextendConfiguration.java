package com.inglourious.overextension.config;

import com.inglourious.overextension.BeanNameResolver;
import com.inglourious.overextension.ExtensionBeanDefinitionDecorator;
import com.inglourious.overextension.ExtensionBeanDefinitionRegistryPostProcessor;
import com.inglourious.overextension.ExtensionNamespaceHandler;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gbrescia on 28/03/2018.
 */
@Configuration
public class SpringOverextendConfiguration {

    @Bean
    public BeanNameResolver beanNameResolver() {
        return new BeanNameResolver();
    }

    @Bean
    public BeanFactoryPostProcessor extensionBeanDefinitionRegistryPostProcessor(BeanNameResolver beanNameResolver) {
        return new ExtensionBeanDefinitionRegistryPostProcessor(beanNameResolver);
    }

    @Bean
    public BeanDefinitionDecorator extensionBeanDefinitionDecorator() {
        return new ExtensionBeanDefinitionDecorator();
    }

    @Bean
    public NamespaceHandler extensionNamespaceHandler() {
        return new ExtensionNamespaceHandler();
    }
}
