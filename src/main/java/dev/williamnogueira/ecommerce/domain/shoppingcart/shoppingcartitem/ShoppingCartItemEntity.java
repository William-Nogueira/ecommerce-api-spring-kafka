package dev.williamnogueira.ecommerce.domain.shoppingcart.shoppingcartitem;

import dev.williamnogueira.ecommerce.domain.common.BaseEntity;
import dev.williamnogueira.ecommerce.domain.product.ProductEntity;
import dev.williamnogueira.ecommerce.domain.shoppingcart.ShoppingCartEntity;
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

@Entity(name = "ShoppingCartItem")
@Table(name = "shopping_cart_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ShoppingCartItemEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "shopping_cart", nullable = false)
    private ShoppingCartEntity shoppingCart;

    @ManyToOne
    @JoinColumn(name = "product", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal priceAtAddedTime;
}
