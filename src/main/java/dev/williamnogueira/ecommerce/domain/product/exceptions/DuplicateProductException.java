package dev.williamnogueira.ecommerce.domain.product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateProductException extends ResponseStatusException {
    public DuplicateProductException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
