package com.mycompany.myapp.system.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.system.domain.AnnouncementRecord}的DTO。
 */
@ApiModel(description = "通告阅读记录")
public class AnnouncementRecordDTO implements Serializable {

    /**
     *
     */
    private Long id;

    /**
     * 通告ID
     */
    @ApiModelProperty(value = "通告ID")
    private Long anntId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 是否已读
     */
    @ApiModelProperty(value = "是否已读")
    private Boolean hasRead;

    /**
     * 阅读时间
     */
    @ApiModelProperty(value = "阅读时间")
    private ZonedDateTime readTime;

    /**
     * 软删除时间
     */
    @ApiModelProperty(value = "软删除时间")
    private LocalDate removedAt;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnntId() {
        return anntId;
    }

    public void setAnntId(Long anntId) {
        this.anntId = anntId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getHasRead() {
        return hasRead;
    }

    public void setHasRead(Boolean hasRead) {
        this.hasRead = hasRead;
    }

    public ZonedDateTime getReadTime() {
        return readTime;
    }

    public void setReadTime(ZonedDateTime readTime) {
        this.readTime = readTime;
    }

    public LocalDate getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnouncementRecordDTO)) {
            return false;
        }

        AnnouncementRecordDTO announcementRecordDTO = (AnnouncementRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, announcementRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnnouncementRecordDTO{" +
            "id=" + getId() +
            ", anntId=" + getAnntId() +
            ", userId=" + getUserId() +
            ", hasRead='" + getHasRead() + "'" +
            ", readTime='" + getReadTime() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
