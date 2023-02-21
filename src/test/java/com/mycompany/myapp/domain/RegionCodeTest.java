package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegionCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegionCode.class);
        RegionCode regionCode1 = new RegionCode();
        regionCode1.setId(1L);
        RegionCode regionCode2 = new RegionCode();
        regionCode2.setId(regionCode1.getId());
        assertThat(regionCode1).isEqualTo(regionCode2);
        regionCode2.setId(2L);
        assertThat(regionCode1).isNotEqualTo(regionCode2);
        regionCode1.setId(null);
        assertThat(regionCode1).isNotEqualTo(regionCode2);
    }
}
