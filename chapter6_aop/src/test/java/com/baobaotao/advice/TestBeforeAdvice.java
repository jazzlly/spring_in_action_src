package com.baobaotao.advice;

import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;

public class TestBeforeAdvice {
	public static void main(String[] args) {
	    // target object
        Waiter target = new NaiveWaiter();
        // advice
        BeforeAdvice  advice = new GreetingBeforeAdvice();

        // proxy factory
        ProxyFactory pf = new ProxyFactory();
        // proxy factory: set target
        pf.setInterfaces(target.getClass().getInterfaces());
        pf.setTarget(target);
        // proxy factory: set advice
        pf.addAdvice(advice);
        pf.setOptimize(true);

        // create proxy and invoke
        Waiter proxy = (Waiter)pf.getProxy(); 
        proxy.greetTo("John");
        proxy.serveTo("Tom");
	}
}
