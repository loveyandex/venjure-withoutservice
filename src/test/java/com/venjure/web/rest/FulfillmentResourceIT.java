package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Fulfillment;
import com.venjure.domain.OrderItem;
import com.venjure.repository.FulfillmentRepository;
import com.venjure.service.criteria.FulfillmentCriteria;
import com.venjure.service.dto.FulfillmentDTO;
import com.venjure.service.mapper.FulfillmentMapper;
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
 * Integration tests for the {@link FulfillmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FulfillmentResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_TRACKINGCODE = "AAAAAAAAAA";
    private static final String UPDATED_TRACKINGCODE = "BBBBBBBBBB";

    private static final String DEFAULT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_HANDLERCODE = "AAAAAAAAAA";
    private static final String UPDATED_HANDLERCODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fulfillments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FulfillmentRepository fulfillmentRepository;

    @Autowired
    private FulfillmentMapper fulfillmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFulfillmentMockMvc;

    private Fulfillment fulfillment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fulfillment createEntity(EntityManager em) {
        Fulfillment fulfillment = new Fulfillment()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .state(DEFAULT_STATE)
            .trackingcode(DEFAULT_TRACKINGCODE)
            .method(DEFAULT_METHOD)
            .handlercode(DEFAULT_HANDLERCODE);
        return fulfillment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fulfillment createUpdatedEntity(EntityManager em) {
        Fulfillment fulfillment = new Fulfillment()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .state(UPDATED_STATE)
            .trackingcode(UPDATED_TRACKINGCODE)
            .method(UPDATED_METHOD)
            .handlercode(UPDATED_HANDLERCODE);
        return fulfillment;
    }

    @BeforeEach
    public void initTest() {
        fulfillment = createEntity(em);
    }

    @Test
    @Transactional
    void createFulfillment() throws Exception {
        int databaseSizeBeforeCreate = fulfillmentRepository.findAll().size();
        // Create the Fulfillment
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);
        restFulfillmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Fulfillment in the database
        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeCreate + 1);
        Fulfillment testFulfillment = fulfillmentList.get(fulfillmentList.size() - 1);
        assertThat(testFulfillment.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testFulfillment.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testFulfillment.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testFulfillment.getTrackingcode()).isEqualTo(DEFAULT_TRACKINGCODE);
        assertThat(testFulfillment.getMethod()).isEqualTo(DEFAULT_METHOD);
        assertThat(testFulfillment.getHandlercode()).isEqualTo(DEFAULT_HANDLERCODE);
    }

    @Test
    @Transactional
    void createFulfillmentWithExistingId() throws Exception {
        // Create the Fulfillment with an existing ID
        fulfillment.setId(1L);
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        int databaseSizeBeforeCreate = fulfillmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFulfillmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fulfillment in the database
        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = fulfillmentRepository.findAll().size();
        // set the field null
        fulfillment.setCreatedat(null);

        // Create the Fulfillment, which fails.
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        restFulfillmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = fulfillmentRepository.findAll().size();
        // set the field null
        fulfillment.setUpdatedat(null);

        // Create the Fulfillment, which fails.
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        restFulfillmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fulfillmentRepository.findAll().size();
        // set the field null
        fulfillment.setState(null);

        // Create the Fulfillment, which fails.
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        restFulfillmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTrackingcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fulfillmentRepository.findAll().size();
        // set the field null
        fulfillment.setTrackingcode(null);

        // Create the Fulfillment, which fails.
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        restFulfillmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = fulfillmentRepository.findAll().size();
        // set the field null
        fulfillment.setMethod(null);

        // Create the Fulfillment, which fails.
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        restFulfillmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHandlercodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fulfillmentRepository.findAll().size();
        // set the field null
        fulfillment.setHandlercode(null);

        // Create the Fulfillment, which fails.
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        restFulfillmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFulfillments() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList
        restFulfillmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fulfillment.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].trackingcode").value(hasItem(DEFAULT_TRACKINGCODE)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].handlercode").value(hasItem(DEFAULT_HANDLERCODE)));
    }

    @Test
    @Transactional
    void getFulfillment() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get the fulfillment
        restFulfillmentMockMvc
            .perform(get(ENTITY_API_URL_ID, fulfillment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fulfillment.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.trackingcode").value(DEFAULT_TRACKINGCODE))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD))
            .andExpect(jsonPath("$.handlercode").value(DEFAULT_HANDLERCODE));
    }

    @Test
    @Transactional
    void getFulfillmentsByIdFiltering() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        Long id = fulfillment.getId();

        defaultFulfillmentShouldBeFound("id.equals=" + id);
        defaultFulfillmentShouldNotBeFound("id.notEquals=" + id);

        defaultFulfillmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFulfillmentShouldNotBeFound("id.greaterThan=" + id);

        defaultFulfillmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFulfillmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where createdat equals to DEFAULT_CREATEDAT
        defaultFulfillmentShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the fulfillmentList where createdat equals to UPDATED_CREATEDAT
        defaultFulfillmentShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where createdat not equals to DEFAULT_CREATEDAT
        defaultFulfillmentShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the fulfillmentList where createdat not equals to UPDATED_CREATEDAT
        defaultFulfillmentShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultFulfillmentShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the fulfillmentList where createdat equals to UPDATED_CREATEDAT
        defaultFulfillmentShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where createdat is not null
        defaultFulfillmentShouldBeFound("createdat.specified=true");

        // Get all the fulfillmentList where createdat is null
        defaultFulfillmentShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllFulfillmentsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where updatedat equals to DEFAULT_UPDATEDAT
        defaultFulfillmentShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the fulfillmentList where updatedat equals to UPDATED_UPDATEDAT
        defaultFulfillmentShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultFulfillmentShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the fulfillmentList where updatedat not equals to UPDATED_UPDATEDAT
        defaultFulfillmentShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultFulfillmentShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the fulfillmentList where updatedat equals to UPDATED_UPDATEDAT
        defaultFulfillmentShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where updatedat is not null
        defaultFulfillmentShouldBeFound("updatedat.specified=true");

        // Get all the fulfillmentList where updatedat is null
        defaultFulfillmentShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllFulfillmentsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where state equals to DEFAULT_STATE
        defaultFulfillmentShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the fulfillmentList where state equals to UPDATED_STATE
        defaultFulfillmentShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where state not equals to DEFAULT_STATE
        defaultFulfillmentShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the fulfillmentList where state not equals to UPDATED_STATE
        defaultFulfillmentShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where state in DEFAULT_STATE or UPDATED_STATE
        defaultFulfillmentShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the fulfillmentList where state equals to UPDATED_STATE
        defaultFulfillmentShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where state is not null
        defaultFulfillmentShouldBeFound("state.specified=true");

        // Get all the fulfillmentList where state is null
        defaultFulfillmentShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllFulfillmentsByStateContainsSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where state contains DEFAULT_STATE
        defaultFulfillmentShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the fulfillmentList where state contains UPDATED_STATE
        defaultFulfillmentShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByStateNotContainsSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where state does not contain DEFAULT_STATE
        defaultFulfillmentShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the fulfillmentList where state does not contain UPDATED_STATE
        defaultFulfillmentShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByTrackingcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where trackingcode equals to DEFAULT_TRACKINGCODE
        defaultFulfillmentShouldBeFound("trackingcode.equals=" + DEFAULT_TRACKINGCODE);

        // Get all the fulfillmentList where trackingcode equals to UPDATED_TRACKINGCODE
        defaultFulfillmentShouldNotBeFound("trackingcode.equals=" + UPDATED_TRACKINGCODE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByTrackingcodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where trackingcode not equals to DEFAULT_TRACKINGCODE
        defaultFulfillmentShouldNotBeFound("trackingcode.notEquals=" + DEFAULT_TRACKINGCODE);

        // Get all the fulfillmentList where trackingcode not equals to UPDATED_TRACKINGCODE
        defaultFulfillmentShouldBeFound("trackingcode.notEquals=" + UPDATED_TRACKINGCODE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByTrackingcodeIsInShouldWork() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where trackingcode in DEFAULT_TRACKINGCODE or UPDATED_TRACKINGCODE
        defaultFulfillmentShouldBeFound("trackingcode.in=" + DEFAULT_TRACKINGCODE + "," + UPDATED_TRACKINGCODE);

        // Get all the fulfillmentList where trackingcode equals to UPDATED_TRACKINGCODE
        defaultFulfillmentShouldNotBeFound("trackingcode.in=" + UPDATED_TRACKINGCODE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByTrackingcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where trackingcode is not null
        defaultFulfillmentShouldBeFound("trackingcode.specified=true");

        // Get all the fulfillmentList where trackingcode is null
        defaultFulfillmentShouldNotBeFound("trackingcode.specified=false");
    }

    @Test
    @Transactional
    void getAllFulfillmentsByTrackingcodeContainsSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where trackingcode contains DEFAULT_TRACKINGCODE
        defaultFulfillmentShouldBeFound("trackingcode.contains=" + DEFAULT_TRACKINGCODE);

        // Get all the fulfillmentList where trackingcode contains UPDATED_TRACKINGCODE
        defaultFulfillmentShouldNotBeFound("trackingcode.contains=" + UPDATED_TRACKINGCODE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByTrackingcodeNotContainsSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where trackingcode does not contain DEFAULT_TRACKINGCODE
        defaultFulfillmentShouldNotBeFound("trackingcode.doesNotContain=" + DEFAULT_TRACKINGCODE);

        // Get all the fulfillmentList where trackingcode does not contain UPDATED_TRACKINGCODE
        defaultFulfillmentShouldBeFound("trackingcode.doesNotContain=" + UPDATED_TRACKINGCODE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where method equals to DEFAULT_METHOD
        defaultFulfillmentShouldBeFound("method.equals=" + DEFAULT_METHOD);

        // Get all the fulfillmentList where method equals to UPDATED_METHOD
        defaultFulfillmentShouldNotBeFound("method.equals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByMethodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where method not equals to DEFAULT_METHOD
        defaultFulfillmentShouldNotBeFound("method.notEquals=" + DEFAULT_METHOD);

        // Get all the fulfillmentList where method not equals to UPDATED_METHOD
        defaultFulfillmentShouldBeFound("method.notEquals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByMethodIsInShouldWork() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where method in DEFAULT_METHOD or UPDATED_METHOD
        defaultFulfillmentShouldBeFound("method.in=" + DEFAULT_METHOD + "," + UPDATED_METHOD);

        // Get all the fulfillmentList where method equals to UPDATED_METHOD
        defaultFulfillmentShouldNotBeFound("method.in=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where method is not null
        defaultFulfillmentShouldBeFound("method.specified=true");

        // Get all the fulfillmentList where method is null
        defaultFulfillmentShouldNotBeFound("method.specified=false");
    }

    @Test
    @Transactional
    void getAllFulfillmentsByMethodContainsSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where method contains DEFAULT_METHOD
        defaultFulfillmentShouldBeFound("method.contains=" + DEFAULT_METHOD);

        // Get all the fulfillmentList where method contains UPDATED_METHOD
        defaultFulfillmentShouldNotBeFound("method.contains=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByMethodNotContainsSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where method does not contain DEFAULT_METHOD
        defaultFulfillmentShouldNotBeFound("method.doesNotContain=" + DEFAULT_METHOD);

        // Get all the fulfillmentList where method does not contain UPDATED_METHOD
        defaultFulfillmentShouldBeFound("method.doesNotContain=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByHandlercodeIsEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where handlercode equals to DEFAULT_HANDLERCODE
        defaultFulfillmentShouldBeFound("handlercode.equals=" + DEFAULT_HANDLERCODE);

        // Get all the fulfillmentList where handlercode equals to UPDATED_HANDLERCODE
        defaultFulfillmentShouldNotBeFound("handlercode.equals=" + UPDATED_HANDLERCODE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByHandlercodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where handlercode not equals to DEFAULT_HANDLERCODE
        defaultFulfillmentShouldNotBeFound("handlercode.notEquals=" + DEFAULT_HANDLERCODE);

        // Get all the fulfillmentList where handlercode not equals to UPDATED_HANDLERCODE
        defaultFulfillmentShouldBeFound("handlercode.notEquals=" + UPDATED_HANDLERCODE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByHandlercodeIsInShouldWork() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where handlercode in DEFAULT_HANDLERCODE or UPDATED_HANDLERCODE
        defaultFulfillmentShouldBeFound("handlercode.in=" + DEFAULT_HANDLERCODE + "," + UPDATED_HANDLERCODE);

        // Get all the fulfillmentList where handlercode equals to UPDATED_HANDLERCODE
        defaultFulfillmentShouldNotBeFound("handlercode.in=" + UPDATED_HANDLERCODE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByHandlercodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where handlercode is not null
        defaultFulfillmentShouldBeFound("handlercode.specified=true");

        // Get all the fulfillmentList where handlercode is null
        defaultFulfillmentShouldNotBeFound("handlercode.specified=false");
    }

    @Test
    @Transactional
    void getAllFulfillmentsByHandlercodeContainsSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where handlercode contains DEFAULT_HANDLERCODE
        defaultFulfillmentShouldBeFound("handlercode.contains=" + DEFAULT_HANDLERCODE);

        // Get all the fulfillmentList where handlercode contains UPDATED_HANDLERCODE
        defaultFulfillmentShouldNotBeFound("handlercode.contains=" + UPDATED_HANDLERCODE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByHandlercodeNotContainsSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        // Get all the fulfillmentList where handlercode does not contain DEFAULT_HANDLERCODE
        defaultFulfillmentShouldNotBeFound("handlercode.doesNotContain=" + DEFAULT_HANDLERCODE);

        // Get all the fulfillmentList where handlercode does not contain UPDATED_HANDLERCODE
        defaultFulfillmentShouldBeFound("handlercode.doesNotContain=" + UPDATED_HANDLERCODE);
    }

    @Test
    @Transactional
    void getAllFulfillmentsByOrderItemIsEqualToSomething() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);
        OrderItem orderItem = OrderItemResourceIT.createEntity(em);
        em.persist(orderItem);
        em.flush();
        fulfillment.addOrderItem(orderItem);
        fulfillmentRepository.saveAndFlush(fulfillment);
        Long orderItemId = orderItem.getId();

        // Get all the fulfillmentList where orderItem equals to orderItemId
        defaultFulfillmentShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the fulfillmentList where orderItem equals to (orderItemId + 1)
        defaultFulfillmentShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFulfillmentShouldBeFound(String filter) throws Exception {
        restFulfillmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fulfillment.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].trackingcode").value(hasItem(DEFAULT_TRACKINGCODE)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].handlercode").value(hasItem(DEFAULT_HANDLERCODE)));

        // Check, that the count call also returns 1
        restFulfillmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFulfillmentShouldNotBeFound(String filter) throws Exception {
        restFulfillmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFulfillmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFulfillment() throws Exception {
        // Get the fulfillment
        restFulfillmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFulfillment() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        int databaseSizeBeforeUpdate = fulfillmentRepository.findAll().size();

        // Update the fulfillment
        Fulfillment updatedFulfillment = fulfillmentRepository.findById(fulfillment.getId()).get();
        // Disconnect from session so that the updates on updatedFulfillment are not directly saved in db
        em.detach(updatedFulfillment);
        updatedFulfillment
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .state(UPDATED_STATE)
            .trackingcode(UPDATED_TRACKINGCODE)
            .method(UPDATED_METHOD)
            .handlercode(UPDATED_HANDLERCODE);
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(updatedFulfillment);

        restFulfillmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fulfillmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fulfillment in the database
        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeUpdate);
        Fulfillment testFulfillment = fulfillmentList.get(fulfillmentList.size() - 1);
        assertThat(testFulfillment.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFulfillment.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testFulfillment.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testFulfillment.getTrackingcode()).isEqualTo(UPDATED_TRACKINGCODE);
        assertThat(testFulfillment.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testFulfillment.getHandlercode()).isEqualTo(UPDATED_HANDLERCODE);
    }

    @Test
    @Transactional
    void putNonExistingFulfillment() throws Exception {
        int databaseSizeBeforeUpdate = fulfillmentRepository.findAll().size();
        fulfillment.setId(count.incrementAndGet());

        // Create the Fulfillment
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFulfillmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fulfillmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fulfillment in the database
        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFulfillment() throws Exception {
        int databaseSizeBeforeUpdate = fulfillmentRepository.findAll().size();
        fulfillment.setId(count.incrementAndGet());

        // Create the Fulfillment
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFulfillmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fulfillment in the database
        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFulfillment() throws Exception {
        int databaseSizeBeforeUpdate = fulfillmentRepository.findAll().size();
        fulfillment.setId(count.incrementAndGet());

        // Create the Fulfillment
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFulfillmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fulfillment in the database
        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFulfillmentWithPatch() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        int databaseSizeBeforeUpdate = fulfillmentRepository.findAll().size();

        // Update the fulfillment using partial update
        Fulfillment partialUpdatedFulfillment = new Fulfillment();
        partialUpdatedFulfillment.setId(fulfillment.getId());

        partialUpdatedFulfillment.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).trackingcode(UPDATED_TRACKINGCODE);

        restFulfillmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFulfillment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFulfillment))
            )
            .andExpect(status().isOk());

        // Validate the Fulfillment in the database
        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeUpdate);
        Fulfillment testFulfillment = fulfillmentList.get(fulfillmentList.size() - 1);
        assertThat(testFulfillment.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFulfillment.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testFulfillment.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testFulfillment.getTrackingcode()).isEqualTo(UPDATED_TRACKINGCODE);
        assertThat(testFulfillment.getMethod()).isEqualTo(DEFAULT_METHOD);
        assertThat(testFulfillment.getHandlercode()).isEqualTo(DEFAULT_HANDLERCODE);
    }

    @Test
    @Transactional
    void fullUpdateFulfillmentWithPatch() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        int databaseSizeBeforeUpdate = fulfillmentRepository.findAll().size();

        // Update the fulfillment using partial update
        Fulfillment partialUpdatedFulfillment = new Fulfillment();
        partialUpdatedFulfillment.setId(fulfillment.getId());

        partialUpdatedFulfillment
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .state(UPDATED_STATE)
            .trackingcode(UPDATED_TRACKINGCODE)
            .method(UPDATED_METHOD)
            .handlercode(UPDATED_HANDLERCODE);

        restFulfillmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFulfillment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFulfillment))
            )
            .andExpect(status().isOk());

        // Validate the Fulfillment in the database
        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeUpdate);
        Fulfillment testFulfillment = fulfillmentList.get(fulfillmentList.size() - 1);
        assertThat(testFulfillment.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFulfillment.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testFulfillment.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testFulfillment.getTrackingcode()).isEqualTo(UPDATED_TRACKINGCODE);
        assertThat(testFulfillment.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testFulfillment.getHandlercode()).isEqualTo(UPDATED_HANDLERCODE);
    }

    @Test
    @Transactional
    void patchNonExistingFulfillment() throws Exception {
        int databaseSizeBeforeUpdate = fulfillmentRepository.findAll().size();
        fulfillment.setId(count.incrementAndGet());

        // Create the Fulfillment
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFulfillmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fulfillmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fulfillment in the database
        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFulfillment() throws Exception {
        int databaseSizeBeforeUpdate = fulfillmentRepository.findAll().size();
        fulfillment.setId(count.incrementAndGet());

        // Create the Fulfillment
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFulfillmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fulfillment in the database
        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFulfillment() throws Exception {
        int databaseSizeBeforeUpdate = fulfillmentRepository.findAll().size();
        fulfillment.setId(count.incrementAndGet());

        // Create the Fulfillment
        FulfillmentDTO fulfillmentDTO = fulfillmentMapper.toDto(fulfillment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFulfillmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fulfillmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fulfillment in the database
        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFulfillment() throws Exception {
        // Initialize the database
        fulfillmentRepository.saveAndFlush(fulfillment);

        int databaseSizeBeforeDelete = fulfillmentRepository.findAll().size();

        // Delete the fulfillment
        restFulfillmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, fulfillment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fulfillment> fulfillmentList = fulfillmentRepository.findAll();
        assertThat(fulfillmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
