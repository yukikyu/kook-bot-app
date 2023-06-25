package com.yukikyu.kook.boot.app.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 事件内容
 */
@Table("kba_event_content")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventContent extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    /**
     * 事件名
     */
    @Column("name")
    private String name;

    /**
     * 事件标题
     */
    @Column("title")
    private String title;

    /**
     * 类名
     */
    @Column("class_name")
    private String className;

    /**
     * 元数据
     */
    @Column("metadata")
    private String metadata;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventContent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public EventContent name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public EventContent title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClassName() {
        return this.className;
    }

    public EventContent className(String className) {
        this.setClassName(className);
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public EventContent metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public EventContent createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public EventContent createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public EventContent lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public EventContent lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventContent)) {
            return false;
        }
        return id != null && id.equals(((EventContent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventContent{" + "id=" + getId() + ", name='" + getName() + "'" + ", title='" + getTitle() + "'" + ", className='" + getClassName() + "'" + ", metadata='" + getMetadata() + "'" + ", createdBy='" + getCreatedBy() + "'" + ", createdDate='" + getCreatedDate() + "'" + ", lastModifiedBy='" + getLastModifiedBy() + "'" + ", lastModifiedDate='" + getLastModifiedDate() + "'" + "}";
    }
}
