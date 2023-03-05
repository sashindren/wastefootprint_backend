package com.unimap.footprinttest.repository;

import com.unimap.footprinttest.domain.ProductionWaste;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductionWaste entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductionWasteRepository extends JpaRepository<ProductionWaste, Long>, JpaSpecificationExecutor<ProductionWaste> {}
