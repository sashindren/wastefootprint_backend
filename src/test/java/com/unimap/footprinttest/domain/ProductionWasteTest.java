package com.unimap.footprinttest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.unimap.footprinttest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductionWasteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductionWaste.class);
        ProductionWaste productionWaste1 = new ProductionWaste();
        productionWaste1.setId(1L);
        ProductionWaste productionWaste2 = new ProductionWaste();
        productionWaste2.setId(productionWaste1.getId());
        assertThat(productionWaste1).isEqualTo(productionWaste2);
        productionWaste2.setId(2L);
        assertThat(productionWaste1).isNotEqualTo(productionWaste2);
        productionWaste1.setId(null);
        assertThat(productionWaste1).isNotEqualTo(productionWaste2);
    }
}
