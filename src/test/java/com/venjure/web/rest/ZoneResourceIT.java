package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Country;
import com.venjure.domain.Zone;
import com.venjure.repository.ZoneRepository;
import com.venjure.service.ZoneService;
import com.venjure.service.criteria.ZoneCriteria;
import com.venjure.service.dto.ZoneDTO;
import com.venjure.service.mapper.ZoneMapper;
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
 * Integration tests for the {@link ZoneResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ZoneResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/zones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ZoneRepository zoneRepository;

    @Mock
    private ZoneRepository zoneRepositoryMock;

    @Autowired
    private ZoneMapper zoneMapper;

    @Mock
    private ZoneService zoneServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restZoneMockMvc;

    private Zone zone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zone createEntity(EntityManager em) {
        Zone zone = new Zone().createdat(DEFAULT_CREATEDAT).updatedat(DEFAULT_UPDATEDAT).name(DEFAULT_NAME);
        return zone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zone createUpdatedEntity(EntityManager em) {
        Zone zone = new Zone().createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME);
        return zone;
    }

    @BeforeEach
    public void initTest() {
        zone = createEntity(em);
    }

    @Test
    @Transactional
    void createZone() throws Exception {
        int databaseSizeBeforeCreate = zoneRepository.findAll().size();
        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);
        restZoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isCreated());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeCreate + 1);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testZone.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testZone.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createZoneWithExistingId() throws Exception {
        // Create the Zone with an existing ID
        zone.setId(1L);
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        int databaseSizeBeforeCreate = zoneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restZoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setCreatedat(null);

        // Create the Zone, which fails.
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        restZoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setUpdatedat(null);

        // Create the Zone, which fails.
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        restZoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setName(null);

        // Create the Zone, which fails.
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        restZoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllZones() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList
        restZoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zone.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllZonesWithEagerRelationshipsIsEnabled() throws Exception {
        when(zoneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restZoneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(zoneServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllZonesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(zoneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restZoneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(zoneServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get the zone
        restZoneMockMvc
            .perform(get(ENTITY_API_URL_ID, zone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zone.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getZonesByIdFiltering() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        Long id = zone.getId();

        defaultZoneShouldBeFound("id.equals=" + id);
        defaultZoneShouldNotBeFound("id.notEquals=" + id);

        defaultZoneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultZoneShouldNotBeFound("id.greaterThan=" + id);

        defaultZoneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultZoneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllZonesByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where createdat equals to DEFAULT_CREATEDAT
        defaultZoneShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the zoneList where createdat equals to UPDATED_CREATEDAT
        defaultZoneShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllZonesByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where createdat not equals to DEFAULT_CREATEDAT
        defaultZoneShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the zoneList where createdat not equals to UPDATED_CREATEDAT
        defaultZoneShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllZonesByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultZoneShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the zoneList where createdat equals to UPDATED_CREATEDAT
        defaultZoneShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllZonesByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where createdat is not null
        defaultZoneShouldBeFound("createdat.specified=true");

        // Get all the zoneList where createdat is null
        defaultZoneShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllZonesByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where updatedat equals to DEFAULT_UPDATEDAT
        defaultZoneShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the zoneList where updatedat equals to UPDATED_UPDATEDAT
        defaultZoneShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllZonesByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultZoneShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the zoneList where updatedat not equals to UPDATED_UPDATEDAT
        defaultZoneShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllZonesByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultZoneShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the zoneList where updatedat equals to UPDATED_UPDATEDAT
        defaultZoneShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllZonesByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where updatedat is not null
        defaultZoneShouldBeFound("updatedat.specified=true");

        // Get all the zoneList where updatedat is null
        defaultZoneShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllZonesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where name equals to DEFAULT_NAME
        defaultZoneShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the zoneList where name equals to UPDATED_NAME
        defaultZoneShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllZonesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where name not equals to DEFAULT_NAME
        defaultZoneShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the zoneList where name not equals to UPDATED_NAME
        defaultZoneShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllZonesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where name in DEFAULT_NAME or UPDATED_NAME
        defaultZoneShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the zoneList where name equals to UPDATED_NAME
        defaultZoneShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllZonesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where name is not null
        defaultZoneShouldBeFound("name.specified=true");

        // Get all the zoneList where name is null
        defaultZoneShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllZonesByNameContainsSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where name contains DEFAULT_NAME
        defaultZoneShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the zoneList where name contains UPDATED_NAME
        defaultZoneShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllZonesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where name does not contain DEFAULT_NAME
        defaultZoneShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the zoneList where name does not contain UPDATED_NAME
        defaultZoneShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllZonesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);
        Country country = CountryResourceIT.createEntity(em);
        em.persist(country);
        em.flush();
        zone.addCountry(country);
        zoneRepository.saveAndFlush(zone);
        Long countryId = country.getId();

        // Get all the zoneList where country equals to countryId
        defaultZoneShouldBeFound("countryId.equals=" + countryId);

        // Get all the zoneList where country equals to (countryId + 1)
        defaultZoneShouldNotBeFound("countryId.equals=" + (countryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultZoneShouldBeFound(String filter) throws Exception {
        restZoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zone.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restZoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultZoneShouldNotBeFound(String filter) throws Exception {
        restZoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restZoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingZone() throws Exception {
        // Get the zone
        restZoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();

        // Update the zone
        Zone updatedZone = zoneRepository.findById(zone.getId()).get();
        // Disconnect from session so that the updates on updatedZone are not directly saved in db
        em.detach(updatedZone);
        updatedZone.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME);
        ZoneDTO zoneDTO = zoneMapper.toDto(updatedZone);

        restZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zoneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isOk());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testZone.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testZone.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();
        zone.setId(count.incrementAndGet());

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zoneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();
        zone.setId(count.incrementAndGet());

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();
        zone.setId(count.incrementAndGet());

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateZoneWithPatch() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();

        // Update the zone using partial update
        Zone partialUpdatedZone = new Zone();
        partialUpdatedZone.setId(zone.getId());

        partialUpdatedZone.updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME);

        restZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZone))
            )
            .andExpect(status().isOk());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testZone.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testZone.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateZoneWithPatch() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();

        // Update the zone using partial update
        Zone partialUpdatedZone = new Zone();
        partialUpdatedZone.setId(zone.getId());

        partialUpdatedZone.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME);

        restZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZone))
            )
            .andExpect(status().isOk());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testZone.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testZone.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();
        zone.setId(count.incrementAndGet());

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, zoneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();
        zone.setId(count.incrementAndGet());

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();
        zone.setId(count.incrementAndGet());

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(zoneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        int databaseSizeBeforeDelete = zoneRepository.findAll().size();

        // Delete the zone
        restZoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, zone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
