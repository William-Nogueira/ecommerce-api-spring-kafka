package dev.williamnogueira.ecommerce.domain.order.orderitem;

import dev.williamnogueira.ecommerce.domain.common.BaseEntity;
import dev.williamnogueira.ecommerce.domain.order.OrderEntity;
import dev.williamnogueira.ecommerce.domain.product.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity(name = "OrderItem")
@Table(name = "order_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderItemEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "order_table", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal priceAtPurchase;

    @Column(nullable = false)
    private BigDecimal totalPrice;
}

