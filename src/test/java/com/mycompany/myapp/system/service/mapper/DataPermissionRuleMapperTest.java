package com.mycompany.myapp.system.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataPermissionRuleMapperTest {

    private DataPermissionRuleMapper dataPermissionRuleMapper;

    @BeforeEach
    public void setUp() {
        dataPermissionRuleMapper = new DataPermissionRuleMapperImpl();
    }
}
