package cc.page.study.week5.practice10.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class HikariConnection {

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("jdbcUrl", "jdbc:mysql://localhost:3308/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true");
        props.setProperty("driverClassName", "com.mysql.jdbc.Driver");
        props.setProperty("dataSource.user", "root");
        props.setProperty("dataSource.password", "123456");
        props.setProperty("dataSource.databaseName", "test");
        props.setProperty("dataSource.serverName", "localhost");
        props.setProperty("dataSource.maximumPoolSize", "10");
        HikariConfig config = new HikariConfig(props);
        HikariDataSource sHikariDataSource = new HikariDataSource(config);
        return sHikariDataSource.getConnection();
    }
}
