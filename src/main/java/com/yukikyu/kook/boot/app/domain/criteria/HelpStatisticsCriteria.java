package com.yukikyu.kook.boot.app.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yukikyu.kook.boot.app.domain.HelpStatistics} entity. This class is used
 * in {@link com.yukikyu.kook.boot.app.web.rest.HelpStatisticsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /help-statistics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpStatisticsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter guildId;

    private StringFilter helpUserId;

    private IntegerFilter duration;

    private InstantFilter month;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public HelpStatisticsCriteria() {}

    public HelpStatisticsCriteria(HelpStatisticsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
        this.helpUserId = other.helpUserId == null ? null : other.helpUserId.copy();
        this.duration = other.duration == null ? null : other.duration.copy();
        this.month = other.month == null ? null : other.month.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public HelpStatisticsCriteria copy() {
        return new HelpStatisticsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getGuildId() {
        return guildId;
    }

    public StringFilter guildId() {
        if (guildId == null) {
            guildId = new StringFilter();
        }
        return guildId;
    }

    public void setGuildId(StringFilter guildId) {
        this.guildId = guildId;
    }

    public StringFilter getHelpUserId() {
        return helpUserId;
    }

    public StringFilter helpUserId() {
        if (helpUserId == null) {
            helpUserId = new StringFilter();
        }
        return helpUserId;
    }

    public void setHelpUserId(StringFilter helpUserId) {
        this.helpUserId = helpUserId;
    }

    public IntegerFilter getDuration() {
        return duration;
    }

    public IntegerFilter duration() {
        if (duration == null) {
            duration = new IntegerFilter();
        }
        return duration;
    }

    public void setDuration(IntegerFilter duration) {
        this.duration = duration;
    }

    public InstantFilter getMonth() {
        return month;
    }

    public InstantFilter month() {
        if (month == null) {
            month = new InstantFilter();
        }
        return month;
    }

    public void setMonth(InstantFilter month) {
        this.month = month;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HelpStatisticsCriteria that = (HelpStatisticsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(helpUserId, that.helpUserId) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(month, that.month) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guildId, helpUserId, duration, month, createdBy, createdDate, lastModifiedBy, lastModifiedDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpStatisticsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (guildId != null ? "guildId=" + guildId + ", " : "") +
            (helpUserId != null ? "helpUserId=" + helpUserId + ", " : "") +
            (duration != null ? "duration=" + duration + ", " : "") +
            (month != null ? "month=" + month + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
