package com.unimap.footprinttest.web.rest;

import com.unimap.footprinttest.domain.ProductionWaste;
import com.unimap.footprinttest.repository.ProductionWasteRepository;
import com.unimap.footprinttest.service.ProductionWasteQueryService;
import com.unimap.footprinttest.service.ProductionWasteService;
import com.unimap.footprinttest.service.criteria.ProductionWasteCriteria;
import com.unimap.footprinttest.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.unimap.footprinttest.domain.ProductionWaste}.
 */
@RestController
@RequestMapping("/api")
public class ProductionWasteResource {

    private final Logger log = LoggerFactory.getLogger(ProductionWasteResource.class);

    private static final String ENTITY_NAME = "productionWaste";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductionWasteService productionWasteService;

    private final ProductionWasteRepository productionWasteRepository;

    private final ProductionWasteQueryService productionWasteQueryService;

    public ProductionWasteResource(
        ProductionWasteService productionWasteService,
        ProductionWasteRepository productionWasteRepository,
        ProductionWasteQueryService productionWasteQueryService
    ) {
        this.productionWasteService = productionWasteService;
        this.productionWasteRepository = productionWasteRepository;
        this.productionWasteQueryService = productionWasteQueryService;
    }

    /**
     * {@code POST  /production-wastes} : Create a new productionWaste.
     *
     * @param productionWaste the productionWaste to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productionWaste, or with status {@code 400 (Bad Request)} if the productionWaste has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/production-wastes")
    public ResponseEntity<ProductionWaste> createProductionWaste(@RequestBody ProductionWaste productionWaste) throws URISyntaxException {
        log.debug("REST request to save ProductionWaste : {}", productionWaste);
        if (productionWaste.getId() != null) {
            throw new BadRequestAlertException("A new productionWaste cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductionWaste result = productionWasteService.save(productionWaste);
        return ResponseEntity
            .created(new URI("/api/production-wastes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /production-wastes/:id} : Updates an existing productionWaste.
     *
     * @param id the id of the productionWaste to save.
     * @param productionWaste the productionWaste to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productionWaste,
     * or with status {@code 400 (Bad Request)} if the productionWaste is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productionWaste couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/production-wastes/{id}")
    public ResponseEntity<ProductionWaste> updateProductionWaste(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductionWaste productionWaste
    ) throws URISyntaxException {
        log.debug("REST request to update ProductionWaste : {}, {}", id, productionWaste);
        if (productionWaste.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productionWaste.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productionWasteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductionWaste result = productionWasteService.update(productionWaste);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productionWaste.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /production-wastes/:id} : Partial updates given fields of an existing productionWaste, field will ignore if it is null
     *
     * @param id the id of the productionWaste to save.
     * @param productionWaste the productionWaste to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productionWaste,
     * or with status {@code 400 (Bad Request)} if the productionWaste is not valid,
     * or with status {@code 404 (Not Found)} if the productionWaste is not found,
     * or with status {@code 500 (Internal Server Error)} if the productionWaste couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/production-wastes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductionWaste> partialUpdateProductionWaste(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductionWaste productionWaste
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductionWaste partially : {}, {}", id, productionWaste);
        if (productionWaste.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productionWaste.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productionWasteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductionWaste> result = productionWasteService.partialUpdate(productionWaste);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productionWaste.getId().toString())
        );
    }

    /**
     * {@code GET  /production-wastes} : get all the productionWastes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productionWastes in body.
     */
    @GetMapping("/production-wastes")
    public ResponseEntity<List<ProductionWaste>> getAllProductionWastes(
        ProductionWasteCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ProductionWastes by criteria: {}", criteria);
        Page<ProductionWaste> page = productionWasteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /production-wastes/count} : count all the productionWastes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/production-wastes/count")
    public ResponseEntity<Long> countProductionWastes(ProductionWasteCriteria criteria) {
        log.debug("REST request to count ProductionWastes by criteria: {}", criteria);
        return ResponseEntity.ok().body(productionWasteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /production-wastes/:id} : get the "id" productionWaste.
     *
     * @param id the id of the productionWaste to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productionWaste, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/production-wastes/{id}")
    public ResponseEntity<ProductionWaste> getProductionWaste(@PathVariable Long id) {
        log.debug("REST request to get ProductionWaste : {}", id);
        Optional<ProductionWaste> productionWaste = productionWasteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productionWaste);
    }

    /**
     * {@code DELETE  /production-wastes/:id} : delete the "id" productionWaste.
     *
     * @param id the id of the productionWaste to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/production-wastes/{id}")
    public ResponseEntity<Void> deleteProductionWaste(@PathVariable Long id) {
        log.debug("REST request to delete ProductionWaste : {}", id);
        productionWasteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
