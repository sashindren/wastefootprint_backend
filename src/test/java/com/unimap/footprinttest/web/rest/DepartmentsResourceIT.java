package com.unimap.footprinttest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.unimap.footprinttest.IntegrationTest;
import com.unimap.footprinttest.domain.Departments;
import com.unimap.footprinttest.repository.DepartmentsRepository;
import com.unimap.footprinttest.service.criteria.DepartmentsCriteria;
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
 * Integration tests for the {@link DepartmentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepartmentsResourceIT {

    private static final String DEFAULT_DEPARTMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/departments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepartmentsRepository departmentsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartmentsMockMvc;

    private Departments departments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departments createEntity(EntityManager em) {
        Departments departments = new Departments().departmentName(DEFAULT_DEPARTMENT_NAME);
        return departments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departments createUpdatedEntity(EntityManager em) {
        Departments departments = new Departments().departmentName(UPDATED_DEPARTMENT_NAME);
        return departments;
    }

    @BeforeEach
    public void initTest() {
        departments = createEntity(em);
    }

    @Test
    @Transactional
    void createDepartments() throws Exception {
        int databaseSizeBeforeCreate = departmentsRepository.findAll().size();
        // Create the Departments
        restDepartmentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departments)))
            .andExpect(status().isCreated());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeCreate + 1);
        Departments testDepartments = departmentsList.get(departmentsList.size() - 1);
        assertThat(testDepartments.getDepartmentName()).isEqualTo(DEFAULT_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void createDepartmentsWithExistingId() throws Exception {
        // Create the Departments with an existing ID
        departments.setId(1L);

        int databaseSizeBeforeCreate = departmentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departments)))
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departments.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)));
    }

    @Test
    @Transactional
    void getDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get the departments
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL_ID, departments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departments.getId().intValue()))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME));
    }

    @Test
    @Transactional
    void getDepartmentsByIdFiltering() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        Long id = departments.getId();

        defaultDepartmentsShouldBeFound("id.equals=" + id);
        defaultDepartmentsShouldNotBeFound("id.notEquals=" + id);

        defaultDepartmentsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepartmentsShouldNotBeFound("id.greaterThan=" + id);

        defaultDepartmentsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepartmentsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where departmentName equals to DEFAULT_DEPARTMENT_NAME
        defaultDepartmentsShouldBeFound("departmentName.equals=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the departmentsList where departmentName equals to UPDATED_DEPARTMENT_NAME
        defaultDepartmentsShouldNotBeFound("departmentName.equals=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameIsInShouldWork() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where departmentName in DEFAULT_DEPARTMENT_NAME or UPDATED_DEPARTMENT_NAME
        defaultDepartmentsShouldBeFound("departmentName.in=" + DEFAULT_DEPARTMENT_NAME + "," + UPDATED_DEPARTMENT_NAME);

        // Get all the departmentsList where departmentName equals to UPDATED_DEPARTMENT_NAME
        defaultDepartmentsShouldNotBeFound("departmentName.in=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where departmentName is not null
        defaultDepartmentsShouldBeFound("departmentName.specified=true");

        // Get all the departmentsList where departmentName is null
        defaultDepartmentsShouldNotBeFound("departmentName.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameContainsSomething() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where departmentName contains DEFAULT_DEPARTMENT_NAME
        defaultDepartmentsShouldBeFound("departmentName.contains=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the departmentsList where departmentName contains UPDATED_DEPARTMENT_NAME
        defaultDepartmentsShouldNotBeFound("departmentName.contains=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameNotContainsSomething() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where departmentName does not contain DEFAULT_DEPARTMENT_NAME
        defaultDepartmentsShouldNotBeFound("departmentName.doesNotContain=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the departmentsList where departmentName does not contain UPDATED_DEPARTMENT_NAME
        defaultDepartmentsShouldBeFound("departmentName.doesNotContain=" + UPDATED_DEPARTMENT_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepartmentsShouldBeFound(String filter) throws Exception {
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departments.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)));

        // Check, that the count call also returns 1
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepartmentsShouldNotBeFound(String filter) throws Exception {
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepartments() throws Exception {
        // Get the departments
        restDepartmentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();

        // Update the departments
        Departments updatedDepartments = departmentsRepository.findById(departments.getId()).get();
        // Disconnect from session so that the updates on updatedDepartments are not directly saved in db
        em.detach(updatedDepartments);
        updatedDepartments.departmentName(UPDATED_DEPARTMENT_NAME);

        restDepartmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDepartments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDepartments))
            )
            .andExpect(status().isOk());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
        Departments testDepartments = departmentsList.get(departmentsList.size() - 1);
        assertThat(testDepartments.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departments))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departments))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departments)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartmentsWithPatch() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();

        // Update the departments using partial update
        Departments partialUpdatedDepartments = new Departments();
        partialUpdatedDepartments.setId(departments.getId());

        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartments))
            )
            .andExpect(status().isOk());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
        Departments testDepartments = departmentsList.get(departmentsList.size() - 1);
        assertThat(testDepartments.getDepartmentName()).isEqualTo(DEFAULT_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDepartmentsWithPatch() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();

        // Update the departments using partial update
        Departments partialUpdatedDepartments = new Departments();
        partialUpdatedDepartments.setId(departments.getId());

        partialUpdatedDepartments.departmentName(UPDATED_DEPARTMENT_NAME);

        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartments))
            )
            .andExpect(status().isOk());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
        Departments testDepartments = departmentsList.get(departmentsList.size() - 1);
        assertThat(testDepartments.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departments))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departments))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(departments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        int databaseSizeBeforeDelete = departmentsRepository.findAll().size();

        // Delete the departments
        restDepartmentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, departments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
