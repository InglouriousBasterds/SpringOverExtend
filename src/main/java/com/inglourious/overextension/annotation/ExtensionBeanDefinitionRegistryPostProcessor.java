package com.inglourious.overextension.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

import java.util.List;
import java.util.Optional;

/**
 * Created by gbrescia on 26/03/2018.
 */
public class ExtensionBeanDefinitionRegistryPostProcessor implements BeanFactoryPostProcessor {

    private final Log logger = LogFactory.getLog(ExtensionBeanDefinitionRegistryPostProcessor.class);
    private final ParentBeanNamesRetriever parentBeanNamesRetriever;
    private final BeanRedefinitionRegistry beanRedefinitionRegistry;

    public ExtensionBeanDefinitionRegistryPostProcessor(ParentBeanNamesRetriever parentBeanNamesRetriever,
                                                        BeanRedefinitionRegistry beanRedefinitionRegistry) {
        this.parentBeanNamesRetriever = parentBeanNamesRetriever;
        this.beanRedefinitionRegistry = beanRedefinitionRegistry;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        for (String beanDefinitionName : configurableListableBeanFactory.getBeanDefinitionNames()) {

            try {

                if (isBeanAnnotatedWithOverExtension(beanDefinitionName, configurableListableBeanFactory)) {

                    AnnotatedBean annotatedBean = new AnnotatedBean(beanDefinitionName, (ScannedGenericBeanDefinition) configurableListableBeanFactory.getBeanDefinition(beanDefinitionName));

                    String superClassName = annotatedBean.superClassName()
                            .orElseThrow(() -> new MissingExtensionBeanException(annotatedBean));

                    List<String> parentBeanNames = parentBeanNamesRetriever.from(annotatedBean.metadata())
                            .orElseThrow(() -> new NotExistingExtendedBeanException(annotatedBean, superClassName));

                    Optional<ParentBean> parentBean = parentBeanNames.stream()
                            .map(name -> new ParentBean(name, configurableListableBeanFactory.getBeanDefinition(name)))
                            .filter(ParentBean::hasValidName)
                            .filter(pb -> pb.isSubClassOf(superClassName))
                            .findFirst();

                    BeansRelationship beansRelationship = parentBean.map(pb -> new BeansRelationship(annotatedBean, pb))
                            .orElseThrow(() -> new InvalidSuperClassBeanException(annotatedBean, superClassName, parentBeanNames));

                    beanRedefinitionRegistry.remappingRegistry(beansRelationship);
                }
            } catch (BeanCreationException be) {
                logger.error("Bean Creation error on OverExtension", be);
            }
        }
    }

    private boolean isBeanAnnotatedWithOverExtension(String beanDefinitionName, ConfigurableListableBeanFactory configurableListableBeanFactory) {
        return configurableListableBeanFactory.findAnnotationOnBean(beanDefinitionName, OverExtension.class) != null;
    }

}
