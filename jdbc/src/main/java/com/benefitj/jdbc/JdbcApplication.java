package com.benefitj.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

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

		try {
			Statement s = c.createStatement();

			String sql = "SHOW DATABASES";
//      String sql = "SHOW TABLES ON hsrg";
//      String sql = "SELECT * FROM sys_rbac.sys_user";

			ResultSet set = s.executeQuery(sql);

			ResultSetMetaData metaData = set.getMetaData();


			int columnCount = metaData.getColumnCount();
			System.err.println("columnCount: " + columnCount);

			for (int i = 1; i <= columnCount; i++) {
				System.err.println("ColumnLabel: " + metaData.getColumnLabel(i) // 列名
						+ ", ColumnTypeName: " + metaData.getColumnTypeName(i) // 列类型名称(MYSQL)
						+ ", ColumnType: " + metaData.getColumnType(i) // 列类型(JAVA)
						+ ", CatalogName: " + metaData.getCatalogName(i) //目录名称
						+ ", ColumnClassName: " + metaData.getColumnClassName(i) // Class
						+ ", TableName: " + metaData.getTableName(i) // 表名
						+ ", SchemaName: " + metaData.getSchemaName(i) // 协议名
						+ ", ColumnDisplaySize: " + metaData.getColumnDisplaySize(i) // 列显示大小
						+ ", Scale: " + metaData.getScale(i) // 数值范围
						+ ", Precision: " + metaData.getPrecision(i) // 精度(位数)
				);
			}

      while (set.next()) {
        System.err.println("Database: " + set.getString("Database"));
      }
		} finally {
			c.close();
		}
	}

	private static final String URL = "jdbc:mysql://192.168.1.198:53306";
	private static final String USER = "root";
	private static final String PASSWORD = "hsrg8888";
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";


	private static final class Holder {

		private static final Connection INSTANCE;

		static {
			try {
				Class.forName(DRIVER);
				INSTANCE = DriverManager.getConnection(URL, USER, PASSWORD);
			} catch (SQLException | ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	public static Connection getConnection() {
		return Holder.INSTANCE;
	}
}
