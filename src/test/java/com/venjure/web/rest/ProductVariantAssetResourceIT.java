package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Asset;
import com.venjure.domain.ProductVariant;
import com.venjure.domain.ProductVariantAsset;
import com.venjure.repository.ProductVariantAssetRepository;
import com.venjure.service.criteria.ProductVariantAssetCriteria;
import com.venjure.service.dto.ProductVariantAssetDTO;
import com.venjure.service.mapper.ProductVariantAssetMapper;
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
 * Integration tests for the {@link ProductVariantAssetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductVariantAssetResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_POSITION = 1;
    private static final Integer UPDATED_POSITION = 2;
    private static final Integer SMALLER_POSITION = 1 - 1;

    private static final String ENTITY_API_URL = "/api/product-variant-assets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductVariantAssetRepository productVariantAssetRepository;

    @Autowired
    private ProductVariantAssetMapper productVariantAssetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductVariantAssetMockMvc;

    private ProductVariantAsset productVariantAsset;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVariantAsset createEntity(EntityManager em) {
        ProductVariantAsset productVariantAsset = new ProductVariantAsset()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .position(DEFAULT_POSITION);
        // Add required entity
        Asset asset;
        if (TestUtil.findAll(em, Asset.class).isEmpty()) {
            asset = AssetResourceIT.createEntity(em);
            em.persist(asset);
            em.flush();
        } else {
            asset = TestUtil.findAll(em, Asset.class).get(0);
        }
        productVariantAsset.setAsset(asset);
        // Add required entity
        ProductVariant productVariant;
        if (TestUtil.findAll(em, ProductVariant.class).isEmpty()) {
            productVariant = ProductVariantResourceIT.createEntity(em);
            em.persist(productVariant);
            em.flush();
        } else {
            productVariant = TestUtil.findAll(em, ProductVariant.class).get(0);
        }
        productVariantAsset.setProductvariant(productVariant);
        return productVariantAsset;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVariantAsset createUpdatedEntity(EntityManager em) {
        ProductVariantAsset productVariantAsset = new ProductVariantAsset()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .position(UPDATED_POSITION);
        // Add required entity
        Asset asset;
        if (TestUtil.findAll(em, Asset.class).isEmpty()) {
            asset = AssetResourceIT.createUpdatedEntity(em);
            em.persist(asset);
            em.flush();
        } else {
            asset = TestUtil.findAll(em, Asset.class).get(0);
        }
        productVariantAsset.setAsset(asset);
        // Add required entity
        ProductVariant productVariant;
        if (TestUtil.findAll(em, ProductVariant.class).isEmpty()) {
            productVariant = ProductVariantResourceIT.createUpdatedEntity(em);
            em.persist(productVariant);
            em.flush();
        } else {
            productVariant = TestUtil.findAll(em, ProductVariant.class).get(0);
        }
        productVariantAsset.setProductvariant(productVariant);
        return productVariantAsset;
    }

    @BeforeEach
    public void initTest() {
        productVariantAsset = createEntity(em);
    }

    @Test
    @Transactional
    void createProductVariantAsset() throws Exception {
        int databaseSizeBeforeCreate = productVariantAssetRepository.findAll().size();
        // Create the ProductVariantAsset
        ProductVariantAssetDTO productVariantAssetDTO = productVariantAssetMapper.toDto(productVariantAsset);
        restProductVariantAssetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantAssetDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductVariantAsset in the database
        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeCreate + 1);
        ProductVariantAsset testProductVariantAsset = productVariantAssetList.get(productVariantAssetList.size() - 1);
        assertThat(testProductVariantAsset.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testProductVariantAsset.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductVariantAsset.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void createProductVariantAssetWithExistingId() throws Exception {
        // Create the ProductVariantAsset with an existing ID
        productVariantAsset.setId(1L);
        ProductVariantAssetDTO productVariantAssetDTO = productVariantAssetMapper.toDto(productVariantAsset);

        int databaseSizeBeforeCreate = productVariantAssetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductVariantAssetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantAsset in the database
        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantAssetRepository.findAll().size();
        // set the field null
        productVariantAsset.setCreatedat(null);

        // Create the ProductVariantAsset, which fails.
        ProductVariantAssetDTO productVariantAssetDTO = productVariantAssetMapper.toDto(productVariantAsset);

        restProductVariantAssetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantAssetDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantAssetRepository.findAll().size();
        // set the field null
        productVariantAsset.setUpdatedat(null);

        // Create the ProductVariantAsset, which fails.
        ProductVariantAssetDTO productVariantAssetDTO = productVariantAssetMapper.toDto(productVariantAsset);

        restProductVariantAssetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantAssetDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantAssetRepository.findAll().size();
        // set the field null
        productVariantAsset.setPosition(null);

        // Create the ProductVariantAsset, which fails.
        ProductVariantAssetDTO productVariantAssetDTO = productVariantAssetMapper.toDto(productVariantAsset);

        restProductVariantAssetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantAssetDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductVariantAssets() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList
        restProductVariantAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVariantAsset.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getProductVariantAsset() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get the productVariantAsset
        restProductVariantAssetMockMvc
            .perform(get(ENTITY_API_URL_ID, productVariantAsset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productVariantAsset.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getProductVariantAssetsByIdFiltering() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        Long id = productVariantAsset.getId();

        defaultProductVariantAssetShouldBeFound("id.equals=" + id);
        defaultProductVariantAssetShouldNotBeFound("id.notEquals=" + id);

        defaultProductVariantAssetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductVariantAssetShouldNotBeFound("id.greaterThan=" + id);

        defaultProductVariantAssetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductVariantAssetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where createdat equals to DEFAULT_CREATEDAT
        defaultProductVariantAssetShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the productVariantAssetList where createdat equals to UPDATED_CREATEDAT
        defaultProductVariantAssetShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where createdat not equals to DEFAULT_CREATEDAT
        defaultProductVariantAssetShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the productVariantAssetList where createdat not equals to UPDATED_CREATEDAT
        defaultProductVariantAssetShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultProductVariantAssetShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the productVariantAssetList where createdat equals to UPDATED_CREATEDAT
        defaultProductVariantAssetShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where createdat is not null
        defaultProductVariantAssetShouldBeFound("createdat.specified=true");

        // Get all the productVariantAssetList where createdat is null
        defaultProductVariantAssetShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where updatedat equals to DEFAULT_UPDATEDAT
        defaultProductVariantAssetShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the productVariantAssetList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductVariantAssetShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultProductVariantAssetShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the productVariantAssetList where updatedat not equals to UPDATED_UPDATEDAT
        defaultProductVariantAssetShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultProductVariantAssetShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the productVariantAssetList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductVariantAssetShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where updatedat is not null
        defaultProductVariantAssetShouldBeFound("updatedat.specified=true");

        // Get all the productVariantAssetList where updatedat is null
        defaultProductVariantAssetShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where position equals to DEFAULT_POSITION
        defaultProductVariantAssetShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the productVariantAssetList where position equals to UPDATED_POSITION
        defaultProductVariantAssetShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where position not equals to DEFAULT_POSITION
        defaultProductVariantAssetShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the productVariantAssetList where position not equals to UPDATED_POSITION
        defaultProductVariantAssetShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultProductVariantAssetShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the productVariantAssetList where position equals to UPDATED_POSITION
        defaultProductVariantAssetShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where position is not null
        defaultProductVariantAssetShouldBeFound("position.specified=true");

        // Get all the productVariantAssetList where position is null
        defaultProductVariantAssetShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByPositionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where position is greater than or equal to DEFAULT_POSITION
        defaultProductVariantAssetShouldBeFound("position.greaterThanOrEqual=" + DEFAULT_POSITION);

        // Get all the productVariantAssetList where position is greater than or equal to UPDATED_POSITION
        defaultProductVariantAssetShouldNotBeFound("position.greaterThanOrEqual=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByPositionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where position is less than or equal to DEFAULT_POSITION
        defaultProductVariantAssetShouldBeFound("position.lessThanOrEqual=" + DEFAULT_POSITION);

        // Get all the productVariantAssetList where position is less than or equal to SMALLER_POSITION
        defaultProductVariantAssetShouldNotBeFound("position.lessThanOrEqual=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByPositionIsLessThanSomething() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where position is less than DEFAULT_POSITION
        defaultProductVariantAssetShouldNotBeFound("position.lessThan=" + DEFAULT_POSITION);

        // Get all the productVariantAssetList where position is less than UPDATED_POSITION
        defaultProductVariantAssetShouldBeFound("position.lessThan=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByPositionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        // Get all the productVariantAssetList where position is greater than DEFAULT_POSITION
        defaultProductVariantAssetShouldNotBeFound("position.greaterThan=" + DEFAULT_POSITION);

        // Get all the productVariantAssetList where position is greater than SMALLER_POSITION
        defaultProductVariantAssetShouldBeFound("position.greaterThan=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByAssetIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);
        Asset asset = AssetResourceIT.createEntity(em);
        em.persist(asset);
        em.flush();
        productVariantAsset.setAsset(asset);
        productVariantAssetRepository.saveAndFlush(productVariantAsset);
        Long assetId = asset.getId();

        // Get all the productVariantAssetList where asset equals to assetId
        defaultProductVariantAssetShouldBeFound("assetId.equals=" + assetId);

        // Get all the productVariantAssetList where asset equals to (assetId + 1)
        defaultProductVariantAssetShouldNotBeFound("assetId.equals=" + (assetId + 1));
    }

    @Test
    @Transactional
    void getAllProductVariantAssetsByProductvariantIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);
        ProductVariant productvariant = ProductVariantResourceIT.createEntity(em);
        em.persist(productvariant);
        em.flush();
        productVariantAsset.setProductvariant(productvariant);
        productVariantAssetRepository.saveAndFlush(productVariantAsset);
        Long productvariantId = productvariant.getId();

        // Get all the productVariantAssetList where productvariant equals to productvariantId
        defaultProductVariantAssetShouldBeFound("productvariantId.equals=" + productvariantId);

        // Get all the productVariantAssetList where productvariant equals to (productvariantId + 1)
        defaultProductVariantAssetShouldNotBeFound("productvariantId.equals=" + (productvariantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductVariantAssetShouldBeFound(String filter) throws Exception {
        restProductVariantAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVariantAsset.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restProductVariantAssetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductVariantAssetShouldNotBeFound(String filter) throws Exception {
        restProductVariantAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductVariantAssetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductVariantAsset() throws Exception {
        // Get the productVariantAsset
        restProductVariantAssetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductVariantAsset() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        int databaseSizeBeforeUpdate = productVariantAssetRepository.findAll().size();

        // Update the productVariantAsset
        ProductVariantAsset updatedProductVariantAsset = productVariantAssetRepository.findById(productVariantAsset.getId()).get();
        // Disconnect from session so that the updates on updatedProductVariantAsset are not directly saved in db
        em.detach(updatedProductVariantAsset);
        updatedProductVariantAsset.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).position(UPDATED_POSITION);
        ProductVariantAssetDTO productVariantAssetDTO = productVariantAssetMapper.toDto(updatedProductVariantAsset);

        restProductVariantAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productVariantAssetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantAssetDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariantAsset in the database
        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeUpdate);
        ProductVariantAsset testProductVariantAsset = productVariantAssetList.get(productVariantAssetList.size() - 1);
        assertThat(testProductVariantAsset.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductVariantAsset.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductVariantAsset.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingProductVariantAsset() throws Exception {
        int databaseSizeBeforeUpdate = productVariantAssetRepository.findAll().size();
        productVariantAsset.setId(count.incrementAndGet());

        // Create the ProductVariantAsset
        ProductVariantAssetDTO productVariantAssetDTO = productVariantAssetMapper.toDto(productVariantAsset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductVariantAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productVariantAssetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantAsset in the database
        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductVariantAsset() throws Exception {
        int databaseSizeBeforeUpdate = productVariantAssetRepository.findAll().size();
        productVariantAsset.setId(count.incrementAndGet());

        // Create the ProductVariantAsset
        ProductVariantAssetDTO productVariantAssetDTO = productVariantAssetMapper.toDto(productVariantAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantAsset in the database
        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductVariantAsset() throws Exception {
        int databaseSizeBeforeUpdate = productVariantAssetRepository.findAll().size();
        productVariantAsset.setId(count.incrementAndGet());

        // Create the ProductVariantAsset
        ProductVariantAssetDTO productVariantAssetDTO = productVariantAssetMapper.toDto(productVariantAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantAssetMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVariantAsset in the database
        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductVariantAssetWithPatch() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        int databaseSizeBeforeUpdate = productVariantAssetRepository.findAll().size();

        // Update the productVariantAsset using partial update
        ProductVariantAsset partialUpdatedProductVariantAsset = new ProductVariantAsset();
        partialUpdatedProductVariantAsset.setId(productVariantAsset.getId());

        partialUpdatedProductVariantAsset.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).position(UPDATED_POSITION);

        restProductVariantAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVariantAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductVariantAsset))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariantAsset in the database
        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeUpdate);
        ProductVariantAsset testProductVariantAsset = productVariantAssetList.get(productVariantAssetList.size() - 1);
        assertThat(testProductVariantAsset.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductVariantAsset.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductVariantAsset.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateProductVariantAssetWithPatch() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        int databaseSizeBeforeUpdate = productVariantAssetRepository.findAll().size();

        // Update the productVariantAsset using partial update
        ProductVariantAsset partialUpdatedProductVariantAsset = new ProductVariantAsset();
        partialUpdatedProductVariantAsset.setId(productVariantAsset.getId());

        partialUpdatedProductVariantAsset.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).position(UPDATED_POSITION);

        restProductVariantAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVariantAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductVariantAsset))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariantAsset in the database
        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeUpdate);
        ProductVariantAsset testProductVariantAsset = productVariantAssetList.get(productVariantAssetList.size() - 1);
        assertThat(testProductVariantAsset.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductVariantAsset.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductVariantAsset.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingProductVariantAsset() throws Exception {
        int databaseSizeBeforeUpdate = productVariantAssetRepository.findAll().size();
        productVariantAsset.setId(count.incrementAndGet());

        // Create the ProductVariantAsset
        ProductVariantAssetDTO productVariantAssetDTO = productVariantAssetMapper.toDto(productVariantAsset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductVariantAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productVariantAssetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariantAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantAsset in the database
        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductVariantAsset() throws Exception {
        int databaseSizeBeforeUpdate = productVariantAssetRepository.findAll().size();
        productVariantAsset.setId(count.incrementAndGet());

        // Create the ProductVariantAsset
        ProductVariantAssetDTO productVariantAssetDTO = productVariantAssetMapper.toDto(productVariantAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariantAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantAsset in the database
        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductVariantAsset() throws Exception {
        int databaseSizeBeforeUpdate = productVariantAssetRepository.findAll().size();
        productVariantAsset.setId(count.incrementAndGet());

        // Create the ProductVariantAsset
        ProductVariantAssetDTO productVariantAssetDTO = productVariantAssetMapper.toDto(productVariantAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantAssetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariantAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVariantAsset in the database
        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductVariantAsset() throws Exception {
        // Initialize the database
        productVariantAssetRepository.saveAndFlush(productVariantAsset);

        int databaseSizeBeforeDelete = productVariantAssetRepository.findAll().size();

        // Delete the productVariantAsset
        restProductVariantAssetMockMvc
            .perform(delete(ENTITY_API_URL_ID, productVariantAsset.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductVariantAsset> productVariantAssetList = productVariantAssetRepository.findAll();
        assertThat(productVariantAssetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
