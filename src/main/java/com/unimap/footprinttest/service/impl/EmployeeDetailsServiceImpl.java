package com.unimap.footprinttest.service.impl;

import com.unimap.footprinttest.domain.EmployeeDetails;
import com.unimap.footprinttest.repository.EmployeeDetailsRepository;
import com.unimap.footprinttest.service.EmployeeDetailsService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EmployeeDetails}.
 */
@Service
@Transactional
public class EmployeeDetailsServiceImpl implements EmployeeDetailsService {

    private final Logger log = LoggerFactory.getLogger(EmployeeDetailsServiceImpl.class);

    private final EmployeeDetailsRepository employeeDetailsRepository;

    public EmployeeDetailsServiceImpl(EmployeeDetailsRepository employeeDetailsRepository) {
        this.employeeDetailsRepository = employeeDetailsRepository;
    }

    @Override
    public EmployeeDetails save(EmployeeDetails employeeDetails) {
        log.debug("Request to save EmployeeDetails : {}", employeeDetails);
        return employeeDetailsRepository.save(employeeDetails);
    }

    @Override
    public EmployeeDetails update(EmployeeDetails employeeDetails) {
        log.debug("Request to update EmployeeDetails : {}", employeeDetails);
        return employeeDetailsRepository.save(employeeDetails);
    }

    @Override
    public Optional<EmployeeDetails> partialUpdate(EmployeeDetails employeeDetails) {
        log.debug("Request to partially update EmployeeDetails : {}", employeeDetails);

        return employeeDetailsRepository
            .findById(employeeDetails.getId())
            .map(existingEmployeeDetails -> {
                if (employeeDetails.getEmployeeName() != null) {
                    existingEmployeeDetails.setEmployeeName(employeeDetails.getEmployeeName());
                }
                if (employeeDetails.getAddress() != null) {
                    existingEmployeeDetails.setAddress(employeeDetails.getAddress());
                }
                if (employeeDetails.getPhoneNumber() != null) {
                    existingEmployeeDetails.setPhoneNumber(employeeDetails.getPhoneNumber());
                }
                if (employeeDetails.getHomeNumber() != null) {
                    existingEmployeeDetails.setHomeNumber(employeeDetails.getHomeNumber());
                }
                if (employeeDetails.getEmailAddress() != null) {
                    existingEmployeeDetails.setEmailAddress(employeeDetails.getEmailAddress());
                }
                if (employeeDetails.getTransportType() != null) {
                    existingEmployeeDetails.setTransportType(employeeDetails.getTransportType());
                }
                if (employeeDetails.getJobTitle() != null) {
                    existingEmployeeDetails.setJobTitle(employeeDetails.getJobTitle());
                }
                if (employeeDetails.getSupervisorName() != null) {
                    existingEmployeeDetails.setSupervisorName(employeeDetails.getSupervisorName());
                }
                if (employeeDetails.getCompanyId() != null) {
                    existingEmployeeDetails.setCompanyId(employeeDetails.getCompanyId());
                }

                return existingEmployeeDetails;
            })
            .map(employeeDetailsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDetails> findAll(Pageable pageable) {
        log.debug("Request to get all EmployeeDetails");
        return employeeDetailsRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDetails> findOne(Long id) {
        log.debug("Request to get EmployeeDetails : {}", id);
        return employeeDetailsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmployeeDetails : {}", id);
        employeeDetailsRepository.deleteById(id);
    }
}
