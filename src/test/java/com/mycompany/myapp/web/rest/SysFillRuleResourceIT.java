package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.SysFillRule;
import com.mycompany.myapp.repository.SysFillRuleRepository;
import com.mycompany.myapp.service.SysFillRuleQueryService;
import com.mycompany.myapp.service.criteria.SysFillRuleCriteria;
import com.mycompany.myapp.service.dto.SysFillRuleDTO;
import com.mycompany.myapp.service.mapper.SysFillRuleMapper;
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
 * Integration tests for the {@link SysFillRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SysFillRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_IMPL_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_IMPL_CLASS = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMS = "AAAAAAAAAA";
    private static final String UPDATED_PARAMS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REMOVED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMOVED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REMOVED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/sys-fill-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SysFillRuleRepository sysFillRuleRepository;

    @Autowired
    private SysFillRuleMapper sysFillRuleMapper;

    @Autowired
    private MockMvc restSysFillRuleMockMvc;

    private SysFillRule sysFillRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysFillRule createEntity() {
        SysFillRule sysFillRule = new SysFillRule()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .implClass(DEFAULT_IMPL_CLASS)
            .params(DEFAULT_PARAMS)
            .removedAt(DEFAULT_REMOVED_AT);
        return sysFillRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SysFillRule createUpdatedEntity() {
        SysFillRule sysFillRule = new SysFillRule()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .implClass(UPDATED_IMPL_CLASS)
            .params(UPDATED_PARAMS)
            .removedAt(UPDATED_REMOVED_AT);
        return sysFillRule;
    }

    @BeforeEach
    public void initTest() {
        sysFillRule = createEntity();
    }

    @Test
    @Transactional
    void createSysFillRule() throws Exception {
        int databaseSizeBeforeCreate = sysFillRuleRepository.findAll().size();
        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);
        restSysFillRuleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeCreate + 1);
        SysFillRule testSysFillRule = sysFillRuleList.get(sysFillRuleList.size() - 1);
        assertThat(testSysFillRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSysFillRule.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSysFillRule.getImplClass()).isEqualTo(DEFAULT_IMPL_CLASS);
        assertThat(testSysFillRule.getParams()).isEqualTo(DEFAULT_PARAMS);
        assertThat(testSysFillRule.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void createSysFillRuleWithExistingId() throws Exception {
        // Create the SysFillRule with an existing ID
        sysFillRule.setId(1L);
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        int databaseSizeBeforeCreate = sysFillRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSysFillRuleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSysFillRules() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList
        restSysFillRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysFillRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].implClass").value(hasItem(DEFAULT_IMPL_CLASS)))
            .andExpect(jsonPath("$.[*].params").value(hasItem(DEFAULT_PARAMS)))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));
    }

    @Test
    @Transactional
    void getSysFillRule() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get the sysFillRule
        restSysFillRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, sysFillRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sysFillRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.implClass").value(DEFAULT_IMPL_CLASS))
            .andExpect(jsonPath("$.params").value(DEFAULT_PARAMS))
            .andExpect(jsonPath("$.removedAt").value(DEFAULT_REMOVED_AT.toString()));
    }

    @Test
    @Transactional
    void getSysFillRulesByIdFiltering() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        Long id = sysFillRule.getId();

        defaultSysFillRuleShouldBeFound("id.equals=" + id);
        defaultSysFillRuleShouldNotBeFound("id.notEquals=" + id);

        defaultSysFillRuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSysFillRuleShouldNotBeFound("id.greaterThan=" + id);

        defaultSysFillRuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSysFillRuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where name equals to DEFAULT_NAME
        defaultSysFillRuleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the sysFillRuleList where name equals to UPDATED_NAME
        defaultSysFillRuleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where name not equals to DEFAULT_NAME
        defaultSysFillRuleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the sysFillRuleList where name not equals to UPDATED_NAME
        defaultSysFillRuleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSysFillRuleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the sysFillRuleList where name equals to UPDATED_NAME
        defaultSysFillRuleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where name is not null
        defaultSysFillRuleShouldBeFound("name.specified=true");

        // Get all the sysFillRuleList where name is null
        defaultSysFillRuleShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where name contains DEFAULT_NAME
        defaultSysFillRuleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the sysFillRuleList where name contains UPDATED_NAME
        defaultSysFillRuleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where name does not contain DEFAULT_NAME
        defaultSysFillRuleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the sysFillRuleList where name does not contain UPDATED_NAME
        defaultSysFillRuleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where code equals to DEFAULT_CODE
        defaultSysFillRuleShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the sysFillRuleList where code equals to UPDATED_CODE
        defaultSysFillRuleShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where code not equals to DEFAULT_CODE
        defaultSysFillRuleShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the sysFillRuleList where code not equals to UPDATED_CODE
        defaultSysFillRuleShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where code in DEFAULT_CODE or UPDATED_CODE
        defaultSysFillRuleShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the sysFillRuleList where code equals to UPDATED_CODE
        defaultSysFillRuleShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where code is not null
        defaultSysFillRuleShouldBeFound("code.specified=true");

        // Get all the sysFillRuleList where code is null
        defaultSysFillRuleShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where code contains DEFAULT_CODE
        defaultSysFillRuleShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the sysFillRuleList where code contains UPDATED_CODE
        defaultSysFillRuleShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where code does not contain DEFAULT_CODE
        defaultSysFillRuleShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the sysFillRuleList where code does not contain UPDATED_CODE
        defaultSysFillRuleShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where implClass equals to DEFAULT_IMPL_CLASS
        defaultSysFillRuleShouldBeFound("implClass.equals=" + DEFAULT_IMPL_CLASS);

        // Get all the sysFillRuleList where implClass equals to UPDATED_IMPL_CLASS
        defaultSysFillRuleShouldNotBeFound("implClass.equals=" + UPDATED_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where implClass not equals to DEFAULT_IMPL_CLASS
        defaultSysFillRuleShouldNotBeFound("implClass.notEquals=" + DEFAULT_IMPL_CLASS);

        // Get all the sysFillRuleList where implClass not equals to UPDATED_IMPL_CLASS
        defaultSysFillRuleShouldBeFound("implClass.notEquals=" + UPDATED_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where implClass in DEFAULT_IMPL_CLASS or UPDATED_IMPL_CLASS
        defaultSysFillRuleShouldBeFound("implClass.in=" + DEFAULT_IMPL_CLASS + "," + UPDATED_IMPL_CLASS);

        // Get all the sysFillRuleList where implClass equals to UPDATED_IMPL_CLASS
        defaultSysFillRuleShouldNotBeFound("implClass.in=" + UPDATED_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where implClass is not null
        defaultSysFillRuleShouldBeFound("implClass.specified=true");

        // Get all the sysFillRuleList where implClass is null
        defaultSysFillRuleShouldNotBeFound("implClass.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where implClass contains DEFAULT_IMPL_CLASS
        defaultSysFillRuleShouldBeFound("implClass.contains=" + DEFAULT_IMPL_CLASS);

        // Get all the sysFillRuleList where implClass contains UPDATED_IMPL_CLASS
        defaultSysFillRuleShouldNotBeFound("implClass.contains=" + UPDATED_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByImplClassNotContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where implClass does not contain DEFAULT_IMPL_CLASS
        defaultSysFillRuleShouldNotBeFound("implClass.doesNotContain=" + DEFAULT_IMPL_CLASS);

        // Get all the sysFillRuleList where implClass does not contain UPDATED_IMPL_CLASS
        defaultSysFillRuleShouldBeFound("implClass.doesNotContain=" + UPDATED_IMPL_CLASS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where params equals to DEFAULT_PARAMS
        defaultSysFillRuleShouldBeFound("params.equals=" + DEFAULT_PARAMS);

        // Get all the sysFillRuleList where params equals to UPDATED_PARAMS
        defaultSysFillRuleShouldNotBeFound("params.equals=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where params not equals to DEFAULT_PARAMS
        defaultSysFillRuleShouldNotBeFound("params.notEquals=" + DEFAULT_PARAMS);

        // Get all the sysFillRuleList where params not equals to UPDATED_PARAMS
        defaultSysFillRuleShouldBeFound("params.notEquals=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where params in DEFAULT_PARAMS or UPDATED_PARAMS
        defaultSysFillRuleShouldBeFound("params.in=" + DEFAULT_PARAMS + "," + UPDATED_PARAMS);

        // Get all the sysFillRuleList where params equals to UPDATED_PARAMS
        defaultSysFillRuleShouldNotBeFound("params.in=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where params is not null
        defaultSysFillRuleShouldBeFound("params.specified=true");

        // Get all the sysFillRuleList where params is null
        defaultSysFillRuleShouldNotBeFound("params.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where params contains DEFAULT_PARAMS
        defaultSysFillRuleShouldBeFound("params.contains=" + DEFAULT_PARAMS);

        // Get all the sysFillRuleList where params contains UPDATED_PARAMS
        defaultSysFillRuleShouldNotBeFound("params.contains=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByParamsNotContainsSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where params does not contain DEFAULT_PARAMS
        defaultSysFillRuleShouldNotBeFound("params.doesNotContain=" + DEFAULT_PARAMS);

        // Get all the sysFillRuleList where params does not contain UPDATED_PARAMS
        defaultSysFillRuleShouldBeFound("params.doesNotContain=" + UPDATED_PARAMS);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByRemovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where removedAt equals to DEFAULT_REMOVED_AT
        defaultSysFillRuleShouldBeFound("removedAt.equals=" + DEFAULT_REMOVED_AT);

        // Get all the sysFillRuleList where removedAt equals to UPDATED_REMOVED_AT
        defaultSysFillRuleShouldNotBeFound("removedAt.equals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByRemovedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where removedAt not equals to DEFAULT_REMOVED_AT
        defaultSysFillRuleShouldNotBeFound("removedAt.notEquals=" + DEFAULT_REMOVED_AT);

        // Get all the sysFillRuleList where removedAt not equals to UPDATED_REMOVED_AT
        defaultSysFillRuleShouldBeFound("removedAt.notEquals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByRemovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where removedAt in DEFAULT_REMOVED_AT or UPDATED_REMOVED_AT
        defaultSysFillRuleShouldBeFound("removedAt.in=" + DEFAULT_REMOVED_AT + "," + UPDATED_REMOVED_AT);

        // Get all the sysFillRuleList where removedAt equals to UPDATED_REMOVED_AT
        defaultSysFillRuleShouldNotBeFound("removedAt.in=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByRemovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where removedAt is not null
        defaultSysFillRuleShouldBeFound("removedAt.specified=true");

        // Get all the sysFillRuleList where removedAt is null
        defaultSysFillRuleShouldNotBeFound("removedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSysFillRulesByRemovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where removedAt is greater than or equal to DEFAULT_REMOVED_AT
        defaultSysFillRuleShouldBeFound("removedAt.greaterThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the sysFillRuleList where removedAt is greater than or equal to UPDATED_REMOVED_AT
        defaultSysFillRuleShouldNotBeFound("removedAt.greaterThanOrEqual=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByRemovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where removedAt is less than or equal to DEFAULT_REMOVED_AT
        defaultSysFillRuleShouldBeFound("removedAt.lessThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the sysFillRuleList where removedAt is less than or equal to SMALLER_REMOVED_AT
        defaultSysFillRuleShouldNotBeFound("removedAt.lessThanOrEqual=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByRemovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where removedAt is less than DEFAULT_REMOVED_AT
        defaultSysFillRuleShouldNotBeFound("removedAt.lessThan=" + DEFAULT_REMOVED_AT);

        // Get all the sysFillRuleList where removedAt is less than UPDATED_REMOVED_AT
        defaultSysFillRuleShouldBeFound("removedAt.lessThan=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllSysFillRulesByRemovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        // Get all the sysFillRuleList where removedAt is greater than DEFAULT_REMOVED_AT
        defaultSysFillRuleShouldNotBeFound("removedAt.greaterThan=" + DEFAULT_REMOVED_AT);

        // Get all the sysFillRuleList where removedAt is greater than SMALLER_REMOVED_AT
        defaultSysFillRuleShouldBeFound("removedAt.greaterThan=" + SMALLER_REMOVED_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSysFillRuleShouldBeFound(String filter) throws Exception {
        restSysFillRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sysFillRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].implClass").value(hasItem(DEFAULT_IMPL_CLASS)))
            .andExpect(jsonPath("$.[*].params").value(hasItem(DEFAULT_PARAMS)))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));

        // Check, that the count call also returns 1
        restSysFillRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSysFillRuleShouldNotBeFound(String filter) throws Exception {
        restSysFillRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSysFillRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSysFillRule() throws Exception {
        // Get the sysFillRule
        restSysFillRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSysFillRule() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();

        // Update the sysFillRule
        SysFillRule updatedSysFillRule = sysFillRuleRepository.findById(sysFillRule.getId()).get();
        // Disconnect from session so that the updates on updatedSysFillRule are not directly saved in db
        updatedSysFillRule
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .implClass(UPDATED_IMPL_CLASS)
            .params(UPDATED_PARAMS)
            .removedAt(UPDATED_REMOVED_AT);
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(updatedSysFillRule);

        restSysFillRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysFillRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
        SysFillRule testSysFillRule = sysFillRuleList.get(sysFillRuleList.size() - 1);
        assertThat(testSysFillRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSysFillRule.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSysFillRule.getImplClass()).isEqualTo(UPDATED_IMPL_CLASS);
        assertThat(testSysFillRule.getParams()).isEqualTo(UPDATED_PARAMS);
        assertThat(testSysFillRule.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void putNonExistingSysFillRule() throws Exception {
        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();
        sysFillRule.setId(count.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sysFillRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSysFillRule() throws Exception {
        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();
        sysFillRule.setId(count.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSysFillRule() throws Exception {
        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();
        sysFillRule.setId(count.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSysFillRuleWithPatch() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();

        // Update the sysFillRule using partial update
        SysFillRule partialUpdatedSysFillRule = new SysFillRule();
        partialUpdatedSysFillRule.setId(sysFillRule.getId());

        partialUpdatedSysFillRule.name(UPDATED_NAME).code(UPDATED_CODE).params(UPDATED_PARAMS).removedAt(UPDATED_REMOVED_AT);

        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysFillRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysFillRule))
            )
            .andExpect(status().isOk());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
        SysFillRule testSysFillRule = sysFillRuleList.get(sysFillRuleList.size() - 1);
        assertThat(testSysFillRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSysFillRule.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSysFillRule.getImplClass()).isEqualTo(DEFAULT_IMPL_CLASS);
        assertThat(testSysFillRule.getParams()).isEqualTo(UPDATED_PARAMS);
        assertThat(testSysFillRule.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void fullUpdateSysFillRuleWithPatch() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();

        // Update the sysFillRule using partial update
        SysFillRule partialUpdatedSysFillRule = new SysFillRule();
        partialUpdatedSysFillRule.setId(sysFillRule.getId());

        partialUpdatedSysFillRule
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .implClass(UPDATED_IMPL_CLASS)
            .params(UPDATED_PARAMS)
            .removedAt(UPDATED_REMOVED_AT);

        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSysFillRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSysFillRule))
            )
            .andExpect(status().isOk());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
        SysFillRule testSysFillRule = sysFillRuleList.get(sysFillRuleList.size() - 1);
        assertThat(testSysFillRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSysFillRule.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSysFillRule.getImplClass()).isEqualTo(UPDATED_IMPL_CLASS);
        assertThat(testSysFillRule.getParams()).isEqualTo(UPDATED_PARAMS);
        assertThat(testSysFillRule.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingSysFillRule() throws Exception {
        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();
        sysFillRule.setId(count.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sysFillRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSysFillRule() throws Exception {
        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();
        sysFillRule.setId(count.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSysFillRule() throws Exception {
        int databaseSizeBeforeUpdate = sysFillRuleRepository.findAll().size();
        sysFillRule.setId(count.incrementAndGet());

        // Create the SysFillRule
        SysFillRuleDTO sysFillRuleDTO = sysFillRuleMapper.toDto(sysFillRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSysFillRuleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sysFillRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SysFillRule in the database
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSysFillRule() throws Exception {
        // Initialize the database
        sysFillRuleRepository.insert(sysFillRule);

        int databaseSizeBeforeDelete = sysFillRuleRepository.findAll().size();

        // Delete the sysFillRule
        restSysFillRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, sysFillRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SysFillRule> sysFillRuleList = sysFillRuleRepository.findAll();
        assertThat(sysFillRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
