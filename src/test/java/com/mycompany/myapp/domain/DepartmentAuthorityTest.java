package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepartmentAuthorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepartmentAuthority.class);
        DepartmentAuthority departmentAuthority1 = new DepartmentAuthority();
        departmentAuthority1.setId(1L);
        DepartmentAuthority departmentAuthority2 = new DepartmentAuthority();
        departmentAuthority2.setId(departmentAuthority1.getId());
        assertThat(departmentAuthority1).isEqualTo(departmentAuthority2);
        departmentAuthority2.setId(2L);
        assertThat(departmentAuthority1).isNotEqualTo(departmentAuthority2);
        departmentAuthority1.setId(null);
        assertThat(departmentAuthority1).isNotEqualTo(departmentAuthority2);
    }
}
