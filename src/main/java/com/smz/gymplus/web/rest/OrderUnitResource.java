package com.smz.gymplus.web.rest;

import com.smz.gymplus.domain.OrderUnit;
import com.smz.gymplus.repository.OrderUnitRepository;
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
 * REST controller for managing {@link com.smz.gymplus.domain.OrderUnit}.
 */
@RestController
@RequestMapping("/api/order-units")
@Transactional
public class OrderUnitResource {

    private final Logger log = LoggerFactory.getLogger(OrderUnitResource.class);

    private static final String ENTITY_NAME = "orderUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderUnitRepository orderUnitRepository;

    public OrderUnitResource(OrderUnitRepository orderUnitRepository) {
        this.orderUnitRepository = orderUnitRepository;
    }

    /**
     * {@code POST  /order-units} : Create a new orderUnit.
     *
     * @param orderUnit the orderUnit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderUnit, or with status {@code 400 (Bad Request)} if the orderUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderUnit> createOrderUnit(@RequestBody OrderUnit orderUnit) throws URISyntaxException {
        log.debug("REST request to save OrderUnit : {}", orderUnit);
        if (orderUnit.getId() != null) {
            throw new BadRequestAlertException("A new orderUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderUnit = orderUnitRepository.save(orderUnit);
        return ResponseEntity
            .created(new URI("/api/order-units/" + orderUnit.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderUnit.getId().toString()))
            .body(orderUnit);
    }

    /**
     * {@code PUT  /order-units/:id} : Updates an existing orderUnit.
     *
     * @param id the id of the orderUnit to save.
     * @param orderUnit the orderUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderUnit,
     * or with status {@code 400 (Bad Request)} if the orderUnit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderUnit> updateOrderUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderUnit orderUnit
    ) throws URISyntaxException {
        log.debug("REST request to update OrderUnit : {}, {}", id, orderUnit);
        if (orderUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderUnit = orderUnitRepository.save(orderUnit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderUnit.getId().toString()))
            .body(orderUnit);
    }

    /**
     * {@code PATCH  /order-units/:id} : Partial updates given fields of an existing orderUnit, field will ignore if it is null
     *
     * @param id the id of the orderUnit to save.
     * @param orderUnit the orderUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderUnit,
     * or with status {@code 400 (Bad Request)} if the orderUnit is not valid,
     * or with status {@code 404 (Not Found)} if the orderUnit is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderUnit> partialUpdateOrderUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderUnit orderUnit
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderUnit partially : {}, {}", id, orderUnit);
        if (orderUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderUnit> result = orderUnitRepository
            .findById(orderUnit.getId())
            .map(existingOrderUnit -> {
                if (orderUnit.getQuantity() != null) {
                    existingOrderUnit.setQuantity(orderUnit.getQuantity());
                }
                if (orderUnit.getUnitPrice() != null) {
                    existingOrderUnit.setUnitPrice(orderUnit.getUnitPrice());
                }

                return existingOrderUnit;
            })
            .map(orderUnitRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderUnit.getId().toString())
        );
    }

    /**
     * {@code GET  /order-units} : get all the orderUnits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderUnits in body.
     */
    @GetMapping("")
    public List<OrderUnit> getAllOrderUnits() {
        log.debug("REST request to get all OrderUnits");
        return orderUnitRepository.findAll();
    }

    /**
     * {@code GET  /order-units/:id} : get the "id" orderUnit.
     *
     * @param id the id of the orderUnit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderUnit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderUnit> getOrderUnit(@PathVariable("id") Long id) {
        log.debug("REST request to get OrderUnit : {}", id);
        Optional<OrderUnit> orderUnit = orderUnitRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orderUnit);
    }

    /**
     * {@code DELETE  /order-units/:id} : delete the "id" orderUnit.
     *
     * @param id the id of the orderUnit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderUnit(@PathVariable("id") Long id) {
        log.debug("REST request to delete OrderUnit : {}", id);
        orderUnitRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
