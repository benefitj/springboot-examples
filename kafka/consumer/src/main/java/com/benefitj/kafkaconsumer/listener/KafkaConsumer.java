package com.benefitj.kafkaconsumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

  @KafkaListener(topics = {"test"}, id = "1")
  public void listen(ConsumerRecord record) {
    String json = record.value().toString();
    log.info("1. kafka consumer message, key:{}, msg: {}, partition: {}, offset: {} ", record.key(), json, record.partition(), record.offset());
  }

  @KafkaListener(topics = {"test"}, id = "2")
  public void listen2(ConsumerRecord record) {
    String json = record.value().toString();
    log.info("2. kafka consumer message, key:{}, msg: {}, partition: {}, offset: {} ", record.key(), json, record.partition(), record.offset());
  }
}
