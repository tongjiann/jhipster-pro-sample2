package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Authority} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AuthorityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /authorities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AuthorityCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private Boolean useOr = false;

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private StringFilter info;

    private IntegerFilter order;

    private BooleanFilter display;

    private LocalDateFilter removedAt;

    private LongFilter childrenId;

    private StringFilter childrenName;

    private LongFilter parentId;

    private StringFilter parentName;

    private LongFilter apiPermissionsId;

    private StringFilter apiPermissionsName;

    private LongFilter viewPermissionsId;

    private StringFilter viewPermissionsText;

    public AuthorityCriteria() {}

    public AuthorityCriteria(AuthorityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.info = other.info == null ? null : other.info.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.display = other.display == null ? null : other.display.copy();
        this.removedAt = other.removedAt == null ? null : other.removedAt.copy();
        this.childrenId = other.childrenId == null ? null : other.childrenId.copy();
        this.childrenName = other.childrenName == null ? null : other.childrenName.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.parentName = other.parentName == null ? null : other.parentName.copy();
        this.apiPermissionsId = other.apiPermissionsId == null ? null : other.apiPermissionsId.copy();
        this.apiPermissionsName = other.apiPermissionsName == null ? null : other.apiPermissionsName.copy();
        this.viewPermissionsId = other.viewPermissionsId == null ? null : other.viewPermissionsId.copy();
        this.viewPermissionsText = other.viewPermissionsText == null ? null : other.viewPermissionsText.copy();
    }

    @Override
    public AuthorityCriteria copy() {
        return new AuthorityCriteria(this);
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

    public StringFilter getInfo() {
        return info;
    }

    public StringFilter info() {
        if (info == null) {
            info = new StringFilter();
        }
        return info;
    }

    public void setInfo(StringFilter info) {
        this.info = info;
    }

    public IntegerFilter getOrder() {
        return order;
    }

    public IntegerFilter order() {
        if (order == null) {
            order = new IntegerFilter();
        }
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public BooleanFilter getDisplay() {
        return display;
    }

    public BooleanFilter display() {
        if (display == null) {
            display = new BooleanFilter();
        }
        return display;
    }

    public void setDisplay(BooleanFilter display) {
        this.display = display;
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
        final AuthorityCriteria that = (AuthorityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(info, that.info) &&
            Objects.equals(order, that.order) &&
            Objects.equals(display, that.display) &&
            Objects.equals(removedAt, that.removedAt) &&
            Objects.equals(childrenId, that.childrenId) &&
            Objects.equals(childrenName, that.childrenName) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(parentName, that.parentName) &&
            Objects.equals(apiPermissionsId, that.apiPermissionsId) &&
            Objects.equals(apiPermissionsName, that.apiPermissionsName) &&
            Objects.equals(viewPermissionsId, that.viewPermissionsId) &&
            Objects.equals(viewPermissionsText, that.viewPermissionsText)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            code,
            info,
            order,
            display,
            removedAt,
            childrenId,
            childrenName,
            parentId,
            parentName,
            apiPermissionsId,
            apiPermissionsName,
            viewPermissionsId,
            viewPermissionsText
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorityCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (info != null ? "info=" + info + ", " : "") +
                (order != null ? "order=" + order + ", " : "") +
                (display != null ? "display=" + display + ", " : "") +
                (removedAt != null ? "removedAt=" + removedAt + ", " : "") +
                (childrenId != null ? "childrenId=" + childrenId + ", " : "") +
                (childrenName != null ? "childrenName=" + childrenName + ", " : "") +
                (parentId != null ? "parentId=" + parentId + ", " : "") +
                (parentName != null ? "parentName=" + parentName + ", " : "") +
                (apiPermissionsId != null ? "apiPermissionsId=" + apiPermissionsId + ", " : "") +
                (apiPermissionsName != null ? "apiPermissionsName=" + apiPermissionsName + ", " : "") +
                (viewPermissionsId != null ? "viewPermissionsId=" + viewPermissionsId + ", " : "") +
                (viewPermissionsText != null ? "viewPermissionsText=" + viewPermissionsText + ", " : "") +
                (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
                "useOr=" + useOr +
            "}";
    }
}
