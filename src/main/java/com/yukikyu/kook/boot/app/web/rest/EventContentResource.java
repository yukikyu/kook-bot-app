package com.yukikyu.kook.boot.app.web.rest;

import com.yukikyu.kook.boot.app.domain.criteria.EventContentCriteria;
import com.yukikyu.kook.boot.app.repository.EventContentRepository;
import com.yukikyu.kook.boot.app.service.EventContentService;
import com.yukikyu.kook.boot.app.service.dto.EventContentDTO;
import com.yukikyu.kook.boot.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.yukikyu.kook.boot.app.domain.EventContent}.
 */
@RestController
@RequestMapping("/api")
public class EventContentResource {

    private final Logger log = LoggerFactory.getLogger(EventContentResource.class);

    private static final String ENTITY_NAME = "kookBotAppEventContent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventContentService eventContentService;

    private final EventContentRepository eventContentRepository;

    public EventContentResource(EventContentService eventContentService, EventContentRepository eventContentRepository) {
        this.eventContentService = eventContentService;
        this.eventContentRepository = eventContentRepository;
    }

    /**
     * {@code POST  /event-contents} : Create a new eventContent.
     *
     * @param eventContentDTO the eventContentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventContentDTO, or with status {@code 400 (Bad Request)} if the eventContent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-contents")
    public Mono<ResponseEntity<EventContentDTO>> createEventContent(@RequestBody EventContentDTO eventContentDTO)
        throws URISyntaxException {
        log.debug("REST request to save EventContent : {}", eventContentDTO);
        if (eventContentDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventContent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return eventContentService
            .save(eventContentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/event-contents/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /event-contents/:id} : Updates an existing eventContent.
     *
     * @param id the id of the eventContentDTO to save.
     * @param eventContentDTO the eventContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventContentDTO,
     * or with status {@code 400 (Bad Request)} if the eventContentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-contents/{id}")
    public Mono<ResponseEntity<EventContentDTO>> updateEventContent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventContentDTO eventContentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EventContent : {}, {}", id, eventContentDTO);
        if (eventContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return eventContentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return eventContentService
                    .update(eventContentDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /event-contents/:id} : Partial updates given fields of an existing eventContent, field will ignore if it is null
     *
     * @param id the id of the eventContentDTO to save.
     * @param eventContentDTO the eventContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventContentDTO,
     * or with status {@code 400 (Bad Request)} if the eventContentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventContentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/event-contents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EventContentDTO>> partialUpdateEventContent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventContentDTO eventContentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventContent partially : {}, {}", id, eventContentDTO);
        if (eventContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return eventContentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EventContentDTO> result = eventContentService.partialUpdate(eventContentDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /event-contents} : get all the eventContents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventContents in body.
     */
    @GetMapping(value = "/event-contents", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<EventContentDTO> getAllEventContents(EventContentCriteria criteria) {
        log.debug("REST request to get EventContents by criteria: {}", criteria);
        return eventContentService.findByCriteria(criteria);
    }

    /**
     * {@code GET  /event-contents/count} : count all the eventContents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/event-contents/count")
    public Mono<ResponseEntity<Long>> countEventContents(EventContentCriteria criteria) {
        log.debug("REST request to count EventContents by criteria: {}", criteria);
        return eventContentService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /event-contents/:id} : get the "id" eventContent.
     *
     * @param id the id of the eventContentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventContentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-contents/{id}")
    public Mono<ResponseEntity<EventContentDTO>> getEventContent(@PathVariable Long id) {
        log.debug("REST request to get EventContent : {}", id);
        Mono<EventContentDTO> eventContentDTO = eventContentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventContentDTO);
    }

    /**
     * {@code DELETE  /event-contents/:id} : delete the "id" eventContent.
     *
     * @param id the id of the eventContentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-contents/{id}")
    public Mono<ResponseEntity<Void>> deleteEventContent(@PathVariable Long id) {
        log.debug("REST request to delete EventContent : {}", id);
        return eventContentService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
