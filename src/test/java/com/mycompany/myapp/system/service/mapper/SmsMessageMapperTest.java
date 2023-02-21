package com.mycompany.myapp.system.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SmsMessageMapperTest {

    private SmsMessageMapper smsMessageMapper;

    @BeforeEach
    public void setUp() {
        smsMessageMapper = new SmsMessageMapperImpl();
    }
}
