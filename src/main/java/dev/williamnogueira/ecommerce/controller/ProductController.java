package dev.williamnogueira.ecommerce.controller;

import dev.williamnogueira.ecommerce.domain.product.dto.ProductRequestDTO;
import dev.williamnogueira.ecommerce.domain.product.dto.ProductResponseDTO;
import dev.williamnogueira.ecommerce.domain.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> findAll(Pageable pageable) {
        var response = productService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ProductResponseDTO>> findByCategory(@PathVariable("category") String category,
                                                                   Pageable pageable) {
        return ResponseEntity.ok(productService.findAllByCategory(category, pageable));
    }

    @GetMapping("/label/{label}")
    public ResponseEntity<Page<ProductResponseDTO>> findByLabel(@PathVariable("label") String label,
                                                                Pageable pageable) {
        return ResponseEntity.ok(productService.findAllByLabel(label, pageable));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO product) {
        var createdProduct = productService.create(product);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.id())
                .toUri();
        return ResponseEntity.created(location).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateById(@PathVariable UUID id,
                                                         @Valid @RequestBody ProductRequestDTO product) {
        return ResponseEntity.ok(productService.updateById(id, product));
    }

    @PatchMapping("/stock/{id}/{quantity}")
    public ResponseEntity<ProductResponseDTO> addStockById(@PathVariable UUID id,
                                                           @PathVariable Integer quantity) {
        return ResponseEntity.ok(productService.addStockById(id, quantity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
