package com.benefitj.jdbc;

import com.alibaba.fastjson.JSON;
import com.benefitj.core.DateFmtter;
import com.benefitj.core.ShutdownHook;
import com.benefitj.jdbc.sql.SqlMetaData;
import com.benefitj.jdbc.sql.SqlUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.*;

@SpringBootApplication
public class JdbcApplication {
  public static void main(String[] args) throws Exception {
//		SpringApplication.run(JdbcApplication.class, args);
    test();
  }


//	@Component
//	public static class OnAppStarter implements IApplicationReadyEventListener {
//		@Override
//		public void onApplicationReadyEvent(ApplicationReadyEvent applicationReadyEvent) {
//		}
//	}

  public static void test() throws Exception {
    final Connection c = getConnection();
    c.setAutoCommit(false);

    System.err.println("schema: " + c.getSchema()
        + ", catalog: " + c.getCatalog()
        + ", autoCommit: " + c.getAutoCommit()
        + ", holdability: " + c.getHoldability()
        + ", networkTimeout: " + c.getNetworkTimeout()
        + ", transactionIsolation: " + c.getTransactionIsolation()
        + ", typeMap: " + c.getTypeMap()
        + ", clientInfo: " + c.getClientInfo()
    );
    System.err.println();

    try {

      Statement s = c.createStatement();

      String sql = "SHOW DATABASES";
//      String sql = "SHOW TABLES";
//      String sql = "SELECT * FROM HS_PERSON";
//      String sql = "SHOW TABLE STATUS";

      ResultSet set = s.executeQuery(sql);
      ResultSetMetaData metaData = set.getMetaData();
      List<SqlMetaData> metaDataList = SqlUtils.getMataDatas(metaData);

      System.err.println(JSON.toJSONString(metaDataList));
      System.err.println("\n");
      System.err.println("\n");

      List<Map<String, Object>> values = new ArrayList<>();
      while (set.next()) {
        Map<String, Object> value = new LinkedHashMap<>();
        for (SqlMetaData smd : metaDataList) {
          String columnLabel = smd.getColumnLabel();
          switch (smd.getColumnType()) {
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.LONGNVARCHAR:
              value.put(columnLabel, set.getString(columnLabel));
              break;
            case Types.INTEGER:
              value.put(columnLabel, set.getInt(columnLabel));
              break;
            case Types.FLOAT:
              value.put(columnLabel, set.getFloat(columnLabel));
              break;
            case Types.DOUBLE:
              value.put(columnLabel, set.getDouble(columnLabel));
              break;
            case Types.BOOLEAN:
              value.put(columnLabel, set.getBoolean(columnLabel));
              break;
            case Types.BIGINT:
              value.put(columnLabel, set.getLong(columnLabel));
              break;
            case Types.ARRAY:
              value.put(columnLabel, set.getArray(columnLabel));
              break;
            case Types.DATE:
              value.put(columnLabel, fmt(set.getDate(columnLabel)));
              break;
            case Types.TIME:
              value.put(columnLabel, fmt(set.getTime(columnLabel)));
              break;
            case Types.TIMESTAMP:
              value.put(columnLabel, fmt(set.getTimestamp(columnLabel)));
              break;
            default:
              value.put(columnLabel, "");
              break;
          }
        }
        values.add(value);
        System.err.println(JSON.toJSONString(value));
      }

      System.err.println();
      System.err.println(JSON.toJSONString(values));
      System.err.println();

    } finally {
      c.close();
    }
  }

  private static String fmt(Object time) {
    return time != null ? DateFmtter.fmt(time) : "";
  }



//  private static final String URL = "jdbc:mysql://192.168.1.198:53306";
  private static final String URL = "jdbc:mysql://127.0.0.1:6030";
//  private static final String URL = "jdbc:mysql://192.168.1.198:53306/mysql";
//  private static final String URL = "jdbc:mysql://127.0.0.1:53306/mysql";
  private static final String USER = "root";
  private static final String PASSWORD = "hsrg8888";
  private static final String DRIVER = "com.mysql.cj.jdbc.Driver";


  private static final Set<String> SCHEMA_DATABASES;

  static {
    Set<String> databases = new HashSet<>(3);
    databases.add("information_schema");
    databases.add("performance_schema");
    databases.add("mysql");
    SCHEMA_DATABASES = Collections.unmodifiableSet(databases);
  }

  private static final class Holder {

    private static final Connection INSTANCE;

    static {
      try {
        Class.forName(DRIVER);
        INSTANCE = DriverManager.getConnection(URL, USER, PASSWORD);
        registerShutdown(INSTANCE);
      } catch (SQLException | ClassNotFoundException e) {
        throw new IllegalStateException(e);
      }
    }

    private static void registerShutdown(final Connection connection) {
      ShutdownHook.register(() -> {
        try {
          connection.close();
        } catch (SQLException ignore) { /* ~ */ }
      });
    }
  }

  public static Connection getConnection() {
    return Holder.INSTANCE;
  }



}
