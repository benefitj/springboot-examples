package com.benefitj.rabbitmqpublisher;

import com.alibaba.fastjson.JSON;
import com.benefitj.core.DateFmtter;
import com.benefitj.core.EventLoop;
import com.benefitj.rabbitmqpublisher.config.AmqpConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RabbitMQ消息发布
 */
@EnableRabbit
@SpringBootApplication
public class RabbitMQPublisherApplication {
  public static void main(String[] args) {
    SpringApplication.run(RabbitMQPublisherApplication.class, args);
  }

  @Slf4j
  @Component
  public static class OnStarter {

    @Autowired
    private RabbitTemplate template;

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
      EventLoop.io().scheduleAtFixedRate(() -> {
        String msg = DateFmtter.fmtNow();
        log.info("发送消息: {}", msg);
        template.convertSendAndReceive(AmqpConfig.EXCHANGE, AmqpConfig.ROUTING_KEY, msg);
      }, 1, 5, TimeUnit.SECONDS);
    }
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
