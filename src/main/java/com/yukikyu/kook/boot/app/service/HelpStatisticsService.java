package com.yukikyu.kook.boot.app.service;

import com.yukikyu.kook.boot.app.domain.HelpStatistics;
import com.yukikyu.kook.boot.app.domain.criteria.HelpStatisticsCriteria;
import com.yukikyu.kook.boot.app.repository.HelpStatisticsRepository;
import com.yukikyu.kook.boot.app.service.dto.HelpStatisticsDTO;
import com.yukikyu.kook.boot.app.service.mapper.HelpStatisticsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link HelpStatistics}.
 */
@Service
@Transactional
public class HelpStatisticsService {

    private final Logger log = LoggerFactory.getLogger(HelpStatisticsService.class);

    private final HelpStatisticsRepository helpStatisticsRepository;

    private final HelpStatisticsMapper helpStatisticsMapper;

    public HelpStatisticsService(HelpStatisticsRepository helpStatisticsRepository, HelpStatisticsMapper helpStatisticsMapper) {
        this.helpStatisticsRepository = helpStatisticsRepository;
        this.helpStatisticsMapper = helpStatisticsMapper;
    }

    /**
     * Save a helpStatistics.
     *
     * @param helpStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<HelpStatisticsDTO> save(HelpStatisticsDTO helpStatisticsDTO) {
        log.debug("Request to save HelpStatistics : {}", helpStatisticsDTO);
        return helpStatisticsRepository.save(helpStatisticsMapper.toEntity(helpStatisticsDTO)).map(helpStatisticsMapper::toDto);
    }

    /**
     * Update a helpStatistics.
     *
     * @param helpStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<HelpStatisticsDTO> update(HelpStatisticsDTO helpStatisticsDTO) {
        log.debug("Request to update HelpStatistics : {}", helpStatisticsDTO);
        return helpStatisticsRepository.save(helpStatisticsMapper.toEntity(helpStatisticsDTO)).map(helpStatisticsMapper::toDto);
    }

    /**
     * Partially update a helpStatistics.
     *
     * @param helpStatisticsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<HelpStatisticsDTO> partialUpdate(HelpStatisticsDTO helpStatisticsDTO) {
        log.debug("Request to partially update HelpStatistics : {}", helpStatisticsDTO);

        return helpStatisticsRepository
            .findById(helpStatisticsDTO.getId())
            .map(existingHelpStatistics -> {
                helpStatisticsMapper.partialUpdate(existingHelpStatistics, helpStatisticsDTO);

                return existingHelpStatistics;
            })
            .flatMap(helpStatisticsRepository::save)
            .map(helpStatisticsMapper::toDto);
    }

    /**
     * Get all the helpStatistics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<HelpStatisticsDTO> findAll() {
        log.debug("Request to get all HelpStatistics");
        return helpStatisticsRepository.findAll().map(helpStatisticsMapper::toDto);
    }

    /**
     * Find helpStatistics by Criteria.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<HelpStatisticsDTO> findByCriteria(HelpStatisticsCriteria criteria) {
        log.debug("Request to get all HelpStatistics by Criteria");
        return helpStatisticsRepository.findByCriteria(criteria, null).map(helpStatisticsMapper::toDto);
    }

    /**
     * Find the count of helpStatistics by criteria.
     * @param criteria filtering criteria
     * @return the count of helpStatistics
     */
    public Mono<Long> countByCriteria(HelpStatisticsCriteria criteria) {
        log.debug("Request to get the count of all HelpStatistics by Criteria");
        return helpStatisticsRepository.countByCriteria(criteria);
    }

    /**
     * Returns the number of helpStatistics available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return helpStatisticsRepository.count();
    }

    /**
     * Get one helpStatistics by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<HelpStatisticsDTO> findOne(Long id) {
        log.debug("Request to get HelpStatistics : {}", id);
        return helpStatisticsRepository.findById(id).map(helpStatisticsMapper::toDto);
    }

    /**
     * Delete the helpStatistics by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete HelpStatistics : {}", id);
        return helpStatisticsRepository.deleteById(id);
    }
}
