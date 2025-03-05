package dev.williamnogueira.ecommerce.controller;

import dev.williamnogueira.ecommerce.domain.address.AddressService;
import dev.williamnogueira.ecommerce.domain.address.dto.AddressRequestDTO;
import dev.williamnogueira.ecommerce.domain.address.dto.AddressResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponseDTO> create(@RequestBody AddressRequestDTO address) {
        var createdAddress = addressService.create(address);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdAddress.id())
                .toUri();
        return ResponseEntity.created(location).body(createdAddress);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> updateById(@PathVariable UUID id, @RequestBody AddressRequestDTO address) {
        return ResponseEntity.ok(addressService.updateById(id, address));
    }
}
