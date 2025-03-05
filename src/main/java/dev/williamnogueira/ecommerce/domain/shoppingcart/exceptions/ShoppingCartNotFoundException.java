package dev.williamnogueira.ecommerce.domain.shoppingcart.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ShoppingCartNotFoundException extends ResponseStatusException {
    public ShoppingCartNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
