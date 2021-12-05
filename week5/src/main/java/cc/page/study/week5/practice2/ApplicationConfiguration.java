package cc.page.study.week5.practice2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"cc.page.study.week5.practice2"})
public class ApplicationConfiguration {

    @Bean
    public Dog dog() {
        return Dog.builder().name("kity").age(4).build();
    }
}
