package com.inglourious.overextension.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * GB && DG Decorator permits the ovverride and inheritance of a bean.
 */
public class ExtensionBeanDefinitionDecorator implements BeanDefinitionDecorator {


    public static final String SUFFIX_BEAN_EXTENDED = "$overExtension";
    public static final String OVEREXTENSION_ABSTRACT = "abstract";

    private final Log logger = LogFactory.getLog(ExtensionBeanDefinitionDecorator.class);

    @Override
    public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext) {

        if (isAttributeValueAbstract(node)) {
            if (isBeanDefinedInRegistry(definition, parserContext.getRegistry())) {
                redefineAndAbstractParentBean(definition, parserContext);
                return definition;
            } else {
                throw new RuntimeException("Bean with id " + definition.getBeanName() + " doesn't exist no bean to apply extension");
            }
        } else {
            throw new RuntimeException("Wrong value set in attribute over:extension=" + ((Attr) node).getValue() + " for " + definition.getBeanName());
        }
    }

    private boolean isAttributeValueAbstract(Node node) {
        String attributeValue = ((Attr) node).getValue();
        return OVEREXTENSION_ABSTRACT.equals(attributeValue);
    }

    private boolean isBeanDefinedInRegistry(BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {
        return registry.getBeanDefinition(definition.getBeanName()) != null;
    }

    private void redefineAndAbstractParentBean(BeanDefinitionHolder definition, ParserContext parserContext) {
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        String beanName = definition.getBeanName();

        AbstractBeanDefinition bean = removeBeanDefinitionFromRegistry(beanName, registry);

        String newParentName = buildParentName(definition, bean);
        registerBeanAsAbstractWithNewParentName(registry, bean, newParentName);

        if (isParentNameAlreadyConfiguredFor(definition.getBeanDefinition())) {
            throw new RuntimeException("The attribute parent is not allowed for the bean " + beanName);
        }

        logger.info("Bean with id : '" + beanName + "' extended in " + parserContext.getReaderContext().getResource().getFilename());

        configuredAsParentTheRenamedBean(definition, newParentName);
    }

    private void configuredAsParentTheRenamedBean(BeanDefinitionHolder definition, String newParentName) {
        definition.getBeanDefinition().setParentName(newParentName);
    }

    private boolean isParentNameAlreadyConfiguredFor(BeanDefinition beanDefinition) {
        return beanDefinition.getParentName() != null && !beanDefinition.getParentName().equalsIgnoreCase("");
    }

    private AbstractBeanDefinition removeBeanDefinitionFromRegistry(String beanName, BeanDefinitionRegistry registry) {
        AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) registry.getBeanDefinition(beanName);

        registry.removeBeanDefinition(beanName);

        return beanDefinition;
    }

    private void registerBeanAsAbstractWithNewParentName(BeanDefinitionRegistry beanDefinitionRegistry, AbstractBeanDefinition beanDefinition, String newParentName) {
        beanDefinition.setAbstract(true);
        beanDefinitionRegistry.registerBeanDefinition(newParentName, beanDefinition);
    }

    private String buildParentName(BeanDefinitionHolder definitionChild, AbstractBeanDefinition beanFather) {

        String parentName = beanFather.getParentName();

        String childBeanName = definitionChild.getBeanName();

        return isEmpty(parentName) ? childBeanName + SUFFIX_BEAN_EXTENDED :
                parentName + "_" + childBeanName + SUFFIX_BEAN_EXTENDED;
    }
}