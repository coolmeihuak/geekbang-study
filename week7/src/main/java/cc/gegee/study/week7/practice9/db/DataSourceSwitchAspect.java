package cc.gegee.study.week7.practice9.db;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 多数据源切换AOP
 */
@Aspect
@Component
@Slf4j
public class DataSourceSwitchAspect {

    /**
     * 切换为 master 数据源织入点
     */
    @Pointcut("@annotation(cc.gegee.study.week7.practice9.db.tags.Master) ")
    public void switchToMasterPointcut() {

    }

    /**
     * 切换为 slave 数据源织入点
     */
    @Pointcut("@annotation(cc.gegee.study.week7.practice9.db.tags.Slave) ")
    public void switchToSlavePointcut() {

    }

    @Before("switchToSlavePointcut()")
    public void readBefore() {
        DBContextHolder.switchToSlave();
    }

    /**
     * slave 结束后切回到主数据源
     */
    @AfterReturning("switchToSlavePointcut()")
    public void readAfter() {
        DBContextHolder.switchToMaster();
        log.info("after use slave db, set to master db");
    }

    @Before("switchToMasterPointcut()")
    public void writeBefore() {
        DBContextHolder.switchToMaster();
    }

}
