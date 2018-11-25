package com.inglourious.overextension.config;

import com.inglourious.overextension.BeanNameResolver;
import com.inglourious.overextension.ExtensionBeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gbrescia on 28/03/2018.
 */
@ComponentScan(basePackages = "com.inglourious.overextension")
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
}
