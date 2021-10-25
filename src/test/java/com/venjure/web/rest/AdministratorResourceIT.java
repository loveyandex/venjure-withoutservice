package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Administrator;
import com.venjure.domain.HistoryEntry;
import com.venjure.domain.User;
import com.venjure.repository.AdministratorRepository;
import com.venjure.service.criteria.AdministratorCriteria;
import com.venjure.service.dto.AdministratorDTO;
import com.venjure.service.mapper.AdministratorMapper;
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
 * Integration tests for the {@link AdministratorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdministratorResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAILADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAILADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/administrators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private AdministratorMapper administratorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdministratorMockMvc;

    private Administrator administrator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrator createEntity(EntityManager em) {
        Administrator administrator = new Administrator()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .deletedat(DEFAULT_DELETEDAT)
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .emailaddress(DEFAULT_EMAILADDRESS);
        return administrator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrator createUpdatedEntity(EntityManager em) {
        Administrator administrator = new Administrator()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .emailaddress(UPDATED_EMAILADDRESS);
        return administrator;
    }

    @BeforeEach
    public void initTest() {
        administrator = createEntity(em);
    }

    @Test
    @Transactional
    void createAdministrator() throws Exception {
        int databaseSizeBeforeCreate = administratorRepository.findAll().size();
        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);
        restAdministratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeCreate + 1);
        Administrator testAdministrator = administratorList.get(administratorList.size() - 1);
        assertThat(testAdministrator.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testAdministrator.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testAdministrator.getDeletedat()).isEqualTo(DEFAULT_DELETEDAT);
        assertThat(testAdministrator.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testAdministrator.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testAdministrator.getEmailaddress()).isEqualTo(DEFAULT_EMAILADDRESS);
    }

    @Test
    @Transactional
    void createAdministratorWithExistingId() throws Exception {
        // Create the Administrator with an existing ID
        administrator.setId(1L);
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        int databaseSizeBeforeCreate = administratorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = administratorRepository.findAll().size();
        // set the field null
        administrator.setCreatedat(null);

        // Create the Administrator, which fails.
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        restAdministratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = administratorRepository.findAll().size();
        // set the field null
        administrator.setUpdatedat(null);

        // Create the Administrator, which fails.
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        restAdministratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = administratorRepository.findAll().size();
        // set the field null
        administrator.setFirstname(null);

        // Create the Administrator, which fails.
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        restAdministratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = administratorRepository.findAll().size();
        // set the field null
        administrator.setLastname(null);

        // Create the Administrator, which fails.
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        restAdministratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailaddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = administratorRepository.findAll().size();
        // set the field null
        administrator.setEmailaddress(null);

        // Create the Administrator, which fails.
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        restAdministratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdministrators() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList
        restAdministratorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrator.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].emailaddress").value(hasItem(DEFAULT_EMAILADDRESS)));
    }

    @Test
    @Transactional
    void getAdministrator() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get the administrator
        restAdministratorMockMvc
            .perform(get(ENTITY_API_URL_ID, administrator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(administrator.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.deletedat").value(DEFAULT_DELETEDAT.toString()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.emailaddress").value(DEFAULT_EMAILADDRESS));
    }

    @Test
    @Transactional
    void getAdministratorsByIdFiltering() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        Long id = administrator.getId();

        defaultAdministratorShouldBeFound("id.equals=" + id);
        defaultAdministratorShouldNotBeFound("id.notEquals=" + id);

        defaultAdministratorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdministratorShouldNotBeFound("id.greaterThan=" + id);

        defaultAdministratorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdministratorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAdministratorsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where createdat equals to DEFAULT_CREATEDAT
        defaultAdministratorShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the administratorList where createdat equals to UPDATED_CREATEDAT
        defaultAdministratorShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllAdministratorsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where createdat not equals to DEFAULT_CREATEDAT
        defaultAdministratorShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the administratorList where createdat not equals to UPDATED_CREATEDAT
        defaultAdministratorShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllAdministratorsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultAdministratorShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the administratorList where createdat equals to UPDATED_CREATEDAT
        defaultAdministratorShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllAdministratorsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where createdat is not null
        defaultAdministratorShouldBeFound("createdat.specified=true");

        // Get all the administratorList where createdat is null
        defaultAdministratorShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministratorsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where updatedat equals to DEFAULT_UPDATEDAT
        defaultAdministratorShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the administratorList where updatedat equals to UPDATED_UPDATEDAT
        defaultAdministratorShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllAdministratorsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultAdministratorShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the administratorList where updatedat not equals to UPDATED_UPDATEDAT
        defaultAdministratorShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllAdministratorsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultAdministratorShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the administratorList where updatedat equals to UPDATED_UPDATEDAT
        defaultAdministratorShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllAdministratorsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where updatedat is not null
        defaultAdministratorShouldBeFound("updatedat.specified=true");

        // Get all the administratorList where updatedat is null
        defaultAdministratorShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministratorsByDeletedatIsEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where deletedat equals to DEFAULT_DELETEDAT
        defaultAdministratorShouldBeFound("deletedat.equals=" + DEFAULT_DELETEDAT);

        // Get all the administratorList where deletedat equals to UPDATED_DELETEDAT
        defaultAdministratorShouldNotBeFound("deletedat.equals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllAdministratorsByDeletedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where deletedat not equals to DEFAULT_DELETEDAT
        defaultAdministratorShouldNotBeFound("deletedat.notEquals=" + DEFAULT_DELETEDAT);

        // Get all the administratorList where deletedat not equals to UPDATED_DELETEDAT
        defaultAdministratorShouldBeFound("deletedat.notEquals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllAdministratorsByDeletedatIsInShouldWork() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where deletedat in DEFAULT_DELETEDAT or UPDATED_DELETEDAT
        defaultAdministratorShouldBeFound("deletedat.in=" + DEFAULT_DELETEDAT + "," + UPDATED_DELETEDAT);

        // Get all the administratorList where deletedat equals to UPDATED_DELETEDAT
        defaultAdministratorShouldNotBeFound("deletedat.in=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllAdministratorsByDeletedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where deletedat is not null
        defaultAdministratorShouldBeFound("deletedat.specified=true");

        // Get all the administratorList where deletedat is null
        defaultAdministratorShouldNotBeFound("deletedat.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministratorsByFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where firstname equals to DEFAULT_FIRSTNAME
        defaultAdministratorShouldBeFound("firstname.equals=" + DEFAULT_FIRSTNAME);

        // Get all the administratorList where firstname equals to UPDATED_FIRSTNAME
        defaultAdministratorShouldNotBeFound("firstname.equals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllAdministratorsByFirstnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where firstname not equals to DEFAULT_FIRSTNAME
        defaultAdministratorShouldNotBeFound("firstname.notEquals=" + DEFAULT_FIRSTNAME);

        // Get all the administratorList where firstname not equals to UPDATED_FIRSTNAME
        defaultAdministratorShouldBeFound("firstname.notEquals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllAdministratorsByFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where firstname in DEFAULT_FIRSTNAME or UPDATED_FIRSTNAME
        defaultAdministratorShouldBeFound("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME);

        // Get all the administratorList where firstname equals to UPDATED_FIRSTNAME
        defaultAdministratorShouldNotBeFound("firstname.in=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllAdministratorsByFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where firstname is not null
        defaultAdministratorShouldBeFound("firstname.specified=true");

        // Get all the administratorList where firstname is null
        defaultAdministratorShouldNotBeFound("firstname.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministratorsByFirstnameContainsSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where firstname contains DEFAULT_FIRSTNAME
        defaultAdministratorShouldBeFound("firstname.contains=" + DEFAULT_FIRSTNAME);

        // Get all the administratorList where firstname contains UPDATED_FIRSTNAME
        defaultAdministratorShouldNotBeFound("firstname.contains=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllAdministratorsByFirstnameNotContainsSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where firstname does not contain DEFAULT_FIRSTNAME
        defaultAdministratorShouldNotBeFound("firstname.doesNotContain=" + DEFAULT_FIRSTNAME);

        // Get all the administratorList where firstname does not contain UPDATED_FIRSTNAME
        defaultAdministratorShouldBeFound("firstname.doesNotContain=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllAdministratorsByLastnameIsEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where lastname equals to DEFAULT_LASTNAME
        defaultAdministratorShouldBeFound("lastname.equals=" + DEFAULT_LASTNAME);

        // Get all the administratorList where lastname equals to UPDATED_LASTNAME
        defaultAdministratorShouldNotBeFound("lastname.equals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllAdministratorsByLastnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where lastname not equals to DEFAULT_LASTNAME
        defaultAdministratorShouldNotBeFound("lastname.notEquals=" + DEFAULT_LASTNAME);

        // Get all the administratorList where lastname not equals to UPDATED_LASTNAME
        defaultAdministratorShouldBeFound("lastname.notEquals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllAdministratorsByLastnameIsInShouldWork() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where lastname in DEFAULT_LASTNAME or UPDATED_LASTNAME
        defaultAdministratorShouldBeFound("lastname.in=" + DEFAULT_LASTNAME + "," + UPDATED_LASTNAME);

        // Get all the administratorList where lastname equals to UPDATED_LASTNAME
        defaultAdministratorShouldNotBeFound("lastname.in=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllAdministratorsByLastnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where lastname is not null
        defaultAdministratorShouldBeFound("lastname.specified=true");

        // Get all the administratorList where lastname is null
        defaultAdministratorShouldNotBeFound("lastname.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministratorsByLastnameContainsSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where lastname contains DEFAULT_LASTNAME
        defaultAdministratorShouldBeFound("lastname.contains=" + DEFAULT_LASTNAME);

        // Get all the administratorList where lastname contains UPDATED_LASTNAME
        defaultAdministratorShouldNotBeFound("lastname.contains=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllAdministratorsByLastnameNotContainsSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where lastname does not contain DEFAULT_LASTNAME
        defaultAdministratorShouldNotBeFound("lastname.doesNotContain=" + DEFAULT_LASTNAME);

        // Get all the administratorList where lastname does not contain UPDATED_LASTNAME
        defaultAdministratorShouldBeFound("lastname.doesNotContain=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllAdministratorsByEmailaddressIsEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where emailaddress equals to DEFAULT_EMAILADDRESS
        defaultAdministratorShouldBeFound("emailaddress.equals=" + DEFAULT_EMAILADDRESS);

        // Get all the administratorList where emailaddress equals to UPDATED_EMAILADDRESS
        defaultAdministratorShouldNotBeFound("emailaddress.equals=" + UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void getAllAdministratorsByEmailaddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where emailaddress not equals to DEFAULT_EMAILADDRESS
        defaultAdministratorShouldNotBeFound("emailaddress.notEquals=" + DEFAULT_EMAILADDRESS);

        // Get all the administratorList where emailaddress not equals to UPDATED_EMAILADDRESS
        defaultAdministratorShouldBeFound("emailaddress.notEquals=" + UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void getAllAdministratorsByEmailaddressIsInShouldWork() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where emailaddress in DEFAULT_EMAILADDRESS or UPDATED_EMAILADDRESS
        defaultAdministratorShouldBeFound("emailaddress.in=" + DEFAULT_EMAILADDRESS + "," + UPDATED_EMAILADDRESS);

        // Get all the administratorList where emailaddress equals to UPDATED_EMAILADDRESS
        defaultAdministratorShouldNotBeFound("emailaddress.in=" + UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void getAllAdministratorsByEmailaddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where emailaddress is not null
        defaultAdministratorShouldBeFound("emailaddress.specified=true");

        // Get all the administratorList where emailaddress is null
        defaultAdministratorShouldNotBeFound("emailaddress.specified=false");
    }

    @Test
    @Transactional
    void getAllAdministratorsByEmailaddressContainsSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where emailaddress contains DEFAULT_EMAILADDRESS
        defaultAdministratorShouldBeFound("emailaddress.contains=" + DEFAULT_EMAILADDRESS);

        // Get all the administratorList where emailaddress contains UPDATED_EMAILADDRESS
        defaultAdministratorShouldNotBeFound("emailaddress.contains=" + UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void getAllAdministratorsByEmailaddressNotContainsSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList where emailaddress does not contain DEFAULT_EMAILADDRESS
        defaultAdministratorShouldNotBeFound("emailaddress.doesNotContain=" + DEFAULT_EMAILADDRESS);

        // Get all the administratorList where emailaddress does not contain UPDATED_EMAILADDRESS
        defaultAdministratorShouldBeFound("emailaddress.doesNotContain=" + UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void getAllAdministratorsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        administrator.setUser(user);
        administratorRepository.saveAndFlush(administrator);
        Long userId = user.getId();

        // Get all the administratorList where user equals to userId
        defaultAdministratorShouldBeFound("userId.equals=" + userId);

        // Get all the administratorList where user equals to (userId + 1)
        defaultAdministratorShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllAdministratorsByHistoryEntryIsEqualToSomething() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);
        HistoryEntry historyEntry = HistoryEntryResourceIT.createEntity(em);
        em.persist(historyEntry);
        em.flush();
        administrator.addHistoryEntry(historyEntry);
        administratorRepository.saveAndFlush(administrator);
        Long historyEntryId = historyEntry.getId();

        // Get all the administratorList where historyEntry equals to historyEntryId
        defaultAdministratorShouldBeFound("historyEntryId.equals=" + historyEntryId);

        // Get all the administratorList where historyEntry equals to (historyEntryId + 1)
        defaultAdministratorShouldNotBeFound("historyEntryId.equals=" + (historyEntryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdministratorShouldBeFound(String filter) throws Exception {
        restAdministratorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrator.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].emailaddress").value(hasItem(DEFAULT_EMAILADDRESS)));

        // Check, that the count call also returns 1
        restAdministratorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdministratorShouldNotBeFound(String filter) throws Exception {
        restAdministratorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdministratorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAdministrator() throws Exception {
        // Get the administrator
        restAdministratorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAdministrator() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();

        // Update the administrator
        Administrator updatedAdministrator = administratorRepository.findById(administrator.getId()).get();
        // Disconnect from session so that the updates on updatedAdministrator are not directly saved in db
        em.detach(updatedAdministrator);
        updatedAdministrator
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .emailaddress(UPDATED_EMAILADDRESS);
        AdministratorDTO administratorDTO = administratorMapper.toDto(updatedAdministrator);

        restAdministratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administratorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
        Administrator testAdministrator = administratorList.get(administratorList.size() - 1);
        assertThat(testAdministrator.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testAdministrator.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testAdministrator.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testAdministrator.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testAdministrator.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testAdministrator.getEmailaddress()).isEqualTo(UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingAdministrator() throws Exception {
        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();
        administrator.setId(count.incrementAndGet());

        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administratorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdministrator() throws Exception {
        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();
        administrator.setId(count.incrementAndGet());

        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdministrator() throws Exception {
        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();
        administrator.setId(count.incrementAndGet());

        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministratorMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdministratorWithPatch() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();

        // Update the administrator using partial update
        Administrator partialUpdatedAdministrator = new Administrator();
        partialUpdatedAdministrator.setId(administrator.getId());

        partialUpdatedAdministrator
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .firstname(UPDATED_FIRSTNAME)
            .emailaddress(UPDATED_EMAILADDRESS);

        restAdministratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrator))
            )
            .andExpect(status().isOk());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
        Administrator testAdministrator = administratorList.get(administratorList.size() - 1);
        assertThat(testAdministrator.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testAdministrator.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testAdministrator.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testAdministrator.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testAdministrator.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testAdministrator.getEmailaddress()).isEqualTo(UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateAdministratorWithPatch() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();

        // Update the administrator using partial update
        Administrator partialUpdatedAdministrator = new Administrator();
        partialUpdatedAdministrator.setId(administrator.getId());

        partialUpdatedAdministrator
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .emailaddress(UPDATED_EMAILADDRESS);

        restAdministratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrator))
            )
            .andExpect(status().isOk());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
        Administrator testAdministrator = administratorList.get(administratorList.size() - 1);
        assertThat(testAdministrator.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testAdministrator.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testAdministrator.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testAdministrator.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testAdministrator.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testAdministrator.getEmailaddress()).isEqualTo(UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingAdministrator() throws Exception {
        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();
        administrator.setId(count.incrementAndGet());

        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, administratorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdministrator() throws Exception {
        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();
        administrator.setId(count.incrementAndGet());

        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdministrator() throws Exception {
        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();
        administrator.setId(count.incrementAndGet());

        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministratorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdministrator() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        int databaseSizeBeforeDelete = administratorRepository.findAll().size();

        // Delete the administrator
        restAdministratorMockMvc
            .perform(delete(ENTITY_API_URL_ID, administrator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
