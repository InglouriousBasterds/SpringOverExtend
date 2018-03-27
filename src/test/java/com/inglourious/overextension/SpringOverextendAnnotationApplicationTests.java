package com.inglourious.overextension;

import com.inglourious.overextension.bean.AbstractMainConfigurationTestBean;
import com.inglourious.overextension.beanWithAnnotation.FinalVariable;
import com.inglourious.overextension.beanWithAnnotation.InjectOfMainConfigurationTestBean;
import com.inglourious.overextension.beanWithAnnotation.MainConfigurationTestBean;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringOverextendAnnotationApplicationTests extends TestCase {

    @Autowired
    ApplicationContext ctx;


    static public int setValueFromOverExtendAutowiredAnnotationProcessor = 0;

    @Test
    public final void testProjectA() {

        AbstractMainConfigurationTestBean beanExtended = ctx.getBean(MainConfigurationTestBean.class);
        assertNotNull(beanExtended);
        assertTrue(beanExtended.getConfigA().equalsIgnoreCase(FinalVariable.CONFIG_A));
        assertTrue(beanExtended.getConfigC().equalsIgnoreCase(FinalVariable.CONFIG_C_EXTENDED));

        beanExtended = (MainConfigurationTestBean) ctx.getBean("mainConfigurationTestBean");
        assertNotNull(beanExtended);
        assertTrue(beanExtended.getConfigA().equalsIgnoreCase(FinalVariable.CONFIG_A));
        assertTrue(beanExtended.getConfigC().equalsIgnoreCase(FinalVariable.CONFIG_C_EXTENDED));
    }

    @Test
    public final void testProjectInject() {

        InjectOfMainConfigurationTestBean injectABeanExtended = ctx.getBean(InjectOfMainConfigurationTestBean.class);
        assertNotNull(injectABeanExtended);
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
        assertNotNull(beanExtended);

        assertEquals(beanExtended.getConfigB(), FinalVariable.CONFIG_B_EXTENDED);
        assertTrue(beanExtended.getConfigA().equalsIgnoreCase(FinalVariable.CONFIG_A));

        InjectOfMainConfigurationTestBean injectABeanExtended = ctx.getBean(InjectOfMainConfigurationTestBean.class);
        assertTrue(injectABeanExtended.getMainConfigurationTestBeanId().getConfigB().equalsIgnoreCase(FinalVariable.CONFIG_B_EXTENDED));
    }

    @Test
    public final void testOverExtendSpringBeanIdCore() {
        assertTrue(setValueFromOverExtendAutowiredAnnotationProcessor != 0 );
    }

}
