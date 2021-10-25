package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.ProductVariant;
import com.venjure.domain.ProductVariantPrice;
import com.venjure.repository.ProductVariantPriceRepository;
import com.venjure.service.criteria.ProductVariantPriceCriteria;
import com.venjure.service.dto.ProductVariantPriceDTO;
import com.venjure.service.mapper.ProductVariantPriceMapper;
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
 * Integration tests for the {@link ProductVariantPriceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductVariantPriceResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;
    private static final Integer SMALLER_PRICE = 1 - 1;

    private static final Integer DEFAULT_CHANNELID = 1;
    private static final Integer UPDATED_CHANNELID = 2;
    private static final Integer SMALLER_CHANNELID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/product-variant-prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductVariantPriceRepository productVariantPriceRepository;

    @Autowired
    private ProductVariantPriceMapper productVariantPriceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductVariantPriceMockMvc;

    private ProductVariantPrice productVariantPrice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVariantPrice createEntity(EntityManager em) {
        ProductVariantPrice productVariantPrice = new ProductVariantPrice()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .price(DEFAULT_PRICE)
            .channelid(DEFAULT_CHANNELID);
        return productVariantPrice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductVariantPrice createUpdatedEntity(EntityManager em) {
        ProductVariantPrice productVariantPrice = new ProductVariantPrice()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .price(UPDATED_PRICE)
            .channelid(UPDATED_CHANNELID);
        return productVariantPrice;
    }

    @BeforeEach
    public void initTest() {
        productVariantPrice = createEntity(em);
    }

    @Test
    @Transactional
    void createProductVariantPrice() throws Exception {
        int databaseSizeBeforeCreate = productVariantPriceRepository.findAll().size();
        // Create the ProductVariantPrice
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(productVariantPrice);
        restProductVariantPriceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductVariantPrice in the database
        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeCreate + 1);
        ProductVariantPrice testProductVariantPrice = productVariantPriceList.get(productVariantPriceList.size() - 1);
        assertThat(testProductVariantPrice.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testProductVariantPrice.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductVariantPrice.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProductVariantPrice.getChannelid()).isEqualTo(DEFAULT_CHANNELID);
    }

    @Test
    @Transactional
    void createProductVariantPriceWithExistingId() throws Exception {
        // Create the ProductVariantPrice with an existing ID
        productVariantPrice.setId(1L);
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(productVariantPrice);

        int databaseSizeBeforeCreate = productVariantPriceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductVariantPriceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantPrice in the database
        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantPriceRepository.findAll().size();
        // set the field null
        productVariantPrice.setCreatedat(null);

        // Create the ProductVariantPrice, which fails.
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(productVariantPrice);

        restProductVariantPriceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantPriceRepository.findAll().size();
        // set the field null
        productVariantPrice.setUpdatedat(null);

        // Create the ProductVariantPrice, which fails.
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(productVariantPrice);

        restProductVariantPriceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantPriceRepository.findAll().size();
        // set the field null
        productVariantPrice.setPrice(null);

        // Create the ProductVariantPrice, which fails.
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(productVariantPrice);

        restProductVariantPriceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChannelidIsRequired() throws Exception {
        int databaseSizeBeforeTest = productVariantPriceRepository.findAll().size();
        // set the field null
        productVariantPrice.setChannelid(null);

        // Create the ProductVariantPrice, which fails.
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(productVariantPrice);

        restProductVariantPriceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductVariantPrices() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList
        restProductVariantPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVariantPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].channelid").value(hasItem(DEFAULT_CHANNELID)));
    }

    @Test
    @Transactional
    void getProductVariantPrice() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get the productVariantPrice
        restProductVariantPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, productVariantPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productVariantPrice.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.channelid").value(DEFAULT_CHANNELID));
    }

    @Test
    @Transactional
    void getProductVariantPricesByIdFiltering() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        Long id = productVariantPrice.getId();

        defaultProductVariantPriceShouldBeFound("id.equals=" + id);
        defaultProductVariantPriceShouldNotBeFound("id.notEquals=" + id);

        defaultProductVariantPriceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductVariantPriceShouldNotBeFound("id.greaterThan=" + id);

        defaultProductVariantPriceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductVariantPriceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where createdat equals to DEFAULT_CREATEDAT
        defaultProductVariantPriceShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the productVariantPriceList where createdat equals to UPDATED_CREATEDAT
        defaultProductVariantPriceShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where createdat not equals to DEFAULT_CREATEDAT
        defaultProductVariantPriceShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the productVariantPriceList where createdat not equals to UPDATED_CREATEDAT
        defaultProductVariantPriceShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultProductVariantPriceShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the productVariantPriceList where createdat equals to UPDATED_CREATEDAT
        defaultProductVariantPriceShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where createdat is not null
        defaultProductVariantPriceShouldBeFound("createdat.specified=true");

        // Get all the productVariantPriceList where createdat is null
        defaultProductVariantPriceShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where updatedat equals to DEFAULT_UPDATEDAT
        defaultProductVariantPriceShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the productVariantPriceList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductVariantPriceShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultProductVariantPriceShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the productVariantPriceList where updatedat not equals to UPDATED_UPDATEDAT
        defaultProductVariantPriceShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultProductVariantPriceShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the productVariantPriceList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductVariantPriceShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where updatedat is not null
        defaultProductVariantPriceShouldBeFound("updatedat.specified=true");

        // Get all the productVariantPriceList where updatedat is null
        defaultProductVariantPriceShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where price equals to DEFAULT_PRICE
        defaultProductVariantPriceShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the productVariantPriceList where price equals to UPDATED_PRICE
        defaultProductVariantPriceShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where price not equals to DEFAULT_PRICE
        defaultProductVariantPriceShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the productVariantPriceList where price not equals to UPDATED_PRICE
        defaultProductVariantPriceShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProductVariantPriceShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the productVariantPriceList where price equals to UPDATED_PRICE
        defaultProductVariantPriceShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where price is not null
        defaultProductVariantPriceShouldBeFound("price.specified=true");

        // Get all the productVariantPriceList where price is null
        defaultProductVariantPriceShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where price is greater than or equal to DEFAULT_PRICE
        defaultProductVariantPriceShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the productVariantPriceList where price is greater than or equal to UPDATED_PRICE
        defaultProductVariantPriceShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where price is less than or equal to DEFAULT_PRICE
        defaultProductVariantPriceShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the productVariantPriceList where price is less than or equal to SMALLER_PRICE
        defaultProductVariantPriceShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where price is less than DEFAULT_PRICE
        defaultProductVariantPriceShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the productVariantPriceList where price is less than UPDATED_PRICE
        defaultProductVariantPriceShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where price is greater than DEFAULT_PRICE
        defaultProductVariantPriceShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the productVariantPriceList where price is greater than SMALLER_PRICE
        defaultProductVariantPriceShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByChannelidIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where channelid equals to DEFAULT_CHANNELID
        defaultProductVariantPriceShouldBeFound("channelid.equals=" + DEFAULT_CHANNELID);

        // Get all the productVariantPriceList where channelid equals to UPDATED_CHANNELID
        defaultProductVariantPriceShouldNotBeFound("channelid.equals=" + UPDATED_CHANNELID);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByChannelidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where channelid not equals to DEFAULT_CHANNELID
        defaultProductVariantPriceShouldNotBeFound("channelid.notEquals=" + DEFAULT_CHANNELID);

        // Get all the productVariantPriceList where channelid not equals to UPDATED_CHANNELID
        defaultProductVariantPriceShouldBeFound("channelid.notEquals=" + UPDATED_CHANNELID);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByChannelidIsInShouldWork() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where channelid in DEFAULT_CHANNELID or UPDATED_CHANNELID
        defaultProductVariantPriceShouldBeFound("channelid.in=" + DEFAULT_CHANNELID + "," + UPDATED_CHANNELID);

        // Get all the productVariantPriceList where channelid equals to UPDATED_CHANNELID
        defaultProductVariantPriceShouldNotBeFound("channelid.in=" + UPDATED_CHANNELID);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByChannelidIsNullOrNotNull() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where channelid is not null
        defaultProductVariantPriceShouldBeFound("channelid.specified=true");

        // Get all the productVariantPriceList where channelid is null
        defaultProductVariantPriceShouldNotBeFound("channelid.specified=false");
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByChannelidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where channelid is greater than or equal to DEFAULT_CHANNELID
        defaultProductVariantPriceShouldBeFound("channelid.greaterThanOrEqual=" + DEFAULT_CHANNELID);

        // Get all the productVariantPriceList where channelid is greater than or equal to UPDATED_CHANNELID
        defaultProductVariantPriceShouldNotBeFound("channelid.greaterThanOrEqual=" + UPDATED_CHANNELID);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByChannelidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where channelid is less than or equal to DEFAULT_CHANNELID
        defaultProductVariantPriceShouldBeFound("channelid.lessThanOrEqual=" + DEFAULT_CHANNELID);

        // Get all the productVariantPriceList where channelid is less than or equal to SMALLER_CHANNELID
        defaultProductVariantPriceShouldNotBeFound("channelid.lessThanOrEqual=" + SMALLER_CHANNELID);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByChannelidIsLessThanSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where channelid is less than DEFAULT_CHANNELID
        defaultProductVariantPriceShouldNotBeFound("channelid.lessThan=" + DEFAULT_CHANNELID);

        // Get all the productVariantPriceList where channelid is less than UPDATED_CHANNELID
        defaultProductVariantPriceShouldBeFound("channelid.lessThan=" + UPDATED_CHANNELID);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByChannelidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        // Get all the productVariantPriceList where channelid is greater than DEFAULT_CHANNELID
        defaultProductVariantPriceShouldNotBeFound("channelid.greaterThan=" + DEFAULT_CHANNELID);

        // Get all the productVariantPriceList where channelid is greater than SMALLER_CHANNELID
        defaultProductVariantPriceShouldBeFound("channelid.greaterThan=" + SMALLER_CHANNELID);
    }

    @Test
    @Transactional
    void getAllProductVariantPricesByVariantIsEqualToSomething() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);
        ProductVariant variant = ProductVariantResourceIT.createEntity(em);
        em.persist(variant);
        em.flush();
        productVariantPrice.setVariant(variant);
        productVariantPriceRepository.saveAndFlush(productVariantPrice);
        Long variantId = variant.getId();

        // Get all the productVariantPriceList where variant equals to variantId
        defaultProductVariantPriceShouldBeFound("variantId.equals=" + variantId);

        // Get all the productVariantPriceList where variant equals to (variantId + 1)
        defaultProductVariantPriceShouldNotBeFound("variantId.equals=" + (variantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductVariantPriceShouldBeFound(String filter) throws Exception {
        restProductVariantPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productVariantPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].channelid").value(hasItem(DEFAULT_CHANNELID)));

        // Check, that the count call also returns 1
        restProductVariantPriceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductVariantPriceShouldNotBeFound(String filter) throws Exception {
        restProductVariantPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductVariantPriceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductVariantPrice() throws Exception {
        // Get the productVariantPrice
        restProductVariantPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductVariantPrice() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        int databaseSizeBeforeUpdate = productVariantPriceRepository.findAll().size();

        // Update the productVariantPrice
        ProductVariantPrice updatedProductVariantPrice = productVariantPriceRepository.findById(productVariantPrice.getId()).get();
        // Disconnect from session so that the updates on updatedProductVariantPrice are not directly saved in db
        em.detach(updatedProductVariantPrice);
        updatedProductVariantPrice
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .price(UPDATED_PRICE)
            .channelid(UPDATED_CHANNELID);
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(updatedProductVariantPrice);

        restProductVariantPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productVariantPriceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariantPrice in the database
        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeUpdate);
        ProductVariantPrice testProductVariantPrice = productVariantPriceList.get(productVariantPriceList.size() - 1);
        assertThat(testProductVariantPrice.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductVariantPrice.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductVariantPrice.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProductVariantPrice.getChannelid()).isEqualTo(UPDATED_CHANNELID);
    }

    @Test
    @Transactional
    void putNonExistingProductVariantPrice() throws Exception {
        int databaseSizeBeforeUpdate = productVariantPriceRepository.findAll().size();
        productVariantPrice.setId(count.incrementAndGet());

        // Create the ProductVariantPrice
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(productVariantPrice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductVariantPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productVariantPriceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantPrice in the database
        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductVariantPrice() throws Exception {
        int databaseSizeBeforeUpdate = productVariantPriceRepository.findAll().size();
        productVariantPrice.setId(count.incrementAndGet());

        // Create the ProductVariantPrice
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(productVariantPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantPrice in the database
        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductVariantPrice() throws Exception {
        int databaseSizeBeforeUpdate = productVariantPriceRepository.findAll().size();
        productVariantPrice.setId(count.incrementAndGet());

        // Create the ProductVariantPrice
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(productVariantPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantPriceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVariantPrice in the database
        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductVariantPriceWithPatch() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        int databaseSizeBeforeUpdate = productVariantPriceRepository.findAll().size();

        // Update the productVariantPrice using partial update
        ProductVariantPrice partialUpdatedProductVariantPrice = new ProductVariantPrice();
        partialUpdatedProductVariantPrice.setId(productVariantPrice.getId());

        partialUpdatedProductVariantPrice.createdat(UPDATED_CREATEDAT).channelid(UPDATED_CHANNELID);

        restProductVariantPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVariantPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductVariantPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariantPrice in the database
        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeUpdate);
        ProductVariantPrice testProductVariantPrice = productVariantPriceList.get(productVariantPriceList.size() - 1);
        assertThat(testProductVariantPrice.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductVariantPrice.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductVariantPrice.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProductVariantPrice.getChannelid()).isEqualTo(UPDATED_CHANNELID);
    }

    @Test
    @Transactional
    void fullUpdateProductVariantPriceWithPatch() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        int databaseSizeBeforeUpdate = productVariantPriceRepository.findAll().size();

        // Update the productVariantPrice using partial update
        ProductVariantPrice partialUpdatedProductVariantPrice = new ProductVariantPrice();
        partialUpdatedProductVariantPrice.setId(productVariantPrice.getId());

        partialUpdatedProductVariantPrice
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .price(UPDATED_PRICE)
            .channelid(UPDATED_CHANNELID);

        restProductVariantPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductVariantPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductVariantPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProductVariantPrice in the database
        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeUpdate);
        ProductVariantPrice testProductVariantPrice = productVariantPriceList.get(productVariantPriceList.size() - 1);
        assertThat(testProductVariantPrice.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductVariantPrice.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductVariantPrice.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProductVariantPrice.getChannelid()).isEqualTo(UPDATED_CHANNELID);
    }

    @Test
    @Transactional
    void patchNonExistingProductVariantPrice() throws Exception {
        int databaseSizeBeforeUpdate = productVariantPriceRepository.findAll().size();
        productVariantPrice.setId(count.incrementAndGet());

        // Create the ProductVariantPrice
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(productVariantPrice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductVariantPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productVariantPriceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantPrice in the database
        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductVariantPrice() throws Exception {
        int databaseSizeBeforeUpdate = productVariantPriceRepository.findAll().size();
        productVariantPrice.setId(count.incrementAndGet());

        // Create the ProductVariantPrice
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(productVariantPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductVariantPrice in the database
        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductVariantPrice() throws Exception {
        int databaseSizeBeforeUpdate = productVariantPriceRepository.findAll().size();
        productVariantPrice.setId(count.incrementAndGet());

        // Create the ProductVariantPrice
        ProductVariantPriceDTO productVariantPriceDTO = productVariantPriceMapper.toDto(productVariantPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductVariantPriceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productVariantPriceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductVariantPrice in the database
        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductVariantPrice() throws Exception {
        // Initialize the database
        productVariantPriceRepository.saveAndFlush(productVariantPrice);

        int databaseSizeBeforeDelete = productVariantPriceRepository.findAll().size();

        // Delete the productVariantPrice
        restProductVariantPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, productVariantPrice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductVariantPrice> productVariantPriceList = productVariantPriceRepository.findAll();
        assertThat(productVariantPriceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
