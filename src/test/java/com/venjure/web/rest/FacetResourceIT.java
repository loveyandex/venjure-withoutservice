package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Channel;
import com.venjure.domain.Facet;
import com.venjure.domain.FacetTranslation;
import com.venjure.domain.FacetValue;
import com.venjure.repository.FacetRepository;
import com.venjure.service.FacetService;
import com.venjure.service.criteria.FacetCriteria;
import com.venjure.service.dto.FacetDTO;
import com.venjure.service.mapper.FacetMapper;
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
 * Integration tests for the {@link FacetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FacetResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ISPRIVATE = false;
    private static final Boolean UPDATED_ISPRIVATE = true;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacetRepository facetRepository;

    @Mock
    private FacetRepository facetRepositoryMock;

    @Autowired
    private FacetMapper facetMapper;

    @Mock
    private FacetService facetServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacetMockMvc;

    private Facet facet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facet createEntity(EntityManager em) {
        Facet facet = new Facet().createdat(DEFAULT_CREATEDAT).updatedat(DEFAULT_UPDATEDAT).isprivate(DEFAULT_ISPRIVATE).code(DEFAULT_CODE);
        return facet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facet createUpdatedEntity(EntityManager em) {
        Facet facet = new Facet().createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).isprivate(UPDATED_ISPRIVATE).code(UPDATED_CODE);
        return facet;
    }

    @BeforeEach
    public void initTest() {
        facet = createEntity(em);
    }

    @Test
    @Transactional
    void createFacet() throws Exception {
        int databaseSizeBeforeCreate = facetRepository.findAll().size();
        // Create the Facet
        FacetDTO facetDTO = facetMapper.toDto(facet);
        restFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetDTO)))
            .andExpect(status().isCreated());

        // Validate the Facet in the database
        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeCreate + 1);
        Facet testFacet = facetList.get(facetList.size() - 1);
        assertThat(testFacet.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testFacet.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testFacet.getIsprivate()).isEqualTo(DEFAULT_ISPRIVATE);
        assertThat(testFacet.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createFacetWithExistingId() throws Exception {
        // Create the Facet with an existing ID
        facet.setId(1L);
        FacetDTO facetDTO = facetMapper.toDto(facet);

        int databaseSizeBeforeCreate = facetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facet in the database
        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetRepository.findAll().size();
        // set the field null
        facet.setCreatedat(null);

        // Create the Facet, which fails.
        FacetDTO facetDTO = facetMapper.toDto(facet);

        restFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetDTO)))
            .andExpect(status().isBadRequest());

        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetRepository.findAll().size();
        // set the field null
        facet.setUpdatedat(null);

        // Create the Facet, which fails.
        FacetDTO facetDTO = facetMapper.toDto(facet);

        restFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetDTO)))
            .andExpect(status().isBadRequest());

        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsprivateIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetRepository.findAll().size();
        // set the field null
        facet.setIsprivate(null);

        // Create the Facet, which fails.
        FacetDTO facetDTO = facetMapper.toDto(facet);

        restFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetDTO)))
            .andExpect(status().isBadRequest());

        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = facetRepository.findAll().size();
        // set the field null
        facet.setCode(null);

        // Create the Facet, which fails.
        FacetDTO facetDTO = facetMapper.toDto(facet);

        restFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetDTO)))
            .andExpect(status().isBadRequest());

        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacets() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList
        restFacetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facet.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].isprivate").value(hasItem(DEFAULT_ISPRIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(facetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFacetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(facetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFacetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(facetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFacet() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get the facet
        restFacetMockMvc
            .perform(get(ENTITY_API_URL_ID, facet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facet.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.isprivate").value(DEFAULT_ISPRIVATE.booleanValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getFacetsByIdFiltering() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        Long id = facet.getId();

        defaultFacetShouldBeFound("id.equals=" + id);
        defaultFacetShouldNotBeFound("id.notEquals=" + id);

        defaultFacetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFacetShouldNotBeFound("id.greaterThan=" + id);

        defaultFacetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFacetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacetsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where createdat equals to DEFAULT_CREATEDAT
        defaultFacetShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the facetList where createdat equals to UPDATED_CREATEDAT
        defaultFacetShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where createdat not equals to DEFAULT_CREATEDAT
        defaultFacetShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the facetList where createdat not equals to UPDATED_CREATEDAT
        defaultFacetShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultFacetShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the facetList where createdat equals to UPDATED_CREATEDAT
        defaultFacetShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where createdat is not null
        defaultFacetShouldBeFound("createdat.specified=true");

        // Get all the facetList where createdat is null
        defaultFacetShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where updatedat equals to DEFAULT_UPDATEDAT
        defaultFacetShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the facetList where updatedat equals to UPDATED_UPDATEDAT
        defaultFacetShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultFacetShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the facetList where updatedat not equals to UPDATED_UPDATEDAT
        defaultFacetShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultFacetShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the facetList where updatedat equals to UPDATED_UPDATEDAT
        defaultFacetShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllFacetsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where updatedat is not null
        defaultFacetShouldBeFound("updatedat.specified=true");

        // Get all the facetList where updatedat is null
        defaultFacetShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetsByIsprivateIsEqualToSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where isprivate equals to DEFAULT_ISPRIVATE
        defaultFacetShouldBeFound("isprivate.equals=" + DEFAULT_ISPRIVATE);

        // Get all the facetList where isprivate equals to UPDATED_ISPRIVATE
        defaultFacetShouldNotBeFound("isprivate.equals=" + UPDATED_ISPRIVATE);
    }

    @Test
    @Transactional
    void getAllFacetsByIsprivateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where isprivate not equals to DEFAULT_ISPRIVATE
        defaultFacetShouldNotBeFound("isprivate.notEquals=" + DEFAULT_ISPRIVATE);

        // Get all the facetList where isprivate not equals to UPDATED_ISPRIVATE
        defaultFacetShouldBeFound("isprivate.notEquals=" + UPDATED_ISPRIVATE);
    }

    @Test
    @Transactional
    void getAllFacetsByIsprivateIsInShouldWork() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where isprivate in DEFAULT_ISPRIVATE or UPDATED_ISPRIVATE
        defaultFacetShouldBeFound("isprivate.in=" + DEFAULT_ISPRIVATE + "," + UPDATED_ISPRIVATE);

        // Get all the facetList where isprivate equals to UPDATED_ISPRIVATE
        defaultFacetShouldNotBeFound("isprivate.in=" + UPDATED_ISPRIVATE);
    }

    @Test
    @Transactional
    void getAllFacetsByIsprivateIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where isprivate is not null
        defaultFacetShouldBeFound("isprivate.specified=true");

        // Get all the facetList where isprivate is null
        defaultFacetShouldNotBeFound("isprivate.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where code equals to DEFAULT_CODE
        defaultFacetShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the facetList where code equals to UPDATED_CODE
        defaultFacetShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacetsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where code not equals to DEFAULT_CODE
        defaultFacetShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the facetList where code not equals to UPDATED_CODE
        defaultFacetShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacetsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where code in DEFAULT_CODE or UPDATED_CODE
        defaultFacetShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the facetList where code equals to UPDATED_CODE
        defaultFacetShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacetsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where code is not null
        defaultFacetShouldBeFound("code.specified=true");

        // Get all the facetList where code is null
        defaultFacetShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllFacetsByCodeContainsSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where code contains DEFAULT_CODE
        defaultFacetShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the facetList where code contains UPDATED_CODE
        defaultFacetShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacetsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        // Get all the facetList where code does not contain DEFAULT_CODE
        defaultFacetShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the facetList where code does not contain UPDATED_CODE
        defaultFacetShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllFacetsByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);
        Channel channel = ChannelResourceIT.createEntity(em);
        em.persist(channel);
        em.flush();
        facet.addChannel(channel);
        facetRepository.saveAndFlush(facet);
        Long channelId = channel.getId();

        // Get all the facetList where channel equals to channelId
        defaultFacetShouldBeFound("channelId.equals=" + channelId);

        // Get all the facetList where channel equals to (channelId + 1)
        defaultFacetShouldNotBeFound("channelId.equals=" + (channelId + 1));
    }

    @Test
    @Transactional
    void getAllFacetsByFacetTranslationIsEqualToSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);
        FacetTranslation facetTranslation = FacetTranslationResourceIT.createEntity(em);
        em.persist(facetTranslation);
        em.flush();
        facet.addFacetTranslation(facetTranslation);
        facetRepository.saveAndFlush(facet);
        Long facetTranslationId = facetTranslation.getId();

        // Get all the facetList where facetTranslation equals to facetTranslationId
        defaultFacetShouldBeFound("facetTranslationId.equals=" + facetTranslationId);

        // Get all the facetList where facetTranslation equals to (facetTranslationId + 1)
        defaultFacetShouldNotBeFound("facetTranslationId.equals=" + (facetTranslationId + 1));
    }

    @Test
    @Transactional
    void getAllFacetsByFacetValueIsEqualToSomething() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);
        FacetValue facetValue = FacetValueResourceIT.createEntity(em);
        em.persist(facetValue);
        em.flush();
        facet.addFacetValue(facetValue);
        facetRepository.saveAndFlush(facet);
        Long facetValueId = facetValue.getId();

        // Get all the facetList where facetValue equals to facetValueId
        defaultFacetShouldBeFound("facetValueId.equals=" + facetValueId);

        // Get all the facetList where facetValue equals to (facetValueId + 1)
        defaultFacetShouldNotBeFound("facetValueId.equals=" + (facetValueId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacetShouldBeFound(String filter) throws Exception {
        restFacetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facet.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].isprivate").value(hasItem(DEFAULT_ISPRIVATE.booleanValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restFacetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacetShouldNotBeFound(String filter) throws Exception {
        restFacetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacet() throws Exception {
        // Get the facet
        restFacetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFacet() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        int databaseSizeBeforeUpdate = facetRepository.findAll().size();

        // Update the facet
        Facet updatedFacet = facetRepository.findById(facet.getId()).get();
        // Disconnect from session so that the updates on updatedFacet are not directly saved in db
        em.detach(updatedFacet);
        updatedFacet.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).isprivate(UPDATED_ISPRIVATE).code(UPDATED_CODE);
        FacetDTO facetDTO = facetMapper.toDto(updatedFacet);

        restFacetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetDTO))
            )
            .andExpect(status().isOk());

        // Validate the Facet in the database
        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeUpdate);
        Facet testFacet = facetList.get(facetList.size() - 1);
        assertThat(testFacet.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFacet.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testFacet.getIsprivate()).isEqualTo(UPDATED_ISPRIVATE);
        assertThat(testFacet.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingFacet() throws Exception {
        int databaseSizeBeforeUpdate = facetRepository.findAll().size();
        facet.setId(count.incrementAndGet());

        // Create the Facet
        FacetDTO facetDTO = facetMapper.toDto(facet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facet in the database
        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacet() throws Exception {
        int databaseSizeBeforeUpdate = facetRepository.findAll().size();
        facet.setId(count.incrementAndGet());

        // Create the Facet
        FacetDTO facetDTO = facetMapper.toDto(facet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facet in the database
        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacet() throws Exception {
        int databaseSizeBeforeUpdate = facetRepository.findAll().size();
        facet.setId(count.incrementAndGet());

        // Create the Facet
        FacetDTO facetDTO = facetMapper.toDto(facet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facet in the database
        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacetWithPatch() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        int databaseSizeBeforeUpdate = facetRepository.findAll().size();

        // Update the facet using partial update
        Facet partialUpdatedFacet = new Facet();
        partialUpdatedFacet.setId(facet.getId());

        partialUpdatedFacet.createdat(UPDATED_CREATEDAT);

        restFacetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacet))
            )
            .andExpect(status().isOk());

        // Validate the Facet in the database
        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeUpdate);
        Facet testFacet = facetList.get(facetList.size() - 1);
        assertThat(testFacet.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFacet.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testFacet.getIsprivate()).isEqualTo(DEFAULT_ISPRIVATE);
        assertThat(testFacet.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateFacetWithPatch() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        int databaseSizeBeforeUpdate = facetRepository.findAll().size();

        // Update the facet using partial update
        Facet partialUpdatedFacet = new Facet();
        partialUpdatedFacet.setId(facet.getId());

        partialUpdatedFacet.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).isprivate(UPDATED_ISPRIVATE).code(UPDATED_CODE);

        restFacetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacet))
            )
            .andExpect(status().isOk());

        // Validate the Facet in the database
        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeUpdate);
        Facet testFacet = facetList.get(facetList.size() - 1);
        assertThat(testFacet.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testFacet.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testFacet.getIsprivate()).isEqualTo(UPDATED_ISPRIVATE);
        assertThat(testFacet.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingFacet() throws Exception {
        int databaseSizeBeforeUpdate = facetRepository.findAll().size();
        facet.setId(count.incrementAndGet());

        // Create the Facet
        FacetDTO facetDTO = facetMapper.toDto(facet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facet in the database
        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacet() throws Exception {
        int databaseSizeBeforeUpdate = facetRepository.findAll().size();
        facet.setId(count.incrementAndGet());

        // Create the Facet
        FacetDTO facetDTO = facetMapper.toDto(facet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facet in the database
        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacet() throws Exception {
        int databaseSizeBeforeUpdate = facetRepository.findAll().size();
        facet.setId(count.incrementAndGet());

        // Create the Facet
        FacetDTO facetDTO = facetMapper.toDto(facet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(facetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facet in the database
        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacet() throws Exception {
        // Initialize the database
        facetRepository.saveAndFlush(facet);

        int databaseSizeBeforeDelete = facetRepository.findAll().size();

        // Delete the facet
        restFacetMockMvc
            .perform(delete(ENTITY_API_URL_ID, facet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Facet> facetList = facetRepository.findAll();
        assertThat(facetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
