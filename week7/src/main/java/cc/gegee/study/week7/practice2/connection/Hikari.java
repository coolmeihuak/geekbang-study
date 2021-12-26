package cc.gegee.study.week7.practice2.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Properties;

public class Hikari {

    public static HikariDataSource getDataSource() {
        Properties props = new Properties();
        props.setProperty("jdbcUrl", "jdbc:mysql://localhost:3308/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true");
        props.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");
        props.setProperty("dataSource.user", "root");
        props.setProperty("dataSource.password", "123456");
        props.setProperty("dataSource.databaseName", "test");
        HikariConfig config = new HikariConfig(props);
        config.setMinimumIdle(15);
        config.setMaximumPoolSize(20);
        return new HikariDataSource(config);
    }
}
