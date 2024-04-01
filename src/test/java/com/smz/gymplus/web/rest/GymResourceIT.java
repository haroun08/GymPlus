package com.smz.gymplus.web.rest;

import static com.smz.gymplus.domain.GymAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smz.gymplus.IntegrationTest;
import com.smz.gymplus.domain.Gym;
import com.smz.gymplus.repository.GymRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GymResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GymResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE_NUMBER = 1;
    private static final Integer UPDATED_PHONE_NUMBER = 2;

    private static final String ENTITY_API_URL = "/api/gyms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GymRepository gymRepository;

    @Mock
    private GymRepository gymRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGymMockMvc;

    private Gym gym;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gym createEntity(EntityManager em) {
        Gym gym = new Gym()
            .name(DEFAULT_NAME)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .city(DEFAULT_CITY)
            .stateProvince(DEFAULT_STATE_PROVINCE)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return gym;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gym createUpdatedEntity(EntityManager em) {
        Gym gym = new Gym()
            .name(UPDATED_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return gym;
    }

    @BeforeEach
    public void initTest() {
        gym = createEntity(em);
    }

    @Test
    @Transactional
    void createGym() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Gym
        var returnedGym = om.readValue(
            restGymMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gym)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Gym.class
        );

        // Validate the Gym in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertGymUpdatableFieldsEquals(returnedGym, getPersistedGym(returnedGym));
    }

    @Test
    @Transactional
    void createGymWithExistingId() throws Exception {
        // Create the Gym with an existing ID
        gym.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGymMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gym)))
            .andExpect(status().isBadRequest());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGyms() throws Exception {
        // Initialize the database
        gymRepository.saveAndFlush(gym);

        // Get all the gymList
        restGymMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gym.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGymsWithEagerRelationshipsIsEnabled() throws Exception {
        when(gymRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGymMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gymRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGymsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(gymRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGymMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(gymRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGym() throws Exception {
        // Initialize the database
        gymRepository.saveAndFlush(gym);

        // Get the gym
        restGymMockMvc
            .perform(get(ENTITY_API_URL_ID, gym.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gym.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingGym() throws Exception {
        // Get the gym
        restGymMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGym() throws Exception {
        // Initialize the database
        gymRepository.saveAndFlush(gym);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gym
        Gym updatedGym = gymRepository.findById(gym.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGym are not directly saved in db
        em.detach(updatedGym);
        updatedGym
            .name(UPDATED_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restGymMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGym.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedGym))
            )
            .andExpect(status().isOk());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGymToMatchAllProperties(updatedGym);
    }

    @Test
    @Transactional
    void putNonExistingGym() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gym.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGymMockMvc
            .perform(put(ENTITY_API_URL_ID, gym.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gym)))
            .andExpect(status().isBadRequest());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGym() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gym.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGymMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gym))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGym() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gym.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGymMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gym)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGymWithPatch() throws Exception {
        // Initialize the database
        gymRepository.saveAndFlush(gym);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gym using partial update
        Gym partialUpdatedGym = new Gym();
        partialUpdatedGym.setId(gym.getId());

        restGymMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGym.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGym))
            )
            .andExpect(status().isOk());

        // Validate the Gym in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGymUpdatableFieldsEquals(partialUpdatedGym, getPersistedGym(gym));
    }

    @Test
    @Transactional
    void fullUpdateGymWithPatch() throws Exception {
        // Initialize the database
        gymRepository.saveAndFlush(gym);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gym using partial update
        Gym partialUpdatedGym = new Gym();
        partialUpdatedGym.setId(gym.getId());

        partialUpdatedGym
            .name(UPDATED_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restGymMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGym.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGym))
            )
            .andExpect(status().isOk());

        // Validate the Gym in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGymUpdatableFieldsEquals(partialUpdatedGym, getPersistedGym(partialUpdatedGym));
    }

    @Test
    @Transactional
    void patchNonExistingGym() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gym.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGymMockMvc
            .perform(patch(ENTITY_API_URL_ID, gym.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(gym)))
            .andExpect(status().isBadRequest());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGym() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gym.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGymMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(gym))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGym() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gym.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGymMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(gym)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gym in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGym() throws Exception {
        // Initialize the database
        gymRepository.saveAndFlush(gym);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the gym
        restGymMockMvc.perform(delete(ENTITY_API_URL_ID, gym.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return gymRepository.count();
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

    protected Gym getPersistedGym(Gym gym) {
        return gymRepository.findById(gym.getId()).orElseThrow();
    }

    protected void assertPersistedGymToMatchAllProperties(Gym expectedGym) {
        assertGymAllPropertiesEquals(expectedGym, getPersistedGym(expectedGym));
    }

    protected void assertPersistedGymToMatchUpdatableProperties(Gym expectedGym) {
        assertGymAllUpdatablePropertiesEquals(expectedGym, getPersistedGym(expectedGym));
    }
}
