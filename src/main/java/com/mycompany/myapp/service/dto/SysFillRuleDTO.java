package com.mycompany.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.domain.SysFillRule}的DTO。
 */
@ApiModel(description = "填充规则")
public class SysFillRuleDTO implements Serializable {

    /**
     *
     */
    private Long id;

    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String name;

    /**
     * 规则Code
     */
    @ApiModelProperty(value = "规则Code")
    private String code;

    /**
     * 规则实现类
     */
    @ApiModelProperty(value = "规则实现类")
    private String implClass;

    /**
     * 规则参数
     */
    @ApiModelProperty(value = "规则参数")
    private String params;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImplClass() {
        return implClass;
    }

    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
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
        if (!(o instanceof SysFillRuleDTO)) {
            return false;
        }

        SysFillRuleDTO sysFillRuleDTO = (SysFillRuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sysFillRuleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysFillRuleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", implClass='" + getImplClass() + "'" +
            ", params='" + getParams() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
