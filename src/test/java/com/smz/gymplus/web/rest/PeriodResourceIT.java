package com.smz.gymplus.web.rest;

import static com.smz.gymplus.domain.PeriodAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smz.gymplus.IntegrationTest;
import com.smz.gymplus.domain.Period;
import com.smz.gymplus.repository.PeriodRepository;
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
 * Integration tests for the {@link PeriodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PeriodResourceIT {

    private static final Integer DEFAULT_MONTH_OCCURRENCE = 1;
    private static final Integer UPDATED_MONTH_OCCURRENCE = 2;

    private static final Integer DEFAULT_DAY_OCCURRENCE = 1;
    private static final Integer UPDATED_DAY_OCCURRENCE = 2;

    private static final String ENTITY_API_URL = "/api/periods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeriodMockMvc;

    private Period period;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Period createEntity(EntityManager em) {
        Period period = new Period().monthOccurrence(DEFAULT_MONTH_OCCURRENCE).dayOccurrence(DEFAULT_DAY_OCCURRENCE);
        return period;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Period createUpdatedEntity(EntityManager em) {
        Period period = new Period().monthOccurrence(UPDATED_MONTH_OCCURRENCE).dayOccurrence(UPDATED_DAY_OCCURRENCE);
        return period;
    }

    @BeforeEach
    public void initTest() {
        period = createEntity(em);
    }

    @Test
    @Transactional
    void createPeriod() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Period
        var returnedPeriod = om.readValue(
            restPeriodMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(period)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Period.class
        );

        // Validate the Period in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPeriodUpdatableFieldsEquals(returnedPeriod, getPersistedPeriod(returnedPeriod));
    }

    @Test
    @Transactional
    void createPeriodWithExistingId() throws Exception {
        // Create the Period with an existing ID
        period.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(period)))
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPeriods() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get all the periodList
        restPeriodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(period.getId().intValue())))
            .andExpect(jsonPath("$.[*].monthOccurrence").value(hasItem(DEFAULT_MONTH_OCCURRENCE)))
            .andExpect(jsonPath("$.[*].dayOccurrence").value(hasItem(DEFAULT_DAY_OCCURRENCE)));
    }

    @Test
    @Transactional
    void getPeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        // Get the period
        restPeriodMockMvc
            .perform(get(ENTITY_API_URL_ID, period.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(period.getId().intValue()))
            .andExpect(jsonPath("$.monthOccurrence").value(DEFAULT_MONTH_OCCURRENCE))
            .andExpect(jsonPath("$.dayOccurrence").value(DEFAULT_DAY_OCCURRENCE));
    }

    @Test
    @Transactional
    void getNonExistingPeriod() throws Exception {
        // Get the period
        restPeriodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the period
        Period updatedPeriod = periodRepository.findById(period.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPeriod are not directly saved in db
        em.detach(updatedPeriod);
        updatedPeriod.monthOccurrence(UPDATED_MONTH_OCCURRENCE).dayOccurrence(UPDATED_DAY_OCCURRENCE);

        restPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPeriod.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPeriod))
            )
            .andExpect(status().isOk());

        // Validate the Period in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPeriodToMatchAllProperties(updatedPeriod);
    }

    @Test
    @Transactional
    void putNonExistingPeriod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        period.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(put(ENTITY_API_URL_ID, period.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(period)))
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeriod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        period.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(period))
            )
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeriod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        period.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(period)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Period in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeriodWithPatch() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the period using partial update
        Period partialUpdatedPeriod = new Period();
        partialUpdatedPeriod.setId(period.getId());

        partialUpdatedPeriod.monthOccurrence(UPDATED_MONTH_OCCURRENCE);

        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPeriod))
            )
            .andExpect(status().isOk());

        // Validate the Period in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPeriodUpdatableFieldsEquals(partialUpdatedPeriod, getPersistedPeriod(period));
    }

    @Test
    @Transactional
    void fullUpdatePeriodWithPatch() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the period using partial update
        Period partialUpdatedPeriod = new Period();
        partialUpdatedPeriod.setId(period.getId());

        partialUpdatedPeriod.monthOccurrence(UPDATED_MONTH_OCCURRENCE).dayOccurrence(UPDATED_DAY_OCCURRENCE);

        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeriod.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPeriod))
            )
            .andExpect(status().isOk());

        // Validate the Period in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPeriodUpdatableFieldsEquals(partialUpdatedPeriod, getPersistedPeriod(partialUpdatedPeriod));
    }

    @Test
    @Transactional
    void patchNonExistingPeriod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        period.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, period.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(period))
            )
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeriod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        period.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(period))
            )
            .andExpect(status().isBadRequest());

        // Validate the Period in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeriod() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        period.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeriodMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(period)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Period in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeriod() throws Exception {
        // Initialize the database
        periodRepository.saveAndFlush(period);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the period
        restPeriodMockMvc
            .perform(delete(ENTITY_API_URL_ID, period.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return periodRepository.count();
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

    protected Period getPersistedPeriod(Period period) {
        return periodRepository.findById(period.getId()).orElseThrow();
    }

    protected void assertPersistedPeriodToMatchAllProperties(Period expectedPeriod) {
        assertPeriodAllPropertiesEquals(expectedPeriod, getPersistedPeriod(expectedPeriod));
    }

    protected void assertPersistedPeriodToMatchUpdatableProperties(Period expectedPeriod) {
        assertPeriodAllUpdatablePropertiesEquals(expectedPeriod, getPersistedPeriod(expectedPeriod));
    }
}
