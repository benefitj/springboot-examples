package com.benefitj.tdengine.mapper;

import com.benefitj.tdengine.influx.Temperature;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TemperatureMapper extends TdengineMapper<Temperature> {

  @Update("CREATE TABLE if not exists hs_temperature(time timestamp, point float) tags(person_zid nchar(32), device_id nchar(32), tbIndex int)")
  int createSuperTable();

  @Update("CREATE TABLE #{tbName} USING hs_temperature TAGS( #{person_zid}, #{device_id}, #{tbindex})")
  int createTable(@Param("tbName") String tbName, @Param("person_zid") String personZid, @Param("device_id") String deviceId, @Param("tbindex") int tbindex);

  @Update("DROP TABLE IF EXISTS hs_temperature")
  void dropSuperTable();

  @Insert("INSERT INTO t${tbIndex}(time, point) VALUES(#{time}, #{point})")
  int insertOne(Temperature record);

}
