package com.benefitj.rabbitmqpublisher;


import com.alibaba.fastjson.JSON;
import com.benefitj.core.ReflectUtils;
import com.benefitj.spring.applicationevent.EnableAutoApplicationListener;
import com.benefitj.spring.applicationevent.IApplicationReadyEventListener;
import com.benefitj.spring.rabbitmq.RabbitQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * RabbitMQ消息订阅
 */
@EnableAutoApplicationListener
@SpringBootApplication
public class RabbitMQSubscriberApplication {
  public static void main(String[] args) {
    SpringApplication.run(RabbitMQSubscriberApplication.class, args);
  }

  @Slf4j
  @Configuration
  public static class Config {

    @Bean
    public Queue testQueue() {
      // x-message-ttl
      Map<String, Object> args = new HashMap<>(1);
      args.put("x-message-ttl", 1800000);
      return new Queue("test", false, false, true, args);
    }

    @RabbitQueue(name = "test")
    @RabbitListener(queues = "test")
    @RabbitHandler
    public void process(@Headers Map<String, Object> headers, @Payload String payload) {
      log.info("1.  headers: {}, payload: {}\n", JSON.toJSONString(headers), payload);
    }
  }

  @Component
  @Slf4j
  @RabbitQueue(name = "test")
  @RabbitListener(queues = "test")
  public static class TestHandler {

    @RabbitHandler
    public void process(@Headers Map<String, Object> headers, @Payload String payload) {
      log.info("2.  headers: {}, payload: {}\n", JSON.toJSONString(headers), payload);
    }

  }


  @Slf4j
  @Component
  public static class OnTestStarter implements IApplicationReadyEventListener {

    @Override
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
      ConfigurableApplicationContext ctx = event.getApplicationContext();
      long start = System.currentTimeMillis();
      List<RabbitQueue> queues = Arrays.stream(ctx.getBeanDefinitionNames())
          .map(ctx::getBean)
          .map(Object::getClass)
          .flatMap(beanType -> {
            if (ReflectUtils.isAnnotationPresent(beanType, RabbitListener.class, RabbitQueue.class)) {
              return Stream.of(beanType.getAnnotation(RabbitQueue.class));
            } else {
              return ReflectUtils.foreachMethods(beanType
                  , m -> ReflectUtils.isAnnotationPresent(m, RabbitListener.class, RabbitQueue.class)
                  , m -> m.getAnnotation(RabbitQueue.class))
                  .stream();
            }
          })
          .filter(Objects::nonNull)
          .collect(Collectors.toList());

      log.info("233... {}, 耗时: {}", queues.size(), System.currentTimeMillis() - start);

      System.err.println("\n-------------------------------------------------\n");
      System.err.println(String.join("\n", ctx.getBeanDefinitionNames()));
      System.err.println("\n-------------------------------------------------\n");
    }
  }


}
