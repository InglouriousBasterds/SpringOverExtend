package com.inglourious.overextension.beanWithAnnotationExtendId;

import com.inglourious.overextension.bean.AbstractMainConfigurationTestBean;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by gbrescia on 27/03/2018.
 */
@Data
@Component("BeanC_Child")
public class MainConfigurationWithIdTestBean extends AbstractMainConfigurationTestBean {

}
