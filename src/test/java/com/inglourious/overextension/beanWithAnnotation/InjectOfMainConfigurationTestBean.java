package com.inglourious.overextension.beanWithAnnotation;

import com.inglourious.overextension.bean.AbstractMainConfigurationTestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by gbrescia on 26/03/2018.
 */
@Component
public class InjectOfMainConfigurationTestBean {

    @Autowired
    AbstractMainConfigurationTestBean mainConfigurationTestBean;


    @Autowired
    @Qualifier("BeanC_Child")
    AbstractMainConfigurationTestBean mainConfigurationTestBeanId;

    public AbstractMainConfigurationTestBean getMainConfigurationTestBean() {
        return mainConfigurationTestBean;
    }

    public AbstractMainConfigurationTestBean getMainConfigurationTestBeanId() {
        return mainConfigurationTestBeanId;
    }
}
