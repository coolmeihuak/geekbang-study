package cc.page.study.week13;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class KafkaProducerApplicationTests {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    void send() {
        kafkaProducer.produce();
    }
}
