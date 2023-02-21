package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepartmentAuthorityMapperTest {

    private DepartmentAuthorityMapper departmentAuthorityMapper;

    @BeforeEach
    public void setUp() {
        departmentAuthorityMapper = new DepartmentAuthorityMapperImpl();
    }
}
