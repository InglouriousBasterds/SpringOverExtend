package com.inglourious.overextension;

import com.inglourious.overextension.annotation.OverExtension;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

public class BeanNamesRetriever {

    public String[] from(AnnotationMetadata metadata,
                         ConfigurableListableBeanFactory configurableListableBeanFactory) throws ClassNotFoundException {

        Object extendBeanId = getExtendBeanIdAttribute(metadata);

        return isValid(extendBeanId) ? new String[]{extendBeanId.toString()} :
                getBeanNamesForTypeFor(metadata.getSuperClassName(), configurableListableBeanFactory);
    }

    private Object getExtendBeanIdAttribute(AnnotationMetadata metadata) {
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(OverExtension.class.getName(), true);

        return annotationAttributes.get("extendBeanId");
    }

    private boolean isValid(Object extendBeanId) {
        return extendBeanId != null && !"".equalsIgnoreCase(extendBeanId.toString());
    }

    private String[] getBeanNamesForTypeFor(String superClassName,
                                            ConfigurableListableBeanFactory configurableListableBeanFactory) throws ClassNotFoundException {
        return configurableListableBeanFactory.getBeanNamesForType(Class.forName(superClassName));
    }
}
