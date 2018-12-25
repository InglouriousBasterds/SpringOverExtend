package com.inglourious.overextension.fixture.bean;

public class BeanWithReferenceMockExtParentMock extends BeanParentMock {

    private String a;
    private String b;
    private BeanParentMock mockParent;


    public BeanParentMock getMockParent() {
        return mockParent;
    }


    public void setMockParent(BeanParentMock mockParent) {
        this.mockParent = mockParent;
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
