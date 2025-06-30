package dev.williamnogueira.ecommerce.domain.order.kafka;

import dev.williamnogueira.ecommerce.domain.shoppingcart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static dev.williamnogueira.ecommerce.infrastructure.constants.KafkaConstants.CLEAR_CART_RESPONSE;
import static dev.williamnogueira.ecommerce.infrastructure.constants.KafkaConstants.CLEAR_CART_TOPIC;
import static dev.williamnogueira.ecommerce.infrastructure.constants.KafkaConstants.GROUP_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final ShoppingCartService shoppingCartService;

    @Transactional
    @KafkaListener(topics = CLEAR_CART_TOPIC, groupId = GROUP_ID)
    public void consumePaymentResponse(String customerId) {
        log.trace(CLEAR_CART_RESPONSE, customerId);
        var shoppingCart = shoppingCartService.findByCustomerId(UUID.fromString(customerId));

        shoppingCart.getItems().clear();
        shoppingCart.setTotalPrice(BigDecimal.ZERO);
        shoppingCartService.save(shoppingCart);
    }
}
