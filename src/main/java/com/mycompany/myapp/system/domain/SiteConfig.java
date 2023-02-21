package com.mycompany.myapp.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.mycompany.myapp.domain.AbstractAuditingEntity;
import com.mycompany.myapp.domain.enumeration.CommonFieldType;
import java.time.LocalDate;

/**
 * 网站配置
 */

@TableName(value = "site_config")
public class SiteConfig extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 说明
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 属性名
     */
    @TableField(value = "field_name")
    private String fieldName;

    /**
     * 属性值
     */
    @TableField(value = "field_value")
    private String fieldValue;

    /**
     * 属性类型
     */
    @TableField(value = "field_type")
    private CommonFieldType fieldType;

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

    public SiteConfig id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public SiteConfig title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return this.remark;
    }

    public SiteConfig remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public SiteConfig fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return this.fieldValue;
    }

    public SiteConfig fieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
        return this;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public CommonFieldType getFieldType() {
        return this.fieldType;
    }

    public SiteConfig fieldType(CommonFieldType fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public void setFieldType(CommonFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public LocalDate getRemovedAt() {
        return this.removedAt;
    }

    public SiteConfig removedAt(LocalDate removedAt) {
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
        if (!(o instanceof SiteConfig)) {
            return false;
        }
        return id != null && id.equals(((SiteConfig) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteConfig{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", remark='" + getRemark() + "'" +
            ", fieldName='" + getFieldName() + "'" +
            ", fieldValue='" + getFieldValue() + "'" +
            ", fieldType='" + getFieldType() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
