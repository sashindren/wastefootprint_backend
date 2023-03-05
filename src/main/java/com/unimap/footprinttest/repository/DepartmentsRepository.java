package com.unimap.footprinttest.repository;

import com.unimap.footprinttest.domain.Departments;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Departments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentsRepository extends JpaRepository<Departments, Long>, JpaSpecificationExecutor<Departments> {}
