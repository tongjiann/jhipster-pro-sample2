package com.mycompany.myapp.system.service.dto;

import com.mycompany.myapp.domain.enumeration.CommonFieldType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.system.domain.SiteConfig}的DTO。
 */
@ApiModel(description = "网站配置")
public class SiteConfigDTO implements Serializable {

    /**
     *
     */
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String remark;

    /**
     * 属性名
     */
    @ApiModelProperty(value = "属性名")
    private String fieldName;

    /**
     * 属性值
     */
    @ApiModelProperty(value = "属性值")
    private String fieldValue;

    /**
     * 属性类型
     */
    @ApiModelProperty(value = "属性类型")
    private CommonFieldType fieldType;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public CommonFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(CommonFieldType fieldType) {
        this.fieldType = fieldType;
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
        if (!(o instanceof SiteConfigDTO)) {
            return false;
        }

        SiteConfigDTO siteConfigDTO = (SiteConfigDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, siteConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteConfigDTO{" +
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
