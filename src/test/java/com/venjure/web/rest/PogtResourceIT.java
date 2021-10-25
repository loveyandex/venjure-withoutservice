package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Pogt;
import com.venjure.domain.ProductOptionGroup;
import com.venjure.repository.PogtRepository;
import com.venjure.service.criteria.PogtCriteria;
import com.venjure.service.dto.PogtDTO;
import com.venjure.service.mapper.PogtMapper;
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
 * Integration tests for the {@link PogtResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PogtResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LANGUAGECODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGECODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pogts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PogtRepository pogtRepository;

    @Autowired
    private PogtMapper pogtMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPogtMockMvc;

    private Pogt pogt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pogt createEntity(EntityManager em) {
        Pogt pogt = new Pogt()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .languagecode(DEFAULT_LANGUAGECODE)
            .name(DEFAULT_NAME);
        return pogt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pogt createUpdatedEntity(EntityManager em) {
        Pogt pogt = new Pogt()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .languagecode(UPDATED_LANGUAGECODE)
            .name(UPDATED_NAME);
        return pogt;
    }

    @BeforeEach
    public void initTest() {
        pogt = createEntity(em);
    }

    @Test
    @Transactional
    void createPogt() throws Exception {
        int databaseSizeBeforeCreate = pogtRepository.findAll().size();
        // Create the Pogt
        PogtDTO pogtDTO = pogtMapper.toDto(pogt);
        restPogtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pogtDTO)))
            .andExpect(status().isCreated());

        // Validate the Pogt in the database
        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeCreate + 1);
        Pogt testPogt = pogtList.get(pogtList.size() - 1);
        assertThat(testPogt.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testPogt.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testPogt.getLanguagecode()).isEqualTo(DEFAULT_LANGUAGECODE);
        assertThat(testPogt.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPogtWithExistingId() throws Exception {
        // Create the Pogt with an existing ID
        pogt.setId(1L);
        PogtDTO pogtDTO = pogtMapper.toDto(pogt);

        int databaseSizeBeforeCreate = pogtRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPogtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pogtDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pogt in the database
        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = pogtRepository.findAll().size();
        // set the field null
        pogt.setCreatedat(null);

        // Create the Pogt, which fails.
        PogtDTO pogtDTO = pogtMapper.toDto(pogt);

        restPogtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pogtDTO)))
            .andExpect(status().isBadRequest());

        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = pogtRepository.findAll().size();
        // set the field null
        pogt.setUpdatedat(null);

        // Create the Pogt, which fails.
        PogtDTO pogtDTO = pogtMapper.toDto(pogt);

        restPogtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pogtDTO)))
            .andExpect(status().isBadRequest());

        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguagecodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pogtRepository.findAll().size();
        // set the field null
        pogt.setLanguagecode(null);

        // Create the Pogt, which fails.
        PogtDTO pogtDTO = pogtMapper.toDto(pogt);

        restPogtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pogtDTO)))
            .andExpect(status().isBadRequest());

        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pogtRepository.findAll().size();
        // set the field null
        pogt.setName(null);

        // Create the Pogt, which fails.
        PogtDTO pogtDTO = pogtMapper.toDto(pogt);

        restPogtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pogtDTO)))
            .andExpect(status().isBadRequest());

        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPogts() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList
        restPogtMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pogt.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPogt() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get the pogt
        restPogtMockMvc
            .perform(get(ENTITY_API_URL_ID, pogt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pogt.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.languagecode").value(DEFAULT_LANGUAGECODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getPogtsByIdFiltering() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        Long id = pogt.getId();

        defaultPogtShouldBeFound("id.equals=" + id);
        defaultPogtShouldNotBeFound("id.notEquals=" + id);

        defaultPogtShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPogtShouldNotBeFound("id.greaterThan=" + id);

        defaultPogtShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPogtShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPogtsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where createdat equals to DEFAULT_CREATEDAT
        defaultPogtShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the pogtList where createdat equals to UPDATED_CREATEDAT
        defaultPogtShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllPogtsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where createdat not equals to DEFAULT_CREATEDAT
        defaultPogtShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the pogtList where createdat not equals to UPDATED_CREATEDAT
        defaultPogtShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllPogtsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultPogtShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the pogtList where createdat equals to UPDATED_CREATEDAT
        defaultPogtShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllPogtsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where createdat is not null
        defaultPogtShouldBeFound("createdat.specified=true");

        // Get all the pogtList where createdat is null
        defaultPogtShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllPogtsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where updatedat equals to DEFAULT_UPDATEDAT
        defaultPogtShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the pogtList where updatedat equals to UPDATED_UPDATEDAT
        defaultPogtShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllPogtsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultPogtShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the pogtList where updatedat not equals to UPDATED_UPDATEDAT
        defaultPogtShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllPogtsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultPogtShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the pogtList where updatedat equals to UPDATED_UPDATEDAT
        defaultPogtShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllPogtsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where updatedat is not null
        defaultPogtShouldBeFound("updatedat.specified=true");

        // Get all the pogtList where updatedat is null
        defaultPogtShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllPogtsByLanguagecodeIsEqualToSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where languagecode equals to DEFAULT_LANGUAGECODE
        defaultPogtShouldBeFound("languagecode.equals=" + DEFAULT_LANGUAGECODE);

        // Get all the pogtList where languagecode equals to UPDATED_LANGUAGECODE
        defaultPogtShouldNotBeFound("languagecode.equals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllPogtsByLanguagecodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where languagecode not equals to DEFAULT_LANGUAGECODE
        defaultPogtShouldNotBeFound("languagecode.notEquals=" + DEFAULT_LANGUAGECODE);

        // Get all the pogtList where languagecode not equals to UPDATED_LANGUAGECODE
        defaultPogtShouldBeFound("languagecode.notEquals=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllPogtsByLanguagecodeIsInShouldWork() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where languagecode in DEFAULT_LANGUAGECODE or UPDATED_LANGUAGECODE
        defaultPogtShouldBeFound("languagecode.in=" + DEFAULT_LANGUAGECODE + "," + UPDATED_LANGUAGECODE);

        // Get all the pogtList where languagecode equals to UPDATED_LANGUAGECODE
        defaultPogtShouldNotBeFound("languagecode.in=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllPogtsByLanguagecodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where languagecode is not null
        defaultPogtShouldBeFound("languagecode.specified=true");

        // Get all the pogtList where languagecode is null
        defaultPogtShouldNotBeFound("languagecode.specified=false");
    }

    @Test
    @Transactional
    void getAllPogtsByLanguagecodeContainsSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where languagecode contains DEFAULT_LANGUAGECODE
        defaultPogtShouldBeFound("languagecode.contains=" + DEFAULT_LANGUAGECODE);

        // Get all the pogtList where languagecode contains UPDATED_LANGUAGECODE
        defaultPogtShouldNotBeFound("languagecode.contains=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllPogtsByLanguagecodeNotContainsSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where languagecode does not contain DEFAULT_LANGUAGECODE
        defaultPogtShouldNotBeFound("languagecode.doesNotContain=" + DEFAULT_LANGUAGECODE);

        // Get all the pogtList where languagecode does not contain UPDATED_LANGUAGECODE
        defaultPogtShouldBeFound("languagecode.doesNotContain=" + UPDATED_LANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllPogtsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where name equals to DEFAULT_NAME
        defaultPogtShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the pogtList where name equals to UPDATED_NAME
        defaultPogtShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPogtsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where name not equals to DEFAULT_NAME
        defaultPogtShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the pogtList where name not equals to UPDATED_NAME
        defaultPogtShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPogtsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPogtShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the pogtList where name equals to UPDATED_NAME
        defaultPogtShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPogtsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where name is not null
        defaultPogtShouldBeFound("name.specified=true");

        // Get all the pogtList where name is null
        defaultPogtShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPogtsByNameContainsSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where name contains DEFAULT_NAME
        defaultPogtShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the pogtList where name contains UPDATED_NAME
        defaultPogtShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPogtsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        // Get all the pogtList where name does not contain DEFAULT_NAME
        defaultPogtShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the pogtList where name does not contain UPDATED_NAME
        defaultPogtShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPogtsByBaseIsEqualToSomething() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);
        ProductOptionGroup base = ProductOptionGroupResourceIT.createEntity(em);
        em.persist(base);
        em.flush();
        pogt.setBase(base);
        pogtRepository.saveAndFlush(pogt);
        Long baseId = base.getId();

        // Get all the pogtList where base equals to baseId
        defaultPogtShouldBeFound("baseId.equals=" + baseId);

        // Get all the pogtList where base equals to (baseId + 1)
        defaultPogtShouldNotBeFound("baseId.equals=" + (baseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPogtShouldBeFound(String filter) throws Exception {
        restPogtMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pogt.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].languagecode").value(hasItem(DEFAULT_LANGUAGECODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restPogtMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPogtShouldNotBeFound(String filter) throws Exception {
        restPogtMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPogtMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPogt() throws Exception {
        // Get the pogt
        restPogtMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPogt() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        int databaseSizeBeforeUpdate = pogtRepository.findAll().size();

        // Update the pogt
        Pogt updatedPogt = pogtRepository.findById(pogt.getId()).get();
        // Disconnect from session so that the updates on updatedPogt are not directly saved in db
        em.detach(updatedPogt);
        updatedPogt.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).languagecode(UPDATED_LANGUAGECODE).name(UPDATED_NAME);
        PogtDTO pogtDTO = pogtMapper.toDto(updatedPogt);

        restPogtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pogtDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pogtDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pogt in the database
        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeUpdate);
        Pogt testPogt = pogtList.get(pogtList.size() - 1);
        assertThat(testPogt.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testPogt.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testPogt.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testPogt.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPogt() throws Exception {
        int databaseSizeBeforeUpdate = pogtRepository.findAll().size();
        pogt.setId(count.incrementAndGet());

        // Create the Pogt
        PogtDTO pogtDTO = pogtMapper.toDto(pogt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPogtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pogtDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pogtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pogt in the database
        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPogt() throws Exception {
        int databaseSizeBeforeUpdate = pogtRepository.findAll().size();
        pogt.setId(count.incrementAndGet());

        // Create the Pogt
        PogtDTO pogtDTO = pogtMapper.toDto(pogt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPogtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pogtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pogt in the database
        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPogt() throws Exception {
        int databaseSizeBeforeUpdate = pogtRepository.findAll().size();
        pogt.setId(count.incrementAndGet());

        // Create the Pogt
        PogtDTO pogtDTO = pogtMapper.toDto(pogt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPogtMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pogtDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pogt in the database
        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePogtWithPatch() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        int databaseSizeBeforeUpdate = pogtRepository.findAll().size();

        // Update the pogt using partial update
        Pogt partialUpdatedPogt = new Pogt();
        partialUpdatedPogt.setId(pogt.getId());

        partialUpdatedPogt.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME);

        restPogtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPogt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPogt))
            )
            .andExpect(status().isOk());

        // Validate the Pogt in the database
        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeUpdate);
        Pogt testPogt = pogtList.get(pogtList.size() - 1);
        assertThat(testPogt.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testPogt.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testPogt.getLanguagecode()).isEqualTo(DEFAULT_LANGUAGECODE);
        assertThat(testPogt.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePogtWithPatch() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        int databaseSizeBeforeUpdate = pogtRepository.findAll().size();

        // Update the pogt using partial update
        Pogt partialUpdatedPogt = new Pogt();
        partialUpdatedPogt.setId(pogt.getId());

        partialUpdatedPogt.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).languagecode(UPDATED_LANGUAGECODE).name(UPDATED_NAME);

        restPogtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPogt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPogt))
            )
            .andExpect(status().isOk());

        // Validate the Pogt in the database
        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeUpdate);
        Pogt testPogt = pogtList.get(pogtList.size() - 1);
        assertThat(testPogt.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testPogt.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testPogt.getLanguagecode()).isEqualTo(UPDATED_LANGUAGECODE);
        assertThat(testPogt.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPogt() throws Exception {
        int databaseSizeBeforeUpdate = pogtRepository.findAll().size();
        pogt.setId(count.incrementAndGet());

        // Create the Pogt
        PogtDTO pogtDTO = pogtMapper.toDto(pogt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPogtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pogtDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pogtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pogt in the database
        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPogt() throws Exception {
        int databaseSizeBeforeUpdate = pogtRepository.findAll().size();
        pogt.setId(count.incrementAndGet());

        // Create the Pogt
        PogtDTO pogtDTO = pogtMapper.toDto(pogt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPogtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pogtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pogt in the database
        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPogt() throws Exception {
        int databaseSizeBeforeUpdate = pogtRepository.findAll().size();
        pogt.setId(count.incrementAndGet());

        // Create the Pogt
        PogtDTO pogtDTO = pogtMapper.toDto(pogt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPogtMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pogtDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pogt in the database
        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePogt() throws Exception {
        // Initialize the database
        pogtRepository.saveAndFlush(pogt);

        int databaseSizeBeforeDelete = pogtRepository.findAll().size();

        // Delete the pogt
        restPogtMockMvc
            .perform(delete(ENTITY_API_URL_ID, pogt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pogt> pogtList = pogtRepository.findAll();
        assertThat(pogtList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
