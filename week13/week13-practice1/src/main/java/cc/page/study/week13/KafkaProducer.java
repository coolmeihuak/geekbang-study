package cc.page.study.week13;

import com.google.gson.Gson;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void produce() {
        val msg = Message.builder()
                .id(UUID.randomUUID().toString())
                .content("this is kafka message")
                .time(new Date())
                .build();
        kafkaTemplate.send(KafkaTopic.TOPIC1, new Gson().toJson(msg));
    }
}
