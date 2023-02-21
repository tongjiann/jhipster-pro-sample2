package com.mycompany.myapp.system.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.system.domain.AnnouncementRecord;
import com.mycompany.myapp.system.repository.AnnouncementRecordRepository;
import com.mycompany.myapp.system.service.AnnouncementRecordQueryService;
import com.mycompany.myapp.system.service.criteria.AnnouncementRecordCriteria;
import com.mycompany.myapp.system.service.dto.AnnouncementRecordDTO;
import com.mycompany.myapp.system.service.mapper.AnnouncementRecordMapper;
import com.mycompany.myapp.web.rest.TestUtil;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

/**
 * Integration tests for the {@link AnnouncementRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnnouncementRecordResourceIT {

    private static final Long DEFAULT_ANNT_ID = 1L;
    private static final Long UPDATED_ANNT_ID = 2L;
    private static final Long SMALLER_ANNT_ID = 1L - 1L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final Boolean DEFAULT_HAS_READ = false;
    private static final Boolean UPDATED_HAS_READ = true;

    private static final ZonedDateTime DEFAULT_READ_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_READ_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_READ_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final LocalDate DEFAULT_REMOVED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMOVED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REMOVED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/announcement-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnnouncementRecordRepository announcementRecordRepository;

    @Autowired
    private AnnouncementRecordMapper announcementRecordMapper;

    @Autowired
    private MockMvc restAnnouncementRecordMockMvc;

    private AnnouncementRecord announcementRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnouncementRecord createEntity() {
        AnnouncementRecord announcementRecord = new AnnouncementRecord()
            .anntId(DEFAULT_ANNT_ID)
            .userId(DEFAULT_USER_ID)
            .hasRead(DEFAULT_HAS_READ)
            .readTime(DEFAULT_READ_TIME)
            .removedAt(DEFAULT_REMOVED_AT);
        return announcementRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnouncementRecord createUpdatedEntity() {
        AnnouncementRecord announcementRecord = new AnnouncementRecord()
            .anntId(UPDATED_ANNT_ID)
            .userId(UPDATED_USER_ID)
            .hasRead(UPDATED_HAS_READ)
            .readTime(UPDATED_READ_TIME)
            .removedAt(UPDATED_REMOVED_AT);
        return announcementRecord;
    }

    @BeforeEach
    public void initTest() {
        announcementRecord = createEntity();
    }

    @Test
    @Transactional
    void createAnnouncementRecord() throws Exception {
        int databaseSizeBeforeCreate = announcementRecordRepository.findAll().size();
        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);
        restAnnouncementRecordMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(announcementRecordDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AnnouncementRecord in the database
        List<AnnouncementRecord> announcementRecordList = announcementRecordRepository.findAll();
        assertThat(announcementRecordList).hasSize(databaseSizeBeforeCreate + 1);
        AnnouncementRecord testAnnouncementRecord = announcementRecordList.get(announcementRecordList.size() - 1);
        assertThat(testAnnouncementRecord.getAnntId()).isEqualTo(DEFAULT_ANNT_ID);
        assertThat(testAnnouncementRecord.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testAnnouncementRecord.getHasRead()).isEqualTo(DEFAULT_HAS_READ);
        assertThat(testAnnouncementRecord.getReadTime()).isEqualTo(DEFAULT_READ_TIME);
        assertThat(testAnnouncementRecord.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void createAnnouncementRecordWithExistingId() throws Exception {
        // Create the AnnouncementRecord with an existing ID
        announcementRecord.setId(1L);
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        int databaseSizeBeforeCreate = announcementRecordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnouncementRecordMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(announcementRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnouncementRecord in the database
        List<AnnouncementRecord> announcementRecordList = announcementRecordRepository.findAll();
        assertThat(announcementRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecords() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList
        restAnnouncementRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(announcementRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].anntId").value(hasItem(DEFAULT_ANNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].hasRead").value(hasItem(DEFAULT_HAS_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].readTime").value(hasItem(sameInstant(DEFAULT_READ_TIME))))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));
    }

    @Test
    @Transactional
    void getAnnouncementRecord() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get the announcementRecord
        restAnnouncementRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, announcementRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(announcementRecord.getId().intValue()))
            .andExpect(jsonPath("$.anntId").value(DEFAULT_ANNT_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.hasRead").value(DEFAULT_HAS_READ.booleanValue()))
            .andExpect(jsonPath("$.readTime").value(sameInstant(DEFAULT_READ_TIME)))
            .andExpect(jsonPath("$.removedAt").value(DEFAULT_REMOVED_AT.toString()));
    }

    @Test
    @Transactional
    void getAnnouncementRecordsByIdFiltering() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        Long id = announcementRecord.getId();

        defaultAnnouncementRecordShouldBeFound("id.equals=" + id);
        defaultAnnouncementRecordShouldNotBeFound("id.notEquals=" + id);

        defaultAnnouncementRecordShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnnouncementRecordShouldNotBeFound("id.greaterThan=" + id);

        defaultAnnouncementRecordShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnnouncementRecordShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where anntId equals to DEFAULT_ANNT_ID
        defaultAnnouncementRecordShouldBeFound("anntId.equals=" + DEFAULT_ANNT_ID);

        // Get all the announcementRecordList where anntId equals to UPDATED_ANNT_ID
        defaultAnnouncementRecordShouldNotBeFound("anntId.equals=" + UPDATED_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where anntId not equals to DEFAULT_ANNT_ID
        defaultAnnouncementRecordShouldNotBeFound("anntId.notEquals=" + DEFAULT_ANNT_ID);

        // Get all the announcementRecordList where anntId not equals to UPDATED_ANNT_ID
        defaultAnnouncementRecordShouldBeFound("anntId.notEquals=" + UPDATED_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where anntId in DEFAULT_ANNT_ID or UPDATED_ANNT_ID
        defaultAnnouncementRecordShouldBeFound("anntId.in=" + DEFAULT_ANNT_ID + "," + UPDATED_ANNT_ID);

        // Get all the announcementRecordList where anntId equals to UPDATED_ANNT_ID
        defaultAnnouncementRecordShouldNotBeFound("anntId.in=" + UPDATED_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where anntId is not null
        defaultAnnouncementRecordShouldBeFound("anntId.specified=true");

        // Get all the announcementRecordList where anntId is null
        defaultAnnouncementRecordShouldNotBeFound("anntId.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where anntId is greater than or equal to DEFAULT_ANNT_ID
        defaultAnnouncementRecordShouldBeFound("anntId.greaterThanOrEqual=" + DEFAULT_ANNT_ID);

        // Get all the announcementRecordList where anntId is greater than or equal to UPDATED_ANNT_ID
        defaultAnnouncementRecordShouldNotBeFound("anntId.greaterThanOrEqual=" + UPDATED_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where anntId is less than or equal to DEFAULT_ANNT_ID
        defaultAnnouncementRecordShouldBeFound("anntId.lessThanOrEqual=" + DEFAULT_ANNT_ID);

        // Get all the announcementRecordList where anntId is less than or equal to SMALLER_ANNT_ID
        defaultAnnouncementRecordShouldNotBeFound("anntId.lessThanOrEqual=" + SMALLER_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where anntId is less than DEFAULT_ANNT_ID
        defaultAnnouncementRecordShouldNotBeFound("anntId.lessThan=" + DEFAULT_ANNT_ID);

        // Get all the announcementRecordList where anntId is less than UPDATED_ANNT_ID
        defaultAnnouncementRecordShouldBeFound("anntId.lessThan=" + UPDATED_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByAnntIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where anntId is greater than DEFAULT_ANNT_ID
        defaultAnnouncementRecordShouldNotBeFound("anntId.greaterThan=" + DEFAULT_ANNT_ID);

        // Get all the announcementRecordList where anntId is greater than SMALLER_ANNT_ID
        defaultAnnouncementRecordShouldBeFound("anntId.greaterThan=" + SMALLER_ANNT_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where userId equals to DEFAULT_USER_ID
        defaultAnnouncementRecordShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the announcementRecordList where userId equals to UPDATED_USER_ID
        defaultAnnouncementRecordShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where userId not equals to DEFAULT_USER_ID
        defaultAnnouncementRecordShouldNotBeFound("userId.notEquals=" + DEFAULT_USER_ID);

        // Get all the announcementRecordList where userId not equals to UPDATED_USER_ID
        defaultAnnouncementRecordShouldBeFound("userId.notEquals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultAnnouncementRecordShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the announcementRecordList where userId equals to UPDATED_USER_ID
        defaultAnnouncementRecordShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where userId is not null
        defaultAnnouncementRecordShouldBeFound("userId.specified=true");

        // Get all the announcementRecordList where userId is null
        defaultAnnouncementRecordShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where userId is greater than or equal to DEFAULT_USER_ID
        defaultAnnouncementRecordShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the announcementRecordList where userId is greater than or equal to UPDATED_USER_ID
        defaultAnnouncementRecordShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where userId is less than or equal to DEFAULT_USER_ID
        defaultAnnouncementRecordShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the announcementRecordList where userId is less than or equal to SMALLER_USER_ID
        defaultAnnouncementRecordShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where userId is less than DEFAULT_USER_ID
        defaultAnnouncementRecordShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the announcementRecordList where userId is less than UPDATED_USER_ID
        defaultAnnouncementRecordShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where userId is greater than DEFAULT_USER_ID
        defaultAnnouncementRecordShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the announcementRecordList where userId is greater than SMALLER_USER_ID
        defaultAnnouncementRecordShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByHasReadIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where hasRead equals to DEFAULT_HAS_READ
        defaultAnnouncementRecordShouldBeFound("hasRead.equals=" + DEFAULT_HAS_READ);

        // Get all the announcementRecordList where hasRead equals to UPDATED_HAS_READ
        defaultAnnouncementRecordShouldNotBeFound("hasRead.equals=" + UPDATED_HAS_READ);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByHasReadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where hasRead not equals to DEFAULT_HAS_READ
        defaultAnnouncementRecordShouldNotBeFound("hasRead.notEquals=" + DEFAULT_HAS_READ);

        // Get all the announcementRecordList where hasRead not equals to UPDATED_HAS_READ
        defaultAnnouncementRecordShouldBeFound("hasRead.notEquals=" + UPDATED_HAS_READ);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByHasReadIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where hasRead in DEFAULT_HAS_READ or UPDATED_HAS_READ
        defaultAnnouncementRecordShouldBeFound("hasRead.in=" + DEFAULT_HAS_READ + "," + UPDATED_HAS_READ);

        // Get all the announcementRecordList where hasRead equals to UPDATED_HAS_READ
        defaultAnnouncementRecordShouldNotBeFound("hasRead.in=" + UPDATED_HAS_READ);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByHasReadIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where hasRead is not null
        defaultAnnouncementRecordShouldBeFound("hasRead.specified=true");

        // Get all the announcementRecordList where hasRead is null
        defaultAnnouncementRecordShouldNotBeFound("hasRead.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where readTime equals to DEFAULT_READ_TIME
        defaultAnnouncementRecordShouldBeFound("readTime.equals=" + DEFAULT_READ_TIME);

        // Get all the announcementRecordList where readTime equals to UPDATED_READ_TIME
        defaultAnnouncementRecordShouldNotBeFound("readTime.equals=" + UPDATED_READ_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where readTime not equals to DEFAULT_READ_TIME
        defaultAnnouncementRecordShouldNotBeFound("readTime.notEquals=" + DEFAULT_READ_TIME);

        // Get all the announcementRecordList where readTime not equals to UPDATED_READ_TIME
        defaultAnnouncementRecordShouldBeFound("readTime.notEquals=" + UPDATED_READ_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where readTime in DEFAULT_READ_TIME or UPDATED_READ_TIME
        defaultAnnouncementRecordShouldBeFound("readTime.in=" + DEFAULT_READ_TIME + "," + UPDATED_READ_TIME);

        // Get all the announcementRecordList where readTime equals to UPDATED_READ_TIME
        defaultAnnouncementRecordShouldNotBeFound("readTime.in=" + UPDATED_READ_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where readTime is not null
        defaultAnnouncementRecordShouldBeFound("readTime.specified=true");

        // Get all the announcementRecordList where readTime is null
        defaultAnnouncementRecordShouldNotBeFound("readTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where readTime is greater than or equal to DEFAULT_READ_TIME
        defaultAnnouncementRecordShouldBeFound("readTime.greaterThanOrEqual=" + DEFAULT_READ_TIME);

        // Get all the announcementRecordList where readTime is greater than or equal to UPDATED_READ_TIME
        defaultAnnouncementRecordShouldNotBeFound("readTime.greaterThanOrEqual=" + UPDATED_READ_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where readTime is less than or equal to DEFAULT_READ_TIME
        defaultAnnouncementRecordShouldBeFound("readTime.lessThanOrEqual=" + DEFAULT_READ_TIME);

        // Get all the announcementRecordList where readTime is less than or equal to SMALLER_READ_TIME
        defaultAnnouncementRecordShouldNotBeFound("readTime.lessThanOrEqual=" + SMALLER_READ_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where readTime is less than DEFAULT_READ_TIME
        defaultAnnouncementRecordShouldNotBeFound("readTime.lessThan=" + DEFAULT_READ_TIME);

        // Get all the announcementRecordList where readTime is less than UPDATED_READ_TIME
        defaultAnnouncementRecordShouldBeFound("readTime.lessThan=" + UPDATED_READ_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByReadTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where readTime is greater than DEFAULT_READ_TIME
        defaultAnnouncementRecordShouldNotBeFound("readTime.greaterThan=" + DEFAULT_READ_TIME);

        // Get all the announcementRecordList where readTime is greater than SMALLER_READ_TIME
        defaultAnnouncementRecordShouldBeFound("readTime.greaterThan=" + SMALLER_READ_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByRemovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where removedAt equals to DEFAULT_REMOVED_AT
        defaultAnnouncementRecordShouldBeFound("removedAt.equals=" + DEFAULT_REMOVED_AT);

        // Get all the announcementRecordList where removedAt equals to UPDATED_REMOVED_AT
        defaultAnnouncementRecordShouldNotBeFound("removedAt.equals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByRemovedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where removedAt not equals to DEFAULT_REMOVED_AT
        defaultAnnouncementRecordShouldNotBeFound("removedAt.notEquals=" + DEFAULT_REMOVED_AT);

        // Get all the announcementRecordList where removedAt not equals to UPDATED_REMOVED_AT
        defaultAnnouncementRecordShouldBeFound("removedAt.notEquals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByRemovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where removedAt in DEFAULT_REMOVED_AT or UPDATED_REMOVED_AT
        defaultAnnouncementRecordShouldBeFound("removedAt.in=" + DEFAULT_REMOVED_AT + "," + UPDATED_REMOVED_AT);

        // Get all the announcementRecordList where removedAt equals to UPDATED_REMOVED_AT
        defaultAnnouncementRecordShouldNotBeFound("removedAt.in=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByRemovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where removedAt is not null
        defaultAnnouncementRecordShouldBeFound("removedAt.specified=true");

        // Get all the announcementRecordList where removedAt is null
        defaultAnnouncementRecordShouldNotBeFound("removedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByRemovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where removedAt is greater than or equal to DEFAULT_REMOVED_AT
        defaultAnnouncementRecordShouldBeFound("removedAt.greaterThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the announcementRecordList where removedAt is greater than or equal to UPDATED_REMOVED_AT
        defaultAnnouncementRecordShouldNotBeFound("removedAt.greaterThanOrEqual=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByRemovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where removedAt is less than or equal to DEFAULT_REMOVED_AT
        defaultAnnouncementRecordShouldBeFound("removedAt.lessThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the announcementRecordList where removedAt is less than or equal to SMALLER_REMOVED_AT
        defaultAnnouncementRecordShouldNotBeFound("removedAt.lessThanOrEqual=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByRemovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where removedAt is less than DEFAULT_REMOVED_AT
        defaultAnnouncementRecordShouldNotBeFound("removedAt.lessThan=" + DEFAULT_REMOVED_AT);

        // Get all the announcementRecordList where removedAt is less than UPDATED_REMOVED_AT
        defaultAnnouncementRecordShouldBeFound("removedAt.lessThan=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllAnnouncementRecordsByRemovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        // Get all the announcementRecordList where removedAt is greater than DEFAULT_REMOVED_AT
        defaultAnnouncementRecordShouldNotBeFound("removedAt.greaterThan=" + DEFAULT_REMOVED_AT);

        // Get all the announcementRecordList where removedAt is greater than SMALLER_REMOVED_AT
        defaultAnnouncementRecordShouldBeFound("removedAt.greaterThan=" + SMALLER_REMOVED_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnnouncementRecordShouldBeFound(String filter) throws Exception {
        restAnnouncementRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(announcementRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].anntId").value(hasItem(DEFAULT_ANNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].hasRead").value(hasItem(DEFAULT_HAS_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].readTime").value(hasItem(sameInstant(DEFAULT_READ_TIME))))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));

        // Check, that the count call also returns 1
        restAnnouncementRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnnouncementRecordShouldNotBeFound(String filter) throws Exception {
        restAnnouncementRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnnouncementRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAnnouncementRecord() throws Exception {
        // Get the announcementRecord
        restAnnouncementRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAnnouncementRecord() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        int databaseSizeBeforeUpdate = announcementRecordRepository.findAll().size();

        // Update the announcementRecord
        AnnouncementRecord updatedAnnouncementRecord = announcementRecordRepository.findById(announcementRecord.getId()).get();
        // Disconnect from session so that the updates on updatedAnnouncementRecord are not directly saved in db
        updatedAnnouncementRecord
            .anntId(UPDATED_ANNT_ID)
            .userId(UPDATED_USER_ID)
            .hasRead(UPDATED_HAS_READ)
            .readTime(UPDATED_READ_TIME)
            .removedAt(UPDATED_REMOVED_AT);
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(updatedAnnouncementRecord);

        restAnnouncementRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, announcementRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(announcementRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the AnnouncementRecord in the database
        List<AnnouncementRecord> announcementRecordList = announcementRecordRepository.findAll();
        assertThat(announcementRecordList).hasSize(databaseSizeBeforeUpdate);
        AnnouncementRecord testAnnouncementRecord = announcementRecordList.get(announcementRecordList.size() - 1);
        assertThat(testAnnouncementRecord.getAnntId()).isEqualTo(UPDATED_ANNT_ID);
        assertThat(testAnnouncementRecord.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAnnouncementRecord.getHasRead()).isEqualTo(UPDATED_HAS_READ);
        assertThat(testAnnouncementRecord.getReadTime()).isEqualTo(UPDATED_READ_TIME);
        assertThat(testAnnouncementRecord.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void putNonExistingAnnouncementRecord() throws Exception {
        int databaseSizeBeforeUpdate = announcementRecordRepository.findAll().size();
        announcementRecord.setId(count.incrementAndGet());

        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnouncementRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, announcementRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(announcementRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnouncementRecord in the database
        List<AnnouncementRecord> announcementRecordList = announcementRecordRepository.findAll();
        assertThat(announcementRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnnouncementRecord() throws Exception {
        int databaseSizeBeforeUpdate = announcementRecordRepository.findAll().size();
        announcementRecord.setId(count.incrementAndGet());

        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(announcementRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnouncementRecord in the database
        List<AnnouncementRecord> announcementRecordList = announcementRecordRepository.findAll();
        assertThat(announcementRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnnouncementRecord() throws Exception {
        int databaseSizeBeforeUpdate = announcementRecordRepository.findAll().size();
        announcementRecord.setId(count.incrementAndGet());

        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementRecordMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(announcementRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnnouncementRecord in the database
        List<AnnouncementRecord> announcementRecordList = announcementRecordRepository.findAll();
        assertThat(announcementRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnnouncementRecordWithPatch() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        int databaseSizeBeforeUpdate = announcementRecordRepository.findAll().size();

        // Update the announcementRecord using partial update
        AnnouncementRecord partialUpdatedAnnouncementRecord = new AnnouncementRecord();
        partialUpdatedAnnouncementRecord.setId(announcementRecord.getId());

        partialUpdatedAnnouncementRecord.hasRead(UPDATED_HAS_READ).readTime(UPDATED_READ_TIME);

        restAnnouncementRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnouncementRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnouncementRecord))
            )
            .andExpect(status().isOk());

        // Validate the AnnouncementRecord in the database
        List<AnnouncementRecord> announcementRecordList = announcementRecordRepository.findAll();
        assertThat(announcementRecordList).hasSize(databaseSizeBeforeUpdate);
        AnnouncementRecord testAnnouncementRecord = announcementRecordList.get(announcementRecordList.size() - 1);
        assertThat(testAnnouncementRecord.getAnntId()).isEqualTo(DEFAULT_ANNT_ID);
        assertThat(testAnnouncementRecord.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testAnnouncementRecord.getHasRead()).isEqualTo(UPDATED_HAS_READ);
        assertThat(testAnnouncementRecord.getReadTime()).isEqualTo(UPDATED_READ_TIME);
        assertThat(testAnnouncementRecord.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void fullUpdateAnnouncementRecordWithPatch() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        int databaseSizeBeforeUpdate = announcementRecordRepository.findAll().size();

        // Update the announcementRecord using partial update
        AnnouncementRecord partialUpdatedAnnouncementRecord = new AnnouncementRecord();
        partialUpdatedAnnouncementRecord.setId(announcementRecord.getId());

        partialUpdatedAnnouncementRecord
            .anntId(UPDATED_ANNT_ID)
            .userId(UPDATED_USER_ID)
            .hasRead(UPDATED_HAS_READ)
            .readTime(UPDATED_READ_TIME)
            .removedAt(UPDATED_REMOVED_AT);

        restAnnouncementRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnouncementRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnouncementRecord))
            )
            .andExpect(status().isOk());

        // Validate the AnnouncementRecord in the database
        List<AnnouncementRecord> announcementRecordList = announcementRecordRepository.findAll();
        assertThat(announcementRecordList).hasSize(databaseSizeBeforeUpdate);
        AnnouncementRecord testAnnouncementRecord = announcementRecordList.get(announcementRecordList.size() - 1);
        assertThat(testAnnouncementRecord.getAnntId()).isEqualTo(UPDATED_ANNT_ID);
        assertThat(testAnnouncementRecord.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAnnouncementRecord.getHasRead()).isEqualTo(UPDATED_HAS_READ);
        assertThat(testAnnouncementRecord.getReadTime()).isEqualTo(UPDATED_READ_TIME);
        assertThat(testAnnouncementRecord.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAnnouncementRecord() throws Exception {
        int databaseSizeBeforeUpdate = announcementRecordRepository.findAll().size();
        announcementRecord.setId(count.incrementAndGet());

        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnouncementRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, announcementRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(announcementRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnouncementRecord in the database
        List<AnnouncementRecord> announcementRecordList = announcementRecordRepository.findAll();
        assertThat(announcementRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnnouncementRecord() throws Exception {
        int databaseSizeBeforeUpdate = announcementRecordRepository.findAll().size();
        announcementRecord.setId(count.incrementAndGet());

        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(announcementRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnouncementRecord in the database
        List<AnnouncementRecord> announcementRecordList = announcementRecordRepository.findAll();
        assertThat(announcementRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnnouncementRecord() throws Exception {
        int databaseSizeBeforeUpdate = announcementRecordRepository.findAll().size();
        announcementRecord.setId(count.incrementAndGet());

        // Create the AnnouncementRecord
        AnnouncementRecordDTO announcementRecordDTO = announcementRecordMapper.toDto(announcementRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementRecordMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(announcementRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnnouncementRecord in the database
        List<AnnouncementRecord> announcementRecordList = announcementRecordRepository.findAll();
        assertThat(announcementRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnnouncementRecord() throws Exception {
        // Initialize the database
        announcementRecordRepository.insert(announcementRecord);

        int databaseSizeBeforeDelete = announcementRecordRepository.findAll().size();

        // Delete the announcementRecord
        restAnnouncementRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, announcementRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnnouncementRecord> announcementRecordList = announcementRecordRepository.findAll();
        assertThat(announcementRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
