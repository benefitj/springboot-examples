package com.benefitj.tdengine;

import com.benefitj.spring.applicationevent.ApplicationEventListener;
import com.benefitj.spring.applicationevent.EnableApplicationListener;
import com.benefitj.tdengine.driver.TDengineManager;
import com.benefitj.tdengine.mapper.TemperatureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

@EnableApplicationListener
@SpringBootApplication
public class TDengineApplication {
  public static void main(String[] args) {
    SpringApplication.run(TDengineApplication.class, args);
  }


  @Component
  public static class OnStarter {

    @Autowired
    private TDengineManager manager;

    @Autowired
    private TemperatureMapper mapper;

    @ApplicationEventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
      try {
        mapper.createSuperTable();
        //manager.test();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }
}
