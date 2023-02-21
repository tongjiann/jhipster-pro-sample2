package com.mycompany.myapp.system.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.enumeration.LogType;
import com.mycompany.myapp.system.domain.SysLog;
import com.mycompany.myapp.system.repository.SysLogRepository;
import com.mycompany.myapp.system.service.SysLogQueryService;
import com.mycompany.myapp.system.service.criteria.SysLogCriteria;
import com.mycompany.myapp.system.service.dto.SysLogDTO;
import com.mycompany.myapp.system.service.mapper.SysLogMapper;
import com.mycompany.myapp.web.rest.TestUtil;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SysLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SysLogResourceIT {

    private static final LogType DEFAULT_LOG_TYPE = LogType.LOGIN;
    private static final LogType UPDATED_LOG_TYPE = LogType.OPERATE;

    private static final String DEFAULT_LOG_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_LOG_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_OPERATE_TYPE = 1;
    private static final Integer UPDATED_OPERATE_TYPE = 2;
    private static final Integer SMALLER_OPERATE_TYPE = 1 - 1;

    private static final String DEFAULT_USERID = "AAAAAAAAAA";
    private static final String UPDATED_USERID = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final String DEFAULT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_URL = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_URL = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_PARAM = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_PARAM = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_COST_TIME = 1L;
    private static final Long UPDATED_COST_TIME = 2L;
    private static final Long SMALLER_COST_TIME = 1L - 1L;

    private static final LocalDate DEFAULT_REMOVED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMOVED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REMOVED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/sys-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SysLogRepository sysLogRepository;

    @Autowired
    private SysLogMapper sysLogMapper;

    @Autowired
    private MockMvc restSysLogMockMvc;

    private SysLog sysLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysLog createEntity() {
        SysLog sysLog = new SysLog()
            .logType(DEFAULT_LOG_TYPE)
            .logContent(DEFAULT_LOG_CONTENT)
            .operateType(DEFAULT_OPERATE_TYPE)
            .userid(DEFAULT_USERID)
            .username(DEFAULT_USERNAME)
            .ip(DEFAULT_IP)
            .method(DEFAULT_METHOD)
            .requestUrl(DEFAULT_REQUEST_URL)
            .requestParam(DEFAULT_REQUEST_PARAM)
            .requestType(DEFAULT_REQUEST_TYPE)
            .costTime(DEFAULT_COST_TIME)
            .removedAt(DEFAULT_REMOVED_AT);
        return sysLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysLog createUpdatedEntity() {
        SysLog sysLog = new SysLog()
            .logType(UPDATED_LOG_TYPE)
            .logContent(UPDATED_LOG_CONTENT)
            .operateType(UPDATED_OPERATE_TYPE)
            .userid(UPDATED_USERID)
            .username(UPDATED_USERNAME)
            .ip(UPDATED_IP)
            .method(UPDATED_METHOD)
            .requestUrl(UPDATED_REQUEST_URL)
            .requestParam(UPDATED_REQUEST_PARAM)
            .requestType(UPDATED_REQUEST_TYPE)
            .costTime(UPDATED_COST_TIME)
            .removedAt(UPDATED_REMOVED_AT);
        return sysLog;
    }

    @BeforeEach
    public void initTest() {
        sysLog = createEntity();
    }

    @Test
    @Transactional
    void createSysLog() throws Exception {
        int databaseSizeBeforeCreate = sysLogRepository.findAll().size();
        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);
        restSysLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysLogDTO)))
            .andExpect(status().isCreated());

        // Validate the SysLog in the database
        List<SysLog> sysLogList = sysLogRepository.findAll();
        assertThat(sysLogList).hasSize(databaseSizeBeforeCreate + 1);
        SysLog testSysLog = sysLogList.get(sysLogList.size() - 1);
        assertThat(testSysLog.getLogType()).isEqualTo(DEFAULT_LOG_TYPE);
        assertThat(testSysLog.getLogContent()).isEqualTo(DEFAULT_LOG_CONTENT);
        assertThat(testSysLog.getOperateType()).isEqualTo(DEFAULT_OPERATE_TYPE);
        assertThat(testSysLog.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testSysLog.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testSysLog.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testSysLog.getMethod()).isEqualTo(DEFAULT_METHOD);
        assertThat(testSysLog.getRequestUrl()).isEqualTo(DEFAULT_REQUEST_URL);
        assertThat(testSysLog.getRequestParam()).isEqualTo(DEFAULT_REQUEST_PARAM);
        assertThat(testSysLog.getRequestType()).isEqualTo(DEFAULT_REQUEST_TYPE);
        assertThat(testSysLog.getCostTime()).isEqualTo(DEFAULT_COST_TIME);
        assertThat(testSysLog.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void createSysLogWithExistingId() throws Exception {
        // Create the SysLog with an existing ID
        sysLog.setId(1L);
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        int databaseSizeBeforeCreate = sysLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SysLog in the database
        List<SysLog> sysLogList = sysLogRepository.findAll();
        assertThat(sysLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSysLogs() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList
        restSysLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].logType").value(hasItem(DEFAULT_LOG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].logContent").value(hasItem(DEFAULT_LOG_CONTENT)))
            .andExpect(jsonPath("$.[*].operateType").value(hasItem(DEFAULT_OPERATE_TYPE)))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].requestUrl").value(hasItem(DEFAULT_REQUEST_URL)))
            .andExpect(jsonPath("$.[*].requestParam").value(hasItem(DEFAULT_REQUEST_PARAM.toString())))
            .andExpect(jsonPath("$.[*].requestType").value(hasItem(DEFAULT_REQUEST_TYPE)))
            .andExpect(jsonPath("$.[*].costTime").value(hasItem(DEFAULT_COST_TIME.intValue())))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));
    }

    @Test
    @Transactional
    void getSysLog() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get the sysLog
        restSysLogMockMvc
            .perform(get(ENTITY_API_URL_ID, sysLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sysLog.getId().intValue()))
            .andExpect(jsonPath("$.logType").value(DEFAULT_LOG_TYPE.toString()))
            .andExpect(jsonPath("$.logContent").value(DEFAULT_LOG_CONTENT))
            .andExpect(jsonPath("$.operateType").value(DEFAULT_OPERATE_TYPE))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD))
            .andExpect(jsonPath("$.requestUrl").value(DEFAULT_REQUEST_URL))
            .andExpect(jsonPath("$.requestParam").value(DEFAULT_REQUEST_PARAM.toString()))
            .andExpect(jsonPath("$.requestType").value(DEFAULT_REQUEST_TYPE))
            .andExpect(jsonPath("$.costTime").value(DEFAULT_COST_TIME.intValue()))
            .andExpect(jsonPath("$.removedAt").value(DEFAULT_REMOVED_AT.toString()));
    }

    @Test
    @Transactional
    void getSysLogsByIdFiltering() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        Long id = sysLog.getId();

        defaultSysLogShouldBeFound("id.equals=" + id);
        defaultSysLogShouldNotBeFound("id.notEquals=" + id);

        defaultSysLogShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSysLogShouldNotBeFound("id.greaterThan=" + id);

        defaultSysLogShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSysLogShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where logType equals to DEFAULT_LOG_TYPE
        defaultSysLogShouldBeFound("logType.equals=" + DEFAULT_LOG_TYPE);

        // Get all the sysLogList where logType equals to UPDATED_LOG_TYPE
        defaultSysLogShouldNotBeFound("logType.equals=" + UPDATED_LOG_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where logType not equals to DEFAULT_LOG_TYPE
        defaultSysLogShouldNotBeFound("logType.notEquals=" + DEFAULT_LOG_TYPE);

        // Get all the sysLogList where logType not equals to UPDATED_LOG_TYPE
        defaultSysLogShouldBeFound("logType.notEquals=" + UPDATED_LOG_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogTypeIsInShouldWork() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where logType in DEFAULT_LOG_TYPE or UPDATED_LOG_TYPE
        defaultSysLogShouldBeFound("logType.in=" + DEFAULT_LOG_TYPE + "," + UPDATED_LOG_TYPE);

        // Get all the sysLogList where logType equals to UPDATED_LOG_TYPE
        defaultSysLogShouldNotBeFound("logType.in=" + UPDATED_LOG_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where logType is not null
        defaultSysLogShouldBeFound("logType.specified=true");

        // Get all the sysLogList where logType is null
        defaultSysLogShouldNotBeFound("logType.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByLogContentIsEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where logContent equals to DEFAULT_LOG_CONTENT
        defaultSysLogShouldBeFound("logContent.equals=" + DEFAULT_LOG_CONTENT);

        // Get all the sysLogList where logContent equals to UPDATED_LOG_CONTENT
        defaultSysLogShouldNotBeFound("logContent.equals=" + UPDATED_LOG_CONTENT);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where logContent not equals to DEFAULT_LOG_CONTENT
        defaultSysLogShouldNotBeFound("logContent.notEquals=" + DEFAULT_LOG_CONTENT);

        // Get all the sysLogList where logContent not equals to UPDATED_LOG_CONTENT
        defaultSysLogShouldBeFound("logContent.notEquals=" + UPDATED_LOG_CONTENT);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogContentIsInShouldWork() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where logContent in DEFAULT_LOG_CONTENT or UPDATED_LOG_CONTENT
        defaultSysLogShouldBeFound("logContent.in=" + DEFAULT_LOG_CONTENT + "," + UPDATED_LOG_CONTENT);

        // Get all the sysLogList where logContent equals to UPDATED_LOG_CONTENT
        defaultSysLogShouldNotBeFound("logContent.in=" + UPDATED_LOG_CONTENT);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where logContent is not null
        defaultSysLogShouldBeFound("logContent.specified=true");

        // Get all the sysLogList where logContent is null
        defaultSysLogShouldNotBeFound("logContent.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByLogContentContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where logContent contains DEFAULT_LOG_CONTENT
        defaultSysLogShouldBeFound("logContent.contains=" + DEFAULT_LOG_CONTENT);

        // Get all the sysLogList where logContent contains UPDATED_LOG_CONTENT
        defaultSysLogShouldNotBeFound("logContent.contains=" + UPDATED_LOG_CONTENT);
    }

    @Test
    @Transactional
    void getAllSysLogsByLogContentNotContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where logContent does not contain DEFAULT_LOG_CONTENT
        defaultSysLogShouldNotBeFound("logContent.doesNotContain=" + DEFAULT_LOG_CONTENT);

        // Get all the sysLogList where logContent does not contain UPDATED_LOG_CONTENT
        defaultSysLogShouldBeFound("logContent.doesNotContain=" + UPDATED_LOG_CONTENT);
    }

    @Test
    @Transactional
    void getAllSysLogsByOperateTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where operateType equals to DEFAULT_OPERATE_TYPE
        defaultSysLogShouldBeFound("operateType.equals=" + DEFAULT_OPERATE_TYPE);

        // Get all the sysLogList where operateType equals to UPDATED_OPERATE_TYPE
        defaultSysLogShouldNotBeFound("operateType.equals=" + UPDATED_OPERATE_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByOperateTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where operateType not equals to DEFAULT_OPERATE_TYPE
        defaultSysLogShouldNotBeFound("operateType.notEquals=" + DEFAULT_OPERATE_TYPE);

        // Get all the sysLogList where operateType not equals to UPDATED_OPERATE_TYPE
        defaultSysLogShouldBeFound("operateType.notEquals=" + UPDATED_OPERATE_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByOperateTypeIsInShouldWork() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where operateType in DEFAULT_OPERATE_TYPE or UPDATED_OPERATE_TYPE
        defaultSysLogShouldBeFound("operateType.in=" + DEFAULT_OPERATE_TYPE + "," + UPDATED_OPERATE_TYPE);

        // Get all the sysLogList where operateType equals to UPDATED_OPERATE_TYPE
        defaultSysLogShouldNotBeFound("operateType.in=" + UPDATED_OPERATE_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByOperateTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where operateType is not null
        defaultSysLogShouldBeFound("operateType.specified=true");

        // Get all the sysLogList where operateType is null
        defaultSysLogShouldNotBeFound("operateType.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByOperateTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where operateType is greater than or equal to DEFAULT_OPERATE_TYPE
        defaultSysLogShouldBeFound("operateType.greaterThanOrEqual=" + DEFAULT_OPERATE_TYPE);

        // Get all the sysLogList where operateType is greater than or equal to UPDATED_OPERATE_TYPE
        defaultSysLogShouldNotBeFound("operateType.greaterThanOrEqual=" + UPDATED_OPERATE_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByOperateTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where operateType is less than or equal to DEFAULT_OPERATE_TYPE
        defaultSysLogShouldBeFound("operateType.lessThanOrEqual=" + DEFAULT_OPERATE_TYPE);

        // Get all the sysLogList where operateType is less than or equal to SMALLER_OPERATE_TYPE
        defaultSysLogShouldNotBeFound("operateType.lessThanOrEqual=" + SMALLER_OPERATE_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByOperateTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where operateType is less than DEFAULT_OPERATE_TYPE
        defaultSysLogShouldNotBeFound("operateType.lessThan=" + DEFAULT_OPERATE_TYPE);

        // Get all the sysLogList where operateType is less than UPDATED_OPERATE_TYPE
        defaultSysLogShouldBeFound("operateType.lessThan=" + UPDATED_OPERATE_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByOperateTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where operateType is greater than DEFAULT_OPERATE_TYPE
        defaultSysLogShouldNotBeFound("operateType.greaterThan=" + DEFAULT_OPERATE_TYPE);

        // Get all the sysLogList where operateType is greater than SMALLER_OPERATE_TYPE
        defaultSysLogShouldBeFound("operateType.greaterThan=" + SMALLER_OPERATE_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByUseridIsEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where userid equals to DEFAULT_USERID
        defaultSysLogShouldBeFound("userid.equals=" + DEFAULT_USERID);

        // Get all the sysLogList where userid equals to UPDATED_USERID
        defaultSysLogShouldNotBeFound("userid.equals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    void getAllSysLogsByUseridIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where userid not equals to DEFAULT_USERID
        defaultSysLogShouldNotBeFound("userid.notEquals=" + DEFAULT_USERID);

        // Get all the sysLogList where userid not equals to UPDATED_USERID
        defaultSysLogShouldBeFound("userid.notEquals=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    void getAllSysLogsByUseridIsInShouldWork() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where userid in DEFAULT_USERID or UPDATED_USERID
        defaultSysLogShouldBeFound("userid.in=" + DEFAULT_USERID + "," + UPDATED_USERID);

        // Get all the sysLogList where userid equals to UPDATED_USERID
        defaultSysLogShouldNotBeFound("userid.in=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    void getAllSysLogsByUseridIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where userid is not null
        defaultSysLogShouldBeFound("userid.specified=true");

        // Get all the sysLogList where userid is null
        defaultSysLogShouldNotBeFound("userid.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByUseridContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where userid contains DEFAULT_USERID
        defaultSysLogShouldBeFound("userid.contains=" + DEFAULT_USERID);

        // Get all the sysLogList where userid contains UPDATED_USERID
        defaultSysLogShouldNotBeFound("userid.contains=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    void getAllSysLogsByUseridNotContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where userid does not contain DEFAULT_USERID
        defaultSysLogShouldNotBeFound("userid.doesNotContain=" + DEFAULT_USERID);

        // Get all the sysLogList where userid does not contain UPDATED_USERID
        defaultSysLogShouldBeFound("userid.doesNotContain=" + UPDATED_USERID);
    }

    @Test
    @Transactional
    void getAllSysLogsByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where username equals to DEFAULT_USERNAME
        defaultSysLogShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the sysLogList where username equals to UPDATED_USERNAME
        defaultSysLogShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSysLogsByUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where username not equals to DEFAULT_USERNAME
        defaultSysLogShouldNotBeFound("username.notEquals=" + DEFAULT_USERNAME);

        // Get all the sysLogList where username not equals to UPDATED_USERNAME
        defaultSysLogShouldBeFound("username.notEquals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSysLogsByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultSysLogShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the sysLogList where username equals to UPDATED_USERNAME
        defaultSysLogShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSysLogsByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where username is not null
        defaultSysLogShouldBeFound("username.specified=true");

        // Get all the sysLogList where username is null
        defaultSysLogShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByUsernameContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where username contains DEFAULT_USERNAME
        defaultSysLogShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the sysLogList where username contains UPDATED_USERNAME
        defaultSysLogShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSysLogsByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where username does not contain DEFAULT_USERNAME
        defaultSysLogShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the sysLogList where username does not contain UPDATED_USERNAME
        defaultSysLogShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSysLogsByIpIsEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where ip equals to DEFAULT_IP
        defaultSysLogShouldBeFound("ip.equals=" + DEFAULT_IP);

        // Get all the sysLogList where ip equals to UPDATED_IP
        defaultSysLogShouldNotBeFound("ip.equals=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllSysLogsByIpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where ip not equals to DEFAULT_IP
        defaultSysLogShouldNotBeFound("ip.notEquals=" + DEFAULT_IP);

        // Get all the sysLogList where ip not equals to UPDATED_IP
        defaultSysLogShouldBeFound("ip.notEquals=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllSysLogsByIpIsInShouldWork() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where ip in DEFAULT_IP or UPDATED_IP
        defaultSysLogShouldBeFound("ip.in=" + DEFAULT_IP + "," + UPDATED_IP);

        // Get all the sysLogList where ip equals to UPDATED_IP
        defaultSysLogShouldNotBeFound("ip.in=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllSysLogsByIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where ip is not null
        defaultSysLogShouldBeFound("ip.specified=true");

        // Get all the sysLogList where ip is null
        defaultSysLogShouldNotBeFound("ip.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByIpContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where ip contains DEFAULT_IP
        defaultSysLogShouldBeFound("ip.contains=" + DEFAULT_IP);

        // Get all the sysLogList where ip contains UPDATED_IP
        defaultSysLogShouldNotBeFound("ip.contains=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllSysLogsByIpNotContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where ip does not contain DEFAULT_IP
        defaultSysLogShouldNotBeFound("ip.doesNotContain=" + DEFAULT_IP);

        // Get all the sysLogList where ip does not contain UPDATED_IP
        defaultSysLogShouldBeFound("ip.doesNotContain=" + UPDATED_IP);
    }

    @Test
    @Transactional
    void getAllSysLogsByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where method equals to DEFAULT_METHOD
        defaultSysLogShouldBeFound("method.equals=" + DEFAULT_METHOD);

        // Get all the sysLogList where method equals to UPDATED_METHOD
        defaultSysLogShouldNotBeFound("method.equals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllSysLogsByMethodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where method not equals to DEFAULT_METHOD
        defaultSysLogShouldNotBeFound("method.notEquals=" + DEFAULT_METHOD);

        // Get all the sysLogList where method not equals to UPDATED_METHOD
        defaultSysLogShouldBeFound("method.notEquals=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllSysLogsByMethodIsInShouldWork() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where method in DEFAULT_METHOD or UPDATED_METHOD
        defaultSysLogShouldBeFound("method.in=" + DEFAULT_METHOD + "," + UPDATED_METHOD);

        // Get all the sysLogList where method equals to UPDATED_METHOD
        defaultSysLogShouldNotBeFound("method.in=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllSysLogsByMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where method is not null
        defaultSysLogShouldBeFound("method.specified=true");

        // Get all the sysLogList where method is null
        defaultSysLogShouldNotBeFound("method.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByMethodContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where method contains DEFAULT_METHOD
        defaultSysLogShouldBeFound("method.contains=" + DEFAULT_METHOD);

        // Get all the sysLogList where method contains UPDATED_METHOD
        defaultSysLogShouldNotBeFound("method.contains=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllSysLogsByMethodNotContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where method does not contain DEFAULT_METHOD
        defaultSysLogShouldNotBeFound("method.doesNotContain=" + DEFAULT_METHOD);

        // Get all the sysLogList where method does not contain UPDATED_METHOD
        defaultSysLogShouldBeFound("method.doesNotContain=" + UPDATED_METHOD);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where requestUrl equals to DEFAULT_REQUEST_URL
        defaultSysLogShouldBeFound("requestUrl.equals=" + DEFAULT_REQUEST_URL);

        // Get all the sysLogList where requestUrl equals to UPDATED_REQUEST_URL
        defaultSysLogShouldNotBeFound("requestUrl.equals=" + UPDATED_REQUEST_URL);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where requestUrl not equals to DEFAULT_REQUEST_URL
        defaultSysLogShouldNotBeFound("requestUrl.notEquals=" + DEFAULT_REQUEST_URL);

        // Get all the sysLogList where requestUrl not equals to UPDATED_REQUEST_URL
        defaultSysLogShouldBeFound("requestUrl.notEquals=" + UPDATED_REQUEST_URL);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestUrlIsInShouldWork() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where requestUrl in DEFAULT_REQUEST_URL or UPDATED_REQUEST_URL
        defaultSysLogShouldBeFound("requestUrl.in=" + DEFAULT_REQUEST_URL + "," + UPDATED_REQUEST_URL);

        // Get all the sysLogList where requestUrl equals to UPDATED_REQUEST_URL
        defaultSysLogShouldNotBeFound("requestUrl.in=" + UPDATED_REQUEST_URL);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where requestUrl is not null
        defaultSysLogShouldBeFound("requestUrl.specified=true");

        // Get all the sysLogList where requestUrl is null
        defaultSysLogShouldNotBeFound("requestUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestUrlContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where requestUrl contains DEFAULT_REQUEST_URL
        defaultSysLogShouldBeFound("requestUrl.contains=" + DEFAULT_REQUEST_URL);

        // Get all the sysLogList where requestUrl contains UPDATED_REQUEST_URL
        defaultSysLogShouldNotBeFound("requestUrl.contains=" + UPDATED_REQUEST_URL);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestUrlNotContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where requestUrl does not contain DEFAULT_REQUEST_URL
        defaultSysLogShouldNotBeFound("requestUrl.doesNotContain=" + DEFAULT_REQUEST_URL);

        // Get all the sysLogList where requestUrl does not contain UPDATED_REQUEST_URL
        defaultSysLogShouldBeFound("requestUrl.doesNotContain=" + UPDATED_REQUEST_URL);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where requestType equals to DEFAULT_REQUEST_TYPE
        defaultSysLogShouldBeFound("requestType.equals=" + DEFAULT_REQUEST_TYPE);

        // Get all the sysLogList where requestType equals to UPDATED_REQUEST_TYPE
        defaultSysLogShouldNotBeFound("requestType.equals=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where requestType not equals to DEFAULT_REQUEST_TYPE
        defaultSysLogShouldNotBeFound("requestType.notEquals=" + DEFAULT_REQUEST_TYPE);

        // Get all the sysLogList where requestType not equals to UPDATED_REQUEST_TYPE
        defaultSysLogShouldBeFound("requestType.notEquals=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestTypeIsInShouldWork() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where requestType in DEFAULT_REQUEST_TYPE or UPDATED_REQUEST_TYPE
        defaultSysLogShouldBeFound("requestType.in=" + DEFAULT_REQUEST_TYPE + "," + UPDATED_REQUEST_TYPE);

        // Get all the sysLogList where requestType equals to UPDATED_REQUEST_TYPE
        defaultSysLogShouldNotBeFound("requestType.in=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where requestType is not null
        defaultSysLogShouldBeFound("requestType.specified=true");

        // Get all the sysLogList where requestType is null
        defaultSysLogShouldNotBeFound("requestType.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestTypeContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where requestType contains DEFAULT_REQUEST_TYPE
        defaultSysLogShouldBeFound("requestType.contains=" + DEFAULT_REQUEST_TYPE);

        // Get all the sysLogList where requestType contains UPDATED_REQUEST_TYPE
        defaultSysLogShouldNotBeFound("requestType.contains=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByRequestTypeNotContainsSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where requestType does not contain DEFAULT_REQUEST_TYPE
        defaultSysLogShouldNotBeFound("requestType.doesNotContain=" + DEFAULT_REQUEST_TYPE);

        // Get all the sysLogList where requestType does not contain UPDATED_REQUEST_TYPE
        defaultSysLogShouldBeFound("requestType.doesNotContain=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where costTime equals to DEFAULT_COST_TIME
        defaultSysLogShouldBeFound("costTime.equals=" + DEFAULT_COST_TIME);

        // Get all the sysLogList where costTime equals to UPDATED_COST_TIME
        defaultSysLogShouldNotBeFound("costTime.equals=" + UPDATED_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where costTime not equals to DEFAULT_COST_TIME
        defaultSysLogShouldNotBeFound("costTime.notEquals=" + DEFAULT_COST_TIME);

        // Get all the sysLogList where costTime not equals to UPDATED_COST_TIME
        defaultSysLogShouldBeFound("costTime.notEquals=" + UPDATED_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsInShouldWork() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where costTime in DEFAULT_COST_TIME or UPDATED_COST_TIME
        defaultSysLogShouldBeFound("costTime.in=" + DEFAULT_COST_TIME + "," + UPDATED_COST_TIME);

        // Get all the sysLogList where costTime equals to UPDATED_COST_TIME
        defaultSysLogShouldNotBeFound("costTime.in=" + UPDATED_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where costTime is not null
        defaultSysLogShouldBeFound("costTime.specified=true");

        // Get all the sysLogList where costTime is null
        defaultSysLogShouldNotBeFound("costTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where costTime is greater than or equal to DEFAULT_COST_TIME
        defaultSysLogShouldBeFound("costTime.greaterThanOrEqual=" + DEFAULT_COST_TIME);

        // Get all the sysLogList where costTime is greater than or equal to UPDATED_COST_TIME
        defaultSysLogShouldNotBeFound("costTime.greaterThanOrEqual=" + UPDATED_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where costTime is less than or equal to DEFAULT_COST_TIME
        defaultSysLogShouldBeFound("costTime.lessThanOrEqual=" + DEFAULT_COST_TIME);

        // Get all the sysLogList where costTime is less than or equal to SMALLER_COST_TIME
        defaultSysLogShouldNotBeFound("costTime.lessThanOrEqual=" + SMALLER_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where costTime is less than DEFAULT_COST_TIME
        defaultSysLogShouldNotBeFound("costTime.lessThan=" + DEFAULT_COST_TIME);

        // Get all the sysLogList where costTime is less than UPDATED_COST_TIME
        defaultSysLogShouldBeFound("costTime.lessThan=" + UPDATED_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByCostTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where costTime is greater than DEFAULT_COST_TIME
        defaultSysLogShouldNotBeFound("costTime.greaterThan=" + DEFAULT_COST_TIME);

        // Get all the sysLogList where costTime is greater than SMALLER_COST_TIME
        defaultSysLogShouldBeFound("costTime.greaterThan=" + SMALLER_COST_TIME);
    }

    @Test
    @Transactional
    void getAllSysLogsByRemovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where removedAt equals to DEFAULT_REMOVED_AT
        defaultSysLogShouldBeFound("removedAt.equals=" + DEFAULT_REMOVED_AT);

        // Get all the sysLogList where removedAt equals to UPDATED_REMOVED_AT
        defaultSysLogShouldNotBeFound("removedAt.equals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSysLogsByRemovedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where removedAt not equals to DEFAULT_REMOVED_AT
        defaultSysLogShouldNotBeFound("removedAt.notEquals=" + DEFAULT_REMOVED_AT);

        // Get all the sysLogList where removedAt not equals to UPDATED_REMOVED_AT
        defaultSysLogShouldBeFound("removedAt.notEquals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSysLogsByRemovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where removedAt in DEFAULT_REMOVED_AT or UPDATED_REMOVED_AT
        defaultSysLogShouldBeFound("removedAt.in=" + DEFAULT_REMOVED_AT + "," + UPDATED_REMOVED_AT);

        // Get all the sysLogList where removedAt equals to UPDATED_REMOVED_AT
        defaultSysLogShouldNotBeFound("removedAt.in=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSysLogsByRemovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where removedAt is not null
        defaultSysLogShouldBeFound("removedAt.specified=true");

        // Get all the sysLogList where removedAt is null
        defaultSysLogShouldNotBeFound("removedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSysLogsByRemovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where removedAt is greater than or equal to DEFAULT_REMOVED_AT
        defaultSysLogShouldBeFound("removedAt.greaterThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the sysLogList where removedAt is greater than or equal to UPDATED_REMOVED_AT
        defaultSysLogShouldNotBeFound("removedAt.greaterThanOrEqual=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSysLogsByRemovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where removedAt is less than or equal to DEFAULT_REMOVED_AT
        defaultSysLogShouldBeFound("removedAt.lessThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the sysLogList where removedAt is less than or equal to SMALLER_REMOVED_AT
        defaultSysLogShouldNotBeFound("removedAt.lessThanOrEqual=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSysLogsByRemovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where removedAt is less than DEFAULT_REMOVED_AT
        defaultSysLogShouldNotBeFound("removedAt.lessThan=" + DEFAULT_REMOVED_AT);

        // Get all the sysLogList where removedAt is less than UPDATED_REMOVED_AT
        defaultSysLogShouldBeFound("removedAt.lessThan=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSysLogsByRemovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        // Get all the sysLogList where removedAt is greater than DEFAULT_REMOVED_AT
        defaultSysLogShouldNotBeFound("removedAt.greaterThan=" + DEFAULT_REMOVED_AT);

        // Get all the sysLogList where removedAt is greater than SMALLER_REMOVED_AT
        defaultSysLogShouldBeFound("removedAt.greaterThan=" + SMALLER_REMOVED_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSysLogShouldBeFound(String filter) throws Exception {
        restSysLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].logType").value(hasItem(DEFAULT_LOG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].logContent").value(hasItem(DEFAULT_LOG_CONTENT)))
            .andExpect(jsonPath("$.[*].operateType").value(hasItem(DEFAULT_OPERATE_TYPE)))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].requestUrl").value(hasItem(DEFAULT_REQUEST_URL)))
            .andExpect(jsonPath("$.[*].requestParam").value(hasItem(DEFAULT_REQUEST_PARAM.toString())))
            .andExpect(jsonPath("$.[*].requestType").value(hasItem(DEFAULT_REQUEST_TYPE)))
            .andExpect(jsonPath("$.[*].costTime").value(hasItem(DEFAULT_COST_TIME.intValue())))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));

        // Check, that the count call also returns 1
        restSysLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSysLogShouldNotBeFound(String filter) throws Exception {
        restSysLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSysLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSysLog() throws Exception {
        // Get the sysLog
        restSysLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSysLog() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        int databaseSizeBeforeUpdate = sysLogRepository.findAll().size();

        // Update the sysLog
        SysLog updatedSysLog = sysLogRepository.findById(sysLog.getId()).get();
        // Disconnect from session so that the updates on updatedSysLog are not directly saved in db
        updatedSysLog
            .logType(UPDATED_LOG_TYPE)
            .logContent(UPDATED_LOG_CONTENT)
            .operateType(UPDATED_OPERATE_TYPE)
            .userid(UPDATED_USERID)
            .username(UPDATED_USERNAME)
            .ip(UPDATED_IP)
            .method(UPDATED_METHOD)
            .requestUrl(UPDATED_REQUEST_URL)
            .requestParam(UPDATED_REQUEST_PARAM)
            .requestType(UPDATED_REQUEST_TYPE)
            .costTime(UPDATED_COST_TIME)
            .removedAt(UPDATED_REMOVED_AT);
        SysLogDTO sysLogDTO = sysLogMapper.toDto(updatedSysLog);

        restSysLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the SysLog in the database
        List<SysLog> sysLogList = sysLogRepository.findAll();
        assertThat(sysLogList).hasSize(databaseSizeBeforeUpdate);
        SysLog testSysLog = sysLogList.get(sysLogList.size() - 1);
        assertThat(testSysLog.getLogType()).isEqualTo(UPDATED_LOG_TYPE);
        assertThat(testSysLog.getLogContent()).isEqualTo(UPDATED_LOG_CONTENT);
        assertThat(testSysLog.getOperateType()).isEqualTo(UPDATED_OPERATE_TYPE);
        assertThat(testSysLog.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testSysLog.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testSysLog.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testSysLog.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testSysLog.getRequestUrl()).isEqualTo(UPDATED_REQUEST_URL);
        assertThat(testSysLog.getRequestParam()).isEqualTo(UPDATED_REQUEST_PARAM);
        assertThat(testSysLog.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testSysLog.getCostTime()).isEqualTo(UPDATED_COST_TIME);
        assertThat(testSysLog.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSysLog() throws Exception {
        int databaseSizeBeforeUpdate = sysLogRepository.findAll().size();
        sysLog.setId(count.incrementAndGet());

        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysLog in the database
        List<SysLog> sysLogList = sysLogRepository.findAll();
        assertThat(sysLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSysLog() throws Exception {
        int databaseSizeBeforeUpdate = sysLogRepository.findAll().size();
        sysLog.setId(count.incrementAndGet());

        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysLog in the database
        List<SysLog> sysLogList = sysLogRepository.findAll();
        assertThat(sysLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSysLog() throws Exception {
        int databaseSizeBeforeUpdate = sysLogRepository.findAll().size();
        sysLog.setId(count.incrementAndGet());

        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysLog in the database
        List<SysLog> sysLogList = sysLogRepository.findAll();
        assertThat(sysLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSysLogWithPatch() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        int databaseSizeBeforeUpdate = sysLogRepository.findAll().size();

        // Update the sysLog using partial update
        SysLog partialUpdatedSysLog = new SysLog();
        partialUpdatedSysLog.setId(sysLog.getId());

        partialUpdatedSysLog
            .operateType(UPDATED_OPERATE_TYPE)
            .userid(UPDATED_USERID)
            .username(UPDATED_USERNAME)
            .ip(UPDATED_IP)
            .method(UPDATED_METHOD)
            .requestUrl(UPDATED_REQUEST_URL)
            .requestParam(UPDATED_REQUEST_PARAM)
            .requestType(UPDATED_REQUEST_TYPE)
            .removedAt(UPDATED_REMOVED_AT);

        restSysLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysLog))
            )
            .andExpect(status().isOk());

        // Validate the SysLog in the database
        List<SysLog> sysLogList = sysLogRepository.findAll();
        assertThat(sysLogList).hasSize(databaseSizeBeforeUpdate);
        SysLog testSysLog = sysLogList.get(sysLogList.size() - 1);
        assertThat(testSysLog.getLogType()).isEqualTo(DEFAULT_LOG_TYPE);
        assertThat(testSysLog.getLogContent()).isEqualTo(DEFAULT_LOG_CONTENT);
        assertThat(testSysLog.getOperateType()).isEqualTo(UPDATED_OPERATE_TYPE);
        assertThat(testSysLog.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testSysLog.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testSysLog.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testSysLog.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testSysLog.getRequestUrl()).isEqualTo(UPDATED_REQUEST_URL);
        assertThat(testSysLog.getRequestParam()).isEqualTo(UPDATED_REQUEST_PARAM);
        assertThat(testSysLog.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testSysLog.getCostTime()).isEqualTo(DEFAULT_COST_TIME);
        assertThat(testSysLog.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSysLogWithPatch() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        int databaseSizeBeforeUpdate = sysLogRepository.findAll().size();

        // Update the sysLog using partial update
        SysLog partialUpdatedSysLog = new SysLog();
        partialUpdatedSysLog.setId(sysLog.getId());

        partialUpdatedSysLog
            .logType(UPDATED_LOG_TYPE)
            .logContent(UPDATED_LOG_CONTENT)
            .operateType(UPDATED_OPERATE_TYPE)
            .userid(UPDATED_USERID)
            .username(UPDATED_USERNAME)
            .ip(UPDATED_IP)
            .method(UPDATED_METHOD)
            .requestUrl(UPDATED_REQUEST_URL)
            .requestParam(UPDATED_REQUEST_PARAM)
            .requestType(UPDATED_REQUEST_TYPE)
            .costTime(UPDATED_COST_TIME)
            .removedAt(UPDATED_REMOVED_AT);

        restSysLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysLog))
            )
            .andExpect(status().isOk());

        // Validate the SysLog in the database
        List<SysLog> sysLogList = sysLogRepository.findAll();
        assertThat(sysLogList).hasSize(databaseSizeBeforeUpdate);
        SysLog testSysLog = sysLogList.get(sysLogList.size() - 1);
        assertThat(testSysLog.getLogType()).isEqualTo(UPDATED_LOG_TYPE);
        assertThat(testSysLog.getLogContent()).isEqualTo(UPDATED_LOG_CONTENT);
        assertThat(testSysLog.getOperateType()).isEqualTo(UPDATED_OPERATE_TYPE);
        assertThat(testSysLog.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testSysLog.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testSysLog.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testSysLog.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testSysLog.getRequestUrl()).isEqualTo(UPDATED_REQUEST_URL);
        assertThat(testSysLog.getRequestParam()).isEqualTo(UPDATED_REQUEST_PARAM);
        assertThat(testSysLog.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testSysLog.getCostTime()).isEqualTo(UPDATED_COST_TIME);
        assertThat(testSysLog.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSysLog() throws Exception {
        int databaseSizeBeforeUpdate = sysLogRepository.findAll().size();
        sysLog.setId(count.incrementAndGet());

        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sysLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysLog in the database
        List<SysLog> sysLogList = sysLogRepository.findAll();
        assertThat(sysLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSysLog() throws Exception {
        int databaseSizeBeforeUpdate = sysLogRepository.findAll().size();
        sysLog.setId(count.incrementAndGet());

        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysLog in the database
        List<SysLog> sysLogList = sysLogRepository.findAll();
        assertThat(sysLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSysLog() throws Exception {
        int databaseSizeBeforeUpdate = sysLogRepository.findAll().size();
        sysLog.setId(count.incrementAndGet());

        // Create the SysLog
        SysLogDTO sysLogDTO = sysLogMapper.toDto(sysLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysLogMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sysLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysLog in the database
        List<SysLog> sysLogList = sysLogRepository.findAll();
        assertThat(sysLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSysLog() throws Exception {
        // Initialize the database
        sysLogRepository.insert(sysLog);

        int databaseSizeBeforeDelete = sysLogRepository.findAll().size();

        // Delete the sysLog
        restSysLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, sysLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SysLog> sysLogList = sysLogRepository.findAll();
        assertThat(sysLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
