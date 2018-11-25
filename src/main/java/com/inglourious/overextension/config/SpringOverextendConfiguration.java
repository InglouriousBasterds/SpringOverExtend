package com.inglourious.overextension.config;

import com.inglourious.overextension.BeanNamesRetriever;
import com.inglourious.overextension.BeanRedefinitionRegistry;
import com.inglourious.overextension.ExtensionBeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gbrescia on 28/03/2018.
 */
@Configuration
public class SpringOverextendConfiguration {

    @Bean
    public BeanNamesRetriever beanNameResolver(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        return new BeanNamesRetriever(configurableListableBeanFactory);
    }

    @Bean
    public BeanRedefinitionRegistry beanRedefinitionRegistry(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        return new BeanRedefinitionRegistry((BeanDefinitionRegistry) configurableListableBeanFactory);
    }

    @Bean
    public BeanFactoryPostProcessor extensionBeanDefinitionRegistryPostProcessor(BeanNamesRetriever beanNamesRetriever,
                                                                                 BeanRedefinitionRegistry beanRedefinitionRegistry) {
        return new ExtensionBeanDefinitionRegistryPostProcessor(beanNamesRetriever, beanRedefinitionRegistry);
    }

}
