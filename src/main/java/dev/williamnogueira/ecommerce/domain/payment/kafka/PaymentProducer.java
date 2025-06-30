package dev.williamnogueira.ecommerce.domain.payment.kafka;

import dev.williamnogueira.ecommerce.domain.order.OrderService;
import dev.williamnogueira.ecommerce.domain.order.OrderStatusEnum;
import dev.williamnogueira.ecommerce.domain.payment.dto.PaymentProducerResponseDTO;
import dev.williamnogueira.ecommerce.domain.payment.exceptions.PaymentStatusException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.ORDER_STATUS_MUST_BE_PENDING;
import static dev.williamnogueira.ecommerce.infrastructure.constants.KafkaConstants.PAYMENT_REQUEST_IS_NOW_BEING_PROCESSED;
import static dev.williamnogueira.ecommerce.infrastructure.constants.KafkaConstants.PAYMENT_TOPIC;
import static dev.williamnogueira.ecommerce.infrastructure.constants.KafkaConstants.SENDING_PAYMENT_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderService orderService;

    public PaymentProducerResponseDTO sendPaymentRequest(String orderId) {
        var order = orderService.getEntity(UUID.fromString(orderId));

        if (ObjectUtils.notEqual(order.getStatus(), OrderStatusEnum.PENDING)) {
            throw new PaymentStatusException(ORDER_STATUS_MUST_BE_PENDING);
        }

        log.trace(SENDING_PAYMENT_REQUEST, orderId);
        kafkaTemplate.send(PAYMENT_TOPIC, orderId);
        return new PaymentProducerResponseDTO(PAYMENT_REQUEST_IS_NOW_BEING_PROCESSED);
    }
}
