package cc.gegee.study.week7.practice2.common;

import lombok.val;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class BatchUtils {

    public static void addPreparedStatement(Connection connection, long userId, int size) throws SQLException {
        String sql = "insert into `order`(user_id, status, money) values (?, ?, ?)";
        connection.setAutoCommit(false);
        val preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < size; i++) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setInt(2, 0);
            preparedStatement.setBigDecimal(3, BigDecimal.valueOf(10000.58d));
            preparedStatement.addBatch();
        }
        val statement = connection.createStatement();
        statement.execute("set foreign_key_checks = 0");
        statement.execute("set unique_checks = 0");
        System.out.println("@@@@@@@ 开始插入");
        Long startTime = System.currentTimeMillis();
        preparedStatement.executeBatch();
        preparedStatement.close();
        connection.commit();
        Long endTime = System.currentTimeMillis();
        System.out.println("@@@@@@@ 插入完毕,用时：" + (endTime - startTime) / 1000);
        statement.execute("set foreign_key_checks = 1");
        statement.execute("set unique_checks = 1");
        statement.close();
    }

    public static void addStatement(Connection connection, long userId, int size) throws SQLException {
        val sqlOrder = new StringBuilder("insert into `order`(user_id, status, money) values");
        connection.setAutoCommit(false);
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sqlOrder.append(",");
            }
            sqlOrder.append("(")
                    .append(userId)
                    .append(", ")
                    .append(0)
                    .append(", ")
                    .append(BigDecimal.valueOf(10000.58d))
                    .append(")")
            ;
        }
        val statement = connection.createStatement();
        statement.execute("set foreign_key_checks = 0");
        statement.execute("set unique_checks = 0");
        System.out.println("@@@@@@@ 开始插入");
        Long startTime = System.currentTimeMillis();
        statement.execute(sqlOrder.toString());
        statement.execute("set foreign_key_checks = 1");
        statement.execute("set unique_checks = 1");
        statement.close();
        connection.commit();
        Long endTime = System.currentTimeMillis();
        System.out.println("@@@@@@@ 插入完毕,用时：" + (endTime - startTime) / 1000);
    }
}
