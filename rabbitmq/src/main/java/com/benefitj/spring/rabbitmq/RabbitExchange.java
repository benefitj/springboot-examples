package com.benefitj.spring.rabbitmq;

import java.lang.annotation.*;

/**
 * RabbitMQ 交换机
 *
 * @author DINGXIUAN
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Documented
public @interface RabbitExchange {
}
