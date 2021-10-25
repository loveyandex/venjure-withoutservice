package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Address;
import com.venjure.domain.Country;
import com.venjure.domain.Customer;
import com.venjure.repository.AddressRepository;
import com.venjure.service.criteria.AddressCriteria;
import com.venjure.service.dto.AddressDTO;
import com.venjure.service.mapper.AddressMapper;
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
 * Integration tests for the {@link AddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AddressResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FULLNAME = "AAAAAAAAAA";
    private static final String UPDATED_FULLNAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    private static final String DEFAULT_STREETLINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_STREETLINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_STREETLINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_STREETLINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_POSTALCODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTALCODE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONENUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONENUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEFAULTSHIPPINGADDRESS = false;
    private static final Boolean UPDATED_DEFAULTSHIPPINGADDRESS = true;

    private static final Boolean DEFAULT_DEFAULTBILLINGADDRESS = false;
    private static final Boolean UPDATED_DEFAULTBILLINGADDRESS = true;

    private static final String ENTITY_API_URL = "/api/addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressMockMvc;

    private Address address;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createEntity(EntityManager em) {
        Address address = new Address()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .fullname(DEFAULT_FULLNAME)
            .company(DEFAULT_COMPANY)
            .streetline1(DEFAULT_STREETLINE_1)
            .streetline2(DEFAULT_STREETLINE_2)
            .city(DEFAULT_CITY)
            .province(DEFAULT_PROVINCE)
            .postalcode(DEFAULT_POSTALCODE)
            .phonenumber(DEFAULT_PHONENUMBER)
            .defaultshippingaddress(DEFAULT_DEFAULTSHIPPINGADDRESS)
            .defaultbillingaddress(DEFAULT_DEFAULTBILLINGADDRESS);
        return address;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createUpdatedEntity(EntityManager em) {
        Address address = new Address()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .fullname(UPDATED_FULLNAME)
            .company(UPDATED_COMPANY)
            .streetline1(UPDATED_STREETLINE_1)
            .streetline2(UPDATED_STREETLINE_2)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE)
            .postalcode(UPDATED_POSTALCODE)
            .phonenumber(UPDATED_PHONENUMBER)
            .defaultshippingaddress(UPDATED_DEFAULTSHIPPINGADDRESS)
            .defaultbillingaddress(UPDATED_DEFAULTBILLINGADDRESS);
        return address;
    }

    @BeforeEach
    public void initTest() {
        address = createEntity(em);
    }

    @Test
    @Transactional
    void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();
        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);
        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testAddress.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testAddress.getFullname()).isEqualTo(DEFAULT_FULLNAME);
        assertThat(testAddress.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testAddress.getStreetline1()).isEqualTo(DEFAULT_STREETLINE_1);
        assertThat(testAddress.getStreetline2()).isEqualTo(DEFAULT_STREETLINE_2);
        assertThat(testAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAddress.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testAddress.getPostalcode()).isEqualTo(DEFAULT_POSTALCODE);
        assertThat(testAddress.getPhonenumber()).isEqualTo(DEFAULT_PHONENUMBER);
        assertThat(testAddress.getDefaultshippingaddress()).isEqualTo(DEFAULT_DEFAULTSHIPPINGADDRESS);
        assertThat(testAddress.getDefaultbillingaddress()).isEqualTo(DEFAULT_DEFAULTBILLINGADDRESS);
    }

    @Test
    @Transactional
    void createAddressWithExistingId() throws Exception {
        // Create the Address with an existing ID
        address.setId(1L);
        AddressDTO addressDTO = addressMapper.toDto(address);

        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setCreatedat(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setUpdatedat(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFullnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setFullname(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompanyIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setCompany(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStreetline1IsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setStreetline1(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStreetline2IsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setStreetline2(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setCity(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProvinceIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setProvince(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostalcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setPostalcode(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhonenumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setPhonenumber(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDefaultshippingaddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setDefaultshippingaddress(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDefaultbillingaddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setDefaultbillingaddress(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].fullname").value(hasItem(DEFAULT_FULLNAME)))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].streetline1").value(hasItem(DEFAULT_STREETLINE_1)))
            .andExpect(jsonPath("$.[*].streetline2").value(hasItem(DEFAULT_STREETLINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE)))
            .andExpect(jsonPath("$.[*].postalcode").value(hasItem(DEFAULT_POSTALCODE)))
            .andExpect(jsonPath("$.[*].phonenumber").value(hasItem(DEFAULT_PHONENUMBER)))
            .andExpect(jsonPath("$.[*].defaultshippingaddress").value(hasItem(DEFAULT_DEFAULTSHIPPINGADDRESS.booleanValue())))
            .andExpect(jsonPath("$.[*].defaultbillingaddress").value(hasItem(DEFAULT_DEFAULTBILLINGADDRESS.booleanValue())));
    }

    @Test
    @Transactional
    void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.fullname").value(DEFAULT_FULLNAME))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY))
            .andExpect(jsonPath("$.streetline1").value(DEFAULT_STREETLINE_1))
            .andExpect(jsonPath("$.streetline2").value(DEFAULT_STREETLINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE))
            .andExpect(jsonPath("$.postalcode").value(DEFAULT_POSTALCODE))
            .andExpect(jsonPath("$.phonenumber").value(DEFAULT_PHONENUMBER))
            .andExpect(jsonPath("$.defaultshippingaddress").value(DEFAULT_DEFAULTSHIPPINGADDRESS.booleanValue()))
            .andExpect(jsonPath("$.defaultbillingaddress").value(DEFAULT_DEFAULTBILLINGADDRESS.booleanValue()));
    }

    @Test
    @Transactional
    void getAddressesByIdFiltering() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        Long id = address.getId();

        defaultAddressShouldBeFound("id.equals=" + id);
        defaultAddressShouldNotBeFound("id.notEquals=" + id);

        defaultAddressShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAddressShouldNotBeFound("id.greaterThan=" + id);

        defaultAddressShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAddressShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAddressesByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where createdat equals to DEFAULT_CREATEDAT
        defaultAddressShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the addressList where createdat equals to UPDATED_CREATEDAT
        defaultAddressShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllAddressesByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where createdat not equals to DEFAULT_CREATEDAT
        defaultAddressShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the addressList where createdat not equals to UPDATED_CREATEDAT
        defaultAddressShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllAddressesByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultAddressShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the addressList where createdat equals to UPDATED_CREATEDAT
        defaultAddressShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllAddressesByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where createdat is not null
        defaultAddressShouldBeFound("createdat.specified=true");

        // Get all the addressList where createdat is null
        defaultAddressShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where updatedat equals to DEFAULT_UPDATEDAT
        defaultAddressShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the addressList where updatedat equals to UPDATED_UPDATEDAT
        defaultAddressShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllAddressesByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultAddressShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the addressList where updatedat not equals to UPDATED_UPDATEDAT
        defaultAddressShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllAddressesByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultAddressShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the addressList where updatedat equals to UPDATED_UPDATEDAT
        defaultAddressShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllAddressesByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where updatedat is not null
        defaultAddressShouldBeFound("updatedat.specified=true");

        // Get all the addressList where updatedat is null
        defaultAddressShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByFullnameIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where fullname equals to DEFAULT_FULLNAME
        defaultAddressShouldBeFound("fullname.equals=" + DEFAULT_FULLNAME);

        // Get all the addressList where fullname equals to UPDATED_FULLNAME
        defaultAddressShouldNotBeFound("fullname.equals=" + UPDATED_FULLNAME);
    }

    @Test
    @Transactional
    void getAllAddressesByFullnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where fullname not equals to DEFAULT_FULLNAME
        defaultAddressShouldNotBeFound("fullname.notEquals=" + DEFAULT_FULLNAME);

        // Get all the addressList where fullname not equals to UPDATED_FULLNAME
        defaultAddressShouldBeFound("fullname.notEquals=" + UPDATED_FULLNAME);
    }

    @Test
    @Transactional
    void getAllAddressesByFullnameIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where fullname in DEFAULT_FULLNAME or UPDATED_FULLNAME
        defaultAddressShouldBeFound("fullname.in=" + DEFAULT_FULLNAME + "," + UPDATED_FULLNAME);

        // Get all the addressList where fullname equals to UPDATED_FULLNAME
        defaultAddressShouldNotBeFound("fullname.in=" + UPDATED_FULLNAME);
    }

    @Test
    @Transactional
    void getAllAddressesByFullnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where fullname is not null
        defaultAddressShouldBeFound("fullname.specified=true");

        // Get all the addressList where fullname is null
        defaultAddressShouldNotBeFound("fullname.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByFullnameContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where fullname contains DEFAULT_FULLNAME
        defaultAddressShouldBeFound("fullname.contains=" + DEFAULT_FULLNAME);

        // Get all the addressList where fullname contains UPDATED_FULLNAME
        defaultAddressShouldNotBeFound("fullname.contains=" + UPDATED_FULLNAME);
    }

    @Test
    @Transactional
    void getAllAddressesByFullnameNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where fullname does not contain DEFAULT_FULLNAME
        defaultAddressShouldNotBeFound("fullname.doesNotContain=" + DEFAULT_FULLNAME);

        // Get all the addressList where fullname does not contain UPDATED_FULLNAME
        defaultAddressShouldBeFound("fullname.doesNotContain=" + UPDATED_FULLNAME);
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where company equals to DEFAULT_COMPANY
        defaultAddressShouldBeFound("company.equals=" + DEFAULT_COMPANY);

        // Get all the addressList where company equals to UPDATED_COMPANY
        defaultAddressShouldNotBeFound("company.equals=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where company not equals to DEFAULT_COMPANY
        defaultAddressShouldNotBeFound("company.notEquals=" + DEFAULT_COMPANY);

        // Get all the addressList where company not equals to UPDATED_COMPANY
        defaultAddressShouldBeFound("company.notEquals=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where company in DEFAULT_COMPANY or UPDATED_COMPANY
        defaultAddressShouldBeFound("company.in=" + DEFAULT_COMPANY + "," + UPDATED_COMPANY);

        // Get all the addressList where company equals to UPDATED_COMPANY
        defaultAddressShouldNotBeFound("company.in=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where company is not null
        defaultAddressShouldBeFound("company.specified=true");

        // Get all the addressList where company is null
        defaultAddressShouldNotBeFound("company.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where company contains DEFAULT_COMPANY
        defaultAddressShouldBeFound("company.contains=" + DEFAULT_COMPANY);

        // Get all the addressList where company contains UPDATED_COMPANY
        defaultAddressShouldNotBeFound("company.contains=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllAddressesByCompanyNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where company does not contain DEFAULT_COMPANY
        defaultAddressShouldNotBeFound("company.doesNotContain=" + DEFAULT_COMPANY);

        // Get all the addressList where company does not contain UPDATED_COMPANY
        defaultAddressShouldBeFound("company.doesNotContain=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllAddressesByStreetline1IsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where streetline1 equals to DEFAULT_STREETLINE_1
        defaultAddressShouldBeFound("streetline1.equals=" + DEFAULT_STREETLINE_1);

        // Get all the addressList where streetline1 equals to UPDATED_STREETLINE_1
        defaultAddressShouldNotBeFound("streetline1.equals=" + UPDATED_STREETLINE_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreetline1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where streetline1 not equals to DEFAULT_STREETLINE_1
        defaultAddressShouldNotBeFound("streetline1.notEquals=" + DEFAULT_STREETLINE_1);

        // Get all the addressList where streetline1 not equals to UPDATED_STREETLINE_1
        defaultAddressShouldBeFound("streetline1.notEquals=" + UPDATED_STREETLINE_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreetline1IsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where streetline1 in DEFAULT_STREETLINE_1 or UPDATED_STREETLINE_1
        defaultAddressShouldBeFound("streetline1.in=" + DEFAULT_STREETLINE_1 + "," + UPDATED_STREETLINE_1);

        // Get all the addressList where streetline1 equals to UPDATED_STREETLINE_1
        defaultAddressShouldNotBeFound("streetline1.in=" + UPDATED_STREETLINE_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreetline1IsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where streetline1 is not null
        defaultAddressShouldBeFound("streetline1.specified=true");

        // Get all the addressList where streetline1 is null
        defaultAddressShouldNotBeFound("streetline1.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByStreetline1ContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where streetline1 contains DEFAULT_STREETLINE_1
        defaultAddressShouldBeFound("streetline1.contains=" + DEFAULT_STREETLINE_1);

        // Get all the addressList where streetline1 contains UPDATED_STREETLINE_1
        defaultAddressShouldNotBeFound("streetline1.contains=" + UPDATED_STREETLINE_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreetline1NotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where streetline1 does not contain DEFAULT_STREETLINE_1
        defaultAddressShouldNotBeFound("streetline1.doesNotContain=" + DEFAULT_STREETLINE_1);

        // Get all the addressList where streetline1 does not contain UPDATED_STREETLINE_1
        defaultAddressShouldBeFound("streetline1.doesNotContain=" + UPDATED_STREETLINE_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreetline2IsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where streetline2 equals to DEFAULT_STREETLINE_2
        defaultAddressShouldBeFound("streetline2.equals=" + DEFAULT_STREETLINE_2);

        // Get all the addressList where streetline2 equals to UPDATED_STREETLINE_2
        defaultAddressShouldNotBeFound("streetline2.equals=" + UPDATED_STREETLINE_2);
    }

    @Test
    @Transactional
    void getAllAddressesByStreetline2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where streetline2 not equals to DEFAULT_STREETLINE_2
        defaultAddressShouldNotBeFound("streetline2.notEquals=" + DEFAULT_STREETLINE_2);

        // Get all the addressList where streetline2 not equals to UPDATED_STREETLINE_2
        defaultAddressShouldBeFound("streetline2.notEquals=" + UPDATED_STREETLINE_2);
    }

    @Test
    @Transactional
    void getAllAddressesByStreetline2IsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where streetline2 in DEFAULT_STREETLINE_2 or UPDATED_STREETLINE_2
        defaultAddressShouldBeFound("streetline2.in=" + DEFAULT_STREETLINE_2 + "," + UPDATED_STREETLINE_2);

        // Get all the addressList where streetline2 equals to UPDATED_STREETLINE_2
        defaultAddressShouldNotBeFound("streetline2.in=" + UPDATED_STREETLINE_2);
    }

    @Test
    @Transactional
    void getAllAddressesByStreetline2IsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where streetline2 is not null
        defaultAddressShouldBeFound("streetline2.specified=true");

        // Get all the addressList where streetline2 is null
        defaultAddressShouldNotBeFound("streetline2.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByStreetline2ContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where streetline2 contains DEFAULT_STREETLINE_2
        defaultAddressShouldBeFound("streetline2.contains=" + DEFAULT_STREETLINE_2);

        // Get all the addressList where streetline2 contains UPDATED_STREETLINE_2
        defaultAddressShouldNotBeFound("streetline2.contains=" + UPDATED_STREETLINE_2);
    }

    @Test
    @Transactional
    void getAllAddressesByStreetline2NotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where streetline2 does not contain DEFAULT_STREETLINE_2
        defaultAddressShouldNotBeFound("streetline2.doesNotContain=" + DEFAULT_STREETLINE_2);

        // Get all the addressList where streetline2 does not contain UPDATED_STREETLINE_2
        defaultAddressShouldBeFound("streetline2.doesNotContain=" + UPDATED_STREETLINE_2);
    }

    @Test
    @Transactional
    void getAllAddressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city equals to DEFAULT_CITY
        defaultAddressShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the addressList where city equals to UPDATED_CITY
        defaultAddressShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAddressesByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city not equals to DEFAULT_CITY
        defaultAddressShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the addressList where city not equals to UPDATED_CITY
        defaultAddressShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAddressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city in DEFAULT_CITY or UPDATED_CITY
        defaultAddressShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the addressList where city equals to UPDATED_CITY
        defaultAddressShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAddressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city is not null
        defaultAddressShouldBeFound("city.specified=true");

        // Get all the addressList where city is null
        defaultAddressShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByCityContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city contains DEFAULT_CITY
        defaultAddressShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the addressList where city contains UPDATED_CITY
        defaultAddressShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAddressesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city does not contain DEFAULT_CITY
        defaultAddressShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the addressList where city does not contain UPDATED_CITY
        defaultAddressShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAddressesByProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where province equals to DEFAULT_PROVINCE
        defaultAddressShouldBeFound("province.equals=" + DEFAULT_PROVINCE);

        // Get all the addressList where province equals to UPDATED_PROVINCE
        defaultAddressShouldNotBeFound("province.equals=" + UPDATED_PROVINCE);
    }

    @Test
    @Transactional
    void getAllAddressesByProvinceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where province not equals to DEFAULT_PROVINCE
        defaultAddressShouldNotBeFound("province.notEquals=" + DEFAULT_PROVINCE);

        // Get all the addressList where province not equals to UPDATED_PROVINCE
        defaultAddressShouldBeFound("province.notEquals=" + UPDATED_PROVINCE);
    }

    @Test
    @Transactional
    void getAllAddressesByProvinceIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where province in DEFAULT_PROVINCE or UPDATED_PROVINCE
        defaultAddressShouldBeFound("province.in=" + DEFAULT_PROVINCE + "," + UPDATED_PROVINCE);

        // Get all the addressList where province equals to UPDATED_PROVINCE
        defaultAddressShouldNotBeFound("province.in=" + UPDATED_PROVINCE);
    }

    @Test
    @Transactional
    void getAllAddressesByProvinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where province is not null
        defaultAddressShouldBeFound("province.specified=true");

        // Get all the addressList where province is null
        defaultAddressShouldNotBeFound("province.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByProvinceContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where province contains DEFAULT_PROVINCE
        defaultAddressShouldBeFound("province.contains=" + DEFAULT_PROVINCE);

        // Get all the addressList where province contains UPDATED_PROVINCE
        defaultAddressShouldNotBeFound("province.contains=" + UPDATED_PROVINCE);
    }

    @Test
    @Transactional
    void getAllAddressesByProvinceNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where province does not contain DEFAULT_PROVINCE
        defaultAddressShouldNotBeFound("province.doesNotContain=" + DEFAULT_PROVINCE);

        // Get all the addressList where province does not contain UPDATED_PROVINCE
        defaultAddressShouldBeFound("province.doesNotContain=" + UPDATED_PROVINCE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalcode equals to DEFAULT_POSTALCODE
        defaultAddressShouldBeFound("postalcode.equals=" + DEFAULT_POSTALCODE);

        // Get all the addressList where postalcode equals to UPDATED_POSTALCODE
        defaultAddressShouldNotBeFound("postalcode.equals=" + UPDATED_POSTALCODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalcodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalcode not equals to DEFAULT_POSTALCODE
        defaultAddressShouldNotBeFound("postalcode.notEquals=" + DEFAULT_POSTALCODE);

        // Get all the addressList where postalcode not equals to UPDATED_POSTALCODE
        defaultAddressShouldBeFound("postalcode.notEquals=" + UPDATED_POSTALCODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalcodeIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalcode in DEFAULT_POSTALCODE or UPDATED_POSTALCODE
        defaultAddressShouldBeFound("postalcode.in=" + DEFAULT_POSTALCODE + "," + UPDATED_POSTALCODE);

        // Get all the addressList where postalcode equals to UPDATED_POSTALCODE
        defaultAddressShouldNotBeFound("postalcode.in=" + UPDATED_POSTALCODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalcode is not null
        defaultAddressShouldBeFound("postalcode.specified=true");

        // Get all the addressList where postalcode is null
        defaultAddressShouldNotBeFound("postalcode.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByPostalcodeContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalcode contains DEFAULT_POSTALCODE
        defaultAddressShouldBeFound("postalcode.contains=" + DEFAULT_POSTALCODE);

        // Get all the addressList where postalcode contains UPDATED_POSTALCODE
        defaultAddressShouldNotBeFound("postalcode.contains=" + UPDATED_POSTALCODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalcodeNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalcode does not contain DEFAULT_POSTALCODE
        defaultAddressShouldNotBeFound("postalcode.doesNotContain=" + DEFAULT_POSTALCODE);

        // Get all the addressList where postalcode does not contain UPDATED_POSTALCODE
        defaultAddressShouldBeFound("postalcode.doesNotContain=" + UPDATED_POSTALCODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPhonenumberIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phonenumber equals to DEFAULT_PHONENUMBER
        defaultAddressShouldBeFound("phonenumber.equals=" + DEFAULT_PHONENUMBER);

        // Get all the addressList where phonenumber equals to UPDATED_PHONENUMBER
        defaultAddressShouldNotBeFound("phonenumber.equals=" + UPDATED_PHONENUMBER);
    }

    @Test
    @Transactional
    void getAllAddressesByPhonenumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phonenumber not equals to DEFAULT_PHONENUMBER
        defaultAddressShouldNotBeFound("phonenumber.notEquals=" + DEFAULT_PHONENUMBER);

        // Get all the addressList where phonenumber not equals to UPDATED_PHONENUMBER
        defaultAddressShouldBeFound("phonenumber.notEquals=" + UPDATED_PHONENUMBER);
    }

    @Test
    @Transactional
    void getAllAddressesByPhonenumberIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phonenumber in DEFAULT_PHONENUMBER or UPDATED_PHONENUMBER
        defaultAddressShouldBeFound("phonenumber.in=" + DEFAULT_PHONENUMBER + "," + UPDATED_PHONENUMBER);

        // Get all the addressList where phonenumber equals to UPDATED_PHONENUMBER
        defaultAddressShouldNotBeFound("phonenumber.in=" + UPDATED_PHONENUMBER);
    }

    @Test
    @Transactional
    void getAllAddressesByPhonenumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phonenumber is not null
        defaultAddressShouldBeFound("phonenumber.specified=true");

        // Get all the addressList where phonenumber is null
        defaultAddressShouldNotBeFound("phonenumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByPhonenumberContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phonenumber contains DEFAULT_PHONENUMBER
        defaultAddressShouldBeFound("phonenumber.contains=" + DEFAULT_PHONENUMBER);

        // Get all the addressList where phonenumber contains UPDATED_PHONENUMBER
        defaultAddressShouldNotBeFound("phonenumber.contains=" + UPDATED_PHONENUMBER);
    }

    @Test
    @Transactional
    void getAllAddressesByPhonenumberNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phonenumber does not contain DEFAULT_PHONENUMBER
        defaultAddressShouldNotBeFound("phonenumber.doesNotContain=" + DEFAULT_PHONENUMBER);

        // Get all the addressList where phonenumber does not contain UPDATED_PHONENUMBER
        defaultAddressShouldBeFound("phonenumber.doesNotContain=" + UPDATED_PHONENUMBER);
    }

    @Test
    @Transactional
    void getAllAddressesByDefaultshippingaddressIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where defaultshippingaddress equals to DEFAULT_DEFAULTSHIPPINGADDRESS
        defaultAddressShouldBeFound("defaultshippingaddress.equals=" + DEFAULT_DEFAULTSHIPPINGADDRESS);

        // Get all the addressList where defaultshippingaddress equals to UPDATED_DEFAULTSHIPPINGADDRESS
        defaultAddressShouldNotBeFound("defaultshippingaddress.equals=" + UPDATED_DEFAULTSHIPPINGADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByDefaultshippingaddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where defaultshippingaddress not equals to DEFAULT_DEFAULTSHIPPINGADDRESS
        defaultAddressShouldNotBeFound("defaultshippingaddress.notEquals=" + DEFAULT_DEFAULTSHIPPINGADDRESS);

        // Get all the addressList where defaultshippingaddress not equals to UPDATED_DEFAULTSHIPPINGADDRESS
        defaultAddressShouldBeFound("defaultshippingaddress.notEquals=" + UPDATED_DEFAULTSHIPPINGADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByDefaultshippingaddressIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where defaultshippingaddress in DEFAULT_DEFAULTSHIPPINGADDRESS or UPDATED_DEFAULTSHIPPINGADDRESS
        defaultAddressShouldBeFound("defaultshippingaddress.in=" + DEFAULT_DEFAULTSHIPPINGADDRESS + "," + UPDATED_DEFAULTSHIPPINGADDRESS);

        // Get all the addressList where defaultshippingaddress equals to UPDATED_DEFAULTSHIPPINGADDRESS
        defaultAddressShouldNotBeFound("defaultshippingaddress.in=" + UPDATED_DEFAULTSHIPPINGADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByDefaultshippingaddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where defaultshippingaddress is not null
        defaultAddressShouldBeFound("defaultshippingaddress.specified=true");

        // Get all the addressList where defaultshippingaddress is null
        defaultAddressShouldNotBeFound("defaultshippingaddress.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByDefaultbillingaddressIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where defaultbillingaddress equals to DEFAULT_DEFAULTBILLINGADDRESS
        defaultAddressShouldBeFound("defaultbillingaddress.equals=" + DEFAULT_DEFAULTBILLINGADDRESS);

        // Get all the addressList where defaultbillingaddress equals to UPDATED_DEFAULTBILLINGADDRESS
        defaultAddressShouldNotBeFound("defaultbillingaddress.equals=" + UPDATED_DEFAULTBILLINGADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByDefaultbillingaddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where defaultbillingaddress not equals to DEFAULT_DEFAULTBILLINGADDRESS
        defaultAddressShouldNotBeFound("defaultbillingaddress.notEquals=" + DEFAULT_DEFAULTBILLINGADDRESS);

        // Get all the addressList where defaultbillingaddress not equals to UPDATED_DEFAULTBILLINGADDRESS
        defaultAddressShouldBeFound("defaultbillingaddress.notEquals=" + UPDATED_DEFAULTBILLINGADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByDefaultbillingaddressIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where defaultbillingaddress in DEFAULT_DEFAULTBILLINGADDRESS or UPDATED_DEFAULTBILLINGADDRESS
        defaultAddressShouldBeFound("defaultbillingaddress.in=" + DEFAULT_DEFAULTBILLINGADDRESS + "," + UPDATED_DEFAULTBILLINGADDRESS);

        // Get all the addressList where defaultbillingaddress equals to UPDATED_DEFAULTBILLINGADDRESS
        defaultAddressShouldNotBeFound("defaultbillingaddress.in=" + UPDATED_DEFAULTBILLINGADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByDefaultbillingaddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where defaultbillingaddress is not null
        defaultAddressShouldBeFound("defaultbillingaddress.specified=true");

        // Get all the addressList where defaultbillingaddress is null
        defaultAddressShouldNotBeFound("defaultbillingaddress.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        address.setCustomer(customer);
        addressRepository.saveAndFlush(address);
        Long customerId = customer.getId();

        // Get all the addressList where customer equals to customerId
        defaultAddressShouldBeFound("customerId.equals=" + customerId);

        // Get all the addressList where customer equals to (customerId + 1)
        defaultAddressShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllAddressesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        Country country = CountryResourceIT.createEntity(em);
        em.persist(country);
        em.flush();
        address.setCountry(country);
        addressRepository.saveAndFlush(address);
        Long countryId = country.getId();

        // Get all the addressList where country equals to countryId
        defaultAddressShouldBeFound("countryId.equals=" + countryId);

        // Get all the addressList where country equals to (countryId + 1)
        defaultAddressShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAddressShouldBeFound(String filter) throws Exception {
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].fullname").value(hasItem(DEFAULT_FULLNAME)))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].streetline1").value(hasItem(DEFAULT_STREETLINE_1)))
            .andExpect(jsonPath("$.[*].streetline2").value(hasItem(DEFAULT_STREETLINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE)))
            .andExpect(jsonPath("$.[*].postalcode").value(hasItem(DEFAULT_POSTALCODE)))
            .andExpect(jsonPath("$.[*].phonenumber").value(hasItem(DEFAULT_PHONENUMBER)))
            .andExpect(jsonPath("$.[*].defaultshippingaddress").value(hasItem(DEFAULT_DEFAULTSHIPPINGADDRESS.booleanValue())))
            .andExpect(jsonPath("$.[*].defaultbillingaddress").value(hasItem(DEFAULT_DEFAULTBILLINGADDRESS.booleanValue())));

        // Check, that the count call also returns 1
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAddressShouldNotBeFound(String filter) throws Exception {
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = addressRepository.findById(address.getId()).get();
        // Disconnect from session so that the updates on updatedAddress are not directly saved in db
        em.detach(updatedAddress);
        updatedAddress
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .fullname(UPDATED_FULLNAME)
            .company(UPDATED_COMPANY)
            .streetline1(UPDATED_STREETLINE_1)
            .streetline2(UPDATED_STREETLINE_2)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE)
            .postalcode(UPDATED_POSTALCODE)
            .phonenumber(UPDATED_PHONENUMBER)
            .defaultshippingaddress(UPDATED_DEFAULTSHIPPINGADDRESS)
            .defaultbillingaddress(UPDATED_DEFAULTBILLINGADDRESS);
        AddressDTO addressDTO = addressMapper.toDto(updatedAddress);

        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testAddress.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testAddress.getFullname()).isEqualTo(UPDATED_FULLNAME);
        assertThat(testAddress.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testAddress.getStreetline1()).isEqualTo(UPDATED_STREETLINE_1);
        assertThat(testAddress.getStreetline2()).isEqualTo(UPDATED_STREETLINE_2);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testAddress.getPostalcode()).isEqualTo(UPDATED_POSTALCODE);
        assertThat(testAddress.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testAddress.getDefaultshippingaddress()).isEqualTo(UPDATED_DEFAULTSHIPPINGADDRESS);
        assertThat(testAddress.getDefaultbillingaddress()).isEqualTo(UPDATED_DEFAULTBILLINGADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        Address partialUpdatedAddress = new Address();
        partialUpdatedAddress.setId(address.getId());

        partialUpdatedAddress
            .createdat(UPDATED_CREATEDAT)
            .fullname(UPDATED_FULLNAME)
            .company(UPDATED_COMPANY)
            .streetline1(UPDATED_STREETLINE_1)
            .streetline2(UPDATED_STREETLINE_2)
            .city(UPDATED_CITY)
            .postalcode(UPDATED_POSTALCODE)
            .phonenumber(UPDATED_PHONENUMBER)
            .defaultshippingaddress(UPDATED_DEFAULTSHIPPINGADDRESS);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testAddress.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testAddress.getFullname()).isEqualTo(UPDATED_FULLNAME);
        assertThat(testAddress.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testAddress.getStreetline1()).isEqualTo(UPDATED_STREETLINE_1);
        assertThat(testAddress.getStreetline2()).isEqualTo(UPDATED_STREETLINE_2);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testAddress.getPostalcode()).isEqualTo(UPDATED_POSTALCODE);
        assertThat(testAddress.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testAddress.getDefaultshippingaddress()).isEqualTo(UPDATED_DEFAULTSHIPPINGADDRESS);
        assertThat(testAddress.getDefaultbillingaddress()).isEqualTo(DEFAULT_DEFAULTBILLINGADDRESS);
    }

    @Test
    @Transactional
    void fullUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        Address partialUpdatedAddress = new Address();
        partialUpdatedAddress.setId(address.getId());

        partialUpdatedAddress
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .fullname(UPDATED_FULLNAME)
            .company(UPDATED_COMPANY)
            .streetline1(UPDATED_STREETLINE_1)
            .streetline2(UPDATED_STREETLINE_2)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE)
            .postalcode(UPDATED_POSTALCODE)
            .phonenumber(UPDATED_PHONENUMBER)
            .defaultshippingaddress(UPDATED_DEFAULTSHIPPINGADDRESS)
            .defaultbillingaddress(UPDATED_DEFAULTBILLINGADDRESS);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testAddress.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testAddress.getFullname()).isEqualTo(UPDATED_FULLNAME);
        assertThat(testAddress.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testAddress.getStreetline1()).isEqualTo(UPDATED_STREETLINE_1);
        assertThat(testAddress.getStreetline2()).isEqualTo(UPDATED_STREETLINE_2);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testAddress.getPostalcode()).isEqualTo(UPDATED_POSTALCODE);
        assertThat(testAddress.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testAddress.getDefaultshippingaddress()).isEqualTo(UPDATED_DEFAULTSHIPPINGADDRESS);
        assertThat(testAddress.getDefaultbillingaddress()).isEqualTo(UPDATED_DEFAULTBILLINGADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, addressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Delete the address
        restAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, address.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
