package dev.williamnogueira.ecommerce.domain.customer.dto;

import jakarta.validation.constraints.Email;

public record CustomerPatchDTO(
        String name,
        @Email
        String email,
        String phoneNumber,
        Boolean active
) {
}
