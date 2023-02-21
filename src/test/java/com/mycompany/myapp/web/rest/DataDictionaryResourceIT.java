package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.DataDictionary;
import com.mycompany.myapp.domain.DataDictionary;
import com.mycompany.myapp.domain.enumeration.DictType;
import com.mycompany.myapp.repository.DataDictionaryRepository;
import com.mycompany.myapp.service.DataDictionaryQueryService;
import com.mycompany.myapp.service.criteria.DataDictionaryCriteria;
import com.mycompany.myapp.service.dto.DataDictionaryDTO;
import com.mycompany.myapp.service.mapper.DataDictionaryMapper;
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
 * Integration tests for the {@link DataDictionaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DataDictionaryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;
    private static final Integer SMALLER_SORT_ORDER = 1 - 1;

    private static final Boolean DEFAULT_DISABLED = false;
    private static final Boolean UPDATED_DISABLED = true;

    private static final String DEFAULT_FONT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_FONT_COLOR = "BBBBBBBBBB";

    private static final DictType DEFAULT_VALUE_TYPE = DictType.NUMBER;
    private static final DictType UPDATED_VALUE_TYPE = DictType.STRING;

    private static final String DEFAULT_BACKGROUND_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_BACKGROUND_COLOR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REMOVED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMOVED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REMOVED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/data-dictionaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DataDictionaryRepository dataDictionaryRepository;

    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;

    @Autowired
    private MockMvc restDataDictionaryMockMvc;

    private DataDictionary dataDictionary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataDictionary createEntity() {
        DataDictionary dataDictionary = new DataDictionary()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .title(DEFAULT_TITLE)
            .value(DEFAULT_VALUE)
            .description(DEFAULT_DESCRIPTION)
            .sortOrder(DEFAULT_SORT_ORDER)
            .disabled(DEFAULT_DISABLED)
            .fontColor(DEFAULT_FONT_COLOR)
            .valueType(DEFAULT_VALUE_TYPE)
            .backgroundColor(DEFAULT_BACKGROUND_COLOR)
            .removedAt(DEFAULT_REMOVED_AT);
        return dataDictionary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataDictionary createUpdatedEntity() {
        DataDictionary dataDictionary = new DataDictionary()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .sortOrder(UPDATED_SORT_ORDER)
            .disabled(UPDATED_DISABLED)
            .fontColor(UPDATED_FONT_COLOR)
            .valueType(UPDATED_VALUE_TYPE)
            .backgroundColor(UPDATED_BACKGROUND_COLOR)
            .removedAt(UPDATED_REMOVED_AT);
        return dataDictionary;
    }

    @BeforeEach
    public void initTest() {
        dataDictionary = createEntity();
    }

    @Test
    @Transactional
    void createDataDictionary() throws Exception {
        int databaseSizeBeforeCreate = dataDictionaryRepository.findAll().size();
        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);
        restDataDictionaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeCreate + 1);
        DataDictionary testDataDictionary = dataDictionaryList.get(dataDictionaryList.size() - 1);
        assertThat(testDataDictionary.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDataDictionary.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDataDictionary.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDataDictionary.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testDataDictionary.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDataDictionary.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testDataDictionary.getDisabled()).isEqualTo(DEFAULT_DISABLED);
        assertThat(testDataDictionary.getFontColor()).isEqualTo(DEFAULT_FONT_COLOR);
        assertThat(testDataDictionary.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testDataDictionary.getBackgroundColor()).isEqualTo(DEFAULT_BACKGROUND_COLOR);
        assertThat(testDataDictionary.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void createDataDictionaryWithExistingId() throws Exception {
        // Create the DataDictionary with an existing ID
        dataDictionary.setId(1L);
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        int databaseSizeBeforeCreate = dataDictionaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataDictionaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDataDictionaries() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList
        restDataDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataDictionary.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].fontColor").value(hasItem(DEFAULT_FONT_COLOR)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].backgroundColor").value(hasItem(DEFAULT_BACKGROUND_COLOR)))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));
    }

    @Test
    @Transactional
    void getDataDictionary() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get the dataDictionary
        restDataDictionaryMockMvc
            .perform(get(ENTITY_API_URL_ID, dataDictionary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataDictionary.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.disabled").value(DEFAULT_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.fontColor").value(DEFAULT_FONT_COLOR))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.backgroundColor").value(DEFAULT_BACKGROUND_COLOR))
            .andExpect(jsonPath("$.removedAt").value(DEFAULT_REMOVED_AT.toString()));
    }

    @Test
    @Transactional
    void getDataDictionariesByIdFiltering() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        Long id = dataDictionary.getId();

        defaultDataDictionaryShouldBeFound("id.equals=" + id);
        defaultDataDictionaryShouldNotBeFound("id.notEquals=" + id);

        defaultDataDictionaryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDataDictionaryShouldNotBeFound("id.greaterThan=" + id);

        defaultDataDictionaryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDataDictionaryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where name equals to DEFAULT_NAME
        defaultDataDictionaryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the dataDictionaryList where name equals to UPDATED_NAME
        defaultDataDictionaryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where name not equals to DEFAULT_NAME
        defaultDataDictionaryShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the dataDictionaryList where name not equals to UPDATED_NAME
        defaultDataDictionaryShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDataDictionaryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the dataDictionaryList where name equals to UPDATED_NAME
        defaultDataDictionaryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where name is not null
        defaultDataDictionaryShouldBeFound("name.specified=true");

        // Get all the dataDictionaryList where name is null
        defaultDataDictionaryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByNameContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where name contains DEFAULT_NAME
        defaultDataDictionaryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the dataDictionaryList where name contains UPDATED_NAME
        defaultDataDictionaryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where name does not contain DEFAULT_NAME
        defaultDataDictionaryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the dataDictionaryList where name does not contain UPDATED_NAME
        defaultDataDictionaryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where code equals to DEFAULT_CODE
        defaultDataDictionaryShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the dataDictionaryList where code equals to UPDATED_CODE
        defaultDataDictionaryShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where code not equals to DEFAULT_CODE
        defaultDataDictionaryShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the dataDictionaryList where code not equals to UPDATED_CODE
        defaultDataDictionaryShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where code in DEFAULT_CODE or UPDATED_CODE
        defaultDataDictionaryShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the dataDictionaryList where code equals to UPDATED_CODE
        defaultDataDictionaryShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where code is not null
        defaultDataDictionaryShouldBeFound("code.specified=true");

        // Get all the dataDictionaryList where code is null
        defaultDataDictionaryShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByCodeContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where code contains DEFAULT_CODE
        defaultDataDictionaryShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the dataDictionaryList where code contains UPDATED_CODE
        defaultDataDictionaryShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where code does not contain DEFAULT_CODE
        defaultDataDictionaryShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the dataDictionaryList where code does not contain UPDATED_CODE
        defaultDataDictionaryShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where title equals to DEFAULT_TITLE
        defaultDataDictionaryShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the dataDictionaryList where title equals to UPDATED_TITLE
        defaultDataDictionaryShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where title not equals to DEFAULT_TITLE
        defaultDataDictionaryShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the dataDictionaryList where title not equals to UPDATED_TITLE
        defaultDataDictionaryShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultDataDictionaryShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the dataDictionaryList where title equals to UPDATED_TITLE
        defaultDataDictionaryShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where title is not null
        defaultDataDictionaryShouldBeFound("title.specified=true");

        // Get all the dataDictionaryList where title is null
        defaultDataDictionaryShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByTitleContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where title contains DEFAULT_TITLE
        defaultDataDictionaryShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the dataDictionaryList where title contains UPDATED_TITLE
        defaultDataDictionaryShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where title does not contain DEFAULT_TITLE
        defaultDataDictionaryShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the dataDictionaryList where title does not contain UPDATED_TITLE
        defaultDataDictionaryShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where value equals to DEFAULT_VALUE
        defaultDataDictionaryShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the dataDictionaryList where value equals to UPDATED_VALUE
        defaultDataDictionaryShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where value not equals to DEFAULT_VALUE
        defaultDataDictionaryShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the dataDictionaryList where value not equals to UPDATED_VALUE
        defaultDataDictionaryShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultDataDictionaryShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the dataDictionaryList where value equals to UPDATED_VALUE
        defaultDataDictionaryShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where value is not null
        defaultDataDictionaryShouldBeFound("value.specified=true");

        // Get all the dataDictionaryList where value is null
        defaultDataDictionaryShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByValueContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where value contains DEFAULT_VALUE
        defaultDataDictionaryShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the dataDictionaryList where value contains UPDATED_VALUE
        defaultDataDictionaryShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByValueNotContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where value does not contain DEFAULT_VALUE
        defaultDataDictionaryShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the dataDictionaryList where value does not contain UPDATED_VALUE
        defaultDataDictionaryShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where description equals to DEFAULT_DESCRIPTION
        defaultDataDictionaryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the dataDictionaryList where description equals to UPDATED_DESCRIPTION
        defaultDataDictionaryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where description not equals to DEFAULT_DESCRIPTION
        defaultDataDictionaryShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the dataDictionaryList where description not equals to UPDATED_DESCRIPTION
        defaultDataDictionaryShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDataDictionaryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the dataDictionaryList where description equals to UPDATED_DESCRIPTION
        defaultDataDictionaryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where description is not null
        defaultDataDictionaryShouldBeFound("description.specified=true");

        // Get all the dataDictionaryList where description is null
        defaultDataDictionaryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where description contains DEFAULT_DESCRIPTION
        defaultDataDictionaryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the dataDictionaryList where description contains UPDATED_DESCRIPTION
        defaultDataDictionaryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where description does not contain DEFAULT_DESCRIPTION
        defaultDataDictionaryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the dataDictionaryList where description does not contain UPDATED_DESCRIPTION
        defaultDataDictionaryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDataDictionariesBySortOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where sortOrder equals to DEFAULT_SORT_ORDER
        defaultDataDictionaryShouldBeFound("sortOrder.equals=" + DEFAULT_SORT_ORDER);

        // Get all the dataDictionaryList where sortOrder equals to UPDATED_SORT_ORDER
        defaultDataDictionaryShouldNotBeFound("sortOrder.equals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllDataDictionariesBySortOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where sortOrder not equals to DEFAULT_SORT_ORDER
        defaultDataDictionaryShouldNotBeFound("sortOrder.notEquals=" + DEFAULT_SORT_ORDER);

        // Get all the dataDictionaryList where sortOrder not equals to UPDATED_SORT_ORDER
        defaultDataDictionaryShouldBeFound("sortOrder.notEquals=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllDataDictionariesBySortOrderIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where sortOrder in DEFAULT_SORT_ORDER or UPDATED_SORT_ORDER
        defaultDataDictionaryShouldBeFound("sortOrder.in=" + DEFAULT_SORT_ORDER + "," + UPDATED_SORT_ORDER);

        // Get all the dataDictionaryList where sortOrder equals to UPDATED_SORT_ORDER
        defaultDataDictionaryShouldNotBeFound("sortOrder.in=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllDataDictionariesBySortOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where sortOrder is not null
        defaultDataDictionaryShouldBeFound("sortOrder.specified=true");

        // Get all the dataDictionaryList where sortOrder is null
        defaultDataDictionaryShouldNotBeFound("sortOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesBySortOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where sortOrder is greater than or equal to DEFAULT_SORT_ORDER
        defaultDataDictionaryShouldBeFound("sortOrder.greaterThanOrEqual=" + DEFAULT_SORT_ORDER);

        // Get all the dataDictionaryList where sortOrder is greater than or equal to UPDATED_SORT_ORDER
        defaultDataDictionaryShouldNotBeFound("sortOrder.greaterThanOrEqual=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllDataDictionariesBySortOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where sortOrder is less than or equal to DEFAULT_SORT_ORDER
        defaultDataDictionaryShouldBeFound("sortOrder.lessThanOrEqual=" + DEFAULT_SORT_ORDER);

        // Get all the dataDictionaryList where sortOrder is less than or equal to SMALLER_SORT_ORDER
        defaultDataDictionaryShouldNotBeFound("sortOrder.lessThanOrEqual=" + SMALLER_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllDataDictionariesBySortOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where sortOrder is less than DEFAULT_SORT_ORDER
        defaultDataDictionaryShouldNotBeFound("sortOrder.lessThan=" + DEFAULT_SORT_ORDER);

        // Get all the dataDictionaryList where sortOrder is less than UPDATED_SORT_ORDER
        defaultDataDictionaryShouldBeFound("sortOrder.lessThan=" + UPDATED_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllDataDictionariesBySortOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where sortOrder is greater than DEFAULT_SORT_ORDER
        defaultDataDictionaryShouldNotBeFound("sortOrder.greaterThan=" + DEFAULT_SORT_ORDER);

        // Get all the dataDictionaryList where sortOrder is greater than SMALLER_SORT_ORDER
        defaultDataDictionaryShouldBeFound("sortOrder.greaterThan=" + SMALLER_SORT_ORDER);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where disabled equals to DEFAULT_DISABLED
        defaultDataDictionaryShouldBeFound("disabled.equals=" + DEFAULT_DISABLED);

        // Get all the dataDictionaryList where disabled equals to UPDATED_DISABLED
        defaultDataDictionaryShouldNotBeFound("disabled.equals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDisabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where disabled not equals to DEFAULT_DISABLED
        defaultDataDictionaryShouldNotBeFound("disabled.notEquals=" + DEFAULT_DISABLED);

        // Get all the dataDictionaryList where disabled not equals to UPDATED_DISABLED
        defaultDataDictionaryShouldBeFound("disabled.notEquals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where disabled in DEFAULT_DISABLED or UPDATED_DISABLED
        defaultDataDictionaryShouldBeFound("disabled.in=" + DEFAULT_DISABLED + "," + UPDATED_DISABLED);

        // Get all the dataDictionaryList where disabled equals to UPDATED_DISABLED
        defaultDataDictionaryShouldNotBeFound("disabled.in=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where disabled is not null
        defaultDataDictionaryShouldBeFound("disabled.specified=true");

        // Get all the dataDictionaryList where disabled is null
        defaultDataDictionaryShouldNotBeFound("disabled.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByFontColorIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where fontColor equals to DEFAULT_FONT_COLOR
        defaultDataDictionaryShouldBeFound("fontColor.equals=" + DEFAULT_FONT_COLOR);

        // Get all the dataDictionaryList where fontColor equals to UPDATED_FONT_COLOR
        defaultDataDictionaryShouldNotBeFound("fontColor.equals=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByFontColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where fontColor not equals to DEFAULT_FONT_COLOR
        defaultDataDictionaryShouldNotBeFound("fontColor.notEquals=" + DEFAULT_FONT_COLOR);

        // Get all the dataDictionaryList where fontColor not equals to UPDATED_FONT_COLOR
        defaultDataDictionaryShouldBeFound("fontColor.notEquals=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByFontColorIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where fontColor in DEFAULT_FONT_COLOR or UPDATED_FONT_COLOR
        defaultDataDictionaryShouldBeFound("fontColor.in=" + DEFAULT_FONT_COLOR + "," + UPDATED_FONT_COLOR);

        // Get all the dataDictionaryList where fontColor equals to UPDATED_FONT_COLOR
        defaultDataDictionaryShouldNotBeFound("fontColor.in=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByFontColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where fontColor is not null
        defaultDataDictionaryShouldBeFound("fontColor.specified=true");

        // Get all the dataDictionaryList where fontColor is null
        defaultDataDictionaryShouldNotBeFound("fontColor.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByFontColorContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where fontColor contains DEFAULT_FONT_COLOR
        defaultDataDictionaryShouldBeFound("fontColor.contains=" + DEFAULT_FONT_COLOR);

        // Get all the dataDictionaryList where fontColor contains UPDATED_FONT_COLOR
        defaultDataDictionaryShouldNotBeFound("fontColor.contains=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByFontColorNotContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where fontColor does not contain DEFAULT_FONT_COLOR
        defaultDataDictionaryShouldNotBeFound("fontColor.doesNotContain=" + DEFAULT_FONT_COLOR);

        // Get all the dataDictionaryList where fontColor does not contain UPDATED_FONT_COLOR
        defaultDataDictionaryShouldBeFound("fontColor.doesNotContain=" + UPDATED_FONT_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where valueType equals to DEFAULT_VALUE_TYPE
        defaultDataDictionaryShouldBeFound("valueType.equals=" + DEFAULT_VALUE_TYPE);

        // Get all the dataDictionaryList where valueType equals to UPDATED_VALUE_TYPE
        defaultDataDictionaryShouldNotBeFound("valueType.equals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByValueTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where valueType not equals to DEFAULT_VALUE_TYPE
        defaultDataDictionaryShouldNotBeFound("valueType.notEquals=" + DEFAULT_VALUE_TYPE);

        // Get all the dataDictionaryList where valueType not equals to UPDATED_VALUE_TYPE
        defaultDataDictionaryShouldBeFound("valueType.notEquals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where valueType in DEFAULT_VALUE_TYPE or UPDATED_VALUE_TYPE
        defaultDataDictionaryShouldBeFound("valueType.in=" + DEFAULT_VALUE_TYPE + "," + UPDATED_VALUE_TYPE);

        // Get all the dataDictionaryList where valueType equals to UPDATED_VALUE_TYPE
        defaultDataDictionaryShouldNotBeFound("valueType.in=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where valueType is not null
        defaultDataDictionaryShouldBeFound("valueType.specified=true");

        // Get all the dataDictionaryList where valueType is null
        defaultDataDictionaryShouldNotBeFound("valueType.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByBackgroundColorIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where backgroundColor equals to DEFAULT_BACKGROUND_COLOR
        defaultDataDictionaryShouldBeFound("backgroundColor.equals=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the dataDictionaryList where backgroundColor equals to UPDATED_BACKGROUND_COLOR
        defaultDataDictionaryShouldNotBeFound("backgroundColor.equals=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByBackgroundColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where backgroundColor not equals to DEFAULT_BACKGROUND_COLOR
        defaultDataDictionaryShouldNotBeFound("backgroundColor.notEquals=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the dataDictionaryList where backgroundColor not equals to UPDATED_BACKGROUND_COLOR
        defaultDataDictionaryShouldBeFound("backgroundColor.notEquals=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByBackgroundColorIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where backgroundColor in DEFAULT_BACKGROUND_COLOR or UPDATED_BACKGROUND_COLOR
        defaultDataDictionaryShouldBeFound("backgroundColor.in=" + DEFAULT_BACKGROUND_COLOR + "," + UPDATED_BACKGROUND_COLOR);

        // Get all the dataDictionaryList where backgroundColor equals to UPDATED_BACKGROUND_COLOR
        defaultDataDictionaryShouldNotBeFound("backgroundColor.in=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByBackgroundColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where backgroundColor is not null
        defaultDataDictionaryShouldBeFound("backgroundColor.specified=true");

        // Get all the dataDictionaryList where backgroundColor is null
        defaultDataDictionaryShouldNotBeFound("backgroundColor.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByBackgroundColorContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where backgroundColor contains DEFAULT_BACKGROUND_COLOR
        defaultDataDictionaryShouldBeFound("backgroundColor.contains=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the dataDictionaryList where backgroundColor contains UPDATED_BACKGROUND_COLOR
        defaultDataDictionaryShouldNotBeFound("backgroundColor.contains=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByBackgroundColorNotContainsSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where backgroundColor does not contain DEFAULT_BACKGROUND_COLOR
        defaultDataDictionaryShouldNotBeFound("backgroundColor.doesNotContain=" + DEFAULT_BACKGROUND_COLOR);

        // Get all the dataDictionaryList where backgroundColor does not contain UPDATED_BACKGROUND_COLOR
        defaultDataDictionaryShouldBeFound("backgroundColor.doesNotContain=" + UPDATED_BACKGROUND_COLOR);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByRemovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where removedAt equals to DEFAULT_REMOVED_AT
        defaultDataDictionaryShouldBeFound("removedAt.equals=" + DEFAULT_REMOVED_AT);

        // Get all the dataDictionaryList where removedAt equals to UPDATED_REMOVED_AT
        defaultDataDictionaryShouldNotBeFound("removedAt.equals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByRemovedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where removedAt not equals to DEFAULT_REMOVED_AT
        defaultDataDictionaryShouldNotBeFound("removedAt.notEquals=" + DEFAULT_REMOVED_AT);

        // Get all the dataDictionaryList where removedAt not equals to UPDATED_REMOVED_AT
        defaultDataDictionaryShouldBeFound("removedAt.notEquals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByRemovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where removedAt in DEFAULT_REMOVED_AT or UPDATED_REMOVED_AT
        defaultDataDictionaryShouldBeFound("removedAt.in=" + DEFAULT_REMOVED_AT + "," + UPDATED_REMOVED_AT);

        // Get all the dataDictionaryList where removedAt equals to UPDATED_REMOVED_AT
        defaultDataDictionaryShouldNotBeFound("removedAt.in=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByRemovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where removedAt is not null
        defaultDataDictionaryShouldBeFound("removedAt.specified=true");

        // Get all the dataDictionaryList where removedAt is null
        defaultDataDictionaryShouldNotBeFound("removedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllDataDictionariesByRemovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where removedAt is greater than or equal to DEFAULT_REMOVED_AT
        defaultDataDictionaryShouldBeFound("removedAt.greaterThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the dataDictionaryList where removedAt is greater than or equal to UPDATED_REMOVED_AT
        defaultDataDictionaryShouldNotBeFound("removedAt.greaterThanOrEqual=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByRemovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where removedAt is less than or equal to DEFAULT_REMOVED_AT
        defaultDataDictionaryShouldBeFound("removedAt.lessThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the dataDictionaryList where removedAt is less than or equal to SMALLER_REMOVED_AT
        defaultDataDictionaryShouldNotBeFound("removedAt.lessThanOrEqual=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByRemovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where removedAt is less than DEFAULT_REMOVED_AT
        defaultDataDictionaryShouldNotBeFound("removedAt.lessThan=" + DEFAULT_REMOVED_AT);

        // Get all the dataDictionaryList where removedAt is less than UPDATED_REMOVED_AT
        defaultDataDictionaryShouldBeFound("removedAt.lessThan=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByRemovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        // Get all the dataDictionaryList where removedAt is greater than DEFAULT_REMOVED_AT
        defaultDataDictionaryShouldNotBeFound("removedAt.greaterThan=" + DEFAULT_REMOVED_AT);

        // Get all the dataDictionaryList where removedAt is greater than SMALLER_REMOVED_AT
        defaultDataDictionaryShouldBeFound("removedAt.greaterThan=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllDataDictionariesByChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);
        DataDictionary children = DataDictionaryResourceIT.createEntity();
        dataDictionary.addChildren(children);
        dataDictionaryRepository.insert(dataDictionary);
        Long childrenId = children.getId();

        // Get all the dataDictionaryList where children equals to childrenId
        defaultDataDictionaryShouldBeFound("childrenId.equals=" + childrenId);

        // Get all the dataDictionaryList where children equals to (childrenId + 1)
        defaultDataDictionaryShouldNotBeFound("childrenId.equals=" + (childrenId + 1));
    }

    @Test
    @Transactional
    void getAllDataDictionariesByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);
        DataDictionary parent = DataDictionaryResourceIT.createEntity();
        dataDictionary.setParent(parent);
        dataDictionaryRepository.insert(dataDictionary);
        Long parentId = parent.getId();

        // Get all the dataDictionaryList where parent equals to parentId
        defaultDataDictionaryShouldBeFound("parentId.equals=" + parentId);

        // Get all the dataDictionaryList where parent equals to (parentId + 1)
        defaultDataDictionaryShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDataDictionaryShouldBeFound(String filter) throws Exception {
        restDataDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataDictionary.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].fontColor").value(hasItem(DEFAULT_FONT_COLOR)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].backgroundColor").value(hasItem(DEFAULT_BACKGROUND_COLOR)))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));

        // Check, that the count call also returns 1
        restDataDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDataDictionaryShouldNotBeFound(String filter) throws Exception {
        restDataDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDataDictionaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDataDictionary() throws Exception {
        // Get the dataDictionary
        restDataDictionaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDataDictionary() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();

        // Update the dataDictionary
        DataDictionary updatedDataDictionary = dataDictionaryRepository.findById(dataDictionary.getId()).get();
        // Disconnect from session so that the updates on updatedDataDictionary are not directly saved in db
        updatedDataDictionary
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .sortOrder(UPDATED_SORT_ORDER)
            .disabled(UPDATED_DISABLED)
            .fontColor(UPDATED_FONT_COLOR)
            .valueType(UPDATED_VALUE_TYPE)
            .backgroundColor(UPDATED_BACKGROUND_COLOR)
            .removedAt(UPDATED_REMOVED_AT);
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(updatedDataDictionary);

        restDataDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataDictionaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
        DataDictionary testDataDictionary = dataDictionaryList.get(dataDictionaryList.size() - 1);
        assertThat(testDataDictionary.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataDictionary.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDataDictionary.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDataDictionary.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testDataDictionary.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataDictionary.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testDataDictionary.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testDataDictionary.getFontColor()).isEqualTo(UPDATED_FONT_COLOR);
        assertThat(testDataDictionary.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testDataDictionary.getBackgroundColor()).isEqualTo(UPDATED_BACKGROUND_COLOR);
        assertThat(testDataDictionary.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void putNonExistingDataDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();
        dataDictionary.setId(count.incrementAndGet());

        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataDictionaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();
        dataDictionary.setId(count.incrementAndGet());

        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();
        dataDictionary.setId(count.incrementAndGet());

        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataDictionaryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataDictionaryWithPatch() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();

        // Update the dataDictionary using partial update
        DataDictionary partialUpdatedDataDictionary = new DataDictionary();
        partialUpdatedDataDictionary.setId(dataDictionary.getId());

        partialUpdatedDataDictionary
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .sortOrder(UPDATED_SORT_ORDER)
            .valueType(UPDATED_VALUE_TYPE)
            .removedAt(UPDATED_REMOVED_AT);

        restDataDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataDictionary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataDictionary))
            )
            .andExpect(status().isOk());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
        DataDictionary testDataDictionary = dataDictionaryList.get(dataDictionaryList.size() - 1);
        assertThat(testDataDictionary.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDataDictionary.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDataDictionary.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDataDictionary.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testDataDictionary.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataDictionary.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testDataDictionary.getDisabled()).isEqualTo(DEFAULT_DISABLED);
        assertThat(testDataDictionary.getFontColor()).isEqualTo(DEFAULT_FONT_COLOR);
        assertThat(testDataDictionary.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testDataDictionary.getBackgroundColor()).isEqualTo(DEFAULT_BACKGROUND_COLOR);
        assertThat(testDataDictionary.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void fullUpdateDataDictionaryWithPatch() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();

        // Update the dataDictionary using partial update
        DataDictionary partialUpdatedDataDictionary = new DataDictionary();
        partialUpdatedDataDictionary.setId(dataDictionary.getId());

        partialUpdatedDataDictionary
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .title(UPDATED_TITLE)
            .value(UPDATED_VALUE)
            .description(UPDATED_DESCRIPTION)
            .sortOrder(UPDATED_SORT_ORDER)
            .disabled(UPDATED_DISABLED)
            .fontColor(UPDATED_FONT_COLOR)
            .valueType(UPDATED_VALUE_TYPE)
            .backgroundColor(UPDATED_BACKGROUND_COLOR)
            .removedAt(UPDATED_REMOVED_AT);

        restDataDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataDictionary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDataDictionary))
            )
            .andExpect(status().isOk());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
        DataDictionary testDataDictionary = dataDictionaryList.get(dataDictionaryList.size() - 1);
        assertThat(testDataDictionary.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataDictionary.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDataDictionary.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDataDictionary.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testDataDictionary.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDataDictionary.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testDataDictionary.getDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testDataDictionary.getFontColor()).isEqualTo(UPDATED_FONT_COLOR);
        assertThat(testDataDictionary.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testDataDictionary.getBackgroundColor()).isEqualTo(UPDATED_BACKGROUND_COLOR);
        assertThat(testDataDictionary.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingDataDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();
        dataDictionary.setId(count.incrementAndGet());

        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataDictionaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();
        dataDictionary.setId(count.incrementAndGet());

        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataDictionary() throws Exception {
        int databaseSizeBeforeUpdate = dataDictionaryRepository.findAll().size();
        dataDictionary.setId(count.incrementAndGet());

        // Create the DataDictionary
        DataDictionaryDTO dataDictionaryDTO = dataDictionaryMapper.toDto(dataDictionary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataDictionaryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dataDictionaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataDictionary in the database
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataDictionary() throws Exception {
        // Initialize the database
        dataDictionaryRepository.insert(dataDictionary);

        int databaseSizeBeforeDelete = dataDictionaryRepository.findAll().size();

        // Delete the dataDictionary
        restDataDictionaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataDictionary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataDictionary> dataDictionaryList = dataDictionaryRepository.findAll();
        assertThat(dataDictionaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
