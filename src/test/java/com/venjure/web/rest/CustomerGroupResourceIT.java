package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Customer;
import com.venjure.domain.CustomerGroup;
import com.venjure.repository.CustomerGroupRepository;
import com.venjure.service.criteria.CustomerGroupCriteria;
import com.venjure.service.dto.CustomerGroupDTO;
import com.venjure.service.mapper.CustomerGroupMapper;
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
 * Integration tests for the {@link CustomerGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerGroupResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerGroupRepository customerGroupRepository;

    @Autowired
    private CustomerGroupMapper customerGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerGroupMockMvc;

    private CustomerGroup customerGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerGroup createEntity(EntityManager em) {
        CustomerGroup customerGroup = new CustomerGroup().createdat(DEFAULT_CREATEDAT).updatedat(DEFAULT_UPDATEDAT).name(DEFAULT_NAME);
        return customerGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerGroup createUpdatedEntity(EntityManager em) {
        CustomerGroup customerGroup = new CustomerGroup().createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME);
        return customerGroup;
    }

    @BeforeEach
    public void initTest() {
        customerGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerGroup() throws Exception {
        int databaseSizeBeforeCreate = customerGroupRepository.findAll().size();
        // Create the CustomerGroup
        CustomerGroupDTO customerGroupDTO = customerGroupMapper.toDto(customerGroup);
        restCustomerGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerGroupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerGroup testCustomerGroup = customerGroupList.get(customerGroupList.size() - 1);
        assertThat(testCustomerGroup.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testCustomerGroup.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testCustomerGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCustomerGroupWithExistingId() throws Exception {
        // Create the CustomerGroup with an existing ID
        customerGroup.setId(1L);
        CustomerGroupDTO customerGroupDTO = customerGroupMapper.toDto(customerGroup);

        int databaseSizeBeforeCreate = customerGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerGroupRepository.findAll().size();
        // set the field null
        customerGroup.setCreatedat(null);

        // Create the CustomerGroup, which fails.
        CustomerGroupDTO customerGroupDTO = customerGroupMapper.toDto(customerGroup);

        restCustomerGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerGroupRepository.findAll().size();
        // set the field null
        customerGroup.setUpdatedat(null);

        // Create the CustomerGroup, which fails.
        CustomerGroupDTO customerGroupDTO = customerGroupMapper.toDto(customerGroup);

        restCustomerGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerGroupRepository.findAll().size();
        // set the field null
        customerGroup.setName(null);

        // Create the CustomerGroup, which fails.
        CustomerGroupDTO customerGroupDTO = customerGroupMapper.toDto(customerGroup);

        restCustomerGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerGroups() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList
        restCustomerGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCustomerGroup() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get the customerGroup
        restCustomerGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, customerGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerGroup.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getCustomerGroupsByIdFiltering() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        Long id = customerGroup.getId();

        defaultCustomerGroupShouldBeFound("id.equals=" + id);
        defaultCustomerGroupShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerGroupShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where createdat equals to DEFAULT_CREATEDAT
        defaultCustomerGroupShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the customerGroupList where createdat equals to UPDATED_CREATEDAT
        defaultCustomerGroupShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where createdat not equals to DEFAULT_CREATEDAT
        defaultCustomerGroupShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the customerGroupList where createdat not equals to UPDATED_CREATEDAT
        defaultCustomerGroupShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultCustomerGroupShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the customerGroupList where createdat equals to UPDATED_CREATEDAT
        defaultCustomerGroupShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where createdat is not null
        defaultCustomerGroupShouldBeFound("createdat.specified=true");

        // Get all the customerGroupList where createdat is null
        defaultCustomerGroupShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where updatedat equals to DEFAULT_UPDATEDAT
        defaultCustomerGroupShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the customerGroupList where updatedat equals to UPDATED_UPDATEDAT
        defaultCustomerGroupShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultCustomerGroupShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the customerGroupList where updatedat not equals to UPDATED_UPDATEDAT
        defaultCustomerGroupShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultCustomerGroupShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the customerGroupList where updatedat equals to UPDATED_UPDATEDAT
        defaultCustomerGroupShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where updatedat is not null
        defaultCustomerGroupShouldBeFound("updatedat.specified=true");

        // Get all the customerGroupList where updatedat is null
        defaultCustomerGroupShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where name equals to DEFAULT_NAME
        defaultCustomerGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the customerGroupList where name equals to UPDATED_NAME
        defaultCustomerGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where name not equals to DEFAULT_NAME
        defaultCustomerGroupShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the customerGroupList where name not equals to UPDATED_NAME
        defaultCustomerGroupShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCustomerGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the customerGroupList where name equals to UPDATED_NAME
        defaultCustomerGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where name is not null
        defaultCustomerGroupShouldBeFound("name.specified=true");

        // Get all the customerGroupList where name is null
        defaultCustomerGroupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where name contains DEFAULT_NAME
        defaultCustomerGroupShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the customerGroupList where name contains UPDATED_NAME
        defaultCustomerGroupShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroupList where name does not contain DEFAULT_NAME
        defaultCustomerGroupShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the customerGroupList where name does not contain UPDATED_NAME
        defaultCustomerGroupShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerGroupsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        customerGroup.addCustomer(customer);
        customerGroupRepository.saveAndFlush(customerGroup);
        Long customerId = customer.getId();

        // Get all the customerGroupList where customer equals to customerId
        defaultCustomerGroupShouldBeFound("customerId.equals=" + customerId);

        // Get all the customerGroupList where customer equals to (customerId + 1)
        defaultCustomerGroupShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerGroupShouldBeFound(String filter) throws Exception {
        restCustomerGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restCustomerGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerGroupShouldNotBeFound(String filter) throws Exception {
        restCustomerGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerGroup() throws Exception {
        // Get the customerGroup
        restCustomerGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomerGroup() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        int databaseSizeBeforeUpdate = customerGroupRepository.findAll().size();

        // Update the customerGroup
        CustomerGroup updatedCustomerGroup = customerGroupRepository.findById(customerGroup.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerGroup are not directly saved in db
        em.detach(updatedCustomerGroup);
        updatedCustomerGroup.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME);
        CustomerGroupDTO customerGroupDTO = customerGroupMapper.toDto(updatedCustomerGroup);

        restCustomerGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeUpdate);
        CustomerGroup testCustomerGroup = customerGroupList.get(customerGroupList.size() - 1);
        assertThat(testCustomerGroup.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCustomerGroup.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCustomerGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCustomerGroup() throws Exception {
        int databaseSizeBeforeUpdate = customerGroupRepository.findAll().size();
        customerGroup.setId(count.incrementAndGet());

        // Create the CustomerGroup
        CustomerGroupDTO customerGroupDTO = customerGroupMapper.toDto(customerGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerGroup() throws Exception {
        int databaseSizeBeforeUpdate = customerGroupRepository.findAll().size();
        customerGroup.setId(count.incrementAndGet());

        // Create the CustomerGroup
        CustomerGroupDTO customerGroupDTO = customerGroupMapper.toDto(customerGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerGroup() throws Exception {
        int databaseSizeBeforeUpdate = customerGroupRepository.findAll().size();
        customerGroup.setId(count.incrementAndGet());

        // Create the CustomerGroup
        CustomerGroupDTO customerGroupDTO = customerGroupMapper.toDto(customerGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerGroupMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customerGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerGroupWithPatch() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        int databaseSizeBeforeUpdate = customerGroupRepository.findAll().size();

        // Update the customerGroup using partial update
        CustomerGroup partialUpdatedCustomerGroup = new CustomerGroup();
        partialUpdatedCustomerGroup.setId(customerGroup.getId());

        partialUpdatedCustomerGroup.createdat(UPDATED_CREATEDAT);

        restCustomerGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerGroup))
            )
            .andExpect(status().isOk());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeUpdate);
        CustomerGroup testCustomerGroup = customerGroupList.get(customerGroupList.size() - 1);
        assertThat(testCustomerGroup.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCustomerGroup.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testCustomerGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCustomerGroupWithPatch() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        int databaseSizeBeforeUpdate = customerGroupRepository.findAll().size();

        // Update the customerGroup using partial update
        CustomerGroup partialUpdatedCustomerGroup = new CustomerGroup();
        partialUpdatedCustomerGroup.setId(customerGroup.getId());

        partialUpdatedCustomerGroup.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).name(UPDATED_NAME);

        restCustomerGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerGroup))
            )
            .andExpect(status().isOk());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeUpdate);
        CustomerGroup testCustomerGroup = customerGroupList.get(customerGroupList.size() - 1);
        assertThat(testCustomerGroup.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testCustomerGroup.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testCustomerGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerGroup() throws Exception {
        int databaseSizeBeforeUpdate = customerGroupRepository.findAll().size();
        customerGroup.setId(count.incrementAndGet());

        // Create the CustomerGroup
        CustomerGroupDTO customerGroupDTO = customerGroupMapper.toDto(customerGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerGroup() throws Exception {
        int databaseSizeBeforeUpdate = customerGroupRepository.findAll().size();
        customerGroup.setId(count.incrementAndGet());

        // Create the CustomerGroup
        CustomerGroupDTO customerGroupDTO = customerGroupMapper.toDto(customerGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerGroup() throws Exception {
        int databaseSizeBeforeUpdate = customerGroupRepository.findAll().size();
        customerGroup.setId(count.incrementAndGet());

        // Create the CustomerGroup
        CustomerGroupDTO customerGroupDTO = customerGroupMapper.toDto(customerGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerGroup() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        int databaseSizeBeforeDelete = customerGroupRepository.findAll().size();

        // Delete the customerGroup
        restCustomerGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerGroup> customerGroupList = customerGroupRepository.findAll();
        assertThat(customerGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
