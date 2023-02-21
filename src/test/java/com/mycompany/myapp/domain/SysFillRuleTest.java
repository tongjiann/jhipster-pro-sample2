package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SysFillRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysFillRule.class);
        SysFillRule sysFillRule1 = new SysFillRule();
        sysFillRule1.setId(1L);
        SysFillRule sysFillRule2 = new SysFillRule();
        sysFillRule2.setId(sysFillRule1.getId());
        assertThat(sysFillRule1).isEqualTo(sysFillRule2);
        sysFillRule2.setId(2L);
        assertThat(sysFillRule1).isNotEqualTo(sysFillRule2);
        sysFillRule1.setId(null);
        assertThat(sysFillRule1).isNotEqualTo(sysFillRule2);
    }
}
