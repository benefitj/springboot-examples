package com.benefitj.spring.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@EnableRabbit
@ConditionalOnMissingBean
@Configuration
public class RabbitQueueConfigurationSelector implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                      BeanDefinitionRegistry registry,
                                      BeanNameGenerator importBeanNameGenerator) {
    System.err.println("\n-------------------------");
    System.err.print("\nregistry.getBeanDefinitionNames: ");
    System.err.println(String.join("\n", registry.getBeanDefinitionNames()));
    System.err.println("\n-------------------------\n");

//    Map<String, Object> beans = ((DefaultListableBeanFactory) registry).getBeansWithAnnotation(RabbitListener.class);
//    beans.values()
//        .stream()
//        .map(Object::getClass)
//        .map(beanType -> beanType.getAnnotation(RabbitListener.class))
//        .forEach(queue -> registerQueue(queue, registry));
  }

  protected void registerQueue(RabbitListener listener, BeanDefinitionRegistry registry) {

    String[] queues = listener.queues();
    org.springframework.amqp.rabbit.annotation.Queue[] queuesAnnotations = listener.queuesToDeclare();
    if (queuesAnnotations.length == 0) {
      for (String queue : queues) {
        register(registry, queue, false, false, false, Collections.emptyMap());
      }
    } else {
      for (org.springframework.amqp.rabbit.annotation.Queue queue : queuesAnnotations) {
        Map<String, Object> arguments = new HashMap<>(queue.arguments().length);
        for (Argument argument : queue.arguments()) {
          try {
            Class<?> type = Class.forName(argument.type());
            if (type.isAssignableFrom(Number.class)) {
              arguments.put(argument.name(), Double.parseDouble(argument.value()));
            } else if (type.isAssignableFrom(Boolean.class)) {
              arguments.put(argument.name(), Boolean.parseBoolean(argument.value()));
            } else {
              arguments.put(argument.name(), argument.value());
            }
          } catch (Exception ignore) {}
        }
        register(registry
            , queue.name()
            , Boolean.parseBoolean(queue.durable())
            , Boolean.parseBoolean(queue.exclusive())
            , Boolean.parseBoolean(queue.autoDelete())
            , arguments
            );
      }
    }

//    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Queue.class);
//    String name = listener.name();
//    beanDefinitionBuilder.addConstructorArgValue(name);
//    beanDefinitionBuilder.addConstructorArgValue(listener.durable());
//    beanDefinitionBuilder.addConstructorArgValue(listener.exclusive());
//    beanDefinitionBuilder.addConstructorArgValue(listener.autoDelete());
//    Map<String, String> args = new HashMap<>(listener.arguments().length);
//    for (String argument : listener.arguments()) {
//      String[] keyValue = argument.split(":");
//      args.put(keyValue[0].trim(), keyValue[1].trim());
//    }
//    beanDefinitionBuilder.addConstructorArgValue(args);
//    BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
//    registry.registerBeanDefinition(name.endsWith("Queue") ? name : name + "Queue", beanDefinition);

  }

  protected void register(BeanDefinitionRegistry registry, String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) {
    String beanName = name.endsWith("Queue") ? name : name + "Queue";
    DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) registry;
    if (!beanFactory.containsBean(beanName)) {
      System.err.println("注册Bean: " + beanName);
      beanFactory.registerSingleton(beanName, new Queue(name, durable, exclusive, autoDelete, arguments));
    }
  }


}
