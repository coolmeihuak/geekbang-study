package cc.gegee.study.week7.practice2.common;

import cc.gegee.study.week7.practice2.connection.Jdbc;
import cc.gegee.study.week7.practice2.dao.UserDao;
import lombok.val;

import java.sql.SQLException;

public class JdbcAdder {

    public static void add(Adder adder, int size) throws SQLException {
        val connection = Jdbc.getConnection();
        val userId = 1L;
        UserDao.add(connection);
        adder.add(connection, userId, size);
        connection.close();
    }
}
