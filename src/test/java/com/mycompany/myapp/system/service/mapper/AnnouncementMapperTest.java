package com.mycompany.myapp.system.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnnouncementMapperTest {

    private AnnouncementMapper announcementMapper;

    @BeforeEach
    public void setUp() {
        announcementMapper = new AnnouncementMapperImpl();
    }
}
