package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Authority.class);
        Authority authority1 = new Authority();
        authority1.setId(1L);
        Authority authority2 = new Authority();
        authority2.setId(authority1.getId());
        assertThat(authority1).isEqualTo(authority2);
        authority2.setId(2L);
        assertThat(authority1).isNotEqualTo(authority2);
        authority1.setId(null);
        assertThat(authority1).isNotEqualTo(authority2);
    }
}
