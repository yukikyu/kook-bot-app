package com.yukikyu.kook.boot.app.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.yukikyu.kook.boot.app.domain.HelpStatistics;
import com.yukikyu.kook.boot.app.domain.criteria.HelpStatisticsCriteria;
import com.yukikyu.kook.boot.app.repository.rowmapper.ColumnConverter;
import com.yukikyu.kook.boot.app.repository.rowmapper.HelpStatisticsRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the HelpStatistics entity.
 */
@SuppressWarnings("unused")
class HelpStatisticsRepositoryInternalImpl extends SimpleR2dbcRepository<HelpStatistics, Long> implements HelpStatisticsRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final HelpStatisticsRowMapper helpstatisticsMapper;
    private final ColumnConverter columnConverter;

    private static final Table entityTable = Table.aliased("kba_help_statistics", EntityManager.ENTITY_ALIAS);

    public HelpStatisticsRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        HelpStatisticsRowMapper helpstatisticsMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter,
        ColumnConverter columnConverter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(HelpStatistics.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.helpstatisticsMapper = helpstatisticsMapper;
        this.columnConverter = columnConverter;
    }

    @Override
    public Flux<HelpStatistics> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<HelpStatistics> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = HelpStatisticsSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, HelpStatistics.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<HelpStatistics> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<HelpStatistics> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private HelpStatistics process(Row row, RowMetadata metadata) {
        HelpStatistics entity = helpstatisticsMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends HelpStatistics> Mono<S> save(S entity) {
        return super.save(entity);
    }

    @Override
    public Flux<HelpStatistics> findByCriteria(HelpStatisticsCriteria helpStatisticsCriteria, Pageable page) {
        return createQuery(page, buildConditions(helpStatisticsCriteria)).all();
    }

    @Override
    public Mono<Long> countByCriteria(HelpStatisticsCriteria criteria) {
        return findByCriteria(criteria, null)
            .collectList()
            .map(collectedList -> collectedList != null ? (long) collectedList.size() : (long) 0);
    }

    private Condition buildConditions(HelpStatisticsCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        List<Condition> allConditions = new ArrayList<Condition>();
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getGuildId() != null) {
                builder.buildFilterConditionForField(criteria.getGuildId(), entityTable.column("guild_id"));
            }
            if (criteria.getHelpUserId() != null) {
                builder.buildFilterConditionForField(criteria.getHelpUserId(), entityTable.column("help_user_id"));
            }
            if (criteria.getDuration() != null) {
                builder.buildFilterConditionForField(criteria.getDuration(), entityTable.column("duration"));
            }
            if (criteria.getMonth() != null) {
                builder.buildFilterConditionForField(criteria.getMonth(), entityTable.column("month"));
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
