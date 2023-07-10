package com.yukikyu.kook.boot.app.web.rest;

import com.yukikyu.kook.boot.app.domain.criteria.HelpUserLogCriteria;
import com.yukikyu.kook.boot.app.repository.HelpUserLogRepository;
import com.yukikyu.kook.boot.app.service.HelpUserLogService;
import com.yukikyu.kook.boot.app.service.dto.HelpUserLogDTO;
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
 * REST controller for managing {@link com.yukikyu.kook.boot.app.domain.HelpUserLog}.
 */
@RestController
@RequestMapping("/api")
public class HelpUserLogResource {

    private final Logger log = LoggerFactory.getLogger(HelpUserLogResource.class);

    private static final String ENTITY_NAME = "kookBotAppHelpUserLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HelpUserLogService helpUserLogService;

    private final HelpUserLogRepository helpUserLogRepository;

    public HelpUserLogResource(HelpUserLogService helpUserLogService, HelpUserLogRepository helpUserLogRepository) {
        this.helpUserLogService = helpUserLogService;
        this.helpUserLogRepository = helpUserLogRepository;
    }

    /**
     * {@code POST  /help-user-logs} : Create a new helpUserLog.
     *
     * @param helpUserLogDTO the helpUserLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new helpUserLogDTO, or with status {@code 400 (Bad Request)} if the helpUserLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/help-user-logs")
    public Mono<ResponseEntity<HelpUserLogDTO>> createHelpUserLog(@RequestBody HelpUserLogDTO helpUserLogDTO) throws URISyntaxException {
        log.debug("REST request to save HelpUserLog : {}", helpUserLogDTO);
        if (helpUserLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new helpUserLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return helpUserLogService
            .save(helpUserLogDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/help-user-logs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /help-user-logs/:id} : Updates an existing helpUserLog.
     *
     * @param id the id of the helpUserLogDTO to save.
     * @param helpUserLogDTO the helpUserLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpUserLogDTO,
     * or with status {@code 400 (Bad Request)} if the helpUserLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the helpUserLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/help-user-logs/{id}")
    public Mono<ResponseEntity<HelpUserLogDTO>> updateHelpUserLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HelpUserLogDTO helpUserLogDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HelpUserLog : {}, {}", id, helpUserLogDTO);
        if (helpUserLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpUserLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return helpUserLogRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return helpUserLogService
                    .update(helpUserLogDTO)
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
     * {@code PATCH  /help-user-logs/:id} : Partial updates given fields of an existing helpUserLog, field will ignore if it is null
     *
     * @param id the id of the helpUserLogDTO to save.
     * @param helpUserLogDTO the helpUserLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpUserLogDTO,
     * or with status {@code 400 (Bad Request)} if the helpUserLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the helpUserLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the helpUserLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/help-user-logs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HelpUserLogDTO>> partialUpdateHelpUserLog(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HelpUserLogDTO helpUserLogDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HelpUserLog partially : {}, {}", id, helpUserLogDTO);
        if (helpUserLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpUserLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return helpUserLogRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HelpUserLogDTO> result = helpUserLogService.partialUpdate(helpUserLogDTO);

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
     * {@code GET  /help-user-logs} : get all the helpUserLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of helpUserLogs in body.
     */
    @GetMapping(value = "/help-user-logs", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<HelpUserLogDTO> getAllHelpUserLogs(HelpUserLogCriteria criteria) {
        log.debug("REST request to get HelpUserLogs by criteria: {}", criteria);
        return helpUserLogService.findByCriteria(criteria);
    }

    /**
     * {@code GET  /help-user-logs/count} : count all the helpUserLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/help-user-logs/count")
    public Mono<ResponseEntity<Long>> countHelpUserLogs(HelpUserLogCriteria criteria) {
        log.debug("REST request to count HelpUserLogs by criteria: {}", criteria);
        return helpUserLogService.countByCriteria(criteria).map(count -> ResponseEntity.status(HttpStatus.OK).body(count));
    }

    /**
     * {@code GET  /help-user-logs/:id} : get the "id" helpUserLog.
     *
     * @param id the id of the helpUserLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the helpUserLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/help-user-logs/{id}")
    public Mono<ResponseEntity<HelpUserLogDTO>> getHelpUserLog(@PathVariable Long id) {
        log.debug("REST request to get HelpUserLog : {}", id);
        Mono<HelpUserLogDTO> helpUserLogDTO = helpUserLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(helpUserLogDTO);
    }

    /**
     * {@code DELETE  /help-user-logs/:id} : delete the "id" helpUserLog.
     *
     * @param id the id of the helpUserLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/help-user-logs/{id}")
    public Mono<ResponseEntity<Void>> deleteHelpUserLog(@PathVariable Long id) {
        log.debug("REST request to delete HelpUserLog : {}", id);
        return helpUserLogService
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
