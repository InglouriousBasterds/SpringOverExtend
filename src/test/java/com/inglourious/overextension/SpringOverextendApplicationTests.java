package com.inglourious.overextension;

import com.inglourious.overextension.bean.BeanParentMock;
import com.inglourious.overextension.bean.BeanProductMock;
import com.inglourious.overextension.bean.BeanWithReferenceMock;
import com.inglourious.overextension.bean.BeanWithReferenceMockExtParentMock;
import com.inglourious.overextension.xml.ExtensionBeanDefinitionDecorator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config/main-spring-beans.xml",
        "classpath:spring-config/project-spring-beans-A.xml",
        "classpath:spring-config/project-spring-beans-B.xml",
        "classpath:spring-config/project-spring-beans-C.xml",})
public class SpringOverextendApplicationTests {

    @Autowired
    ApplicationContext ctx;

    /**
     * bean A product beans in my project spring confing,
     * I redefine the bean by changing the value of property A and extending its behavior using attribute C. I compare the results with the original product bean.
     */
    @Test
    public final void testProjectA() {
        assertTrue(ctx.containsBean("BeanA" + ExtensionBeanDefinitionDecorator.SUFFIX_BEAN_EXTENDED));

        BeanProductMock beanExtended = (BeanProductMock) ctx.getBean("BeanA");

        assertEquals(beanExtended.getB(), "B padre");
        assertEquals(beanExtended.getA(), "A figlio");
        assertEquals(beanExtended.getC(), "C figlio");
    }

    /**
     * in my spring bean project config, I create Bean which refers to the B product bean, which always extends it in my confing file.
     */
    @Test
    public final void testProjectB() {
        BeanWithReferenceMock beanChild = (BeanWithReferenceMock) ctx.getBean("BeanCProgetto");
        BeanProductMock myBeanExtended = beanChild.getMock();
        BeanProductMock beanBExt = (BeanProductMock) ctx.getBean("BeanB");
        assertEquals(myBeanExtended, beanBExt);
    }

    /**
     * in the beanC product spring bean refers to a BeanC_Child mock that is extended to projectC in the beanC product spring bean extends BeanC_Father which is overridden on projectC
     */
    @Test
    public final void testProjectC() {

        BeanWithReferenceMockExtParentMock beanC = (BeanWithReferenceMockExtParentMock) ctx.getBean("BeanC");

        BeanProductMock beanA = (BeanProductMock) ctx.getBean("BeanA");
        assertNotSame(beanC.getMockParent().getMock(), beanA);

        // BeanC-Father viene overridato in progettoC
        BeanParentMock beanCFather = (BeanParentMock) ctx.getBean("BeanC_Father");
        assertEquals(beanCFather.getUno(), beanC.getUno());
        assertEquals(beanCFather.getDue(), beanC.getDue());
    }

    /**
     * in the beanD product spring bean it refers to a mock BeanD that is extended on projectA and on projectB
     */
    @Test
    public final void testProjectD() {

        BeanProductMock beanD = (BeanProductMock) ctx.getBean("BeanD");

        // BeanC-Father viene overridato in progettoC
        assertEquals(beanD.getA(), "A padre");
        assertEquals(beanD.getB(), "B figlio");
        assertEquals(beanD.getC(), "C nipote");
    }

    /**
     * bean A product beans in my project spring confing, I redefine the bean by changing the value of property A and extending the values of the map contained within the parent bean.
     * NB: Use the standard spring merge attribute I compare the results with the original product bean.
     */
    @Test
    public final void testProjectMergeMappa() {
        assertTrue(ctx.containsBean("BeanTestMappa" + ExtensionBeanDefinitionDecorator.SUFFIX_BEAN_EXTENDED));

        BeanProductMock beanExtended = (BeanProductMock) ctx.getBean("BeanTestMappa");
        Iterator it = beanExtended.getMappa().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();

            if (pairs.getKey().equalsIgnoreCase("key")) {
                assertEquals(pairs.getValue(), "valoreProdotto");
            }

            if (pairs.getKey().equalsIgnoreCase("key2")) {
                assertEquals(pairs.getValue(), "valoreFiglio");
            }
        }
    }
}
