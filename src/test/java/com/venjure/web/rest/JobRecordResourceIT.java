package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.JobRecord;
import com.venjure.repository.JobRecordRepository;
import com.venjure.service.criteria.JobRecordCriteria;
import com.venjure.service.dto.JobRecordDTO;
import com.venjure.service.mapper.JobRecordMapper;
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
 * Integration tests for the {@link JobRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JobRecordResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_QUEUENAME = "AAAAAAAAAA";
    private static final String UPDATED_QUEUENAME = "BBBBBBBBBB";

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PROGRESS = 1;
    private static final Integer UPDATED_PROGRESS = 2;
    private static final Integer SMALLER_PROGRESS = 1 - 1;

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR = "AAAAAAAAAA";
    private static final String UPDATED_ERROR = "BBBBBBBBBB";

    private static final Instant DEFAULT_STARTEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SETTLEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SETTLEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ISSETTLED = false;
    private static final Boolean UPDATED_ISSETTLED = true;

    private static final Integer DEFAULT_RETRIES = 1;
    private static final Integer UPDATED_RETRIES = 2;
    private static final Integer SMALLER_RETRIES = 1 - 1;

    private static final Integer DEFAULT_ATTEMPTS = 1;
    private static final Integer UPDATED_ATTEMPTS = 2;
    private static final Integer SMALLER_ATTEMPTS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/job-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobRecordRepository jobRecordRepository;

    @Autowired
    private JobRecordMapper jobRecordMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobRecordMockMvc;

    private JobRecord jobRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobRecord createEntity(EntityManager em) {
        JobRecord jobRecord = new JobRecord()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .queuename(DEFAULT_QUEUENAME)
            .data(DEFAULT_DATA)
            .state(DEFAULT_STATE)
            .progress(DEFAULT_PROGRESS)
            .result(DEFAULT_RESULT)
            .error(DEFAULT_ERROR)
            .startedat(DEFAULT_STARTEDAT)
            .settledat(DEFAULT_SETTLEDAT)
            .issettled(DEFAULT_ISSETTLED)
            .retries(DEFAULT_RETRIES)
            .attempts(DEFAULT_ATTEMPTS);
        return jobRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobRecord createUpdatedEntity(EntityManager em) {
        JobRecord jobRecord = new JobRecord()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .queuename(UPDATED_QUEUENAME)
            .data(UPDATED_DATA)
            .state(UPDATED_STATE)
            .progress(UPDATED_PROGRESS)
            .result(UPDATED_RESULT)
            .error(UPDATED_ERROR)
            .startedat(UPDATED_STARTEDAT)
            .settledat(UPDATED_SETTLEDAT)
            .issettled(UPDATED_ISSETTLED)
            .retries(UPDATED_RETRIES)
            .attempts(UPDATED_ATTEMPTS);
        return jobRecord;
    }

    @BeforeEach
    public void initTest() {
        jobRecord = createEntity(em);
    }

    @Test
    @Transactional
    void createJobRecord() throws Exception {
        int databaseSizeBeforeCreate = jobRecordRepository.findAll().size();
        // Create the JobRecord
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);
        restJobRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the JobRecord in the database
        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeCreate + 1);
        JobRecord testJobRecord = jobRecordList.get(jobRecordList.size() - 1);
        assertThat(testJobRecord.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testJobRecord.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testJobRecord.getQueuename()).isEqualTo(DEFAULT_QUEUENAME);
        assertThat(testJobRecord.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testJobRecord.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testJobRecord.getProgress()).isEqualTo(DEFAULT_PROGRESS);
        assertThat(testJobRecord.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testJobRecord.getError()).isEqualTo(DEFAULT_ERROR);
        assertThat(testJobRecord.getStartedat()).isEqualTo(DEFAULT_STARTEDAT);
        assertThat(testJobRecord.getSettledat()).isEqualTo(DEFAULT_SETTLEDAT);
        assertThat(testJobRecord.getIssettled()).isEqualTo(DEFAULT_ISSETTLED);
        assertThat(testJobRecord.getRetries()).isEqualTo(DEFAULT_RETRIES);
        assertThat(testJobRecord.getAttempts()).isEqualTo(DEFAULT_ATTEMPTS);
    }

    @Test
    @Transactional
    void createJobRecordWithExistingId() throws Exception {
        // Create the JobRecord with an existing ID
        jobRecord.setId(1L);
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        int databaseSizeBeforeCreate = jobRecordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JobRecord in the database
        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRecordRepository.findAll().size();
        // set the field null
        jobRecord.setCreatedat(null);

        // Create the JobRecord, which fails.
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        restJobRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRecordDTO)))
            .andExpect(status().isBadRequest());

        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRecordRepository.findAll().size();
        // set the field null
        jobRecord.setUpdatedat(null);

        // Create the JobRecord, which fails.
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        restJobRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRecordDTO)))
            .andExpect(status().isBadRequest());

        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQueuenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRecordRepository.findAll().size();
        // set the field null
        jobRecord.setQueuename(null);

        // Create the JobRecord, which fails.
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        restJobRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRecordDTO)))
            .andExpect(status().isBadRequest());

        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRecordRepository.findAll().size();
        // set the field null
        jobRecord.setState(null);

        // Create the JobRecord, which fails.
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        restJobRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRecordDTO)))
            .andExpect(status().isBadRequest());

        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProgressIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRecordRepository.findAll().size();
        // set the field null
        jobRecord.setProgress(null);

        // Create the JobRecord, which fails.
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        restJobRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRecordDTO)))
            .andExpect(status().isBadRequest());

        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssettledIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRecordRepository.findAll().size();
        // set the field null
        jobRecord.setIssettled(null);

        // Create the JobRecord, which fails.
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        restJobRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRecordDTO)))
            .andExpect(status().isBadRequest());

        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRetriesIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRecordRepository.findAll().size();
        // set the field null
        jobRecord.setRetries(null);

        // Create the JobRecord, which fails.
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        restJobRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRecordDTO)))
            .andExpect(status().isBadRequest());

        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAttemptsIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRecordRepository.findAll().size();
        // set the field null
        jobRecord.setAttempts(null);

        // Create the JobRecord, which fails.
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        restJobRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRecordDTO)))
            .andExpect(status().isBadRequest());

        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJobRecords() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList
        restJobRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].queuename").value(hasItem(DEFAULT_QUEUENAME)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].error").value(hasItem(DEFAULT_ERROR)))
            .andExpect(jsonPath("$.[*].startedat").value(hasItem(DEFAULT_STARTEDAT.toString())))
            .andExpect(jsonPath("$.[*].settledat").value(hasItem(DEFAULT_SETTLEDAT.toString())))
            .andExpect(jsonPath("$.[*].issettled").value(hasItem(DEFAULT_ISSETTLED.booleanValue())))
            .andExpect(jsonPath("$.[*].retries").value(hasItem(DEFAULT_RETRIES)))
            .andExpect(jsonPath("$.[*].attempts").value(hasItem(DEFAULT_ATTEMPTS)));
    }

    @Test
    @Transactional
    void getJobRecord() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get the jobRecord
        restJobRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, jobRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobRecord.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.queuename").value(DEFAULT_QUEUENAME))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.progress").value(DEFAULT_PROGRESS))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT))
            .andExpect(jsonPath("$.error").value(DEFAULT_ERROR))
            .andExpect(jsonPath("$.startedat").value(DEFAULT_STARTEDAT.toString()))
            .andExpect(jsonPath("$.settledat").value(DEFAULT_SETTLEDAT.toString()))
            .andExpect(jsonPath("$.issettled").value(DEFAULT_ISSETTLED.booleanValue()))
            .andExpect(jsonPath("$.retries").value(DEFAULT_RETRIES))
            .andExpect(jsonPath("$.attempts").value(DEFAULT_ATTEMPTS));
    }

    @Test
    @Transactional
    void getJobRecordsByIdFiltering() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        Long id = jobRecord.getId();

        defaultJobRecordShouldBeFound("id.equals=" + id);
        defaultJobRecordShouldNotBeFound("id.notEquals=" + id);

        defaultJobRecordShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJobRecordShouldNotBeFound("id.greaterThan=" + id);

        defaultJobRecordShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJobRecordShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllJobRecordsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where createdat equals to DEFAULT_CREATEDAT
        defaultJobRecordShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the jobRecordList where createdat equals to UPDATED_CREATEDAT
        defaultJobRecordShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where createdat not equals to DEFAULT_CREATEDAT
        defaultJobRecordShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the jobRecordList where createdat not equals to UPDATED_CREATEDAT
        defaultJobRecordShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultJobRecordShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the jobRecordList where createdat equals to UPDATED_CREATEDAT
        defaultJobRecordShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where createdat is not null
        defaultJobRecordShouldBeFound("createdat.specified=true");

        // Get all the jobRecordList where createdat is null
        defaultJobRecordShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where updatedat equals to DEFAULT_UPDATEDAT
        defaultJobRecordShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the jobRecordList where updatedat equals to UPDATED_UPDATEDAT
        defaultJobRecordShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultJobRecordShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the jobRecordList where updatedat not equals to UPDATED_UPDATEDAT
        defaultJobRecordShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultJobRecordShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the jobRecordList where updatedat equals to UPDATED_UPDATEDAT
        defaultJobRecordShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where updatedat is not null
        defaultJobRecordShouldBeFound("updatedat.specified=true");

        // Get all the jobRecordList where updatedat is null
        defaultJobRecordShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsByQueuenameIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where queuename equals to DEFAULT_QUEUENAME
        defaultJobRecordShouldBeFound("queuename.equals=" + DEFAULT_QUEUENAME);

        // Get all the jobRecordList where queuename equals to UPDATED_QUEUENAME
        defaultJobRecordShouldNotBeFound("queuename.equals=" + UPDATED_QUEUENAME);
    }

    @Test
    @Transactional
    void getAllJobRecordsByQueuenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where queuename not equals to DEFAULT_QUEUENAME
        defaultJobRecordShouldNotBeFound("queuename.notEquals=" + DEFAULT_QUEUENAME);

        // Get all the jobRecordList where queuename not equals to UPDATED_QUEUENAME
        defaultJobRecordShouldBeFound("queuename.notEquals=" + UPDATED_QUEUENAME);
    }

    @Test
    @Transactional
    void getAllJobRecordsByQueuenameIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where queuename in DEFAULT_QUEUENAME or UPDATED_QUEUENAME
        defaultJobRecordShouldBeFound("queuename.in=" + DEFAULT_QUEUENAME + "," + UPDATED_QUEUENAME);

        // Get all the jobRecordList where queuename equals to UPDATED_QUEUENAME
        defaultJobRecordShouldNotBeFound("queuename.in=" + UPDATED_QUEUENAME);
    }

    @Test
    @Transactional
    void getAllJobRecordsByQueuenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where queuename is not null
        defaultJobRecordShouldBeFound("queuename.specified=true");

        // Get all the jobRecordList where queuename is null
        defaultJobRecordShouldNotBeFound("queuename.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsByQueuenameContainsSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where queuename contains DEFAULT_QUEUENAME
        defaultJobRecordShouldBeFound("queuename.contains=" + DEFAULT_QUEUENAME);

        // Get all the jobRecordList where queuename contains UPDATED_QUEUENAME
        defaultJobRecordShouldNotBeFound("queuename.contains=" + UPDATED_QUEUENAME);
    }

    @Test
    @Transactional
    void getAllJobRecordsByQueuenameNotContainsSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where queuename does not contain DEFAULT_QUEUENAME
        defaultJobRecordShouldNotBeFound("queuename.doesNotContain=" + DEFAULT_QUEUENAME);

        // Get all the jobRecordList where queuename does not contain UPDATED_QUEUENAME
        defaultJobRecordShouldBeFound("queuename.doesNotContain=" + UPDATED_QUEUENAME);
    }

    @Test
    @Transactional
    void getAllJobRecordsByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where data equals to DEFAULT_DATA
        defaultJobRecordShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the jobRecordList where data equals to UPDATED_DATA
        defaultJobRecordShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllJobRecordsByDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where data not equals to DEFAULT_DATA
        defaultJobRecordShouldNotBeFound("data.notEquals=" + DEFAULT_DATA);

        // Get all the jobRecordList where data not equals to UPDATED_DATA
        defaultJobRecordShouldBeFound("data.notEquals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllJobRecordsByDataIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where data in DEFAULT_DATA or UPDATED_DATA
        defaultJobRecordShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the jobRecordList where data equals to UPDATED_DATA
        defaultJobRecordShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllJobRecordsByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where data is not null
        defaultJobRecordShouldBeFound("data.specified=true");

        // Get all the jobRecordList where data is null
        defaultJobRecordShouldNotBeFound("data.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsByDataContainsSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where data contains DEFAULT_DATA
        defaultJobRecordShouldBeFound("data.contains=" + DEFAULT_DATA);

        // Get all the jobRecordList where data contains UPDATED_DATA
        defaultJobRecordShouldNotBeFound("data.contains=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllJobRecordsByDataNotContainsSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where data does not contain DEFAULT_DATA
        defaultJobRecordShouldNotBeFound("data.doesNotContain=" + DEFAULT_DATA);

        // Get all the jobRecordList where data does not contain UPDATED_DATA
        defaultJobRecordShouldBeFound("data.doesNotContain=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllJobRecordsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where state equals to DEFAULT_STATE
        defaultJobRecordShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the jobRecordList where state equals to UPDATED_STATE
        defaultJobRecordShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllJobRecordsByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where state not equals to DEFAULT_STATE
        defaultJobRecordShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the jobRecordList where state not equals to UPDATED_STATE
        defaultJobRecordShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllJobRecordsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where state in DEFAULT_STATE or UPDATED_STATE
        defaultJobRecordShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the jobRecordList where state equals to UPDATED_STATE
        defaultJobRecordShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllJobRecordsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where state is not null
        defaultJobRecordShouldBeFound("state.specified=true");

        // Get all the jobRecordList where state is null
        defaultJobRecordShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsByStateContainsSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where state contains DEFAULT_STATE
        defaultJobRecordShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the jobRecordList where state contains UPDATED_STATE
        defaultJobRecordShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllJobRecordsByStateNotContainsSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where state does not contain DEFAULT_STATE
        defaultJobRecordShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the jobRecordList where state does not contain UPDATED_STATE
        defaultJobRecordShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllJobRecordsByProgressIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where progress equals to DEFAULT_PROGRESS
        defaultJobRecordShouldBeFound("progress.equals=" + DEFAULT_PROGRESS);

        // Get all the jobRecordList where progress equals to UPDATED_PROGRESS
        defaultJobRecordShouldNotBeFound("progress.equals=" + UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByProgressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where progress not equals to DEFAULT_PROGRESS
        defaultJobRecordShouldNotBeFound("progress.notEquals=" + DEFAULT_PROGRESS);

        // Get all the jobRecordList where progress not equals to UPDATED_PROGRESS
        defaultJobRecordShouldBeFound("progress.notEquals=" + UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByProgressIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where progress in DEFAULT_PROGRESS or UPDATED_PROGRESS
        defaultJobRecordShouldBeFound("progress.in=" + DEFAULT_PROGRESS + "," + UPDATED_PROGRESS);

        // Get all the jobRecordList where progress equals to UPDATED_PROGRESS
        defaultJobRecordShouldNotBeFound("progress.in=" + UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByProgressIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where progress is not null
        defaultJobRecordShouldBeFound("progress.specified=true");

        // Get all the jobRecordList where progress is null
        defaultJobRecordShouldNotBeFound("progress.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsByProgressIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where progress is greater than or equal to DEFAULT_PROGRESS
        defaultJobRecordShouldBeFound("progress.greaterThanOrEqual=" + DEFAULT_PROGRESS);

        // Get all the jobRecordList where progress is greater than or equal to UPDATED_PROGRESS
        defaultJobRecordShouldNotBeFound("progress.greaterThanOrEqual=" + UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByProgressIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where progress is less than or equal to DEFAULT_PROGRESS
        defaultJobRecordShouldBeFound("progress.lessThanOrEqual=" + DEFAULT_PROGRESS);

        // Get all the jobRecordList where progress is less than or equal to SMALLER_PROGRESS
        defaultJobRecordShouldNotBeFound("progress.lessThanOrEqual=" + SMALLER_PROGRESS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByProgressIsLessThanSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where progress is less than DEFAULT_PROGRESS
        defaultJobRecordShouldNotBeFound("progress.lessThan=" + DEFAULT_PROGRESS);

        // Get all the jobRecordList where progress is less than UPDATED_PROGRESS
        defaultJobRecordShouldBeFound("progress.lessThan=" + UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByProgressIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where progress is greater than DEFAULT_PROGRESS
        defaultJobRecordShouldNotBeFound("progress.greaterThan=" + DEFAULT_PROGRESS);

        // Get all the jobRecordList where progress is greater than SMALLER_PROGRESS
        defaultJobRecordShouldBeFound("progress.greaterThan=" + SMALLER_PROGRESS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByResultIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where result equals to DEFAULT_RESULT
        defaultJobRecordShouldBeFound("result.equals=" + DEFAULT_RESULT);

        // Get all the jobRecordList where result equals to UPDATED_RESULT
        defaultJobRecordShouldNotBeFound("result.equals=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByResultIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where result not equals to DEFAULT_RESULT
        defaultJobRecordShouldNotBeFound("result.notEquals=" + DEFAULT_RESULT);

        // Get all the jobRecordList where result not equals to UPDATED_RESULT
        defaultJobRecordShouldBeFound("result.notEquals=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByResultIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where result in DEFAULT_RESULT or UPDATED_RESULT
        defaultJobRecordShouldBeFound("result.in=" + DEFAULT_RESULT + "," + UPDATED_RESULT);

        // Get all the jobRecordList where result equals to UPDATED_RESULT
        defaultJobRecordShouldNotBeFound("result.in=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where result is not null
        defaultJobRecordShouldBeFound("result.specified=true");

        // Get all the jobRecordList where result is null
        defaultJobRecordShouldNotBeFound("result.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsByResultContainsSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where result contains DEFAULT_RESULT
        defaultJobRecordShouldBeFound("result.contains=" + DEFAULT_RESULT);

        // Get all the jobRecordList where result contains UPDATED_RESULT
        defaultJobRecordShouldNotBeFound("result.contains=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByResultNotContainsSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where result does not contain DEFAULT_RESULT
        defaultJobRecordShouldNotBeFound("result.doesNotContain=" + DEFAULT_RESULT);

        // Get all the jobRecordList where result does not contain UPDATED_RESULT
        defaultJobRecordShouldBeFound("result.doesNotContain=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByErrorIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where error equals to DEFAULT_ERROR
        defaultJobRecordShouldBeFound("error.equals=" + DEFAULT_ERROR);

        // Get all the jobRecordList where error equals to UPDATED_ERROR
        defaultJobRecordShouldNotBeFound("error.equals=" + UPDATED_ERROR);
    }

    @Test
    @Transactional
    void getAllJobRecordsByErrorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where error not equals to DEFAULT_ERROR
        defaultJobRecordShouldNotBeFound("error.notEquals=" + DEFAULT_ERROR);

        // Get all the jobRecordList where error not equals to UPDATED_ERROR
        defaultJobRecordShouldBeFound("error.notEquals=" + UPDATED_ERROR);
    }

    @Test
    @Transactional
    void getAllJobRecordsByErrorIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where error in DEFAULT_ERROR or UPDATED_ERROR
        defaultJobRecordShouldBeFound("error.in=" + DEFAULT_ERROR + "," + UPDATED_ERROR);

        // Get all the jobRecordList where error equals to UPDATED_ERROR
        defaultJobRecordShouldNotBeFound("error.in=" + UPDATED_ERROR);
    }

    @Test
    @Transactional
    void getAllJobRecordsByErrorIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where error is not null
        defaultJobRecordShouldBeFound("error.specified=true");

        // Get all the jobRecordList where error is null
        defaultJobRecordShouldNotBeFound("error.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsByErrorContainsSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where error contains DEFAULT_ERROR
        defaultJobRecordShouldBeFound("error.contains=" + DEFAULT_ERROR);

        // Get all the jobRecordList where error contains UPDATED_ERROR
        defaultJobRecordShouldNotBeFound("error.contains=" + UPDATED_ERROR);
    }

    @Test
    @Transactional
    void getAllJobRecordsByErrorNotContainsSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where error does not contain DEFAULT_ERROR
        defaultJobRecordShouldNotBeFound("error.doesNotContain=" + DEFAULT_ERROR);

        // Get all the jobRecordList where error does not contain UPDATED_ERROR
        defaultJobRecordShouldBeFound("error.doesNotContain=" + UPDATED_ERROR);
    }

    @Test
    @Transactional
    void getAllJobRecordsByStartedatIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where startedat equals to DEFAULT_STARTEDAT
        defaultJobRecordShouldBeFound("startedat.equals=" + DEFAULT_STARTEDAT);

        // Get all the jobRecordList where startedat equals to UPDATED_STARTEDAT
        defaultJobRecordShouldNotBeFound("startedat.equals=" + UPDATED_STARTEDAT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByStartedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where startedat not equals to DEFAULT_STARTEDAT
        defaultJobRecordShouldNotBeFound("startedat.notEquals=" + DEFAULT_STARTEDAT);

        // Get all the jobRecordList where startedat not equals to UPDATED_STARTEDAT
        defaultJobRecordShouldBeFound("startedat.notEquals=" + UPDATED_STARTEDAT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByStartedatIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where startedat in DEFAULT_STARTEDAT or UPDATED_STARTEDAT
        defaultJobRecordShouldBeFound("startedat.in=" + DEFAULT_STARTEDAT + "," + UPDATED_STARTEDAT);

        // Get all the jobRecordList where startedat equals to UPDATED_STARTEDAT
        defaultJobRecordShouldNotBeFound("startedat.in=" + UPDATED_STARTEDAT);
    }

    @Test
    @Transactional
    void getAllJobRecordsByStartedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where startedat is not null
        defaultJobRecordShouldBeFound("startedat.specified=true");

        // Get all the jobRecordList where startedat is null
        defaultJobRecordShouldNotBeFound("startedat.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsBySettledatIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where settledat equals to DEFAULT_SETTLEDAT
        defaultJobRecordShouldBeFound("settledat.equals=" + DEFAULT_SETTLEDAT);

        // Get all the jobRecordList where settledat equals to UPDATED_SETTLEDAT
        defaultJobRecordShouldNotBeFound("settledat.equals=" + UPDATED_SETTLEDAT);
    }

    @Test
    @Transactional
    void getAllJobRecordsBySettledatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where settledat not equals to DEFAULT_SETTLEDAT
        defaultJobRecordShouldNotBeFound("settledat.notEquals=" + DEFAULT_SETTLEDAT);

        // Get all the jobRecordList where settledat not equals to UPDATED_SETTLEDAT
        defaultJobRecordShouldBeFound("settledat.notEquals=" + UPDATED_SETTLEDAT);
    }

    @Test
    @Transactional
    void getAllJobRecordsBySettledatIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where settledat in DEFAULT_SETTLEDAT or UPDATED_SETTLEDAT
        defaultJobRecordShouldBeFound("settledat.in=" + DEFAULT_SETTLEDAT + "," + UPDATED_SETTLEDAT);

        // Get all the jobRecordList where settledat equals to UPDATED_SETTLEDAT
        defaultJobRecordShouldNotBeFound("settledat.in=" + UPDATED_SETTLEDAT);
    }

    @Test
    @Transactional
    void getAllJobRecordsBySettledatIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where settledat is not null
        defaultJobRecordShouldBeFound("settledat.specified=true");

        // Get all the jobRecordList where settledat is null
        defaultJobRecordShouldNotBeFound("settledat.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsByIssettledIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where issettled equals to DEFAULT_ISSETTLED
        defaultJobRecordShouldBeFound("issettled.equals=" + DEFAULT_ISSETTLED);

        // Get all the jobRecordList where issettled equals to UPDATED_ISSETTLED
        defaultJobRecordShouldNotBeFound("issettled.equals=" + UPDATED_ISSETTLED);
    }

    @Test
    @Transactional
    void getAllJobRecordsByIssettledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where issettled not equals to DEFAULT_ISSETTLED
        defaultJobRecordShouldNotBeFound("issettled.notEquals=" + DEFAULT_ISSETTLED);

        // Get all the jobRecordList where issettled not equals to UPDATED_ISSETTLED
        defaultJobRecordShouldBeFound("issettled.notEquals=" + UPDATED_ISSETTLED);
    }

    @Test
    @Transactional
    void getAllJobRecordsByIssettledIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where issettled in DEFAULT_ISSETTLED or UPDATED_ISSETTLED
        defaultJobRecordShouldBeFound("issettled.in=" + DEFAULT_ISSETTLED + "," + UPDATED_ISSETTLED);

        // Get all the jobRecordList where issettled equals to UPDATED_ISSETTLED
        defaultJobRecordShouldNotBeFound("issettled.in=" + UPDATED_ISSETTLED);
    }

    @Test
    @Transactional
    void getAllJobRecordsByIssettledIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where issettled is not null
        defaultJobRecordShouldBeFound("issettled.specified=true");

        // Get all the jobRecordList where issettled is null
        defaultJobRecordShouldNotBeFound("issettled.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsByRetriesIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where retries equals to DEFAULT_RETRIES
        defaultJobRecordShouldBeFound("retries.equals=" + DEFAULT_RETRIES);

        // Get all the jobRecordList where retries equals to UPDATED_RETRIES
        defaultJobRecordShouldNotBeFound("retries.equals=" + UPDATED_RETRIES);
    }

    @Test
    @Transactional
    void getAllJobRecordsByRetriesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where retries not equals to DEFAULT_RETRIES
        defaultJobRecordShouldNotBeFound("retries.notEquals=" + DEFAULT_RETRIES);

        // Get all the jobRecordList where retries not equals to UPDATED_RETRIES
        defaultJobRecordShouldBeFound("retries.notEquals=" + UPDATED_RETRIES);
    }

    @Test
    @Transactional
    void getAllJobRecordsByRetriesIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where retries in DEFAULT_RETRIES or UPDATED_RETRIES
        defaultJobRecordShouldBeFound("retries.in=" + DEFAULT_RETRIES + "," + UPDATED_RETRIES);

        // Get all the jobRecordList where retries equals to UPDATED_RETRIES
        defaultJobRecordShouldNotBeFound("retries.in=" + UPDATED_RETRIES);
    }

    @Test
    @Transactional
    void getAllJobRecordsByRetriesIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where retries is not null
        defaultJobRecordShouldBeFound("retries.specified=true");

        // Get all the jobRecordList where retries is null
        defaultJobRecordShouldNotBeFound("retries.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsByRetriesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where retries is greater than or equal to DEFAULT_RETRIES
        defaultJobRecordShouldBeFound("retries.greaterThanOrEqual=" + DEFAULT_RETRIES);

        // Get all the jobRecordList where retries is greater than or equal to UPDATED_RETRIES
        defaultJobRecordShouldNotBeFound("retries.greaterThanOrEqual=" + UPDATED_RETRIES);
    }

    @Test
    @Transactional
    void getAllJobRecordsByRetriesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where retries is less than or equal to DEFAULT_RETRIES
        defaultJobRecordShouldBeFound("retries.lessThanOrEqual=" + DEFAULT_RETRIES);

        // Get all the jobRecordList where retries is less than or equal to SMALLER_RETRIES
        defaultJobRecordShouldNotBeFound("retries.lessThanOrEqual=" + SMALLER_RETRIES);
    }

    @Test
    @Transactional
    void getAllJobRecordsByRetriesIsLessThanSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where retries is less than DEFAULT_RETRIES
        defaultJobRecordShouldNotBeFound("retries.lessThan=" + DEFAULT_RETRIES);

        // Get all the jobRecordList where retries is less than UPDATED_RETRIES
        defaultJobRecordShouldBeFound("retries.lessThan=" + UPDATED_RETRIES);
    }

    @Test
    @Transactional
    void getAllJobRecordsByRetriesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where retries is greater than DEFAULT_RETRIES
        defaultJobRecordShouldNotBeFound("retries.greaterThan=" + DEFAULT_RETRIES);

        // Get all the jobRecordList where retries is greater than SMALLER_RETRIES
        defaultJobRecordShouldBeFound("retries.greaterThan=" + SMALLER_RETRIES);
    }

    @Test
    @Transactional
    void getAllJobRecordsByAttemptsIsEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where attempts equals to DEFAULT_ATTEMPTS
        defaultJobRecordShouldBeFound("attempts.equals=" + DEFAULT_ATTEMPTS);

        // Get all the jobRecordList where attempts equals to UPDATED_ATTEMPTS
        defaultJobRecordShouldNotBeFound("attempts.equals=" + UPDATED_ATTEMPTS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByAttemptsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where attempts not equals to DEFAULT_ATTEMPTS
        defaultJobRecordShouldNotBeFound("attempts.notEquals=" + DEFAULT_ATTEMPTS);

        // Get all the jobRecordList where attempts not equals to UPDATED_ATTEMPTS
        defaultJobRecordShouldBeFound("attempts.notEquals=" + UPDATED_ATTEMPTS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByAttemptsIsInShouldWork() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where attempts in DEFAULT_ATTEMPTS or UPDATED_ATTEMPTS
        defaultJobRecordShouldBeFound("attempts.in=" + DEFAULT_ATTEMPTS + "," + UPDATED_ATTEMPTS);

        // Get all the jobRecordList where attempts equals to UPDATED_ATTEMPTS
        defaultJobRecordShouldNotBeFound("attempts.in=" + UPDATED_ATTEMPTS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByAttemptsIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where attempts is not null
        defaultJobRecordShouldBeFound("attempts.specified=true");

        // Get all the jobRecordList where attempts is null
        defaultJobRecordShouldNotBeFound("attempts.specified=false");
    }

    @Test
    @Transactional
    void getAllJobRecordsByAttemptsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where attempts is greater than or equal to DEFAULT_ATTEMPTS
        defaultJobRecordShouldBeFound("attempts.greaterThanOrEqual=" + DEFAULT_ATTEMPTS);

        // Get all the jobRecordList where attempts is greater than or equal to UPDATED_ATTEMPTS
        defaultJobRecordShouldNotBeFound("attempts.greaterThanOrEqual=" + UPDATED_ATTEMPTS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByAttemptsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where attempts is less than or equal to DEFAULT_ATTEMPTS
        defaultJobRecordShouldBeFound("attempts.lessThanOrEqual=" + DEFAULT_ATTEMPTS);

        // Get all the jobRecordList where attempts is less than or equal to SMALLER_ATTEMPTS
        defaultJobRecordShouldNotBeFound("attempts.lessThanOrEqual=" + SMALLER_ATTEMPTS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByAttemptsIsLessThanSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where attempts is less than DEFAULT_ATTEMPTS
        defaultJobRecordShouldNotBeFound("attempts.lessThan=" + DEFAULT_ATTEMPTS);

        // Get all the jobRecordList where attempts is less than UPDATED_ATTEMPTS
        defaultJobRecordShouldBeFound("attempts.lessThan=" + UPDATED_ATTEMPTS);
    }

    @Test
    @Transactional
    void getAllJobRecordsByAttemptsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        // Get all the jobRecordList where attempts is greater than DEFAULT_ATTEMPTS
        defaultJobRecordShouldNotBeFound("attempts.greaterThan=" + DEFAULT_ATTEMPTS);

        // Get all the jobRecordList where attempts is greater than SMALLER_ATTEMPTS
        defaultJobRecordShouldBeFound("attempts.greaterThan=" + SMALLER_ATTEMPTS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobRecordShouldBeFound(String filter) throws Exception {
        restJobRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].queuename").value(hasItem(DEFAULT_QUEUENAME)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].error").value(hasItem(DEFAULT_ERROR)))
            .andExpect(jsonPath("$.[*].startedat").value(hasItem(DEFAULT_STARTEDAT.toString())))
            .andExpect(jsonPath("$.[*].settledat").value(hasItem(DEFAULT_SETTLEDAT.toString())))
            .andExpect(jsonPath("$.[*].issettled").value(hasItem(DEFAULT_ISSETTLED.booleanValue())))
            .andExpect(jsonPath("$.[*].retries").value(hasItem(DEFAULT_RETRIES)))
            .andExpect(jsonPath("$.[*].attempts").value(hasItem(DEFAULT_ATTEMPTS)));

        // Check, that the count call also returns 1
        restJobRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobRecordShouldNotBeFound(String filter) throws Exception {
        restJobRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingJobRecord() throws Exception {
        // Get the jobRecord
        restJobRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJobRecord() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        int databaseSizeBeforeUpdate = jobRecordRepository.findAll().size();

        // Update the jobRecord
        JobRecord updatedJobRecord = jobRecordRepository.findById(jobRecord.getId()).get();
        // Disconnect from session so that the updates on updatedJobRecord are not directly saved in db
        em.detach(updatedJobRecord);
        updatedJobRecord
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .queuename(UPDATED_QUEUENAME)
            .data(UPDATED_DATA)
            .state(UPDATED_STATE)
            .progress(UPDATED_PROGRESS)
            .result(UPDATED_RESULT)
            .error(UPDATED_ERROR)
            .startedat(UPDATED_STARTEDAT)
            .settledat(UPDATED_SETTLEDAT)
            .issettled(UPDATED_ISSETTLED)
            .retries(UPDATED_RETRIES)
            .attempts(UPDATED_ATTEMPTS);
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(updatedJobRecord);

        restJobRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the JobRecord in the database
        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeUpdate);
        JobRecord testJobRecord = jobRecordList.get(jobRecordList.size() - 1);
        assertThat(testJobRecord.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testJobRecord.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testJobRecord.getQueuename()).isEqualTo(UPDATED_QUEUENAME);
        assertThat(testJobRecord.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testJobRecord.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testJobRecord.getProgress()).isEqualTo(UPDATED_PROGRESS);
        assertThat(testJobRecord.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testJobRecord.getError()).isEqualTo(UPDATED_ERROR);
        assertThat(testJobRecord.getStartedat()).isEqualTo(UPDATED_STARTEDAT);
        assertThat(testJobRecord.getSettledat()).isEqualTo(UPDATED_SETTLEDAT);
        assertThat(testJobRecord.getIssettled()).isEqualTo(UPDATED_ISSETTLED);
        assertThat(testJobRecord.getRetries()).isEqualTo(UPDATED_RETRIES);
        assertThat(testJobRecord.getAttempts()).isEqualTo(UPDATED_ATTEMPTS);
    }

    @Test
    @Transactional
    void putNonExistingJobRecord() throws Exception {
        int databaseSizeBeforeUpdate = jobRecordRepository.findAll().size();
        jobRecord.setId(count.incrementAndGet());

        // Create the JobRecord
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobRecord in the database
        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobRecord() throws Exception {
        int databaseSizeBeforeUpdate = jobRecordRepository.findAll().size();
        jobRecord.setId(count.incrementAndGet());

        // Create the JobRecord
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobRecord in the database
        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobRecord() throws Exception {
        int databaseSizeBeforeUpdate = jobRecordRepository.findAll().size();
        jobRecord.setId(count.incrementAndGet());

        // Create the JobRecord
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRecordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobRecord in the database
        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJobRecordWithPatch() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        int databaseSizeBeforeUpdate = jobRecordRepository.findAll().size();

        // Update the jobRecord using partial update
        JobRecord partialUpdatedJobRecord = new JobRecord();
        partialUpdatedJobRecord.setId(jobRecord.getId());

        partialUpdatedJobRecord
            .createdat(UPDATED_CREATEDAT)
            .data(UPDATED_DATA)
            .state(UPDATED_STATE)
            .progress(UPDATED_PROGRESS)
            .result(UPDATED_RESULT)
            .startedat(UPDATED_STARTEDAT)
            .settledat(UPDATED_SETTLEDAT)
            .attempts(UPDATED_ATTEMPTS);

        restJobRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobRecord))
            )
            .andExpect(status().isOk());

        // Validate the JobRecord in the database
        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeUpdate);
        JobRecord testJobRecord = jobRecordList.get(jobRecordList.size() - 1);
        assertThat(testJobRecord.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testJobRecord.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testJobRecord.getQueuename()).isEqualTo(DEFAULT_QUEUENAME);
        assertThat(testJobRecord.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testJobRecord.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testJobRecord.getProgress()).isEqualTo(UPDATED_PROGRESS);
        assertThat(testJobRecord.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testJobRecord.getError()).isEqualTo(DEFAULT_ERROR);
        assertThat(testJobRecord.getStartedat()).isEqualTo(UPDATED_STARTEDAT);
        assertThat(testJobRecord.getSettledat()).isEqualTo(UPDATED_SETTLEDAT);
        assertThat(testJobRecord.getIssettled()).isEqualTo(DEFAULT_ISSETTLED);
        assertThat(testJobRecord.getRetries()).isEqualTo(DEFAULT_RETRIES);
        assertThat(testJobRecord.getAttempts()).isEqualTo(UPDATED_ATTEMPTS);
    }

    @Test
    @Transactional
    void fullUpdateJobRecordWithPatch() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        int databaseSizeBeforeUpdate = jobRecordRepository.findAll().size();

        // Update the jobRecord using partial update
        JobRecord partialUpdatedJobRecord = new JobRecord();
        partialUpdatedJobRecord.setId(jobRecord.getId());

        partialUpdatedJobRecord
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .queuename(UPDATED_QUEUENAME)
            .data(UPDATED_DATA)
            .state(UPDATED_STATE)
            .progress(UPDATED_PROGRESS)
            .result(UPDATED_RESULT)
            .error(UPDATED_ERROR)
            .startedat(UPDATED_STARTEDAT)
            .settledat(UPDATED_SETTLEDAT)
            .issettled(UPDATED_ISSETTLED)
            .retries(UPDATED_RETRIES)
            .attempts(UPDATED_ATTEMPTS);

        restJobRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobRecord))
            )
            .andExpect(status().isOk());

        // Validate the JobRecord in the database
        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeUpdate);
        JobRecord testJobRecord = jobRecordList.get(jobRecordList.size() - 1);
        assertThat(testJobRecord.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testJobRecord.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testJobRecord.getQueuename()).isEqualTo(UPDATED_QUEUENAME);
        assertThat(testJobRecord.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testJobRecord.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testJobRecord.getProgress()).isEqualTo(UPDATED_PROGRESS);
        assertThat(testJobRecord.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testJobRecord.getError()).isEqualTo(UPDATED_ERROR);
        assertThat(testJobRecord.getStartedat()).isEqualTo(UPDATED_STARTEDAT);
        assertThat(testJobRecord.getSettledat()).isEqualTo(UPDATED_SETTLEDAT);
        assertThat(testJobRecord.getIssettled()).isEqualTo(UPDATED_ISSETTLED);
        assertThat(testJobRecord.getRetries()).isEqualTo(UPDATED_RETRIES);
        assertThat(testJobRecord.getAttempts()).isEqualTo(UPDATED_ATTEMPTS);
    }

    @Test
    @Transactional
    void patchNonExistingJobRecord() throws Exception {
        int databaseSizeBeforeUpdate = jobRecordRepository.findAll().size();
        jobRecord.setId(count.incrementAndGet());

        // Create the JobRecord
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobRecord in the database
        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobRecord() throws Exception {
        int databaseSizeBeforeUpdate = jobRecordRepository.findAll().size();
        jobRecord.setId(count.incrementAndGet());

        // Create the JobRecord
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobRecord in the database
        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobRecord() throws Exception {
        int databaseSizeBeforeUpdate = jobRecordRepository.findAll().size();
        jobRecord.setId(count.incrementAndGet());

        // Create the JobRecord
        JobRecordDTO jobRecordDTO = jobRecordMapper.toDto(jobRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobRecordMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jobRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobRecord in the database
        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJobRecord() throws Exception {
        // Initialize the database
        jobRecordRepository.saveAndFlush(jobRecord);

        int databaseSizeBeforeDelete = jobRecordRepository.findAll().size();

        // Delete the jobRecord
        restJobRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobRecord> jobRecordList = jobRecordRepository.findAll();
        assertThat(jobRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
