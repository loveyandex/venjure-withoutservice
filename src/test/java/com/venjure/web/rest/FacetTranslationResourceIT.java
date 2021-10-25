package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Facet;
import com.venjure.domain.FacetTranslation;
import com.venjure.repository.FacetTranslationRepository;
import com.venjure.service.criteria.FacetTranslationCriteria;
import com.venjure.service.dto.FacetTranslationDTO;
import com.venjure.service.mapper.FacetTranslationMapper;
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
 * Integration tests for the {@link FacetTranslationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacetTranslationResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LANGUAGECODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGECODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facet-translations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacetTranslationRepository facetTranslationRepository;

    @Autowired
    private FacetTranslationMapper facetTranslationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacetTranslationMockMvc;

    private FacetTranslation facetTranslation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacetTranslation createEntity(EntityManager em) {
        FacetTranslation facetTranslation = new FacetTranslation()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .languagecode(DEFAULT_LANGUAGECODE)
            .name(DEFAULT_NAME);
        return facetTranslation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacetTranslation createUpdatedEntity(EntityManager em) {
        FacetTranslation facetTranslation = new FacetTranslation()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);
        return facetTranslation;
    }

    @BeforeEach
    public void initTest() {
        facetTranslation = createEntity(em);
    }

    @Test
    @Transactional
    void createFacetTranslation() throws Exception {
        int databaseSizeBeforeCreate = facetTranslationRepository.findAll().size();
        // Create the FacetTranslation
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(facetTranslation);
        restFacetTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FacetTranslation in the database
        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeCreate + 1);
        FacetTranslation testFacetTranslation = facetTranslationList.get(facetTranslationList.size() - 1);
        assertThat(testFacetTranslation.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testFacetTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testFacetTranslation.getLanguagecode()).isEqualTo(DEFAULT_LANGUAGECODE);
        assertThat(testFacetTranslation.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createFacetTranslationWithExistingId() throws Exception {
        // Create the FacetTranslation with an existing ID
        facetTranslation.setId(1L);
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(facetTranslation);

        int databaseSizeBeforeCreate = facetTranslationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacetTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetTranslation in the database
        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetTranslationRepository.findAll().size();
        // set the field null
        facetTranslation.setCreatedat(null);

        // Create the FacetTranslation, which fails.
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(facetTranslation);

        restFacetTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetTranslationRepository.findAll().size();
        // set the field null
        facetTranslation.setUpdatedat(null);

        // Create the FacetTranslation, which fails.
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(facetTranslation);

        restFacetTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguagecodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetTranslationRepository.findAll().size();
        // set the field null
        facetTranslation.setLanguagecode(null);

        // Create the FacetTranslation, which fails.
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(facetTranslation);

        restFacetTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetTranslationRepository.findAll().size();
        // set the field null
        facetTranslation.setName(null);

        // Create the FacetTranslation, which fails.
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(facetTranslation);

        restFacetTranslationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacetTranslations() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList
        restFacetTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facetTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getFacetTranslation() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get the facetTranslation
        restFacetTranslationMockMvc
            .perform(get(ENTITY_API_URL_ID, facetTranslation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facetTranslation.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.languagecode").value(DEFAULT_LANGUAGECODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getFacetTranslationsByIdFiltering() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        Long id = facetTranslation.getId();

        defaultFacetTranslationShouldBeFound("id.equals=" + id);
        defaultFacetTranslationShouldNotBeFound("id.notEquals=" + id);

        defaultFacetTranslationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFacetTranslationShouldNotBeFound("id.greaterThan=" + id);

        defaultFacetTranslationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFacetTranslationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where createdat equals to DEFAULT_CREATEDAT
        defaultFacetTranslationShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the facetTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultFacetTranslationShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where createdat not equals to DEFAULT_CREATEDAT
        defaultFacetTranslationShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the facetTranslationList where createdat not equals to UPDATED_CREATEDAT
        defaultFacetTranslationShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultFacetTranslationShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the facetTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultFacetTranslationShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where createdat is not null
        defaultFacetTranslationShouldBeFound("createdat.specified=true");

        // Get all the facetTranslationList where createdat is null
        defaultFacetTranslationShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where updatedat equals to DEFAULT_UPDATEDAT
        defaultFacetTranslationShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the facetTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultFacetTranslationShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultFacetTranslationShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the facetTranslationList where updatedat not equals to UPDATED_UPDATEDAT
        defaultFacetTranslationShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultFacetTranslationShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the facetTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultFacetTranslationShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where updatedat is not null
        defaultFacetTranslationShouldBeFound("updatedat.specified=true");

        // Get all the facetTranslationList where updatedat is null
        defaultFacetTranslationShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByLanguagecodeIsEqualToSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where languagecode equals to DEFAULT_LANGUAGECODE
        defaultFacetTranslationShouldBeFound("languagecode.equals=" + DEFAULT_LANGUAGECODE);

        // Get all the facetTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultFacetTranslationShouldNotBeFound("languagecode.equals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByLanguagecodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where languagecode not equals to DEFAULT_LANGUAGECODE
        defaultFacetTranslationShouldNotBeFound("languagecode.notEquals=" + DEFAULT_LANGUAGECODE);

        // Get all the facetTranslationList where languagecode not equals to UPDATED_LANGUAGECODE
        defaultFacetTranslationShouldBeFound("languagecode.notEquals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByLanguagecodeIsInShouldWork() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where languagecode in DEFAULT_LANGUAGECODE or UPDATED_LANGUAGECODE
        defaultFacetTranslationShouldBeFound("languagecode.in=" + DEFAULT_LANGUAGECODE + "," + UPDATED_LANGUAGECODE);

        // Get all the facetTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultFacetTranslationShouldNotBeFound("languagecode.in=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByLanguagecodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where languagecode is not null
        defaultFacetTranslationShouldBeFound("languagecode.specified=true");

        // Get all the facetTranslationList where languagecode is null
        defaultFacetTranslationShouldNotBeFound("languagecode.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByLanguagecodeContainsSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where languagecode contains DEFAULT_LANGUAGECODE
        defaultFacetTranslationShouldBeFound("languagecode.contains=" + DEFAULT_LANGUAGECODE);

        // Get all the facetTranslationList where languagecode contains UPDATED_LANGUAGECODE
        defaultFacetTranslationShouldNotBeFound("languagecode.contains=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByLanguagecodeNotContainsSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where languagecode does not contain DEFAULT_LANGUAGECODE
        defaultFacetTranslationShouldNotBeFound("languagecode.doesNotContain=" + DEFAULT_LANGUAGECODE);

        // Get all the facetTranslationList where languagecode does not contain UPDATED_LANGUAGECODE
        defaultFacetTranslationShouldBeFound("languagecode.doesNotContain=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where name equals to DEFAULT_NAME
        defaultFacetTranslationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the facetTranslationList where name equals to UPDATED_NAME
        defaultFacetTranslationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where name not equals to DEFAULT_NAME
        defaultFacetTranslationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the facetTranslationList where name not equals to UPDATED_NAME
        defaultFacetTranslationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFacetTranslationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the facetTranslationList where name equals to UPDATED_NAME
        defaultFacetTranslationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where name is not null
        defaultFacetTranslationShouldBeFound("name.specified=true");

        // Get all the facetTranslationList where name is null
        defaultFacetTranslationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByNameContainsSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where name contains DEFAULT_NAME
        defaultFacetTranslationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the facetTranslationList where name contains UPDATED_NAME
        defaultFacetTranslationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        // Get all the facetTranslationList where name does not contain DEFAULT_NAME
        defaultFacetTranslationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the facetTranslationList where name does not contain UPDATED_NAME
        defaultFacetTranslationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFacetTranslationsByBaseIsEqualToSomething() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);
        Facet base = FacetResourceIT.createEntity(em);
        em.persist(base);
        em.flush();
        facetTranslation.setBase(base);
        facetTranslationRepository.saveAndFlush(facetTranslation);
        Long baseId = base.getId();

        // Get all the facetTranslationList where base equals to baseId
        defaultFacetTranslationShouldBeFound("baseId.equals=" + baseId);

        // Get all the facetTranslationList where base equals to (baseId + 1)
        defaultFacetTranslationShouldNotBeFound("baseId.equals=" + (baseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacetTranslationShouldBeFound(String filter) throws Exception {
        restFacetTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facetTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restFacetTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacetTranslationShouldNotBeFound(String filter) throws Exception {
        restFacetTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacetTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacetTranslation() throws Exception {
        // Get the facetTranslation
        restFacetTranslationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFacetTranslation() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        int databaseSizeBeforeUpdate = facetTranslationRepository.findAll().size();

        // Update the facetTranslation
        FacetTranslation updatedFacetTranslation = facetTranslationRepository.findById(facetTranslation.getId()).get();
        // Disconnect from session so that the updates on updatedFacetTranslation are not directly saved in db
        em.detach(updatedFacetTranslation);
        updatedFacetTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(updatedFacetTranslation);

        restFacetTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facetTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isOk());

        // Validate the FacetTranslation in the database
        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeUpdate);
        FacetTranslation testFacetTranslation = facetTranslationList.get(facetTranslationList.size() - 1);
        assertThat(testFacetTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFacetTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testFacetTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testFacetTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingFacetTranslation() throws Exception {
        int databaseSizeBeforeUpdate = facetTranslationRepository.findAll().size();
        facetTranslation.setId(count.incrementAndGet());

        // Create the FacetTranslation
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(facetTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacetTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facetTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetTranslation in the database
        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacetTranslation() throws Exception {
        int databaseSizeBeforeUpdate = facetTranslationRepository.findAll().size();
        facetTranslation.setId(count.incrementAndGet());

        // Create the FacetTranslation
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(facetTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetTranslation in the database
        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacetTranslation() throws Exception {
        int databaseSizeBeforeUpdate = facetTranslationRepository.findAll().size();
        facetTranslation.setId(count.incrementAndGet());

        // Create the FacetTranslation
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(facetTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetTranslationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacetTranslation in the database
        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacetTranslationWithPatch() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        int databaseSizeBeforeUpdate = facetTranslationRepository.findAll().size();

        // Update the facetTranslation using partial update
        FacetTranslation partialUpdatedFacetTranslation = new FacetTranslation();
        partialUpdatedFacetTranslation.setId(facetTranslation.getId());

        partialUpdatedFacetTranslation.languagecode(UPDATED_LANGUAGECODE).name(UPDATED_NAME);

        restFacetTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacetTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacetTranslation))
            )
            .andExpect(status().isOk());

        // Validate the FacetTranslation in the database
        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeUpdate);
        FacetTranslation testFacetTranslation = facetTranslationList.get(facetTranslationList.size() - 1);
        assertThat(testFacetTranslation.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testFacetTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testFacetTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testFacetTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateFacetTranslationWithPatch() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        int databaseSizeBeforeUpdate = facetTranslationRepository.findAll().size();

        // Update the facetTranslation using partial update
        FacetTranslation partialUpdatedFacetTranslation = new FacetTranslation();
        partialUpdatedFacetTranslation.setId(facetTranslation.getId());

        partialUpdatedFacetTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);

        restFacetTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacetTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacetTranslation))
            )
            .andExpect(status().isOk());

        // Validate the FacetTranslation in the database
        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeUpdate);
        FacetTranslation testFacetTranslation = facetTranslationList.get(facetTranslationList.size() - 1);
        assertThat(testFacetTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFacetTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testFacetTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testFacetTranslation.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingFacetTranslation() throws Exception {
        int databaseSizeBeforeUpdate = facetTranslationRepository.findAll().size();
        facetTranslation.setId(count.incrementAndGet());

        // Create the FacetTranslation
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(facetTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacetTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facetTranslationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetTranslation in the database
        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacetTranslation() throws Exception {
        int databaseSizeBeforeUpdate = facetTranslationRepository.findAll().size();
        facetTranslation.setId(count.incrementAndGet());

        // Create the FacetTranslation
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(facetTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetTranslation in the database
        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacetTranslation() throws Exception {
        int databaseSizeBeforeUpdate = facetTranslationRepository.findAll().size();
        facetTranslation.setId(count.incrementAndGet());

        // Create the FacetTranslation
        FacetTranslationDTO facetTranslationDTO = facetTranslationMapper.toDto(facetTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facetTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacetTranslation in the database
        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacetTranslation() throws Exception {
        // Initialize the database
        facetTranslationRepository.saveAndFlush(facetTranslation);

        int databaseSizeBeforeDelete = facetTranslationRepository.findAll().size();

        // Delete the facetTranslation
        restFacetTranslationMockMvc
            .perform(delete(ENTITY_API_URL_ID, facetTranslation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FacetTranslation> facetTranslationList = facetTranslationRepository.findAll();
        assertThat(facetTranslationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
