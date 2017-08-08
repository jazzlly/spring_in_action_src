package sia.knights;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.
                   ClassPathXmlApplicationContext;

public class KnightMain {

  public static void main(String[] args) throws Exception {
    ClassPathXmlApplicationContext context = 
        new ClassPathXmlApplicationContext(
            "META-INF/spring/minstrel.xml");

    Knight knight = context.getBean(Knight.class);
    knight.embarkOnQuest();
    context.close();

    AnnotationConfigApplicationContext annoContext =
            new AnnotationConfigApplicationContext();
    annoContext.scan("sia.knights.config");
    annoContext.refresh();
    Knight knight1 = annoContext.getBean(Knight.class);
    knight1.embarkOnQuest();
    annoContext.close();
  }

}
