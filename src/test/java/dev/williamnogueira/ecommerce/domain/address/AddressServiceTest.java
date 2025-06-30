package dev.williamnogueira.ecommerce.domain.address;

import dev.williamnogueira.ecommerce.domain.address.dto.AddressRequestDTO;
import dev.williamnogueira.ecommerce.domain.address.dto.AddressResponseDTO;
import dev.williamnogueira.ecommerce.domain.address.exceptions.AddressNotFoundException;
import dev.williamnogueira.ecommerce.domain.customer.CustomerService;
import dev.williamnogueira.ecommerce.domain.customer.CustomerEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dev.williamnogueira.ecommerce.utils.AddressTestUtils.*;
import static dev.williamnogueira.ecommerce.utils.CustomerTestUtils.createCustomerEntity;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.ADDRESS_NOT_FOUND_WITH_ID;
import static dev.williamnogueira.ecommerce.utils.TestConstants.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AddressService addressService;

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
    void testCreate() {
        // arrange
        when(customerService.getEntity(addressRequestDTO.customer())).thenReturn(customerEntity);
        when(addressMapper.toEntity(addressRequestDTO)).thenReturn(addressEntity);
        when(addressRepository.save(addressEntity)).thenReturn(addressEntity);
        when(addressMapper.toResponseDTO(addressEntity)).thenReturn(addressResponseDTO);

        // act
        var response = addressService.create(addressRequestDTO);

        // assert
        assertThat(response).isNotNull().isEqualTo(addressResponseDTO);
        verify(addressRepository).save(addressEntity);
        verify(addressMapper).toEntity(addressRequestDTO);
        verify(addressMapper).toResponseDTO(addressEntity);
    }

    @Test
    void testUpdateById() {
        // arrange
        when(addressRepository.findById(ID)).thenReturn(Optional.of(addressEntity));
        when(addressRepository.save(addressEntity)).thenReturn(addressEntity);
        when(addressMapper.toResponseDTO(addressEntity)).thenReturn(addressResponseDTO);

        // act
        var response = addressService.updateById(ID, addressRequestDTO);

        // assert
        assertThat(response).isNotNull().isEqualTo(addressResponseDTO);
        verify(addressRepository).save(addressEntity);
        verify(addressMapper).toResponseDTO(addressEntity);
    }

    @Test
    void testGetEntity() {
        // arrange
        when(addressRepository.findById(ID)).thenReturn(Optional.of(addressEntity));

        // act
        var response = addressService.getEntity(ID);

        // assert
        assertThat(response).isNotNull().isEqualTo(addressEntity);
        verify(addressRepository).findById(ID);
    }

    @Test
    void testGetEntityDoesNotFindEntity() {
        // arrange
        when(addressRepository.findById(ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatException()
                .isThrownBy(() -> addressService.getEntity(ID))
                .isInstanceOf(AddressNotFoundException.class)
                .withMessageContaining(String.format(ADDRESS_NOT_FOUND_WITH_ID, ID));
        verify(addressRepository).findById(ID);
    }
}
