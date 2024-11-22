package com.xpanse.cp.product.repository;

import com.xpanse.cp.product.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    List<Publication> findByProductProductId(Long productId);
    boolean existsByProductProductIdAndTenantID(Long productId, Long tenantId);
    List<Publication> findByTenantID(Long tenantId);
    Publication findByProductProductIdAndTenantID(Long productId, Long tenantId);

}
