package com.benefitj.vertx.pingpong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;

/**
 * 响应端
 */
public class Pong extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(), res -> {
      if (res.succeeded()) {
        Vertx vertx = res.result();
        vertx.deployVerticle(new Pong());
      } else {
        res.cause().printStackTrace();
      }
    });
  }

  @Override
  public void start() throws Exception {
    EventBus eb = vertx.eventBus();

    eb.consumer("ping-address", message -> {

      System.out.println("Received message: " + message.body())
      ;
      // Now send back reply
      message.reply("pong!");
    });

    System.out.println("Receiver ready!");
  }

}
