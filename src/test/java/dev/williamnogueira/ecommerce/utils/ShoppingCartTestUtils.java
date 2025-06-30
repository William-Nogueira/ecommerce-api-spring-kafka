package dev.williamnogueira.ecommerce.utils;

import dev.williamnogueira.ecommerce.domain.shoppingcart.ShoppingCartEntity;
import dev.williamnogueira.ecommerce.domain.shoppingcart.dto.ShoppingCartRequestDTO;
import dev.williamnogueira.ecommerce.domain.shoppingcart.dto.ShoppingCartResponseDTO;
import dev.williamnogueira.ecommerce.domain.shoppingcart.shoppingcartitem.ShoppingCartItemEntity;
import dev.williamnogueira.ecommerce.domain.shoppingcart.shoppingcartitem.dto.ShoppingCartItemResponseDTO;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static dev.williamnogueira.ecommerce.utils.CustomerTestUtils.createCustomerEntity;
import static dev.williamnogueira.ecommerce.utils.ProductTestUtils.createProductEntity;
import static dev.williamnogueira.ecommerce.utils.TestConstants.ID;

@UtilityClass
public class ShoppingCartTestUtils {

    public static ShoppingCartEntity createShoppingCartEntity() {
        List<ShoppingCartItemEntity> shoppingCartItemEntities = new ArrayList<>();
        shoppingCartItemEntities.add(createShoppingCartItemEntity());

        return ShoppingCartEntity.builder()
                .id(ID)
                .customer(createCustomerEntity())
                .items(shoppingCartItemEntities)
                .totalPrice(BigDecimal.valueOf(10.99))
                .build();
    }

    public static ShoppingCartRequestDTO createShoppingCartRequestDTO() {
        return new ShoppingCartRequestDTO(
                ID,
                5
        );
    }

    public static ShoppingCartRequestDTO createShoppingCartRequestDTOWithInvalidQuantity() {
        return new ShoppingCartRequestDTO(
                ID,
                15
        );
    }

    public static ShoppingCartResponseDTO createShoppingCartResponseDTO() {
        return new ShoppingCartResponseDTO(
                ID,
                ID,
                List.of(createShoppingCartItemResponseDTO()),
                BigDecimal.valueOf(10.99),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static ShoppingCartItemEntity createShoppingCartItemEntity() {
        return ShoppingCartItemEntity.builder()
                .id(ID)
                .shoppingCart(new ShoppingCartEntity())
                .product(createProductEntity())
                .quantity(5)
                .priceAtAddedTime(BigDecimal.valueOf(10.99))
                .build();
    }

    public static ShoppingCartItemResponseDTO createShoppingCartItemResponseDTO() {
        return new ShoppingCartItemResponseDTO(
                ID,
                "Intel Core i9 5500 2.9GHZ",
                1,
                BigDecimal.valueOf(10.99),
                BigDecimal.valueOf(10.99),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}