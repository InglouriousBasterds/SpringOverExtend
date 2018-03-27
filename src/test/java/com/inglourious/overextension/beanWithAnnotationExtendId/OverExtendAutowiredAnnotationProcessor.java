package com.inglourious.overextension.beanWithAnnotationExtendId;

import com.inglourious.overextension.SpringOverextendAnnotationApplicationTests;
import com.inglourious.overextension.annotation.OverExtension;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Created by gbrescia on 27/03/2018.
 */
@OverExtension(extendBeanId = "org.springframework.context.annotation.internalAutowiredAnnotationProcessor")
public class OverExtendAutowiredAnnotationProcessor extends AutowiredAnnotationBeanPostProcessor {


    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);

        //Example of override function
        SpringOverextendAnnotationApplicationTests.setValueFromOverExtendAutowiredAnnotationProcessor = 100;
    }

}
