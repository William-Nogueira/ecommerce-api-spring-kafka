package dev.williamnogueira.ecommerce.domain.shoppingcart.shoppingcartitem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ShoppingCartItemResponseDTO(
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal priceAtAddedTime,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

