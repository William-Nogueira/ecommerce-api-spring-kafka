package dev.williamnogueira.ecommerce.domain.shoppingcart.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NegativeQuantityException extends ResponseStatusException {
    public NegativeQuantityException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
