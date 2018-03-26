package com.inglourious.overextension.beanWithAnnotation;

import com.inglourious.overextension.bean.BeanProductInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class MainConfigurationTestBean {

    private String configA = FinalVariable.CONFIG_A;
    private String configB = FinalVariable.CONFIG_B;
    private String configC = FinalVariable.CONFIG_C;
    private Map mapOfSomeDefinition;


}
