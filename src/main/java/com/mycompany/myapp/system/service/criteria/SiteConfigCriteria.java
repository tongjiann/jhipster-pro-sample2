package com.mycompany.myapp.system.service.criteria;

import com.mycompany.myapp.domain.enumeration.CommonFieldType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.system.domain.SiteConfig} entity. This class is used
 * in {@link com.mycompany.myapp.system.web.rest.SiteConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /site-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SiteConfigCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private Boolean useOr = false;

    /**
     * Class for filtering CommonFieldType
     */
    public static class CommonFieldTypeFilter extends Filter<CommonFieldType> {

        public CommonFieldTypeFilter() {}

        public CommonFieldTypeFilter(CommonFieldTypeFilter filter) {
            super(filter);
        }

        @Override
        public CommonFieldTypeFilter copy() {
            return new CommonFieldTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter remark;

    private StringFilter fieldName;

    private StringFilter fieldValue;

    private CommonFieldTypeFilter fieldType;

    private LocalDateFilter removedAt;

    private LongFilter createdBy;

    private InstantFilter createdDate;

    private LongFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    public SiteConfigCriteria() {}

    public SiteConfigCriteria(SiteConfigCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.remark = other.remark == null ? null : other.remark.copy();
        this.fieldName = other.fieldName == null ? null : other.fieldName.copy();
        this.fieldValue = other.fieldValue == null ? null : other.fieldValue.copy();
        this.fieldType = other.fieldType == null ? null : other.fieldType.copy();
        this.removedAt = other.removedAt == null ? null : other.removedAt.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
    }

    @Override
    public SiteConfigCriteria copy() {
        return new SiteConfigCriteria(this);
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

    public StringFilter getRemark() {
        return remark;
    }

    public StringFilter remark() {
        if (remark == null) {
            remark = new StringFilter();
        }
        return remark;
    }

    public void setRemark(StringFilter remark) {
        this.remark = remark;
    }

    public StringFilter getFieldName() {
        return fieldName;
    }

    public StringFilter fieldName() {
        if (fieldName == null) {
            fieldName = new StringFilter();
        }
        return fieldName;
    }

    public void setFieldName(StringFilter fieldName) {
        this.fieldName = fieldName;
    }

    public StringFilter getFieldValue() {
        return fieldValue;
    }

    public StringFilter fieldValue() {
        if (fieldValue == null) {
            fieldValue = new StringFilter();
        }
        return fieldValue;
    }

    public void setFieldValue(StringFilter fieldValue) {
        this.fieldValue = fieldValue;
    }

    public CommonFieldTypeFilter getFieldType() {
        return fieldType;
    }

    public CommonFieldTypeFilter fieldType() {
        if (fieldType == null) {
            fieldType = new CommonFieldTypeFilter();
        }
        return fieldType;
    }

    public void setFieldType(CommonFieldTypeFilter fieldType) {
        this.fieldType = fieldType;
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

    public LongFilter getCreatedBy() {
        return createdBy;
    }

    public LongFilter createdBy() {
        if (createdBy == null) {
            createdBy = new LongFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(LongFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public LongFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public LongFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new LongFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(LongFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
        final SiteConfigCriteria that = (SiteConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(remark, that.remark) &&
            Objects.equals(fieldName, that.fieldName) &&
            Objects.equals(fieldValue, that.fieldValue) &&
            Objects.equals(fieldType, that.fieldType) &&
            Objects.equals(removedAt, that.removedAt) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            remark,
            fieldName,
            fieldValue,
            fieldType,
            removedAt,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (remark != null ? "remark=" + remark + ", " : "") +
                (fieldName != null ? "fieldName=" + fieldName + ", " : "") +
                (fieldValue != null ? "fieldValue=" + fieldValue + ", " : "") +
                (fieldType != null ? "fieldType=" + fieldType + ", " : "") +
                (removedAt != null ? "removedAt=" + removedAt + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
                (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
                "useOr=" + useOr +
            "}";
    }
}
