package cc.page.study.week9.practice2.rpcfx.provider;

import cc.page.study.week9.practice2.rpcfx.api.Order;
import cc.page.study.week9.practice2.rpcfx.api.OrderService;
public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
