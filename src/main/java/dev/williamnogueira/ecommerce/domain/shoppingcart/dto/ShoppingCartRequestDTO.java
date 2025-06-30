package dev.williamnogueira.ecommerce.domain.shoppingcart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ShoppingCartRequestDTO(
        @NotNull
        UUID productId,
        @Positive
        Integer quantity
) {}

