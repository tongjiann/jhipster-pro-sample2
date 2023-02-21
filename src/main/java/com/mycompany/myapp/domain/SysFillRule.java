package com.mycompany.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 填充规则
 */

@TableName(value = "sys_fill_rule")
public class SysFillRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 规则名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 规则Code
     */
    @TableField(value = "code")
    private String code;

    /**
     * 规则实现类
     */
    @TableField(value = "impl_class")
    private String implClass;

    /**
     * 规则参数
     */
    @TableField(value = "params")
    private String params;

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

    public SysFillRule id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SysFillRule name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public SysFillRule code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImplClass() {
        return this.implClass;
    }

    public SysFillRule implClass(String implClass) {
        this.implClass = implClass;
        return this;
    }

    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }

    public String getParams() {
        return this.params;
    }

    public SysFillRule params(String params) {
        this.params = params;
        return this;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public LocalDate getRemovedAt() {
        return this.removedAt;
    }

    public SysFillRule removedAt(LocalDate removedAt) {
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
        if (!(o instanceof SysFillRule)) {
            return false;
        }
        return id != null && id.equals(((SysFillRule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysFillRule{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", implClass='" + getImplClass() + "'" +
            ", params='" + getParams() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
