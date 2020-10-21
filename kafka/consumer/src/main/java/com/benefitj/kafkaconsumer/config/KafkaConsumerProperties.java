package com.benefitj.kafkaconsumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = KafkaConsumerProperties.KAFKA_CONSUMER_PREFIX)
public class KafkaConsumerProperties {

  public static final String KAFKA_CONSUMER_PREFIX = "kafka";

  private String brokerAddress;

  private String groupId;

  public String getBrokerAddress() {
    return brokerAddress;
  }

  public void setBrokerAddress(String brokerAddress) {
    this.brokerAddress = brokerAddress;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

}
