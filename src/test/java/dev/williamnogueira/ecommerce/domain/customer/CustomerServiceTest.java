package dev.williamnogueira.ecommerce.domain.customer;

import dev.williamnogueira.ecommerce.domain.address.AddressMapper;
import dev.williamnogueira.ecommerce.domain.customer.dto.CustomerPatchDTO;
import dev.williamnogueira.ecommerce.domain.customer.dto.CustomerRequestDTO;
import dev.williamnogueira.ecommerce.domain.customer.dto.CustomerResponseDTO;
import dev.williamnogueira.ecommerce.domain.customer.exceptions.CustomerAlreadyExistsWithEmail;
import dev.williamnogueira.ecommerce.domain.customer.exceptions.CustomerNotFoundException;
import dev.williamnogueira.ecommerce.domain.shoppingcart.ShoppingCartService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.CUSTOMER_ALREADY_EXISTS_WITH_EMAIL;
import static dev.williamnogueira.ecommerce.utils.CustomerTestUtils.createCustomerEntity;
import static dev.williamnogueira.ecommerce.utils.CustomerTestUtils.createCustomerPatchDTO;
import static dev.williamnogueira.ecommerce.utils.CustomerTestUtils.createCustomerRequestDTO;
import static dev.williamnogueira.ecommerce.utils.CustomerTestUtils.createCustomerResponseDTO;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.CUSTOMER_NOT_FOUND_WITH_ID;
import static dev.williamnogueira.ecommerce.utils.TestConstants.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private CustomerService customerService;

    static CustomerEntity customerEntity;
    static CustomerResponseDTO customerResponseDTO;
    static CustomerRequestDTO customerRequestDTO;
    static CustomerPatchDTO customerPatchDTO;

    @BeforeAll
    static void setUp() {
        customerEntity = createCustomerEntity();
        customerRequestDTO = createCustomerRequestDTO();
        customerResponseDTO = createCustomerResponseDTO();
        customerPatchDTO = createCustomerPatchDTO();
    }

    @Test
    void testFindAll() {
        // arrange
        Page<CustomerEntity> entityPage = new PageImpl<>(List.of(customerEntity));

        when(customerRepository.findAllByActiveTrue(pageable)).thenReturn(entityPage);
        when(customerMapper.toResponseDTO(customerEntity)).thenReturn(customerResponseDTO);

        // act
        Page<CustomerResponseDTO> response = customerService.findAll(pageable);

        // assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1).containsExactly(customerResponseDTO);
        verify(customerRepository).findAllByActiveTrue(pageable);
        verify(customerMapper).toResponseDTO(customerEntity);
    }

    @Test
    void testFindById() {
        // arrange
        when(customerRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(customerEntity));
        when(customerMapper.toResponseDTO(customerEntity)).thenReturn(customerResponseDTO);

        // act
        var response = customerService.findById(ID);

        // assert
        assertThat(response).isNotNull().isEqualTo(customerResponseDTO);
        verify(customerRepository).findByIdAndActiveTrue(ID);
        verify(customerMapper).toResponseDTO(customerEntity);
    }

    @Test
    void testCreate() {
        // arrange
        when(customerMapper.toEntityWithAddresses(customerRequestDTO)).thenReturn(customerEntity);
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerMapper.toResponseDTO(customerEntity)).thenReturn(customerResponseDTO);

        // act
        var response = customerService.create(customerRequestDTO);

        // assert
        assertThat(response).isNotNull().isEqualTo(customerResponseDTO);
        verify(customerRepository).save(customerEntity);
        verify(customerMapper).toEntityWithAddresses(customerRequestDTO);
        verify(customerMapper).toResponseDTO(customerEntity);

    }

    @Test
    void testCreateWithSameEmailThrowsException() {
        // arrange
        when(customerRepository.existsByEmailIgnoreCase(customerRequestDTO.email())).thenReturn(true);

        // act & assert
        assertThatException()
                .isThrownBy(() -> customerService.create(customerRequestDTO))
                .isInstanceOf(CustomerAlreadyExistsWithEmail.class)
                .withMessageContaining(String.format(CUSTOMER_ALREADY_EXISTS_WITH_EMAIL, customerRequestDTO.email()));
        verify(customerRepository).existsByEmailIgnoreCase(customerRequestDTO.email());
    }

    @Test
    void testUpdateById() {
        // arrange
        when(customerRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(customerEntity));
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerMapper.toResponseDTO(customerEntity)).thenReturn(customerResponseDTO);

        // act
        var response = customerService.updateById(ID, customerRequestDTO);

        // assert
        assertThat(response).isNotNull().isEqualTo(customerResponseDTO);
        verify(customerRepository).save(customerEntity);
        verify(customerMapper).toResponseDTO(customerEntity);
    }

    @Test
    void testPatchById() {
        // arrange
        when(customerRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(customerEntity));
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerMapper.toResponseDTO(customerEntity)).thenReturn(customerResponseDTO);

        // act
        var response = customerService.patchById(ID, customerPatchDTO);

        // assert
        assertThat(response).isNotNull().isEqualTo(customerResponseDTO);
        verify(customerRepository).save(customerEntity);
        verify(customerMapper).toResponseDTO(customerEntity);
        verify(customerMapper).patchCustomerFromDto(customerPatchDTO, customerEntity);
    }

    @Test
    void testGetEntity() {
        // arrange
        when(customerRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(customerEntity));

        // act
        var response = customerService.getEntity(ID);

        // assert
        assertThat(response).isNotNull().isEqualTo(customerEntity);
        verify(customerRepository).findByIdAndActiveTrue(ID);
    }

    @Test
    void testGetEntityDoesNotFindEntity() {
        // arrange
        when(customerRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatException()
                .isThrownBy(() -> customerService.getEntity(ID))
                .isInstanceOf(CustomerNotFoundException.class)
                .withMessageContaining(String.format(CUSTOMER_NOT_FOUND_WITH_ID, ID));
        verify(customerRepository).findByIdAndActiveTrue(ID);
    }
}
