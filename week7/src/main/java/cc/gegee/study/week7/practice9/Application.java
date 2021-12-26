package cc.gegee.study.week7.practice9;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

import static cc.gegee.study.week7.practice9.Application.BASE_PACKAGE;

@SpringBootApplication(scanBasePackages = {BASE_PACKAGE})
@EnableJpaAuditing
public class Application {

    public static final String BASE_PACKAGE = "cc.gegee.study.week7.practice9";

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        SpringApplication.run(Application.class, args);
    }
}
