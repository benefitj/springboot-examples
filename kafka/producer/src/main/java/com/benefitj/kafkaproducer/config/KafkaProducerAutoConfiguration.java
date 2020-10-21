package com.benefitj.kafkaproducer.config;

import com.google.common.collect.Maps;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaProducerProperties.class)
@ConditionalOnClass(value = org.apache.kafka.clients.consumer.KafkaConsumer.class)
public class KafkaProducerAutoConfiguration {

  @Bean
  public ProducerFactory<String, String> producerFactory(KafkaProducerProperties properties) {
    Map<String, Object> configs = Maps.newHashMap();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBrokerAddress());
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configs.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);
    configs.put(ProducerConfig.ACKS_CONFIG, "all");
    configs.put(ProducerConfig.RETRIES_CONFIG, 1);
    configs.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
    configs.put(ProducerConfig.LINGER_MS_CONFIG, 1);
    configs.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
    return new DefaultKafkaProducerFactory<>(configs);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
    return new KafkaTemplate<String, String>(producerFactory);
  }
}
