package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.ExampleEntity;
import com.venjure.repository.ExampleEntityRepository;
import com.venjure.service.criteria.ExampleEntityCriteria;
import com.venjure.service.dto.ExampleEntityDTO;
import com.venjure.service.mapper.ExampleEntityMapper;
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
 * Integration tests for the {@link ExampleEntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExampleEntityResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/example-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExampleEntityRepository exampleEntityRepository;

    @Autowired
    private ExampleEntityMapper exampleEntityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExampleEntityMockMvc;

    private ExampleEntity exampleEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExampleEntity createEntity(EntityManager em) {
        ExampleEntity exampleEntity = new ExampleEntity().createdat(DEFAULT_CREATEDAT).updatedat(DEFAULT_UPDATEDAT).name(DEFAULT_NAME);
        return exampleEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExampleEntity createUpdatedEntity(EntityManager em) {
        ExampleEntity exampleEntity = new ExampleEntity().createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME);
        return exampleEntity;
    }

    @BeforeEach
    public void initTest() {
        exampleEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createExampleEntity() throws Exception {
        int databaseSizeBeforeCreate = exampleEntityRepository.findAll().size();
        // Create the ExampleEntity
        ExampleEntityDTO exampleEntityDTO = exampleEntityMapper.toDto(exampleEntity);
        restExampleEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exampleEntityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExampleEntity in the database
        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeCreate + 1);
        ExampleEntity testExampleEntity = exampleEntityList.get(exampleEntityList.size() - 1);
        assertThat(testExampleEntity.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testExampleEntity.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testExampleEntity.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createExampleEntityWithExistingId() throws Exception {
        // Create the ExampleEntity with an existing ID
        exampleEntity.setId(1L);
        ExampleEntityDTO exampleEntityDTO = exampleEntityMapper.toDto(exampleEntity);

        int databaseSizeBeforeCreate = exampleEntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExampleEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exampleEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExampleEntity in the database
        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = exampleEntityRepository.findAll().size();
        // set the field null
        exampleEntity.setCreatedat(null);

        // Create the ExampleEntity, which fails.
        ExampleEntityDTO exampleEntityDTO = exampleEntityMapper.toDto(exampleEntity);

        restExampleEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exampleEntityDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = exampleEntityRepository.findAll().size();
        // set the field null
        exampleEntity.setUpdatedat(null);

        // Create the ExampleEntity, which fails.
        ExampleEntityDTO exampleEntityDTO = exampleEntityMapper.toDto(exampleEntity);

        restExampleEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exampleEntityDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exampleEntityRepository.findAll().size();
        // set the field null
        exampleEntity.setName(null);

        // Create the ExampleEntity, which fails.
        ExampleEntityDTO exampleEntityDTO = exampleEntityMapper.toDto(exampleEntity);

        restExampleEntityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exampleEntityDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExampleEntities() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList
        restExampleEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exampleEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getExampleEntity() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get the exampleEntity
        restExampleEntityMockMvc
            .perform(get(ENTITY_API_URL_ID, exampleEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exampleEntity.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getExampleEntitiesByIdFiltering() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        Long id = exampleEntity.getId();

        defaultExampleEntityShouldBeFound("id.equals=" + id);
        defaultExampleEntityShouldNotBeFound("id.notEquals=" + id);

        defaultExampleEntityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExampleEntityShouldNotBeFound("id.greaterThan=" + id);

        defaultExampleEntityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExampleEntityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where createdat equals to DEFAULT_CREATEDAT
        defaultExampleEntityShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the exampleEntityList where createdat equals to UPDATED_CREATEDAT
        defaultExampleEntityShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where createdat not equals to DEFAULT_CREATEDAT
        defaultExampleEntityShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the exampleEntityList where createdat not equals to UPDATED_CREATEDAT
        defaultExampleEntityShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultExampleEntityShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the exampleEntityList where createdat equals to UPDATED_CREATEDAT
        defaultExampleEntityShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where createdat is not null
        defaultExampleEntityShouldBeFound("createdat.specified=true");

        // Get all the exampleEntityList where createdat is null
        defaultExampleEntityShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where updatedat equals to DEFAULT_UPDATEDAT
        defaultExampleEntityShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the exampleEntityList where updatedat equals to UPDATED_UPDATEDAT
        defaultExampleEntityShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultExampleEntityShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the exampleEntityList where updatedat not equals to UPDATED_UPDATEDAT
        defaultExampleEntityShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultExampleEntityShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the exampleEntityList where updatedat equals to UPDATED_UPDATEDAT
        defaultExampleEntityShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where updatedat is not null
        defaultExampleEntityShouldBeFound("updatedat.specified=true");

        // Get all the exampleEntityList where updatedat is null
        defaultExampleEntityShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where name equals to DEFAULT_NAME
        defaultExampleEntityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the exampleEntityList where name equals to UPDATED_NAME
        defaultExampleEntityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where name not equals to DEFAULT_NAME
        defaultExampleEntityShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the exampleEntityList where name not equals to UPDATED_NAME
        defaultExampleEntityShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultExampleEntityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the exampleEntityList where name equals to UPDATED_NAME
        defaultExampleEntityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where name is not null
        defaultExampleEntityShouldBeFound("name.specified=true");

        // Get all the exampleEntityList where name is null
        defaultExampleEntityShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where name contains DEFAULT_NAME
        defaultExampleEntityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the exampleEntityList where name contains UPDATED_NAME
        defaultExampleEntityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExampleEntitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        // Get all the exampleEntityList where name does not contain DEFAULT_NAME
        defaultExampleEntityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the exampleEntityList where name does not contain UPDATED_NAME
        defaultExampleEntityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExampleEntityShouldBeFound(String filter) throws Exception {
        restExampleEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exampleEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restExampleEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExampleEntityShouldNotBeFound(String filter) throws Exception {
        restExampleEntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExampleEntityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExampleEntity() throws Exception {
        // Get the exampleEntity
        restExampleEntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExampleEntity() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        int databaseSizeBeforeUpdate = exampleEntityRepository.findAll().size();

        // Update the exampleEntity
        ExampleEntity updatedExampleEntity = exampleEntityRepository.findById(exampleEntity.getId()).get();
        // Disconnect from session so that the updates on updatedExampleEntity are not directly saved in db
        em.detach(updatedExampleEntity);
        updatedExampleEntity.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME);
        ExampleEntityDTO exampleEntityDTO = exampleEntityMapper.toDto(updatedExampleEntity);

        restExampleEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exampleEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exampleEntityDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExampleEntity in the database
        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeUpdate);
        ExampleEntity testExampleEntity = exampleEntityList.get(exampleEntityList.size() - 1);
        assertThat(testExampleEntity.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testExampleEntity.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testExampleEntity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingExampleEntity() throws Exception {
        int databaseSizeBeforeUpdate = exampleEntityRepository.findAll().size();
        exampleEntity.setId(count.incrementAndGet());

        // Create the ExampleEntity
        ExampleEntityDTO exampleEntityDTO = exampleEntityMapper.toDto(exampleEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExampleEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exampleEntityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exampleEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExampleEntity in the database
        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExampleEntity() throws Exception {
        int databaseSizeBeforeUpdate = exampleEntityRepository.findAll().size();
        exampleEntity.setId(count.incrementAndGet());

        // Create the ExampleEntity
        ExampleEntityDTO exampleEntityDTO = exampleEntityMapper.toDto(exampleEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExampleEntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exampleEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExampleEntity in the database
        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExampleEntity() throws Exception {
        int databaseSizeBeforeUpdate = exampleEntityRepository.findAll().size();
        exampleEntity.setId(count.incrementAndGet());

        // Create the ExampleEntity
        ExampleEntityDTO exampleEntityDTO = exampleEntityMapper.toDto(exampleEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExampleEntityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exampleEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExampleEntity in the database
        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExampleEntityWithPatch() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        int databaseSizeBeforeUpdate = exampleEntityRepository.findAll().size();

        // Update the exampleEntity using partial update
        ExampleEntity partialUpdatedExampleEntity = new ExampleEntity();
        partialUpdatedExampleEntity.setId(exampleEntity.getId());

        partialUpdatedExampleEntity.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME);

        restExampleEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExampleEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExampleEntity))
            )
            .andExpect(status().isOk());

        // Validate the ExampleEntity in the database
        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeUpdate);
        ExampleEntity testExampleEntity = exampleEntityList.get(exampleEntityList.size() - 1);
        assertThat(testExampleEntity.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testExampleEntity.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testExampleEntity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateExampleEntityWithPatch() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        int databaseSizeBeforeUpdate = exampleEntityRepository.findAll().size();

        // Update the exampleEntity using partial update
        ExampleEntity partialUpdatedExampleEntity = new ExampleEntity();
        partialUpdatedExampleEntity.setId(exampleEntity.getId());

        partialUpdatedExampleEntity.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME);

        restExampleEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExampleEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExampleEntity))
            )
            .andExpect(status().isOk());

        // Validate the ExampleEntity in the database
        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeUpdate);
        ExampleEntity testExampleEntity = exampleEntityList.get(exampleEntityList.size() - 1);
        assertThat(testExampleEntity.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testExampleEntity.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testExampleEntity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingExampleEntity() throws Exception {
        int databaseSizeBeforeUpdate = exampleEntityRepository.findAll().size();
        exampleEntity.setId(count.incrementAndGet());

        // Create the ExampleEntity
        ExampleEntityDTO exampleEntityDTO = exampleEntityMapper.toDto(exampleEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExampleEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exampleEntityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exampleEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExampleEntity in the database
        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExampleEntity() throws Exception {
        int databaseSizeBeforeUpdate = exampleEntityRepository.findAll().size();
        exampleEntity.setId(count.incrementAndGet());

        // Create the ExampleEntity
        ExampleEntityDTO exampleEntityDTO = exampleEntityMapper.toDto(exampleEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExampleEntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exampleEntityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExampleEntity in the database
        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExampleEntity() throws Exception {
        int databaseSizeBeforeUpdate = exampleEntityRepository.findAll().size();
        exampleEntity.setId(count.incrementAndGet());

        // Create the ExampleEntity
        ExampleEntityDTO exampleEntityDTO = exampleEntityMapper.toDto(exampleEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExampleEntityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exampleEntityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExampleEntity in the database
        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExampleEntity() throws Exception {
        // Initialize the database
        exampleEntityRepository.saveAndFlush(exampleEntity);

        int databaseSizeBeforeDelete = exampleEntityRepository.findAll().size();

        // Delete the exampleEntity
        restExampleEntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, exampleEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExampleEntity> exampleEntityList = exampleEntityRepository.findAll();
        assertThat(exampleEntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
