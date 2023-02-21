package com.mycompany.myapp.system.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnnouncementRecordMapperTest {

    private AnnouncementRecordMapper announcementRecordMapper;

    @BeforeEach
    public void setUp() {
        announcementRecordMapper = new AnnouncementRecordMapperImpl();
    }
}
