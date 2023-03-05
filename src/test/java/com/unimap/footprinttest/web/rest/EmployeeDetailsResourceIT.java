package com.unimap.footprinttest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.unimap.footprinttest.IntegrationTest;
import com.unimap.footprinttest.domain.Departments;
import com.unimap.footprinttest.domain.EmployeeDetails;
import com.unimap.footprinttest.repository.EmployeeDetailsRepository;
import com.unimap.footprinttest.service.criteria.EmployeeDetailsCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmployeeDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeDetailsResourceIT {

    private static final String DEFAULT_EMPLOYEE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE_NUMBER = 1;
    private static final Integer UPDATED_PHONE_NUMBER = 2;
    private static final Integer SMALLER_PHONE_NUMBER = 1 - 1;

    private static final Integer DEFAULT_HOME_NUMBER = 1;
    private static final Integer UPDATED_HOME_NUMBER = 2;
    private static final Integer SMALLER_HOME_NUMBER = 1 - 1;

    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSPORT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSPORT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUPERVISOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUPERVISOR_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_COMPANY_ID = 1;
    private static final Integer UPDATED_COMPANY_ID = 2;
    private static final Integer SMALLER_COMPANY_ID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/employee-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeDetailsMockMvc;

    private EmployeeDetails employeeDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeDetails createEntity(EntityManager em) {
        EmployeeDetails employeeDetails = new EmployeeDetails()
            .employeeName(DEFAULT_EMPLOYEE_NAME)
            .address(DEFAULT_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .homeNumber(DEFAULT_HOME_NUMBER)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .transportType(DEFAULT_TRANSPORT_TYPE)
            .jobTitle(DEFAULT_JOB_TITLE)
            .supervisorName(DEFAULT_SUPERVISOR_NAME)
            .companyId(DEFAULT_COMPANY_ID);
        return employeeDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeDetails createUpdatedEntity(EntityManager em) {
        EmployeeDetails employeeDetails = new EmployeeDetails()
            .employeeName(UPDATED_EMPLOYEE_NAME)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .homeNumber(UPDATED_HOME_NUMBER)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .transportType(UPDATED_TRANSPORT_TYPE)
            .jobTitle(UPDATED_JOB_TITLE)
            .supervisorName(UPDATED_SUPERVISOR_NAME)
            .companyId(UPDATED_COMPANY_ID);
        return employeeDetails;
    }

    @BeforeEach
    public void initTest() {
        employeeDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeDetails() throws Exception {
        int databaseSizeBeforeCreate = employeeDetailsRepository.findAll().size();
        // Create the EmployeeDetails
        restEmployeeDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDetails))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeeDetails in the database
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        assertThat(employeeDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeDetails testEmployeeDetails = employeeDetailsList.get(employeeDetailsList.size() - 1);
        assertThat(testEmployeeDetails.getEmployeeName()).isEqualTo(DEFAULT_EMPLOYEE_NAME);
        assertThat(testEmployeeDetails.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEmployeeDetails.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmployeeDetails.getHomeNumber()).isEqualTo(DEFAULT_HOME_NUMBER);
        assertThat(testEmployeeDetails.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testEmployeeDetails.getTransportType()).isEqualTo(DEFAULT_TRANSPORT_TYPE);
        assertThat(testEmployeeDetails.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testEmployeeDetails.getSupervisorName()).isEqualTo(DEFAULT_SUPERVISOR_NAME);
        assertThat(testEmployeeDetails.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
    }

    @Test
    @Transactional
    void createEmployeeDetailsWithExistingId() throws Exception {
        // Create the EmployeeDetails with an existing ID
        employeeDetails.setId(1L);

        int databaseSizeBeforeCreate = employeeDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeDetails in the database
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        assertThat(employeeDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployeeDetails() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList
        restEmployeeDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeName").value(hasItem(DEFAULT_EMPLOYEE_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].homeNumber").value(hasItem(DEFAULT_HOME_NUMBER)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].transportType").value(hasItem(DEFAULT_TRANSPORT_TYPE)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].supervisorName").value(hasItem(DEFAULT_SUPERVISOR_NAME)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID)));
    }

    @Test
    @Transactional
    void getEmployeeDetails() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get the employeeDetails
        restEmployeeDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeDetails.getId().intValue()))
            .andExpect(jsonPath("$.employeeName").value(DEFAULT_EMPLOYEE_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.homeNumber").value(DEFAULT_HOME_NUMBER))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.transportType").value(DEFAULT_TRANSPORT_TYPE))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.supervisorName").value(DEFAULT_SUPERVISOR_NAME))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID));
    }

    @Test
    @Transactional
    void getEmployeeDetailsByIdFiltering() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        Long id = employeeDetails.getId();

        defaultEmployeeDetailsShouldBeFound("id.equals=" + id);
        defaultEmployeeDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByEmployeeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where employeeName equals to DEFAULT_EMPLOYEE_NAME
        defaultEmployeeDetailsShouldBeFound("employeeName.equals=" + DEFAULT_EMPLOYEE_NAME);

        // Get all the employeeDetailsList where employeeName equals to UPDATED_EMPLOYEE_NAME
        defaultEmployeeDetailsShouldNotBeFound("employeeName.equals=" + UPDATED_EMPLOYEE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByEmployeeNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where employeeName in DEFAULT_EMPLOYEE_NAME or UPDATED_EMPLOYEE_NAME
        defaultEmployeeDetailsShouldBeFound("employeeName.in=" + DEFAULT_EMPLOYEE_NAME + "," + UPDATED_EMPLOYEE_NAME);

        // Get all the employeeDetailsList where employeeName equals to UPDATED_EMPLOYEE_NAME
        defaultEmployeeDetailsShouldNotBeFound("employeeName.in=" + UPDATED_EMPLOYEE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByEmployeeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where employeeName is not null
        defaultEmployeeDetailsShouldBeFound("employeeName.specified=true");

        // Get all the employeeDetailsList where employeeName is null
        defaultEmployeeDetailsShouldNotBeFound("employeeName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByEmployeeNameContainsSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where employeeName contains DEFAULT_EMPLOYEE_NAME
        defaultEmployeeDetailsShouldBeFound("employeeName.contains=" + DEFAULT_EMPLOYEE_NAME);

        // Get all the employeeDetailsList where employeeName contains UPDATED_EMPLOYEE_NAME
        defaultEmployeeDetailsShouldNotBeFound("employeeName.contains=" + UPDATED_EMPLOYEE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByEmployeeNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where employeeName does not contain DEFAULT_EMPLOYEE_NAME
        defaultEmployeeDetailsShouldNotBeFound("employeeName.doesNotContain=" + DEFAULT_EMPLOYEE_NAME);

        // Get all the employeeDetailsList where employeeName does not contain UPDATED_EMPLOYEE_NAME
        defaultEmployeeDetailsShouldBeFound("employeeName.doesNotContain=" + UPDATED_EMPLOYEE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where address equals to DEFAULT_ADDRESS
        defaultEmployeeDetailsShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the employeeDetailsList where address equals to UPDATED_ADDRESS
        defaultEmployeeDetailsShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultEmployeeDetailsShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the employeeDetailsList where address equals to UPDATED_ADDRESS
        defaultEmployeeDetailsShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where address is not null
        defaultEmployeeDetailsShouldBeFound("address.specified=true");

        // Get all the employeeDetailsList where address is null
        defaultEmployeeDetailsShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByAddressContainsSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where address contains DEFAULT_ADDRESS
        defaultEmployeeDetailsShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the employeeDetailsList where address contains UPDATED_ADDRESS
        defaultEmployeeDetailsShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where address does not contain DEFAULT_ADDRESS
        defaultEmployeeDetailsShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the employeeDetailsList where address does not contain UPDATED_ADDRESS
        defaultEmployeeDetailsShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultEmployeeDetailsShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the employeeDetailsList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultEmployeeDetailsShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultEmployeeDetailsShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the employeeDetailsList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultEmployeeDetailsShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where phoneNumber is not null
        defaultEmployeeDetailsShouldBeFound("phoneNumber.specified=true");

        // Get all the employeeDetailsList where phoneNumber is null
        defaultEmployeeDetailsShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByPhoneNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where phoneNumber is greater than or equal to DEFAULT_PHONE_NUMBER
        defaultEmployeeDetailsShouldBeFound("phoneNumber.greaterThanOrEqual=" + DEFAULT_PHONE_NUMBER);

        // Get all the employeeDetailsList where phoneNumber is greater than or equal to UPDATED_PHONE_NUMBER
        defaultEmployeeDetailsShouldNotBeFound("phoneNumber.greaterThanOrEqual=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByPhoneNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where phoneNumber is less than or equal to DEFAULT_PHONE_NUMBER
        defaultEmployeeDetailsShouldBeFound("phoneNumber.lessThanOrEqual=" + DEFAULT_PHONE_NUMBER);

        // Get all the employeeDetailsList where phoneNumber is less than or equal to SMALLER_PHONE_NUMBER
        defaultEmployeeDetailsShouldNotBeFound("phoneNumber.lessThanOrEqual=" + SMALLER_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByPhoneNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where phoneNumber is less than DEFAULT_PHONE_NUMBER
        defaultEmployeeDetailsShouldNotBeFound("phoneNumber.lessThan=" + DEFAULT_PHONE_NUMBER);

        // Get all the employeeDetailsList where phoneNumber is less than UPDATED_PHONE_NUMBER
        defaultEmployeeDetailsShouldBeFound("phoneNumber.lessThan=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByPhoneNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where phoneNumber is greater than DEFAULT_PHONE_NUMBER
        defaultEmployeeDetailsShouldNotBeFound("phoneNumber.greaterThan=" + DEFAULT_PHONE_NUMBER);

        // Get all the employeeDetailsList where phoneNumber is greater than SMALLER_PHONE_NUMBER
        defaultEmployeeDetailsShouldBeFound("phoneNumber.greaterThan=" + SMALLER_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByHomeNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where homeNumber equals to DEFAULT_HOME_NUMBER
        defaultEmployeeDetailsShouldBeFound("homeNumber.equals=" + DEFAULT_HOME_NUMBER);

        // Get all the employeeDetailsList where homeNumber equals to UPDATED_HOME_NUMBER
        defaultEmployeeDetailsShouldNotBeFound("homeNumber.equals=" + UPDATED_HOME_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByHomeNumberIsInShouldWork() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where homeNumber in DEFAULT_HOME_NUMBER or UPDATED_HOME_NUMBER
        defaultEmployeeDetailsShouldBeFound("homeNumber.in=" + DEFAULT_HOME_NUMBER + "," + UPDATED_HOME_NUMBER);

        // Get all the employeeDetailsList where homeNumber equals to UPDATED_HOME_NUMBER
        defaultEmployeeDetailsShouldNotBeFound("homeNumber.in=" + UPDATED_HOME_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByHomeNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where homeNumber is not null
        defaultEmployeeDetailsShouldBeFound("homeNumber.specified=true");

        // Get all the employeeDetailsList where homeNumber is null
        defaultEmployeeDetailsShouldNotBeFound("homeNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByHomeNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where homeNumber is greater than or equal to DEFAULT_HOME_NUMBER
        defaultEmployeeDetailsShouldBeFound("homeNumber.greaterThanOrEqual=" + DEFAULT_HOME_NUMBER);

        // Get all the employeeDetailsList where homeNumber is greater than or equal to UPDATED_HOME_NUMBER
        defaultEmployeeDetailsShouldNotBeFound("homeNumber.greaterThanOrEqual=" + UPDATED_HOME_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByHomeNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where homeNumber is less than or equal to DEFAULT_HOME_NUMBER
        defaultEmployeeDetailsShouldBeFound("homeNumber.lessThanOrEqual=" + DEFAULT_HOME_NUMBER);

        // Get all the employeeDetailsList where homeNumber is less than or equal to SMALLER_HOME_NUMBER
        defaultEmployeeDetailsShouldNotBeFound("homeNumber.lessThanOrEqual=" + SMALLER_HOME_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByHomeNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where homeNumber is less than DEFAULT_HOME_NUMBER
        defaultEmployeeDetailsShouldNotBeFound("homeNumber.lessThan=" + DEFAULT_HOME_NUMBER);

        // Get all the employeeDetailsList where homeNumber is less than UPDATED_HOME_NUMBER
        defaultEmployeeDetailsShouldBeFound("homeNumber.lessThan=" + UPDATED_HOME_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByHomeNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where homeNumber is greater than DEFAULT_HOME_NUMBER
        defaultEmployeeDetailsShouldNotBeFound("homeNumber.greaterThan=" + DEFAULT_HOME_NUMBER);

        // Get all the employeeDetailsList where homeNumber is greater than SMALLER_HOME_NUMBER
        defaultEmployeeDetailsShouldBeFound("homeNumber.greaterThan=" + SMALLER_HOME_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByEmailAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where emailAddress equals to DEFAULT_EMAIL_ADDRESS
        defaultEmployeeDetailsShouldBeFound("emailAddress.equals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the employeeDetailsList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultEmployeeDetailsShouldNotBeFound("emailAddress.equals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByEmailAddressIsInShouldWork() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where emailAddress in DEFAULT_EMAIL_ADDRESS or UPDATED_EMAIL_ADDRESS
        defaultEmployeeDetailsShouldBeFound("emailAddress.in=" + DEFAULT_EMAIL_ADDRESS + "," + UPDATED_EMAIL_ADDRESS);

        // Get all the employeeDetailsList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultEmployeeDetailsShouldNotBeFound("emailAddress.in=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByEmailAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where emailAddress is not null
        defaultEmployeeDetailsShouldBeFound("emailAddress.specified=true");

        // Get all the employeeDetailsList where emailAddress is null
        defaultEmployeeDetailsShouldNotBeFound("emailAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByEmailAddressContainsSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where emailAddress contains DEFAULT_EMAIL_ADDRESS
        defaultEmployeeDetailsShouldBeFound("emailAddress.contains=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the employeeDetailsList where emailAddress contains UPDATED_EMAIL_ADDRESS
        defaultEmployeeDetailsShouldNotBeFound("emailAddress.contains=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByEmailAddressNotContainsSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where emailAddress does not contain DEFAULT_EMAIL_ADDRESS
        defaultEmployeeDetailsShouldNotBeFound("emailAddress.doesNotContain=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the employeeDetailsList where emailAddress does not contain UPDATED_EMAIL_ADDRESS
        defaultEmployeeDetailsShouldBeFound("emailAddress.doesNotContain=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByTransportTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where transportType equals to DEFAULT_TRANSPORT_TYPE
        defaultEmployeeDetailsShouldBeFound("transportType.equals=" + DEFAULT_TRANSPORT_TYPE);

        // Get all the employeeDetailsList where transportType equals to UPDATED_TRANSPORT_TYPE
        defaultEmployeeDetailsShouldNotBeFound("transportType.equals=" + UPDATED_TRANSPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByTransportTypeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where transportType in DEFAULT_TRANSPORT_TYPE or UPDATED_TRANSPORT_TYPE
        defaultEmployeeDetailsShouldBeFound("transportType.in=" + DEFAULT_TRANSPORT_TYPE + "," + UPDATED_TRANSPORT_TYPE);

        // Get all the employeeDetailsList where transportType equals to UPDATED_TRANSPORT_TYPE
        defaultEmployeeDetailsShouldNotBeFound("transportType.in=" + UPDATED_TRANSPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByTransportTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where transportType is not null
        defaultEmployeeDetailsShouldBeFound("transportType.specified=true");

        // Get all the employeeDetailsList where transportType is null
        defaultEmployeeDetailsShouldNotBeFound("transportType.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByTransportTypeContainsSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where transportType contains DEFAULT_TRANSPORT_TYPE
        defaultEmployeeDetailsShouldBeFound("transportType.contains=" + DEFAULT_TRANSPORT_TYPE);

        // Get all the employeeDetailsList where transportType contains UPDATED_TRANSPORT_TYPE
        defaultEmployeeDetailsShouldNotBeFound("transportType.contains=" + UPDATED_TRANSPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByTransportTypeNotContainsSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where transportType does not contain DEFAULT_TRANSPORT_TYPE
        defaultEmployeeDetailsShouldNotBeFound("transportType.doesNotContain=" + DEFAULT_TRANSPORT_TYPE);

        // Get all the employeeDetailsList where transportType does not contain UPDATED_TRANSPORT_TYPE
        defaultEmployeeDetailsShouldBeFound("transportType.doesNotContain=" + UPDATED_TRANSPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByJobTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where jobTitle equals to DEFAULT_JOB_TITLE
        defaultEmployeeDetailsShouldBeFound("jobTitle.equals=" + DEFAULT_JOB_TITLE);

        // Get all the employeeDetailsList where jobTitle equals to UPDATED_JOB_TITLE
        defaultEmployeeDetailsShouldNotBeFound("jobTitle.equals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByJobTitleIsInShouldWork() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where jobTitle in DEFAULT_JOB_TITLE or UPDATED_JOB_TITLE
        defaultEmployeeDetailsShouldBeFound("jobTitle.in=" + DEFAULT_JOB_TITLE + "," + UPDATED_JOB_TITLE);

        // Get all the employeeDetailsList where jobTitle equals to UPDATED_JOB_TITLE
        defaultEmployeeDetailsShouldNotBeFound("jobTitle.in=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByJobTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where jobTitle is not null
        defaultEmployeeDetailsShouldBeFound("jobTitle.specified=true");

        // Get all the employeeDetailsList where jobTitle is null
        defaultEmployeeDetailsShouldNotBeFound("jobTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByJobTitleContainsSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where jobTitle contains DEFAULT_JOB_TITLE
        defaultEmployeeDetailsShouldBeFound("jobTitle.contains=" + DEFAULT_JOB_TITLE);

        // Get all the employeeDetailsList where jobTitle contains UPDATED_JOB_TITLE
        defaultEmployeeDetailsShouldNotBeFound("jobTitle.contains=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByJobTitleNotContainsSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where jobTitle does not contain DEFAULT_JOB_TITLE
        defaultEmployeeDetailsShouldNotBeFound("jobTitle.doesNotContain=" + DEFAULT_JOB_TITLE);

        // Get all the employeeDetailsList where jobTitle does not contain UPDATED_JOB_TITLE
        defaultEmployeeDetailsShouldBeFound("jobTitle.doesNotContain=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsBySupervisorNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where supervisorName equals to DEFAULT_SUPERVISOR_NAME
        defaultEmployeeDetailsShouldBeFound("supervisorName.equals=" + DEFAULT_SUPERVISOR_NAME);

        // Get all the employeeDetailsList where supervisorName equals to UPDATED_SUPERVISOR_NAME
        defaultEmployeeDetailsShouldNotBeFound("supervisorName.equals=" + UPDATED_SUPERVISOR_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsBySupervisorNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where supervisorName in DEFAULT_SUPERVISOR_NAME or UPDATED_SUPERVISOR_NAME
        defaultEmployeeDetailsShouldBeFound("supervisorName.in=" + DEFAULT_SUPERVISOR_NAME + "," + UPDATED_SUPERVISOR_NAME);

        // Get all the employeeDetailsList where supervisorName equals to UPDATED_SUPERVISOR_NAME
        defaultEmployeeDetailsShouldNotBeFound("supervisorName.in=" + UPDATED_SUPERVISOR_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsBySupervisorNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where supervisorName is not null
        defaultEmployeeDetailsShouldBeFound("supervisorName.specified=true");

        // Get all the employeeDetailsList where supervisorName is null
        defaultEmployeeDetailsShouldNotBeFound("supervisorName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsBySupervisorNameContainsSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where supervisorName contains DEFAULT_SUPERVISOR_NAME
        defaultEmployeeDetailsShouldBeFound("supervisorName.contains=" + DEFAULT_SUPERVISOR_NAME);

        // Get all the employeeDetailsList where supervisorName contains UPDATED_SUPERVISOR_NAME
        defaultEmployeeDetailsShouldNotBeFound("supervisorName.contains=" + UPDATED_SUPERVISOR_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsBySupervisorNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where supervisorName does not contain DEFAULT_SUPERVISOR_NAME
        defaultEmployeeDetailsShouldNotBeFound("supervisorName.doesNotContain=" + DEFAULT_SUPERVISOR_NAME);

        // Get all the employeeDetailsList where supervisorName does not contain UPDATED_SUPERVISOR_NAME
        defaultEmployeeDetailsShouldBeFound("supervisorName.doesNotContain=" + UPDATED_SUPERVISOR_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByCompanyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where companyId equals to DEFAULT_COMPANY_ID
        defaultEmployeeDetailsShouldBeFound("companyId.equals=" + DEFAULT_COMPANY_ID);

        // Get all the employeeDetailsList where companyId equals to UPDATED_COMPANY_ID
        defaultEmployeeDetailsShouldNotBeFound("companyId.equals=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByCompanyIdIsInShouldWork() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where companyId in DEFAULT_COMPANY_ID or UPDATED_COMPANY_ID
        defaultEmployeeDetailsShouldBeFound("companyId.in=" + DEFAULT_COMPANY_ID + "," + UPDATED_COMPANY_ID);

        // Get all the employeeDetailsList where companyId equals to UPDATED_COMPANY_ID
        defaultEmployeeDetailsShouldNotBeFound("companyId.in=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByCompanyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where companyId is not null
        defaultEmployeeDetailsShouldBeFound("companyId.specified=true");

        // Get all the employeeDetailsList where companyId is null
        defaultEmployeeDetailsShouldNotBeFound("companyId.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByCompanyIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where companyId is greater than or equal to DEFAULT_COMPANY_ID
        defaultEmployeeDetailsShouldBeFound("companyId.greaterThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the employeeDetailsList where companyId is greater than or equal to UPDATED_COMPANY_ID
        defaultEmployeeDetailsShouldNotBeFound("companyId.greaterThanOrEqual=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByCompanyIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where companyId is less than or equal to DEFAULT_COMPANY_ID
        defaultEmployeeDetailsShouldBeFound("companyId.lessThanOrEqual=" + DEFAULT_COMPANY_ID);

        // Get all the employeeDetailsList where companyId is less than or equal to SMALLER_COMPANY_ID
        defaultEmployeeDetailsShouldNotBeFound("companyId.lessThanOrEqual=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByCompanyIdIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where companyId is less than DEFAULT_COMPANY_ID
        defaultEmployeeDetailsShouldNotBeFound("companyId.lessThan=" + DEFAULT_COMPANY_ID);

        // Get all the employeeDetailsList where companyId is less than UPDATED_COMPANY_ID
        defaultEmployeeDetailsShouldBeFound("companyId.lessThan=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByCompanyIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        // Get all the employeeDetailsList where companyId is greater than DEFAULT_COMPANY_ID
        defaultEmployeeDetailsShouldNotBeFound("companyId.greaterThan=" + DEFAULT_COMPANY_ID);

        // Get all the employeeDetailsList where companyId is greater than SMALLER_COMPANY_ID
        defaultEmployeeDetailsShouldBeFound("companyId.greaterThan=" + SMALLER_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeDetailsByEmployeedetailsIsEqualToSomething() throws Exception {
        Departments employeedetails;
        if (TestUtil.findAll(em, Departments.class).isEmpty()) {
            employeeDetailsRepository.saveAndFlush(employeeDetails);
            employeedetails = DepartmentsResourceIT.createEntity(em);
        } else {
            employeedetails = TestUtil.findAll(em, Departments.class).get(0);
        }
        em.persist(employeedetails);
        em.flush();
        employeeDetails.setEmployeedetails(employeedetails);
        employeeDetailsRepository.saveAndFlush(employeeDetails);
        Long employeedetailsId = employeedetails.getId();

        // Get all the employeeDetailsList where employeedetails equals to employeedetailsId
        defaultEmployeeDetailsShouldBeFound("employeedetailsId.equals=" + employeedetailsId);

        // Get all the employeeDetailsList where employeedetails equals to (employeedetailsId + 1)
        defaultEmployeeDetailsShouldNotBeFound("employeedetailsId.equals=" + (employeedetailsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeDetailsShouldBeFound(String filter) throws Exception {
        restEmployeeDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeName").value(hasItem(DEFAULT_EMPLOYEE_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].homeNumber").value(hasItem(DEFAULT_HOME_NUMBER)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].transportType").value(hasItem(DEFAULT_TRANSPORT_TYPE)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].supervisorName").value(hasItem(DEFAULT_SUPERVISOR_NAME)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID)));

        // Check, that the count call also returns 1
        restEmployeeDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeDetailsShouldNotBeFound(String filter) throws Exception {
        restEmployeeDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeDetails() throws Exception {
        // Get the employeeDetails
        restEmployeeDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeDetails() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        int databaseSizeBeforeUpdate = employeeDetailsRepository.findAll().size();

        // Update the employeeDetails
        EmployeeDetails updatedEmployeeDetails = employeeDetailsRepository.findById(employeeDetails.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeDetails are not directly saved in db
        em.detach(updatedEmployeeDetails);
        updatedEmployeeDetails
            .employeeName(UPDATED_EMPLOYEE_NAME)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .homeNumber(UPDATED_HOME_NUMBER)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .transportType(UPDATED_TRANSPORT_TYPE)
            .jobTitle(UPDATED_JOB_TITLE)
            .supervisorName(UPDATED_SUPERVISOR_NAME)
            .companyId(UPDATED_COMPANY_ID);

        restEmployeeDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmployeeDetails))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeDetails in the database
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        assertThat(employeeDetailsList).hasSize(databaseSizeBeforeUpdate);
        EmployeeDetails testEmployeeDetails = employeeDetailsList.get(employeeDetailsList.size() - 1);
        assertThat(testEmployeeDetails.getEmployeeName()).isEqualTo(UPDATED_EMPLOYEE_NAME);
        assertThat(testEmployeeDetails.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEmployeeDetails.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployeeDetails.getHomeNumber()).isEqualTo(UPDATED_HOME_NUMBER);
        assertThat(testEmployeeDetails.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testEmployeeDetails.getTransportType()).isEqualTo(UPDATED_TRANSPORT_TYPE);
        assertThat(testEmployeeDetails.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testEmployeeDetails.getSupervisorName()).isEqualTo(UPDATED_SUPERVISOR_NAME);
        assertThat(testEmployeeDetails.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeDetails() throws Exception {
        int databaseSizeBeforeUpdate = employeeDetailsRepository.findAll().size();
        employeeDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeDetails in the database
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        assertThat(employeeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeDetails() throws Exception {
        int databaseSizeBeforeUpdate = employeeDetailsRepository.findAll().size();
        employeeDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeDetails in the database
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        assertThat(employeeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeDetails() throws Exception {
        int databaseSizeBeforeUpdate = employeeDetailsRepository.findAll().size();
        employeeDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeDetails in the database
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        assertThat(employeeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeDetailsWithPatch() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        int databaseSizeBeforeUpdate = employeeDetailsRepository.findAll().size();

        // Update the employeeDetails using partial update
        EmployeeDetails partialUpdatedEmployeeDetails = new EmployeeDetails();
        partialUpdatedEmployeeDetails.setId(employeeDetails.getId());

        partialUpdatedEmployeeDetails.phoneNumber(UPDATED_PHONE_NUMBER).emailAddress(UPDATED_EMAIL_ADDRESS).companyId(UPDATED_COMPANY_ID);

        restEmployeeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeDetails))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeDetails in the database
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        assertThat(employeeDetailsList).hasSize(databaseSizeBeforeUpdate);
        EmployeeDetails testEmployeeDetails = employeeDetailsList.get(employeeDetailsList.size() - 1);
        assertThat(testEmployeeDetails.getEmployeeName()).isEqualTo(DEFAULT_EMPLOYEE_NAME);
        assertThat(testEmployeeDetails.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEmployeeDetails.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployeeDetails.getHomeNumber()).isEqualTo(DEFAULT_HOME_NUMBER);
        assertThat(testEmployeeDetails.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testEmployeeDetails.getTransportType()).isEqualTo(DEFAULT_TRANSPORT_TYPE);
        assertThat(testEmployeeDetails.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testEmployeeDetails.getSupervisorName()).isEqualTo(DEFAULT_SUPERVISOR_NAME);
        assertThat(testEmployeeDetails.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeDetailsWithPatch() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        int databaseSizeBeforeUpdate = employeeDetailsRepository.findAll().size();

        // Update the employeeDetails using partial update
        EmployeeDetails partialUpdatedEmployeeDetails = new EmployeeDetails();
        partialUpdatedEmployeeDetails.setId(employeeDetails.getId());

        partialUpdatedEmployeeDetails
            .employeeName(UPDATED_EMPLOYEE_NAME)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .homeNumber(UPDATED_HOME_NUMBER)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .transportType(UPDATED_TRANSPORT_TYPE)
            .jobTitle(UPDATED_JOB_TITLE)
            .supervisorName(UPDATED_SUPERVISOR_NAME)
            .companyId(UPDATED_COMPANY_ID);

        restEmployeeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeDetails))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeDetails in the database
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        assertThat(employeeDetailsList).hasSize(databaseSizeBeforeUpdate);
        EmployeeDetails testEmployeeDetails = employeeDetailsList.get(employeeDetailsList.size() - 1);
        assertThat(testEmployeeDetails.getEmployeeName()).isEqualTo(UPDATED_EMPLOYEE_NAME);
        assertThat(testEmployeeDetails.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEmployeeDetails.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployeeDetails.getHomeNumber()).isEqualTo(UPDATED_HOME_NUMBER);
        assertThat(testEmployeeDetails.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testEmployeeDetails.getTransportType()).isEqualTo(UPDATED_TRANSPORT_TYPE);
        assertThat(testEmployeeDetails.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testEmployeeDetails.getSupervisorName()).isEqualTo(UPDATED_SUPERVISOR_NAME);
        assertThat(testEmployeeDetails.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeDetails() throws Exception {
        int databaseSizeBeforeUpdate = employeeDetailsRepository.findAll().size();
        employeeDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeDetails in the database
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        assertThat(employeeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeDetails() throws Exception {
        int databaseSizeBeforeUpdate = employeeDetailsRepository.findAll().size();
        employeeDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeDetails in the database
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        assertThat(employeeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeDetails() throws Exception {
        int databaseSizeBeforeUpdate = employeeDetailsRepository.findAll().size();
        employeeDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeDetails in the database
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        assertThat(employeeDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeDetails() throws Exception {
        // Initialize the database
        employeeDetailsRepository.saveAndFlush(employeeDetails);

        int databaseSizeBeforeDelete = employeeDetailsRepository.findAll().size();

        // Delete the employeeDetails
        restEmployeeDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAll();
        assertThat(employeeDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
