package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.DepartmentAuthority} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.DepartmentAuthorityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /department-authorities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepartmentAuthorityCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private Boolean useOr = false;

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private StringFilter description;

    private LongFilter createUserId;

    private ZonedDateTimeFilter createTime;

    private LocalDateFilter removedAt;

    private LongFilter usersId;

    private StringFilter usersFirstName;

    private LongFilter apiPermissionsId;

    private StringFilter apiPermissionsName;

    private LongFilter viewPermissionsId;

    private StringFilter viewPermissionsText;

    private LongFilter departmentId;

    private StringFilter departmentName;

    public DepartmentAuthorityCriteria() {}

    public DepartmentAuthorityCriteria(DepartmentAuthorityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.createUserId = other.createUserId == null ? null : other.createUserId.copy();
        this.createTime = other.createTime == null ? null : other.createTime.copy();
        this.removedAt = other.removedAt == null ? null : other.removedAt.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.usersFirstName = other.usersFirstName == null ? null : other.usersFirstName.copy();
        this.apiPermissionsId = other.apiPermissionsId == null ? null : other.apiPermissionsId.copy();
        this.apiPermissionsName = other.apiPermissionsName == null ? null : other.apiPermissionsName.copy();
        this.viewPermissionsId = other.viewPermissionsId == null ? null : other.viewPermissionsId.copy();
        this.viewPermissionsText = other.viewPermissionsText == null ? null : other.viewPermissionsText.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.departmentName = other.departmentName == null ? null : other.departmentName.copy();
    }

    @Override
    public DepartmentAuthorityCriteria copy() {
        return new DepartmentAuthorityCriteria(this);
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

    public LongFilter getCreateUserId() {
        return createUserId;
    }

    public LongFilter createUserId() {
        if (createUserId == null) {
            createUserId = new LongFilter();
        }
        return createUserId;
    }

    public void setCreateUserId(LongFilter createUserId) {
        this.createUserId = createUserId;
    }

    public ZonedDateTimeFilter getCreateTime() {
        return createTime;
    }

    public ZonedDateTimeFilter createTime() {
        if (createTime == null) {
            createTime = new ZonedDateTimeFilter();
        }
        return createTime;
    }

    public void setCreateTime(ZonedDateTimeFilter createTime) {
        this.createTime = createTime;
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

    public LongFilter getApiPermissionsId() {
        return apiPermissionsId;
    }

    public LongFilter apiPermissionsId() {
        if (apiPermissionsId == null) {
            apiPermissionsId = new LongFilter();
        }
        return apiPermissionsId;
    }

    public void setApiPermissionsId(LongFilter apiPermissionsId) {
        this.apiPermissionsId = apiPermissionsId;
    }

    public StringFilter getApiPermissionsName() {
        return apiPermissionsName;
    }

    public StringFilter apiPermissionsName() {
        if (apiPermissionsName == null) {
            apiPermissionsName = new StringFilter();
        }
        return apiPermissionsName;
    }

    public void setApiPermissionsName(StringFilter apiPermissionsName) {
        this.apiPermissionsName = apiPermissionsName;
    }

    public LongFilter getViewPermissionsId() {
        return viewPermissionsId;
    }

    public LongFilter viewPermissionsId() {
        if (viewPermissionsId == null) {
            viewPermissionsId = new LongFilter();
        }
        return viewPermissionsId;
    }

    public void setViewPermissionsId(LongFilter viewPermissionsId) {
        this.viewPermissionsId = viewPermissionsId;
    }

    public StringFilter getViewPermissionsText() {
        return viewPermissionsText;
    }

    public StringFilter viewPermissionsText() {
        if (viewPermissionsText == null) {
            viewPermissionsText = new StringFilter();
        }
        return viewPermissionsText;
    }

    public void setViewPermissionsText(StringFilter viewPermissionsText) {
        this.viewPermissionsText = viewPermissionsText;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            departmentId = new LongFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public StringFilter getDepartmentName() {
        return departmentName;
    }

    public StringFilter departmentName() {
        if (departmentName == null) {
            departmentName = new StringFilter();
        }
        return departmentName;
    }

    public void setDepartmentName(StringFilter departmentName) {
        this.departmentName = departmentName;
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
        final DepartmentAuthorityCriteria that = (DepartmentAuthorityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createUserId, that.createUserId) &&
            Objects.equals(createTime, that.createTime) &&
            Objects.equals(removedAt, that.removedAt) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(usersFirstName, that.usersFirstName) &&
            Objects.equals(apiPermissionsId, that.apiPermissionsId) &&
            Objects.equals(apiPermissionsName, that.apiPermissionsName) &&
            Objects.equals(viewPermissionsId, that.viewPermissionsId) &&
            Objects.equals(viewPermissionsText, that.viewPermissionsText) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(departmentName, that.departmentName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            code,
            description,
            createUserId,
            createTime,
            removedAt,
            usersId,
            usersFirstName,
            apiPermissionsId,
            apiPermissionsName,
            viewPermissionsId,
            viewPermissionsText,
            departmentId,
            departmentName
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentAuthorityCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (createUserId != null ? "createUserId=" + createUserId + ", " : "") +
                (createTime != null ? "createTime=" + createTime + ", " : "") +
                (removedAt != null ? "removedAt=" + removedAt + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
                (usersFirstName != null ? "usersFirstName=" + usersFirstName + ", " : "") +
                (apiPermissionsId != null ? "apiPermissionsId=" + apiPermissionsId + ", " : "") +
                (apiPermissionsName != null ? "apiPermissionsName=" + apiPermissionsName + ", " : "") +
                (viewPermissionsId != null ? "viewPermissionsId=" + viewPermissionsId + ", " : "") +
                (viewPermissionsText != null ? "viewPermissionsText=" + viewPermissionsText + ", " : "") +
                (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
                (departmentName != null ? "departmentName=" + departmentName + ", " : "") +
                (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
                "useOr=" + useOr +
            "}";
    }
}
