package com.smz.gymplus.web.rest;

import static com.smz.gymplus.domain.ProductHistoryAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smz.gymplus.IntegrationTest;
import com.smz.gymplus.domain.ProductHistory;
import com.smz.gymplus.repository.ProductHistoryRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ProductHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductHistoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final Long DEFAULT_AVAILABLE_STOCK_QUANTITY = 1L;
    private static final Long UPDATED_AVAILABLE_STOCK_QUANTITY = 2L;

    private static final Instant DEFAULT_VALID_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALID_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/product-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductHistoryMockMvc;

    private ProductHistory productHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductHistory createEntity(EntityManager em) {
        ProductHistory productHistory = new ProductHistory()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .availableStockQuantity(DEFAULT_AVAILABLE_STOCK_QUANTITY)
            .validFrom(DEFAULT_VALID_FROM)
            .validTo(DEFAULT_VALID_TO);
        return productHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductHistory createUpdatedEntity(EntityManager em) {
        ProductHistory productHistory = new ProductHistory()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .availableStockQuantity(UPDATED_AVAILABLE_STOCK_QUANTITY)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);
        return productHistory;
    }

    @BeforeEach
    public void initTest() {
        productHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createProductHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductHistory
        var returnedProductHistory = om.readValue(
            restProductHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productHistory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductHistory.class
        );

        // Validate the ProductHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductHistoryUpdatableFieldsEquals(returnedProductHistory, getPersistedProductHistory(returnedProductHistory));
    }

    @Test
    @Transactional
    void createProductHistoryWithExistingId() throws Exception {
        // Create the ProductHistory with an existing ID
        productHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productHistory)))
            .andExpect(status().isBadRequest());

        // Validate the ProductHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductHistories() throws Exception {
        // Initialize the database
        productHistoryRepository.saveAndFlush(productHistory);

        // Get all the productHistoryList
        restProductHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].availableStockQuantity").value(hasItem(DEFAULT_AVAILABLE_STOCK_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO.toString())));
    }

    @Test
    @Transactional
    void getProductHistory() throws Exception {
        // Initialize the database
        productHistoryRepository.saveAndFlush(productHistory);

        // Get the productHistory
        restProductHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, productHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productHistory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.availableStockQuantity").value(DEFAULT_AVAILABLE_STOCK_QUANTITY.intValue()))
            .andExpect(jsonPath("$.validFrom").value(DEFAULT_VALID_FROM.toString()))
            .andExpect(jsonPath("$.validTo").value(DEFAULT_VALID_TO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductHistory() throws Exception {
        // Get the productHistory
        restProductHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductHistory() throws Exception {
        // Initialize the database
        productHistoryRepository.saveAndFlush(productHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productHistory
        ProductHistory updatedProductHistory = productHistoryRepository.findById(productHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductHistory are not directly saved in db
        em.detach(updatedProductHistory);
        updatedProductHistory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .availableStockQuantity(UPDATED_AVAILABLE_STOCK_QUANTITY)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);

        restProductHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductHistory))
            )
            .andExpect(status().isOk());

        // Validate the ProductHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductHistoryToMatchAllProperties(updatedProductHistory);
    }

    @Test
    @Transactional
    void putNonExistingProductHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductHistoryWithPatch() throws Exception {
        // Initialize the database
        productHistoryRepository.saveAndFlush(productHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productHistory using partial update
        ProductHistory partialUpdatedProductHistory = new ProductHistory();
        partialUpdatedProductHistory.setId(productHistory.getId());

        partialUpdatedProductHistory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .availableStockQuantity(UPDATED_AVAILABLE_STOCK_QUANTITY)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);

        restProductHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductHistory))
            )
            .andExpect(status().isOk());

        // Validate the ProductHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductHistoryUpdatableFieldsEquals(partialUpdatedProductHistory, getPersistedProductHistory(productHistory));
    }

    @Test
    @Transactional
    void fullUpdateProductHistoryWithPatch() throws Exception {
        // Initialize the database
        productHistoryRepository.saveAndFlush(productHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productHistory using partial update
        ProductHistory partialUpdatedProductHistory = new ProductHistory();
        partialUpdatedProductHistory.setId(productHistory.getId());

        partialUpdatedProductHistory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .availableStockQuantity(UPDATED_AVAILABLE_STOCK_QUANTITY)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO);

        restProductHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductHistory))
            )
            .andExpect(status().isOk());

        // Validate the ProductHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductHistoryUpdatableFieldsEquals(partialUpdatedProductHistory, getPersistedProductHistory(partialUpdatedProductHistory));
    }

    @Test
    @Transactional
    void patchNonExistingProductHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductHistory() throws Exception {
        // Initialize the database
        productHistoryRepository.saveAndFlush(productHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productHistory
        restProductHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, productHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productHistoryRepository.count();
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

    protected ProductHistory getPersistedProductHistory(ProductHistory productHistory) {
        return productHistoryRepository.findById(productHistory.getId()).orElseThrow();
    }

    protected void assertPersistedProductHistoryToMatchAllProperties(ProductHistory expectedProductHistory) {
        assertProductHistoryAllPropertiesEquals(expectedProductHistory, getPersistedProductHistory(expectedProductHistory));
    }

    protected void assertPersistedProductHistoryToMatchUpdatableProperties(ProductHistory expectedProductHistory) {
        assertProductHistoryAllUpdatablePropertiesEquals(expectedProductHistory, getPersistedProductHistory(expectedProductHistory));
    }
}
