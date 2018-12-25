package com.inglourious.overextension.fixture.beanWithAnnotationExtendId;

import com.inglourious.overextension.annotation.OverExtension;
import com.inglourious.overextension.annotation.SpringOverextendAnnotationApplicationTests;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

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
