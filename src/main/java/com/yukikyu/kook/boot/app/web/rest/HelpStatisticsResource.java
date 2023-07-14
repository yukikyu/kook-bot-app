package com.yukikyu.kook.boot.app.web.rest;

import com.yukikyu.kook.boot.app.domain.criteria.HelpStatisticsCriteria;
import com.yukikyu.kook.boot.app.repository.HelpStatisticsRepository;
import com.yukikyu.kook.boot.app.service.HelpStatisticsService;
import com.yukikyu.kook.boot.app.service.dto.HelpStatisticsDTO;
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
 * REST controller for managing {@link com.yukikyu.kook.boot.app.domain.HelpStatistics}.
 */
@RestController
@RequestMapping("/api")
public class HelpStatisticsResource {

    private final Logger log = LoggerFactory.getLogger(HelpStatisticsResource.class);

    private static final String ENTITY_NAME = "kookBotAppHelpStatistics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HelpStatisticsService helpStatisticsService;

    private final HelpStatisticsRepository helpStatisticsRepository;

    public HelpStatisticsResource(HelpStatisticsService helpStatisticsService, HelpStatisticsRepository helpStatisticsRepository) {
        this.helpStatisticsService = helpStatisticsService;
        this.helpStatisticsRepository = helpStatisticsRepository;
    }

    /**
     * {@code POST  /help-statistics} : Create a new helpStatistics.
     *
     * @param helpStatisticsDTO the helpStatisticsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new helpStatisticsDTO, or with status {@code 400 (Bad Request)} if the helpStatistics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/help-statistics")
    public Mono<ResponseEntity<HelpStatisticsDTO>> createHelpStatistics(@RequestBody HelpStatisticsDTO helpStatisticsDTO)
        throws URISyntaxException {
        log.debug("REST request to save HelpStatistics : {}", helpStatisticsDTO);
        if (helpStatisticsDTO.getId() != null) {
            throw new BadRequestAlertException("A new helpStatistics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return helpStatisticsService
            .save(helpStatisticsDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/help-statistics/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /help-statistics/:id} : Updates an existing helpStatistics.
     *
     * @param id the id of the helpStatisticsDTO to save.
     * @param helpStatisticsDTO the helpStatisticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpStatisticsDTO,
     * or with status {@code 400 (Bad Request)} if the helpStatisticsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the helpStatisticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/help-statistics/{id}")
    public Mono<ResponseEntity<HelpStatisticsDTO>> updateHelpStatistics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HelpStatisticsDTO helpStatisticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HelpStatistics : {}, {}", id, helpStatisticsDTO);
        if (helpStatisticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpStatisticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return helpStatisticsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return helpStatisticsService
                    .update(helpStatisticsDTO)
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
     * {@code PATCH  /help-statistics/:id} : Partial updates given fields of an existing helpStatistics, field will ignore if it is null
     *
     * @param id the id of the helpStatisticsDTO to save.
     * @param helpStatisticsDTO the helpStatisticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpStatisticsDTO,
     * or with status {@code 400 (Bad Request)} if the helpStatisticsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the helpStatisticsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the helpStatisticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/help-statistics/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HelpStatisticsDTO>> partialUpdateHelpStatistics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HelpStatisticsDTO helpStatisticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HelpStatistics partially : {}, {}", id, helpStatisticsDTO);
        if (helpStatisticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpStatisticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return helpStatisticsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HelpStatisticsDTO> result = helpStatisticsService.partialUpdate(helpStatisticsDTO);

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
     * {@code GET  /help-statistics} : get all the helpStatistics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of helpStatistics in body.
     */
    @GetMapping(value = "/help-statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<HelpStatisticsDTO> getAllHelpStatistics(HelpStatisticsCriteria criteria) {
        log.debug("REST request to get HelpStatistics by criteria: {}", criteria);
        return helpStatisticsService.findByCriteria(criteria);
    }

    /**
     * {@code GET  /help-statistics/count} : count all the helpStatistics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/help-statistics/count")
    public Mono<ResponseEntity<Long>> countHelpStatistics(HelpStatisticsCriteria criteria) {
        log.debug("REST request to count HelpStatistics by criteria: {}", criteria);
        return helpStatisticsService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /help-statistics/:id} : get the "id" helpStatistics.
     *
     * @param id the id of the helpStatisticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the helpStatisticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/help-statistics/{id}")
    public Mono<ResponseEntity<HelpStatisticsDTO>> getHelpStatistics(@PathVariable Long id) {
        log.debug("REST request to get HelpStatistics : {}", id);
        Mono<HelpStatisticsDTO> helpStatisticsDTO = helpStatisticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(helpStatisticsDTO);
    }

    /**
     * {@code DELETE  /help-statistics/:id} : delete the "id" helpStatistics.
     *
     * @param id the id of the helpStatisticsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/help-statistics/{id}")
    public Mono<ResponseEntity<Void>> deleteHelpStatistics(@PathVariable Long id) {
        log.debug("REST request to delete HelpStatistics : {}", id);
        return helpStatisticsService
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
