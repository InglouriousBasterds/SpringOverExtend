package com.inglourious.overextension.annotation;

import org.springframework.beans.factory.config.BeanDefinition;

import static java.lang.Class.forName;

class ParentBean {
    private final String name;
    private final BeanDefinition definition;

    ParentBean(String name, BeanDefinition definition) {
        this.name = name;
        this.definition = definition;
    }

    String name() {
        return name;
    }

    BeanDefinition definition() {
        return definition;
    }

    boolean hasValidName() {
        return name != null && !name.isEmpty();
    }

    boolean isSubClassOf(String className) {
        return className.equalsIgnoreCase(definition.getBeanClassName())
                || isAssignableTo(className);
    }

    private boolean isAssignableTo(String className) {
        try {
            return forName(definition.getBeanClassName()).isAssignableFrom(forName(className));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
