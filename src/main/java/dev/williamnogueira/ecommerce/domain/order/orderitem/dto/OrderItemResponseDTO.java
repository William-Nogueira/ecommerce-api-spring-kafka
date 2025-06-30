package dev.williamnogueira.ecommerce.domain.order.orderitem.dto;

import dev.williamnogueira.ecommerce.domain.product.dto.ProductResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderItemResponseDTO(
    UUID id,
    ProductResponseDTO product,
    Integer quantity,
    BigDecimal priceAtPurchase,
    BigDecimal totalPrice,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
