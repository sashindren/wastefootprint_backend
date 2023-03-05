package com.unimap.footprinttest.service.impl;

import com.unimap.footprinttest.domain.ProductionWaste;
import com.unimap.footprinttest.repository.ProductionWasteRepository;
import com.unimap.footprinttest.service.ProductionWasteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductionWaste}.
 */
@Service
@Transactional
public class ProductionWasteServiceImpl implements ProductionWasteService {

    private final Logger log = LoggerFactory.getLogger(ProductionWasteServiceImpl.class);

    private final ProductionWasteRepository productionWasteRepository;

    public ProductionWasteServiceImpl(ProductionWasteRepository productionWasteRepository) {
        this.productionWasteRepository = productionWasteRepository;
    }

    @Override
    public ProductionWaste save(ProductionWaste productionWaste) {
        log.debug("Request to save ProductionWaste : {}", productionWaste);
        return productionWasteRepository.save(productionWaste);
    }

    @Override
    public ProductionWaste update(ProductionWaste productionWaste) {
        log.debug("Request to update ProductionWaste : {}", productionWaste);
        return productionWasteRepository.save(productionWaste);
    }

    @Override
    public Optional<ProductionWaste> partialUpdate(ProductionWaste productionWaste) {
        log.debug("Request to partially update ProductionWaste : {}", productionWaste);

        return productionWasteRepository
            .findById(productionWaste.getId())
            .map(existingProductionWaste -> {
                if (productionWaste.getMaterial() != null) {
                    existingProductionWaste.setMaterial(productionWaste.getMaterial());
                }
                if (productionWaste.getQuantity() != null) {
                    existingProductionWaste.setQuantity(productionWaste.getQuantity());
                }
                if (productionWaste.getTransportType() != null) {
                    existingProductionWaste.setTransportType(productionWaste.getTransportType());
                }
                if (productionWaste.getElectric() != null) {
                    existingProductionWaste.setElectric(productionWaste.getElectric());
                }
                if (productionWaste.getWater() != null) {
                    existingProductionWaste.setWater(productionWaste.getWater());
                }
                if (productionWaste.getWaste() != null) {
                    existingProductionWaste.setWaste(productionWaste.getWaste());
                }

                return existingProductionWaste;
            })
            .map(productionWasteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductionWaste> findAll(Pageable pageable) {
        log.debug("Request to get all ProductionWastes");
        return productionWasteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductionWaste> findOne(Long id) {
        log.debug("Request to get ProductionWaste : {}", id);
        return productionWasteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductionWaste : {}", id);
        productionWasteRepository.deleteById(id);
    }
}
