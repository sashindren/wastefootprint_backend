package com.unimap.footprinttest.service;

import com.unimap.footprinttest.domain.Departments;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Departments}.
 */
public interface DepartmentsService {
    /**
     * Save a departments.
     *
     * @param departments the entity to save.
     * @return the persisted entity.
     */
    Departments save(Departments departments);

    /**
     * Updates a departments.
     *
     * @param departments the entity to update.
     * @return the persisted entity.
     */
    Departments update(Departments departments);

    /**
     * Partially updates a departments.
     *
     * @param departments the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Departments> partialUpdate(Departments departments);

    /**
     * Get all the departments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Departments> findAll(Pageable pageable);

    /**
     * Get the "id" departments.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Departments> findOne(Long id);

    /**
     * Delete the "id" departments.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
