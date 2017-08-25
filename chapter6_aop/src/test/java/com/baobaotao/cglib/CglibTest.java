package com.baobaotao.cglib;

import net.sf.cglib.beans.*;
import net.sf.cglib.core.KeyFactory;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.*;
import net.sf.cglib.reflect.MethodDelegate;
import net.sf.cglib.util.StringSwitcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.objectweb.asm.Type;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ryanjiang on 2017/8/25.
 */
@RunWith(JUnit4.class)
public class CglibTest {

    @Test
    public void testFixedValue() throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new FixedValue() {
            public Object loadObject() throws Exception {
                return "Hello cglib!";
            }
        });
        SampleClass proxy = (SampleClass) enhancer.create();
        assertEquals("Hello cglib!", proxy.test(null));
    }

    @Test(expected = RuntimeException.class)
    public void testInvocationHandler() throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                if(method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                    System.out.println("method declaring class: " + method.getDeclaringClass());
                    return "Hello cglib!";
                } else {
                    throw new RuntimeException("Do not know what to do.");
                }
            }
        });
        SampleClass proxy = (SampleClass) enhancer.create();
        assertEquals("Hello cglib!", proxy.test(null));

        proxy.toString(); // will throw runtime exception
    }

    @Test
    public void testMethodInterceptor() throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
                    throws Throwable {
                if(method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                    return "Hello cglib!";
                } else {
                    return proxy.invokeSuper(obj, args);
                }
            }
        });
        SampleClass proxy = (SampleClass) enhancer.create();
        assertEquals("Hello cglib!", proxy.test(null));
        assertNotEquals("Hello cglib!", proxy.toString());
        System.out.println("proxy toString: " + proxy.toString());
        System.out.println("proxy hash code: " + proxy.hashCode());
    }

    @Test
    public void testCallbackFilter() throws Exception {
        Enhancer enhancer = new Enhancer();
        CallbackHelper callbackHelper = new CallbackHelper(SampleClass.class, new Class[0]) {
            @Override
            protected Object getCallback(Method method) {
                if(method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                    return new FixedValue() {
                        public Object loadObject() throws Exception {
                            return "Hello cglib!";
                        }
                    };
                } else {
                    return NoOp.INSTANCE; // A singleton provided by NoOp.
                }
            }
        };
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallbackFilter(callbackHelper);
        enhancer.setCallbacks(callbackHelper.getCallbacks());
        SampleClass proxy = (SampleClass) enhancer.create();
        assertEquals("Hello cglib!", proxy.test(null));
        assertNotEquals("Hello cglib!", proxy.toString());

        System.out.println("proxy toString: " + proxy.toString());
        System.out.println("proxy hash code: " + proxy.hashCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testImmutableBean() throws Exception {
        SampleClass bean = new SampleClass();
        bean.setInput("Hello world!");
        SampleClass immutableBean = (SampleClass) ImmutableBean.create(bean);
        assertEquals("Hello world!", immutableBean.getInput());
        bean.setInput("Hello world, again!");
        assertEquals("Hello world, again!", immutableBean.getInput());
        immutableBean.setInput("Hello cglib!"); // Causes exception.
    }

    @Test
    public void testBeanGenerator() throws Exception {
        BeanGenerator beanGenerator = new BeanGenerator();
        beanGenerator.addProperty("value", String.class);
        beanGenerator.addProperty("name", String.class);
        Object myBean = beanGenerator.create();

        Method setter = myBean.getClass().getMethod("setValue", String.class);
        setter.invoke(myBean, "Hello cglib!");

        Method setter1 = myBean.getClass().getMethod("setName", String.class);
        setter1.invoke(myBean, "Ryan");

        Method getter = myBean.getClass().getMethod("getValue");
        assertEquals("Hello cglib!", getter.invoke(myBean));

        Method getter1 = myBean.getClass().getMethod("getName");
        assertEquals("Ryan", getter1.invoke(myBean));
    }

    @Test
    public void testBeanCopier() throws Exception {
        BeanCopier copier = BeanCopier.create(SampleBean.class, OtherSampleBean.class, false);
        SampleBean bean = new SampleBean();
        bean.setValue("Hello cglib!");
        OtherSampleBean otherBean = new OtherSampleBean();
        copier.copy(bean, otherBean, null);
        assertEquals("Hello cglib!", otherBean.getValue());
    }

    @Test
    public void testBulkBean() throws Exception {
        BulkBean bulkBean = BulkBean.create(SampleBean.class,
                new String[]{"getValue"},
                new String[]{"setValue"},
                new Class[]{String.class});
        SampleBean bean = new SampleBean();
        bean.setValue("Hello world!");
        assertEquals(1, bulkBean.getPropertyValues(bean).length);
        assertEquals("Hello world!", bulkBean.getPropertyValues(bean)[0]);
        bulkBean.setPropertyValues(bean, new Object[] {"Hello cglib!"});
        assertEquals("Hello cglib!", bean.getValue());
    }

    @Test
    public void testBeanMap() throws Exception {
        SampleBean bean = new SampleBean();
        bean.setValue("Hello cglib!");
        bean.setName("ryan");

        BeanMap map = BeanMap.create(bean);
        assertEquals("Hello cglib!", map.get("value"));
        assertEquals("ryan", map.get("name"));
    }


    @Test
    public void testKeyFactory() throws Exception {
        SampleKeyFactory keyFactory = (SampleKeyFactory) KeyFactory.create(SampleKeyFactory.class);
        Object key = keyFactory.newInstance("foo", 42);
        Map<Object, String> map = new HashMap<Object, String>();
        map.put(key, "Hello cglib!");
        assertEquals("Hello cglib!", map.get(keyFactory.newInstance("foo", 42)));
    }

    @Test
    public void testMixin() throws Exception {
        Mixin mixin = Mixin.create(new Class[]{Interface1.class, Interface2.class,
                MixinInterface.class}, new Object[]{new Class1(), new Class2()});
        MixinInterface mixinDelegate = (MixinInterface) mixin;
        assertEquals("first", mixinDelegate.first());
        assertEquals("second", mixinDelegate.second());
    }

    @Test
    public void testStringSwitcher() throws Exception {
        String[] strings = new String[]{"one", "two"};
        int[] values = new int[]{10, 20};
        StringSwitcher stringSwitcher = StringSwitcher.create(strings, values, true);
        assertEquals(10, stringSwitcher.intValue("one"));
        assertEquals(20, stringSwitcher.intValue("two"));
        assertEquals(-1, stringSwitcher.intValue("three"));
    }

    @Test
    public void testInterfaceMaker() throws Exception {
        Signature signature = new Signature("foo", Type.DOUBLE_TYPE, new Type[]{Type.INT_TYPE});
        InterfaceMaker interfaceMaker = new InterfaceMaker();
        interfaceMaker.add(signature, new Type[0]);
        Class iface = interfaceMaker.create();
        assertEquals(1, iface.getMethods().length);
        assertEquals("foo", iface.getMethods()[0].getName());
        assertEquals(double.class, iface.getMethods()[0].getReturnType());
    }

    @Test
    public void testMethodDelegate() throws Exception {
        SampleBean bean = new SampleBean();
        bean.setValue("Hello cglib!");
        bean.setName("ryan");

        BeanDelegate delegate = (BeanDelegate) MethodDelegate.create(
                bean, "getName", BeanDelegate.class);
//        assertEquals("Hello cglib!", delegate.getValueFromDelegate());
        assertEquals("ryan", delegate.getAbc());
    }
}
