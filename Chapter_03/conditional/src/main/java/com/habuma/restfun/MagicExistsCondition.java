package com.habuma.restfun;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MagicExistsCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context,
                         AnnotatedTypeMetadata metadata) {
    System.out.println("name: " + Qualifier.class.getName());
    System.out.println("canonical name: " + Qualifier.class.getCanonicalName());
    System.out.println("simple name: " + Qualifier.class.getSimpleName());
    System.out.println("typye name: " + Qualifier.class.getTypeName());

    System.out.println(metadata.isAnnotated(Qualifier.class.getName()));
    System.out.println(metadata.getAllAnnotationAttributes(Qualifier.class.getName()));

    Environment env = context.getEnvironment();
    return env.containsProperty("magic");
  }
}
