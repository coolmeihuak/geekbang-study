package cc.gegee.study.week7.practice2;

import cc.gegee.study.week7.practice2.common.BatchUtils;
import cc.gegee.study.week7.practice2.common.JdbcAdder;

import java.sql.SQLException;

/**
 * 单线程单连接 + 预处理sql
 */
public class Thread1PreparedStatement {

    /**
     * 22 秒
     */
    public static void main(String[] args) throws SQLException {
        JdbcAdder.add(BatchUtils::addPreparedStatement, 1000000);
    }
}
