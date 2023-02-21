package com.mycompany.myapp.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.mycompany.myapp.domain.AbstractAuditingEntity;
import java.time.LocalDate;

/**
 * 数据权限规则
 */

@TableName(value = "data_permission_rule")
public class DataPermissionRule extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单ID
     */
    @TableField(value = "permission_id")
    private String permissionId;

    /**
     * 规则名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 字段
     */
    @TableField(value = "`column`")
    private String column;

    /**
     * 条件
     */
    @TableField(value = "conditions")
    private String conditions;

    /**
     * 规则值
     */
    @TableField(value = "value")
    private String value;

    /**
     * 禁用状态
     */
    @TableField(value = "disabled")
    private Boolean disabled;

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

    public DataPermissionRule id(Long id) {
        this.id = id;
        return this;
    }

    public String getPermissionId() {
        return this.permissionId;
    }

    public DataPermissionRule permissionId(String permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getName() {
        return this.name;
    }

    public DataPermissionRule name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumn() {
        return this.column;
    }

    public DataPermissionRule column(String column) {
        this.column = column;
        return this;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getConditions() {
        return this.conditions;
    }

    public DataPermissionRule conditions(String conditions) {
        this.conditions = conditions;
        return this;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getValue() {
        return this.value;
    }

    public DataPermissionRule value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getDisabled() {
        return this.disabled;
    }

    public DataPermissionRule disabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public LocalDate getRemovedAt() {
        return this.removedAt;
    }

    public DataPermissionRule removedAt(LocalDate removedAt) {
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
        if (!(o instanceof DataPermissionRule)) {
            return false;
        }
        return id != null && id.equals(((DataPermissionRule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataPermissionRule{" +
            "id=" + getId() +
            ", permissionId='" + getPermissionId() + "'" +
            ", name='" + getName() + "'" +
            ", column='" + getColumn() + "'" +
            ", conditions='" + getConditions() + "'" +
            ", value='" + getValue() + "'" +
            ", disabled='" + getDisabled() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
