package com.smz.gymplus.web.rest;

import static com.smz.gymplus.domain.OrderUnitAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smz.gymplus.IntegrationTest;
import com.smz.gymplus.domain.OrderUnit;
import com.smz.gymplus.repository.OrderUnitRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrderUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderUnitResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Float DEFAULT_UNIT_PRICE = 1F;
    private static final Float UPDATED_UNIT_PRICE = 2F;

    private static final String ENTITY_API_URL = "/api/order-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrderUnitRepository orderUnitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderUnitMockMvc;

    private OrderUnit orderUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderUnit createEntity(EntityManager em) {
        OrderUnit orderUnit = new OrderUnit().quantity(DEFAULT_QUANTITY).unitPrice(DEFAULT_UNIT_PRICE);
        return orderUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderUnit createUpdatedEntity(EntityManager em) {
        OrderUnit orderUnit = new OrderUnit().quantity(UPDATED_QUANTITY).unitPrice(UPDATED_UNIT_PRICE);
        return orderUnit;
    }

    @BeforeEach
    public void initTest() {
        orderUnit = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderUnit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrderUnit
        var returnedOrderUnit = om.readValue(
            restOrderUnitMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderUnit)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrderUnit.class
        );

        // Validate the OrderUnit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOrderUnitUpdatableFieldsEquals(returnedOrderUnit, getPersistedOrderUnit(returnedOrderUnit));
    }

    @Test
    @Transactional
    void createOrderUnitWithExistingId() throws Exception {
        // Create the OrderUnit with an existing ID
        orderUnit.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderUnit)))
            .andExpect(status().isBadRequest());

        // Validate the OrderUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderUnits() throws Exception {
        // Initialize the database
        orderUnitRepository.saveAndFlush(orderUnit);

        // Get all the orderUnitList
        restOrderUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getOrderUnit() throws Exception {
        // Initialize the database
        orderUnitRepository.saveAndFlush(orderUnit);

        // Get the orderUnit
        restOrderUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, orderUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderUnit.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingOrderUnit() throws Exception {
        // Get the orderUnit
        restOrderUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderUnit() throws Exception {
        // Initialize the database
        orderUnitRepository.saveAndFlush(orderUnit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderUnit
        OrderUnit updatedOrderUnit = orderUnitRepository.findById(orderUnit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrderUnit are not directly saved in db
        em.detach(updatedOrderUnit);
        updatedOrderUnit.quantity(UPDATED_QUANTITY).unitPrice(UPDATED_UNIT_PRICE);

        restOrderUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderUnit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOrderUnit))
            )
            .andExpect(status().isOk());

        // Validate the OrderUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrderUnitToMatchAllProperties(updatedOrderUnit);
    }

    @Test
    @Transactional
    void putNonExistingOrderUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderUnit.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderUnit.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderUnit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orderUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderUnit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orderUnit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderUnitWithPatch() throws Exception {
        // Initialize the database
        orderUnitRepository.saveAndFlush(orderUnit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderUnit using partial update
        OrderUnit partialUpdatedOrderUnit = new OrderUnit();
        partialUpdatedOrderUnit.setId(orderUnit.getId());

        partialUpdatedOrderUnit.unitPrice(UPDATED_UNIT_PRICE);

        restOrderUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderUnit))
            )
            .andExpect(status().isOk());

        // Validate the OrderUnit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderUnitUpdatableFieldsEquals(partialUpdatedOrderUnit, getPersistedOrderUnit(orderUnit));
    }

    @Test
    @Transactional
    void fullUpdateOrderUnitWithPatch() throws Exception {
        // Initialize the database
        orderUnitRepository.saveAndFlush(orderUnit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orderUnit using partial update
        OrderUnit partialUpdatedOrderUnit = new OrderUnit();
        partialUpdatedOrderUnit.setId(orderUnit.getId());

        partialUpdatedOrderUnit.quantity(UPDATED_QUANTITY).unitPrice(UPDATED_UNIT_PRICE);

        restOrderUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrderUnit))
            )
            .andExpect(status().isOk());

        // Validate the OrderUnit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrderUnitUpdatableFieldsEquals(partialUpdatedOrderUnit, getPersistedOrderUnit(partialUpdatedOrderUnit));
    }

    @Test
    @Transactional
    void patchNonExistingOrderUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderUnit.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderUnit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orderUnit))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderUnit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        orderUnit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderUnitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orderUnit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderUnit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderUnit() throws Exception {
        // Initialize the database
        orderUnitRepository.saveAndFlush(orderUnit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the orderUnit
        restOrderUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return orderUnitRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected OrderUnit getPersistedOrderUnit(OrderUnit orderUnit) {
        return orderUnitRepository.findById(orderUnit.getId()).orElseThrow();
    }

    protected void assertPersistedOrderUnitToMatchAllProperties(OrderUnit expectedOrderUnit) {
        assertOrderUnitAllPropertiesEquals(expectedOrderUnit, getPersistedOrderUnit(expectedOrderUnit));
    }

    protected void assertPersistedOrderUnitToMatchUpdatableProperties(OrderUnit expectedOrderUnit) {
        assertOrderUnitAllUpdatablePropertiesEquals(expectedOrderUnit, getPersistedOrderUnit(expectedOrderUnit));
    }
}
