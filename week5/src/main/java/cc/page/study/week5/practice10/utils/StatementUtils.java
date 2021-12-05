package cc.page.study.week5.practice10.utils;

import cc.page.study.week5.practice10.ParamSetter;
import cc.page.study.week5.practice10.connection.HikariConnection;
import cc.page.study.week5.practice10.connection.JdbcConnection;

import java.sql.*;
import java.util.Objects;

public class StatementUtils {

    private static Connection conn = null;

    static {
        try {
            // 获得数据库连接
            conn = JdbcConnection.getConnection();
//            conn = HikariConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Statement getStatement(String sql) throws SQLException {
        return conn.createStatement();
    }

    public static void execute(String sql, boolean transactions) throws SQLException {
        Statement statement = conn.createStatement();
        if (transactions) {
            conn.setAutoCommit(false);
        }
        statement.execute(sql);
        if (transactions) {
            conn.commit();
            conn.setAutoCommit(true);
        }
        close(null, statement);
    }

    public static void executeQuery(String sql, ParamSetter<ResultSet> consumerResultSet, boolean transactions) throws SQLException {
        Statement statement = conn.createStatement();
        if (transactions) {
            conn.setAutoCommit(false);
        }
        ResultSet resultSet = statement.executeQuery(sql);
        if (transactions) {
            conn.commit();
            conn.setAutoCommit(true);
        }
        if (Objects.nonNull(consumerResultSet)) {
            consumerResultSet.set(resultSet);
        }
        close(resultSet, statement);
    }

    public static PreparedStatement getPrepareStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    public static void execute(String sql, ParamSetter<PreparedStatement> consumer, boolean transactions) throws SQLException {
        PreparedStatement ps = getPrepareStatement(sql);
        if (Objects.nonNull(consumer)) {
            consumer.set(ps);
        }
        if (transactions) {
            conn.setAutoCommit(false);
        }
        ps.execute();
        if (transactions) {
            conn.commit();
            conn.setAutoCommit(true);
        }
        close(null, ps);
    }

    public static void executeQuery(String sql, ParamSetter<PreparedStatement> consumer, ParamSetter<ResultSet> consumerResultSet, boolean transactions) throws SQLException {
        PreparedStatement ps = getPrepareStatement(sql);
        if (Objects.nonNull(consumer)) {
            consumer.set(ps);
        }
        if (transactions) {
            conn.setAutoCommit(false);
        }
        ResultSet resultSet = ps.executeQuery();
        if (transactions) {
            conn.commit();
            conn.setAutoCommit(true);
        }
        if (Objects.nonNull(consumerResultSet)) {
            consumerResultSet.set(resultSet);
        }
        close(resultSet, ps);
    }

    public static void close(AutoCloseable ac1, AutoCloseable ac2) throws SQLException {
        if (ac1 != null) {
            try {
                ac1.close();
            } catch (Exception e1) {
                if (ac2 != null) {
                    try {
                        ac2.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }
}
