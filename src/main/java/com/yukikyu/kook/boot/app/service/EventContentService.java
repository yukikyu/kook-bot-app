package com.yukikyu.kook.boot.app.service;

import com.yukikyu.kook.boot.app.domain.EventContent;
import com.yukikyu.kook.boot.app.domain.criteria.EventContentCriteria;
import com.yukikyu.kook.boot.app.repository.EventContentRepository;
import com.yukikyu.kook.boot.app.service.dto.EventContentDTO;
import com.yukikyu.kook.boot.app.service.mapper.EventContentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link EventContent}.
 */
@Service
@Transactional
public class EventContentService {

    private final Logger log = LoggerFactory.getLogger(EventContentService.class);

    private final EventContentRepository eventContentRepository;

    private final EventContentMapper eventContentMapper;

    public EventContentService(EventContentRepository eventContentRepository, EventContentMapper eventContentMapper) {
        this.eventContentRepository = eventContentRepository;
        this.eventContentMapper = eventContentMapper;
    }

    /**
     * Save a eventContent.
     *
     * @param eventContentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EventContentDTO> save(EventContentDTO eventContentDTO) {
        log.debug("Request to save EventContent : {}", eventContentDTO);
        return eventContentRepository.save(eventContentMapper.toEntity(eventContentDTO)).map(eventContentMapper::toDto);
    }

    /**
     * Update a eventContent.
     *
     * @param eventContentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<EventContentDTO> update(EventContentDTO eventContentDTO) {
        log.debug("Request to update EventContent : {}", eventContentDTO);
        return eventContentRepository.save(eventContentMapper.toEntity(eventContentDTO)).map(eventContentMapper::toDto);
    }

    /**
     * Partially update a eventContent.
     *
     * @param eventContentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<EventContentDTO> partialUpdate(EventContentDTO eventContentDTO) {
        log.debug("Request to partially update EventContent : {}", eventContentDTO);

        return eventContentRepository
            .findById(eventContentDTO.getId())
            .map(existingEventContent -> {
                eventContentMapper.partialUpdate(existingEventContent, eventContentDTO);

                return existingEventContent;
            })
            .flatMap(eventContentRepository::save)
            .map(eventContentMapper::toDto);
    }

    /**
     * Get all the eventContents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EventContentDTO> findAll() {
        log.debug("Request to get all EventContents");
        return eventContentRepository.findAll().map(eventContentMapper::toDto);
    }

    /**
     * Find eventContents by Criteria.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<EventContentDTO> findByCriteria(EventContentCriteria criteria) {
        log.debug("Request to get all EventContents by Criteria");
        return eventContentRepository.findByCriteria(criteria, null).map(eventContentMapper::toDto);
    }

    /**
     * Find the count of eventContents by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of eventContents
     */
    public Mono<Long> countByCriteria(EventContentCriteria criteria) {
        log.debug("Request to get the count of all EventContents by Criteria");
        return eventContentRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of eventContents available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return eventContentRepository.count();
    }

    /**
     * Get one eventContent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<EventContentDTO> findOne(Long id) {
        log.debug("Request to get EventContent : {}", id);
        return eventContentRepository.findById(id).map(eventContentMapper::toDto);
    }

    /**
     * Delete the eventContent by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete EventContent : {}", id);
        return eventContentRepository.deleteById(id);
    }
}
