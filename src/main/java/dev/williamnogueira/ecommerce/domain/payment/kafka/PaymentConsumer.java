package dev.williamnogueira.ecommerce.domain.payment.kafka;

import dev.williamnogueira.ecommerce.domain.order.OrderService;
import dev.williamnogueira.ecommerce.domain.order.OrderStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static dev.williamnogueira.ecommerce.infrastructure.constants.KafkaConstants.GROUP_ID;
import static dev.williamnogueira.ecommerce.infrastructure.constants.KafkaConstants.PAYMENT_RESPONSE_TOPIC;
import static dev.williamnogueira.ecommerce.infrastructure.constants.KafkaConstants.RECEIVED_PAYMENT_RESPONSE;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = PAYMENT_RESPONSE_TOPIC, groupId = GROUP_ID)
    public void consumePaymentResponse(String orderId) {
        log.trace(RECEIVED_PAYMENT_RESPONSE, orderId);
        orderService.updateStatus(orderId, OrderStatusEnum.PAID);
    }
}