package com.unimap.footprinttest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.unimap.footprinttest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeeDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeDetails.class);
        EmployeeDetails employeeDetails1 = new EmployeeDetails();
        employeeDetails1.setId(1L);
        EmployeeDetails employeeDetails2 = new EmployeeDetails();
        employeeDetails2.setId(employeeDetails1.getId());
        assertThat(employeeDetails1).isEqualTo(employeeDetails2);
        employeeDetails2.setId(2L);
        assertThat(employeeDetails1).isNotEqualTo(employeeDetails2);
        employeeDetails1.setId(null);
        assertThat(employeeDetails1).isNotEqualTo(employeeDetails2);
    }
}
