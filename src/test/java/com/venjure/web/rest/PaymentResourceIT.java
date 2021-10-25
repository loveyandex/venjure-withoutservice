package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Jorder;
import com.venjure.domain.Payment;
import com.venjure.domain.Refund;
import com.venjure.repository.PaymentRepository;
import com.venjure.service.criteria.PaymentCriteria;
import com.venjure.service.dto.PaymentDTO;
import com.venjure.service.mapper.PaymentMapper;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_METHOD = "BBBBBBBBBB";

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;
    private static final Integer SMALLER_AMOUNT = 1 - 1;

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ERRORMESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERRORMESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTIONID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTIONID = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .method(DEFAULT_METHOD)
            .amount(DEFAULT_AMOUNT)
            .state(DEFAULT_STATE)
            .errormessage(DEFAULT_ERRORMESSAGE)
            .transactionid(DEFAULT_TRANSACTIONID)
            .metadata(DEFAULT_METADATA);
        return payment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .method(UPDATED_METHOD)
            .amount(UPDATED_AMOUNT)
            .state(UPDATED_STATE)
            .errormessage(UPDATED_ERRORMESSAGE)
            .transactionid(UPDATED_TRANSACTIONID)
            .metadata(UPDATED_METADATA);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testPayment.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testPayment.getMethod()).isEqualTo(DEFAULT_METHOD);
        assertThat(testPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPayment.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testPayment.getErrormessage()).isEqualTo(DEFAULT_ERRORMESSAGE);
        assertThat(testPayment.getTransactionid()).isEqualTo(DEFAULT_TRANSACTIONID);
        assertThat(testPayment.getMetadata()).isEqualTo(DEFAULT_METADATA);
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setCreatedat(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setUpdatedat(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setMethod(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setAmount(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setState(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMetadataIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setMetadata(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].errormessage").value(hasItem(DEFAULT_ERRORMESSAGE)))
            .andExpect(jsonPath("$.[*].transactionid").value(hasItem(DEFAULT_TRANSACTIONID)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.errormessage").value(DEFAULT_ERRORMESSAGE))
            .andExpect(jsonPath("$.transactionid").value(DEFAULT_TRANSACTIONID))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA));
    }

    @Test
    @Transactional
    void getPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        Long id = payment.getId();

        defaultPaymentShouldBeFound("id.equals=" + id);
        defaultPaymentShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdat equals to DEFAULT_CREATEDAT
        defaultPaymentShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the paymentList where createdat equals to UPDATED_CREATEDAT
        defaultPaymentShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdat not equals to DEFAULT_CREATEDAT
        defaultPaymentShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the paymentList where createdat not equals to UPDATED_CREATEDAT
        defaultPaymentShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultPaymentShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the paymentList where createdat equals to UPDATED_CREATEDAT
        defaultPaymentShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdat is not null
        defaultPaymentShouldBeFound("createdat.specified=true");

        // Get all the paymentList where createdat is null
        defaultPaymentShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where updatedat equals to DEFAULT_UPDATEDAT
        defaultPaymentShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the paymentList where updatedat equals to UPDATED_UPDATEDAT
        defaultPaymentShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllPaymentsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultPaymentShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the paymentList where updatedat not equals to UPDATED_UPDATEDAT
        defaultPaymentShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllPaymentsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultPaymentShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the paymentList where updatedat equals to UPDATED_UPDATEDAT
        defaultPaymentShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllPaymentsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where updatedat is not null
        defaultPaymentShouldBeFound("updatedat.specified=true");

        // Get all the paymentList where updatedat is null
        defaultPaymentShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where method equals to DEFAULT_METHOD
        defaultPaymentShouldBeFound("method.equals=" + DEFAULT_METHOD);

        // Get all the paymentList where method equals to UPDATED_METHOD
        defaultPaymentShouldNotBeFound("method.equals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByMethodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where method not equals to DEFAULT_METHOD
        defaultPaymentShouldNotBeFound("method.notEquals=" + DEFAULT_METHOD);

        // Get all the paymentList where method not equals to UPDATED_METHOD
        defaultPaymentShouldBeFound("method.notEquals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByMethodIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where method in DEFAULT_METHOD or UPDATED_METHOD
        defaultPaymentShouldBeFound("method.in=" + DEFAULT_METHOD + "," + UPDATED_METHOD);

        // Get all the paymentList where method equals to UPDATED_METHOD
        defaultPaymentShouldNotBeFound("method.in=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where method is not null
        defaultPaymentShouldBeFound("method.specified=true");

        // Get all the paymentList where method is null
        defaultPaymentShouldNotBeFound("method.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByMethodContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where method contains DEFAULT_METHOD
        defaultPaymentShouldBeFound("method.contains=" + DEFAULT_METHOD);

        // Get all the paymentList where method contains UPDATED_METHOD
        defaultPaymentShouldNotBeFound("method.contains=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByMethodNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where method does not contain DEFAULT_METHOD
        defaultPaymentShouldNotBeFound("method.doesNotContain=" + DEFAULT_METHOD);

        // Get all the paymentList where method does not contain UPDATED_METHOD
        defaultPaymentShouldBeFound("method.doesNotContain=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount equals to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount equals to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount not equals to DEFAULT_AMOUNT
        defaultPaymentShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount not equals to UPDATED_AMOUNT
        defaultPaymentShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultPaymentShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the paymentList where amount equals to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is not null
        defaultPaymentShouldBeFound("amount.specified=true");

        // Get all the paymentList where amount is null
        defaultPaymentShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is greater than or equal to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than or equal to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is less than or equal to SMALLER_AMOUNT
        defaultPaymentShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than DEFAULT_AMOUNT
        defaultPaymentShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is less than UPDATED_AMOUNT
        defaultPaymentShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than DEFAULT_AMOUNT
        defaultPaymentShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is greater than SMALLER_AMOUNT
        defaultPaymentShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where state equals to DEFAULT_STATE
        defaultPaymentShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the paymentList where state equals to UPDATED_STATE
        defaultPaymentShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where state not equals to DEFAULT_STATE
        defaultPaymentShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the paymentList where state not equals to UPDATED_STATE
        defaultPaymentShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where state in DEFAULT_STATE or UPDATED_STATE
        defaultPaymentShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the paymentList where state equals to UPDATED_STATE
        defaultPaymentShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where state is not null
        defaultPaymentShouldBeFound("state.specified=true");

        // Get all the paymentList where state is null
        defaultPaymentShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByStateContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where state contains DEFAULT_STATE
        defaultPaymentShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the paymentList where state contains UPDATED_STATE
        defaultPaymentShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByStateNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where state does not contain DEFAULT_STATE
        defaultPaymentShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the paymentList where state does not contain UPDATED_STATE
        defaultPaymentShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByErrormessageIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where errormessage equals to DEFAULT_ERRORMESSAGE
        defaultPaymentShouldBeFound("errormessage.equals=" + DEFAULT_ERRORMESSAGE);

        // Get all the paymentList where errormessage equals to UPDATED_ERRORMESSAGE
        defaultPaymentShouldNotBeFound("errormessage.equals=" + UPDATED_ERRORMESSAGE);
    }

    @Test
    @Transactional
    void getAllPaymentsByErrormessageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where errormessage not equals to DEFAULT_ERRORMESSAGE
        defaultPaymentShouldNotBeFound("errormessage.notEquals=" + DEFAULT_ERRORMESSAGE);

        // Get all the paymentList where errormessage not equals to UPDATED_ERRORMESSAGE
        defaultPaymentShouldBeFound("errormessage.notEquals=" + UPDATED_ERRORMESSAGE);
    }

    @Test
    @Transactional
    void getAllPaymentsByErrormessageIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where errormessage in DEFAULT_ERRORMESSAGE or UPDATED_ERRORMESSAGE
        defaultPaymentShouldBeFound("errormessage.in=" + DEFAULT_ERRORMESSAGE + "," + UPDATED_ERRORMESSAGE);

        // Get all the paymentList where errormessage equals to UPDATED_ERRORMESSAGE
        defaultPaymentShouldNotBeFound("errormessage.in=" + UPDATED_ERRORMESSAGE);
    }

    @Test
    @Transactional
    void getAllPaymentsByErrormessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where errormessage is not null
        defaultPaymentShouldBeFound("errormessage.specified=true");

        // Get all the paymentList where errormessage is null
        defaultPaymentShouldNotBeFound("errormessage.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByErrormessageContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where errormessage contains DEFAULT_ERRORMESSAGE
        defaultPaymentShouldBeFound("errormessage.contains=" + DEFAULT_ERRORMESSAGE);

        // Get all the paymentList where errormessage contains UPDATED_ERRORMESSAGE
        defaultPaymentShouldNotBeFound("errormessage.contains=" + UPDATED_ERRORMESSAGE);
    }

    @Test
    @Transactional
    void getAllPaymentsByErrormessageNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where errormessage does not contain DEFAULT_ERRORMESSAGE
        defaultPaymentShouldNotBeFound("errormessage.doesNotContain=" + DEFAULT_ERRORMESSAGE);

        // Get all the paymentList where errormessage does not contain UPDATED_ERRORMESSAGE
        defaultPaymentShouldBeFound("errormessage.doesNotContain=" + UPDATED_ERRORMESSAGE);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionidIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionid equals to DEFAULT_TRANSACTIONID
        defaultPaymentShouldBeFound("transactionid.equals=" + DEFAULT_TRANSACTIONID);

        // Get all the paymentList where transactionid equals to UPDATED_TRANSACTIONID
        defaultPaymentShouldNotBeFound("transactionid.equals=" + UPDATED_TRANSACTIONID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionid not equals to DEFAULT_TRANSACTIONID
        defaultPaymentShouldNotBeFound("transactionid.notEquals=" + DEFAULT_TRANSACTIONID);

        // Get all the paymentList where transactionid not equals to UPDATED_TRANSACTIONID
        defaultPaymentShouldBeFound("transactionid.notEquals=" + UPDATED_TRANSACTIONID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionidIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionid in DEFAULT_TRANSACTIONID or UPDATED_TRANSACTIONID
        defaultPaymentShouldBeFound("transactionid.in=" + DEFAULT_TRANSACTIONID + "," + UPDATED_TRANSACTIONID);

        // Get all the paymentList where transactionid equals to UPDATED_TRANSACTIONID
        defaultPaymentShouldNotBeFound("transactionid.in=" + UPDATED_TRANSACTIONID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionidIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionid is not null
        defaultPaymentShouldBeFound("transactionid.specified=true");

        // Get all the paymentList where transactionid is null
        defaultPaymentShouldNotBeFound("transactionid.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionidContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionid contains DEFAULT_TRANSACTIONID
        defaultPaymentShouldBeFound("transactionid.contains=" + DEFAULT_TRANSACTIONID);

        // Get all the paymentList where transactionid contains UPDATED_TRANSACTIONID
        defaultPaymentShouldNotBeFound("transactionid.contains=" + UPDATED_TRANSACTIONID);
    }

    @Test
    @Transactional
    void getAllPaymentsByTransactionidNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where transactionid does not contain DEFAULT_TRANSACTIONID
        defaultPaymentShouldNotBeFound("transactionid.doesNotContain=" + DEFAULT_TRANSACTIONID);

        // Get all the paymentList where transactionid does not contain UPDATED_TRANSACTIONID
        defaultPaymentShouldBeFound("transactionid.doesNotContain=" + UPDATED_TRANSACTIONID);
    }

    @Test
    @Transactional
    void getAllPaymentsByMetadataIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where metadata equals to DEFAULT_METADATA
        defaultPaymentShouldBeFound("metadata.equals=" + DEFAULT_METADATA);

        // Get all the paymentList where metadata equals to UPDATED_METADATA
        defaultPaymentShouldNotBeFound("metadata.equals=" + UPDATED_METADATA);
    }

    @Test
    @Transactional
    void getAllPaymentsByMetadataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where metadata not equals to DEFAULT_METADATA
        defaultPaymentShouldNotBeFound("metadata.notEquals=" + DEFAULT_METADATA);

        // Get all the paymentList where metadata not equals to UPDATED_METADATA
        defaultPaymentShouldBeFound("metadata.notEquals=" + UPDATED_METADATA);
    }

    @Test
    @Transactional
    void getAllPaymentsByMetadataIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where metadata in DEFAULT_METADATA or UPDATED_METADATA
        defaultPaymentShouldBeFound("metadata.in=" + DEFAULT_METADATA + "," + UPDATED_METADATA);

        // Get all the paymentList where metadata equals to UPDATED_METADATA
        defaultPaymentShouldNotBeFound("metadata.in=" + UPDATED_METADATA);
    }

    @Test
    @Transactional
    void getAllPaymentsByMetadataIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where metadata is not null
        defaultPaymentShouldBeFound("metadata.specified=true");

        // Get all the paymentList where metadata is null
        defaultPaymentShouldNotBeFound("metadata.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByMetadataContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where metadata contains DEFAULT_METADATA
        defaultPaymentShouldBeFound("metadata.contains=" + DEFAULT_METADATA);

        // Get all the paymentList where metadata contains UPDATED_METADATA
        defaultPaymentShouldNotBeFound("metadata.contains=" + UPDATED_METADATA);
    }

    @Test
    @Transactional
    void getAllPaymentsByMetadataNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where metadata does not contain DEFAULT_METADATA
        defaultPaymentShouldNotBeFound("metadata.doesNotContain=" + DEFAULT_METADATA);

        // Get all the paymentList where metadata does not contain UPDATED_METADATA
        defaultPaymentShouldBeFound("metadata.doesNotContain=" + UPDATED_METADATA);
    }

    @Test
    @Transactional
    void getAllPaymentsByJorderIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        Jorder jorder = JorderResourceIT.createEntity(em);
        em.persist(jorder);
        em.flush();
        payment.setJorder(jorder);
        paymentRepository.saveAndFlush(payment);
        Long jorderId = jorder.getId();

        // Get all the paymentList where jorder equals to jorderId
        defaultPaymentShouldBeFound("jorderId.equals=" + jorderId);

        // Get all the paymentList where jorder equals to (jorderId + 1)
        defaultPaymentShouldNotBeFound("jorderId.equals=" + (jorderId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByRefundIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        Refund refund = RefundResourceIT.createEntity(em);
        em.persist(refund);
        em.flush();
        payment.addRefund(refund);
        paymentRepository.saveAndFlush(payment);
        Long refundId = refund.getId();

        // Get all the paymentList where refund equals to refundId
        defaultPaymentShouldBeFound("refundId.equals=" + refundId);

        // Get all the paymentList where refund equals to (refundId + 1)
        defaultPaymentShouldNotBeFound("refundId.equals=" + (refundId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].errormessage").value(hasItem(DEFAULT_ERRORMESSAGE)))
            .andExpect(jsonPath("$.[*].transactionid").value(hasItem(DEFAULT_TRANSACTIONID)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)));

        // Check, that the count call also returns 1
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .method(UPDATED_METHOD)
            .amount(UPDATED_AMOUNT)
            .state(UPDATED_STATE)
            .errormessage(UPDATED_ERRORMESSAGE)
            .transactionid(UPDATED_TRANSACTIONID)
            .metadata(UPDATED_METADATA);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testPayment.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testPayment.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testPayment.getErrormessage()).isEqualTo(UPDATED_ERRORMESSAGE);
        assertThat(testPayment.getTransactionid()).isEqualTo(UPDATED_TRANSACTIONID);
        assertThat(testPayment.getMetadata()).isEqualTo(UPDATED_METADATA);
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment.method(UPDATED_METHOD).amount(UPDATED_AMOUNT).transactionid(UPDATED_TRANSACTIONID);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testPayment.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testPayment.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testPayment.getErrormessage()).isEqualTo(DEFAULT_ERRORMESSAGE);
        assertThat(testPayment.getTransactionid()).isEqualTo(UPDATED_TRANSACTIONID);
        assertThat(testPayment.getMetadata()).isEqualTo(DEFAULT_METADATA);
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .method(UPDATED_METHOD)
            .amount(UPDATED_AMOUNT)
            .state(UPDATED_STATE)
            .errormessage(UPDATED_ERRORMESSAGE)
            .transactionid(UPDATED_TRANSACTIONID)
            .metadata(UPDATED_METADATA);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testPayment.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testPayment.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testPayment.getErrormessage()).isEqualTo(UPDATED_ERRORMESSAGE);
        assertThat(testPayment.getTransactionid()).isEqualTo(UPDATED_TRANSACTIONID);
        assertThat(testPayment.getMetadata()).isEqualTo(UPDATED_METADATA);
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
