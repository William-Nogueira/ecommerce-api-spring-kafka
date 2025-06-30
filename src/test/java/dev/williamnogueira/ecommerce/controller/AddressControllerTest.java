package dev.williamnogueira.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.williamnogueira.ecommerce.domain.address.AddressEntity;
import dev.williamnogueira.ecommerce.domain.address.AddressService;
import dev.williamnogueira.ecommerce.domain.address.dto.AddressRequestDTO;
import dev.williamnogueira.ecommerce.domain.address.dto.AddressResponseDTO;
import dev.williamnogueira.ecommerce.domain.customer.CustomerEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static dev.williamnogueira.ecommerce.utils.AddressTestUtils.createAddressEntity;
import static dev.williamnogueira.ecommerce.utils.AddressTestUtils.createAddressRequestDTO;
import static dev.williamnogueira.ecommerce.utils.AddressTestUtils.createAddressResponseDTO;
import static dev.williamnogueira.ecommerce.utils.CustomerTestUtils.createCustomerEntity;
import static dev.williamnogueira.ecommerce.utils.TestConstants.ID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    static AddressEntity addressEntity;
    static AddressResponseDTO addressResponseDTO;
    static AddressRequestDTO addressRequestDTO;
    static CustomerEntity customerEntity;

    @BeforeAll
    static void setUp() {
        addressEntity = createAddressEntity();
        addressRequestDTO = createAddressRequestDTO();
        addressResponseDTO = createAddressResponseDTO();
        customerEntity = createCustomerEntity();
    }

    @Test
    @DisplayName("POST /api/address should return 201 Created with Location header")
    void createAddressShouldReturnCreated() throws Exception {
        // Arrange
        when(addressService.create(addressRequestDTO)).thenReturn(addressResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(addressResponseDTO.id().toString()));
    }

    @Test
    @DisplayName("PUT /api/address/{id} should return 200 OK with updated address")
    void updateAddressShouldReturnOk() throws Exception {
        // Arrange
        when(addressService.updateById(ID, addressRequestDTO)).thenReturn(addressResponseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/address/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(addressResponseDTO.id().toString()));
    }
}
