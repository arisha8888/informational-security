package nsu.security.demoapplication.controller;

import nsu.security.demoapplication.model.Order;
import nsu.security.demoapplication.repository.OrderRepository;
import nsu.security.demoapplication.repository.OrderJdbcRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; //
import org.springframework.web.servlet.view.RedirectView; // 

import java.util.List;

@RestController
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderJdbcRepository orderJdbcRepository;

    public OrderController(OrderRepository orderRepository, OrderJdbcRepository orderJdbcRepository) {
        this.orderRepository = orderRepository;
        this.orderJdbcRepository = orderJdbcRepository;
    }
    
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/orders")
    public Long createOrder(@RequestBody Order order) {
        Order saved = orderRepository.save(order);
        return saved.getId();
    }

    @PostMapping("/ordersql")
    public Long createOrderSql(@RequestBody Order order) {
        Order saved = orderRepository.insertOrder(order.getProductType().name(), order.getQuantity());
        return saved.getId();
    }

    @GetMapping("/orders/jdbc/{id}")
    public List<Order> getOrdersJdbc(@PathVariable String id) {
        return orderJdbcRepository.getOrders(id);
    }

    // уязвимость 
    @GetMapping("/orders/redirect")
    public RedirectView redirect(@RequestParam String url) {
        // false означает, что URL не является относительным контекстом приложения,
        // что позволяет перенаправлять на внешние сайты (уязвимость Open Redirect)
        return new RedirectView(url, false); 
    }

    @GetMapping("/orders/safe-redirect")
    public RedirectView safeRedirect(@RequestParam String url) {
        // Разрешаем только относительные пути (начинаются с /)
        if (url.startsWith("/") && !url.startsWith("//")) {
            return new RedirectView(url, true);
        }
        
        // Если URL внешний или подозрительный — редиректим на главную страницу
        return new RedirectView("/", true);
    }

} 