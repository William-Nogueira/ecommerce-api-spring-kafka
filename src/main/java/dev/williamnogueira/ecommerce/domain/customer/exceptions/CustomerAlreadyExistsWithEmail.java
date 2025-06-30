package dev.williamnogueira.ecommerce.domain.customer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomerAlreadyExistsWithEmail extends ResponseStatusException {
    public CustomerAlreadyExistsWithEmail(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
