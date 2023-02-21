package com.mycompany.myapp.system.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.system.domain.DataPermissionRule;
import com.mycompany.myapp.system.repository.DataPermissionRuleRepository;
import com.mycompany.myapp.system.service.DataPermissionRuleQueryService;
import com.mycompany.myapp.system.service.criteria.DataPermissionRuleCriteria;
import com.mycompany.myapp.system.service.dto.DataPermissionRuleDTO;
import com.mycompany.myapp.system.service.mapper.DataPermissionRuleMapper;
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
 * Integration tests for the {@link DataPermissionRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DataPermissionRuleResourceIT {

    private static final String DEFAULT_PERMISSION_ID = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COLUMN = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITIONS = "AAAAAAAAAA";
    private static final String UPDATED_CONDITIONS = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DISABLED = false;
    private static final Boolean UPDATED_DISABLED = true;

    private static final LocalDate DEFAULT_REMOVED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMOVED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REMOVED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/data-permission-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DataPermissionRuleRepository dataPermissionRuleRepository;

    @Autowired
    private DataPermissionRuleMapper dataPermissionRuleMapper;

    @Autowired
    private MockMvc restDataPermissionRuleMockMvc;

    private DataPermissionRule dataPermissionRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataPermissionRule createEntity() {
        DataPermissionRule dataPermissionRule = new DataPermissionRule()
            .permissionId(DEFAULT_PERMISSION_ID)
            .name(DEFAULT_NAME)
            .column(DEFAULT_COLUMN)
            .conditions(DEFAULT_CONDITIONS)
            .value(DEFAULT_VALUE)
            .disabled(DEFAULT_DISABLED)
            .removedAt(DEFAULT_REMOVED_AT);
        return dataPermissionRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataPermissionRule createUpdatedEntity() {
        DataPermissionRule dataPermissionRule = new DataPermissionRule()
            .permissionId(UPDATED_PERMISSION_ID)
            .name(UPDATED_NAME)
            .column(UPDATED_COLUMN)
            .conditions(UPDATED_CONDITIONS)
            .value(UPDATED_VALUE)
            .disabled(UPDATED_DISABLED)
            .removedAt(UPDATED_REMOVED_AT);
        return dataPermissionRule;
    }

    @BeforeEach
    public void initTest() {
        dataPermissionRule = createEntity();
    }

    @Test
    @Transactional
    void createDataPermissionRule() throws Exception {
        int databaseSizeBeforeCreate = dataPermissionRuleRepository.findAll().size();
        // Create the DataPermissionRule
        DataPermissionRuleDTO dataPermissionRuleDTO = dataPermissionRuleMapper.toDto(dataPermissionRule);
        restDataPermissionRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataPermissionRuleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DataPermissionRule in the database
        List<DataPermissionRule> dataPermissionRuleList = dataPermissionRuleRepository.findAll();
        assertThat(dataPermissionRuleList).hasSize(databaseSizeBeforeCreate + 1);
        DataPermissionRule testDataPermissionRule = dataPermissionRuleList.get(dataPermissionRuleList.size() - 1);
        assertThat(testDataPermissionRule.getPermissionId()).isEqualTo(DEFAULT_PERMISSION_ID);
        assertThat(testDataPermissionRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDataPermissionRule.getColumn()).isEqualTo(DEFAULT_COLUMN);
        assertThat(testDataPermissionRule.getConditions()).isEqualTo(DEFAULT_CONDITIONS);
        assertThat(testDataPermissionRule.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testDataPermissionRule.getDisabled()).isEqualTo(DEFAULT_DISABLED);
        assertThat(testDataPermissionRule.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void createDataPermissionRuleWithExistingId() throws Exception {
        // Create the DataPermissionRule with an existing ID
        dataPermissionRule.setId(1L);
        DataPermissionRuleDTO dataPermissionRuleDTO = dataPermissionRuleMapper.toDto(dataPermissionRule);

        int databaseSizeBeforeCreate = dataPermissionRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataPermissionRuleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataPermissionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataPermissionRule in the database
        List<DataPermissionRule> dataPermissionRuleList = dataPermissionRuleRepository.findAll();
        assertThat(dataPermissionRuleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDataPermissionRules() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList
        restDataPermissionRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataPermissionRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].permissionId").value(hasItem(DEFAULT_PERMISSION_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].column").value(hasItem(DEFAULT_COLUMN)))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));
    }

    @Test
    @Transactional
    void getDataPermissionRule() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get the dataPermissionRule
        restDataPermissionRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, dataPermissionRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataPermissionRule.getId().intValue()))
            .andExpect(jsonPath("$.permissionId").value(DEFAULT_PERMISSION_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.column").value(DEFAULT_COLUMN))
            .andExpect(jsonPath("$.conditions").value(DEFAULT_CONDITIONS))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.disabled").value(DEFAULT_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.removedAt").value(DEFAULT_REMOVED_AT.toString()));
    }

    @Test
    @Transactional
    void getDataPermissionRulesByIdFiltering() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        Long id = dataPermissionRule.getId();

        defaultDataPermissionRuleShouldBeFound("id.equals=" + id);
        defaultDataPermissionRuleShouldNotBeFound("id.notEquals=" + id);

        defaultDataPermissionRuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDataPermissionRuleShouldNotBeFound("id.greaterThan=" + id);

        defaultDataPermissionRuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDataPermissionRuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByPermissionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where permissionId equals to DEFAULT_PERMISSION_ID
        defaultDataPermissionRuleShouldBeFound("permissionId.equals=" + DEFAULT_PERMISSION_ID);

        // Get all the dataPermissionRuleList where permissionId equals to UPDATED_PERMISSION_ID
        defaultDataPermissionRuleShouldNotBeFound("permissionId.equals=" + UPDATED_PERMISSION_ID);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByPermissionIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where permissionId not equals to DEFAULT_PERMISSION_ID
        defaultDataPermissionRuleShouldNotBeFound("permissionId.notEquals=" + DEFAULT_PERMISSION_ID);

        // Get all the dataPermissionRuleList where permissionId not equals to UPDATED_PERMISSION_ID
        defaultDataPermissionRuleShouldBeFound("permissionId.notEquals=" + UPDATED_PERMISSION_ID);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByPermissionIdIsInShouldWork() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where permissionId in DEFAULT_PERMISSION_ID or UPDATED_PERMISSION_ID
        defaultDataPermissionRuleShouldBeFound("permissionId.in=" + DEFAULT_PERMISSION_ID + "," + UPDATED_PERMISSION_ID);

        // Get all the dataPermissionRuleList where permissionId equals to UPDATED_PERMISSION_ID
        defaultDataPermissionRuleShouldNotBeFound("permissionId.in=" + UPDATED_PERMISSION_ID);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByPermissionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where permissionId is not null
        defaultDataPermissionRuleShouldBeFound("permissionId.specified=true");

        // Get all the dataPermissionRuleList where permissionId is null
        defaultDataPermissionRuleShouldNotBeFound("permissionId.specified=false");
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByPermissionIdContainsSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where permissionId contains DEFAULT_PERMISSION_ID
        defaultDataPermissionRuleShouldBeFound("permissionId.contains=" + DEFAULT_PERMISSION_ID);

        // Get all the dataPermissionRuleList where permissionId contains UPDATED_PERMISSION_ID
        defaultDataPermissionRuleShouldNotBeFound("permissionId.contains=" + UPDATED_PERMISSION_ID);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByPermissionIdNotContainsSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where permissionId does not contain DEFAULT_PERMISSION_ID
        defaultDataPermissionRuleShouldNotBeFound("permissionId.doesNotContain=" + DEFAULT_PERMISSION_ID);

        // Get all the dataPermissionRuleList where permissionId does not contain UPDATED_PERMISSION_ID
        defaultDataPermissionRuleShouldBeFound("permissionId.doesNotContain=" + UPDATED_PERMISSION_ID);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where name equals to DEFAULT_NAME
        defaultDataPermissionRuleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the dataPermissionRuleList where name equals to UPDATED_NAME
        defaultDataPermissionRuleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where name not equals to DEFAULT_NAME
        defaultDataPermissionRuleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the dataPermissionRuleList where name not equals to UPDATED_NAME
        defaultDataPermissionRuleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDataPermissionRuleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the dataPermissionRuleList where name equals to UPDATED_NAME
        defaultDataPermissionRuleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where name is not null
        defaultDataPermissionRuleShouldBeFound("name.specified=true");

        // Get all the dataPermissionRuleList where name is null
        defaultDataPermissionRuleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where name contains DEFAULT_NAME
        defaultDataPermissionRuleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the dataPermissionRuleList where name contains UPDATED_NAME
        defaultDataPermissionRuleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where name does not contain DEFAULT_NAME
        defaultDataPermissionRuleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the dataPermissionRuleList where name does not contain UPDATED_NAME
        defaultDataPermissionRuleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByColumnIsEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where column equals to DEFAULT_COLUMN
        defaultDataPermissionRuleShouldBeFound("column.equals=" + DEFAULT_COLUMN);

        // Get all the dataPermissionRuleList where column equals to UPDATED_COLUMN
        defaultDataPermissionRuleShouldNotBeFound("column.equals=" + UPDATED_COLUMN);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByColumnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where column not equals to DEFAULT_COLUMN
        defaultDataPermissionRuleShouldNotBeFound("column.notEquals=" + DEFAULT_COLUMN);

        // Get all the dataPermissionRuleList where column not equals to UPDATED_COLUMN
        defaultDataPermissionRuleShouldBeFound("column.notEquals=" + UPDATED_COLUMN);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByColumnIsInShouldWork() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where column in DEFAULT_COLUMN or UPDATED_COLUMN
        defaultDataPermissionRuleShouldBeFound("column.in=" + DEFAULT_COLUMN + "," + UPDATED_COLUMN);

        // Get all the dataPermissionRuleList where column equals to UPDATED_COLUMN
        defaultDataPermissionRuleShouldNotBeFound("column.in=" + UPDATED_COLUMN);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByColumnIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where column is not null
        defaultDataPermissionRuleShouldBeFound("column.specified=true");

        // Get all the dataPermissionRuleList where column is null
        defaultDataPermissionRuleShouldNotBeFound("column.specified=false");
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByColumnContainsSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where column contains DEFAULT_COLUMN
        defaultDataPermissionRuleShouldBeFound("column.contains=" + DEFAULT_COLUMN);

        // Get all the dataPermissionRuleList where column contains UPDATED_COLUMN
        defaultDataPermissionRuleShouldNotBeFound("column.contains=" + UPDATED_COLUMN);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByColumnNotContainsSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where column does not contain DEFAULT_COLUMN
        defaultDataPermissionRuleShouldNotBeFound("column.doesNotContain=" + DEFAULT_COLUMN);

        // Get all the dataPermissionRuleList where column does not contain UPDATED_COLUMN
        defaultDataPermissionRuleShouldBeFound("column.doesNotContain=" + UPDATED_COLUMN);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByConditionsIsEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where conditions equals to DEFAULT_CONDITIONS
        defaultDataPermissionRuleShouldBeFound("conditions.equals=" + DEFAULT_CONDITIONS);

        // Get all the dataPermissionRuleList where conditions equals to UPDATED_CONDITIONS
        defaultDataPermissionRuleShouldNotBeFound("conditions.equals=" + UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByConditionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where conditions not equals to DEFAULT_CONDITIONS
        defaultDataPermissionRuleShouldNotBeFound("conditions.notEquals=" + DEFAULT_CONDITIONS);

        // Get all the dataPermissionRuleList where conditions not equals to UPDATED_CONDITIONS
        defaultDataPermissionRuleShouldBeFound("conditions.notEquals=" + UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByConditionsIsInShouldWork() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where conditions in DEFAULT_CONDITIONS or UPDATED_CONDITIONS
        defaultDataPermissionRuleShouldBeFound("conditions.in=" + DEFAULT_CONDITIONS + "," + UPDATED_CONDITIONS);

        // Get all the dataPermissionRuleList where conditions equals to UPDATED_CONDITIONS
        defaultDataPermissionRuleShouldNotBeFound("conditions.in=" + UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByConditionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where conditions is not null
        defaultDataPermissionRuleShouldBeFound("conditions.specified=true");

        // Get all the dataPermissionRuleList where conditions is null
        defaultDataPermissionRuleShouldNotBeFound("conditions.specified=false");
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByConditionsContainsSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where conditions contains DEFAULT_CONDITIONS
        defaultDataPermissionRuleShouldBeFound("conditions.contains=" + DEFAULT_CONDITIONS);

        // Get all the dataPermissionRuleList where conditions contains UPDATED_CONDITIONS
        defaultDataPermissionRuleShouldNotBeFound("conditions.contains=" + UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByConditionsNotContainsSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where conditions does not contain DEFAULT_CONDITIONS
        defaultDataPermissionRuleShouldNotBeFound("conditions.doesNotContain=" + DEFAULT_CONDITIONS);

        // Get all the dataPermissionRuleList where conditions does not contain UPDATED_CONDITIONS
        defaultDataPermissionRuleShouldBeFound("conditions.doesNotContain=" + UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where value equals to DEFAULT_VALUE
        defaultDataPermissionRuleShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the dataPermissionRuleList where value equals to UPDATED_VALUE
        defaultDataPermissionRuleShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where value not equals to DEFAULT_VALUE
        defaultDataPermissionRuleShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the dataPermissionRuleList where value not equals to UPDATED_VALUE
        defaultDataPermissionRuleShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultDataPermissionRuleShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the dataPermissionRuleList where value equals to UPDATED_VALUE
        defaultDataPermissionRuleShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where value is not null
        defaultDataPermissionRuleShouldBeFound("value.specified=true");

        // Get all the dataPermissionRuleList where value is null
        defaultDataPermissionRuleShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByValueContainsSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where value contains DEFAULT_VALUE
        defaultDataPermissionRuleShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the dataPermissionRuleList where value contains UPDATED_VALUE
        defaultDataPermissionRuleShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByValueNotContainsSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where value does not contain DEFAULT_VALUE
        defaultDataPermissionRuleShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the dataPermissionRuleList where value does not contain UPDATED_VALUE
        defaultDataPermissionRuleShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where disabled equals to DEFAULT_DISABLED
        defaultDataPermissionRuleShouldBeFound("disabled.equals=" + DEFAULT_DISABLED);

        // Get all the dataPermissionRuleList where disabled equals to UPDATED_DISABLED
        defaultDataPermissionRuleShouldNotBeFound("disabled.equals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByDisabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where disabled not equals to DEFAULT_DISABLED
        defaultDataPermissionRuleShouldNotBeFound("disabled.notEquals=" + DEFAULT_DISABLED);

        // Get all the dataPermissionRuleList where disabled not equals to UPDATED_DISABLED
        defaultDataPermissionRuleShouldBeFound("disabled.notEquals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where disabled in DEFAULT_DISABLED or UPDATED_DISABLED
        defaultDataPermissionRuleShouldBeFound("disabled.in=" + DEFAULT_DISABLED + "," + UPDATED_DISABLED);

        // Get all the dataPermissionRuleList where disabled equals to UPDATED_DISABLED
        defaultDataPermissionRuleShouldNotBeFound("disabled.in=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where disabled is not null
        defaultDataPermissionRuleShouldBeFound("disabled.specified=true");

        // Get all the dataPermissionRuleList where disabled is null
        defaultDataPermissionRuleShouldNotBeFound("disabled.specified=false");
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByRemovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where removedAt equals to DEFAULT_REMOVED_AT
        defaultDataPermissionRuleShouldBeFound("removedAt.equals=" + DEFAULT_REMOVED_AT);

        // Get all the dataPermissionRuleList where removedAt equals to UPDATED_REMOVED_AT
        defaultDataPermissionRuleShouldNotBeFound("removedAt.equals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByRemovedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where removedAt not equals to DEFAULT_REMOVED_AT
        defaultDataPermissionRuleShouldNotBeFound("removedAt.notEquals=" + DEFAULT_REMOVED_AT);

        // Get all the dataPermissionRuleList where removedAt not equals to UPDATED_REMOVED_AT
        defaultDataPermissionRuleShouldBeFound("removedAt.notEquals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByRemovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where removedAt in DEFAULT_REMOVED_AT or UPDATED_REMOVED_AT
        defaultDataPermissionRuleShouldBeFound("removedAt.in=" + DEFAULT_REMOVED_AT + "," + UPDATED_REMOVED_AT);

        // Get all the dataPermissionRuleList where removedAt equals to UPDATED_REMOVED_AT
        defaultDataPermissionRuleShouldNotBeFound("removedAt.in=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByRemovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where removedAt is not null
        defaultDataPermissionRuleShouldBeFound("removedAt.specified=true");

        // Get all the dataPermissionRuleList where removedAt is null
        defaultDataPermissionRuleShouldNotBeFound("removedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByRemovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where removedAt is greater than or equal to DEFAULT_REMOVED_AT
        defaultDataPermissionRuleShouldBeFound("removedAt.greaterThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the dataPermissionRuleList where removedAt is greater than or equal to UPDATED_REMOVED_AT
        defaultDataPermissionRuleShouldNotBeFound("removedAt.greaterThanOrEqual=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByRemovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where removedAt is less than or equal to DEFAULT_REMOVED_AT
        defaultDataPermissionRuleShouldBeFound("removedAt.lessThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the dataPermissionRuleList where removedAt is less than or equal to SMALLER_REMOVED_AT
        defaultDataPermissionRuleShouldNotBeFound("removedAt.lessThanOrEqual=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByRemovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where removedAt is less than DEFAULT_REMOVED_AT
        defaultDataPermissionRuleShouldNotBeFound("removedAt.lessThan=" + DEFAULT_REMOVED_AT);

        // Get all the dataPermissionRuleList where removedAt is less than UPDATED_REMOVED_AT
        defaultDataPermissionRuleShouldBeFound("removedAt.lessThan=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataPermissionRulesByRemovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        // Get all the dataPermissionRuleList where removedAt is greater than DEFAULT_REMOVED_AT
        defaultDataPermissionRuleShouldNotBeFound("removedAt.greaterThan=" + DEFAULT_REMOVED_AT);

        // Get all the dataPermissionRuleList where removedAt is greater than SMALLER_REMOVED_AT
        defaultDataPermissionRuleShouldBeFound("removedAt.greaterThan=" + SMALLER_REMOVED_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDataPermissionRuleShouldBeFound(String filter) throws Exception {
        restDataPermissionRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataPermissionRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].permissionId").value(hasItem(DEFAULT_PERMISSION_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].column").value(hasItem(DEFAULT_COLUMN)))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));

        // Check, that the count call also returns 1
        restDataPermissionRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDataPermissionRuleShouldNotBeFound(String filter) throws Exception {
        restDataPermissionRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDataPermissionRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDataPermissionRule() throws Exception {
        // Get the dataPermissionRule
        restDataPermissionRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDataPermissionRule() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        int databaseSizeBeforeUpdate = dataPermissionRuleRepository.findAll().size();

        // Update the dataPermissionRule
        DataPermissionRule updatedDataPermissionRule = dataPermissionRuleRepository.findById(dataPermissionRule.getId()).get();
        // Disconnect from session so that the updates on updatedDataPermissionRule are not directly saved in db
        updatedDataPermissionRule
            .permissionId(UPDATED_PERMISSION_ID)
            .name(UPDATED_NAME)
            .column(UPDATED_COLUMN)
            .conditions(UPDATED_CONDITIONS)
            .value(UPDATED_VALUE)
            .disabled(UPDATED_DISABLED)
            .removedAt(UPDATED_REMOVED_AT);
        DataPermissionRuleDTO dataPermissionRuleDTO = dataPermissionRuleMapper.toDto(updatedDataPermissionRule);

        restDataPermissionRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataPermissionRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataPermissionRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the DataPermissionRule in the database
        List<DataPermissionRule> dataPermissionRuleList = dataPermissionRuleRepository.findAll();
        assertThat(dataPermissionRuleList).hasSize(databaseSizeBeforeUpdate);
        DataPermissionRule testDataPermissionRule = dataPermissionRuleList.get(dataPermissionRuleList.size() - 1);
        assertThat(testDataPermissionRule.getPermissionId()).isEqualTo(UPDATED_PERMISSION_ID);
        assertThat(testDataPermissionRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataPermissionRule.getColumn()).isEqualTo(UPDATED_COLUMN);
        assertThat(testDataPermissionRule.getConditions()).isEqualTo(UPDATED_CONDITIONS);
        assertThat(testDataPermissionRule.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testDataPermissionRule.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testDataPermissionRule.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void putNonExistingDataPermissionRule() throws Exception {
        int databaseSizeBeforeUpdate = dataPermissionRuleRepository.findAll().size();
        dataPermissionRule.setId(count.incrementAndGet());

        // Create the DataPermissionRule
        DataPermissionRuleDTO dataPermissionRuleDTO = dataPermissionRuleMapper.toDto(dataPermissionRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataPermissionRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataPermissionRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataPermissionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataPermissionRule in the database
        List<DataPermissionRule> dataPermissionRuleList = dataPermissionRuleRepository.findAll();
        assertThat(dataPermissionRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataPermissionRule() throws Exception {
        int databaseSizeBeforeUpdate = dataPermissionRuleRepository.findAll().size();
        dataPermissionRule.setId(count.incrementAndGet());

        // Create the DataPermissionRule
        DataPermissionRuleDTO dataPermissionRuleDTO = dataPermissionRuleMapper.toDto(dataPermissionRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataPermissionRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataPermissionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataPermissionRule in the database
        List<DataPermissionRule> dataPermissionRuleList = dataPermissionRuleRepository.findAll();
        assertThat(dataPermissionRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataPermissionRule() throws Exception {
        int databaseSizeBeforeUpdate = dataPermissionRuleRepository.findAll().size();
        dataPermissionRule.setId(count.incrementAndGet());

        // Create the DataPermissionRule
        DataPermissionRuleDTO dataPermissionRuleDTO = dataPermissionRuleMapper.toDto(dataPermissionRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataPermissionRuleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataPermissionRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataPermissionRule in the database
        List<DataPermissionRule> dataPermissionRuleList = dataPermissionRuleRepository.findAll();
        assertThat(dataPermissionRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataPermissionRuleWithPatch() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        int databaseSizeBeforeUpdate = dataPermissionRuleRepository.findAll().size();

        // Update the dataPermissionRule using partial update
        DataPermissionRule partialUpdatedDataPermissionRule = new DataPermissionRule();
        partialUpdatedDataPermissionRule.setId(dataPermissionRule.getId());

        partialUpdatedDataPermissionRule
            .name(UPDATED_NAME)
            .column(UPDATED_COLUMN)
            .value(UPDATED_VALUE)
            .disabled(UPDATED_DISABLED)
            .removedAt(UPDATED_REMOVED_AT);

        restDataPermissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataPermissionRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataPermissionRule))
            )
            .andExpect(status().isOk());

        // Validate the DataPermissionRule in the database
        List<DataPermissionRule> dataPermissionRuleList = dataPermissionRuleRepository.findAll();
        assertThat(dataPermissionRuleList).hasSize(databaseSizeBeforeUpdate);
        DataPermissionRule testDataPermissionRule = dataPermissionRuleList.get(dataPermissionRuleList.size() - 1);
        assertThat(testDataPermissionRule.getPermissionId()).isEqualTo(DEFAULT_PERMISSION_ID);
        assertThat(testDataPermissionRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataPermissionRule.getColumn()).isEqualTo(UPDATED_COLUMN);
        assertThat(testDataPermissionRule.getConditions()).isEqualTo(DEFAULT_CONDITIONS);
        assertThat(testDataPermissionRule.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testDataPermissionRule.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testDataPermissionRule.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void fullUpdateDataPermissionRuleWithPatch() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        int databaseSizeBeforeUpdate = dataPermissionRuleRepository.findAll().size();

        // Update the dataPermissionRule using partial update
        DataPermissionRule partialUpdatedDataPermissionRule = new DataPermissionRule();
        partialUpdatedDataPermissionRule.setId(dataPermissionRule.getId());

        partialUpdatedDataPermissionRule
            .permissionId(UPDATED_PERMISSION_ID)
            .name(UPDATED_NAME)
            .column(UPDATED_COLUMN)
            .conditions(UPDATED_CONDITIONS)
            .value(UPDATED_VALUE)
            .disabled(UPDATED_DISABLED)
            .removedAt(UPDATED_REMOVED_AT);

        restDataPermissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataPermissionRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataPermissionRule))
            )
            .andExpect(status().isOk());

        // Validate the DataPermissionRule in the database
        List<DataPermissionRule> dataPermissionRuleList = dataPermissionRuleRepository.findAll();
        assertThat(dataPermissionRuleList).hasSize(databaseSizeBeforeUpdate);
        DataPermissionRule testDataPermissionRule = dataPermissionRuleList.get(dataPermissionRuleList.size() - 1);
        assertThat(testDataPermissionRule.getPermissionId()).isEqualTo(UPDATED_PERMISSION_ID);
        assertThat(testDataPermissionRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataPermissionRule.getColumn()).isEqualTo(UPDATED_COLUMN);
        assertThat(testDataPermissionRule.getConditions()).isEqualTo(UPDATED_CONDITIONS);
        assertThat(testDataPermissionRule.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testDataPermissionRule.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testDataPermissionRule.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingDataPermissionRule() throws Exception {
        int databaseSizeBeforeUpdate = dataPermissionRuleRepository.findAll().size();
        dataPermissionRule.setId(count.incrementAndGet());

        // Create the DataPermissionRule
        DataPermissionRuleDTO dataPermissionRuleDTO = dataPermissionRuleMapper.toDto(dataPermissionRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataPermissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataPermissionRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataPermissionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataPermissionRule in the database
        List<DataPermissionRule> dataPermissionRuleList = dataPermissionRuleRepository.findAll();
        assertThat(dataPermissionRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataPermissionRule() throws Exception {
        int databaseSizeBeforeUpdate = dataPermissionRuleRepository.findAll().size();
        dataPermissionRule.setId(count.incrementAndGet());

        // Create the DataPermissionRule
        DataPermissionRuleDTO dataPermissionRuleDTO = dataPermissionRuleMapper.toDto(dataPermissionRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataPermissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataPermissionRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataPermissionRule in the database
        List<DataPermissionRule> dataPermissionRuleList = dataPermissionRuleRepository.findAll();
        assertThat(dataPermissionRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataPermissionRule() throws Exception {
        int databaseSizeBeforeUpdate = dataPermissionRuleRepository.findAll().size();
        dataPermissionRule.setId(count.incrementAndGet());

        // Create the DataPermissionRule
        DataPermissionRuleDTO dataPermissionRuleDTO = dataPermissionRuleMapper.toDto(dataPermissionRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataPermissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataPermissionRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataPermissionRule in the database
        List<DataPermissionRule> dataPermissionRuleList = dataPermissionRuleRepository.findAll();
        assertThat(dataPermissionRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataPermissionRule() throws Exception {
        // Initialize the database
        dataPermissionRuleRepository.insert(dataPermissionRule);

        int databaseSizeBeforeDelete = dataPermissionRuleRepository.findAll().size();

        // Delete the dataPermissionRule
        restDataPermissionRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataPermissionRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataPermissionRule> dataPermissionRuleList = dataPermissionRuleRepository.findAll();
        assertThat(dataPermissionRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
