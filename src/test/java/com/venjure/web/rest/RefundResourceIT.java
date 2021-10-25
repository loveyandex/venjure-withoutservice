package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.OrderItem;
import com.venjure.domain.Payment;
import com.venjure.domain.Refund;
import com.venjure.repository.RefundRepository;
import com.venjure.service.criteria.RefundCriteria;
import com.venjure.service.dto.RefundDTO;
import com.venjure.service.mapper.RefundMapper;
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
 * Integration tests for the {@link RefundResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RefundResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ITEMS = 1;
    private static final Integer UPDATED_ITEMS = 2;
    private static final Integer SMALLER_ITEMS = 1 - 1;

    private static final Integer DEFAULT_SHIPPING = 1;
    private static final Integer UPDATED_SHIPPING = 2;
    private static final Integer SMALLER_SHIPPING = 1 - 1;

    private static final Integer DEFAULT_ADJUSTMENT = 1;
    private static final Integer UPDATED_ADJUSTMENT = 2;
    private static final Integer SMALLER_ADJUSTMENT = 1 - 1;

    private static final Integer DEFAULT_TOTAL = 1;
    private static final Integer UPDATED_TOTAL = 2;
    private static final Integer SMALLER_TOTAL = 1 - 1;

    private static final String DEFAULT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTIONID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTIONID = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/refunds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRefundMockMvc;

    private Refund refund;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Refund createEntity(EntityManager em) {
        Refund refund = new Refund()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .items(DEFAULT_ITEMS)
            .shipping(DEFAULT_SHIPPING)
            .adjustment(DEFAULT_ADJUSTMENT)
            .total(DEFAULT_TOTAL)
            .method(DEFAULT_METHOD)
            .reason(DEFAULT_REASON)
            .state(DEFAULT_STATE)
            .transactionid(DEFAULT_TRANSACTIONID)
            .metadata(DEFAULT_METADATA);
        // Add required entity
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createEntity(em);
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).get(0);
        }
        refund.setPayment(payment);
        return refund;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Refund createUpdatedEntity(EntityManager em) {
        Refund refund = new Refund()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .items(UPDATED_ITEMS)
            .shipping(UPDATED_SHIPPING)
            .adjustment(UPDATED_ADJUSTMENT)
            .total(UPDATED_TOTAL)
            .method(UPDATED_METHOD)
            .reason(UPDATED_REASON)
            .state(UPDATED_STATE)
            .transactionid(UPDATED_TRANSACTIONID)
            .metadata(UPDATED_METADATA);
        // Add required entity
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createUpdatedEntity(em);
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).get(0);
        }
        refund.setPayment(payment);
        return refund;
    }

    @BeforeEach
    public void initTest() {
        refund = createEntity(em);
    }

    @Test
    @Transactional
    void createRefund() throws Exception {
        int databaseSizeBeforeCreate = refundRepository.findAll().size();
        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);
        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundDTO)))
            .andExpect(status().isCreated());

        // Validate the Refund in the database
        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeCreate + 1);
        Refund testRefund = refundList.get(refundList.size() - 1);
        assertThat(testRefund.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testRefund.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testRefund.getItems()).isEqualTo(DEFAULT_ITEMS);
        assertThat(testRefund.getShipping()).isEqualTo(DEFAULT_SHIPPING);
        assertThat(testRefund.getAdjustment()).isEqualTo(DEFAULT_ADJUSTMENT);
        assertThat(testRefund.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testRefund.getMethod()).isEqualTo(DEFAULT_METHOD);
        assertThat(testRefund.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testRefund.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testRefund.getTransactionid()).isEqualTo(DEFAULT_TRANSACTIONID);
        assertThat(testRefund.getMetadata()).isEqualTo(DEFAULT_METADATA);
    }

    @Test
    @Transactional
    void createRefundWithExistingId() throws Exception {
        // Create the Refund with an existing ID
        refund.setId(1L);
        RefundDTO refundDTO = refundMapper.toDto(refund);

        int databaseSizeBeforeCreate = refundRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Refund in the database
        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = refundRepository.findAll().size();
        // set the field null
        refund.setCreatedat(null);

        // Create the Refund, which fails.
        RefundDTO refundDTO = refundMapper.toDto(refund);

        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = refundRepository.findAll().size();
        // set the field null
        refund.setUpdatedat(null);

        // Create the Refund, which fails.
        RefundDTO refundDTO = refundMapper.toDto(refund);

        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkItemsIsRequired() throws Exception {
        int databaseSizeBeforeTest = refundRepository.findAll().size();
        // set the field null
        refund.setItems(null);

        // Create the Refund, which fails.
        RefundDTO refundDTO = refundMapper.toDto(refund);

        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShippingIsRequired() throws Exception {
        int databaseSizeBeforeTest = refundRepository.findAll().size();
        // set the field null
        refund.setShipping(null);

        // Create the Refund, which fails.
        RefundDTO refundDTO = refundMapper.toDto(refund);

        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdjustmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = refundRepository.findAll().size();
        // set the field null
        refund.setAdjustment(null);

        // Create the Refund, which fails.
        RefundDTO refundDTO = refundMapper.toDto(refund);

        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = refundRepository.findAll().size();
        // set the field null
        refund.setTotal(null);

        // Create the Refund, which fails.
        RefundDTO refundDTO = refundMapper.toDto(refund);

        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = refundRepository.findAll().size();
        // set the field null
        refund.setMethod(null);

        // Create the Refund, which fails.
        RefundDTO refundDTO = refundMapper.toDto(refund);

        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = refundRepository.findAll().size();
        // set the field null
        refund.setState(null);

        // Create the Refund, which fails.
        RefundDTO refundDTO = refundMapper.toDto(refund);

        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMetadataIsRequired() throws Exception {
        int databaseSizeBeforeTest = refundRepository.findAll().size();
        // set the field null
        refund.setMetadata(null);

        // Create the Refund, which fails.
        RefundDTO refundDTO = refundMapper.toDto(refund);

        restRefundMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundDTO)))
            .andExpect(status().isBadRequest());

        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRefunds() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList
        restRefundMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(refund.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].items").value(hasItem(DEFAULT_ITEMS)))
            .andExpect(jsonPath("$.[*].shipping").value(hasItem(DEFAULT_SHIPPING)))
            .andExpect(jsonPath("$.[*].adjustment").value(hasItem(DEFAULT_ADJUSTMENT)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].transactionid").value(hasItem(DEFAULT_TRANSACTIONID)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)));
    }

    @Test
    @Transactional
    void getRefund() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get the refund
        restRefundMockMvc
            .perform(get(ENTITY_API_URL_ID, refund.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(refund.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.items").value(DEFAULT_ITEMS))
            .andExpect(jsonPath("$.shipping").value(DEFAULT_SHIPPING))
            .andExpect(jsonPath("$.adjustment").value(DEFAULT_ADJUSTMENT))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.transactionid").value(DEFAULT_TRANSACTIONID))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA));
    }

    @Test
    @Transactional
    void getRefundsByIdFiltering() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        Long id = refund.getId();

        defaultRefundShouldBeFound("id.equals=" + id);
        defaultRefundShouldNotBeFound("id.notEquals=" + id);

        defaultRefundShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRefundShouldNotBeFound("id.greaterThan=" + id);

        defaultRefundShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRefundShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRefundsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where createdat equals to DEFAULT_CREATEDAT
        defaultRefundShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the refundList where createdat equals to UPDATED_CREATEDAT
        defaultRefundShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllRefundsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where createdat not equals to DEFAULT_CREATEDAT
        defaultRefundShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the refundList where createdat not equals to UPDATED_CREATEDAT
        defaultRefundShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllRefundsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultRefundShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the refundList where createdat equals to UPDATED_CREATEDAT
        defaultRefundShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllRefundsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where createdat is not null
        defaultRefundShouldBeFound("createdat.specified=true");

        // Get all the refundList where createdat is null
        defaultRefundShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where updatedat equals to DEFAULT_UPDATEDAT
        defaultRefundShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the refundList where updatedat equals to UPDATED_UPDATEDAT
        defaultRefundShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllRefundsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultRefundShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the refundList where updatedat not equals to UPDATED_UPDATEDAT
        defaultRefundShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllRefundsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultRefundShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the refundList where updatedat equals to UPDATED_UPDATEDAT
        defaultRefundShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllRefundsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where updatedat is not null
        defaultRefundShouldBeFound("updatedat.specified=true");

        // Get all the refundList where updatedat is null
        defaultRefundShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where items equals to DEFAULT_ITEMS
        defaultRefundShouldBeFound("items.equals=" + DEFAULT_ITEMS);

        // Get all the refundList where items equals to UPDATED_ITEMS
        defaultRefundShouldNotBeFound("items.equals=" + UPDATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllRefundsByItemsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where items not equals to DEFAULT_ITEMS
        defaultRefundShouldNotBeFound("items.notEquals=" + DEFAULT_ITEMS);

        // Get all the refundList where items not equals to UPDATED_ITEMS
        defaultRefundShouldBeFound("items.notEquals=" + UPDATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllRefundsByItemsIsInShouldWork() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where items in DEFAULT_ITEMS or UPDATED_ITEMS
        defaultRefundShouldBeFound("items.in=" + DEFAULT_ITEMS + "," + UPDATED_ITEMS);

        // Get all the refundList where items equals to UPDATED_ITEMS
        defaultRefundShouldNotBeFound("items.in=" + UPDATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllRefundsByItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where items is not null
        defaultRefundShouldBeFound("items.specified=true");

        // Get all the refundList where items is null
        defaultRefundShouldNotBeFound("items.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByItemsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where items is greater than or equal to DEFAULT_ITEMS
        defaultRefundShouldBeFound("items.greaterThanOrEqual=" + DEFAULT_ITEMS);

        // Get all the refundList where items is greater than or equal to UPDATED_ITEMS
        defaultRefundShouldNotBeFound("items.greaterThanOrEqual=" + UPDATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllRefundsByItemsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where items is less than or equal to DEFAULT_ITEMS
        defaultRefundShouldBeFound("items.lessThanOrEqual=" + DEFAULT_ITEMS);

        // Get all the refundList where items is less than or equal to SMALLER_ITEMS
        defaultRefundShouldNotBeFound("items.lessThanOrEqual=" + SMALLER_ITEMS);
    }

    @Test
    @Transactional
    void getAllRefundsByItemsIsLessThanSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where items is less than DEFAULT_ITEMS
        defaultRefundShouldNotBeFound("items.lessThan=" + DEFAULT_ITEMS);

        // Get all the refundList where items is less than UPDATED_ITEMS
        defaultRefundShouldBeFound("items.lessThan=" + UPDATED_ITEMS);
    }

    @Test
    @Transactional
    void getAllRefundsByItemsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where items is greater than DEFAULT_ITEMS
        defaultRefundShouldNotBeFound("items.greaterThan=" + DEFAULT_ITEMS);

        // Get all the refundList where items is greater than SMALLER_ITEMS
        defaultRefundShouldBeFound("items.greaterThan=" + SMALLER_ITEMS);
    }

    @Test
    @Transactional
    void getAllRefundsByShippingIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where shipping equals to DEFAULT_SHIPPING
        defaultRefundShouldBeFound("shipping.equals=" + DEFAULT_SHIPPING);

        // Get all the refundList where shipping equals to UPDATED_SHIPPING
        defaultRefundShouldNotBeFound("shipping.equals=" + UPDATED_SHIPPING);
    }

    @Test
    @Transactional
    void getAllRefundsByShippingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where shipping not equals to DEFAULT_SHIPPING
        defaultRefundShouldNotBeFound("shipping.notEquals=" + DEFAULT_SHIPPING);

        // Get all the refundList where shipping not equals to UPDATED_SHIPPING
        defaultRefundShouldBeFound("shipping.notEquals=" + UPDATED_SHIPPING);
    }

    @Test
    @Transactional
    void getAllRefundsByShippingIsInShouldWork() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where shipping in DEFAULT_SHIPPING or UPDATED_SHIPPING
        defaultRefundShouldBeFound("shipping.in=" + DEFAULT_SHIPPING + "," + UPDATED_SHIPPING);

        // Get all the refundList where shipping equals to UPDATED_SHIPPING
        defaultRefundShouldNotBeFound("shipping.in=" + UPDATED_SHIPPING);
    }

    @Test
    @Transactional
    void getAllRefundsByShippingIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where shipping is not null
        defaultRefundShouldBeFound("shipping.specified=true");

        // Get all the refundList where shipping is null
        defaultRefundShouldNotBeFound("shipping.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByShippingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where shipping is greater than or equal to DEFAULT_SHIPPING
        defaultRefundShouldBeFound("shipping.greaterThanOrEqual=" + DEFAULT_SHIPPING);

        // Get all the refundList where shipping is greater than or equal to UPDATED_SHIPPING
        defaultRefundShouldNotBeFound("shipping.greaterThanOrEqual=" + UPDATED_SHIPPING);
    }

    @Test
    @Transactional
    void getAllRefundsByShippingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where shipping is less than or equal to DEFAULT_SHIPPING
        defaultRefundShouldBeFound("shipping.lessThanOrEqual=" + DEFAULT_SHIPPING);

        // Get all the refundList where shipping is less than or equal to SMALLER_SHIPPING
        defaultRefundShouldNotBeFound("shipping.lessThanOrEqual=" + SMALLER_SHIPPING);
    }

    @Test
    @Transactional
    void getAllRefundsByShippingIsLessThanSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where shipping is less than DEFAULT_SHIPPING
        defaultRefundShouldNotBeFound("shipping.lessThan=" + DEFAULT_SHIPPING);

        // Get all the refundList where shipping is less than UPDATED_SHIPPING
        defaultRefundShouldBeFound("shipping.lessThan=" + UPDATED_SHIPPING);
    }

    @Test
    @Transactional
    void getAllRefundsByShippingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where shipping is greater than DEFAULT_SHIPPING
        defaultRefundShouldNotBeFound("shipping.greaterThan=" + DEFAULT_SHIPPING);

        // Get all the refundList where shipping is greater than SMALLER_SHIPPING
        defaultRefundShouldBeFound("shipping.greaterThan=" + SMALLER_SHIPPING);
    }

    @Test
    @Transactional
    void getAllRefundsByAdjustmentIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where adjustment equals to DEFAULT_ADJUSTMENT
        defaultRefundShouldBeFound("adjustment.equals=" + DEFAULT_ADJUSTMENT);

        // Get all the refundList where adjustment equals to UPDATED_ADJUSTMENT
        defaultRefundShouldNotBeFound("adjustment.equals=" + UPDATED_ADJUSTMENT);
    }

    @Test
    @Transactional
    void getAllRefundsByAdjustmentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where adjustment not equals to DEFAULT_ADJUSTMENT
        defaultRefundShouldNotBeFound("adjustment.notEquals=" + DEFAULT_ADJUSTMENT);

        // Get all the refundList where adjustment not equals to UPDATED_ADJUSTMENT
        defaultRefundShouldBeFound("adjustment.notEquals=" + UPDATED_ADJUSTMENT);
    }

    @Test
    @Transactional
    void getAllRefundsByAdjustmentIsInShouldWork() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where adjustment in DEFAULT_ADJUSTMENT or UPDATED_ADJUSTMENT
        defaultRefundShouldBeFound("adjustment.in=" + DEFAULT_ADJUSTMENT + "," + UPDATED_ADJUSTMENT);

        // Get all the refundList where adjustment equals to UPDATED_ADJUSTMENT
        defaultRefundShouldNotBeFound("adjustment.in=" + UPDATED_ADJUSTMENT);
    }

    @Test
    @Transactional
    void getAllRefundsByAdjustmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where adjustment is not null
        defaultRefundShouldBeFound("adjustment.specified=true");

        // Get all the refundList where adjustment is null
        defaultRefundShouldNotBeFound("adjustment.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByAdjustmentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where adjustment is greater than or equal to DEFAULT_ADJUSTMENT
        defaultRefundShouldBeFound("adjustment.greaterThanOrEqual=" + DEFAULT_ADJUSTMENT);

        // Get all the refundList where adjustment is greater than or equal to UPDATED_ADJUSTMENT
        defaultRefundShouldNotBeFound("adjustment.greaterThanOrEqual=" + UPDATED_ADJUSTMENT);
    }

    @Test
    @Transactional
    void getAllRefundsByAdjustmentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where adjustment is less than or equal to DEFAULT_ADJUSTMENT
        defaultRefundShouldBeFound("adjustment.lessThanOrEqual=" + DEFAULT_ADJUSTMENT);

        // Get all the refundList where adjustment is less than or equal to SMALLER_ADJUSTMENT
        defaultRefundShouldNotBeFound("adjustment.lessThanOrEqual=" + SMALLER_ADJUSTMENT);
    }

    @Test
    @Transactional
    void getAllRefundsByAdjustmentIsLessThanSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where adjustment is less than DEFAULT_ADJUSTMENT
        defaultRefundShouldNotBeFound("adjustment.lessThan=" + DEFAULT_ADJUSTMENT);

        // Get all the refundList where adjustment is less than UPDATED_ADJUSTMENT
        defaultRefundShouldBeFound("adjustment.lessThan=" + UPDATED_ADJUSTMENT);
    }

    @Test
    @Transactional
    void getAllRefundsByAdjustmentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where adjustment is greater than DEFAULT_ADJUSTMENT
        defaultRefundShouldNotBeFound("adjustment.greaterThan=" + DEFAULT_ADJUSTMENT);

        // Get all the refundList where adjustment is greater than SMALLER_ADJUSTMENT
        defaultRefundShouldBeFound("adjustment.greaterThan=" + SMALLER_ADJUSTMENT);
    }

    @Test
    @Transactional
    void getAllRefundsByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where total equals to DEFAULT_TOTAL
        defaultRefundShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the refundList where total equals to UPDATED_TOTAL
        defaultRefundShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllRefundsByTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where total not equals to DEFAULT_TOTAL
        defaultRefundShouldNotBeFound("total.notEquals=" + DEFAULT_TOTAL);

        // Get all the refundList where total not equals to UPDATED_TOTAL
        defaultRefundShouldBeFound("total.notEquals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllRefundsByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultRefundShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the refundList where total equals to UPDATED_TOTAL
        defaultRefundShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllRefundsByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where total is not null
        defaultRefundShouldBeFound("total.specified=true");

        // Get all the refundList where total is null
        defaultRefundShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where total is greater than or equal to DEFAULT_TOTAL
        defaultRefundShouldBeFound("total.greaterThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the refundList where total is greater than or equal to UPDATED_TOTAL
        defaultRefundShouldNotBeFound("total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllRefundsByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where total is less than or equal to DEFAULT_TOTAL
        defaultRefundShouldBeFound("total.lessThanOrEqual=" + DEFAULT_TOTAL);

        // Get all the refundList where total is less than or equal to SMALLER_TOTAL
        defaultRefundShouldNotBeFound("total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllRefundsByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where total is less than DEFAULT_TOTAL
        defaultRefundShouldNotBeFound("total.lessThan=" + DEFAULT_TOTAL);

        // Get all the refundList where total is less than UPDATED_TOTAL
        defaultRefundShouldBeFound("total.lessThan=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllRefundsByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where total is greater than DEFAULT_TOTAL
        defaultRefundShouldNotBeFound("total.greaterThan=" + DEFAULT_TOTAL);

        // Get all the refundList where total is greater than SMALLER_TOTAL
        defaultRefundShouldBeFound("total.greaterThan=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllRefundsByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where method equals to DEFAULT_METHOD
        defaultRefundShouldBeFound("method.equals=" + DEFAULT_METHOD);

        // Get all the refundList where method equals to UPDATED_METHOD
        defaultRefundShouldNotBeFound("method.equals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllRefundsByMethodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where method not equals to DEFAULT_METHOD
        defaultRefundShouldNotBeFound("method.notEquals=" + DEFAULT_METHOD);

        // Get all the refundList where method not equals to UPDATED_METHOD
        defaultRefundShouldBeFound("method.notEquals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllRefundsByMethodIsInShouldWork() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where method in DEFAULT_METHOD or UPDATED_METHOD
        defaultRefundShouldBeFound("method.in=" + DEFAULT_METHOD + "," + UPDATED_METHOD);

        // Get all the refundList where method equals to UPDATED_METHOD
        defaultRefundShouldNotBeFound("method.in=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllRefundsByMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where method is not null
        defaultRefundShouldBeFound("method.specified=true");

        // Get all the refundList where method is null
        defaultRefundShouldNotBeFound("method.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByMethodContainsSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where method contains DEFAULT_METHOD
        defaultRefundShouldBeFound("method.contains=" + DEFAULT_METHOD);

        // Get all the refundList where method contains UPDATED_METHOD
        defaultRefundShouldNotBeFound("method.contains=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllRefundsByMethodNotContainsSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where method does not contain DEFAULT_METHOD
        defaultRefundShouldNotBeFound("method.doesNotContain=" + DEFAULT_METHOD);

        // Get all the refundList where method does not contain UPDATED_METHOD
        defaultRefundShouldBeFound("method.doesNotContain=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllRefundsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where reason equals to DEFAULT_REASON
        defaultRefundShouldBeFound("reason.equals=" + DEFAULT_REASON);

        // Get all the refundList where reason equals to UPDATED_REASON
        defaultRefundShouldNotBeFound("reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllRefundsByReasonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where reason not equals to DEFAULT_REASON
        defaultRefundShouldNotBeFound("reason.notEquals=" + DEFAULT_REASON);

        // Get all the refundList where reason not equals to UPDATED_REASON
        defaultRefundShouldBeFound("reason.notEquals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllRefundsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where reason in DEFAULT_REASON or UPDATED_REASON
        defaultRefundShouldBeFound("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON);

        // Get all the refundList where reason equals to UPDATED_REASON
        defaultRefundShouldNotBeFound("reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllRefundsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where reason is not null
        defaultRefundShouldBeFound("reason.specified=true");

        // Get all the refundList where reason is null
        defaultRefundShouldNotBeFound("reason.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByReasonContainsSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where reason contains DEFAULT_REASON
        defaultRefundShouldBeFound("reason.contains=" + DEFAULT_REASON);

        // Get all the refundList where reason contains UPDATED_REASON
        defaultRefundShouldNotBeFound("reason.contains=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllRefundsByReasonNotContainsSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where reason does not contain DEFAULT_REASON
        defaultRefundShouldNotBeFound("reason.doesNotContain=" + DEFAULT_REASON);

        // Get all the refundList where reason does not contain UPDATED_REASON
        defaultRefundShouldBeFound("reason.doesNotContain=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllRefundsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where state equals to DEFAULT_STATE
        defaultRefundShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the refundList where state equals to UPDATED_STATE
        defaultRefundShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllRefundsByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where state not equals to DEFAULT_STATE
        defaultRefundShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the refundList where state not equals to UPDATED_STATE
        defaultRefundShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllRefundsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where state in DEFAULT_STATE or UPDATED_STATE
        defaultRefundShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the refundList where state equals to UPDATED_STATE
        defaultRefundShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllRefundsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where state is not null
        defaultRefundShouldBeFound("state.specified=true");

        // Get all the refundList where state is null
        defaultRefundShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByStateContainsSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where state contains DEFAULT_STATE
        defaultRefundShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the refundList where state contains UPDATED_STATE
        defaultRefundShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllRefundsByStateNotContainsSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where state does not contain DEFAULT_STATE
        defaultRefundShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the refundList where state does not contain UPDATED_STATE
        defaultRefundShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllRefundsByTransactionidIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where transactionid equals to DEFAULT_TRANSACTIONID
        defaultRefundShouldBeFound("transactionid.equals=" + DEFAULT_TRANSACTIONID);

        // Get all the refundList where transactionid equals to UPDATED_TRANSACTIONID
        defaultRefundShouldNotBeFound("transactionid.equals=" + UPDATED_TRANSACTIONID);
    }

    @Test
    @Transactional
    void getAllRefundsByTransactionidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where transactionid not equals to DEFAULT_TRANSACTIONID
        defaultRefundShouldNotBeFound("transactionid.notEquals=" + DEFAULT_TRANSACTIONID);

        // Get all the refundList where transactionid not equals to UPDATED_TRANSACTIONID
        defaultRefundShouldBeFound("transactionid.notEquals=" + UPDATED_TRANSACTIONID);
    }

    @Test
    @Transactional
    void getAllRefundsByTransactionidIsInShouldWork() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where transactionid in DEFAULT_TRANSACTIONID or UPDATED_TRANSACTIONID
        defaultRefundShouldBeFound("transactionid.in=" + DEFAULT_TRANSACTIONID + "," + UPDATED_TRANSACTIONID);

        // Get all the refundList where transactionid equals to UPDATED_TRANSACTIONID
        defaultRefundShouldNotBeFound("transactionid.in=" + UPDATED_TRANSACTIONID);
    }

    @Test
    @Transactional
    void getAllRefundsByTransactionidIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where transactionid is not null
        defaultRefundShouldBeFound("transactionid.specified=true");

        // Get all the refundList where transactionid is null
        defaultRefundShouldNotBeFound("transactionid.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByTransactionidContainsSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where transactionid contains DEFAULT_TRANSACTIONID
        defaultRefundShouldBeFound("transactionid.contains=" + DEFAULT_TRANSACTIONID);

        // Get all the refundList where transactionid contains UPDATED_TRANSACTIONID
        defaultRefundShouldNotBeFound("transactionid.contains=" + UPDATED_TRANSACTIONID);
    }

    @Test
    @Transactional
    void getAllRefundsByTransactionidNotContainsSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where transactionid does not contain DEFAULT_TRANSACTIONID
        defaultRefundShouldNotBeFound("transactionid.doesNotContain=" + DEFAULT_TRANSACTIONID);

        // Get all the refundList where transactionid does not contain UPDATED_TRANSACTIONID
        defaultRefundShouldBeFound("transactionid.doesNotContain=" + UPDATED_TRANSACTIONID);
    }

    @Test
    @Transactional
    void getAllRefundsByMetadataIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where metadata equals to DEFAULT_METADATA
        defaultRefundShouldBeFound("metadata.equals=" + DEFAULT_METADATA);

        // Get all the refundList where metadata equals to UPDATED_METADATA
        defaultRefundShouldNotBeFound("metadata.equals=" + UPDATED_METADATA);
    }

    @Test
    @Transactional
    void getAllRefundsByMetadataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where metadata not equals to DEFAULT_METADATA
        defaultRefundShouldNotBeFound("metadata.notEquals=" + DEFAULT_METADATA);

        // Get all the refundList where metadata not equals to UPDATED_METADATA
        defaultRefundShouldBeFound("metadata.notEquals=" + UPDATED_METADATA);
    }

    @Test
    @Transactional
    void getAllRefundsByMetadataIsInShouldWork() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where metadata in DEFAULT_METADATA or UPDATED_METADATA
        defaultRefundShouldBeFound("metadata.in=" + DEFAULT_METADATA + "," + UPDATED_METADATA);

        // Get all the refundList where metadata equals to UPDATED_METADATA
        defaultRefundShouldNotBeFound("metadata.in=" + UPDATED_METADATA);
    }

    @Test
    @Transactional
    void getAllRefundsByMetadataIsNullOrNotNull() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where metadata is not null
        defaultRefundShouldBeFound("metadata.specified=true");

        // Get all the refundList where metadata is null
        defaultRefundShouldNotBeFound("metadata.specified=false");
    }

    @Test
    @Transactional
    void getAllRefundsByMetadataContainsSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where metadata contains DEFAULT_METADATA
        defaultRefundShouldBeFound("metadata.contains=" + DEFAULT_METADATA);

        // Get all the refundList where metadata contains UPDATED_METADATA
        defaultRefundShouldNotBeFound("metadata.contains=" + UPDATED_METADATA);
    }

    @Test
    @Transactional
    void getAllRefundsByMetadataNotContainsSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        // Get all the refundList where metadata does not contain DEFAULT_METADATA
        defaultRefundShouldNotBeFound("metadata.doesNotContain=" + DEFAULT_METADATA);

        // Get all the refundList where metadata does not contain UPDATED_METADATA
        defaultRefundShouldBeFound("metadata.doesNotContain=" + UPDATED_METADATA);
    }

    @Test
    @Transactional
    void getAllRefundsByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);
        Payment payment = PaymentResourceIT.createEntity(em);
        em.persist(payment);
        em.flush();
        refund.setPayment(payment);
        refundRepository.saveAndFlush(refund);
        Long paymentId = payment.getId();

        // Get all the refundList where payment equals to paymentId
        defaultRefundShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the refundList where payment equals to (paymentId + 1)
        defaultRefundShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllRefundsByOrderItemIsEqualToSomething() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);
        OrderItem orderItem = OrderItemResourceIT.createEntity(em);
        em.persist(orderItem);
        em.flush();
        refund.addOrderItem(orderItem);
        refundRepository.saveAndFlush(refund);
        Long orderItemId = orderItem.getId();

        // Get all the refundList where orderItem equals to orderItemId
        defaultRefundShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the refundList where orderItem equals to (orderItemId + 1)
        defaultRefundShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRefundShouldBeFound(String filter) throws Exception {
        restRefundMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(refund.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].items").value(hasItem(DEFAULT_ITEMS)))
            .andExpect(jsonPath("$.[*].shipping").value(hasItem(DEFAULT_SHIPPING)))
            .andExpect(jsonPath("$.[*].adjustment").value(hasItem(DEFAULT_ADJUSTMENT)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].transactionid").value(hasItem(DEFAULT_TRANSACTIONID)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)));

        // Check, that the count call also returns 1
        restRefundMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRefundShouldNotBeFound(String filter) throws Exception {
        restRefundMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRefundMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRefund() throws Exception {
        // Get the refund
        restRefundMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRefund() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        int databaseSizeBeforeUpdate = refundRepository.findAll().size();

        // Update the refund
        Refund updatedRefund = refundRepository.findById(refund.getId()).get();
        // Disconnect from session so that the updates on updatedRefund are not directly saved in db
        em.detach(updatedRefund);
        updatedRefund
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .items(UPDATED_ITEMS)
            .shipping(UPDATED_SHIPPING)
            .adjustment(UPDATED_ADJUSTMENT)
            .total(UPDATED_TOTAL)
            .method(UPDATED_METHOD)
            .reason(UPDATED_REASON)
            .state(UPDATED_STATE)
            .transactionid(UPDATED_TRANSACTIONID)
            .metadata(UPDATED_METADATA);
        RefundDTO refundDTO = refundMapper.toDto(updatedRefund);

        restRefundMockMvc
            .perform(
                put(ENTITY_API_URL_ID, refundDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refundDTO))
            )
            .andExpect(status().isOk());

        // Validate the Refund in the database
        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeUpdate);
        Refund testRefund = refundList.get(refundList.size() - 1);
        assertThat(testRefund.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testRefund.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testRefund.getItems()).isEqualTo(UPDATED_ITEMS);
        assertThat(testRefund.getShipping()).isEqualTo(UPDATED_SHIPPING);
        assertThat(testRefund.getAdjustment()).isEqualTo(UPDATED_ADJUSTMENT);
        assertThat(testRefund.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testRefund.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testRefund.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testRefund.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testRefund.getTransactionid()).isEqualTo(UPDATED_TRANSACTIONID);
        assertThat(testRefund.getMetadata()).isEqualTo(UPDATED_METADATA);
    }

    @Test
    @Transactional
    void putNonExistingRefund() throws Exception {
        int databaseSizeBeforeUpdate = refundRepository.findAll().size();
        refund.setId(count.incrementAndGet());

        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefundMockMvc
            .perform(
                put(ENTITY_API_URL_ID, refundDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refundDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refund in the database
        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRefund() throws Exception {
        int databaseSizeBeforeUpdate = refundRepository.findAll().size();
        refund.setId(count.incrementAndGet());

        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(refundDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refund in the database
        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRefund() throws Exception {
        int databaseSizeBeforeUpdate = refundRepository.findAll().size();
        refund.setId(count.incrementAndGet());

        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(refundDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Refund in the database
        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRefundWithPatch() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        int databaseSizeBeforeUpdate = refundRepository.findAll().size();

        // Update the refund using partial update
        Refund partialUpdatedRefund = new Refund();
        partialUpdatedRefund.setId(refund.getId());

        partialUpdatedRefund.shipping(UPDATED_SHIPPING).total(UPDATED_TOTAL).reason(UPDATED_REASON).state(UPDATED_STATE);

        restRefundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefund.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRefund))
            )
            .andExpect(status().isOk());

        // Validate the Refund in the database
        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeUpdate);
        Refund testRefund = refundList.get(refundList.size() - 1);
        assertThat(testRefund.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testRefund.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testRefund.getItems()).isEqualTo(DEFAULT_ITEMS);
        assertThat(testRefund.getShipping()).isEqualTo(UPDATED_SHIPPING);
        assertThat(testRefund.getAdjustment()).isEqualTo(DEFAULT_ADJUSTMENT);
        assertThat(testRefund.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testRefund.getMethod()).isEqualTo(DEFAULT_METHOD);
        assertThat(testRefund.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testRefund.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testRefund.getTransactionid()).isEqualTo(DEFAULT_TRANSACTIONID);
        assertThat(testRefund.getMetadata()).isEqualTo(DEFAULT_METADATA);
    }

    @Test
    @Transactional
    void fullUpdateRefundWithPatch() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        int databaseSizeBeforeUpdate = refundRepository.findAll().size();

        // Update the refund using partial update
        Refund partialUpdatedRefund = new Refund();
        partialUpdatedRefund.setId(refund.getId());

        partialUpdatedRefund
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .items(UPDATED_ITEMS)
            .shipping(UPDATED_SHIPPING)
            .adjustment(UPDATED_ADJUSTMENT)
            .total(UPDATED_TOTAL)
            .method(UPDATED_METHOD)
            .reason(UPDATED_REASON)
            .state(UPDATED_STATE)
            .transactionid(UPDATED_TRANSACTIONID)
            .metadata(UPDATED_METADATA);

        restRefundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRefund.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRefund))
            )
            .andExpect(status().isOk());

        // Validate the Refund in the database
        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeUpdate);
        Refund testRefund = refundList.get(refundList.size() - 1);
        assertThat(testRefund.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testRefund.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testRefund.getItems()).isEqualTo(UPDATED_ITEMS);
        assertThat(testRefund.getShipping()).isEqualTo(UPDATED_SHIPPING);
        assertThat(testRefund.getAdjustment()).isEqualTo(UPDATED_ADJUSTMENT);
        assertThat(testRefund.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testRefund.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testRefund.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testRefund.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testRefund.getTransactionid()).isEqualTo(UPDATED_TRANSACTIONID);
        assertThat(testRefund.getMetadata()).isEqualTo(UPDATED_METADATA);
    }

    @Test
    @Transactional
    void patchNonExistingRefund() throws Exception {
        int databaseSizeBeforeUpdate = refundRepository.findAll().size();
        refund.setId(count.incrementAndGet());

        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRefundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, refundDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(refundDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refund in the database
        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRefund() throws Exception {
        int databaseSizeBeforeUpdate = refundRepository.findAll().size();
        refund.setId(count.incrementAndGet());

        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(refundDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Refund in the database
        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRefund() throws Exception {
        int databaseSizeBeforeUpdate = refundRepository.findAll().size();
        refund.setId(count.incrementAndGet());

        // Create the Refund
        RefundDTO refundDTO = refundMapper.toDto(refund);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRefundMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(refundDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Refund in the database
        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRefund() throws Exception {
        // Initialize the database
        refundRepository.saveAndFlush(refund);

        int databaseSizeBeforeDelete = refundRepository.findAll().size();

        // Delete the refund
        restRefundMockMvc
            .perform(delete(ENTITY_API_URL_ID, refund.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Refund> refundList = refundRepository.findAll();
        assertThat(refundList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
