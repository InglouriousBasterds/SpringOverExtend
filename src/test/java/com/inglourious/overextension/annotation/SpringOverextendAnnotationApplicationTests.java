package com.inglourious.overextension.annotation;

import com.inglourious.overextension.fixture.bean.AbstractMainConfigurationTestBean;
import com.inglourious.overextension.fixture.beanWithAnnotation.FinalVariable;
import com.inglourious.overextension.fixture.beanWithAnnotation.InjectOfMainConfigurationTestBean;
import com.inglourious.overextension.fixture.beanWithAnnotation.MainConfigurationTestBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestSpringOverextendConfiguration.class})
public class SpringOverextendAnnotationApplicationTests {

    @Autowired
    ApplicationContext ctx;


    static public int setValueFromOverExtendAutowiredAnnotationProcessor = 0;

    @Test
    public final void testProjectA() {

        AbstractMainConfigurationTestBean beanExtended = ctx.getBean(MainConfigurationTestBean.class);

        assertTrue(beanExtended.getConfigA().equalsIgnoreCase(FinalVariable.CONFIG_A));
        assertTrue(beanExtended.getConfigC().equalsIgnoreCase(FinalVariable.CONFIG_C_EXTENDED));

        beanExtended = (MainConfigurationTestBean) ctx.getBean("mainConfigurationTestBean");

        assertTrue(beanExtended.getConfigA().equalsIgnoreCase(FinalVariable.CONFIG_A));
        assertTrue(beanExtended.getConfigC().equalsIgnoreCase(FinalVariable.CONFIG_C_EXTENDED));
    }

    @Test
    public final void testProjectInject() {

        InjectOfMainConfigurationTestBean injectABeanExtended = ctx.getBean(InjectOfMainConfigurationTestBean.class);


        assertTrue(injectABeanExtended.getMainConfigurationTestBean().getConfigA().equalsIgnoreCase(FinalVariable.CONFIG_A));
        assertTrue(injectABeanExtended.getMainConfigurationTestBean().getConfigC().equalsIgnoreCase(FinalVariable.CONFIG_C_EXTENDED));
    }


    /**
     * bean A product beans in my project spring confing,
     * I redefine the bean by changing the value of property A and extending its behavior using attribute C. I compare the results with the original product bean.
     */
    @Test
    public final void testProjectWithId() {
        AbstractMainConfigurationTestBean beanExtended = (AbstractMainConfigurationTestBean) ctx.getBean("BeanC_Child");

        assertEquals(beanExtended.getConfigB(), FinalVariable.CONFIG_B_EXTENDED);
        assertTrue(beanExtended.getConfigA().equalsIgnoreCase(FinalVariable.CONFIG_A));

        InjectOfMainConfigurationTestBean injectABeanExtended = ctx.getBean(InjectOfMainConfigurationTestBean.class);
        assertTrue(injectABeanExtended.getMainConfigurationTestBeanId().getConfigB().equalsIgnoreCase(FinalVariable.CONFIG_B_EXTENDED));
    }

    @Test
    public final void testOverExtendSpringBeanIdCore() {
        assertTrue(setValueFromOverExtendAutowiredAnnotationProcessor != 0);
    }

}
