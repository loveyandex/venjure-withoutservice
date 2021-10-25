package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Asset;
import com.venjure.domain.Collection;
import com.venjure.domain.CollectionAsset;
import com.venjure.repository.CollectionAssetRepository;
import com.venjure.service.criteria.CollectionAssetCriteria;
import com.venjure.service.dto.CollectionAssetDTO;
import com.venjure.service.mapper.CollectionAssetMapper;
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
 * Integration tests for the {@link CollectionAssetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CollectionAssetResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_POSITION = 1;
    private static final Integer UPDATED_POSITION = 2;
    private static final Integer SMALLER_POSITION = 1 - 1;

    private static final String ENTITY_API_URL = "/api/collection-assets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CollectionAssetRepository collectionAssetRepository;

    @Autowired
    private CollectionAssetMapper collectionAssetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCollectionAssetMockMvc;

    private CollectionAsset collectionAsset;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectionAsset createEntity(EntityManager em) {
        CollectionAsset collectionAsset = new CollectionAsset()
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
        collectionAsset.setAsset(asset);
        // Add required entity
        Collection collection;
        if (TestUtil.findAll(em, Collection.class).isEmpty()) {
            collection = CollectionResourceIT.createEntity(em);
            em.persist(collection);
            em.flush();
        } else {
            collection = TestUtil.findAll(em, Collection.class).get(0);
        }
        collectionAsset.setCollection(collection);
        return collectionAsset;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollectionAsset createUpdatedEntity(EntityManager em) {
        CollectionAsset collectionAsset = new CollectionAsset()
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
        collectionAsset.setAsset(asset);
        // Add required entity
        Collection collection;
        if (TestUtil.findAll(em, Collection.class).isEmpty()) {
            collection = CollectionResourceIT.createUpdatedEntity(em);
            em.persist(collection);
            em.flush();
        } else {
            collection = TestUtil.findAll(em, Collection.class).get(0);
        }
        collectionAsset.setCollection(collection);
        return collectionAsset;
    }

    @BeforeEach
    public void initTest() {
        collectionAsset = createEntity(em);
    }

    @Test
    @Transactional
    void createCollectionAsset() throws Exception {
        int databaseSizeBeforeCreate = collectionAssetRepository.findAll().size();
        // Create the CollectionAsset
        CollectionAssetDTO collectionAssetDTO = collectionAssetMapper.toDto(collectionAsset);
        restCollectionAssetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionAssetDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CollectionAsset in the database
        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeCreate + 1);
        CollectionAsset testCollectionAsset = collectionAssetList.get(collectionAssetList.size() - 1);
        assertThat(testCollectionAsset.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testCollectionAsset.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testCollectionAsset.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void createCollectionAssetWithExistingId() throws Exception {
        // Create the CollectionAsset with an existing ID
        collectionAsset.setId(1L);
        CollectionAssetDTO collectionAssetDTO = collectionAssetMapper.toDto(collectionAsset);

        int databaseSizeBeforeCreate = collectionAssetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollectionAssetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionAsset in the database
        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionAssetRepository.findAll().size();
        // set the field null
        collectionAsset.setCreatedat(null);

        // Create the CollectionAsset, which fails.
        CollectionAssetDTO collectionAssetDTO = collectionAssetMapper.toDto(collectionAsset);

        restCollectionAssetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionAssetDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionAssetRepository.findAll().size();
        // set the field null
        collectionAsset.setUpdatedat(null);

        // Create the CollectionAsset, which fails.
        CollectionAssetDTO collectionAssetDTO = collectionAssetMapper.toDto(collectionAsset);

        restCollectionAssetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionAssetDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionAssetRepository.findAll().size();
        // set the field null
        collectionAsset.setPosition(null);

        // Create the CollectionAsset, which fails.
        CollectionAssetDTO collectionAssetDTO = collectionAssetMapper.toDto(collectionAsset);

        restCollectionAssetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionAssetDTO))
            )
            .andExpect(status().isBadRequest());

        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCollectionAssets() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList
        restCollectionAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collectionAsset.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getCollectionAsset() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get the collectionAsset
        restCollectionAssetMockMvc
            .perform(get(ENTITY_API_URL_ID, collectionAsset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(collectionAsset.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getCollectionAssetsByIdFiltering() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        Long id = collectionAsset.getId();

        defaultCollectionAssetShouldBeFound("id.equals=" + id);
        defaultCollectionAssetShouldNotBeFound("id.notEquals=" + id);

        defaultCollectionAssetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCollectionAssetShouldNotBeFound("id.greaterThan=" + id);

        defaultCollectionAssetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCollectionAssetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where createdat equals to DEFAULT_CREATEDAT
        defaultCollectionAssetShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the collectionAssetList where createdat equals to UPDATED_CREATEDAT
        defaultCollectionAssetShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where createdat not equals to DEFAULT_CREATEDAT
        defaultCollectionAssetShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the collectionAssetList where createdat not equals to UPDATED_CREATEDAT
        defaultCollectionAssetShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultCollectionAssetShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the collectionAssetList where createdat equals to UPDATED_CREATEDAT
        defaultCollectionAssetShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where createdat is not null
        defaultCollectionAssetShouldBeFound("createdat.specified=true");

        // Get all the collectionAssetList where createdat is null
        defaultCollectionAssetShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where updatedat equals to DEFAULT_UPDATEDAT
        defaultCollectionAssetShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the collectionAssetList where updatedat equals to UPDATED_UPDATEDAT
        defaultCollectionAssetShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultCollectionAssetShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the collectionAssetList where updatedat not equals to UPDATED_UPDATEDAT
        defaultCollectionAssetShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultCollectionAssetShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the collectionAssetList where updatedat equals to UPDATED_UPDATEDAT
        defaultCollectionAssetShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where updatedat is not null
        defaultCollectionAssetShouldBeFound("updatedat.specified=true");

        // Get all the collectionAssetList where updatedat is null
        defaultCollectionAssetShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where position equals to DEFAULT_POSITION
        defaultCollectionAssetShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the collectionAssetList where position equals to UPDATED_POSITION
        defaultCollectionAssetShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where position not equals to DEFAULT_POSITION
        defaultCollectionAssetShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the collectionAssetList where position not equals to UPDATED_POSITION
        defaultCollectionAssetShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultCollectionAssetShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the collectionAssetList where position equals to UPDATED_POSITION
        defaultCollectionAssetShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where position is not null
        defaultCollectionAssetShouldBeFound("position.specified=true");

        // Get all the collectionAssetList where position is null
        defaultCollectionAssetShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByPositionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where position is greater than or equal to DEFAULT_POSITION
        defaultCollectionAssetShouldBeFound("position.greaterThanOrEqual=" + DEFAULT_POSITION);

        // Get all the collectionAssetList where position is greater than or equal to UPDATED_POSITION
        defaultCollectionAssetShouldNotBeFound("position.greaterThanOrEqual=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByPositionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where position is less than or equal to DEFAULT_POSITION
        defaultCollectionAssetShouldBeFound("position.lessThanOrEqual=" + DEFAULT_POSITION);

        // Get all the collectionAssetList where position is less than or equal to SMALLER_POSITION
        defaultCollectionAssetShouldNotBeFound("position.lessThanOrEqual=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByPositionIsLessThanSomething() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where position is less than DEFAULT_POSITION
        defaultCollectionAssetShouldNotBeFound("position.lessThan=" + DEFAULT_POSITION);

        // Get all the collectionAssetList where position is less than UPDATED_POSITION
        defaultCollectionAssetShouldBeFound("position.lessThan=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByPositionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        // Get all the collectionAssetList where position is greater than DEFAULT_POSITION
        defaultCollectionAssetShouldNotBeFound("position.greaterThan=" + DEFAULT_POSITION);

        // Get all the collectionAssetList where position is greater than SMALLER_POSITION
        defaultCollectionAssetShouldBeFound("position.greaterThan=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByAssetIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);
        Asset asset = AssetResourceIT.createEntity(em);
        em.persist(asset);
        em.flush();
        collectionAsset.setAsset(asset);
        collectionAssetRepository.saveAndFlush(collectionAsset);
        Long assetId = asset.getId();

        // Get all the collectionAssetList where asset equals to assetId
        defaultCollectionAssetShouldBeFound("assetId.equals=" + assetId);

        // Get all the collectionAssetList where asset equals to (assetId + 1)
        defaultCollectionAssetShouldNotBeFound("assetId.equals=" + (assetId + 1));
    }

    @Test
    @Transactional
    void getAllCollectionAssetsByCollectionIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);
        Collection collection = CollectionResourceIT.createEntity(em);
        em.persist(collection);
        em.flush();
        collectionAsset.setCollection(collection);
        collectionAssetRepository.saveAndFlush(collectionAsset);
        Long collectionId = collection.getId();

        // Get all the collectionAssetList where collection equals to collectionId
        defaultCollectionAssetShouldBeFound("collectionId.equals=" + collectionId);

        // Get all the collectionAssetList where collection equals to (collectionId + 1)
        defaultCollectionAssetShouldNotBeFound("collectionId.equals=" + (collectionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCollectionAssetShouldBeFound(String filter) throws Exception {
        restCollectionAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collectionAsset.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));

        // Check, that the count call also returns 1
        restCollectionAssetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCollectionAssetShouldNotBeFound(String filter) throws Exception {
        restCollectionAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCollectionAssetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCollectionAsset() throws Exception {
        // Get the collectionAsset
        restCollectionAssetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCollectionAsset() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        int databaseSizeBeforeUpdate = collectionAssetRepository.findAll().size();

        // Update the collectionAsset
        CollectionAsset updatedCollectionAsset = collectionAssetRepository.findById(collectionAsset.getId()).get();
        // Disconnect from session so that the updates on updatedCollectionAsset are not directly saved in db
        em.detach(updatedCollectionAsset);
        updatedCollectionAsset.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).position(UPDATED_POSITION);
        CollectionAssetDTO collectionAssetDTO = collectionAssetMapper.toDto(updatedCollectionAsset);

        restCollectionAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collectionAssetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionAssetDTO))
            )
            .andExpect(status().isOk());

        // Validate the CollectionAsset in the database
        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeUpdate);
        CollectionAsset testCollectionAsset = collectionAssetList.get(collectionAssetList.size() - 1);
        assertThat(testCollectionAsset.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCollectionAsset.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCollectionAsset.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingCollectionAsset() throws Exception {
        int databaseSizeBeforeUpdate = collectionAssetRepository.findAll().size();
        collectionAsset.setId(count.incrementAndGet());

        // Create the CollectionAsset
        CollectionAssetDTO collectionAssetDTO = collectionAssetMapper.toDto(collectionAsset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectionAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collectionAssetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionAsset in the database
        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCollectionAsset() throws Exception {
        int databaseSizeBeforeUpdate = collectionAssetRepository.findAll().size();
        collectionAsset.setId(count.incrementAndGet());

        // Create the CollectionAsset
        CollectionAssetDTO collectionAssetDTO = collectionAssetMapper.toDto(collectionAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionAsset in the database
        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCollectionAsset() throws Exception {
        int databaseSizeBeforeUpdate = collectionAssetRepository.findAll().size();
        collectionAsset.setId(count.incrementAndGet());

        // Create the CollectionAsset
        CollectionAssetDTO collectionAssetDTO = collectionAssetMapper.toDto(collectionAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionAssetMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollectionAsset in the database
        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCollectionAssetWithPatch() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        int databaseSizeBeforeUpdate = collectionAssetRepository.findAll().size();

        // Update the collectionAsset using partial update
        CollectionAsset partialUpdatedCollectionAsset = new CollectionAsset();
        partialUpdatedCollectionAsset.setId(collectionAsset.getId());

        partialUpdatedCollectionAsset.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT);

        restCollectionAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollectionAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollectionAsset))
            )
            .andExpect(status().isOk());

        // Validate the CollectionAsset in the database
        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeUpdate);
        CollectionAsset testCollectionAsset = collectionAssetList.get(collectionAssetList.size() - 1);
        assertThat(testCollectionAsset.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCollectionAsset.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCollectionAsset.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateCollectionAssetWithPatch() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        int databaseSizeBeforeUpdate = collectionAssetRepository.findAll().size();

        // Update the collectionAsset using partial update
        CollectionAsset partialUpdatedCollectionAsset = new CollectionAsset();
        partialUpdatedCollectionAsset.setId(collectionAsset.getId());

        partialUpdatedCollectionAsset.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).position(UPDATED_POSITION);

        restCollectionAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollectionAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollectionAsset))
            )
            .andExpect(status().isOk());

        // Validate the CollectionAsset in the database
        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeUpdate);
        CollectionAsset testCollectionAsset = collectionAssetList.get(collectionAssetList.size() - 1);
        assertThat(testCollectionAsset.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCollectionAsset.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCollectionAsset.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingCollectionAsset() throws Exception {
        int databaseSizeBeforeUpdate = collectionAssetRepository.findAll().size();
        collectionAsset.setId(count.incrementAndGet());

        // Create the CollectionAsset
        CollectionAssetDTO collectionAssetDTO = collectionAssetMapper.toDto(collectionAsset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectionAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, collectionAssetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collectionAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionAsset in the database
        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCollectionAsset() throws Exception {
        int databaseSizeBeforeUpdate = collectionAssetRepository.findAll().size();
        collectionAsset.setId(count.incrementAndGet());

        // Create the CollectionAsset
        CollectionAssetDTO collectionAssetDTO = collectionAssetMapper.toDto(collectionAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collectionAssetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollectionAsset in the database
        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCollectionAsset() throws Exception {
        int databaseSizeBeforeUpdate = collectionAssetRepository.findAll().size();
        collectionAsset.setId(count.incrementAndGet());

        // Create the CollectionAsset
        CollectionAssetDTO collectionAssetDTO = collectionAssetMapper.toDto(collectionAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionAssetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collectionAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollectionAsset in the database
        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCollectionAsset() throws Exception {
        // Initialize the database
        collectionAssetRepository.saveAndFlush(collectionAsset);

        int databaseSizeBeforeDelete = collectionAssetRepository.findAll().size();

        // Delete the collectionAsset
        restCollectionAssetMockMvc
            .perform(delete(ENTITY_API_URL_ID, collectionAsset.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CollectionAsset> collectionAssetList = collectionAssetRepository.findAll();
        assertThat(collectionAssetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
