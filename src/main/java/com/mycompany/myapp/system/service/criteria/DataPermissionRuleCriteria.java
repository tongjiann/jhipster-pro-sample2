package com.mycompany.myapp.system.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.system.domain.DataPermissionRule} entity. This class is used
 * in {@link com.mycompany.myapp.system.web.rest.DataPermissionRuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /data-permission-rules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DataPermissionRuleCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private Boolean useOr = false;

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter permissionId;

    private StringFilter name;

    private StringFilter column;

    private StringFilter conditions;

    private StringFilter value;

    private BooleanFilter disabled;

    private LocalDateFilter removedAt;

    private LongFilter createdBy;

    private InstantFilter createdDate;

    private LongFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    public DataPermissionRuleCriteria() {}

    public DataPermissionRuleCriteria(DataPermissionRuleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.permissionId = other.permissionId == null ? null : other.permissionId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.column = other.column == null ? null : other.column.copy();
        this.conditions = other.conditions == null ? null : other.conditions.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.disabled = other.disabled == null ? null : other.disabled.copy();
        this.removedAt = other.removedAt == null ? null : other.removedAt.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
    }

    @Override
    public DataPermissionRuleCriteria copy() {
        return new DataPermissionRuleCriteria(this);
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

    public StringFilter getPermissionId() {
        return permissionId;
    }

    public StringFilter permissionId() {
        if (permissionId == null) {
            permissionId = new StringFilter();
        }
        return permissionId;
    }

    public void setPermissionId(StringFilter permissionId) {
        this.permissionId = permissionId;
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

    public StringFilter getColumn() {
        return column;
    }

    public StringFilter column() {
        if (column == null) {
            column = new StringFilter();
        }
        return column;
    }

    public void setColumn(StringFilter column) {
        this.column = column;
    }

    public StringFilter getConditions() {
        return conditions;
    }

    public StringFilter conditions() {
        if (conditions == null) {
            conditions = new StringFilter();
        }
        return conditions;
    }

    public void setConditions(StringFilter conditions) {
        this.conditions = conditions;
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
        final DataPermissionRuleCriteria that = (DataPermissionRuleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(permissionId, that.permissionId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(column, that.column) &&
            Objects.equals(conditions, that.conditions) &&
            Objects.equals(value, that.value) &&
            Objects.equals(disabled, that.disabled) &&
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
            permissionId,
            name,
            column,
            conditions,
            value,
            disabled,
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
        return "DataPermissionRuleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (permissionId != null ? "permissionId=" + permissionId + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (column != null ? "column=" + column + ", " : "") +
                (conditions != null ? "conditions=" + conditions + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
                (disabled != null ? "disabled=" + disabled + ", " : "") +
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
