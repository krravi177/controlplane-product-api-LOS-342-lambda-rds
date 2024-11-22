package com.xpanse.cp.product.service;

import com.xpanse.cp.product.constants.ProductConstant;
import com.xpanse.cp.product.entity.APIResponse;
import com.xpanse.cp.product.entity.Product;
import com.xpanse.cp.product.exception.DuplicateProductException;
import com.xpanse.cp.product.exception.InvalidRequestException;
import com.xpanse.cp.product.exception.ProductNotFoundException;
import com.xpanse.cp.product.dto.ProductDTO;
import com.xpanse.cp.product.dto.TenantDTO;
import com.xpanse.cp.product.repository.ProductRepository;
import com.xpanse.cp.product.repository.TenantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private ModelMapper modelMapper;

    private ProductDTO validProductDetails;
    private Product validProduct;

    @BeforeEach
    void setUp() {
        // Setup valid product details
        validProductDetails = new ProductDTO();
        validProductDetails.setProductCode("TEST001");
        validProductDetails.setProductName("Test Product");
        validProductDetails.setProductOwner("test@example.com");
        //validProductDetails.setExpirationDate(String.valueOf(LocalDate.now().plusMonths(1)));
        validProductDetails.setProductPublicationRule(ProductConstant.ProductPublicationRule.ALL);

        // Setup valid product
        validProduct = new Product();
        validProduct.setProductId(1L);
        validProduct.setProductCode("TEST001");
        validProduct.setProductName("Test Product");
        validProduct.setProductOwner("test@example.com");
        //validProduct.setExpirationDate(LocalDate.now().plusMonths(1));
        validProduct.setProductPublicationRule(ProductConstant.ProductPublicationRule.ALL);
    }


    @AfterEach
    void tearDown() throws Exception {
        Mockito.reset(productRepository);
        Mockito.reset(tenantRepository);
        Mockito.reset(modelMapper);
    }
    @Test
    void createProductSuccessTest() {
        // Arrange
        when(productRepository.existsByProductCode(anyString())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(validProduct);
        Product product = populateProduct(validProductDetails);
        when(modelMapper.map(validProductDetails, Product.class)).thenReturn(product);
        Long parentProductId = 1L;

        // Act
        APIResponse response = productService.createProduct(validProductDetails,parentProductId);

        // Assert
        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        assertEquals(ProductConstant.SUCCESS_MESSAGE, response.getMessage());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProductWithSpecificTenantSuccessTest() {
        // Arrange
        List<TenantDTO> tenants = new ArrayList<>(Arrays.asList(
                new TenantDTO(1L),
                new TenantDTO(2L)
        ));
        validProductDetails.setProductPublicationRule(ProductConstant.ProductPublicationRule.SPECIFIC);
        validProductDetails.setTenants(tenants);

        Product product = populateProduct(validProductDetails);
        when(modelMapper.map(validProductDetails, Product.class)).thenReturn(product);
        when(productRepository.existsByProductCode(anyString())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(validProduct);
        Long parentProductId = 1L;
        // Act
        APIResponse response = productService.createProduct(validProductDetails,parentProductId);

        // Assert
        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProductWithSpecificAndNoTenantsInvalidRequestExceptionTest() {
        // Arrange
        validProductDetails.setProductPublicationRule(ProductConstant.ProductPublicationRule.SPECIFIC);
        List<TenantDTO> tenants = new ArrayList<>();
        validProductDetails.setTenants(tenants);
        Long parentProductId = 1L;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () ->
                productService.createProduct(validProductDetails,parentProductId));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProductWithDuplicateProductIdentifierTest() {
        // Arrange
        when(productRepository.existsByProductCode(anyString())).thenReturn(true);
        Long parentProductId = 1L;
        // Act & Assert
        assertThrows(DuplicateProductException.class, () ->
                productService.createProduct(validProductDetails,null));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getByProductIdSuccessTest() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(validProduct));

        // Act
        Product result = productService.getByProductId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(validProduct.getProductId(), result.getProductId());
        assertEquals(validProduct.getProductCode(), result.getProductCode());
    }

    @Test
    void getByProductIdWithInvalidIdTest() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () ->
                productService.getByProductId(999L));
    }

    @Test
    void createProductWithNonSpecificTenantTest() {
        // Arrange
        List<TenantDTO> tenants = new ArrayList<>(Arrays.asList(
                new TenantDTO(1L),
                new TenantDTO(2L)
        ));
        validProductDetails.setProductPublicationRule(ProductConstant.ProductPublicationRule.ALL);
        validProductDetails.setTenants(tenants);

        Product product = populateProduct(validProductDetails);
        when(modelMapper.map(validProductDetails, Product.class)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(validProduct);

        // Act
        APIResponse response = productService.createProduct(validProductDetails,null);

        // Assert
        assertNotNull(response);
        assertEquals("Success", response.getStatus());
        /*verify(productRepository).save(argThat(productDetail ->
                productDetail.getTenants() == null
        ));*/
    }

    private Product populateProduct(ProductDTO validProductDetails) {
        Product product = new Product();
        BeanUtils.copyProperties(validProductDetails, product);
        /*List<Publication> tenants = new ArrayList<>();
        validProductDetails.getTenants().forEach(tenantDetail -> {
            Publication tenant = new Publication();
            BeanUtils.copyProperties(tenantDetail, tenant);
            tenants.add(tenant);
        });*/
        //product.setTenants(tenants);
        return product;
    }
}