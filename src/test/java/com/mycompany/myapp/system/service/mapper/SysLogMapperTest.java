package com.mycompany.myapp.system.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SysLogMapperTest {

    private SysLogMapper sysLogMapper;

    @BeforeEach
    public void setUp() {
        sysLogMapper = new SysLogMapperImpl();
    }
}
