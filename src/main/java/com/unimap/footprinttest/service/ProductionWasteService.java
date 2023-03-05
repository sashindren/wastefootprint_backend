package com.unimap.footprinttest.service;

import com.unimap.footprinttest.domain.ProductionWaste;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ProductionWaste}.
 */
public interface ProductionWasteService {
    /**
     * Save a productionWaste.
     *
     * @param productionWaste the entity to save.
     * @return the persisted entity.
     */
    ProductionWaste save(ProductionWaste productionWaste);

    /**
     * Updates a productionWaste.
     *
     * @param productionWaste the entity to update.
     * @return the persisted entity.
     */
    ProductionWaste update(ProductionWaste productionWaste);

    /**
     * Partially updates a productionWaste.
     *
     * @param productionWaste the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductionWaste> partialUpdate(ProductionWaste productionWaste);

    /**
     * Get all the productionWastes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductionWaste> findAll(Pageable pageable);

    /**
     * Get the "id" productionWaste.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductionWaste> findOne(Long id);

    /**
     * Delete the "id" productionWaste.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
