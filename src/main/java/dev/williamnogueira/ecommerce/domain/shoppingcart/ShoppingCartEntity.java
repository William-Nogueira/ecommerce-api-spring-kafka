package dev.williamnogueira.ecommerce.domain.shoppingcart;

import dev.williamnogueira.ecommerce.domain.common.BaseEntity;
import dev.williamnogueira.ecommerce.domain.customer.CustomerEntity;
import dev.williamnogueira.ecommerce.domain.shoppingcart.shoppingcartitem.ShoppingCartItemEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ShoppingCart")
@Table(name = "shopping_cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ShoppingCartEntity extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "customer", nullable = false, unique = true)
    private CustomerEntity customer;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingCartItemEntity> items = new ArrayList<>();

    private BigDecimal totalPrice;
}
