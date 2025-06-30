package dev.williamnogueira.ecommerce.controller;

import dev.williamnogueira.ecommerce.domain.order.OrderService;
import dev.williamnogueira.ecommerce.domain.order.dto.OrderResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<OrderResponseDTO>> findAllByCustomerId(@PathVariable UUID customerId,
                                                                      Pageable pageable) {
        return ResponseEntity.ok(orderService.findAllByCustomerId(customerId, pageable));
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<OrderResponseDTO> checkout(@PathVariable UUID customerId) {
        var createdOrder = orderService.purchaseShoppingCart(customerId);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdOrder.id())
                .toUri();
        return ResponseEntity.created(location).body(createdOrder);
    }
}
