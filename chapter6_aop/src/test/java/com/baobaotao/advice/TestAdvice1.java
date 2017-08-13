package com.baobaotao.advice;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestAdvice1 {
	public static void main(String[] args) {
		String configPath = "advice_config.xml";
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configPath);
		Waiter waiter = (Waiter)ctx.getBean("waiter");
		waiter.greetTo("John");
	}
}
