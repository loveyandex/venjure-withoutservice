package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.FacetValue;
import com.venjure.domain.FacetValueTranslation;
import com.venjure.repository.FacetValueTranslationRepository;
import com.venjure.service.criteria.FacetValueTranslationCriteria;
import com.venjure.service.dto.FacetValueTranslationDTO;
import com.venjure.service.mapper.FacetValueTranslationMapper;
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
 * Integration tests for the {@link FacetValueTranslationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacetValueTranslationResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LANGUAGECODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGECODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facet-value-translations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacetValueTranslationRepository facetValueTranslationRepository;

    @Autowired
    private FacetValueTranslationMapper facetValueTranslationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacetValueTranslationMockMvc;

    private FacetValueTranslation facetValueTranslation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacetValueTranslation createEntity(EntityManager em) {
        FacetValueTranslation facetValueTranslation = new FacetValueTranslation()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .languagecode(DEFAULT_LANGUAGECODE)
            .name(DEFAULT_NAME);
        return facetValueTranslation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacetValueTranslation createUpdatedEntity(EntityManager em) {
        FacetValueTranslation facetValueTranslation = new FacetValueTranslation()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);
        return facetValueTranslation;
    }

    @BeforeEach
    public void initTest() {
        facetValueTranslation = createEntity(em);
    }

    @Test
    @Transactional
    void createFacetValueTranslation() throws Exception {
        int databaseSizeBeforeCreate = facetValueTranslationRepository.findAll().size();
        // Create the FacetValueTranslation
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(facetValueTranslation);
        restFacetValueTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FacetValueTranslation in the database
        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeCreate + 1);
        FacetValueTranslation testFacetValueTranslation = facetValueTranslationList.get(facetValueTranslationList.size() - 1);
        assertThat(testFacetValueTranslation.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testFacetValueTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testFacetValueTranslation.getLanguagecode()).isEqualTo(DEFAULT_LANGUAGECODE);
        assertThat(testFacetValueTranslation.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createFacetValueTranslationWithExistingId() throws Exception {
        // Create the FacetValueTranslation with an existing ID
        facetValueTranslation.setId(1L);
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(facetValueTranslation);

        int databaseSizeBeforeCreate = facetValueTranslationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacetValueTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetValueTranslation in the database
        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetValueTranslationRepository.findAll().size();
        // set the field null
        facetValueTranslation.setCreatedat(null);

        // Create the FacetValueTranslation, which fails.
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(facetValueTranslation);

        restFacetValueTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetValueTranslationRepository.findAll().size();
        // set the field null
        facetValueTranslation.setUpdatedat(null);

        // Create the FacetValueTranslation, which fails.
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(facetValueTranslation);

        restFacetValueTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguagecodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetValueTranslationRepository.findAll().size();
        // set the field null
        facetValueTranslation.setLanguagecode(null);

        // Create the FacetValueTranslation, which fails.
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(facetValueTranslation);

        restFacetValueTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetValueTranslationRepository.findAll().size();
        // set the field null
        facetValueTranslation.setName(null);

        // Create the FacetValueTranslation, which fails.
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(facetValueTranslation);

        restFacetValueTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslations() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList
        restFacetValueTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facetValueTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getFacetValueTranslation() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get the facetValueTranslation
        restFacetValueTranslationMockMvc
            .perform(get(ENTITY_API_URL_ID, facetValueTranslation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facetValueTranslation.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.languagecode").value(DEFAULT_LANGUAGECODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getFacetValueTranslationsByIdFiltering() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        Long id = facetValueTranslation.getId();

        defaultFacetValueTranslationShouldBeFound("id.equals=" + id);
        defaultFacetValueTranslationShouldNotBeFound("id.notEquals=" + id);

        defaultFacetValueTranslationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFacetValueTranslationShouldNotBeFound("id.greaterThan=" + id);

        defaultFacetValueTranslationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFacetValueTranslationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where createdat equals to DEFAULT_CREATEDAT
        defaultFacetValueTranslationShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the facetValueTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultFacetValueTranslationShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where createdat not equals to DEFAULT_CREATEDAT
        defaultFacetValueTranslationShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the facetValueTranslationList where createdat not equals to UPDATED_CREATEDAT
        defaultFacetValueTranslationShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultFacetValueTranslationShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the facetValueTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultFacetValueTranslationShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where createdat is not null
        defaultFacetValueTranslationShouldBeFound("createdat.specified=true");

        // Get all the facetValueTranslationList where createdat is null
        defaultFacetValueTranslationShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where updatedat equals to DEFAULT_UPDATEDAT
        defaultFacetValueTranslationShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the facetValueTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultFacetValueTranslationShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultFacetValueTranslationShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the facetValueTranslationList where updatedat not equals to UPDATED_UPDATEDAT
        defaultFacetValueTranslationShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultFacetValueTranslationShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the facetValueTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultFacetValueTranslationShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where updatedat is not null
        defaultFacetValueTranslationShouldBeFound("updatedat.specified=true");

        // Get all the facetValueTranslationList where updatedat is null
        defaultFacetValueTranslationShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByLanguagecodeIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where languagecode equals to DEFAULT_LANGUAGECODE
        defaultFacetValueTranslationShouldBeFound("languagecode.equals=" + DEFAULT_LANGUAGECODE);

        // Get all the facetValueTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultFacetValueTranslationShouldNotBeFound("languagecode.equals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByLanguagecodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where languagecode not equals to DEFAULT_LANGUAGECODE
        defaultFacetValueTranslationShouldNotBeFound("languagecode.notEquals=" + DEFAULT_LANGUAGECODE);

        // Get all the facetValueTranslationList where languagecode not equals to UPDATED_LANGUAGECODE
        defaultFacetValueTranslationShouldBeFound("languagecode.notEquals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByLanguagecodeIsInShouldWork() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where languagecode in DEFAULT_LANGUAGECODE or UPDATED_LANGUAGECODE
        defaultFacetValueTranslationShouldBeFound("languagecode.in=" + DEFAULT_LANGUAGECODE + "," + UPDATED_LANGUAGECODE);

        // Get all the facetValueTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultFacetValueTranslationShouldNotBeFound("languagecode.in=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByLanguagecodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where languagecode is not null
        defaultFacetValueTranslationShouldBeFound("languagecode.specified=true");

        // Get all the facetValueTranslationList where languagecode is null
        defaultFacetValueTranslationShouldNotBeFound("languagecode.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByLanguagecodeContainsSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where languagecode contains DEFAULT_LANGUAGECODE
        defaultFacetValueTranslationShouldBeFound("languagecode.contains=" + DEFAULT_LANGUAGECODE);

        // Get all the facetValueTranslationList where languagecode contains UPDATED_LANGUAGECODE
        defaultFacetValueTranslationShouldNotBeFound("languagecode.contains=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByLanguagecodeNotContainsSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where languagecode does not contain DEFAULT_LANGUAGECODE
        defaultFacetValueTranslationShouldNotBeFound("languagecode.doesNotContain=" + DEFAULT_LANGUAGECODE);

        // Get all the facetValueTranslationList where languagecode does not contain UPDATED_LANGUAGECODE
        defaultFacetValueTranslationShouldBeFound("languagecode.doesNotContain=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where name equals to DEFAULT_NAME
        defaultFacetValueTranslationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the facetValueTranslationList where name equals to UPDATED_NAME
        defaultFacetValueTranslationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where name not equals to DEFAULT_NAME
        defaultFacetValueTranslationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the facetValueTranslationList where name not equals to UPDATED_NAME
        defaultFacetValueTranslationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFacetValueTranslationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the facetValueTranslationList where name equals to UPDATED_NAME
        defaultFacetValueTranslationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where name is not null
        defaultFacetValueTranslationShouldBeFound("name.specified=true");

        // Get all the facetValueTranslationList where name is null
        defaultFacetValueTranslationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByNameContainsSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where name contains DEFAULT_NAME
        defaultFacetValueTranslationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the facetValueTranslationList where name contains UPDATED_NAME
        defaultFacetValueTranslationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        // Get all the facetValueTranslationList where name does not contain DEFAULT_NAME
        defaultFacetValueTranslationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the facetValueTranslationList where name does not contain UPDATED_NAME
        defaultFacetValueTranslationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacetValueTranslationsByBaseIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);
        FacetValue base = FacetValueResourceIT.createEntity(em);
        em.persist(base);
        em.flush();
        facetValueTranslation.setBase(base);
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);
        Long baseId = base.getId();

        // Get all the facetValueTranslationList where base equals to baseId
        defaultFacetValueTranslationShouldBeFound("baseId.equals=" + baseId);

        // Get all the facetValueTranslationList where base equals to (baseId + 1)
        defaultFacetValueTranslationShouldNotBeFound("baseId.equals=" + (baseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacetValueTranslationShouldBeFound(String filter) throws Exception {
        restFacetValueTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facetValueTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restFacetValueTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacetValueTranslationShouldNotBeFound(String filter) throws Exception {
        restFacetValueTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacetValueTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacetValueTranslation() throws Exception {
        // Get the facetValueTranslation
        restFacetValueTranslationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFacetValueTranslation() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        int databaseSizeBeforeUpdate = facetValueTranslationRepository.findAll().size();

        // Update the facetValueTranslation
        FacetValueTranslation updatedFacetValueTranslation = facetValueTranslationRepository.findById(facetValueTranslation.getId()).get();
        // Disconnect from session so that the updates on updatedFacetValueTranslation are not directly saved in db
        em.detach(updatedFacetValueTranslation);
        updatedFacetValueTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(updatedFacetValueTranslation);

        restFacetValueTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facetValueTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isOk());

        // Validate the FacetValueTranslation in the database
        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeUpdate);
        FacetValueTranslation testFacetValueTranslation = facetValueTranslationList.get(facetValueTranslationList.size() - 1);
        assertThat(testFacetValueTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFacetValueTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testFacetValueTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testFacetValueTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingFacetValueTranslation() throws Exception {
        int databaseSizeBeforeUpdate = facetValueTranslationRepository.findAll().size();
        facetValueTranslation.setId(count.incrementAndGet());

        // Create the FacetValueTranslation
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(facetValueTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacetValueTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facetValueTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetValueTranslation in the database
        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacetValueTranslation() throws Exception {
        int databaseSizeBeforeUpdate = facetValueTranslationRepository.findAll().size();
        facetValueTranslation.setId(count.incrementAndGet());

        // Create the FacetValueTranslation
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(facetValueTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetValueTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetValueTranslation in the database
        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacetValueTranslation() throws Exception {
        int databaseSizeBeforeUpdate = facetValueTranslationRepository.findAll().size();
        facetValueTranslation.setId(count.incrementAndGet());

        // Create the FacetValueTranslation
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(facetValueTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetValueTranslationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacetValueTranslation in the database
        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacetValueTranslationWithPatch() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        int databaseSizeBeforeUpdate = facetValueTranslationRepository.findAll().size();

        // Update the facetValueTranslation using partial update
        FacetValueTranslation partialUpdatedFacetValueTranslation = new FacetValueTranslation();
        partialUpdatedFacetValueTranslation.setId(facetValueTranslation.getId());

        partialUpdatedFacetValueTranslation.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT);

        restFacetValueTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacetValueTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacetValueTranslation))
            )
            .andExpect(status().isOk());

        // Validate the FacetValueTranslation in the database
        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeUpdate);
        FacetValueTranslation testFacetValueTranslation = facetValueTranslationList.get(facetValueTranslationList.size() - 1);
        assertThat(testFacetValueTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFacetValueTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testFacetValueTranslation.getLanguagecode()).isEqualTo(DEFAULT_LANGUAGECODE);
        assertThat(testFacetValueTranslation.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateFacetValueTranslationWithPatch() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        int databaseSizeBeforeUpdate = facetValueTranslationRepository.findAll().size();

        // Update the facetValueTranslation using partial update
        FacetValueTranslation partialUpdatedFacetValueTranslation = new FacetValueTranslation();
        partialUpdatedFacetValueTranslation.setId(facetValueTranslation.getId());

        partialUpdatedFacetValueTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);

        restFacetValueTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacetValueTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacetValueTranslation))
            )
            .andExpect(status().isOk());

        // Validate the FacetValueTranslation in the database
        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeUpdate);
        FacetValueTranslation testFacetValueTranslation = facetValueTranslationList.get(facetValueTranslationList.size() - 1);
        assertThat(testFacetValueTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFacetValueTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testFacetValueTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testFacetValueTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingFacetValueTranslation() throws Exception {
        int databaseSizeBeforeUpdate = facetValueTranslationRepository.findAll().size();
        facetValueTranslation.setId(count.incrementAndGet());

        // Create the FacetValueTranslation
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(facetValueTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacetValueTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facetValueTranslationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetValueTranslation in the database
        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacetValueTranslation() throws Exception {
        int databaseSizeBeforeUpdate = facetValueTranslationRepository.findAll().size();
        facetValueTranslation.setId(count.incrementAndGet());

        // Create the FacetValueTranslation
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(facetValueTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetValueTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetValueTranslation in the database
        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacetValueTranslation() throws Exception {
        int databaseSizeBeforeUpdate = facetValueTranslationRepository.findAll().size();
        facetValueTranslation.setId(count.incrementAndGet());

        // Create the FacetValueTranslation
        FacetValueTranslationDTO facetValueTranslationDTO = facetValueTranslationMapper.toDto(facetValueTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetValueTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facetValueTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacetValueTranslation in the database
        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacetValueTranslation() throws Exception {
        // Initialize the database
        facetValueTranslationRepository.saveAndFlush(facetValueTranslation);

        int databaseSizeBeforeDelete = facetValueTranslationRepository.findAll().size();

        // Delete the facetValueTranslation
        restFacetValueTranslationMockMvc
            .perform(delete(ENTITY_API_URL_ID, facetValueTranslation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FacetValueTranslation> facetValueTranslationList = facetValueTranslationRepository.findAll();
        assertThat(facetValueTranslationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
