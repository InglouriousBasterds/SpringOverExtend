package com.inglourious.overextension;

import com.inglourious.overextension.annotation.OverExtension;
import com.inglourious.overextension.bean.ReplacerKeyRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.inglourious.overextension.ExtensionBeanDefinitionDecorator.SUFFIX_BEAN_EXTENDED;

/**
 * Created by gbrescia on 26/03/2018.
 */
@Component
public class ExtensionBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    /** The logger. */
    protected final Log logger = LogFactory.getLog(ExtensionBeanDefinitionRegistryPostProcessor.class);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {


        if (registry instanceof ConfigurableListableBeanFactory) {
            List<ReplacerKeyRegistry> addInMapRegistry = new ArrayList<ReplacerKeyRegistry>();
            ListableBeanFactory configurableListableBeanFactory = (ConfigurableListableBeanFactory) registry;
            String[] beanDefinitionNames = configurableListableBeanFactory.getBeanDefinitionNames();

            for (String beanDefinitionName : beanDefinitionNames) {

                OverExtension annotationOnBean = configurableListableBeanFactory.findAnnotationOnBean(beanDefinitionName, OverExtension.class);

                try {
                    if (annotationOnBean != null) {
                        String beanNameOfChildren = beanDefinitionName;
                        if (logger.isDebugEnabled()) {
                            logger.debug("Find " + beanNameOfChildren + " candidate for OverExtension");
                        }

                        ScannedGenericBeanDefinition beanChildrenResult = (ScannedGenericBeanDefinition) ((ConfigurableListableBeanFactory) registry).getBeanDefinition(beanNameOfChildren);
                        String superClassName = beanChildrenResult.getMetadata().getSuperClassName();
                        Map<String, Object> annotationAttributes = beanChildrenResult.getMetadata().getAnnotationAttributes(OverExtension.class.getName(), true);
                        if (superClassName == null
                                || "".equalsIgnoreCase(superClassName)) {
                            throw new BeanCreationException("Bean " + beanNameOfChildren + " annotated with OverExtension must extend a superclass");
                        }

                        String[] beanNamesForType;
                        Object extendBeanId = annotationAttributes.get("extendBeanId");
                        if (extendBeanId != null
                                && !"".equalsIgnoreCase(extendBeanId.toString())) {
                            beanNamesForType = new String[]{extendBeanId.toString()};
                        } else {
                            beanNamesForType = ((ConfigurableListableBeanFactory) registry).getBeanNamesForType(Class.forName(superClassName));
                        }

                        if (beanNamesForType == null) {
                            throw new BeanCreationException("Bean " + beanNameOfChildren + " must extends a spring bean component or specify extendBeanId , doesn't exist a spring bean for the class " + superClassName + " ");
                        }

                        //The bean name for the parent to extend
                        String beanNameOfParent = null;
                        BeanDefinition beanParentDefinition = null;

                        if (beanNamesForType.length > 1) {
                            for (String beanNameForType : beanNamesForType) {
                                beanParentDefinition = (BeanDefinition) registry.getBeanDefinition(beanNameForType);
                                if (beanParentDefinition.getBeanClassName().equalsIgnoreCase(superClassName)) {
                                    beanNameOfParent = beanNameForType;
                                    break;
                                }
                            }
                        } else {
                            beanParentDefinition = (BeanDefinition) registry.getBeanDefinition(beanNamesForType[0]);
                            if (beanParentDefinition.getBeanClassName().equalsIgnoreCase(superClassName)) {
                                beanNameOfParent = beanNamesForType[0];
                            }
                        }


                        if (beanNameOfParent == null
                                || beanNameOfParent.equalsIgnoreCase("")
                                    || beanParentDefinition == null) {
                            throw new BeanCreationException("Bean " + beanNameOfChildren + " must extends a unique spring bean component or specify extendBeanId ,empty or too much spring bean for the class " + superClassName + " (" + beanNamesForType.toString() + ")");
                        }

                        addInMapRegistry.add(new ReplacerKeyRegistry( beanNameOfChildren, beanChildrenResult, beanNameOfParent, beanParentDefinition));
                    }
                } catch (ClassNotFoundException e) {
                    logger.error("",e );
                } catch (BeanCreationException be) {
                    logger.error("Bean Creation error on OverExtension", be );
                }
            }


            if(addInMapRegistry.size() > 0) {
                for (ReplacerKeyRegistry replacerKeyRegistry : addInMapRegistry) {
                    remappingRegistry(registry, replacerKeyRegistry.getBeanNameOfChildren(), replacerKeyRegistry.getBeanChildrenResult(), replacerKeyRegistry.getBeanNameOfParent(), replacerKeyRegistry.getBeanParentDefinition());
                }
            }
        }
    }

    private void remappingRegistry(BeanDefinitionRegistry registry, String beanNameOfChildren, ScannedGenericBeanDefinition beanChildrenResult, String beanNameOfParent, BeanDefinition beanParentDefinition) {
        // rimuoviamo il bean originale dal registry
        registry.removeBeanDefinition(beanNameOfParent);

        //Rendiamo abstract il parent
        ((AbstractBeanDefinition)beanParentDefinition).setAbstract(true);

        // rinominiamo il bean originale secondo la convenzione definita e lo salviamo nel registry
        String newParentName = buildParentName(beanNameOfParent);
        registry.registerBeanDefinition(newParentName, beanParentDefinition);

        // rimuoviamo il bean originale dal registry
        registry.removeBeanDefinition(beanNameOfChildren);

        //Rendiamo abstract il parent
        beanChildrenResult.setParentName(newParentName);

        registry.registerBeanDefinition(beanNameOfParent, beanChildrenResult);
    }


    public String buildParentName(String parentName) {
        return parentName+"_"+ SUFFIX_BEAN_EXTENDED;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {;}
}
