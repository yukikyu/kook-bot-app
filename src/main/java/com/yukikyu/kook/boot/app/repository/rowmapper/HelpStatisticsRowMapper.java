package com.yukikyu.kook.boot.app.repository.rowmapper;

import com.yukikyu.kook.boot.app.domain.HelpStatistics;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HelpStatistics}, with proper type conversions.
 */
@Service
public class HelpStatisticsRowMapper implements BiFunction<Row, String, HelpStatistics> {

    private final ColumnConverter converter;

    public HelpStatisticsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HelpStatistics} stored in the database.
     */
    @Override
    public HelpStatistics apply(Row row, String prefix) {
        HelpStatistics entity = new HelpStatistics();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setGuildId(converter.fromRow(row, prefix + "_guild_id", String.class));
        entity.setHelpUserId(converter.fromRow(row, prefix + "_help_user_id", String.class));
        entity.setDuration(converter.fromRow(row, prefix + "_duration", Integer.class));
        entity.setMonth(converter.fromRow(row, prefix + "_month", Instant.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        return entity;
    }
}
