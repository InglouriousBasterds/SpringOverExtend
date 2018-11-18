package com.inglourious.overextension;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * The class {@code ExtensionNamespaceHandler} configured in spring.handlers reads and registers the {@link ExtensionBeanDefinitionDecorator} decorator
 *
 * @author dg gb
 */
public class ExtensionNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionDecoratorForAttribute("extension", new ExtensionBeanDefinitionDecorator());
    }
}