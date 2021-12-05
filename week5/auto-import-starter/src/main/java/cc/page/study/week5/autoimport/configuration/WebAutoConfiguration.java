package cc.page.study.week5.autoimport.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WebConfiguration.class)
public class WebAutoConfiguration {

    
}
