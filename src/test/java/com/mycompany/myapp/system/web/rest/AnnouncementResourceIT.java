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
import com.mycompany.myapp.domain.enumeration.AnnoBusinessType;
import com.mycompany.myapp.domain.enumeration.AnnoCategory;
import com.mycompany.myapp.domain.enumeration.AnnoOpenType;
import com.mycompany.myapp.domain.enumeration.AnnoSendStatus;
import com.mycompany.myapp.domain.enumeration.PriorityLevel;
import com.mycompany.myapp.domain.enumeration.ReceiverType;
import com.mycompany.myapp.system.domain.Announcement;
import com.mycompany.myapp.system.repository.AnnouncementRepository;
import com.mycompany.myapp.system.service.AnnouncementQueryService;
import com.mycompany.myapp.system.service.criteria.AnnouncementCriteria;
import com.mycompany.myapp.system.service.dto.AnnouncementDTO;
import com.mycompany.myapp.system.service.mapper.AnnouncementMapper;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

/**
 * Integration tests for the {@link AnnouncementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnnouncementResourceIT {

    private static final String DEFAULT_TITILE = "AAAAAAAAAA";
    private static final String UPDATED_TITILE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Long DEFAULT_SENDER_ID = 1L;
    private static final Long UPDATED_SENDER_ID = 2L;
    private static final Long SMALLER_SENDER_ID = 1L - 1L;

    private static final PriorityLevel DEFAULT_PRIORITY = PriorityLevel.HIGH;
    private static final PriorityLevel UPDATED_PRIORITY = PriorityLevel.MEDIUM;

    private static final AnnoCategory DEFAULT_CATEGORY = AnnoCategory.SYSTEM_INFO;
    private static final AnnoCategory UPDATED_CATEGORY = AnnoCategory.NOTICE;

    private static final ReceiverType DEFAULT_RECEIVER_TYPE = ReceiverType.USER;
    private static final ReceiverType UPDATED_RECEIVER_TYPE = ReceiverType.ALL;

    private static final AnnoSendStatus DEFAULT_SEND_STATUS = AnnoSendStatus.NOT_RELEASE;
    private static final AnnoSendStatus UPDATED_SEND_STATUS = AnnoSendStatus.RELEASED;

    private static final ZonedDateTime DEFAULT_SEND_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SEND_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_SEND_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_CANCEL_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CANCEL_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CANCEL_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final AnnoBusinessType DEFAULT_BUSINESS_TYPE = AnnoBusinessType.EMAIL;
    private static final AnnoBusinessType UPDATED_BUSINESS_TYPE = AnnoBusinessType.WORKFLOW;

    private static final Long DEFAULT_BUSINESS_ID = 1L;
    private static final Long UPDATED_BUSINESS_ID = 2L;
    private static final Long SMALLER_BUSINESS_ID = 1L - 1L;

    private static final AnnoOpenType DEFAULT_OPEN_TYPE = AnnoOpenType.URL;
    private static final AnnoOpenType UPDATED_OPEN_TYPE = AnnoOpenType.COMPONENT;

    private static final String DEFAULT_OPEN_PAGE = "AAAAAAAAAA";
    private static final String UPDATED_OPEN_PAGE = "BBBBBBBBBB";
    private static final Long[] DEFAULT_RECEIVER_IDS = {};
    private static final Long[] UPDATED_RECEIVER_IDS = {};

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REMOVED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMOVED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REMOVED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/announcements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private MockMvc restAnnouncementMockMvc;

    private Announcement announcement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Announcement createEntity() {
        Announcement announcement = new Announcement()
            .titile(DEFAULT_TITILE)
            .content(DEFAULT_CONTENT)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .senderId(DEFAULT_SENDER_ID)
            .priority(DEFAULT_PRIORITY)
            .category(DEFAULT_CATEGORY)
            .receiverType(DEFAULT_RECEIVER_TYPE)
            .sendStatus(DEFAULT_SEND_STATUS)
            .sendTime(DEFAULT_SEND_TIME)
            .cancelTime(DEFAULT_CANCEL_TIME)
            .businessType(DEFAULT_BUSINESS_TYPE)
            .businessId(DEFAULT_BUSINESS_ID)
            .openType(DEFAULT_OPEN_TYPE)
            .openPage(DEFAULT_OPEN_PAGE)
            .receiverIds(DEFAULT_RECEIVER_IDS)
            .summary(DEFAULT_SUMMARY)
            .removedAt(DEFAULT_REMOVED_AT);
        return announcement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Announcement createUpdatedEntity() {
        Announcement announcement = new Announcement()
            .titile(UPDATED_TITILE)
            .content(UPDATED_CONTENT)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .senderId(UPDATED_SENDER_ID)
            .priority(UPDATED_PRIORITY)
            .category(UPDATED_CATEGORY)
            .receiverType(UPDATED_RECEIVER_TYPE)
            .sendStatus(UPDATED_SEND_STATUS)
            .sendTime(UPDATED_SEND_TIME)
            .cancelTime(UPDATED_CANCEL_TIME)
            .businessType(UPDATED_BUSINESS_TYPE)
            .businessId(UPDATED_BUSINESS_ID)
            .openType(UPDATED_OPEN_TYPE)
            .openPage(UPDATED_OPEN_PAGE)
            .receiverIds(UPDATED_RECEIVER_IDS)
            .summary(UPDATED_SUMMARY)
            .removedAt(UPDATED_REMOVED_AT);
        return announcement;
    }

    @BeforeEach
    public void initTest() {
        announcement = createEntity();
    }

    @Test
    @Transactional
    void createAnnouncement() throws Exception {
        int databaseSizeBeforeCreate = announcementRepository.findAll().size();
        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);
        restAnnouncementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(announcementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeCreate + 1);
        Announcement testAnnouncement = announcementList.get(announcementList.size() - 1);
        assertThat(testAnnouncement.getTitile()).isEqualTo(DEFAULT_TITILE);
        assertThat(testAnnouncement.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testAnnouncement.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testAnnouncement.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testAnnouncement.getSenderId()).isEqualTo(DEFAULT_SENDER_ID);
        assertThat(testAnnouncement.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testAnnouncement.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testAnnouncement.getReceiverType()).isEqualTo(DEFAULT_RECEIVER_TYPE);
        assertThat(testAnnouncement.getSendStatus()).isEqualTo(DEFAULT_SEND_STATUS);
        assertThat(testAnnouncement.getSendTime()).isEqualTo(DEFAULT_SEND_TIME);
        assertThat(testAnnouncement.getCancelTime()).isEqualTo(DEFAULT_CANCEL_TIME);
        assertThat(testAnnouncement.getBusinessType()).isEqualTo(DEFAULT_BUSINESS_TYPE);
        assertThat(testAnnouncement.getBusinessId()).isEqualTo(DEFAULT_BUSINESS_ID);
        assertThat(testAnnouncement.getOpenType()).isEqualTo(DEFAULT_OPEN_TYPE);
        assertThat(testAnnouncement.getOpenPage()).isEqualTo(DEFAULT_OPEN_PAGE);
        assertThat(testAnnouncement.getReceiverIds()).isEqualTo(DEFAULT_RECEIVER_IDS);
        assertThat(testAnnouncement.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testAnnouncement.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void createAnnouncementWithExistingId() throws Exception {
        // Create the Announcement with an existing ID
        announcement.setId(1L);
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        int databaseSizeBeforeCreate = announcementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnouncementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(announcementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAnnouncements() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList
        restAnnouncementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(announcement.getId().intValue())))
            .andExpect(jsonPath("$.[*].titile").value(hasItem(DEFAULT_TITILE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].senderId").value(hasItem(DEFAULT_SENDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].receiverType").value(hasItem(DEFAULT_RECEIVER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sendStatus").value(hasItem(DEFAULT_SEND_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sendTime").value(hasItem(sameInstant(DEFAULT_SEND_TIME))))
            .andExpect(jsonPath("$.[*].cancelTime").value(hasItem(sameInstant(DEFAULT_CANCEL_TIME))))
            .andExpect(jsonPath("$.[*].businessType").value(hasItem(DEFAULT_BUSINESS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].businessId").value(hasItem(DEFAULT_BUSINESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].openType").value(hasItem(DEFAULT_OPEN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].openPage").value(hasItem(DEFAULT_OPEN_PAGE)))
            .andExpect(jsonPath("$.[*].receiverIds").value(hasItem(Arrays.toString(DEFAULT_RECEIVER_IDS))))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));
    }

    @Test
    @Transactional
    void getAnnouncement() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get the announcement
        restAnnouncementMockMvc
            .perform(get(ENTITY_API_URL_ID, announcement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(announcement.getId().intValue()))
            .andExpect(jsonPath("$.titile").value(DEFAULT_TITILE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.senderId").value(DEFAULT_SENDER_ID.intValue()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.receiverType").value(DEFAULT_RECEIVER_TYPE.toString()))
            .andExpect(jsonPath("$.sendStatus").value(DEFAULT_SEND_STATUS.toString()))
            .andExpect(jsonPath("$.sendTime").value(sameInstant(DEFAULT_SEND_TIME)))
            .andExpect(jsonPath("$.cancelTime").value(sameInstant(DEFAULT_CANCEL_TIME)))
            .andExpect(jsonPath("$.businessType").value(DEFAULT_BUSINESS_TYPE.toString()))
            .andExpect(jsonPath("$.businessId").value(DEFAULT_BUSINESS_ID.intValue()))
            .andExpect(jsonPath("$.openType").value(DEFAULT_OPEN_TYPE.toString()))
            .andExpect(jsonPath("$.openPage").value(DEFAULT_OPEN_PAGE))
            .andExpect(jsonPath("$.receiverIds").value(Arrays.toString(DEFAULT_RECEIVER_IDS)))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY.toString()))
            .andExpect(jsonPath("$.removedAt").value(DEFAULT_REMOVED_AT.toString()));
    }

    @Test
    @Transactional
    void getAnnouncementsByIdFiltering() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        Long id = announcement.getId();

        defaultAnnouncementShouldBeFound("id.equals=" + id);
        defaultAnnouncementShouldNotBeFound("id.notEquals=" + id);

        defaultAnnouncementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnnouncementShouldNotBeFound("id.greaterThan=" + id);

        defaultAnnouncementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnnouncementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByTitileIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where titile equals to DEFAULT_TITILE
        defaultAnnouncementShouldBeFound("titile.equals=" + DEFAULT_TITILE);

        // Get all the announcementList where titile equals to UPDATED_TITILE
        defaultAnnouncementShouldNotBeFound("titile.equals=" + UPDATED_TITILE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByTitileIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where titile not equals to DEFAULT_TITILE
        defaultAnnouncementShouldNotBeFound("titile.notEquals=" + DEFAULT_TITILE);

        // Get all the announcementList where titile not equals to UPDATED_TITILE
        defaultAnnouncementShouldBeFound("titile.notEquals=" + UPDATED_TITILE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByTitileIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where titile in DEFAULT_TITILE or UPDATED_TITILE
        defaultAnnouncementShouldBeFound("titile.in=" + DEFAULT_TITILE + "," + UPDATED_TITILE);

        // Get all the announcementList where titile equals to UPDATED_TITILE
        defaultAnnouncementShouldNotBeFound("titile.in=" + UPDATED_TITILE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByTitileIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where titile is not null
        defaultAnnouncementShouldBeFound("titile.specified=true");

        // Get all the announcementList where titile is null
        defaultAnnouncementShouldNotBeFound("titile.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByTitileContainsSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where titile contains DEFAULT_TITILE
        defaultAnnouncementShouldBeFound("titile.contains=" + DEFAULT_TITILE);

        // Get all the announcementList where titile contains UPDATED_TITILE
        defaultAnnouncementShouldNotBeFound("titile.contains=" + UPDATED_TITILE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByTitileNotContainsSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where titile does not contain DEFAULT_TITILE
        defaultAnnouncementShouldNotBeFound("titile.doesNotContain=" + DEFAULT_TITILE);

        // Get all the announcementList where titile does not contain UPDATED_TITILE
        defaultAnnouncementShouldBeFound("titile.doesNotContain=" + UPDATED_TITILE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where startTime equals to DEFAULT_START_TIME
        defaultAnnouncementShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the announcementList where startTime equals to UPDATED_START_TIME
        defaultAnnouncementShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where startTime not equals to DEFAULT_START_TIME
        defaultAnnouncementShouldNotBeFound("startTime.notEquals=" + DEFAULT_START_TIME);

        // Get all the announcementList where startTime not equals to UPDATED_START_TIME
        defaultAnnouncementShouldBeFound("startTime.notEquals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultAnnouncementShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the announcementList where startTime equals to UPDATED_START_TIME
        defaultAnnouncementShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where startTime is not null
        defaultAnnouncementShouldBeFound("startTime.specified=true");

        // Get all the announcementList where startTime is null
        defaultAnnouncementShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where startTime is greater than or equal to DEFAULT_START_TIME
        defaultAnnouncementShouldBeFound("startTime.greaterThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the announcementList where startTime is greater than or equal to UPDATED_START_TIME
        defaultAnnouncementShouldNotBeFound("startTime.greaterThanOrEqual=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where startTime is less than or equal to DEFAULT_START_TIME
        defaultAnnouncementShouldBeFound("startTime.lessThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the announcementList where startTime is less than or equal to SMALLER_START_TIME
        defaultAnnouncementShouldNotBeFound("startTime.lessThanOrEqual=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where startTime is less than DEFAULT_START_TIME
        defaultAnnouncementShouldNotBeFound("startTime.lessThan=" + DEFAULT_START_TIME);

        // Get all the announcementList where startTime is less than UPDATED_START_TIME
        defaultAnnouncementShouldBeFound("startTime.lessThan=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where startTime is greater than DEFAULT_START_TIME
        defaultAnnouncementShouldNotBeFound("startTime.greaterThan=" + DEFAULT_START_TIME);

        // Get all the announcementList where startTime is greater than SMALLER_START_TIME
        defaultAnnouncementShouldBeFound("startTime.greaterThan=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where endTime equals to DEFAULT_END_TIME
        defaultAnnouncementShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the announcementList where endTime equals to UPDATED_END_TIME
        defaultAnnouncementShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where endTime not equals to DEFAULT_END_TIME
        defaultAnnouncementShouldNotBeFound("endTime.notEquals=" + DEFAULT_END_TIME);

        // Get all the announcementList where endTime not equals to UPDATED_END_TIME
        defaultAnnouncementShouldBeFound("endTime.notEquals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultAnnouncementShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the announcementList where endTime equals to UPDATED_END_TIME
        defaultAnnouncementShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where endTime is not null
        defaultAnnouncementShouldBeFound("endTime.specified=true");

        // Get all the announcementList where endTime is null
        defaultAnnouncementShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where endTime is greater than or equal to DEFAULT_END_TIME
        defaultAnnouncementShouldBeFound("endTime.greaterThanOrEqual=" + DEFAULT_END_TIME);

        // Get all the announcementList where endTime is greater than or equal to UPDATED_END_TIME
        defaultAnnouncementShouldNotBeFound("endTime.greaterThanOrEqual=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where endTime is less than or equal to DEFAULT_END_TIME
        defaultAnnouncementShouldBeFound("endTime.lessThanOrEqual=" + DEFAULT_END_TIME);

        // Get all the announcementList where endTime is less than or equal to SMALLER_END_TIME
        defaultAnnouncementShouldNotBeFound("endTime.lessThanOrEqual=" + SMALLER_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where endTime is less than DEFAULT_END_TIME
        defaultAnnouncementShouldNotBeFound("endTime.lessThan=" + DEFAULT_END_TIME);

        // Get all the announcementList where endTime is less than UPDATED_END_TIME
        defaultAnnouncementShouldBeFound("endTime.lessThan=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByEndTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where endTime is greater than DEFAULT_END_TIME
        defaultAnnouncementShouldNotBeFound("endTime.greaterThan=" + DEFAULT_END_TIME);

        // Get all the announcementList where endTime is greater than SMALLER_END_TIME
        defaultAnnouncementShouldBeFound("endTime.greaterThan=" + SMALLER_END_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where senderId equals to DEFAULT_SENDER_ID
        defaultAnnouncementShouldBeFound("senderId.equals=" + DEFAULT_SENDER_ID);

        // Get all the announcementList where senderId equals to UPDATED_SENDER_ID
        defaultAnnouncementShouldNotBeFound("senderId.equals=" + UPDATED_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where senderId not equals to DEFAULT_SENDER_ID
        defaultAnnouncementShouldNotBeFound("senderId.notEquals=" + DEFAULT_SENDER_ID);

        // Get all the announcementList where senderId not equals to UPDATED_SENDER_ID
        defaultAnnouncementShouldBeFound("senderId.notEquals=" + UPDATED_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where senderId in DEFAULT_SENDER_ID or UPDATED_SENDER_ID
        defaultAnnouncementShouldBeFound("senderId.in=" + DEFAULT_SENDER_ID + "," + UPDATED_SENDER_ID);

        // Get all the announcementList where senderId equals to UPDATED_SENDER_ID
        defaultAnnouncementShouldNotBeFound("senderId.in=" + UPDATED_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where senderId is not null
        defaultAnnouncementShouldBeFound("senderId.specified=true");

        // Get all the announcementList where senderId is null
        defaultAnnouncementShouldNotBeFound("senderId.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where senderId is greater than or equal to DEFAULT_SENDER_ID
        defaultAnnouncementShouldBeFound("senderId.greaterThanOrEqual=" + DEFAULT_SENDER_ID);

        // Get all the announcementList where senderId is greater than or equal to UPDATED_SENDER_ID
        defaultAnnouncementShouldNotBeFound("senderId.greaterThanOrEqual=" + UPDATED_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where senderId is less than or equal to DEFAULT_SENDER_ID
        defaultAnnouncementShouldBeFound("senderId.lessThanOrEqual=" + DEFAULT_SENDER_ID);

        // Get all the announcementList where senderId is less than or equal to SMALLER_SENDER_ID
        defaultAnnouncementShouldNotBeFound("senderId.lessThanOrEqual=" + SMALLER_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where senderId is less than DEFAULT_SENDER_ID
        defaultAnnouncementShouldNotBeFound("senderId.lessThan=" + DEFAULT_SENDER_ID);

        // Get all the announcementList where senderId is less than UPDATED_SENDER_ID
        defaultAnnouncementShouldBeFound("senderId.lessThan=" + UPDATED_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySenderIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where senderId is greater than DEFAULT_SENDER_ID
        defaultAnnouncementShouldNotBeFound("senderId.greaterThan=" + DEFAULT_SENDER_ID);

        // Get all the announcementList where senderId is greater than SMALLER_SENDER_ID
        defaultAnnouncementShouldBeFound("senderId.greaterThan=" + SMALLER_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where priority equals to DEFAULT_PRIORITY
        defaultAnnouncementShouldBeFound("priority.equals=" + DEFAULT_PRIORITY);

        // Get all the announcementList where priority equals to UPDATED_PRIORITY
        defaultAnnouncementShouldNotBeFound("priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByPriorityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where priority not equals to DEFAULT_PRIORITY
        defaultAnnouncementShouldNotBeFound("priority.notEquals=" + DEFAULT_PRIORITY);

        // Get all the announcementList where priority not equals to UPDATED_PRIORITY
        defaultAnnouncementShouldBeFound("priority.notEquals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where priority in DEFAULT_PRIORITY or UPDATED_PRIORITY
        defaultAnnouncementShouldBeFound("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY);

        // Get all the announcementList where priority equals to UPDATED_PRIORITY
        defaultAnnouncementShouldNotBeFound("priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where priority is not null
        defaultAnnouncementShouldBeFound("priority.specified=true");

        // Get all the announcementList where priority is null
        defaultAnnouncementShouldNotBeFound("priority.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where category equals to DEFAULT_CATEGORY
        defaultAnnouncementShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the announcementList where category equals to UPDATED_CATEGORY
        defaultAnnouncementShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where category not equals to DEFAULT_CATEGORY
        defaultAnnouncementShouldNotBeFound("category.notEquals=" + DEFAULT_CATEGORY);

        // Get all the announcementList where category not equals to UPDATED_CATEGORY
        defaultAnnouncementShouldBeFound("category.notEquals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultAnnouncementShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the announcementList where category equals to UPDATED_CATEGORY
        defaultAnnouncementShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where category is not null
        defaultAnnouncementShouldBeFound("category.specified=true");

        // Get all the announcementList where category is null
        defaultAnnouncementShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByReceiverTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where receiverType equals to DEFAULT_RECEIVER_TYPE
        defaultAnnouncementShouldBeFound("receiverType.equals=" + DEFAULT_RECEIVER_TYPE);

        // Get all the announcementList where receiverType equals to UPDATED_RECEIVER_TYPE
        defaultAnnouncementShouldNotBeFound("receiverType.equals=" + UPDATED_RECEIVER_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByReceiverTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where receiverType not equals to DEFAULT_RECEIVER_TYPE
        defaultAnnouncementShouldNotBeFound("receiverType.notEquals=" + DEFAULT_RECEIVER_TYPE);

        // Get all the announcementList where receiverType not equals to UPDATED_RECEIVER_TYPE
        defaultAnnouncementShouldBeFound("receiverType.notEquals=" + UPDATED_RECEIVER_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByReceiverTypeIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where receiverType in DEFAULT_RECEIVER_TYPE or UPDATED_RECEIVER_TYPE
        defaultAnnouncementShouldBeFound("receiverType.in=" + DEFAULT_RECEIVER_TYPE + "," + UPDATED_RECEIVER_TYPE);

        // Get all the announcementList where receiverType equals to UPDATED_RECEIVER_TYPE
        defaultAnnouncementShouldNotBeFound("receiverType.in=" + UPDATED_RECEIVER_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByReceiverTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where receiverType is not null
        defaultAnnouncementShouldBeFound("receiverType.specified=true");

        // Get all the announcementList where receiverType is null
        defaultAnnouncementShouldNotBeFound("receiverType.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where sendStatus equals to DEFAULT_SEND_STATUS
        defaultAnnouncementShouldBeFound("sendStatus.equals=" + DEFAULT_SEND_STATUS);

        // Get all the announcementList where sendStatus equals to UPDATED_SEND_STATUS
        defaultAnnouncementShouldNotBeFound("sendStatus.equals=" + UPDATED_SEND_STATUS);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where sendStatus not equals to DEFAULT_SEND_STATUS
        defaultAnnouncementShouldNotBeFound("sendStatus.notEquals=" + DEFAULT_SEND_STATUS);

        // Get all the announcementList where sendStatus not equals to UPDATED_SEND_STATUS
        defaultAnnouncementShouldBeFound("sendStatus.notEquals=" + UPDATED_SEND_STATUS);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendStatusIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where sendStatus in DEFAULT_SEND_STATUS or UPDATED_SEND_STATUS
        defaultAnnouncementShouldBeFound("sendStatus.in=" + DEFAULT_SEND_STATUS + "," + UPDATED_SEND_STATUS);

        // Get all the announcementList where sendStatus equals to UPDATED_SEND_STATUS
        defaultAnnouncementShouldNotBeFound("sendStatus.in=" + UPDATED_SEND_STATUS);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where sendStatus is not null
        defaultAnnouncementShouldBeFound("sendStatus.specified=true");

        // Get all the announcementList where sendStatus is null
        defaultAnnouncementShouldNotBeFound("sendStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where sendTime equals to DEFAULT_SEND_TIME
        defaultAnnouncementShouldBeFound("sendTime.equals=" + DEFAULT_SEND_TIME);

        // Get all the announcementList where sendTime equals to UPDATED_SEND_TIME
        defaultAnnouncementShouldNotBeFound("sendTime.equals=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where sendTime not equals to DEFAULT_SEND_TIME
        defaultAnnouncementShouldNotBeFound("sendTime.notEquals=" + DEFAULT_SEND_TIME);

        // Get all the announcementList where sendTime not equals to UPDATED_SEND_TIME
        defaultAnnouncementShouldBeFound("sendTime.notEquals=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where sendTime in DEFAULT_SEND_TIME or UPDATED_SEND_TIME
        defaultAnnouncementShouldBeFound("sendTime.in=" + DEFAULT_SEND_TIME + "," + UPDATED_SEND_TIME);

        // Get all the announcementList where sendTime equals to UPDATED_SEND_TIME
        defaultAnnouncementShouldNotBeFound("sendTime.in=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where sendTime is not null
        defaultAnnouncementShouldBeFound("sendTime.specified=true");

        // Get all the announcementList where sendTime is null
        defaultAnnouncementShouldNotBeFound("sendTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where sendTime is greater than or equal to DEFAULT_SEND_TIME
        defaultAnnouncementShouldBeFound("sendTime.greaterThanOrEqual=" + DEFAULT_SEND_TIME);

        // Get all the announcementList where sendTime is greater than or equal to UPDATED_SEND_TIME
        defaultAnnouncementShouldNotBeFound("sendTime.greaterThanOrEqual=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where sendTime is less than or equal to DEFAULT_SEND_TIME
        defaultAnnouncementShouldBeFound("sendTime.lessThanOrEqual=" + DEFAULT_SEND_TIME);

        // Get all the announcementList where sendTime is less than or equal to SMALLER_SEND_TIME
        defaultAnnouncementShouldNotBeFound("sendTime.lessThanOrEqual=" + SMALLER_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where sendTime is less than DEFAULT_SEND_TIME
        defaultAnnouncementShouldNotBeFound("sendTime.lessThan=" + DEFAULT_SEND_TIME);

        // Get all the announcementList where sendTime is less than UPDATED_SEND_TIME
        defaultAnnouncementShouldBeFound("sendTime.lessThan=" + UPDATED_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsBySendTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where sendTime is greater than DEFAULT_SEND_TIME
        defaultAnnouncementShouldNotBeFound("sendTime.greaterThan=" + DEFAULT_SEND_TIME);

        // Get all the announcementList where sendTime is greater than SMALLER_SEND_TIME
        defaultAnnouncementShouldBeFound("sendTime.greaterThan=" + SMALLER_SEND_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where cancelTime equals to DEFAULT_CANCEL_TIME
        defaultAnnouncementShouldBeFound("cancelTime.equals=" + DEFAULT_CANCEL_TIME);

        // Get all the announcementList where cancelTime equals to UPDATED_CANCEL_TIME
        defaultAnnouncementShouldNotBeFound("cancelTime.equals=" + UPDATED_CANCEL_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where cancelTime not equals to DEFAULT_CANCEL_TIME
        defaultAnnouncementShouldNotBeFound("cancelTime.notEquals=" + DEFAULT_CANCEL_TIME);

        // Get all the announcementList where cancelTime not equals to UPDATED_CANCEL_TIME
        defaultAnnouncementShouldBeFound("cancelTime.notEquals=" + UPDATED_CANCEL_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where cancelTime in DEFAULT_CANCEL_TIME or UPDATED_CANCEL_TIME
        defaultAnnouncementShouldBeFound("cancelTime.in=" + DEFAULT_CANCEL_TIME + "," + UPDATED_CANCEL_TIME);

        // Get all the announcementList where cancelTime equals to UPDATED_CANCEL_TIME
        defaultAnnouncementShouldNotBeFound("cancelTime.in=" + UPDATED_CANCEL_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where cancelTime is not null
        defaultAnnouncementShouldBeFound("cancelTime.specified=true");

        // Get all the announcementList where cancelTime is null
        defaultAnnouncementShouldNotBeFound("cancelTime.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where cancelTime is greater than or equal to DEFAULT_CANCEL_TIME
        defaultAnnouncementShouldBeFound("cancelTime.greaterThanOrEqual=" + DEFAULT_CANCEL_TIME);

        // Get all the announcementList where cancelTime is greater than or equal to UPDATED_CANCEL_TIME
        defaultAnnouncementShouldNotBeFound("cancelTime.greaterThanOrEqual=" + UPDATED_CANCEL_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where cancelTime is less than or equal to DEFAULT_CANCEL_TIME
        defaultAnnouncementShouldBeFound("cancelTime.lessThanOrEqual=" + DEFAULT_CANCEL_TIME);

        // Get all the announcementList where cancelTime is less than or equal to SMALLER_CANCEL_TIME
        defaultAnnouncementShouldNotBeFound("cancelTime.lessThanOrEqual=" + SMALLER_CANCEL_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where cancelTime is less than DEFAULT_CANCEL_TIME
        defaultAnnouncementShouldNotBeFound("cancelTime.lessThan=" + DEFAULT_CANCEL_TIME);

        // Get all the announcementList where cancelTime is less than UPDATED_CANCEL_TIME
        defaultAnnouncementShouldBeFound("cancelTime.lessThan=" + UPDATED_CANCEL_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByCancelTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where cancelTime is greater than DEFAULT_CANCEL_TIME
        defaultAnnouncementShouldNotBeFound("cancelTime.greaterThan=" + DEFAULT_CANCEL_TIME);

        // Get all the announcementList where cancelTime is greater than SMALLER_CANCEL_TIME
        defaultAnnouncementShouldBeFound("cancelTime.greaterThan=" + SMALLER_CANCEL_TIME);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where businessType equals to DEFAULT_BUSINESS_TYPE
        defaultAnnouncementShouldBeFound("businessType.equals=" + DEFAULT_BUSINESS_TYPE);

        // Get all the announcementList where businessType equals to UPDATED_BUSINESS_TYPE
        defaultAnnouncementShouldNotBeFound("businessType.equals=" + UPDATED_BUSINESS_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where businessType not equals to DEFAULT_BUSINESS_TYPE
        defaultAnnouncementShouldNotBeFound("businessType.notEquals=" + DEFAULT_BUSINESS_TYPE);

        // Get all the announcementList where businessType not equals to UPDATED_BUSINESS_TYPE
        defaultAnnouncementShouldBeFound("businessType.notEquals=" + UPDATED_BUSINESS_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessTypeIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where businessType in DEFAULT_BUSINESS_TYPE or UPDATED_BUSINESS_TYPE
        defaultAnnouncementShouldBeFound("businessType.in=" + DEFAULT_BUSINESS_TYPE + "," + UPDATED_BUSINESS_TYPE);

        // Get all the announcementList where businessType equals to UPDATED_BUSINESS_TYPE
        defaultAnnouncementShouldNotBeFound("businessType.in=" + UPDATED_BUSINESS_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where businessType is not null
        defaultAnnouncementShouldBeFound("businessType.specified=true");

        // Get all the announcementList where businessType is null
        defaultAnnouncementShouldNotBeFound("businessType.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where businessId equals to DEFAULT_BUSINESS_ID
        defaultAnnouncementShouldBeFound("businessId.equals=" + DEFAULT_BUSINESS_ID);

        // Get all the announcementList where businessId equals to UPDATED_BUSINESS_ID
        defaultAnnouncementShouldNotBeFound("businessId.equals=" + UPDATED_BUSINESS_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where businessId not equals to DEFAULT_BUSINESS_ID
        defaultAnnouncementShouldNotBeFound("businessId.notEquals=" + DEFAULT_BUSINESS_ID);

        // Get all the announcementList where businessId not equals to UPDATED_BUSINESS_ID
        defaultAnnouncementShouldBeFound("businessId.notEquals=" + UPDATED_BUSINESS_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where businessId in DEFAULT_BUSINESS_ID or UPDATED_BUSINESS_ID
        defaultAnnouncementShouldBeFound("businessId.in=" + DEFAULT_BUSINESS_ID + "," + UPDATED_BUSINESS_ID);

        // Get all the announcementList where businessId equals to UPDATED_BUSINESS_ID
        defaultAnnouncementShouldNotBeFound("businessId.in=" + UPDATED_BUSINESS_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where businessId is not null
        defaultAnnouncementShouldBeFound("businessId.specified=true");

        // Get all the announcementList where businessId is null
        defaultAnnouncementShouldNotBeFound("businessId.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where businessId is greater than or equal to DEFAULT_BUSINESS_ID
        defaultAnnouncementShouldBeFound("businessId.greaterThanOrEqual=" + DEFAULT_BUSINESS_ID);

        // Get all the announcementList where businessId is greater than or equal to UPDATED_BUSINESS_ID
        defaultAnnouncementShouldNotBeFound("businessId.greaterThanOrEqual=" + UPDATED_BUSINESS_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where businessId is less than or equal to DEFAULT_BUSINESS_ID
        defaultAnnouncementShouldBeFound("businessId.lessThanOrEqual=" + DEFAULT_BUSINESS_ID);

        // Get all the announcementList where businessId is less than or equal to SMALLER_BUSINESS_ID
        defaultAnnouncementShouldNotBeFound("businessId.lessThanOrEqual=" + SMALLER_BUSINESS_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where businessId is less than DEFAULT_BUSINESS_ID
        defaultAnnouncementShouldNotBeFound("businessId.lessThan=" + DEFAULT_BUSINESS_ID);

        // Get all the announcementList where businessId is less than UPDATED_BUSINESS_ID
        defaultAnnouncementShouldBeFound("businessId.lessThan=" + UPDATED_BUSINESS_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByBusinessIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where businessId is greater than DEFAULT_BUSINESS_ID
        defaultAnnouncementShouldNotBeFound("businessId.greaterThan=" + DEFAULT_BUSINESS_ID);

        // Get all the announcementList where businessId is greater than SMALLER_BUSINESS_ID
        defaultAnnouncementShouldBeFound("businessId.greaterThan=" + SMALLER_BUSINESS_ID);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where openType equals to DEFAULT_OPEN_TYPE
        defaultAnnouncementShouldBeFound("openType.equals=" + DEFAULT_OPEN_TYPE);

        // Get all the announcementList where openType equals to UPDATED_OPEN_TYPE
        defaultAnnouncementShouldNotBeFound("openType.equals=" + UPDATED_OPEN_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where openType not equals to DEFAULT_OPEN_TYPE
        defaultAnnouncementShouldNotBeFound("openType.notEquals=" + DEFAULT_OPEN_TYPE);

        // Get all the announcementList where openType not equals to UPDATED_OPEN_TYPE
        defaultAnnouncementShouldBeFound("openType.notEquals=" + UPDATED_OPEN_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenTypeIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where openType in DEFAULT_OPEN_TYPE or UPDATED_OPEN_TYPE
        defaultAnnouncementShouldBeFound("openType.in=" + DEFAULT_OPEN_TYPE + "," + UPDATED_OPEN_TYPE);

        // Get all the announcementList where openType equals to UPDATED_OPEN_TYPE
        defaultAnnouncementShouldNotBeFound("openType.in=" + UPDATED_OPEN_TYPE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where openType is not null
        defaultAnnouncementShouldBeFound("openType.specified=true");

        // Get all the announcementList where openType is null
        defaultAnnouncementShouldNotBeFound("openType.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenPageIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where openPage equals to DEFAULT_OPEN_PAGE
        defaultAnnouncementShouldBeFound("openPage.equals=" + DEFAULT_OPEN_PAGE);

        // Get all the announcementList where openPage equals to UPDATED_OPEN_PAGE
        defaultAnnouncementShouldNotBeFound("openPage.equals=" + UPDATED_OPEN_PAGE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenPageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where openPage not equals to DEFAULT_OPEN_PAGE
        defaultAnnouncementShouldNotBeFound("openPage.notEquals=" + DEFAULT_OPEN_PAGE);

        // Get all the announcementList where openPage not equals to UPDATED_OPEN_PAGE
        defaultAnnouncementShouldBeFound("openPage.notEquals=" + UPDATED_OPEN_PAGE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenPageIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where openPage in DEFAULT_OPEN_PAGE or UPDATED_OPEN_PAGE
        defaultAnnouncementShouldBeFound("openPage.in=" + DEFAULT_OPEN_PAGE + "," + UPDATED_OPEN_PAGE);

        // Get all the announcementList where openPage equals to UPDATED_OPEN_PAGE
        defaultAnnouncementShouldNotBeFound("openPage.in=" + UPDATED_OPEN_PAGE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenPageIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where openPage is not null
        defaultAnnouncementShouldBeFound("openPage.specified=true");

        // Get all the announcementList where openPage is null
        defaultAnnouncementShouldNotBeFound("openPage.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenPageContainsSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where openPage contains DEFAULT_OPEN_PAGE
        defaultAnnouncementShouldBeFound("openPage.contains=" + DEFAULT_OPEN_PAGE);

        // Get all the announcementList where openPage contains UPDATED_OPEN_PAGE
        defaultAnnouncementShouldNotBeFound("openPage.contains=" + UPDATED_OPEN_PAGE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByOpenPageNotContainsSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where openPage does not contain DEFAULT_OPEN_PAGE
        defaultAnnouncementShouldNotBeFound("openPage.doesNotContain=" + DEFAULT_OPEN_PAGE);

        // Get all the announcementList where openPage does not contain UPDATED_OPEN_PAGE
        defaultAnnouncementShouldBeFound("openPage.doesNotContain=" + UPDATED_OPEN_PAGE);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByReceiverIdsIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where receiverIds equals to DEFAULT_RECEIVER_IDS
        defaultAnnouncementShouldBeFound("receiverIds.equals=" + DEFAULT_RECEIVER_IDS);

        // Get all the announcementList where receiverIds equals to UPDATED_RECEIVER_IDS
        defaultAnnouncementShouldNotBeFound("receiverIds.equals=" + UPDATED_RECEIVER_IDS);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByReceiverIdsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where receiverIds not equals to DEFAULT_RECEIVER_IDS
        defaultAnnouncementShouldNotBeFound("receiverIds.notEquals=" + DEFAULT_RECEIVER_IDS);

        // Get all the announcementList where receiverIds not equals to UPDATED_RECEIVER_IDS
        defaultAnnouncementShouldBeFound("receiverIds.notEquals=" + UPDATED_RECEIVER_IDS);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByReceiverIdsIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where receiverIds in DEFAULT_RECEIVER_IDS or UPDATED_RECEIVER_IDS
        defaultAnnouncementShouldBeFound("receiverIds.in=" + DEFAULT_RECEIVER_IDS + "," + UPDATED_RECEIVER_IDS);

        // Get all the announcementList where receiverIds equals to UPDATED_RECEIVER_IDS
        defaultAnnouncementShouldNotBeFound("receiverIds.in=" + UPDATED_RECEIVER_IDS);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByReceiverIdsIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where receiverIds is not null
        defaultAnnouncementShouldBeFound("receiverIds.specified=true");

        // Get all the announcementList where receiverIds is null
        defaultAnnouncementShouldNotBeFound("receiverIds.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByRemovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where removedAt equals to DEFAULT_REMOVED_AT
        defaultAnnouncementShouldBeFound("removedAt.equals=" + DEFAULT_REMOVED_AT);

        // Get all the announcementList where removedAt equals to UPDATED_REMOVED_AT
        defaultAnnouncementShouldNotBeFound("removedAt.equals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByRemovedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where removedAt not equals to DEFAULT_REMOVED_AT
        defaultAnnouncementShouldNotBeFound("removedAt.notEquals=" + DEFAULT_REMOVED_AT);

        // Get all the announcementList where removedAt not equals to UPDATED_REMOVED_AT
        defaultAnnouncementShouldBeFound("removedAt.notEquals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByRemovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where removedAt in DEFAULT_REMOVED_AT or UPDATED_REMOVED_AT
        defaultAnnouncementShouldBeFound("removedAt.in=" + DEFAULT_REMOVED_AT + "," + UPDATED_REMOVED_AT);

        // Get all the announcementList where removedAt equals to UPDATED_REMOVED_AT
        defaultAnnouncementShouldNotBeFound("removedAt.in=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByRemovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where removedAt is not null
        defaultAnnouncementShouldBeFound("removedAt.specified=true");

        // Get all the announcementList where removedAt is null
        defaultAnnouncementShouldNotBeFound("removedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnouncementsByRemovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where removedAt is greater than or equal to DEFAULT_REMOVED_AT
        defaultAnnouncementShouldBeFound("removedAt.greaterThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the announcementList where removedAt is greater than or equal to UPDATED_REMOVED_AT
        defaultAnnouncementShouldNotBeFound("removedAt.greaterThanOrEqual=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByRemovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where removedAt is less than or equal to DEFAULT_REMOVED_AT
        defaultAnnouncementShouldBeFound("removedAt.lessThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the announcementList where removedAt is less than or equal to SMALLER_REMOVED_AT
        defaultAnnouncementShouldNotBeFound("removedAt.lessThanOrEqual=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByRemovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where removedAt is less than DEFAULT_REMOVED_AT
        defaultAnnouncementShouldNotBeFound("removedAt.lessThan=" + DEFAULT_REMOVED_AT);

        // Get all the announcementList where removedAt is less than UPDATED_REMOVED_AT
        defaultAnnouncementShouldBeFound("removedAt.lessThan=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllAnnouncementsByRemovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        // Get all the announcementList where removedAt is greater than DEFAULT_REMOVED_AT
        defaultAnnouncementShouldNotBeFound("removedAt.greaterThan=" + DEFAULT_REMOVED_AT);

        // Get all the announcementList where removedAt is greater than SMALLER_REMOVED_AT
        defaultAnnouncementShouldBeFound("removedAt.greaterThan=" + SMALLER_REMOVED_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnnouncementShouldBeFound(String filter) throws Exception {
        restAnnouncementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(announcement.getId().intValue())))
            .andExpect(jsonPath("$.[*].titile").value(hasItem(DEFAULT_TITILE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].senderId").value(hasItem(DEFAULT_SENDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].receiverType").value(hasItem(DEFAULT_RECEIVER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].sendStatus").value(hasItem(DEFAULT_SEND_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sendTime").value(hasItem(sameInstant(DEFAULT_SEND_TIME))))
            .andExpect(jsonPath("$.[*].cancelTime").value(hasItem(sameInstant(DEFAULT_CANCEL_TIME))))
            .andExpect(jsonPath("$.[*].businessType").value(hasItem(DEFAULT_BUSINESS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].businessId").value(hasItem(DEFAULT_BUSINESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].openType").value(hasItem(DEFAULT_OPEN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].openPage").value(hasItem(DEFAULT_OPEN_PAGE)))
            .andExpect(jsonPath("$.[*].receiverIds").value(hasItem(Arrays.toString(DEFAULT_RECEIVER_IDS))))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));

        // Check, that the count call also returns 1
        restAnnouncementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnnouncementShouldNotBeFound(String filter) throws Exception {
        restAnnouncementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnnouncementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAnnouncement() throws Exception {
        // Get the announcement
        restAnnouncementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAnnouncement() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        int databaseSizeBeforeUpdate = announcementRepository.findAll().size();

        // Update the announcement
        Announcement updatedAnnouncement = announcementRepository.findById(announcement.getId()).get();
        // Disconnect from session so that the updates on updatedAnnouncement are not directly saved in db
        updatedAnnouncement
            .titile(UPDATED_TITILE)
            .content(UPDATED_CONTENT)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .senderId(UPDATED_SENDER_ID)
            .priority(UPDATED_PRIORITY)
            .category(UPDATED_CATEGORY)
            .receiverType(UPDATED_RECEIVER_TYPE)
            .sendStatus(UPDATED_SEND_STATUS)
            .sendTime(UPDATED_SEND_TIME)
            .cancelTime(UPDATED_CANCEL_TIME)
            .businessType(UPDATED_BUSINESS_TYPE)
            .businessId(UPDATED_BUSINESS_ID)
            .openType(UPDATED_OPEN_TYPE)
            .openPage(UPDATED_OPEN_PAGE)
            .receiverIds(UPDATED_RECEIVER_IDS)
            .summary(UPDATED_SUMMARY)
            .removedAt(UPDATED_REMOVED_AT);
        AnnouncementDTO announcementDTO = announcementMapper.toDto(updatedAnnouncement);

        restAnnouncementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, announcementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(announcementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeUpdate);
        Announcement testAnnouncement = announcementList.get(announcementList.size() - 1);
        assertThat(testAnnouncement.getTitile()).isEqualTo(UPDATED_TITILE);
        assertThat(testAnnouncement.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAnnouncement.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testAnnouncement.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testAnnouncement.getSenderId()).isEqualTo(UPDATED_SENDER_ID);
        assertThat(testAnnouncement.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testAnnouncement.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testAnnouncement.getReceiverType()).isEqualTo(UPDATED_RECEIVER_TYPE);
        assertThat(testAnnouncement.getSendStatus()).isEqualTo(UPDATED_SEND_STATUS);
        assertThat(testAnnouncement.getSendTime()).isEqualTo(UPDATED_SEND_TIME);
        assertThat(testAnnouncement.getCancelTime()).isEqualTo(UPDATED_CANCEL_TIME);
        assertThat(testAnnouncement.getBusinessType()).isEqualTo(UPDATED_BUSINESS_TYPE);
        assertThat(testAnnouncement.getBusinessId()).isEqualTo(UPDATED_BUSINESS_ID);
        assertThat(testAnnouncement.getOpenType()).isEqualTo(UPDATED_OPEN_TYPE);
        assertThat(testAnnouncement.getOpenPage()).isEqualTo(UPDATED_OPEN_PAGE);
        assertThat(testAnnouncement.getReceiverIds()).isEqualTo(UPDATED_RECEIVER_IDS);
        assertThat(testAnnouncement.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testAnnouncement.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void putNonExistingAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = announcementRepository.findAll().size();
        announcement.setId(count.incrementAndGet());

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnouncementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, announcementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(announcementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = announcementRepository.findAll().size();
        announcement.setId(count.incrementAndGet());

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(announcementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = announcementRepository.findAll().size();
        announcement.setId(count.incrementAndGet());

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(announcementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnnouncementWithPatch() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        int databaseSizeBeforeUpdate = announcementRepository.findAll().size();

        // Update the announcement using partial update
        Announcement partialUpdatedAnnouncement = new Announcement();
        partialUpdatedAnnouncement.setId(announcement.getId());

        partialUpdatedAnnouncement
            .titile(UPDATED_TITILE)
            .endTime(UPDATED_END_TIME)
            .category(UPDATED_CATEGORY)
            .receiverType(UPDATED_RECEIVER_TYPE)
            .businessId(UPDATED_BUSINESS_ID)
            .openType(UPDATED_OPEN_TYPE)
            .summary(UPDATED_SUMMARY);

        restAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnouncement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnouncement))
            )
            .andExpect(status().isOk());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeUpdate);
        Announcement testAnnouncement = announcementList.get(announcementList.size() - 1);
        assertThat(testAnnouncement.getTitile()).isEqualTo(UPDATED_TITILE);
        assertThat(testAnnouncement.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testAnnouncement.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testAnnouncement.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testAnnouncement.getSenderId()).isEqualTo(DEFAULT_SENDER_ID);
        assertThat(testAnnouncement.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testAnnouncement.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testAnnouncement.getReceiverType()).isEqualTo(UPDATED_RECEIVER_TYPE);
        assertThat(testAnnouncement.getSendStatus()).isEqualTo(DEFAULT_SEND_STATUS);
        assertThat(testAnnouncement.getSendTime()).isEqualTo(DEFAULT_SEND_TIME);
        assertThat(testAnnouncement.getCancelTime()).isEqualTo(DEFAULT_CANCEL_TIME);
        assertThat(testAnnouncement.getBusinessType()).isEqualTo(DEFAULT_BUSINESS_TYPE);
        assertThat(testAnnouncement.getBusinessId()).isEqualTo(UPDATED_BUSINESS_ID);
        assertThat(testAnnouncement.getOpenType()).isEqualTo(UPDATED_OPEN_TYPE);
        assertThat(testAnnouncement.getOpenPage()).isEqualTo(DEFAULT_OPEN_PAGE);
        assertThat(testAnnouncement.getReceiverIds()).isEqualTo(DEFAULT_RECEIVER_IDS);
        assertThat(testAnnouncement.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testAnnouncement.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void fullUpdateAnnouncementWithPatch() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        int databaseSizeBeforeUpdate = announcementRepository.findAll().size();

        // Update the announcement using partial update
        Announcement partialUpdatedAnnouncement = new Announcement();
        partialUpdatedAnnouncement.setId(announcement.getId());

        partialUpdatedAnnouncement
            .titile(UPDATED_TITILE)
            .content(UPDATED_CONTENT)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .senderId(UPDATED_SENDER_ID)
            .priority(UPDATED_PRIORITY)
            .category(UPDATED_CATEGORY)
            .receiverType(UPDATED_RECEIVER_TYPE)
            .sendStatus(UPDATED_SEND_STATUS)
            .sendTime(UPDATED_SEND_TIME)
            .cancelTime(UPDATED_CANCEL_TIME)
            .businessType(UPDATED_BUSINESS_TYPE)
            .businessId(UPDATED_BUSINESS_ID)
            .openType(UPDATED_OPEN_TYPE)
            .openPage(UPDATED_OPEN_PAGE)
            .receiverIds(UPDATED_RECEIVER_IDS)
            .summary(UPDATED_SUMMARY)
            .removedAt(UPDATED_REMOVED_AT);

        restAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnouncement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnouncement))
            )
            .andExpect(status().isOk());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeUpdate);
        Announcement testAnnouncement = announcementList.get(announcementList.size() - 1);
        assertThat(testAnnouncement.getTitile()).isEqualTo(UPDATED_TITILE);
        assertThat(testAnnouncement.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAnnouncement.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testAnnouncement.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testAnnouncement.getSenderId()).isEqualTo(UPDATED_SENDER_ID);
        assertThat(testAnnouncement.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testAnnouncement.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testAnnouncement.getReceiverType()).isEqualTo(UPDATED_RECEIVER_TYPE);
        assertThat(testAnnouncement.getSendStatus()).isEqualTo(UPDATED_SEND_STATUS);
        assertThat(testAnnouncement.getSendTime()).isEqualTo(UPDATED_SEND_TIME);
        assertThat(testAnnouncement.getCancelTime()).isEqualTo(UPDATED_CANCEL_TIME);
        assertThat(testAnnouncement.getBusinessType()).isEqualTo(UPDATED_BUSINESS_TYPE);
        assertThat(testAnnouncement.getBusinessId()).isEqualTo(UPDATED_BUSINESS_ID);
        assertThat(testAnnouncement.getOpenType()).isEqualTo(UPDATED_OPEN_TYPE);
        assertThat(testAnnouncement.getOpenPage()).isEqualTo(UPDATED_OPEN_PAGE);
        assertThat(testAnnouncement.getReceiverIds()).isEqualTo(UPDATED_RECEIVER_IDS);
        assertThat(testAnnouncement.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testAnnouncement.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = announcementRepository.findAll().size();
        announcement.setId(count.incrementAndGet());

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, announcementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(announcementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = announcementRepository.findAll().size();
        announcement.setId(count.incrementAndGet());

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(announcementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = announcementRepository.findAll().size();
        announcement.setId(count.incrementAndGet());

        // Create the Announcement
        AnnouncementDTO announcementDTO = announcementMapper.toDto(announcement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnouncementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(announcementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Announcement in the database
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnnouncement() throws Exception {
        // Initialize the database
        announcementRepository.insert(announcement);

        int databaseSizeBeforeDelete = announcementRepository.findAll().size();

        // Delete the announcement
        restAnnouncementMockMvc
            .perform(delete(ENTITY_API_URL_ID, announcement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Announcement> announcementList = announcementRepository.findAll();
        assertThat(announcementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
