package cc.page.study.week8;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    void add(OrderModel model);

    void edit(Long id, OrderModel model);

    OrderDto get(Long id);

    Page<OrderDto> getPage(Pageable pageable);
}
