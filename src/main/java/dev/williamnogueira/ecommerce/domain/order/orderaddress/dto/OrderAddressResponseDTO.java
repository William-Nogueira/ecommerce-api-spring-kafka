package dev.williamnogueira.ecommerce.domain.order.orderaddress.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderAddressResponseDTO(
        UUID id,
        String street,
        String number,
        String neighborhood,
        String city,
        String state,
        String country,
        String zipCode,
        String additionalInfo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
