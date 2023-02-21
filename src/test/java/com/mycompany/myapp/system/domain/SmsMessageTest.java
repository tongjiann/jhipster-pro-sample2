package com.mycompany.myapp.system.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SmsMessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsMessage.class);
        SmsMessage smsMessage1 = new SmsMessage();
        smsMessage1.setId(1L);
        SmsMessage smsMessage2 = new SmsMessage();
        smsMessage2.setId(smsMessage1.getId());
        assertThat(smsMessage1).isEqualTo(smsMessage2);
        smsMessage2.setId(2L);
        assertThat(smsMessage1).isNotEqualTo(smsMessage2);
        smsMessage1.setId(null);
        assertThat(smsMessage1).isNotEqualTo(smsMessage2);
    }
}
