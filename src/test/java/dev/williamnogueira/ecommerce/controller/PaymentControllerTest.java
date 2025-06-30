package dev.williamnogueira.ecommerce.controller;

import dev.williamnogueira.ecommerce.domain.payment.dto.PaymentProducerResponseDTO;
import dev.williamnogueira.ecommerce.domain.payment.kafka.PaymentProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static dev.williamnogueira.ecommerce.utils.TestConstants.ID;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentProducer paymentProducer;

    @Test
    @DisplayName("POST /api/payment/{orderId} should return 200 OK")
    void processPaymentShouldReturnOk() throws Exception {
        // Arrange
        var message = "Payment request sent";
        var responseDTO = new PaymentProducerResponseDTO(message);
        when(paymentProducer.sendPaymentRequest(ID.toString())).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/payment/{orderId}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(message)));
    }
}
