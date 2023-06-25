package com.yukikyu.kook.boot.app.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.yukikyu.kook.boot.app.domain.EventContent;
import com.yukikyu.kook.boot.app.domain.criteria.EventContentCriteria;
import com.yukikyu.kook.boot.app.repository.rowmapper.ColumnConverter;
import com.yukikyu.kook.boot.app.repository.rowmapper.EventContentRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC custom repository implementation for the EventContent entity.
 */
@SuppressWarnings("unused")
class EventContentRepositoryInternalImpl extends SimpleR2dbcRepository<EventContent, Long> implements EventContentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EventContentRowMapper eventcontentMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("kba_event_content", EntityManager.ENTITY_ALIAS);

    public EventContentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EventContentRowMapper eventcontentMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(EventContent.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.eventcontentMapper = eventcontentMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<EventContent> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<EventContent> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = EventContentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, EventContent.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<EventContent> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<EventContent> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private EventContent process(Row row, RowMetadata metadata) {
        EventContent entity = eventcontentMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends EventContent> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<EventContent> findByCriteria(EventContentCriteria eventContentCriteria, Pageable page) {
        return createQuery(page, buildConditions(eventContentCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(EventContentCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(EventContentCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getName() != null) {
                builder.buildFilterConditionForField(criteria.getName(), entityTable.column("name"));
            }
            if (criteria.getTitle() != null) {
                builder.buildFilterConditionForField(criteria.getTitle(), entityTable.column("title"));
            }
            if (criteria.getClassName() != null) {
                builder.buildFilterConditionForField(criteria.getClassName(), entityTable.column("class_name"));
            }
            if (criteria.getCreatedBy() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedBy(), entityTable.column("created_by"));
            }
            if (criteria.getCreatedDate() != null) {
                builder.buildFilterConditionForField(criteria.getCreatedDate(), entityTable.column("created_date"));
            }
            if (criteria.getLastModifiedBy() != null) {
                builder.buildFilterConditionForField(criteria.getLastModifiedBy(), entityTable.column("last_modified_by"));
            }
            if (criteria.getLastModifiedDate() != null) {
                builder.buildFilterConditionForField(criteria.getLastModifiedDate(), entityTable.column("last_modified_date"));
            }
        }
        return builder.buildConditions();
    }
}
