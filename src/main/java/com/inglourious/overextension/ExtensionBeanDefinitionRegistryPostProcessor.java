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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.inglourious.overextension.ExtensionBeanDefinitionDecorator.SUFFIX_BEAN_EXTENDED;

/**
 * Created by gbrescia on 26/03/2018.
 */
@Component
public class ExtensionBeanDefinitionRegistryPostProcessor implements BeanFactoryPostProcessor {

    private final Log logger = LogFactory.getLog(ExtensionBeanDefinitionRegistryPostProcessor.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        List<ReplacerKeyRegistry> addInMapRegistry = new ArrayList<>();
        String[] beanDefinitionNames = configurableListableBeanFactory.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {

            OverExtension annotationOnBean = configurableListableBeanFactory.findAnnotationOnBean(beanDefinitionName, OverExtension.class);

            try {
                if (annotationOnBean != null) {
                    String beanNameOfChildren = beanDefinitionName;
                    if (logger.isDebugEnabled()) {
                        logger.debug("Find " + beanNameOfChildren + " candidate for OverExtension");
                    }

                    ScannedGenericBeanDefinition beanChildrenResult = (ScannedGenericBeanDefinition) configurableListableBeanFactory.getBeanDefinition(beanNameOfChildren);
                    String superClassName = beanChildrenResult.getMetadata().getSuperClassName();
                    Map<String, Object> annotationAttributes = beanChildrenResult.getMetadata().getAnnotationAttributes(OverExtension.class.getName(), true);
                    if (superClassName == null
                            || "".equalsIgnoreCase(superClassName)
                            || Object.class.getCanonicalName().equalsIgnoreCase(superClassName)) {
                        throw new BeanCreationException("Bean " + beanNameOfChildren + " annotated with OverExtension must extend a superclass");
                    }

                    String[] beanNamesForType;
                    Class<?> classForSuperClass = Class.forName(superClassName);
                    Object extendBeanId = annotationAttributes.get("extendBeanId");
                    if (extendBeanId != null
                            && !"".equalsIgnoreCase(extendBeanId.toString())) {
                        beanNamesForType = new String[]{extendBeanId.toString()};
                    } else {
                        beanNamesForType = configurableListableBeanFactory.getBeanNamesForType(classForSuperClass);
                    }

                    if (beanNamesForType == null) {
                        throw new BeanCreationException("Bean " + beanNameOfChildren + " must extends a spring bean component or specify extendBeanId , doesn't exist a spring bean for the class " + superClassName + " ");
                    }

                    //The bean name for the parent to extend
                    String beanNameOfParent = null;
                    BeanDefinition beanParentDefinition = null;
                    boolean isValidSuperClass = true;


                    if (beanNamesForType.length > 1) {
                        for (String beanNameForType : beanNamesForType) {
                            beanParentDefinition = configurableListableBeanFactory.getBeanDefinition(beanNameForType);
                            if (beanParentDefinition.getBeanClassName().equalsIgnoreCase(superClassName)) {
                                beanNameOfParent = beanNameForType;
                                isValidSuperClass = true;
                                break;
                            } else if (beanNameForType.equalsIgnoreCase(beanNameOfChildren)) {
                                isValidSuperClass = false;
                            }
                        }
                    } else {
                        beanParentDefinition = configurableListableBeanFactory.getBeanDefinition(beanNamesForType[0]);

                        Class classOfParent = Class.forName(beanParentDefinition.getBeanClassName());
                        boolean isAssignable = classOfParent.isAssignableFrom(classForSuperClass);

                        if (beanParentDefinition.getBeanClassName().equalsIgnoreCase(superClassName)
                                || isAssignable) {
                            beanNameOfParent = beanNamesForType[0];
                        } else if (beanNamesForType[0].equalsIgnoreCase(beanNameOfChildren)) {
                            isValidSuperClass = false;
                        }

                    }


                    if (beanNameOfParent == null
                            || beanNameOfParent.equalsIgnoreCase("")
                            || beanParentDefinition == null) {

                        if (!isValidSuperClass) {
                            throw new BeanCreationException("Bean " + beanNameOfChildren + " must extends a unique spring bean component  or specify extendBeanId. Invalid superClass " + superClassName + " (" + beanNamesForType.toString() + ")");
                        } else {
                            throw new BeanCreationException("Bean " + beanNameOfChildren + " must extends a unique spring bean component or specify extendBeanId ,empty or too much spring bean for the " + superClassName + " (" + beanNamesForType.toString() + ")");
                        }
                    }

                    addInMapRegistry.add(new ReplacerKeyRegistry(beanNameOfChildren, beanChildrenResult, beanNameOfParent, beanParentDefinition));
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



}
