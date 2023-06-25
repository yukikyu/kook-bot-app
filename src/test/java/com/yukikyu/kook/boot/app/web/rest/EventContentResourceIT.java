package com.yukikyu.kook.boot.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.yukikyu.kook.boot.app.IntegrationTest;
import com.yukikyu.kook.boot.app.domain.EventContent;
import com.yukikyu.kook.boot.app.repository.EntityManager;
import com.yukikyu.kook.boot.app.repository.EventContentRepository;
import com.yukikyu.kook.boot.app.service.dto.EventContentDTO;
import com.yukikyu.kook.boot.app.service.mapper.EventContentMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link EventContentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EventContentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/event-contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventContentRepository eventContentRepository;

    @Autowired
    private EventContentMapper eventContentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EventContent eventContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventContent createEntity(EntityManager em) {
        EventContent eventContent = new EventContent()
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE)
            .className(DEFAULT_CLASS_NAME)
            .metadata(DEFAULT_METADATA)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return eventContent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventContent createUpdatedEntity(EntityManager em) {
        EventContent eventContent = new EventContent()
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .className(UPDATED_CLASS_NAME)
            .metadata(UPDATED_METADATA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return eventContent;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EventContent.class).block();
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
        eventContent = createEntity(em);
    }

    @Test
    void createEventContent() throws Exception {
        int databaseSizeBeforeCreate = eventContentRepository.findAll().collectList().block().size();
        // Create the EventContent
        EventContentDTO eventContentDTO = eventContentMapper.toDto(eventContent);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventContentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the EventContent in the database
        List<EventContent> eventContentList = eventContentRepository.findAll().collectList().block();
        assertThat(eventContentList).hasSize(databaseSizeBeforeCreate + 1);
        EventContent testEventContent = eventContentList.get(eventContentList.size() - 1);
        assertThat(testEventContent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventContent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEventContent.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testEventContent.getMetadata()).isEqualTo(DEFAULT_METADATA);
        assertThat(testEventContent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testEventContent.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testEventContent.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testEventContent.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    void createEventContentWithExistingId() throws Exception {
        // Create the EventContent with an existing ID
        eventContent.setId(1L);
        EventContentDTO eventContentDTO = eventContentMapper.toDto(eventContent);

        int databaseSizeBeforeCreate = eventContentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EventContent in the database
        List<EventContent> eventContentList = eventContentRepository.findAll().collectList().block();
        assertThat(eventContentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEventContents() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList
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
            .value(hasItem(eventContent.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].className")
            .value(hasItem(DEFAULT_CLASS_NAME))
            .jsonPath("$.[*].metadata")
            .value(hasItem(DEFAULT_METADATA.toString()))
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
    void getEventContent() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get the eventContent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, eventContent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(eventContent.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.className")
            .value(is(DEFAULT_CLASS_NAME))
            .jsonPath("$.metadata")
            .value(is(DEFAULT_METADATA.toString()))
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
    void getEventContentsByIdFiltering() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        Long id = eventContent.getId();

        defaultEventContentShouldBeFound("id.equals=" + id);
        defaultEventContentShouldNotBeFound("id.notEquals=" + id);

        defaultEventContentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventContentShouldNotBeFound("id.greaterThan=" + id);

        defaultEventContentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventContentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    void getAllEventContentsByNameIsEqualToSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where name equals to DEFAULT_NAME
        defaultEventContentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the eventContentList where name equals to UPDATED_NAME
        defaultEventContentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    void getAllEventContentsByNameIsInShouldWork() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEventContentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the eventContentList where name equals to UPDATED_NAME
        defaultEventContentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    void getAllEventContentsByNameIsNullOrNotNull() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where name is not null
        defaultEventContentShouldBeFound("name.specified=true");

        // Get all the eventContentList where name is null
        defaultEventContentShouldNotBeFound("name.specified=false");
    }

    @Test
    void getAllEventContentsByNameContainsSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where name contains DEFAULT_NAME
        defaultEventContentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the eventContentList where name contains UPDATED_NAME
        defaultEventContentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    void getAllEventContentsByNameNotContainsSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where name does not contain DEFAULT_NAME
        defaultEventContentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the eventContentList where name does not contain UPDATED_NAME
        defaultEventContentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    void getAllEventContentsByTitleIsEqualToSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where title equals to DEFAULT_TITLE
        defaultEventContentShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the eventContentList where title equals to UPDATED_TITLE
        defaultEventContentShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    void getAllEventContentsByTitleIsInShouldWork() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultEventContentShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the eventContentList where title equals to UPDATED_TITLE
        defaultEventContentShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    void getAllEventContentsByTitleIsNullOrNotNull() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where title is not null
        defaultEventContentShouldBeFound("title.specified=true");

        // Get all the eventContentList where title is null
        defaultEventContentShouldNotBeFound("title.specified=false");
    }

    @Test
    void getAllEventContentsByTitleContainsSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where title contains DEFAULT_TITLE
        defaultEventContentShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the eventContentList where title contains UPDATED_TITLE
        defaultEventContentShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    void getAllEventContentsByTitleNotContainsSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where title does not contain DEFAULT_TITLE
        defaultEventContentShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the eventContentList where title does not contain UPDATED_TITLE
        defaultEventContentShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    void getAllEventContentsByClassNameIsEqualToSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where className equals to DEFAULT_CLASS_NAME
        defaultEventContentShouldBeFound("className.equals=" + DEFAULT_CLASS_NAME);

        // Get all the eventContentList where className equals to UPDATED_CLASS_NAME
        defaultEventContentShouldNotBeFound("className.equals=" + UPDATED_CLASS_NAME);
    }

    @Test
    void getAllEventContentsByClassNameIsInShouldWork() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where className in DEFAULT_CLASS_NAME or UPDATED_CLASS_NAME
        defaultEventContentShouldBeFound("className.in=" + DEFAULT_CLASS_NAME + "," + UPDATED_CLASS_NAME);

        // Get all the eventContentList where className equals to UPDATED_CLASS_NAME
        defaultEventContentShouldNotBeFound("className.in=" + UPDATED_CLASS_NAME);
    }

    @Test
    void getAllEventContentsByClassNameIsNullOrNotNull() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where className is not null
        defaultEventContentShouldBeFound("className.specified=true");

        // Get all the eventContentList where className is null
        defaultEventContentShouldNotBeFound("className.specified=false");
    }

    @Test
    void getAllEventContentsByClassNameContainsSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where className contains DEFAULT_CLASS_NAME
        defaultEventContentShouldBeFound("className.contains=" + DEFAULT_CLASS_NAME);

        // Get all the eventContentList where className contains UPDATED_CLASS_NAME
        defaultEventContentShouldNotBeFound("className.contains=" + UPDATED_CLASS_NAME);
    }

    @Test
    void getAllEventContentsByClassNameNotContainsSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where className does not contain DEFAULT_CLASS_NAME
        defaultEventContentShouldNotBeFound("className.doesNotContain=" + DEFAULT_CLASS_NAME);

        // Get all the eventContentList where className does not contain UPDATED_CLASS_NAME
        defaultEventContentShouldBeFound("className.doesNotContain=" + UPDATED_CLASS_NAME);
    }

    @Test
    void getAllEventContentsByCreatedByIsEqualToSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where createdBy equals to DEFAULT_CREATED_BY
        defaultEventContentShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the eventContentList where createdBy equals to UPDATED_CREATED_BY
        defaultEventContentShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    void getAllEventContentsByCreatedByIsInShouldWork() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultEventContentShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the eventContentList where createdBy equals to UPDATED_CREATED_BY
        defaultEventContentShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    void getAllEventContentsByCreatedByIsNullOrNotNull() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where createdBy is not null
        defaultEventContentShouldBeFound("createdBy.specified=true");

        // Get all the eventContentList where createdBy is null
        defaultEventContentShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    void getAllEventContentsByCreatedByContainsSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where createdBy contains DEFAULT_CREATED_BY
        defaultEventContentShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the eventContentList where createdBy contains UPDATED_CREATED_BY
        defaultEventContentShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    void getAllEventContentsByCreatedByNotContainsSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where createdBy does not contain DEFAULT_CREATED_BY
        defaultEventContentShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the eventContentList where createdBy does not contain UPDATED_CREATED_BY
        defaultEventContentShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    void getAllEventContentsByCreatedDateIsEqualToSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where createdDate equals to DEFAULT_CREATED_DATE
        defaultEventContentShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the eventContentList where createdDate equals to UPDATED_CREATED_DATE
        defaultEventContentShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    void getAllEventContentsByCreatedDateIsInShouldWork() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultEventContentShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the eventContentList where createdDate equals to UPDATED_CREATED_DATE
        defaultEventContentShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    void getAllEventContentsByCreatedDateIsNullOrNotNull() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where createdDate is not null
        defaultEventContentShouldBeFound("createdDate.specified=true");

        // Get all the eventContentList where createdDate is null
        defaultEventContentShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    void getAllEventContentsByLastModifiedByIsEqualToSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultEventContentShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the eventContentList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultEventContentShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void getAllEventContentsByLastModifiedByIsInShouldWork() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultEventContentShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the eventContentList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultEventContentShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void getAllEventContentsByLastModifiedByIsNullOrNotNull() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where lastModifiedBy is not null
        defaultEventContentShouldBeFound("lastModifiedBy.specified=true");

        // Get all the eventContentList where lastModifiedBy is null
        defaultEventContentShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    void getAllEventContentsByLastModifiedByContainsSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultEventContentShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the eventContentList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultEventContentShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void getAllEventContentsByLastModifiedByNotContainsSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultEventContentShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the eventContentList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultEventContentShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void getAllEventContentsByLastModifiedDateIsEqualToSomething() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultEventContentShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the eventContentList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultEventContentShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    void getAllEventContentsByLastModifiedDateIsInShouldWork() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultEventContentShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the eventContentList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultEventContentShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    void getAllEventContentsByLastModifiedDateIsNullOrNotNull() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        // Get all the eventContentList where lastModifiedDate is not null
        defaultEventContentShouldBeFound("lastModifiedDate.specified=true");

        // Get all the eventContentList where lastModifiedDate is null
        defaultEventContentShouldNotBeFound("lastModifiedDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventContentShouldBeFound(String filter) {
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
            .value(hasItem(eventContent.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].className")
            .value(hasItem(DEFAULT_CLASS_NAME))
            .jsonPath("$.[*].metadata")
            .value(hasItem(DEFAULT_METADATA.toString()))
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
    private void defaultEventContentShouldNotBeFound(String filter) {
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
    void getNonExistingEventContent() {
        // Get the eventContent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEventContent() throws Exception {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        int databaseSizeBeforeUpdate = eventContentRepository.findAll().collectList().block().size();

        // Update the eventContent
        EventContent updatedEventContent = eventContentRepository.findById(eventContent.getId()).block();
        updatedEventContent
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .className(UPDATED_CLASS_NAME)
            .metadata(UPDATED_METADATA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        EventContentDTO eventContentDTO = eventContentMapper.toDto(updatedEventContent);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, eventContentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventContentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EventContent in the database
        List<EventContent> eventContentList = eventContentRepository.findAll().collectList().block();
        assertThat(eventContentList).hasSize(databaseSizeBeforeUpdate);
        EventContent testEventContent = eventContentList.get(eventContentList.size() - 1);
        assertThat(testEventContent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventContent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventContent.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testEventContent.getMetadata()).isEqualTo(UPDATED_METADATA);
        assertThat(testEventContent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEventContent.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEventContent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testEventContent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    void putNonExistingEventContent() throws Exception {
        int databaseSizeBeforeUpdate = eventContentRepository.findAll().collectList().block().size();
        eventContent.setId(count.incrementAndGet());

        // Create the EventContent
        EventContentDTO eventContentDTO = eventContentMapper.toDto(eventContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, eventContentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EventContent in the database
        List<EventContent> eventContentList = eventContentRepository.findAll().collectList().block();
        assertThat(eventContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEventContent() throws Exception {
        int databaseSizeBeforeUpdate = eventContentRepository.findAll().collectList().block().size();
        eventContent.setId(count.incrementAndGet());

        // Create the EventContent
        EventContentDTO eventContentDTO = eventContentMapper.toDto(eventContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EventContent in the database
        List<EventContent> eventContentList = eventContentRepository.findAll().collectList().block();
        assertThat(eventContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEventContent() throws Exception {
        int databaseSizeBeforeUpdate = eventContentRepository.findAll().collectList().block().size();
        eventContent.setId(count.incrementAndGet());

        // Create the EventContent
        EventContentDTO eventContentDTO = eventContentMapper.toDto(eventContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventContentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EventContent in the database
        List<EventContent> eventContentList = eventContentRepository.findAll().collectList().block();
        assertThat(eventContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEventContentWithPatch() throws Exception {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        int databaseSizeBeforeUpdate = eventContentRepository.findAll().collectList().block().size();

        // Update the eventContent using partial update
        EventContent partialUpdatedEventContent = new EventContent();
        partialUpdatedEventContent.setId(eventContent.getId());

        partialUpdatedEventContent.title(UPDATED_TITLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEventContent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEventContent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EventContent in the database
        List<EventContent> eventContentList = eventContentRepository.findAll().collectList().block();
        assertThat(eventContentList).hasSize(databaseSizeBeforeUpdate);
        EventContent testEventContent = eventContentList.get(eventContentList.size() - 1);
        assertThat(testEventContent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventContent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventContent.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testEventContent.getMetadata()).isEqualTo(DEFAULT_METADATA);
        assertThat(testEventContent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testEventContent.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testEventContent.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testEventContent.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    void fullUpdateEventContentWithPatch() throws Exception {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        int databaseSizeBeforeUpdate = eventContentRepository.findAll().collectList().block().size();

        // Update the eventContent using partial update
        EventContent partialUpdatedEventContent = new EventContent();
        partialUpdatedEventContent.setId(eventContent.getId());

        partialUpdatedEventContent
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .className(UPDATED_CLASS_NAME)
            .metadata(UPDATED_METADATA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEventContent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEventContent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EventContent in the database
        List<EventContent> eventContentList = eventContentRepository.findAll().collectList().block();
        assertThat(eventContentList).hasSize(databaseSizeBeforeUpdate);
        EventContent testEventContent = eventContentList.get(eventContentList.size() - 1);
        assertThat(testEventContent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventContent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventContent.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testEventContent.getMetadata()).isEqualTo(UPDATED_METADATA);
        assertThat(testEventContent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEventContent.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEventContent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testEventContent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    void patchNonExistingEventContent() throws Exception {
        int databaseSizeBeforeUpdate = eventContentRepository.findAll().collectList().block().size();
        eventContent.setId(count.incrementAndGet());

        // Create the EventContent
        EventContentDTO eventContentDTO = eventContentMapper.toDto(eventContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, eventContentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EventContent in the database
        List<EventContent> eventContentList = eventContentRepository.findAll().collectList().block();
        assertThat(eventContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEventContent() throws Exception {
        int databaseSizeBeforeUpdate = eventContentRepository.findAll().collectList().block().size();
        eventContent.setId(count.incrementAndGet());

        // Create the EventContent
        EventContentDTO eventContentDTO = eventContentMapper.toDto(eventContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EventContent in the database
        List<EventContent> eventContentList = eventContentRepository.findAll().collectList().block();
        assertThat(eventContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEventContent() throws Exception {
        int databaseSizeBeforeUpdate = eventContentRepository.findAll().collectList().block().size();
        eventContent.setId(count.incrementAndGet());

        // Create the EventContent
        EventContentDTO eventContentDTO = eventContentMapper.toDto(eventContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(eventContentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EventContent in the database
        List<EventContent> eventContentList = eventContentRepository.findAll().collectList().block();
        assertThat(eventContentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEventContent() {
        // Initialize the database
        eventContentRepository.save(eventContent).block();

        int databaseSizeBeforeDelete = eventContentRepository.findAll().collectList().block().size();

        // Delete the eventContent
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, eventContent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<EventContent> eventContentList = eventContentRepository.findAll().collectList().block();
        assertThat(eventContentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
