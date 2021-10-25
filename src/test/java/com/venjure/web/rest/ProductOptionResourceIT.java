package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.ProductOption;
import com.venjure.domain.ProductOptionGroup;
import com.venjure.domain.ProductOptionTranslation;
import com.venjure.domain.ProductVariant;
import com.venjure.repository.ProductOptionRepository;
import com.venjure.service.criteria.ProductOptionCriteria;
import com.venjure.service.dto.ProductOptionDTO;
import com.venjure.service.mapper.ProductOptionMapper;
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
 * Integration tests for the {@link ProductOptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductOptionResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductOptionMapper productOptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductOptionMockMvc;

    private ProductOption productOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOption createEntity(EntityManager em) {
        ProductOption productOption = new ProductOption()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .deletedat(DEFAULT_DELETEDAT)
            .code(DEFAULT_CODE);
        // Add required entity
        ProductOptionGroup productOptionGroup;
        if (TestUtil.findAll(em, ProductOptionGroup.class).isEmpty()) {
            productOptionGroup = ProductOptionGroupResourceIT.createEntity(em);
            em.persist(productOptionGroup);
            em.flush();
        } else {
            productOptionGroup = TestUtil.findAll(em, ProductOptionGroup.class).get(0);
        }
        productOption.setGroup(productOptionGroup);
        return productOption;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOption createUpdatedEntity(EntityManager em) {
        ProductOption productOption = new ProductOption()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .code(UPDATED_CODE);
        // Add required entity
        ProductOptionGroup productOptionGroup;
        if (TestUtil.findAll(em, ProductOptionGroup.class).isEmpty()) {
            productOptionGroup = ProductOptionGroupResourceIT.createUpdatedEntity(em);
            em.persist(productOptionGroup);
            em.flush();
        } else {
            productOptionGroup = TestUtil.findAll(em, ProductOptionGroup.class).get(0);
        }
        productOption.setGroup(productOptionGroup);
        return productOption;
    }

    @BeforeEach
    public void initTest() {
        productOption = createEntity(em);
    }

    @Test
    @Transactional
    void createProductOption() throws Exception {
        int databaseSizeBeforeCreate = productOptionRepository.findAll().size();
        // Create the ProductOption
        ProductOptionDTO productOptionDTO = productOptionMapper.toDto(productOption);
        restProductOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductOption in the database
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeCreate + 1);
        ProductOption testProductOption = productOptionList.get(productOptionList.size() - 1);
        assertThat(testProductOption.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testProductOption.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductOption.getDeletedat()).isEqualTo(DEFAULT_DELETEDAT);
        assertThat(testProductOption.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createProductOptionWithExistingId() throws Exception {
        // Create the ProductOption with an existing ID
        productOption.setId(1L);
        ProductOptionDTO productOptionDTO = productOptionMapper.toDto(productOption);

        int databaseSizeBeforeCreate = productOptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOption in the database
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOptionRepository.findAll().size();
        // set the field null
        productOption.setCreatedat(null);

        // Create the ProductOption, which fails.
        ProductOptionDTO productOptionDTO = productOptionMapper.toDto(productOption);

        restProductOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOptionRepository.findAll().size();
        // set the field null
        productOption.setUpdatedat(null);

        // Create the ProductOption, which fails.
        ProductOptionDTO productOptionDTO = productOptionMapper.toDto(productOption);

        restProductOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOptionRepository.findAll().size();
        // set the field null
        productOption.setCode(null);

        // Create the ProductOption, which fails.
        ProductOptionDTO productOptionDTO = productOptionMapper.toDto(productOption);

        restProductOptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductOptions() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList
        restProductOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getProductOption() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get the productOption
        restProductOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, productOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productOption.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.deletedat").value(DEFAULT_DELETEDAT.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getProductOptionsByIdFiltering() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        Long id = productOption.getId();

        defaultProductOptionShouldBeFound("id.equals=" + id);
        defaultProductOptionShouldNotBeFound("id.notEquals=" + id);

        defaultProductOptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductOptionShouldNotBeFound("id.greaterThan=" + id);

        defaultProductOptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductOptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductOptionsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where createdat equals to DEFAULT_CREATEDAT
        defaultProductOptionShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the productOptionList where createdat equals to UPDATED_CREATEDAT
        defaultProductOptionShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where createdat not equals to DEFAULT_CREATEDAT
        defaultProductOptionShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the productOptionList where createdat not equals to UPDATED_CREATEDAT
        defaultProductOptionShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultProductOptionShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the productOptionList where createdat equals to UPDATED_CREATEDAT
        defaultProductOptionShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where createdat is not null
        defaultProductOptionShouldBeFound("createdat.specified=true");

        // Get all the productOptionList where createdat is null
        defaultProductOptionShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOptionsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where updatedat equals to DEFAULT_UPDATEDAT
        defaultProductOptionShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the productOptionList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductOptionShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultProductOptionShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the productOptionList where updatedat not equals to UPDATED_UPDATEDAT
        defaultProductOptionShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultProductOptionShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the productOptionList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductOptionShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where updatedat is not null
        defaultProductOptionShouldBeFound("updatedat.specified=true");

        // Get all the productOptionList where updatedat is null
        defaultProductOptionShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOptionsByDeletedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where deletedat equals to DEFAULT_DELETEDAT
        defaultProductOptionShouldBeFound("deletedat.equals=" + DEFAULT_DELETEDAT);

        // Get all the productOptionList where deletedat equals to UPDATED_DELETEDAT
        defaultProductOptionShouldNotBeFound("deletedat.equals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionsByDeletedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where deletedat not equals to DEFAULT_DELETEDAT
        defaultProductOptionShouldNotBeFound("deletedat.notEquals=" + DEFAULT_DELETEDAT);

        // Get all the productOptionList where deletedat not equals to UPDATED_DELETEDAT
        defaultProductOptionShouldBeFound("deletedat.notEquals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionsByDeletedatIsInShouldWork() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where deletedat in DEFAULT_DELETEDAT or UPDATED_DELETEDAT
        defaultProductOptionShouldBeFound("deletedat.in=" + DEFAULT_DELETEDAT + "," + UPDATED_DELETEDAT);

        // Get all the productOptionList where deletedat equals to UPDATED_DELETEDAT
        defaultProductOptionShouldNotBeFound("deletedat.in=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionsByDeletedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where deletedat is not null
        defaultProductOptionShouldBeFound("deletedat.specified=true");

        // Get all the productOptionList where deletedat is null
        defaultProductOptionShouldNotBeFound("deletedat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOptionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where code equals to DEFAULT_CODE
        defaultProductOptionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the productOptionList where code equals to UPDATED_CODE
        defaultProductOptionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOptionsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where code not equals to DEFAULT_CODE
        defaultProductOptionShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the productOptionList where code not equals to UPDATED_CODE
        defaultProductOptionShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOptionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultProductOptionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the productOptionList where code equals to UPDATED_CODE
        defaultProductOptionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOptionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where code is not null
        defaultProductOptionShouldBeFound("code.specified=true");

        // Get all the productOptionList where code is null
        defaultProductOptionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOptionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where code contains DEFAULT_CODE
        defaultProductOptionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the productOptionList where code contains UPDATED_CODE
        defaultProductOptionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOptionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        // Get all the productOptionList where code does not contain DEFAULT_CODE
        defaultProductOptionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the productOptionList where code does not contain UPDATED_CODE
        defaultProductOptionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOptionsByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);
        ProductOptionGroup group = ProductOptionGroupResourceIT.createEntity(em);
        em.persist(group);
        em.flush();
        productOption.setGroup(group);
        productOptionRepository.saveAndFlush(productOption);
        Long groupId = group.getId();

        // Get all the productOptionList where group equals to groupId
        defaultProductOptionShouldBeFound("groupId.equals=" + groupId);

        // Get all the productOptionList where group equals to (groupId + 1)
        defaultProductOptionShouldNotBeFound("groupId.equals=" + (groupId + 1));
    }

    @Test
    @Transactional
    void getAllProductOptionsByProductOptionTranslationIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);
        ProductOptionTranslation productOptionTranslation = ProductOptionTranslationResourceIT.createEntity(em);
        em.persist(productOptionTranslation);
        em.flush();
        productOption.addProductOptionTranslation(productOptionTranslation);
        productOptionRepository.saveAndFlush(productOption);
        Long productOptionTranslationId = productOptionTranslation.getId();

        // Get all the productOptionList where productOptionTranslation equals to productOptionTranslationId
        defaultProductOptionShouldBeFound("productOptionTranslationId.equals=" + productOptionTranslationId);

        // Get all the productOptionList where productOptionTranslation equals to (productOptionTranslationId + 1)
        defaultProductOptionShouldNotBeFound("productOptionTranslationId.equals=" + (productOptionTranslationId + 1));
    }

    @Test
    @Transactional
    void getAllProductOptionsByProductVariantIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);
        ProductVariant productVariant = ProductVariantResourceIT.createEntity(em);
        em.persist(productVariant);
        em.flush();
        productOption.addProductVariant(productVariant);
        productOptionRepository.saveAndFlush(productOption);
        Long productVariantId = productVariant.getId();

        // Get all the productOptionList where productVariant equals to productVariantId
        defaultProductOptionShouldBeFound("productVariantId.equals=" + productVariantId);

        // Get all the productOptionList where productVariant equals to (productVariantId + 1)
        defaultProductOptionShouldNotBeFound("productVariantId.equals=" + (productVariantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductOptionShouldBeFound(String filter) throws Exception {
        restProductOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restProductOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductOptionShouldNotBeFound(String filter) throws Exception {
        restProductOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductOption() throws Exception {
        // Get the productOption
        restProductOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductOption() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        int databaseSizeBeforeUpdate = productOptionRepository.findAll().size();

        // Update the productOption
        ProductOption updatedProductOption = productOptionRepository.findById(productOption.getId()).get();
        // Disconnect from session so that the updates on updatedProductOption are not directly saved in db
        em.detach(updatedProductOption);
        updatedProductOption.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).deletedat(UPDATED_DELETEDAT).code(UPDATED_CODE);
        ProductOptionDTO productOptionDTO = productOptionMapper.toDto(updatedProductOption);

        restProductOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductOption in the database
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeUpdate);
        ProductOption testProductOption = productOptionList.get(productOptionList.size() - 1);
        assertThat(testProductOption.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductOption.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductOption.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testProductOption.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingProductOption() throws Exception {
        int databaseSizeBeforeUpdate = productOptionRepository.findAll().size();
        productOption.setId(count.incrementAndGet());

        // Create the ProductOption
        ProductOptionDTO productOptionDTO = productOptionMapper.toDto(productOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOption in the database
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductOption() throws Exception {
        int databaseSizeBeforeUpdate = productOptionRepository.findAll().size();
        productOption.setId(count.incrementAndGet());

        // Create the ProductOption
        ProductOptionDTO productOptionDTO = productOptionMapper.toDto(productOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOption in the database
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductOption() throws Exception {
        int databaseSizeBeforeUpdate = productOptionRepository.findAll().size();
        productOption.setId(count.incrementAndGet());

        // Create the ProductOption
        ProductOptionDTO productOptionDTO = productOptionMapper.toDto(productOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOption in the database
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductOptionWithPatch() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        int databaseSizeBeforeUpdate = productOptionRepository.findAll().size();

        // Update the productOption using partial update
        ProductOption partialUpdatedProductOption = new ProductOption();
        partialUpdatedProductOption.setId(productOption.getId());

        partialUpdatedProductOption.updatedat(UPDATED_UPDATEDAT).deletedat(UPDATED_DELETEDAT);

        restProductOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductOption))
            )
            .andExpect(status().isOk());

        // Validate the ProductOption in the database
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeUpdate);
        ProductOption testProductOption = productOptionList.get(productOptionList.size() - 1);
        assertThat(testProductOption.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testProductOption.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductOption.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testProductOption.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateProductOptionWithPatch() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        int databaseSizeBeforeUpdate = productOptionRepository.findAll().size();

        // Update the productOption using partial update
        ProductOption partialUpdatedProductOption = new ProductOption();
        partialUpdatedProductOption.setId(productOption.getId());

        partialUpdatedProductOption
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .code(UPDATED_CODE);

        restProductOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductOption))
            )
            .andExpect(status().isOk());

        // Validate the ProductOption in the database
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeUpdate);
        ProductOption testProductOption = productOptionList.get(productOptionList.size() - 1);
        assertThat(testProductOption.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductOption.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductOption.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testProductOption.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingProductOption() throws Exception {
        int databaseSizeBeforeUpdate = productOptionRepository.findAll().size();
        productOption.setId(count.incrementAndGet());

        // Create the ProductOption
        ProductOptionDTO productOptionDTO = productOptionMapper.toDto(productOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productOptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOption in the database
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductOption() throws Exception {
        int databaseSizeBeforeUpdate = productOptionRepository.findAll().size();
        productOption.setId(count.incrementAndGet());

        // Create the ProductOption
        ProductOptionDTO productOptionDTO = productOptionMapper.toDto(productOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOption in the database
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductOption() throws Exception {
        int databaseSizeBeforeUpdate = productOptionRepository.findAll().size();
        productOption.setId(count.incrementAndGet());

        // Create the ProductOption
        ProductOptionDTO productOptionDTO = productOptionMapper.toDto(productOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOption in the database
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductOption() throws Exception {
        // Initialize the database
        productOptionRepository.saveAndFlush(productOption);

        int databaseSizeBeforeDelete = productOptionRepository.findAll().size();

        // Delete the productOption
        restProductOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, productOption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductOption> productOptionList = productOptionRepository.findAll();
        assertThat(productOptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
