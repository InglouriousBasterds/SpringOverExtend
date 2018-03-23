package com.inglourious.overextension.bean;

import java.util.Map;

public class BeanProductMock implements BeanProductInterface {
    private String a;
    private String b;
    private String c;
    private Map mappa;


    public String getC() {
        return c;
    }


    public void setC(String c) {
        this.c = c;
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
        return a + " & " + b;
    }


    public Map getMappa() {
        return mappa;
    }


    public void setMappa(Map mappa) {
        this.mappa = mappa;
    }
}