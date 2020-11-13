package com.benefitj.rabbitmqpublisher;

import com.benefitj.core.DateFmtter;
import com.benefitj.core.EventLoop;
import com.benefitj.spring.applicationevent.EnableAutoApplicationListener;
import com.benefitj.spring.applicationevent.IApplicationReadyEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * RabbitMQ消息发布
 */
@EnableAutoApplicationListener
@SpringBootApplication
public class RabbitMQPublisherApplication {
  public static void main(String[] args) {
    SpringApplication.run(RabbitMQPublisherApplication.class, args);
  }

  private static final EventLoop single = EventLoop.newSingle(false);

  @Slf4j
  @Component
  public static class OnStarter implements IApplicationReadyEventListener {

    @Autowired
    private RabbitTemplate template;

    @Override
    public void onApplicationReadyEvent(ApplicationReadyEvent applicationReadyEvent) {
      single.scheduleAtFixedRate(() -> {
        String msg = DateFmtter.fmtNow();
        log.info("发送消息: {}", msg);
        template.convertAndSend("test", msg);
      }, 1, 5, TimeUnit.SECONDS);
    }
  }

}
