package dev.williamnogueira.ecommerce.utils;

import dev.williamnogueira.ecommerce.domain.product.ProductCategoryEnum;
import dev.williamnogueira.ecommerce.domain.product.ProductEntity;
import dev.williamnogueira.ecommerce.domain.product.dto.ProductRequestDTO;
import dev.williamnogueira.ecommerce.domain.product.dto.ProductResponseDTO;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static dev.williamnogueira.ecommerce.utils.TestConstants.ID;

@UtilityClass
public class ProductTestUtils {

    public static ProductEntity createProductEntity() {
        return ProductEntity.builder()
                .id(ID)
                .sku("INTEL CORE I9 5500")
                .name("Intel Core i9 5500 2.9GHZ")
                .label("Intel")
                .category(ProductCategoryEnum.CPU)
                .price(new BigDecimal(1200))
                .discount(new BigDecimal(10))
                .installments((byte) 2)
                .stockQuantity(10)
                .active(true)
                .build();
    }

    public static ProductResponseDTO createProductResponseDTO() {
        ProductEntity product = createProductEntity();
        return new ProductResponseDTO(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getLabel(),
                product.getCategory().name(),
                product.getPrice(),
                product.getDiscount(),
                product.getStockQuantity(),
                product.getInstallments(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static ProductRequestDTO createProductRequestDTO() {
        ProductEntity product = createProductEntity();
        return new ProductRequestDTO(
                product.getSku(),
                product.getName(),
                product.getLabel(),
                product.getCategory().name(),
                product.getPrice(),
                product.getDiscount(),
                product.getInstallments(),
                product.getStockQuantity(),
                product.getActive()
        );
    }

    public static ProductRequestDTO createProductWithNewSku() {
        ProductEntity product = createProductEntity();
        return new ProductRequestDTO(
                product.getSku() + " NEW",
                product.getName(),
                product.getLabel(),
                product.getCategory().name(),
                product.getPrice(),
                product.getDiscount(),
                product.getInstallments(),
                product.getStockQuantity(),
                product.getActive()
        );
    }

    public static ProductRequestDTO createInvalidProductRequestDTO() {
        ProductEntity product = createProductEntity();
        return new ProductRequestDTO(
                product.getSku(),
                product.getName(),
                product.getLabel(),
                "INVALID CATEGORY",
                product.getPrice(),
                product.getDiscount(),
                product.getInstallments(),
                product.getStockQuantity(),
                product.getActive()
        );
    }
}
