package dev.williamnogueira.ecommerce.domain.shoppingcart;

import dev.williamnogueira.ecommerce.domain.shoppingcart.dto.ShoppingCartRequestDTO;
import dev.williamnogueira.ecommerce.domain.shoppingcart.dto.ShoppingCartResponseDTO;
import dev.williamnogueira.ecommerce.domain.shoppingcart.shoppingcartitem.ShoppingCartItemEntity;
import dev.williamnogueira.ecommerce.domain.shoppingcart.shoppingcartitem.dto.ShoppingCartItemResponseDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface ShoppingCartMapper {

    @Mapping(target = "id", ignore = true)
    ShoppingCartEntity toEntity(ShoppingCartRequestDTO dto);

    @Mapping(target = "shoppingCartId", source = "id")
    @Mapping(target = "customerId", source = "customer.id")
    ShoppingCartResponseDTO toResponseDTO(ShoppingCartEntity entity);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "totalPrice", expression = "java(item.getPriceAtAddedTime().multiply(BigDecimal.valueOf(item.getQuantity())))")
    ShoppingCartItemResponseDTO toResponseDTO(ShoppingCartItemEntity item);

}
