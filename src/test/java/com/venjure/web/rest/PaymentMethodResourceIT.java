package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Channel;
import com.venjure.domain.PaymentMethod;
import com.venjure.repository.PaymentMethodRepository;
import com.venjure.service.criteria.PaymentMethodCriteria;
import com.venjure.service.dto.PaymentMethodDTO;
import com.venjure.service.mapper.PaymentMethodMapper;
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
 * Integration tests for the {@link PaymentMethodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentMethodResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String DEFAULT_CHECKER = "AAAAAAAAAA";
    private static final String UPDATED_CHECKER = "BBBBBBBBBB";

    private static final String DEFAULT_HANDLER = "AAAAAAAAAA";
    private static final String UPDATED_HANDLER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-methods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMethodMockMvc;

    private PaymentMethod paymentMethod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMethod createEntity(EntityManager em) {
        PaymentMethod paymentMethod = new PaymentMethod()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .enabled(DEFAULT_ENABLED)
            .checker(DEFAULT_CHECKER)
            .handler(DEFAULT_HANDLER);
        return paymentMethod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMethod createUpdatedEntity(EntityManager em) {
        PaymentMethod paymentMethod = new PaymentMethod()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .enabled(UPDATED_ENABLED)
            .checker(UPDATED_CHECKER)
            .handler(UPDATED_HANDLER);
        return paymentMethod;
    }

    @BeforeEach
    public void initTest() {
        paymentMethod = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentMethod() throws Exception {
        int databaseSizeBeforeCreate = paymentMethodRepository.findAll().size();
        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);
        restPaymentMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentMethod testPaymentMethod = paymentMethodList.get(paymentMethodList.size() - 1);
        assertThat(testPaymentMethod.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testPaymentMethod.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testPaymentMethod.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPaymentMethod.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPaymentMethod.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPaymentMethod.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testPaymentMethod.getChecker()).isEqualTo(DEFAULT_CHECKER);
        assertThat(testPaymentMethod.getHandler()).isEqualTo(DEFAULT_HANDLER);
    }

    @Test
    @Transactional
    void createPaymentMethodWithExistingId() throws Exception {
        // Create the PaymentMethod with an existing ID
        paymentMethod.setId(1L);
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        int databaseSizeBeforeCreate = paymentMethodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setCreatedat(null);

        // Create the PaymentMethod, which fails.
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        restPaymentMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setUpdatedat(null);

        // Create the PaymentMethod, which fails.
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        restPaymentMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setName(null);

        // Create the PaymentMethod, which fails.
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        restPaymentMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setCode(null);

        // Create the PaymentMethod, which fails.
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        restPaymentMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setDescription(null);

        // Create the PaymentMethod, which fails.
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        restPaymentMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setEnabled(null);

        // Create the PaymentMethod, which fails.
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        restPaymentMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHandlerIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().size();
        // set the field null
        paymentMethod.setHandler(null);

        // Create the PaymentMethod, which fails.
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        restPaymentMethodMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentMethods() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList
        restPaymentMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].checker").value(hasItem(DEFAULT_CHECKER)))
            .andExpect(jsonPath("$.[*].handler").value(hasItem(DEFAULT_HANDLER)));
    }

    @Test
    @Transactional
    void getPaymentMethod() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get the paymentMethod
        restPaymentMethodMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentMethod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentMethod.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.checker").value(DEFAULT_CHECKER))
            .andExpect(jsonPath("$.handler").value(DEFAULT_HANDLER));
    }

    @Test
    @Transactional
    void getPaymentMethodsByIdFiltering() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        Long id = paymentMethod.getId();

        defaultPaymentMethodShouldBeFound("id.equals=" + id);
        defaultPaymentMethodShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentMethodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentMethodShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentMethodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentMethodShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where createdat equals to DEFAULT_CREATEDAT
        defaultPaymentMethodShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the paymentMethodList where createdat equals to UPDATED_CREATEDAT
        defaultPaymentMethodShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where createdat not equals to DEFAULT_CREATEDAT
        defaultPaymentMethodShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the paymentMethodList where createdat not equals to UPDATED_CREATEDAT
        defaultPaymentMethodShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultPaymentMethodShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the paymentMethodList where createdat equals to UPDATED_CREATEDAT
        defaultPaymentMethodShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where createdat is not null
        defaultPaymentMethodShouldBeFound("createdat.specified=true");

        // Get all the paymentMethodList where createdat is null
        defaultPaymentMethodShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where updatedat equals to DEFAULT_UPDATEDAT
        defaultPaymentMethodShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the paymentMethodList where updatedat equals to UPDATED_UPDATEDAT
        defaultPaymentMethodShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultPaymentMethodShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the paymentMethodList where updatedat not equals to UPDATED_UPDATEDAT
        defaultPaymentMethodShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultPaymentMethodShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the paymentMethodList where updatedat equals to UPDATED_UPDATEDAT
        defaultPaymentMethodShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where updatedat is not null
        defaultPaymentMethodShouldBeFound("updatedat.specified=true");

        // Get all the paymentMethodList where updatedat is null
        defaultPaymentMethodShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where name equals to DEFAULT_NAME
        defaultPaymentMethodShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the paymentMethodList where name equals to UPDATED_NAME
        defaultPaymentMethodShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where name not equals to DEFAULT_NAME
        defaultPaymentMethodShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the paymentMethodList where name not equals to UPDATED_NAME
        defaultPaymentMethodShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPaymentMethodShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the paymentMethodList where name equals to UPDATED_NAME
        defaultPaymentMethodShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where name is not null
        defaultPaymentMethodShouldBeFound("name.specified=true");

        // Get all the paymentMethodList where name is null
        defaultPaymentMethodShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByNameContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where name contains DEFAULT_NAME
        defaultPaymentMethodShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the paymentMethodList where name contains UPDATED_NAME
        defaultPaymentMethodShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where name does not contain DEFAULT_NAME
        defaultPaymentMethodShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the paymentMethodList where name does not contain UPDATED_NAME
        defaultPaymentMethodShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where code equals to DEFAULT_CODE
        defaultPaymentMethodShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the paymentMethodList where code equals to UPDATED_CODE
        defaultPaymentMethodShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where code not equals to DEFAULT_CODE
        defaultPaymentMethodShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the paymentMethodList where code not equals to UPDATED_CODE
        defaultPaymentMethodShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPaymentMethodShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the paymentMethodList where code equals to UPDATED_CODE
        defaultPaymentMethodShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where code is not null
        defaultPaymentMethodShouldBeFound("code.specified=true");

        // Get all the paymentMethodList where code is null
        defaultPaymentMethodShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCodeContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where code contains DEFAULT_CODE
        defaultPaymentMethodShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the paymentMethodList where code contains UPDATED_CODE
        defaultPaymentMethodShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where code does not contain DEFAULT_CODE
        defaultPaymentMethodShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the paymentMethodList where code does not contain UPDATED_CODE
        defaultPaymentMethodShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description equals to DEFAULT_DESCRIPTION
        defaultPaymentMethodShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the paymentMethodList where description equals to UPDATED_DESCRIPTION
        defaultPaymentMethodShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description not equals to DEFAULT_DESCRIPTION
        defaultPaymentMethodShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the paymentMethodList where description not equals to UPDATED_DESCRIPTION
        defaultPaymentMethodShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPaymentMethodShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the paymentMethodList where description equals to UPDATED_DESCRIPTION
        defaultPaymentMethodShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description is not null
        defaultPaymentMethodShouldBeFound("description.specified=true");

        // Get all the paymentMethodList where description is null
        defaultPaymentMethodShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description contains DEFAULT_DESCRIPTION
        defaultPaymentMethodShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the paymentMethodList where description contains UPDATED_DESCRIPTION
        defaultPaymentMethodShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where description does not contain DEFAULT_DESCRIPTION
        defaultPaymentMethodShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the paymentMethodList where description does not contain UPDATED_DESCRIPTION
        defaultPaymentMethodShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where enabled equals to DEFAULT_ENABLED
        defaultPaymentMethodShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the paymentMethodList where enabled equals to UPDATED_ENABLED
        defaultPaymentMethodShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where enabled not equals to DEFAULT_ENABLED
        defaultPaymentMethodShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the paymentMethodList where enabled not equals to UPDATED_ENABLED
        defaultPaymentMethodShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultPaymentMethodShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the paymentMethodList where enabled equals to UPDATED_ENABLED
        defaultPaymentMethodShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where enabled is not null
        defaultPaymentMethodShouldBeFound("enabled.specified=true");

        // Get all the paymentMethodList where enabled is null
        defaultPaymentMethodShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCheckerIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where checker equals to DEFAULT_CHECKER
        defaultPaymentMethodShouldBeFound("checker.equals=" + DEFAULT_CHECKER);

        // Get all the paymentMethodList where checker equals to UPDATED_CHECKER
        defaultPaymentMethodShouldNotBeFound("checker.equals=" + UPDATED_CHECKER);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCheckerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where checker not equals to DEFAULT_CHECKER
        defaultPaymentMethodShouldNotBeFound("checker.notEquals=" + DEFAULT_CHECKER);

        // Get all the paymentMethodList where checker not equals to UPDATED_CHECKER
        defaultPaymentMethodShouldBeFound("checker.notEquals=" + UPDATED_CHECKER);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCheckerIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where checker in DEFAULT_CHECKER or UPDATED_CHECKER
        defaultPaymentMethodShouldBeFound("checker.in=" + DEFAULT_CHECKER + "," + UPDATED_CHECKER);

        // Get all the paymentMethodList where checker equals to UPDATED_CHECKER
        defaultPaymentMethodShouldNotBeFound("checker.in=" + UPDATED_CHECKER);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCheckerIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where checker is not null
        defaultPaymentMethodShouldBeFound("checker.specified=true");

        // Get all the paymentMethodList where checker is null
        defaultPaymentMethodShouldNotBeFound("checker.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCheckerContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where checker contains DEFAULT_CHECKER
        defaultPaymentMethodShouldBeFound("checker.contains=" + DEFAULT_CHECKER);

        // Get all the paymentMethodList where checker contains UPDATED_CHECKER
        defaultPaymentMethodShouldNotBeFound("checker.contains=" + UPDATED_CHECKER);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByCheckerNotContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where checker does not contain DEFAULT_CHECKER
        defaultPaymentMethodShouldNotBeFound("checker.doesNotContain=" + DEFAULT_CHECKER);

        // Get all the paymentMethodList where checker does not contain UPDATED_CHECKER
        defaultPaymentMethodShouldBeFound("checker.doesNotContain=" + UPDATED_CHECKER);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByHandlerIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where handler equals to DEFAULT_HANDLER
        defaultPaymentMethodShouldBeFound("handler.equals=" + DEFAULT_HANDLER);

        // Get all the paymentMethodList where handler equals to UPDATED_HANDLER
        defaultPaymentMethodShouldNotBeFound("handler.equals=" + UPDATED_HANDLER);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByHandlerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where handler not equals to DEFAULT_HANDLER
        defaultPaymentMethodShouldNotBeFound("handler.notEquals=" + DEFAULT_HANDLER);

        // Get all the paymentMethodList where handler not equals to UPDATED_HANDLER
        defaultPaymentMethodShouldBeFound("handler.notEquals=" + UPDATED_HANDLER);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByHandlerIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where handler in DEFAULT_HANDLER or UPDATED_HANDLER
        defaultPaymentMethodShouldBeFound("handler.in=" + DEFAULT_HANDLER + "," + UPDATED_HANDLER);

        // Get all the paymentMethodList where handler equals to UPDATED_HANDLER
        defaultPaymentMethodShouldNotBeFound("handler.in=" + UPDATED_HANDLER);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByHandlerIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where handler is not null
        defaultPaymentMethodShouldBeFound("handler.specified=true");

        // Get all the paymentMethodList where handler is null
        defaultPaymentMethodShouldNotBeFound("handler.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByHandlerContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where handler contains DEFAULT_HANDLER
        defaultPaymentMethodShouldBeFound("handler.contains=" + DEFAULT_HANDLER);

        // Get all the paymentMethodList where handler contains UPDATED_HANDLER
        defaultPaymentMethodShouldNotBeFound("handler.contains=" + UPDATED_HANDLER);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByHandlerNotContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        // Get all the paymentMethodList where handler does not contain DEFAULT_HANDLER
        defaultPaymentMethodShouldNotBeFound("handler.doesNotContain=" + DEFAULT_HANDLER);

        // Get all the paymentMethodList where handler does not contain UPDATED_HANDLER
        defaultPaymentMethodShouldBeFound("handler.doesNotContain=" + UPDATED_HANDLER);
    }

    @Test
    @Transactional
    void getAllPaymentMethodsByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);
        Channel channel = ChannelResourceIT.createEntity(em);
        em.persist(channel);
        em.flush();
        paymentMethod.addChannel(channel);
        paymentMethodRepository.saveAndFlush(paymentMethod);
        Long channelId = channel.getId();

        // Get all the paymentMethodList where channel equals to channelId
        defaultPaymentMethodShouldBeFound("channelId.equals=" + channelId);

        // Get all the paymentMethodList where channel equals to (channelId + 1)
        defaultPaymentMethodShouldNotBeFound("channelId.equals=" + (channelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentMethodShouldBeFound(String filter) throws Exception {
        restPaymentMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethod.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].checker").value(hasItem(DEFAULT_CHECKER)))
            .andExpect(jsonPath("$.[*].handler").value(hasItem(DEFAULT_HANDLER)));

        // Check, that the count call also returns 1
        restPaymentMethodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentMethodShouldNotBeFound(String filter) throws Exception {
        restPaymentMethodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMethodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentMethod() throws Exception {
        // Get the paymentMethod
        restPaymentMethodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaymentMethod() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();

        // Update the paymentMethod
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.findById(paymentMethod.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentMethod are not directly saved in db
        em.detach(updatedPaymentMethod);
        updatedPaymentMethod
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .enabled(UPDATED_ENABLED)
            .checker(UPDATED_CHECKER)
            .handler(UPDATED_HANDLER);
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(updatedPaymentMethod);

        restPaymentMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentMethodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        PaymentMethod testPaymentMethod = paymentMethodList.get(paymentMethodList.size() - 1);
        assertThat(testPaymentMethod.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testPaymentMethod.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testPaymentMethod.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPaymentMethod.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPaymentMethod.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentMethod.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testPaymentMethod.getChecker()).isEqualTo(UPDATED_CHECKER);
        assertThat(testPaymentMethod.getHandler()).isEqualTo(UPDATED_HANDLER);
    }

    @Test
    @Transactional
    void putNonExistingPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();
        paymentMethod.setId(count.incrementAndGet());

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentMethodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();
        paymentMethod.setId(count.incrementAndGet());

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();
        paymentMethod.setId(count.incrementAndGet());

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentMethodWithPatch() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();

        // Update the paymentMethod using partial update
        PaymentMethod partialUpdatedPaymentMethod = new PaymentMethod();
        partialUpdatedPaymentMethod.setId(paymentMethod.getId());

        partialUpdatedPaymentMethod.code(UPDATED_CODE).description(UPDATED_DESCRIPTION).enabled(UPDATED_ENABLED).checker(UPDATED_CHECKER);

        restPaymentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentMethod))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        PaymentMethod testPaymentMethod = paymentMethodList.get(paymentMethodList.size() - 1);
        assertThat(testPaymentMethod.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testPaymentMethod.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testPaymentMethod.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPaymentMethod.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPaymentMethod.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentMethod.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testPaymentMethod.getChecker()).isEqualTo(UPDATED_CHECKER);
        assertThat(testPaymentMethod.getHandler()).isEqualTo(DEFAULT_HANDLER);
    }

    @Test
    @Transactional
    void fullUpdatePaymentMethodWithPatch() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();

        // Update the paymentMethod using partial update
        PaymentMethod partialUpdatedPaymentMethod = new PaymentMethod();
        partialUpdatedPaymentMethod.setId(paymentMethod.getId());

        partialUpdatedPaymentMethod
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .enabled(UPDATED_ENABLED)
            .checker(UPDATED_CHECKER)
            .handler(UPDATED_HANDLER);

        restPaymentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentMethod.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentMethod))
            )
            .andExpect(status().isOk());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        PaymentMethod testPaymentMethod = paymentMethodList.get(paymentMethodList.size() - 1);
        assertThat(testPaymentMethod.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testPaymentMethod.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testPaymentMethod.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPaymentMethod.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPaymentMethod.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentMethod.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testPaymentMethod.getChecker()).isEqualTo(UPDATED_CHECKER);
        assertThat(testPaymentMethod.getHandler()).isEqualTo(UPDATED_HANDLER);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();
        paymentMethod.setId(count.incrementAndGet());

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentMethodDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();
        paymentMethod.setId(count.incrementAndGet());

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().size();
        paymentMethod.setId(count.incrementAndGet());

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMethodMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentMethod() throws Exception {
        // Initialize the database
        paymentMethodRepository.saveAndFlush(paymentMethod);

        int databaseSizeBeforeDelete = paymentMethodRepository.findAll().size();

        // Delete the paymentMethod
        restPaymentMethodMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentMethod.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
