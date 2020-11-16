package com.benefitj.rabbitmqpublisher.config;

//@Configuration
public class AmqpConfig {

  public static final String QUEUE = "test";
  public static final String EXCHANGE = "test.exchange";
  //    public static final String ROUTING_KEY = "test.#"; // # 匹配多个，* 匹配单个
  public static final String ROUTING_KEY = "test.key";

//  /**
//   * 交换机
//   */
//  @Bean(EXCHANGE)
//  public Exchange exchange() {
//    return ExchangeBuilder.directExchange(EXCHANGE)
//        .durable(false)
//        .build();
//  }
//
//  /**
//   * 绑定
//   *
//   * @param queue    队列
//   * @param exchange 交换机
//   * @return 返回绑定
//   */
//  @Bean
//  public Binding bind(@Qualifier(QUEUE) Queue queue,
//                      @Qualifier(EXCHANGE) Exchange exchange) {
//    return BindingBuilder.bind(queue)
//        .to(exchange)
//        .with(ROUTING_KEY)
//        .noargs();
//  }
//
//  /**
//   * 队列
//   */
//  @Bean(QUEUE)
//  public Queue queue() {
//    return new Queue(QUEUE, false, false, true);
//  }

}


