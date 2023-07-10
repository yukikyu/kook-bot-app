package com.yukikyu.kook.boot.app.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A HelpUserLog.
 */
@Table("kb_help_user_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpUserLog extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("guild_id")
    private String guildId;

    @Column("channel_id")
    private String channelId;

    @Column("user_id")
    private String userId;

    @Column("help_user_id")
    private String helpUserId;

    @Column("join_at")
    private Instant joinAt;

    @Column("exit_at")
    private Instant exitAt;

    @Column("status")
    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HelpUserLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuildId() {
        return this.guildId;
    }

    public HelpUserLog guildId(String guildId) {
        this.setGuildId(guildId);
        return this;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public HelpUserLog channelId(String channelId) {
        this.setChannelId(channelId);
        return this;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserId() {
        return this.userId;
    }

    public HelpUserLog userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHelpUserId() {
        return this.helpUserId;
    }

    public HelpUserLog helpUserId(String helpUserId) {
        this.setHelpUserId(helpUserId);
        return this;
    }

    public void setHelpUserId(String helpUserId) {
        this.helpUserId = helpUserId;
    }

    public Instant getJoinAt() {
        return this.joinAt;
    }

    public HelpUserLog joinAt(Instant joinAt) {
        this.setJoinAt(joinAt);
        return this;
    }

    public void setJoinAt(Instant joinAt) {
        this.joinAt = joinAt;
    }

    public Instant getExitAt() {
        return this.exitAt;
    }

    public HelpUserLog exitAt(Instant exitAt) {
        this.setExitAt(exitAt);
        return this;
    }

    public void setExitAt(Instant exitAt) {
        this.exitAt = exitAt;
    }

    public String getStatus() {
        return this.status;
    }

    public HelpUserLog status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HelpUserLog createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public HelpUserLog createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public HelpUserLog lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public HelpUserLog lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HelpUserLog)) {
            return false;
        }
        return id != null && id.equals(((HelpUserLog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpUserLog{" +
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
