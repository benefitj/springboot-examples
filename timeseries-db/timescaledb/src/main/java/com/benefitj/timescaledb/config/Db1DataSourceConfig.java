//package com.benefitj.timescaledb.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@MapperScan(basePackages = "com.liuyanzhao.sens.mapper.db1", sqlSessionFactoryRef = "db1SqlSessionFactory")
//public class Db1DataSourceConfig {
//
//  @Autowired
//  private Db1DataSourceProperties db1DataSourceProperties;
//
//  @Bean(name = "db1DataSource")
//  public DataSource dataSource() {
//    DruidDataSource dataSource = new DruidDataSource();
//    dataSource.setDriverClassName(db1DataSourceProperties.getDriverClassName());
//    dataSource.setUrl(db1DataSourceProperties.getUrl());
//    dataSource.setUsername(db1DataSourceProperties.getUsername());
//    dataSource.setPassword(db1DataSourceProperties.getPassword());
//    dataSource.setInitialSize(db1DataSourceProperties.getInitialSize());
//    dataSource.setMinIdle(db1DataSourceProperties.getMinIdle());
//    dataSource.setMaxActive(db1DataSourceProperties.getMaxActive());
//    return dataSource;
//  }
//
//  @Bean(name = "db1SqlSessionFactory")
//  public SqlSessionFactory sqlSessionFactory(@Qualifier("db1DataSource") DataSource dataSource,
//                                             @Qualifier("camelCaseConfiguration") org.apache.ibatis.session.Configuration configuration) throws Exception {
//    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//    sqlSessionFactoryBean.setDataSource(dataSource);
//    sqlSessionFactoryBean.setConfiguration(configuration);
//    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//    Resource[] resources = resourcePatternResolver.getResources("classpath*:mapper/db1/*Mapper.xml");
//    sqlSessionFactoryBean.setMapperLocations(resources);
//    return sqlSessionFactoryBean.getObject();
//  }
//
//  @Bean(name = "db1SqlSessionTemplate")
//  public SqlSessionTemplate sqlSessionTemplate(@Qualifier("db1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//    return new SqlSessionTemplate(sqlSessionFactory);
//  }
//
//  @Bean(name = "db1TransactionManager")
//  public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("db1DataSource") DataSource dataSource) {
//    return new DataSourceTransactionManager(dataSource);
//  }
//
//  @Bean(name = "camelCaseConfiguration")
//  @ConfigurationProperties(prefix = "mybatis.configuration")
//  public org.apache.ibatis.session.Configuration configuration() {
//    return new org.apache.ibatis.session.Configuration();
//  }
//}
