package dev.williamnogueira.ecommerce.controller;

import dev.williamnogueira.ecommerce.domain.payment.kafka.PaymentProducer;
import dev.williamnogueira.ecommerce.domain.payment.dto.PaymentProducerResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/payment")
public class PaymentController {

    private final PaymentProducer paymentProducer;

    @PostMapping("/{orderId}")
    public ResponseEntity<PaymentProducerResponseDTO> processPayment(@PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentProducer.sendPaymentRequest(orderId.toString()));
    }
}
