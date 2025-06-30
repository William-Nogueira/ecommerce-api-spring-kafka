package dev.williamnogueira.ecommerce.controller;

import dev.williamnogueira.ecommerce.domain.shoppingcart.ShoppingCartService;
import dev.williamnogueira.ecommerce.domain.shoppingcart.dto.ShoppingCartRequestDTO;
import dev.williamnogueira.ecommerce.domain.shoppingcart.dto.ShoppingCartResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping("/{customerId}")
    public ResponseEntity<ShoppingCartResponseDTO> getShoppingCartByCustomerId(@PathVariable UUID customerId) {
        return ResponseEntity.ok(shoppingCartService.getShoppingCartByCustomerId(customerId));
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<ShoppingCartResponseDTO> addToCart(@PathVariable String customerId,
                                                             @Valid @RequestBody ShoppingCartRequestDTO request) {
        return ResponseEntity.ok(shoppingCartService.addToCart(customerId, request));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ShoppingCartResponseDTO> removeFromCart(@PathVariable String customerId,
                                                                  @Valid @RequestBody ShoppingCartRequestDTO request) {
        return ResponseEntity.ok(shoppingCartService.removeFromCart(customerId, request));
    }
}
