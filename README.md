 [ ![Download](https://api.bintray.com/packages/giskard80/InglouriousBasterds/spring-overextend/images/download.svg) ](https://bintray.com/giskard80/InglouriousBasterds/spring-overextend/_latestVersion) [![CircleCI](https://circleci.com/gh/InglouriousBasterds/SpringOverExtend/tree/master.svg?style=svg)](https://circleci.com/gh/InglouriousBasterds/SpringOverExtend/tree/master)   

# Spring Over Extension
This library provides an XML namespace handler and an Annotation that allows for overriding, extending or modifying beans in Spring environment. 
Spring Over Extension is a component for extend an existing bean and inject a custom Spring Bean over.

If you are using Maven, include the dependency into your pom.xml
```
<dependency>
    <groupId>com.inglourious.overextension</groupId>
    <artifactId>spring-overextend</artifactId>
    <version>${spring-overextend.version}</version>
</dependency>
```

The target of this library is to give the chance to extend the spring bean defined in the application context of spring, and override the method or property defined in the original bean.
This library is tested and works with Spring 5 

Concept example for XML configuration:
Main spring beans file (file master) in a context of a dependency library:
```
<bean id="BeanA" class="com.inglourious.overextension.bean.BeanProductMock">
    <property name="a" value="A main value"/>
    <property name="b" value="B main value"/>
</bean>
```

Project spring file (file slave) in a project that wants to override some method of the parent without change the parent bean id but inhering his properties and functions
```
<bean id="BeanA" class="com.inglourious.overextension.bean.BeanChildrenMock"  over:extension="abstract" >
    <property name="a" value="A override value"/>
    <property name="c" value="C children value"/>
</bean>
```



## How To use in Xml Configuration

Let's assume you have the following configuration in your main project (application-context-main.xml):

```
<beans 
    xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd" >

    <bean id="dataOrderManager" class="com.mycompany.manager.OrderManagerMainImplementation"  >
        <property name="skipLogging" value="false"/>
        <property name="limitOrdersNumber" value="100"/>
        <property name="customerManager" ref="customerManager" />
    </bean>

    <bean id="customerManager" class="com.mycompany.manager.CustomerManagerMainImplementation"  >
        <property name="toMail" value="user@mail.com"/>
    </bean>
</beans>
```

Now a submodule can change application's behavior by providing a configuration such as (application-context-sub.xml):

```
<beans 
    xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:over="http://www.inglourious.com/schema/spring/ExtensionSchema"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.inglourious.com/schema/spring/ExtensionSchema
    http://www.inglourious.com/schema/spring/ExtensionSchema.xsd" >

    <bean id="dataOrderManager" class="com.mycompany.manager.overextension.CustomOrderManagerMainImplementation"  over:extension="abstract" >
        <property name="skipLogging" value="true"/>   <!-- Setting skipLoggin to true -->
        <property name="limitOrdersNumber" value="200"/>  <!-- Setting new limit to 200 -->
        <!-- <property name="customerManager" ref="customerManager" /> Omitted because we mantain the same property of the original bean=dataOrderManager -->
    </bean>
</beans>
```


In this scenario every injection of dataOrderManager would be replaced with new one that extends and inherit the property of the original spring bean.


## How To use with Annotation
Extending a spring bean is possible using the annotation @OverExtension and enableing the componentScan of spring
on package "com.inglourious.overextension". So in your java project you must add:

```
@ComponentScan({"com.inglourious.overextension"})
```


Let's assume you have the following configuration in your main project:

```
@Component
public class MainConfigurationTestBean {

    private String valueForExample = "Hello from main configuration!!!";

    public String getValueForExample() {
        return valueForExample;
    }

    public void setValueForExample(String valueForExample) {
        this.valueForExample = valueForExample;
    }    
}
```


Now a submodule can change application's behavior by providing an override configuration such as:

```
import com.inglourious.overextension.annotation.OverExtension;

@OverExtension
public class OverrideMainConfigurationTestBean extends MainConfigurationTestBean {

    public OverrideMainConfigurationTestBean() {
        super();
        this.setValueForExample("Hello from override configuration!!!");
    }
}
```

In this scenario every injection of MainConfigurationTestBean would be replaced with new one that extends and inherit the property of the original spring bean.
It's also possible to specify the id of the spring that want to extends with the attribute 'extendBeanId' of the annotation. 
For example we can override a spring bean that has id = "org.springframework.context.annotation.internalAutowiredAnnotationProcessor"
 
```
import com.inglourious.overextension.annotation.OverExtension;

@OverExtension(extendBeanId = "org.springframework.context.annotation.internalAutowiredAnnotationProcessor")
public class OverExtendAutowiredAnnotationProcessor extends AutowiredAnnotationBeanPostProcessor {


    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);

        //CODE OR DEFINE WHAT YOU NEED
    }
}
```
 
All autowired dependencies to bean will see the modifications made by the OverExtension, so you are able to modify every bean in the registry of spring, reading at startup of the application.

 
 
 

