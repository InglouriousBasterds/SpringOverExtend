package com.inglourious.overextension.beanWithAnnotation;

import com.inglourious.overextension.bean.AbstractMainConfigurationTestBean;
import com.inglourious.overextension.bean.BeanProductInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@NoArgsConstructor
@Component
@Primary
public class MainConfigurationTestBean extends AbstractMainConfigurationTestBean {


}
