package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Asset;
import com.venjure.domain.Collection;
import com.venjure.domain.Collection;
import com.venjure.domain.CollectionTranslation;
import com.venjure.domain.ProductVariant;
import com.venjure.repository.CollectionRepository;
import com.venjure.service.criteria.CollectionCriteria;
import com.venjure.service.dto.CollectionDTO;
import com.venjure.service.mapper.CollectionMapper;
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
 * Integration tests for the {@link CollectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CollectionResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ISROOT = false;
    private static final Boolean UPDATED_ISROOT = true;

    private static final Integer DEFAULT_POSITION = 1;
    private static final Integer UPDATED_POSITION = 2;
    private static final Integer SMALLER_POSITION = 1 - 1;

    private static final Boolean DEFAULT_ISPRIVATE = false;
    private static final Boolean UPDATED_ISPRIVATE = true;

    private static final String DEFAULT_FILTERS = "AAAAAAAAAA";
    private static final String UPDATED_FILTERS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/collections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCollectionMockMvc;

    private Collection collection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Collection createEntity(EntityManager em) {
        Collection collection = new Collection()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .isroot(DEFAULT_ISROOT)
            .position(DEFAULT_POSITION)
            .isprivate(DEFAULT_ISPRIVATE)
            .filters(DEFAULT_FILTERS);
        return collection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Collection createUpdatedEntity(EntityManager em) {
        Collection collection = new Collection()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .isroot(UPDATED_ISROOT)
            .position(UPDATED_POSITION)
            .isprivate(UPDATED_ISPRIVATE)
            .filters(UPDATED_FILTERS);
        return collection;
    }

    @BeforeEach
    public void initTest() {
        collection = createEntity(em);
    }

    @Test
    @Transactional
    void createCollection() throws Exception {
        int databaseSizeBeforeCreate = collectionRepository.findAll().size();
        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);
        restCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isCreated());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeCreate + 1);
        Collection testCollection = collectionList.get(collectionList.size() - 1);
        assertThat(testCollection.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testCollection.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testCollection.getIsroot()).isEqualTo(DEFAULT_ISROOT);
        assertThat(testCollection.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testCollection.getIsprivate()).isEqualTo(DEFAULT_ISPRIVATE);
        assertThat(testCollection.getFilters()).isEqualTo(DEFAULT_FILTERS);
    }

    @Test
    @Transactional
    void createCollectionWithExistingId() throws Exception {
        // Create the Collection with an existing ID
        collection.setId(1L);
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        int databaseSizeBeforeCreate = collectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionRepository.findAll().size();
        // set the field null
        collection.setCreatedat(null);

        // Create the Collection, which fails.
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        restCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionRepository.findAll().size();
        // set the field null
        collection.setUpdatedat(null);

        // Create the Collection, which fails.
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        restCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsrootIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionRepository.findAll().size();
        // set the field null
        collection.setIsroot(null);

        // Create the Collection, which fails.
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        restCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionRepository.findAll().size();
        // set the field null
        collection.setPosition(null);

        // Create the Collection, which fails.
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        restCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsprivateIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionRepository.findAll().size();
        // set the field null
        collection.setIsprivate(null);

        // Create the Collection, which fails.
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        restCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiltersIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionRepository.findAll().size();
        // set the field null
        collection.setFilters(null);

        // Create the Collection, which fails.
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        restCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCollections() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList
        restCollectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collection.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].isroot").value(hasItem(DEFAULT_ISROOT.booleanValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].isprivate").value(hasItem(DEFAULT_ISPRIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].filters").value(hasItem(DEFAULT_FILTERS)));
    }

    @Test
    @Transactional
    void getCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get the collection
        restCollectionMockMvc
            .perform(get(ENTITY_API_URL_ID, collection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(collection.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.isroot").value(DEFAULT_ISROOT.booleanValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.isprivate").value(DEFAULT_ISPRIVATE.booleanValue()))
            .andExpect(jsonPath("$.filters").value(DEFAULT_FILTERS));
    }

    @Test
    @Transactional
    void getCollectionsByIdFiltering() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        Long id = collection.getId();

        defaultCollectionShouldBeFound("id.equals=" + id);
        defaultCollectionShouldNotBeFound("id.notEquals=" + id);

        defaultCollectionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCollectionShouldNotBeFound("id.greaterThan=" + id);

        defaultCollectionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCollectionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCollectionsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where createdat equals to DEFAULT_CREATEDAT
        defaultCollectionShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the collectionList where createdat equals to UPDATED_CREATEDAT
        defaultCollectionShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where createdat not equals to DEFAULT_CREATEDAT
        defaultCollectionShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the collectionList where createdat not equals to UPDATED_CREATEDAT
        defaultCollectionShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultCollectionShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the collectionList where createdat equals to UPDATED_CREATEDAT
        defaultCollectionShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where createdat is not null
        defaultCollectionShouldBeFound("createdat.specified=true");

        // Get all the collectionList where createdat is null
        defaultCollectionShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where updatedat equals to DEFAULT_UPDATEDAT
        defaultCollectionShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the collectionList where updatedat equals to UPDATED_UPDATEDAT
        defaultCollectionShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultCollectionShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the collectionList where updatedat not equals to UPDATED_UPDATEDAT
        defaultCollectionShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultCollectionShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the collectionList where updatedat equals to UPDATED_UPDATEDAT
        defaultCollectionShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCollectionsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where updatedat is not null
        defaultCollectionShouldBeFound("updatedat.specified=true");

        // Get all the collectionList where updatedat is null
        defaultCollectionShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionsByIsrootIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where isroot equals to DEFAULT_ISROOT
        defaultCollectionShouldBeFound("isroot.equals=" + DEFAULT_ISROOT);

        // Get all the collectionList where isroot equals to UPDATED_ISROOT
        defaultCollectionShouldNotBeFound("isroot.equals=" + UPDATED_ISROOT);
    }

    @Test
    @Transactional
    void getAllCollectionsByIsrootIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where isroot not equals to DEFAULT_ISROOT
        defaultCollectionShouldNotBeFound("isroot.notEquals=" + DEFAULT_ISROOT);

        // Get all the collectionList where isroot not equals to UPDATED_ISROOT
        defaultCollectionShouldBeFound("isroot.notEquals=" + UPDATED_ISROOT);
    }

    @Test
    @Transactional
    void getAllCollectionsByIsrootIsInShouldWork() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where isroot in DEFAULT_ISROOT or UPDATED_ISROOT
        defaultCollectionShouldBeFound("isroot.in=" + DEFAULT_ISROOT + "," + UPDATED_ISROOT);

        // Get all the collectionList where isroot equals to UPDATED_ISROOT
        defaultCollectionShouldNotBeFound("isroot.in=" + UPDATED_ISROOT);
    }

    @Test
    @Transactional
    void getAllCollectionsByIsrootIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where isroot is not null
        defaultCollectionShouldBeFound("isroot.specified=true");

        // Get all the collectionList where isroot is null
        defaultCollectionShouldNotBeFound("isroot.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where position equals to DEFAULT_POSITION
        defaultCollectionShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the collectionList where position equals to UPDATED_POSITION
        defaultCollectionShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionsByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where position not equals to DEFAULT_POSITION
        defaultCollectionShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the collectionList where position not equals to UPDATED_POSITION
        defaultCollectionShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultCollectionShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the collectionList where position equals to UPDATED_POSITION
        defaultCollectionShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where position is not null
        defaultCollectionShouldBeFound("position.specified=true");

        // Get all the collectionList where position is null
        defaultCollectionShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionsByPositionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where position is greater than or equal to DEFAULT_POSITION
        defaultCollectionShouldBeFound("position.greaterThanOrEqual=" + DEFAULT_POSITION);

        // Get all the collectionList where position is greater than or equal to UPDATED_POSITION
        defaultCollectionShouldNotBeFound("position.greaterThanOrEqual=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionsByPositionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where position is less than or equal to DEFAULT_POSITION
        defaultCollectionShouldBeFound("position.lessThanOrEqual=" + DEFAULT_POSITION);

        // Get all the collectionList where position is less than or equal to SMALLER_POSITION
        defaultCollectionShouldNotBeFound("position.lessThanOrEqual=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionsByPositionIsLessThanSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where position is less than DEFAULT_POSITION
        defaultCollectionShouldNotBeFound("position.lessThan=" + DEFAULT_POSITION);

        // Get all the collectionList where position is less than UPDATED_POSITION
        defaultCollectionShouldBeFound("position.lessThan=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionsByPositionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where position is greater than DEFAULT_POSITION
        defaultCollectionShouldNotBeFound("position.greaterThan=" + DEFAULT_POSITION);

        // Get all the collectionList where position is greater than SMALLER_POSITION
        defaultCollectionShouldBeFound("position.greaterThan=" + SMALLER_POSITION);
    }

    @Test
    @Transactional
    void getAllCollectionsByIsprivateIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where isprivate equals to DEFAULT_ISPRIVATE
        defaultCollectionShouldBeFound("isprivate.equals=" + DEFAULT_ISPRIVATE);

        // Get all the collectionList where isprivate equals to UPDATED_ISPRIVATE
        defaultCollectionShouldNotBeFound("isprivate.equals=" + UPDATED_ISPRIVATE);
    }

    @Test
    @Transactional
    void getAllCollectionsByIsprivateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where isprivate not equals to DEFAULT_ISPRIVATE
        defaultCollectionShouldNotBeFound("isprivate.notEquals=" + DEFAULT_ISPRIVATE);

        // Get all the collectionList where isprivate not equals to UPDATED_ISPRIVATE
        defaultCollectionShouldBeFound("isprivate.notEquals=" + UPDATED_ISPRIVATE);
    }

    @Test
    @Transactional
    void getAllCollectionsByIsprivateIsInShouldWork() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where isprivate in DEFAULT_ISPRIVATE or UPDATED_ISPRIVATE
        defaultCollectionShouldBeFound("isprivate.in=" + DEFAULT_ISPRIVATE + "," + UPDATED_ISPRIVATE);

        // Get all the collectionList where isprivate equals to UPDATED_ISPRIVATE
        defaultCollectionShouldNotBeFound("isprivate.in=" + UPDATED_ISPRIVATE);
    }

    @Test
    @Transactional
    void getAllCollectionsByIsprivateIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where isprivate is not null
        defaultCollectionShouldBeFound("isprivate.specified=true");

        // Get all the collectionList where isprivate is null
        defaultCollectionShouldNotBeFound("isprivate.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionsByFiltersIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where filters equals to DEFAULT_FILTERS
        defaultCollectionShouldBeFound("filters.equals=" + DEFAULT_FILTERS);

        // Get all the collectionList where filters equals to UPDATED_FILTERS
        defaultCollectionShouldNotBeFound("filters.equals=" + UPDATED_FILTERS);
    }

    @Test
    @Transactional
    void getAllCollectionsByFiltersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where filters not equals to DEFAULT_FILTERS
        defaultCollectionShouldNotBeFound("filters.notEquals=" + DEFAULT_FILTERS);

        // Get all the collectionList where filters not equals to UPDATED_FILTERS
        defaultCollectionShouldBeFound("filters.notEquals=" + UPDATED_FILTERS);
    }

    @Test
    @Transactional
    void getAllCollectionsByFiltersIsInShouldWork() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where filters in DEFAULT_FILTERS or UPDATED_FILTERS
        defaultCollectionShouldBeFound("filters.in=" + DEFAULT_FILTERS + "," + UPDATED_FILTERS);

        // Get all the collectionList where filters equals to UPDATED_FILTERS
        defaultCollectionShouldNotBeFound("filters.in=" + UPDATED_FILTERS);
    }

    @Test
    @Transactional
    void getAllCollectionsByFiltersIsNullOrNotNull() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where filters is not null
        defaultCollectionShouldBeFound("filters.specified=true");

        // Get all the collectionList where filters is null
        defaultCollectionShouldNotBeFound("filters.specified=false");
    }

    @Test
    @Transactional
    void getAllCollectionsByFiltersContainsSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where filters contains DEFAULT_FILTERS
        defaultCollectionShouldBeFound("filters.contains=" + DEFAULT_FILTERS);

        // Get all the collectionList where filters contains UPDATED_FILTERS
        defaultCollectionShouldNotBeFound("filters.contains=" + UPDATED_FILTERS);
    }

    @Test
    @Transactional
    void getAllCollectionsByFiltersNotContainsSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList where filters does not contain DEFAULT_FILTERS
        defaultCollectionShouldNotBeFound("filters.doesNotContain=" + DEFAULT_FILTERS);

        // Get all the collectionList where filters does not contain UPDATED_FILTERS
        defaultCollectionShouldBeFound("filters.doesNotContain=" + UPDATED_FILTERS);
    }

    @Test
    @Transactional
    void getAllCollectionsByFeaturedassetIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);
        Asset featuredasset = AssetResourceIT.createEntity(em);
        em.persist(featuredasset);
        em.flush();
        collection.setFeaturedasset(featuredasset);
        collectionRepository.saveAndFlush(collection);
        Long featuredassetId = featuredasset.getId();

        // Get all the collectionList where featuredasset equals to featuredassetId
        defaultCollectionShouldBeFound("featuredassetId.equals=" + featuredassetId);

        // Get all the collectionList where featuredasset equals to (featuredassetId + 1)
        defaultCollectionShouldNotBeFound("featuredassetId.equals=" + (featuredassetId + 1));
    }

    @Test
    @Transactional
    void getAllCollectionsByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);
        Collection parent = CollectionResourceIT.createEntity(em);
        em.persist(parent);
        em.flush();
        collection.setParent(parent);
        collectionRepository.saveAndFlush(collection);
        Long parentId = parent.getId();

        // Get all the collectionList where parent equals to parentId
        defaultCollectionShouldBeFound("parentId.equals=" + parentId);

        // Get all the collectionList where parent equals to (parentId + 1)
        defaultCollectionShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllCollectionsByCollectionTranslationIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);
        CollectionTranslation collectionTranslation = CollectionTranslationResourceIT.createEntity(em);
        em.persist(collectionTranslation);
        em.flush();
        collection.addCollectionTranslation(collectionTranslation);
        collectionRepository.saveAndFlush(collection);
        Long collectionTranslationId = collectionTranslation.getId();

        // Get all the collectionList where collectionTranslation equals to collectionTranslationId
        defaultCollectionShouldBeFound("collectionTranslationId.equals=" + collectionTranslationId);

        // Get all the collectionList where collectionTranslation equals to (collectionTranslationId + 1)
        defaultCollectionShouldNotBeFound("collectionTranslationId.equals=" + (collectionTranslationId + 1));
    }

    @Test
    @Transactional
    void getAllCollectionsByProductvariantIsEqualToSomething() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);
        ProductVariant productvariant = ProductVariantResourceIT.createEntity(em);
        em.persist(productvariant);
        em.flush();
        collection.addProductvariant(productvariant);
        collectionRepository.saveAndFlush(collection);
        Long productvariantId = productvariant.getId();

        // Get all the collectionList where productvariant equals to productvariantId
        defaultCollectionShouldBeFound("productvariantId.equals=" + productvariantId);

        // Get all the collectionList where productvariant equals to (productvariantId + 1)
        defaultCollectionShouldNotBeFound("productvariantId.equals=" + (productvariantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCollectionShouldBeFound(String filter) throws Exception {
        restCollectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collection.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].isroot").value(hasItem(DEFAULT_ISROOT.booleanValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].isprivate").value(hasItem(DEFAULT_ISPRIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].filters").value(hasItem(DEFAULT_FILTERS)));

        // Check, that the count call also returns 1
        restCollectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCollectionShouldNotBeFound(String filter) throws Exception {
        restCollectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCollectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCollection() throws Exception {
        // Get the collection
        restCollectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();

        // Update the collection
        Collection updatedCollection = collectionRepository.findById(collection.getId()).get();
        // Disconnect from session so that the updates on updatedCollection are not directly saved in db
        em.detach(updatedCollection);
        updatedCollection
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .isroot(UPDATED_ISROOT)
            .position(UPDATED_POSITION)
            .isprivate(UPDATED_ISPRIVATE)
            .filters(UPDATED_FILTERS);
        CollectionDTO collectionDTO = collectionMapper.toDto(updatedCollection);

        restCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate);
        Collection testCollection = collectionList.get(collectionList.size() - 1);
        assertThat(testCollection.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCollection.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCollection.getIsroot()).isEqualTo(UPDATED_ISROOT);
        assertThat(testCollection.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testCollection.getIsprivate()).isEqualTo(UPDATED_ISPRIVATE);
        assertThat(testCollection.getFilters()).isEqualTo(UPDATED_FILTERS);
    }

    @Test
    @Transactional
    void putNonExistingCollection() throws Exception {
        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();
        collection.setId(count.incrementAndGet());

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCollection() throws Exception {
        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();
        collection.setId(count.incrementAndGet());

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCollection() throws Exception {
        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();
        collection.setId(count.incrementAndGet());

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCollectionWithPatch() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();

        // Update the collection using partial update
        Collection partialUpdatedCollection = new Collection();
        partialUpdatedCollection.setId(collection.getId());

        partialUpdatedCollection.updatedat(UPDATED_UPDATEDAT);

        restCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollection))
            )
            .andExpect(status().isOk());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate);
        Collection testCollection = collectionList.get(collectionList.size() - 1);
        assertThat(testCollection.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testCollection.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCollection.getIsroot()).isEqualTo(DEFAULT_ISROOT);
        assertThat(testCollection.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testCollection.getIsprivate()).isEqualTo(DEFAULT_ISPRIVATE);
        assertThat(testCollection.getFilters()).isEqualTo(DEFAULT_FILTERS);
    }

    @Test
    @Transactional
    void fullUpdateCollectionWithPatch() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();

        // Update the collection using partial update
        Collection partialUpdatedCollection = new Collection();
        partialUpdatedCollection.setId(collection.getId());

        partialUpdatedCollection
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .isroot(UPDATED_ISROOT)
            .position(UPDATED_POSITION)
            .isprivate(UPDATED_ISPRIVATE)
            .filters(UPDATED_FILTERS);

        restCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollection))
            )
            .andExpect(status().isOk());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate);
        Collection testCollection = collectionList.get(collectionList.size() - 1);
        assertThat(testCollection.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCollection.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCollection.getIsroot()).isEqualTo(UPDATED_ISROOT);
        assertThat(testCollection.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testCollection.getIsprivate()).isEqualTo(UPDATED_ISPRIVATE);
        assertThat(testCollection.getFilters()).isEqualTo(UPDATED_FILTERS);
    }

    @Test
    @Transactional
    void patchNonExistingCollection() throws Exception {
        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();
        collection.setId(count.incrementAndGet());

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, collectionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCollection() throws Exception {
        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();
        collection.setId(count.incrementAndGet());

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCollection() throws Exception {
        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();
        collection.setId(count.incrementAndGet());

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(collectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        int databaseSizeBeforeDelete = collectionRepository.findAll().size();

        // Delete the collection
        restCollectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, collection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
