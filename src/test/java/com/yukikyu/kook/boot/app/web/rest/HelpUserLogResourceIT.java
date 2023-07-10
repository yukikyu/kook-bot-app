package com.yukikyu.kook.boot.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.yukikyu.kook.boot.app.IntegrationTest;
import com.yukikyu.kook.boot.app.domain.HelpUserLog;
import com.yukikyu.kook.boot.app.repository.EntityManager;
import com.yukikyu.kook.boot.app.repository.HelpUserLogRepository;
import com.yukikyu.kook.boot.app.service.dto.HelpUserLogDTO;
import com.yukikyu.kook.boot.app.service.mapper.HelpUserLogMapper;
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
 * Integration tests for the {@link HelpUserLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HelpUserLogResourceIT {

    private static final String DEFAULT_GUILD_ID = "AAAAAAAAAA";
    private static final String UPDATED_GUILD_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CHANNEL_ID = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_HELP_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_HELP_USER_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_JOIN_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_JOIN_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXIT_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXIT_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/help-user-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HelpUserLogRepository helpUserLogRepository;

    @Autowired
    private HelpUserLogMapper helpUserLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HelpUserLog helpUserLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpUserLog createEntity(EntityManager em) {
        HelpUserLog helpUserLog = new HelpUserLog()
            .guildId(DEFAULT_GUILD_ID)
            .channelId(DEFAULT_CHANNEL_ID)
            .userId(DEFAULT_USER_ID)
            .helpUserId(DEFAULT_HELP_USER_ID)
            .joinAt(DEFAULT_JOIN_AT)
            .exitAt(DEFAULT_EXIT_AT)
            .status(DEFAULT_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return helpUserLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpUserLog createUpdatedEntity(EntityManager em) {
        HelpUserLog helpUserLog = new HelpUserLog()
            .guildId(UPDATED_GUILD_ID)
            .channelId(UPDATED_CHANNEL_ID)
            .userId(UPDATED_USER_ID)
            .helpUserId(UPDATED_HELP_USER_ID)
            .joinAt(UPDATED_JOIN_AT)
            .exitAt(UPDATED_EXIT_AT)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return helpUserLog;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(HelpUserLog.class).block();
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
        helpUserLog = createEntity(em);
    }

    @Test
    void createHelpUserLog() throws Exception {
        int databaseSizeBeforeCreate = helpUserLogRepository.findAll().collectList().block().size();
        // Create the HelpUserLog
        HelpUserLogDTO helpUserLogDTO = helpUserLogMapper.toDto(helpUserLog);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpUserLogDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the HelpUserLog in the database
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findAll().collectList().block();
        assertThat(helpUserLogList).hasSize(databaseSizeBeforeCreate + 1);
        HelpUserLog testHelpUserLog = helpUserLogList.get(helpUserLogList.size() - 1);
        assertThat(testHelpUserLog.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testHelpUserLog.getChannelId()).isEqualTo(DEFAULT_CHANNEL_ID);
        assertThat(testHelpUserLog.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testHelpUserLog.getHelpUserId()).isEqualTo(DEFAULT_HELP_USER_ID);
        assertThat(testHelpUserLog.getJoinAt()).isEqualTo(DEFAULT_JOIN_AT);
        assertThat(testHelpUserLog.getExitAt()).isEqualTo(DEFAULT_EXIT_AT);
        assertThat(testHelpUserLog.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testHelpUserLog.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testHelpUserLog.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testHelpUserLog.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testHelpUserLog.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    void createHelpUserLogWithExistingId() throws Exception {
        // Create the HelpUserLog with an existing ID
        helpUserLog.setId(1L);
        HelpUserLogDTO helpUserLogDTO = helpUserLogMapper.toDto(helpUserLog);

        int databaseSizeBeforeCreate = helpUserLogRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpUserLogDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpUserLog in the database
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findAll().collectList().block();
        assertThat(helpUserLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllHelpUserLogs() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList
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
            .value(hasItem(helpUserLog.getId().intValue()))
            .jsonPath("$.[*].guildId")
            .value(hasItem(DEFAULT_GUILD_ID))
            .jsonPath("$.[*].channelId")
            .value(hasItem(DEFAULT_CHANNEL_ID))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID))
            .jsonPath("$.[*].helpUserId")
            .value(hasItem(DEFAULT_HELP_USER_ID))
            .jsonPath("$.[*].joinAt")
            .value(hasItem(DEFAULT_JOIN_AT.toString()))
            .jsonPath("$.[*].exitAt")
            .value(hasItem(DEFAULT_EXIT_AT.toString()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS))
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
    void getHelpUserLog() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get the helpUserLog
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, helpUserLog.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(helpUserLog.getId().intValue()))
            .jsonPath("$.guildId")
            .value(is(DEFAULT_GUILD_ID))
            .jsonPath("$.channelId")
            .value(is(DEFAULT_CHANNEL_ID))
            .jsonPath("$.userId")
            .value(is(DEFAULT_USER_ID))
            .jsonPath("$.helpUserId")
            .value(is(DEFAULT_HELP_USER_ID))
            .jsonPath("$.joinAt")
            .value(is(DEFAULT_JOIN_AT.toString()))
            .jsonPath("$.exitAt")
            .value(is(DEFAULT_EXIT_AT.toString()))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS))
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
    void getHelpUserLogsByIdFiltering() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        Long id = helpUserLog.getId();

        defaultHelpUserLogShouldBeFound("id.equals=" + id);
        defaultHelpUserLogShouldNotBeFound("id.notEquals=" + id);

        defaultHelpUserLogShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHelpUserLogShouldNotBeFound("id.greaterThan=" + id);

        defaultHelpUserLogShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHelpUserLogShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllHelpUserLogsByGuildIdIsEqualToSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where guildId equals to DEFAULT_GUILD_ID
        defaultHelpUserLogShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the helpUserLogList where guildId equals to UPDATED_GUILD_ID
        defaultHelpUserLogShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    void getAllHelpUserLogsByGuildIdIsInShouldWork() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultHelpUserLogShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the helpUserLogList where guildId equals to UPDATED_GUILD_ID
        defaultHelpUserLogShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    void getAllHelpUserLogsByGuildIdIsNullOrNotNull() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where guildId is not null
        defaultHelpUserLogShouldBeFound("guildId.specified=true");

        // Get all the helpUserLogList where guildId is null
        defaultHelpUserLogShouldNotBeFound("guildId.specified=false");
    }

    @Test
    void getAllHelpUserLogsByGuildIdContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where guildId contains DEFAULT_GUILD_ID
        defaultHelpUserLogShouldBeFound("guildId.contains=" + DEFAULT_GUILD_ID);

        // Get all the helpUserLogList where guildId contains UPDATED_GUILD_ID
        defaultHelpUserLogShouldNotBeFound("guildId.contains=" + UPDATED_GUILD_ID);
    }

    @Test
    void getAllHelpUserLogsByGuildIdNotContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where guildId does not contain DEFAULT_GUILD_ID
        defaultHelpUserLogShouldNotBeFound("guildId.doesNotContain=" + DEFAULT_GUILD_ID);

        // Get all the helpUserLogList where guildId does not contain UPDATED_GUILD_ID
        defaultHelpUserLogShouldBeFound("guildId.doesNotContain=" + UPDATED_GUILD_ID);
    }

    @Test
    void getAllHelpUserLogsByChannelIdIsEqualToSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where channelId equals to DEFAULT_CHANNEL_ID
        defaultHelpUserLogShouldBeFound("channelId.equals=" + DEFAULT_CHANNEL_ID);

        // Get all the helpUserLogList where channelId equals to UPDATED_CHANNEL_ID
        defaultHelpUserLogShouldNotBeFound("channelId.equals=" + UPDATED_CHANNEL_ID);
    }

    @Test
    void getAllHelpUserLogsByChannelIdIsInShouldWork() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where channelId in DEFAULT_CHANNEL_ID or UPDATED_CHANNEL_ID
        defaultHelpUserLogShouldBeFound("channelId.in=" + DEFAULT_CHANNEL_ID + "," + UPDATED_CHANNEL_ID);

        // Get all the helpUserLogList where channelId equals to UPDATED_CHANNEL_ID
        defaultHelpUserLogShouldNotBeFound("channelId.in=" + UPDATED_CHANNEL_ID);
    }

    @Test
    void getAllHelpUserLogsByChannelIdIsNullOrNotNull() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where channelId is not null
        defaultHelpUserLogShouldBeFound("channelId.specified=true");

        // Get all the helpUserLogList where channelId is null
        defaultHelpUserLogShouldNotBeFound("channelId.specified=false");
    }

    @Test
    void getAllHelpUserLogsByChannelIdContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where channelId contains DEFAULT_CHANNEL_ID
        defaultHelpUserLogShouldBeFound("channelId.contains=" + DEFAULT_CHANNEL_ID);

        // Get all the helpUserLogList where channelId contains UPDATED_CHANNEL_ID
        defaultHelpUserLogShouldNotBeFound("channelId.contains=" + UPDATED_CHANNEL_ID);
    }

    @Test
    void getAllHelpUserLogsByChannelIdNotContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where channelId does not contain DEFAULT_CHANNEL_ID
        defaultHelpUserLogShouldNotBeFound("channelId.doesNotContain=" + DEFAULT_CHANNEL_ID);

        // Get all the helpUserLogList where channelId does not contain UPDATED_CHANNEL_ID
        defaultHelpUserLogShouldBeFound("channelId.doesNotContain=" + UPDATED_CHANNEL_ID);
    }

    @Test
    void getAllHelpUserLogsByUserIdIsEqualToSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where userId equals to DEFAULT_USER_ID
        defaultHelpUserLogShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the helpUserLogList where userId equals to UPDATED_USER_ID
        defaultHelpUserLogShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    void getAllHelpUserLogsByUserIdIsInShouldWork() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultHelpUserLogShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the helpUserLogList where userId equals to UPDATED_USER_ID
        defaultHelpUserLogShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    void getAllHelpUserLogsByUserIdIsNullOrNotNull() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where userId is not null
        defaultHelpUserLogShouldBeFound("userId.specified=true");

        // Get all the helpUserLogList where userId is null
        defaultHelpUserLogShouldNotBeFound("userId.specified=false");
    }

    @Test
    void getAllHelpUserLogsByUserIdContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where userId contains DEFAULT_USER_ID
        defaultHelpUserLogShouldBeFound("userId.contains=" + DEFAULT_USER_ID);

        // Get all the helpUserLogList where userId contains UPDATED_USER_ID
        defaultHelpUserLogShouldNotBeFound("userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    void getAllHelpUserLogsByUserIdNotContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where userId does not contain DEFAULT_USER_ID
        defaultHelpUserLogShouldNotBeFound("userId.doesNotContain=" + DEFAULT_USER_ID);

        // Get all the helpUserLogList where userId does not contain UPDATED_USER_ID
        defaultHelpUserLogShouldBeFound("userId.doesNotContain=" + UPDATED_USER_ID);
    }

    @Test
    void getAllHelpUserLogsByHelpUserIdIsEqualToSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where helpUserId equals to DEFAULT_HELP_USER_ID
        defaultHelpUserLogShouldBeFound("helpUserId.equals=" + DEFAULT_HELP_USER_ID);

        // Get all the helpUserLogList where helpUserId equals to UPDATED_HELP_USER_ID
        defaultHelpUserLogShouldNotBeFound("helpUserId.equals=" + UPDATED_HELP_USER_ID);
    }

    @Test
    void getAllHelpUserLogsByHelpUserIdIsInShouldWork() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where helpUserId in DEFAULT_HELP_USER_ID or UPDATED_HELP_USER_ID
        defaultHelpUserLogShouldBeFound("helpUserId.in=" + DEFAULT_HELP_USER_ID + "," + UPDATED_HELP_USER_ID);

        // Get all the helpUserLogList where helpUserId equals to UPDATED_HELP_USER_ID
        defaultHelpUserLogShouldNotBeFound("helpUserId.in=" + UPDATED_HELP_USER_ID);
    }

    @Test
    void getAllHelpUserLogsByHelpUserIdIsNullOrNotNull() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where helpUserId is not null
        defaultHelpUserLogShouldBeFound("helpUserId.specified=true");

        // Get all the helpUserLogList where helpUserId is null
        defaultHelpUserLogShouldNotBeFound("helpUserId.specified=false");
    }

    @Test
    void getAllHelpUserLogsByHelpUserIdContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where helpUserId contains DEFAULT_HELP_USER_ID
        defaultHelpUserLogShouldBeFound("helpUserId.contains=" + DEFAULT_HELP_USER_ID);

        // Get all the helpUserLogList where helpUserId contains UPDATED_HELP_USER_ID
        defaultHelpUserLogShouldNotBeFound("helpUserId.contains=" + UPDATED_HELP_USER_ID);
    }

    @Test
    void getAllHelpUserLogsByHelpUserIdNotContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where helpUserId does not contain DEFAULT_HELP_USER_ID
        defaultHelpUserLogShouldNotBeFound("helpUserId.doesNotContain=" + DEFAULT_HELP_USER_ID);

        // Get all the helpUserLogList where helpUserId does not contain UPDATED_HELP_USER_ID
        defaultHelpUserLogShouldBeFound("helpUserId.doesNotContain=" + UPDATED_HELP_USER_ID);
    }

    @Test
    void getAllHelpUserLogsByJoinAtIsEqualToSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where joinAt equals to DEFAULT_JOIN_AT
        defaultHelpUserLogShouldBeFound("joinAt.equals=" + DEFAULT_JOIN_AT);

        // Get all the helpUserLogList where joinAt equals to UPDATED_JOIN_AT
        defaultHelpUserLogShouldNotBeFound("joinAt.equals=" + UPDATED_JOIN_AT);
    }

    @Test
    void getAllHelpUserLogsByJoinAtIsInShouldWork() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where joinAt in DEFAULT_JOIN_AT or UPDATED_JOIN_AT
        defaultHelpUserLogShouldBeFound("joinAt.in=" + DEFAULT_JOIN_AT + "," + UPDATED_JOIN_AT);

        // Get all the helpUserLogList where joinAt equals to UPDATED_JOIN_AT
        defaultHelpUserLogShouldNotBeFound("joinAt.in=" + UPDATED_JOIN_AT);
    }

    @Test
    void getAllHelpUserLogsByJoinAtIsNullOrNotNull() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where joinAt is not null
        defaultHelpUserLogShouldBeFound("joinAt.specified=true");

        // Get all the helpUserLogList where joinAt is null
        defaultHelpUserLogShouldNotBeFound("joinAt.specified=false");
    }

    @Test
    void getAllHelpUserLogsByExitAtIsEqualToSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where exitAt equals to DEFAULT_EXIT_AT
        defaultHelpUserLogShouldBeFound("exitAt.equals=" + DEFAULT_EXIT_AT);

        // Get all the helpUserLogList where exitAt equals to UPDATED_EXIT_AT
        defaultHelpUserLogShouldNotBeFound("exitAt.equals=" + UPDATED_EXIT_AT);
    }

    @Test
    void getAllHelpUserLogsByExitAtIsInShouldWork() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where exitAt in DEFAULT_EXIT_AT or UPDATED_EXIT_AT
        defaultHelpUserLogShouldBeFound("exitAt.in=" + DEFAULT_EXIT_AT + "," + UPDATED_EXIT_AT);

        // Get all the helpUserLogList where exitAt equals to UPDATED_EXIT_AT
        defaultHelpUserLogShouldNotBeFound("exitAt.in=" + UPDATED_EXIT_AT);
    }

    @Test
    void getAllHelpUserLogsByExitAtIsNullOrNotNull() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where exitAt is not null
        defaultHelpUserLogShouldBeFound("exitAt.specified=true");

        // Get all the helpUserLogList where exitAt is null
        defaultHelpUserLogShouldNotBeFound("exitAt.specified=false");
    }

    @Test
    void getAllHelpUserLogsByStatusIsEqualToSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where status equals to DEFAULT_STATUS
        defaultHelpUserLogShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the helpUserLogList where status equals to UPDATED_STATUS
        defaultHelpUserLogShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    void getAllHelpUserLogsByStatusIsInShouldWork() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultHelpUserLogShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the helpUserLogList where status equals to UPDATED_STATUS
        defaultHelpUserLogShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    void getAllHelpUserLogsByStatusIsNullOrNotNull() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where status is not null
        defaultHelpUserLogShouldBeFound("status.specified=true");

        // Get all the helpUserLogList where status is null
        defaultHelpUserLogShouldNotBeFound("status.specified=false");
    }

    @Test
    void getAllHelpUserLogsByStatusContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where status contains DEFAULT_STATUS
        defaultHelpUserLogShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the helpUserLogList where status contains UPDATED_STATUS
        defaultHelpUserLogShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    void getAllHelpUserLogsByStatusNotContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where status does not contain DEFAULT_STATUS
        defaultHelpUserLogShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the helpUserLogList where status does not contain UPDATED_STATUS
        defaultHelpUserLogShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    void getAllHelpUserLogsByCreatedByIsEqualToSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where createdBy equals to DEFAULT_CREATED_BY
        defaultHelpUserLogShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the helpUserLogList where createdBy equals to UPDATED_CREATED_BY
        defaultHelpUserLogShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    void getAllHelpUserLogsByCreatedByIsInShouldWork() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultHelpUserLogShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the helpUserLogList where createdBy equals to UPDATED_CREATED_BY
        defaultHelpUserLogShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    void getAllHelpUserLogsByCreatedByIsNullOrNotNull() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where createdBy is not null
        defaultHelpUserLogShouldBeFound("createdBy.specified=true");

        // Get all the helpUserLogList where createdBy is null
        defaultHelpUserLogShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    void getAllHelpUserLogsByCreatedByContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where createdBy contains DEFAULT_CREATED_BY
        defaultHelpUserLogShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the helpUserLogList where createdBy contains UPDATED_CREATED_BY
        defaultHelpUserLogShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    void getAllHelpUserLogsByCreatedByNotContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where createdBy does not contain DEFAULT_CREATED_BY
        defaultHelpUserLogShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the helpUserLogList where createdBy does not contain UPDATED_CREATED_BY
        defaultHelpUserLogShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    void getAllHelpUserLogsByCreatedDateIsEqualToSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where createdDate equals to DEFAULT_CREATED_DATE
        defaultHelpUserLogShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the helpUserLogList where createdDate equals to UPDATED_CREATED_DATE
        defaultHelpUserLogShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    void getAllHelpUserLogsByCreatedDateIsInShouldWork() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultHelpUserLogShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the helpUserLogList where createdDate equals to UPDATED_CREATED_DATE
        defaultHelpUserLogShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    void getAllHelpUserLogsByCreatedDateIsNullOrNotNull() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where createdDate is not null
        defaultHelpUserLogShouldBeFound("createdDate.specified=true");

        // Get all the helpUserLogList where createdDate is null
        defaultHelpUserLogShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    void getAllHelpUserLogsByLastModifiedByIsEqualToSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultHelpUserLogShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the helpUserLogList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultHelpUserLogShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void getAllHelpUserLogsByLastModifiedByIsInShouldWork() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultHelpUserLogShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the helpUserLogList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultHelpUserLogShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void getAllHelpUserLogsByLastModifiedByIsNullOrNotNull() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where lastModifiedBy is not null
        defaultHelpUserLogShouldBeFound("lastModifiedBy.specified=true");

        // Get all the helpUserLogList where lastModifiedBy is null
        defaultHelpUserLogShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    void getAllHelpUserLogsByLastModifiedByContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultHelpUserLogShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the helpUserLogList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultHelpUserLogShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void getAllHelpUserLogsByLastModifiedByNotContainsSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultHelpUserLogShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the helpUserLogList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultHelpUserLogShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void getAllHelpUserLogsByLastModifiedDateIsEqualToSomething() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultHelpUserLogShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the helpUserLogList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultHelpUserLogShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    void getAllHelpUserLogsByLastModifiedDateIsInShouldWork() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultHelpUserLogShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the helpUserLogList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultHelpUserLogShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    void getAllHelpUserLogsByLastModifiedDateIsNullOrNotNull() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        // Get all the helpUserLogList where lastModifiedDate is not null
        defaultHelpUserLogShouldBeFound("lastModifiedDate.specified=true");

        // Get all the helpUserLogList where lastModifiedDate is null
        defaultHelpUserLogShouldNotBeFound("lastModifiedDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHelpUserLogShouldBeFound(String filter) {
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
            .value(hasItem(helpUserLog.getId().intValue()))
            .jsonPath("$.[*].guildId")
            .value(hasItem(DEFAULT_GUILD_ID))
            .jsonPath("$.[*].channelId")
            .value(hasItem(DEFAULT_CHANNEL_ID))
            .jsonPath("$.[*].userId")
            .value(hasItem(DEFAULT_USER_ID))
            .jsonPath("$.[*].helpUserId")
            .value(hasItem(DEFAULT_HELP_USER_ID))
            .jsonPath("$.[*].joinAt")
            .value(hasItem(DEFAULT_JOIN_AT.toString()))
            .jsonPath("$.[*].exitAt")
            .value(hasItem(DEFAULT_EXIT_AT.toString()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS))
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
    private void defaultHelpUserLogShouldNotBeFound(String filter) {
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
    void getNonExistingHelpUserLog() {
        // Get the helpUserLog
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHelpUserLog() throws Exception {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        int databaseSizeBeforeUpdate = helpUserLogRepository.findAll().collectList().block().size();

        // Update the helpUserLog
        HelpUserLog updatedHelpUserLog = helpUserLogRepository.findById(helpUserLog.getId()).block();
        updatedHelpUserLog
            .guildId(UPDATED_GUILD_ID)
            .channelId(UPDATED_CHANNEL_ID)
            .userId(UPDATED_USER_ID)
            .helpUserId(UPDATED_HELP_USER_ID)
            .joinAt(UPDATED_JOIN_AT)
            .exitAt(UPDATED_EXIT_AT)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        HelpUserLogDTO helpUserLogDTO = helpUserLogMapper.toDto(updatedHelpUserLog);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, helpUserLogDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpUserLogDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpUserLog in the database
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findAll().collectList().block();
        assertThat(helpUserLogList).hasSize(databaseSizeBeforeUpdate);
        HelpUserLog testHelpUserLog = helpUserLogList.get(helpUserLogList.size() - 1);
        assertThat(testHelpUserLog.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testHelpUserLog.getChannelId()).isEqualTo(UPDATED_CHANNEL_ID);
        assertThat(testHelpUserLog.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testHelpUserLog.getHelpUserId()).isEqualTo(UPDATED_HELP_USER_ID);
        assertThat(testHelpUserLog.getJoinAt()).isEqualTo(UPDATED_JOIN_AT);
        assertThat(testHelpUserLog.getExitAt()).isEqualTo(UPDATED_EXIT_AT);
        assertThat(testHelpUserLog.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testHelpUserLog.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testHelpUserLog.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testHelpUserLog.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testHelpUserLog.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    void putNonExistingHelpUserLog() throws Exception {
        int databaseSizeBeforeUpdate = helpUserLogRepository.findAll().collectList().block().size();
        helpUserLog.setId(count.incrementAndGet());

        // Create the HelpUserLog
        HelpUserLogDTO helpUserLogDTO = helpUserLogMapper.toDto(helpUserLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, helpUserLogDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpUserLogDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpUserLog in the database
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findAll().collectList().block();
        assertThat(helpUserLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHelpUserLog() throws Exception {
        int databaseSizeBeforeUpdate = helpUserLogRepository.findAll().collectList().block().size();
        helpUserLog.setId(count.incrementAndGet());

        // Create the HelpUserLog
        HelpUserLogDTO helpUserLogDTO = helpUserLogMapper.toDto(helpUserLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpUserLogDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpUserLog in the database
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findAll().collectList().block();
        assertThat(helpUserLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHelpUserLog() throws Exception {
        int databaseSizeBeforeUpdate = helpUserLogRepository.findAll().collectList().block().size();
        helpUserLog.setId(count.incrementAndGet());

        // Create the HelpUserLog
        HelpUserLogDTO helpUserLogDTO = helpUserLogMapper.toDto(helpUserLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpUserLogDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HelpUserLog in the database
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findAll().collectList().block();
        assertThat(helpUserLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHelpUserLogWithPatch() throws Exception {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        int databaseSizeBeforeUpdate = helpUserLogRepository.findAll().collectList().block().size();

        // Update the helpUserLog using partial update
        HelpUserLog partialUpdatedHelpUserLog = new HelpUserLog();
        partialUpdatedHelpUserLog.setId(helpUserLog.getId());

        partialUpdatedHelpUserLog
            .channelId(UPDATED_CHANNEL_ID)
            .helpUserId(UPDATED_HELP_USER_ID)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHelpUserLog.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpUserLog))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpUserLog in the database
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findAll().collectList().block();
        assertThat(helpUserLogList).hasSize(databaseSizeBeforeUpdate);
        HelpUserLog testHelpUserLog = helpUserLogList.get(helpUserLogList.size() - 1);
        assertThat(testHelpUserLog.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testHelpUserLog.getChannelId()).isEqualTo(UPDATED_CHANNEL_ID);
        assertThat(testHelpUserLog.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testHelpUserLog.getHelpUserId()).isEqualTo(UPDATED_HELP_USER_ID);
        assertThat(testHelpUserLog.getJoinAt()).isEqualTo(DEFAULT_JOIN_AT);
        assertThat(testHelpUserLog.getExitAt()).isEqualTo(DEFAULT_EXIT_AT);
        assertThat(testHelpUserLog.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testHelpUserLog.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testHelpUserLog.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testHelpUserLog.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testHelpUserLog.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    void fullUpdateHelpUserLogWithPatch() throws Exception {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        int databaseSizeBeforeUpdate = helpUserLogRepository.findAll().collectList().block().size();

        // Update the helpUserLog using partial update
        HelpUserLog partialUpdatedHelpUserLog = new HelpUserLog();
        partialUpdatedHelpUserLog.setId(helpUserLog.getId());

        partialUpdatedHelpUserLog
            .guildId(UPDATED_GUILD_ID)
            .channelId(UPDATED_CHANNEL_ID)
            .userId(UPDATED_USER_ID)
            .helpUserId(UPDATED_HELP_USER_ID)
            .joinAt(UPDATED_JOIN_AT)
            .exitAt(UPDATED_EXIT_AT)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHelpUserLog.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpUserLog))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpUserLog in the database
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findAll().collectList().block();
        assertThat(helpUserLogList).hasSize(databaseSizeBeforeUpdate);
        HelpUserLog testHelpUserLog = helpUserLogList.get(helpUserLogList.size() - 1);
        assertThat(testHelpUserLog.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testHelpUserLog.getChannelId()).isEqualTo(UPDATED_CHANNEL_ID);
        assertThat(testHelpUserLog.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testHelpUserLog.getHelpUserId()).isEqualTo(UPDATED_HELP_USER_ID);
        assertThat(testHelpUserLog.getJoinAt()).isEqualTo(UPDATED_JOIN_AT);
        assertThat(testHelpUserLog.getExitAt()).isEqualTo(UPDATED_EXIT_AT);
        assertThat(testHelpUserLog.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testHelpUserLog.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testHelpUserLog.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testHelpUserLog.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testHelpUserLog.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    void patchNonExistingHelpUserLog() throws Exception {
        int databaseSizeBeforeUpdate = helpUserLogRepository.findAll().collectList().block().size();
        helpUserLog.setId(count.incrementAndGet());

        // Create the HelpUserLog
        HelpUserLogDTO helpUserLogDTO = helpUserLogMapper.toDto(helpUserLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, helpUserLogDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpUserLogDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpUserLog in the database
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findAll().collectList().block();
        assertThat(helpUserLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHelpUserLog() throws Exception {
        int databaseSizeBeforeUpdate = helpUserLogRepository.findAll().collectList().block().size();
        helpUserLog.setId(count.incrementAndGet());

        // Create the HelpUserLog
        HelpUserLogDTO helpUserLogDTO = helpUserLogMapper.toDto(helpUserLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpUserLogDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpUserLog in the database
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findAll().collectList().block();
        assertThat(helpUserLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHelpUserLog() throws Exception {
        int databaseSizeBeforeUpdate = helpUserLogRepository.findAll().collectList().block().size();
        helpUserLog.setId(count.incrementAndGet());

        // Create the HelpUserLog
        HelpUserLogDTO helpUserLogDTO = helpUserLogMapper.toDto(helpUserLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpUserLogDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HelpUserLog in the database
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findAll().collectList().block();
        assertThat(helpUserLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHelpUserLog() {
        // Initialize the database
        helpUserLogRepository.save(helpUserLog).block();

        int databaseSizeBeforeDelete = helpUserLogRepository.findAll().collectList().block().size();

        // Delete the helpUserLog
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, helpUserLog.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<HelpUserLog> helpUserLogList = helpUserLogRepository.findAll().collectList().block();
        assertThat(helpUserLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
