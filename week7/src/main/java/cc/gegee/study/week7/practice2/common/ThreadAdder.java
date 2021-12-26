package cc.gegee.study.week7.practice2.common;

import cc.gegee.study.week7.practice2.ThreadNPreparedStatement;
import cc.gegee.study.week7.practice2.connection.Hikari;
import cc.gegee.study.week7.practice2.dao.UserDao;
import lombok.Builder;
import lombok.Data;
import lombok.val;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadAdder {

    public static void add(Adder adder, int size, int count) throws SQLException, InterruptedException {
        val dataSource = Hikari.getDataSource();
        val connection = dataSource.getConnection();
        val userId = 1L;
        UserDao.add(connection);
        connection.close();
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        final CountDownLatch countDownLatch = new CountDownLatch(count);
        List<Connection> cs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            cs.add(dataSource.getConnection());
        }
        System.out.println("@@@@@@@ 开始插入");
        Long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            executorService.execute(new Task(cs.get(i), countDownLatch, userId, size / count, adder));
        }
        countDownLatch.await();
        Long endTime = System.currentTimeMillis();
        System.out.println("@@@@@@@ 插入完毕,用时：" + (endTime - startTime) / 1000);
    }

    @Builder
    @Data
    static class Task implements Runnable {
        private Connection connection;
        private CountDownLatch countDownLatch;
        private long userId;
        private int size;
        private Adder adder;

        @Override
        public void run() {
            try {
                System.out.println("###### 开始插入");
                adder.add(connection, userId, size);
                connection.close();
                System.out.println("###### 插入完毕");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
