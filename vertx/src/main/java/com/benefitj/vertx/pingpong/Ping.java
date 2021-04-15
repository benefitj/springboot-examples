package com.benefitj.vertx.pingpong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;

public class Ping extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(), event -> {
      if (event.succeeded()) {
        event.result().deployVerticle(new Ping());
      } else {
        event.cause().printStackTrace();
      }
    });
  }

  @Override
  public void start() throws Exception {
    EventBus eb = vertx.eventBus();

    // Send a message every second

    vertx.setPeriodic(1000, v -> {

      eb.request("ping-address", "ping!", reply -> {
        if (reply.succeeded()) {
          System.out.println("Received reply: " + reply.result().body());
        } else {
          System.out.println("No reply");
        }
      });

    });
  }


}
