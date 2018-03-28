package com.inglourious.overextension.beanWithAnnotationExtendId;

import com.inglourious.overextension.annotation.OverExtension;
import com.inglourious.overextension.beanWithAnnotation.FinalVariable;

/**
 * Created by gbrescia on 27/03/2018.
 */

@OverExtension(extendBeanId = "BeanC_Child")
public class OverExtendMainConfigurationWithIdTestBean extends MainConfigurationWithIdTestBean {


    public OverExtendMainConfigurationWithIdTestBean() {

        this.setConfigB(FinalVariable.CONFIG_B_EXTENDED);
    }
}
