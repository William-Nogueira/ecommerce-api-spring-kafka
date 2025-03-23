package dev.williamnogueira.ecommerce.domain.product;

import dev.williamnogueira.ecommerce.domain.product.exceptions.DuplicateProductException;
import dev.williamnogueira.ecommerce.domain.product.exceptions.InvalidCategoryException;
import dev.williamnogueira.ecommerce.domain.product.exceptions.ProductNotFoundException;
import dev.williamnogueira.ecommerce.domain.product.dto.ProductRequestDTO;
import dev.williamnogueira.ecommerce.domain.product.dto.ProductResponseDTO;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.INVALID_CATEGORY;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.PRODUCT_NOT_FOUND_WITH_ID;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.SKU_ALREADY_EXISTS;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductResponseDTO> findAll(Pageable pageable) {
        return productRepository.findAllByActiveTrue(pageable).map(productMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#id")
    public ProductResponseDTO findById(UUID id) {
        return productMapper.toResponseDTO(getEntity(id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'category-' + #category + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductResponseDTO> findAllByCategory(String category, Pageable pageable) {
        return productRepository.findAllByCategoryIgnoreCaseAndActiveTrue(validateCategory(category), pageable)
                .map(productMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'label-' + #label + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductResponseDTO> findAllByLabel(String label, Pageable pageable) {
        return productRepository.findAllByLabelIgnoreCaseAndActiveTrue(label, pageable)
                .map(productMapper::toResponseDTO);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponseDTO create(ProductRequestDTO product) {

        if (verifyIfThisSkuAlreadyExists(product.sku())) {
            throw new DuplicateProductException(SKU_ALREADY_EXISTS);
        }

        validateCategory(product.category());

        return productMapper.toResponseDTO(productRepository.save(productMapper.toEntity(product)));
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public ProductResponseDTO updateById(UUID id, ProductRequestDTO product) {

        var entity = getEntity(id);

        if (ObjectUtils.notEqual(entity.getSku(), product.sku()) && verifyIfThisSkuAlreadyExists(product.sku())) {
            throw new DuplicateProductException(SKU_ALREADY_EXISTS);
        }

        validateCategory(product.category());
        updateEntityFields(entity, product);

        return productMapper.toResponseDTO(productRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public void deleteById(UUID id) {
        var product = getEntity(id);

        product.setActive(false);
        productRepository.save(product);
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public ProductResponseDTO addStockById(UUID id, Integer quantity) {
        var product = getEntity(id);
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);
        return productMapper.toResponseDTO(product);
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public void subtractStockQuantity(UUID id, Integer quantity) {
        var product = getEntity(id);
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }

    public ProductEntity getEntity(UUID id) {
        return productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID, id)));
    }

    private void updateEntityFields(ProductEntity entity, ProductRequestDTO product) {
        entity.setSku(product.sku());
        entity.setName(product.name());
        entity.setLabel(product.label());
        entity.setCategory(validateCategory(product.category()));
        entity.setPrice(product.price());
        entity.setDiscount(product.discount());
        entity.setInstallments(product.installments());
    }

    private ProductCategoryEnum validateCategory(String category) {
        try {
            return ProductCategoryEnum.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCategoryException(String.format(INVALID_CATEGORY, category));
        }
    }

    private boolean verifyIfThisSkuAlreadyExists(String sku) {
        return productRepository.existsBySku(sku);
    }

}
