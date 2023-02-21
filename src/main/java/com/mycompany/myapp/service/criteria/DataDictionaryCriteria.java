package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.DictType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.DataDictionary} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.DataDictionaryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /data-dictionaries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DataDictionaryCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private Boolean useOr = false;

    /**
     * Class for filtering DictType
     */
    public static class DictTypeFilter extends Filter<DictType> {

        public DictTypeFilter() {}

        public DictTypeFilter(DictTypeFilter filter) {
            super(filter);
        }

        @Override
        public DictTypeFilter copy() {
            return new DictTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private StringFilter title;

    private StringFilter value;

    private StringFilter description;

    private IntegerFilter sortOrder;

    private BooleanFilter disabled;

    private StringFilter fontColor;

    private DictTypeFilter valueType;

    private StringFilter backgroundColor;

    private LocalDateFilter removedAt;

    private LongFilter childrenId;

    private StringFilter childrenName;

    private LongFilter parentId;

    private StringFilter parentName;

    public DataDictionaryCriteria() {}

    public DataDictionaryCriteria(DataDictionaryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.sortOrder = other.sortOrder == null ? null : other.sortOrder.copy();
        this.disabled = other.disabled == null ? null : other.disabled.copy();
        this.fontColor = other.fontColor == null ? null : other.fontColor.copy();
        this.valueType = other.valueType == null ? null : other.valueType.copy();
        this.backgroundColor = other.backgroundColor == null ? null : other.backgroundColor.copy();
        this.removedAt = other.removedAt == null ? null : other.removedAt.copy();
        this.childrenId = other.childrenId == null ? null : other.childrenId.copy();
        this.childrenName = other.childrenName == null ? null : other.childrenName.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.parentName = other.parentName == null ? null : other.parentName.copy();
    }

    @Override
    public DataDictionaryCriteria copy() {
        return new DataDictionaryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getValue() {
        return value;
    }

    public StringFilter value() {
        if (value == null) {
            value = new StringFilter();
        }
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getSortOrder() {
        return sortOrder;
    }

    public IntegerFilter sortOrder() {
        if (sortOrder == null) {
            sortOrder = new IntegerFilter();
        }
        return sortOrder;
    }

    public void setSortOrder(IntegerFilter sortOrder) {
        this.sortOrder = sortOrder;
    }

    public BooleanFilter getDisabled() {
        return disabled;
    }

    public BooleanFilter disabled() {
        if (disabled == null) {
            disabled = new BooleanFilter();
        }
        return disabled;
    }

    public void setDisabled(BooleanFilter disabled) {
        this.disabled = disabled;
    }

    public StringFilter getFontColor() {
        return fontColor;
    }

    public StringFilter fontColor() {
        if (fontColor == null) {
            fontColor = new StringFilter();
        }
        return fontColor;
    }

    public void setFontColor(StringFilter fontColor) {
        this.fontColor = fontColor;
    }

    public DictTypeFilter getValueType() {
        return valueType;
    }

    public DictTypeFilter valueType() {
        if (valueType == null) {
            valueType = new DictTypeFilter();
        }
        return valueType;
    }

    public void setValueType(DictTypeFilter valueType) {
        this.valueType = valueType;
    }

    public StringFilter getBackgroundColor() {
        return backgroundColor;
    }

    public StringFilter backgroundColor() {
        if (backgroundColor == null) {
            backgroundColor = new StringFilter();
        }
        return backgroundColor;
    }

    public void setBackgroundColor(StringFilter backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public LocalDateFilter getRemovedAt() {
        return removedAt;
    }

    public LocalDateFilter removedAt() {
        if (removedAt == null) {
            removedAt = new LocalDateFilter();
        }
        return removedAt;
    }

    public void setRemovedAt(LocalDateFilter removedAt) {
        this.removedAt = removedAt;
    }

    public LongFilter getChildrenId() {
        return childrenId;
    }

    public LongFilter childrenId() {
        if (childrenId == null) {
            childrenId = new LongFilter();
        }
        return childrenId;
    }

    public void setChildrenId(LongFilter childrenId) {
        this.childrenId = childrenId;
    }

    public StringFilter getChildrenName() {
        return childrenName;
    }

    public StringFilter childrenName() {
        if (childrenName == null) {
            childrenName = new StringFilter();
        }
        return childrenName;
    }

    public void setChildrenName(StringFilter childrenName) {
        this.childrenName = childrenName;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public StringFilter getParentName() {
        return parentName;
    }

    public StringFilter parentName() {
        if (parentName == null) {
            parentName = new StringFilter();
        }
        return parentName;
    }

    public void setParentName(StringFilter parentName) {
        this.parentName = parentName;
    }

    public String getJhiCommonSearchKeywords() {
        return jhiCommonSearchKeywords;
    }

    public void setJhiCommonSearchKeywords(String jhiCommonSearchKeywords) {
        this.jhiCommonSearchKeywords = jhiCommonSearchKeywords;
    }

    public Boolean getUseOr() {
        return useOr;
    }

    public void setUseOr(Boolean useOr) {
        this.useOr = useOr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DataDictionaryCriteria that = (DataDictionaryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(title, that.title) &&
            Objects.equals(value, that.value) &&
            Objects.equals(description, that.description) &&
            Objects.equals(sortOrder, that.sortOrder) &&
            Objects.equals(disabled, that.disabled) &&
            Objects.equals(fontColor, that.fontColor) &&
            Objects.equals(valueType, that.valueType) &&
            Objects.equals(backgroundColor, that.backgroundColor) &&
            Objects.equals(removedAt, that.removedAt) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(childrenName, that.childrenName) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(parentName, that.parentName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            code,
            title,
            value,
            description,
            sortOrder,
            disabled,
            fontColor,
            valueType,
            backgroundColor,
            removedAt,
            childrenId,
            childrenName,
            parentId,
            parentName
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataDictionaryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (sortOrder != null ? "sortOrder=" + sortOrder + ", " : "") +
                (disabled != null ? "disabled=" + disabled + ", " : "") +
                (fontColor != null ? "fontColor=" + fontColor + ", " : "") +
                (valueType != null ? "valueType=" + valueType + ", " : "") +
                (backgroundColor != null ? "backgroundColor=" + backgroundColor + ", " : "") +
                (removedAt != null ? "removedAt=" + removedAt + ", " : "") +
                (childrenId != null ? "childrenId=" + childrenId + ", " : "") +
                (childrenName != null ? "childrenName=" + childrenName + ", " : "") +
                (parentId != null ? "parentId=" + parentId + ", " : "") +
                (parentName != null ? "parentName=" + parentName + ", " : "") +
                (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
                "useOr=" + useOr +
            "}";
    }
}
