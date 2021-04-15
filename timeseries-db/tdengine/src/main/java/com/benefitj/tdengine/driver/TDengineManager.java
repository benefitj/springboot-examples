package com.benefitj.tdengine.driver;

import com.taosdata.jdbc.TSDBDriver;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Component
public class TDengineManager {

  //Class.forName("com.taosdata.jdbc.TSDBDriver");
  //String jdbcUrl = "jdbc:TAOS://taosdemo.com:6030/test?user=root&password=taosdata";
  //Connection conn = DriverManager.getConnection(jdbcUrl);

  private volatile Connection connection;

  public Connection getConnection() throws IllegalStateException {
    Connection conn = this.connection;
    if (conn == null) {
      synchronized (this) {
        if ((conn = this.connection) == null) {
          try {
            Class.forName("com.taosdata.jdbc.TSDBDriver");
            //String jdbcUrl = "jdbc:TAOS://127.0.0.1:6030/test?user=root&password=taosdata";
            String jdbcUrl = "jdbc:TAOS://127.0.0.1:6030?user=root&password=taosdata";
            Properties connProps = new Properties();
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
            this.connection = (conn = DriverManager.getConnection(jdbcUrl, connProps));
          } catch (ClassNotFoundException | SQLException e) {
            throw new IllegalStateException(e);
          }
        }
      }
    }
    return conn;
  }

  public void test() {
    Connection conn = getConnection();
    try (Statement stmt = conn.createStatement();) {
      // create database
      stmt.execute("CREATE DATABASE IF NOT EXISTS hsrg;");
      // use database
      stmt.execute("USE hsrg;");
      // create table
      boolean i = stmt.execute("CREATE TABLE IF NOT EXISTS hs_all_rates (time timestamp, heart_rate int, humidity float)");
      System.err.println(" =============> " + i);

    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

}
