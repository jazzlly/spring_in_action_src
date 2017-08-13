package com.baobaotao.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PerformaceHandler implements InvocationHandler {
    private Object target;
	public PerformaceHandler(Object target){
		this.target = target;
	}
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println("begin handing ...");
		PerformanceMonitor.begin(target.getClass().getName()+"."+ method.getName());
		Object obj = method.invoke(target, args);
		PerformanceMonitor.end();
		System.out.println("end handing!");
		return obj;
	}
}
