package com.smz.gymplus.web.rest;

import com.smz.gymplus.domain.ProductHistory;
import com.smz.gymplus.repository.ProductHistoryRepository;
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
 * REST controller for managing {@link com.smz.gymplus.domain.ProductHistory}.
 */
@RestController
@RequestMapping("/api/product-histories")
@Transactional
public class ProductHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ProductHistoryResource.class);

    private static final String ENTITY_NAME = "productHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductHistoryRepository productHistoryRepository;

    public ProductHistoryResource(ProductHistoryRepository productHistoryRepository) {
        this.productHistoryRepository = productHistoryRepository;
    }

    /**
     * {@code POST  /product-histories} : Create a new productHistory.
     *
     * @param productHistory the productHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productHistory, or with status {@code 400 (Bad Request)} if the productHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductHistory> createProductHistory(@RequestBody ProductHistory productHistory) throws URISyntaxException {
        log.debug("REST request to save ProductHistory : {}", productHistory);
        if (productHistory.getId() != null) {
            throw new BadRequestAlertException("A new productHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productHistory = productHistoryRepository.save(productHistory);
        return ResponseEntity
            .created(new URI("/api/product-histories/" + productHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productHistory.getId().toString()))
            .body(productHistory);
    }

    /**
     * {@code PUT  /product-histories/:id} : Updates an existing productHistory.
     *
     * @param id the id of the productHistory to save.
     * @param productHistory the productHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productHistory,
     * or with status {@code 400 (Bad Request)} if the productHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductHistory> updateProductHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductHistory productHistory
    ) throws URISyntaxException {
        log.debug("REST request to update ProductHistory : {}, {}", id, productHistory);
        if (productHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productHistory = productHistoryRepository.save(productHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productHistory.getId().toString()))
            .body(productHistory);
    }

    /**
     * {@code PATCH  /product-histories/:id} : Partial updates given fields of an existing productHistory, field will ignore if it is null
     *
     * @param id the id of the productHistory to save.
     * @param productHistory the productHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productHistory,
     * or with status {@code 400 (Bad Request)} if the productHistory is not valid,
     * or with status {@code 404 (Not Found)} if the productHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the productHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductHistory> partialUpdateProductHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductHistory productHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductHistory partially : {}, {}", id, productHistory);
        if (productHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductHistory> result = productHistoryRepository
            .findById(productHistory.getId())
            .map(existingProductHistory -> {
                if (productHistory.getName() != null) {
                    existingProductHistory.setName(productHistory.getName());
                }
                if (productHistory.getDescription() != null) {
                    existingProductHistory.setDescription(productHistory.getDescription());
                }
                if (productHistory.getPrice() != null) {
                    existingProductHistory.setPrice(productHistory.getPrice());
                }
                if (productHistory.getAvailableStockQuantity() != null) {
                    existingProductHistory.setAvailableStockQuantity(productHistory.getAvailableStockQuantity());
                }
                if (productHistory.getValidFrom() != null) {
                    existingProductHistory.setValidFrom(productHistory.getValidFrom());
                }
                if (productHistory.getValidTo() != null) {
                    existingProductHistory.setValidTo(productHistory.getValidTo());
                }

                return existingProductHistory;
            })
            .map(productHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /product-histories} : get all the productHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productHistories in body.
     */
    @GetMapping("")
    public List<ProductHistory> getAllProductHistories() {
        log.debug("REST request to get all ProductHistories");
        return productHistoryRepository.findAll();
    }

    /**
     * {@code GET  /product-histories/:id} : get the "id" productHistory.
     *
     * @param id the id of the productHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductHistory> getProductHistory(@PathVariable("id") Long id) {
        log.debug("REST request to get ProductHistory : {}", id);
        Optional<ProductHistory> productHistory = productHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productHistory);
    }

    /**
     * {@code DELETE  /product-histories/:id} : delete the "id" productHistory.
     *
     * @param id the id of the productHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductHistory(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProductHistory : {}", id);
        productHistoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
