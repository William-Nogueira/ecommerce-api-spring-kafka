package dev.williamnogueira.ecommerce.domain.product;

import dev.williamnogueira.ecommerce.domain.product.dto.ProductRequestDTO;
import dev.williamnogueira.ecommerce.domain.product.dto.ProductResponseDTO;
import dev.williamnogueira.ecommerce.domain.product.exceptions.DuplicateProductException;
import dev.williamnogueira.ecommerce.domain.product.exceptions.InvalidCategoryException;
import dev.williamnogueira.ecommerce.domain.product.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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

import static dev.williamnogueira.ecommerce.utils.ProductTestUtils.createInvalidProductRequestDTO;
import static dev.williamnogueira.ecommerce.utils.ProductTestUtils.createProductEntity;
import static dev.williamnogueira.ecommerce.utils.ProductTestUtils.createProductRequestDTO;
import static dev.williamnogueira.ecommerce.utils.ProductTestUtils.createProductResponseDTO;
import static dev.williamnogueira.ecommerce.utils.ProductTestUtils.createProductWithNewSku;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.INVALID_CATEGORY;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.PRODUCT_NOT_FOUND_WITH_ID;
import static dev.williamnogueira.ecommerce.infrastructure.constants.ErrorMessages.SKU_ALREADY_EXISTS;
import static dev.williamnogueira.ecommerce.utils.TestConstants.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private ProductService productService;

    static ProductEntity productEntity;
    static ProductResponseDTO productResponseDTO;
    static ProductRequestDTO productRequestDTO;

    @BeforeEach
    void setUp() {
        productEntity = createProductEntity();
        productRequestDTO = createProductRequestDTO();
        productResponseDTO = createProductResponseDTO();
    }

    @Test
    void testFindAll() {
        // arrange
        Page<ProductEntity> entityPage = new PageImpl<>(List.of(productEntity));
        when(productRepository.findAllByActiveTrue(pageable)).thenReturn(entityPage);
        when(productMapper.toResponseDTO(productEntity)).thenReturn(productResponseDTO);

        // act
        Page<ProductResponseDTO> response = productService.findAll(pageable);

        // assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).containsExactly(productResponseDTO);
        verify(productRepository).findAllByActiveTrue(pageable);
        verify(productMapper).toResponseDTO(productEntity);
    }

    @Test
    void testFindById() {
        // arrange
        when(productRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(productEntity));
        when(productMapper.toResponseDTO(productEntity)).thenReturn(productResponseDTO);

        // act
        var response = productService.findById(ID);

        // assert
        assertThat(response).isNotNull().isEqualTo(productResponseDTO);
        verify(productRepository).findByIdAndActiveTrue(ID);
        verify(productMapper).toResponseDTO(productEntity);
    }

    @Test
    void testFindByCategory() {
        // arrange
        Page<ProductEntity> entityPage = new PageImpl<>(List.of(productEntity));
        when(productRepository.findAllByCategoryIgnoreCaseAndActiveTrue(ProductCategoryEnum.CPU, pageable))
                .thenReturn(entityPage);
        when(productMapper.toResponseDTO(productEntity)).thenReturn(productResponseDTO);

        // act
        Page<ProductResponseDTO> response = productService.findAllByCategory(ProductCategoryEnum.CPU.name(), pageable);

        // assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).containsExactly(productResponseDTO);
        verify(productRepository).findAllByCategoryIgnoreCaseAndActiveTrue(ProductCategoryEnum.CPU, pageable);
        verify(productMapper).toResponseDTO(productEntity);
    }

    @Test
    void testFindAllByLabel() {
        // arrange
        Page<ProductEntity> entityPage = new PageImpl<>(List.of(productEntity));
        when(productRepository.findAllByLabelIgnoreCaseAndActiveTrue(productRequestDTO.label(), pageable))
                .thenReturn(entityPage);
        when(productMapper.toResponseDTO(productEntity)).thenReturn(productResponseDTO);

        // act
        Page<ProductResponseDTO> response = productService.findAllByLabel(productRequestDTO.label(), pageable);

        // assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).containsExactly(productResponseDTO);
        verify(productRepository).findAllByLabelIgnoreCaseAndActiveTrue(productRequestDTO.label(), pageable);
        verify(productMapper).toResponseDTO(productEntity);
    }

    @Test
    void testCreate() {
        // arrange
        when(productRepository.existsBySku(productRequestDTO.sku())).thenReturn(false);
        when(productMapper.toEntity(productRequestDTO)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(productMapper.toResponseDTO(productEntity)).thenReturn(productResponseDTO);

        // act
        var response = productService.create(productRequestDTO);

        // assert
        assertThat(response).isNotNull().isEqualTo(productResponseDTO);
        verify(productRepository).save(productEntity);
        verify(productRepository).existsBySku(productRequestDTO.sku());
        verify(productMapper).toEntity(productRequestDTO);
        verify(productMapper).toResponseDTO(productEntity);
    }

    @Test
    void testCreateInvalidCategory() {
        // arrange
        var productRequestDTOInvalidCategory = createInvalidProductRequestDTO();

        // act & assert
        assertThatException()
                .isThrownBy(() -> productService.create(productRequestDTOInvalidCategory))
                .isInstanceOf(InvalidCategoryException.class)
                .withMessageContaining(String.format(INVALID_CATEGORY, productRequestDTOInvalidCategory.category()));
        verify(productRepository).existsBySku(productRequestDTOInvalidCategory.sku());
    }

    @Test
    void testCreateDuplicateSku() {
        // arrange
        when(productRepository.existsBySku(productRequestDTO.sku())).thenReturn(true);

        // act & assert
        assertThatException()
                .isThrownBy(() -> productService.create(productRequestDTO))
                .isInstanceOf(DuplicateProductException.class)
                .withMessageContaining(SKU_ALREADY_EXISTS);
        verify(productRepository).existsBySku(productRequestDTO.sku());
    }

    @Test
    void testUpdateById() {
        // arrange
        when(productRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(productEntity));
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(productMapper.toResponseDTO(productEntity)).thenReturn(productResponseDTO);

        // act
        var response = productService.updateById(ID, productRequestDTO);

        // assert
        assertThat(response).isNotNull().isEqualTo(productResponseDTO);
        verify(productRepository).save(productEntity);
    }

    @Test
    void testUpdateByIdWithNewSku() {
        // arrange
        var productRequestDTOWithNewSku = createProductWithNewSku();
        when(productRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(productEntity));
        when(productRepository.existsBySku(productRequestDTOWithNewSku.sku())).thenReturn(false);
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(productMapper.toResponseDTO(productEntity)).thenReturn(productResponseDTO);

        // act
        var response = productService.updateById(ID, productRequestDTOWithNewSku);

        // assert
        assertThat(response).isNotNull().isEqualTo(productResponseDTO);
        verify(productRepository).save(productEntity);
    }

    @Test
    void testUpdateByIdSkuAlreadyExists() {
        // arrange
        var productRequestDTOWithNewSku = createProductWithNewSku();
        when(productRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(productEntity));
        when(productRepository.existsBySku(productRequestDTOWithNewSku.sku())).thenReturn(true);

        // act & assert
        assertThatException()
                .isThrownBy(() -> productService.updateById(ID, productRequestDTOWithNewSku))
                .isInstanceOf(DuplicateProductException.class)
                .withMessageContaining(SKU_ALREADY_EXISTS);
        verify(productRepository).existsBySku(productRequestDTOWithNewSku.sku());
    }

    @Test
    void testDeleteById() {
        // arrange
        when(productRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(productEntity));

        // act
        productService.deleteById(ID);

        assertThat(productEntity.getActive()).isFalse();
        verify(productRepository).save(productEntity);
    }

    @Test
    void testGetEntity() {
        // arrange
        when(productRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(productEntity));

        // act
        var response = productService.getEntity(ID);

        // assert
        assertThat(response).isNotNull().isEqualTo(productEntity);
    }

    @Test
    void testGetEntityDoesNotFindEntity() {
        // arrange
        when(productRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.empty());

        // act & assert
        assertThatException()
                .isThrownBy(() -> productService.getEntity(ID))
                .isInstanceOf(ProductNotFoundException.class)
                .withMessageContaining(String.format(PRODUCT_NOT_FOUND_WITH_ID, ID));
    }

    @Test
    void testAddStockById() {
        // arrange
        when(productRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(productEntity));
        when(productMapper.toResponseDTO(productEntity)).thenReturn(productResponseDTO);

        // act
        var response = productService.addStockById(ID, 10);

        // assert
        assertThat(response).isNotNull().isEqualTo(productResponseDTO);
        verify(productRepository).save(productEntity);
    }

    @Test
    void testSubtractStockQuantity() {
        // arrange
        when(productRepository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(productEntity));

        // act
        productService.subtractStockQuantity(ID, 8);

        // assert
        assertThat(productEntity.getStockQuantity()).isEqualTo(2);
        verify(productRepository).save(productEntity);
    }
}
