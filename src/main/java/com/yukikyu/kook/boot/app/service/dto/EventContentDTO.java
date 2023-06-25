package com.yukikyu.kook.boot.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.yukikyu.kook.boot.app.domain.EventContent} entity.
 */
@Schema(description = "事件内容")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventContentDTO implements Serializable {

    private Long id;

    /**
     * 事件名
     */
    @Schema(description = "事件名")
    private String name;

    /**
     * 事件标题
     */
    @Schema(description = "事件标题")
    private String title;

    /**
     * 类名
     */
    @Schema(description = "类名")
    private String className;

    /**
     * 元数据
     */
    @Schema(description = "元数据")
    @Lob
    private String metadata;

    /**
     * 创建用户
     */
    @Schema(description = "创建用户")
    private String createdBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Instant createdDate;

    /**
     * 最后修改用户
     */
    @Schema(description = "最后修改用户")
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    @Schema(description = "最后修改时间")
    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
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
        if (!(o instanceof EventContentDTO)) {
            return false;
        }

        EventContentDTO eventContentDTO = (EventContentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventContentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventContentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", title='" + getTitle() + "'" +
            ", className='" + getClassName() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
