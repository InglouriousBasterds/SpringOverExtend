package com.inglourious.overextension;

import com.inglourious.overextension.annotation.OverExtension;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

import java.util.Map;

public class BeanNameResolver {

    private ConfigurableListableBeanFactory configurableListableBeanFactory;

    public BeanNameResolver(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        this.configurableListableBeanFactory = configurableListableBeanFactory;
    }

    public String[] getBeanNamesForType(ScannedGenericBeanDefinition beanChildrenResult,
                                        String superClassName) throws ClassNotFoundException {

        Object extendBeanId = getExtendBeanIdAttribute(beanChildrenResult);

        return isValid(extendBeanId) ? new String[]{extendBeanId.toString()} :
                getBeanNamesForTypeFor(superClassName);
    }


    private String[] getBeanNamesForTypeFor(String superClassName) throws ClassNotFoundException {
        return this.configurableListableBeanFactory.getBeanNamesForType(Class.forName(superClassName));
    }

    private boolean isValid(Object extendBeanId) {
        return extendBeanId != null && !"".equalsIgnoreCase(extendBeanId.toString());
    }

    private Object getExtendBeanIdAttribute(ScannedGenericBeanDefinition beanChildrenResult) {
        Map<String, Object> annotationAttributes = beanChildrenResult.getMetadata().getAnnotationAttributes(OverExtension.class.getName(), true);

        return annotationAttributes.get("extendBeanId");
    }
}
