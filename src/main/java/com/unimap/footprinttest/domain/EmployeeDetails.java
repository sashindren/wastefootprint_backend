package com.unimap.footprinttest.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmployeeDetails.
 */
@Entity
@Table(name = "employee_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private Integer phoneNumber;

    @Column(name = "home_number")
    private Integer homeNumber;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "transport_type")
    private String transportType;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "supervisor_name")
    private String supervisorName;

    @Column(name = "company_id")
    private Integer companyId;

    @ManyToOne
    private Departments employeedetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmployeeDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return this.employeeName;
    }

    public EmployeeDetails employeeName(String employeeName) {
        this.setEmployeeName(employeeName);
        return this;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAddress() {
        return this.address;
    }

    public EmployeeDetails address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPhoneNumber() {
        return this.phoneNumber;
    }

    public EmployeeDetails phoneNumber(Integer phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getHomeNumber() {
        return this.homeNumber;
    }

    public EmployeeDetails homeNumber(Integer homeNumber) {
        this.setHomeNumber(homeNumber);
        return this;
    }

    public void setHomeNumber(Integer homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public EmployeeDetails emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTransportType() {
        return this.transportType;
    }

    public EmployeeDetails transportType(String transportType) {
        this.setTransportType(transportType);
        return this;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public EmployeeDetails jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getSupervisorName() {
        return this.supervisorName;
    }

    public EmployeeDetails supervisorName(String supervisorName) {
        this.setSupervisorName(supervisorName);
        return this;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public Integer getCompanyId() {
        return this.companyId;
    }

    public EmployeeDetails companyId(Integer companyId) {
        this.setCompanyId(companyId);
        return this;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Departments getEmployeedetails() {
        return this.employeedetails;
    }

    public void setEmployeedetails(Departments departments) {
        this.employeedetails = departments;
    }

    public EmployeeDetails employeedetails(Departments departments) {
        this.setEmployeedetails(departments);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDetails)) {
            return false;
        }
        return id != null && id.equals(((EmployeeDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDetails{" +
            "id=" + getId() +
            ", employeeName='" + getEmployeeName() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            ", homeNumber=" + getHomeNumber() +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", transportType='" + getTransportType() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", supervisorName='" + getSupervisorName() + "'" +
            ", companyId=" + getCompanyId() +
            "}";
    }
}
