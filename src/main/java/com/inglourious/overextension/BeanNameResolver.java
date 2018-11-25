package com.inglourious.overextension;

import com.inglourious.overextension.annotation.OverExtension;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

import java.util.Map;

public class BeanNameResolver {



    public String[] getBeanNamesForType(ScannedGenericBeanDefinition beanChildrenResult,
                                        String superClassName, ConfigurableListableBeanFactory configurableListableBeanFactory1) throws ClassNotFoundException {

        Object extendBeanId = getExtendBeanIdAttribute(beanChildrenResult);

        return isValid(extendBeanId) ? new String[]{extendBeanId.toString()} :
                getBeanNamesForTypeFor(superClassName, configurableListableBeanFactory1);
    }


    private String[] getBeanNamesForTypeFor(String superClassName, ConfigurableListableBeanFactory configurableListableBeanFactory1) throws ClassNotFoundException {
        return configurableListableBeanFactory1.getBeanNamesForType(Class.forName(superClassName));
    }

    private boolean isValid(Object extendBeanId) {
        return extendBeanId != null && !"".equalsIgnoreCase(extendBeanId.toString());
    }

    private Object getExtendBeanIdAttribute(ScannedGenericBeanDefinition beanChildrenResult) {
        Map<String, Object> annotationAttributes = beanChildrenResult.getMetadata().getAnnotationAttributes(OverExtension.class.getName(), true);

        return annotationAttributes.get("extendBeanId");
    }
}
