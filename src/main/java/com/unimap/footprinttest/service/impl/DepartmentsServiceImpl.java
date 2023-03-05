package com.unimap.footprinttest.service.impl;

import com.unimap.footprinttest.domain.Departments;
import com.unimap.footprinttest.repository.DepartmentsRepository;
import com.unimap.footprinttest.service.DepartmentsService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Departments}.
 */
@Service
@Transactional
public class DepartmentsServiceImpl implements DepartmentsService {

    private final Logger log = LoggerFactory.getLogger(DepartmentsServiceImpl.class);

    private final DepartmentsRepository departmentsRepository;

    public DepartmentsServiceImpl(DepartmentsRepository departmentsRepository) {
        this.departmentsRepository = departmentsRepository;
    }

    @Override
    public Departments save(Departments departments) {
        log.debug("Request to save Departments : {}", departments);
        return departmentsRepository.save(departments);
    }

    @Override
    public Departments update(Departments departments) {
        log.debug("Request to update Departments : {}", departments);
        return departmentsRepository.save(departments);
    }

    @Override
    public Optional<Departments> partialUpdate(Departments departments) {
        log.debug("Request to partially update Departments : {}", departments);

        return departmentsRepository
            .findById(departments.getId())
            .map(existingDepartments -> {
                if (departments.getDepartmentName() != null) {
                    existingDepartments.setDepartmentName(departments.getDepartmentName());
                }

                return existingDepartments;
            })
            .map(departmentsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Departments> findAll(Pageable pageable) {
        log.debug("Request to get all Departments");
        return departmentsRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Departments> findOne(Long id) {
        log.debug("Request to get Departments : {}", id);
        return departmentsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Departments : {}", id);
        departmentsRepository.deleteById(id);
    }
}
