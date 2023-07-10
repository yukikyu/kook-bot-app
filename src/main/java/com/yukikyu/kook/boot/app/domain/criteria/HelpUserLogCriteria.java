package com.yukikyu.kook.boot.app.domain.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.yukikyu.kook.boot.app.domain.HelpUserLog} entity. This class is used
 * in {@link com.yukikyu.kook.boot.app.web.rest.HelpUserLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /help-user-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpUserLogCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter guildId;

    private StringFilter channelId;

    private StringFilter userId;

    private StringFilter helpUserId;

    private InstantFilter joinAt;

    private InstantFilter exitAt;

    private StringFilter status;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public HelpUserLogCriteria() {}

    public HelpUserLogCriteria(HelpUserLogCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.helpUserId = other.helpUserId == null ? null : other.helpUserId.copy();
        this.joinAt = other.joinAt == null ? null : other.joinAt.copy();
        this.exitAt = other.exitAt == null ? null : other.exitAt.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.distinct = other.distinct;
    }

    @Override
    public HelpUserLogCriteria copy() {
        return new HelpUserLogCriteria(this);
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

    public StringFilter getChannelId() {
        return channelId;
    }

    public StringFilter channelId() {
        if (channelId == null) {
            channelId = new StringFilter();
        }
        return channelId;
    }

    public void setChannelId(StringFilter channelId) {
        this.channelId = channelId;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public StringFilter userId() {
        if (userId == null) {
            userId = new StringFilter();
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
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

    public InstantFilter getJoinAt() {
        return joinAt;
    }

    public InstantFilter joinAt() {
        if (joinAt == null) {
            joinAt = new InstantFilter();
        }
        return joinAt;
    }

    public void setJoinAt(InstantFilter joinAt) {
        this.joinAt = joinAt;
    }

    public InstantFilter getExitAt() {
        return exitAt;
    }

    public InstantFilter exitAt() {
        if (exitAt == null) {
            exitAt = new InstantFilter();
        }
        return exitAt;
    }

    public void setExitAt(InstantFilter exitAt) {
        this.exitAt = exitAt;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
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
        final HelpUserLogCriteria that = (HelpUserLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(channelId, that.channelId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(helpUserId, that.helpUserId) &&
            Objects.equals(joinAt, that.joinAt) &&
            Objects.equals(exitAt, that.exitAt) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            guildId,
            channelId,
            userId,
            helpUserId,
            joinAt,
            exitAt,
            status,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpUserLogCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (guildId != null ? "guildId=" + guildId + ", " : "") +
            (channelId != null ? "channelId=" + channelId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (helpUserId != null ? "helpUserId=" + helpUserId + ", " : "") +
            (joinAt != null ? "joinAt=" + joinAt + ", " : "") +
            (exitAt != null ? "exitAt=" + exitAt + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
