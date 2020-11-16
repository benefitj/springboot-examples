package com.benefitj.spring.rabbitmq;

import java.lang.annotation.*;

/**
 * RabbitMQ队列
 *
 * @author DINGXIUAN
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Documented
public @interface RabbitQueue {

  /**
   * 队列名称
   */
  String name();

  /**
   * 是否持久化，默认false
   */
  boolean durable() default false;

  /**
   * 排他，默认false
   */
  boolean exclusive() default false;

  /**
   * 是否自动删除，默认false
   */
  boolean autoDelete() default false;

  /**
   * 参数，以冒号(:)分割，如：超时时长:30分钟(x-message-ttl:1800000)
   */
  String[] arguments() default {};

}
