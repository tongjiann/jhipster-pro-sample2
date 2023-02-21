package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Position} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PositionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /positions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PositionCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private Boolean useOr = false;

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private IntegerFilter sortNo;

    private StringFilter description;

    private LocalDateFilter removedAt;

    private LongFilter usersId;

    private StringFilter usersFirstName;

    public PositionCriteria() {}

    public PositionCriteria(PositionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.sortNo = other.sortNo == null ? null : other.sortNo.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.removedAt = other.removedAt == null ? null : other.removedAt.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.usersFirstName = other.usersFirstName == null ? null : other.usersFirstName.copy();
    }

    @Override
    public PositionCriteria copy() {
        return new PositionCriteria(this);
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

    public IntegerFilter getSortNo() {
        return sortNo;
    }

    public IntegerFilter sortNo() {
        if (sortNo == null) {
            sortNo = new IntegerFilter();
        }
        return sortNo;
    }

    public void setSortNo(IntegerFilter sortNo) {
        this.sortNo = sortNo;
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

    public LongFilter getUsersId() {
        return usersId;
    }

    public LongFilter usersId() {
        if (usersId == null) {
            usersId = new LongFilter();
        }
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }

    public StringFilter getUsersFirstName() {
        return usersFirstName;
    }

    public StringFilter usersFirstName() {
        if (usersFirstName == null) {
            usersFirstName = new StringFilter();
        }
        return usersFirstName;
    }

    public void setUsersFirstName(StringFilter usersFirstName) {
        this.usersFirstName = usersFirstName;
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
        final PositionCriteria that = (PositionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(sortNo, that.sortNo) &&
            Objects.equals(description, that.description) &&
            Objects.equals(removedAt, that.removedAt) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(usersFirstName, that.usersFirstName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, sortNo, description, removedAt, usersId, usersFirstName);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (sortNo != null ? "sortNo=" + sortNo + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (removedAt != null ? "removedAt=" + removedAt + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
                (usersFirstName != null ? "usersFirstName=" + usersFirstName + ", " : "") +
                (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
                "useOr=" + useOr +
            "}";
    }
}
