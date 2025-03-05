package dev.williamnogueira.ecommerce.domain.order.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmptyShoppingCartException extends ResponseStatusException {
    public EmptyShoppingCartException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
