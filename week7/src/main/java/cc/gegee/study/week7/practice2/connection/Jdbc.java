package cc.gegee.study.week7.practice2.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Jdbc {

    private static final String URL = "jdbc:mysql://localhost:3308/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        //1.加载驱动程序
        try {
            // com.mysql.cj.jdbc.Driver
//            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //2. 获得数据库连接
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
