package com.unimap.footprinttest.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.unimap.footprinttest.domain.EmployeeDetails} entity. This class is used
 * in {@link com.unimap.footprinttest.web.rest.EmployeeDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employee-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter employeeName;

    private StringFilter address;

    private IntegerFilter phoneNumber;

    private IntegerFilter homeNumber;

    private StringFilter emailAddress;

    private StringFilter transportType;

    private StringFilter jobTitle;

    private StringFilter supervisorName;

    private IntegerFilter companyId;

    private LongFilter employeedetailsId;

    private Boolean distinct;

    public EmployeeDetailsCriteria() {}

    public EmployeeDetailsCriteria(EmployeeDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeeName = other.employeeName == null ? null : other.employeeName.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.homeNumber = other.homeNumber == null ? null : other.homeNumber.copy();
        this.emailAddress = other.emailAddress == null ? null : other.emailAddress.copy();
        this.transportType = other.transportType == null ? null : other.transportType.copy();
        this.jobTitle = other.jobTitle == null ? null : other.jobTitle.copy();
        this.supervisorName = other.supervisorName == null ? null : other.supervisorName.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.employeedetailsId = other.employeedetailsId == null ? null : other.employeedetailsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmployeeDetailsCriteria copy() {
        return new EmployeeDetailsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEmployeeName() {
        return employeeName;
    }

    public StringFilter employeeName() {
        if (employeeName == null) {
            employeeName = new StringFilter();
        }
        return employeeName;
    }

    public void setEmployeeName(StringFilter employeeName) {
        this.employeeName = employeeName;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public IntegerFilter getPhoneNumber() {
        return phoneNumber;
    }

    public IntegerFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new IntegerFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(IntegerFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public IntegerFilter getHomeNumber() {
        return homeNumber;
    }

    public IntegerFilter homeNumber() {
        if (homeNumber == null) {
            homeNumber = new IntegerFilter();
        }
        return homeNumber;
    }

    public void setHomeNumber(IntegerFilter homeNumber) {
        this.homeNumber = homeNumber;
    }

    public StringFilter getEmailAddress() {
        return emailAddress;
    }

    public StringFilter emailAddress() {
        if (emailAddress == null) {
            emailAddress = new StringFilter();
        }
        return emailAddress;
    }

    public void setEmailAddress(StringFilter emailAddress) {
        this.emailAddress = emailAddress;
    }

    public StringFilter getTransportType() {
        return transportType;
    }

    public StringFilter transportType() {
        if (transportType == null) {
            transportType = new StringFilter();
        }
        return transportType;
    }

    public void setTransportType(StringFilter transportType) {
        this.transportType = transportType;
    }

    public StringFilter getJobTitle() {
        return jobTitle;
    }

    public StringFilter jobTitle() {
        if (jobTitle == null) {
            jobTitle = new StringFilter();
        }
        return jobTitle;
    }

    public void setJobTitle(StringFilter jobTitle) {
        this.jobTitle = jobTitle;
    }

    public StringFilter getSupervisorName() {
        return supervisorName;
    }

    public StringFilter supervisorName() {
        if (supervisorName == null) {
            supervisorName = new StringFilter();
        }
        return supervisorName;
    }

    public void setSupervisorName(StringFilter supervisorName) {
        this.supervisorName = supervisorName;
    }

    public IntegerFilter getCompanyId() {
        return companyId;
    }

    public IntegerFilter companyId() {
        if (companyId == null) {
            companyId = new IntegerFilter();
        }
        return companyId;
    }

    public void setCompanyId(IntegerFilter companyId) {
        this.companyId = companyId;
    }

    public LongFilter getEmployeedetailsId() {
        return employeedetailsId;
    }

    public LongFilter employeedetailsId() {
        if (employeedetailsId == null) {
            employeedetailsId = new LongFilter();
        }
        return employeedetailsId;
    }

    public void setEmployeedetailsId(LongFilter employeedetailsId) {
        this.employeedetailsId = employeedetailsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployeeDetailsCriteria that = (EmployeeDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeName, that.employeeName) &&
            Objects.equals(address, that.address) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(homeNumber, that.homeNumber) &&
            Objects.equals(emailAddress, that.emailAddress) &&
            Objects.equals(transportType, that.transportType) &&
            Objects.equals(jobTitle, that.jobTitle) &&
            Objects.equals(supervisorName, that.supervisorName) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(employeedetailsId, that.employeedetailsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            employeeName,
            address,
            phoneNumber,
            homeNumber,
            emailAddress,
            transportType,
            jobTitle,
            supervisorName,
            companyId,
            employeedetailsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (employeeName != null ? "employeeName=" + employeeName + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (homeNumber != null ? "homeNumber=" + homeNumber + ", " : "") +
            (emailAddress != null ? "emailAddress=" + emailAddress + ", " : "") +
            (transportType != null ? "transportType=" + transportType + ", " : "") +
            (jobTitle != null ? "jobTitle=" + jobTitle + ", " : "") +
            (supervisorName != null ? "supervisorName=" + supervisorName + ", " : "") +
            (companyId != null ? "companyId=" + companyId + ", " : "") +
            (employeedetailsId != null ? "employeedetailsId=" + employeedetailsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
