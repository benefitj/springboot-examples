package com.benefitj.mqttpublisher;

import com.benefitj.core.DateFmtter;
import com.benefitj.core.EventLoop;
import com.benefitj.mqtt.MqttOptionsProperty;
import com.benefitj.mqtt.MqttPublisher;
import com.benefitj.mqtt.spring.EnableMqttConfiguration;
import com.benefitj.spring.applicationevent.EnableAutoApplicationListener;
import com.benefitj.spring.applicationevent.IApplicationReadyEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * MQTT消息发布
 */
@EnableAutoApplicationListener
@EnableMqttConfiguration
@SpringBootApplication
public class MqttPublisherApplication {
  public static void main(String[] args) {
    SpringApplication.run(MqttPublisherApplication.class, args);
  }


  /**
   * 消息发布
   */
  @Slf4j
  @Component
  public static class PublisherTimer implements IApplicationReadyEventListener {

    private final EventLoop single = EventLoop.newSingle(false);

    @Autowired
    private MqttPublisher publisher;
    @Autowired
    private MqttOptionsProperty property;

    @Override
    public void onApplicationReadyEvent(ApplicationReadyEvent applicationReadyEvent) {
      single.scheduleAtFixedRate(this::sendMqttMessage, 1, 5, TimeUnit.SECONDS);
    }

    private void sendMqttMessage() {
      String publishTopics = property.getPublishTopics();
      publisher.send(publishTopics + "010003b8", DateFmtter.fmtNowS());
      log.info("发布消息...");
    }
  }
}
