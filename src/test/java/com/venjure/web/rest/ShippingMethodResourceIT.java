package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Channel;
import com.venjure.domain.ShippingMethod;
import com.venjure.domain.ShippingMethodTranslation;
import com.venjure.repository.ShippingMethodRepository;
import com.venjure.service.criteria.ShippingMethodCriteria;
import com.venjure.service.dto.ShippingMethodDTO;
import com.venjure.service.mapper.ShippingMethodMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ShippingMethodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShippingMethodResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CHECKER = "AAAAAAAAAA";
    private static final String UPDATED_CHECKER = "BBBBBBBBBB";

    private static final String DEFAULT_CALCULATOR = "AAAAAAAAAA";
    private static final String UPDATED_CALCULATOR = "BBBBBBBBBB";

    private static final String DEFAULT_FULFILLMENTHANDLERCODE = "AAAAAAAAAA";
    private static final String UPDATED_FULFILLMENTHANDLERCODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shipping-methods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    @Autowired
    private ShippingMethodMapper shippingMethodMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShippingMethodMockMvc;

    private ShippingMethod shippingMethod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShippingMethod createEntity(EntityManager em) {
        ShippingMethod shippingMethod = new ShippingMethod()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .deletedat(DEFAULT_DELETEDAT)
            .code(DEFAULT_CODE)
            .checker(DEFAULT_CHECKER)
            .calculator(DEFAULT_CALCULATOR)
            .fulfillmenthandlercode(DEFAULT_FULFILLMENTHANDLERCODE);
        return shippingMethod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShippingMethod createUpdatedEntity(EntityManager em) {
        ShippingMethod shippingMethod = new ShippingMethod()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .code(UPDATED_CODE)
            .checker(UPDATED_CHECKER)
            .calculator(UPDATED_CALCULATOR)
            .fulfillmenthandlercode(UPDATED_FULFILLMENTHANDLERCODE);
        return shippingMethod;
    }

    @BeforeEach
    public void initTest() {
        shippingMethod = createEntity(em);
    }

    @Test
    @Transactional
    void createShippingMethod() throws Exception {
        int databaseSizeBeforeCreate = shippingMethodRepository.findAll().size();
        // Create the ShippingMethod
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);
        restShippingMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeCreate + 1);
        ShippingMethod testShippingMethod = shippingMethodList.get(shippingMethodList.size() - 1);
        assertThat(testShippingMethod.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testShippingMethod.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testShippingMethod.getDeletedat()).isEqualTo(DEFAULT_DELETEDAT);
        assertThat(testShippingMethod.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testShippingMethod.getChecker()).isEqualTo(DEFAULT_CHECKER);
        assertThat(testShippingMethod.getCalculator()).isEqualTo(DEFAULT_CALCULATOR);
        assertThat(testShippingMethod.getFulfillmenthandlercode()).isEqualTo(DEFAULT_FULFILLMENTHANDLERCODE);
    }

    @Test
    @Transactional
    void createShippingMethodWithExistingId() throws Exception {
        // Create the ShippingMethod with an existing ID
        shippingMethod.setId(1L);
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        int databaseSizeBeforeCreate = shippingMethodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShippingMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingMethodRepository.findAll().size();
        // set the field null
        shippingMethod.setCreatedat(null);

        // Create the ShippingMethod, which fails.
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        restShippingMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingMethodRepository.findAll().size();
        // set the field null
        shippingMethod.setUpdatedat(null);

        // Create the ShippingMethod, which fails.
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        restShippingMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingMethodRepository.findAll().size();
        // set the field null
        shippingMethod.setCode(null);

        // Create the ShippingMethod, which fails.
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        restShippingMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCheckerIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingMethodRepository.findAll().size();
        // set the field null
        shippingMethod.setChecker(null);

        // Create the ShippingMethod, which fails.
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        restShippingMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCalculatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingMethodRepository.findAll().size();
        // set the field null
        shippingMethod.setCalculator(null);

        // Create the ShippingMethod, which fails.
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        restShippingMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFulfillmenthandlercodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingMethodRepository.findAll().size();
        // set the field null
        shippingMethod.setFulfillmenthandlercode(null);

        // Create the ShippingMethod, which fails.
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        restShippingMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShippingMethods() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList
        restShippingMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shippingMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].checker").value(hasItem(DEFAULT_CHECKER)))
            .andExpect(jsonPath("$.[*].calculator").value(hasItem(DEFAULT_CALCULATOR)))
            .andExpect(jsonPath("$.[*].fulfillmenthandlercode").value(hasItem(DEFAULT_FULFILLMENTHANDLERCODE)));
    }

    @Test
    @Transactional
    void getShippingMethod() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get the shippingMethod
        restShippingMethodMockMvc
            .perform(get(ENTITY_API_URL_ID, shippingMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shippingMethod.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.deletedat").value(DEFAULT_DELETEDAT.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.checker").value(DEFAULT_CHECKER))
            .andExpect(jsonPath("$.calculator").value(DEFAULT_CALCULATOR))
            .andExpect(jsonPath("$.fulfillmenthandlercode").value(DEFAULT_FULFILLMENTHANDLERCODE));
    }

    @Test
    @Transactional
    void getShippingMethodsByIdFiltering() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        Long id = shippingMethod.getId();

        defaultShippingMethodShouldBeFound("id.equals=" + id);
        defaultShippingMethodShouldNotBeFound("id.notEquals=" + id);

        defaultShippingMethodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultShippingMethodShouldNotBeFound("id.greaterThan=" + id);

        defaultShippingMethodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultShippingMethodShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where createdat equals to DEFAULT_CREATEDAT
        defaultShippingMethodShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the shippingMethodList where createdat equals to UPDATED_CREATEDAT
        defaultShippingMethodShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where createdat not equals to DEFAULT_CREATEDAT
        defaultShippingMethodShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the shippingMethodList where createdat not equals to UPDATED_CREATEDAT
        defaultShippingMethodShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultShippingMethodShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the shippingMethodList where createdat equals to UPDATED_CREATEDAT
        defaultShippingMethodShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where createdat is not null
        defaultShippingMethodShouldBeFound("createdat.specified=true");

        // Get all the shippingMethodList where createdat is null
        defaultShippingMethodShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingMethodsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where updatedat equals to DEFAULT_UPDATEDAT
        defaultShippingMethodShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the shippingMethodList where updatedat equals to UPDATED_UPDATEDAT
        defaultShippingMethodShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultShippingMethodShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the shippingMethodList where updatedat not equals to UPDATED_UPDATEDAT
        defaultShippingMethodShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultShippingMethodShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the shippingMethodList where updatedat equals to UPDATED_UPDATEDAT
        defaultShippingMethodShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where updatedat is not null
        defaultShippingMethodShouldBeFound("updatedat.specified=true");

        // Get all the shippingMethodList where updatedat is null
        defaultShippingMethodShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingMethodsByDeletedatIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where deletedat equals to DEFAULT_DELETEDAT
        defaultShippingMethodShouldBeFound("deletedat.equals=" + DEFAULT_DELETEDAT);

        // Get all the shippingMethodList where deletedat equals to UPDATED_DELETEDAT
        defaultShippingMethodShouldNotBeFound("deletedat.equals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByDeletedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where deletedat not equals to DEFAULT_DELETEDAT
        defaultShippingMethodShouldNotBeFound("deletedat.notEquals=" + DEFAULT_DELETEDAT);

        // Get all the shippingMethodList where deletedat not equals to UPDATED_DELETEDAT
        defaultShippingMethodShouldBeFound("deletedat.notEquals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByDeletedatIsInShouldWork() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where deletedat in DEFAULT_DELETEDAT or UPDATED_DELETEDAT
        defaultShippingMethodShouldBeFound("deletedat.in=" + DEFAULT_DELETEDAT + "," + UPDATED_DELETEDAT);

        // Get all the shippingMethodList where deletedat equals to UPDATED_DELETEDAT
        defaultShippingMethodShouldNotBeFound("deletedat.in=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByDeletedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where deletedat is not null
        defaultShippingMethodShouldBeFound("deletedat.specified=true");

        // Get all the shippingMethodList where deletedat is null
        defaultShippingMethodShouldNotBeFound("deletedat.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where code equals to DEFAULT_CODE
        defaultShippingMethodShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the shippingMethodList where code equals to UPDATED_CODE
        defaultShippingMethodShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where code not equals to DEFAULT_CODE
        defaultShippingMethodShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the shippingMethodList where code not equals to UPDATED_CODE
        defaultShippingMethodShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where code in DEFAULT_CODE or UPDATED_CODE
        defaultShippingMethodShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the shippingMethodList where code equals to UPDATED_CODE
        defaultShippingMethodShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where code is not null
        defaultShippingMethodShouldBeFound("code.specified=true");

        // Get all the shippingMethodList where code is null
        defaultShippingMethodShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCodeContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where code contains DEFAULT_CODE
        defaultShippingMethodShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the shippingMethodList where code contains UPDATED_CODE
        defaultShippingMethodShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where code does not contain DEFAULT_CODE
        defaultShippingMethodShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the shippingMethodList where code does not contain UPDATED_CODE
        defaultShippingMethodShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCheckerIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where checker equals to DEFAULT_CHECKER
        defaultShippingMethodShouldBeFound("checker.equals=" + DEFAULT_CHECKER);

        // Get all the shippingMethodList where checker equals to UPDATED_CHECKER
        defaultShippingMethodShouldNotBeFound("checker.equals=" + UPDATED_CHECKER);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCheckerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where checker not equals to DEFAULT_CHECKER
        defaultShippingMethodShouldNotBeFound("checker.notEquals=" + DEFAULT_CHECKER);

        // Get all the shippingMethodList where checker not equals to UPDATED_CHECKER
        defaultShippingMethodShouldBeFound("checker.notEquals=" + UPDATED_CHECKER);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCheckerIsInShouldWork() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where checker in DEFAULT_CHECKER or UPDATED_CHECKER
        defaultShippingMethodShouldBeFound("checker.in=" + DEFAULT_CHECKER + "," + UPDATED_CHECKER);

        // Get all the shippingMethodList where checker equals to UPDATED_CHECKER
        defaultShippingMethodShouldNotBeFound("checker.in=" + UPDATED_CHECKER);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCheckerIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where checker is not null
        defaultShippingMethodShouldBeFound("checker.specified=true");

        // Get all the shippingMethodList where checker is null
        defaultShippingMethodShouldNotBeFound("checker.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCheckerContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where checker contains DEFAULT_CHECKER
        defaultShippingMethodShouldBeFound("checker.contains=" + DEFAULT_CHECKER);

        // Get all the shippingMethodList where checker contains UPDATED_CHECKER
        defaultShippingMethodShouldNotBeFound("checker.contains=" + UPDATED_CHECKER);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCheckerNotContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where checker does not contain DEFAULT_CHECKER
        defaultShippingMethodShouldNotBeFound("checker.doesNotContain=" + DEFAULT_CHECKER);

        // Get all the shippingMethodList where checker does not contain UPDATED_CHECKER
        defaultShippingMethodShouldBeFound("checker.doesNotContain=" + UPDATED_CHECKER);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCalculatorIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where calculator equals to DEFAULT_CALCULATOR
        defaultShippingMethodShouldBeFound("calculator.equals=" + DEFAULT_CALCULATOR);

        // Get all the shippingMethodList where calculator equals to UPDATED_CALCULATOR
        defaultShippingMethodShouldNotBeFound("calculator.equals=" + UPDATED_CALCULATOR);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCalculatorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where calculator not equals to DEFAULT_CALCULATOR
        defaultShippingMethodShouldNotBeFound("calculator.notEquals=" + DEFAULT_CALCULATOR);

        // Get all the shippingMethodList where calculator not equals to UPDATED_CALCULATOR
        defaultShippingMethodShouldBeFound("calculator.notEquals=" + UPDATED_CALCULATOR);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCalculatorIsInShouldWork() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where calculator in DEFAULT_CALCULATOR or UPDATED_CALCULATOR
        defaultShippingMethodShouldBeFound("calculator.in=" + DEFAULT_CALCULATOR + "," + UPDATED_CALCULATOR);

        // Get all the shippingMethodList where calculator equals to UPDATED_CALCULATOR
        defaultShippingMethodShouldNotBeFound("calculator.in=" + UPDATED_CALCULATOR);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCalculatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where calculator is not null
        defaultShippingMethodShouldBeFound("calculator.specified=true");

        // Get all the shippingMethodList where calculator is null
        defaultShippingMethodShouldNotBeFound("calculator.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCalculatorContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where calculator contains DEFAULT_CALCULATOR
        defaultShippingMethodShouldBeFound("calculator.contains=" + DEFAULT_CALCULATOR);

        // Get all the shippingMethodList where calculator contains UPDATED_CALCULATOR
        defaultShippingMethodShouldNotBeFound("calculator.contains=" + UPDATED_CALCULATOR);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByCalculatorNotContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where calculator does not contain DEFAULT_CALCULATOR
        defaultShippingMethodShouldNotBeFound("calculator.doesNotContain=" + DEFAULT_CALCULATOR);

        // Get all the shippingMethodList where calculator does not contain UPDATED_CALCULATOR
        defaultShippingMethodShouldBeFound("calculator.doesNotContain=" + UPDATED_CALCULATOR);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByFulfillmenthandlercodeIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where fulfillmenthandlercode equals to DEFAULT_FULFILLMENTHANDLERCODE
        defaultShippingMethodShouldBeFound("fulfillmenthandlercode.equals=" + DEFAULT_FULFILLMENTHANDLERCODE);

        // Get all the shippingMethodList where fulfillmenthandlercode equals to UPDATED_FULFILLMENTHANDLERCODE
        defaultShippingMethodShouldNotBeFound("fulfillmenthandlercode.equals=" + UPDATED_FULFILLMENTHANDLERCODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByFulfillmenthandlercodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where fulfillmenthandlercode not equals to DEFAULT_FULFILLMENTHANDLERCODE
        defaultShippingMethodShouldNotBeFound("fulfillmenthandlercode.notEquals=" + DEFAULT_FULFILLMENTHANDLERCODE);

        // Get all the shippingMethodList where fulfillmenthandlercode not equals to UPDATED_FULFILLMENTHANDLERCODE
        defaultShippingMethodShouldBeFound("fulfillmenthandlercode.notEquals=" + UPDATED_FULFILLMENTHANDLERCODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByFulfillmenthandlercodeIsInShouldWork() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where fulfillmenthandlercode in DEFAULT_FULFILLMENTHANDLERCODE or UPDATED_FULFILLMENTHANDLERCODE
        defaultShippingMethodShouldBeFound(
            "fulfillmenthandlercode.in=" + DEFAULT_FULFILLMENTHANDLERCODE + "," + UPDATED_FULFILLMENTHANDLERCODE
        );

        // Get all the shippingMethodList where fulfillmenthandlercode equals to UPDATED_FULFILLMENTHANDLERCODE
        defaultShippingMethodShouldNotBeFound("fulfillmenthandlercode.in=" + UPDATED_FULFILLMENTHANDLERCODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByFulfillmenthandlercodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where fulfillmenthandlercode is not null
        defaultShippingMethodShouldBeFound("fulfillmenthandlercode.specified=true");

        // Get all the shippingMethodList where fulfillmenthandlercode is null
        defaultShippingMethodShouldNotBeFound("fulfillmenthandlercode.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingMethodsByFulfillmenthandlercodeContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where fulfillmenthandlercode contains DEFAULT_FULFILLMENTHANDLERCODE
        defaultShippingMethodShouldBeFound("fulfillmenthandlercode.contains=" + DEFAULT_FULFILLMENTHANDLERCODE);

        // Get all the shippingMethodList where fulfillmenthandlercode contains UPDATED_FULFILLMENTHANDLERCODE
        defaultShippingMethodShouldNotBeFound("fulfillmenthandlercode.contains=" + UPDATED_FULFILLMENTHANDLERCODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByFulfillmenthandlercodeNotContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        // Get all the shippingMethodList where fulfillmenthandlercode does not contain DEFAULT_FULFILLMENTHANDLERCODE
        defaultShippingMethodShouldNotBeFound("fulfillmenthandlercode.doesNotContain=" + DEFAULT_FULFILLMENTHANDLERCODE);

        // Get all the shippingMethodList where fulfillmenthandlercode does not contain UPDATED_FULFILLMENTHANDLERCODE
        defaultShippingMethodShouldBeFound("fulfillmenthandlercode.doesNotContain=" + UPDATED_FULFILLMENTHANDLERCODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodsByShippingMethodTranslationIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);
        ShippingMethodTranslation shippingMethodTranslation = ShippingMethodTranslationResourceIT.createEntity(em);
        em.persist(shippingMethodTranslation);
        em.flush();
        shippingMethod.addShippingMethodTranslation(shippingMethodTranslation);
        shippingMethodRepository.saveAndFlush(shippingMethod);
        Long shippingMethodTranslationId = shippingMethodTranslation.getId();

        // Get all the shippingMethodList where shippingMethodTranslation equals to shippingMethodTranslationId
        defaultShippingMethodShouldBeFound("shippingMethodTranslationId.equals=" + shippingMethodTranslationId);

        // Get all the shippingMethodList where shippingMethodTranslation equals to (shippingMethodTranslationId + 1)
        defaultShippingMethodShouldNotBeFound("shippingMethodTranslationId.equals=" + (shippingMethodTranslationId + 1));
    }

    @Test
    @Transactional
    void getAllShippingMethodsByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);
        Channel channel = ChannelResourceIT.createEntity(em);
        em.persist(channel);
        em.flush();
        shippingMethod.addChannel(channel);
        shippingMethodRepository.saveAndFlush(shippingMethod);
        Long channelId = channel.getId();

        // Get all the shippingMethodList where channel equals to channelId
        defaultShippingMethodShouldBeFound("channelId.equals=" + channelId);

        // Get all the shippingMethodList where channel equals to (channelId + 1)
        defaultShippingMethodShouldNotBeFound("channelId.equals=" + (channelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShippingMethodShouldBeFound(String filter) throws Exception {
        restShippingMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shippingMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].checker").value(hasItem(DEFAULT_CHECKER)))
            .andExpect(jsonPath("$.[*].calculator").value(hasItem(DEFAULT_CALCULATOR)))
            .andExpect(jsonPath("$.[*].fulfillmenthandlercode").value(hasItem(DEFAULT_FULFILLMENTHANDLERCODE)));

        // Check, that the count call also returns 1
        restShippingMethodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShippingMethodShouldNotBeFound(String filter) throws Exception {
        restShippingMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShippingMethodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShippingMethod() throws Exception {
        // Get the shippingMethod
        restShippingMethodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShippingMethod() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();

        // Update the shippingMethod
        ShippingMethod updatedShippingMethod = shippingMethodRepository.findById(shippingMethod.getId()).get();
        // Disconnect from session so that the updates on updatedShippingMethod are not directly saved in db
        em.detach(updatedShippingMethod);
        updatedShippingMethod
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .code(UPDATED_CODE)
            .checker(UPDATED_CHECKER)
            .calculator(UPDATED_CALCULATOR)
            .fulfillmenthandlercode(UPDATED_FULFILLMENTHANDLERCODE);
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(updatedShippingMethod);

        restShippingMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shippingMethodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate);
        ShippingMethod testShippingMethod = shippingMethodList.get(shippingMethodList.size() - 1);
        assertThat(testShippingMethod.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testShippingMethod.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testShippingMethod.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testShippingMethod.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testShippingMethod.getChecker()).isEqualTo(UPDATED_CHECKER);
        assertThat(testShippingMethod.getCalculator()).isEqualTo(UPDATED_CALCULATOR);
        assertThat(testShippingMethod.getFulfillmenthandlercode()).isEqualTo(UPDATED_FULFILLMENTHANDLERCODE);
    }

    @Test
    @Transactional
    void putNonExistingShippingMethod() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();
        shippingMethod.setId(count.incrementAndGet());

        // Create the ShippingMethod
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shippingMethodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShippingMethod() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();
        shippingMethod.setId(count.incrementAndGet());

        // Create the ShippingMethod
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShippingMethod() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();
        shippingMethod.setId(count.incrementAndGet());

        // Create the ShippingMethod
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMethodMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShippingMethodWithPatch() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();

        // Update the shippingMethod using partial update
        ShippingMethod partialUpdatedShippingMethod = new ShippingMethod();
        partialUpdatedShippingMethod.setId(shippingMethod.getId());

        partialUpdatedShippingMethod.code(UPDATED_CODE).fulfillmenthandlercode(UPDATED_FULFILLMENTHANDLERCODE);

        restShippingMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShippingMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShippingMethod))
            )
            .andExpect(status().isOk());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate);
        ShippingMethod testShippingMethod = shippingMethodList.get(shippingMethodList.size() - 1);
        assertThat(testShippingMethod.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testShippingMethod.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testShippingMethod.getDeletedat()).isEqualTo(DEFAULT_DELETEDAT);
        assertThat(testShippingMethod.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testShippingMethod.getChecker()).isEqualTo(DEFAULT_CHECKER);
        assertThat(testShippingMethod.getCalculator()).isEqualTo(DEFAULT_CALCULATOR);
        assertThat(testShippingMethod.getFulfillmenthandlercode()).isEqualTo(UPDATED_FULFILLMENTHANDLERCODE);
    }

    @Test
    @Transactional
    void fullUpdateShippingMethodWithPatch() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();

        // Update the shippingMethod using partial update
        ShippingMethod partialUpdatedShippingMethod = new ShippingMethod();
        partialUpdatedShippingMethod.setId(shippingMethod.getId());

        partialUpdatedShippingMethod
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .code(UPDATED_CODE)
            .checker(UPDATED_CHECKER)
            .calculator(UPDATED_CALCULATOR)
            .fulfillmenthandlercode(UPDATED_FULFILLMENTHANDLERCODE);

        restShippingMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShippingMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShippingMethod))
            )
            .andExpect(status().isOk());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate);
        ShippingMethod testShippingMethod = shippingMethodList.get(shippingMethodList.size() - 1);
        assertThat(testShippingMethod.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testShippingMethod.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testShippingMethod.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testShippingMethod.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testShippingMethod.getChecker()).isEqualTo(UPDATED_CHECKER);
        assertThat(testShippingMethod.getCalculator()).isEqualTo(UPDATED_CALCULATOR);
        assertThat(testShippingMethod.getFulfillmenthandlercode()).isEqualTo(UPDATED_FULFILLMENTHANDLERCODE);
    }

    @Test
    @Transactional
    void patchNonExistingShippingMethod() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();
        shippingMethod.setId(count.incrementAndGet());

        // Create the ShippingMethod
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shippingMethodDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShippingMethod() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();
        shippingMethod.setId(count.incrementAndGet());

        // Create the ShippingMethod
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShippingMethod() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodRepository.findAll().size();
        shippingMethod.setId(count.incrementAndGet());

        // Create the ShippingMethod
        ShippingMethodDTO shippingMethodDTO = shippingMethodMapper.toDto(shippingMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMethodMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShippingMethod in the database
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShippingMethod() throws Exception {
        // Initialize the database
        shippingMethodRepository.saveAndFlush(shippingMethod);

        int databaseSizeBeforeDelete = shippingMethodRepository.findAll().size();

        // Delete the shippingMethod
        restShippingMethodMockMvc
            .perform(delete(ENTITY_API_URL_ID, shippingMethod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShippingMethod> shippingMethodList = shippingMethodRepository.findAll();
        assertThat(shippingMethodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
