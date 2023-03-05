package com.unimap.footprinttest.service;

import com.unimap.footprinttest.domain.EmployeeDetails;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link EmployeeDetails}.
 */
public interface EmployeeDetailsService {
    /**
     * Save a employeeDetails.
     *
     * @param employeeDetails the entity to save.
     * @return the persisted entity.
     */
    EmployeeDetails save(EmployeeDetails employeeDetails);

    /**
     * Updates a employeeDetails.
     *
     * @param employeeDetails the entity to update.
     * @return the persisted entity.
     */
    EmployeeDetails update(EmployeeDetails employeeDetails);

    /**
     * Partially updates a employeeDetails.
     *
     * @param employeeDetails the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeeDetails> partialUpdate(EmployeeDetails employeeDetails);

    /**
     * Get all the employeeDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmployeeDetails> findAll(Pageable pageable);

    /**
     * Get the "id" employeeDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeDetails> findOne(Long id);

    /**
     * Delete the "id" employeeDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
