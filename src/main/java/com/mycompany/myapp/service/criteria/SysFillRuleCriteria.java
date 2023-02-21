package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.SysFillRule} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SysFillRuleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sys-fill-rules?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SysFillRuleCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private Boolean useOr = false;

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter code;

    private StringFilter implClass;

    private StringFilter params;

    private LocalDateFilter removedAt;

    public SysFillRuleCriteria() {}

    public SysFillRuleCriteria(SysFillRuleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.implClass = other.implClass == null ? null : other.implClass.copy();
        this.params = other.params == null ? null : other.params.copy();
        this.removedAt = other.removedAt == null ? null : other.removedAt.copy();
    }

    @Override
    public SysFillRuleCriteria copy() {
        return new SysFillRuleCriteria(this);
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

    public StringFilter getImplClass() {
        return implClass;
    }

    public StringFilter implClass() {
        if (implClass == null) {
            implClass = new StringFilter();
        }
        return implClass;
    }

    public void setImplClass(StringFilter implClass) {
        this.implClass = implClass;
    }

    public StringFilter getParams() {
        return params;
    }

    public StringFilter params() {
        if (params == null) {
            params = new StringFilter();
        }
        return params;
    }

    public void setParams(StringFilter params) {
        this.params = params;
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
        final SysFillRuleCriteria that = (SysFillRuleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(code, that.code) &&
            Objects.equals(implClass, that.implClass) &&
            Objects.equals(params, that.params) &&
            Objects.equals(removedAt, that.removedAt)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, implClass, params, removedAt);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysFillRuleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (implClass != null ? "implClass=" + implClass + ", " : "") +
                (params != null ? "params=" + params + ", " : "") +
                (removedAt != null ? "removedAt=" + removedAt + ", " : "") +
                (jhiCommonSearchKeywords != null ? "jhiCommonSearchKeywords=" + jhiCommonSearchKeywords + ", " : "") +
                "useOr=" + useOr +
            "}";
    }
}
