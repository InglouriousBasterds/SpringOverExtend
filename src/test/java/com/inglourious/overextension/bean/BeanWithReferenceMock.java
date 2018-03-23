package com.inglourious.overextension.bean;

public class BeanWithReferenceMock {
    private String a;
    private String b;
    private BeanProductMock mock;


    public BeanProductMock getMock() {
        return mock;
    }


    public void setMock(BeanProductMock mock) {
        this.mock = mock;
    }


    public String getA() {
        return a;
    }


    public void setA(String a) {
        this.a = a;
    }


    public String getB() {
        return b;
    }


    public void setB(String b) {
        this.b = b;
    }


    public String cal() {
        return " padre :calling bean: " + a + " & " + b;
    }
}
