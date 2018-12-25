package com.inglourious.overextension.fixture.bean;

public class BeanChildrenMock extends BeanProductMock {


    private String c;


    public String getC() {
        return c;
    }


    public void setC(String c) {
        this.c = c;
    }


    public String secondCall() {
        return "seconda call " + c;
    }

    public String cal() {
        return getA() + " & " + getB() + " " + c;
    }

}
