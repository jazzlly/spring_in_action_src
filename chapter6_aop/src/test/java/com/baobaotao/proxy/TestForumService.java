package com.baobaotao.proxy;

import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Proxy;

@RunWith(JUnit4.class)
public class TestForumService {

	@Test
	public void testNoAop() throws Exception {
		// 业务类正常编码的测试
		ForumService forumService = new ForumServiceImpl();
		forumService.removeForum(10);
		forumService.removeTopic(1012);
	}

	@Test
	public void testCGLib() throws Exception {
		//使用CGLib动态代理
		CglibProxy proxy = new CglibProxy();
		ForumService forumService = (ForumService)proxy.getProxy(ForumServiceImpl.class);
		forumService.removeForum(10);
		forumService.removeTopic(1023);
	}

	@Test
	public void testJdkDynamicProxy() throws Exception {
		// 使用JDK动态代理
		ForumService target = new ForumServiceImpl();
		PerformaceHandler handler = new PerformaceHandler(target);
		ForumService proxy = (ForumService) Proxy.newProxyInstance(target
						.getClass().getClassLoader(),
				target.getClass().getInterfaces(), handler);
		proxy.removeForum(10);
		proxy.removeTopic(1012);

	}
}
