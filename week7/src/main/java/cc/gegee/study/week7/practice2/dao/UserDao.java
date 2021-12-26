package cc.gegee.study.week7.practice2.dao;

import cc.gegee.study.week7.practice9.domain.User;
import lombok.val;

import java.sql.Connection;

public final class UserDao {

    public static void add(Connection connection, User user) {
        val sql = "insert into user(mobile, username) values ('"
                + user.getMobile() + "', '"
                + user.getUsername() + "')";
        Dao.execute(connection, sql);
    }

    public static void add(Connection connection) {
        add(connection, User.builder().mobile("13916934749").username("page").build());
    }
}
