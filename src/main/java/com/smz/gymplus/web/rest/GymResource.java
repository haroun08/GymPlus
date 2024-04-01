package com.smz.gymplus.web.rest;

import com.smz.gymplus.domain.Gym;
import com.smz.gymplus.repository.GymRepository;
import com.smz.gymplus.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.smz.gymplus.domain.Gym}.
 */
@RestController
@RequestMapping("/api/gyms")
@Transactional
public class GymResource {

    private final Logger log = LoggerFactory.getLogger(GymResource.class);

    private static final String ENTITY_NAME = "gym";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GymRepository gymRepository;

    public GymResource(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    /**
     * {@code POST  /gyms} : Create a new gym.
     *
     * @param gym the gym to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gym, or with status {@code 400 (Bad Request)} if the gym has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Gym> createGym(@RequestBody Gym gym) throws URISyntaxException {
        log.debug("REST request to save Gym : {}", gym);
        if (gym.getId() != null) {
            throw new BadRequestAlertException("A new gym cannot already have an ID", ENTITY_NAME, "idexists");
        }
        gym = gymRepository.save(gym);
        return ResponseEntity.created(new URI("/api/gyms/" + gym.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, gym.getId().toString()))
            .body(gym);
    }

    /**
     * {@code PUT  /gyms/:id} : Updates an existing gym.
     *
     * @param id the id of the gym to save.
     * @param gym the gym to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gym,
     * or with status {@code 400 (Bad Request)} if the gym is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gym couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Gym> updateGym(@PathVariable(value = "id", required = false) final Long id, @RequestBody Gym gym)
        throws URISyntaxException {
        log.debug("REST request to update Gym : {}, {}", id, gym);
        if (gym.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gym.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gymRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        gym = gymRepository.save(gym);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gym.getId().toString()))
            .body(gym);
    }

    /**
     * {@code PATCH  /gyms/:id} : Partial updates given fields of an existing gym, field will ignore if it is null
     *
     * @param id the id of the gym to save.
     * @param gym the gym to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gym,
     * or with status {@code 400 (Bad Request)} if the gym is not valid,
     * or with status {@code 404 (Not Found)} if the gym is not found,
     * or with status {@code 500 (Internal Server Error)} if the gym couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Gym> partialUpdateGym(@PathVariable(value = "id", required = false) final Long id, @RequestBody Gym gym)
        throws URISyntaxException {
        log.debug("REST request to partial update Gym partially : {}, {}", id, gym);
        if (gym.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gym.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gymRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Gym> result = gymRepository
            .findById(gym.getId())
            .map(existingGym -> {
                if (gym.getName() != null) {
                    existingGym.setName(gym.getName());
                }
                if (gym.getStreetAddress() != null) {
                    existingGym.setStreetAddress(gym.getStreetAddress());
                }
                if (gym.getPostalCode() != null) {
                    existingGym.setPostalCode(gym.getPostalCode());
                }
                if (gym.getCity() != null) {
                    existingGym.setCity(gym.getCity());
                }
                if (gym.getStateProvince() != null) {
                    existingGym.setStateProvince(gym.getStateProvince());
                }
                if (gym.getPhoneNumber() != null) {
                    existingGym.setPhoneNumber(gym.getPhoneNumber());
                }

                return existingGym;
            })
            .map(gymRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gym.getId().toString())
        );
    }

    /**
     * {@code GET  /gyms} : get all the gyms.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gyms in body.
     */
    @GetMapping("")
    public List<Gym> getAllGyms(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Gyms");
        if (eagerload) {
            return gymRepository.findAllWithEagerRelationships();
        } else {
            return gymRepository.findAll();
        }
    }

    /**
     * {@code GET  /gyms/:id} : get the "id" gym.
     *
     * @param id the id of the gym to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gym, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Gym> getGym(@PathVariable("id") Long id) {
        log.debug("REST request to get Gym : {}", id);
        Optional<Gym> gym = gymRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(gym);
    }

    /**
     * {@code DELETE  /gyms/:id} : delete the "id" gym.
     *
     * @param id the id of the gym to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGym(@PathVariable("id") Long id) {
        log.debug("REST request to delete Gym : {}", id);
        gymRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
