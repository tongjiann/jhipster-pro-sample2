package com.mycompany.myapp.system.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.enumeration.MessageSendType;
import com.mycompany.myapp.system.domain.SmsTemplate;
import com.mycompany.myapp.system.repository.SmsTemplateRepository;
import com.mycompany.myapp.system.service.SmsTemplateQueryService;
import com.mycompany.myapp.system.service.criteria.SmsTemplateCriteria;
import com.mycompany.myapp.system.service.dto.SmsTemplateDTO;
import com.mycompany.myapp.system.service.mapper.SmsTemplateMapper;
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
import org.springframework.validation.Validator;

/**
 * Integration tests for the {@link SmsTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SmsTemplateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final MessageSendType DEFAULT_TYPE = MessageSendType.EMAIL;
    private static final MessageSendType UPDATED_TYPE = MessageSendType.SMS;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_TEST_JSON = "AAAAAAAAAA";
    private static final String UPDATED_TEST_JSON = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REMOVED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMOVED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REMOVED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/sms-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SmsTemplateRepository smsTemplateRepository;

    @Autowired
    private SmsTemplateMapper smsTemplateMapper;

    @Autowired
    private MockMvc restSmsTemplateMockMvc;

    private SmsTemplate smsTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsTemplate createEntity() {
        SmsTemplate smsTemplate = new SmsTemplate()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .type(DEFAULT_TYPE)
            .content(DEFAULT_CONTENT)
            .testJson(DEFAULT_TEST_JSON)
            .removedAt(DEFAULT_REMOVED_AT);
        return smsTemplate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmsTemplate createUpdatedEntity() {
        SmsTemplate smsTemplate = new SmsTemplate()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .type(UPDATED_TYPE)
            .content(UPDATED_CONTENT)
            .testJson(UPDATED_TEST_JSON)
            .removedAt(UPDATED_REMOVED_AT);
        return smsTemplate;
    }

    @BeforeEach
    public void initTest() {
        smsTemplate = createEntity();
    }

    @Test
    @Transactional
    void createSmsTemplate() throws Exception {
        int databaseSizeBeforeCreate = smsTemplateRepository.findAll().size();
        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);
        restSmsTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smsTemplateDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SmsTemplate in the database
        List<SmsTemplate> smsTemplateList = smsTemplateRepository.findAll();
        assertThat(smsTemplateList).hasSize(databaseSizeBeforeCreate + 1);
        SmsTemplate testSmsTemplate = smsTemplateList.get(smsTemplateList.size() - 1);
        assertThat(testSmsTemplate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSmsTemplate.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSmsTemplate.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSmsTemplate.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSmsTemplate.getTestJson()).isEqualTo(DEFAULT_TEST_JSON);
        assertThat(testSmsTemplate.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void createSmsTemplateWithExistingId() throws Exception {
        // Create the SmsTemplate with an existing ID
        smsTemplate.setId(1L);
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        int databaseSizeBeforeCreate = smsTemplateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smsTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsTemplate in the database
        List<SmsTemplate> smsTemplateList = smsTemplateRepository.findAll();
        assertThat(smsTemplateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSmsTemplates() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList
        restSmsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].testJson").value(hasItem(DEFAULT_TEST_JSON)))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));
    }

    @Test
    @Transactional
    void getSmsTemplate() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get the smsTemplate
        restSmsTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, smsTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(smsTemplate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.testJson").value(DEFAULT_TEST_JSON))
            .andExpect(jsonPath("$.removedAt").value(DEFAULT_REMOVED_AT.toString()));
    }

    @Test
    @Transactional
    void getSmsTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        Long id = smsTemplate.getId();

        defaultSmsTemplateShouldBeFound("id.equals=" + id);
        defaultSmsTemplateShouldNotBeFound("id.notEquals=" + id);

        defaultSmsTemplateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSmsTemplateShouldNotBeFound("id.greaterThan=" + id);

        defaultSmsTemplateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSmsTemplateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where name equals to DEFAULT_NAME
        defaultSmsTemplateShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the smsTemplateList where name equals to UPDATED_NAME
        defaultSmsTemplateShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where name not equals to DEFAULT_NAME
        defaultSmsTemplateShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the smsTemplateList where name not equals to UPDATED_NAME
        defaultSmsTemplateShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSmsTemplateShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the smsTemplateList where name equals to UPDATED_NAME
        defaultSmsTemplateShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where name is not null
        defaultSmsTemplateShouldBeFound("name.specified=true");

        // Get all the smsTemplateList where name is null
        defaultSmsTemplateShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByNameContainsSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where name contains DEFAULT_NAME
        defaultSmsTemplateShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the smsTemplateList where name contains UPDATED_NAME
        defaultSmsTemplateShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where name does not contain DEFAULT_NAME
        defaultSmsTemplateShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the smsTemplateList where name does not contain UPDATED_NAME
        defaultSmsTemplateShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where code equals to DEFAULT_CODE
        defaultSmsTemplateShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the smsTemplateList where code equals to UPDATED_CODE
        defaultSmsTemplateShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where code not equals to DEFAULT_CODE
        defaultSmsTemplateShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the smsTemplateList where code not equals to UPDATED_CODE
        defaultSmsTemplateShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSmsTemplateShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the smsTemplateList where code equals to UPDATED_CODE
        defaultSmsTemplateShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where code is not null
        defaultSmsTemplateShouldBeFound("code.specified=true");

        // Get all the smsTemplateList where code is null
        defaultSmsTemplateShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCodeContainsSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where code contains DEFAULT_CODE
        defaultSmsTemplateShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the smsTemplateList where code contains UPDATED_CODE
        defaultSmsTemplateShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where code does not contain DEFAULT_CODE
        defaultSmsTemplateShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the smsTemplateList where code does not contain UPDATED_CODE
        defaultSmsTemplateShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where type equals to DEFAULT_TYPE
        defaultSmsTemplateShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the smsTemplateList where type equals to UPDATED_TYPE
        defaultSmsTemplateShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where type not equals to DEFAULT_TYPE
        defaultSmsTemplateShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the smsTemplateList where type not equals to UPDATED_TYPE
        defaultSmsTemplateShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultSmsTemplateShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the smsTemplateList where type equals to UPDATED_TYPE
        defaultSmsTemplateShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where type is not null
        defaultSmsTemplateShouldBeFound("type.specified=true");

        // Get all the smsTemplateList where type is null
        defaultSmsTemplateShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where content equals to DEFAULT_CONTENT
        defaultSmsTemplateShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the smsTemplateList where content equals to UPDATED_CONTENT
        defaultSmsTemplateShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where content not equals to DEFAULT_CONTENT
        defaultSmsTemplateShouldNotBeFound("content.notEquals=" + DEFAULT_CONTENT);

        // Get all the smsTemplateList where content not equals to UPDATED_CONTENT
        defaultSmsTemplateShouldBeFound("content.notEquals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByContentIsInShouldWork() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultSmsTemplateShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the smsTemplateList where content equals to UPDATED_CONTENT
        defaultSmsTemplateShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where content is not null
        defaultSmsTemplateShouldBeFound("content.specified=true");

        // Get all the smsTemplateList where content is null
        defaultSmsTemplateShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByContentContainsSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where content contains DEFAULT_CONTENT
        defaultSmsTemplateShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the smsTemplateList where content contains UPDATED_CONTENT
        defaultSmsTemplateShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByContentNotContainsSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where content does not contain DEFAULT_CONTENT
        defaultSmsTemplateShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the smsTemplateList where content does not contain UPDATED_CONTENT
        defaultSmsTemplateShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTestJsonIsEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where testJson equals to DEFAULT_TEST_JSON
        defaultSmsTemplateShouldBeFound("testJson.equals=" + DEFAULT_TEST_JSON);

        // Get all the smsTemplateList where testJson equals to UPDATED_TEST_JSON
        defaultSmsTemplateShouldNotBeFound("testJson.equals=" + UPDATED_TEST_JSON);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTestJsonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where testJson not equals to DEFAULT_TEST_JSON
        defaultSmsTemplateShouldNotBeFound("testJson.notEquals=" + DEFAULT_TEST_JSON);

        // Get all the smsTemplateList where testJson not equals to UPDATED_TEST_JSON
        defaultSmsTemplateShouldBeFound("testJson.notEquals=" + UPDATED_TEST_JSON);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTestJsonIsInShouldWork() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where testJson in DEFAULT_TEST_JSON or UPDATED_TEST_JSON
        defaultSmsTemplateShouldBeFound("testJson.in=" + DEFAULT_TEST_JSON + "," + UPDATED_TEST_JSON);

        // Get all the smsTemplateList where testJson equals to UPDATED_TEST_JSON
        defaultSmsTemplateShouldNotBeFound("testJson.in=" + UPDATED_TEST_JSON);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTestJsonIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where testJson is not null
        defaultSmsTemplateShouldBeFound("testJson.specified=true");

        // Get all the smsTemplateList where testJson is null
        defaultSmsTemplateShouldNotBeFound("testJson.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTestJsonContainsSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where testJson contains DEFAULT_TEST_JSON
        defaultSmsTemplateShouldBeFound("testJson.contains=" + DEFAULT_TEST_JSON);

        // Get all the smsTemplateList where testJson contains UPDATED_TEST_JSON
        defaultSmsTemplateShouldNotBeFound("testJson.contains=" + UPDATED_TEST_JSON);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByTestJsonNotContainsSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where testJson does not contain DEFAULT_TEST_JSON
        defaultSmsTemplateShouldNotBeFound("testJson.doesNotContain=" + DEFAULT_TEST_JSON);

        // Get all the smsTemplateList where testJson does not contain UPDATED_TEST_JSON
        defaultSmsTemplateShouldBeFound("testJson.doesNotContain=" + UPDATED_TEST_JSON);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where removedAt equals to DEFAULT_REMOVED_AT
        defaultSmsTemplateShouldBeFound("removedAt.equals=" + DEFAULT_REMOVED_AT);

        // Get all the smsTemplateList where removedAt equals to UPDATED_REMOVED_AT
        defaultSmsTemplateShouldNotBeFound("removedAt.equals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemovedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where removedAt not equals to DEFAULT_REMOVED_AT
        defaultSmsTemplateShouldNotBeFound("removedAt.notEquals=" + DEFAULT_REMOVED_AT);

        // Get all the smsTemplateList where removedAt not equals to UPDATED_REMOVED_AT
        defaultSmsTemplateShouldBeFound("removedAt.notEquals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where removedAt in DEFAULT_REMOVED_AT or UPDATED_REMOVED_AT
        defaultSmsTemplateShouldBeFound("removedAt.in=" + DEFAULT_REMOVED_AT + "," + UPDATED_REMOVED_AT);

        // Get all the smsTemplateList where removedAt equals to UPDATED_REMOVED_AT
        defaultSmsTemplateShouldNotBeFound("removedAt.in=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where removedAt is not null
        defaultSmsTemplateShouldBeFound("removedAt.specified=true");

        // Get all the smsTemplateList where removedAt is null
        defaultSmsTemplateShouldNotBeFound("removedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where removedAt is greater than or equal to DEFAULT_REMOVED_AT
        defaultSmsTemplateShouldBeFound("removedAt.greaterThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the smsTemplateList where removedAt is greater than or equal to UPDATED_REMOVED_AT
        defaultSmsTemplateShouldNotBeFound("removedAt.greaterThanOrEqual=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where removedAt is less than or equal to DEFAULT_REMOVED_AT
        defaultSmsTemplateShouldBeFound("removedAt.lessThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the smsTemplateList where removedAt is less than or equal to SMALLER_REMOVED_AT
        defaultSmsTemplateShouldNotBeFound("removedAt.lessThanOrEqual=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where removedAt is less than DEFAULT_REMOVED_AT
        defaultSmsTemplateShouldNotBeFound("removedAt.lessThan=" + DEFAULT_REMOVED_AT);

        // Get all the smsTemplateList where removedAt is less than UPDATED_REMOVED_AT
        defaultSmsTemplateShouldBeFound("removedAt.lessThan=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSmsTemplatesByRemovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        // Get all the smsTemplateList where removedAt is greater than DEFAULT_REMOVED_AT
        defaultSmsTemplateShouldNotBeFound("removedAt.greaterThan=" + DEFAULT_REMOVED_AT);

        // Get all the smsTemplateList where removedAt is greater than SMALLER_REMOVED_AT
        defaultSmsTemplateShouldBeFound("removedAt.greaterThan=" + SMALLER_REMOVED_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSmsTemplateShouldBeFound(String filter) throws Exception {
        restSmsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].testJson").value(hasItem(DEFAULT_TEST_JSON)))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));

        // Check, that the count call also returns 1
        restSmsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSmsTemplateShouldNotBeFound(String filter) throws Exception {
        restSmsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSmsTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSmsTemplate() throws Exception {
        // Get the smsTemplate
        restSmsTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSmsTemplate() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        int databaseSizeBeforeUpdate = smsTemplateRepository.findAll().size();

        // Update the smsTemplate
        SmsTemplate updatedSmsTemplate = smsTemplateRepository.findById(smsTemplate.getId()).get();
        // Disconnect from session so that the updates on updatedSmsTemplate are not directly saved in db
        updatedSmsTemplate
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .type(UPDATED_TYPE)
            .content(UPDATED_CONTENT)
            .testJson(UPDATED_TEST_JSON)
            .removedAt(UPDATED_REMOVED_AT);
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(updatedSmsTemplate);

        restSmsTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smsTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smsTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the SmsTemplate in the database
        List<SmsTemplate> smsTemplateList = smsTemplateRepository.findAll();
        assertThat(smsTemplateList).hasSize(databaseSizeBeforeUpdate);
        SmsTemplate testSmsTemplate = smsTemplateList.get(smsTemplateList.size() - 1);
        assertThat(testSmsTemplate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSmsTemplate.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSmsTemplate.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSmsTemplate.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSmsTemplate.getTestJson()).isEqualTo(UPDATED_TEST_JSON);
        assertThat(testSmsTemplate.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSmsTemplate() throws Exception {
        int databaseSizeBeforeUpdate = smsTemplateRepository.findAll().size();
        smsTemplate.setId(count.incrementAndGet());

        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smsTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smsTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsTemplate in the database
        List<SmsTemplate> smsTemplateList = smsTemplateRepository.findAll();
        assertThat(smsTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSmsTemplate() throws Exception {
        int databaseSizeBeforeUpdate = smsTemplateRepository.findAll().size();
        smsTemplate.setId(count.incrementAndGet());

        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smsTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsTemplate in the database
        List<SmsTemplate> smsTemplateList = smsTemplateRepository.findAll();
        assertThat(smsTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSmsTemplate() throws Exception {
        int databaseSizeBeforeUpdate = smsTemplateRepository.findAll().size();
        smsTemplate.setId(count.incrementAndGet());

        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smsTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsTemplate in the database
        List<SmsTemplate> smsTemplateList = smsTemplateRepository.findAll();
        assertThat(smsTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSmsTemplateWithPatch() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        int databaseSizeBeforeUpdate = smsTemplateRepository.findAll().size();

        // Update the smsTemplate using partial update
        SmsTemplate partialUpdatedSmsTemplate = new SmsTemplate();
        partialUpdatedSmsTemplate.setId(smsTemplate.getId());

        partialUpdatedSmsTemplate.code(UPDATED_CODE).content(UPDATED_CONTENT).testJson(UPDATED_TEST_JSON);

        restSmsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmsTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSmsTemplate))
            )
            .andExpect(status().isOk());

        // Validate the SmsTemplate in the database
        List<SmsTemplate> smsTemplateList = smsTemplateRepository.findAll();
        assertThat(smsTemplateList).hasSize(databaseSizeBeforeUpdate);
        SmsTemplate testSmsTemplate = smsTemplateList.get(smsTemplateList.size() - 1);
        assertThat(testSmsTemplate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSmsTemplate.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSmsTemplate.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSmsTemplate.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSmsTemplate.getTestJson()).isEqualTo(UPDATED_TEST_JSON);
        assertThat(testSmsTemplate.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSmsTemplateWithPatch() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        int databaseSizeBeforeUpdate = smsTemplateRepository.findAll().size();

        // Update the smsTemplate using partial update
        SmsTemplate partialUpdatedSmsTemplate = new SmsTemplate();
        partialUpdatedSmsTemplate.setId(smsTemplate.getId());

        partialUpdatedSmsTemplate
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .type(UPDATED_TYPE)
            .content(UPDATED_CONTENT)
            .testJson(UPDATED_TEST_JSON)
            .removedAt(UPDATED_REMOVED_AT);

        restSmsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmsTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSmsTemplate))
            )
            .andExpect(status().isOk());

        // Validate the SmsTemplate in the database
        List<SmsTemplate> smsTemplateList = smsTemplateRepository.findAll();
        assertThat(smsTemplateList).hasSize(databaseSizeBeforeUpdate);
        SmsTemplate testSmsTemplate = smsTemplateList.get(smsTemplateList.size() - 1);
        assertThat(testSmsTemplate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSmsTemplate.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSmsTemplate.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSmsTemplate.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSmsTemplate.getTestJson()).isEqualTo(UPDATED_TEST_JSON);
        assertThat(testSmsTemplate.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSmsTemplate() throws Exception {
        int databaseSizeBeforeUpdate = smsTemplateRepository.findAll().size();
        smsTemplate.setId(count.incrementAndGet());

        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, smsTemplateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(smsTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsTemplate in the database
        List<SmsTemplate> smsTemplateList = smsTemplateRepository.findAll();
        assertThat(smsTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSmsTemplate() throws Exception {
        int databaseSizeBeforeUpdate = smsTemplateRepository.findAll().size();
        smsTemplate.setId(count.incrementAndGet());

        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(smsTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmsTemplate in the database
        List<SmsTemplate> smsTemplateList = smsTemplateRepository.findAll();
        assertThat(smsTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSmsTemplate() throws Exception {
        int databaseSizeBeforeUpdate = smsTemplateRepository.findAll().size();
        smsTemplate.setId(count.incrementAndGet());

        // Create the SmsTemplate
        SmsTemplateDTO smsTemplateDTO = smsTemplateMapper.toDto(smsTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmsTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(smsTemplateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmsTemplate in the database
        List<SmsTemplate> smsTemplateList = smsTemplateRepository.findAll();
        assertThat(smsTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSmsTemplate() throws Exception {
        // Initialize the database
        smsTemplateRepository.insert(smsTemplate);

        int databaseSizeBeforeDelete = smsTemplateRepository.findAll().size();

        // Delete the smsTemplate
        restSmsTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, smsTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SmsTemplate> smsTemplateList = smsTemplateRepository.findAll();
        assertThat(smsTemplateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
