package com.inglourious.overextension.config;

import com.inglourious.overextension.BeanNamesRetriever;
import com.inglourious.overextension.ExtensionBeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gbrescia on 28/03/2018.
 */
@Configuration
public class SpringOverextendConfiguration {

    @Bean
    public BeanNamesRetriever beanNameResolver() {
        return new BeanNamesRetriever();
    }

    @Bean
    public BeanFactoryPostProcessor extensionBeanDefinitionRegistryPostProcessor(BeanNamesRetriever beanNamesRetriever) {
        return new ExtensionBeanDefinitionRegistryPostProcessor(beanNamesRetriever);
    }


}
