package com.inglourious.overextension.fixture.bean;

import com.inglourious.overextension.fixture.beanWithAnnotation.FinalVariable;

import java.util.Map;

/**
 * Created by gbrescia on 27/03/2018.
 */
public class AbstractMainConfigurationTestBean {
    private String configA = FinalVariable.CONFIG_A;
    private String configB = FinalVariable.CONFIG_B;
    private String configC = FinalVariable.CONFIG_C;
    private Map mapOfSomeDefinition;

    public String getConfigA() {
        return this.configA;
    }

    public String getConfigB() {
        return this.configB;
    }

    public String getConfigC() {
        return this.configC;
    }

    public java.util.Map getMapOfSomeDefinition() {
        return this.mapOfSomeDefinition;
    }

    public void setConfigA(String configA) {
        this.configA = configA;
    }

    public void setConfigB(String configB) {
        this.configB = configB;
    }

    public void setConfigC(String configC) {
        this.configC = configC;
    }

    public void setMapOfSomeDefinition(java.util.Map mapOfSomeDefinition) {
        this.mapOfSomeDefinition = mapOfSomeDefinition;
    }
}
