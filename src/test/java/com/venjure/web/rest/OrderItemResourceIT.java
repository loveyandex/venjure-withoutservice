package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Fulfillment;
import com.venjure.domain.OrderItem;
import com.venjure.domain.OrderLine;
import com.venjure.domain.OrderModification;
import com.venjure.domain.Refund;
import com.venjure.domain.StockMovement;
import com.venjure.repository.OrderItemRepository;
import com.venjure.service.OrderItemService;
import com.venjure.service.criteria.OrderItemCriteria;
import com.venjure.service.dto.OrderItemDTO;
import com.venjure.service.mapper.OrderItemMapper;
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
 * Integration tests for the {@link OrderItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderItemResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_INITIALLISTPRICE = 1;
    private static final Integer UPDATED_INITIALLISTPRICE = 2;
    private static final Integer SMALLER_INITIALLISTPRICE = 1 - 1;

    private static final Integer DEFAULT_LISTPRICE = 1;
    private static final Integer UPDATED_LISTPRICE = 2;
    private static final Integer SMALLER_LISTPRICE = 1 - 1;

    private static final Boolean DEFAULT_LISTPRICEINCLUDESTAX = false;
    private static final Boolean UPDATED_LISTPRICEINCLUDESTAX = true;

    private static final String DEFAULT_ADJUSTMENTS = "AAAAAAAAAA";
    private static final String UPDATED_ADJUSTMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_TAXLINES = "AAAAAAAAAA";
    private static final String UPDATED_TAXLINES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CANCELLED = false;
    private static final Boolean UPDATED_CANCELLED = true;

    private static final String ENTITY_API_URL = "/api/order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemRepository orderItemRepositoryMock;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Mock
    private OrderItemService orderItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemMockMvc;

    private OrderItem orderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .initiallistprice(DEFAULT_INITIALLISTPRICE)
            .listprice(DEFAULT_LISTPRICE)
            .listpriceincludestax(DEFAULT_LISTPRICEINCLUDESTAX)
            .adjustments(DEFAULT_ADJUSTMENTS)
            .taxlines(DEFAULT_TAXLINES)
            .cancelled(DEFAULT_CANCELLED);
        // Add required entity
        OrderLine orderLine;
        if (TestUtil.findAll(em, OrderLine.class).isEmpty()) {
            orderLine = OrderLineResourceIT.createEntity(em);
            em.persist(orderLine);
            em.flush();
        } else {
            orderLine = TestUtil.findAll(em, OrderLine.class).get(0);
        }
        orderItem.setLine(orderLine);
        return orderItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createUpdatedEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .initiallistprice(UPDATED_INITIALLISTPRICE)
            .listprice(UPDATED_LISTPRICE)
            .listpriceincludestax(UPDATED_LISTPRICEINCLUDESTAX)
            .adjustments(UPDATED_ADJUSTMENTS)
            .taxlines(UPDATED_TAXLINES)
            .cancelled(UPDATED_CANCELLED);
        // Add required entity
        OrderLine orderLine;
        if (TestUtil.findAll(em, OrderLine.class).isEmpty()) {
            orderLine = OrderLineResourceIT.createUpdatedEntity(em);
            em.persist(orderLine);
            em.flush();
        } else {
            orderLine = TestUtil.findAll(em, OrderLine.class).get(0);
        }
        orderItem.setLine(orderLine);
        return orderItem;
    }

    @BeforeEach
    public void initTest() {
        orderItem = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderItem() throws Exception {
        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();
        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);
        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeCreate + 1);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testOrderItem.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testOrderItem.getInitiallistprice()).isEqualTo(DEFAULT_INITIALLISTPRICE);
        assertThat(testOrderItem.getListprice()).isEqualTo(DEFAULT_LISTPRICE);
        assertThat(testOrderItem.getListpriceincludestax()).isEqualTo(DEFAULT_LISTPRICEINCLUDESTAX);
        assertThat(testOrderItem.getAdjustments()).isEqualTo(DEFAULT_ADJUSTMENTS);
        assertThat(testOrderItem.getTaxlines()).isEqualTo(DEFAULT_TAXLINES);
        assertThat(testOrderItem.getCancelled()).isEqualTo(DEFAULT_CANCELLED);
    }

    @Test
    @Transactional
    void createOrderItemWithExistingId() throws Exception {
        // Create the OrderItem with an existing ID
        orderItem.setId(1L);
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setCreatedat(null);

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setUpdatedat(null);

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkListpriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setListprice(null);

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkListpriceincludestaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setListpriceincludestax(null);

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdjustmentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setAdjustments(null);

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxlinesIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setTaxlines(null);

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCancelledIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setCancelled(null);

        // Create the OrderItem, which fails.
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderItems() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].initiallistprice").value(hasItem(DEFAULT_INITIALLISTPRICE)))
            .andExpect(jsonPath("$.[*].listprice").value(hasItem(DEFAULT_LISTPRICE)))
            .andExpect(jsonPath("$.[*].listpriceincludestax").value(hasItem(DEFAULT_LISTPRICEINCLUDESTAX.booleanValue())))
            .andExpect(jsonPath("$.[*].adjustments").value(hasItem(DEFAULT_ADJUSTMENTS)))
            .andExpect(jsonPath("$.[*].taxlines").value(hasItem(DEFAULT_TAXLINES)))
            .andExpect(jsonPath("$.[*].cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrderItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get the orderItem
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItem.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.initiallistprice").value(DEFAULT_INITIALLISTPRICE))
            .andExpect(jsonPath("$.listprice").value(DEFAULT_LISTPRICE))
            .andExpect(jsonPath("$.listpriceincludestax").value(DEFAULT_LISTPRICEINCLUDESTAX.booleanValue()))
            .andExpect(jsonPath("$.adjustments").value(DEFAULT_ADJUSTMENTS))
            .andExpect(jsonPath("$.taxlines").value(DEFAULT_TAXLINES))
            .andExpect(jsonPath("$.cancelled").value(DEFAULT_CANCELLED.booleanValue()));
    }

    @Test
    @Transactional
    void getOrderItemsByIdFiltering() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        Long id = orderItem.getId();

        defaultOrderItemShouldBeFound("id.equals=" + id);
        defaultOrderItemShouldNotBeFound("id.notEquals=" + id);

        defaultOrderItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderItemShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where createdat equals to DEFAULT_CREATEDAT
        defaultOrderItemShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the orderItemList where createdat equals to UPDATED_CREATEDAT
        defaultOrderItemShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where createdat not equals to DEFAULT_CREATEDAT
        defaultOrderItemShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the orderItemList where createdat not equals to UPDATED_CREATEDAT
        defaultOrderItemShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultOrderItemShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the orderItemList where createdat equals to UPDATED_CREATEDAT
        defaultOrderItemShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where createdat is not null
        defaultOrderItemShouldBeFound("createdat.specified=true");

        // Get all the orderItemList where createdat is null
        defaultOrderItemShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where updatedat equals to DEFAULT_UPDATEDAT
        defaultOrderItemShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the orderItemList where updatedat equals to UPDATED_UPDATEDAT
        defaultOrderItemShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderItemsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultOrderItemShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the orderItemList where updatedat not equals to UPDATED_UPDATEDAT
        defaultOrderItemShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderItemsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultOrderItemShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the orderItemList where updatedat equals to UPDATED_UPDATEDAT
        defaultOrderItemShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderItemsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where updatedat is not null
        defaultOrderItemShouldBeFound("updatedat.specified=true");

        // Get all the orderItemList where updatedat is null
        defaultOrderItemShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByInitiallistpriceIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where initiallistprice equals to DEFAULT_INITIALLISTPRICE
        defaultOrderItemShouldBeFound("initiallistprice.equals=" + DEFAULT_INITIALLISTPRICE);

        // Get all the orderItemList where initiallistprice equals to UPDATED_INITIALLISTPRICE
        defaultOrderItemShouldNotBeFound("initiallistprice.equals=" + UPDATED_INITIALLISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByInitiallistpriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where initiallistprice not equals to DEFAULT_INITIALLISTPRICE
        defaultOrderItemShouldNotBeFound("initiallistprice.notEquals=" + DEFAULT_INITIALLISTPRICE);

        // Get all the orderItemList where initiallistprice not equals to UPDATED_INITIALLISTPRICE
        defaultOrderItemShouldBeFound("initiallistprice.notEquals=" + UPDATED_INITIALLISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByInitiallistpriceIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where initiallistprice in DEFAULT_INITIALLISTPRICE or UPDATED_INITIALLISTPRICE
        defaultOrderItemShouldBeFound("initiallistprice.in=" + DEFAULT_INITIALLISTPRICE + "," + UPDATED_INITIALLISTPRICE);

        // Get all the orderItemList where initiallistprice equals to UPDATED_INITIALLISTPRICE
        defaultOrderItemShouldNotBeFound("initiallistprice.in=" + UPDATED_INITIALLISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByInitiallistpriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where initiallistprice is not null
        defaultOrderItemShouldBeFound("initiallistprice.specified=true");

        // Get all the orderItemList where initiallistprice is null
        defaultOrderItemShouldNotBeFound("initiallistprice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByInitiallistpriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where initiallistprice is greater than or equal to DEFAULT_INITIALLISTPRICE
        defaultOrderItemShouldBeFound("initiallistprice.greaterThanOrEqual=" + DEFAULT_INITIALLISTPRICE);

        // Get all the orderItemList where initiallistprice is greater than or equal to UPDATED_INITIALLISTPRICE
        defaultOrderItemShouldNotBeFound("initiallistprice.greaterThanOrEqual=" + UPDATED_INITIALLISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByInitiallistpriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where initiallistprice is less than or equal to DEFAULT_INITIALLISTPRICE
        defaultOrderItemShouldBeFound("initiallistprice.lessThanOrEqual=" + DEFAULT_INITIALLISTPRICE);

        // Get all the orderItemList where initiallistprice is less than or equal to SMALLER_INITIALLISTPRICE
        defaultOrderItemShouldNotBeFound("initiallistprice.lessThanOrEqual=" + SMALLER_INITIALLISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByInitiallistpriceIsLessThanSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where initiallistprice is less than DEFAULT_INITIALLISTPRICE
        defaultOrderItemShouldNotBeFound("initiallistprice.lessThan=" + DEFAULT_INITIALLISTPRICE);

        // Get all the orderItemList where initiallistprice is less than UPDATED_INITIALLISTPRICE
        defaultOrderItemShouldBeFound("initiallistprice.lessThan=" + UPDATED_INITIALLISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByInitiallistpriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where initiallistprice is greater than DEFAULT_INITIALLISTPRICE
        defaultOrderItemShouldNotBeFound("initiallistprice.greaterThan=" + DEFAULT_INITIALLISTPRICE);

        // Get all the orderItemList where initiallistprice is greater than SMALLER_INITIALLISTPRICE
        defaultOrderItemShouldBeFound("initiallistprice.greaterThan=" + SMALLER_INITIALLISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByListpriceIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where listprice equals to DEFAULT_LISTPRICE
        defaultOrderItemShouldBeFound("listprice.equals=" + DEFAULT_LISTPRICE);

        // Get all the orderItemList where listprice equals to UPDATED_LISTPRICE
        defaultOrderItemShouldNotBeFound("listprice.equals=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByListpriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where listprice not equals to DEFAULT_LISTPRICE
        defaultOrderItemShouldNotBeFound("listprice.notEquals=" + DEFAULT_LISTPRICE);

        // Get all the orderItemList where listprice not equals to UPDATED_LISTPRICE
        defaultOrderItemShouldBeFound("listprice.notEquals=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByListpriceIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where listprice in DEFAULT_LISTPRICE or UPDATED_LISTPRICE
        defaultOrderItemShouldBeFound("listprice.in=" + DEFAULT_LISTPRICE + "," + UPDATED_LISTPRICE);

        // Get all the orderItemList where listprice equals to UPDATED_LISTPRICE
        defaultOrderItemShouldNotBeFound("listprice.in=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByListpriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where listprice is not null
        defaultOrderItemShouldBeFound("listprice.specified=true");

        // Get all the orderItemList where listprice is null
        defaultOrderItemShouldNotBeFound("listprice.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByListpriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where listprice is greater than or equal to DEFAULT_LISTPRICE
        defaultOrderItemShouldBeFound("listprice.greaterThanOrEqual=" + DEFAULT_LISTPRICE);

        // Get all the orderItemList where listprice is greater than or equal to UPDATED_LISTPRICE
        defaultOrderItemShouldNotBeFound("listprice.greaterThanOrEqual=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByListpriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where listprice is less than or equal to DEFAULT_LISTPRICE
        defaultOrderItemShouldBeFound("listprice.lessThanOrEqual=" + DEFAULT_LISTPRICE);

        // Get all the orderItemList where listprice is less than or equal to SMALLER_LISTPRICE
        defaultOrderItemShouldNotBeFound("listprice.lessThanOrEqual=" + SMALLER_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByListpriceIsLessThanSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where listprice is less than DEFAULT_LISTPRICE
        defaultOrderItemShouldNotBeFound("listprice.lessThan=" + DEFAULT_LISTPRICE);

        // Get all the orderItemList where listprice is less than UPDATED_LISTPRICE
        defaultOrderItemShouldBeFound("listprice.lessThan=" + UPDATED_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByListpriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where listprice is greater than DEFAULT_LISTPRICE
        defaultOrderItemShouldNotBeFound("listprice.greaterThan=" + DEFAULT_LISTPRICE);

        // Get all the orderItemList where listprice is greater than SMALLER_LISTPRICE
        defaultOrderItemShouldBeFound("listprice.greaterThan=" + SMALLER_LISTPRICE);
    }

    @Test
    @Transactional
    void getAllOrderItemsByListpriceincludestaxIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where listpriceincludestax equals to DEFAULT_LISTPRICEINCLUDESTAX
        defaultOrderItemShouldBeFound("listpriceincludestax.equals=" + DEFAULT_LISTPRICEINCLUDESTAX);

        // Get all the orderItemList where listpriceincludestax equals to UPDATED_LISTPRICEINCLUDESTAX
        defaultOrderItemShouldNotBeFound("listpriceincludestax.equals=" + UPDATED_LISTPRICEINCLUDESTAX);
    }

    @Test
    @Transactional
    void getAllOrderItemsByListpriceincludestaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where listpriceincludestax not equals to DEFAULT_LISTPRICEINCLUDESTAX
        defaultOrderItemShouldNotBeFound("listpriceincludestax.notEquals=" + DEFAULT_LISTPRICEINCLUDESTAX);

        // Get all the orderItemList where listpriceincludestax not equals to UPDATED_LISTPRICEINCLUDESTAX
        defaultOrderItemShouldBeFound("listpriceincludestax.notEquals=" + UPDATED_LISTPRICEINCLUDESTAX);
    }

    @Test
    @Transactional
    void getAllOrderItemsByListpriceincludestaxIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where listpriceincludestax in DEFAULT_LISTPRICEINCLUDESTAX or UPDATED_LISTPRICEINCLUDESTAX
        defaultOrderItemShouldBeFound("listpriceincludestax.in=" + DEFAULT_LISTPRICEINCLUDESTAX + "," + UPDATED_LISTPRICEINCLUDESTAX);

        // Get all the orderItemList where listpriceincludestax equals to UPDATED_LISTPRICEINCLUDESTAX
        defaultOrderItemShouldNotBeFound("listpriceincludestax.in=" + UPDATED_LISTPRICEINCLUDESTAX);
    }

    @Test
    @Transactional
    void getAllOrderItemsByListpriceincludestaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where listpriceincludestax is not null
        defaultOrderItemShouldBeFound("listpriceincludestax.specified=true");

        // Get all the orderItemList where listpriceincludestax is null
        defaultOrderItemShouldNotBeFound("listpriceincludestax.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByAdjustmentsIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where adjustments equals to DEFAULT_ADJUSTMENTS
        defaultOrderItemShouldBeFound("adjustments.equals=" + DEFAULT_ADJUSTMENTS);

        // Get all the orderItemList where adjustments equals to UPDATED_ADJUSTMENTS
        defaultOrderItemShouldNotBeFound("adjustments.equals=" + UPDATED_ADJUSTMENTS);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAdjustmentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where adjustments not equals to DEFAULT_ADJUSTMENTS
        defaultOrderItemShouldNotBeFound("adjustments.notEquals=" + DEFAULT_ADJUSTMENTS);

        // Get all the orderItemList where adjustments not equals to UPDATED_ADJUSTMENTS
        defaultOrderItemShouldBeFound("adjustments.notEquals=" + UPDATED_ADJUSTMENTS);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAdjustmentsIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where adjustments in DEFAULT_ADJUSTMENTS or UPDATED_ADJUSTMENTS
        defaultOrderItemShouldBeFound("adjustments.in=" + DEFAULT_ADJUSTMENTS + "," + UPDATED_ADJUSTMENTS);

        // Get all the orderItemList where adjustments equals to UPDATED_ADJUSTMENTS
        defaultOrderItemShouldNotBeFound("adjustments.in=" + UPDATED_ADJUSTMENTS);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAdjustmentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where adjustments is not null
        defaultOrderItemShouldBeFound("adjustments.specified=true");

        // Get all the orderItemList where adjustments is null
        defaultOrderItemShouldNotBeFound("adjustments.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByAdjustmentsContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where adjustments contains DEFAULT_ADJUSTMENTS
        defaultOrderItemShouldBeFound("adjustments.contains=" + DEFAULT_ADJUSTMENTS);

        // Get all the orderItemList where adjustments contains UPDATED_ADJUSTMENTS
        defaultOrderItemShouldNotBeFound("adjustments.contains=" + UPDATED_ADJUSTMENTS);
    }

    @Test
    @Transactional
    void getAllOrderItemsByAdjustmentsNotContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where adjustments does not contain DEFAULT_ADJUSTMENTS
        defaultOrderItemShouldNotBeFound("adjustments.doesNotContain=" + DEFAULT_ADJUSTMENTS);

        // Get all the orderItemList where adjustments does not contain UPDATED_ADJUSTMENTS
        defaultOrderItemShouldBeFound("adjustments.doesNotContain=" + UPDATED_ADJUSTMENTS);
    }

    @Test
    @Transactional
    void getAllOrderItemsByTaxlinesIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where taxlines equals to DEFAULT_TAXLINES
        defaultOrderItemShouldBeFound("taxlines.equals=" + DEFAULT_TAXLINES);

        // Get all the orderItemList where taxlines equals to UPDATED_TAXLINES
        defaultOrderItemShouldNotBeFound("taxlines.equals=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllOrderItemsByTaxlinesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where taxlines not equals to DEFAULT_TAXLINES
        defaultOrderItemShouldNotBeFound("taxlines.notEquals=" + DEFAULT_TAXLINES);

        // Get all the orderItemList where taxlines not equals to UPDATED_TAXLINES
        defaultOrderItemShouldBeFound("taxlines.notEquals=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllOrderItemsByTaxlinesIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where taxlines in DEFAULT_TAXLINES or UPDATED_TAXLINES
        defaultOrderItemShouldBeFound("taxlines.in=" + DEFAULT_TAXLINES + "," + UPDATED_TAXLINES);

        // Get all the orderItemList where taxlines equals to UPDATED_TAXLINES
        defaultOrderItemShouldNotBeFound("taxlines.in=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllOrderItemsByTaxlinesIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where taxlines is not null
        defaultOrderItemShouldBeFound("taxlines.specified=true");

        // Get all the orderItemList where taxlines is null
        defaultOrderItemShouldNotBeFound("taxlines.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByTaxlinesContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where taxlines contains DEFAULT_TAXLINES
        defaultOrderItemShouldBeFound("taxlines.contains=" + DEFAULT_TAXLINES);

        // Get all the orderItemList where taxlines contains UPDATED_TAXLINES
        defaultOrderItemShouldNotBeFound("taxlines.contains=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllOrderItemsByTaxlinesNotContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where taxlines does not contain DEFAULT_TAXLINES
        defaultOrderItemShouldNotBeFound("taxlines.doesNotContain=" + DEFAULT_TAXLINES);

        // Get all the orderItemList where taxlines does not contain UPDATED_TAXLINES
        defaultOrderItemShouldBeFound("taxlines.doesNotContain=" + UPDATED_TAXLINES);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCancelledIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where cancelled equals to DEFAULT_CANCELLED
        defaultOrderItemShouldBeFound("cancelled.equals=" + DEFAULT_CANCELLED);

        // Get all the orderItemList where cancelled equals to UPDATED_CANCELLED
        defaultOrderItemShouldNotBeFound("cancelled.equals=" + UPDATED_CANCELLED);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCancelledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where cancelled not equals to DEFAULT_CANCELLED
        defaultOrderItemShouldNotBeFound("cancelled.notEquals=" + DEFAULT_CANCELLED);

        // Get all the orderItemList where cancelled not equals to UPDATED_CANCELLED
        defaultOrderItemShouldBeFound("cancelled.notEquals=" + UPDATED_CANCELLED);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCancelledIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where cancelled in DEFAULT_CANCELLED or UPDATED_CANCELLED
        defaultOrderItemShouldBeFound("cancelled.in=" + DEFAULT_CANCELLED + "," + UPDATED_CANCELLED);

        // Get all the orderItemList where cancelled equals to UPDATED_CANCELLED
        defaultOrderItemShouldNotBeFound("cancelled.in=" + UPDATED_CANCELLED);
    }

    @Test
    @Transactional
    void getAllOrderItemsByCancelledIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where cancelled is not null
        defaultOrderItemShouldBeFound("cancelled.specified=true");

        // Get all the orderItemList where cancelled is null
        defaultOrderItemShouldNotBeFound("cancelled.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderItemsByLineIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);
        OrderLine line = OrderLineResourceIT.createEntity(em);
        em.persist(line);
        em.flush();
        orderItem.setLine(line);
        orderItemRepository.saveAndFlush(orderItem);
        Long lineId = line.getId();

        // Get all the orderItemList where line equals to lineId
        defaultOrderItemShouldBeFound("lineId.equals=" + lineId);

        // Get all the orderItemList where line equals to (lineId + 1)
        defaultOrderItemShouldNotBeFound("lineId.equals=" + (lineId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByRefundIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);
        Refund refund = RefundResourceIT.createEntity(em);
        em.persist(refund);
        em.flush();
        orderItem.setRefund(refund);
        orderItemRepository.saveAndFlush(orderItem);
        Long refundId = refund.getId();

        // Get all the orderItemList where refund equals to refundId
        defaultOrderItemShouldBeFound("refundId.equals=" + refundId);

        // Get all the orderItemList where refund equals to (refundId + 1)
        defaultOrderItemShouldNotBeFound("refundId.equals=" + (refundId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByFulfillmentIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);
        Fulfillment fulfillment = FulfillmentResourceIT.createEntity(em);
        em.persist(fulfillment);
        em.flush();
        orderItem.addFulfillment(fulfillment);
        orderItemRepository.saveAndFlush(orderItem);
        Long fulfillmentId = fulfillment.getId();

        // Get all the orderItemList where fulfillment equals to fulfillmentId
        defaultOrderItemShouldBeFound("fulfillmentId.equals=" + fulfillmentId);

        // Get all the orderItemList where fulfillment equals to (fulfillmentId + 1)
        defaultOrderItemShouldNotBeFound("fulfillmentId.equals=" + (fulfillmentId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByOrderModificationIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);
        OrderModification orderModification = OrderModificationResourceIT.createEntity(em);
        em.persist(orderModification);
        em.flush();
        orderItem.addOrderModification(orderModification);
        orderItemRepository.saveAndFlush(orderItem);
        Long orderModificationId = orderModification.getId();

        // Get all the orderItemList where orderModification equals to orderModificationId
        defaultOrderItemShouldBeFound("orderModificationId.equals=" + orderModificationId);

        // Get all the orderItemList where orderModification equals to (orderModificationId + 1)
        defaultOrderItemShouldNotBeFound("orderModificationId.equals=" + (orderModificationId + 1));
    }

    @Test
    @Transactional
    void getAllOrderItemsByStockMovementIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);
        StockMovement stockMovement = StockMovementResourceIT.createEntity(em);
        em.persist(stockMovement);
        em.flush();
        orderItem.addStockMovement(stockMovement);
        orderItemRepository.saveAndFlush(orderItem);
        Long stockMovementId = stockMovement.getId();

        // Get all the orderItemList where stockMovement equals to stockMovementId
        defaultOrderItemShouldBeFound("stockMovementId.equals=" + stockMovementId);

        // Get all the orderItemList where stockMovement equals to (stockMovementId + 1)
        defaultOrderItemShouldNotBeFound("stockMovementId.equals=" + (stockMovementId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderItemShouldBeFound(String filter) throws Exception {
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].initiallistprice").value(hasItem(DEFAULT_INITIALLISTPRICE)))
            .andExpect(jsonPath("$.[*].listprice").value(hasItem(DEFAULT_LISTPRICE)))
            .andExpect(jsonPath("$.[*].listpriceincludestax").value(hasItem(DEFAULT_LISTPRICEINCLUDESTAX.booleanValue())))
            .andExpect(jsonPath("$.[*].adjustments").value(hasItem(DEFAULT_ADJUSTMENTS)))
            .andExpect(jsonPath("$.[*].taxlines").value(hasItem(DEFAULT_TAXLINES)))
            .andExpect(jsonPath("$.[*].cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())));

        // Check, that the count call also returns 1
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderItemShouldNotBeFound(String filter) throws Exception {
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderItem() throws Exception {
        // Get the orderItem
        restOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem
        OrderItem updatedOrderItem = orderItemRepository.findById(orderItem.getId()).get();
        // Disconnect from session so that the updates on updatedOrderItem are not directly saved in db
        em.detach(updatedOrderItem);
        updatedOrderItem
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .initiallistprice(UPDATED_INITIALLISTPRICE)
            .listprice(UPDATED_LISTPRICE)
            .listpriceincludestax(UPDATED_LISTPRICEINCLUDESTAX)
            .adjustments(UPDATED_ADJUSTMENTS)
            .taxlines(UPDATED_TAXLINES)
            .cancelled(UPDATED_CANCELLED);
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(updatedOrderItem);

        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testOrderItem.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testOrderItem.getInitiallistprice()).isEqualTo(UPDATED_INITIALLISTPRICE);
        assertThat(testOrderItem.getListprice()).isEqualTo(UPDATED_LISTPRICE);
        assertThat(testOrderItem.getListpriceincludestax()).isEqualTo(UPDATED_LISTPRICEINCLUDESTAX);
        assertThat(testOrderItem.getAdjustments()).isEqualTo(UPDATED_ADJUSTMENTS);
        assertThat(testOrderItem.getTaxlines()).isEqualTo(UPDATED_TAXLINES);
        assertThat(testOrderItem.getCancelled()).isEqualTo(UPDATED_CANCELLED);
    }

    @Test
    @Transactional
    void putNonExistingOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(count.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(count.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(count.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem using partial update
        OrderItem partialUpdatedOrderItem = new OrderItem();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem
            .listprice(UPDATED_LISTPRICE)
            .listpriceincludestax(UPDATED_LISTPRICEINCLUDESTAX)
            .taxlines(UPDATED_TAXLINES)
            .cancelled(UPDATED_CANCELLED);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testOrderItem.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testOrderItem.getInitiallistprice()).isEqualTo(DEFAULT_INITIALLISTPRICE);
        assertThat(testOrderItem.getListprice()).isEqualTo(UPDATED_LISTPRICE);
        assertThat(testOrderItem.getListpriceincludestax()).isEqualTo(UPDATED_LISTPRICEINCLUDESTAX);
        assertThat(testOrderItem.getAdjustments()).isEqualTo(DEFAULT_ADJUSTMENTS);
        assertThat(testOrderItem.getTaxlines()).isEqualTo(UPDATED_TAXLINES);
        assertThat(testOrderItem.getCancelled()).isEqualTo(UPDATED_CANCELLED);
    }

    @Test
    @Transactional
    void fullUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem using partial update
        OrderItem partialUpdatedOrderItem = new OrderItem();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .initiallistprice(UPDATED_INITIALLISTPRICE)
            .listprice(UPDATED_LISTPRICE)
            .listpriceincludestax(UPDATED_LISTPRICEINCLUDESTAX)
            .adjustments(UPDATED_ADJUSTMENTS)
            .taxlines(UPDATED_TAXLINES)
            .cancelled(UPDATED_CANCELLED);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testOrderItem.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testOrderItem.getInitiallistprice()).isEqualTo(UPDATED_INITIALLISTPRICE);
        assertThat(testOrderItem.getListprice()).isEqualTo(UPDATED_LISTPRICE);
        assertThat(testOrderItem.getListpriceincludestax()).isEqualTo(UPDATED_LISTPRICEINCLUDESTAX);
        assertThat(testOrderItem.getAdjustments()).isEqualTo(UPDATED_ADJUSTMENTS);
        assertThat(testOrderItem.getTaxlines()).isEqualTo(UPDATED_TAXLINES);
        assertThat(testOrderItem.getCancelled()).isEqualTo(UPDATED_CANCELLED);
    }

    @Test
    @Transactional
    void patchNonExistingOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(count.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(count.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(count.incrementAndGet());

        // Create the OrderItem
        OrderItemDTO orderItemDTO = orderItemMapper.toDto(orderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        int databaseSizeBeforeDelete = orderItemRepository.findAll().size();

        // Delete the orderItem
        restOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
