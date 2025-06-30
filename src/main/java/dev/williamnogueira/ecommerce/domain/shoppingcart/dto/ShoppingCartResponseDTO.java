package dev.williamnogueira.ecommerce.domain.shoppingcart.dto;

import dev.williamnogueira.ecommerce.domain.shoppingcart.shoppingcartitem.dto.ShoppingCartItemResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ShoppingCartResponseDTO(
        UUID shoppingCartId,
        UUID customerId,
        List<ShoppingCartItemResponseDTO> items,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
