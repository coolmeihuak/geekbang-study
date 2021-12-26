package cc.gegee.study.week7.practice2;

import cc.gegee.study.week7.practice2.common.BatchUtils;
import cc.gegee.study.week7.practice2.common.JdbcAdder;
import cc.gegee.study.week7.practice2.connection.Jdbc;
import cc.gegee.study.week7.practice2.dao.UserDao;
import lombok.val;

import java.sql.SQLException;

/**
 * 单线程单连接 + Statement
 */
public class Thread1Statement {

    /**
     * 19 秒
     */
    public static void main(String[] args) throws SQLException {
        JdbcAdder.add(BatchUtils::addStatement, 1000000);
    }
}
