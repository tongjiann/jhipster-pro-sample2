package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Position;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.PositionRepository;
import com.mycompany.myapp.service.PositionQueryService;
import com.mycompany.myapp.service.criteria.PositionCriteria;
import com.mycompany.myapp.service.dto.PositionDTO;
import com.mycompany.myapp.service.mapper.PositionMapper;
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
 * Integration tests for the {@link PositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PositionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_NO = 1;
    private static final Integer UPDATED_SORT_NO = 2;
    private static final Integer SMALLER_SORT_NO = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REMOVED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REMOVED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REMOVED_AT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/positions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private MockMvc restPositionMockMvc;

    private Position position;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Position createEntity() {
        Position position = new Position()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .sortNo(DEFAULT_SORT_NO)
            .description(DEFAULT_DESCRIPTION)
            .removedAt(DEFAULT_REMOVED_AT);
        return position;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Position createUpdatedEntity() {
        Position position = new Position()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .sortNo(UPDATED_SORT_NO)
            .description(UPDATED_DESCRIPTION)
            .removedAt(UPDATED_REMOVED_AT);
        return position;
    }

    @BeforeEach
    public void initTest() {
        position = createEntity();
    }

    @Test
    @Transactional
    void createPosition() throws Exception {
        int databaseSizeBeforeCreate = positionRepository.findAll().size();
        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);
        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(positionDTO)))
            .andExpect(status().isCreated());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeCreate + 1);
        Position testPosition = positionList.get(positionList.size() - 1);
        assertThat(testPosition.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPosition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPosition.getSortNo()).isEqualTo(DEFAULT_SORT_NO);
        assertThat(testPosition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPosition.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void createPositionWithExistingId() throws Exception {
        // Create the Position with an existing ID
        position.setId(1L);
        PositionDTO positionDTO = positionMapper.toDto(position);

        int databaseSizeBeforeCreate = positionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionRepository.findAll().size();
        // set the field null
        position.setCode(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionRepository.findAll().size();
        // set the field null
        position.setName(null);

        // Create the Position, which fails.
        PositionDTO positionDTO = positionMapper.toDto(position);

        restPositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(positionDTO)))
            .andExpect(status().isBadRequest());

        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPositions() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortNo").value(hasItem(DEFAULT_SORT_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));
    }

    @Test
    @Transactional
    void getPosition() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get the position
        restPositionMockMvc
            .perform(get(ENTITY_API_URL_ID, position.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(position.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.sortNo").value(DEFAULT_SORT_NO))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.removedAt").value(DEFAULT_REMOVED_AT.toString()));
    }

    @Test
    @Transactional
    void getPositionsByIdFiltering() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        Long id = position.getId();

        defaultPositionShouldBeFound("id.equals=" + id);
        defaultPositionShouldNotBeFound("id.notEquals=" + id);

        defaultPositionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPositionShouldNotBeFound("id.greaterThan=" + id);

        defaultPositionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPositionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPositionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where code equals to DEFAULT_CODE
        defaultPositionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the positionList where code equals to UPDATED_CODE
        defaultPositionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPositionsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where code not equals to DEFAULT_CODE
        defaultPositionShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the positionList where code not equals to UPDATED_CODE
        defaultPositionShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPositionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPositionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the positionList where code equals to UPDATED_CODE
        defaultPositionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPositionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where code is not null
        defaultPositionShouldBeFound("code.specified=true");

        // Get all the positionList where code is null
        defaultPositionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where code contains DEFAULT_CODE
        defaultPositionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the positionList where code contains UPDATED_CODE
        defaultPositionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPositionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where code does not contain DEFAULT_CODE
        defaultPositionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the positionList where code does not contain UPDATED_CODE
        defaultPositionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPositionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where name equals to DEFAULT_NAME
        defaultPositionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the positionList where name equals to UPDATED_NAME
        defaultPositionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where name not equals to DEFAULT_NAME
        defaultPositionShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the positionList where name not equals to UPDATED_NAME
        defaultPositionShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPositionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the positionList where name equals to UPDATED_NAME
        defaultPositionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where name is not null
        defaultPositionShouldBeFound("name.specified=true");

        // Get all the positionList where name is null
        defaultPositionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByNameContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where name contains DEFAULT_NAME
        defaultPositionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the positionList where name contains UPDATED_NAME
        defaultPositionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where name does not contain DEFAULT_NAME
        defaultPositionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the positionList where name does not contain UPDATED_NAME
        defaultPositionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where sortNo equals to DEFAULT_SORT_NO
        defaultPositionShouldBeFound("sortNo.equals=" + DEFAULT_SORT_NO);

        // Get all the positionList where sortNo equals to UPDATED_SORT_NO
        defaultPositionShouldNotBeFound("sortNo.equals=" + UPDATED_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where sortNo not equals to DEFAULT_SORT_NO
        defaultPositionShouldNotBeFound("sortNo.notEquals=" + DEFAULT_SORT_NO);

        // Get all the positionList where sortNo not equals to UPDATED_SORT_NO
        defaultPositionShouldBeFound("sortNo.notEquals=" + UPDATED_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsInShouldWork() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where sortNo in DEFAULT_SORT_NO or UPDATED_SORT_NO
        defaultPositionShouldBeFound("sortNo.in=" + DEFAULT_SORT_NO + "," + UPDATED_SORT_NO);

        // Get all the positionList where sortNo equals to UPDATED_SORT_NO
        defaultPositionShouldNotBeFound("sortNo.in=" + UPDATED_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where sortNo is not null
        defaultPositionShouldBeFound("sortNo.specified=true");

        // Get all the positionList where sortNo is null
        defaultPositionShouldNotBeFound("sortNo.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where sortNo is greater than or equal to DEFAULT_SORT_NO
        defaultPositionShouldBeFound("sortNo.greaterThanOrEqual=" + DEFAULT_SORT_NO);

        // Get all the positionList where sortNo is greater than or equal to UPDATED_SORT_NO
        defaultPositionShouldNotBeFound("sortNo.greaterThanOrEqual=" + UPDATED_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where sortNo is less than or equal to DEFAULT_SORT_NO
        defaultPositionShouldBeFound("sortNo.lessThanOrEqual=" + DEFAULT_SORT_NO);

        // Get all the positionList where sortNo is less than or equal to SMALLER_SORT_NO
        defaultPositionShouldNotBeFound("sortNo.lessThanOrEqual=" + SMALLER_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsLessThanSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where sortNo is less than DEFAULT_SORT_NO
        defaultPositionShouldNotBeFound("sortNo.lessThan=" + DEFAULT_SORT_NO);

        // Get all the positionList where sortNo is less than UPDATED_SORT_NO
        defaultPositionShouldBeFound("sortNo.lessThan=" + UPDATED_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsBySortNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where sortNo is greater than DEFAULT_SORT_NO
        defaultPositionShouldNotBeFound("sortNo.greaterThan=" + DEFAULT_SORT_NO);

        // Get all the positionList where sortNo is greater than SMALLER_SORT_NO
        defaultPositionShouldBeFound("sortNo.greaterThan=" + SMALLER_SORT_NO);
    }

    @Test
    @Transactional
    void getAllPositionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where description equals to DEFAULT_DESCRIPTION
        defaultPositionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the positionList where description equals to UPDATED_DESCRIPTION
        defaultPositionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPositionsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where description not equals to DEFAULT_DESCRIPTION
        defaultPositionShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the positionList where description not equals to UPDATED_DESCRIPTION
        defaultPositionShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPositionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPositionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the positionList where description equals to UPDATED_DESCRIPTION
        defaultPositionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPositionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where description is not null
        defaultPositionShouldBeFound("description.specified=true");

        // Get all the positionList where description is null
        defaultPositionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where description contains DEFAULT_DESCRIPTION
        defaultPositionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the positionList where description contains UPDATED_DESCRIPTION
        defaultPositionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPositionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where description does not contain DEFAULT_DESCRIPTION
        defaultPositionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the positionList where description does not contain UPDATED_DESCRIPTION
        defaultPositionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPositionsByRemovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where removedAt equals to DEFAULT_REMOVED_AT
        defaultPositionShouldBeFound("removedAt.equals=" + DEFAULT_REMOVED_AT);

        // Get all the positionList where removedAt equals to UPDATED_REMOVED_AT
        defaultPositionShouldNotBeFound("removedAt.equals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllPositionsByRemovedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where removedAt not equals to DEFAULT_REMOVED_AT
        defaultPositionShouldNotBeFound("removedAt.notEquals=" + DEFAULT_REMOVED_AT);

        // Get all the positionList where removedAt not equals to UPDATED_REMOVED_AT
        defaultPositionShouldBeFound("removedAt.notEquals=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllPositionsByRemovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where removedAt in DEFAULT_REMOVED_AT or UPDATED_REMOVED_AT
        defaultPositionShouldBeFound("removedAt.in=" + DEFAULT_REMOVED_AT + "," + UPDATED_REMOVED_AT);

        // Get all the positionList where removedAt equals to UPDATED_REMOVED_AT
        defaultPositionShouldNotBeFound("removedAt.in=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllPositionsByRemovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where removedAt is not null
        defaultPositionShouldBeFound("removedAt.specified=true");

        // Get all the positionList where removedAt is null
        defaultPositionShouldNotBeFound("removedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllPositionsByRemovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where removedAt is greater than or equal to DEFAULT_REMOVED_AT
        defaultPositionShouldBeFound("removedAt.greaterThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the positionList where removedAt is greater than or equal to UPDATED_REMOVED_AT
        defaultPositionShouldNotBeFound("removedAt.greaterThanOrEqual=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllPositionsByRemovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where removedAt is less than or equal to DEFAULT_REMOVED_AT
        defaultPositionShouldBeFound("removedAt.lessThanOrEqual=" + DEFAULT_REMOVED_AT);

        // Get all the positionList where removedAt is less than or equal to SMALLER_REMOVED_AT
        defaultPositionShouldNotBeFound("removedAt.lessThanOrEqual=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllPositionsByRemovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where removedAt is less than DEFAULT_REMOVED_AT
        defaultPositionShouldNotBeFound("removedAt.lessThan=" + DEFAULT_REMOVED_AT);

        // Get all the positionList where removedAt is less than UPDATED_REMOVED_AT
        defaultPositionShouldBeFound("removedAt.lessThan=" + UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllPositionsByRemovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        // Get all the positionList where removedAt is greater than DEFAULT_REMOVED_AT
        defaultPositionShouldNotBeFound("removedAt.greaterThan=" + DEFAULT_REMOVED_AT);

        // Get all the positionList where removedAt is greater than SMALLER_REMOVED_AT
        defaultPositionShouldBeFound("removedAt.greaterThan=" + SMALLER_REMOVED_AT);
    }

    @Test
    @Transactional
    void getAllPositionsByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        positionRepository.insert(position);
        User users = UserResourceIT.createEntity();
        position.addUsers(users);
        positionRepository.insert(position);
        Long usersId = users.getId();

        // Get all the positionList where users equals to usersId
        defaultPositionShouldBeFound("usersId.equals=" + usersId);

        // Get all the positionList where users equals to (usersId + 1)
        defaultPositionShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPositionShouldBeFound(String filter) throws Exception {
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sortNo").value(hasItem(DEFAULT_SORT_NO)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].removedAt").value(hasItem(DEFAULT_REMOVED_AT.toString())));

        // Check, that the count call also returns 1
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPositionShouldNotBeFound(String filter) throws Exception {
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPosition() throws Exception {
        // Get the position
        restPositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPosition() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        int databaseSizeBeforeUpdate = positionRepository.findAll().size();

        // Update the position
        Position updatedPosition = positionRepository.findById(position.getId()).get();
        // Disconnect from session so that the updates on updatedPosition are not directly saved in db
        updatedPosition
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .sortNo(UPDATED_SORT_NO)
            .description(UPDATED_DESCRIPTION)
            .removedAt(UPDATED_REMOVED_AT);
        PositionDTO positionDTO = positionMapper.toDto(updatedPosition);

        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(positionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeUpdate);
        Position testPosition = positionList.get(positionList.size() - 1);
        assertThat(testPosition.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPosition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPosition.getSortNo()).isEqualTo(UPDATED_SORT_NO);
        assertThat(testPosition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPosition.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void putNonExistingPosition() throws Exception {
        int databaseSizeBeforeUpdate = positionRepository.findAll().size();
        position.setId(count.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPosition() throws Exception {
        int databaseSizeBeforeUpdate = positionRepository.findAll().size();
        position.setId(count.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPosition() throws Exception {
        int databaseSizeBeforeUpdate = positionRepository.findAll().size();
        position.setId(count.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(positionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePositionWithPatch() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        int databaseSizeBeforeUpdate = positionRepository.findAll().size();

        // Update the position using partial update
        Position partialUpdatedPosition = new Position();
        partialUpdatedPosition.setId(position.getId());

        partialUpdatedPosition.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPosition))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeUpdate);
        Position testPosition = positionList.get(positionList.size() - 1);
        assertThat(testPosition.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPosition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPosition.getSortNo()).isEqualTo(DEFAULT_SORT_NO);
        assertThat(testPosition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPosition.getRemovedAt()).isEqualTo(DEFAULT_REMOVED_AT);
    }

    @Test
    @Transactional
    void fullUpdatePositionWithPatch() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        int databaseSizeBeforeUpdate = positionRepository.findAll().size();

        // Update the position using partial update
        Position partialUpdatedPosition = new Position();
        partialUpdatedPosition.setId(position.getId());

        partialUpdatedPosition
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .sortNo(UPDATED_SORT_NO)
            .description(UPDATED_DESCRIPTION)
            .removedAt(UPDATED_REMOVED_AT);

        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPosition))
            )
            .andExpect(status().isOk());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeUpdate);
        Position testPosition = positionList.get(positionList.size() - 1);
        assertThat(testPosition.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPosition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPosition.getSortNo()).isEqualTo(UPDATED_SORT_NO);
        assertThat(testPosition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPosition.getRemovedAt()).isEqualTo(UPDATED_REMOVED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingPosition() throws Exception {
        int databaseSizeBeforeUpdate = positionRepository.findAll().size();
        position.setId(count.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, positionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPosition() throws Exception {
        int databaseSizeBeforeUpdate = positionRepository.findAll().size();
        position.setId(count.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(positionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPosition() throws Exception {
        int databaseSizeBeforeUpdate = positionRepository.findAll().size();
        position.setId(count.incrementAndGet());

        // Create the Position
        PositionDTO positionDTO = positionMapper.toDto(position);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(positionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Position in the database
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePosition() throws Exception {
        // Initialize the database
        positionRepository.insert(position);

        int databaseSizeBeforeDelete = positionRepository.findAll().size();

        // Delete the position
        restPositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, position.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Position> positionList = positionRepository.findAll();
        assertThat(positionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
