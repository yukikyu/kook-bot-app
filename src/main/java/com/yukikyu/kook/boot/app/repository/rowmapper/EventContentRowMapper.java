package com.yukikyu.kook.boot.app.repository.rowmapper;

import com.yukikyu.kook.boot.app.domain.EventContent;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link EventContent}, with proper type conversions.
 */
@Service
public class EventContentRowMapper implements BiFunction<Row, String, EventContent> {

    private final ColumnConverter converter;

    public EventContentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link EventContent} stored in the database.
     */
    @Override
    public EventContent apply(Row row, String prefix) {
        EventContent entity = new EventContent();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setClassName(converter.fromRow(row, prefix + "_class_name", String.class));
        entity.setMetadata(converter.fromRow(row, prefix + "_metadata", String.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", Instant.class));
        entity.setLastModifiedBy(converter.fromRow(row, prefix + "_last_modified_by", String.class));
        entity.setLastModifiedDate(converter.fromRow(row, prefix + "_last_modified_date", Instant.class));
        return entity;
    }
}
