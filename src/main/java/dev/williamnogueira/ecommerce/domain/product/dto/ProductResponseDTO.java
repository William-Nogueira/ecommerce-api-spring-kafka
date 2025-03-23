package dev.williamnogueira.ecommerce.domain.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDTO(UUID id,
                                 String sku,
                                 String name,
                                 String label,
                                 String category,
                                 BigDecimal price,
                                 BigDecimal discount,
                                 Integer stockQuantity,
                                 Byte installments) implements Serializable {
}
