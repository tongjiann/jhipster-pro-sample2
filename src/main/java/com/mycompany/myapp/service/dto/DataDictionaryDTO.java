package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.DictType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.domain.DataDictionary}的DTO。
 */
@ApiModel(description = "数据字典")
public class DataDictionaryDTO implements Serializable {

    /**
     *
     */
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 代码
     */
    @ApiModelProperty(value = "代码")
    private String code;

    /**
     * 字典项文本
     */
    @ApiModelProperty(value = "字典项文本")
    private String title;

    /**
     * 字典项值
     */
    @ApiModelProperty(value = "字典项值")
    private String value;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

    /**
     * 是否禁用
     */
    @ApiModelProperty(value = "是否禁用")
    private Boolean disabled;

    /**
     * 字体颜色
     */
    @ApiModelProperty(value = "字体颜色")
    private String fontColor;

    /**
     * 值类型
     */
    @ApiModelProperty(value = "值类型")
    private DictType valueType;

    /**
     * 背景颜色
     */
    @ApiModelProperty(value = "背景颜色")
    private String backgroundColor;

    /**
     * 软删除时间
     */
    @ApiModelProperty(value = "软删除时间")
    private LocalDate removedAt;

    /**
     *
     */
    private List<DataDictionaryDTO> children = new ArrayList<>();

    /**
     *
     */
    private DataDictionarySimpleDTO parent;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public DictType getValueType() {
        return valueType;
    }

    public void setValueType(DictType valueType) {
        this.valueType = valueType;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public LocalDate getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
    }

    public List<DataDictionaryDTO> getChildren() {
        return children;
    }

    public void setChildren(List<DataDictionaryDTO> children) {
        this.children = children;
    }

    public DataDictionarySimpleDTO getParent() {
        return parent;
    }

    public void setParent(DataDictionarySimpleDTO parent) {
        this.parent = parent;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataDictionaryDTO)) {
            return false;
        }

        DataDictionaryDTO dataDictionaryDTO = (DataDictionaryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dataDictionaryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataDictionaryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", value='" + getValue() + "'" +
            ", description='" + getDescription() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", disabled='" + getDisabled() + "'" +
            ", fontColor='" + getFontColor() + "'" +
            ", valueType='" + getValueType() + "'" +
            ", backgroundColor='" + getBackgroundColor() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            ", children=" + getChildren() +
            ", parent=" + getParent() +
            "}";
    }
}
