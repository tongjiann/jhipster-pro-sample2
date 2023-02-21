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
import com.mycompany.myapp.domain.UReportFile;
import com.mycompany.myapp.repository.UReportFileRepository;
import com.mycompany.myapp.service.UReportFileQueryService;
import com.mycompany.myapp.service.criteria.UReportFileCriteria;
import com.mycompany.myapp.service.dto.UReportFileDTO;
import com.mycompany.myapp.service.mapper.UReportFileMapper;
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
 * Integration tests for the {@link UReportFileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UReportFileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final LocalDate DEFAULT_REMOVED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMOVED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REMOVED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/u-report-files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UReportFileRepository uReportFileRepository;

    @Autowired
    private UReportFileMapper uReportFileMapper;

    @Autowired
    private MockMvc restUReportFileMockMvc;

    private UReportFile uReportFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UReportFile createEntity() {
        UReportFile uReportFile = new UReportFile()
            .name(DEFAULT_NAME)
            .content(DEFAULT_CONTENT)
            .createAt(DEFAULT_CREATE_AT)
            .updateAt(DEFAULT_UPDATE_AT)
            .removedAt(DEFAULT_REMOVED_AT);
        return uReportFile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UReportFile createUpdatedEntity() {
        UReportFile uReportFile = new UReportFile()
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .removedAt(UPDATED_REMOVED_AT);
        return uReportFile;
    }

    @BeforeEach
    public void initTest() {
        uReportFile = createEntity();
    }

    @Test
    @Transactional
    void createUReportFile() throws Exception {
        int databaseSizeBeforeCreate = uReportFileRepository.findAll().size();
        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);
        restUReportFileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uReportFileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UReportFile in the database
        List<UReportFile> uReportFileList = uReportFileRepository.findAll();
        assertThat(uReportFileList).hasSize(databaseSizeBeforeCreate + 1);
        UReportFile testUReportFile = uReportFileList.get(uReportFileList.size() - 1);
        assertThat(testUReportFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUReportFile.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testUReportFile.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testUReportFile.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
        assertThat(testUReportFile.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void createUReportFileWithExistingId() throws Exception {
        // Create the UReportFile with an existing ID
        uReportFile.setId(1L);
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        int databaseSizeBeforeCreate = uReportFileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUReportFileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uReportFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UReportFile in the database
        List<UReportFile> uReportFileList = uReportFileRepository.findAll();
        assertThat(uReportFileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUReportFiles() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList
        restUReportFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uReportFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(sameInstant(DEFAULT_CREATE_AT))))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(sameInstant(DEFAULT_UPDATE_AT))))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));
    }

    @Test
    @Transactional
    void getUReportFile() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get the uReportFile
        restUReportFileMockMvc
            .perform(get(ENTITY_API_URL_ID, uReportFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uReportFile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createAt").value(sameInstant(DEFAULT_CREATE_AT)))
            .andExpect(jsonPath("$.updateAt").value(sameInstant(DEFAULT_UPDATE_AT)))
            .andExpect(jsonPath("$.removedAt").value(DEFAULT_REMOVED_AT.toString()));
    }

    @Test
    @Transactional
    void getUReportFilesByIdFiltering() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        Long id = uReportFile.getId();

        defaultUReportFileShouldBeFound("id.equals=" + id);
        defaultUReportFileShouldNotBeFound("id.notEquals=" + id);

        defaultUReportFileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUReportFileShouldNotBeFound("id.greaterThan=" + id);

        defaultUReportFileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUReportFileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUReportFilesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where name equals to DEFAULT_NAME
        defaultUReportFileShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the uReportFileList where name equals to UPDATED_NAME
        defaultUReportFileShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUReportFilesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where name not equals to DEFAULT_NAME
        defaultUReportFileShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the uReportFileList where name not equals to UPDATED_NAME
        defaultUReportFileShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUReportFilesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUReportFileShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the uReportFileList where name equals to UPDATED_NAME
        defaultUReportFileShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUReportFilesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where name is not null
        defaultUReportFileShouldBeFound("name.specified=true");

        // Get all the uReportFileList where name is null
        defaultUReportFileShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllUReportFilesByNameContainsSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where name contains DEFAULT_NAME
        defaultUReportFileShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the uReportFileList where name contains UPDATED_NAME
        defaultUReportFileShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUReportFilesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where name does not contain DEFAULT_NAME
        defaultUReportFileShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the uReportFileList where name does not contain UPDATED_NAME
        defaultUReportFileShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where createAt equals to DEFAULT_CREATE_AT
        defaultUReportFileShouldBeFound("createAt.equals=" + DEFAULT_CREATE_AT);

        // Get all the uReportFileList where createAt equals to UPDATED_CREATE_AT
        defaultUReportFileShouldNotBeFound("createAt.equals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where createAt not equals to DEFAULT_CREATE_AT
        defaultUReportFileShouldNotBeFound("createAt.notEquals=" + DEFAULT_CREATE_AT);

        // Get all the uReportFileList where createAt not equals to UPDATED_CREATE_AT
        defaultUReportFileShouldBeFound("createAt.notEquals=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsInShouldWork() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where createAt in DEFAULT_CREATE_AT or UPDATED_CREATE_AT
        defaultUReportFileShouldBeFound("createAt.in=" + DEFAULT_CREATE_AT + "," + UPDATED_CREATE_AT);

        // Get all the uReportFileList where createAt equals to UPDATED_CREATE_AT
        defaultUReportFileShouldNotBeFound("createAt.in=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where createAt is not null
        defaultUReportFileShouldBeFound("createAt.specified=true");

        // Get all the uReportFileList where createAt is null
        defaultUReportFileShouldNotBeFound("createAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where createAt is greater than or equal to DEFAULT_CREATE_AT
        defaultUReportFileShouldBeFound("createAt.greaterThanOrEqual=" + DEFAULT_CREATE_AT);

        // Get all the uReportFileList where createAt is greater than or equal to UPDATED_CREATE_AT
        defaultUReportFileShouldNotBeFound("createAt.greaterThanOrEqual=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where createAt is less than or equal to DEFAULT_CREATE_AT
        defaultUReportFileShouldBeFound("createAt.lessThanOrEqual=" + DEFAULT_CREATE_AT);

        // Get all the uReportFileList where createAt is less than or equal to SMALLER_CREATE_AT
        defaultUReportFileShouldNotBeFound("createAt.lessThanOrEqual=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where createAt is less than DEFAULT_CREATE_AT
        defaultUReportFileShouldNotBeFound("createAt.lessThan=" + DEFAULT_CREATE_AT);

        // Get all the uReportFileList where createAt is less than UPDATED_CREATE_AT
        defaultUReportFileShouldBeFound("createAt.lessThan=" + UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByCreateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where createAt is greater than DEFAULT_CREATE_AT
        defaultUReportFileShouldNotBeFound("createAt.greaterThan=" + DEFAULT_CREATE_AT);

        // Get all the uReportFileList where createAt is greater than SMALLER_CREATE_AT
        defaultUReportFileShouldBeFound("createAt.greaterThan=" + SMALLER_CREATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where updateAt equals to DEFAULT_UPDATE_AT
        defaultUReportFileShouldBeFound("updateAt.equals=" + DEFAULT_UPDATE_AT);

        // Get all the uReportFileList where updateAt equals to UPDATED_UPDATE_AT
        defaultUReportFileShouldNotBeFound("updateAt.equals=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where updateAt not equals to DEFAULT_UPDATE_AT
        defaultUReportFileShouldNotBeFound("updateAt.notEquals=" + DEFAULT_UPDATE_AT);

        // Get all the uReportFileList where updateAt not equals to UPDATED_UPDATE_AT
        defaultUReportFileShouldBeFound("updateAt.notEquals=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsInShouldWork() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where updateAt in DEFAULT_UPDATE_AT or UPDATED_UPDATE_AT
        defaultUReportFileShouldBeFound("updateAt.in=" + DEFAULT_UPDATE_AT + "," + UPDATED_UPDATE_AT);

        // Get all the uReportFileList where updateAt equals to UPDATED_UPDATE_AT
        defaultUReportFileShouldNotBeFound("updateAt.in=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where updateAt is not null
        defaultUReportFileShouldBeFound("updateAt.specified=true");

        // Get all the uReportFileList where updateAt is null
        defaultUReportFileShouldNotBeFound("updateAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where updateAt is greater than or equal to DEFAULT_UPDATE_AT
        defaultUReportFileShouldBeFound("updateAt.greaterThanOrEqual=" + DEFAULT_UPDATE_AT);

        // Get all the uReportFileList where updateAt is greater than or equal to UPDATED_UPDATE_AT
        defaultUReportFileShouldNotBeFound("updateAt.greaterThanOrEqual=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where updateAt is less than or equal to DEFAULT_UPDATE_AT
        defaultUReportFileShouldBeFound("updateAt.lessThanOrEqual=" + DEFAULT_UPDATE_AT);

        // Get all the uReportFileList where updateAt is less than or equal to SMALLER_UPDATE_AT
        defaultUReportFileShouldNotBeFound("updateAt.lessThanOrEqual=" + SMALLER_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsLessThanSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where updateAt is less than DEFAULT_UPDATE_AT
        defaultUReportFileShouldNotBeFound("updateAt.lessThan=" + DEFAULT_UPDATE_AT);

        // Get all the uReportFileList where updateAt is less than UPDATED_UPDATE_AT
        defaultUReportFileShouldBeFound("updateAt.lessThan=" + UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByUpdateAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where updateAt is greater than DEFAULT_UPDATE_AT
        defaultUReportFileShouldNotBeFound("updateAt.greaterThan=" + DEFAULT_UPDATE_AT);

        // Get all the uReportFileList where updateAt is greater than SMALLER_UPDATE_AT
        defaultUReportFileShouldBeFound("updateAt.greaterThan=" + SMALLER_UPDATE_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByRemovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where removedAt equals to DEFAULT_REMOVED_AT
        defaultUReportFileShouldBeFound("removedAt.equals=" + DEFAULT_REMOVED_AT);

        // Get all the uReportFileList where removedAt equals to UPDATED_REMOVED_AT
        defaultUReportFileShouldNotBeFound("removedAt.equals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByRemovedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where removedAt not equals to DEFAULT_REMOVED_AT
        defaultUReportFileShouldNotBeFound("removedAt.notEquals=" + DEFAULT_REMOVED_AT);

        // Get all the uReportFileList where removedAt not equals to UPDATED_REMOVED_AT
        defaultUReportFileShouldBeFound("removedAt.notEquals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByRemovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where removedAt in DEFAULT_REMOVED_AT or UPDATED_REMOVED_AT
        defaultUReportFileShouldBeFound("removedAt.in=" + DEFAULT_REMOVED_AT + "," + UPDATED_REMOVED_AT);

        // Get all the uReportFileList where removedAt equals to UPDATED_REMOVED_AT
        defaultUReportFileShouldNotBeFound("removedAt.in=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByRemovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where removedAt is not null
        defaultUReportFileShouldBeFound("removedAt.specified=true");

        // Get all the uReportFileList where removedAt is null
        defaultUReportFileShouldNotBeFound("removedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUReportFilesByRemovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where removedAt is greater than or equal to DEFAULT_REMOVED_AT
        defaultUReportFileShouldBeFound("removedAt.greaterThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the uReportFileList where removedAt is greater than or equal to UPDATED_REMOVED_AT
        defaultUReportFileShouldNotBeFound("removedAt.greaterThanOrEqual=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByRemovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where removedAt is less than or equal to DEFAULT_REMOVED_AT
        defaultUReportFileShouldBeFound("removedAt.lessThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the uReportFileList where removedAt is less than or equal to SMALLER_REMOVED_AT
        defaultUReportFileShouldNotBeFound("removedAt.lessThanOrEqual=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByRemovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where removedAt is less than DEFAULT_REMOVED_AT
        defaultUReportFileShouldNotBeFound("removedAt.lessThan=" + DEFAULT_REMOVED_AT);

        // Get all the uReportFileList where removedAt is less than UPDATED_REMOVED_AT
        defaultUReportFileShouldBeFound("removedAt.lessThan=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllUReportFilesByRemovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        // Get all the uReportFileList where removedAt is greater than DEFAULT_REMOVED_AT
        defaultUReportFileShouldNotBeFound("removedAt.greaterThan=" + DEFAULT_REMOVED_AT);

        // Get all the uReportFileList where removedAt is greater than SMALLER_REMOVED_AT
        defaultUReportFileShouldBeFound("removedAt.greaterThan=" + SMALLER_REMOVED_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUReportFileShouldBeFound(String filter) throws Exception {
        restUReportFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uReportFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(sameInstant(DEFAULT_CREATE_AT))))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(sameInstant(DEFAULT_UPDATE_AT))))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));

        // Check, that the count call also returns 1
        restUReportFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUReportFileShouldNotBeFound(String filter) throws Exception {
        restUReportFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUReportFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUReportFile() throws Exception {
        // Get the uReportFile
        restUReportFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUReportFile() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        int databaseSizeBeforeUpdate = uReportFileRepository.findAll().size();

        // Update the uReportFile
        UReportFile updatedUReportFile = uReportFileRepository.findById(uReportFile.getId()).get();
        // Disconnect from session so that the updates on updatedUReportFile are not directly saved in db
        updatedUReportFile
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .removedAt(UPDATED_REMOVED_AT);
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(updatedUReportFile);

        restUReportFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uReportFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uReportFileDTO))
            )
            .andExpect(status().isOk());

        // Validate the UReportFile in the database
        List<UReportFile> uReportFileList = uReportFileRepository.findAll();
        assertThat(uReportFileList).hasSize(databaseSizeBeforeUpdate);
        UReportFile testUReportFile = uReportFileList.get(uReportFileList.size() - 1);
        assertThat(testUReportFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUReportFile.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testUReportFile.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testUReportFile.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
        assertThat(testUReportFile.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void putNonExistingUReportFile() throws Exception {
        int databaseSizeBeforeUpdate = uReportFileRepository.findAll().size();
        uReportFile.setId(count.incrementAndGet());

        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUReportFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uReportFileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uReportFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UReportFile in the database
        List<UReportFile> uReportFileList = uReportFileRepository.findAll();
        assertThat(uReportFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUReportFile() throws Exception {
        int databaseSizeBeforeUpdate = uReportFileRepository.findAll().size();
        uReportFile.setId(count.incrementAndGet());

        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUReportFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uReportFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UReportFile in the database
        List<UReportFile> uReportFileList = uReportFileRepository.findAll();
        assertThat(uReportFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUReportFile() throws Exception {
        int databaseSizeBeforeUpdate = uReportFileRepository.findAll().size();
        uReportFile.setId(count.incrementAndGet());

        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUReportFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uReportFileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UReportFile in the database
        List<UReportFile> uReportFileList = uReportFileRepository.findAll();
        assertThat(uReportFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUReportFileWithPatch() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        int databaseSizeBeforeUpdate = uReportFileRepository.findAll().size();

        // Update the uReportFile using partial update
        UReportFile partialUpdatedUReportFile = new UReportFile();
        partialUpdatedUReportFile.setId(uReportFile.getId());

        partialUpdatedUReportFile.name(UPDATED_NAME).createAt(UPDATED_CREATE_AT);

        restUReportFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUReportFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUReportFile))
            )
            .andExpect(status().isOk());

        // Validate the UReportFile in the database
        List<UReportFile> uReportFileList = uReportFileRepository.findAll();
        assertThat(uReportFileList).hasSize(databaseSizeBeforeUpdate);
        UReportFile testUReportFile = uReportFileList.get(uReportFileList.size() - 1);
        assertThat(testUReportFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUReportFile.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testUReportFile.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testUReportFile.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
        assertThat(testUReportFile.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void fullUpdateUReportFileWithPatch() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        int databaseSizeBeforeUpdate = uReportFileRepository.findAll().size();

        // Update the uReportFile using partial update
        UReportFile partialUpdatedUReportFile = new UReportFile();
        partialUpdatedUReportFile.setId(uReportFile.getId());

        partialUpdatedUReportFile
            .name(UPDATED_NAME)
            .content(UPDATED_CONTENT)
            .createAt(UPDATED_CREATE_AT)
            .updateAt(UPDATED_UPDATE_AT)
            .removedAt(UPDATED_REMOVED_AT);

        restUReportFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUReportFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUReportFile))
            )
            .andExpect(status().isOk());

        // Validate the UReportFile in the database
        List<UReportFile> uReportFileList = uReportFileRepository.findAll();
        assertThat(uReportFileList).hasSize(databaseSizeBeforeUpdate);
        UReportFile testUReportFile = uReportFileList.get(uReportFileList.size() - 1);
        assertThat(testUReportFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUReportFile.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testUReportFile.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testUReportFile.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
        assertThat(testUReportFile.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingUReportFile() throws Exception {
        int databaseSizeBeforeUpdate = uReportFileRepository.findAll().size();
        uReportFile.setId(count.incrementAndGet());

        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUReportFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uReportFileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uReportFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UReportFile in the database
        List<UReportFile> uReportFileList = uReportFileRepository.findAll();
        assertThat(uReportFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUReportFile() throws Exception {
        int databaseSizeBeforeUpdate = uReportFileRepository.findAll().size();
        uReportFile.setId(count.incrementAndGet());

        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUReportFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uReportFileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UReportFile in the database
        List<UReportFile> uReportFileList = uReportFileRepository.findAll();
        assertThat(uReportFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUReportFile() throws Exception {
        int databaseSizeBeforeUpdate = uReportFileRepository.findAll().size();
        uReportFile.setId(count.incrementAndGet());

        // Create the UReportFile
        UReportFileDTO uReportFileDTO = uReportFileMapper.toDto(uReportFile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUReportFileMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(uReportFileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UReportFile in the database
        List<UReportFile> uReportFileList = uReportFileRepository.findAll();
        assertThat(uReportFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUReportFile() throws Exception {
        // Initialize the database
        uReportFileRepository.insert(uReportFile);

        int databaseSizeBeforeDelete = uReportFileRepository.findAll().size();

        // Delete the uReportFile
        restUReportFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, uReportFile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UReportFile> uReportFileList = uReportFileRepository.findAll();
        assertThat(uReportFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
