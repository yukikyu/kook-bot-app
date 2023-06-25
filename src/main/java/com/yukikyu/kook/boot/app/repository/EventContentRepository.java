package com.yukikyu.kook.boot.app.repository;

import com.yukikyu.kook.boot.app.domain.EventContent;
import com.yukikyu.kook.boot.app.domain.criteria.EventContentCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the EventContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventContentRepository extends ReactiveCrudRepository<EventContent, Long>, EventContentRepositoryInternal {
    @Override
    <S extends EventContent> Mono<S> save(S entity);

    @Override
    Flux<EventContent> findAll();

    @Override
    Mono<EventContent> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EventContentRepositoryInternal {
    <S extends EventContent> Mono<S> save(S entity);

    Flux<EventContent> findAllBy(Pageable pageable);

    Flux<EventContent> findAll();

    Mono<EventContent> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EventContent> findAllBy(Pageable pageable, Criteria criteria);
    Flux<EventContent> findByCriteria(EventContentCriteria criteria, Pageable pageable);

    Mono<Long> countByCriteria(EventContentCriteria criteria);
}
