package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Address;
import com.venjure.domain.Asset;
import com.venjure.domain.Channel;
import com.venjure.domain.Customer;
import com.venjure.domain.CustomerGroup;
import com.venjure.domain.HistoryEntry;
import com.venjure.domain.Jorder;
import com.venjure.domain.User;
import com.venjure.repository.CustomerRepository;
import com.venjure.service.CustomerService;
import com.venjure.service.criteria.CustomerCriteria;
import com.venjure.service.dto.CustomerDTO;
import com.venjure.service.mapper.CustomerMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustomerResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONENUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONENUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAILADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAILADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerRepository customerRepository;

    @Mock
    private CustomerRepository customerRepositoryMock;

    @Autowired
    private CustomerMapper customerMapper;

    @Mock
    private CustomerService customerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .deletedat(DEFAULT_DELETEDAT)
            .title(DEFAULT_TITLE)
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .phonenumber(DEFAULT_PHONENUMBER)
            .emailaddress(DEFAULT_EMAILADDRESS);
        return customer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .title(UPDATED_TITLE)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .phonenumber(UPDATED_PHONENUMBER)
            .emailaddress(UPDATED_EMAILADDRESS);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();
        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testCustomer.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testCustomer.getDeletedat()).isEqualTo(DEFAULT_DELETEDAT);
        assertThat(testCustomer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCustomer.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testCustomer.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testCustomer.getPhonenumber()).isEqualTo(DEFAULT_PHONENUMBER);
        assertThat(testCustomer.getEmailaddress()).isEqualTo(DEFAULT_EMAILADDRESS);
    }

    @Test
    @Transactional
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCreatedat(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setUpdatedat(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setFirstname(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setLastname(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailaddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setEmailaddress(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].phonenumber").value(hasItem(DEFAULT_PHONENUMBER)))
            .andExpect(jsonPath("$.[*].emailaddress").value(hasItem(DEFAULT_EMAILADDRESS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomersWithEagerRelationshipsIsEnabled() throws Exception {
        when(customerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(customerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.deletedat").value(DEFAULT_DELETEDAT.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.phonenumber").value(DEFAULT_PHONENUMBER))
            .andExpect(jsonPath("$.emailaddress").value(DEFAULT_EMAILADDRESS));
    }

    @Test
    @Transactional
    void getCustomersByIdFiltering() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        Long id = customer.getId();

        defaultCustomerShouldBeFound("id.equals=" + id);
        defaultCustomerShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomersByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdat equals to DEFAULT_CREATEDAT
        defaultCustomerShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the customerList where createdat equals to UPDATED_CREATEDAT
        defaultCustomerShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCustomersByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdat not equals to DEFAULT_CREATEDAT
        defaultCustomerShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the customerList where createdat not equals to UPDATED_CREATEDAT
        defaultCustomerShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCustomersByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultCustomerShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the customerList where createdat equals to UPDATED_CREATEDAT
        defaultCustomerShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCustomersByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdat is not null
        defaultCustomerShouldBeFound("createdat.specified=true");

        // Get all the customerList where createdat is null
        defaultCustomerShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where updatedat equals to DEFAULT_UPDATEDAT
        defaultCustomerShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the customerList where updatedat equals to UPDATED_UPDATEDAT
        defaultCustomerShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCustomersByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultCustomerShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the customerList where updatedat not equals to UPDATED_UPDATEDAT
        defaultCustomerShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCustomersByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultCustomerShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the customerList where updatedat equals to UPDATED_UPDATEDAT
        defaultCustomerShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCustomersByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where updatedat is not null
        defaultCustomerShouldBeFound("updatedat.specified=true");

        // Get all the customerList where updatedat is null
        defaultCustomerShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByDeletedatIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where deletedat equals to DEFAULT_DELETEDAT
        defaultCustomerShouldBeFound("deletedat.equals=" + DEFAULT_DELETEDAT);

        // Get all the customerList where deletedat equals to UPDATED_DELETEDAT
        defaultCustomerShouldNotBeFound("deletedat.equals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllCustomersByDeletedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where deletedat not equals to DEFAULT_DELETEDAT
        defaultCustomerShouldNotBeFound("deletedat.notEquals=" + DEFAULT_DELETEDAT);

        // Get all the customerList where deletedat not equals to UPDATED_DELETEDAT
        defaultCustomerShouldBeFound("deletedat.notEquals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllCustomersByDeletedatIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where deletedat in DEFAULT_DELETEDAT or UPDATED_DELETEDAT
        defaultCustomerShouldBeFound("deletedat.in=" + DEFAULT_DELETEDAT + "," + UPDATED_DELETEDAT);

        // Get all the customerList where deletedat equals to UPDATED_DELETEDAT
        defaultCustomerShouldNotBeFound("deletedat.in=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllCustomersByDeletedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where deletedat is not null
        defaultCustomerShouldBeFound("deletedat.specified=true");

        // Get all the customerList where deletedat is null
        defaultCustomerShouldNotBeFound("deletedat.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where title equals to DEFAULT_TITLE
        defaultCustomerShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the customerList where title equals to UPDATED_TITLE
        defaultCustomerShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCustomersByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where title not equals to DEFAULT_TITLE
        defaultCustomerShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the customerList where title not equals to UPDATED_TITLE
        defaultCustomerShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCustomersByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCustomerShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the customerList where title equals to UPDATED_TITLE
        defaultCustomerShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCustomersByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where title is not null
        defaultCustomerShouldBeFound("title.specified=true");

        // Get all the customerList where title is null
        defaultCustomerShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByTitleContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where title contains DEFAULT_TITLE
        defaultCustomerShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the customerList where title contains UPDATED_TITLE
        defaultCustomerShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCustomersByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where title does not contain DEFAULT_TITLE
        defaultCustomerShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the customerList where title does not contain UPDATED_TITLE
        defaultCustomerShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstname equals to DEFAULT_FIRSTNAME
        defaultCustomerShouldBeFound("firstname.equals=" + DEFAULT_FIRSTNAME);

        // Get all the customerList where firstname equals to UPDATED_FIRSTNAME
        defaultCustomerShouldNotBeFound("firstname.equals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstname not equals to DEFAULT_FIRSTNAME
        defaultCustomerShouldNotBeFound("firstname.notEquals=" + DEFAULT_FIRSTNAME);

        // Get all the customerList where firstname not equals to UPDATED_FIRSTNAME
        defaultCustomerShouldBeFound("firstname.notEquals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstname in DEFAULT_FIRSTNAME or UPDATED_FIRSTNAME
        defaultCustomerShouldBeFound("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME);

        // Get all the customerList where firstname equals to UPDATED_FIRSTNAME
        defaultCustomerShouldNotBeFound("firstname.in=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstname is not null
        defaultCustomerShouldBeFound("firstname.specified=true");

        // Get all the customerList where firstname is null
        defaultCustomerShouldNotBeFound("firstname.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByFirstnameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstname contains DEFAULT_FIRSTNAME
        defaultCustomerShouldBeFound("firstname.contains=" + DEFAULT_FIRSTNAME);

        // Get all the customerList where firstname contains UPDATED_FIRSTNAME
        defaultCustomerShouldNotBeFound("firstname.contains=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstnameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstname does not contain DEFAULT_FIRSTNAME
        defaultCustomerShouldNotBeFound("firstname.doesNotContain=" + DEFAULT_FIRSTNAME);

        // Get all the customerList where firstname does not contain UPDATED_FIRSTNAME
        defaultCustomerShouldBeFound("firstname.doesNotContain=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastnameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastname equals to DEFAULT_LASTNAME
        defaultCustomerShouldBeFound("lastname.equals=" + DEFAULT_LASTNAME);

        // Get all the customerList where lastname equals to UPDATED_LASTNAME
        defaultCustomerShouldNotBeFound("lastname.equals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastname not equals to DEFAULT_LASTNAME
        defaultCustomerShouldNotBeFound("lastname.notEquals=" + DEFAULT_LASTNAME);

        // Get all the customerList where lastname not equals to UPDATED_LASTNAME
        defaultCustomerShouldBeFound("lastname.notEquals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastnameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastname in DEFAULT_LASTNAME or UPDATED_LASTNAME
        defaultCustomerShouldBeFound("lastname.in=" + DEFAULT_LASTNAME + "," + UPDATED_LASTNAME);

        // Get all the customerList where lastname equals to UPDATED_LASTNAME
        defaultCustomerShouldNotBeFound("lastname.in=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastname is not null
        defaultCustomerShouldBeFound("lastname.specified=true");

        // Get all the customerList where lastname is null
        defaultCustomerShouldNotBeFound("lastname.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByLastnameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastname contains DEFAULT_LASTNAME
        defaultCustomerShouldBeFound("lastname.contains=" + DEFAULT_LASTNAME);

        // Get all the customerList where lastname contains UPDATED_LASTNAME
        defaultCustomerShouldNotBeFound("lastname.contains=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastnameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastname does not contain DEFAULT_LASTNAME
        defaultCustomerShouldNotBeFound("lastname.doesNotContain=" + DEFAULT_LASTNAME);

        // Get all the customerList where lastname does not contain UPDATED_LASTNAME
        defaultCustomerShouldBeFound("lastname.doesNotContain=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllCustomersByPhonenumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phonenumber equals to DEFAULT_PHONENUMBER
        defaultCustomerShouldBeFound("phonenumber.equals=" + DEFAULT_PHONENUMBER);

        // Get all the customerList where phonenumber equals to UPDATED_PHONENUMBER
        defaultCustomerShouldNotBeFound("phonenumber.equals=" + UPDATED_PHONENUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByPhonenumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phonenumber not equals to DEFAULT_PHONENUMBER
        defaultCustomerShouldNotBeFound("phonenumber.notEquals=" + DEFAULT_PHONENUMBER);

        // Get all the customerList where phonenumber not equals to UPDATED_PHONENUMBER
        defaultCustomerShouldBeFound("phonenumber.notEquals=" + UPDATED_PHONENUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByPhonenumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phonenumber in DEFAULT_PHONENUMBER or UPDATED_PHONENUMBER
        defaultCustomerShouldBeFound("phonenumber.in=" + DEFAULT_PHONENUMBER + "," + UPDATED_PHONENUMBER);

        // Get all the customerList where phonenumber equals to UPDATED_PHONENUMBER
        defaultCustomerShouldNotBeFound("phonenumber.in=" + UPDATED_PHONENUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByPhonenumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phonenumber is not null
        defaultCustomerShouldBeFound("phonenumber.specified=true");

        // Get all the customerList where phonenumber is null
        defaultCustomerShouldNotBeFound("phonenumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByPhonenumberContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phonenumber contains DEFAULT_PHONENUMBER
        defaultCustomerShouldBeFound("phonenumber.contains=" + DEFAULT_PHONENUMBER);

        // Get all the customerList where phonenumber contains UPDATED_PHONENUMBER
        defaultCustomerShouldNotBeFound("phonenumber.contains=" + UPDATED_PHONENUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByPhonenumberNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phonenumber does not contain DEFAULT_PHONENUMBER
        defaultCustomerShouldNotBeFound("phonenumber.doesNotContain=" + DEFAULT_PHONENUMBER);

        // Get all the customerList where phonenumber does not contain UPDATED_PHONENUMBER
        defaultCustomerShouldBeFound("phonenumber.doesNotContain=" + UPDATED_PHONENUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailaddressIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where emailaddress equals to DEFAULT_EMAILADDRESS
        defaultCustomerShouldBeFound("emailaddress.equals=" + DEFAULT_EMAILADDRESS);

        // Get all the customerList where emailaddress equals to UPDATED_EMAILADDRESS
        defaultCustomerShouldNotBeFound("emailaddress.equals=" + UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailaddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where emailaddress not equals to DEFAULT_EMAILADDRESS
        defaultCustomerShouldNotBeFound("emailaddress.notEquals=" + DEFAULT_EMAILADDRESS);

        // Get all the customerList where emailaddress not equals to UPDATED_EMAILADDRESS
        defaultCustomerShouldBeFound("emailaddress.notEquals=" + UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailaddressIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where emailaddress in DEFAULT_EMAILADDRESS or UPDATED_EMAILADDRESS
        defaultCustomerShouldBeFound("emailaddress.in=" + DEFAULT_EMAILADDRESS + "," + UPDATED_EMAILADDRESS);

        // Get all the customerList where emailaddress equals to UPDATED_EMAILADDRESS
        defaultCustomerShouldNotBeFound("emailaddress.in=" + UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailaddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where emailaddress is not null
        defaultCustomerShouldBeFound("emailaddress.specified=true");

        // Get all the customerList where emailaddress is null
        defaultCustomerShouldNotBeFound("emailaddress.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByEmailaddressContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where emailaddress contains DEFAULT_EMAILADDRESS
        defaultCustomerShouldBeFound("emailaddress.contains=" + DEFAULT_EMAILADDRESS);

        // Get all the customerList where emailaddress contains UPDATED_EMAILADDRESS
        defaultCustomerShouldNotBeFound("emailaddress.contains=" + UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailaddressNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where emailaddress does not contain DEFAULT_EMAILADDRESS
        defaultCustomerShouldNotBeFound("emailaddress.doesNotContain=" + DEFAULT_EMAILADDRESS);

        // Get all the customerList where emailaddress does not contain UPDATED_EMAILADDRESS
        defaultCustomerShouldBeFound("emailaddress.doesNotContain=" + UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        customer.setUser(user);
        customerRepository.saveAndFlush(customer);
        Long userId = user.getId();

        // Get all the customerList where user equals to userId
        defaultCustomerShouldBeFound("userId.equals=" + userId);

        // Get all the customerList where user equals to (userId + 1)
        defaultCustomerShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByAvatarIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        Asset avatar = AssetResourceIT.createEntity(em);
        em.persist(avatar);
        em.flush();
        customer.setAvatar(avatar);
        customerRepository.saveAndFlush(customer);
        Long avatarId = avatar.getId();

        // Get all the customerList where avatar equals to avatarId
        defaultCustomerShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the customerList where avatar equals to (avatarId + 1)
        defaultCustomerShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        Channel channel = ChannelResourceIT.createEntity(em);
        em.persist(channel);
        em.flush();
        customer.addChannel(channel);
        customerRepository.saveAndFlush(customer);
        Long channelId = channel.getId();

        // Get all the customerList where channel equals to channelId
        defaultCustomerShouldBeFound("channelId.equals=" + channelId);

        // Get all the customerList where channel equals to (channelId + 1)
        defaultCustomerShouldNotBeFound("channelId.equals=" + (channelId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        CustomerGroup customerGroup = CustomerGroupResourceIT.createEntity(em);
        em.persist(customerGroup);
        em.flush();
        customer.addCustomerGroup(customerGroup);
        customerRepository.saveAndFlush(customer);
        Long customerGroupId = customerGroup.getId();

        // Get all the customerList where customerGroup equals to customerGroupId
        defaultCustomerShouldBeFound("customerGroupId.equals=" + customerGroupId);

        // Get all the customerList where customerGroup equals to (customerGroupId + 1)
        defaultCustomerShouldNotBeFound("customerGroupId.equals=" + (customerGroupId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        Address address = AddressResourceIT.createEntity(em);
        em.persist(address);
        em.flush();
        customer.addAddress(address);
        customerRepository.saveAndFlush(customer);
        Long addressId = address.getId();

        // Get all the customerList where address equals to addressId
        defaultCustomerShouldBeFound("addressId.equals=" + addressId);

        // Get all the customerList where address equals to (addressId + 1)
        defaultCustomerShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByHistoryEntryIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        HistoryEntry historyEntry = HistoryEntryResourceIT.createEntity(em);
        em.persist(historyEntry);
        em.flush();
        customer.addHistoryEntry(historyEntry);
        customerRepository.saveAndFlush(customer);
        Long historyEntryId = historyEntry.getId();

        // Get all the customerList where historyEntry equals to historyEntryId
        defaultCustomerShouldBeFound("historyEntryId.equals=" + historyEntryId);

        // Get all the customerList where historyEntry equals to (historyEntryId + 1)
        defaultCustomerShouldNotBeFound("historyEntryId.equals=" + (historyEntryId + 1));
    }

    @Test
    @Transactional
    void getAllCustomersByJorderIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        Jorder jorder = JorderResourceIT.createEntity(em);
        em.persist(jorder);
        em.flush();
        customer.addJorder(jorder);
        customerRepository.saveAndFlush(customer);
        Long jorderId = jorder.getId();

        // Get all the customerList where jorder equals to jorderId
        defaultCustomerShouldBeFound("jorderId.equals=" + jorderId);

        // Get all the customerList where jorder equals to (jorderId + 1)
        defaultCustomerShouldNotBeFound("jorderId.equals=" + (jorderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].phonenumber").value(hasItem(DEFAULT_PHONENUMBER)))
            .andExpect(jsonPath("$.[*].emailaddress").value(hasItem(DEFAULT_EMAILADDRESS)));

        // Check, that the count call also returns 1
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .title(UPDATED_TITLE)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .phonenumber(UPDATED_PHONENUMBER)
            .emailaddress(UPDATED_EMAILADDRESS);
        CustomerDTO customerDTO = customerMapper.toDto(updatedCustomer);

        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCustomer.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCustomer.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testCustomer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCustomer.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testCustomer.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testCustomer.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testCustomer.getEmailaddress()).isEqualTo(UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer.updatedat(UPDATED_UPDATEDAT).deletedat(UPDATED_DELETEDAT).title(UPDATED_TITLE).lastname(UPDATED_LASTNAME);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testCustomer.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCustomer.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testCustomer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCustomer.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testCustomer.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testCustomer.getPhonenumber()).isEqualTo(DEFAULT_PHONENUMBER);
        assertThat(testCustomer.getEmailaddress()).isEqualTo(DEFAULT_EMAILADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .title(UPDATED_TITLE)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .phonenumber(UPDATED_PHONENUMBER)
            .emailaddress(UPDATED_EMAILADDRESS);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCustomer.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCustomer.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testCustomer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCustomer.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testCustomer.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testCustomer.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testCustomer.getEmailaddress()).isEqualTo(UPDATED_EMAILADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, customer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
