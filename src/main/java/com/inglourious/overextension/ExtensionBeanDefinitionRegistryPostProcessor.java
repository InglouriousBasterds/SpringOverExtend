package com.inglourious.overextension;

import com.inglourious.overextension.annotation.OverExtension;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.inglourious.overextension.ExtensionBeanDefinitionDecorator.SUFFIX_BEAN_EXTENDED;

/**
 * Created by gbrescia on 26/03/2018.
 */
@Component
public class ExtensionBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    /** The logger. */
    protected final Log logger = LogFactory.getLog(ExtensionBeanDefinitionRegistryPostProcessor.class);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException{


        if ( registry instanceof ConfigurableListableBeanFactory) {
            ListableBeanFactory configurableListableBeanFactory = (ConfigurableListableBeanFactory)registry;


          /* Map<String, Object> beansWithAnnotation = configurableListableBeanFactory.getBeansWithAnnotation(OverExtension.class);
            for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()){
                System.out.println(entry.getKey() + "/" + entry.getValue());
            }*/

            Set<String> alreadyReplaced = new HashSet<String>();
            String[] beanDefinitionNames = configurableListableBeanFactory.getBeanDefinitionNames();
            for (String beanDefinitionName : beanDefinitionNames) {

                OverExtension annotationOnBean = configurableListableBeanFactory.findAnnotationOnBean(beanDefinitionName, OverExtension.class);
                if (annotationOnBean!=null && !alreadyReplaced.contains(beanDefinitionName)) {


                    String beanNameOfChildren = beanDefinitionName;
                    System.out.println("------------------------------------>"+beanNameOfChildren);
                    ScannedGenericBeanDefinition beanChildrenResult = (ScannedGenericBeanDefinition) ((ConfigurableListableBeanFactory) registry).getBeanDefinition(beanNameOfChildren);


                    String superClassName = beanChildrenResult.getMetadata().getSuperClassName();

                    System.out.println("------------------------------------>"+ superClassName );


                    try {
                        String[] beanNamesForType = ((ConfigurableListableBeanFactory) registry).getBeanNamesForType(Class.forName(superClassName));




                        for (String beanNameOfParent : beanNamesForType) {
                            System.out.println("------------------------------------> TROVATO PADRE N OME "+ beanNameOfParent );
                            System.out.println("------------------------------------>             "+registry.getBeanDefinition(beanNameOfParent).getBeanClassName() );
                            ScannedGenericBeanDefinition beanParentDefinition = (ScannedGenericBeanDefinition) registry.getBeanDefinition(beanNameOfParent);
                            if (beanParentDefinition.getBeanClassName().equalsIgnoreCase(superClassName)) {
                                System.out.println("------------------------------------>     TROVATEO E  mi fermo beanNameOfParent        "+registry.getBeanDefinition(beanNameOfParent).getBeanClassName() );

                                // rimuoviamo il bean originale dal registry
                                registry.removeBeanDefinition(beanNameOfParent);

                                //Rendiamo abstract il parent
                                beanParentDefinition.setAbstract(true);


                                // rinominiamo il bean originale secondo la convenzione definita e lo salviamo nel registry
                                String newParentName = buildParentName(beanNameOfParent);
                                registry.registerBeanDefinition(newParentName, beanParentDefinition);



                                // rimuoviamo il bean originale dal registry
                                registry.removeBeanDefinition(beanNameOfChildren);

                                //Rendiamo abstract il parent
                                beanChildrenResult.setParentName(newParentName);

                                registry.registerBeanDefinition(beanNameOfParent, beanChildrenResult);


                                alreadyReplaced.add(beanNameOfParent);





                                break;
                            }




                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


                }
            }
        }



//        var1.removeBeanDefinition("beanProductAnnotedMock");

        System.out.println("paperino");
        System.out.println("paperino");
        System.out.println("paperino");
        System.out.println("paperino");
        System.out.println("paperino");
        System.out.println("paperino");

    }









    public String buildParentName(String parentName) {
        return parentName+"_"+ SUFFIX_BEAN_EXTENDED;
    }











    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {




    }
}
