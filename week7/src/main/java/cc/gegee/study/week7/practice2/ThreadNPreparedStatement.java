package cc.gegee.study.week7.practice2;

import cc.gegee.study.week7.practice2.common.BatchUtils;
import cc.gegee.study.week7.practice2.common.ThreadAdder;

import java.sql.SQLException;

/**
 * 多线程多连接 + 预处理
 */
public class ThreadNPreparedStatement {

    /**
     * 20 秒
     */
    public static void main(String[] args) throws SQLException, InterruptedException {
        ThreadAdder.add(BatchUtils::addPreparedStatement, 1000000, 10);
    }
}
