package com.yukikyu.kook.boot.app.service;

import com.yukikyu.kook.boot.app.domain.HelpUserLog;
import com.yukikyu.kook.boot.app.domain.criteria.HelpUserLogCriteria;
import com.yukikyu.kook.boot.app.repository.HelpUserLogRepository;
import com.yukikyu.kook.boot.app.service.dto.HelpUserLogDTO;
import com.yukikyu.kook.boot.app.service.mapper.HelpUserLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link HelpUserLog}.
 */
@Service
@Transactional
public class HelpUserLogService {

    private final Logger log = LoggerFactory.getLogger(HelpUserLogService.class);

    private final HelpUserLogRepository helpUserLogRepository;

    private final HelpUserLogMapper helpUserLogMapper;

    public HelpUserLogService(HelpUserLogRepository helpUserLogRepository, HelpUserLogMapper helpUserLogMapper) {
        this.helpUserLogRepository = helpUserLogRepository;
        this.helpUserLogMapper = helpUserLogMapper;
    }

    /**
     * Save a helpUserLog.
     *
     * @param helpUserLogDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<HelpUserLogDTO> save(HelpUserLogDTO helpUserLogDTO) {
        log.debug("Request to save HelpUserLog : {}", helpUserLogDTO);
        return helpUserLogRepository.save(helpUserLogMapper.toEntity(helpUserLogDTO)).map(helpUserLogMapper::toDto);
    }

    /**
     * Update a helpUserLog.
     *
     * @param helpUserLogDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<HelpUserLogDTO> update(HelpUserLogDTO helpUserLogDTO) {
        log.debug("Request to update HelpUserLog : {}", helpUserLogDTO);
        return helpUserLogRepository.save(helpUserLogMapper.toEntity(helpUserLogDTO)).map(helpUserLogMapper::toDto);
    }

    /**
     * Partially update a helpUserLog.
     *
     * @param helpUserLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<HelpUserLogDTO> partialUpdate(HelpUserLogDTO helpUserLogDTO) {
        log.debug("Request to partially update HelpUserLog : {}", helpUserLogDTO);

        return helpUserLogRepository
            .findById(helpUserLogDTO.getId())
            .map(existingHelpUserLog -> {
                helpUserLogMapper.partialUpdate(existingHelpUserLog, helpUserLogDTO);

                return existingHelpUserLog;
            })
            .flatMap(helpUserLogRepository::save)
            .map(helpUserLogMapper::toDto);
    }

    /**
     * Get all the helpUserLogs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<HelpUserLogDTO> findAll() {
        log.debug("Request to get all HelpUserLogs");
        return helpUserLogRepository.findAll().map(helpUserLogMapper::toDto);
    }

    /**
     * Find helpUserLogs by Criteria.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<HelpUserLogDTO> findByCriteria(HelpUserLogCriteria criteria) {
        log.debug("Request to get all HelpUserLogs by Criteria");
        return helpUserLogRepository.findByCriteria(criteria, Pageable.unpaged()).map(helpUserLogMapper::toDto);
    }

    /**
     * Find the count of helpUserLogs by criteria.
     *
     * @param criteria filtering criteria
     * @return the count of helpUserLogs
     */
    public Mono<Long> countByCriteria(HelpUserLogCriteria criteria) {
        log.debug("Request to get the count of all HelpUserLogs by Criteria");
        return helpUserLogRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of helpUserLogs available.
     *
     * @return the number of entities in the database.
     */
    public Mono<Long> countAll() {
        return helpUserLogRepository.count();
    }

    /**
     * Get one helpUserLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<HelpUserLogDTO> findOne(Long id) {
        log.debug("Request to get HelpUserLog : {}", id);
        return helpUserLogRepository.findById(id).map(helpUserLogMapper::toDto);
    }

    /**
     * Delete the helpUserLog by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete HelpUserLog : {}", id);
        return helpUserLogRepository.deleteById(id);
    }
}
