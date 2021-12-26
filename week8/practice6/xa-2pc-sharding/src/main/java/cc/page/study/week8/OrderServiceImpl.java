package cc.page.study.week8;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    @ShardingTransactionType(TransactionType.XA)
    public void add(OrderModel model) {
        val order = Order.builder()
                .money(model.getMoney())
                .status(model.getStatus())
                .userId(model.getUserId())
                .build();
        orderRepository.save(order);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @ShardingTransactionType(TransactionType.XA)
    public void edit(Long id, OrderModel model) {
        val order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("invalid id"));
        order.setMoney(model.getMoney());
        order.setStatus(model.getStatus());
        order.setUserId(model.getUserId());
        orderRepository.save(order);
    }

    @Override
    public OrderDto get(Long id) {
        val order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("invalid id"));
        return OrderDto.builder()
                .money(order.getMoney())
                .status(order.getStatus())
                .userId(order.getUserId())
                .build();
    }

    @Override
    public Page<OrderDto> getPage(Pageable pageable) {
        return orderRepository.findAll(pageable).map(x -> OrderDto.builder()
                .money(x.getMoney())
                .status(x.getStatus())
                .userId(x.getUserId())
                .build());
    }

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
