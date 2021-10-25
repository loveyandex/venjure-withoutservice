package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Channel;
import com.venjure.domain.Jorder;
import com.venjure.domain.Promotion;
import com.venjure.repository.PromotionRepository;
import com.venjure.service.criteria.PromotionCriteria;
import com.venjure.service.dto.PromotionDTO;
import com.venjure.service.mapper.PromotionMapper;
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
 * Integration tests for the {@link PromotionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PromotionResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_STARTSAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTSAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ENDSAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENDSAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COUPONCODE = "AAAAAAAAAA";
    private static final String UPDATED_COUPONCODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PERCUSTOMERUSAGELIMIT = 1;
    private static final Integer UPDATED_PERCUSTOMERUSAGELIMIT = 2;
    private static final Integer SMALLER_PERCUSTOMERUSAGELIMIT = 1 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String DEFAULT_CONDITIONS = "AAAAAAAAAA";
    private static final String UPDATED_CONDITIONS = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIONS = "AAAAAAAAAA";
    private static final String UPDATED_ACTIONS = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITYSCORE = 1;
    private static final Integer UPDATED_PRIORITYSCORE = 2;
    private static final Integer SMALLER_PRIORITYSCORE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/promotions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionMapper promotionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPromotionMockMvc;

    private Promotion promotion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promotion createEntity(EntityManager em) {
        Promotion promotion = new Promotion()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .deletedat(DEFAULT_DELETEDAT)
            .startsat(DEFAULT_STARTSAT)
            .endsat(DEFAULT_ENDSAT)
            .couponcode(DEFAULT_COUPONCODE)
            .percustomerusagelimit(DEFAULT_PERCUSTOMERUSAGELIMIT)
            .name(DEFAULT_NAME)
            .enabled(DEFAULT_ENABLED)
            .conditions(DEFAULT_CONDITIONS)
            .actions(DEFAULT_ACTIONS)
            .priorityscore(DEFAULT_PRIORITYSCORE);
        return promotion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promotion createUpdatedEntity(EntityManager em) {
        Promotion promotion = new Promotion()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .startsat(UPDATED_STARTSAT)
            .endsat(UPDATED_ENDSAT)
            .couponcode(UPDATED_COUPONCODE)
            .percustomerusagelimit(UPDATED_PERCUSTOMERUSAGELIMIT)
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED)
            .conditions(UPDATED_CONDITIONS)
            .actions(UPDATED_ACTIONS)
            .priorityscore(UPDATED_PRIORITYSCORE);
        return promotion;
    }

    @BeforeEach
    public void initTest() {
        promotion = createEntity(em);
    }

    @Test
    @Transactional
    void createPromotion() throws Exception {
        int databaseSizeBeforeCreate = promotionRepository.findAll().size();
        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);
        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isCreated());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeCreate + 1);
        Promotion testPromotion = promotionList.get(promotionList.size() - 1);
        assertThat(testPromotion.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testPromotion.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testPromotion.getDeletedat()).isEqualTo(DEFAULT_DELETEDAT);
        assertThat(testPromotion.getStartsat()).isEqualTo(DEFAULT_STARTSAT);
        assertThat(testPromotion.getEndsat()).isEqualTo(DEFAULT_ENDSAT);
        assertThat(testPromotion.getCouponcode()).isEqualTo(DEFAULT_COUPONCODE);
        assertThat(testPromotion.getPercustomerusagelimit()).isEqualTo(DEFAULT_PERCUSTOMERUSAGELIMIT);
        assertThat(testPromotion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPromotion.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testPromotion.getConditions()).isEqualTo(DEFAULT_CONDITIONS);
        assertThat(testPromotion.getActions()).isEqualTo(DEFAULT_ACTIONS);
        assertThat(testPromotion.getPriorityscore()).isEqualTo(DEFAULT_PRIORITYSCORE);
    }

    @Test
    @Transactional
    void createPromotionWithExistingId() throws Exception {
        // Create the Promotion with an existing ID
        promotion.setId(1L);
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        int databaseSizeBeforeCreate = promotionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().size();
        // set the field null
        promotion.setCreatedat(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().size();
        // set the field null
        promotion.setUpdatedat(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().size();
        // set the field null
        promotion.setName(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().size();
        // set the field null
        promotion.setEnabled(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConditionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().size();
        // set the field null
        promotion.setConditions(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().size();
        // set the field null
        promotion.setActions(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityscoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().size();
        // set the field null
        promotion.setPriorityscore(null);

        // Create the Promotion, which fails.
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        restPromotionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isBadRequest());

        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPromotions() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList
        restPromotionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotion.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].startsat").value(hasItem(DEFAULT_STARTSAT.toString())))
            .andExpect(jsonPath("$.[*].endsat").value(hasItem(DEFAULT_ENDSAT.toString())))
            .andExpect(jsonPath("$.[*].couponcode").value(hasItem(DEFAULT_COUPONCODE)))
            .andExpect(jsonPath("$.[*].percustomerusagelimit").value(hasItem(DEFAULT_PERCUSTOMERUSAGELIMIT)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS)))
            .andExpect(jsonPath("$.[*].actions").value(hasItem(DEFAULT_ACTIONS)))
            .andExpect(jsonPath("$.[*].priorityscore").value(hasItem(DEFAULT_PRIORITYSCORE)));
    }

    @Test
    @Transactional
    void getPromotion() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get the promotion
        restPromotionMockMvc
            .perform(get(ENTITY_API_URL_ID, promotion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(promotion.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.deletedat").value(DEFAULT_DELETEDAT.toString()))
            .andExpect(jsonPath("$.startsat").value(DEFAULT_STARTSAT.toString()))
            .andExpect(jsonPath("$.endsat").value(DEFAULT_ENDSAT.toString()))
            .andExpect(jsonPath("$.couponcode").value(DEFAULT_COUPONCODE))
            .andExpect(jsonPath("$.percustomerusagelimit").value(DEFAULT_PERCUSTOMERUSAGELIMIT))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.conditions").value(DEFAULT_CONDITIONS))
            .andExpect(jsonPath("$.actions").value(DEFAULT_ACTIONS))
            .andExpect(jsonPath("$.priorityscore").value(DEFAULT_PRIORITYSCORE));
    }

    @Test
    @Transactional
    void getPromotionsByIdFiltering() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        Long id = promotion.getId();

        defaultPromotionShouldBeFound("id.equals=" + id);
        defaultPromotionShouldNotBeFound("id.notEquals=" + id);

        defaultPromotionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPromotionShouldNotBeFound("id.greaterThan=" + id);

        defaultPromotionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPromotionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPromotionsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where createdat equals to DEFAULT_CREATEDAT
        defaultPromotionShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the promotionList where createdat equals to UPDATED_CREATEDAT
        defaultPromotionShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where createdat not equals to DEFAULT_CREATEDAT
        defaultPromotionShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the promotionList where createdat not equals to UPDATED_CREATEDAT
        defaultPromotionShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultPromotionShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the promotionList where createdat equals to UPDATED_CREATEDAT
        defaultPromotionShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where createdat is not null
        defaultPromotionShouldBeFound("createdat.specified=true");

        // Get all the promotionList where createdat is null
        defaultPromotionShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where updatedat equals to DEFAULT_UPDATEDAT
        defaultPromotionShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the promotionList where updatedat equals to UPDATED_UPDATEDAT
        defaultPromotionShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultPromotionShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the promotionList where updatedat not equals to UPDATED_UPDATEDAT
        defaultPromotionShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultPromotionShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the promotionList where updatedat equals to UPDATED_UPDATEDAT
        defaultPromotionShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where updatedat is not null
        defaultPromotionShouldBeFound("updatedat.specified=true");

        // Get all the promotionList where updatedat is null
        defaultPromotionShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByDeletedatIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where deletedat equals to DEFAULT_DELETEDAT
        defaultPromotionShouldBeFound("deletedat.equals=" + DEFAULT_DELETEDAT);

        // Get all the promotionList where deletedat equals to UPDATED_DELETEDAT
        defaultPromotionShouldNotBeFound("deletedat.equals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByDeletedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where deletedat not equals to DEFAULT_DELETEDAT
        defaultPromotionShouldNotBeFound("deletedat.notEquals=" + DEFAULT_DELETEDAT);

        // Get all the promotionList where deletedat not equals to UPDATED_DELETEDAT
        defaultPromotionShouldBeFound("deletedat.notEquals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByDeletedatIsInShouldWork() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where deletedat in DEFAULT_DELETEDAT or UPDATED_DELETEDAT
        defaultPromotionShouldBeFound("deletedat.in=" + DEFAULT_DELETEDAT + "," + UPDATED_DELETEDAT);

        // Get all the promotionList where deletedat equals to UPDATED_DELETEDAT
        defaultPromotionShouldNotBeFound("deletedat.in=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByDeletedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where deletedat is not null
        defaultPromotionShouldBeFound("deletedat.specified=true");

        // Get all the promotionList where deletedat is null
        defaultPromotionShouldNotBeFound("deletedat.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByStartsatIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where startsat equals to DEFAULT_STARTSAT
        defaultPromotionShouldBeFound("startsat.equals=" + DEFAULT_STARTSAT);

        // Get all the promotionList where startsat equals to UPDATED_STARTSAT
        defaultPromotionShouldNotBeFound("startsat.equals=" + UPDATED_STARTSAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByStartsatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where startsat not equals to DEFAULT_STARTSAT
        defaultPromotionShouldNotBeFound("startsat.notEquals=" + DEFAULT_STARTSAT);

        // Get all the promotionList where startsat not equals to UPDATED_STARTSAT
        defaultPromotionShouldBeFound("startsat.notEquals=" + UPDATED_STARTSAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByStartsatIsInShouldWork() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where startsat in DEFAULT_STARTSAT or UPDATED_STARTSAT
        defaultPromotionShouldBeFound("startsat.in=" + DEFAULT_STARTSAT + "," + UPDATED_STARTSAT);

        // Get all the promotionList where startsat equals to UPDATED_STARTSAT
        defaultPromotionShouldNotBeFound("startsat.in=" + UPDATED_STARTSAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByStartsatIsNullOrNotNull() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where startsat is not null
        defaultPromotionShouldBeFound("startsat.specified=true");

        // Get all the promotionList where startsat is null
        defaultPromotionShouldNotBeFound("startsat.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByEndsatIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where endsat equals to DEFAULT_ENDSAT
        defaultPromotionShouldBeFound("endsat.equals=" + DEFAULT_ENDSAT);

        // Get all the promotionList where endsat equals to UPDATED_ENDSAT
        defaultPromotionShouldNotBeFound("endsat.equals=" + UPDATED_ENDSAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByEndsatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where endsat not equals to DEFAULT_ENDSAT
        defaultPromotionShouldNotBeFound("endsat.notEquals=" + DEFAULT_ENDSAT);

        // Get all the promotionList where endsat not equals to UPDATED_ENDSAT
        defaultPromotionShouldBeFound("endsat.notEquals=" + UPDATED_ENDSAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByEndsatIsInShouldWork() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where endsat in DEFAULT_ENDSAT or UPDATED_ENDSAT
        defaultPromotionShouldBeFound("endsat.in=" + DEFAULT_ENDSAT + "," + UPDATED_ENDSAT);

        // Get all the promotionList where endsat equals to UPDATED_ENDSAT
        defaultPromotionShouldNotBeFound("endsat.in=" + UPDATED_ENDSAT);
    }

    @Test
    @Transactional
    void getAllPromotionsByEndsatIsNullOrNotNull() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where endsat is not null
        defaultPromotionShouldBeFound("endsat.specified=true");

        // Get all the promotionList where endsat is null
        defaultPromotionShouldNotBeFound("endsat.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByCouponcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where couponcode equals to DEFAULT_COUPONCODE
        defaultPromotionShouldBeFound("couponcode.equals=" + DEFAULT_COUPONCODE);

        // Get all the promotionList where couponcode equals to UPDATED_COUPONCODE
        defaultPromotionShouldNotBeFound("couponcode.equals=" + UPDATED_COUPONCODE);
    }

    @Test
    @Transactional
    void getAllPromotionsByCouponcodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where couponcode not equals to DEFAULT_COUPONCODE
        defaultPromotionShouldNotBeFound("couponcode.notEquals=" + DEFAULT_COUPONCODE);

        // Get all the promotionList where couponcode not equals to UPDATED_COUPONCODE
        defaultPromotionShouldBeFound("couponcode.notEquals=" + UPDATED_COUPONCODE);
    }

    @Test
    @Transactional
    void getAllPromotionsByCouponcodeIsInShouldWork() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where couponcode in DEFAULT_COUPONCODE or UPDATED_COUPONCODE
        defaultPromotionShouldBeFound("couponcode.in=" + DEFAULT_COUPONCODE + "," + UPDATED_COUPONCODE);

        // Get all the promotionList where couponcode equals to UPDATED_COUPONCODE
        defaultPromotionShouldNotBeFound("couponcode.in=" + UPDATED_COUPONCODE);
    }

    @Test
    @Transactional
    void getAllPromotionsByCouponcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where couponcode is not null
        defaultPromotionShouldBeFound("couponcode.specified=true");

        // Get all the promotionList where couponcode is null
        defaultPromotionShouldNotBeFound("couponcode.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByCouponcodeContainsSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where couponcode contains DEFAULT_COUPONCODE
        defaultPromotionShouldBeFound("couponcode.contains=" + DEFAULT_COUPONCODE);

        // Get all the promotionList where couponcode contains UPDATED_COUPONCODE
        defaultPromotionShouldNotBeFound("couponcode.contains=" + UPDATED_COUPONCODE);
    }

    @Test
    @Transactional
    void getAllPromotionsByCouponcodeNotContainsSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where couponcode does not contain DEFAULT_COUPONCODE
        defaultPromotionShouldNotBeFound("couponcode.doesNotContain=" + DEFAULT_COUPONCODE);

        // Get all the promotionList where couponcode does not contain UPDATED_COUPONCODE
        defaultPromotionShouldBeFound("couponcode.doesNotContain=" + UPDATED_COUPONCODE);
    }

    @Test
    @Transactional
    void getAllPromotionsByPercustomerusagelimitIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where percustomerusagelimit equals to DEFAULT_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldBeFound("percustomerusagelimit.equals=" + DEFAULT_PERCUSTOMERUSAGELIMIT);

        // Get all the promotionList where percustomerusagelimit equals to UPDATED_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldNotBeFound("percustomerusagelimit.equals=" + UPDATED_PERCUSTOMERUSAGELIMIT);
    }

    @Test
    @Transactional
    void getAllPromotionsByPercustomerusagelimitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where percustomerusagelimit not equals to DEFAULT_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldNotBeFound("percustomerusagelimit.notEquals=" + DEFAULT_PERCUSTOMERUSAGELIMIT);

        // Get all the promotionList where percustomerusagelimit not equals to UPDATED_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldBeFound("percustomerusagelimit.notEquals=" + UPDATED_PERCUSTOMERUSAGELIMIT);
    }

    @Test
    @Transactional
    void getAllPromotionsByPercustomerusagelimitIsInShouldWork() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where percustomerusagelimit in DEFAULT_PERCUSTOMERUSAGELIMIT or UPDATED_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldBeFound("percustomerusagelimit.in=" + DEFAULT_PERCUSTOMERUSAGELIMIT + "," + UPDATED_PERCUSTOMERUSAGELIMIT);

        // Get all the promotionList where percustomerusagelimit equals to UPDATED_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldNotBeFound("percustomerusagelimit.in=" + UPDATED_PERCUSTOMERUSAGELIMIT);
    }

    @Test
    @Transactional
    void getAllPromotionsByPercustomerusagelimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where percustomerusagelimit is not null
        defaultPromotionShouldBeFound("percustomerusagelimit.specified=true");

        // Get all the promotionList where percustomerusagelimit is null
        defaultPromotionShouldNotBeFound("percustomerusagelimit.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByPercustomerusagelimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where percustomerusagelimit is greater than or equal to DEFAULT_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldBeFound("percustomerusagelimit.greaterThanOrEqual=" + DEFAULT_PERCUSTOMERUSAGELIMIT);

        // Get all the promotionList where percustomerusagelimit is greater than or equal to UPDATED_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldNotBeFound("percustomerusagelimit.greaterThanOrEqual=" + UPDATED_PERCUSTOMERUSAGELIMIT);
    }

    @Test
    @Transactional
    void getAllPromotionsByPercustomerusagelimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where percustomerusagelimit is less than or equal to DEFAULT_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldBeFound("percustomerusagelimit.lessThanOrEqual=" + DEFAULT_PERCUSTOMERUSAGELIMIT);

        // Get all the promotionList where percustomerusagelimit is less than or equal to SMALLER_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldNotBeFound("percustomerusagelimit.lessThanOrEqual=" + SMALLER_PERCUSTOMERUSAGELIMIT);
    }

    @Test
    @Transactional
    void getAllPromotionsByPercustomerusagelimitIsLessThanSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where percustomerusagelimit is less than DEFAULT_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldNotBeFound("percustomerusagelimit.lessThan=" + DEFAULT_PERCUSTOMERUSAGELIMIT);

        // Get all the promotionList where percustomerusagelimit is less than UPDATED_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldBeFound("percustomerusagelimit.lessThan=" + UPDATED_PERCUSTOMERUSAGELIMIT);
    }

    @Test
    @Transactional
    void getAllPromotionsByPercustomerusagelimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where percustomerusagelimit is greater than DEFAULT_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldNotBeFound("percustomerusagelimit.greaterThan=" + DEFAULT_PERCUSTOMERUSAGELIMIT);

        // Get all the promotionList where percustomerusagelimit is greater than SMALLER_PERCUSTOMERUSAGELIMIT
        defaultPromotionShouldBeFound("percustomerusagelimit.greaterThan=" + SMALLER_PERCUSTOMERUSAGELIMIT);
    }

    @Test
    @Transactional
    void getAllPromotionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where name equals to DEFAULT_NAME
        defaultPromotionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the promotionList where name equals to UPDATED_NAME
        defaultPromotionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromotionsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where name not equals to DEFAULT_NAME
        defaultPromotionShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the promotionList where name not equals to UPDATED_NAME
        defaultPromotionShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromotionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPromotionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the promotionList where name equals to UPDATED_NAME
        defaultPromotionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromotionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where name is not null
        defaultPromotionShouldBeFound("name.specified=true");

        // Get all the promotionList where name is null
        defaultPromotionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByNameContainsSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where name contains DEFAULT_NAME
        defaultPromotionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the promotionList where name contains UPDATED_NAME
        defaultPromotionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromotionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where name does not contain DEFAULT_NAME
        defaultPromotionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the promotionList where name does not contain UPDATED_NAME
        defaultPromotionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPromotionsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where enabled equals to DEFAULT_ENABLED
        defaultPromotionShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the promotionList where enabled equals to UPDATED_ENABLED
        defaultPromotionShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllPromotionsByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where enabled not equals to DEFAULT_ENABLED
        defaultPromotionShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the promotionList where enabled not equals to UPDATED_ENABLED
        defaultPromotionShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllPromotionsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultPromotionShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the promotionList where enabled equals to UPDATED_ENABLED
        defaultPromotionShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    void getAllPromotionsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where enabled is not null
        defaultPromotionShouldBeFound("enabled.specified=true");

        // Get all the promotionList where enabled is null
        defaultPromotionShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByConditionsIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where conditions equals to DEFAULT_CONDITIONS
        defaultPromotionShouldBeFound("conditions.equals=" + DEFAULT_CONDITIONS);

        // Get all the promotionList where conditions equals to UPDATED_CONDITIONS
        defaultPromotionShouldNotBeFound("conditions.equals=" + UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllPromotionsByConditionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where conditions not equals to DEFAULT_CONDITIONS
        defaultPromotionShouldNotBeFound("conditions.notEquals=" + DEFAULT_CONDITIONS);

        // Get all the promotionList where conditions not equals to UPDATED_CONDITIONS
        defaultPromotionShouldBeFound("conditions.notEquals=" + UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllPromotionsByConditionsIsInShouldWork() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where conditions in DEFAULT_CONDITIONS or UPDATED_CONDITIONS
        defaultPromotionShouldBeFound("conditions.in=" + DEFAULT_CONDITIONS + "," + UPDATED_CONDITIONS);

        // Get all the promotionList where conditions equals to UPDATED_CONDITIONS
        defaultPromotionShouldNotBeFound("conditions.in=" + UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllPromotionsByConditionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where conditions is not null
        defaultPromotionShouldBeFound("conditions.specified=true");

        // Get all the promotionList where conditions is null
        defaultPromotionShouldNotBeFound("conditions.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByConditionsContainsSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where conditions contains DEFAULT_CONDITIONS
        defaultPromotionShouldBeFound("conditions.contains=" + DEFAULT_CONDITIONS);

        // Get all the promotionList where conditions contains UPDATED_CONDITIONS
        defaultPromotionShouldNotBeFound("conditions.contains=" + UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllPromotionsByConditionsNotContainsSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where conditions does not contain DEFAULT_CONDITIONS
        defaultPromotionShouldNotBeFound("conditions.doesNotContain=" + DEFAULT_CONDITIONS);

        // Get all the promotionList where conditions does not contain UPDATED_CONDITIONS
        defaultPromotionShouldBeFound("conditions.doesNotContain=" + UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllPromotionsByActionsIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where actions equals to DEFAULT_ACTIONS
        defaultPromotionShouldBeFound("actions.equals=" + DEFAULT_ACTIONS);

        // Get all the promotionList where actions equals to UPDATED_ACTIONS
        defaultPromotionShouldNotBeFound("actions.equals=" + UPDATED_ACTIONS);
    }

    @Test
    @Transactional
    void getAllPromotionsByActionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where actions not equals to DEFAULT_ACTIONS
        defaultPromotionShouldNotBeFound("actions.notEquals=" + DEFAULT_ACTIONS);

        // Get all the promotionList where actions not equals to UPDATED_ACTIONS
        defaultPromotionShouldBeFound("actions.notEquals=" + UPDATED_ACTIONS);
    }

    @Test
    @Transactional
    void getAllPromotionsByActionsIsInShouldWork() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where actions in DEFAULT_ACTIONS or UPDATED_ACTIONS
        defaultPromotionShouldBeFound("actions.in=" + DEFAULT_ACTIONS + "," + UPDATED_ACTIONS);

        // Get all the promotionList where actions equals to UPDATED_ACTIONS
        defaultPromotionShouldNotBeFound("actions.in=" + UPDATED_ACTIONS);
    }

    @Test
    @Transactional
    void getAllPromotionsByActionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where actions is not null
        defaultPromotionShouldBeFound("actions.specified=true");

        // Get all the promotionList where actions is null
        defaultPromotionShouldNotBeFound("actions.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByActionsContainsSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where actions contains DEFAULT_ACTIONS
        defaultPromotionShouldBeFound("actions.contains=" + DEFAULT_ACTIONS);

        // Get all the promotionList where actions contains UPDATED_ACTIONS
        defaultPromotionShouldNotBeFound("actions.contains=" + UPDATED_ACTIONS);
    }

    @Test
    @Transactional
    void getAllPromotionsByActionsNotContainsSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where actions does not contain DEFAULT_ACTIONS
        defaultPromotionShouldNotBeFound("actions.doesNotContain=" + DEFAULT_ACTIONS);

        // Get all the promotionList where actions does not contain UPDATED_ACTIONS
        defaultPromotionShouldBeFound("actions.doesNotContain=" + UPDATED_ACTIONS);
    }

    @Test
    @Transactional
    void getAllPromotionsByPriorityscoreIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where priorityscore equals to DEFAULT_PRIORITYSCORE
        defaultPromotionShouldBeFound("priorityscore.equals=" + DEFAULT_PRIORITYSCORE);

        // Get all the promotionList where priorityscore equals to UPDATED_PRIORITYSCORE
        defaultPromotionShouldNotBeFound("priorityscore.equals=" + UPDATED_PRIORITYSCORE);
    }

    @Test
    @Transactional
    void getAllPromotionsByPriorityscoreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where priorityscore not equals to DEFAULT_PRIORITYSCORE
        defaultPromotionShouldNotBeFound("priorityscore.notEquals=" + DEFAULT_PRIORITYSCORE);

        // Get all the promotionList where priorityscore not equals to UPDATED_PRIORITYSCORE
        defaultPromotionShouldBeFound("priorityscore.notEquals=" + UPDATED_PRIORITYSCORE);
    }

    @Test
    @Transactional
    void getAllPromotionsByPriorityscoreIsInShouldWork() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where priorityscore in DEFAULT_PRIORITYSCORE or UPDATED_PRIORITYSCORE
        defaultPromotionShouldBeFound("priorityscore.in=" + DEFAULT_PRIORITYSCORE + "," + UPDATED_PRIORITYSCORE);

        // Get all the promotionList where priorityscore equals to UPDATED_PRIORITYSCORE
        defaultPromotionShouldNotBeFound("priorityscore.in=" + UPDATED_PRIORITYSCORE);
    }

    @Test
    @Transactional
    void getAllPromotionsByPriorityscoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where priorityscore is not null
        defaultPromotionShouldBeFound("priorityscore.specified=true");

        // Get all the promotionList where priorityscore is null
        defaultPromotionShouldNotBeFound("priorityscore.specified=false");
    }

    @Test
    @Transactional
    void getAllPromotionsByPriorityscoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where priorityscore is greater than or equal to DEFAULT_PRIORITYSCORE
        defaultPromotionShouldBeFound("priorityscore.greaterThanOrEqual=" + DEFAULT_PRIORITYSCORE);

        // Get all the promotionList where priorityscore is greater than or equal to UPDATED_PRIORITYSCORE
        defaultPromotionShouldNotBeFound("priorityscore.greaterThanOrEqual=" + UPDATED_PRIORITYSCORE);
    }

    @Test
    @Transactional
    void getAllPromotionsByPriorityscoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where priorityscore is less than or equal to DEFAULT_PRIORITYSCORE
        defaultPromotionShouldBeFound("priorityscore.lessThanOrEqual=" + DEFAULT_PRIORITYSCORE);

        // Get all the promotionList where priorityscore is less than or equal to SMALLER_PRIORITYSCORE
        defaultPromotionShouldNotBeFound("priorityscore.lessThanOrEqual=" + SMALLER_PRIORITYSCORE);
    }

    @Test
    @Transactional
    void getAllPromotionsByPriorityscoreIsLessThanSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where priorityscore is less than DEFAULT_PRIORITYSCORE
        defaultPromotionShouldNotBeFound("priorityscore.lessThan=" + DEFAULT_PRIORITYSCORE);

        // Get all the promotionList where priorityscore is less than UPDATED_PRIORITYSCORE
        defaultPromotionShouldBeFound("priorityscore.lessThan=" + UPDATED_PRIORITYSCORE);
    }

    @Test
    @Transactional
    void getAllPromotionsByPriorityscoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        // Get all the promotionList where priorityscore is greater than DEFAULT_PRIORITYSCORE
        defaultPromotionShouldNotBeFound("priorityscore.greaterThan=" + DEFAULT_PRIORITYSCORE);

        // Get all the promotionList where priorityscore is greater than SMALLER_PRIORITYSCORE
        defaultPromotionShouldBeFound("priorityscore.greaterThan=" + SMALLER_PRIORITYSCORE);
    }

    @Test
    @Transactional
    void getAllPromotionsByJorderIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);
        Jorder jorder = JorderResourceIT.createEntity(em);
        em.persist(jorder);
        em.flush();
        promotion.addJorder(jorder);
        promotionRepository.saveAndFlush(promotion);
        Long jorderId = jorder.getId();

        // Get all the promotionList where jorder equals to jorderId
        defaultPromotionShouldBeFound("jorderId.equals=" + jorderId);

        // Get all the promotionList where jorder equals to (jorderId + 1)
        defaultPromotionShouldNotBeFound("jorderId.equals=" + (jorderId + 1));
    }

    @Test
    @Transactional
    void getAllPromotionsByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);
        Channel channel = ChannelResourceIT.createEntity(em);
        em.persist(channel);
        em.flush();
        promotion.addChannel(channel);
        promotionRepository.saveAndFlush(promotion);
        Long channelId = channel.getId();

        // Get all the promotionList where channel equals to channelId
        defaultPromotionShouldBeFound("channelId.equals=" + channelId);

        // Get all the promotionList where channel equals to (channelId + 1)
        defaultPromotionShouldNotBeFound("channelId.equals=" + (channelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPromotionShouldBeFound(String filter) throws Exception {
        restPromotionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(promotion.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].startsat").value(hasItem(DEFAULT_STARTSAT.toString())))
            .andExpect(jsonPath("$.[*].endsat").value(hasItem(DEFAULT_ENDSAT.toString())))
            .andExpect(jsonPath("$.[*].couponcode").value(hasItem(DEFAULT_COUPONCODE)))
            .andExpect(jsonPath("$.[*].percustomerusagelimit").value(hasItem(DEFAULT_PERCUSTOMERUSAGELIMIT)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS)))
            .andExpect(jsonPath("$.[*].actions").value(hasItem(DEFAULT_ACTIONS)))
            .andExpect(jsonPath("$.[*].priorityscore").value(hasItem(DEFAULT_PRIORITYSCORE)));

        // Check, that the count call also returns 1
        restPromotionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPromotionShouldNotBeFound(String filter) throws Exception {
        restPromotionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPromotionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPromotion() throws Exception {
        // Get the promotion
        restPromotionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPromotion() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        int databaseSizeBeforeUpdate = promotionRepository.findAll().size();

        // Update the promotion
        Promotion updatedPromotion = promotionRepository.findById(promotion.getId()).get();
        // Disconnect from session so that the updates on updatedPromotion are not directly saved in db
        em.detach(updatedPromotion);
        updatedPromotion
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .startsat(UPDATED_STARTSAT)
            .endsat(UPDATED_ENDSAT)
            .couponcode(UPDATED_COUPONCODE)
            .percustomerusagelimit(UPDATED_PERCUSTOMERUSAGELIMIT)
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED)
            .conditions(UPDATED_CONDITIONS)
            .actions(UPDATED_ACTIONS)
            .priorityscore(UPDATED_PRIORITYSCORE);
        PromotionDTO promotionDTO = promotionMapper.toDto(updatedPromotion);

        restPromotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promotionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promotionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
        Promotion testPromotion = promotionList.get(promotionList.size() - 1);
        assertThat(testPromotion.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testPromotion.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testPromotion.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testPromotion.getStartsat()).isEqualTo(UPDATED_STARTSAT);
        assertThat(testPromotion.getEndsat()).isEqualTo(UPDATED_ENDSAT);
        assertThat(testPromotion.getCouponcode()).isEqualTo(UPDATED_COUPONCODE);
        assertThat(testPromotion.getPercustomerusagelimit()).isEqualTo(UPDATED_PERCUSTOMERUSAGELIMIT);
        assertThat(testPromotion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPromotion.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testPromotion.getConditions()).isEqualTo(UPDATED_CONDITIONS);
        assertThat(testPromotion.getActions()).isEqualTo(UPDATED_ACTIONS);
        assertThat(testPromotion.getPriorityscore()).isEqualTo(UPDATED_PRIORITYSCORE);
    }

    @Test
    @Transactional
    void putNonExistingPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().size();
        promotion.setId(count.incrementAndGet());

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, promotionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().size();
        promotion.setId(count.incrementAndGet());

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(promotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().size();
        promotion.setId(count.incrementAndGet());

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(promotionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePromotionWithPatch() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        int databaseSizeBeforeUpdate = promotionRepository.findAll().size();

        // Update the promotion using partial update
        Promotion partialUpdatedPromotion = new Promotion();
        partialUpdatedPromotion.setId(promotion.getId());

        partialUpdatedPromotion
            .createdat(UPDATED_CREATEDAT)
            .startsat(UPDATED_STARTSAT)
            .percustomerusagelimit(UPDATED_PERCUSTOMERUSAGELIMIT)
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED)
            .conditions(UPDATED_CONDITIONS);

        restPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromotion))
            )
            .andExpect(status().isOk());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
        Promotion testPromotion = promotionList.get(promotionList.size() - 1);
        assertThat(testPromotion.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testPromotion.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testPromotion.getDeletedat()).isEqualTo(DEFAULT_DELETEDAT);
        assertThat(testPromotion.getStartsat()).isEqualTo(UPDATED_STARTSAT);
        assertThat(testPromotion.getEndsat()).isEqualTo(DEFAULT_ENDSAT);
        assertThat(testPromotion.getCouponcode()).isEqualTo(DEFAULT_COUPONCODE);
        assertThat(testPromotion.getPercustomerusagelimit()).isEqualTo(UPDATED_PERCUSTOMERUSAGELIMIT);
        assertThat(testPromotion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPromotion.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testPromotion.getConditions()).isEqualTo(UPDATED_CONDITIONS);
        assertThat(testPromotion.getActions()).isEqualTo(DEFAULT_ACTIONS);
        assertThat(testPromotion.getPriorityscore()).isEqualTo(DEFAULT_PRIORITYSCORE);
    }

    @Test
    @Transactional
    void fullUpdatePromotionWithPatch() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        int databaseSizeBeforeUpdate = promotionRepository.findAll().size();

        // Update the promotion using partial update
        Promotion partialUpdatedPromotion = new Promotion();
        partialUpdatedPromotion.setId(promotion.getId());

        partialUpdatedPromotion
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .startsat(UPDATED_STARTSAT)
            .endsat(UPDATED_ENDSAT)
            .couponcode(UPDATED_COUPONCODE)
            .percustomerusagelimit(UPDATED_PERCUSTOMERUSAGELIMIT)
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED)
            .conditions(UPDATED_CONDITIONS)
            .actions(UPDATED_ACTIONS)
            .priorityscore(UPDATED_PRIORITYSCORE);

        restPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPromotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPromotion))
            )
            .andExpect(status().isOk());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
        Promotion testPromotion = promotionList.get(promotionList.size() - 1);
        assertThat(testPromotion.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testPromotion.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testPromotion.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testPromotion.getStartsat()).isEqualTo(UPDATED_STARTSAT);
        assertThat(testPromotion.getEndsat()).isEqualTo(UPDATED_ENDSAT);
        assertThat(testPromotion.getCouponcode()).isEqualTo(UPDATED_COUPONCODE);
        assertThat(testPromotion.getPercustomerusagelimit()).isEqualTo(UPDATED_PERCUSTOMERUSAGELIMIT);
        assertThat(testPromotion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPromotion.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testPromotion.getConditions()).isEqualTo(UPDATED_CONDITIONS);
        assertThat(testPromotion.getActions()).isEqualTo(UPDATED_ACTIONS);
        assertThat(testPromotion.getPriorityscore()).isEqualTo(UPDATED_PRIORITYSCORE);
    }

    @Test
    @Transactional
    void patchNonExistingPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().size();
        promotion.setId(count.incrementAndGet());

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, promotionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().size();
        promotion.setId(count.incrementAndGet());

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(promotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().size();
        promotion.setId(count.incrementAndGet());

        // Create the Promotion
        PromotionDTO promotionDTO = promotionMapper.toDto(promotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(promotionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePromotion() throws Exception {
        // Initialize the database
        promotionRepository.saveAndFlush(promotion);

        int databaseSizeBeforeDelete = promotionRepository.findAll().size();

        // Delete the promotion
        restPromotionMockMvc
            .perform(delete(ENTITY_API_URL_ID, promotion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Promotion> promotionList = promotionRepository.findAll();
        assertThat(promotionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
