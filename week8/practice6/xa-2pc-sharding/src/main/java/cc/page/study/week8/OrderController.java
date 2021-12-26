package cc.page.study.week8;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @PostMapping
    public HttpEntity<Void> add(@RequestBody OrderModel model) {
        orderService.add(model);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public HttpEntity<Void> edit(@PathVariable Long id, @RequestBody OrderModel model) {
        orderService.edit(id, model);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public HttpEntity<OrderDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @GetMapping
    public HttpEntity<Page<OrderDto>> getPage(Pageable pageable) {
        return ResponseEntity.ok(orderService.getPage(pageable));
    }

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
}
