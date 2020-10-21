package com.benefitj.kafkaproducer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/kafka/")
public class MessageController {

  @Autowired
  private KafkaTemplate kafkaTemplate;

  private final AtomicLong index = new AtomicLong(0);

  @PostMapping("/send")
  public ResponseEntity send(int count, String msg) {
    log.info(">>>>> msg = {}, count: {}", msg, count);
    for (int i = 0; i < count; i++) {
      kafkaTemplate.send("test", msg);
    }
    return ResponseEntity.ok("发送消息成功, msg: " + msg);
  }


}
