package dev.williamnogueira.ecommerce.domain.order.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static dev.williamnogueira.ecommerce.infrastructure.constants.KafkaConstants.CLEAR_CART_TOPIC;
import static dev.williamnogueira.ecommerce.infrastructure.constants.KafkaConstants.SENDING_CLEAR_CART_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void clearShoppingCart(String customerId) {
        log.trace(SENDING_CLEAR_CART_REQUEST, customerId);
        kafkaTemplate.send(CLEAR_CART_TOPIC, customerId);
    }
}
