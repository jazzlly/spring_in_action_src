package com.baobaotao.aspectj.example;

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import com.baobaotao.NaiveWaiter;
import com.baobaotao.Waiter;

public class AspectJProxyTest {
	public static void main(String[] args) {

		// create target
		Waiter target = new NaiveWaiter();
		// create factory
		AspectJProxyFactory factory = new AspectJProxyFactory();
		// set target
		factory.setTarget(target);
		// add aspect
		factory.addAspect(PreGreetingAspect.class);
		// create proxy object
		Waiter proxy = factory.getProxy();
		proxy.greetTo("John");
		proxy.serveTo("John");

	}
}
