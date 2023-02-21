package com.mycompany.myapp.system.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SiteConfigMapperTest {

    private SiteConfigMapper siteConfigMapper;

    @BeforeEach
    public void setUp() {
        siteConfigMapper = new SiteConfigMapperImpl();
    }
}
