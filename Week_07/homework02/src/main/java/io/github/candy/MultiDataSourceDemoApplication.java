package io.github.candy;


import io.github.candy.proxyDataSource.DataSourceConstant;
import io.github.candy.proxyDataSource.DynamicRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by candy on 2020/12/3.
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)  //使用CG-Lib的可以基于类
@Slf4j
public class MultiDataSourceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiDataSourceDemoApplication.class, args);
    }

    @Bean(name = "dataSource")
    public AbstractRoutingDataSource datasource() {
        DynamicRoutingDataSource customRoutingDataSource = new DynamicRoutingDataSource();

        //创建map存放你配置的数据源 key值为determineCurrentLookupKey() 方法可能返回的东西
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>(2);
        targetDataSources.put(DataSourceConstant.MASTER,fooDataSource());
        targetDataSources.put(DataSourceConstant.SLAVE1,barDataSource());
        targetDataSources.put(DataSourceConstant.SLAVE2,bar1DataSource());
        //创建我们实现AbstractRoutingDataSource的数据源

        //设置目标数据源
        customRoutingDataSource.setTargetDataSources(targetDataSources);
        //设置默认数据源
        customRoutingDataSource.setDefaultTargetDataSource(fooDataSource());

        customRoutingDataSource.afterPropertiesSet();
        return  customRoutingDataSource;
    }

    @Bean
    @ConfigurationProperties("foo.datasource")
    public DataSourceProperties fooDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource fooDataSource() {
        DataSourceProperties dataSourceProperties = fooDataSourceProperties();
        log.info("foo datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Resource
    public PlatformTransactionManager fooTxManager(DataSource fooDataSource) {
        return new DataSourceTransactionManager(fooDataSource);
    }

    @Bean
    @ConfigurationProperties("bar.datasource")
    public DataSourceProperties barDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource barDataSource() {
        DataSourceProperties dataSourceProperties = barDataSourceProperties();
        log.info("bar datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @ConfigurationProperties("bar1.datasource")
    public DataSourceProperties bar1DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource bar1DataSource() {
        DataSourceProperties dataSourceProperties = bar1DataSourceProperties();
        log.info("bar1 datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Resource
    public PlatformTransactionManager barTxManager(DataSource barDataSource) {
        return new DataSourceTransactionManager(barDataSource);
    }
}