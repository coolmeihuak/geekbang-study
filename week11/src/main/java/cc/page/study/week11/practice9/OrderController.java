package cc.page.study.week11.practice9;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final PublishUtils publishUtils;
    private final SubscribeUtils subscribeUtils;

    public OrderController(PublishUtils publishUtils, SubscribeUtils subscribeUtils) {
        this.publishUtils = publishUtils;
        this.subscribeUtils = subscribeUtils;
    }

    @PostMapping
    public HttpEntity<Void> add(@RequestBody OrderModel model) {
        subscribeUtils.subscribe();
        publishUtils.publish(model);
        return ResponseEntity.ok().build();
    }
}
