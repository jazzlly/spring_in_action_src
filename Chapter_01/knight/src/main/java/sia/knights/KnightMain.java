package sia.knights;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.
                   ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;

public class KnightMain {

  public static void main(String[] args) throws Exception {
    ClassPathXmlApplicationContext context = 
        new ClassPathXmlApplicationContext(
            "META-INF/spring/knight.xml");

    Knight knight1 = (Knight) context.getBean("knight1");
    knight1.embarkOnQuest();

    Knight knight2 = (Knight) context.getBean("knight2");
    knight2.embarkOnQuest();

    Knight k1 = (Knight) context.getBean("k1");
    k1.embarkOnQuest();
    context.close();

    // test();
//    AnnotationConfigApplicationContext annoContext =
//            new AnnotationConfigApplicationContext();
//    annoContext.scan("sia.knights.config");
//    annoContext.refresh();
//    Knight knight1 = annoContext.getBean(Knight.class);
//    knight1.embarkOnQuest();
//    annoContext.close();
  }

  static void test() {
    ClassPathResource classPathResource = new ClassPathResource("META-INF/spring/simple.xml");

    DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
    reader.loadBeanDefinitions(classPathResource);

    System.out.println(Arrays.toString(factory.getBeanDefinitionNames()));

  }

  static void testRegisterAlias() {
  }



}
