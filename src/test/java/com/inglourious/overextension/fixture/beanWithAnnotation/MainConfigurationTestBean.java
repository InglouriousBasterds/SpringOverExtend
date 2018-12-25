package com.inglourious.overextension.fixture.beanWithAnnotation;

import com.inglourious.overextension.fixture.bean.AbstractMainConfigurationTestBean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Component
@Primary
public class MainConfigurationTestBean extends AbstractMainConfigurationTestBean {


}
