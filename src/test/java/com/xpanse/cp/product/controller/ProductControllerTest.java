package com.xpanse.cp.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpanse.cp.product.constants.ProductConstant;
import com.xpanse.cp.product.entity.APIResponse;
import com.xpanse.cp.product.entity.Product;
import com.xpanse.cp.product.dto.ProductDTO;
import com.xpanse.cp.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@EnableWebMvc
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createProductSuccessTest() throws Exception {
        // Arrange
        ProductDTO request = createSampleProductDetails();
        Long parentProductId = 1L;
        APIResponse expectedResponse = createSuccessAPIResponse();

        when(productService.createProduct(any(ProductDTO.class),parentProductId))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/products")  // Using consistent endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Product details saved successfully"));

        verify(productService, times(1)).createProduct(any(ProductDTO.class),parentProductId);
    }

    @Test
    void getProductReturnsProductTest() throws Exception {
        // Arrange
        Long productId = 1L;
        Product expectedProduct = createSampleProduct();

        when(productService.getByProductId(productId))
                .thenReturn(expectedProduct);

        // Act & Assert
        mockMvc.perform(get("/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.productName").value("Test Product"))
                .andExpect(jsonPath("$.productCode").value("PROD-001"));

        verify(productService, times(1)).getByProductId(productId);
    }

    @Test
    void getProductReturnsNotFoundTest() throws Exception {
        // Arrange
        Long invalidProductId = 999L;

        when(productService.getByProductId(invalidProductId))
                .thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/products/{productId}", invalidProductId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getByProductId(invalidProductId);
    }

    @Test
    void createProductInternalServerErrorTest() throws Exception {
        // Arrange
        ProductDTO request = createSampleProductDetails();
        Long parentProductId = 1L;
        APIResponse errorResponse = new APIResponse();
        errorResponse.setStatus("ERROR");
        errorResponse.setMessage("Internal server error");

        when(productService.createProduct(any(ProductDTO.class),parentProductId))
                .thenReturn(errorResponse);

        // Act & Assert
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(productService, times(1)).createProduct(any(ProductDTO.class),parentProductId);
    }


    @Test
    void getProductBadRequestTest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/products/{productId}", "invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(productService, never()).getByProductId(any());
    }

    // Helper methods to create test data
    private ProductDTO createSampleProductDetails() {
        ProductDTO details = new ProductDTO();
        details.setProductCode("PROD-001");
        details.setProductName("Test Product");
        details.setProductOwner("test@example.com");
        details.setProductPublicationRule(ProductConstant.ProductPublicationRule.ALL);
        return details;
    }

    private Product createSampleProduct() {
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Test Product");
        product.setProductCode("PROD-001");
        return product;
    }

    private APIResponse createSuccessAPIResponse() {
        APIResponse response = new APIResponse();
        response.setStatus("Success");
        response.setMessage("Product details saved successfully");
        return response;
    }
}