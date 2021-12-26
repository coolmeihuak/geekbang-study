package cc.page.study.week8;

import cc.page.study.week8.config.TransactionConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@SpringBootApplication
@Import(TransactionConfig.class)
@Slf4j
public class Xa2pcShardingApplication implements CommandLineRunner {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Xa2pcShardingApplication.class, args);
        DataSource dataSource = ctx.getBean(DataSource.class);
        log.info("当前使用的数据源: " + dataSource.getClass());
    }

    @Override
    public void run(String... args) throws Exception {

    }
}