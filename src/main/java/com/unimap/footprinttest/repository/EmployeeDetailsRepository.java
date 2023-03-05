package com.unimap.footprinttest.repository;

import com.unimap.footprinttest.domain.EmployeeDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmployeeDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long>, JpaSpecificationExecutor<EmployeeDetails> {}
