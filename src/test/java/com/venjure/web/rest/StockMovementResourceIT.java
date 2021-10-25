package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.OrderItem;
import com.venjure.domain.OrderLine;
import com.venjure.domain.ProductVariant;
import com.venjure.domain.StockMovement;
import com.venjure.repository.StockMovementRepository;
import com.venjure.service.criteria.StockMovementCriteria;
import com.venjure.service.dto.StockMovementDTO;
import com.venjure.service.mapper.StockMovementMapper;
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
 * Integration tests for the {@link StockMovementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockMovementResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String DEFAULT_DISCRIMINATOR = "AAAAAAAAAA";
    private static final String UPDATED_DISCRIMINATOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/stock-movements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private StockMovementMapper stockMovementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockMovementMockMvc;

    private StockMovement stockMovement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockMovement createEntity(EntityManager em) {
        StockMovement stockMovement = new StockMovement()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .type(DEFAULT_TYPE)
            .quantity(DEFAULT_QUANTITY)
            .discriminator(DEFAULT_DISCRIMINATOR);
        return stockMovement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockMovement createUpdatedEntity(EntityManager em) {
        StockMovement stockMovement = new StockMovement()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .type(UPDATED_TYPE)
            .quantity(UPDATED_QUANTITY)
            .discriminator(UPDATED_DISCRIMINATOR);
        return stockMovement;
    }

    @BeforeEach
    public void initTest() {
        stockMovement = createEntity(em);
    }

    @Test
    @Transactional
    void createStockMovement() throws Exception {
        int databaseSizeBeforeCreate = stockMovementRepository.findAll().size();
        // Create the StockMovement
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);
        restStockMovementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the StockMovement in the database
        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeCreate + 1);
        StockMovement testStockMovement = stockMovementList.get(stockMovementList.size() - 1);
        assertThat(testStockMovement.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testStockMovement.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testStockMovement.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testStockMovement.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testStockMovement.getDiscriminator()).isEqualTo(DEFAULT_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void createStockMovementWithExistingId() throws Exception {
        // Create the StockMovement with an existing ID
        stockMovement.setId(1L);
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);

        int databaseSizeBeforeCreate = stockMovementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockMovementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockMovement in the database
        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockMovementRepository.findAll().size();
        // set the field null
        stockMovement.setCreatedat(null);

        // Create the StockMovement, which fails.
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);

        restStockMovementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isBadRequest());

        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockMovementRepository.findAll().size();
        // set the field null
        stockMovement.setUpdatedat(null);

        // Create the StockMovement, which fails.
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);

        restStockMovementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isBadRequest());

        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockMovementRepository.findAll().size();
        // set the field null
        stockMovement.setType(null);

        // Create the StockMovement, which fails.
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);

        restStockMovementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isBadRequest());

        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockMovementRepository.findAll().size();
        // set the field null
        stockMovement.setQuantity(null);

        // Create the StockMovement, which fails.
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);

        restStockMovementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isBadRequest());

        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscriminatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockMovementRepository.findAll().size();
        // set the field null
        stockMovement.setDiscriminator(null);

        // Create the StockMovement, which fails.
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);

        restStockMovementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isBadRequest());

        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStockMovements() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList
        restStockMovementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockMovement.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].discriminator").value(hasItem(DEFAULT_DISCRIMINATOR)));
    }

    @Test
    @Transactional
    void getStockMovement() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get the stockMovement
        restStockMovementMockMvc
            .perform(get(ENTITY_API_URL_ID, stockMovement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockMovement.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.discriminator").value(DEFAULT_DISCRIMINATOR));
    }

    @Test
    @Transactional
    void getStockMovementsByIdFiltering() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        Long id = stockMovement.getId();

        defaultStockMovementShouldBeFound("id.equals=" + id);
        defaultStockMovementShouldNotBeFound("id.notEquals=" + id);

        defaultStockMovementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStockMovementShouldNotBeFound("id.greaterThan=" + id);

        defaultStockMovementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStockMovementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStockMovementsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where createdat equals to DEFAULT_CREATEDAT
        defaultStockMovementShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the stockMovementList where createdat equals to UPDATED_CREATEDAT
        defaultStockMovementShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllStockMovementsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where createdat not equals to DEFAULT_CREATEDAT
        defaultStockMovementShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the stockMovementList where createdat not equals to UPDATED_CREATEDAT
        defaultStockMovementShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllStockMovementsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultStockMovementShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the stockMovementList where createdat equals to UPDATED_CREATEDAT
        defaultStockMovementShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllStockMovementsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where createdat is not null
        defaultStockMovementShouldBeFound("createdat.specified=true");

        // Get all the stockMovementList where createdat is null
        defaultStockMovementShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllStockMovementsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where updatedat equals to DEFAULT_UPDATEDAT
        defaultStockMovementShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the stockMovementList where updatedat equals to UPDATED_UPDATEDAT
        defaultStockMovementShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllStockMovementsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultStockMovementShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the stockMovementList where updatedat not equals to UPDATED_UPDATEDAT
        defaultStockMovementShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllStockMovementsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultStockMovementShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the stockMovementList where updatedat equals to UPDATED_UPDATEDAT
        defaultStockMovementShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllStockMovementsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where updatedat is not null
        defaultStockMovementShouldBeFound("updatedat.specified=true");

        // Get all the stockMovementList where updatedat is null
        defaultStockMovementShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllStockMovementsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where type equals to DEFAULT_TYPE
        defaultStockMovementShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the stockMovementList where type equals to UPDATED_TYPE
        defaultStockMovementShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllStockMovementsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where type not equals to DEFAULT_TYPE
        defaultStockMovementShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the stockMovementList where type not equals to UPDATED_TYPE
        defaultStockMovementShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllStockMovementsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultStockMovementShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the stockMovementList where type equals to UPDATED_TYPE
        defaultStockMovementShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllStockMovementsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where type is not null
        defaultStockMovementShouldBeFound("type.specified=true");

        // Get all the stockMovementList where type is null
        defaultStockMovementShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllStockMovementsByTypeContainsSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where type contains DEFAULT_TYPE
        defaultStockMovementShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the stockMovementList where type contains UPDATED_TYPE
        defaultStockMovementShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllStockMovementsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where type does not contain DEFAULT_TYPE
        defaultStockMovementShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the stockMovementList where type does not contain UPDATED_TYPE
        defaultStockMovementShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllStockMovementsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where quantity equals to DEFAULT_QUANTITY
        defaultStockMovementShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the stockMovementList where quantity equals to UPDATED_QUANTITY
        defaultStockMovementShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllStockMovementsByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where quantity not equals to DEFAULT_QUANTITY
        defaultStockMovementShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the stockMovementList where quantity not equals to UPDATED_QUANTITY
        defaultStockMovementShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllStockMovementsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultStockMovementShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the stockMovementList where quantity equals to UPDATED_QUANTITY
        defaultStockMovementShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllStockMovementsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where quantity is not null
        defaultStockMovementShouldBeFound("quantity.specified=true");

        // Get all the stockMovementList where quantity is null
        defaultStockMovementShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllStockMovementsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultStockMovementShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the stockMovementList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultStockMovementShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllStockMovementsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultStockMovementShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the stockMovementList where quantity is less than or equal to SMALLER_QUANTITY
        defaultStockMovementShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllStockMovementsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where quantity is less than DEFAULT_QUANTITY
        defaultStockMovementShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the stockMovementList where quantity is less than UPDATED_QUANTITY
        defaultStockMovementShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllStockMovementsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where quantity is greater than DEFAULT_QUANTITY
        defaultStockMovementShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the stockMovementList where quantity is greater than SMALLER_QUANTITY
        defaultStockMovementShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllStockMovementsByDiscriminatorIsEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where discriminator equals to DEFAULT_DISCRIMINATOR
        defaultStockMovementShouldBeFound("discriminator.equals=" + DEFAULT_DISCRIMINATOR);

        // Get all the stockMovementList where discriminator equals to UPDATED_DISCRIMINATOR
        defaultStockMovementShouldNotBeFound("discriminator.equals=" + UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void getAllStockMovementsByDiscriminatorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where discriminator not equals to DEFAULT_DISCRIMINATOR
        defaultStockMovementShouldNotBeFound("discriminator.notEquals=" + DEFAULT_DISCRIMINATOR);

        // Get all the stockMovementList where discriminator not equals to UPDATED_DISCRIMINATOR
        defaultStockMovementShouldBeFound("discriminator.notEquals=" + UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void getAllStockMovementsByDiscriminatorIsInShouldWork() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where discriminator in DEFAULT_DISCRIMINATOR or UPDATED_DISCRIMINATOR
        defaultStockMovementShouldBeFound("discriminator.in=" + DEFAULT_DISCRIMINATOR + "," + UPDATED_DISCRIMINATOR);

        // Get all the stockMovementList where discriminator equals to UPDATED_DISCRIMINATOR
        defaultStockMovementShouldNotBeFound("discriminator.in=" + UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void getAllStockMovementsByDiscriminatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where discriminator is not null
        defaultStockMovementShouldBeFound("discriminator.specified=true");

        // Get all the stockMovementList where discriminator is null
        defaultStockMovementShouldNotBeFound("discriminator.specified=false");
    }

    @Test
    @Transactional
    void getAllStockMovementsByDiscriminatorContainsSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where discriminator contains DEFAULT_DISCRIMINATOR
        defaultStockMovementShouldBeFound("discriminator.contains=" + DEFAULT_DISCRIMINATOR);

        // Get all the stockMovementList where discriminator contains UPDATED_DISCRIMINATOR
        defaultStockMovementShouldNotBeFound("discriminator.contains=" + UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void getAllStockMovementsByDiscriminatorNotContainsSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        // Get all the stockMovementList where discriminator does not contain DEFAULT_DISCRIMINATOR
        defaultStockMovementShouldNotBeFound("discriminator.doesNotContain=" + DEFAULT_DISCRIMINATOR);

        // Get all the stockMovementList where discriminator does not contain UPDATED_DISCRIMINATOR
        defaultStockMovementShouldBeFound("discriminator.doesNotContain=" + UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void getAllStockMovementsByProductvariantIsEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);
        ProductVariant productvariant = ProductVariantResourceIT.createEntity(em);
        em.persist(productvariant);
        em.flush();
        stockMovement.setProductvariant(productvariant);
        stockMovementRepository.saveAndFlush(stockMovement);
        Long productvariantId = productvariant.getId();

        // Get all the stockMovementList where productvariant equals to productvariantId
        defaultStockMovementShouldBeFound("productvariantId.equals=" + productvariantId);

        // Get all the stockMovementList where productvariant equals to (productvariantId + 1)
        defaultStockMovementShouldNotBeFound("productvariantId.equals=" + (productvariantId + 1));
    }

    @Test
    @Transactional
    void getAllStockMovementsByOrderitemIsEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);
        OrderItem orderitem = OrderItemResourceIT.createEntity(em);
        em.persist(orderitem);
        em.flush();
        stockMovement.setOrderitem(orderitem);
        stockMovementRepository.saveAndFlush(stockMovement);
        Long orderitemId = orderitem.getId();

        // Get all the stockMovementList where orderitem equals to orderitemId
        defaultStockMovementShouldBeFound("orderitemId.equals=" + orderitemId);

        // Get all the stockMovementList where orderitem equals to (orderitemId + 1)
        defaultStockMovementShouldNotBeFound("orderitemId.equals=" + (orderitemId + 1));
    }

    @Test
    @Transactional
    void getAllStockMovementsByOrderlineIsEqualToSomething() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);
        OrderLine orderline = OrderLineResourceIT.createEntity(em);
        em.persist(orderline);
        em.flush();
        stockMovement.setOrderline(orderline);
        stockMovementRepository.saveAndFlush(stockMovement);
        Long orderlineId = orderline.getId();

        // Get all the stockMovementList where orderline equals to orderlineId
        defaultStockMovementShouldBeFound("orderlineId.equals=" + orderlineId);

        // Get all the stockMovementList where orderline equals to (orderlineId + 1)
        defaultStockMovementShouldNotBeFound("orderlineId.equals=" + (orderlineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStockMovementShouldBeFound(String filter) throws Exception {
        restStockMovementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockMovement.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].discriminator").value(hasItem(DEFAULT_DISCRIMINATOR)));

        // Check, that the count call also returns 1
        restStockMovementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStockMovementShouldNotBeFound(String filter) throws Exception {
        restStockMovementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockMovementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStockMovement() throws Exception {
        // Get the stockMovement
        restStockMovementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStockMovement() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        int databaseSizeBeforeUpdate = stockMovementRepository.findAll().size();

        // Update the stockMovement
        StockMovement updatedStockMovement = stockMovementRepository.findById(stockMovement.getId()).get();
        // Disconnect from session so that the updates on updatedStockMovement are not directly saved in db
        em.detach(updatedStockMovement);
        updatedStockMovement
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .type(UPDATED_TYPE)
            .quantity(UPDATED_QUANTITY)
            .discriminator(UPDATED_DISCRIMINATOR);
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(updatedStockMovement);

        restStockMovementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockMovementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isOk());

        // Validate the StockMovement in the database
        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeUpdate);
        StockMovement testStockMovement = stockMovementList.get(stockMovementList.size() - 1);
        assertThat(testStockMovement.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testStockMovement.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testStockMovement.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testStockMovement.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockMovement.getDiscriminator()).isEqualTo(UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void putNonExistingStockMovement() throws Exception {
        int databaseSizeBeforeUpdate = stockMovementRepository.findAll().size();
        stockMovement.setId(count.incrementAndGet());

        // Create the StockMovement
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMovementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockMovementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockMovement in the database
        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockMovement() throws Exception {
        int databaseSizeBeforeUpdate = stockMovementRepository.findAll().size();
        stockMovement.setId(count.incrementAndGet());

        // Create the StockMovement
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockMovementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockMovement in the database
        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockMovement() throws Exception {
        int databaseSizeBeforeUpdate = stockMovementRepository.findAll().size();
        stockMovement.setId(count.incrementAndGet());

        // Create the StockMovement
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockMovementMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockMovement in the database
        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockMovementWithPatch() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        int databaseSizeBeforeUpdate = stockMovementRepository.findAll().size();

        // Update the stockMovement using partial update
        StockMovement partialUpdatedStockMovement = new StockMovement();
        partialUpdatedStockMovement.setId(stockMovement.getId());

        partialUpdatedStockMovement.updatedat(UPDATED_UPDATEDAT).type(UPDATED_TYPE);

        restStockMovementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockMovement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockMovement))
            )
            .andExpect(status().isOk());

        // Validate the StockMovement in the database
        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeUpdate);
        StockMovement testStockMovement = stockMovementList.get(stockMovementList.size() - 1);
        assertThat(testStockMovement.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testStockMovement.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testStockMovement.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testStockMovement.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testStockMovement.getDiscriminator()).isEqualTo(DEFAULT_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void fullUpdateStockMovementWithPatch() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        int databaseSizeBeforeUpdate = stockMovementRepository.findAll().size();

        // Update the stockMovement using partial update
        StockMovement partialUpdatedStockMovement = new StockMovement();
        partialUpdatedStockMovement.setId(stockMovement.getId());

        partialUpdatedStockMovement
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .type(UPDATED_TYPE)
            .quantity(UPDATED_QUANTITY)
            .discriminator(UPDATED_DISCRIMINATOR);

        restStockMovementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockMovement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockMovement))
            )
            .andExpect(status().isOk());

        // Validate the StockMovement in the database
        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeUpdate);
        StockMovement testStockMovement = stockMovementList.get(stockMovementList.size() - 1);
        assertThat(testStockMovement.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testStockMovement.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testStockMovement.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testStockMovement.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockMovement.getDiscriminator()).isEqualTo(UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void patchNonExistingStockMovement() throws Exception {
        int databaseSizeBeforeUpdate = stockMovementRepository.findAll().size();
        stockMovement.setId(count.incrementAndGet());

        // Create the StockMovement
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMovementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockMovementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockMovement in the database
        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockMovement() throws Exception {
        int databaseSizeBeforeUpdate = stockMovementRepository.findAll().size();
        stockMovement.setId(count.incrementAndGet());

        // Create the StockMovement
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockMovementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockMovement in the database
        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockMovement() throws Exception {
        int databaseSizeBeforeUpdate = stockMovementRepository.findAll().size();
        stockMovement.setId(count.incrementAndGet());

        // Create the StockMovement
        StockMovementDTO stockMovementDTO = stockMovementMapper.toDto(stockMovement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockMovementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockMovementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockMovement in the database
        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockMovement() throws Exception {
        // Initialize the database
        stockMovementRepository.saveAndFlush(stockMovement);

        int databaseSizeBeforeDelete = stockMovementRepository.findAll().size();

        // Delete the stockMovement
        restStockMovementMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockMovement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StockMovement> stockMovementList = stockMovementRepository.findAll();
        assertThat(stockMovementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
