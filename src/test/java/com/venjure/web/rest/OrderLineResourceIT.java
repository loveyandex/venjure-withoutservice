package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Asset;
import com.venjure.domain.Jorder;
import com.venjure.domain.OrderItem;
import com.venjure.domain.OrderLine;
import com.venjure.domain.ProductVariant;
import com.venjure.domain.StockMovement;
import com.venjure.domain.TaxCategory;
import com.venjure.repository.OrderLineRepository;
import com.venjure.service.criteria.OrderLineCriteria;
import com.venjure.service.dto.OrderLineDTO;
import com.venjure.service.mapper.OrderLineMapper;
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
 * Integration tests for the {@link OrderLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderLineResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/order-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private OrderLineMapper orderLineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderLineMockMvc;

    private OrderLine orderLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderLine createEntity(EntityManager em) {
        OrderLine orderLine = new OrderLine().createdat(DEFAULT_CREATEDAT).updatedat(DEFAULT_UPDATEDAT);
        return orderLine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderLine createUpdatedEntity(EntityManager em) {
        OrderLine orderLine = new OrderLine().createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT);
        return orderLine;
    }

    @BeforeEach
    public void initTest() {
        orderLine = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderLine() throws Exception {
        int databaseSizeBeforeCreate = orderLineRepository.findAll().size();
        // Create the OrderLine
        OrderLineDTO orderLineDTO = orderLineMapper.toDto(orderLine);
        restOrderLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderLineDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderLine in the database
        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeCreate + 1);
        OrderLine testOrderLine = orderLineList.get(orderLineList.size() - 1);
        assertThat(testOrderLine.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testOrderLine.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
    }

    @Test
    @Transactional
    void createOrderLineWithExistingId() throws Exception {
        // Create the OrderLine with an existing ID
        orderLine.setId(1L);
        OrderLineDTO orderLineDTO = orderLineMapper.toDto(orderLine);

        int databaseSizeBeforeCreate = orderLineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderLineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderLine in the database
        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderLineRepository.findAll().size();
        // set the field null
        orderLine.setCreatedat(null);

        // Create the OrderLine, which fails.
        OrderLineDTO orderLineDTO = orderLineMapper.toDto(orderLine);

        restOrderLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderLineDTO)))
            .andExpect(status().isBadRequest());

        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderLineRepository.findAll().size();
        // set the field null
        orderLine.setUpdatedat(null);

        // Create the OrderLine, which fails.
        OrderLineDTO orderLineDTO = orderLineMapper.toDto(orderLine);

        restOrderLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderLineDTO)))
            .andExpect(status().isBadRequest());

        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrderLines() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        // Get all the orderLineList
        restOrderLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())));
    }

    @Test
    @Transactional
    void getOrderLine() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        // Get the orderLine
        restOrderLineMockMvc
            .perform(get(ENTITY_API_URL_ID, orderLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderLine.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()));
    }

    @Test
    @Transactional
    void getOrderLinesByIdFiltering() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        Long id = orderLine.getId();

        defaultOrderLineShouldBeFound("id.equals=" + id);
        defaultOrderLineShouldNotBeFound("id.notEquals=" + id);

        defaultOrderLineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderLineShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderLineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderLineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrderLinesByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        // Get all the orderLineList where createdat equals to DEFAULT_CREATEDAT
        defaultOrderLineShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the orderLineList where createdat equals to UPDATED_CREATEDAT
        defaultOrderLineShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderLinesByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        // Get all the orderLineList where createdat not equals to DEFAULT_CREATEDAT
        defaultOrderLineShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the orderLineList where createdat not equals to UPDATED_CREATEDAT
        defaultOrderLineShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderLinesByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        // Get all the orderLineList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultOrderLineShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the orderLineList where createdat equals to UPDATED_CREATEDAT
        defaultOrderLineShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderLinesByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        // Get all the orderLineList where createdat is not null
        defaultOrderLineShouldBeFound("createdat.specified=true");

        // Get all the orderLineList where createdat is null
        defaultOrderLineShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderLinesByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        // Get all the orderLineList where updatedat equals to DEFAULT_UPDATEDAT
        defaultOrderLineShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the orderLineList where updatedat equals to UPDATED_UPDATEDAT
        defaultOrderLineShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderLinesByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        // Get all the orderLineList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultOrderLineShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the orderLineList where updatedat not equals to UPDATED_UPDATEDAT
        defaultOrderLineShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderLinesByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        // Get all the orderLineList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultOrderLineShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the orderLineList where updatedat equals to UPDATED_UPDATEDAT
        defaultOrderLineShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllOrderLinesByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        // Get all the orderLineList where updatedat is not null
        defaultOrderLineShouldBeFound("updatedat.specified=true");

        // Get all the orderLineList where updatedat is null
        defaultOrderLineShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllOrderLinesByProductvariantIsEqualToSomething() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);
        ProductVariant productvariant = ProductVariantResourceIT.createEntity(em);
        em.persist(productvariant);
        em.flush();
        orderLine.setProductvariant(productvariant);
        orderLineRepository.saveAndFlush(orderLine);
        Long productvariantId = productvariant.getId();

        // Get all the orderLineList where productvariant equals to productvariantId
        defaultOrderLineShouldBeFound("productvariantId.equals=" + productvariantId);

        // Get all the orderLineList where productvariant equals to (productvariantId + 1)
        defaultOrderLineShouldNotBeFound("productvariantId.equals=" + (productvariantId + 1));
    }

    @Test
    @Transactional
    void getAllOrderLinesByTaxcategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);
        TaxCategory taxcategory = TaxCategoryResourceIT.createEntity(em);
        em.persist(taxcategory);
        em.flush();
        orderLine.setTaxcategory(taxcategory);
        orderLineRepository.saveAndFlush(orderLine);
        Long taxcategoryId = taxcategory.getId();

        // Get all the orderLineList where taxcategory equals to taxcategoryId
        defaultOrderLineShouldBeFound("taxcategoryId.equals=" + taxcategoryId);

        // Get all the orderLineList where taxcategory equals to (taxcategoryId + 1)
        defaultOrderLineShouldNotBeFound("taxcategoryId.equals=" + (taxcategoryId + 1));
    }

    @Test
    @Transactional
    void getAllOrderLinesByFeaturedAssetIsEqualToSomething() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);
        Asset featuredAsset = AssetResourceIT.createEntity(em);
        em.persist(featuredAsset);
        em.flush();
        orderLine.setFeaturedAsset(featuredAsset);
        orderLineRepository.saveAndFlush(orderLine);
        Long featuredAssetId = featuredAsset.getId();

        // Get all the orderLineList where featuredAsset equals to featuredAssetId
        defaultOrderLineShouldBeFound("featuredAssetId.equals=" + featuredAssetId);

        // Get all the orderLineList where featuredAsset equals to (featuredAssetId + 1)
        defaultOrderLineShouldNotBeFound("featuredAssetId.equals=" + (featuredAssetId + 1));
    }

    @Test
    @Transactional
    void getAllOrderLinesByJorderIsEqualToSomething() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);
        Jorder jorder = JorderResourceIT.createEntity(em);
        em.persist(jorder);
        em.flush();
        orderLine.setJorder(jorder);
        orderLineRepository.saveAndFlush(orderLine);
        Long jorderId = jorder.getId();

        // Get all the orderLineList where jorder equals to jorderId
        defaultOrderLineShouldBeFound("jorderId.equals=" + jorderId);

        // Get all the orderLineList where jorder equals to (jorderId + 1)
        defaultOrderLineShouldNotBeFound("jorderId.equals=" + (jorderId + 1));
    }

    @Test
    @Transactional
    void getAllOrderLinesByOrderItemIsEqualToSomething() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);
        OrderItem orderItem = OrderItemResourceIT.createEntity(em);
        em.persist(orderItem);
        em.flush();
        orderLine.addOrderItem(orderItem);
        orderLineRepository.saveAndFlush(orderLine);
        Long orderItemId = orderItem.getId();

        // Get all the orderLineList where orderItem equals to orderItemId
        defaultOrderLineShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the orderLineList where orderItem equals to (orderItemId + 1)
        defaultOrderLineShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    @Test
    @Transactional
    void getAllOrderLinesByStockMovementIsEqualToSomething() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);
        StockMovement stockMovement = StockMovementResourceIT.createEntity(em);
        em.persist(stockMovement);
        em.flush();
        orderLine.addStockMovement(stockMovement);
        orderLineRepository.saveAndFlush(orderLine);
        Long stockMovementId = stockMovement.getId();

        // Get all the orderLineList where stockMovement equals to stockMovementId
        defaultOrderLineShouldBeFound("stockMovementId.equals=" + stockMovementId);

        // Get all the orderLineList where stockMovement equals to (stockMovementId + 1)
        defaultOrderLineShouldNotBeFound("stockMovementId.equals=" + (stockMovementId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderLineShouldBeFound(String filter) throws Exception {
        restOrderLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())));

        // Check, that the count call also returns 1
        restOrderLineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderLineShouldNotBeFound(String filter) throws Exception {
        restOrderLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderLineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrderLine() throws Exception {
        // Get the orderLine
        restOrderLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderLine() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        int databaseSizeBeforeUpdate = orderLineRepository.findAll().size();

        // Update the orderLine
        OrderLine updatedOrderLine = orderLineRepository.findById(orderLine.getId()).get();
        // Disconnect from session so that the updates on updatedOrderLine are not directly saved in db
        em.detach(updatedOrderLine);
        updatedOrderLine.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT);
        OrderLineDTO orderLineDTO = orderLineMapper.toDto(updatedOrderLine);

        restOrderLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderLineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderLineDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderLine in the database
        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeUpdate);
        OrderLine testOrderLine = orderLineList.get(orderLineList.size() - 1);
        assertThat(testOrderLine.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testOrderLine.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void putNonExistingOrderLine() throws Exception {
        int databaseSizeBeforeUpdate = orderLineRepository.findAll().size();
        orderLine.setId(count.incrementAndGet());

        // Create the OrderLine
        OrderLineDTO orderLineDTO = orderLineMapper.toDto(orderLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderLineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderLine in the database
        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderLine() throws Exception {
        int databaseSizeBeforeUpdate = orderLineRepository.findAll().size();
        orderLine.setId(count.incrementAndGet());

        // Create the OrderLine
        OrderLineDTO orderLineDTO = orderLineMapper.toDto(orderLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderLine in the database
        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderLine() throws Exception {
        int databaseSizeBeforeUpdate = orderLineRepository.findAll().size();
        orderLine.setId(count.incrementAndGet());

        // Create the OrderLine
        OrderLineDTO orderLineDTO = orderLineMapper.toDto(orderLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderLineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderLineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderLine in the database
        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderLineWithPatch() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        int databaseSizeBeforeUpdate = orderLineRepository.findAll().size();

        // Update the orderLine using partial update
        OrderLine partialUpdatedOrderLine = new OrderLine();
        partialUpdatedOrderLine.setId(orderLine.getId());

        restOrderLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderLine))
            )
            .andExpect(status().isOk());

        // Validate the OrderLine in the database
        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeUpdate);
        OrderLine testOrderLine = orderLineList.get(orderLineList.size() - 1);
        assertThat(testOrderLine.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testOrderLine.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
    }

    @Test
    @Transactional
    void fullUpdateOrderLineWithPatch() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        int databaseSizeBeforeUpdate = orderLineRepository.findAll().size();

        // Update the orderLine using partial update
        OrderLine partialUpdatedOrderLine = new OrderLine();
        partialUpdatedOrderLine.setId(orderLine.getId());

        partialUpdatedOrderLine.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT);

        restOrderLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderLine))
            )
            .andExpect(status().isOk());

        // Validate the OrderLine in the database
        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeUpdate);
        OrderLine testOrderLine = orderLineList.get(orderLineList.size() - 1);
        assertThat(testOrderLine.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testOrderLine.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void patchNonExistingOrderLine() throws Exception {
        int databaseSizeBeforeUpdate = orderLineRepository.findAll().size();
        orderLine.setId(count.incrementAndGet());

        // Create the OrderLine
        OrderLineDTO orderLineDTO = orderLineMapper.toDto(orderLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderLineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderLine in the database
        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderLine() throws Exception {
        int databaseSizeBeforeUpdate = orderLineRepository.findAll().size();
        orderLine.setId(count.incrementAndGet());

        // Create the OrderLine
        OrderLineDTO orderLineDTO = orderLineMapper.toDto(orderLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderLine in the database
        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderLine() throws Exception {
        int databaseSizeBeforeUpdate = orderLineRepository.findAll().size();
        orderLine.setId(count.incrementAndGet());

        // Create the OrderLine
        OrderLineDTO orderLineDTO = orderLineMapper.toDto(orderLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderLineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderLineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderLine in the database
        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderLine() throws Exception {
        // Initialize the database
        orderLineRepository.saveAndFlush(orderLine);

        int databaseSizeBeforeDelete = orderLineRepository.findAll().size();

        // Delete the orderLine
        restOrderLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderLine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderLine> orderLineList = orderLineRepository.findAll();
        assertThat(orderLineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
