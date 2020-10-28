package com.benefitj.mqttsubscriber;

import com.benefitj.mqtt.MqttMessageSubscriber;
import com.benefitj.mqtt.spring.EnableMqttConfiguration;
import com.benefitj.spring.applicationevent.EnableAutoApplicationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * MQTT消息订阅
 */
@EnableAutoApplicationListener
@EnableMqttConfiguration
@SpringBootApplication
public class MqttSubscriberApplication {
  public static void main(String[] args) {
    SpringApplication.run(MqttSubscriberApplication.class, args);
  }


  @Slf4j
  @Component
  public static class DefaultMqttMessageSubscriber implements MqttMessageSubscriber {

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
      log.info("{}, payload: {}"
          , message.getHeaders()
          , new String((byte[]) message.getPayload())
      );
    }
  }
}
