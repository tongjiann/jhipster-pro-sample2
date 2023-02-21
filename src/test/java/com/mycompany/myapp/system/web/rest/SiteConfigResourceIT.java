package com.mycompany.myapp.system.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.enumeration.CommonFieldType;
import com.mycompany.myapp.system.domain.SiteConfig;
import com.mycompany.myapp.system.repository.SiteConfigRepository;
import com.mycompany.myapp.system.service.SiteConfigQueryService;
import com.mycompany.myapp.system.service.criteria.SiteConfigCriteria;
import com.mycompany.myapp.system.service.dto.SiteConfigDTO;
import com.mycompany.myapp.system.service.mapper.SiteConfigMapper;
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
 * Integration tests for the {@link SiteConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SiteConfigResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_VALUE = "BBBBBBBBBB";

    private static final CommonFieldType DEFAULT_FIELD_TYPE = CommonFieldType.INTEGER;
    private static final CommonFieldType UPDATED_FIELD_TYPE = CommonFieldType.LONG;

    private static final LocalDate DEFAULT_REMOVED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMOVED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REMOVED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/site-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SiteConfigRepository siteConfigRepository;

    @Autowired
    private SiteConfigMapper siteConfigMapper;

    @Autowired
    private MockMvc restSiteConfigMockMvc;

    private SiteConfig siteConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteConfig createEntity() {
        SiteConfig siteConfig = new SiteConfig()
            .title(DEFAULT_TITLE)
            .remark(DEFAULT_REMARK)
            .fieldName(DEFAULT_FIELD_NAME)
            .fieldValue(DEFAULT_FIELD_VALUE)
            .fieldType(DEFAULT_FIELD_TYPE)
            .removedAt(DEFAULT_REMOVED_AT);
        return siteConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteConfig createUpdatedEntity() {
        SiteConfig siteConfig = new SiteConfig()
            .title(UPDATED_TITLE)
            .remark(UPDATED_REMARK)
            .fieldName(UPDATED_FIELD_NAME)
            .fieldValue(UPDATED_FIELD_VALUE)
            .fieldType(UPDATED_FIELD_TYPE)
            .removedAt(UPDATED_REMOVED_AT);
        return siteConfig;
    }

    @BeforeEach
    public void initTest() {
        siteConfig = createEntity();
    }

    @Test
    @Transactional
    void createSiteConfig() throws Exception {
        int databaseSizeBeforeCreate = siteConfigRepository.findAll().size();
        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);
        restSiteConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeCreate + 1);
        SiteConfig testSiteConfig = siteConfigList.get(siteConfigList.size() - 1);
        assertThat(testSiteConfig.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSiteConfig.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testSiteConfig.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testSiteConfig.getFieldValue()).isEqualTo(DEFAULT_FIELD_VALUE);
        assertThat(testSiteConfig.getFieldType()).isEqualTo(DEFAULT_FIELD_TYPE);
        assertThat(testSiteConfig.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void createSiteConfigWithExistingId() throws Exception {
        // Create the SiteConfig with an existing ID
        siteConfig.setId(1L);
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        int databaseSizeBeforeCreate = siteConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSiteConfigs() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList
        restSiteConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(siteConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].fieldValue").value(hasItem(DEFAULT_FIELD_VALUE)))
            .andExpect(jsonPath("$.[*].fieldType").value(hasItem(DEFAULT_FIELD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));
    }

    @Test
    @Transactional
    void getSiteConfig() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get the siteConfig
        restSiteConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, siteConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(siteConfig.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME))
            .andExpect(jsonPath("$.fieldValue").value(DEFAULT_FIELD_VALUE))
            .andExpect(jsonPath("$.fieldType").value(DEFAULT_FIELD_TYPE.toString()))
            .andExpect(jsonPath("$.removedAt").value(DEFAULT_REMOVED_AT.toString()));
    }

    @Test
    @Transactional
    void getSiteConfigsByIdFiltering() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        Long id = siteConfig.getId();

        defaultSiteConfigShouldBeFound("id.equals=" + id);
        defaultSiteConfigShouldNotBeFound("id.notEquals=" + id);

        defaultSiteConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSiteConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultSiteConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSiteConfigShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where title equals to DEFAULT_TITLE
        defaultSiteConfigShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the siteConfigList where title equals to UPDATED_TITLE
        defaultSiteConfigShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where title not equals to DEFAULT_TITLE
        defaultSiteConfigShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the siteConfigList where title not equals to UPDATED_TITLE
        defaultSiteConfigShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultSiteConfigShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the siteConfigList where title equals to UPDATED_TITLE
        defaultSiteConfigShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where title is not null
        defaultSiteConfigShouldBeFound("title.specified=true");

        // Get all the siteConfigList where title is null
        defaultSiteConfigShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByTitleContainsSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where title contains DEFAULT_TITLE
        defaultSiteConfigShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the siteConfigList where title contains UPDATED_TITLE
        defaultSiteConfigShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where title does not contain DEFAULT_TITLE
        defaultSiteConfigShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the siteConfigList where title does not contain UPDATED_TITLE
        defaultSiteConfigShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where remark equals to DEFAULT_REMARK
        defaultSiteConfigShouldBeFound("remark.equals=" + DEFAULT_REMARK);

        // Get all the siteConfigList where remark equals to UPDATED_REMARK
        defaultSiteConfigShouldNotBeFound("remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemarkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where remark not equals to DEFAULT_REMARK
        defaultSiteConfigShouldNotBeFound("remark.notEquals=" + DEFAULT_REMARK);

        // Get all the siteConfigList where remark not equals to UPDATED_REMARK
        defaultSiteConfigShouldBeFound("remark.notEquals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where remark in DEFAULT_REMARK or UPDATED_REMARK
        defaultSiteConfigShouldBeFound("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK);

        // Get all the siteConfigList where remark equals to UPDATED_REMARK
        defaultSiteConfigShouldNotBeFound("remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where remark is not null
        defaultSiteConfigShouldBeFound("remark.specified=true");

        // Get all the siteConfigList where remark is null
        defaultSiteConfigShouldNotBeFound("remark.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemarkContainsSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where remark contains DEFAULT_REMARK
        defaultSiteConfigShouldBeFound("remark.contains=" + DEFAULT_REMARK);

        // Get all the siteConfigList where remark contains UPDATED_REMARK
        defaultSiteConfigShouldNotBeFound("remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where remark does not contain DEFAULT_REMARK
        defaultSiteConfigShouldNotBeFound("remark.doesNotContain=" + DEFAULT_REMARK);

        // Get all the siteConfigList where remark does not contain UPDATED_REMARK
        defaultSiteConfigShouldBeFound("remark.doesNotContain=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldNameIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldName equals to DEFAULT_FIELD_NAME
        defaultSiteConfigShouldBeFound("fieldName.equals=" + DEFAULT_FIELD_NAME);

        // Get all the siteConfigList where fieldName equals to UPDATED_FIELD_NAME
        defaultSiteConfigShouldNotBeFound("fieldName.equals=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldName not equals to DEFAULT_FIELD_NAME
        defaultSiteConfigShouldNotBeFound("fieldName.notEquals=" + DEFAULT_FIELD_NAME);

        // Get all the siteConfigList where fieldName not equals to UPDATED_FIELD_NAME
        defaultSiteConfigShouldBeFound("fieldName.notEquals=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldNameIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldName in DEFAULT_FIELD_NAME or UPDATED_FIELD_NAME
        defaultSiteConfigShouldBeFound("fieldName.in=" + DEFAULT_FIELD_NAME + "," + UPDATED_FIELD_NAME);

        // Get all the siteConfigList where fieldName equals to UPDATED_FIELD_NAME
        defaultSiteConfigShouldNotBeFound("fieldName.in=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldName is not null
        defaultSiteConfigShouldBeFound("fieldName.specified=true");

        // Get all the siteConfigList where fieldName is null
        defaultSiteConfigShouldNotBeFound("fieldName.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldNameContainsSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldName contains DEFAULT_FIELD_NAME
        defaultSiteConfigShouldBeFound("fieldName.contains=" + DEFAULT_FIELD_NAME);

        // Get all the siteConfigList where fieldName contains UPDATED_FIELD_NAME
        defaultSiteConfigShouldNotBeFound("fieldName.contains=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldNameNotContainsSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldName does not contain DEFAULT_FIELD_NAME
        defaultSiteConfigShouldNotBeFound("fieldName.doesNotContain=" + DEFAULT_FIELD_NAME);

        // Get all the siteConfigList where fieldName does not contain UPDATED_FIELD_NAME
        defaultSiteConfigShouldBeFound("fieldName.doesNotContain=" + UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldValueIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldValue equals to DEFAULT_FIELD_VALUE
        defaultSiteConfigShouldBeFound("fieldValue.equals=" + DEFAULT_FIELD_VALUE);

        // Get all the siteConfigList where fieldValue equals to UPDATED_FIELD_VALUE
        defaultSiteConfigShouldNotBeFound("fieldValue.equals=" + UPDATED_FIELD_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldValue not equals to DEFAULT_FIELD_VALUE
        defaultSiteConfigShouldNotBeFound("fieldValue.notEquals=" + DEFAULT_FIELD_VALUE);

        // Get all the siteConfigList where fieldValue not equals to UPDATED_FIELD_VALUE
        defaultSiteConfigShouldBeFound("fieldValue.notEquals=" + UPDATED_FIELD_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldValueIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldValue in DEFAULT_FIELD_VALUE or UPDATED_FIELD_VALUE
        defaultSiteConfigShouldBeFound("fieldValue.in=" + DEFAULT_FIELD_VALUE + "," + UPDATED_FIELD_VALUE);

        // Get all the siteConfigList where fieldValue equals to UPDATED_FIELD_VALUE
        defaultSiteConfigShouldNotBeFound("fieldValue.in=" + UPDATED_FIELD_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldValue is not null
        defaultSiteConfigShouldBeFound("fieldValue.specified=true");

        // Get all the siteConfigList where fieldValue is null
        defaultSiteConfigShouldNotBeFound("fieldValue.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldValueContainsSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldValue contains DEFAULT_FIELD_VALUE
        defaultSiteConfigShouldBeFound("fieldValue.contains=" + DEFAULT_FIELD_VALUE);

        // Get all the siteConfigList where fieldValue contains UPDATED_FIELD_VALUE
        defaultSiteConfigShouldNotBeFound("fieldValue.contains=" + UPDATED_FIELD_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldValueNotContainsSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldValue does not contain DEFAULT_FIELD_VALUE
        defaultSiteConfigShouldNotBeFound("fieldValue.doesNotContain=" + DEFAULT_FIELD_VALUE);

        // Get all the siteConfigList where fieldValue does not contain UPDATED_FIELD_VALUE
        defaultSiteConfigShouldBeFound("fieldValue.doesNotContain=" + UPDATED_FIELD_VALUE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldType equals to DEFAULT_FIELD_TYPE
        defaultSiteConfigShouldBeFound("fieldType.equals=" + DEFAULT_FIELD_TYPE);

        // Get all the siteConfigList where fieldType equals to UPDATED_FIELD_TYPE
        defaultSiteConfigShouldNotBeFound("fieldType.equals=" + UPDATED_FIELD_TYPE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldType not equals to DEFAULT_FIELD_TYPE
        defaultSiteConfigShouldNotBeFound("fieldType.notEquals=" + DEFAULT_FIELD_TYPE);

        // Get all the siteConfigList where fieldType not equals to UPDATED_FIELD_TYPE
        defaultSiteConfigShouldBeFound("fieldType.notEquals=" + UPDATED_FIELD_TYPE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldTypeIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldType in DEFAULT_FIELD_TYPE or UPDATED_FIELD_TYPE
        defaultSiteConfigShouldBeFound("fieldType.in=" + DEFAULT_FIELD_TYPE + "," + UPDATED_FIELD_TYPE);

        // Get all the siteConfigList where fieldType equals to UPDATED_FIELD_TYPE
        defaultSiteConfigShouldNotBeFound("fieldType.in=" + UPDATED_FIELD_TYPE);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByFieldTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where fieldType is not null
        defaultSiteConfigShouldBeFound("fieldType.specified=true");

        // Get all the siteConfigList where fieldType is null
        defaultSiteConfigShouldNotBeFound("fieldType.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where removedAt equals to DEFAULT_REMOVED_AT
        defaultSiteConfigShouldBeFound("removedAt.equals=" + DEFAULT_REMOVED_AT);

        // Get all the siteConfigList where removedAt equals to UPDATED_REMOVED_AT
        defaultSiteConfigShouldNotBeFound("removedAt.equals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemovedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where removedAt not equals to DEFAULT_REMOVED_AT
        defaultSiteConfigShouldNotBeFound("removedAt.notEquals=" + DEFAULT_REMOVED_AT);

        // Get all the siteConfigList where removedAt not equals to UPDATED_REMOVED_AT
        defaultSiteConfigShouldBeFound("removedAt.notEquals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where removedAt in DEFAULT_REMOVED_AT or UPDATED_REMOVED_AT
        defaultSiteConfigShouldBeFound("removedAt.in=" + DEFAULT_REMOVED_AT + "," + UPDATED_REMOVED_AT);

        // Get all the siteConfigList where removedAt equals to UPDATED_REMOVED_AT
        defaultSiteConfigShouldNotBeFound("removedAt.in=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where removedAt is not null
        defaultSiteConfigShouldBeFound("removedAt.specified=true");

        // Get all the siteConfigList where removedAt is null
        defaultSiteConfigShouldNotBeFound("removedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where removedAt is greater than or equal to DEFAULT_REMOVED_AT
        defaultSiteConfigShouldBeFound("removedAt.greaterThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the siteConfigList where removedAt is greater than or equal to UPDATED_REMOVED_AT
        defaultSiteConfigShouldNotBeFound("removedAt.greaterThanOrEqual=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where removedAt is less than or equal to DEFAULT_REMOVED_AT
        defaultSiteConfigShouldBeFound("removedAt.lessThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the siteConfigList where removedAt is less than or equal to SMALLER_REMOVED_AT
        defaultSiteConfigShouldNotBeFound("removedAt.lessThanOrEqual=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where removedAt is less than DEFAULT_REMOVED_AT
        defaultSiteConfigShouldNotBeFound("removedAt.lessThan=" + DEFAULT_REMOVED_AT);

        // Get all the siteConfigList where removedAt is less than UPDATED_REMOVED_AT
        defaultSiteConfigShouldBeFound("removedAt.lessThan=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSiteConfigsByRemovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        // Get all the siteConfigList where removedAt is greater than DEFAULT_REMOVED_AT
        defaultSiteConfigShouldNotBeFound("removedAt.greaterThan=" + DEFAULT_REMOVED_AT);

        // Get all the siteConfigList where removedAt is greater than SMALLER_REMOVED_AT
        defaultSiteConfigShouldBeFound("removedAt.greaterThan=" + SMALLER_REMOVED_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSiteConfigShouldBeFound(String filter) throws Exception {
        restSiteConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(siteConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].fieldValue").value(hasItem(DEFAULT_FIELD_VALUE)))
            .andExpect(jsonPath("$.[*].fieldType").value(hasItem(DEFAULT_FIELD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));

        // Check, that the count call also returns 1
        restSiteConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSiteConfigShouldNotBeFound(String filter) throws Exception {
        restSiteConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSiteConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSiteConfig() throws Exception {
        // Get the siteConfig
        restSiteConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSiteConfig() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();

        // Update the siteConfig
        SiteConfig updatedSiteConfig = siteConfigRepository.findById(siteConfig.getId()).get();
        // Disconnect from session so that the updates on updatedSiteConfig are not directly saved in db
        updatedSiteConfig
            .title(UPDATED_TITLE)
            .remark(UPDATED_REMARK)
            .fieldName(UPDATED_FIELD_NAME)
            .fieldValue(UPDATED_FIELD_VALUE)
            .fieldType(UPDATED_FIELD_TYPE)
            .removedAt(UPDATED_REMOVED_AT);
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(updatedSiteConfig);

        restSiteConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
        SiteConfig testSiteConfig = siteConfigList.get(siteConfigList.size() - 1);
        assertThat(testSiteConfig.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSiteConfig.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testSiteConfig.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testSiteConfig.getFieldValue()).isEqualTo(UPDATED_FIELD_VALUE);
        assertThat(testSiteConfig.getFieldType()).isEqualTo(UPDATED_FIELD_TYPE);
        assertThat(testSiteConfig.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSiteConfig() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();
        siteConfig.setId(count.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSiteConfig() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();
        siteConfig.setId(count.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSiteConfig() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();
        siteConfig.setId(count.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSiteConfigWithPatch() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();

        // Update the siteConfig using partial update
        SiteConfig partialUpdatedSiteConfig = new SiteConfig();
        partialUpdatedSiteConfig.setId(siteConfig.getId());

        partialUpdatedSiteConfig.title(UPDATED_TITLE).remark(UPDATED_REMARK).fieldName(UPDATED_FIELD_NAME).removedAt(UPDATED_REMOVED_AT);

        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSiteConfig))
            )
            .andExpect(status().isOk());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
        SiteConfig testSiteConfig = siteConfigList.get(siteConfigList.size() - 1);
        assertThat(testSiteConfig.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSiteConfig.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testSiteConfig.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testSiteConfig.getFieldValue()).isEqualTo(DEFAULT_FIELD_VALUE);
        assertThat(testSiteConfig.getFieldType()).isEqualTo(DEFAULT_FIELD_TYPE);
        assertThat(testSiteConfig.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSiteConfigWithPatch() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();

        // Update the siteConfig using partial update
        SiteConfig partialUpdatedSiteConfig = new SiteConfig();
        partialUpdatedSiteConfig.setId(siteConfig.getId());

        partialUpdatedSiteConfig
            .title(UPDATED_TITLE)
            .remark(UPDATED_REMARK)
            .fieldName(UPDATED_FIELD_NAME)
            .fieldValue(UPDATED_FIELD_VALUE)
            .fieldType(UPDATED_FIELD_TYPE)
            .removedAt(UPDATED_REMOVED_AT);

        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSiteConfig))
            )
            .andExpect(status().isOk());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
        SiteConfig testSiteConfig = siteConfigList.get(siteConfigList.size() - 1);
        assertThat(testSiteConfig.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSiteConfig.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testSiteConfig.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testSiteConfig.getFieldValue()).isEqualTo(UPDATED_FIELD_VALUE);
        assertThat(testSiteConfig.getFieldType()).isEqualTo(UPDATED_FIELD_TYPE);
        assertThat(testSiteConfig.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSiteConfig() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();
        siteConfig.setId(count.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, siteConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSiteConfig() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();
        siteConfig.setId(count.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSiteConfig() throws Exception {
        int databaseSizeBeforeUpdate = siteConfigRepository.findAll().size();
        siteConfig.setId(count.incrementAndGet());

        // Create the SiteConfig
        SiteConfigDTO siteConfigDTO = siteConfigMapper.toDto(siteConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(siteConfigDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteConfig in the database
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSiteConfig() throws Exception {
        // Initialize the database
        siteConfigRepository.insert(siteConfig);

        int databaseSizeBeforeDelete = siteConfigRepository.findAll().size();

        // Delete the siteConfig
        restSiteConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, siteConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SiteConfig> siteConfigList = siteConfigRepository.findAll();
        assertThat(siteConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
