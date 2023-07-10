package com.yukikyu.kook.boot.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.yukikyu.kook.boot.app.domain.HelpUserLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpUserLogDTO implements Serializable {

    private Long id;

    private String guildId;

    private String channelId;

    private String userId;

    private String helpUserId;

    private Instant joinAt;

    private Instant exitAt;

    private String status;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHelpUserId() {
        return helpUserId;
    }

    public void setHelpUserId(String helpUserId) {
        this.helpUserId = helpUserId;
    }

    public Instant getJoinAt() {
        return joinAt;
    }

    public void setJoinAt(Instant joinAt) {
        this.joinAt = joinAt;
    }

    public Instant getExitAt() {
        return exitAt;
    }

    public void setExitAt(Instant exitAt) {
        this.exitAt = exitAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HelpUserLogDTO)) {
            return false;
        }

        HelpUserLogDTO helpUserLogDTO = (HelpUserLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, helpUserLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpUserLogDTO{" +
            "id=" + getId() +
            ", guildId='" + getGuildId() + "'" +
            ", channelId='" + getChannelId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", helpUserId='" + getHelpUserId() + "'" +
            ", joinAt='" + getJoinAt() + "'" +
            ", exitAt='" + getExitAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
