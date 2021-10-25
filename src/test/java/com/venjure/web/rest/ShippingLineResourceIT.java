package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Jorder;
import com.venjure.domain.ShippingLine;
import com.venjure.domain.ShippingMethod;
import com.venjure.repository.ShippingLineRepository;
import com.venjure.service.criteria.ShippingLineCriteria;
import com.venjure.service.dto.ShippingLineDTO;
import com.venjure.service.mapper.ShippingLineMapper;
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
 * Integration tests for the {@link ShippingLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShippingLineResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_LISTPRICE = 1;
    private static final Integer UPDATED_LISTPRICE = 2;
    private static final Integer SMALLER_LISTPRICE = 1 - 1;

    private static final Boolean DEFAULT_LISTPRICEINCLUDESTAX = false;
    private static final Boolean UPDATED_LISTPRICEINCLUDESTAX = true;

    private static final String DEFAULT_ADJUSTMENTS = "AAAAAAAAAA";
    private static final String UPDATED_ADJUSTMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_TAXLINES = "AAAAAAAAAA";
    private static final String UPDATED_TAXLINES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shipping-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShippingLineRepository shippingLineRepository;

    @Autowired
    private ShippingLineMapper shippingLineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShippingLineMockMvc;

    private ShippingLine shippingLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShippingLine createEntity(EntityManager em) {
        ShippingLine shippingLine = new ShippingLine()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .listprice(DEFAULT_LISTPRICE)
            .listpriceincludestax(DEFAULT_LISTPRICEINCLUDESTAX)
            .adjustments(DEFAULT_ADJUSTMENTS)
            .taxlines(DEFAULT_TAXLINES);
        // Add required entity
        ShippingMethod shippingMethod;
        if (TestUtil.findAll(em, ShippingMethod.class).isEmpty()) {
            shippingMethod = ShippingMethodResourceIT.createEntity(em);
            em.persist(shippingMethod);
            em.flush();
        } else {
            shippingMethod = TestUtil.findAll(em, ShippingMethod.class).get(0);
        }
        shippingLine.setShippingmethod(shippingMethod);
        return shippingLine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShippingLine createUpdatedEntity(EntityManager em) {
        ShippingLine shippingLine = new ShippingLine()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .listprice(UPDATED_LISTPRICE)
            .listpriceincludestax(UPDATED_LISTPRICEINCLUDESTAX)
            .adjustments(UPDATED_ADJUSTMENTS)
            .taxlines(UPDATED_TAXLINES);
        // Add required entity
        ShippingMethod shippingMethod;
        if (TestUtil.findAll(em, ShippingMethod.class).isEmpty()) {
            shippingMethod = ShippingMethodResourceIT.createUpdatedEntity(em);
            em.persist(shippingMethod);
            em.flush();
        } else {
            shippingMethod = TestUtil.findAll(em, ShippingMethod.class).get(0);
        }
        shippingLine.setShippingmethod(shippingMethod);
        return shippingLine;
    }

    @BeforeEach
    public void initTest() {
        shippingLine = createEntity(em);
    }

    @Test
    @Transactional
    void createShippingLine() throws Exception {
        int databaseSizeBeforeCreate = shippingLineRepository.findAll().size();
        // Create the ShippingLine
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);
        restShippingLineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ShippingLine in the database
        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeCreate + 1);
        ShippingLine testShippingLine = shippingLineList.get(shippingLineList.size() - 1);
        assertThat(testShippingLine.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testShippingLine.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testShippingLine.getListprice()).isEqualTo(DEFAULT_LISTPRICE);
        assertThat(testShippingLine.getListpriceincludestax()).isEqualTo(DEFAULT_LISTPRICEINCLUDESTAX);
        assertThat(testShippingLine.getAdjustments()).isEqualTo(DEFAULT_ADJUSTMENTS);
        assertThat(testShippingLine.getTaxlines()).isEqualTo(DEFAULT_TAXLINES);
    }

    @Test
    @Transactional
    void createShippingLineWithExistingId() throws Exception {
        // Create the ShippingLine with an existing ID
        shippingLine.setId(1L);
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        int databaseSizeBeforeCreate = shippingLineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShippingLineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingLine in the database
        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingLineRepository.findAll().size();
        // set the field null
        shippingLine.setCreatedat(null);

        // Create the ShippingLine, which fails.
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        restShippingLineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingLineRepository.findAll().size();
        // set the field null
        shippingLine.setUpdatedat(null);

        // Create the ShippingLine, which fails.
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        restShippingLineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkListpriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingLineRepository.findAll().size();
        // set the field null
        shippingLine.setListprice(null);

        // Create the ShippingLine, which fails.
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        restShippingLineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkListpriceincludestaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingLineRepository.findAll().size();
        // set the field null
        shippingLine.setListpriceincludestax(null);

        // Create the ShippingLine, which fails.
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        restShippingLineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdjustmentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingLineRepository.findAll().size();
        // set the field null
        shippingLine.setAdjustments(null);

        // Create the ShippingLine, which fails.
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        restShippingLineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxlinesIsRequired() throws Exception {
        int databaseSizeBeforeTest = shippingLineRepository.findAll().size();
        // set the field null
        shippingLine.setTaxlines(null);

        // Create the ShippingLine, which fails.
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        restShippingLineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isBadRequest());

        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShippingLines() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList
        restShippingLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shippingLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].listprice").value(hasItem(DEFAULT_LISTPRICE)))
            .andExpect(jsonPath("$.[*].listpriceincludestax").value(hasItem(DEFAULT_LISTPRICEINCLUDESTAX.booleanValue())))
            .andExpect(jsonPath("$.[*].adjustments").value(hasItem(DEFAULT_ADJUSTMENTS)))
            .andExpect(jsonPath("$.[*].taxlines").value(hasItem(DEFAULT_TAXLINES)));
    }

    @Test
    @Transactional
    void getShippingLine() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get the shippingLine
        restShippingLineMockMvc
            .perform(get(ENTITY_API_URL_ID, shippingLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shippingLine.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.listprice").value(DEFAULT_LISTPRICE))
            .andExpect(jsonPath("$.listpriceincludestax").value(DEFAULT_LISTPRICEINCLUDESTAX.booleanValue()))
            .andExpect(jsonPath("$.adjustments").value(DEFAULT_ADJUSTMENTS))
            .andExpect(jsonPath("$.taxlines").value(DEFAULT_TAXLINES));
    }

    @Test
    @Transactional
    void getShippingLinesByIdFiltering() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        Long id = shippingLine.getId();

        defaultShippingLineShouldBeFound("id.equals=" + id);
        defaultShippingLineShouldNotBeFound("id.notEquals=" + id);

        defaultShippingLineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultShippingLineShouldNotBeFound("id.greaterThan=" + id);

        defaultShippingLineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultShippingLineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShippingLinesByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where createdat equals to DEFAULT_CREATEDAT
        defaultShippingLineShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the shippingLineList where createdat equals to UPDATED_CREATEDAT
        defaultShippingLineShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingLinesByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where createdat not equals to DEFAULT_CREATEDAT
        defaultShippingLineShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the shippingLineList where createdat not equals to UPDATED_CREATEDAT
        defaultShippingLineShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingLinesByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultShippingLineShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the shippingLineList where createdat equals to UPDATED_CREATEDAT
        defaultShippingLineShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingLinesByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where createdat is not null
        defaultShippingLineShouldBeFound("createdat.specified=true");

        // Get all the shippingLineList where createdat is null
        defaultShippingLineShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingLinesByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where updatedat equals to DEFAULT_UPDATEDAT
        defaultShippingLineShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the shippingLineList where updatedat equals to UPDATED_UPDATEDAT
        defaultShippingLineShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingLinesByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultShippingLineShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the shippingLineList where updatedat not equals to UPDATED_UPDATEDAT
        defaultShippingLineShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingLinesByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultShippingLineShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the shippingLineList where updatedat equals to UPDATED_UPDATEDAT
        defaultShippingLineShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllShippingLinesByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where updatedat is not null
        defaultShippingLineShouldBeFound("updatedat.specified=true");

        // Get all the shippingLineList where updatedat is null
        defaultShippingLineShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingLinesByListpriceIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where listprice equals to DEFAULT_LISTPRICE
        defaultShippingLineShouldBeFound("listprice.equals=" + DEFAULT_LISTPRICE);

        // Get all the shippingLineList where listprice equals to UPDATED_LISTPRICE
        defaultShippingLineShouldNotBeFound("listprice.equals=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllShippingLinesByListpriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where listprice not equals to DEFAULT_LISTPRICE
        defaultShippingLineShouldNotBeFound("listprice.notEquals=" + DEFAULT_LISTPRICE);

        // Get all the shippingLineList where listprice not equals to UPDATED_LISTPRICE
        defaultShippingLineShouldBeFound("listprice.notEquals=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllShippingLinesByListpriceIsInShouldWork() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where listprice in DEFAULT_LISTPRICE or UPDATED_LISTPRICE
        defaultShippingLineShouldBeFound("listprice.in=" + DEFAULT_LISTPRICE + "," + UPDATED_LISTPRICE);

        // Get all the shippingLineList where listprice equals to UPDATED_LISTPRICE
        defaultShippingLineShouldNotBeFound("listprice.in=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllShippingLinesByListpriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where listprice is not null
        defaultShippingLineShouldBeFound("listprice.specified=true");

        // Get all the shippingLineList where listprice is null
        defaultShippingLineShouldNotBeFound("listprice.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingLinesByListpriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where listprice is greater than or equal to DEFAULT_LISTPRICE
        defaultShippingLineShouldBeFound("listprice.greaterThanOrEqual=" + DEFAULT_LISTPRICE);

        // Get all the shippingLineList where listprice is greater than or equal to UPDATED_LISTPRICE
        defaultShippingLineShouldNotBeFound("listprice.greaterThanOrEqual=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllShippingLinesByListpriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where listprice is less than or equal to DEFAULT_LISTPRICE
        defaultShippingLineShouldBeFound("listprice.lessThanOrEqual=" + DEFAULT_LISTPRICE);

        // Get all the shippingLineList where listprice is less than or equal to SMALLER_LISTPRICE
        defaultShippingLineShouldNotBeFound("listprice.lessThanOrEqual=" + SMALLER_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllShippingLinesByListpriceIsLessThanSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where listprice is less than DEFAULT_LISTPRICE
        defaultShippingLineShouldNotBeFound("listprice.lessThan=" + DEFAULT_LISTPRICE);

        // Get all the shippingLineList where listprice is less than UPDATED_LISTPRICE
        defaultShippingLineShouldBeFound("listprice.lessThan=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllShippingLinesByListpriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where listprice is greater than DEFAULT_LISTPRICE
        defaultShippingLineShouldNotBeFound("listprice.greaterThan=" + DEFAULT_LISTPRICE);

        // Get all the shippingLineList where listprice is greater than SMALLER_LISTPRICE
        defaultShippingLineShouldBeFound("listprice.greaterThan=" + SMALLER_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllShippingLinesByListpriceincludestaxIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where listpriceincludestax equals to DEFAULT_LISTPRICEINCLUDESTAX
        defaultShippingLineShouldBeFound("listpriceincludestax.equals=" + DEFAULT_LISTPRICEINCLUDESTAX);

        // Get all the shippingLineList where listpriceincludestax equals to UPDATED_LISTPRICEINCLUDESTAX
        defaultShippingLineShouldNotBeFound("listpriceincludestax.equals=" + UPDATED_LISTPRICEINCLUDESTAX);
    }

    @Test
    @Transactional
    void getAllShippingLinesByListpriceincludestaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where listpriceincludestax not equals to DEFAULT_LISTPRICEINCLUDESTAX
        defaultShippingLineShouldNotBeFound("listpriceincludestax.notEquals=" + DEFAULT_LISTPRICEINCLUDESTAX);

        // Get all the shippingLineList where listpriceincludestax not equals to UPDATED_LISTPRICEINCLUDESTAX
        defaultShippingLineShouldBeFound("listpriceincludestax.notEquals=" + UPDATED_LISTPRICEINCLUDESTAX);
    }

    @Test
    @Transactional
    void getAllShippingLinesByListpriceincludestaxIsInShouldWork() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where listpriceincludestax in DEFAULT_LISTPRICEINCLUDESTAX or UPDATED_LISTPRICEINCLUDESTAX
        defaultShippingLineShouldBeFound("listpriceincludestax.in=" + DEFAULT_LISTPRICEINCLUDESTAX + "," + UPDATED_LISTPRICEINCLUDESTAX);

        // Get all the shippingLineList where listpriceincludestax equals to UPDATED_LISTPRICEINCLUDESTAX
        defaultShippingLineShouldNotBeFound("listpriceincludestax.in=" + UPDATED_LISTPRICEINCLUDESTAX);
    }

    @Test
    @Transactional
    void getAllShippingLinesByListpriceincludestaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where listpriceincludestax is not null
        defaultShippingLineShouldBeFound("listpriceincludestax.specified=true");

        // Get all the shippingLineList where listpriceincludestax is null
        defaultShippingLineShouldNotBeFound("listpriceincludestax.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingLinesByAdjustmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where adjustments equals to DEFAULT_ADJUSTMENTS
        defaultShippingLineShouldBeFound("adjustments.equals=" + DEFAULT_ADJUSTMENTS);

        // Get all the shippingLineList where adjustments equals to UPDATED_ADJUSTMENTS
        defaultShippingLineShouldNotBeFound("adjustments.equals=" + UPDATED_ADJUSTMENTS);
    }

    @Test
    @Transactional
    void getAllShippingLinesByAdjustmentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where adjustments not equals to DEFAULT_ADJUSTMENTS
        defaultShippingLineShouldNotBeFound("adjustments.notEquals=" + DEFAULT_ADJUSTMENTS);

        // Get all the shippingLineList where adjustments not equals to UPDATED_ADJUSTMENTS
        defaultShippingLineShouldBeFound("adjustments.notEquals=" + UPDATED_ADJUSTMENTS);
    }

    @Test
    @Transactional
    void getAllShippingLinesByAdjustmentsIsInShouldWork() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where adjustments in DEFAULT_ADJUSTMENTS or UPDATED_ADJUSTMENTS
        defaultShippingLineShouldBeFound("adjustments.in=" + DEFAULT_ADJUSTMENTS + "," + UPDATED_ADJUSTMENTS);

        // Get all the shippingLineList where adjustments equals to UPDATED_ADJUSTMENTS
        defaultShippingLineShouldNotBeFound("adjustments.in=" + UPDATED_ADJUSTMENTS);
    }

    @Test
    @Transactional
    void getAllShippingLinesByAdjustmentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where adjustments is not null
        defaultShippingLineShouldBeFound("adjustments.specified=true");

        // Get all the shippingLineList where adjustments is null
        defaultShippingLineShouldNotBeFound("adjustments.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingLinesByAdjustmentsContainsSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where adjustments contains DEFAULT_ADJUSTMENTS
        defaultShippingLineShouldBeFound("adjustments.contains=" + DEFAULT_ADJUSTMENTS);

        // Get all the shippingLineList where adjustments contains UPDATED_ADJUSTMENTS
        defaultShippingLineShouldNotBeFound("adjustments.contains=" + UPDATED_ADJUSTMENTS);
    }

    @Test
    @Transactional
    void getAllShippingLinesByAdjustmentsNotContainsSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where adjustments does not contain DEFAULT_ADJUSTMENTS
        defaultShippingLineShouldNotBeFound("adjustments.doesNotContain=" + DEFAULT_ADJUSTMENTS);

        // Get all the shippingLineList where adjustments does not contain UPDATED_ADJUSTMENTS
        defaultShippingLineShouldBeFound("adjustments.doesNotContain=" + UPDATED_ADJUSTMENTS);
    }

    @Test
    @Transactional
    void getAllShippingLinesByTaxlinesIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where taxlines equals to DEFAULT_TAXLINES
        defaultShippingLineShouldBeFound("taxlines.equals=" + DEFAULT_TAXLINES);

        // Get all the shippingLineList where taxlines equals to UPDATED_TAXLINES
        defaultShippingLineShouldNotBeFound("taxlines.equals=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllShippingLinesByTaxlinesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where taxlines not equals to DEFAULT_TAXLINES
        defaultShippingLineShouldNotBeFound("taxlines.notEquals=" + DEFAULT_TAXLINES);

        // Get all the shippingLineList where taxlines not equals to UPDATED_TAXLINES
        defaultShippingLineShouldBeFound("taxlines.notEquals=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllShippingLinesByTaxlinesIsInShouldWork() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where taxlines in DEFAULT_TAXLINES or UPDATED_TAXLINES
        defaultShippingLineShouldBeFound("taxlines.in=" + DEFAULT_TAXLINES + "," + UPDATED_TAXLINES);

        // Get all the shippingLineList where taxlines equals to UPDATED_TAXLINES
        defaultShippingLineShouldNotBeFound("taxlines.in=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllShippingLinesByTaxlinesIsNullOrNotNull() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where taxlines is not null
        defaultShippingLineShouldBeFound("taxlines.specified=true");

        // Get all the shippingLineList where taxlines is null
        defaultShippingLineShouldNotBeFound("taxlines.specified=false");
    }

    @Test
    @Transactional
    void getAllShippingLinesByTaxlinesContainsSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where taxlines contains DEFAULT_TAXLINES
        defaultShippingLineShouldBeFound("taxlines.contains=" + DEFAULT_TAXLINES);

        // Get all the shippingLineList where taxlines contains UPDATED_TAXLINES
        defaultShippingLineShouldNotBeFound("taxlines.contains=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllShippingLinesByTaxlinesNotContainsSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        // Get all the shippingLineList where taxlines does not contain DEFAULT_TAXLINES
        defaultShippingLineShouldNotBeFound("taxlines.doesNotContain=" + DEFAULT_TAXLINES);

        // Get all the shippingLineList where taxlines does not contain UPDATED_TAXLINES
        defaultShippingLineShouldBeFound("taxlines.doesNotContain=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllShippingLinesByShippingmethodIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);
        ShippingMethod shippingmethod = ShippingMethodResourceIT.createEntity(em);
        em.persist(shippingmethod);
        em.flush();
        shippingLine.setShippingmethod(shippingmethod);
        shippingLineRepository.saveAndFlush(shippingLine);
        Long shippingmethodId = shippingmethod.getId();

        // Get all the shippingLineList where shippingmethod equals to shippingmethodId
        defaultShippingLineShouldBeFound("shippingmethodId.equals=" + shippingmethodId);

        // Get all the shippingLineList where shippingmethod equals to (shippingmethodId + 1)
        defaultShippingLineShouldNotBeFound("shippingmethodId.equals=" + (shippingmethodId + 1));
    }

    @Test
    @Transactional
    void getAllShippingLinesByJorderIsEqualToSomething() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);
        Jorder jorder = JorderResourceIT.createEntity(em);
        em.persist(jorder);
        em.flush();
        shippingLine.setJorder(jorder);
        shippingLineRepository.saveAndFlush(shippingLine);
        Long jorderId = jorder.getId();

        // Get all the shippingLineList where jorder equals to jorderId
        defaultShippingLineShouldBeFound("jorderId.equals=" + jorderId);

        // Get all the shippingLineList where jorder equals to (jorderId + 1)
        defaultShippingLineShouldNotBeFound("jorderId.equals=" + (jorderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShippingLineShouldBeFound(String filter) throws Exception {
        restShippingLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shippingLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].listprice").value(hasItem(DEFAULT_LISTPRICE)))
            .andExpect(jsonPath("$.[*].listpriceincludestax").value(hasItem(DEFAULT_LISTPRICEINCLUDESTAX.booleanValue())))
            .andExpect(jsonPath("$.[*].adjustments").value(hasItem(DEFAULT_ADJUSTMENTS)))
            .andExpect(jsonPath("$.[*].taxlines").value(hasItem(DEFAULT_TAXLINES)));

        // Check, that the count call also returns 1
        restShippingLineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShippingLineShouldNotBeFound(String filter) throws Exception {
        restShippingLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShippingLineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShippingLine() throws Exception {
        // Get the shippingLine
        restShippingLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShippingLine() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        int databaseSizeBeforeUpdate = shippingLineRepository.findAll().size();

        // Update the shippingLine
        ShippingLine updatedShippingLine = shippingLineRepository.findById(shippingLine.getId()).get();
        // Disconnect from session so that the updates on updatedShippingLine are not directly saved in db
        em.detach(updatedShippingLine);
        updatedShippingLine
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .listprice(UPDATED_LISTPRICE)
            .listpriceincludestax(UPDATED_LISTPRICEINCLUDESTAX)
            .adjustments(UPDATED_ADJUSTMENTS)
            .taxlines(UPDATED_TAXLINES);
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(updatedShippingLine);

        restShippingLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shippingLineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShippingLine in the database
        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeUpdate);
        ShippingLine testShippingLine = shippingLineList.get(shippingLineList.size() - 1);
        assertThat(testShippingLine.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testShippingLine.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testShippingLine.getListprice()).isEqualTo(UPDATED_LISTPRICE);
        assertThat(testShippingLine.getListpriceincludestax()).isEqualTo(UPDATED_LISTPRICEINCLUDESTAX);
        assertThat(testShippingLine.getAdjustments()).isEqualTo(UPDATED_ADJUSTMENTS);
        assertThat(testShippingLine.getTaxlines()).isEqualTo(UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void putNonExistingShippingLine() throws Exception {
        int databaseSizeBeforeUpdate = shippingLineRepository.findAll().size();
        shippingLine.setId(count.incrementAndGet());

        // Create the ShippingLine
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shippingLineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingLine in the database
        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShippingLine() throws Exception {
        int databaseSizeBeforeUpdate = shippingLineRepository.findAll().size();
        shippingLine.setId(count.incrementAndGet());

        // Create the ShippingLine
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingLine in the database
        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShippingLine() throws Exception {
        int databaseSizeBeforeUpdate = shippingLineRepository.findAll().size();
        shippingLine.setId(count.incrementAndGet());

        // Create the ShippingLine
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingLineMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShippingLine in the database
        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShippingLineWithPatch() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        int databaseSizeBeforeUpdate = shippingLineRepository.findAll().size();

        // Update the shippingLine using partial update
        ShippingLine partialUpdatedShippingLine = new ShippingLine();
        partialUpdatedShippingLine.setId(shippingLine.getId());

        partialUpdatedShippingLine.listprice(UPDATED_LISTPRICE).listpriceincludestax(UPDATED_LISTPRICEINCLUDESTAX);

        restShippingLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShippingLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShippingLine))
            )
            .andExpect(status().isOk());

        // Validate the ShippingLine in the database
        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeUpdate);
        ShippingLine testShippingLine = shippingLineList.get(shippingLineList.size() - 1);
        assertThat(testShippingLine.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testShippingLine.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testShippingLine.getListprice()).isEqualTo(UPDATED_LISTPRICE);
        assertThat(testShippingLine.getListpriceincludestax()).isEqualTo(UPDATED_LISTPRICEINCLUDESTAX);
        assertThat(testShippingLine.getAdjustments()).isEqualTo(DEFAULT_ADJUSTMENTS);
        assertThat(testShippingLine.getTaxlines()).isEqualTo(DEFAULT_TAXLINES);
    }

    @Test
    @Transactional
    void fullUpdateShippingLineWithPatch() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        int databaseSizeBeforeUpdate = shippingLineRepository.findAll().size();

        // Update the shippingLine using partial update
        ShippingLine partialUpdatedShippingLine = new ShippingLine();
        partialUpdatedShippingLine.setId(shippingLine.getId());

        partialUpdatedShippingLine
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .listprice(UPDATED_LISTPRICE)
            .listpriceincludestax(UPDATED_LISTPRICEINCLUDESTAX)
            .adjustments(UPDATED_ADJUSTMENTS)
            .taxlines(UPDATED_TAXLINES);

        restShippingLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShippingLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShippingLine))
            )
            .andExpect(status().isOk());

        // Validate the ShippingLine in the database
        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeUpdate);
        ShippingLine testShippingLine = shippingLineList.get(shippingLineList.size() - 1);
        assertThat(testShippingLine.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testShippingLine.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testShippingLine.getListprice()).isEqualTo(UPDATED_LISTPRICE);
        assertThat(testShippingLine.getListpriceincludestax()).isEqualTo(UPDATED_LISTPRICEINCLUDESTAX);
        assertThat(testShippingLine.getAdjustments()).isEqualTo(UPDATED_ADJUSTMENTS);
        assertThat(testShippingLine.getTaxlines()).isEqualTo(UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void patchNonExistingShippingLine() throws Exception {
        int databaseSizeBeforeUpdate = shippingLineRepository.findAll().size();
        shippingLine.setId(count.incrementAndGet());

        // Create the ShippingLine
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShippingLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shippingLineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingLine in the database
        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShippingLine() throws Exception {
        int databaseSizeBeforeUpdate = shippingLineRepository.findAll().size();
        shippingLine.setId(count.incrementAndGet());

        // Create the ShippingLine
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShippingLine in the database
        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShippingLine() throws Exception {
        int databaseSizeBeforeUpdate = shippingLineRepository.findAll().size();
        shippingLine.setId(count.incrementAndGet());

        // Create the ShippingLine
        ShippingLineDTO shippingLineDTO = shippingLineMapper.toDto(shippingLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShippingLineMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shippingLineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShippingLine in the database
        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShippingLine() throws Exception {
        // Initialize the database
        shippingLineRepository.saveAndFlush(shippingLine);

        int databaseSizeBeforeDelete = shippingLineRepository.findAll().size();

        // Delete the shippingLine
        restShippingLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, shippingLine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShippingLine> shippingLineList = shippingLineRepository.findAll();
        assertThat(shippingLineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
