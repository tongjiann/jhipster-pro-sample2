package com.mycompany.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.DictType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典
 */

@TableName(value = "data_dictionary")
public class DataDictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 代码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 字典项文本
     */
    @TableField(value = "title")
    private String title;

    /**
     * 字典项值
     */
    @TableField(value = "value")
    private String value;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 排序
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;

    /**
     * 是否禁用
     */
    @TableField(value = "disabled")
    private Boolean disabled;

    /**
     * 字体颜色
     */
    @TableField(value = "font_color")
    private String fontColor;

    /**
     * 值类型
     */
    @TableField(value = "value_type")
    private DictType valueType;

    /**
     * 背景颜色
     */
    @TableField(value = "background_color")
    private String backgroundColor;

    /**
     * 软删除时间
     */
    @TableField(value = "removed_at")
    private LocalDate removedAt;

    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 子节点
     */
    @TableField(exist = false)
    @BindEntityList(entity = DataDictionary.class, deepBind = true, condition = "id=parent_id")
    @JsonIgnoreProperties(value = { "children", "parent" }, allowSetters = true)
    private List<DataDictionary> children = new ArrayList<>();

    /**
     * 上级节点
     */
    @TableField(exist = false)
    @BindEntity(entity = DataDictionary.class, condition = "this.parent_id=id")
    @JsonIgnoreProperties(value = { "children", "parent" }, allowSetters = true)
    private DataDictionary parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DataDictionary id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public DataDictionary name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public DataDictionary code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return this.title;
    }

    public DataDictionary title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return this.value;
    }

    public DataDictionary value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return this.description;
    }

    public DataDictionary description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public DataDictionary sortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getDisabled() {
        return this.disabled;
    }

    public DataDictionary disabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getFontColor() {
        return this.fontColor;
    }

    public DataDictionary fontColor(String fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public DictType getValueType() {
        return this.valueType;
    }

    public DataDictionary valueType(DictType valueType) {
        this.valueType = valueType;
        return this;
    }

    public void setValueType(DictType valueType) {
        this.valueType = valueType;
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    public DataDictionary backgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public LocalDate getRemovedAt() {
        return this.removedAt;
    }

    public DataDictionary removedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
        return this;
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
    }

    public List<DataDictionary> getChildren() {
        return this.children;
    }

    public DataDictionary children(List<DataDictionary> dataDictionaries) {
        this.setChildren(dataDictionaries);
        return this;
    }

    public DataDictionary addChildren(DataDictionary dataDictionary) {
        this.children.add(dataDictionary);
        dataDictionary.setParent(this);
        return this;
    }

    public DataDictionary removeChildren(DataDictionary dataDictionary) {
        this.children.remove(dataDictionary);
        dataDictionary.setParent(null);
        return this;
    }

    public void setChildren(List<DataDictionary> dataDictionaries) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (dataDictionaries != null) {
            dataDictionaries.forEach(i -> i.setParent(this));
        }
        this.children = dataDictionaries;
    }

    public DataDictionary getParent() {
        return this.parent;
    }

    public DataDictionary parent(DataDictionary dataDictionary) {
        this.setParent(dataDictionary);
        return this;
    }

    public void setParent(DataDictionary dataDictionary) {
        this.parent = dataDictionary;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataDictionary)) {
            return false;
        }
        return id != null && id.equals(((DataDictionary) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataDictionary{" +
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
            "}";
    }
}
