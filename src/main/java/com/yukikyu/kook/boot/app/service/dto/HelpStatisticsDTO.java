package com.yukikyu.kook.boot.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.yukikyu.kook.boot.app.domain.HelpStatistics} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpStatisticsDTO implements Serializable {

    private Long id;

    private String guildId;

    private String helpUserId;

    private Integer duration;

    private Instant month;

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

    public String getHelpUserId() {
        return helpUserId;
    }

    public void setHelpUserId(String helpUserId) {
        this.helpUserId = helpUserId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Instant getMonth() {
        return month;
    }

    public void setMonth(Instant month) {
        this.month = month;
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
        if (!(o instanceof HelpStatisticsDTO)) {
            return false;
        }

        HelpStatisticsDTO helpStatisticsDTO = (HelpStatisticsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, helpStatisticsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpStatisticsDTO{" +
            "id=" + getId() +
            ", guildId='" + getGuildId() + "'" +
            ", helpUserId='" + getHelpUserId() + "'" +
            ", duration=" + getDuration() +
            ", month='" + getMonth() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
