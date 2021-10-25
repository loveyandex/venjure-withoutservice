package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Asset;
import com.venjure.domain.Channel;
import com.venjure.domain.Collection;
import com.venjure.domain.FacetValue;
import com.venjure.domain.Product;
import com.venjure.domain.ProductOption;
import com.venjure.domain.ProductVariant;
import com.venjure.domain.ProductVariantAsset;
import com.venjure.domain.ProductVariantPrice;
import com.venjure.domain.ProductVariantTranslation;
import com.venjure.domain.StockMovement;
import com.venjure.domain.TaxCategory;
import com.venjure.repository.ProductVariantRepository;
import com.venjure.service.ProductVariantService;
import com.venjure.service.criteria.ProductVariantCriteria;
import com.venjure.service.dto.ProductVariantDTO;
import com.venjure.service.mapper.ProductVariantMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductVariantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductVariantResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final Integer DEFAULT_STOCKONHAND = 1;
    private static final Integer UPDATED_STOCKONHAND = 2;
    private static final Integer SMALLER_STOCKONHAND = 1 - 1;

    private static final Integer DEFAULT_STOCKALLOCATED = 1;
    private static final Integer UPDATED_STOCKALLOCATED = 2;
    private static final Integer SMALLER_STOCKALLOCATED = 1 - 1;

    private static final Integer DEFAULT_OUTOFSTOCKTHRESHOLD = 1;
    private static final Integer UPDATED_OUTOFSTOCKTHRESHOLD = 2;
    private static final Integer SMALLER_OUTOFSTOCKTHRESHOLD = 1 - 1;

    private static final Boolean DEFAULT_USEGLOBALOUTOFSTOCKTHRESHOLD = false;
    private static final Boolean UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD = true;

    private static final String DEFAULT_TRACKINVENTORY = "AAAAAAAAAA";
    private static final String UPDATED_TRACKINVENTORY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-variants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Mock
    private ProductVariantRepository productVariantRepositoryMock;

    @Autowired
    private ProductVariantMapper productVariantMapper;

    @Mock
    private ProductVariantService productVariantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductVariantMockMvc;

    private ProductVariant productVariant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVariant createEntity(EntityManager em) {
        ProductVariant productVariant = new ProductVariant()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .deletedat(DEFAULT_DELETEDAT)
            .enabled(DEFAULT_ENABLED)
            .sku(DEFAULT_SKU)
            .stockonhand(DEFAULT_STOCKONHAND)
            .stockallocated(DEFAULT_STOCKALLOCATED)
            .outofstockthreshold(DEFAULT_OUTOFSTOCKTHRESHOLD)
            .useglobaloutofstockthreshold(DEFAULT_USEGLOBALOUTOFSTOCKTHRESHOLD)
            .trackinventory(DEFAULT_TRACKINVENTORY);
        return productVariant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVariant createUpdatedEntity(EntityManager em) {
        ProductVariant productVariant = new ProductVariant()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .enabled(UPDATED_ENABLED)
            .sku(UPDATED_SKU)
            .stockonhand(UPDATED_STOCKONHAND)
            .stockallocated(UPDATED_STOCKALLOCATED)
            .outofstockthreshold(UPDATED_OUTOFSTOCKTHRESHOLD)
            .useglobaloutofstockthreshold(UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD)
            .trackinventory(UPDATED_TRACKINVENTORY);
        return productVariant;
    }

    @BeforeEach
    public void initTest() {
        productVariant = createEntity(em);
    }

    @Test
    @Transactional
    void createProductVariant() throws Exception {
        int databaseSizeBeforeCreate = productVariantRepository.findAll().size();
        // Create the ProductVariant
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);
        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeCreate + 1);
        ProductVariant testProductVariant = productVariantList.get(productVariantList.size() - 1);
        assertThat(testProductVariant.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testProductVariant.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductVariant.getDeletedat()).isEqualTo(DEFAULT_DELETEDAT);
        assertThat(testProductVariant.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testProductVariant.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testProductVariant.getStockonhand()).isEqualTo(DEFAULT_STOCKONHAND);
        assertThat(testProductVariant.getStockallocated()).isEqualTo(DEFAULT_STOCKALLOCATED);
        assertThat(testProductVariant.getOutofstockthreshold()).isEqualTo(DEFAULT_OUTOFSTOCKTHRESHOLD);
        assertThat(testProductVariant.getUseglobaloutofstockthreshold()).isEqualTo(DEFAULT_USEGLOBALOUTOFSTOCKTHRESHOLD);
        assertThat(testProductVariant.getTrackinventory()).isEqualTo(DEFAULT_TRACKINVENTORY);
    }

    @Test
    @Transactional
    void createProductVariantWithExistingId() throws Exception {
        // Create the ProductVariant with an existing ID
        productVariant.setId(1L);
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        int databaseSizeBeforeCreate = productVariantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantRepository.findAll().size();
        // set the field null
        productVariant.setCreatedat(null);

        // Create the ProductVariant, which fails.
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantRepository.findAll().size();
        // set the field null
        productVariant.setUpdatedat(null);

        // Create the ProductVariant, which fails.
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantRepository.findAll().size();
        // set the field null
        productVariant.setEnabled(null);

        // Create the ProductVariant, which fails.
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSkuIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantRepository.findAll().size();
        // set the field null
        productVariant.setSku(null);

        // Create the ProductVariant, which fails.
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockonhandIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantRepository.findAll().size();
        // set the field null
        productVariant.setStockonhand(null);

        // Create the ProductVariant, which fails.
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockallocatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantRepository.findAll().size();
        // set the field null
        productVariant.setStockallocated(null);

        // Create the ProductVariant, which fails.
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOutofstockthresholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantRepository.findAll().size();
        // set the field null
        productVariant.setOutofstockthreshold(null);

        // Create the ProductVariant, which fails.
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUseglobaloutofstockthresholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantRepository.findAll().size();
        // set the field null
        productVariant.setUseglobaloutofstockthreshold(null);

        // Create the ProductVariant, which fails.
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTrackinventoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantRepository.findAll().size();
        // set the field null
        productVariant.setTrackinventory(null);

        // Create the ProductVariant, which fails.
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        restProductVariantMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductVariants() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList
        restProductVariantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVariant.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].stockonhand").value(hasItem(DEFAULT_STOCKONHAND)))
            .andExpect(jsonPath("$.[*].stockallocated").value(hasItem(DEFAULT_STOCKALLOCATED)))
            .andExpect(jsonPath("$.[*].outofstockthreshold").value(hasItem(DEFAULT_OUTOFSTOCKTHRESHOLD)))
            .andExpect(jsonPath("$.[*].useglobaloutofstockthreshold").value(hasItem(DEFAULT_USEGLOBALOUTOFSTOCKTHRESHOLD.booleanValue())))
            .andExpect(jsonPath("$.[*].trackinventory").value(hasItem(DEFAULT_TRACKINVENTORY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductVariantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productVariantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductVariantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productVariantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductVariantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productVariantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductVariantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productVariantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProductVariant() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get the productVariant
        restProductVariantMockMvc
            .perform(get(ENTITY_API_URL_ID, productVariant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productVariant.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.deletedat").value(DEFAULT_DELETEDAT.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.stockonhand").value(DEFAULT_STOCKONHAND))
            .andExpect(jsonPath("$.stockallocated").value(DEFAULT_STOCKALLOCATED))
            .andExpect(jsonPath("$.outofstockthreshold").value(DEFAULT_OUTOFSTOCKTHRESHOLD))
            .andExpect(jsonPath("$.useglobaloutofstockthreshold").value(DEFAULT_USEGLOBALOUTOFSTOCKTHRESHOLD.booleanValue()))
            .andExpect(jsonPath("$.trackinventory").value(DEFAULT_TRACKINVENTORY));
    }

    @Test
    @Transactional
    void getProductVariantsByIdFiltering() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        Long id = productVariant.getId();

        defaultProductVariantShouldBeFound("id.equals=" + id);
        defaultProductVariantShouldNotBeFound("id.notEquals=" + id);

        defaultProductVariantShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductVariantShouldNotBeFound("id.greaterThan=" + id);

        defaultProductVariantShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductVariantShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductVariantsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where createdat equals to DEFAULT_CREATEDAT
        defaultProductVariantShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the productVariantList where createdat equals to UPDATED_CREATEDAT
        defaultProductVariantShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where createdat not equals to DEFAULT_CREATEDAT
        defaultProductVariantShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the productVariantList where createdat not equals to UPDATED_CREATEDAT
        defaultProductVariantShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultProductVariantShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the productVariantList where createdat equals to UPDATED_CREATEDAT
        defaultProductVariantShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where createdat is not null
        defaultProductVariantShouldBeFound("createdat.specified=true");

        // Get all the productVariantList where createdat is null
        defaultProductVariantShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where updatedat equals to DEFAULT_UPDATEDAT
        defaultProductVariantShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the productVariantList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductVariantShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultProductVariantShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the productVariantList where updatedat not equals to UPDATED_UPDATEDAT
        defaultProductVariantShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultProductVariantShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the productVariantList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductVariantShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where updatedat is not null
        defaultProductVariantShouldBeFound("updatedat.specified=true");

        // Get all the productVariantList where updatedat is null
        defaultProductVariantShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantsByDeletedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where deletedat equals to DEFAULT_DELETEDAT
        defaultProductVariantShouldBeFound("deletedat.equals=" + DEFAULT_DELETEDAT);

        // Get all the productVariantList where deletedat equals to UPDATED_DELETEDAT
        defaultProductVariantShouldNotBeFound("deletedat.equals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantsByDeletedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where deletedat not equals to DEFAULT_DELETEDAT
        defaultProductVariantShouldNotBeFound("deletedat.notEquals=" + DEFAULT_DELETEDAT);

        // Get all the productVariantList where deletedat not equals to UPDATED_DELETEDAT
        defaultProductVariantShouldBeFound("deletedat.notEquals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantsByDeletedatIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where deletedat in DEFAULT_DELETEDAT or UPDATED_DELETEDAT
        defaultProductVariantShouldBeFound("deletedat.in=" + DEFAULT_DELETEDAT + "," + UPDATED_DELETEDAT);

        // Get all the productVariantList where deletedat equals to UPDATED_DELETEDAT
        defaultProductVariantShouldNotBeFound("deletedat.in=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantsByDeletedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where deletedat is not null
        defaultProductVariantShouldBeFound("deletedat.specified=true");

        // Get all the productVariantList where deletedat is null
        defaultProductVariantShouldNotBeFound("deletedat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where enabled equals to DEFAULT_ENABLED
        defaultProductVariantShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the productVariantList where enabled equals to UPDATED_ENABLED
        defaultProductVariantShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllProductVariantsByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where enabled not equals to DEFAULT_ENABLED
        defaultProductVariantShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the productVariantList where enabled not equals to UPDATED_ENABLED
        defaultProductVariantShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllProductVariantsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultProductVariantShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the productVariantList where enabled equals to UPDATED_ENABLED
        defaultProductVariantShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllProductVariantsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where enabled is not null
        defaultProductVariantShouldBeFound("enabled.specified=true");

        // Get all the productVariantList where enabled is null
        defaultProductVariantShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantsBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where sku equals to DEFAULT_SKU
        defaultProductVariantShouldBeFound("sku.equals=" + DEFAULT_SKU);

        // Get all the productVariantList where sku equals to UPDATED_SKU
        defaultProductVariantShouldNotBeFound("sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductVariantsBySkuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where sku not equals to DEFAULT_SKU
        defaultProductVariantShouldNotBeFound("sku.notEquals=" + DEFAULT_SKU);

        // Get all the productVariantList where sku not equals to UPDATED_SKU
        defaultProductVariantShouldBeFound("sku.notEquals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductVariantsBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where sku in DEFAULT_SKU or UPDATED_SKU
        defaultProductVariantShouldBeFound("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU);

        // Get all the productVariantList where sku equals to UPDATED_SKU
        defaultProductVariantShouldNotBeFound("sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductVariantsBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where sku is not null
        defaultProductVariantShouldBeFound("sku.specified=true");

        // Get all the productVariantList where sku is null
        defaultProductVariantShouldNotBeFound("sku.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantsBySkuContainsSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where sku contains DEFAULT_SKU
        defaultProductVariantShouldBeFound("sku.contains=" + DEFAULT_SKU);

        // Get all the productVariantList where sku contains UPDATED_SKU
        defaultProductVariantShouldNotBeFound("sku.contains=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductVariantsBySkuNotContainsSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where sku does not contain DEFAULT_SKU
        defaultProductVariantShouldNotBeFound("sku.doesNotContain=" + DEFAULT_SKU);

        // Get all the productVariantList where sku does not contain UPDATED_SKU
        defaultProductVariantShouldBeFound("sku.doesNotContain=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockonhandIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockonhand equals to DEFAULT_STOCKONHAND
        defaultProductVariantShouldBeFound("stockonhand.equals=" + DEFAULT_STOCKONHAND);

        // Get all the productVariantList where stockonhand equals to UPDATED_STOCKONHAND
        defaultProductVariantShouldNotBeFound("stockonhand.equals=" + UPDATED_STOCKONHAND);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockonhandIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockonhand not equals to DEFAULT_STOCKONHAND
        defaultProductVariantShouldNotBeFound("stockonhand.notEquals=" + DEFAULT_STOCKONHAND);

        // Get all the productVariantList where stockonhand not equals to UPDATED_STOCKONHAND
        defaultProductVariantShouldBeFound("stockonhand.notEquals=" + UPDATED_STOCKONHAND);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockonhandIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockonhand in DEFAULT_STOCKONHAND or UPDATED_STOCKONHAND
        defaultProductVariantShouldBeFound("stockonhand.in=" + DEFAULT_STOCKONHAND + "," + UPDATED_STOCKONHAND);

        // Get all the productVariantList where stockonhand equals to UPDATED_STOCKONHAND
        defaultProductVariantShouldNotBeFound("stockonhand.in=" + UPDATED_STOCKONHAND);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockonhandIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockonhand is not null
        defaultProductVariantShouldBeFound("stockonhand.specified=true");

        // Get all the productVariantList where stockonhand is null
        defaultProductVariantShouldNotBeFound("stockonhand.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockonhandIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockonhand is greater than or equal to DEFAULT_STOCKONHAND
        defaultProductVariantShouldBeFound("stockonhand.greaterThanOrEqual=" + DEFAULT_STOCKONHAND);

        // Get all the productVariantList where stockonhand is greater than or equal to UPDATED_STOCKONHAND
        defaultProductVariantShouldNotBeFound("stockonhand.greaterThanOrEqual=" + UPDATED_STOCKONHAND);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockonhandIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockonhand is less than or equal to DEFAULT_STOCKONHAND
        defaultProductVariantShouldBeFound("stockonhand.lessThanOrEqual=" + DEFAULT_STOCKONHAND);

        // Get all the productVariantList where stockonhand is less than or equal to SMALLER_STOCKONHAND
        defaultProductVariantShouldNotBeFound("stockonhand.lessThanOrEqual=" + SMALLER_STOCKONHAND);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockonhandIsLessThanSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockonhand is less than DEFAULT_STOCKONHAND
        defaultProductVariantShouldNotBeFound("stockonhand.lessThan=" + DEFAULT_STOCKONHAND);

        // Get all the productVariantList where stockonhand is less than UPDATED_STOCKONHAND
        defaultProductVariantShouldBeFound("stockonhand.lessThan=" + UPDATED_STOCKONHAND);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockonhandIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockonhand is greater than DEFAULT_STOCKONHAND
        defaultProductVariantShouldNotBeFound("stockonhand.greaterThan=" + DEFAULT_STOCKONHAND);

        // Get all the productVariantList where stockonhand is greater than SMALLER_STOCKONHAND
        defaultProductVariantShouldBeFound("stockonhand.greaterThan=" + SMALLER_STOCKONHAND);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockallocatedIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockallocated equals to DEFAULT_STOCKALLOCATED
        defaultProductVariantShouldBeFound("stockallocated.equals=" + DEFAULT_STOCKALLOCATED);

        // Get all the productVariantList where stockallocated equals to UPDATED_STOCKALLOCATED
        defaultProductVariantShouldNotBeFound("stockallocated.equals=" + UPDATED_STOCKALLOCATED);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockallocatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockallocated not equals to DEFAULT_STOCKALLOCATED
        defaultProductVariantShouldNotBeFound("stockallocated.notEquals=" + DEFAULT_STOCKALLOCATED);

        // Get all the productVariantList where stockallocated not equals to UPDATED_STOCKALLOCATED
        defaultProductVariantShouldBeFound("stockallocated.notEquals=" + UPDATED_STOCKALLOCATED);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockallocatedIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockallocated in DEFAULT_STOCKALLOCATED or UPDATED_STOCKALLOCATED
        defaultProductVariantShouldBeFound("stockallocated.in=" + DEFAULT_STOCKALLOCATED + "," + UPDATED_STOCKALLOCATED);

        // Get all the productVariantList where stockallocated equals to UPDATED_STOCKALLOCATED
        defaultProductVariantShouldNotBeFound("stockallocated.in=" + UPDATED_STOCKALLOCATED);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockallocatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockallocated is not null
        defaultProductVariantShouldBeFound("stockallocated.specified=true");

        // Get all the productVariantList where stockallocated is null
        defaultProductVariantShouldNotBeFound("stockallocated.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockallocatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockallocated is greater than or equal to DEFAULT_STOCKALLOCATED
        defaultProductVariantShouldBeFound("stockallocated.greaterThanOrEqual=" + DEFAULT_STOCKALLOCATED);

        // Get all the productVariantList where stockallocated is greater than or equal to UPDATED_STOCKALLOCATED
        defaultProductVariantShouldNotBeFound("stockallocated.greaterThanOrEqual=" + UPDATED_STOCKALLOCATED);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockallocatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockallocated is less than or equal to DEFAULT_STOCKALLOCATED
        defaultProductVariantShouldBeFound("stockallocated.lessThanOrEqual=" + DEFAULT_STOCKALLOCATED);

        // Get all the productVariantList where stockallocated is less than or equal to SMALLER_STOCKALLOCATED
        defaultProductVariantShouldNotBeFound("stockallocated.lessThanOrEqual=" + SMALLER_STOCKALLOCATED);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockallocatedIsLessThanSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockallocated is less than DEFAULT_STOCKALLOCATED
        defaultProductVariantShouldNotBeFound("stockallocated.lessThan=" + DEFAULT_STOCKALLOCATED);

        // Get all the productVariantList where stockallocated is less than UPDATED_STOCKALLOCATED
        defaultProductVariantShouldBeFound("stockallocated.lessThan=" + UPDATED_STOCKALLOCATED);
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockallocatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where stockallocated is greater than DEFAULT_STOCKALLOCATED
        defaultProductVariantShouldNotBeFound("stockallocated.greaterThan=" + DEFAULT_STOCKALLOCATED);

        // Get all the productVariantList where stockallocated is greater than SMALLER_STOCKALLOCATED
        defaultProductVariantShouldBeFound("stockallocated.greaterThan=" + SMALLER_STOCKALLOCATED);
    }

    @Test
    @Transactional
    void getAllProductVariantsByOutofstockthresholdIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where outofstockthreshold equals to DEFAULT_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldBeFound("outofstockthreshold.equals=" + DEFAULT_OUTOFSTOCKTHRESHOLD);

        // Get all the productVariantList where outofstockthreshold equals to UPDATED_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldNotBeFound("outofstockthreshold.equals=" + UPDATED_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllProductVariantsByOutofstockthresholdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where outofstockthreshold not equals to DEFAULT_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldNotBeFound("outofstockthreshold.notEquals=" + DEFAULT_OUTOFSTOCKTHRESHOLD);

        // Get all the productVariantList where outofstockthreshold not equals to UPDATED_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldBeFound("outofstockthreshold.notEquals=" + UPDATED_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllProductVariantsByOutofstockthresholdIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where outofstockthreshold in DEFAULT_OUTOFSTOCKTHRESHOLD or UPDATED_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldBeFound("outofstockthreshold.in=" + DEFAULT_OUTOFSTOCKTHRESHOLD + "," + UPDATED_OUTOFSTOCKTHRESHOLD);

        // Get all the productVariantList where outofstockthreshold equals to UPDATED_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldNotBeFound("outofstockthreshold.in=" + UPDATED_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllProductVariantsByOutofstockthresholdIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where outofstockthreshold is not null
        defaultProductVariantShouldBeFound("outofstockthreshold.specified=true");

        // Get all the productVariantList where outofstockthreshold is null
        defaultProductVariantShouldNotBeFound("outofstockthreshold.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantsByOutofstockthresholdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where outofstockthreshold is greater than or equal to DEFAULT_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldBeFound("outofstockthreshold.greaterThanOrEqual=" + DEFAULT_OUTOFSTOCKTHRESHOLD);

        // Get all the productVariantList where outofstockthreshold is greater than or equal to UPDATED_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldNotBeFound("outofstockthreshold.greaterThanOrEqual=" + UPDATED_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllProductVariantsByOutofstockthresholdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where outofstockthreshold is less than or equal to DEFAULT_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldBeFound("outofstockthreshold.lessThanOrEqual=" + DEFAULT_OUTOFSTOCKTHRESHOLD);

        // Get all the productVariantList where outofstockthreshold is less than or equal to SMALLER_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldNotBeFound("outofstockthreshold.lessThanOrEqual=" + SMALLER_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllProductVariantsByOutofstockthresholdIsLessThanSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where outofstockthreshold is less than DEFAULT_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldNotBeFound("outofstockthreshold.lessThan=" + DEFAULT_OUTOFSTOCKTHRESHOLD);

        // Get all the productVariantList where outofstockthreshold is less than UPDATED_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldBeFound("outofstockthreshold.lessThan=" + UPDATED_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllProductVariantsByOutofstockthresholdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where outofstockthreshold is greater than DEFAULT_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldNotBeFound("outofstockthreshold.greaterThan=" + DEFAULT_OUTOFSTOCKTHRESHOLD);

        // Get all the productVariantList where outofstockthreshold is greater than SMALLER_OUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldBeFound("outofstockthreshold.greaterThan=" + SMALLER_OUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllProductVariantsByUseglobaloutofstockthresholdIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where useglobaloutofstockthreshold equals to DEFAULT_USEGLOBALOUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldBeFound("useglobaloutofstockthreshold.equals=" + DEFAULT_USEGLOBALOUTOFSTOCKTHRESHOLD);

        // Get all the productVariantList where useglobaloutofstockthreshold equals to UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldNotBeFound("useglobaloutofstockthreshold.equals=" + UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllProductVariantsByUseglobaloutofstockthresholdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where useglobaloutofstockthreshold not equals to DEFAULT_USEGLOBALOUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldNotBeFound("useglobaloutofstockthreshold.notEquals=" + DEFAULT_USEGLOBALOUTOFSTOCKTHRESHOLD);

        // Get all the productVariantList where useglobaloutofstockthreshold not equals to UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldBeFound("useglobaloutofstockthreshold.notEquals=" + UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllProductVariantsByUseglobaloutofstockthresholdIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where useglobaloutofstockthreshold in DEFAULT_USEGLOBALOUTOFSTOCKTHRESHOLD or UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldBeFound(
            "useglobaloutofstockthreshold.in=" + DEFAULT_USEGLOBALOUTOFSTOCKTHRESHOLD + "," + UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD
        );

        // Get all the productVariantList where useglobaloutofstockthreshold equals to UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD
        defaultProductVariantShouldNotBeFound("useglobaloutofstockthreshold.in=" + UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD);
    }

    @Test
    @Transactional
    void getAllProductVariantsByUseglobaloutofstockthresholdIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where useglobaloutofstockthreshold is not null
        defaultProductVariantShouldBeFound("useglobaloutofstockthreshold.specified=true");

        // Get all the productVariantList where useglobaloutofstockthreshold is null
        defaultProductVariantShouldNotBeFound("useglobaloutofstockthreshold.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantsByTrackinventoryIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where trackinventory equals to DEFAULT_TRACKINVENTORY
        defaultProductVariantShouldBeFound("trackinventory.equals=" + DEFAULT_TRACKINVENTORY);

        // Get all the productVariantList where trackinventory equals to UPDATED_TRACKINVENTORY
        defaultProductVariantShouldNotBeFound("trackinventory.equals=" + UPDATED_TRACKINVENTORY);
    }

    @Test
    @Transactional
    void getAllProductVariantsByTrackinventoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where trackinventory not equals to DEFAULT_TRACKINVENTORY
        defaultProductVariantShouldNotBeFound("trackinventory.notEquals=" + DEFAULT_TRACKINVENTORY);

        // Get all the productVariantList where trackinventory not equals to UPDATED_TRACKINVENTORY
        defaultProductVariantShouldBeFound("trackinventory.notEquals=" + UPDATED_TRACKINVENTORY);
    }

    @Test
    @Transactional
    void getAllProductVariantsByTrackinventoryIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where trackinventory in DEFAULT_TRACKINVENTORY or UPDATED_TRACKINVENTORY
        defaultProductVariantShouldBeFound("trackinventory.in=" + DEFAULT_TRACKINVENTORY + "," + UPDATED_TRACKINVENTORY);

        // Get all the productVariantList where trackinventory equals to UPDATED_TRACKINVENTORY
        defaultProductVariantShouldNotBeFound("trackinventory.in=" + UPDATED_TRACKINVENTORY);
    }

    @Test
    @Transactional
    void getAllProductVariantsByTrackinventoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where trackinventory is not null
        defaultProductVariantShouldBeFound("trackinventory.specified=true");

        // Get all the productVariantList where trackinventory is null
        defaultProductVariantShouldNotBeFound("trackinventory.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantsByTrackinventoryContainsSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where trackinventory contains DEFAULT_TRACKINVENTORY
        defaultProductVariantShouldBeFound("trackinventory.contains=" + DEFAULT_TRACKINVENTORY);

        // Get all the productVariantList where trackinventory contains UPDATED_TRACKINVENTORY
        defaultProductVariantShouldNotBeFound("trackinventory.contains=" + UPDATED_TRACKINVENTORY);
    }

    @Test
    @Transactional
    void getAllProductVariantsByTrackinventoryNotContainsSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        // Get all the productVariantList where trackinventory does not contain DEFAULT_TRACKINVENTORY
        defaultProductVariantShouldNotBeFound("trackinventory.doesNotContain=" + DEFAULT_TRACKINVENTORY);

        // Get all the productVariantList where trackinventory does not contain UPDATED_TRACKINVENTORY
        defaultProductVariantShouldBeFound("trackinventory.doesNotContain=" + UPDATED_TRACKINVENTORY);
    }

    @Test
    @Transactional
    void getAllProductVariantsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);
        Product product = ProductResourceIT.createEntity(em);
        em.persist(product);
        em.flush();
        productVariant.setProduct(product);
        productVariantRepository.saveAndFlush(productVariant);
        Long productId = product.getId();

        // Get all the productVariantList where product equals to productId
        defaultProductVariantShouldBeFound("productId.equals=" + productId);

        // Get all the productVariantList where product equals to (productId + 1)
        defaultProductVariantShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductVariantsByFeaturedassetIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);
        Asset featuredasset = AssetResourceIT.createEntity(em);
        em.persist(featuredasset);
        em.flush();
        productVariant.setFeaturedasset(featuredasset);
        productVariantRepository.saveAndFlush(productVariant);
        Long featuredassetId = featuredasset.getId();

        // Get all the productVariantList where featuredasset equals to featuredassetId
        defaultProductVariantShouldBeFound("featuredassetId.equals=" + featuredassetId);

        // Get all the productVariantList where featuredasset equals to (featuredassetId + 1)
        defaultProductVariantShouldNotBeFound("featuredassetId.equals=" + (featuredassetId + 1));
    }

    @Test
    @Transactional
    void getAllProductVariantsByTaxcategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);
        TaxCategory taxcategory = TaxCategoryResourceIT.createEntity(em);
        em.persist(taxcategory);
        em.flush();
        productVariant.setTaxcategory(taxcategory);
        productVariantRepository.saveAndFlush(productVariant);
        Long taxcategoryId = taxcategory.getId();

        // Get all the productVariantList where taxcategory equals to taxcategoryId
        defaultProductVariantShouldBeFound("taxcategoryId.equals=" + taxcategoryId);

        // Get all the productVariantList where taxcategory equals to (taxcategoryId + 1)
        defaultProductVariantShouldNotBeFound("taxcategoryId.equals=" + (taxcategoryId + 1));
    }

    @Test
    @Transactional
    void getAllProductVariantsByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);
        Channel channel = ChannelResourceIT.createEntity(em);
        em.persist(channel);
        em.flush();
        productVariant.addChannel(channel);
        productVariantRepository.saveAndFlush(productVariant);
        Long channelId = channel.getId();

        // Get all the productVariantList where channel equals to channelId
        defaultProductVariantShouldBeFound("channelId.equals=" + channelId);

        // Get all the productVariantList where channel equals to (channelId + 1)
        defaultProductVariantShouldNotBeFound("channelId.equals=" + (channelId + 1));
    }

    @Test
    @Transactional
    void getAllProductVariantsByProductVariantsIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);
        Collection productVariants = CollectionResourceIT.createEntity(em);
        em.persist(productVariants);
        em.flush();
        productVariant.addProductVariants(productVariants);
        productVariantRepository.saveAndFlush(productVariant);
        Long productVariantsId = productVariants.getId();

        // Get all the productVariantList where productVariants equals to productVariantsId
        defaultProductVariantShouldBeFound("productVariantsId.equals=" + productVariantsId);

        // Get all the productVariantList where productVariants equals to (productVariantsId + 1)
        defaultProductVariantShouldNotBeFound("productVariantsId.equals=" + (productVariantsId + 1));
    }

    @Test
    @Transactional
    void getAllProductVariantsByFacetValueIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);
        FacetValue facetValue = FacetValueResourceIT.createEntity(em);
        em.persist(facetValue);
        em.flush();
        productVariant.addFacetValue(facetValue);
        productVariantRepository.saveAndFlush(productVariant);
        Long facetValueId = facetValue.getId();

        // Get all the productVariantList where facetValue equals to facetValueId
        defaultProductVariantShouldBeFound("facetValueId.equals=" + facetValueId);

        // Get all the productVariantList where facetValue equals to (facetValueId + 1)
        defaultProductVariantShouldNotBeFound("facetValueId.equals=" + (facetValueId + 1));
    }

    @Test
    @Transactional
    void getAllProductVariantsByProductOptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);
        ProductOption productOption = ProductOptionResourceIT.createEntity(em);
        em.persist(productOption);
        em.flush();
        productVariant.addProductOption(productOption);
        productVariantRepository.saveAndFlush(productVariant);
        Long productOptionId = productOption.getId();

        // Get all the productVariantList where productOption equals to productOptionId
        defaultProductVariantShouldBeFound("productOptionId.equals=" + productOptionId);

        // Get all the productVariantList where productOption equals to (productOptionId + 1)
        defaultProductVariantShouldNotBeFound("productOptionId.equals=" + (productOptionId + 1));
    }

    @Test
    @Transactional
    void getAllProductVariantsByProductVariantAssetIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);
        ProductVariantAsset productVariantAsset = ProductVariantAssetResourceIT.createEntity(em);
        em.persist(productVariantAsset);
        em.flush();
        productVariant.addProductVariantAsset(productVariantAsset);
        productVariantRepository.saveAndFlush(productVariant);
        Long productVariantAssetId = productVariantAsset.getId();

        // Get all the productVariantList where productVariantAsset equals to productVariantAssetId
        defaultProductVariantShouldBeFound("productVariantAssetId.equals=" + productVariantAssetId);

        // Get all the productVariantList where productVariantAsset equals to (productVariantAssetId + 1)
        defaultProductVariantShouldNotBeFound("productVariantAssetId.equals=" + (productVariantAssetId + 1));
    }

    @Test
    @Transactional
    void getAllProductVariantsByProductVariantPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);
        ProductVariantPrice productVariantPrice = ProductVariantPriceResourceIT.createEntity(em);
        em.persist(productVariantPrice);
        em.flush();
        productVariant.addProductVariantPrice(productVariantPrice);
        productVariantRepository.saveAndFlush(productVariant);
        Long productVariantPriceId = productVariantPrice.getId();

        // Get all the productVariantList where productVariantPrice equals to productVariantPriceId
        defaultProductVariantShouldBeFound("productVariantPriceId.equals=" + productVariantPriceId);

        // Get all the productVariantList where productVariantPrice equals to (productVariantPriceId + 1)
        defaultProductVariantShouldNotBeFound("productVariantPriceId.equals=" + (productVariantPriceId + 1));
    }

    @Test
    @Transactional
    void getAllProductVariantsByProductVariantTranslationIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);
        ProductVariantTranslation productVariantTranslation = ProductVariantTranslationResourceIT.createEntity(em);
        em.persist(productVariantTranslation);
        em.flush();
        productVariant.addProductVariantTranslation(productVariantTranslation);
        productVariantRepository.saveAndFlush(productVariant);
        Long productVariantTranslationId = productVariantTranslation.getId();

        // Get all the productVariantList where productVariantTranslation equals to productVariantTranslationId
        defaultProductVariantShouldBeFound("productVariantTranslationId.equals=" + productVariantTranslationId);

        // Get all the productVariantList where productVariantTranslation equals to (productVariantTranslationId + 1)
        defaultProductVariantShouldNotBeFound("productVariantTranslationId.equals=" + (productVariantTranslationId + 1));
    }

    @Test
    @Transactional
    void getAllProductVariantsByStockMovementIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);
        StockMovement stockMovement = StockMovementResourceIT.createEntity(em);
        em.persist(stockMovement);
        em.flush();
        productVariant.addStockMovement(stockMovement);
        productVariantRepository.saveAndFlush(productVariant);
        Long stockMovementId = stockMovement.getId();

        // Get all the productVariantList where stockMovement equals to stockMovementId
        defaultProductVariantShouldBeFound("stockMovementId.equals=" + stockMovementId);

        // Get all the productVariantList where stockMovement equals to (stockMovementId + 1)
        defaultProductVariantShouldNotBeFound("stockMovementId.equals=" + (stockMovementId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductVariantShouldBeFound(String filter) throws Exception {
        restProductVariantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVariant.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].stockonhand").value(hasItem(DEFAULT_STOCKONHAND)))
            .andExpect(jsonPath("$.[*].stockallocated").value(hasItem(DEFAULT_STOCKALLOCATED)))
            .andExpect(jsonPath("$.[*].outofstockthreshold").value(hasItem(DEFAULT_OUTOFSTOCKTHRESHOLD)))
            .andExpect(jsonPath("$.[*].useglobaloutofstockthreshold").value(hasItem(DEFAULT_USEGLOBALOUTOFSTOCKTHRESHOLD.booleanValue())))
            .andExpect(jsonPath("$.[*].trackinventory").value(hasItem(DEFAULT_TRACKINVENTORY)));

        // Check, that the count call also returns 1
        restProductVariantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductVariantShouldNotBeFound(String filter) throws Exception {
        restProductVariantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductVariantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductVariant() throws Exception {
        // Get the productVariant
        restProductVariantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductVariant() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();

        // Update the productVariant
        ProductVariant updatedProductVariant = productVariantRepository.findById(productVariant.getId()).get();
        // Disconnect from session so that the updates on updatedProductVariant are not directly saved in db
        em.detach(updatedProductVariant);
        updatedProductVariant
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .enabled(UPDATED_ENABLED)
            .sku(UPDATED_SKU)
            .stockonhand(UPDATED_STOCKONHAND)
            .stockallocated(UPDATED_STOCKALLOCATED)
            .outofstockthreshold(UPDATED_OUTOFSTOCKTHRESHOLD)
            .useglobaloutofstockthreshold(UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD)
            .trackinventory(UPDATED_TRACKINVENTORY);
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(updatedProductVariant);

        restProductVariantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productVariantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
        ProductVariant testProductVariant = productVariantList.get(productVariantList.size() - 1);
        assertThat(testProductVariant.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductVariant.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductVariant.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testProductVariant.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testProductVariant.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testProductVariant.getStockonhand()).isEqualTo(UPDATED_STOCKONHAND);
        assertThat(testProductVariant.getStockallocated()).isEqualTo(UPDATED_STOCKALLOCATED);
        assertThat(testProductVariant.getOutofstockthreshold()).isEqualTo(UPDATED_OUTOFSTOCKTHRESHOLD);
        assertThat(testProductVariant.getUseglobaloutofstockthreshold()).isEqualTo(UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD);
        assertThat(testProductVariant.getTrackinventory()).isEqualTo(UPDATED_TRACKINVENTORY);
    }

    @Test
    @Transactional
    void putNonExistingProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();
        productVariant.setId(count.incrementAndGet());

        // Create the ProductVariant
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductVariantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productVariantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();
        productVariant.setId(count.incrementAndGet());

        // Create the ProductVariant
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();
        productVariant.setId(count.incrementAndGet());

        // Create the ProductVariant
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductVariantWithPatch() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();

        // Update the productVariant using partial update
        ProductVariant partialUpdatedProductVariant = new ProductVariant();
        partialUpdatedProductVariant.setId(productVariant.getId());

        partialUpdatedProductVariant
            .createdat(UPDATED_CREATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .sku(UPDATED_SKU)
            .useglobaloutofstockthreshold(UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD);

        restProductVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVariant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductVariant))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
        ProductVariant testProductVariant = productVariantList.get(productVariantList.size() - 1);
        assertThat(testProductVariant.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductVariant.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductVariant.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testProductVariant.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testProductVariant.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testProductVariant.getStockonhand()).isEqualTo(DEFAULT_STOCKONHAND);
        assertThat(testProductVariant.getStockallocated()).isEqualTo(DEFAULT_STOCKALLOCATED);
        assertThat(testProductVariant.getOutofstockthreshold()).isEqualTo(DEFAULT_OUTOFSTOCKTHRESHOLD);
        assertThat(testProductVariant.getUseglobaloutofstockthreshold()).isEqualTo(UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD);
        assertThat(testProductVariant.getTrackinventory()).isEqualTo(DEFAULT_TRACKINVENTORY);
    }

    @Test
    @Transactional
    void fullUpdateProductVariantWithPatch() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();

        // Update the productVariant using partial update
        ProductVariant partialUpdatedProductVariant = new ProductVariant();
        partialUpdatedProductVariant.setId(productVariant.getId());

        partialUpdatedProductVariant
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .enabled(UPDATED_ENABLED)
            .sku(UPDATED_SKU)
            .stockonhand(UPDATED_STOCKONHAND)
            .stockallocated(UPDATED_STOCKALLOCATED)
            .outofstockthreshold(UPDATED_OUTOFSTOCKTHRESHOLD)
            .useglobaloutofstockthreshold(UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD)
            .trackinventory(UPDATED_TRACKINVENTORY);

        restProductVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVariant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductVariant))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
        ProductVariant testProductVariant = productVariantList.get(productVariantList.size() - 1);
        assertThat(testProductVariant.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductVariant.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductVariant.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testProductVariant.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testProductVariant.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testProductVariant.getStockonhand()).isEqualTo(UPDATED_STOCKONHAND);
        assertThat(testProductVariant.getStockallocated()).isEqualTo(UPDATED_STOCKALLOCATED);
        assertThat(testProductVariant.getOutofstockthreshold()).isEqualTo(UPDATED_OUTOFSTOCKTHRESHOLD);
        assertThat(testProductVariant.getUseglobaloutofstockthreshold()).isEqualTo(UPDATED_USEGLOBALOUTOFSTOCKTHRESHOLD);
        assertThat(testProductVariant.getTrackinventory()).isEqualTo(UPDATED_TRACKINVENTORY);
    }

    @Test
    @Transactional
    void patchNonExistingProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();
        productVariant.setId(count.incrementAndGet());

        // Create the ProductVariant
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productVariantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();
        productVariant.setId(count.incrementAndGet());

        // Create the ProductVariant
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductVariant() throws Exception {
        int databaseSizeBeforeUpdate = productVariantRepository.findAll().size();
        productVariant.setId(count.incrementAndGet());

        // Create the ProductVariant
        ProductVariantDTO productVariantDTO = productVariantMapper.toDto(productVariant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVariant in the database
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductVariant() throws Exception {
        // Initialize the database
        productVariantRepository.saveAndFlush(productVariant);

        int databaseSizeBeforeDelete = productVariantRepository.findAll().size();

        // Delete the productVariant
        restProductVariantMockMvc
            .perform(delete(ENTITY_API_URL_ID, productVariant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductVariant> productVariantList = productVariantRepository.findAll();
        assertThat(productVariantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
