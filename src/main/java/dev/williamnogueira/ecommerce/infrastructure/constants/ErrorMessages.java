package dev.williamnogueira.ecommerce.infrastructure.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessages {

    public static final String PRODUCT_NOT_FOUND_WITH_ID = "Product with id %s not found.";
    public static final String SKU_ALREADY_EXISTS = "A product with the given SKU already exists.";
    public static final String INVALID_CATEGORY = "Invalid category: %s";
    public static final String CUSTOMER_ALREADY_EXISTS_WITH_EMAIL = "Customer already exists with email %s";
    public static final String CUSTOMER_NOT_FOUND_WITH_ID = "Customer with id %s not found.";
    public static final String ADDRESS_NOT_FOUND_WITH_ID = "Address with id %s not found.";
    public static final String ORDER_NOT_FOUND_WITH_ID = "Order with id %s not found.";
    public static final String SHOPPING_CART_IS_EMPTY = "The shopping cart is empty.";
    public static final String ADDRESS_NOT_FOUND = "Address not found for the given customer.";
    public static final String QUANTITY_GREATER_THAN_AVAILABLE = "The quantity is greater than the available quantity.";
    public static final String SHOPPING_CART_NOT_FOUND = "Shopping cart not found.";
    public static final String SHOPPING_CART_NOT_FOUND_WITH_ID = "Shopping cart with id %s not found.";
    public static final String NEGATIVE_QUANTITY = "The remaining quantity must be greater than zero.";
    public static final String ERROR_PROCESSING_PAYMENT = "Error processing payment for order %s: %s";
    public static final String ORDER_STATUS_MUST_BE_PENDING = "The order status must be PENDING.";

}
