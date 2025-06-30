package dev.williamnogueira.ecommerce.domain.product;

import dev.williamnogueira.ecommerce.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity(name = "Product")
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String sku;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String label;
    
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ProductCategoryEnum category;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(nullable = false)
    private BigDecimal discount;
    
    @Column(nullable = false)
    private Byte installments;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean active;
}
