package com.inglourious.overextension;

import com.inglourious.overextension.beanWithAnnotation.FinalVariable;
import com.inglourious.overextension.beanWithAnnotation.InjectOfMainConfigurationTestBean;
import com.inglourious.overextension.beanWithAnnotation.MainConfigurationTestBean;
import com.inglourious.overextension.beanWithAnnotation.OverExtendMainConfigurationTestBean;
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


    @Test
    public final void testProjectA() {

        MainConfigurationTestBean beanExtended = ctx.getBean(MainConfigurationTestBean.class);
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
}
