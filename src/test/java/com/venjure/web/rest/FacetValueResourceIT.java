package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Channel;
import com.venjure.domain.Facet;
import com.venjure.domain.FacetValue;
import com.venjure.domain.FacetValueTranslation;
import com.venjure.domain.Product;
import com.venjure.domain.ProductVariant;
import com.venjure.repository.FacetValueRepository;
import com.venjure.service.FacetValueService;
import com.venjure.service.criteria.FacetValueCriteria;
import com.venjure.service.dto.FacetValueDTO;
import com.venjure.service.mapper.FacetValueMapper;
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
 * Integration tests for the {@link FacetValueResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FacetValueResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facet-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacetValueRepository facetValueRepository;

    @Mock
    private FacetValueRepository facetValueRepositoryMock;

    @Autowired
    private FacetValueMapper facetValueMapper;

    @Mock
    private FacetValueService facetValueServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacetValueMockMvc;

    private FacetValue facetValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacetValue createEntity(EntityManager em) {
        FacetValue facetValue = new FacetValue().createdat(DEFAULT_CREATEDAT).updatedat(DEFAULT_UPDATEDAT).code(DEFAULT_CODE);
        return facetValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FacetValue createUpdatedEntity(EntityManager em) {
        FacetValue facetValue = new FacetValue().createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).code(UPDATED_CODE);
        return facetValue;
    }

    @BeforeEach
    public void initTest() {
        facetValue = createEntity(em);
    }

    @Test
    @Transactional
    void createFacetValue() throws Exception {
        int databaseSizeBeforeCreate = facetValueRepository.findAll().size();
        // Create the FacetValue
        FacetValueDTO facetValueDTO = facetValueMapper.toDto(facetValue);
        restFacetValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetValueDTO)))
            .andExpect(status().isCreated());

        // Validate the FacetValue in the database
        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeCreate + 1);
        FacetValue testFacetValue = facetValueList.get(facetValueList.size() - 1);
        assertThat(testFacetValue.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testFacetValue.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testFacetValue.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createFacetValueWithExistingId() throws Exception {
        // Create the FacetValue with an existing ID
        facetValue.setId(1L);
        FacetValueDTO facetValueDTO = facetValueMapper.toDto(facetValue);

        int databaseSizeBeforeCreate = facetValueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacetValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FacetValue in the database
        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetValueRepository.findAll().size();
        // set the field null
        facetValue.setCreatedat(null);

        // Create the FacetValue, which fails.
        FacetValueDTO facetValueDTO = facetValueMapper.toDto(facetValue);

        restFacetValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetValueDTO)))
            .andExpect(status().isBadRequest());

        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetValueRepository.findAll().size();
        // set the field null
        facetValue.setUpdatedat(null);

        // Create the FacetValue, which fails.
        FacetValueDTO facetValueDTO = facetValueMapper.toDto(facetValue);

        restFacetValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetValueDTO)))
            .andExpect(status().isBadRequest());

        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetValueRepository.findAll().size();
        // set the field null
        facetValue.setCode(null);

        // Create the FacetValue, which fails.
        FacetValueDTO facetValueDTO = facetValueMapper.toDto(facetValue);

        restFacetValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetValueDTO)))
            .andExpect(status().isBadRequest());

        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacetValues() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList
        restFacetValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facetValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacetValuesWithEagerRelationshipsIsEnabled() throws Exception {
        when(facetValueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacetValueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facetValueServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacetValuesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facetValueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacetValueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facetValueServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFacetValue() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get the facetValue
        restFacetValueMockMvc
            .perform(get(ENTITY_API_URL_ID, facetValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facetValue.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getFacetValuesByIdFiltering() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        Long id = facetValue.getId();

        defaultFacetValueShouldBeFound("id.equals=" + id);
        defaultFacetValueShouldNotBeFound("id.notEquals=" + id);

        defaultFacetValueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFacetValueShouldNotBeFound("id.greaterThan=" + id);

        defaultFacetValueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFacetValueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacetValuesByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where createdat equals to DEFAULT_CREATEDAT
        defaultFacetValueShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the facetValueList where createdat equals to UPDATED_CREATEDAT
        defaultFacetValueShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetValuesByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where createdat not equals to DEFAULT_CREATEDAT
        defaultFacetValueShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the facetValueList where createdat not equals to UPDATED_CREATEDAT
        defaultFacetValueShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetValuesByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultFacetValueShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the facetValueList where createdat equals to UPDATED_CREATEDAT
        defaultFacetValueShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetValuesByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where createdat is not null
        defaultFacetValueShouldBeFound("createdat.specified=true");

        // Get all the facetValueList where createdat is null
        defaultFacetValueShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetValuesByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where updatedat equals to DEFAULT_UPDATEDAT
        defaultFacetValueShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the facetValueList where updatedat equals to UPDATED_UPDATEDAT
        defaultFacetValueShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetValuesByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultFacetValueShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the facetValueList where updatedat not equals to UPDATED_UPDATEDAT
        defaultFacetValueShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetValuesByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultFacetValueShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the facetValueList where updatedat equals to UPDATED_UPDATEDAT
        defaultFacetValueShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetValuesByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where updatedat is not null
        defaultFacetValueShouldBeFound("updatedat.specified=true");

        // Get all the facetValueList where updatedat is null
        defaultFacetValueShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetValuesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where code equals to DEFAULT_CODE
        defaultFacetValueShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the facetValueList where code equals to UPDATED_CODE
        defaultFacetValueShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacetValuesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where code not equals to DEFAULT_CODE
        defaultFacetValueShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the facetValueList where code not equals to UPDATED_CODE
        defaultFacetValueShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacetValuesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where code in DEFAULT_CODE or UPDATED_CODE
        defaultFacetValueShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the facetValueList where code equals to UPDATED_CODE
        defaultFacetValueShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacetValuesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where code is not null
        defaultFacetValueShouldBeFound("code.specified=true");

        // Get all the facetValueList where code is null
        defaultFacetValueShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetValuesByCodeContainsSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where code contains DEFAULT_CODE
        defaultFacetValueShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the facetValueList where code contains UPDATED_CODE
        defaultFacetValueShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacetValuesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        // Get all the facetValueList where code does not contain DEFAULT_CODE
        defaultFacetValueShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the facetValueList where code does not contain UPDATED_CODE
        defaultFacetValueShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacetValuesByFacetIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);
        Facet facet = FacetResourceIT.createEntity(em);
        em.persist(facet);
        em.flush();
        facetValue.setFacet(facet);
        facetValueRepository.saveAndFlush(facetValue);
        Long facetId = facet.getId();

        // Get all the facetValueList where facet equals to facetId
        defaultFacetValueShouldBeFound("facetId.equals=" + facetId);

        // Get all the facetValueList where facet equals to (facetId + 1)
        defaultFacetValueShouldNotBeFound("facetId.equals=" + (facetId + 1));
    }

    @Test
    @Transactional
    void getAllFacetValuesByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);
        Channel channel = ChannelResourceIT.createEntity(em);
        em.persist(channel);
        em.flush();
        facetValue.addChannel(channel);
        facetValueRepository.saveAndFlush(facetValue);
        Long channelId = channel.getId();

        // Get all the facetValueList where channel equals to channelId
        defaultFacetValueShouldBeFound("channelId.equals=" + channelId);

        // Get all the facetValueList where channel equals to (channelId + 1)
        defaultFacetValueShouldNotBeFound("channelId.equals=" + (channelId + 1));
    }

    @Test
    @Transactional
    void getAllFacetValuesByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);
        Product product = ProductResourceIT.createEntity(em);
        em.persist(product);
        em.flush();
        facetValue.addProduct(product);
        facetValueRepository.saveAndFlush(facetValue);
        Long productId = product.getId();

        // Get all the facetValueList where product equals to productId
        defaultFacetValueShouldBeFound("productId.equals=" + productId);

        // Get all the facetValueList where product equals to (productId + 1)
        defaultFacetValueShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllFacetValuesByFacetValueTranslationIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);
        FacetValueTranslation facetValueTranslation = FacetValueTranslationResourceIT.createEntity(em);
        em.persist(facetValueTranslation);
        em.flush();
        facetValue.addFacetValueTranslation(facetValueTranslation);
        facetValueRepository.saveAndFlush(facetValue);
        Long facetValueTranslationId = facetValueTranslation.getId();

        // Get all the facetValueList where facetValueTranslation equals to facetValueTranslationId
        defaultFacetValueShouldBeFound("facetValueTranslationId.equals=" + facetValueTranslationId);

        // Get all the facetValueList where facetValueTranslation equals to (facetValueTranslationId + 1)
        defaultFacetValueShouldNotBeFound("facetValueTranslationId.equals=" + (facetValueTranslationId + 1));
    }

    @Test
    @Transactional
    void getAllFacetValuesByProductVariantIsEqualToSomething() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);
        ProductVariant productVariant = ProductVariantResourceIT.createEntity(em);
        em.persist(productVariant);
        em.flush();
        facetValue.addProductVariant(productVariant);
        facetValueRepository.saveAndFlush(facetValue);
        Long productVariantId = productVariant.getId();

        // Get all the facetValueList where productVariant equals to productVariantId
        defaultFacetValueShouldBeFound("productVariantId.equals=" + productVariantId);

        // Get all the facetValueList where productVariant equals to (productVariantId + 1)
        defaultFacetValueShouldNotBeFound("productVariantId.equals=" + (productVariantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacetValueShouldBeFound(String filter) throws Exception {
        restFacetValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facetValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restFacetValueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacetValueShouldNotBeFound(String filter) throws Exception {
        restFacetValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacetValueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacetValue() throws Exception {
        // Get the facetValue
        restFacetValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFacetValue() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        int databaseSizeBeforeUpdate = facetValueRepository.findAll().size();

        // Update the facetValue
        FacetValue updatedFacetValue = facetValueRepository.findById(facetValue.getId()).get();
        // Disconnect from session so that the updates on updatedFacetValue are not directly saved in db
        em.detach(updatedFacetValue);
        updatedFacetValue.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).code(UPDATED_CODE);
        FacetValueDTO facetValueDTO = facetValueMapper.toDto(updatedFacetValue);

        restFacetValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facetValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueDTO))
            )
            .andExpect(status().isOk());

        // Validate the FacetValue in the database
        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeUpdate);
        FacetValue testFacetValue = facetValueList.get(facetValueList.size() - 1);
        assertThat(testFacetValue.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFacetValue.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testFacetValue.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingFacetValue() throws Exception {
        int databaseSizeBeforeUpdate = facetValueRepository.findAll().size();
        facetValue.setId(count.incrementAndGet());

        // Create the FacetValue
        FacetValueDTO facetValueDTO = facetValueMapper.toDto(facetValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacetValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facetValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetValue in the database
        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacetValue() throws Exception {
        int databaseSizeBeforeUpdate = facetValueRepository.findAll().size();
        facetValue.setId(count.incrementAndGet());

        // Create the FacetValue
        FacetValueDTO facetValueDTO = facetValueMapper.toDto(facetValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetValue in the database
        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacetValue() throws Exception {
        int databaseSizeBeforeUpdate = facetValueRepository.findAll().size();
        facetValue.setId(count.incrementAndGet());

        // Create the FacetValue
        FacetValueDTO facetValueDTO = facetValueMapper.toDto(facetValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetValueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetValueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacetValue in the database
        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacetValueWithPatch() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        int databaseSizeBeforeUpdate = facetValueRepository.findAll().size();

        // Update the facetValue using partial update
        FacetValue partialUpdatedFacetValue = new FacetValue();
        partialUpdatedFacetValue.setId(facetValue.getId());

        partialUpdatedFacetValue.code(UPDATED_CODE);

        restFacetValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacetValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacetValue))
            )
            .andExpect(status().isOk());

        // Validate the FacetValue in the database
        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeUpdate);
        FacetValue testFacetValue = facetValueList.get(facetValueList.size() - 1);
        assertThat(testFacetValue.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testFacetValue.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testFacetValue.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void fullUpdateFacetValueWithPatch() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        int databaseSizeBeforeUpdate = facetValueRepository.findAll().size();

        // Update the facetValue using partial update
        FacetValue partialUpdatedFacetValue = new FacetValue();
        partialUpdatedFacetValue.setId(facetValue.getId());

        partialUpdatedFacetValue.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).code(UPDATED_CODE);

        restFacetValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacetValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacetValue))
            )
            .andExpect(status().isOk());

        // Validate the FacetValue in the database
        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeUpdate);
        FacetValue testFacetValue = facetValueList.get(facetValueList.size() - 1);
        assertThat(testFacetValue.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFacetValue.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testFacetValue.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingFacetValue() throws Exception {
        int databaseSizeBeforeUpdate = facetValueRepository.findAll().size();
        facetValue.setId(count.incrementAndGet());

        // Create the FacetValue
        FacetValueDTO facetValueDTO = facetValueMapper.toDto(facetValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacetValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facetValueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facetValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetValue in the database
        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacetValue() throws Exception {
        int databaseSizeBeforeUpdate = facetValueRepository.findAll().size();
        facetValue.setId(count.incrementAndGet());

        // Create the FacetValue
        FacetValueDTO facetValueDTO = facetValueMapper.toDto(facetValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facetValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FacetValue in the database
        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacetValue() throws Exception {
        int databaseSizeBeforeUpdate = facetValueRepository.findAll().size();
        facetValue.setId(count.incrementAndGet());

        // Create the FacetValue
        FacetValueDTO facetValueDTO = facetValueMapper.toDto(facetValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetValueMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(facetValueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FacetValue in the database
        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacetValue() throws Exception {
        // Initialize the database
        facetValueRepository.saveAndFlush(facetValue);

        int databaseSizeBeforeDelete = facetValueRepository.findAll().size();

        // Delete the facetValue
        restFacetValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, facetValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FacetValue> facetValueList = facetValueRepository.findAll();
        assertThat(facetValueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
