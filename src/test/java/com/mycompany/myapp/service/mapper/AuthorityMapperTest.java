package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthorityMapperTest {

    private AuthorityMapper authorityMapper;

    @BeforeEach
    public void setUp() {
        authorityMapper = new AuthorityMapperImpl();
    }
}
