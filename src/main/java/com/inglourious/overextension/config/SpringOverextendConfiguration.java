package com.inglourious.overextension.config;

import com.inglourious.overextension.annotation.BeanRedefinitionRegistry;
import com.inglourious.overextension.annotation.ExtensionBeanDefinitionRegistryPostProcessor;
import com.inglourious.overextension.xml.ParentBeanNamesRetriever;
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
    public ParentBeanNamesRetriever parentBeanNamesRetriever(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        return new ParentBeanNamesRetriever(configurableListableBeanFactory);
    }

    @Bean
    public BeanRedefinitionRegistry beanRedefinitionRegistry(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        return new BeanRedefinitionRegistry((BeanDefinitionRegistry) configurableListableBeanFactory);
    }

    @Bean
    public BeanFactoryPostProcessor extensionBeanDefinitionRegistryPostProcessor(ParentBeanNamesRetriever parentBeanNamesRetriever,
                                                                                 BeanRedefinitionRegistry beanRedefinitionRegistry) {
        return new ExtensionBeanDefinitionRegistryPostProcessor(parentBeanNamesRetriever, beanRedefinitionRegistry);
    }

}
