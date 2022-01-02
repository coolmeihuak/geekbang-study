package cc.page.study.week9.practice7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:spring-dubbo.xml"})
public class TransactionServiceApplication implements ApplicationRunner {

    @Autowired
    private TransactionService transactionService;

    public static void main(String[] args) {
        SpringApplication.run(TransactionServiceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        transactionService.transaction();
    }
}
