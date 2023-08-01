package com.yukikyu.kook.boot.app.repository;

import com.yukikyu.kook.boot.app.domain.HelpUserLog;
import com.yukikyu.kook.boot.app.domain.criteria.HelpUserLogCriteria;
import java.time.Instant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the HelpUserLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpUserLogRepository extends ReactiveCrudRepository<HelpUserLog, Long>, HelpUserLogRepositoryInternal {
    @Override
    <S extends HelpUserLog> Mono<S> save(S entity);

    @Override
    Flux<HelpUserLog> findAll();

    @Override
    Mono<HelpUserLog> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);

    Flux<HelpUserLog> findByChannelIdAndStatus(String channelId, String name);

    @Modifying
    @Query(
        "update kb_help_user_log hul set hul.status = :updateStatus, hul.exit_at = :exitAt where hul.help_user_id = :helpUserId and hul.status = :status"
    )
    Mono<Void> updateStatusAndExitAtByHelpUserIdAndStatus(
        @Param("updateStatus") String updateStatus,
        @Param("exitAt") Instant exitAt,
        @Param("helpUserId") String helpUserId,
        @Param("status") String status
    );

    Flux<HelpUserLog> findByStatus(String status);
}

interface HelpUserLogRepositoryInternal {
    <S extends HelpUserLog> Mono<S> save(S entity);

    Flux<HelpUserLog> findAllBy(Pageable pageable);

    Flux<HelpUserLog> findAll();

    Mono<HelpUserLog> findById(Long id);

    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<HelpUserLog> findAllBy(Pageable pageable, Criteria criteria);
    Flux<HelpUserLog> findByCriteria(HelpUserLogCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(HelpUserLogCriteria criteria);
}
