package com.unimap.footprinttest.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.unimap.footprinttest.IntegrationTest;
import com.unimap.footprinttest.domain.Departments;
import com.unimap.footprinttest.domain.ProductionWaste;
import com.unimap.footprinttest.repository.ProductionWasteRepository;
import com.unimap.footprinttest.service.criteria.ProductionWasteCriteria;
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
 * Integration tests for the {@link ProductionWasteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductionWasteResourceIT {

    private static final String DEFAULT_MATERIAL = "AAAAAAAAAA";
    private static final String UPDATED_MATERIAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String DEFAULT_TRANSPORT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSPORT_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ELECTRIC = 1;
    private static final Integer UPDATED_ELECTRIC = 2;
    private static final Integer SMALLER_ELECTRIC = 1 - 1;

    private static final Integer DEFAULT_WATER = 1;
    private static final Integer UPDATED_WATER = 2;
    private static final Integer SMALLER_WATER = 1 - 1;

    private static final String DEFAULT_WASTE = "AAAAAAAAAA";
    private static final String UPDATED_WASTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/production-wastes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductionWasteRepository productionWasteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductionWasteMockMvc;

    private ProductionWaste productionWaste;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductionWaste createEntity(EntityManager em) {
        ProductionWaste productionWaste = new ProductionWaste()
            .material(DEFAULT_MATERIAL)
            .quantity(DEFAULT_QUANTITY)
            .transportType(DEFAULT_TRANSPORT_TYPE)
            .electric(DEFAULT_ELECTRIC)
            .water(DEFAULT_WATER)
            .waste(DEFAULT_WASTE);
        return productionWaste;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductionWaste createUpdatedEntity(EntityManager em) {
        ProductionWaste productionWaste = new ProductionWaste()
            .material(UPDATED_MATERIAL)
            .quantity(UPDATED_QUANTITY)
            .transportType(UPDATED_TRANSPORT_TYPE)
            .electric(UPDATED_ELECTRIC)
            .water(UPDATED_WATER)
            .waste(UPDATED_WASTE);
        return productionWaste;
    }

    @BeforeEach
    public void initTest() {
        productionWaste = createEntity(em);
    }

    @Test
    @Transactional
    void createProductionWaste() throws Exception {
        int databaseSizeBeforeCreate = productionWasteRepository.findAll().size();
        // Create the ProductionWaste
        restProductionWasteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productionWaste))
            )
            .andExpect(status().isCreated());

        // Validate the ProductionWaste in the database
        List<ProductionWaste> productionWasteList = productionWasteRepository.findAll();
        assertThat(productionWasteList).hasSize(databaseSizeBeforeCreate + 1);
        ProductionWaste testProductionWaste = productionWasteList.get(productionWasteList.size() - 1);
        assertThat(testProductionWaste.getMaterial()).isEqualTo(DEFAULT_MATERIAL);
        assertThat(testProductionWaste.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testProductionWaste.getTransportType()).isEqualTo(DEFAULT_TRANSPORT_TYPE);
        assertThat(testProductionWaste.getElectric()).isEqualTo(DEFAULT_ELECTRIC);
        assertThat(testProductionWaste.getWater()).isEqualTo(DEFAULT_WATER);
        assertThat(testProductionWaste.getWaste()).isEqualTo(DEFAULT_WASTE);
    }

    @Test
    @Transactional
    void createProductionWasteWithExistingId() throws Exception {
        // Create the ProductionWaste with an existing ID
        productionWaste.setId(1L);

        int databaseSizeBeforeCreate = productionWasteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductionWasteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productionWaste))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionWaste in the database
        List<ProductionWaste> productionWasteList = productionWasteRepository.findAll();
        assertThat(productionWasteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductionWastes() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList
        restProductionWasteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productionWaste.getId().intValue())))
            .andExpect(jsonPath("$.[*].material").value(hasItem(DEFAULT_MATERIAL)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].transportType").value(hasItem(DEFAULT_TRANSPORT_TYPE)))
            .andExpect(jsonPath("$.[*].electric").value(hasItem(DEFAULT_ELECTRIC)))
            .andExpect(jsonPath("$.[*].water").value(hasItem(DEFAULT_WATER)))
            .andExpect(jsonPath("$.[*].waste").value(hasItem(DEFAULT_WASTE)));
    }

    @Test
    @Transactional
    void getProductionWaste() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get the productionWaste
        restProductionWasteMockMvc
            .perform(get(ENTITY_API_URL_ID, productionWaste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productionWaste.getId().intValue()))
            .andExpect(jsonPath("$.material").value(DEFAULT_MATERIAL))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.transportType").value(DEFAULT_TRANSPORT_TYPE))
            .andExpect(jsonPath("$.electric").value(DEFAULT_ELECTRIC))
            .andExpect(jsonPath("$.water").value(DEFAULT_WATER))
            .andExpect(jsonPath("$.waste").value(DEFAULT_WASTE));
    }

    @Test
    @Transactional
    void getProductionWastesByIdFiltering() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        Long id = productionWaste.getId();

        defaultProductionWasteShouldBeFound("id.equals=" + id);
        defaultProductionWasteShouldNotBeFound("id.notEquals=" + id);

        defaultProductionWasteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductionWasteShouldNotBeFound("id.greaterThan=" + id);

        defaultProductionWasteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductionWasteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductionWastesByMaterialIsEqualToSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where material equals to DEFAULT_MATERIAL
        defaultProductionWasteShouldBeFound("material.equals=" + DEFAULT_MATERIAL);

        // Get all the productionWasteList where material equals to UPDATED_MATERIAL
        defaultProductionWasteShouldNotBeFound("material.equals=" + UPDATED_MATERIAL);
    }

    @Test
    @Transactional
    void getAllProductionWastesByMaterialIsInShouldWork() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where material in DEFAULT_MATERIAL or UPDATED_MATERIAL
        defaultProductionWasteShouldBeFound("material.in=" + DEFAULT_MATERIAL + "," + UPDATED_MATERIAL);

        // Get all the productionWasteList where material equals to UPDATED_MATERIAL
        defaultProductionWasteShouldNotBeFound("material.in=" + UPDATED_MATERIAL);
    }

    @Test
    @Transactional
    void getAllProductionWastesByMaterialIsNullOrNotNull() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where material is not null
        defaultProductionWasteShouldBeFound("material.specified=true");

        // Get all the productionWasteList where material is null
        defaultProductionWasteShouldNotBeFound("material.specified=false");
    }

    @Test
    @Transactional
    void getAllProductionWastesByMaterialContainsSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where material contains DEFAULT_MATERIAL
        defaultProductionWasteShouldBeFound("material.contains=" + DEFAULT_MATERIAL);

        // Get all the productionWasteList where material contains UPDATED_MATERIAL
        defaultProductionWasteShouldNotBeFound("material.contains=" + UPDATED_MATERIAL);
    }

    @Test
    @Transactional
    void getAllProductionWastesByMaterialNotContainsSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where material does not contain DEFAULT_MATERIAL
        defaultProductionWasteShouldNotBeFound("material.doesNotContain=" + DEFAULT_MATERIAL);

        // Get all the productionWasteList where material does not contain UPDATED_MATERIAL
        defaultProductionWasteShouldBeFound("material.doesNotContain=" + UPDATED_MATERIAL);
    }

    @Test
    @Transactional
    void getAllProductionWastesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where quantity equals to DEFAULT_QUANTITY
        defaultProductionWasteShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the productionWasteList where quantity equals to UPDATED_QUANTITY
        defaultProductionWasteShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductionWastesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultProductionWasteShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the productionWasteList where quantity equals to UPDATED_QUANTITY
        defaultProductionWasteShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductionWastesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where quantity is not null
        defaultProductionWasteShouldBeFound("quantity.specified=true");

        // Get all the productionWasteList where quantity is null
        defaultProductionWasteShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllProductionWastesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultProductionWasteShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the productionWasteList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultProductionWasteShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductionWastesByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultProductionWasteShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the productionWasteList where quantity is less than or equal to SMALLER_QUANTITY
        defaultProductionWasteShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductionWastesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where quantity is less than DEFAULT_QUANTITY
        defaultProductionWasteShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the productionWasteList where quantity is less than UPDATED_QUANTITY
        defaultProductionWasteShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductionWastesByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where quantity is greater than DEFAULT_QUANTITY
        defaultProductionWasteShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the productionWasteList where quantity is greater than SMALLER_QUANTITY
        defaultProductionWasteShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductionWastesByTransportTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where transportType equals to DEFAULT_TRANSPORT_TYPE
        defaultProductionWasteShouldBeFound("transportType.equals=" + DEFAULT_TRANSPORT_TYPE);

        // Get all the productionWasteList where transportType equals to UPDATED_TRANSPORT_TYPE
        defaultProductionWasteShouldNotBeFound("transportType.equals=" + UPDATED_TRANSPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllProductionWastesByTransportTypeIsInShouldWork() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where transportType in DEFAULT_TRANSPORT_TYPE or UPDATED_TRANSPORT_TYPE
        defaultProductionWasteShouldBeFound("transportType.in=" + DEFAULT_TRANSPORT_TYPE + "," + UPDATED_TRANSPORT_TYPE);

        // Get all the productionWasteList where transportType equals to UPDATED_TRANSPORT_TYPE
        defaultProductionWasteShouldNotBeFound("transportType.in=" + UPDATED_TRANSPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllProductionWastesByTransportTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where transportType is not null
        defaultProductionWasteShouldBeFound("transportType.specified=true");

        // Get all the productionWasteList where transportType is null
        defaultProductionWasteShouldNotBeFound("transportType.specified=false");
    }

    @Test
    @Transactional
    void getAllProductionWastesByTransportTypeContainsSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where transportType contains DEFAULT_TRANSPORT_TYPE
        defaultProductionWasteShouldBeFound("transportType.contains=" + DEFAULT_TRANSPORT_TYPE);

        // Get all the productionWasteList where transportType contains UPDATED_TRANSPORT_TYPE
        defaultProductionWasteShouldNotBeFound("transportType.contains=" + UPDATED_TRANSPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllProductionWastesByTransportTypeNotContainsSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where transportType does not contain DEFAULT_TRANSPORT_TYPE
        defaultProductionWasteShouldNotBeFound("transportType.doesNotContain=" + DEFAULT_TRANSPORT_TYPE);

        // Get all the productionWasteList where transportType does not contain UPDATED_TRANSPORT_TYPE
        defaultProductionWasteShouldBeFound("transportType.doesNotContain=" + UPDATED_TRANSPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllProductionWastesByElectricIsEqualToSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where electric equals to DEFAULT_ELECTRIC
        defaultProductionWasteShouldBeFound("electric.equals=" + DEFAULT_ELECTRIC);

        // Get all the productionWasteList where electric equals to UPDATED_ELECTRIC
        defaultProductionWasteShouldNotBeFound("electric.equals=" + UPDATED_ELECTRIC);
    }

    @Test
    @Transactional
    void getAllProductionWastesByElectricIsInShouldWork() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where electric in DEFAULT_ELECTRIC or UPDATED_ELECTRIC
        defaultProductionWasteShouldBeFound("electric.in=" + DEFAULT_ELECTRIC + "," + UPDATED_ELECTRIC);

        // Get all the productionWasteList where electric equals to UPDATED_ELECTRIC
        defaultProductionWasteShouldNotBeFound("electric.in=" + UPDATED_ELECTRIC);
    }

    @Test
    @Transactional
    void getAllProductionWastesByElectricIsNullOrNotNull() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where electric is not null
        defaultProductionWasteShouldBeFound("electric.specified=true");

        // Get all the productionWasteList where electric is null
        defaultProductionWasteShouldNotBeFound("electric.specified=false");
    }

    @Test
    @Transactional
    void getAllProductionWastesByElectricIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where electric is greater than or equal to DEFAULT_ELECTRIC
        defaultProductionWasteShouldBeFound("electric.greaterThanOrEqual=" + DEFAULT_ELECTRIC);

        // Get all the productionWasteList where electric is greater than or equal to UPDATED_ELECTRIC
        defaultProductionWasteShouldNotBeFound("electric.greaterThanOrEqual=" + UPDATED_ELECTRIC);
    }

    @Test
    @Transactional
    void getAllProductionWastesByElectricIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where electric is less than or equal to DEFAULT_ELECTRIC
        defaultProductionWasteShouldBeFound("electric.lessThanOrEqual=" + DEFAULT_ELECTRIC);

        // Get all the productionWasteList where electric is less than or equal to SMALLER_ELECTRIC
        defaultProductionWasteShouldNotBeFound("electric.lessThanOrEqual=" + SMALLER_ELECTRIC);
    }

    @Test
    @Transactional
    void getAllProductionWastesByElectricIsLessThanSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where electric is less than DEFAULT_ELECTRIC
        defaultProductionWasteShouldNotBeFound("electric.lessThan=" + DEFAULT_ELECTRIC);

        // Get all the productionWasteList where electric is less than UPDATED_ELECTRIC
        defaultProductionWasteShouldBeFound("electric.lessThan=" + UPDATED_ELECTRIC);
    }

    @Test
    @Transactional
    void getAllProductionWastesByElectricIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where electric is greater than DEFAULT_ELECTRIC
        defaultProductionWasteShouldNotBeFound("electric.greaterThan=" + DEFAULT_ELECTRIC);

        // Get all the productionWasteList where electric is greater than SMALLER_ELECTRIC
        defaultProductionWasteShouldBeFound("electric.greaterThan=" + SMALLER_ELECTRIC);
    }

    @Test
    @Transactional
    void getAllProductionWastesByWaterIsEqualToSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where water equals to DEFAULT_WATER
        defaultProductionWasteShouldBeFound("water.equals=" + DEFAULT_WATER);

        // Get all the productionWasteList where water equals to UPDATED_WATER
        defaultProductionWasteShouldNotBeFound("water.equals=" + UPDATED_WATER);
    }

    @Test
    @Transactional
    void getAllProductionWastesByWaterIsInShouldWork() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where water in DEFAULT_WATER or UPDATED_WATER
        defaultProductionWasteShouldBeFound("water.in=" + DEFAULT_WATER + "," + UPDATED_WATER);

        // Get all the productionWasteList where water equals to UPDATED_WATER
        defaultProductionWasteShouldNotBeFound("water.in=" + UPDATED_WATER);
    }

    @Test
    @Transactional
    void getAllProductionWastesByWaterIsNullOrNotNull() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where water is not null
        defaultProductionWasteShouldBeFound("water.specified=true");

        // Get all the productionWasteList where water is null
        defaultProductionWasteShouldNotBeFound("water.specified=false");
    }

    @Test
    @Transactional
    void getAllProductionWastesByWaterIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where water is greater than or equal to DEFAULT_WATER
        defaultProductionWasteShouldBeFound("water.greaterThanOrEqual=" + DEFAULT_WATER);

        // Get all the productionWasteList where water is greater than or equal to UPDATED_WATER
        defaultProductionWasteShouldNotBeFound("water.greaterThanOrEqual=" + UPDATED_WATER);
    }

    @Test
    @Transactional
    void getAllProductionWastesByWaterIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where water is less than or equal to DEFAULT_WATER
        defaultProductionWasteShouldBeFound("water.lessThanOrEqual=" + DEFAULT_WATER);

        // Get all the productionWasteList where water is less than or equal to SMALLER_WATER
        defaultProductionWasteShouldNotBeFound("water.lessThanOrEqual=" + SMALLER_WATER);
    }

    @Test
    @Transactional
    void getAllProductionWastesByWaterIsLessThanSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where water is less than DEFAULT_WATER
        defaultProductionWasteShouldNotBeFound("water.lessThan=" + DEFAULT_WATER);

        // Get all the productionWasteList where water is less than UPDATED_WATER
        defaultProductionWasteShouldBeFound("water.lessThan=" + UPDATED_WATER);
    }

    @Test
    @Transactional
    void getAllProductionWastesByWaterIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where water is greater than DEFAULT_WATER
        defaultProductionWasteShouldNotBeFound("water.greaterThan=" + DEFAULT_WATER);

        // Get all the productionWasteList where water is greater than SMALLER_WATER
        defaultProductionWasteShouldBeFound("water.greaterThan=" + SMALLER_WATER);
    }

    @Test
    @Transactional
    void getAllProductionWastesByWasteIsEqualToSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where waste equals to DEFAULT_WASTE
        defaultProductionWasteShouldBeFound("waste.equals=" + DEFAULT_WASTE);

        // Get all the productionWasteList where waste equals to UPDATED_WASTE
        defaultProductionWasteShouldNotBeFound("waste.equals=" + UPDATED_WASTE);
    }

    @Test
    @Transactional
    void getAllProductionWastesByWasteIsInShouldWork() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where waste in DEFAULT_WASTE or UPDATED_WASTE
        defaultProductionWasteShouldBeFound("waste.in=" + DEFAULT_WASTE + "," + UPDATED_WASTE);

        // Get all the productionWasteList where waste equals to UPDATED_WASTE
        defaultProductionWasteShouldNotBeFound("waste.in=" + UPDATED_WASTE);
    }

    @Test
    @Transactional
    void getAllProductionWastesByWasteIsNullOrNotNull() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where waste is not null
        defaultProductionWasteShouldBeFound("waste.specified=true");

        // Get all the productionWasteList where waste is null
        defaultProductionWasteShouldNotBeFound("waste.specified=false");
    }

    @Test
    @Transactional
    void getAllProductionWastesByWasteContainsSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where waste contains DEFAULT_WASTE
        defaultProductionWasteShouldBeFound("waste.contains=" + DEFAULT_WASTE);

        // Get all the productionWasteList where waste contains UPDATED_WASTE
        defaultProductionWasteShouldNotBeFound("waste.contains=" + UPDATED_WASTE);
    }

    @Test
    @Transactional
    void getAllProductionWastesByWasteNotContainsSomething() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        // Get all the productionWasteList where waste does not contain DEFAULT_WASTE
        defaultProductionWasteShouldNotBeFound("waste.doesNotContain=" + DEFAULT_WASTE);

        // Get all the productionWasteList where waste does not contain UPDATED_WASTE
        defaultProductionWasteShouldBeFound("waste.doesNotContain=" + UPDATED_WASTE);
    }

    @Test
    @Transactional
    void getAllProductionWastesByDepartmentIsEqualToSomething() throws Exception {
        Departments department;
        if (TestUtil.findAll(em, Departments.class).isEmpty()) {
            productionWasteRepository.saveAndFlush(productionWaste);
            department = DepartmentsResourceIT.createEntity(em);
        } else {
            department = TestUtil.findAll(em, Departments.class).get(0);
        }
        em.persist(department);
        em.flush();
        productionWaste.setDepartment(department);
        productionWasteRepository.saveAndFlush(productionWaste);
        Long departmentId = department.getId();

        // Get all the productionWasteList where department equals to departmentId
        defaultProductionWasteShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the productionWasteList where department equals to (departmentId + 1)
        defaultProductionWasteShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductionWasteShouldBeFound(String filter) throws Exception {
        restProductionWasteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productionWaste.getId().intValue())))
            .andExpect(jsonPath("$.[*].material").value(hasItem(DEFAULT_MATERIAL)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].transportType").value(hasItem(DEFAULT_TRANSPORT_TYPE)))
            .andExpect(jsonPath("$.[*].electric").value(hasItem(DEFAULT_ELECTRIC)))
            .andExpect(jsonPath("$.[*].water").value(hasItem(DEFAULT_WATER)))
            .andExpect(jsonPath("$.[*].waste").value(hasItem(DEFAULT_WASTE)));

        // Check, that the count call also returns 1
        restProductionWasteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductionWasteShouldNotBeFound(String filter) throws Exception {
        restProductionWasteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductionWasteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductionWaste() throws Exception {
        // Get the productionWaste
        restProductionWasteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductionWaste() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        int databaseSizeBeforeUpdate = productionWasteRepository.findAll().size();

        // Update the productionWaste
        ProductionWaste updatedProductionWaste = productionWasteRepository.findById(productionWaste.getId()).get();
        // Disconnect from session so that the updates on updatedProductionWaste are not directly saved in db
        em.detach(updatedProductionWaste);
        updatedProductionWaste
            .material(UPDATED_MATERIAL)
            .quantity(UPDATED_QUANTITY)
            .transportType(UPDATED_TRANSPORT_TYPE)
            .electric(UPDATED_ELECTRIC)
            .water(UPDATED_WATER)
            .waste(UPDATED_WASTE);

        restProductionWasteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductionWaste.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductionWaste))
            )
            .andExpect(status().isOk());

        // Validate the ProductionWaste in the database
        List<ProductionWaste> productionWasteList = productionWasteRepository.findAll();
        assertThat(productionWasteList).hasSize(databaseSizeBeforeUpdate);
        ProductionWaste testProductionWaste = productionWasteList.get(productionWasteList.size() - 1);
        assertThat(testProductionWaste.getMaterial()).isEqualTo(UPDATED_MATERIAL);
        assertThat(testProductionWaste.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testProductionWaste.getTransportType()).isEqualTo(UPDATED_TRANSPORT_TYPE);
        assertThat(testProductionWaste.getElectric()).isEqualTo(UPDATED_ELECTRIC);
        assertThat(testProductionWaste.getWater()).isEqualTo(UPDATED_WATER);
        assertThat(testProductionWaste.getWaste()).isEqualTo(UPDATED_WASTE);
    }

    @Test
    @Transactional
    void putNonExistingProductionWaste() throws Exception {
        int databaseSizeBeforeUpdate = productionWasteRepository.findAll().size();
        productionWaste.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductionWasteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productionWaste.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productionWaste))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionWaste in the database
        List<ProductionWaste> productionWasteList = productionWasteRepository.findAll();
        assertThat(productionWasteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductionWaste() throws Exception {
        int databaseSizeBeforeUpdate = productionWasteRepository.findAll().size();
        productionWaste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionWasteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productionWaste))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionWaste in the database
        List<ProductionWaste> productionWasteList = productionWasteRepository.findAll();
        assertThat(productionWasteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductionWaste() throws Exception {
        int databaseSizeBeforeUpdate = productionWasteRepository.findAll().size();
        productionWaste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionWasteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productionWaste))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductionWaste in the database
        List<ProductionWaste> productionWasteList = productionWasteRepository.findAll();
        assertThat(productionWasteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductionWasteWithPatch() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        int databaseSizeBeforeUpdate = productionWasteRepository.findAll().size();

        // Update the productionWaste using partial update
        ProductionWaste partialUpdatedProductionWaste = new ProductionWaste();
        partialUpdatedProductionWaste.setId(productionWaste.getId());

        partialUpdatedProductionWaste.material(UPDATED_MATERIAL).transportType(UPDATED_TRANSPORT_TYPE);

        restProductionWasteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductionWaste.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductionWaste))
            )
            .andExpect(status().isOk());

        // Validate the ProductionWaste in the database
        List<ProductionWaste> productionWasteList = productionWasteRepository.findAll();
        assertThat(productionWasteList).hasSize(databaseSizeBeforeUpdate);
        ProductionWaste testProductionWaste = productionWasteList.get(productionWasteList.size() - 1);
        assertThat(testProductionWaste.getMaterial()).isEqualTo(UPDATED_MATERIAL);
        assertThat(testProductionWaste.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testProductionWaste.getTransportType()).isEqualTo(UPDATED_TRANSPORT_TYPE);
        assertThat(testProductionWaste.getElectric()).isEqualTo(DEFAULT_ELECTRIC);
        assertThat(testProductionWaste.getWater()).isEqualTo(DEFAULT_WATER);
        assertThat(testProductionWaste.getWaste()).isEqualTo(DEFAULT_WASTE);
    }

    @Test
    @Transactional
    void fullUpdateProductionWasteWithPatch() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        int databaseSizeBeforeUpdate = productionWasteRepository.findAll().size();

        // Update the productionWaste using partial update
        ProductionWaste partialUpdatedProductionWaste = new ProductionWaste();
        partialUpdatedProductionWaste.setId(productionWaste.getId());

        partialUpdatedProductionWaste
            .material(UPDATED_MATERIAL)
            .quantity(UPDATED_QUANTITY)
            .transportType(UPDATED_TRANSPORT_TYPE)
            .electric(UPDATED_ELECTRIC)
            .water(UPDATED_WATER)
            .waste(UPDATED_WASTE);

        restProductionWasteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductionWaste.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductionWaste))
            )
            .andExpect(status().isOk());

        // Validate the ProductionWaste in the database
        List<ProductionWaste> productionWasteList = productionWasteRepository.findAll();
        assertThat(productionWasteList).hasSize(databaseSizeBeforeUpdate);
        ProductionWaste testProductionWaste = productionWasteList.get(productionWasteList.size() - 1);
        assertThat(testProductionWaste.getMaterial()).isEqualTo(UPDATED_MATERIAL);
        assertThat(testProductionWaste.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testProductionWaste.getTransportType()).isEqualTo(UPDATED_TRANSPORT_TYPE);
        assertThat(testProductionWaste.getElectric()).isEqualTo(UPDATED_ELECTRIC);
        assertThat(testProductionWaste.getWater()).isEqualTo(UPDATED_WATER);
        assertThat(testProductionWaste.getWaste()).isEqualTo(UPDATED_WASTE);
    }

    @Test
    @Transactional
    void patchNonExistingProductionWaste() throws Exception {
        int databaseSizeBeforeUpdate = productionWasteRepository.findAll().size();
        productionWaste.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductionWasteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productionWaste.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productionWaste))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionWaste in the database
        List<ProductionWaste> productionWasteList = productionWasteRepository.findAll();
        assertThat(productionWasteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductionWaste() throws Exception {
        int databaseSizeBeforeUpdate = productionWasteRepository.findAll().size();
        productionWaste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionWasteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productionWaste))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductionWaste in the database
        List<ProductionWaste> productionWasteList = productionWasteRepository.findAll();
        assertThat(productionWasteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductionWaste() throws Exception {
        int databaseSizeBeforeUpdate = productionWasteRepository.findAll().size();
        productionWaste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductionWasteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productionWaste))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductionWaste in the database
        List<ProductionWaste> productionWasteList = productionWasteRepository.findAll();
        assertThat(productionWasteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductionWaste() throws Exception {
        // Initialize the database
        productionWasteRepository.saveAndFlush(productionWaste);

        int databaseSizeBeforeDelete = productionWasteRepository.findAll().size();

        // Delete the productionWaste
        restProductionWasteMockMvc
            .perform(delete(ENTITY_API_URL_ID, productionWaste.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductionWaste> productionWasteList = productionWasteRepository.findAll();
        assertThat(productionWasteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
