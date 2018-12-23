package com.inglourious.overextension.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;
import java.util.Optional;

import static java.lang.Class.forName;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.springframework.util.StringUtils.isEmpty;

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

                    String superClassName = annotatedBean.getSuperClassName()
                            .orElseThrow(() -> new MissingExtensionBeanException(annotatedBean));

                    List<String> parentBeanNames = parentBeanNamesRetriever.from(annotatedBean.metadata())
                            .orElseThrow(() -> new NotExistingExtendedBeanException(annotatedBean, superClassName));

                    Optional<ParentBean> parentBean = parentBeanNames.stream()
                            .map(name -> new ParentBean(name, configurableListableBeanFactory.getBeanDefinition(name)))
                            .filter(ParentBean::hasValidName)
                            .filter(pb -> pb.isSubClassOf(superClassName))
                            .findFirst();

                    ReplacerKeyRegistry replacerKeyRegistry = parentBean.map(pb -> new ReplacerKeyRegistry(annotatedBean.name(), annotatedBean.definition(), pb.name(), pb.definition()))
                            .orElseThrow(() -> new InvalidSuperClassBeanException(annotatedBean, superClassName, parentBeanNames));

                    beanRedefinitionRegistry.remappingRegistry(replacerKeyRegistry);
                }
            } catch (BeanCreationException be) {
                logger.error("Bean Creation error on OverExtension", be);
            }
        }
    }

    private class MissingExtensionBeanException extends BeanCreationException {
        public MissingExtensionBeanException(AnnotatedBean annotatedBean) {
            super("Bean " + annotatedBean.name() + " annotated with OverExtension must extend a superclass");
        }
    }

    private class NotExistingExtendedBeanException extends BeanCreationException {
        public NotExistingExtendedBeanException(AnnotatedBean annotatedBean, String superClassName) {
            super("Bean " + annotatedBean.name() + " must extends a spring bean component or specify extendBeanId , doesn't exist a spring bean for the class " + superClassName + " ");
        }
    }

    private class InvalidSuperClassBeanException extends BeanCreationException {
        public InvalidSuperClassBeanException(AnnotatedBean annotatedBean, String superClassName, List<String> parentBeanNames) {
            super("Bean " + annotatedBean.name() + " must extends a unique spring bean component or specify extendBeanId. Invalid superClass " + superClassName + " (" + parentBeanNames.toString() + ")");
        }
    }

    private boolean isBeanAnnotatedWithOverExtension(String beanDefinitionName, ConfigurableListableBeanFactory configurableListableBeanFactory) {
        return configurableListableBeanFactory.findAnnotationOnBean(beanDefinitionName, OverExtension.class) != null;
    }

    private class AnnotatedBean {

        private final String name;
        private final AnnotatedBeanDefinition definition;

        private AnnotatedBean(String name, AnnotatedBeanDefinition definition) {
            this.name = name;
            this.definition = definition;
        }

        Optional<String> getSuperClassName() {
            String superClassName = definition.getMetadata().getSuperClassName();
            return isEmpty(superClassName) || isObjectClass(superClassName) ? empty() :
                    of(superClassName);
        }

        private boolean isObjectClass(String superClassName) {
            return Object.class.getCanonicalName().equalsIgnoreCase(superClassName);
        }


        AnnotationMetadata metadata() {
            return definition.getMetadata();
        }

        AnnotatedBeanDefinition definition() {
            return definition;
        }

        String name() {
            return name;
        }
    }

    private class ParentBean {
        private final String name;
        private final BeanDefinition definition;

        private ParentBean(String name, BeanDefinition definition) {
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

}
