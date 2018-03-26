package com.inglourious.overextension.beanWithAnnotation;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gbrescia on 26/03/2018.
 */
@Data
@AllArgsConstructor
@Component
public class InjectOfMainConfigurationTestBean {

    @Autowired
    MainConfigurationTestBean mainConfigurationTestBean;


}
