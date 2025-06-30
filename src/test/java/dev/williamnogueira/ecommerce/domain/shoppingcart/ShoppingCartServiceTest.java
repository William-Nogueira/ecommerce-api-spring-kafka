package dev.williamnogueira.ecommerce.domain.shoppingcart;

import dev.williamnogueira.ecommerce.domain.customer.CustomerService;
import dev.williamnogueira.ecommerce.domain.product.ProductEntity;
import dev.williamnogueira.ecommerce.domain.product.ProductService;
import dev.williamnogueira.ecommerce.domain.shoppingcart.dto.ShoppingCartRequestDTO;
import dev.williamnogueira.ecommerce.domain.shoppingcart.dto.ShoppingCartResponseDTO;
import dev.williamnogueira.ecommerce.domain.shoppingcart.exceptions.NegativeQuantityException;
import dev.williamnogueira.ecommerce.domain.shoppingcart.exceptions.QuantityGreaterThanAvailableException;
import dev.williamnogueira.ecommerce.domain.shoppingcart.exceptions.ShoppingCartNotFoundException;
import dev.williamnogueira.ecommerce.domain.shoppingcart.shoppingcartitem.ShoppingCartItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static dev.williamnogueira.ecommerce.utils.ProductTestUtils.createProductEntity;
import static dev.williamnogueira.ecommerce.utils.ShoppingCartTestUtils.createShoppingCartEntity;
import static dev.williamnogueira.ecommerce.utils.ShoppingCartTestUtils.createShoppingCartRequestDTO;
import static dev.williamnogueira.ecommerce.utils.ShoppingCartTestUtils.createShoppingCartRequestDTOWithInvalidQuantity;
import static dev.williamnogueira.ecommerce.utils.ShoppingCartTestUtils.createShoppingCartResponseDTO;
import static dev.williamnogueira.ecommerce.utils.TestConstants.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ProductService productService;

    @Mock
    private CustomerService customerService;

    @Mock
    private ShoppingCartItemService shoppingCartItemService;

    @Mock
    private Pageable pageable;

    @Mock
    private ShoppingCartMapper mapper;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    private ProductEntity productEntity;
    private ShoppingCartEntity shoppingCartEntity;
    private ShoppingCartRequestDTO shoppingCartRequestDTO;
    private ShoppingCartResponseDTO shoppingCartResponseDTO;

    @BeforeEach
    void setUp() {
        shoppingCartEntity = createShoppingCartEntity();
        productEntity = createProductEntity();
        shoppingCartRequestDTO = createShoppingCartRequestDTO();
        shoppingCartResponseDTO = createShoppingCartResponseDTO();
    }

    @Test
    void testAddProductToShoppingCart() {
        // arrange
        when(productService.getEntity(productEntity.getId())).thenReturn(productEntity);
        when(shoppingCartRepository.findByCustomerId(ID))
                .thenReturn(Optional.of(shoppingCartEntity));
        when(mapper.toResponseDTO(shoppingCartEntity)).thenReturn(shoppingCartResponseDTO);

        // act
        var response = shoppingCartService.addToCart(String.valueOf(ID), shoppingCartRequestDTO);

        // assert
        assertThat(response).isNotNull().isEqualTo(shoppingCartResponseDTO);
        verify(shoppingCartRepository).findByCustomerId(ID);
        verify(productService).getEntity(productEntity.getId());
    }

    @Test
    void testAddProductToShoppingCartEmptyItems() {
        // arrange
        when(productService.getEntity(productEntity.getId())).thenReturn(productEntity);
        when(shoppingCartRepository.findByCustomerId(ID))
                .thenReturn(Optional.of(shoppingCartEntity));
        when(mapper.toResponseDTO(shoppingCartEntity)).thenReturn(shoppingCartResponseDTO);

        shoppingCartEntity.getItems().clear();

        // act
        var response = shoppingCartService.addToCart(String.valueOf(ID), shoppingCartRequestDTO);

        // assert
        assertThat(response).isNotNull().isEqualTo(shoppingCartResponseDTO);
        verify(shoppingCartRepository).findByCustomerId(ID);
        verify(productService).getEntity(productEntity.getId());
    }

    @Test
    void testAddProductToShoppingCartQuantityGreaterThanAvailable() {
        // arrange
        when(productService.getEntity(productEntity.getId())).thenReturn(productEntity);

        productEntity.setStockQuantity(2);

        // act and assert
        assertThatException()
                .isThrownBy(() -> shoppingCartService.addToCart(String.valueOf(ID), shoppingCartRequestDTO))
                .isInstanceOf(QuantityGreaterThanAvailableException.class);
    }

    @Test
    void testRemoveProductFromShoppingCart() {
        // arrange
        when(shoppingCartRepository.findByCustomerId(ID))
                .thenReturn(Optional.of(shoppingCartEntity));
        when(mapper.toResponseDTO(shoppingCartEntity)).thenReturn(shoppingCartResponseDTO);

        // act
        var response = shoppingCartService.removeFromCart(String.valueOf(ID), shoppingCartRequestDTO);

        // assert
        assertThat(response).isNotNull().isEqualTo(shoppingCartResponseDTO);
        verify(shoppingCartRepository).findByCustomerId(ID);
        verify(productService).addStockById(productEntity.getId(), shoppingCartRequestDTO.quantity());
    }

    @Test
    void testRemoveFromCartNegativeQuantity() {
        // arrange
        when(shoppingCartRepository.findByCustomerId(ID))
                .thenReturn(Optional.of(shoppingCartEntity));

        var invalidRequest = createShoppingCartRequestDTOWithInvalidQuantity();

        // act and assert
        assertThatException()
                .isThrownBy(() -> shoppingCartService.removeFromCart(String.valueOf(ID), invalidRequest))
                .isInstanceOf(NegativeQuantityException.class);
    }

    @Test
    void testGetShoppingCartByCustomerId() {
        // arrange
        when(shoppingCartRepository.findByCustomerId(ID))
                .thenReturn(Optional.of(shoppingCartEntity));
        when(mapper.toResponseDTO(shoppingCartEntity)).thenReturn(shoppingCartResponseDTO);

        // act
        var response = shoppingCartService.getShoppingCartByCustomerId(ID);

        // assert
        assertThat(response).isNotNull().isEqualTo(shoppingCartResponseDTO);
    }

    @Test
    void testSave() {
        // arrange
        when(shoppingCartRepository.save(shoppingCartEntity)).thenReturn(shoppingCartEntity);

        // act
        shoppingCartService.save(shoppingCartEntity);

        // assert
        verify(shoppingCartRepository).save(shoppingCartEntity);
    }

    @Test
    void testGetEntity() {
        // arrange
        when(shoppingCartRepository.findById(shoppingCartEntity.getId()))
                .thenReturn(Optional.of(shoppingCartEntity));

        // act
        var response = shoppingCartService.getEntity(ID);

        // assert
        assertThat(response).isNotNull().isEqualTo(shoppingCartEntity);
    }

    @Test
    void testGetEntityNotFound() {
        // arrange
        when(shoppingCartRepository.findById(ID)).thenReturn(Optional.empty());

        // act and assert
        assertThatException()
                .isThrownBy(() -> shoppingCartService.getEntity(ID))
                .isInstanceOf(ShoppingCartNotFoundException.class);
    }

}