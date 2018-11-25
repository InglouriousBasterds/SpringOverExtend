package com.inglourious.overextension;

import com.inglourious.overextension.annotation.OverExtension;
import com.inglourious.overextension.bean.ReplacerKeyRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.inglourious.overextension.ExtensionBeanDefinitionDecorator.SUFFIX_BEAN_EXTENDED;
import static java.lang.Class.forName;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by gbrescia on 26/03/2018.
 */
public class ExtensionBeanDefinitionRegistryPostProcessor implements BeanFactoryPostProcessor {

    private final Log logger = LogFactory.getLog(ExtensionBeanDefinitionRegistryPostProcessor.class);
    private final BeanNamesRetriever beanNamesRetriever;
    private final RegistryRemapping registryRemapping;

    public ExtensionBeanDefinitionRegistryPostProcessor(BeanNamesRetriever beanNamesRetriever, RegistryRemapping registryRemapping) {
        this.beanNamesRetriever = beanNamesRetriever;
        this.registryRemapping = registryRemapping;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        List<ReplacerKeyRegistry> addInMapRegistry = new ArrayList<>();


        for (String beanDefinitionName : configurableListableBeanFactory.getBeanDefinitionNames()) {

            try {

                if (isBeanAnnotatedWithOverExtension(beanDefinitionName, configurableListableBeanFactory)) {

                    ScannedGenericBeanDefinition beanDefinition = (ScannedGenericBeanDefinition) configurableListableBeanFactory.getBeanDefinition(beanDefinitionName);

                    String superClassName = beanDefinition.getMetadata().getSuperClassName();

                    if (isEmpty(superClassName) || isObjectClass(superClassName)) {
                        throw new BeanCreationException("Bean " + beanDefinitionName + " annotated with OverExtension must extend a superclass");
                    }

                    List<String> parentBeanNames = beanNamesRetriever.from(beanDefinition.getMetadata())
                            .orElseThrow(() -> new BeanCreationException("Bean " + beanDefinitionName + " must extends a spring bean component or specify extendBeanId , doesn't exist a spring bean for the class " + superClassName + " "));

                    Optional<ParentBean> parentBean = parentBeanNames.stream()
                            .map(name -> new ParentBean(name, configurableListableBeanFactory.getBeanDefinition(name)))
                            .filter(ParentBean::hasValidName)
                            .filter(pb -> pb.isSubClassOf(superClassName))
                            .findFirst();

                    ReplacerKeyRegistry replacerKeyRegistry = parentBean.map(pb -> new ReplacerKeyRegistry(beanDefinitionName, beanDefinition, pb.getName(), pb.getDefinition()))
                            .orElseThrow(() -> new BeanCreationException("Bean " + beanDefinitionName + " must extends a unique spring bean component  or specify extendBeanId. Invalid superClass " + superClassName + " (" + parentBeanNames.toString() + ")"));

                    addInMapRegistry.add(replacerKeyRegistry);
                }
            } catch (BeanCreationException be) {
                logger.error("Bean Creation error on OverExtension", be);
            }
        }

        if (addInMapRegistry.size() > 0) {
            for (ReplacerKeyRegistry replacerKeyRegistry : addInMapRegistry) {
                registryRemapping.remappingRegistry(replacerKeyRegistry);
            }
        }
    }

    private boolean isObjectClass(String superClassName) {
        return Object.class.getCanonicalName().equalsIgnoreCase(superClassName);
    }


    private boolean isBeanAnnotatedWithOverExtension(String beanDefinitionName, ConfigurableListableBeanFactory configurableListableBeanFactory) {
        return configurableListableBeanFactory.findAnnotationOnBean(beanDefinitionName, OverExtension.class) != null;
    }


    private class ParentBean {
        private final String name;
        private final BeanDefinition definition;

        private ParentBean(String name, BeanDefinition definition) {
            this.name = name;
            this.definition = definition;
        }

        public String getName() {
            return name;
        }

        public BeanDefinition getDefinition() {
            return definition;
        }

        public boolean hasValidName() {
            return name != null && !name.isEmpty();
        }

        public boolean isSubClassOf(String className) {
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

    public static class RegistryRemapping {
        private BeanDefinitionRegistry configurableListableBeanFactory;

        public RegistryRemapping(BeanDefinitionRegistry configurableListableBeanFactory) {
            this.configurableListableBeanFactory = configurableListableBeanFactory;
        }

        public void remappingRegistry(ReplacerKeyRegistry replacerKeyRegistry) {

            // rimuoviamo il bean originale dal registry
            configurableListableBeanFactory.removeBeanDefinition(replacerKeyRegistry.getBeanNameOfParent());

            //Rendiamo abstract il parent
            ((AbstractBeanDefinition) replacerKeyRegistry.getBeanParentDefinition()).setAbstract(true);

            // rinominiamo il bean originale secondo la convenzione definita e lo salviamo nel registry
            String newParentName = buildParentName(replacerKeyRegistry.getBeanNameOfParent());
            configurableListableBeanFactory.registerBeanDefinition(newParentName, replacerKeyRegistry.getBeanParentDefinition());

            // rimuoviamo il bean originale dal registry
            configurableListableBeanFactory.removeBeanDefinition(replacerKeyRegistry.getBeanNameOfChildren());

            //Rendiamo abstract il parent
            replacerKeyRegistry.getBeanChildrenResult().setParentName(newParentName);

            configurableListableBeanFactory.registerBeanDefinition(replacerKeyRegistry.getBeanNameOfParent(), replacerKeyRegistry.getBeanChildrenResult());
        }


        private String buildParentName(String parentName) {
            return parentName + "_" + SUFFIX_BEAN_EXTENDED;
        }
    }
}
