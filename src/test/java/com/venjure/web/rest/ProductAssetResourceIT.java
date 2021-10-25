package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Asset;
import com.venjure.domain.Product;
import com.venjure.domain.ProductAsset;
import com.venjure.repository.ProductAssetRepository;
import com.venjure.service.criteria.ProductAssetCriteria;
import com.venjure.service.dto.ProductAssetDTO;
import com.venjure.service.mapper.ProductAssetMapper;
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
 * Integration tests for the {@link ProductAssetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductAssetResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_POSITION = 1;
    private static final Integer UPDATED_POSITION = 2;
    private static final Integer SMALLER_POSITION = 1 - 1;

    private static final String ENTITY_API_URL = "/api/product-assets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductAssetRepository productAssetRepository;

    @Autowired
    private ProductAssetMapper productAssetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductAssetMockMvc;

    private ProductAsset productAsset;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAsset createEntity(EntityManager em) {
        ProductAsset productAsset = new ProductAsset().createdat(DEFAULT_CREATEDAT).updatedat(DEFAULT_UPDATEDAT).position(DEFAULT_POSITION);
        // Add required entity
        Asset asset;
        if (TestUtil.findAll(em, Asset.class).isEmpty()) {
            asset = AssetResourceIT.createEntity(em);
            em.persist(asset);
            em.flush();
        } else {
            asset = TestUtil.findAll(em, Asset.class).get(0);
        }
        productAsset.setAsset(asset);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productAsset.setProduct(product);
        return productAsset;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductAsset createUpdatedEntity(EntityManager em) {
        ProductAsset productAsset = new ProductAsset().createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).position(UPDATED_POSITION);
        // Add required entity
        Asset asset;
        if (TestUtil.findAll(em, Asset.class).isEmpty()) {
            asset = AssetResourceIT.createUpdatedEntity(em);
            em.persist(asset);
            em.flush();
        } else {
            asset = TestUtil.findAll(em, Asset.class).get(0);
        }
        productAsset.setAsset(asset);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productAsset.setProduct(product);
        return productAsset;
    }

    @BeforeEach
    public void initTest() {
        productAsset = createEntity(em);
    }

    @Test
    @Transactional
    void createProductAsset() throws Exception {
        int databaseSizeBeforeCreate = productAssetRepository.findAll().size();
        // Create the ProductAsset
        ProductAssetDTO productAssetDTO = productAssetMapper.toDto(productAsset);
        restProductAssetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productAssetDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductAsset in the database
        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeCreate + 1);
        ProductAsset testProductAsset = productAssetList.get(productAssetList.size() - 1);
        assertThat(testProductAsset.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testProductAsset.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductAsset.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void createProductAssetWithExistingId() throws Exception {
        // Create the ProductAsset with an existing ID
        productAsset.setId(1L);
        ProductAssetDTO productAssetDTO = productAssetMapper.toDto(productAsset);

        int databaseSizeBeforeCreate = productAssetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductAssetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAsset in the database
        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productAssetRepository.findAll().size();
        // set the field null
        productAsset.setCreatedat(null);

        // Create the ProductAsset, which fails.
        ProductAssetDTO productAssetDTO = productAssetMapper.toDto(productAsset);

        restProductAssetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productAssetDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productAssetRepository.findAll().size();
        // set the field null
        productAsset.setUpdatedat(null);

        // Create the ProductAsset, which fails.
        ProductAssetDTO productAssetDTO = productAssetMapper.toDto(productAsset);

        restProductAssetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productAssetDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productAssetRepository.findAll().size();
        // set the field null
        productAsset.setPosition(null);

        // Create the ProductAsset, which fails.
        ProductAssetDTO productAssetDTO = productAssetMapper.toDto(productAsset);

        restProductAssetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productAssetDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductAssets() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList
        restProductAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAsset.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getProductAsset() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get the productAsset
        restProductAssetMockMvc
            .perform(get(ENTITY_API_URL_ID, productAsset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productAsset.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getProductAssetsByIdFiltering() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        Long id = productAsset.getId();

        defaultProductAssetShouldBeFound("id.equals=" + id);
        defaultProductAssetShouldNotBeFound("id.notEquals=" + id);

        defaultProductAssetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductAssetShouldNotBeFound("id.greaterThan=" + id);

        defaultProductAssetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductAssetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductAssetsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where createdat equals to DEFAULT_CREATEDAT
        defaultProductAssetShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the productAssetList where createdat equals to UPDATED_CREATEDAT
        defaultProductAssetShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductAssetsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where createdat not equals to DEFAULT_CREATEDAT
        defaultProductAssetShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the productAssetList where createdat not equals to UPDATED_CREATEDAT
        defaultProductAssetShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductAssetsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultProductAssetShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the productAssetList where createdat equals to UPDATED_CREATEDAT
        defaultProductAssetShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductAssetsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where createdat is not null
        defaultProductAssetShouldBeFound("createdat.specified=true");

        // Get all the productAssetList where createdat is null
        defaultProductAssetShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAssetsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where updatedat equals to DEFAULT_UPDATEDAT
        defaultProductAssetShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the productAssetList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductAssetShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductAssetsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultProductAssetShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the productAssetList where updatedat not equals to UPDATED_UPDATEDAT
        defaultProductAssetShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductAssetsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultProductAssetShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the productAssetList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductAssetShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductAssetsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where updatedat is not null
        defaultProductAssetShouldBeFound("updatedat.specified=true");

        // Get all the productAssetList where updatedat is null
        defaultProductAssetShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAssetsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where position equals to DEFAULT_POSITION
        defaultProductAssetShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the productAssetList where position equals to UPDATED_POSITION
        defaultProductAssetShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllProductAssetsByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where position not equals to DEFAULT_POSITION
        defaultProductAssetShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the productAssetList where position not equals to UPDATED_POSITION
        defaultProductAssetShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllProductAssetsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultProductAssetShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the productAssetList where position equals to UPDATED_POSITION
        defaultProductAssetShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllProductAssetsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where position is not null
        defaultProductAssetShouldBeFound("position.specified=true");

        // Get all the productAssetList where position is null
        defaultProductAssetShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    void getAllProductAssetsByPositionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where position is greater than or equal to DEFAULT_POSITION
        defaultProductAssetShouldBeFound("position.greaterThanOrEqual=" + DEFAULT_POSITION);

        // Get all the productAssetList where position is greater than or equal to UPDATED_POSITION
        defaultProductAssetShouldNotBeFound("position.greaterThanOrEqual=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllProductAssetsByPositionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where position is less than or equal to DEFAULT_POSITION
        defaultProductAssetShouldBeFound("position.lessThanOrEqual=" + DEFAULT_POSITION);

        // Get all the productAssetList where position is less than or equal to SMALLER_POSITION
        defaultProductAssetShouldNotBeFound("position.lessThanOrEqual=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllProductAssetsByPositionIsLessThanSomething() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where position is less than DEFAULT_POSITION
        defaultProductAssetShouldNotBeFound("position.lessThan=" + DEFAULT_POSITION);

        // Get all the productAssetList where position is less than UPDATED_POSITION
        defaultProductAssetShouldBeFound("position.lessThan=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllProductAssetsByPositionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        // Get all the productAssetList where position is greater than DEFAULT_POSITION
        defaultProductAssetShouldNotBeFound("position.greaterThan=" + DEFAULT_POSITION);

        // Get all the productAssetList where position is greater than SMALLER_POSITION
        defaultProductAssetShouldBeFound("position.greaterThan=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllProductAssetsByAssetIsEqualToSomething() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);
        Asset asset = AssetResourceIT.createEntity(em);
        em.persist(asset);
        em.flush();
        productAsset.setAsset(asset);
        productAssetRepository.saveAndFlush(productAsset);
        Long assetId = asset.getId();

        // Get all the productAssetList where asset equals to assetId
        defaultProductAssetShouldBeFound("assetId.equals=" + assetId);

        // Get all the productAssetList where asset equals to (assetId + 1)
        defaultProductAssetShouldNotBeFound("assetId.equals=" + (assetId + 1));
    }

    @Test
    @Transactional
    void getAllProductAssetsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);
        Product product = ProductResourceIT.createEntity(em);
        em.persist(product);
        em.flush();
        productAsset.setProduct(product);
        productAssetRepository.saveAndFlush(productAsset);
        Long productId = product.getId();

        // Get all the productAssetList where product equals to productId
        defaultProductAssetShouldBeFound("productId.equals=" + productId);

        // Get all the productAssetList where product equals to (productId + 1)
        defaultProductAssetShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductAssetShouldBeFound(String filter) throws Exception {
        restProductAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productAsset.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restProductAssetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductAssetShouldNotBeFound(String filter) throws Exception {
        restProductAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductAssetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductAsset() throws Exception {
        // Get the productAsset
        restProductAssetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductAsset() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        int databaseSizeBeforeUpdate = productAssetRepository.findAll().size();

        // Update the productAsset
        ProductAsset updatedProductAsset = productAssetRepository.findById(productAsset.getId()).get();
        // Disconnect from session so that the updates on updatedProductAsset are not directly saved in db
        em.detach(updatedProductAsset);
        updatedProductAsset.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).position(UPDATED_POSITION);
        ProductAssetDTO productAssetDTO = productAssetMapper.toDto(updatedProductAsset);

        restProductAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productAssetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productAssetDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductAsset in the database
        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeUpdate);
        ProductAsset testProductAsset = productAssetList.get(productAssetList.size() - 1);
        assertThat(testProductAsset.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductAsset.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductAsset.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingProductAsset() throws Exception {
        int databaseSizeBeforeUpdate = productAssetRepository.findAll().size();
        productAsset.setId(count.incrementAndGet());

        // Create the ProductAsset
        ProductAssetDTO productAssetDTO = productAssetMapper.toDto(productAsset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productAssetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAsset in the database
        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductAsset() throws Exception {
        int databaseSizeBeforeUpdate = productAssetRepository.findAll().size();
        productAsset.setId(count.incrementAndGet());

        // Create the ProductAsset
        ProductAssetDTO productAssetDTO = productAssetMapper.toDto(productAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAsset in the database
        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductAsset() throws Exception {
        int databaseSizeBeforeUpdate = productAssetRepository.findAll().size();
        productAsset.setId(count.incrementAndGet());

        // Create the ProductAsset
        ProductAssetDTO productAssetDTO = productAssetMapper.toDto(productAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAssetMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAsset in the database
        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductAssetWithPatch() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        int databaseSizeBeforeUpdate = productAssetRepository.findAll().size();

        // Update the productAsset using partial update
        ProductAsset partialUpdatedProductAsset = new ProductAsset();
        partialUpdatedProductAsset.setId(productAsset.getId());

        partialUpdatedProductAsset.position(UPDATED_POSITION);

        restProductAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductAsset))
            )
            .andExpect(status().isOk());

        // Validate the ProductAsset in the database
        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeUpdate);
        ProductAsset testProductAsset = productAssetList.get(productAssetList.size() - 1);
        assertThat(testProductAsset.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testProductAsset.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductAsset.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateProductAssetWithPatch() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        int databaseSizeBeforeUpdate = productAssetRepository.findAll().size();

        // Update the productAsset using partial update
        ProductAsset partialUpdatedProductAsset = new ProductAsset();
        partialUpdatedProductAsset.setId(productAsset.getId());

        partialUpdatedProductAsset.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).position(UPDATED_POSITION);

        restProductAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductAsset))
            )
            .andExpect(status().isOk());

        // Validate the ProductAsset in the database
        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeUpdate);
        ProductAsset testProductAsset = productAssetList.get(productAssetList.size() - 1);
        assertThat(testProductAsset.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductAsset.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductAsset.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingProductAsset() throws Exception {
        int databaseSizeBeforeUpdate = productAssetRepository.findAll().size();
        productAsset.setId(count.incrementAndGet());

        // Create the ProductAsset
        ProductAssetDTO productAssetDTO = productAssetMapper.toDto(productAsset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productAssetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAsset in the database
        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductAsset() throws Exception {
        int databaseSizeBeforeUpdate = productAssetRepository.findAll().size();
        productAsset.setId(count.incrementAndGet());

        // Create the ProductAsset
        ProductAssetDTO productAssetDTO = productAssetMapper.toDto(productAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductAsset in the database
        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductAsset() throws Exception {
        int databaseSizeBeforeUpdate = productAssetRepository.findAll().size();
        productAsset.setId(count.incrementAndGet());

        // Create the ProductAsset
        ProductAssetDTO productAssetDTO = productAssetMapper.toDto(productAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductAssetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductAsset in the database
        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductAsset() throws Exception {
        // Initialize the database
        productAssetRepository.saveAndFlush(productAsset);

        int databaseSizeBeforeDelete = productAssetRepository.findAll().size();

        // Delete the productAsset
        restProductAssetMockMvc
            .perform(delete(ENTITY_API_URL_ID, productAsset.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductAsset> productAssetList = productAssetRepository.findAll();
        assertThat(productAssetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
