package dev.williamnogueira.ecommerce.controller;

import dev.williamnogueira.ecommerce.domain.customer.CustomerService;
import dev.williamnogueira.ecommerce.domain.customer.dto.CustomerPatchDTO;
import dev.williamnogueira.ecommerce.domain.customer.dto.CustomerRequestDTO;
import dev.williamnogueira.ecommerce.domain.customer.dto.CustomerResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(customerService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDTO customer) {
        var createdCustomer = customerService.create(customer);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCustomer.id())
                .toUri();
        return ResponseEntity.created(location).body(createdCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateById(@PathVariable UUID id,
                                                          @Valid @RequestBody CustomerRequestDTO customer) {
        return ResponseEntity.ok(customerService.updateById(id, customer));
    }

    @PatchMapping("{id}")
    public ResponseEntity<CustomerResponseDTO> patchById(@PathVariable UUID id,
                                                         @RequestBody CustomerPatchDTO customer) {
        return ResponseEntity.ok(customerService.patchById(id, customer));
    }
}
