package com.mycompany.myapp.system.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SmsTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsTemplate.class);
        SmsTemplate smsTemplate1 = new SmsTemplate();
        smsTemplate1.setId(1L);
        SmsTemplate smsTemplate2 = new SmsTemplate();
        smsTemplate2.setId(smsTemplate1.getId());
        assertThat(smsTemplate1).isEqualTo(smsTemplate2);
        smsTemplate2.setId(2L);
        assertThat(smsTemplate1).isNotEqualTo(smsTemplate2);
        smsTemplate1.setId(null);
        assertThat(smsTemplate1).isNotEqualTo(smsTemplate2);
    }
}
