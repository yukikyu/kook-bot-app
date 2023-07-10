package com.yukikyu.kook.boot.app.repository.rowmapper;

import com.yukikyu.kook.boot.app.domain.HelpUserLog;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HelpUserLog}, with proper type conversions.
 */
@Service
public class HelpUserLogRowMapper implements BiFunction<Row, String, HelpUserLog> {

    private final ColumnConverter converter;

    public HelpUserLogRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HelpUserLog} stored in the database.
     */
    @Override
    public HelpUserLog apply(Row row, String prefix) {
        HelpUserLog entity = new HelpUserLog();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setGuildId(converter.fromRow(row, prefix + "_guild_id", String.class));
        entity.setChannelId(converter.fromRow(row, prefix + "_channel_id", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", String.class));
        entity.setHelpUserId(converter.fromRow(row, prefix + "_help_user_id", String.class));
        entity.setJoinAt(converter.fromRow(row, prefix + "_join_at", Instant.class));
        entity.setExitAt(converter.fromRow(row, prefix + "_exit_at", Instant.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", String.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        return entity;
    }
}
