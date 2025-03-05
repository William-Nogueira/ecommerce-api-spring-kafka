package dev.williamnogueira.ecommerce.domain.payment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PaymentStatusException extends ResponseStatusException {
    public PaymentStatusException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
