package com.baobaotao.advisor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestControlFlowAdvisor {
	public static void main(String[] args) {
		String configPath = "advisor_config.xml";
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configPath);
		Waiter waiter = (Waiter) ctx.getBean("waiter3");
		WaiterDelegate wd = new WaiterDelegate();
        wd.setWaiter(waiter);
		waiter.serveTo("Peter");
		waiter.greetTo("Peter");
		wd.service("Peter");
	}
}
