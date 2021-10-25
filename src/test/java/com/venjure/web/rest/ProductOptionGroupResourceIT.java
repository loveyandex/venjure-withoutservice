package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Pogt;
import com.venjure.domain.Product;
import com.venjure.domain.ProductOption;
import com.venjure.domain.ProductOptionGroup;
import com.venjure.repository.ProductOptionGroupRepository;
import com.venjure.service.criteria.ProductOptionGroupCriteria;
import com.venjure.service.dto.ProductOptionGroupDTO;
import com.venjure.service.mapper.ProductOptionGroupMapper;
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
 * Integration tests for the {@link ProductOptionGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductOptionGroupResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-option-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductOptionGroupRepository productOptionGroupRepository;

    @Autowired
    private ProductOptionGroupMapper productOptionGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductOptionGroupMockMvc;

    private ProductOptionGroup productOptionGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOptionGroup createEntity(EntityManager em) {
        ProductOptionGroup productOptionGroup = new ProductOptionGroup()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .deletedat(DEFAULT_DELETEDAT)
            .code(DEFAULT_CODE);
        return productOptionGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOptionGroup createUpdatedEntity(EntityManager em) {
        ProductOptionGroup productOptionGroup = new ProductOptionGroup()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .code(UPDATED_CODE);
        return productOptionGroup;
    }

    @BeforeEach
    public void initTest() {
        productOptionGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createProductOptionGroup() throws Exception {
        int databaseSizeBeforeCreate = productOptionGroupRepository.findAll().size();
        // Create the ProductOptionGroup
        ProductOptionGroupDTO productOptionGroupDTO = productOptionGroupMapper.toDto(productOptionGroup);
        restProductOptionGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionGroupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductOptionGroup in the database
        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeCreate + 1);
        ProductOptionGroup testProductOptionGroup = productOptionGroupList.get(productOptionGroupList.size() - 1);
        assertThat(testProductOptionGroup.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testProductOptionGroup.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductOptionGroup.getDeletedat()).isEqualTo(DEFAULT_DELETEDAT);
        assertThat(testProductOptionGroup.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createProductOptionGroupWithExistingId() throws Exception {
        // Create the ProductOptionGroup with an existing ID
        productOptionGroup.setId(1L);
        ProductOptionGroupDTO productOptionGroupDTO = productOptionGroupMapper.toDto(productOptionGroup);

        int databaseSizeBeforeCreate = productOptionGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOptionGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOptionGroup in the database
        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOptionGroupRepository.findAll().size();
        // set the field null
        productOptionGroup.setCreatedat(null);

        // Create the ProductOptionGroup, which fails.
        ProductOptionGroupDTO productOptionGroupDTO = productOptionGroupMapper.toDto(productOptionGroup);

        restProductOptionGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOptionGroupRepository.findAll().size();
        // set the field null
        productOptionGroup.setUpdatedat(null);

        // Create the ProductOptionGroup, which fails.
        ProductOptionGroupDTO productOptionGroupDTO = productOptionGroupMapper.toDto(productOptionGroup);

        restProductOptionGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOptionGroupRepository.findAll().size();
        // set the field null
        productOptionGroup.setCode(null);

        // Create the ProductOptionGroup, which fails.
        ProductOptionGroupDTO productOptionGroupDTO = productOptionGroupMapper.toDto(productOptionGroup);

        restProductOptionGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductOptionGroups() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList
        restProductOptionGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOptionGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getProductOptionGroup() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get the productOptionGroup
        restProductOptionGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, productOptionGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productOptionGroup.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.deletedat").value(DEFAULT_DELETEDAT.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getProductOptionGroupsByIdFiltering() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        Long id = productOptionGroup.getId();

        defaultProductOptionGroupShouldBeFound("id.equals=" + id);
        defaultProductOptionGroupShouldNotBeFound("id.notEquals=" + id);

        defaultProductOptionGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductOptionGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultProductOptionGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductOptionGroupShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where createdat equals to DEFAULT_CREATEDAT
        defaultProductOptionGroupShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the productOptionGroupList where createdat equals to UPDATED_CREATEDAT
        defaultProductOptionGroupShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where createdat not equals to DEFAULT_CREATEDAT
        defaultProductOptionGroupShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the productOptionGroupList where createdat not equals to UPDATED_CREATEDAT
        defaultProductOptionGroupShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultProductOptionGroupShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the productOptionGroupList where createdat equals to UPDATED_CREATEDAT
        defaultProductOptionGroupShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where createdat is not null
        defaultProductOptionGroupShouldBeFound("createdat.specified=true");

        // Get all the productOptionGroupList where createdat is null
        defaultProductOptionGroupShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where updatedat equals to DEFAULT_UPDATEDAT
        defaultProductOptionGroupShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the productOptionGroupList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductOptionGroupShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultProductOptionGroupShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the productOptionGroupList where updatedat not equals to UPDATED_UPDATEDAT
        defaultProductOptionGroupShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultProductOptionGroupShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the productOptionGroupList where updatedat equals to UPDATED_UPDATEDAT
        defaultProductOptionGroupShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where updatedat is not null
        defaultProductOptionGroupShouldBeFound("updatedat.specified=true");

        // Get all the productOptionGroupList where updatedat is null
        defaultProductOptionGroupShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByDeletedatIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where deletedat equals to DEFAULT_DELETEDAT
        defaultProductOptionGroupShouldBeFound("deletedat.equals=" + DEFAULT_DELETEDAT);

        // Get all the productOptionGroupList where deletedat equals to UPDATED_DELETEDAT
        defaultProductOptionGroupShouldNotBeFound("deletedat.equals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByDeletedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where deletedat not equals to DEFAULT_DELETEDAT
        defaultProductOptionGroupShouldNotBeFound("deletedat.notEquals=" + DEFAULT_DELETEDAT);

        // Get all the productOptionGroupList where deletedat not equals to UPDATED_DELETEDAT
        defaultProductOptionGroupShouldBeFound("deletedat.notEquals=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByDeletedatIsInShouldWork() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where deletedat in DEFAULT_DELETEDAT or UPDATED_DELETEDAT
        defaultProductOptionGroupShouldBeFound("deletedat.in=" + DEFAULT_DELETEDAT + "," + UPDATED_DELETEDAT);

        // Get all the productOptionGroupList where deletedat equals to UPDATED_DELETEDAT
        defaultProductOptionGroupShouldNotBeFound("deletedat.in=" + UPDATED_DELETEDAT);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByDeletedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where deletedat is not null
        defaultProductOptionGroupShouldBeFound("deletedat.specified=true");

        // Get all the productOptionGroupList where deletedat is null
        defaultProductOptionGroupShouldNotBeFound("deletedat.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where code equals to DEFAULT_CODE
        defaultProductOptionGroupShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the productOptionGroupList where code equals to UPDATED_CODE
        defaultProductOptionGroupShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where code not equals to DEFAULT_CODE
        defaultProductOptionGroupShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the productOptionGroupList where code not equals to UPDATED_CODE
        defaultProductOptionGroupShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where code in DEFAULT_CODE or UPDATED_CODE
        defaultProductOptionGroupShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the productOptionGroupList where code equals to UPDATED_CODE
        defaultProductOptionGroupShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where code is not null
        defaultProductOptionGroupShouldBeFound("code.specified=true");

        // Get all the productOptionGroupList where code is null
        defaultProductOptionGroupShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByCodeContainsSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where code contains DEFAULT_CODE
        defaultProductOptionGroupShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the productOptionGroupList where code contains UPDATED_CODE
        defaultProductOptionGroupShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        // Get all the productOptionGroupList where code does not contain DEFAULT_CODE
        defaultProductOptionGroupShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the productOptionGroupList where code does not contain UPDATED_CODE
        defaultProductOptionGroupShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);
        Product product = ProductResourceIT.createEntity(em);
        em.persist(product);
        em.flush();
        productOptionGroup.setProduct(product);
        productOptionGroupRepository.saveAndFlush(productOptionGroup);
        Long productId = product.getId();

        // Get all the productOptionGroupList where product equals to productId
        defaultProductOptionGroupShouldBeFound("productId.equals=" + productId);

        // Get all the productOptionGroupList where product equals to (productId + 1)
        defaultProductOptionGroupShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByProductOptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);
        ProductOption productOption = ProductOptionResourceIT.createEntity(em);
        em.persist(productOption);
        em.flush();
        productOptionGroup.addProductOption(productOption);
        productOptionGroupRepository.saveAndFlush(productOptionGroup);
        Long productOptionId = productOption.getId();

        // Get all the productOptionGroupList where productOption equals to productOptionId
        defaultProductOptionGroupShouldBeFound("productOptionId.equals=" + productOptionId);

        // Get all the productOptionGroupList where productOption equals to (productOptionId + 1)
        defaultProductOptionGroupShouldNotBeFound("productOptionId.equals=" + (productOptionId + 1));
    }

    @Test
    @Transactional
    void getAllProductOptionGroupsByProductOptionGroupTranslationIsEqualToSomething() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);
        Pogt productOptionGroupTranslation = PogtResourceIT.createEntity(em);
        em.persist(productOptionGroupTranslation);
        em.flush();
        productOptionGroup.addProductOptionGroupTranslation(productOptionGroupTranslation);
        productOptionGroupRepository.saveAndFlush(productOptionGroup);
        Long productOptionGroupTranslationId = productOptionGroupTranslation.getId();

        // Get all the productOptionGroupList where productOptionGroupTranslation equals to productOptionGroupTranslationId
        defaultProductOptionGroupShouldBeFound("productOptionGroupTranslationId.equals=" + productOptionGroupTranslationId);

        // Get all the productOptionGroupList where productOptionGroupTranslation equals to (productOptionGroupTranslationId + 1)
        defaultProductOptionGroupShouldNotBeFound("productOptionGroupTranslationId.equals=" + (productOptionGroupTranslationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductOptionGroupShouldBeFound(String filter) throws Exception {
        restProductOptionGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOptionGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].deletedat").value(hasItem(DEFAULT_DELETEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restProductOptionGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductOptionGroupShouldNotBeFound(String filter) throws Exception {
        restProductOptionGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductOptionGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductOptionGroup() throws Exception {
        // Get the productOptionGroup
        restProductOptionGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductOptionGroup() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        int databaseSizeBeforeUpdate = productOptionGroupRepository.findAll().size();

        // Update the productOptionGroup
        ProductOptionGroup updatedProductOptionGroup = productOptionGroupRepository.findById(productOptionGroup.getId()).get();
        // Disconnect from session so that the updates on updatedProductOptionGroup are not directly saved in db
        em.detach(updatedProductOptionGroup);
        updatedProductOptionGroup.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).deletedat(UPDATED_DELETEDAT).code(UPDATED_CODE);
        ProductOptionGroupDTO productOptionGroupDTO = productOptionGroupMapper.toDto(updatedProductOptionGroup);

        restProductOptionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOptionGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductOptionGroup in the database
        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeUpdate);
        ProductOptionGroup testProductOptionGroup = productOptionGroupList.get(productOptionGroupList.size() - 1);
        assertThat(testProductOptionGroup.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductOptionGroup.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductOptionGroup.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testProductOptionGroup.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingProductOptionGroup() throws Exception {
        int databaseSizeBeforeUpdate = productOptionGroupRepository.findAll().size();
        productOptionGroup.setId(count.incrementAndGet());

        // Create the ProductOptionGroup
        ProductOptionGroupDTO productOptionGroupDTO = productOptionGroupMapper.toDto(productOptionGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOptionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOptionGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOptionGroup in the database
        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductOptionGroup() throws Exception {
        int databaseSizeBeforeUpdate = productOptionGroupRepository.findAll().size();
        productOptionGroup.setId(count.incrementAndGet());

        // Create the ProductOptionGroup
        ProductOptionGroupDTO productOptionGroupDTO = productOptionGroupMapper.toDto(productOptionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOptionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOptionGroup in the database
        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductOptionGroup() throws Exception {
        int databaseSizeBeforeUpdate = productOptionGroupRepository.findAll().size();
        productOptionGroup.setId(count.incrementAndGet());

        // Create the ProductOptionGroup
        ProductOptionGroupDTO productOptionGroupDTO = productOptionGroupMapper.toDto(productOptionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOptionGroupMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOptionGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOptionGroup in the database
        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductOptionGroupWithPatch() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        int databaseSizeBeforeUpdate = productOptionGroupRepository.findAll().size();

        // Update the productOptionGroup using partial update
        ProductOptionGroup partialUpdatedProductOptionGroup = new ProductOptionGroup();
        partialUpdatedProductOptionGroup.setId(productOptionGroup.getId());

        partialUpdatedProductOptionGroup.createdat(UPDATED_CREATEDAT).deletedat(UPDATED_DELETEDAT);

        restProductOptionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOptionGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductOptionGroup))
            )
            .andExpect(status().isOk());

        // Validate the ProductOptionGroup in the database
        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeUpdate);
        ProductOptionGroup testProductOptionGroup = productOptionGroupList.get(productOptionGroupList.size() - 1);
        assertThat(testProductOptionGroup.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductOptionGroup.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testProductOptionGroup.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testProductOptionGroup.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateProductOptionGroupWithPatch() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        int databaseSizeBeforeUpdate = productOptionGroupRepository.findAll().size();

        // Update the productOptionGroup using partial update
        ProductOptionGroup partialUpdatedProductOptionGroup = new ProductOptionGroup();
        partialUpdatedProductOptionGroup.setId(productOptionGroup.getId());

        partialUpdatedProductOptionGroup
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .deletedat(UPDATED_DELETEDAT)
            .code(UPDATED_CODE);

        restProductOptionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOptionGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductOptionGroup))
            )
            .andExpect(status().isOk());

        // Validate the ProductOptionGroup in the database
        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeUpdate);
        ProductOptionGroup testProductOptionGroup = productOptionGroupList.get(productOptionGroupList.size() - 1);
        assertThat(testProductOptionGroup.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testProductOptionGroup.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testProductOptionGroup.getDeletedat()).isEqualTo(UPDATED_DELETEDAT);
        assertThat(testProductOptionGroup.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingProductOptionGroup() throws Exception {
        int databaseSizeBeforeUpdate = productOptionGroupRepository.findAll().size();
        productOptionGroup.setId(count.incrementAndGet());

        // Create the ProductOptionGroup
        ProductOptionGroupDTO productOptionGroupDTO = productOptionGroupMapper.toDto(productOptionGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOptionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productOptionGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOptionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOptionGroup in the database
        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductOptionGroup() throws Exception {
        int databaseSizeBeforeUpdate = productOptionGroupRepository.findAll().size();
        productOptionGroup.setId(count.incrementAndGet());

        // Create the ProductOptionGroup
        ProductOptionGroupDTO productOptionGroupDTO = productOptionGroupMapper.toDto(productOptionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOptionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOptionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOptionGroup in the database
        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductOptionGroup() throws Exception {
        int databaseSizeBeforeUpdate = productOptionGroupRepository.findAll().size();
        productOptionGroup.setId(count.incrementAndGet());

        // Create the ProductOptionGroup
        ProductOptionGroupDTO productOptionGroupDTO = productOptionGroupMapper.toDto(productOptionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOptionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOptionGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOptionGroup in the database
        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductOptionGroup() throws Exception {
        // Initialize the database
        productOptionGroupRepository.saveAndFlush(productOptionGroup);

        int databaseSizeBeforeDelete = productOptionGroupRepository.findAll().size();

        // Delete the productOptionGroup
        restProductOptionGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, productOptionGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductOptionGroup> productOptionGroupList = productOptionGroupRepository.findAll();
        assertThat(productOptionGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
