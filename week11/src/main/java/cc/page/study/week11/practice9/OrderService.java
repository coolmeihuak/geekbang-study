package cc.page.study.week11.practice9;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public void add(OrderModel model) {
        System.out.println("add order @@ " + model);
    }
}
