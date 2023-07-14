package com.yukikyu.kook.boot.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.yukikyu.kook.boot.app.IntegrationTest;
import com.yukikyu.kook.boot.app.domain.HelpStatistics;
import com.yukikyu.kook.boot.app.repository.EntityManager;
import com.yukikyu.kook.boot.app.repository.HelpStatisticsRepository;
import com.yukikyu.kook.boot.app.service.dto.HelpStatisticsDTO;
import com.yukikyu.kook.boot.app.service.mapper.HelpStatisticsMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link HelpStatisticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HelpStatisticsResourceIT {

    private static final String DEFAULT_GUILD_ID = "AAAAAAAAAA";
    private static final String UPDATED_GUILD_ID = "BBBBBBBBBB";

    private static final String DEFAULT_HELP_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_HELP_USER_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;
    private static final Integer SMALLER_DURATION = 1 - 1;

    private static final Instant DEFAULT_MONTH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MONTH = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/help-statistics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HelpStatisticsRepository helpStatisticsRepository;

    @Autowired
    private HelpStatisticsMapper helpStatisticsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HelpStatistics helpStatistics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpStatistics createEntity(EntityManager em) {
        HelpStatistics helpStatistics = new HelpStatistics()
            .guildId(DEFAULT_GUILD_ID)
            .helpUserId(DEFAULT_HELP_USER_ID)
            .duration(DEFAULT_DURATION)
            .month(DEFAULT_MONTH)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return helpStatistics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpStatistics createUpdatedEntity(EntityManager em) {
        HelpStatistics helpStatistics = new HelpStatistics()
            .guildId(UPDATED_GUILD_ID)
            .helpUserId(UPDATED_HELP_USER_ID)
            .duration(UPDATED_DURATION)
            .month(UPDATED_MONTH)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return helpStatistics;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(HelpStatistics.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        helpStatistics = createEntity(em);
    }

    @Test
    void createHelpStatistics() throws Exception {
        int databaseSizeBeforeCreate = helpStatisticsRepository.findAll().collectList().block().size();
        // Create the HelpStatistics
        HelpStatisticsDTO helpStatisticsDTO = helpStatisticsMapper.toDto(helpStatistics);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpStatisticsDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the HelpStatistics in the database
        List<HelpStatistics> helpStatisticsList = helpStatisticsRepository.findAll().collectList().block();
        assertThat(helpStatisticsList).hasSize(databaseSizeBeforeCreate + 1);
        HelpStatistics testHelpStatistics = helpStatisticsList.get(helpStatisticsList.size() - 1);
        assertThat(testHelpStatistics.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testHelpStatistics.getHelpUserId()).isEqualTo(DEFAULT_HELP_USER_ID);
        assertThat(testHelpStatistics.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testHelpStatistics.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testHelpStatistics.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testHelpStatistics.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testHelpStatistics.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testHelpStatistics.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    void createHelpStatisticsWithExistingId() throws Exception {
        // Create the HelpStatistics with an existing ID
        helpStatistics.setId(1L);
        HelpStatisticsDTO helpStatisticsDTO = helpStatisticsMapper.toDto(helpStatistics);

        int databaseSizeBeforeCreate = helpStatisticsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpStatisticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpStatistics in the database
        List<HelpStatistics> helpStatisticsList = helpStatisticsRepository.findAll().collectList().block();
        assertThat(helpStatisticsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllHelpStatistics() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(helpStatistics.getId().intValue()))
            .jsonPath("$.[*].guildId")
            .value(hasItem(DEFAULT_GUILD_ID))
            .jsonPath("$.[*].helpUserId")
            .value(hasItem(DEFAULT_HELP_USER_ID))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION))
            .jsonPath("$.[*].month")
            .value(hasItem(DEFAULT_MONTH.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    void getHelpStatistics() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get the helpStatistics
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, helpStatistics.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(helpStatistics.getId().intValue()))
            .jsonPath("$.guildId")
            .value(is(DEFAULT_GUILD_ID))
            .jsonPath("$.helpUserId")
            .value(is(DEFAULT_HELP_USER_ID))
            .jsonPath("$.duration")
            .value(is(DEFAULT_DURATION))
            .jsonPath("$.month")
            .value(is(DEFAULT_MONTH.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    void getHelpStatisticsByIdFiltering() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        Long id = helpStatistics.getId();

        defaultHelpStatisticsShouldBeFound("id.equals=" + id);
        defaultHelpStatisticsShouldNotBeFound("id.notEquals=" + id);

        defaultHelpStatisticsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHelpStatisticsShouldNotBeFound("id.greaterThan=" + id);

        defaultHelpStatisticsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHelpStatisticsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllHelpStatisticsByGuildIdIsEqualToSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where guildId equals to DEFAULT_GUILD_ID
        defaultHelpStatisticsShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the helpStatisticsList where guildId equals to UPDATED_GUILD_ID
        defaultHelpStatisticsShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    void getAllHelpStatisticsByGuildIdIsInShouldWork() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultHelpStatisticsShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the helpStatisticsList where guildId equals to UPDATED_GUILD_ID
        defaultHelpStatisticsShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    void getAllHelpStatisticsByGuildIdIsNullOrNotNull() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where guildId is not null
        defaultHelpStatisticsShouldBeFound("guildId.specified=true");

        // Get all the helpStatisticsList where guildId is null
        defaultHelpStatisticsShouldNotBeFound("guildId.specified=false");
    }

    @Test
    void getAllHelpStatisticsByGuildIdContainsSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where guildId contains DEFAULT_GUILD_ID
        defaultHelpStatisticsShouldBeFound("guildId.contains=" + DEFAULT_GUILD_ID);

        // Get all the helpStatisticsList where guildId contains UPDATED_GUILD_ID
        defaultHelpStatisticsShouldNotBeFound("guildId.contains=" + UPDATED_GUILD_ID);
    }

    @Test
    void getAllHelpStatisticsByGuildIdNotContainsSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where guildId does not contain DEFAULT_GUILD_ID
        defaultHelpStatisticsShouldNotBeFound("guildId.doesNotContain=" + DEFAULT_GUILD_ID);

        // Get all the helpStatisticsList where guildId does not contain UPDATED_GUILD_ID
        defaultHelpStatisticsShouldBeFound("guildId.doesNotContain=" + UPDATED_GUILD_ID);
    }

    @Test
    void getAllHelpStatisticsByHelpUserIdIsEqualToSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where helpUserId equals to DEFAULT_HELP_USER_ID
        defaultHelpStatisticsShouldBeFound("helpUserId.equals=" + DEFAULT_HELP_USER_ID);

        // Get all the helpStatisticsList where helpUserId equals to UPDATED_HELP_USER_ID
        defaultHelpStatisticsShouldNotBeFound("helpUserId.equals=" + UPDATED_HELP_USER_ID);
    }

    @Test
    void getAllHelpStatisticsByHelpUserIdIsInShouldWork() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where helpUserId in DEFAULT_HELP_USER_ID or UPDATED_HELP_USER_ID
        defaultHelpStatisticsShouldBeFound("helpUserId.in=" + DEFAULT_HELP_USER_ID + "," + UPDATED_HELP_USER_ID);

        // Get all the helpStatisticsList where helpUserId equals to UPDATED_HELP_USER_ID
        defaultHelpStatisticsShouldNotBeFound("helpUserId.in=" + UPDATED_HELP_USER_ID);
    }

    @Test
    void getAllHelpStatisticsByHelpUserIdIsNullOrNotNull() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where helpUserId is not null
        defaultHelpStatisticsShouldBeFound("helpUserId.specified=true");

        // Get all the helpStatisticsList where helpUserId is null
        defaultHelpStatisticsShouldNotBeFound("helpUserId.specified=false");
    }

    @Test
    void getAllHelpStatisticsByHelpUserIdContainsSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where helpUserId contains DEFAULT_HELP_USER_ID
        defaultHelpStatisticsShouldBeFound("helpUserId.contains=" + DEFAULT_HELP_USER_ID);

        // Get all the helpStatisticsList where helpUserId contains UPDATED_HELP_USER_ID
        defaultHelpStatisticsShouldNotBeFound("helpUserId.contains=" + UPDATED_HELP_USER_ID);
    }

    @Test
    void getAllHelpStatisticsByHelpUserIdNotContainsSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where helpUserId does not contain DEFAULT_HELP_USER_ID
        defaultHelpStatisticsShouldNotBeFound("helpUserId.doesNotContain=" + DEFAULT_HELP_USER_ID);

        // Get all the helpStatisticsList where helpUserId does not contain UPDATED_HELP_USER_ID
        defaultHelpStatisticsShouldBeFound("helpUserId.doesNotContain=" + UPDATED_HELP_USER_ID);
    }

    @Test
    void getAllHelpStatisticsByDurationIsEqualToSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where duration equals to DEFAULT_DURATION
        defaultHelpStatisticsShouldBeFound("duration.equals=" + DEFAULT_DURATION);

        // Get all the helpStatisticsList where duration equals to UPDATED_DURATION
        defaultHelpStatisticsShouldNotBeFound("duration.equals=" + UPDATED_DURATION);
    }

    @Test
    void getAllHelpStatisticsByDurationIsInShouldWork() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where duration in DEFAULT_DURATION or UPDATED_DURATION
        defaultHelpStatisticsShouldBeFound("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION);

        // Get all the helpStatisticsList where duration equals to UPDATED_DURATION
        defaultHelpStatisticsShouldNotBeFound("duration.in=" + UPDATED_DURATION);
    }

    @Test
    void getAllHelpStatisticsByDurationIsNullOrNotNull() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where duration is not null
        defaultHelpStatisticsShouldBeFound("duration.specified=true");

        // Get all the helpStatisticsList where duration is null
        defaultHelpStatisticsShouldNotBeFound("duration.specified=false");
    }

    @Test
    void getAllHelpStatisticsByDurationIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where duration is greater than or equal to DEFAULT_DURATION
        defaultHelpStatisticsShouldBeFound("duration.greaterThanOrEqual=" + DEFAULT_DURATION);

        // Get all the helpStatisticsList where duration is greater than or equal to UPDATED_DURATION
        defaultHelpStatisticsShouldNotBeFound("duration.greaterThanOrEqual=" + UPDATED_DURATION);
    }

    @Test
    void getAllHelpStatisticsByDurationIsLessThanOrEqualToSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where duration is less than or equal to DEFAULT_DURATION
        defaultHelpStatisticsShouldBeFound("duration.lessThanOrEqual=" + DEFAULT_DURATION);

        // Get all the helpStatisticsList where duration is less than or equal to SMALLER_DURATION
        defaultHelpStatisticsShouldNotBeFound("duration.lessThanOrEqual=" + SMALLER_DURATION);
    }

    @Test
    void getAllHelpStatisticsByDurationIsLessThanSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where duration is less than DEFAULT_DURATION
        defaultHelpStatisticsShouldNotBeFound("duration.lessThan=" + DEFAULT_DURATION);

        // Get all the helpStatisticsList where duration is less than UPDATED_DURATION
        defaultHelpStatisticsShouldBeFound("duration.lessThan=" + UPDATED_DURATION);
    }

    @Test
    void getAllHelpStatisticsByDurationIsGreaterThanSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where duration is greater than DEFAULT_DURATION
        defaultHelpStatisticsShouldNotBeFound("duration.greaterThan=" + DEFAULT_DURATION);

        // Get all the helpStatisticsList where duration is greater than SMALLER_DURATION
        defaultHelpStatisticsShouldBeFound("duration.greaterThan=" + SMALLER_DURATION);
    }

    @Test
    void getAllHelpStatisticsByMonthIsEqualToSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where month equals to DEFAULT_MONTH
        defaultHelpStatisticsShouldBeFound("month.equals=" + DEFAULT_MONTH);

        // Get all the helpStatisticsList where month equals to UPDATED_MONTH
        defaultHelpStatisticsShouldNotBeFound("month.equals=" + UPDATED_MONTH);
    }

    @Test
    void getAllHelpStatisticsByMonthIsInShouldWork() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where month in DEFAULT_MONTH or UPDATED_MONTH
        defaultHelpStatisticsShouldBeFound("month.in=" + DEFAULT_MONTH + "," + UPDATED_MONTH);

        // Get all the helpStatisticsList where month equals to UPDATED_MONTH
        defaultHelpStatisticsShouldNotBeFound("month.in=" + UPDATED_MONTH);
    }

    @Test
    void getAllHelpStatisticsByMonthIsNullOrNotNull() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where month is not null
        defaultHelpStatisticsShouldBeFound("month.specified=true");

        // Get all the helpStatisticsList where month is null
        defaultHelpStatisticsShouldNotBeFound("month.specified=false");
    }

    @Test
    void getAllHelpStatisticsByCreatedByIsEqualToSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where createdBy equals to DEFAULT_CREATED_BY
        defaultHelpStatisticsShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the helpStatisticsList where createdBy equals to UPDATED_CREATED_BY
        defaultHelpStatisticsShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    void getAllHelpStatisticsByCreatedByIsInShouldWork() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultHelpStatisticsShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the helpStatisticsList where createdBy equals to UPDATED_CREATED_BY
        defaultHelpStatisticsShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    void getAllHelpStatisticsByCreatedByIsNullOrNotNull() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where createdBy is not null
        defaultHelpStatisticsShouldBeFound("createdBy.specified=true");

        // Get all the helpStatisticsList where createdBy is null
        defaultHelpStatisticsShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    void getAllHelpStatisticsByCreatedByContainsSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where createdBy contains DEFAULT_CREATED_BY
        defaultHelpStatisticsShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the helpStatisticsList where createdBy contains UPDATED_CREATED_BY
        defaultHelpStatisticsShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    void getAllHelpStatisticsByCreatedByNotContainsSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where createdBy does not contain DEFAULT_CREATED_BY
        defaultHelpStatisticsShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the helpStatisticsList where createdBy does not contain UPDATED_CREATED_BY
        defaultHelpStatisticsShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    void getAllHelpStatisticsByCreatedDateIsEqualToSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where createdDate equals to DEFAULT_CREATED_DATE
        defaultHelpStatisticsShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the helpStatisticsList where createdDate equals to UPDATED_CREATED_DATE
        defaultHelpStatisticsShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    void getAllHelpStatisticsByCreatedDateIsInShouldWork() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultHelpStatisticsShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the helpStatisticsList where createdDate equals to UPDATED_CREATED_DATE
        defaultHelpStatisticsShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    void getAllHelpStatisticsByCreatedDateIsNullOrNotNull() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where createdDate is not null
        defaultHelpStatisticsShouldBeFound("createdDate.specified=true");

        // Get all the helpStatisticsList where createdDate is null
        defaultHelpStatisticsShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    void getAllHelpStatisticsByLastModifiedByIsEqualToSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultHelpStatisticsShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the helpStatisticsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultHelpStatisticsShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void getAllHelpStatisticsByLastModifiedByIsInShouldWork() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultHelpStatisticsShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the helpStatisticsList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultHelpStatisticsShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void getAllHelpStatisticsByLastModifiedByIsNullOrNotNull() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where lastModifiedBy is not null
        defaultHelpStatisticsShouldBeFound("lastModifiedBy.specified=true");

        // Get all the helpStatisticsList where lastModifiedBy is null
        defaultHelpStatisticsShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    void getAllHelpStatisticsByLastModifiedByContainsSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultHelpStatisticsShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the helpStatisticsList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultHelpStatisticsShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void getAllHelpStatisticsByLastModifiedByNotContainsSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultHelpStatisticsShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the helpStatisticsList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultHelpStatisticsShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void getAllHelpStatisticsByLastModifiedDateIsEqualToSomething() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultHelpStatisticsShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the helpStatisticsList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultHelpStatisticsShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    void getAllHelpStatisticsByLastModifiedDateIsInShouldWork() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultHelpStatisticsShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the helpStatisticsList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultHelpStatisticsShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    void getAllHelpStatisticsByLastModifiedDateIsNullOrNotNull() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        // Get all the helpStatisticsList where lastModifiedDate is not null
        defaultHelpStatisticsShouldBeFound("lastModifiedDate.specified=true");

        // Get all the helpStatisticsList where lastModifiedDate is null
        defaultHelpStatisticsShouldNotBeFound("lastModifiedDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHelpStatisticsShouldBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(helpStatistics.getId().intValue()))
            .jsonPath("$.[*].guildId")
            .value(hasItem(DEFAULT_GUILD_ID))
            .jsonPath("$.[*].helpUserId")
            .value(hasItem(DEFAULT_HELP_USER_ID))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION))
            .jsonPath("$.[*].month")
            .value(hasItem(DEFAULT_MONTH.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()));

        // Check, that the count call also returns 1
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(1));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHelpStatisticsShouldNotBeFound(String filter) {
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .isArray()
            .jsonPath("$")
            .isEmpty();

        // Check, that the count call also returns 0
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "/count?sort=id,desc&" + filter)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$")
            .value(is(0));
    }

    @Test
    void getNonExistingHelpStatistics() {
        // Get the helpStatistics
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHelpStatistics() throws Exception {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        int databaseSizeBeforeUpdate = helpStatisticsRepository.findAll().collectList().block().size();

        // Update the helpStatistics
        HelpStatistics updatedHelpStatistics = helpStatisticsRepository.findById(helpStatistics.getId()).block();
        updatedHelpStatistics
            .guildId(UPDATED_GUILD_ID)
            .helpUserId(UPDATED_HELP_USER_ID)
            .duration(UPDATED_DURATION)
            .month(UPDATED_MONTH)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        HelpStatisticsDTO helpStatisticsDTO = helpStatisticsMapper.toDto(updatedHelpStatistics);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, helpStatisticsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpStatisticsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpStatistics in the database
        List<HelpStatistics> helpStatisticsList = helpStatisticsRepository.findAll().collectList().block();
        assertThat(helpStatisticsList).hasSize(databaseSizeBeforeUpdate);
        HelpStatistics testHelpStatistics = helpStatisticsList.get(helpStatisticsList.size() - 1);
        assertThat(testHelpStatistics.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testHelpStatistics.getHelpUserId()).isEqualTo(UPDATED_HELP_USER_ID);
        assertThat(testHelpStatistics.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testHelpStatistics.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testHelpStatistics.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testHelpStatistics.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testHelpStatistics.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testHelpStatistics.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    void putNonExistingHelpStatistics() throws Exception {
        int databaseSizeBeforeUpdate = helpStatisticsRepository.findAll().collectList().block().size();
        helpStatistics.setId(count.incrementAndGet());

        // Create the HelpStatistics
        HelpStatisticsDTO helpStatisticsDTO = helpStatisticsMapper.toDto(helpStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, helpStatisticsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpStatisticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpStatistics in the database
        List<HelpStatistics> helpStatisticsList = helpStatisticsRepository.findAll().collectList().block();
        assertThat(helpStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHelpStatistics() throws Exception {
        int databaseSizeBeforeUpdate = helpStatisticsRepository.findAll().collectList().block().size();
        helpStatistics.setId(count.incrementAndGet());

        // Create the HelpStatistics
        HelpStatisticsDTO helpStatisticsDTO = helpStatisticsMapper.toDto(helpStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpStatisticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpStatistics in the database
        List<HelpStatistics> helpStatisticsList = helpStatisticsRepository.findAll().collectList().block();
        assertThat(helpStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHelpStatistics() throws Exception {
        int databaseSizeBeforeUpdate = helpStatisticsRepository.findAll().collectList().block().size();
        helpStatistics.setId(count.incrementAndGet());

        // Create the HelpStatistics
        HelpStatisticsDTO helpStatisticsDTO = helpStatisticsMapper.toDto(helpStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpStatisticsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HelpStatistics in the database
        List<HelpStatistics> helpStatisticsList = helpStatisticsRepository.findAll().collectList().block();
        assertThat(helpStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHelpStatisticsWithPatch() throws Exception {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        int databaseSizeBeforeUpdate = helpStatisticsRepository.findAll().collectList().block().size();

        // Update the helpStatistics using partial update
        HelpStatistics partialUpdatedHelpStatistics = new HelpStatistics();
        partialUpdatedHelpStatistics.setId(helpStatistics.getId());

        partialUpdatedHelpStatistics.helpUserId(UPDATED_HELP_USER_ID).month(UPDATED_MONTH).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHelpStatistics.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpStatistics))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpStatistics in the database
        List<HelpStatistics> helpStatisticsList = helpStatisticsRepository.findAll().collectList().block();
        assertThat(helpStatisticsList).hasSize(databaseSizeBeforeUpdate);
        HelpStatistics testHelpStatistics = helpStatisticsList.get(helpStatisticsList.size() - 1);
        assertThat(testHelpStatistics.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testHelpStatistics.getHelpUserId()).isEqualTo(UPDATED_HELP_USER_ID);
        assertThat(testHelpStatistics.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testHelpStatistics.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testHelpStatistics.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testHelpStatistics.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testHelpStatistics.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testHelpStatistics.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    void fullUpdateHelpStatisticsWithPatch() throws Exception {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        int databaseSizeBeforeUpdate = helpStatisticsRepository.findAll().collectList().block().size();

        // Update the helpStatistics using partial update
        HelpStatistics partialUpdatedHelpStatistics = new HelpStatistics();
        partialUpdatedHelpStatistics.setId(helpStatistics.getId());

        partialUpdatedHelpStatistics
            .guildId(UPDATED_GUILD_ID)
            .helpUserId(UPDATED_HELP_USER_ID)
            .duration(UPDATED_DURATION)
            .month(UPDATED_MONTH)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHelpStatistics.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpStatistics))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpStatistics in the database
        List<HelpStatistics> helpStatisticsList = helpStatisticsRepository.findAll().collectList().block();
        assertThat(helpStatisticsList).hasSize(databaseSizeBeforeUpdate);
        HelpStatistics testHelpStatistics = helpStatisticsList.get(helpStatisticsList.size() - 1);
        assertThat(testHelpStatistics.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testHelpStatistics.getHelpUserId()).isEqualTo(UPDATED_HELP_USER_ID);
        assertThat(testHelpStatistics.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testHelpStatistics.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testHelpStatistics.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testHelpStatistics.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testHelpStatistics.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testHelpStatistics.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    void patchNonExistingHelpStatistics() throws Exception {
        int databaseSizeBeforeUpdate = helpStatisticsRepository.findAll().collectList().block().size();
        helpStatistics.setId(count.incrementAndGet());

        // Create the HelpStatistics
        HelpStatisticsDTO helpStatisticsDTO = helpStatisticsMapper.toDto(helpStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, helpStatisticsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpStatisticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpStatistics in the database
        List<HelpStatistics> helpStatisticsList = helpStatisticsRepository.findAll().collectList().block();
        assertThat(helpStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHelpStatistics() throws Exception {
        int databaseSizeBeforeUpdate = helpStatisticsRepository.findAll().collectList().block().size();
        helpStatistics.setId(count.incrementAndGet());

        // Create the HelpStatistics
        HelpStatisticsDTO helpStatisticsDTO = helpStatisticsMapper.toDto(helpStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpStatisticsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpStatistics in the database
        List<HelpStatistics> helpStatisticsList = helpStatisticsRepository.findAll().collectList().block();
        assertThat(helpStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHelpStatistics() throws Exception {
        int databaseSizeBeforeUpdate = helpStatisticsRepository.findAll().collectList().block().size();
        helpStatistics.setId(count.incrementAndGet());

        // Create the HelpStatistics
        HelpStatisticsDTO helpStatisticsDTO = helpStatisticsMapper.toDto(helpStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpStatisticsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HelpStatistics in the database
        List<HelpStatistics> helpStatisticsList = helpStatisticsRepository.findAll().collectList().block();
        assertThat(helpStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHelpStatistics() {
        // Initialize the database
        helpStatisticsRepository.save(helpStatistics).block();

        int databaseSizeBeforeDelete = helpStatisticsRepository.findAll().collectList().block().size();

        // Delete the helpStatistics
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, helpStatistics.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<HelpStatistics> helpStatisticsList = helpStatisticsRepository.findAll().collectList().block();
        assertThat(helpStatisticsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
