package cc.gegee.study.week7.practice9.db;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据源选择器
 */
@Slf4j
public final class DBContextHolder {

    private static final ThreadLocal<DBInstanceEnum> contextHolder = ThreadLocal.withInitial(() -> DBInstanceEnum.MASTER);

    private static final AtomicInteger router = new AtomicInteger(-1);

    public static void switchToMaster() {
        contextHolder.set(DBInstanceEnum.MASTER);
        log.info("switch to master db");
    }

    public static void switchToSlave() {
        // 1.3改进：支持多个从库的负载均衡
        val next = router.incrementAndGet();
        val index = next % 2;
        if (index == 0) {
            contextHolder.set(DBInstanceEnum.SLAVE0);
            log.info("switch to slave0 db");
        } else {
            contextHolder.set(DBInstanceEnum.SLAVE1);
            log.info("switch to slave1 db");
        }
        if (next > 9999) {
            router.set(-1);
        }
    }

    public static DBInstanceEnum get() {
        val db = contextHolder.get();
        log.info("get db{} from contextHolder", db);
        return db;
    }
}
