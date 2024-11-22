package com.xpanse.cp.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xpanse.cp.product.entity.Product;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByProductCode(String productCode);
    List<Product> findByProductEndDateIsNullOrProductEndDateAfter(Date endDate);
    List<Product> findByParentProductId(Long parentProductId);
    Product findByProductCode(String productCode);
    List<Product> findByProductEndDateIsNullOrProductEndDateAfterAndParentProductId(Date endDate, Long parentProductId);
    List<Product> findByProductEndDateIsNullOrProductEndDateAfterAndProductIdIn(Date endDate, List<Long> productIds);
    List<Product> findByProductIdIn(List<Long> productIds);

}
