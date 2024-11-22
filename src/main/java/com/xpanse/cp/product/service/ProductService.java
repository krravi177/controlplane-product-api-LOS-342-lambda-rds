package com.xpanse.cp.product.service;

import com.xpanse.cp.product.entity.APIResponse;
import com.xpanse.cp.product.entity.Product;
import com.xpanse.cp.product.exception.ProductNotFoundException;
import com.xpanse.cp.product.dto.ProductDTO;
import java.util.List;

import java.util.List;

public interface ProductService {

    APIResponse createProduct(ProductDTO request,Long parentProductId);

    Product getByProductId(Long productId) throws ProductNotFoundException;
    String updateProducts(List<ProductDTO> products, Long productId) throws ProductNotFoundException;
    String deleteProduct(Long productId) throws ProductNotFoundException;

    List<ProductDTO> getProducts(Long parentProductId, Long tenantId, String includeInactive) throws ProductNotFoundException;

}

