package com.yukikyu.kook.boot.app.domain;

import java.io.Serializable;
import java.time.Instant;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A HelpStatistics.
 */
@Table("kba_help_statistics")
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpStatistics extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("guild_id")
    private String guildId;

    @Column("help_user_id")
    private String helpUserId;

    @Column("duration")
    private Integer duration;

    @Column("month")
    private Instant month;

    public HelpStatistics(String helpUserId, Integer duration) {
        this.helpUserId = helpUserId;
        this.duration = duration;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HelpStatistics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuildId() {
        return this.guildId;
    }

    public HelpStatistics guildId(String guildId) {
        this.setGuildId(guildId);
        return this;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getHelpUserId() {
        return this.helpUserId;
    }

    public HelpStatistics helpUserId(String helpUserId) {
        this.setHelpUserId(helpUserId);
        return this;
    }

    public void setHelpUserId(String helpUserId) {
        this.helpUserId = helpUserId;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public HelpStatistics duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Instant getMonth() {
        return this.month;
    }

    public HelpStatistics month(Instant month) {
        this.setMonth(month);
        return this;
    }

    public void setMonth(Instant month) {
        this.month = month;
    }

    public HelpStatistics createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public HelpStatistics createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public HelpStatistics lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public HelpStatistics lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HelpStatistics)) {
            return false;
        }
        return id != null && id.equals(((HelpStatistics) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpStatistics{" +
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
