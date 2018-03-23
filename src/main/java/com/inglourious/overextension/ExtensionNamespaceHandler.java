package com.inglourious.overextension;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author dg gb The Class ExtensionNamespaceHandler configured in spring.handlers reads and registers the decorator @ExtensionBeanDefinitionDecorator
 */
public class ExtensionNamespaceHandler extends NamespaceHandlerSupport {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
	 */
	public void init() {
		registerBeanDefinitionDecoratorForAttribute("extension", new ExtensionBeanDefinitionDecorator());
	}
}