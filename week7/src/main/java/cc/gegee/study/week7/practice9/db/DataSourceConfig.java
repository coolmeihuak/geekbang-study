package cc.gegee.study.week7.practice9.db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 多数据源配置
 */
@Configuration
public class DataSourceConfig {

    /**
     * master
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "app.datasource.master")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * slave0
     */
    @Bean("slave0DataSourceProperties")
    @ConfigurationProperties(prefix = "app.datasource.slave0")
    public DataSourceProperties slave0DataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * slave1
     */
    @Bean("slave1DataSourceProperties")
    @ConfigurationProperties(prefix = "app.datasource.slave1")
    public DataSourceProperties slave1DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "app.datasource.master.configuration")
    public DataSource masterDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean("slave0DataSource")
    @ConfigurationProperties(prefix = "app.datasource.slave0.configuration")
    public DataSource slave0DataSource(@Qualifier("slave0DataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean("slave1DataSource")
    @ConfigurationProperties(prefix = "app.datasource.slave1.configuration")
    public DataSource slave1DataSource(@Qualifier("slave1DataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
}
