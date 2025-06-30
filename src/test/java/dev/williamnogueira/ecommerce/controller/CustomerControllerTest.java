package dev.williamnogueira.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.williamnogueira.ecommerce.domain.customer.CustomerService;
import dev.williamnogueira.ecommerce.domain.customer.dto.CustomerPatchDTO;
import dev.williamnogueira.ecommerce.domain.customer.dto.CustomerRequestDTO;
import dev.williamnogueira.ecommerce.domain.customer.dto.CustomerResponseDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static dev.williamnogueira.ecommerce.utils.CustomerTestUtils.*;
import static dev.williamnogueira.ecommerce.utils.TestConstants.ID;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    static CustomerRequestDTO customerRequestDTO;
    static CustomerResponseDTO customerResponseDTO;
    static CustomerPatchDTO customerPatchDTO;

    @BeforeAll
    static void setUp() {
        customerRequestDTO = createCustomerRequestDTO();
        customerResponseDTO = createCustomerResponseDTO();
        customerPatchDTO = createCustomerPatchDTO();
    }

    @Test
    @DisplayName("POST /api/customer should return 201 Created")
    void createCustomerShouldReturnCreated() throws Exception {
        // Arrange
        when(customerService.create(any(CustomerRequestDTO.class))).thenReturn(customerResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", is(customerResponseDTO.id().toString())))
                .andExpect(jsonPath("$.name", is(customerResponseDTO.name())));
    }

    @Test
    @DisplayName("GET /api/customer/{id} should return 200 OK")
    void findCustomerByIdShouldReturnOk() throws Exception {
        // Arrange
        when(customerService.findById(ID)).thenReturn(customerResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/customer/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(customerResponseDTO.id().toString())));
    }

    @Test
    @DisplayName("GET /api/customer should return 200 OK with a page of customers")
    void findAllCustomersShouldReturnPage() throws Exception {
        // Arrange
        Page<CustomerResponseDTO> customerPage = new PageImpl<>(List.of(customerResponseDTO), PageRequest.of(0, 1), 1);
        when(customerService.findAll(any(PageRequest.class))).thenReturn(customerPage);

        // Act & Assert
        mockMvc.perform(get("/api/customer")
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(customerResponseDTO.id().toString())))
                .andExpect(jsonPath("$.totalPages", is(1)));
    }


    @Test
    @DisplayName("PUT /api/customer/{id} should return 200 OK")
    void updateCustomerShouldReturnOk() throws Exception {
        // Arrange
        when(customerService.updateById(ID, customerRequestDTO)).thenReturn(customerResponseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/customer/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(customerResponseDTO.id().toString())));
    }

    @Test
    @DisplayName("PATCH /api/customer/{id} should return 200 OK")
    void patchCustomerShouldReturnOk() throws Exception {
        // Arrange
        when(customerService.patchById(ID, customerPatchDTO)).thenReturn(customerResponseDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/customer/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerPatchDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(customerResponseDTO.id().toString())));
    }
}
