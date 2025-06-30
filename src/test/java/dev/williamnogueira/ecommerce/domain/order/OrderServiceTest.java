package dev.williamnogueira.ecommerce.domain.order;

import dev.williamnogueira.ecommerce.domain.address.AddressTypeEnum;
import dev.williamnogueira.ecommerce.domain.order.exceptions.EmptyShoppingCartException;
import dev.williamnogueira.ecommerce.domain.order.exceptions.OrderNotFoundException;
import dev.williamnogueira.ecommerce.domain.order.dto.OrderResponseDTO;
import dev.williamnogueira.ecommerce.domain.order.kafka.OrderProducer;
import dev.williamnogueira.ecommerce.domain.shoppingcart.ShoppingCartEntity;
import dev.williamnogueira.ecommerce.domain.shoppingcart.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static dev.williamnogueira.ecommerce.utils.OrderTestUtils.*;
import static dev.williamnogueira.ecommerce.utils.ShoppingCartTestUtils.*;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.ORDER_NOT_FOUND_WITH_ID;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.SHOPPING_CART_IS_EMPTY;
import static dev.williamnogueira.ecommerce.utils.TestConstants.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderProducer orderProducer;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private OrderService orderService;

    private OrderEntity orderEntity;
    private OrderResponseDTO orderResponseDTO;
    private ShoppingCartEntity shoppingCartEntity;

    @BeforeEach
    void setUp() {
        orderEntity = createOrderEntity();
        orderResponseDTO = createOrderResponseDTO();
        shoppingCartEntity = createShoppingCartEntity();
    }

    @Test
    void testFindByIdSuccess() {
        // Arrange
        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.of(orderEntity));
        when(orderMapper.toResponseDTO(orderEntity)).thenReturn(orderResponseDTO);

        // Act
        var result = orderService.findById(ID);

        // Assert
        assertThat(result).isNotNull().isEqualTo(orderResponseDTO);
        verify(orderRepository).findById(orderEntity.getId());
        verify(orderMapper).toResponseDTO(orderEntity);
    }

    @Test
    void testFindByIdNotFound() {
        // Arrange
        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatException()
                .isThrownBy(() -> orderService.findById(ID))
                .isInstanceOf(OrderNotFoundException.class)
                .withMessageContaining(String.format(ORDER_NOT_FOUND_WITH_ID, ID));
        verify(orderRepository).findById(orderEntity.getId());
    }

    @Test
    void testFillAllByCustomerId() {
        // Arrange
        Page<OrderEntity> entityPage = new PageImpl<>(List.of(orderEntity));
        when(orderRepository.findAllByCustomerId(ID, pageable)).thenReturn(entityPage);
        when(orderMapper.toResponseDTO(orderEntity)).thenReturn(orderResponseDTO);

        // Act
        Page<OrderResponseDTO> response = orderService.findAllByCustomerId(ID, pageable);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).containsExactly(orderResponseDTO);
        verify(orderRepository).findAllByCustomerId(ID, pageable);
        verify(orderMapper).toResponseDTO(orderEntity);
    }

    @Test
    void testPurchaseShoppingCartSuccess() {
        // Arrange
        var shippingAddress = shoppingCartEntity.getCustomer().getAddress().get(0);

        when(shoppingCartService.findByCustomerId(ID)).thenReturn(shoppingCartEntity);
        when(orderMapper.toOrderItemsEntity(shoppingCartEntity.getItems())).thenReturn(orderEntity.getOrderItems());
        when(orderMapper.toOrderAddressEntity(shippingAddress)).thenReturn(createOrderAddressEntity());
        when(orderMapper.toResponseDTO(any(OrderEntity.class))).thenReturn(orderResponseDTO);

        // Act
        OrderResponseDTO result = orderService.purchaseShoppingCart(ID);

        // Assert
        assertThat(result).isNotNull().isEqualTo(orderResponseDTO);
        verify(shoppingCartService).findByCustomerId(ID);
        verify(orderRepository).save(any(OrderEntity.class));
        verify(orderProducer).clearShoppingCart(String.valueOf(ID));
    }

    @Test
    void testPurchaseShoppingCartWithoutShippingAddress() {
        // Arrange
        var shippingAddress = shoppingCartEntity.getCustomer().getAddress().get(0);
        shippingAddress.setType(AddressTypeEnum.BILLING);

        when(shoppingCartService.findByCustomerId(ID)).thenReturn(shoppingCartEntity);
        when(orderMapper.toOrderItemsEntity(shoppingCartEntity.getItems())).thenReturn(orderEntity.getOrderItems());
        when(orderMapper.toOrderAddressEntity(shippingAddress)).thenReturn(createOrderAddressEntity());
        when(orderMapper.toResponseDTO(any(OrderEntity.class))).thenReturn(orderResponseDTO);

        // Act
        OrderResponseDTO result = orderService.purchaseShoppingCart(ID);

        // Assert
        assertThat(result).isNotNull().isEqualTo(orderResponseDTO);
        verify(shoppingCartService).findByCustomerId(ID);
        verify(orderRepository).save(any(OrderEntity.class));
        verify(orderProducer).clearShoppingCart(String.valueOf(ID));
    }

    @Test
    void testPurchaseShoppingCartEmptyCart() {
        // Arrange
        shoppingCartEntity.getItems().clear();
        when(shoppingCartService.findByCustomerId(shoppingCartEntity.getCustomer().getId()))
                .thenReturn(shoppingCartEntity);

        // Act & Assert
        assertThatException()
                .isThrownBy(() -> orderService.purchaseShoppingCart(ID))
                .isInstanceOf(EmptyShoppingCartException.class)
                .withMessageContaining(SHOPPING_CART_IS_EMPTY);
        verify(shoppingCartService).findByCustomerId(shoppingCartEntity.getCustomer().getId());
    }

    @Test
    void testUpdateStatus() {
        // Arrange
        String orderIdStr = orderEntity.getId().toString();
        when(orderRepository.findById(orderEntity.getId())).thenReturn(Optional.of(orderEntity));

        // Act
        orderService.updateStatus(orderIdStr, OrderStatusEnum.PAID);

        // Assert
        assertThat(orderEntity.getStatus()).isEqualTo(OrderStatusEnum.PAID);
        verify(orderRepository).save(orderEntity);
    }
}
