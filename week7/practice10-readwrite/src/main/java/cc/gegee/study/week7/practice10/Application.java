package cc.gegee.study.week7.practice10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

import static cc.gegee.study.week7.practice10.Application.BASE_PACKAGE;

@SpringBootApplication(exclude = JtaAutoConfiguration.class, scanBasePackages = {BASE_PACKAGE})
@EnableJpaAuditing
public class Application {

    public static final String BASE_PACKAGE = "cc.gegee.study.week7.practice10";

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        SpringApplication.run(Application.class, args);
//        try (ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args)) {
//            ExampleExecuteTemplate.run(applicationContext.getBean(ExampleService.class));
//        }
    }
}
