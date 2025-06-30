package dev.williamnogueira.ecommerce.domain.shoppingcart;

import dev.williamnogueira.ecommerce.domain.product.ProductEntity;
import dev.williamnogueira.ecommerce.domain.product.ProductService;
import dev.williamnogueira.ecommerce.domain.product.exceptions.ProductNotFoundException;
import dev.williamnogueira.ecommerce.domain.shoppingcart.dto.ShoppingCartRequestDTO;
import dev.williamnogueira.ecommerce.domain.shoppingcart.dto.ShoppingCartResponseDTO;
import dev.williamnogueira.ecommerce.domain.shoppingcart.exceptions.NegativeQuantityException;
import dev.williamnogueira.ecommerce.domain.shoppingcart.exceptions.QuantityGreaterThanAvailableException;
import dev.williamnogueira.ecommerce.domain.shoppingcart.exceptions.ShoppingCartNotFoundException;
import dev.williamnogueira.ecommerce.domain.shoppingcart.shoppingcartitem.ShoppingCartItemEntity;
import dev.williamnogueira.ecommerce.domain.shoppingcart.shoppingcartitem.ShoppingCartItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.NEGATIVE_QUANTITY;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.PRODUCT_NOT_FOUND_WITH_ID;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.QUANTITY_GREATER_THAN_AVAILABLE;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.SHOPPING_CART_NOT_FOUND;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.SHOPPING_CART_NOT_FOUND_WITH_ID;

@Service
@AllArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemService shoppingCartItemService;
    private final ProductService productService;
    private final ShoppingCartMapper mapper;

    @Transactional
    public ShoppingCartResponseDTO addToCart(String customerId, ShoppingCartRequestDTO request) {
        var product = productService.getEntity(request.productId());

        if (request.quantity() > product.getStockQuantity()) {
            throw new QuantityGreaterThanAvailableException(QUANTITY_GREATER_THAN_AVAILABLE);
        }

        var cart = findByCustomerId(UUID.fromString(customerId));

        buildCartItems(request, cart, product);
        updateTotalPrice(cart);

        shoppingCartRepository.save(cart);
        productService.subtractStockQuantity(product.getId(), request.quantity());

        return mapper.toResponseDTO(cart);
    }

    @Transactional
    public ShoppingCartResponseDTO removeFromCart(String customerId, ShoppingCartRequestDTO request) {
        var cart = findByCustomerId(UUID.fromString(customerId));

        var item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(request.productId()))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID, request.productId())));

        if (item.getQuantity() - request.quantity() < 0) {
            throw new NegativeQuantityException(NEGATIVE_QUANTITY);
        }

        item.setQuantity(item.getQuantity() - request.quantity());

        if (item.getQuantity() == 0) {
            cart.getItems().remove(item);
            shoppingCartItemService.delete(item);
        }

        updateTotalPrice(cart);

        shoppingCartRepository.save(cart);
        productService.addStockById(request.productId(), request.quantity());

        return mapper.toResponseDTO(cart);
    }

    @Transactional(readOnly = true)
    public ShoppingCartResponseDTO getShoppingCartByCustomerId(UUID customerId) {
        return mapper.toResponseDTO(findByCustomerId(customerId));
    }

    @Transactional
    public void save(ShoppingCartEntity cart) {
        shoppingCartRepository.save(cart);
    }

    public ShoppingCartEntity findByCustomerId(UUID customerId) {
        return shoppingCartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ShoppingCartNotFoundException(SHOPPING_CART_NOT_FOUND));
    }

    public ShoppingCartEntity getEntity(UUID id) {
        return shoppingCartRepository.findById(id)
                .orElseThrow(() -> new ShoppingCartNotFoundException(String.format(SHOPPING_CART_NOT_FOUND_WITH_ID, id)));
    }

    private void buildCartItems(ShoppingCartRequestDTO request, ShoppingCartEntity cart, ProductEntity product) {
        Optional<ShoppingCartItemEntity> existingItemOpt = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst();

        ShoppingCartItemEntity cartItem;
        if (existingItemOpt.isPresent()) {
            cartItem = existingItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
        } else {
            cartItem = ShoppingCartItemEntity.builder()
                    .shoppingCart(cart)
                    .product(product)
                    .quantity(request.quantity())
                    .priceAtAddedTime(product.getPrice())
                    .build();
            cart.getItems().add(cartItem);
        }
    }

    private void updateTotalPrice(ShoppingCartEntity cart) {
        var totalPrice = cart.getItems().stream()
                .map(item -> item.getPriceAtAddedTime().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPrice);
    }
}
