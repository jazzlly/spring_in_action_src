package com.baobaotao.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by ryanjiang on 2017/8/25.
 */
public class SampleClass {
    private String input;
    public String test(String input) {
        this.input = input;
        return "Hello world! " + input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
