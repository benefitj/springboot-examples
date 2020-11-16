package com.benefitj.rabbitmqpublisher;


import com.alibaba.fastjson.JSON;
import com.benefitj.core.DateFmtter;
import com.benefitj.core.EventLoop;
import com.benefitj.rabbitmqpublisher.config.AmqpConfig;
import com.benefitj.spring.applicationevent.EnableAutoApplicationListener;
import com.benefitj.spring.rabbitmq.RabbitQueueConfigurationSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RabbitMQ消息订阅
 */
@Import(RabbitQueueConfigurationSelector.class)
@EnableAutoApplicationListener
@SpringBootApplication
public class RabbitMQSubscriberApplication {
  public static void main(String[] args) {
    SpringApplication.run(RabbitMQSubscriberApplication.class, args);
  }

  private static final EventLoop single = EventLoop.newSingle(false);

  static {
    single.schedule(() ->
            System.err.println("now: " + DateFmtter.fmtNow())
        , 1
        , TimeUnit.SECONDS);
  }

  @Component
  @Slf4j
  public static class TestHandler2 {

  @RabbitListener(
      bindings = {
          @QueueBinding(
              value = @Queue(name = AmqpConfig.QUEUE, exclusive = "false", durable = "false", autoDelete = "true"),
              exchange = @Exchange(name = AmqpConfig.EXCHANGE, type = ExchangeTypes.DIRECT),
              key = AmqpConfig.ROUTING_KEY
          )
      }
  )
    @RabbitHandler
    public void process(@Headers Map<String, Object> headers, @Payload String payload) {
      log.info("1.  headers: {}, payload: {}", JSON.toJSONString(headers), payload);
    }

  }

}
