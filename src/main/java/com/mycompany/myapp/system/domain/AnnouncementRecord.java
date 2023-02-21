package com.mycompany.myapp.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.mycompany.myapp.domain.AbstractAuditingEntity;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * 通告阅读记录
 */

@TableName(value = "announcement_record")
public class AnnouncementRecord extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 通告ID
     */
    @TableField(value = "annt_id")
    private Long anntId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 是否已读
     */
    @TableField(value = "has_read")
    private Boolean hasRead;

    /**
     * 阅读时间
     */
    @TableField(value = "read_time")
    private ZonedDateTime readTime;

    /**
     * 软删除时间
     */
    @TableField(value = "removed_at")
    private LocalDate removedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnnouncementRecord id(Long id) {
        this.id = id;
        return this;
    }

    public Long getAnntId() {
        return this.anntId;
    }

    public AnnouncementRecord anntId(Long anntId) {
        this.anntId = anntId;
        return this;
    }

    public void setAnntId(Long anntId) {
        this.anntId = anntId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public AnnouncementRecord userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getHasRead() {
        return this.hasRead;
    }

    public AnnouncementRecord hasRead(Boolean hasRead) {
        this.hasRead = hasRead;
        return this;
    }

    public void setHasRead(Boolean hasRead) {
        this.hasRead = hasRead;
    }

    public ZonedDateTime getReadTime() {
        return this.readTime;
    }

    public AnnouncementRecord readTime(ZonedDateTime readTime) {
        this.readTime = readTime;
        return this;
    }

    public void setReadTime(ZonedDateTime readTime) {
        this.readTime = readTime;
    }

    public LocalDate getRemovedAt() {
        return this.removedAt;
    }

    public AnnouncementRecord removedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
        return this;
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnouncementRecord)) {
            return false;
        }
        return id != null && id.equals(((AnnouncementRecord) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnnouncementRecord{" +
            "id=" + getId() +
            ", anntId=" + getAnntId() +
            ", userId=" + getUserId() +
            ", hasRead='" + getHasRead() + "'" +
            ", readTime='" + getReadTime() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
