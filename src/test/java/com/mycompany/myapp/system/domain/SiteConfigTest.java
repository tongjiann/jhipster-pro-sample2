package com.mycompany.myapp.system.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SiteConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SiteConfig.class);
        SiteConfig siteConfig1 = new SiteConfig();
        siteConfig1.setId(1L);
        SiteConfig siteConfig2 = new SiteConfig();
        siteConfig2.setId(siteConfig1.getId());
        assertThat(siteConfig1).isEqualTo(siteConfig2);
        siteConfig2.setId(2L);
        assertThat(siteConfig1).isNotEqualTo(siteConfig2);
        siteConfig1.setId(null);
        assertThat(siteConfig1).isNotEqualTo(siteConfig2);
    }
}
