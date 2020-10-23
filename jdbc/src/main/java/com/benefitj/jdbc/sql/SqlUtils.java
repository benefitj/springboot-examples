package com.benefitj.jdbc.sql;

import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlUtils {

  /**
   * 获取 MetaData 信息
   */
  public static List<SqlMetaData> getMataDatas(ResultSetMetaData metaData) throws SQLException {
    int columnCount = metaData.getColumnCount();
    List<SqlMetaData> metaDataList = new ArrayList<>(metaData.getColumnCount());
    for (int i = 1; i <= columnCount; i++) {
      SqlMetaData smd = new SqlMetaData();
      smd.setAutoIncrement(metaData.isAutoIncrement(i));
      smd.setCaseSensitive(metaData.isCaseSensitive(i));
      smd.setCatalogName(metaData.getCatalogName(i));
      smd.setColumnClassName(metaData.getColumnClassName(i));
      smd.setColumnDisplaySize(metaData.getColumnDisplaySize(i));
      smd.setColumnLabel(metaData.getColumnLabel(i));
      smd.setColumnName(metaData.getColumnName(i));
      smd.setColumnType(metaData.getColumnType(i));
      smd.setColumnTypeName(metaData.getColumnTypeName(i));
      smd.setCurrency(metaData.isCurrency(i));
      smd.setDefinitelyWritable(metaData.isDefinitelyWritable(i));
      smd.setNullable(metaData.isNullable(i));
      smd.setPrecision(metaData.getPrecision(i));
      smd.setReadOnly(metaData.isReadOnly(i));
      smd.setScale(metaData.getScale(i));
      smd.setSchemaName(metaData.getSchemaName(i));
      smd.setSearchable(metaData.isSearchable(i));
      smd.setSigned(metaData.isSigned(i));
      smd.setTableName(metaData.getTableName(i));
      smd.setWritable(metaData.isWritable(i));
      metaDataList.add(smd);
    }
    return metaDataList;
  }

  /**
   * 判断是否为 Getter 方法
   *
   * @param name 方法名称
   * @return 返回是否为Getter方法
   */
  public static boolean isGetter(String name) {
    return name.startsWith("get") || name.startsWith("is");
  }

  /**
   * 获取字段名
   *
   * @param name 方法名
   * @return 返回字段名
   */
  public static String getFieldName(String name) {
    if (name.startsWith("get")) {
      return Character.toLowerCase(name.charAt(3)) + name.substring(4);
    } else if (name.startsWith("is")) {
      return Character.toLowerCase(name.charAt(2)) + name.substring(3);
    }
    return name;
  }

  /**
   * 判断参数类型
   *
   * @param method Method对象
   * @param types  类型数组
   * @return 返回是否匹配
   */
  public static boolean isParameterType(Method method, Class<?>... types) {
    if (types != null && types.length == method.getParameterCount()) {
      for (int i = 0; i < types.length; i++) {
        //if (!types[i].isAssignableFrom(method.getParameterTypes()[i])) {
        if (types[i] != method.getParameterTypes()[i]) {
          return false;
        }
      }
      return true;
    }
    return types != null && types.length == 0;
  }

}
