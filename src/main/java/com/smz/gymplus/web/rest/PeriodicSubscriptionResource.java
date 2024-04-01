package com.smz.gymplus.web.rest;

import com.smz.gymplus.domain.PeriodicSubscription;
import com.smz.gymplus.repository.PeriodicSubscriptionRepository;
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
 * REST controller for managing {@link com.smz.gymplus.domain.PeriodicSubscription}.
 */
@RestController
@RequestMapping("/api/periodic-subscriptions")
@Transactional
public class PeriodicSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(PeriodicSubscriptionResource.class);

    private static final String ENTITY_NAME = "periodicSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeriodicSubscriptionRepository periodicSubscriptionRepository;

    public PeriodicSubscriptionResource(PeriodicSubscriptionRepository periodicSubscriptionRepository) {
        this.periodicSubscriptionRepository = periodicSubscriptionRepository;
    }

    /**
     * {@code POST  /periodic-subscriptions} : Create a new periodicSubscription.
     *
     * @param periodicSubscription the periodicSubscription to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new periodicSubscription, or with status {@code 400 (Bad Request)} if the periodicSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PeriodicSubscription> createPeriodicSubscription(@RequestBody PeriodicSubscription periodicSubscription)
        throws URISyntaxException {
        log.debug("REST request to save PeriodicSubscription : {}", periodicSubscription);
        if (periodicSubscription.getId() != null) {
            throw new BadRequestAlertException("A new periodicSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        periodicSubscription = periodicSubscriptionRepository.save(periodicSubscription);
        return ResponseEntity
            .created(new URI("/api/periodic-subscriptions/" + periodicSubscription.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, periodicSubscription.getId().toString()))
            .body(periodicSubscription);
    }

    /**
     * {@code PUT  /periodic-subscriptions/:id} : Updates an existing periodicSubscription.
     *
     * @param id the id of the periodicSubscription to save.
     * @param periodicSubscription the periodicSubscription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodicSubscription,
     * or with status {@code 400 (Bad Request)} if the periodicSubscription is not valid,
     * or with status {@code 500 (Internal Server Error)} if the periodicSubscription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PeriodicSubscription> updatePeriodicSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PeriodicSubscription periodicSubscription
    ) throws URISyntaxException {
        log.debug("REST request to update PeriodicSubscription : {}, {}", id, periodicSubscription);
        if (periodicSubscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periodicSubscription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!periodicSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        periodicSubscription = periodicSubscriptionRepository.save(periodicSubscription);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodicSubscription.getId().toString()))
            .body(periodicSubscription);
    }

    /**
     * {@code PATCH  /periodic-subscriptions/:id} : Partial updates given fields of an existing periodicSubscription, field will ignore if it is null
     *
     * @param id the id of the periodicSubscription to save.
     * @param periodicSubscription the periodicSubscription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodicSubscription,
     * or with status {@code 400 (Bad Request)} if the periodicSubscription is not valid,
     * or with status {@code 404 (Not Found)} if the periodicSubscription is not found,
     * or with status {@code 500 (Internal Server Error)} if the periodicSubscription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PeriodicSubscription> partialUpdatePeriodicSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PeriodicSubscription periodicSubscription
    ) throws URISyntaxException {
        log.debug("REST request to partial update PeriodicSubscription partially : {}, {}", id, periodicSubscription);
        if (periodicSubscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periodicSubscription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!periodicSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PeriodicSubscription> result = periodicSubscriptionRepository
            .findById(periodicSubscription.getId())
            .map(existingPeriodicSubscription -> {
                if (periodicSubscription.getName() != null) {
                    existingPeriodicSubscription.setName(periodicSubscription.getName());
                }
                if (periodicSubscription.getDescription() != null) {
                    existingPeriodicSubscription.setDescription(periodicSubscription.getDescription());
                }
                if (periodicSubscription.getStatus() != null) {
                    existingPeriodicSubscription.setStatus(periodicSubscription.getStatus());
                }
                if (periodicSubscription.getPaymentStatus() != null) {
                    existingPeriodicSubscription.setPaymentStatus(periodicSubscription.getPaymentStatus());
                }

                return existingPeriodicSubscription;
            })
            .map(periodicSubscriptionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodicSubscription.getId().toString())
        );
    }

    /**
     * {@code GET  /periodic-subscriptions} : get all the periodicSubscriptions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of periodicSubscriptions in body.
     */
    @GetMapping("")
    public List<PeriodicSubscription> getAllPeriodicSubscriptions(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all PeriodicSubscriptions");
        if (eagerload) {
            return periodicSubscriptionRepository.findAllWithEagerRelationships();
        } else {
            return periodicSubscriptionRepository.findAll();
        }
    }

    /**
     * {@code GET  /periodic-subscriptions/:id} : get the "id" periodicSubscription.
     *
     * @param id the id of the periodicSubscription to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the periodicSubscription, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PeriodicSubscription> getPeriodicSubscription(@PathVariable("id") Long id) {
        log.debug("REST request to get PeriodicSubscription : {}", id);
        Optional<PeriodicSubscription> periodicSubscription = periodicSubscriptionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(periodicSubscription);
    }

    /**
     * {@code DELETE  /periodic-subscriptions/:id} : delete the "id" periodicSubscription.
     *
     * @param id the id of the periodicSubscription to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePeriodicSubscription(@PathVariable("id") Long id) {
        log.debug("REST request to delete PeriodicSubscription : {}", id);
        periodicSubscriptionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
