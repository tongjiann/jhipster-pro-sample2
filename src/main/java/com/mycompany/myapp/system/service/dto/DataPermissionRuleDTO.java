package com.mycompany.myapp.system.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.system.domain.DataPermissionRule}的DTO。
 */
@ApiModel(description = "数据权限规则")
public class DataPermissionRuleDTO implements Serializable {

    /**
     *
     */
    private Long id;

    /**
     * 菜单ID
     */
    @ApiModelProperty(value = "菜单ID")
    private String permissionId;

    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String name;

    /**
     * 字段
     */
    @ApiModelProperty(value = "字段")
    private String column;

    /**
     * 条件
     */
    @ApiModelProperty(value = "条件")
    private String conditions;

    /**
     * 规则值
     */
    @ApiModelProperty(value = "规则值")
    private String value;

    /**
     * 禁用状态
     */
    @ApiModelProperty(value = "禁用状态")
    private Boolean disabled;

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

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
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
        if (!(o instanceof DataPermissionRuleDTO)) {
            return false;
        }

        DataPermissionRuleDTO dataPermissionRuleDTO = (DataPermissionRuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dataPermissionRuleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataPermissionRuleDTO{" +
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
