package com.inglourious.overextension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * GB && DG Decorator permits the ovverride and inheritance of a bean.
 */
public class ExtensionBeanDefinitionDecorator implements BeanDefinitionDecorator {


    public static final String SUFFIX_BEAN_EXTENDED = "$overExtension";
    public static final String OVEREXTENSION_ABSTRACT = "abstract";

    /**
     * The logger.
     */
    protected final Log logger = LogFactory.getLog(DefaultResourceLoader.class);

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.beans.factory.xml.BeanDefinitionDecorator#decorate(org.w3c.dom.Node,
     * org.springframework.beans.factory.config.BeanDefinitionHolder,
     * org.springframework.beans.factory.xml.ParserContext)
     */
    public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext) {
        Attr attr = (Attr) node;
        if (OVEREXTENSION_ABSTRACT.equals(attr.getValue())) {
            if (parserContext.getRegistry().getBeanDefinition(definition.getBeanName()) == null) {
                throw new RuntimeException("Bean with id " + definition.getBeanName() + " doesn't exist no bean to apply extension");
            } else {
                redefineAndAbstractParentBean(definition, parserContext);
            }
        } else {
            throw new RuntimeException("Wrong value set in attribute over:extension=" + attr.getValue() + " for " + definition.getBeanName());
        }
        return definition;
    }

    private void redefineAndAbstractParentBean(BeanDefinitionHolder definition, ParserContext parserContext) {
        AbstractBeanDefinition beanOrigin = (AbstractBeanDefinition) parserContext.getRegistry()
                .getBeanDefinition(definition.getBeanName());

        // rimuoviamo il bean originale dal registry
        parserContext.getRegistry().removeBeanDefinition(definition.getBeanName());

        //Rendiamo abstract il parent
        beanOrigin.setAbstract(true);

        // rinominiamo il bean originale secondo la convenzione definita e lo salviamo nel registry
        String newParentName = buildParentName(definition, beanOrigin);
        parserContext.getRegistry().registerBeanDefinition(newParentName, beanOrigin);


        // Se eventualmente è già stato settato il parent errore a runtime
        if (definition.getBeanDefinition().getParentName() != null && !definition.getBeanDefinition().getParentName().equalsIgnoreCase("")) {
            throw new RuntimeException("The attribute parent it's not allowed for bean " + definition.getBeanName());
        }

        logger.info("Bean with id : '" + definition.getBeanName() + "' extended in "
                + parserContext.getReaderContext().getResource().getFilename());

        // settiamo come parent il bean originale rinominato
        definition.getBeanDefinition().setParentName(newParentName);

    }


    private String buildParentName(BeanDefinitionHolder definitionChild, AbstractBeanDefinition beanFather) {

        String parentName = beanFather.getParentName();

        String childBeanName = definitionChild.getBeanName();

        return isEmpty(parentName) ? childBeanName + SUFFIX_BEAN_EXTENDED :
                parentName + "_" + childBeanName + SUFFIX_BEAN_EXTENDED;

    }
}