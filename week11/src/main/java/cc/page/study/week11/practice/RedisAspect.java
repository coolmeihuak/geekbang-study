package cc.page.study.week11.practice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class RedisAspect {



    /**
     * 切换为 master 数据源织入点
     */
    @Pointcut("@annotation(cc.page.study.week11.practice.RedisAsync) ")
    public void async() {

    }

    /**
     * slave 结束后切回到主数据源
     */
    @Around("async()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("========>around begin klass dong //1");

        joinPoint.proceed();
        System.out.println("========>around after klass dong //3");
    }
}
