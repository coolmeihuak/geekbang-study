package cc.gegee.study.week7.practice2;

import cc.gegee.study.week7.practice2.common.BatchUtils;
import cc.gegee.study.week7.practice2.common.ThreadAdder;
import cc.gegee.study.week7.practice2.connection.Jdbc;
import cc.gegee.study.week7.practice2.dao.UserDao;
import lombok.val;

import java.sql.SQLException;

/**
 * 多线程多连接 + Statement
 */
public class ThreadNStatement {

    /**
     * 21 秒
     */
    public static void main(String[] args) throws SQLException, InterruptedException {
        ThreadAdder.add(BatchUtils::addStatement, 1000000, 10);
    }
}
