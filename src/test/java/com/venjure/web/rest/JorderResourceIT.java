package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Channel;
import com.venjure.domain.Customer;
import com.venjure.domain.HistoryEntry;
import com.venjure.domain.Jorder;
import com.venjure.domain.OrderLine;
import com.venjure.domain.OrderModification;
import com.venjure.domain.Payment;
import com.venjure.domain.Promotion;
import com.venjure.domain.ShippingLine;
import com.venjure.domain.Surcharge;
import com.venjure.repository.JorderRepository;
import com.venjure.service.JorderService;
import com.venjure.service.criteria.JorderCriteria;
import com.venjure.service.dto.JorderDTO;
import com.venjure.service.mapper.JorderMapper;
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
 * Integration tests for the {@link JorderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JorderResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_ORDERPLACEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDERPLACEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COUPONCODES = "AAAAAAAAAA";
    private static final String UPDATED_COUPONCODES = "BBBBBBBBBB";

    private static final String DEFAULT_SHIPPINGADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPINGADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_BILLINGADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_BILLINGADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCYCODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCYCODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_SUBTOTAL = 1;
    private static final Integer UPDATED_SUBTOTAL = 2;
    private static final Integer SMALLER_SUBTOTAL = 1 - 1;

    private static final Integer DEFAULT_SUBTOTALWITHTAX = 1;
    private static final Integer UPDATED_SUBTOTALWITHTAX = 2;
    private static final Integer SMALLER_SUBTOTALWITHTAX = 1 - 1;

    private static final Integer DEFAULT_SHIPPING = 1;
    private static final Integer UPDATED_SHIPPING = 2;
    private static final Integer SMALLER_SHIPPING = 1 - 1;

    private static final Integer DEFAULT_SHIPPINGWITHTAX = 1;
    private static final Integer UPDATED_SHIPPINGWITHTAX = 2;
    private static final Integer SMALLER_SHIPPINGWITHTAX = 1 - 1;

    private static final Integer DEFAULT_TAXZONEID = 1;
    private static final Integer UPDATED_TAXZONEID = 2;
    private static final Integer SMALLER_TAXZONEID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/jorders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JorderRepository jorderRepository;

    @Mock
    private JorderRepository jorderRepositoryMock;

    @Autowired
    private JorderMapper jorderMapper;

    @Mock
    private JorderService jorderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJorderMockMvc;

    private Jorder jorder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jorder createEntity(EntityManager em) {
        Jorder jorder = new Jorder()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .code(DEFAULT_CODE)
            .state(DEFAULT_STATE)
            .active(DEFAULT_ACTIVE)
            .orderplacedat(DEFAULT_ORDERPLACEDAT)
            .couponcodes(DEFAULT_COUPONCODES)
            .shippingaddress(DEFAULT_SHIPPINGADDRESS)
            .billingaddress(DEFAULT_BILLINGADDRESS)
            .currencycode(DEFAULT_CURRENCYCODE)
            .subtotal(DEFAULT_SUBTOTAL)
            .subtotalwithtax(DEFAULT_SUBTOTALWITHTAX)
            .shipping(DEFAULT_SHIPPING)
            .shippingwithtax(DEFAULT_SHIPPINGWITHTAX)
            .taxzoneid(DEFAULT_TAXZONEID);
        return jorder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jorder createUpdatedEntity(EntityManager em) {
        Jorder jorder = new Jorder()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .code(UPDATED_CODE)
            .state(UPDATED_STATE)
            .active(UPDATED_ACTIVE)
            .orderplacedat(UPDATED_ORDERPLACEDAT)
            .couponcodes(UPDATED_COUPONCODES)
            .shippingaddress(UPDATED_SHIPPINGADDRESS)
            .billingaddress(UPDATED_BILLINGADDRESS)
            .currencycode(UPDATED_CURRENCYCODE)
            .subtotal(UPDATED_SUBTOTAL)
            .subtotalwithtax(UPDATED_SUBTOTALWITHTAX)
            .shipping(UPDATED_SHIPPING)
            .shippingwithtax(UPDATED_SHIPPINGWITHTAX)
            .taxzoneid(UPDATED_TAXZONEID);
        return jorder;
    }

    @BeforeEach
    public void initTest() {
        jorder = createEntity(em);
    }

    @Test
    @Transactional
    void createJorder() throws Exception {
        int databaseSizeBeforeCreate = jorderRepository.findAll().size();
        // Create the Jorder
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);
        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isCreated());

        // Validate the Jorder in the database
        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeCreate + 1);
        Jorder testJorder = jorderList.get(jorderList.size() - 1);
        assertThat(testJorder.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testJorder.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testJorder.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testJorder.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testJorder.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testJorder.getOrderplacedat()).isEqualTo(DEFAULT_ORDERPLACEDAT);
        assertThat(testJorder.getCouponcodes()).isEqualTo(DEFAULT_COUPONCODES);
        assertThat(testJorder.getShippingaddress()).isEqualTo(DEFAULT_SHIPPINGADDRESS);
        assertThat(testJorder.getBillingaddress()).isEqualTo(DEFAULT_BILLINGADDRESS);
        assertThat(testJorder.getCurrencycode()).isEqualTo(DEFAULT_CURRENCYCODE);
        assertThat(testJorder.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
        assertThat(testJorder.getSubtotalwithtax()).isEqualTo(DEFAULT_SUBTOTALWITHTAX);
        assertThat(testJorder.getShipping()).isEqualTo(DEFAULT_SHIPPING);
        assertThat(testJorder.getShippingwithtax()).isEqualTo(DEFAULT_SHIPPINGWITHTAX);
        assertThat(testJorder.getTaxzoneid()).isEqualTo(DEFAULT_TAXZONEID);
    }

    @Test
    @Transactional
    void createJorderWithExistingId() throws Exception {
        // Create the Jorder with an existing ID
        jorder.setId(1L);
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        int databaseSizeBeforeCreate = jorderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Jorder in the database
        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setCreatedat(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setUpdatedat(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setCode(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setState(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setActive(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCouponcodesIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setCouponcodes(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShippingaddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setShippingaddress(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBillingaddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setBillingaddress(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencycodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setCurrencycode(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubtotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setSubtotal(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubtotalwithtaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setSubtotalwithtax(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShippingIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setShipping(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShippingwithtaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = jorderRepository.findAll().size();
        // set the field null
        jorder.setShippingwithtax(null);

        // Create the Jorder, which fails.
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        restJorderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isBadRequest());

        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJorders() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList
        restJorderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jorder.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].orderplacedat").value(hasItem(DEFAULT_ORDERPLACEDAT.toString())))
            .andExpect(jsonPath("$.[*].couponcodes").value(hasItem(DEFAULT_COUPONCODES)))
            .andExpect(jsonPath("$.[*].shippingaddress").value(hasItem(DEFAULT_SHIPPINGADDRESS)))
            .andExpect(jsonPath("$.[*].billingaddress").value(hasItem(DEFAULT_BILLINGADDRESS)))
            .andExpect(jsonPath("$.[*].currencycode").value(hasItem(DEFAULT_CURRENCYCODE)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL)))
            .andExpect(jsonPath("$.[*].subtotalwithtax").value(hasItem(DEFAULT_SUBTOTALWITHTAX)))
            .andExpect(jsonPath("$.[*].shipping").value(hasItem(DEFAULT_SHIPPING)))
            .andExpect(jsonPath("$.[*].shippingwithtax").value(hasItem(DEFAULT_SHIPPINGWITHTAX)))
            .andExpect(jsonPath("$.[*].taxzoneid").value(hasItem(DEFAULT_TAXZONEID)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJordersWithEagerRelationshipsIsEnabled() throws Exception {
        when(jorderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJorderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jorderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJordersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(jorderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJorderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jorderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getJorder() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get the jorder
        restJorderMockMvc
            .perform(get(ENTITY_API_URL_ID, jorder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jorder.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.orderplacedat").value(DEFAULT_ORDERPLACEDAT.toString()))
            .andExpect(jsonPath("$.couponcodes").value(DEFAULT_COUPONCODES))
            .andExpect(jsonPath("$.shippingaddress").value(DEFAULT_SHIPPINGADDRESS))
            .andExpect(jsonPath("$.billingaddress").value(DEFAULT_BILLINGADDRESS))
            .andExpect(jsonPath("$.currencycode").value(DEFAULT_CURRENCYCODE))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL))
            .andExpect(jsonPath("$.subtotalwithtax").value(DEFAULT_SUBTOTALWITHTAX))
            .andExpect(jsonPath("$.shipping").value(DEFAULT_SHIPPING))
            .andExpect(jsonPath("$.shippingwithtax").value(DEFAULT_SHIPPINGWITHTAX))
            .andExpect(jsonPath("$.taxzoneid").value(DEFAULT_TAXZONEID));
    }

    @Test
    @Transactional
    void getJordersByIdFiltering() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        Long id = jorder.getId();

        defaultJorderShouldBeFound("id.equals=" + id);
        defaultJorderShouldNotBeFound("id.notEquals=" + id);

        defaultJorderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJorderShouldNotBeFound("id.greaterThan=" + id);

        defaultJorderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJorderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllJordersByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where createdat equals to DEFAULT_CREATEDAT
        defaultJorderShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the jorderList where createdat equals to UPDATED_CREATEDAT
        defaultJorderShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllJordersByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where createdat not equals to DEFAULT_CREATEDAT
        defaultJorderShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the jorderList where createdat not equals to UPDATED_CREATEDAT
        defaultJorderShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllJordersByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultJorderShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the jorderList where createdat equals to UPDATED_CREATEDAT
        defaultJorderShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllJordersByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where createdat is not null
        defaultJorderShouldBeFound("createdat.specified=true");

        // Get all the jorderList where createdat is null
        defaultJorderShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where updatedat equals to DEFAULT_UPDATEDAT
        defaultJorderShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the jorderList where updatedat equals to UPDATED_UPDATEDAT
        defaultJorderShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllJordersByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultJorderShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the jorderList where updatedat not equals to UPDATED_UPDATEDAT
        defaultJorderShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllJordersByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultJorderShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the jorderList where updatedat equals to UPDATED_UPDATEDAT
        defaultJorderShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllJordersByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where updatedat is not null
        defaultJorderShouldBeFound("updatedat.specified=true");

        // Get all the jorderList where updatedat is null
        defaultJorderShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where code equals to DEFAULT_CODE
        defaultJorderShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the jorderList where code equals to UPDATED_CODE
        defaultJorderShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllJordersByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where code not equals to DEFAULT_CODE
        defaultJorderShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the jorderList where code not equals to UPDATED_CODE
        defaultJorderShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllJordersByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where code in DEFAULT_CODE or UPDATED_CODE
        defaultJorderShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the jorderList where code equals to UPDATED_CODE
        defaultJorderShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllJordersByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where code is not null
        defaultJorderShouldBeFound("code.specified=true");

        // Get all the jorderList where code is null
        defaultJorderShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByCodeContainsSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where code contains DEFAULT_CODE
        defaultJorderShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the jorderList where code contains UPDATED_CODE
        defaultJorderShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllJordersByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where code does not contain DEFAULT_CODE
        defaultJorderShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the jorderList where code does not contain UPDATED_CODE
        defaultJorderShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllJordersByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where state equals to DEFAULT_STATE
        defaultJorderShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the jorderList where state equals to UPDATED_STATE
        defaultJorderShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllJordersByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where state not equals to DEFAULT_STATE
        defaultJorderShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the jorderList where state not equals to UPDATED_STATE
        defaultJorderShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllJordersByStateIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where state in DEFAULT_STATE or UPDATED_STATE
        defaultJorderShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the jorderList where state equals to UPDATED_STATE
        defaultJorderShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllJordersByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where state is not null
        defaultJorderShouldBeFound("state.specified=true");

        // Get all the jorderList where state is null
        defaultJorderShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByStateContainsSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where state contains DEFAULT_STATE
        defaultJorderShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the jorderList where state contains UPDATED_STATE
        defaultJorderShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllJordersByStateNotContainsSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where state does not contain DEFAULT_STATE
        defaultJorderShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the jorderList where state does not contain UPDATED_STATE
        defaultJorderShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllJordersByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where active equals to DEFAULT_ACTIVE
        defaultJorderShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the jorderList where active equals to UPDATED_ACTIVE
        defaultJorderShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllJordersByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where active not equals to DEFAULT_ACTIVE
        defaultJorderShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the jorderList where active not equals to UPDATED_ACTIVE
        defaultJorderShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllJordersByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultJorderShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the jorderList where active equals to UPDATED_ACTIVE
        defaultJorderShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllJordersByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where active is not null
        defaultJorderShouldBeFound("active.specified=true");

        // Get all the jorderList where active is null
        defaultJorderShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByOrderplacedatIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where orderplacedat equals to DEFAULT_ORDERPLACEDAT
        defaultJorderShouldBeFound("orderplacedat.equals=" + DEFAULT_ORDERPLACEDAT);

        // Get all the jorderList where orderplacedat equals to UPDATED_ORDERPLACEDAT
        defaultJorderShouldNotBeFound("orderplacedat.equals=" + UPDATED_ORDERPLACEDAT);
    }

    @Test
    @Transactional
    void getAllJordersByOrderplacedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where orderplacedat not equals to DEFAULT_ORDERPLACEDAT
        defaultJorderShouldNotBeFound("orderplacedat.notEquals=" + DEFAULT_ORDERPLACEDAT);

        // Get all the jorderList where orderplacedat not equals to UPDATED_ORDERPLACEDAT
        defaultJorderShouldBeFound("orderplacedat.notEquals=" + UPDATED_ORDERPLACEDAT);
    }

    @Test
    @Transactional
    void getAllJordersByOrderplacedatIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where orderplacedat in DEFAULT_ORDERPLACEDAT or UPDATED_ORDERPLACEDAT
        defaultJorderShouldBeFound("orderplacedat.in=" + DEFAULT_ORDERPLACEDAT + "," + UPDATED_ORDERPLACEDAT);

        // Get all the jorderList where orderplacedat equals to UPDATED_ORDERPLACEDAT
        defaultJorderShouldNotBeFound("orderplacedat.in=" + UPDATED_ORDERPLACEDAT);
    }

    @Test
    @Transactional
    void getAllJordersByOrderplacedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where orderplacedat is not null
        defaultJorderShouldBeFound("orderplacedat.specified=true");

        // Get all the jorderList where orderplacedat is null
        defaultJorderShouldNotBeFound("orderplacedat.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByCouponcodesIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where couponcodes equals to DEFAULT_COUPONCODES
        defaultJorderShouldBeFound("couponcodes.equals=" + DEFAULT_COUPONCODES);

        // Get all the jorderList where couponcodes equals to UPDATED_COUPONCODES
        defaultJorderShouldNotBeFound("couponcodes.equals=" + UPDATED_COUPONCODES);
    }

    @Test
    @Transactional
    void getAllJordersByCouponcodesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where couponcodes not equals to DEFAULT_COUPONCODES
        defaultJorderShouldNotBeFound("couponcodes.notEquals=" + DEFAULT_COUPONCODES);

        // Get all the jorderList where couponcodes not equals to UPDATED_COUPONCODES
        defaultJorderShouldBeFound("couponcodes.notEquals=" + UPDATED_COUPONCODES);
    }

    @Test
    @Transactional
    void getAllJordersByCouponcodesIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where couponcodes in DEFAULT_COUPONCODES or UPDATED_COUPONCODES
        defaultJorderShouldBeFound("couponcodes.in=" + DEFAULT_COUPONCODES + "," + UPDATED_COUPONCODES);

        // Get all the jorderList where couponcodes equals to UPDATED_COUPONCODES
        defaultJorderShouldNotBeFound("couponcodes.in=" + UPDATED_COUPONCODES);
    }

    @Test
    @Transactional
    void getAllJordersByCouponcodesIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where couponcodes is not null
        defaultJorderShouldBeFound("couponcodes.specified=true");

        // Get all the jorderList where couponcodes is null
        defaultJorderShouldNotBeFound("couponcodes.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByCouponcodesContainsSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where couponcodes contains DEFAULT_COUPONCODES
        defaultJorderShouldBeFound("couponcodes.contains=" + DEFAULT_COUPONCODES);

        // Get all the jorderList where couponcodes contains UPDATED_COUPONCODES
        defaultJorderShouldNotBeFound("couponcodes.contains=" + UPDATED_COUPONCODES);
    }

    @Test
    @Transactional
    void getAllJordersByCouponcodesNotContainsSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where couponcodes does not contain DEFAULT_COUPONCODES
        defaultJorderShouldNotBeFound("couponcodes.doesNotContain=" + DEFAULT_COUPONCODES);

        // Get all the jorderList where couponcodes does not contain UPDATED_COUPONCODES
        defaultJorderShouldBeFound("couponcodes.doesNotContain=" + UPDATED_COUPONCODES);
    }

    @Test
    @Transactional
    void getAllJordersByShippingaddressIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingaddress equals to DEFAULT_SHIPPINGADDRESS
        defaultJorderShouldBeFound("shippingaddress.equals=" + DEFAULT_SHIPPINGADDRESS);

        // Get all the jorderList where shippingaddress equals to UPDATED_SHIPPINGADDRESS
        defaultJorderShouldNotBeFound("shippingaddress.equals=" + UPDATED_SHIPPINGADDRESS);
    }

    @Test
    @Transactional
    void getAllJordersByShippingaddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingaddress not equals to DEFAULT_SHIPPINGADDRESS
        defaultJorderShouldNotBeFound("shippingaddress.notEquals=" + DEFAULT_SHIPPINGADDRESS);

        // Get all the jorderList where shippingaddress not equals to UPDATED_SHIPPINGADDRESS
        defaultJorderShouldBeFound("shippingaddress.notEquals=" + UPDATED_SHIPPINGADDRESS);
    }

    @Test
    @Transactional
    void getAllJordersByShippingaddressIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingaddress in DEFAULT_SHIPPINGADDRESS or UPDATED_SHIPPINGADDRESS
        defaultJorderShouldBeFound("shippingaddress.in=" + DEFAULT_SHIPPINGADDRESS + "," + UPDATED_SHIPPINGADDRESS);

        // Get all the jorderList where shippingaddress equals to UPDATED_SHIPPINGADDRESS
        defaultJorderShouldNotBeFound("shippingaddress.in=" + UPDATED_SHIPPINGADDRESS);
    }

    @Test
    @Transactional
    void getAllJordersByShippingaddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingaddress is not null
        defaultJorderShouldBeFound("shippingaddress.specified=true");

        // Get all the jorderList where shippingaddress is null
        defaultJorderShouldNotBeFound("shippingaddress.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByShippingaddressContainsSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingaddress contains DEFAULT_SHIPPINGADDRESS
        defaultJorderShouldBeFound("shippingaddress.contains=" + DEFAULT_SHIPPINGADDRESS);

        // Get all the jorderList where shippingaddress contains UPDATED_SHIPPINGADDRESS
        defaultJorderShouldNotBeFound("shippingaddress.contains=" + UPDATED_SHIPPINGADDRESS);
    }

    @Test
    @Transactional
    void getAllJordersByShippingaddressNotContainsSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingaddress does not contain DEFAULT_SHIPPINGADDRESS
        defaultJorderShouldNotBeFound("shippingaddress.doesNotContain=" + DEFAULT_SHIPPINGADDRESS);

        // Get all the jorderList where shippingaddress does not contain UPDATED_SHIPPINGADDRESS
        defaultJorderShouldBeFound("shippingaddress.doesNotContain=" + UPDATED_SHIPPINGADDRESS);
    }

    @Test
    @Transactional
    void getAllJordersByBillingaddressIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where billingaddress equals to DEFAULT_BILLINGADDRESS
        defaultJorderShouldBeFound("billingaddress.equals=" + DEFAULT_BILLINGADDRESS);

        // Get all the jorderList where billingaddress equals to UPDATED_BILLINGADDRESS
        defaultJorderShouldNotBeFound("billingaddress.equals=" + UPDATED_BILLINGADDRESS);
    }

    @Test
    @Transactional
    void getAllJordersByBillingaddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where billingaddress not equals to DEFAULT_BILLINGADDRESS
        defaultJorderShouldNotBeFound("billingaddress.notEquals=" + DEFAULT_BILLINGADDRESS);

        // Get all the jorderList where billingaddress not equals to UPDATED_BILLINGADDRESS
        defaultJorderShouldBeFound("billingaddress.notEquals=" + UPDATED_BILLINGADDRESS);
    }

    @Test
    @Transactional
    void getAllJordersByBillingaddressIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where billingaddress in DEFAULT_BILLINGADDRESS or UPDATED_BILLINGADDRESS
        defaultJorderShouldBeFound("billingaddress.in=" + DEFAULT_BILLINGADDRESS + "," + UPDATED_BILLINGADDRESS);

        // Get all the jorderList where billingaddress equals to UPDATED_BILLINGADDRESS
        defaultJorderShouldNotBeFound("billingaddress.in=" + UPDATED_BILLINGADDRESS);
    }

    @Test
    @Transactional
    void getAllJordersByBillingaddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where billingaddress is not null
        defaultJorderShouldBeFound("billingaddress.specified=true");

        // Get all the jorderList where billingaddress is null
        defaultJorderShouldNotBeFound("billingaddress.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByBillingaddressContainsSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where billingaddress contains DEFAULT_BILLINGADDRESS
        defaultJorderShouldBeFound("billingaddress.contains=" + DEFAULT_BILLINGADDRESS);

        // Get all the jorderList where billingaddress contains UPDATED_BILLINGADDRESS
        defaultJorderShouldNotBeFound("billingaddress.contains=" + UPDATED_BILLINGADDRESS);
    }

    @Test
    @Transactional
    void getAllJordersByBillingaddressNotContainsSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where billingaddress does not contain DEFAULT_BILLINGADDRESS
        defaultJorderShouldNotBeFound("billingaddress.doesNotContain=" + DEFAULT_BILLINGADDRESS);

        // Get all the jorderList where billingaddress does not contain UPDATED_BILLINGADDRESS
        defaultJorderShouldBeFound("billingaddress.doesNotContain=" + UPDATED_BILLINGADDRESS);
    }

    @Test
    @Transactional
    void getAllJordersByCurrencycodeIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where currencycode equals to DEFAULT_CURRENCYCODE
        defaultJorderShouldBeFound("currencycode.equals=" + DEFAULT_CURRENCYCODE);

        // Get all the jorderList where currencycode equals to UPDATED_CURRENCYCODE
        defaultJorderShouldNotBeFound("currencycode.equals=" + UPDATED_CURRENCYCODE);
    }

    @Test
    @Transactional
    void getAllJordersByCurrencycodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where currencycode not equals to DEFAULT_CURRENCYCODE
        defaultJorderShouldNotBeFound("currencycode.notEquals=" + DEFAULT_CURRENCYCODE);

        // Get all the jorderList where currencycode not equals to UPDATED_CURRENCYCODE
        defaultJorderShouldBeFound("currencycode.notEquals=" + UPDATED_CURRENCYCODE);
    }

    @Test
    @Transactional
    void getAllJordersByCurrencycodeIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where currencycode in DEFAULT_CURRENCYCODE or UPDATED_CURRENCYCODE
        defaultJorderShouldBeFound("currencycode.in=" + DEFAULT_CURRENCYCODE + "," + UPDATED_CURRENCYCODE);

        // Get all the jorderList where currencycode equals to UPDATED_CURRENCYCODE
        defaultJorderShouldNotBeFound("currencycode.in=" + UPDATED_CURRENCYCODE);
    }

    @Test
    @Transactional
    void getAllJordersByCurrencycodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where currencycode is not null
        defaultJorderShouldBeFound("currencycode.specified=true");

        // Get all the jorderList where currencycode is null
        defaultJorderShouldNotBeFound("currencycode.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByCurrencycodeContainsSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where currencycode contains DEFAULT_CURRENCYCODE
        defaultJorderShouldBeFound("currencycode.contains=" + DEFAULT_CURRENCYCODE);

        // Get all the jorderList where currencycode contains UPDATED_CURRENCYCODE
        defaultJorderShouldNotBeFound("currencycode.contains=" + UPDATED_CURRENCYCODE);
    }

    @Test
    @Transactional
    void getAllJordersByCurrencycodeNotContainsSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where currencycode does not contain DEFAULT_CURRENCYCODE
        defaultJorderShouldNotBeFound("currencycode.doesNotContain=" + DEFAULT_CURRENCYCODE);

        // Get all the jorderList where currencycode does not contain UPDATED_CURRENCYCODE
        defaultJorderShouldBeFound("currencycode.doesNotContain=" + UPDATED_CURRENCYCODE);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotal equals to DEFAULT_SUBTOTAL
        defaultJorderShouldBeFound("subtotal.equals=" + DEFAULT_SUBTOTAL);

        // Get all the jorderList where subtotal equals to UPDATED_SUBTOTAL
        defaultJorderShouldNotBeFound("subtotal.equals=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotal not equals to DEFAULT_SUBTOTAL
        defaultJorderShouldNotBeFound("subtotal.notEquals=" + DEFAULT_SUBTOTAL);

        // Get all the jorderList where subtotal not equals to UPDATED_SUBTOTAL
        defaultJorderShouldBeFound("subtotal.notEquals=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotal in DEFAULT_SUBTOTAL or UPDATED_SUBTOTAL
        defaultJorderShouldBeFound("subtotal.in=" + DEFAULT_SUBTOTAL + "," + UPDATED_SUBTOTAL);

        // Get all the jorderList where subtotal equals to UPDATED_SUBTOTAL
        defaultJorderShouldNotBeFound("subtotal.in=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotal is not null
        defaultJorderShouldBeFound("subtotal.specified=true");

        // Get all the jorderList where subtotal is null
        defaultJorderShouldNotBeFound("subtotal.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotal is greater than or equal to DEFAULT_SUBTOTAL
        defaultJorderShouldBeFound("subtotal.greaterThanOrEqual=" + DEFAULT_SUBTOTAL);

        // Get all the jorderList where subtotal is greater than or equal to UPDATED_SUBTOTAL
        defaultJorderShouldNotBeFound("subtotal.greaterThanOrEqual=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotal is less than or equal to DEFAULT_SUBTOTAL
        defaultJorderShouldBeFound("subtotal.lessThanOrEqual=" + DEFAULT_SUBTOTAL);

        // Get all the jorderList where subtotal is less than or equal to SMALLER_SUBTOTAL
        defaultJorderShouldNotBeFound("subtotal.lessThanOrEqual=" + SMALLER_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalIsLessThanSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotal is less than DEFAULT_SUBTOTAL
        defaultJorderShouldNotBeFound("subtotal.lessThan=" + DEFAULT_SUBTOTAL);

        // Get all the jorderList where subtotal is less than UPDATED_SUBTOTAL
        defaultJorderShouldBeFound("subtotal.lessThan=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotal is greater than DEFAULT_SUBTOTAL
        defaultJorderShouldNotBeFound("subtotal.greaterThan=" + DEFAULT_SUBTOTAL);

        // Get all the jorderList where subtotal is greater than SMALLER_SUBTOTAL
        defaultJorderShouldBeFound("subtotal.greaterThan=" + SMALLER_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalwithtaxIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotalwithtax equals to DEFAULT_SUBTOTALWITHTAX
        defaultJorderShouldBeFound("subtotalwithtax.equals=" + DEFAULT_SUBTOTALWITHTAX);

        // Get all the jorderList where subtotalwithtax equals to UPDATED_SUBTOTALWITHTAX
        defaultJorderShouldNotBeFound("subtotalwithtax.equals=" + UPDATED_SUBTOTALWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalwithtaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotalwithtax not equals to DEFAULT_SUBTOTALWITHTAX
        defaultJorderShouldNotBeFound("subtotalwithtax.notEquals=" + DEFAULT_SUBTOTALWITHTAX);

        // Get all the jorderList where subtotalwithtax not equals to UPDATED_SUBTOTALWITHTAX
        defaultJorderShouldBeFound("subtotalwithtax.notEquals=" + UPDATED_SUBTOTALWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalwithtaxIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotalwithtax in DEFAULT_SUBTOTALWITHTAX or UPDATED_SUBTOTALWITHTAX
        defaultJorderShouldBeFound("subtotalwithtax.in=" + DEFAULT_SUBTOTALWITHTAX + "," + UPDATED_SUBTOTALWITHTAX);

        // Get all the jorderList where subtotalwithtax equals to UPDATED_SUBTOTALWITHTAX
        defaultJorderShouldNotBeFound("subtotalwithtax.in=" + UPDATED_SUBTOTALWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalwithtaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotalwithtax is not null
        defaultJorderShouldBeFound("subtotalwithtax.specified=true");

        // Get all the jorderList where subtotalwithtax is null
        defaultJorderShouldNotBeFound("subtotalwithtax.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalwithtaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotalwithtax is greater than or equal to DEFAULT_SUBTOTALWITHTAX
        defaultJorderShouldBeFound("subtotalwithtax.greaterThanOrEqual=" + DEFAULT_SUBTOTALWITHTAX);

        // Get all the jorderList where subtotalwithtax is greater than or equal to UPDATED_SUBTOTALWITHTAX
        defaultJorderShouldNotBeFound("subtotalwithtax.greaterThanOrEqual=" + UPDATED_SUBTOTALWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalwithtaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotalwithtax is less than or equal to DEFAULT_SUBTOTALWITHTAX
        defaultJorderShouldBeFound("subtotalwithtax.lessThanOrEqual=" + DEFAULT_SUBTOTALWITHTAX);

        // Get all the jorderList where subtotalwithtax is less than or equal to SMALLER_SUBTOTALWITHTAX
        defaultJorderShouldNotBeFound("subtotalwithtax.lessThanOrEqual=" + SMALLER_SUBTOTALWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalwithtaxIsLessThanSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotalwithtax is less than DEFAULT_SUBTOTALWITHTAX
        defaultJorderShouldNotBeFound("subtotalwithtax.lessThan=" + DEFAULT_SUBTOTALWITHTAX);

        // Get all the jorderList where subtotalwithtax is less than UPDATED_SUBTOTALWITHTAX
        defaultJorderShouldBeFound("subtotalwithtax.lessThan=" + UPDATED_SUBTOTALWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersBySubtotalwithtaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where subtotalwithtax is greater than DEFAULT_SUBTOTALWITHTAX
        defaultJorderShouldNotBeFound("subtotalwithtax.greaterThan=" + DEFAULT_SUBTOTALWITHTAX);

        // Get all the jorderList where subtotalwithtax is greater than SMALLER_SUBTOTALWITHTAX
        defaultJorderShouldBeFound("subtotalwithtax.greaterThan=" + SMALLER_SUBTOTALWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersByShippingIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shipping equals to DEFAULT_SHIPPING
        defaultJorderShouldBeFound("shipping.equals=" + DEFAULT_SHIPPING);

        // Get all the jorderList where shipping equals to UPDATED_SHIPPING
        defaultJorderShouldNotBeFound("shipping.equals=" + UPDATED_SHIPPING);
    }

    @Test
    @Transactional
    void getAllJordersByShippingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shipping not equals to DEFAULT_SHIPPING
        defaultJorderShouldNotBeFound("shipping.notEquals=" + DEFAULT_SHIPPING);

        // Get all the jorderList where shipping not equals to UPDATED_SHIPPING
        defaultJorderShouldBeFound("shipping.notEquals=" + UPDATED_SHIPPING);
    }

    @Test
    @Transactional
    void getAllJordersByShippingIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shipping in DEFAULT_SHIPPING or UPDATED_SHIPPING
        defaultJorderShouldBeFound("shipping.in=" + DEFAULT_SHIPPING + "," + UPDATED_SHIPPING);

        // Get all the jorderList where shipping equals to UPDATED_SHIPPING
        defaultJorderShouldNotBeFound("shipping.in=" + UPDATED_SHIPPING);
    }

    @Test
    @Transactional
    void getAllJordersByShippingIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shipping is not null
        defaultJorderShouldBeFound("shipping.specified=true");

        // Get all the jorderList where shipping is null
        defaultJorderShouldNotBeFound("shipping.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByShippingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shipping is greater than or equal to DEFAULT_SHIPPING
        defaultJorderShouldBeFound("shipping.greaterThanOrEqual=" + DEFAULT_SHIPPING);

        // Get all the jorderList where shipping is greater than or equal to UPDATED_SHIPPING
        defaultJorderShouldNotBeFound("shipping.greaterThanOrEqual=" + UPDATED_SHIPPING);
    }

    @Test
    @Transactional
    void getAllJordersByShippingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shipping is less than or equal to DEFAULT_SHIPPING
        defaultJorderShouldBeFound("shipping.lessThanOrEqual=" + DEFAULT_SHIPPING);

        // Get all the jorderList where shipping is less than or equal to SMALLER_SHIPPING
        defaultJorderShouldNotBeFound("shipping.lessThanOrEqual=" + SMALLER_SHIPPING);
    }

    @Test
    @Transactional
    void getAllJordersByShippingIsLessThanSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shipping is less than DEFAULT_SHIPPING
        defaultJorderShouldNotBeFound("shipping.lessThan=" + DEFAULT_SHIPPING);

        // Get all the jorderList where shipping is less than UPDATED_SHIPPING
        defaultJorderShouldBeFound("shipping.lessThan=" + UPDATED_SHIPPING);
    }

    @Test
    @Transactional
    void getAllJordersByShippingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shipping is greater than DEFAULT_SHIPPING
        defaultJorderShouldNotBeFound("shipping.greaterThan=" + DEFAULT_SHIPPING);

        // Get all the jorderList where shipping is greater than SMALLER_SHIPPING
        defaultJorderShouldBeFound("shipping.greaterThan=" + SMALLER_SHIPPING);
    }

    @Test
    @Transactional
    void getAllJordersByShippingwithtaxIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingwithtax equals to DEFAULT_SHIPPINGWITHTAX
        defaultJorderShouldBeFound("shippingwithtax.equals=" + DEFAULT_SHIPPINGWITHTAX);

        // Get all the jorderList where shippingwithtax equals to UPDATED_SHIPPINGWITHTAX
        defaultJorderShouldNotBeFound("shippingwithtax.equals=" + UPDATED_SHIPPINGWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersByShippingwithtaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingwithtax not equals to DEFAULT_SHIPPINGWITHTAX
        defaultJorderShouldNotBeFound("shippingwithtax.notEquals=" + DEFAULT_SHIPPINGWITHTAX);

        // Get all the jorderList where shippingwithtax not equals to UPDATED_SHIPPINGWITHTAX
        defaultJorderShouldBeFound("shippingwithtax.notEquals=" + UPDATED_SHIPPINGWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersByShippingwithtaxIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingwithtax in DEFAULT_SHIPPINGWITHTAX or UPDATED_SHIPPINGWITHTAX
        defaultJorderShouldBeFound("shippingwithtax.in=" + DEFAULT_SHIPPINGWITHTAX + "," + UPDATED_SHIPPINGWITHTAX);

        // Get all the jorderList where shippingwithtax equals to UPDATED_SHIPPINGWITHTAX
        defaultJorderShouldNotBeFound("shippingwithtax.in=" + UPDATED_SHIPPINGWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersByShippingwithtaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingwithtax is not null
        defaultJorderShouldBeFound("shippingwithtax.specified=true");

        // Get all the jorderList where shippingwithtax is null
        defaultJorderShouldNotBeFound("shippingwithtax.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByShippingwithtaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingwithtax is greater than or equal to DEFAULT_SHIPPINGWITHTAX
        defaultJorderShouldBeFound("shippingwithtax.greaterThanOrEqual=" + DEFAULT_SHIPPINGWITHTAX);

        // Get all the jorderList where shippingwithtax is greater than or equal to UPDATED_SHIPPINGWITHTAX
        defaultJorderShouldNotBeFound("shippingwithtax.greaterThanOrEqual=" + UPDATED_SHIPPINGWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersByShippingwithtaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingwithtax is less than or equal to DEFAULT_SHIPPINGWITHTAX
        defaultJorderShouldBeFound("shippingwithtax.lessThanOrEqual=" + DEFAULT_SHIPPINGWITHTAX);

        // Get all the jorderList where shippingwithtax is less than or equal to SMALLER_SHIPPINGWITHTAX
        defaultJorderShouldNotBeFound("shippingwithtax.lessThanOrEqual=" + SMALLER_SHIPPINGWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersByShippingwithtaxIsLessThanSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingwithtax is less than DEFAULT_SHIPPINGWITHTAX
        defaultJorderShouldNotBeFound("shippingwithtax.lessThan=" + DEFAULT_SHIPPINGWITHTAX);

        // Get all the jorderList where shippingwithtax is less than UPDATED_SHIPPINGWITHTAX
        defaultJorderShouldBeFound("shippingwithtax.lessThan=" + UPDATED_SHIPPINGWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersByShippingwithtaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where shippingwithtax is greater than DEFAULT_SHIPPINGWITHTAX
        defaultJorderShouldNotBeFound("shippingwithtax.greaterThan=" + DEFAULT_SHIPPINGWITHTAX);

        // Get all the jorderList where shippingwithtax is greater than SMALLER_SHIPPINGWITHTAX
        defaultJorderShouldBeFound("shippingwithtax.greaterThan=" + SMALLER_SHIPPINGWITHTAX);
    }

    @Test
    @Transactional
    void getAllJordersByTaxzoneidIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where taxzoneid equals to DEFAULT_TAXZONEID
        defaultJorderShouldBeFound("taxzoneid.equals=" + DEFAULT_TAXZONEID);

        // Get all the jorderList where taxzoneid equals to UPDATED_TAXZONEID
        defaultJorderShouldNotBeFound("taxzoneid.equals=" + UPDATED_TAXZONEID);
    }

    @Test
    @Transactional
    void getAllJordersByTaxzoneidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where taxzoneid not equals to DEFAULT_TAXZONEID
        defaultJorderShouldNotBeFound("taxzoneid.notEquals=" + DEFAULT_TAXZONEID);

        // Get all the jorderList where taxzoneid not equals to UPDATED_TAXZONEID
        defaultJorderShouldBeFound("taxzoneid.notEquals=" + UPDATED_TAXZONEID);
    }

    @Test
    @Transactional
    void getAllJordersByTaxzoneidIsInShouldWork() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where taxzoneid in DEFAULT_TAXZONEID or UPDATED_TAXZONEID
        defaultJorderShouldBeFound("taxzoneid.in=" + DEFAULT_TAXZONEID + "," + UPDATED_TAXZONEID);

        // Get all the jorderList where taxzoneid equals to UPDATED_TAXZONEID
        defaultJorderShouldNotBeFound("taxzoneid.in=" + UPDATED_TAXZONEID);
    }

    @Test
    @Transactional
    void getAllJordersByTaxzoneidIsNullOrNotNull() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where taxzoneid is not null
        defaultJorderShouldBeFound("taxzoneid.specified=true");

        // Get all the jorderList where taxzoneid is null
        defaultJorderShouldNotBeFound("taxzoneid.specified=false");
    }

    @Test
    @Transactional
    void getAllJordersByTaxzoneidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where taxzoneid is greater than or equal to DEFAULT_TAXZONEID
        defaultJorderShouldBeFound("taxzoneid.greaterThanOrEqual=" + DEFAULT_TAXZONEID);

        // Get all the jorderList where taxzoneid is greater than or equal to UPDATED_TAXZONEID
        defaultJorderShouldNotBeFound("taxzoneid.greaterThanOrEqual=" + UPDATED_TAXZONEID);
    }

    @Test
    @Transactional
    void getAllJordersByTaxzoneidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where taxzoneid is less than or equal to DEFAULT_TAXZONEID
        defaultJorderShouldBeFound("taxzoneid.lessThanOrEqual=" + DEFAULT_TAXZONEID);

        // Get all the jorderList where taxzoneid is less than or equal to SMALLER_TAXZONEID
        defaultJorderShouldNotBeFound("taxzoneid.lessThanOrEqual=" + SMALLER_TAXZONEID);
    }

    @Test
    @Transactional
    void getAllJordersByTaxzoneidIsLessThanSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where taxzoneid is less than DEFAULT_TAXZONEID
        defaultJorderShouldNotBeFound("taxzoneid.lessThan=" + DEFAULT_TAXZONEID);

        // Get all the jorderList where taxzoneid is less than UPDATED_TAXZONEID
        defaultJorderShouldBeFound("taxzoneid.lessThan=" + UPDATED_TAXZONEID);
    }

    @Test
    @Transactional
    void getAllJordersByTaxzoneidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        // Get all the jorderList where taxzoneid is greater than DEFAULT_TAXZONEID
        defaultJorderShouldNotBeFound("taxzoneid.greaterThan=" + DEFAULT_TAXZONEID);

        // Get all the jorderList where taxzoneid is greater than SMALLER_TAXZONEID
        defaultJorderShouldBeFound("taxzoneid.greaterThan=" + SMALLER_TAXZONEID);
    }

    @Test
    @Transactional
    void getAllJordersByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        jorder.setCustomer(customer);
        jorderRepository.saveAndFlush(jorder);
        Long customerId = customer.getId();

        // Get all the jorderList where customer equals to customerId
        defaultJorderShouldBeFound("customerId.equals=" + customerId);

        // Get all the jorderList where customer equals to (customerId + 1)
        defaultJorderShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllJordersByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);
        Channel channel = ChannelResourceIT.createEntity(em);
        em.persist(channel);
        em.flush();
        jorder.addChannel(channel);
        jorderRepository.saveAndFlush(jorder);
        Long channelId = channel.getId();

        // Get all the jorderList where channel equals to channelId
        defaultJorderShouldBeFound("channelId.equals=" + channelId);

        // Get all the jorderList where channel equals to (channelId + 1)
        defaultJorderShouldNotBeFound("channelId.equals=" + (channelId + 1));
    }

    @Test
    @Transactional
    void getAllJordersByPromotionIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);
        Promotion promotion = PromotionResourceIT.createEntity(em);
        em.persist(promotion);
        em.flush();
        jorder.addPromotion(promotion);
        jorderRepository.saveAndFlush(jorder);
        Long promotionId = promotion.getId();

        // Get all the jorderList where promotion equals to promotionId
        defaultJorderShouldBeFound("promotionId.equals=" + promotionId);

        // Get all the jorderList where promotion equals to (promotionId + 1)
        defaultJorderShouldNotBeFound("promotionId.equals=" + (promotionId + 1));
    }

    @Test
    @Transactional
    void getAllJordersByHistoryEntryIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);
        HistoryEntry historyEntry = HistoryEntryResourceIT.createEntity(em);
        em.persist(historyEntry);
        em.flush();
        jorder.addHistoryEntry(historyEntry);
        jorderRepository.saveAndFlush(jorder);
        Long historyEntryId = historyEntry.getId();

        // Get all the jorderList where historyEntry equals to historyEntryId
        defaultJorderShouldBeFound("historyEntryId.equals=" + historyEntryId);

        // Get all the jorderList where historyEntry equals to (historyEntryId + 1)
        defaultJorderShouldNotBeFound("historyEntryId.equals=" + (historyEntryId + 1));
    }

    @Test
    @Transactional
    void getAllJordersByOrderLineIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);
        OrderLine orderLine = OrderLineResourceIT.createEntity(em);
        em.persist(orderLine);
        em.flush();
        jorder.addOrderLine(orderLine);
        jorderRepository.saveAndFlush(jorder);
        Long orderLineId = orderLine.getId();

        // Get all the jorderList where orderLine equals to orderLineId
        defaultJorderShouldBeFound("orderLineId.equals=" + orderLineId);

        // Get all the jorderList where orderLine equals to (orderLineId + 1)
        defaultJorderShouldNotBeFound("orderLineId.equals=" + (orderLineId + 1));
    }

    @Test
    @Transactional
    void getAllJordersByOrderModificationIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);
        OrderModification orderModification = OrderModificationResourceIT.createEntity(em);
        em.persist(orderModification);
        em.flush();
        jorder.addOrderModification(orderModification);
        jorderRepository.saveAndFlush(jorder);
        Long orderModificationId = orderModification.getId();

        // Get all the jorderList where orderModification equals to orderModificationId
        defaultJorderShouldBeFound("orderModificationId.equals=" + orderModificationId);

        // Get all the jorderList where orderModification equals to (orderModificationId + 1)
        defaultJorderShouldNotBeFound("orderModificationId.equals=" + (orderModificationId + 1));
    }

    @Test
    @Transactional
    void getAllJordersByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);
        Payment payment = PaymentResourceIT.createEntity(em);
        em.persist(payment);
        em.flush();
        jorder.addPayment(payment);
        jorderRepository.saveAndFlush(jorder);
        Long paymentId = payment.getId();

        // Get all the jorderList where payment equals to paymentId
        defaultJorderShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the jorderList where payment equals to (paymentId + 1)
        defaultJorderShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllJordersByShippingLineIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);
        ShippingLine shippingLine = ShippingLineResourceIT.createEntity(em);
        em.persist(shippingLine);
        em.flush();
        jorder.addShippingLine(shippingLine);
        jorderRepository.saveAndFlush(jorder);
        Long shippingLineId = shippingLine.getId();

        // Get all the jorderList where shippingLine equals to shippingLineId
        defaultJorderShouldBeFound("shippingLineId.equals=" + shippingLineId);

        // Get all the jorderList where shippingLine equals to (shippingLineId + 1)
        defaultJorderShouldNotBeFound("shippingLineId.equals=" + (shippingLineId + 1));
    }

    @Test
    @Transactional
    void getAllJordersBySurchargeIsEqualToSomething() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);
        Surcharge surcharge = SurchargeResourceIT.createEntity(em);
        em.persist(surcharge);
        em.flush();
        jorder.addSurcharge(surcharge);
        jorderRepository.saveAndFlush(jorder);
        Long surchargeId = surcharge.getId();

        // Get all the jorderList where surcharge equals to surchargeId
        defaultJorderShouldBeFound("surchargeId.equals=" + surchargeId);

        // Get all the jorderList where surcharge equals to (surchargeId + 1)
        defaultJorderShouldNotBeFound("surchargeId.equals=" + (surchargeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJorderShouldBeFound(String filter) throws Exception {
        restJorderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jorder.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].orderplacedat").value(hasItem(DEFAULT_ORDERPLACEDAT.toString())))
            .andExpect(jsonPath("$.[*].couponcodes").value(hasItem(DEFAULT_COUPONCODES)))
            .andExpect(jsonPath("$.[*].shippingaddress").value(hasItem(DEFAULT_SHIPPINGADDRESS)))
            .andExpect(jsonPath("$.[*].billingaddress").value(hasItem(DEFAULT_BILLINGADDRESS)))
            .andExpect(jsonPath("$.[*].currencycode").value(hasItem(DEFAULT_CURRENCYCODE)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL)))
            .andExpect(jsonPath("$.[*].subtotalwithtax").value(hasItem(DEFAULT_SUBTOTALWITHTAX)))
            .andExpect(jsonPath("$.[*].shipping").value(hasItem(DEFAULT_SHIPPING)))
            .andExpect(jsonPath("$.[*].shippingwithtax").value(hasItem(DEFAULT_SHIPPINGWITHTAX)))
            .andExpect(jsonPath("$.[*].taxzoneid").value(hasItem(DEFAULT_TAXZONEID)));

        // Check, that the count call also returns 1
        restJorderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJorderShouldNotBeFound(String filter) throws Exception {
        restJorderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJorderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingJorder() throws Exception {
        // Get the jorder
        restJorderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJorder() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        int databaseSizeBeforeUpdate = jorderRepository.findAll().size();

        // Update the jorder
        Jorder updatedJorder = jorderRepository.findById(jorder.getId()).get();
        // Disconnect from session so that the updates on updatedJorder are not directly saved in db
        em.detach(updatedJorder);
        updatedJorder
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .code(UPDATED_CODE)
            .state(UPDATED_STATE)
            .active(UPDATED_ACTIVE)
            .orderplacedat(UPDATED_ORDERPLACEDAT)
            .couponcodes(UPDATED_COUPONCODES)
            .shippingaddress(UPDATED_SHIPPINGADDRESS)
            .billingaddress(UPDATED_BILLINGADDRESS)
            .currencycode(UPDATED_CURRENCYCODE)
            .subtotal(UPDATED_SUBTOTAL)
            .subtotalwithtax(UPDATED_SUBTOTALWITHTAX)
            .shipping(UPDATED_SHIPPING)
            .shippingwithtax(UPDATED_SHIPPINGWITHTAX)
            .taxzoneid(UPDATED_TAXZONEID);
        JorderDTO jorderDTO = jorderMapper.toDto(updatedJorder);

        restJorderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jorderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jorderDTO))
            )
            .andExpect(status().isOk());

        // Validate the Jorder in the database
        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeUpdate);
        Jorder testJorder = jorderList.get(jorderList.size() - 1);
        assertThat(testJorder.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testJorder.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testJorder.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testJorder.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testJorder.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testJorder.getOrderplacedat()).isEqualTo(UPDATED_ORDERPLACEDAT);
        assertThat(testJorder.getCouponcodes()).isEqualTo(UPDATED_COUPONCODES);
        assertThat(testJorder.getShippingaddress()).isEqualTo(UPDATED_SHIPPINGADDRESS);
        assertThat(testJorder.getBillingaddress()).isEqualTo(UPDATED_BILLINGADDRESS);
        assertThat(testJorder.getCurrencycode()).isEqualTo(UPDATED_CURRENCYCODE);
        assertThat(testJorder.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testJorder.getSubtotalwithtax()).isEqualTo(UPDATED_SUBTOTALWITHTAX);
        assertThat(testJorder.getShipping()).isEqualTo(UPDATED_SHIPPING);
        assertThat(testJorder.getShippingwithtax()).isEqualTo(UPDATED_SHIPPINGWITHTAX);
        assertThat(testJorder.getTaxzoneid()).isEqualTo(UPDATED_TAXZONEID);
    }

    @Test
    @Transactional
    void putNonExistingJorder() throws Exception {
        int databaseSizeBeforeUpdate = jorderRepository.findAll().size();
        jorder.setId(count.incrementAndGet());

        // Create the Jorder
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJorderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jorderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jorderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jorder in the database
        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJorder() throws Exception {
        int databaseSizeBeforeUpdate = jorderRepository.findAll().size();
        jorder.setId(count.incrementAndGet());

        // Create the Jorder
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJorderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jorderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jorder in the database
        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJorder() throws Exception {
        int databaseSizeBeforeUpdate = jorderRepository.findAll().size();
        jorder.setId(count.incrementAndGet());

        // Create the Jorder
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJorderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jorderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Jorder in the database
        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJorderWithPatch() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        int databaseSizeBeforeUpdate = jorderRepository.findAll().size();

        // Update the jorder using partial update
        Jorder partialUpdatedJorder = new Jorder();
        partialUpdatedJorder.setId(jorder.getId());

        partialUpdatedJorder
            .updatedat(UPDATED_UPDATEDAT)
            .state(UPDATED_STATE)
            .active(UPDATED_ACTIVE)
            .shippingaddress(UPDATED_SHIPPINGADDRESS)
            .billingaddress(UPDATED_BILLINGADDRESS)
            .subtotal(UPDATED_SUBTOTAL)
            .subtotalwithtax(UPDATED_SUBTOTALWITHTAX)
            .shippingwithtax(UPDATED_SHIPPINGWITHTAX);

        restJorderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJorder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJorder))
            )
            .andExpect(status().isOk());

        // Validate the Jorder in the database
        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeUpdate);
        Jorder testJorder = jorderList.get(jorderList.size() - 1);
        assertThat(testJorder.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testJorder.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testJorder.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testJorder.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testJorder.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testJorder.getOrderplacedat()).isEqualTo(DEFAULT_ORDERPLACEDAT);
        assertThat(testJorder.getCouponcodes()).isEqualTo(DEFAULT_COUPONCODES);
        assertThat(testJorder.getShippingaddress()).isEqualTo(UPDATED_SHIPPINGADDRESS);
        assertThat(testJorder.getBillingaddress()).isEqualTo(UPDATED_BILLINGADDRESS);
        assertThat(testJorder.getCurrencycode()).isEqualTo(DEFAULT_CURRENCYCODE);
        assertThat(testJorder.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testJorder.getSubtotalwithtax()).isEqualTo(UPDATED_SUBTOTALWITHTAX);
        assertThat(testJorder.getShipping()).isEqualTo(DEFAULT_SHIPPING);
        assertThat(testJorder.getShippingwithtax()).isEqualTo(UPDATED_SHIPPINGWITHTAX);
        assertThat(testJorder.getTaxzoneid()).isEqualTo(DEFAULT_TAXZONEID);
    }

    @Test
    @Transactional
    void fullUpdateJorderWithPatch() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        int databaseSizeBeforeUpdate = jorderRepository.findAll().size();

        // Update the jorder using partial update
        Jorder partialUpdatedJorder = new Jorder();
        partialUpdatedJorder.setId(jorder.getId());

        partialUpdatedJorder
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .code(UPDATED_CODE)
            .state(UPDATED_STATE)
            .active(UPDATED_ACTIVE)
            .orderplacedat(UPDATED_ORDERPLACEDAT)
            .couponcodes(UPDATED_COUPONCODES)
            .shippingaddress(UPDATED_SHIPPINGADDRESS)
            .billingaddress(UPDATED_BILLINGADDRESS)
            .currencycode(UPDATED_CURRENCYCODE)
            .subtotal(UPDATED_SUBTOTAL)
            .subtotalwithtax(UPDATED_SUBTOTALWITHTAX)
            .shipping(UPDATED_SHIPPING)
            .shippingwithtax(UPDATED_SHIPPINGWITHTAX)
            .taxzoneid(UPDATED_TAXZONEID);

        restJorderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJorder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJorder))
            )
            .andExpect(status().isOk());

        // Validate the Jorder in the database
        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeUpdate);
        Jorder testJorder = jorderList.get(jorderList.size() - 1);
        assertThat(testJorder.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testJorder.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testJorder.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testJorder.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testJorder.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testJorder.getOrderplacedat()).isEqualTo(UPDATED_ORDERPLACEDAT);
        assertThat(testJorder.getCouponcodes()).isEqualTo(UPDATED_COUPONCODES);
        assertThat(testJorder.getShippingaddress()).isEqualTo(UPDATED_SHIPPINGADDRESS);
        assertThat(testJorder.getBillingaddress()).isEqualTo(UPDATED_BILLINGADDRESS);
        assertThat(testJorder.getCurrencycode()).isEqualTo(UPDATED_CURRENCYCODE);
        assertThat(testJorder.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testJorder.getSubtotalwithtax()).isEqualTo(UPDATED_SUBTOTALWITHTAX);
        assertThat(testJorder.getShipping()).isEqualTo(UPDATED_SHIPPING);
        assertThat(testJorder.getShippingwithtax()).isEqualTo(UPDATED_SHIPPINGWITHTAX);
        assertThat(testJorder.getTaxzoneid()).isEqualTo(UPDATED_TAXZONEID);
    }

    @Test
    @Transactional
    void patchNonExistingJorder() throws Exception {
        int databaseSizeBeforeUpdate = jorderRepository.findAll().size();
        jorder.setId(count.incrementAndGet());

        // Create the Jorder
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJorderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jorderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jorderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jorder in the database
        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJorder() throws Exception {
        int databaseSizeBeforeUpdate = jorderRepository.findAll().size();
        jorder.setId(count.incrementAndGet());

        // Create the Jorder
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJorderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jorderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Jorder in the database
        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJorder() throws Exception {
        int databaseSizeBeforeUpdate = jorderRepository.findAll().size();
        jorder.setId(count.incrementAndGet());

        // Create the Jorder
        JorderDTO jorderDTO = jorderMapper.toDto(jorder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJorderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jorderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Jorder in the database
        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJorder() throws Exception {
        // Initialize the database
        jorderRepository.saveAndFlush(jorder);

        int databaseSizeBeforeDelete = jorderRepository.findAll().size();

        // Delete the jorder
        restJorderMockMvc
            .perform(delete(ENTITY_API_URL_ID, jorder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Jorder> jorderList = jorderRepository.findAll();
        assertThat(jorderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
