package com.inglourious.overextension.config;

import com.inglourious.overextension.BeanNamesRetriever;
import com.inglourious.overextension.ExtensionBeanDefinitionRegistryPostProcessor;
import com.inglourious.overextension.ExtensionBeanDefinitionRegistryPostProcessor.RegistryRemapping;
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
    public RegistryRemapping registryRemapping(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        return new RegistryRemapping((BeanDefinitionRegistry) configurableListableBeanFactory);
    }

    @Bean
    public BeanFactoryPostProcessor extensionBeanDefinitionRegistryPostProcessor(BeanNamesRetriever beanNamesRetriever,
                                                                                 RegistryRemapping registryRemapping) {
        return new ExtensionBeanDefinitionRegistryPostProcessor(beanNamesRetriever, registryRemapping);
    }

}
