package dev.williamnogueira.ecommerce.domain.customer;

import dev.williamnogueira.ecommerce.domain.address.AddressMapper;
import dev.williamnogueira.ecommerce.domain.customer.dto.CustomerPatchDTO;
import dev.williamnogueira.ecommerce.domain.customer.dto.CustomerRequestDTO;
import dev.williamnogueira.ecommerce.domain.customer.dto.CustomerResponseDTO;
import dev.williamnogueira.ecommerce.domain.customer.exceptions.CustomerAlreadyExistsWithEmail;
import dev.williamnogueira.ecommerce.domain.customer.exceptions.CustomerNotFoundException;
import dev.williamnogueira.ecommerce.domain.shoppingcart.ShoppingCartEntity;
import dev.williamnogueira.ecommerce.domain.shoppingcart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.CUSTOMER_ALREADY_EXISTS_WITH_EMAIL;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.CUSTOMER_NOT_FOUND_WITH_ID;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressMapper addressMapper;
    private final ShoppingCartService shoppingCartService;

    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> findAll(Pageable pageable) {
        return customerRepository.findAllByActiveTrue(pageable).map(customerMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO findById(UUID id) {
        var entity = getEntity(id);
        return customerMapper.toResponseDTO(entity);
    }

    @Transactional
    public CustomerResponseDTO create(CustomerRequestDTO customer) {

        if (customerRepository.existsByEmailIgnoreCase(customer.email())) {
            throw new CustomerAlreadyExistsWithEmail(String.format(CUSTOMER_ALREADY_EXISTS_WITH_EMAIL, customer.email()));
        }

        var entity = customerRepository.save(customerMapper.toEntityWithAddresses(customer));

        shoppingCartService.save(ShoppingCartEntity.builder()
                .customer(entity)
                .items(new ArrayList<>())
                .totalPrice(BigDecimal.ZERO)
                .build());

        return customerMapper.toResponseDTO(entity);
    }

    @Transactional
    public CustomerResponseDTO updateById(UUID id, CustomerRequestDTO customer) {
        var entity = getEntity(id);

        if (!equalsIgnoreCase(entity.getEmail(), customer.email()) && customerRepository.existsByEmailIgnoreCase(customer.email())) {
            throw new CustomerAlreadyExistsWithEmail(String.format(CUSTOMER_ALREADY_EXISTS_WITH_EMAIL, customer.email()));
        }

        updateEntityFields(entity, customer);

        return customerMapper.toResponseDTO(customerRepository.save(entity));
    }

    @Transactional
    public CustomerResponseDTO patchById(UUID id, CustomerPatchDTO customer) {
        var entity = getEntity(id);

        if (!equalsIgnoreCase(entity.getEmail(), customer.email()) && customerRepository.existsByEmailIgnoreCase(customer.email())) {
            throw new CustomerAlreadyExistsWithEmail(String.format(CUSTOMER_ALREADY_EXISTS_WITH_EMAIL, customer.email()));
        }

        customerMapper.patchCustomerFromDto(customer, entity);

        return customerMapper.toResponseDTO(customerRepository.save(entity));
    }

    public CustomerEntity getEntity(UUID id) {
        return customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND_WITH_ID, id)));
    }

    private void updateEntityFields(CustomerEntity entity, CustomerRequestDTO customer) {
        entity.setName(customer.name());
        entity.setEmail(customer.email());
        entity.setPhoneNumber(customer.phoneNumber());
        entity.setAddress(customer.address().stream().map(addressMapper::toEntity).toList());
        if (nonNull(customer.active())) {
            entity.setActive(customer.active());
        }
    }
}
