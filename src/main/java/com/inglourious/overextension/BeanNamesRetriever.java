package com.inglourious.overextension;

import com.inglourious.overextension.annotation.OverExtension;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Optional.of;

public class BeanNamesRetriever {

    private ConfigurableListableBeanFactory configurableListableBeanFactory;

    public BeanNamesRetriever(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        this.configurableListableBeanFactory = configurableListableBeanFactory;
    }

    public Optional<List<String>> from(AnnotationMetadata metadata) {

        Object extendBeanId = getExtendBeanIdAttribute(metadata);

        return isValid(extendBeanId) ? of(asList(extendBeanId.toString())) :
                getBeanNamesForTypeFor(metadata.getSuperClassName());
    }

    private Object getExtendBeanIdAttribute(AnnotationMetadata metadata) {
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(OverExtension.class.getName(), true);

        return annotationAttributes.get("extendBeanId");
    }

    private boolean isValid(Object extendBeanId) {
        return extendBeanId != null && !"".equalsIgnoreCase(extendBeanId.toString());
    }

    private Optional<List<String>> getBeanNamesForTypeFor(String superClassName) {
        try {
            return of(asList(this.configurableListableBeanFactory.getBeanNamesForType(Class.forName(superClassName))));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}