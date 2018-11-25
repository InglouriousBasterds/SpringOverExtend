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

import static com.inglourious.overextension.ExtensionBeanDefinitionDecorator.SUFFIX_BEAN_EXTENDED;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by gbrescia on 26/03/2018.
 */
public class ExtensionBeanDefinitionRegistryPostProcessor implements BeanFactoryPostProcessor {

    private final Log logger = LogFactory.getLog(ExtensionBeanDefinitionRegistryPostProcessor.class);
    private final BeanNamesRetriever beanNamesRetriever;

    public ExtensionBeanDefinitionRegistryPostProcessor(BeanNamesRetriever beanNamesRetriever) {
        this.beanNamesRetriever = beanNamesRetriever;
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

                    String[] parentBeanNames = beanNamesRetriever.from(beanDefinition.getMetadata());

                    if (parentBeanNames == null) {
                        throw new BeanCreationException("Bean " + beanDefinitionName + " must extends a spring bean component or specify extendBeanId , doesn't exist a spring bean for the class " + superClassName + " ");
                    }

                    //The bean name for the parent to extend
                    ParentBean parentBean = null;
                    String parentBeanName = null;
                    BeanDefinition parentBeanDefinition = null;
                    boolean isValidSuperClass = true;


                    if (parentBeanNames.length > 1) {
                        for (String beanNameForType : parentBeanNames) {
                            parentBeanDefinition = configurableListableBeanFactory.getBeanDefinition(beanNameForType);
                            if (parentBeanDefinition.getBeanClassName().equalsIgnoreCase(superClassName)) {
                                parentBeanName = beanNameForType;
                                isValidSuperClass = true;
                                parentBean = new ParentBean(beanNameForType, parentBeanDefinition);
                                break;
                            } else if (beanNameForType.equalsIgnoreCase(beanDefinitionName)) {
                                isValidSuperClass = false;
                            }
                        }
                    } else {
                        parentBeanDefinition = configurableListableBeanFactory.getBeanDefinition(parentBeanNames[0]);

                        Class classOfParent = Class.forName(parentBeanDefinition.getBeanClassName());
                        boolean isAssignable = classOfParent.isAssignableFrom(Class.forName(superClassName));

                        if (parentBeanDefinition.getBeanClassName().equalsIgnoreCase(superClassName) || isAssignable) {
                            parentBeanName = parentBeanNames[0];
                            parentBean = new ParentBean(parentBeanNames[0], parentBeanDefinition);
                        } else if (parentBeanNames[0].equalsIgnoreCase(beanDefinitionName)) {
                            isValidSuperClass = false;
                        }

                    }


                    if (parentBean.getName() == null || parentBean.getName().equalsIgnoreCase("")) {

                        if (!isValidSuperClass) {
                            throw new BeanCreationException("Bean " + beanDefinitionName + " must extends a unique spring bean component  or specify extendBeanId. Invalid superClass " + superClassName + " (" + parentBeanNames.toString() + ")");
                        } else {
                            throw new BeanCreationException("Bean " + beanDefinitionName + " must extends a unique spring bean component or specify extendBeanId ,empty or too much spring bean for the " + superClassName + " (" + parentBeanNames.toString() + ")");
                        }
                    }

                    addInMapRegistry.add(new ReplacerKeyRegistry(beanDefinitionName, beanDefinition, parentBean.getName(), parentBean.getDefinition()));
                }
            } catch (ClassNotFoundException e) {
                logger.error("", e);
            } catch (BeanCreationException be) {
                logger.error("Bean Creation error on OverExtension", be);
            }
        }


        if (addInMapRegistry.size() > 0) {
            for (ReplacerKeyRegistry replacerKeyRegistry : addInMapRegistry) {
                remappingRegistry((BeanDefinitionRegistry) configurableListableBeanFactory,
                        replacerKeyRegistry.getBeanNameOfChildren(),
                        replacerKeyRegistry.getBeanChildrenResult(),
                        replacerKeyRegistry.getBeanNameOfParent(),
                        replacerKeyRegistry.getBeanParentDefinition());
            }
        }
    }

    private boolean isObjectClass(String superClassName) {
        return Object.class.getCanonicalName().equalsIgnoreCase(superClassName);
    }


    private boolean isBeanAnnotatedWithOverExtension(String beanDefinitionName, ConfigurableListableBeanFactory configurableListableBeanFactory) {
        return configurableListableBeanFactory.findAnnotationOnBean(beanDefinitionName, OverExtension.class) != null;
    }

    private void remappingRegistry(BeanDefinitionRegistry registry,
                                   String childBeanName,
                                   BeanDefinition childBeanDefinition,
                                   String parentBeanName,
                                   BeanDefinition beanParentDefinition) {

        // rimuoviamo il bean originale dal registry
        registry.removeBeanDefinition(parentBeanName);

        //Rendiamo abstract il parent
        ((AbstractBeanDefinition) beanParentDefinition).setAbstract(true);

        // rinominiamo il bean originale secondo la convenzione definita e lo salviamo nel registry
        String newParentName = buildParentName(parentBeanName);
        registry.registerBeanDefinition(newParentName, beanParentDefinition);

        // rimuoviamo il bean originale dal registry
        registry.removeBeanDefinition(childBeanName);

        //Rendiamo abstract il parent
        childBeanDefinition.setParentName(newParentName);

        registry.registerBeanDefinition(parentBeanName, childBeanDefinition);
    }


    private String buildParentName(String parentName) {
        return parentName + "_" + SUFFIX_BEAN_EXTENDED;
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
    }
}
