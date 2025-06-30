package dev.williamnogueira.ecommerce.utils;

import dev.williamnogueira.ecommerce.domain.order.OrderEntity;
import dev.williamnogueira.ecommerce.domain.order.OrderStatusEnum;
import dev.williamnogueira.ecommerce.domain.order.dto.OrderResponseDTO;
import dev.williamnogueira.ecommerce.domain.order.orderaddress.OrderAddressEntity;
import dev.williamnogueira.ecommerce.domain.order.orderaddress.dto.OrderAddressResponseDTO;
import dev.williamnogueira.ecommerce.domain.order.orderitem.OrderItemEntity;
import dev.williamnogueira.ecommerce.domain.order.orderitem.dto.OrderItemResponseDTO;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static dev.williamnogueira.ecommerce.utils.CustomerTestUtils.createCustomerEntity;
import static dev.williamnogueira.ecommerce.utils.ProductTestUtils.createProductEntity;
import static dev.williamnogueira.ecommerce.utils.ProductTestUtils.createProductResponseDTO;
import static dev.williamnogueira.ecommerce.utils.TestConstants.ID;

@UtilityClass
public class OrderTestUtils {

    public static OrderAddressEntity createOrderAddressEntity() {
        return OrderAddressEntity.builder()
                .id(ID)
                .street("123 Test St")
                .number("100")
                .neighborhood("Test Neighborhood")
                .city("Test City")
                .state("Test State")
                .country("Test Country")
                .zipCode("12345")
                .additionalInfo("Apartment 1A")
                .build();
    }

    public static OrderItemEntity createOrderItemEntity() {
        return OrderItemEntity.builder()
                .id(ID)
                .product(createProductEntity())
                .quantity(2)
                .priceAtPurchase(BigDecimal.valueOf(50))
                .totalPrice(BigDecimal.valueOf(100))
                .build();
    }

    public static OrderEntity createOrderEntity() {
        return OrderEntity.builder()
                .id(ID)
                .customer(createCustomerEntity())
                .orderItems(List.of(createOrderItemEntity()))
                .shippingAddress(createOrderAddressEntity())
                .totalPrice(BigDecimal.valueOf(100))
                .status(OrderStatusEnum.PENDING)
                .build();
    }

    public static OrderAddressResponseDTO createOrderAddressResponseDTO() {
        var address = createOrderAddressEntity();
        return new OrderAddressResponseDTO(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getZipCode(),
                address.getAdditionalInfo(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static OrderItemResponseDTO createOrderItemResponseDTO() {
        var item = createOrderItemEntity();
        return new OrderItemResponseDTO(
                item.getId(),
                createProductResponseDTO(),
                item.getQuantity(),
                item.getPriceAtPurchase(),
                item.getTotalPrice(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static OrderResponseDTO createOrderResponseDTO() {
        return new OrderResponseDTO(
                ID,
                CustomerTestUtils.createCustomerResponseDTO(),
                List.of(createOrderItemResponseDTO()),
                createOrderAddressResponseDTO(),
                BigDecimal.valueOf(100),
                OrderStatusEnum.PENDING.name(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
