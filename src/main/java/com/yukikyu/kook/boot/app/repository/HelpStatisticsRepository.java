package com.yukikyu.kook.boot.app.repository;

import com.yukikyu.kook.boot.app.domain.HelpStatistics;
import com.yukikyu.kook.boot.app.domain.criteria.HelpStatisticsCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the HelpStatistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpStatisticsRepository extends ReactiveCrudRepository<HelpStatistics, Long>, HelpStatisticsRepositoryInternal {
    @Override
    <S extends HelpStatistics> Mono<S> save(S entity);

    @Override
    Flux<HelpStatistics> findAll();

    @Override
    Mono<HelpStatistics> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);

    @Query(
        "SELECT s.help_user_id, SUM(s.duration) AS duration   " +
        "FROM kba_help_statistics AS s WHERE s.guild_id = :guildId GROUP BY s.help_user_id ORDER BY duration DESC"
    )
    Flux<HelpStatistics> findTotalDurationByGuildIdGroupByHelpUserId(@Param("guildId") String guildId);
}

interface HelpStatisticsRepositoryInternal {
    <S extends HelpStatistics> Mono<S> save(S entity);

    Flux<HelpStatistics> findAllBy(Pageable pageable);

    Flux<HelpStatistics> findAll();

    Mono<HelpStatistics> findById(Long id);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<HelpStatistics> findAllBy(Pageable pageable, Criteria criteria);
    Flux<HelpStatistics> findByCriteria(HelpStatisticsCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(HelpStatisticsCriteria criteria);
}
