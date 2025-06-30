package dev.williamnogueira.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.williamnogueira.ecommerce.domain.order.OrderService;
import dev.williamnogueira.ecommerce.domain.order.dto.OrderResponseDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static dev.williamnogueira.ecommerce.utils.OrderTestUtils.createOrderResponseDTO;
import static dev.williamnogueira.ecommerce.utils.TestConstants.ID;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    static OrderResponseDTO orderResponseDTO;

    @BeforeAll
    static void setUp() {
        orderResponseDTO = createOrderResponseDTO();
    }

    @Test
    @DisplayName("POST /api/order/{customerId} should return 201 Created")
    void checkoutShouldReturnCreated() throws Exception {
        // Arrange
        when(orderService.purchaseShoppingCart(ID)).thenReturn(orderResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/order/{customerId}", ID))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", is(orderResponseDTO.id().toString())));
    }

    @Test
    @DisplayName("GET /api/order/customer/{customerId} should return 200 OK")
    void findOrderByCustomerIdShouldReturnOk() throws Exception {
        // Arrange
        Page<OrderResponseDTO> orderPage = new PageImpl<>(List.of(orderResponseDTO));
        when(orderService.findAllByCustomerId(eq(ID), any(Pageable.class))).thenReturn(orderPage);

        // Act & Assert
        mockMvc.perform(get("/api/order/customer/{customerId}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/order/{id} should return 200 OK")
    void findOrderByIdShouldReturnOk() throws Exception {
        // Arrange
        when(orderService.findById(ID)).thenReturn(orderResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/order/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderResponseDTO.id().toString())));
    }
}
