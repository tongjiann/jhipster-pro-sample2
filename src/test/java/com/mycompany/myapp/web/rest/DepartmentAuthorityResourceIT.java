package com.mycompany.myapp.web.rest;

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
import com.mycompany.myapp.domain.ApiPermission;
import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.domain.DepartmentAuthority;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.ViewPermission;
import com.mycompany.myapp.repository.DepartmentAuthorityRepository;
import com.mycompany.myapp.service.DepartmentAuthorityQueryService;
import com.mycompany.myapp.service.criteria.DepartmentAuthorityCriteria;
import com.mycompany.myapp.service.dto.DepartmentAuthorityDTO;
import com.mycompany.myapp.service.mapper.DepartmentAuthorityMapper;
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
 * Integration tests for the {@link DepartmentAuthorityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepartmentAuthorityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATE_USER_ID = 1L;
    private static final Long UPDATED_CREATE_USER_ID = 2L;
    private static final Long SMALLER_CREATE_USER_ID = 1L - 1L;

    private static final ZonedDateTime DEFAULT_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final LocalDate DEFAULT_REMOVED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMOVED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REMOVED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/department-authorities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepartmentAuthorityRepository departmentAuthorityRepository;

    @Autowired
    private DepartmentAuthorityMapper departmentAuthorityMapper;

    @Autowired
    private MockMvc restDepartmentAuthorityMockMvc;

    private DepartmentAuthority departmentAuthority;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentAuthority createEntity() {
        DepartmentAuthority departmentAuthority = new DepartmentAuthority()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .createUserId(DEFAULT_CREATE_USER_ID)
            .createTime(DEFAULT_CREATE_TIME)
            .removedAt(DEFAULT_REMOVED_AT);
        return departmentAuthority;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepartmentAuthority createUpdatedEntity() {
        DepartmentAuthority departmentAuthority = new DepartmentAuthority()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .createUserId(UPDATED_CREATE_USER_ID)
            .createTime(UPDATED_CREATE_TIME)
            .removedAt(UPDATED_REMOVED_AT);
        return departmentAuthority;
    }

    @BeforeEach
    public void initTest() {
        departmentAuthority = createEntity();
    }

    @Test
    @Transactional
    void createDepartmentAuthority() throws Exception {
        int databaseSizeBeforeCreate = departmentAuthorityRepository.findAll().size();
        // Create the DepartmentAuthority
        DepartmentAuthorityDTO departmentAuthorityDTO = departmentAuthorityMapper.toDto(departmentAuthority);
        restDepartmentAuthorityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentAuthorityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DepartmentAuthority in the database
        List<DepartmentAuthority> departmentAuthorityList = departmentAuthorityRepository.findAll();
        assertThat(departmentAuthorityList).hasSize(databaseSizeBeforeCreate + 1);
        DepartmentAuthority testDepartmentAuthority = departmentAuthorityList.get(departmentAuthorityList.size() - 1);
        assertThat(testDepartmentAuthority.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDepartmentAuthority.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDepartmentAuthority.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDepartmentAuthority.getCreateUserId()).isEqualTo(DEFAULT_CREATE_USER_ID);
        assertThat(testDepartmentAuthority.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testDepartmentAuthority.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void createDepartmentAuthorityWithExistingId() throws Exception {
        // Create the DepartmentAuthority with an existing ID
        departmentAuthority.setId(1L);
        DepartmentAuthorityDTO departmentAuthorityDTO = departmentAuthorityMapper.toDto(departmentAuthority);

        int databaseSizeBeforeCreate = departmentAuthorityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentAuthorityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentAuthorityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentAuthority in the database
        List<DepartmentAuthority> departmentAuthorityList = departmentAuthorityRepository.findAll();
        assertThat(departmentAuthorityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthorities() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList
        restDepartmentAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentAuthority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createUserId").value(hasItem(DEFAULT_CREATE_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(sameInstant(DEFAULT_CREATE_TIME))))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));
    }

    @Test
    @Transactional
    void getDepartmentAuthority() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get the departmentAuthority
        restDepartmentAuthorityMockMvc
            .perform(get(ENTITY_API_URL_ID, departmentAuthority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departmentAuthority.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createUserId").value(DEFAULT_CREATE_USER_ID.intValue()))
            .andExpect(jsonPath("$.createTime").value(sameInstant(DEFAULT_CREATE_TIME)))
            .andExpect(jsonPath("$.removedAt").value(DEFAULT_REMOVED_AT.toString()));
    }

    @Test
    @Transactional
    void getDepartmentAuthoritiesByIdFiltering() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        Long id = departmentAuthority.getId();

        defaultDepartmentAuthorityShouldBeFound("id.equals=" + id);
        defaultDepartmentAuthorityShouldNotBeFound("id.notEquals=" + id);

        defaultDepartmentAuthorityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepartmentAuthorityShouldNotBeFound("id.greaterThan=" + id);

        defaultDepartmentAuthorityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepartmentAuthorityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where name equals to DEFAULT_NAME
        defaultDepartmentAuthorityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the departmentAuthorityList where name equals to UPDATED_NAME
        defaultDepartmentAuthorityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where name not equals to DEFAULT_NAME
        defaultDepartmentAuthorityShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the departmentAuthorityList where name not equals to UPDATED_NAME
        defaultDepartmentAuthorityShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDepartmentAuthorityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the departmentAuthorityList where name equals to UPDATED_NAME
        defaultDepartmentAuthorityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where name is not null
        defaultDepartmentAuthorityShouldBeFound("name.specified=true");

        // Get all the departmentAuthorityList where name is null
        defaultDepartmentAuthorityShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByNameContainsSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where name contains DEFAULT_NAME
        defaultDepartmentAuthorityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the departmentAuthorityList where name contains UPDATED_NAME
        defaultDepartmentAuthorityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where name does not contain DEFAULT_NAME
        defaultDepartmentAuthorityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the departmentAuthorityList where name does not contain UPDATED_NAME
        defaultDepartmentAuthorityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where code equals to DEFAULT_CODE
        defaultDepartmentAuthorityShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the departmentAuthorityList where code equals to UPDATED_CODE
        defaultDepartmentAuthorityShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where code not equals to DEFAULT_CODE
        defaultDepartmentAuthorityShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the departmentAuthorityList where code not equals to UPDATED_CODE
        defaultDepartmentAuthorityShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where code in DEFAULT_CODE or UPDATED_CODE
        defaultDepartmentAuthorityShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the departmentAuthorityList where code equals to UPDATED_CODE
        defaultDepartmentAuthorityShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where code is not null
        defaultDepartmentAuthorityShouldBeFound("code.specified=true");

        // Get all the departmentAuthorityList where code is null
        defaultDepartmentAuthorityShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCodeContainsSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where code contains DEFAULT_CODE
        defaultDepartmentAuthorityShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the departmentAuthorityList where code contains UPDATED_CODE
        defaultDepartmentAuthorityShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where code does not contain DEFAULT_CODE
        defaultDepartmentAuthorityShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the departmentAuthorityList where code does not contain UPDATED_CODE
        defaultDepartmentAuthorityShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where description equals to DEFAULT_DESCRIPTION
        defaultDepartmentAuthorityShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the departmentAuthorityList where description equals to UPDATED_DESCRIPTION
        defaultDepartmentAuthorityShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where description not equals to DEFAULT_DESCRIPTION
        defaultDepartmentAuthorityShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the departmentAuthorityList where description not equals to UPDATED_DESCRIPTION
        defaultDepartmentAuthorityShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDepartmentAuthorityShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the departmentAuthorityList where description equals to UPDATED_DESCRIPTION
        defaultDepartmentAuthorityShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where description is not null
        defaultDepartmentAuthorityShouldBeFound("description.specified=true");

        // Get all the departmentAuthorityList where description is null
        defaultDepartmentAuthorityShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where description contains DEFAULT_DESCRIPTION
        defaultDepartmentAuthorityShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the departmentAuthorityList where description contains UPDATED_DESCRIPTION
        defaultDepartmentAuthorityShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where description does not contain DEFAULT_DESCRIPTION
        defaultDepartmentAuthorityShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the departmentAuthorityList where description does not contain UPDATED_DESCRIPTION
        defaultDepartmentAuthorityShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createUserId equals to DEFAULT_CREATE_USER_ID
        defaultDepartmentAuthorityShouldBeFound("createUserId.equals=" + DEFAULT_CREATE_USER_ID);

        // Get all the departmentAuthorityList where createUserId equals to UPDATED_CREATE_USER_ID
        defaultDepartmentAuthorityShouldNotBeFound("createUserId.equals=" + UPDATED_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createUserId not equals to DEFAULT_CREATE_USER_ID
        defaultDepartmentAuthorityShouldNotBeFound("createUserId.notEquals=" + DEFAULT_CREATE_USER_ID);

        // Get all the departmentAuthorityList where createUserId not equals to UPDATED_CREATE_USER_ID
        defaultDepartmentAuthorityShouldBeFound("createUserId.notEquals=" + UPDATED_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createUserId in DEFAULT_CREATE_USER_ID or UPDATED_CREATE_USER_ID
        defaultDepartmentAuthorityShouldBeFound("createUserId.in=" + DEFAULT_CREATE_USER_ID + "," + UPDATED_CREATE_USER_ID);

        // Get all the departmentAuthorityList where createUserId equals to UPDATED_CREATE_USER_ID
        defaultDepartmentAuthorityShouldNotBeFound("createUserId.in=" + UPDATED_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createUserId is not null
        defaultDepartmentAuthorityShouldBeFound("createUserId.specified=true");

        // Get all the departmentAuthorityList where createUserId is null
        defaultDepartmentAuthorityShouldNotBeFound("createUserId.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createUserId is greater than or equal to DEFAULT_CREATE_USER_ID
        defaultDepartmentAuthorityShouldBeFound("createUserId.greaterThanOrEqual=" + DEFAULT_CREATE_USER_ID);

        // Get all the departmentAuthorityList where createUserId is greater than or equal to UPDATED_CREATE_USER_ID
        defaultDepartmentAuthorityShouldNotBeFound("createUserId.greaterThanOrEqual=" + UPDATED_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createUserId is less than or equal to DEFAULT_CREATE_USER_ID
        defaultDepartmentAuthorityShouldBeFound("createUserId.lessThanOrEqual=" + DEFAULT_CREATE_USER_ID);

        // Get all the departmentAuthorityList where createUserId is less than or equal to SMALLER_CREATE_USER_ID
        defaultDepartmentAuthorityShouldNotBeFound("createUserId.lessThanOrEqual=" + SMALLER_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createUserId is less than DEFAULT_CREATE_USER_ID
        defaultDepartmentAuthorityShouldNotBeFound("createUserId.lessThan=" + DEFAULT_CREATE_USER_ID);

        // Get all the departmentAuthorityList where createUserId is less than UPDATED_CREATE_USER_ID
        defaultDepartmentAuthorityShouldBeFound("createUserId.lessThan=" + UPDATED_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createUserId is greater than DEFAULT_CREATE_USER_ID
        defaultDepartmentAuthorityShouldNotBeFound("createUserId.greaterThan=" + DEFAULT_CREATE_USER_ID);

        // Get all the departmentAuthorityList where createUserId is greater than SMALLER_CREATE_USER_ID
        defaultDepartmentAuthorityShouldBeFound("createUserId.greaterThan=" + SMALLER_CREATE_USER_ID);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createTime equals to DEFAULT_CREATE_TIME
        defaultDepartmentAuthorityShouldBeFound("createTime.equals=" + DEFAULT_CREATE_TIME);

        // Get all the departmentAuthorityList where createTime equals to UPDATED_CREATE_TIME
        defaultDepartmentAuthorityShouldNotBeFound("createTime.equals=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createTime not equals to DEFAULT_CREATE_TIME
        defaultDepartmentAuthorityShouldNotBeFound("createTime.notEquals=" + DEFAULT_CREATE_TIME);

        // Get all the departmentAuthorityList where createTime not equals to UPDATED_CREATE_TIME
        defaultDepartmentAuthorityShouldBeFound("createTime.notEquals=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createTime in DEFAULT_CREATE_TIME or UPDATED_CREATE_TIME
        defaultDepartmentAuthorityShouldBeFound("createTime.in=" + DEFAULT_CREATE_TIME + "," + UPDATED_CREATE_TIME);

        // Get all the departmentAuthorityList where createTime equals to UPDATED_CREATE_TIME
        defaultDepartmentAuthorityShouldNotBeFound("createTime.in=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createTime is not null
        defaultDepartmentAuthorityShouldBeFound("createTime.specified=true");

        // Get all the departmentAuthorityList where createTime is null
        defaultDepartmentAuthorityShouldNotBeFound("createTime.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createTime is greater than or equal to DEFAULT_CREATE_TIME
        defaultDepartmentAuthorityShouldBeFound("createTime.greaterThanOrEqual=" + DEFAULT_CREATE_TIME);

        // Get all the departmentAuthorityList where createTime is greater than or equal to UPDATED_CREATE_TIME
        defaultDepartmentAuthorityShouldNotBeFound("createTime.greaterThanOrEqual=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createTime is less than or equal to DEFAULT_CREATE_TIME
        defaultDepartmentAuthorityShouldBeFound("createTime.lessThanOrEqual=" + DEFAULT_CREATE_TIME);

        // Get all the departmentAuthorityList where createTime is less than or equal to SMALLER_CREATE_TIME
        defaultDepartmentAuthorityShouldNotBeFound("createTime.lessThanOrEqual=" + SMALLER_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createTime is less than DEFAULT_CREATE_TIME
        defaultDepartmentAuthorityShouldNotBeFound("createTime.lessThan=" + DEFAULT_CREATE_TIME);

        // Get all the departmentAuthorityList where createTime is less than UPDATED_CREATE_TIME
        defaultDepartmentAuthorityShouldBeFound("createTime.lessThan=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByCreateTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where createTime is greater than DEFAULT_CREATE_TIME
        defaultDepartmentAuthorityShouldNotBeFound("createTime.greaterThan=" + DEFAULT_CREATE_TIME);

        // Get all the departmentAuthorityList where createTime is greater than SMALLER_CREATE_TIME
        defaultDepartmentAuthorityShouldBeFound("createTime.greaterThan=" + SMALLER_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByRemovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where removedAt equals to DEFAULT_REMOVED_AT
        defaultDepartmentAuthorityShouldBeFound("removedAt.equals=" + DEFAULT_REMOVED_AT);

        // Get all the departmentAuthorityList where removedAt equals to UPDATED_REMOVED_AT
        defaultDepartmentAuthorityShouldNotBeFound("removedAt.equals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByRemovedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where removedAt not equals to DEFAULT_REMOVED_AT
        defaultDepartmentAuthorityShouldNotBeFound("removedAt.notEquals=" + DEFAULT_REMOVED_AT);

        // Get all the departmentAuthorityList where removedAt not equals to UPDATED_REMOVED_AT
        defaultDepartmentAuthorityShouldBeFound("removedAt.notEquals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByRemovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where removedAt in DEFAULT_REMOVED_AT or UPDATED_REMOVED_AT
        defaultDepartmentAuthorityShouldBeFound("removedAt.in=" + DEFAULT_REMOVED_AT + "," + UPDATED_REMOVED_AT);

        // Get all the departmentAuthorityList where removedAt equals to UPDATED_REMOVED_AT
        defaultDepartmentAuthorityShouldNotBeFound("removedAt.in=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByRemovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where removedAt is not null
        defaultDepartmentAuthorityShouldBeFound("removedAt.specified=true");

        // Get all the departmentAuthorityList where removedAt is null
        defaultDepartmentAuthorityShouldNotBeFound("removedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByRemovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where removedAt is greater than or equal to DEFAULT_REMOVED_AT
        defaultDepartmentAuthorityShouldBeFound("removedAt.greaterThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the departmentAuthorityList where removedAt is greater than or equal to UPDATED_REMOVED_AT
        defaultDepartmentAuthorityShouldNotBeFound("removedAt.greaterThanOrEqual=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByRemovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where removedAt is less than or equal to DEFAULT_REMOVED_AT
        defaultDepartmentAuthorityShouldBeFound("removedAt.lessThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the departmentAuthorityList where removedAt is less than or equal to SMALLER_REMOVED_AT
        defaultDepartmentAuthorityShouldNotBeFound("removedAt.lessThanOrEqual=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByRemovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where removedAt is less than DEFAULT_REMOVED_AT
        defaultDepartmentAuthorityShouldNotBeFound("removedAt.lessThan=" + DEFAULT_REMOVED_AT);

        // Get all the departmentAuthorityList where removedAt is less than UPDATED_REMOVED_AT
        defaultDepartmentAuthorityShouldBeFound("removedAt.lessThan=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByRemovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        // Get all the departmentAuthorityList where removedAt is greater than DEFAULT_REMOVED_AT
        defaultDepartmentAuthorityShouldNotBeFound("removedAt.greaterThan=" + DEFAULT_REMOVED_AT);

        // Get all the departmentAuthorityList where removedAt is greater than SMALLER_REMOVED_AT
        defaultDepartmentAuthorityShouldBeFound("removedAt.greaterThan=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);
        User users = UserResourceIT.createEntity();
        departmentAuthority.addUsers(users);
        departmentAuthorityRepository.insert(departmentAuthority);
        Long usersId = users.getId();

        // Get all the departmentAuthorityList where users equals to usersId
        defaultDepartmentAuthorityShouldBeFound("usersId.equals=" + usersId);

        // Get all the departmentAuthorityList where users equals to (usersId + 1)
        defaultDepartmentAuthorityShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByApiPermissionsIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);
        ApiPermission apiPermissions = ApiPermissionResourceIT.createEntity();
        departmentAuthority.addApiPermissions(apiPermissions);
        departmentAuthorityRepository.insert(departmentAuthority);
        Long apiPermissionsId = apiPermissions.getId();

        // Get all the departmentAuthorityList where apiPermissions equals to apiPermissionsId
        defaultDepartmentAuthorityShouldBeFound("apiPermissionsId.equals=" + apiPermissionsId);

        // Get all the departmentAuthorityList where apiPermissions equals to (apiPermissionsId + 1)
        defaultDepartmentAuthorityShouldNotBeFound("apiPermissionsId.equals=" + (apiPermissionsId + 1));
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByViewPermissionsIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);
        ViewPermission viewPermissions = ViewPermissionResourceIT.createEntity();
        departmentAuthority.addViewPermissions(viewPermissions);
        departmentAuthorityRepository.insert(departmentAuthority);
        Long viewPermissionsId = viewPermissions.getId();

        // Get all the departmentAuthorityList where viewPermissions equals to viewPermissionsId
        defaultDepartmentAuthorityShouldBeFound("viewPermissionsId.equals=" + viewPermissionsId);

        // Get all the departmentAuthorityList where viewPermissions equals to (viewPermissionsId + 1)
        defaultDepartmentAuthorityShouldNotBeFound("viewPermissionsId.equals=" + (viewPermissionsId + 1));
    }

    @Test
    @Transactional
    void getAllDepartmentAuthoritiesByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);
        Department department = DepartmentResourceIT.createEntity();
        departmentAuthority.setDepartment(department);
        departmentAuthorityRepository.insert(departmentAuthority);
        Long departmentId = department.getId();

        // Get all the departmentAuthorityList where department equals to departmentId
        defaultDepartmentAuthorityShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the departmentAuthorityList where department equals to (departmentId + 1)
        defaultDepartmentAuthorityShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepartmentAuthorityShouldBeFound(String filter) throws Exception {
        restDepartmentAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departmentAuthority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createUserId").value(hasItem(DEFAULT_CREATE_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(sameInstant(DEFAULT_CREATE_TIME))))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));

        // Check, that the count call also returns 1
        restDepartmentAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepartmentAuthorityShouldNotBeFound(String filter) throws Exception {
        restDepartmentAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepartmentAuthorityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepartmentAuthority() throws Exception {
        // Get the departmentAuthority
        restDepartmentAuthorityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepartmentAuthority() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        int databaseSizeBeforeUpdate = departmentAuthorityRepository.findAll().size();

        // Update the departmentAuthority
        DepartmentAuthority updatedDepartmentAuthority = departmentAuthorityRepository.findById(departmentAuthority.getId()).get();
        // Disconnect from session so that the updates on updatedDepartmentAuthority are not directly saved in db
        updatedDepartmentAuthority
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .createUserId(UPDATED_CREATE_USER_ID)
            .createTime(UPDATED_CREATE_TIME)
            .removedAt(UPDATED_REMOVED_AT);
        DepartmentAuthorityDTO departmentAuthorityDTO = departmentAuthorityMapper.toDto(updatedDepartmentAuthority);

        restDepartmentAuthorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentAuthorityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentAuthorityDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepartmentAuthority in the database
        List<DepartmentAuthority> departmentAuthorityList = departmentAuthorityRepository.findAll();
        assertThat(departmentAuthorityList).hasSize(databaseSizeBeforeUpdate);
        DepartmentAuthority testDepartmentAuthority = departmentAuthorityList.get(departmentAuthorityList.size() - 1);
        assertThat(testDepartmentAuthority.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDepartmentAuthority.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDepartmentAuthority.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDepartmentAuthority.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testDepartmentAuthority.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testDepartmentAuthority.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void putNonExistingDepartmentAuthority() throws Exception {
        int databaseSizeBeforeUpdate = departmentAuthorityRepository.findAll().size();
        departmentAuthority.setId(count.incrementAndGet());

        // Create the DepartmentAuthority
        DepartmentAuthorityDTO departmentAuthorityDTO = departmentAuthorityMapper.toDto(departmentAuthority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentAuthorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentAuthorityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentAuthorityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentAuthority in the database
        List<DepartmentAuthority> departmentAuthorityList = departmentAuthorityRepository.findAll();
        assertThat(departmentAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartmentAuthority() throws Exception {
        int databaseSizeBeforeUpdate = departmentAuthorityRepository.findAll().size();
        departmentAuthority.setId(count.incrementAndGet());

        // Create the DepartmentAuthority
        DepartmentAuthorityDTO departmentAuthorityDTO = departmentAuthorityMapper.toDto(departmentAuthority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentAuthorityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentAuthorityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentAuthority in the database
        List<DepartmentAuthority> departmentAuthorityList = departmentAuthorityRepository.findAll();
        assertThat(departmentAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartmentAuthority() throws Exception {
        int databaseSizeBeforeUpdate = departmentAuthorityRepository.findAll().size();
        departmentAuthority.setId(count.incrementAndGet());

        // Create the DepartmentAuthority
        DepartmentAuthorityDTO departmentAuthorityDTO = departmentAuthorityMapper.toDto(departmentAuthority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentAuthorityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentAuthorityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepartmentAuthority in the database
        List<DepartmentAuthority> departmentAuthorityList = departmentAuthorityRepository.findAll();
        assertThat(departmentAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartmentAuthorityWithPatch() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        int databaseSizeBeforeUpdate = departmentAuthorityRepository.findAll().size();

        // Update the departmentAuthority using partial update
        DepartmentAuthority partialUpdatedDepartmentAuthority = new DepartmentAuthority();
        partialUpdatedDepartmentAuthority.setId(departmentAuthority.getId());

        partialUpdatedDepartmentAuthority.description(UPDATED_DESCRIPTION).removedAt(UPDATED_REMOVED_AT);

        restDepartmentAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartmentAuthority.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartmentAuthority))
            )
            .andExpect(status().isOk());

        // Validate the DepartmentAuthority in the database
        List<DepartmentAuthority> departmentAuthorityList = departmentAuthorityRepository.findAll();
        assertThat(departmentAuthorityList).hasSize(databaseSizeBeforeUpdate);
        DepartmentAuthority testDepartmentAuthority = departmentAuthorityList.get(departmentAuthorityList.size() - 1);
        assertThat(testDepartmentAuthority.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDepartmentAuthority.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDepartmentAuthority.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDepartmentAuthority.getCreateUserId()).isEqualTo(DEFAULT_CREATE_USER_ID);
        assertThat(testDepartmentAuthority.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testDepartmentAuthority.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void fullUpdateDepartmentAuthorityWithPatch() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        int databaseSizeBeforeUpdate = departmentAuthorityRepository.findAll().size();

        // Update the departmentAuthority using partial update
        DepartmentAuthority partialUpdatedDepartmentAuthority = new DepartmentAuthority();
        partialUpdatedDepartmentAuthority.setId(departmentAuthority.getId());

        partialUpdatedDepartmentAuthority
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .createUserId(UPDATED_CREATE_USER_ID)
            .createTime(UPDATED_CREATE_TIME)
            .removedAt(UPDATED_REMOVED_AT);

        restDepartmentAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartmentAuthority.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartmentAuthority))
            )
            .andExpect(status().isOk());

        // Validate the DepartmentAuthority in the database
        List<DepartmentAuthority> departmentAuthorityList = departmentAuthorityRepository.findAll();
        assertThat(departmentAuthorityList).hasSize(databaseSizeBeforeUpdate);
        DepartmentAuthority testDepartmentAuthority = departmentAuthorityList.get(departmentAuthorityList.size() - 1);
        assertThat(testDepartmentAuthority.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDepartmentAuthority.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDepartmentAuthority.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDepartmentAuthority.getCreateUserId()).isEqualTo(UPDATED_CREATE_USER_ID);
        assertThat(testDepartmentAuthority.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testDepartmentAuthority.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingDepartmentAuthority() throws Exception {
        int databaseSizeBeforeUpdate = departmentAuthorityRepository.findAll().size();
        departmentAuthority.setId(count.incrementAndGet());

        // Create the DepartmentAuthority
        DepartmentAuthorityDTO departmentAuthorityDTO = departmentAuthorityMapper.toDto(departmentAuthority);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departmentAuthorityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentAuthorityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentAuthority in the database
        List<DepartmentAuthority> departmentAuthorityList = departmentAuthorityRepository.findAll();
        assertThat(departmentAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartmentAuthority() throws Exception {
        int databaseSizeBeforeUpdate = departmentAuthorityRepository.findAll().size();
        departmentAuthority.setId(count.incrementAndGet());

        // Create the DepartmentAuthority
        DepartmentAuthorityDTO departmentAuthorityDTO = departmentAuthorityMapper.toDto(departmentAuthority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentAuthorityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepartmentAuthority in the database
        List<DepartmentAuthority> departmentAuthorityList = departmentAuthorityRepository.findAll();
        assertThat(departmentAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartmentAuthority() throws Exception {
        int databaseSizeBeforeUpdate = departmentAuthorityRepository.findAll().size();
        departmentAuthority.setId(count.incrementAndGet());

        // Create the DepartmentAuthority
        DepartmentAuthorityDTO departmentAuthorityDTO = departmentAuthorityMapper.toDto(departmentAuthority);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentAuthorityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentAuthorityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepartmentAuthority in the database
        List<DepartmentAuthority> departmentAuthorityList = departmentAuthorityRepository.findAll();
        assertThat(departmentAuthorityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartmentAuthority() throws Exception {
        // Initialize the database
        departmentAuthorityRepository.insert(departmentAuthority);

        int databaseSizeBeforeDelete = departmentAuthorityRepository.findAll().size();

        // Delete the departmentAuthority
        restDepartmentAuthorityMockMvc
            .perform(delete(ENTITY_API_URL_ID, departmentAuthority.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepartmentAuthority> departmentAuthorityList = departmentAuthorityRepository.findAll();
        assertThat(departmentAuthorityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
