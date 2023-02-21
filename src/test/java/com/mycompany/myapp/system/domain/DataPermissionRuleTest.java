package com.mycompany.myapp.system.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DataPermissionRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataPermissionRule.class);
        DataPermissionRule dataPermissionRule1 = new DataPermissionRule();
        dataPermissionRule1.setId(1L);
        DataPermissionRule dataPermissionRule2 = new DataPermissionRule();
        dataPermissionRule2.setId(dataPermissionRule1.getId());
        assertThat(dataPermissionRule1).isEqualTo(dataPermissionRule2);
        dataPermissionRule2.setId(2L);
        assertThat(dataPermissionRule1).isNotEqualTo(dataPermissionRule2);
        dataPermissionRule1.setId(null);
        assertThat(dataPermissionRule1).isNotEqualTo(dataPermissionRule2);
    }
}
