package cc.gegee.study.week7.practice2.common;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface Adder {

    void add(Connection connection, long userId, int size) throws SQLException;
}
