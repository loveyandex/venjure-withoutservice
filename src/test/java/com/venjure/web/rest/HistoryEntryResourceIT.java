package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Administrator;
import com.venjure.domain.Customer;
import com.venjure.domain.HistoryEntry;
import com.venjure.domain.Jorder;
import com.venjure.repository.HistoryEntryRepository;
import com.venjure.service.criteria.HistoryEntryCriteria;
import com.venjure.service.dto.HistoryEntryDTO;
import com.venjure.service.mapper.HistoryEntryMapper;
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
 * Integration tests for the {@link HistoryEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoryEntryResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ISPUBLIC = false;
    private static final Boolean UPDATED_ISPUBLIC = true;

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_DISCRIMINATOR = "AAAAAAAAAA";
    private static final String UPDATED_DISCRIMINATOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/history-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoryEntryRepository historyEntryRepository;

    @Autowired
    private HistoryEntryMapper historyEntryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoryEntryMockMvc;

    private HistoryEntry historyEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoryEntry createEntity(EntityManager em) {
        HistoryEntry historyEntry = new HistoryEntry()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .type(DEFAULT_TYPE)
            .ispublic(DEFAULT_ISPUBLIC)
            .data(DEFAULT_DATA)
            .discriminator(DEFAULT_DISCRIMINATOR);
        return historyEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoryEntry createUpdatedEntity(EntityManager em) {
        HistoryEntry historyEntry = new HistoryEntry()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .type(UPDATED_TYPE)
            .ispublic(UPDATED_ISPUBLIC)
            .data(UPDATED_DATA)
            .discriminator(UPDATED_DISCRIMINATOR);
        return historyEntry;
    }

    @BeforeEach
    public void initTest() {
        historyEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createHistoryEntry() throws Exception {
        int databaseSizeBeforeCreate = historyEntryRepository.findAll().size();
        // Create the HistoryEntry
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);
        restHistoryEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HistoryEntry in the database
        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeCreate + 1);
        HistoryEntry testHistoryEntry = historyEntryList.get(historyEntryList.size() - 1);
        assertThat(testHistoryEntry.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testHistoryEntry.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testHistoryEntry.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testHistoryEntry.getIspublic()).isEqualTo(DEFAULT_ISPUBLIC);
        assertThat(testHistoryEntry.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testHistoryEntry.getDiscriminator()).isEqualTo(DEFAULT_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void createHistoryEntryWithExistingId() throws Exception {
        // Create the HistoryEntry with an existing ID
        historyEntry.setId(1L);
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        int databaseSizeBeforeCreate = historyEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoryEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryEntry in the database
        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyEntryRepository.findAll().size();
        // set the field null
        historyEntry.setCreatedat(null);

        // Create the HistoryEntry, which fails.
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        restHistoryEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyEntryRepository.findAll().size();
        // set the field null
        historyEntry.setUpdatedat(null);

        // Create the HistoryEntry, which fails.
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        restHistoryEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyEntryRepository.findAll().size();
        // set the field null
        historyEntry.setType(null);

        // Create the HistoryEntry, which fails.
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        restHistoryEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIspublicIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyEntryRepository.findAll().size();
        // set the field null
        historyEntry.setIspublic(null);

        // Create the HistoryEntry, which fails.
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        restHistoryEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyEntryRepository.findAll().size();
        // set the field null
        historyEntry.setData(null);

        // Create the HistoryEntry, which fails.
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        restHistoryEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiscriminatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = historyEntryRepository.findAll().size();
        // set the field null
        historyEntry.setDiscriminator(null);

        // Create the HistoryEntry, which fails.
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        restHistoryEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isBadRequest());

        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoryEntries() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList
        restHistoryEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historyEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].ispublic").value(hasItem(DEFAULT_ISPUBLIC.booleanValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].discriminator").value(hasItem(DEFAULT_DISCRIMINATOR)));
    }

    @Test
    @Transactional
    void getHistoryEntry() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get the historyEntry
        restHistoryEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, historyEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historyEntry.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.ispublic").value(DEFAULT_ISPUBLIC.booleanValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA))
            .andExpect(jsonPath("$.discriminator").value(DEFAULT_DISCRIMINATOR));
    }

    @Test
    @Transactional
    void getHistoryEntriesByIdFiltering() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        Long id = historyEntry.getId();

        defaultHistoryEntryShouldBeFound("id.equals=" + id);
        defaultHistoryEntryShouldNotBeFound("id.notEquals=" + id);

        defaultHistoryEntryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHistoryEntryShouldNotBeFound("id.greaterThan=" + id);

        defaultHistoryEntryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHistoryEntryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where createdat equals to DEFAULT_CREATEDAT
        defaultHistoryEntryShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the historyEntryList where createdat equals to UPDATED_CREATEDAT
        defaultHistoryEntryShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where createdat not equals to DEFAULT_CREATEDAT
        defaultHistoryEntryShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the historyEntryList where createdat not equals to UPDATED_CREATEDAT
        defaultHistoryEntryShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultHistoryEntryShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the historyEntryList where createdat equals to UPDATED_CREATEDAT
        defaultHistoryEntryShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where createdat is not null
        defaultHistoryEntryShouldBeFound("createdat.specified=true");

        // Get all the historyEntryList where createdat is null
        defaultHistoryEntryShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where updatedat equals to DEFAULT_UPDATEDAT
        defaultHistoryEntryShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the historyEntryList where updatedat equals to UPDATED_UPDATEDAT
        defaultHistoryEntryShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultHistoryEntryShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the historyEntryList where updatedat not equals to UPDATED_UPDATEDAT
        defaultHistoryEntryShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultHistoryEntryShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the historyEntryList where updatedat equals to UPDATED_UPDATEDAT
        defaultHistoryEntryShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where updatedat is not null
        defaultHistoryEntryShouldBeFound("updatedat.specified=true");

        // Get all the historyEntryList where updatedat is null
        defaultHistoryEntryShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where type equals to DEFAULT_TYPE
        defaultHistoryEntryShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the historyEntryList where type equals to UPDATED_TYPE
        defaultHistoryEntryShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where type not equals to DEFAULT_TYPE
        defaultHistoryEntryShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the historyEntryList where type not equals to UPDATED_TYPE
        defaultHistoryEntryShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultHistoryEntryShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the historyEntryList where type equals to UPDATED_TYPE
        defaultHistoryEntryShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where type is not null
        defaultHistoryEntryShouldBeFound("type.specified=true");

        // Get all the historyEntryList where type is null
        defaultHistoryEntryShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByTypeContainsSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where type contains DEFAULT_TYPE
        defaultHistoryEntryShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the historyEntryList where type contains UPDATED_TYPE
        defaultHistoryEntryShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where type does not contain DEFAULT_TYPE
        defaultHistoryEntryShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the historyEntryList where type does not contain UPDATED_TYPE
        defaultHistoryEntryShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByIspublicIsEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where ispublic equals to DEFAULT_ISPUBLIC
        defaultHistoryEntryShouldBeFound("ispublic.equals=" + DEFAULT_ISPUBLIC);

        // Get all the historyEntryList where ispublic equals to UPDATED_ISPUBLIC
        defaultHistoryEntryShouldNotBeFound("ispublic.equals=" + UPDATED_ISPUBLIC);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByIspublicIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where ispublic not equals to DEFAULT_ISPUBLIC
        defaultHistoryEntryShouldNotBeFound("ispublic.notEquals=" + DEFAULT_ISPUBLIC);

        // Get all the historyEntryList where ispublic not equals to UPDATED_ISPUBLIC
        defaultHistoryEntryShouldBeFound("ispublic.notEquals=" + UPDATED_ISPUBLIC);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByIspublicIsInShouldWork() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where ispublic in DEFAULT_ISPUBLIC or UPDATED_ISPUBLIC
        defaultHistoryEntryShouldBeFound("ispublic.in=" + DEFAULT_ISPUBLIC + "," + UPDATED_ISPUBLIC);

        // Get all the historyEntryList where ispublic equals to UPDATED_ISPUBLIC
        defaultHistoryEntryShouldNotBeFound("ispublic.in=" + UPDATED_ISPUBLIC);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByIspublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where ispublic is not null
        defaultHistoryEntryShouldBeFound("ispublic.specified=true");

        // Get all the historyEntryList where ispublic is null
        defaultHistoryEntryShouldNotBeFound("ispublic.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where data equals to DEFAULT_DATA
        defaultHistoryEntryShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the historyEntryList where data equals to UPDATED_DATA
        defaultHistoryEntryShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where data not equals to DEFAULT_DATA
        defaultHistoryEntryShouldNotBeFound("data.notEquals=" + DEFAULT_DATA);

        // Get all the historyEntryList where data not equals to UPDATED_DATA
        defaultHistoryEntryShouldBeFound("data.notEquals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByDataIsInShouldWork() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where data in DEFAULT_DATA or UPDATED_DATA
        defaultHistoryEntryShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the historyEntryList where data equals to UPDATED_DATA
        defaultHistoryEntryShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where data is not null
        defaultHistoryEntryShouldBeFound("data.specified=true");

        // Get all the historyEntryList where data is null
        defaultHistoryEntryShouldNotBeFound("data.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByDataContainsSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where data contains DEFAULT_DATA
        defaultHistoryEntryShouldBeFound("data.contains=" + DEFAULT_DATA);

        // Get all the historyEntryList where data contains UPDATED_DATA
        defaultHistoryEntryShouldNotBeFound("data.contains=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByDataNotContainsSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where data does not contain DEFAULT_DATA
        defaultHistoryEntryShouldNotBeFound("data.doesNotContain=" + DEFAULT_DATA);

        // Get all the historyEntryList where data does not contain UPDATED_DATA
        defaultHistoryEntryShouldBeFound("data.doesNotContain=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByDiscriminatorIsEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where discriminator equals to DEFAULT_DISCRIMINATOR
        defaultHistoryEntryShouldBeFound("discriminator.equals=" + DEFAULT_DISCRIMINATOR);

        // Get all the historyEntryList where discriminator equals to UPDATED_DISCRIMINATOR
        defaultHistoryEntryShouldNotBeFound("discriminator.equals=" + UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByDiscriminatorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where discriminator not equals to DEFAULT_DISCRIMINATOR
        defaultHistoryEntryShouldNotBeFound("discriminator.notEquals=" + DEFAULT_DISCRIMINATOR);

        // Get all the historyEntryList where discriminator not equals to UPDATED_DISCRIMINATOR
        defaultHistoryEntryShouldBeFound("discriminator.notEquals=" + UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByDiscriminatorIsInShouldWork() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where discriminator in DEFAULT_DISCRIMINATOR or UPDATED_DISCRIMINATOR
        defaultHistoryEntryShouldBeFound("discriminator.in=" + DEFAULT_DISCRIMINATOR + "," + UPDATED_DISCRIMINATOR);

        // Get all the historyEntryList where discriminator equals to UPDATED_DISCRIMINATOR
        defaultHistoryEntryShouldNotBeFound("discriminator.in=" + UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByDiscriminatorIsNullOrNotNull() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where discriminator is not null
        defaultHistoryEntryShouldBeFound("discriminator.specified=true");

        // Get all the historyEntryList where discriminator is null
        defaultHistoryEntryShouldNotBeFound("discriminator.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByDiscriminatorContainsSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where discriminator contains DEFAULT_DISCRIMINATOR
        defaultHistoryEntryShouldBeFound("discriminator.contains=" + DEFAULT_DISCRIMINATOR);

        // Get all the historyEntryList where discriminator contains UPDATED_DISCRIMINATOR
        defaultHistoryEntryShouldNotBeFound("discriminator.contains=" + UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByDiscriminatorNotContainsSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        // Get all the historyEntryList where discriminator does not contain DEFAULT_DISCRIMINATOR
        defaultHistoryEntryShouldNotBeFound("discriminator.doesNotContain=" + DEFAULT_DISCRIMINATOR);

        // Get all the historyEntryList where discriminator does not contain UPDATED_DISCRIMINATOR
        defaultHistoryEntryShouldBeFound("discriminator.doesNotContain=" + UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByAdministratorIsEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);
        Administrator administrator = AdministratorResourceIT.createEntity(em);
        em.persist(administrator);
        em.flush();
        historyEntry.setAdministrator(administrator);
        historyEntryRepository.saveAndFlush(historyEntry);
        Long administratorId = administrator.getId();

        // Get all the historyEntryList where administrator equals to administratorId
        defaultHistoryEntryShouldBeFound("administratorId.equals=" + administratorId);

        // Get all the historyEntryList where administrator equals to (administratorId + 1)
        defaultHistoryEntryShouldNotBeFound("administratorId.equals=" + (administratorId + 1));
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        historyEntry.setCustomer(customer);
        historyEntryRepository.saveAndFlush(historyEntry);
        Long customerId = customer.getId();

        // Get all the historyEntryList where customer equals to customerId
        defaultHistoryEntryShouldBeFound("customerId.equals=" + customerId);

        // Get all the historyEntryList where customer equals to (customerId + 1)
        defaultHistoryEntryShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllHistoryEntriesByJorderIsEqualToSomething() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);
        Jorder jorder = JorderResourceIT.createEntity(em);
        em.persist(jorder);
        em.flush();
        historyEntry.setJorder(jorder);
        historyEntryRepository.saveAndFlush(historyEntry);
        Long jorderId = jorder.getId();

        // Get all the historyEntryList where jorder equals to jorderId
        defaultHistoryEntryShouldBeFound("jorderId.equals=" + jorderId);

        // Get all the historyEntryList where jorder equals to (jorderId + 1)
        defaultHistoryEntryShouldNotBeFound("jorderId.equals=" + (jorderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoryEntryShouldBeFound(String filter) throws Exception {
        restHistoryEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historyEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].ispublic").value(hasItem(DEFAULT_ISPUBLIC.booleanValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].discriminator").value(hasItem(DEFAULT_DISCRIMINATOR)));

        // Check, that the count call also returns 1
        restHistoryEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoryEntryShouldNotBeFound(String filter) throws Exception {
        restHistoryEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoryEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHistoryEntry() throws Exception {
        // Get the historyEntry
        restHistoryEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHistoryEntry() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        int databaseSizeBeforeUpdate = historyEntryRepository.findAll().size();

        // Update the historyEntry
        HistoryEntry updatedHistoryEntry = historyEntryRepository.findById(historyEntry.getId()).get();
        // Disconnect from session so that the updates on updatedHistoryEntry are not directly saved in db
        em.detach(updatedHistoryEntry);
        updatedHistoryEntry
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .type(UPDATED_TYPE)
            .ispublic(UPDATED_ISPUBLIC)
            .data(UPDATED_DATA)
            .discriminator(UPDATED_DISCRIMINATOR);
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(updatedHistoryEntry);

        restHistoryEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historyEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoryEntry in the database
        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeUpdate);
        HistoryEntry testHistoryEntry = historyEntryList.get(historyEntryList.size() - 1);
        assertThat(testHistoryEntry.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testHistoryEntry.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testHistoryEntry.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHistoryEntry.getIspublic()).isEqualTo(UPDATED_ISPUBLIC);
        assertThat(testHistoryEntry.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testHistoryEntry.getDiscriminator()).isEqualTo(UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void putNonExistingHistoryEntry() throws Exception {
        int databaseSizeBeforeUpdate = historyEntryRepository.findAll().size();
        historyEntry.setId(count.incrementAndGet());

        // Create the HistoryEntry
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historyEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryEntry in the database
        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoryEntry() throws Exception {
        int databaseSizeBeforeUpdate = historyEntryRepository.findAll().size();
        historyEntry.setId(count.incrementAndGet());

        // Create the HistoryEntry
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryEntry in the database
        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoryEntry() throws Exception {
        int databaseSizeBeforeUpdate = historyEntryRepository.findAll().size();
        historyEntry.setId(count.incrementAndGet());

        // Create the HistoryEntry
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryEntryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoryEntry in the database
        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoryEntryWithPatch() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        int databaseSizeBeforeUpdate = historyEntryRepository.findAll().size();

        // Update the historyEntry using partial update
        HistoryEntry partialUpdatedHistoryEntry = new HistoryEntry();
        partialUpdatedHistoryEntry.setId(historyEntry.getId());

        partialUpdatedHistoryEntry
            .createdat(UPDATED_CREATEDAT)
            .type(UPDATED_TYPE)
            .ispublic(UPDATED_ISPUBLIC)
            .discriminator(UPDATED_DISCRIMINATOR);

        restHistoryEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoryEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoryEntry))
            )
            .andExpect(status().isOk());

        // Validate the HistoryEntry in the database
        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeUpdate);
        HistoryEntry testHistoryEntry = historyEntryList.get(historyEntryList.size() - 1);
        assertThat(testHistoryEntry.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testHistoryEntry.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testHistoryEntry.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHistoryEntry.getIspublic()).isEqualTo(UPDATED_ISPUBLIC);
        assertThat(testHistoryEntry.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testHistoryEntry.getDiscriminator()).isEqualTo(UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void fullUpdateHistoryEntryWithPatch() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        int databaseSizeBeforeUpdate = historyEntryRepository.findAll().size();

        // Update the historyEntry using partial update
        HistoryEntry partialUpdatedHistoryEntry = new HistoryEntry();
        partialUpdatedHistoryEntry.setId(historyEntry.getId());

        partialUpdatedHistoryEntry
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .type(UPDATED_TYPE)
            .ispublic(UPDATED_ISPUBLIC)
            .data(UPDATED_DATA)
            .discriminator(UPDATED_DISCRIMINATOR);

        restHistoryEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoryEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoryEntry))
            )
            .andExpect(status().isOk());

        // Validate the HistoryEntry in the database
        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeUpdate);
        HistoryEntry testHistoryEntry = historyEntryList.get(historyEntryList.size() - 1);
        assertThat(testHistoryEntry.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testHistoryEntry.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testHistoryEntry.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHistoryEntry.getIspublic()).isEqualTo(UPDATED_ISPUBLIC);
        assertThat(testHistoryEntry.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testHistoryEntry.getDiscriminator()).isEqualTo(UPDATED_DISCRIMINATOR);
    }

    @Test
    @Transactional
    void patchNonExistingHistoryEntry() throws Exception {
        int databaseSizeBeforeUpdate = historyEntryRepository.findAll().size();
        historyEntry.setId(count.incrementAndGet());

        // Create the HistoryEntry
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoryEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historyEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryEntry in the database
        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoryEntry() throws Exception {
        int databaseSizeBeforeUpdate = historyEntryRepository.findAll().size();
        historyEntry.setId(count.incrementAndGet());

        // Create the HistoryEntry
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoryEntry in the database
        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoryEntry() throws Exception {
        int databaseSizeBeforeUpdate = historyEntryRepository.findAll().size();
        historyEntry.setId(count.incrementAndGet());

        // Create the HistoryEntry
        HistoryEntryDTO historyEntryDTO = historyEntryMapper.toDto(historyEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoryEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historyEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoryEntry in the database
        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoryEntry() throws Exception {
        // Initialize the database
        historyEntryRepository.saveAndFlush(historyEntry);

        int databaseSizeBeforeDelete = historyEntryRepository.findAll().size();

        // Delete the historyEntry
        restHistoryEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, historyEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoryEntry> historyEntryList = historyEntryRepository.findAll();
        assertThat(historyEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
