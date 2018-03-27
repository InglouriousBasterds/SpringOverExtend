# SpringOverExtend 
Spring Over Extension is a component for extend an existing bean and inject a custom Spring Bean over.
  

### Intro
The target of this library is to give the chance to extend the spring bean defined in the application context of spring, and override the method or property defined in the original bean

Example:
Main spring beans file (file master) in a context of a dependency library:
<code>

     <bean id="BeanA" class="com.inglourious.overextension.bean.BeanProductMock"  >
      <property name="a" value="A padre"/>
      <property name="b" value="B padre"/>
     </bean>
 
</code>

Project spring file (file slave) in a project that wants to override some method of the parent without change the parent bean id but inhetiing his properties and functions
<code>

    <bean id="BeanA" class="com.inglourious.overextension.bean.BeanChildrenMock" over:extension="abstract" >
       <property name="a" value="A figlio"/>
       <property name="c" value="C figlio"/>
    </bean>

</code>


## Maven Dependency


## How To

