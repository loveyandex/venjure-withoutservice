package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Jorder;
import com.venjure.domain.OrderItem;
import com.venjure.domain.OrderModification;
import com.venjure.domain.Payment;
import com.venjure.domain.Refund;
import com.venjure.domain.Surcharge;
import com.venjure.repository.OrderModificationRepository;
import com.venjure.service.criteria.OrderModificationCriteria;
import com.venjure.service.dto.OrderModificationDTO;
import com.venjure.service.mapper.OrderModificationMapper;
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
 * Integration tests for the {@link OrderModificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderModificationResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRICECHANGE = 1;
    private static final Integer UPDATED_PRICECHANGE = 2;
    private static final Integer SMALLER_PRICECHANGE = 1 - 1;

    private static final String DEFAULT_SHIPPINGADDRESSCHANGE = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPINGADDRESSCHANGE = "BBBBBBBBBB";

    private static final String DEFAULT_BILLINGADDRESSCHANGE = "AAAAAAAAAA";
    private static final String UPDATED_BILLINGADDRESSCHANGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-modifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderModificationRepository orderModificationRepository;

    @Autowired
    private OrderModificationMapper orderModificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderModificationMockMvc;

    private OrderModification orderModification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderModification createEntity(EntityManager em) {
        OrderModification orderModification = new OrderModification()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .note(DEFAULT_NOTE)
            .pricechange(DEFAULT_PRICECHANGE)
            .shippingaddresschange(DEFAULT_SHIPPINGADDRESSCHANGE)
            .billingaddresschange(DEFAULT_BILLINGADDRESSCHANGE);
        return orderModification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderModification createUpdatedEntity(EntityManager em) {
        OrderModification orderModification = new OrderModification()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .note(UPDATED_NOTE)
            .pricechange(UPDATED_PRICECHANGE)
            .shippingaddresschange(UPDATED_SHIPPINGADDRESSCHANGE)
            .billingaddresschange(UPDATED_BILLINGADDRESSCHANGE);
        return orderModification;
    }

    @BeforeEach
    public void initTest() {
        orderModification = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderModification() throws Exception {
        int databaseSizeBeforeCreate = orderModificationRepository.findAll().size();
        // Create the OrderModification
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(orderModification);
        restOrderModificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrderModification in the database
        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeCreate + 1);
        OrderModification testOrderModification = orderModificationList.get(orderModificationList.size() - 1);
        assertThat(testOrderModification.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testOrderModification.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testOrderModification.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testOrderModification.getPricechange()).isEqualTo(DEFAULT_PRICECHANGE);
        assertThat(testOrderModification.getShippingaddresschange()).isEqualTo(DEFAULT_SHIPPINGADDRESSCHANGE);
        assertThat(testOrderModification.getBillingaddresschange()).isEqualTo(DEFAULT_BILLINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void createOrderModificationWithExistingId() throws Exception {
        // Create the OrderModification with an existing ID
        orderModification.setId(1L);
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(orderModification);

        int databaseSizeBeforeCreate = orderModificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderModificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderModification in the database
        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderModificationRepository.findAll().size();
        // set the field null
        orderModification.setCreatedat(null);

        // Create the OrderModification, which fails.
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(orderModification);

        restOrderModificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderModificationRepository.findAll().size();
        // set the field null
        orderModification.setUpdatedat(null);

        // Create the OrderModification, which fails.
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(orderModification);

        restOrderModificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderModificationRepository.findAll().size();
        // set the field null
        orderModification.setNote(null);

        // Create the OrderModification, which fails.
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(orderModification);

        restOrderModificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricechangeIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderModificationRepository.findAll().size();
        // set the field null
        orderModification.setPricechange(null);

        // Create the OrderModification, which fails.
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(orderModification);

        restOrderModificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderModifications() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList
        restOrderModificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderModification.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].pricechange").value(hasItem(DEFAULT_PRICECHANGE)))
            .andExpect(jsonPath("$.[*].shippingaddresschange").value(hasItem(DEFAULT_SHIPPINGADDRESSCHANGE)))
            .andExpect(jsonPath("$.[*].billingaddresschange").value(hasItem(DEFAULT_BILLINGADDRESSCHANGE)));
    }

    @Test
    @Transactional
    void getOrderModification() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get the orderModification
        restOrderModificationMockMvc
            .perform(get(ENTITY_API_URL_ID, orderModification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderModification.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.pricechange").value(DEFAULT_PRICECHANGE))
            .andExpect(jsonPath("$.shippingaddresschange").value(DEFAULT_SHIPPINGADDRESSCHANGE))
            .andExpect(jsonPath("$.billingaddresschange").value(DEFAULT_BILLINGADDRESSCHANGE));
    }

    @Test
    @Transactional
    void getOrderModificationsByIdFiltering() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        Long id = orderModification.getId();

        defaultOrderModificationShouldBeFound("id.equals=" + id);
        defaultOrderModificationShouldNotBeFound("id.notEquals=" + id);

        defaultOrderModificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderModificationShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderModificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderModificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where createdat equals to DEFAULT_CREATEDAT
        defaultOrderModificationShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the orderModificationList where createdat equals to UPDATED_CREATEDAT
        defaultOrderModificationShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where createdat not equals to DEFAULT_CREATEDAT
        defaultOrderModificationShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the orderModificationList where createdat not equals to UPDATED_CREATEDAT
        defaultOrderModificationShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultOrderModificationShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the orderModificationList where createdat equals to UPDATED_CREATEDAT
        defaultOrderModificationShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where createdat is not null
        defaultOrderModificationShouldBeFound("createdat.specified=true");

        // Get all the orderModificationList where createdat is null
        defaultOrderModificationShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderModificationsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where updatedat equals to DEFAULT_UPDATEDAT
        defaultOrderModificationShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the orderModificationList where updatedat equals to UPDATED_UPDATEDAT
        defaultOrderModificationShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultOrderModificationShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the orderModificationList where updatedat not equals to UPDATED_UPDATEDAT
        defaultOrderModificationShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultOrderModificationShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the orderModificationList where updatedat equals to UPDATED_UPDATEDAT
        defaultOrderModificationShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where updatedat is not null
        defaultOrderModificationShouldBeFound("updatedat.specified=true");

        // Get all the orderModificationList where updatedat is null
        defaultOrderModificationShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderModificationsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where note equals to DEFAULT_NOTE
        defaultOrderModificationShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the orderModificationList where note equals to UPDATED_NOTE
        defaultOrderModificationShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where note not equals to DEFAULT_NOTE
        defaultOrderModificationShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the orderModificationList where note not equals to UPDATED_NOTE
        defaultOrderModificationShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultOrderModificationShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the orderModificationList where note equals to UPDATED_NOTE
        defaultOrderModificationShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where note is not null
        defaultOrderModificationShouldBeFound("note.specified=true");

        // Get all the orderModificationList where note is null
        defaultOrderModificationShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderModificationsByNoteContainsSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where note contains DEFAULT_NOTE
        defaultOrderModificationShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the orderModificationList where note contains UPDATED_NOTE
        defaultOrderModificationShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where note does not contain DEFAULT_NOTE
        defaultOrderModificationShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the orderModificationList where note does not contain UPDATED_NOTE
        defaultOrderModificationShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByPricechangeIsEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where pricechange equals to DEFAULT_PRICECHANGE
        defaultOrderModificationShouldBeFound("pricechange.equals=" + DEFAULT_PRICECHANGE);

        // Get all the orderModificationList where pricechange equals to UPDATED_PRICECHANGE
        defaultOrderModificationShouldNotBeFound("pricechange.equals=" + UPDATED_PRICECHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByPricechangeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where pricechange not equals to DEFAULT_PRICECHANGE
        defaultOrderModificationShouldNotBeFound("pricechange.notEquals=" + DEFAULT_PRICECHANGE);

        // Get all the orderModificationList where pricechange not equals to UPDATED_PRICECHANGE
        defaultOrderModificationShouldBeFound("pricechange.notEquals=" + UPDATED_PRICECHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByPricechangeIsInShouldWork() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where pricechange in DEFAULT_PRICECHANGE or UPDATED_PRICECHANGE
        defaultOrderModificationShouldBeFound("pricechange.in=" + DEFAULT_PRICECHANGE + "," + UPDATED_PRICECHANGE);

        // Get all the orderModificationList where pricechange equals to UPDATED_PRICECHANGE
        defaultOrderModificationShouldNotBeFound("pricechange.in=" + UPDATED_PRICECHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByPricechangeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where pricechange is not null
        defaultOrderModificationShouldBeFound("pricechange.specified=true");

        // Get all the orderModificationList where pricechange is null
        defaultOrderModificationShouldNotBeFound("pricechange.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderModificationsByPricechangeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where pricechange is greater than or equal to DEFAULT_PRICECHANGE
        defaultOrderModificationShouldBeFound("pricechange.greaterThanOrEqual=" + DEFAULT_PRICECHANGE);

        // Get all the orderModificationList where pricechange is greater than or equal to UPDATED_PRICECHANGE
        defaultOrderModificationShouldNotBeFound("pricechange.greaterThanOrEqual=" + UPDATED_PRICECHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByPricechangeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where pricechange is less than or equal to DEFAULT_PRICECHANGE
        defaultOrderModificationShouldBeFound("pricechange.lessThanOrEqual=" + DEFAULT_PRICECHANGE);

        // Get all the orderModificationList where pricechange is less than or equal to SMALLER_PRICECHANGE
        defaultOrderModificationShouldNotBeFound("pricechange.lessThanOrEqual=" + SMALLER_PRICECHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByPricechangeIsLessThanSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where pricechange is less than DEFAULT_PRICECHANGE
        defaultOrderModificationShouldNotBeFound("pricechange.lessThan=" + DEFAULT_PRICECHANGE);

        // Get all the orderModificationList where pricechange is less than UPDATED_PRICECHANGE
        defaultOrderModificationShouldBeFound("pricechange.lessThan=" + UPDATED_PRICECHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByPricechangeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where pricechange is greater than DEFAULT_PRICECHANGE
        defaultOrderModificationShouldNotBeFound("pricechange.greaterThan=" + DEFAULT_PRICECHANGE);

        // Get all the orderModificationList where pricechange is greater than SMALLER_PRICECHANGE
        defaultOrderModificationShouldBeFound("pricechange.greaterThan=" + SMALLER_PRICECHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByShippingaddresschangeIsEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where shippingaddresschange equals to DEFAULT_SHIPPINGADDRESSCHANGE
        defaultOrderModificationShouldBeFound("shippingaddresschange.equals=" + DEFAULT_SHIPPINGADDRESSCHANGE);

        // Get all the orderModificationList where shippingaddresschange equals to UPDATED_SHIPPINGADDRESSCHANGE
        defaultOrderModificationShouldNotBeFound("shippingaddresschange.equals=" + UPDATED_SHIPPINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByShippingaddresschangeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where shippingaddresschange not equals to DEFAULT_SHIPPINGADDRESSCHANGE
        defaultOrderModificationShouldNotBeFound("shippingaddresschange.notEquals=" + DEFAULT_SHIPPINGADDRESSCHANGE);

        // Get all the orderModificationList where shippingaddresschange not equals to UPDATED_SHIPPINGADDRESSCHANGE
        defaultOrderModificationShouldBeFound("shippingaddresschange.notEquals=" + UPDATED_SHIPPINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByShippingaddresschangeIsInShouldWork() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where shippingaddresschange in DEFAULT_SHIPPINGADDRESSCHANGE or UPDATED_SHIPPINGADDRESSCHANGE
        defaultOrderModificationShouldBeFound(
            "shippingaddresschange.in=" + DEFAULT_SHIPPINGADDRESSCHANGE + "," + UPDATED_SHIPPINGADDRESSCHANGE
        );

        // Get all the orderModificationList where shippingaddresschange equals to UPDATED_SHIPPINGADDRESSCHANGE
        defaultOrderModificationShouldNotBeFound("shippingaddresschange.in=" + UPDATED_SHIPPINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByShippingaddresschangeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where shippingaddresschange is not null
        defaultOrderModificationShouldBeFound("shippingaddresschange.specified=true");

        // Get all the orderModificationList where shippingaddresschange is null
        defaultOrderModificationShouldNotBeFound("shippingaddresschange.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderModificationsByShippingaddresschangeContainsSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where shippingaddresschange contains DEFAULT_SHIPPINGADDRESSCHANGE
        defaultOrderModificationShouldBeFound("shippingaddresschange.contains=" + DEFAULT_SHIPPINGADDRESSCHANGE);

        // Get all the orderModificationList where shippingaddresschange contains UPDATED_SHIPPINGADDRESSCHANGE
        defaultOrderModificationShouldNotBeFound("shippingaddresschange.contains=" + UPDATED_SHIPPINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByShippingaddresschangeNotContainsSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where shippingaddresschange does not contain DEFAULT_SHIPPINGADDRESSCHANGE
        defaultOrderModificationShouldNotBeFound("shippingaddresschange.doesNotContain=" + DEFAULT_SHIPPINGADDRESSCHANGE);

        // Get all the orderModificationList where shippingaddresschange does not contain UPDATED_SHIPPINGADDRESSCHANGE
        defaultOrderModificationShouldBeFound("shippingaddresschange.doesNotContain=" + UPDATED_SHIPPINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByBillingaddresschangeIsEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where billingaddresschange equals to DEFAULT_BILLINGADDRESSCHANGE
        defaultOrderModificationShouldBeFound("billingaddresschange.equals=" + DEFAULT_BILLINGADDRESSCHANGE);

        // Get all the orderModificationList where billingaddresschange equals to UPDATED_BILLINGADDRESSCHANGE
        defaultOrderModificationShouldNotBeFound("billingaddresschange.equals=" + UPDATED_BILLINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByBillingaddresschangeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where billingaddresschange not equals to DEFAULT_BILLINGADDRESSCHANGE
        defaultOrderModificationShouldNotBeFound("billingaddresschange.notEquals=" + DEFAULT_BILLINGADDRESSCHANGE);

        // Get all the orderModificationList where billingaddresschange not equals to UPDATED_BILLINGADDRESSCHANGE
        defaultOrderModificationShouldBeFound("billingaddresschange.notEquals=" + UPDATED_BILLINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByBillingaddresschangeIsInShouldWork() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where billingaddresschange in DEFAULT_BILLINGADDRESSCHANGE or UPDATED_BILLINGADDRESSCHANGE
        defaultOrderModificationShouldBeFound(
            "billingaddresschange.in=" + DEFAULT_BILLINGADDRESSCHANGE + "," + UPDATED_BILLINGADDRESSCHANGE
        );

        // Get all the orderModificationList where billingaddresschange equals to UPDATED_BILLINGADDRESSCHANGE
        defaultOrderModificationShouldNotBeFound("billingaddresschange.in=" + UPDATED_BILLINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByBillingaddresschangeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where billingaddresschange is not null
        defaultOrderModificationShouldBeFound("billingaddresschange.specified=true");

        // Get all the orderModificationList where billingaddresschange is null
        defaultOrderModificationShouldNotBeFound("billingaddresschange.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderModificationsByBillingaddresschangeContainsSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where billingaddresschange contains DEFAULT_BILLINGADDRESSCHANGE
        defaultOrderModificationShouldBeFound("billingaddresschange.contains=" + DEFAULT_BILLINGADDRESSCHANGE);

        // Get all the orderModificationList where billingaddresschange contains UPDATED_BILLINGADDRESSCHANGE
        defaultOrderModificationShouldNotBeFound("billingaddresschange.contains=" + UPDATED_BILLINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByBillingaddresschangeNotContainsSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        // Get all the orderModificationList where billingaddresschange does not contain DEFAULT_BILLINGADDRESSCHANGE
        defaultOrderModificationShouldNotBeFound("billingaddresschange.doesNotContain=" + DEFAULT_BILLINGADDRESSCHANGE);

        // Get all the orderModificationList where billingaddresschange does not contain UPDATED_BILLINGADDRESSCHANGE
        defaultOrderModificationShouldBeFound("billingaddresschange.doesNotContain=" + UPDATED_BILLINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void getAllOrderModificationsByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);
        Payment payment = PaymentResourceIT.createEntity(em);
        em.persist(payment);
        em.flush();
        orderModification.setPayment(payment);
        orderModificationRepository.saveAndFlush(orderModification);
        Long paymentId = payment.getId();

        // Get all the orderModificationList where payment equals to paymentId
        defaultOrderModificationShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the orderModificationList where payment equals to (paymentId + 1)
        defaultOrderModificationShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderModificationsByRefundIsEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);
        Refund refund = RefundResourceIT.createEntity(em);
        em.persist(refund);
        em.flush();
        orderModification.setRefund(refund);
        orderModificationRepository.saveAndFlush(orderModification);
        Long refundId = refund.getId();

        // Get all the orderModificationList where refund equals to refundId
        defaultOrderModificationShouldBeFound("refundId.equals=" + refundId);

        // Get all the orderModificationList where refund equals to (refundId + 1)
        defaultOrderModificationShouldNotBeFound("refundId.equals=" + (refundId + 1));
    }

    @Test
    @Transactional
    void getAllOrderModificationsByJorderIsEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);
        Jorder jorder = JorderResourceIT.createEntity(em);
        em.persist(jorder);
        em.flush();
        orderModification.setJorder(jorder);
        orderModificationRepository.saveAndFlush(orderModification);
        Long jorderId = jorder.getId();

        // Get all the orderModificationList where jorder equals to jorderId
        defaultOrderModificationShouldBeFound("jorderId.equals=" + jorderId);

        // Get all the orderModificationList where jorder equals to (jorderId + 1)
        defaultOrderModificationShouldNotBeFound("jorderId.equals=" + (jorderId + 1));
    }

    @Test
    @Transactional
    void getAllOrderModificationsBySurchargeIsEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);
        Surcharge surcharge = SurchargeResourceIT.createEntity(em);
        em.persist(surcharge);
        em.flush();
        orderModification.addSurcharge(surcharge);
        orderModificationRepository.saveAndFlush(orderModification);
        Long surchargeId = surcharge.getId();

        // Get all the orderModificationList where surcharge equals to surchargeId
        defaultOrderModificationShouldBeFound("surchargeId.equals=" + surchargeId);

        // Get all the orderModificationList where surcharge equals to (surchargeId + 1)
        defaultOrderModificationShouldNotBeFound("surchargeId.equals=" + (surchargeId + 1));
    }

    @Test
    @Transactional
    void getAllOrderModificationsByOrderItemIsEqualToSomething() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);
        OrderItem orderItem = OrderItemResourceIT.createEntity(em);
        em.persist(orderItem);
        em.flush();
        orderModification.addOrderItem(orderItem);
        orderModificationRepository.saveAndFlush(orderModification);
        Long orderItemId = orderItem.getId();

        // Get all the orderModificationList where orderItem equals to orderItemId
        defaultOrderModificationShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the orderModificationList where orderItem equals to (orderItemId + 1)
        defaultOrderModificationShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderModificationShouldBeFound(String filter) throws Exception {
        restOrderModificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderModification.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].pricechange").value(hasItem(DEFAULT_PRICECHANGE)))
            .andExpect(jsonPath("$.[*].shippingaddresschange").value(hasItem(DEFAULT_SHIPPINGADDRESSCHANGE)))
            .andExpect(jsonPath("$.[*].billingaddresschange").value(hasItem(DEFAULT_BILLINGADDRESSCHANGE)));

        // Check, that the count call also returns 1
        restOrderModificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderModificationShouldNotBeFound(String filter) throws Exception {
        restOrderModificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderModificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderModification() throws Exception {
        // Get the orderModification
        restOrderModificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderModification() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        int databaseSizeBeforeUpdate = orderModificationRepository.findAll().size();

        // Update the orderModification
        OrderModification updatedOrderModification = orderModificationRepository.findById(orderModification.getId()).get();
        // Disconnect from session so that the updates on updatedOrderModification are not directly saved in db
        em.detach(updatedOrderModification);
        updatedOrderModification
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .note(UPDATED_NOTE)
            .pricechange(UPDATED_PRICECHANGE)
            .shippingaddresschange(UPDATED_SHIPPINGADDRESSCHANGE)
            .billingaddresschange(UPDATED_BILLINGADDRESSCHANGE);
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(updatedOrderModification);

        restOrderModificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderModificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderModification in the database
        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeUpdate);
        OrderModification testOrderModification = orderModificationList.get(orderModificationList.size() - 1);
        assertThat(testOrderModification.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testOrderModification.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testOrderModification.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testOrderModification.getPricechange()).isEqualTo(UPDATED_PRICECHANGE);
        assertThat(testOrderModification.getShippingaddresschange()).isEqualTo(UPDATED_SHIPPINGADDRESSCHANGE);
        assertThat(testOrderModification.getBillingaddresschange()).isEqualTo(UPDATED_BILLINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void putNonExistingOrderModification() throws Exception {
        int databaseSizeBeforeUpdate = orderModificationRepository.findAll().size();
        orderModification.setId(count.incrementAndGet());

        // Create the OrderModification
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(orderModification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderModificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderModificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderModification in the database
        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderModification() throws Exception {
        int databaseSizeBeforeUpdate = orderModificationRepository.findAll().size();
        orderModification.setId(count.incrementAndGet());

        // Create the OrderModification
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(orderModification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderModificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderModification in the database
        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderModification() throws Exception {
        int databaseSizeBeforeUpdate = orderModificationRepository.findAll().size();
        orderModification.setId(count.incrementAndGet());

        // Create the OrderModification
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(orderModification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderModificationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderModification in the database
        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderModificationWithPatch() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        int databaseSizeBeforeUpdate = orderModificationRepository.findAll().size();

        // Update the orderModification using partial update
        OrderModification partialUpdatedOrderModification = new OrderModification();
        partialUpdatedOrderModification.setId(orderModification.getId());

        partialUpdatedOrderModification.pricechange(UPDATED_PRICECHANGE).billingaddresschange(UPDATED_BILLINGADDRESSCHANGE);

        restOrderModificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderModification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderModification))
            )
            .andExpect(status().isOk());

        // Validate the OrderModification in the database
        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeUpdate);
        OrderModification testOrderModification = orderModificationList.get(orderModificationList.size() - 1);
        assertThat(testOrderModification.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testOrderModification.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testOrderModification.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testOrderModification.getPricechange()).isEqualTo(UPDATED_PRICECHANGE);
        assertThat(testOrderModification.getShippingaddresschange()).isEqualTo(DEFAULT_SHIPPINGADDRESSCHANGE);
        assertThat(testOrderModification.getBillingaddresschange()).isEqualTo(UPDATED_BILLINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void fullUpdateOrderModificationWithPatch() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        int databaseSizeBeforeUpdate = orderModificationRepository.findAll().size();

        // Update the orderModification using partial update
        OrderModification partialUpdatedOrderModification = new OrderModification();
        partialUpdatedOrderModification.setId(orderModification.getId());

        partialUpdatedOrderModification
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .note(UPDATED_NOTE)
            .pricechange(UPDATED_PRICECHANGE)
            .shippingaddresschange(UPDATED_SHIPPINGADDRESSCHANGE)
            .billingaddresschange(UPDATED_BILLINGADDRESSCHANGE);

        restOrderModificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderModification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderModification))
            )
            .andExpect(status().isOk());

        // Validate the OrderModification in the database
        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeUpdate);
        OrderModification testOrderModification = orderModificationList.get(orderModificationList.size() - 1);
        assertThat(testOrderModification.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testOrderModification.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testOrderModification.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testOrderModification.getPricechange()).isEqualTo(UPDATED_PRICECHANGE);
        assertThat(testOrderModification.getShippingaddresschange()).isEqualTo(UPDATED_SHIPPINGADDRESSCHANGE);
        assertThat(testOrderModification.getBillingaddresschange()).isEqualTo(UPDATED_BILLINGADDRESSCHANGE);
    }

    @Test
    @Transactional
    void patchNonExistingOrderModification() throws Exception {
        int databaseSizeBeforeUpdate = orderModificationRepository.findAll().size();
        orderModification.setId(count.incrementAndGet());

        // Create the OrderModification
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(orderModification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderModificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderModificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderModification in the database
        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderModification() throws Exception {
        int databaseSizeBeforeUpdate = orderModificationRepository.findAll().size();
        orderModification.setId(count.incrementAndGet());

        // Create the OrderModification
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(orderModification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderModificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderModification in the database
        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderModification() throws Exception {
        int databaseSizeBeforeUpdate = orderModificationRepository.findAll().size();
        orderModification.setId(count.incrementAndGet());

        // Create the OrderModification
        OrderModificationDTO orderModificationDTO = orderModificationMapper.toDto(orderModification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderModificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderModificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderModification in the database
        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderModification() throws Exception {
        // Initialize the database
        orderModificationRepository.saveAndFlush(orderModification);

        int databaseSizeBeforeDelete = orderModificationRepository.findAll().size();

        // Delete the orderModification
        restOrderModificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderModification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderModification> orderModificationList = orderModificationRepository.findAll();
        assertThat(orderModificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
