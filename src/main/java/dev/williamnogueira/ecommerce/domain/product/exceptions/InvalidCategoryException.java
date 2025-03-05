package dev.williamnogueira.ecommerce.domain.product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCategoryException extends ResponseStatusException {
    public InvalidCategoryException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
