package com.unimap.footprinttest.service;

import com.unimap.footprinttest.domain.*; // for static metamodels
import com.unimap.footprinttest.domain.ProductionWaste;
import com.unimap.footprinttest.repository.ProductionWasteRepository;
import com.unimap.footprinttest.service.criteria.ProductionWasteCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ProductionWaste} entities in the database.
 * The main input is a {@link ProductionWasteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductionWaste} or a {@link Page} of {@link ProductionWaste} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductionWasteQueryService extends QueryService<ProductionWaste> {

    private final Logger log = LoggerFactory.getLogger(ProductionWasteQueryService.class);

    private final ProductionWasteRepository productionWasteRepository;

    public ProductionWasteQueryService(ProductionWasteRepository productionWasteRepository) {
        this.productionWasteRepository = productionWasteRepository;
    }

    /**
     * Return a {@link List} of {@link ProductionWaste} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductionWaste> findByCriteria(ProductionWasteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductionWaste> specification = createSpecification(criteria);
        return productionWasteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ProductionWaste} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductionWaste> findByCriteria(ProductionWasteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductionWaste> specification = createSpecification(criteria);
        return productionWasteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductionWasteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductionWaste> specification = createSpecification(criteria);
        return productionWasteRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductionWasteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductionWaste> createSpecification(ProductionWasteCriteria criteria) {
        Specification<ProductionWaste> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductionWaste_.id));
            }
            if (criteria.getMaterial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMaterial(), ProductionWaste_.material));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), ProductionWaste_.quantity));
            }
            if (criteria.getTransportType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransportType(), ProductionWaste_.transportType));
            }
            if (criteria.getElectric() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getElectric(), ProductionWaste_.electric));
            }
            if (criteria.getWater() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWater(), ProductionWaste_.water));
            }
            if (criteria.getWaste() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWaste(), ProductionWaste_.waste));
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(ProductionWaste_.department, JoinType.LEFT).get(Departments_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
