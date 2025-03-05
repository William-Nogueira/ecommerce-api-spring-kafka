package dev.williamnogueira.ecommerce.domain.address.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AddressNotFoundException extends ResponseStatusException {
    public AddressNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
