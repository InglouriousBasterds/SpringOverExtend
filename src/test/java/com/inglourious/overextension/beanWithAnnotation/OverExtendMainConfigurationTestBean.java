package com.inglourious.overextension.beanWithAnnotation;

import com.inglourious.overextension.annotation.OverExtension;
import com.inglourious.overextension.bean.AbstractMainConfigurationTestBean;
import lombok.Data;


@Data
@OverExtension
public class OverExtendMainConfigurationTestBean extends MainConfigurationTestBean {


    public OverExtendMainConfigurationTestBean() {
        super();
        this.setConfigC(FinalVariable.CONFIG_C_EXTENDED);
    }
}
