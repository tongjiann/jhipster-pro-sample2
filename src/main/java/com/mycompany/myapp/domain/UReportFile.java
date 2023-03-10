package com.mycompany.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * 报表存储
 */

@TableName(value = "u_report_file")
public class UReportFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 创建时间
     */
    @TableField(value = "create_at")
    private ZonedDateTime createAt;

    /**
     * 更新时间
     */
    @TableField(value = "update_at")
    private ZonedDateTime updateAt;

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

    public UReportFile id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public UReportFile name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return this.content;
    }

    public UReportFile content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public UReportFile createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTime getUpdateAt() {
        return this.updateAt;
    }

    public UReportFile updateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
        return this;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public LocalDate getRemovedAt() {
        return this.removedAt;
    }

    public UReportFile removedAt(LocalDate removedAt) {
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
        if (!(o instanceof UReportFile)) {
            return false;
        }
        return id != null && id.equals(((UReportFile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UReportFile{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", content='" + getContent() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
