package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.ShippingMethod;
import com.venjure.domain.ShippingMethodTranslation;
import com.venjure.repository.ShippingMethodTranslationRepository;
import com.venjure.service.criteria.ShippingMethodTranslationCriteria;
import com.venjure.service.dto.ShippingMethodTranslationDTO;
import com.venjure.service.mapper.ShippingMethodTranslationMapper;
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
 * Integration tests for the {@link ShippingMethodTranslationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShippingMethodTranslationResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LANGUAGECODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGECODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shipping-method-translations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShippingMethodTranslationRepository shippingMethodTranslationRepository;

    @Autowired
    private ShippingMethodTranslationMapper shippingMethodTranslationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShippingMethodTranslationMockMvc;

    private ShippingMethodTranslation shippingMethodTranslation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShippingMethodTranslation createEntity(EntityManager em) {
        ShippingMethodTranslation shippingMethodTranslation = new ShippingMethodTranslation()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .languagecode(DEFAULT_LANGUAGECODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return shippingMethodTranslation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShippingMethodTranslation createUpdatedEntity(EntityManager em) {
        ShippingMethodTranslation shippingMethodTranslation = new ShippingMethodTranslation()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return shippingMethodTranslation;
    }

    @BeforeEach
    public void initTest() {
        shippingMethodTranslation = createEntity(em);
    }

    @Test
    @Transactional
    void createShippingMethodTranslation() throws Exception {
        int databaseSizeBeforeCreate = shippingMethodTranslationRepository.findAll().size();
        // Create the ShippingMethodTranslation
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);
        restShippingMethodTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ShippingMethodTranslation in the database
        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeCreate + 1);
        ShippingMethodTranslation testShippingMethodTranslation = shippingMethodTranslationList.get(
            shippingMethodTranslationList.size() - 1
        );
        assertThat(testShippingMethodTranslation.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testShippingMethodTranslation.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testShippingMethodTranslation.getLanguagecode()).isEqualTo(DEFAULT_LANGUAGECODE);
        assertThat(testShippingMethodTranslation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShippingMethodTranslation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createShippingMethodTranslationWithExistingId() throws Exception {
        // Create the ShippingMethodTranslation with an existing ID
        shippingMethodTranslation.setId(1L);
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);

        int databaseSizeBeforeCreate = shippingMethodTranslationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShippingMethodTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingMethodTranslation in the database
        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingMethodTranslationRepository.findAll().size();
        // set the field null
        shippingMethodTranslation.setCreatedat(null);

        // Create the ShippingMethodTranslation, which fails.
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);

        restShippingMethodTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingMethodTranslationRepository.findAll().size();
        // set the field null
        shippingMethodTranslation.setUpdatedat(null);

        // Create the ShippingMethodTranslation, which fails.
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);

        restShippingMethodTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguagecodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingMethodTranslationRepository.findAll().size();
        // set the field null
        shippingMethodTranslation.setLanguagecode(null);

        // Create the ShippingMethodTranslation, which fails.
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);

        restShippingMethodTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingMethodTranslationRepository.findAll().size();
        // set the field null
        shippingMethodTranslation.setName(null);

        // Create the ShippingMethodTranslation, which fails.
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);

        restShippingMethodTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingMethodTranslationRepository.findAll().size();
        // set the field null
        shippingMethodTranslation.setDescription(null);

        // Create the ShippingMethodTranslation, which fails.
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);

        restShippingMethodTranslationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslations() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList
        restShippingMethodTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shippingMethodTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getShippingMethodTranslation() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get the shippingMethodTranslation
        restShippingMethodTranslationMockMvc
            .perform(get(ENTITY_API_URL_ID, shippingMethodTranslation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shippingMethodTranslation.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.languagecode").value(DEFAULT_LANGUAGECODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getShippingMethodTranslationsByIdFiltering() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        Long id = shippingMethodTranslation.getId();

        defaultShippingMethodTranslationShouldBeFound("id.equals=" + id);
        defaultShippingMethodTranslationShouldNotBeFound("id.notEquals=" + id);

        defaultShippingMethodTranslationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultShippingMethodTranslationShouldNotBeFound("id.greaterThan=" + id);

        defaultShippingMethodTranslationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultShippingMethodTranslationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where createdat equals to DEFAULT_CREATEDAT
        defaultShippingMethodTranslationShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the shippingMethodTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultShippingMethodTranslationShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where createdat not equals to DEFAULT_CREATEDAT
        defaultShippingMethodTranslationShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the shippingMethodTranslationList where createdat not equals to UPDATED_CREATEDAT
        defaultShippingMethodTranslationShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultShippingMethodTranslationShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the shippingMethodTranslationList where createdat equals to UPDATED_CREATEDAT
        defaultShippingMethodTranslationShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where createdat is not null
        defaultShippingMethodTranslationShouldBeFound("createdat.specified=true");

        // Get all the shippingMethodTranslationList where createdat is null
        defaultShippingMethodTranslationShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where updatedat equals to DEFAULT_UPDATEDAT
        defaultShippingMethodTranslationShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the shippingMethodTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultShippingMethodTranslationShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultShippingMethodTranslationShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the shippingMethodTranslationList where updatedat not equals to UPDATED_UPDATEDAT
        defaultShippingMethodTranslationShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultShippingMethodTranslationShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the shippingMethodTranslationList where updatedat equals to UPDATED_UPDATEDAT
        defaultShippingMethodTranslationShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where updatedat is not null
        defaultShippingMethodTranslationShouldBeFound("updatedat.specified=true");

        // Get all the shippingMethodTranslationList where updatedat is null
        defaultShippingMethodTranslationShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByLanguagecodeIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where languagecode equals to DEFAULT_LANGUAGECODE
        defaultShippingMethodTranslationShouldBeFound("languagecode.equals=" + DEFAULT_LANGUAGECODE);

        // Get all the shippingMethodTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultShippingMethodTranslationShouldNotBeFound("languagecode.equals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByLanguagecodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where languagecode not equals to DEFAULT_LANGUAGECODE
        defaultShippingMethodTranslationShouldNotBeFound("languagecode.notEquals=" + DEFAULT_LANGUAGECODE);

        // Get all the shippingMethodTranslationList where languagecode not equals to UPDATED_LANGUAGECODE
        defaultShippingMethodTranslationShouldBeFound("languagecode.notEquals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByLanguagecodeIsInShouldWork() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where languagecode in DEFAULT_LANGUAGECODE or UPDATED_LANGUAGECODE
        defaultShippingMethodTranslationShouldBeFound("languagecode.in=" + DEFAULT_LANGUAGECODE + "," + UPDATED_LANGUAGECODE);

        // Get all the shippingMethodTranslationList where languagecode equals to UPDATED_LANGUAGECODE
        defaultShippingMethodTranslationShouldNotBeFound("languagecode.in=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByLanguagecodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where languagecode is not null
        defaultShippingMethodTranslationShouldBeFound("languagecode.specified=true");

        // Get all the shippingMethodTranslationList where languagecode is null
        defaultShippingMethodTranslationShouldNotBeFound("languagecode.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByLanguagecodeContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where languagecode contains DEFAULT_LANGUAGECODE
        defaultShippingMethodTranslationShouldBeFound("languagecode.contains=" + DEFAULT_LANGUAGECODE);

        // Get all the shippingMethodTranslationList where languagecode contains UPDATED_LANGUAGECODE
        defaultShippingMethodTranslationShouldNotBeFound("languagecode.contains=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByLanguagecodeNotContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where languagecode does not contain DEFAULT_LANGUAGECODE
        defaultShippingMethodTranslationShouldNotBeFound("languagecode.doesNotContain=" + DEFAULT_LANGUAGECODE);

        // Get all the shippingMethodTranslationList where languagecode does not contain UPDATED_LANGUAGECODE
        defaultShippingMethodTranslationShouldBeFound("languagecode.doesNotContain=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where name equals to DEFAULT_NAME
        defaultShippingMethodTranslationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the shippingMethodTranslationList where name equals to UPDATED_NAME
        defaultShippingMethodTranslationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where name not equals to DEFAULT_NAME
        defaultShippingMethodTranslationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the shippingMethodTranslationList where name not equals to UPDATED_NAME
        defaultShippingMethodTranslationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultShippingMethodTranslationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the shippingMethodTranslationList where name equals to UPDATED_NAME
        defaultShippingMethodTranslationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where name is not null
        defaultShippingMethodTranslationShouldBeFound("name.specified=true");

        // Get all the shippingMethodTranslationList where name is null
        defaultShippingMethodTranslationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByNameContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where name contains DEFAULT_NAME
        defaultShippingMethodTranslationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the shippingMethodTranslationList where name contains UPDATED_NAME
        defaultShippingMethodTranslationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where name does not contain DEFAULT_NAME
        defaultShippingMethodTranslationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the shippingMethodTranslationList where name does not contain UPDATED_NAME
        defaultShippingMethodTranslationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where description equals to DEFAULT_DESCRIPTION
        defaultShippingMethodTranslationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the shippingMethodTranslationList where description equals to UPDATED_DESCRIPTION
        defaultShippingMethodTranslationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where description not equals to DEFAULT_DESCRIPTION
        defaultShippingMethodTranslationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the shippingMethodTranslationList where description not equals to UPDATED_DESCRIPTION
        defaultShippingMethodTranslationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultShippingMethodTranslationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the shippingMethodTranslationList where description equals to UPDATED_DESCRIPTION
        defaultShippingMethodTranslationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where description is not null
        defaultShippingMethodTranslationShouldBeFound("description.specified=true");

        // Get all the shippingMethodTranslationList where description is null
        defaultShippingMethodTranslationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where description contains DEFAULT_DESCRIPTION
        defaultShippingMethodTranslationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the shippingMethodTranslationList where description contains UPDATED_DESCRIPTION
        defaultShippingMethodTranslationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        // Get all the shippingMethodTranslationList where description does not contain DEFAULT_DESCRIPTION
        defaultShippingMethodTranslationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the shippingMethodTranslationList where description does not contain UPDATED_DESCRIPTION
        defaultShippingMethodTranslationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllShippingMethodTranslationsByBaseIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);
        ShippingMethod base = ShippingMethodResourceIT.createEntity(em);
        em.persist(base);
        em.flush();
        shippingMethodTranslation.setBase(base);
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);
        Long baseId = base.getId();

        // Get all the shippingMethodTranslationList where base equals to baseId
        defaultShippingMethodTranslationShouldBeFound("baseId.equals=" + baseId);

        // Get all the shippingMethodTranslationList where base equals to (baseId + 1)
        defaultShippingMethodTranslationShouldNotBeFound("baseId.equals=" + (baseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShippingMethodTranslationShouldBeFound(String filter) throws Exception {
        restShippingMethodTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shippingMethodTranslation.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restShippingMethodTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShippingMethodTranslationShouldNotBeFound(String filter) throws Exception {
        restShippingMethodTranslationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShippingMethodTranslationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShippingMethodTranslation() throws Exception {
        // Get the shippingMethodTranslation
        restShippingMethodTranslationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShippingMethodTranslation() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        int databaseSizeBeforeUpdate = shippingMethodTranslationRepository.findAll().size();

        // Update the shippingMethodTranslation
        ShippingMethodTranslation updatedShippingMethodTranslation = shippingMethodTranslationRepository
            .findById(shippingMethodTranslation.getId())
            .get();
        // Disconnect from session so that the updates on updatedShippingMethodTranslation are not directly saved in db
        em.detach(updatedShippingMethodTranslation);
        updatedShippingMethodTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(updatedShippingMethodTranslation);

        restShippingMethodTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shippingMethodTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShippingMethodTranslation in the database
        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeUpdate);
        ShippingMethodTranslation testShippingMethodTranslation = shippingMethodTranslationList.get(
            shippingMethodTranslationList.size() - 1
        );
        assertThat(testShippingMethodTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testShippingMethodTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testShippingMethodTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testShippingMethodTranslation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShippingMethodTranslation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingShippingMethodTranslation() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodTranslationRepository.findAll().size();
        shippingMethodTranslation.setId(count.incrementAndGet());

        // Create the ShippingMethodTranslation
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingMethodTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shippingMethodTranslationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingMethodTranslation in the database
        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShippingMethodTranslation() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodTranslationRepository.findAll().size();
        shippingMethodTranslation.setId(count.incrementAndGet());

        // Create the ShippingMethodTranslation
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMethodTranslationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingMethodTranslation in the database
        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShippingMethodTranslation() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodTranslationRepository.findAll().size();
        shippingMethodTranslation.setId(count.incrementAndGet());

        // Create the ShippingMethodTranslation
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMethodTranslationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShippingMethodTranslation in the database
        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShippingMethodTranslationWithPatch() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        int databaseSizeBeforeUpdate = shippingMethodTranslationRepository.findAll().size();

        // Update the shippingMethodTranslation using partial update
        ShippingMethodTranslation partialUpdatedShippingMethodTranslation = new ShippingMethodTranslation();
        partialUpdatedShippingMethodTranslation.setId(shippingMethodTranslation.getId());

        partialUpdatedShippingMethodTranslation
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .description(UPDATED_DESCRIPTION);

        restShippingMethodTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShippingMethodTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShippingMethodTranslation))
            )
            .andExpect(status().isOk());

        // Validate the ShippingMethodTranslation in the database
        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeUpdate);
        ShippingMethodTranslation testShippingMethodTranslation = shippingMethodTranslationList.get(
            shippingMethodTranslationList.size() - 1
        );
        assertThat(testShippingMethodTranslation.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testShippingMethodTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testShippingMethodTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testShippingMethodTranslation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShippingMethodTranslation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateShippingMethodTranslationWithPatch() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        int databaseSizeBeforeUpdate = shippingMethodTranslationRepository.findAll().size();

        // Update the shippingMethodTranslation using partial update
        ShippingMethodTranslation partialUpdatedShippingMethodTranslation = new ShippingMethodTranslation();
        partialUpdatedShippingMethodTranslation.setId(shippingMethodTranslation.getId());

        partialUpdatedShippingMethodTranslation
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restShippingMethodTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShippingMethodTranslation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShippingMethodTranslation))
            )
            .andExpect(status().isOk());

        // Validate the ShippingMethodTranslation in the database
        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeUpdate);
        ShippingMethodTranslation testShippingMethodTranslation = shippingMethodTranslationList.get(
            shippingMethodTranslationList.size() - 1
        );
        assertThat(testShippingMethodTranslation.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testShippingMethodTranslation.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testShippingMethodTranslation.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testShippingMethodTranslation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShippingMethodTranslation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingShippingMethodTranslation() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodTranslationRepository.findAll().size();
        shippingMethodTranslation.setId(count.incrementAndGet());

        // Create the ShippingMethodTranslation
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingMethodTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shippingMethodTranslationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingMethodTranslation in the database
        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShippingMethodTranslation() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodTranslationRepository.findAll().size();
        shippingMethodTranslation.setId(count.incrementAndGet());

        // Create the ShippingMethodTranslation
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMethodTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingMethodTranslation in the database
        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShippingMethodTranslation() throws Exception {
        int databaseSizeBeforeUpdate = shippingMethodTranslationRepository.findAll().size();
        shippingMethodTranslation.setId(count.incrementAndGet());

        // Create the ShippingMethodTranslation
        ShippingMethodTranslationDTO shippingMethodTranslationDTO = shippingMethodTranslationMapper.toDto(shippingMethodTranslation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingMethodTranslationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shippingMethodTranslationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShippingMethodTranslation in the database
        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShippingMethodTranslation() throws Exception {
        // Initialize the database
        shippingMethodTranslationRepository.saveAndFlush(shippingMethodTranslation);

        int databaseSizeBeforeDelete = shippingMethodTranslationRepository.findAll().size();

        // Delete the shippingMethodTranslation
        restShippingMethodTranslationMockMvc
            .perform(delete(ENTITY_API_URL_ID, shippingMethodTranslation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShippingMethodTranslation> shippingMethodTranslationList = shippingMethodTranslationRepository.findAll();
        assertThat(shippingMethodTranslationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
