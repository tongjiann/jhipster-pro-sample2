package com.mycompany.myapp.system.service.criteria;

import com.mycompany.myapp.domain.enumeration.LogType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.system.domain.SysLog} entity. This class is used
 * in {@link com.mycompany.myapp.system.web.rest.SysLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sys-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SysLogCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private Boolean useOr = false;

    /**
     * Class for filtering LogType
     */
    public static class LogTypeFilter extends Filter<LogType> {

        public LogTypeFilter() {}

        public LogTypeFilter(LogTypeFilter filter) {
            super(filter);
        }

        @Override
        public LogTypeFilter copy() {
            return new LogTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LogTypeFilter logType;

    private StringFilter logContent;

    private IntegerFilter operateType;

    private StringFilter userid;

    private StringFilter username;

    private StringFilter ip;

    private StringFilter method;

    private StringFilter requestUrl;

    private StringFilter requestType;

    private LongFilter costTime;

    private LocalDateFilter removedAt;

    private LongFilter createdBy;

    private InstantFilter createdDate;

    private LongFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    public SysLogCriteria() {}

    public SysLogCriteria(SysLogCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.logType = other.logType == null ? null : other.logType.copy();
        this.logContent = other.logContent == null ? null : other.logContent.copy();
        this.operateType = other.operateType == null ? null : other.operateType.copy();
        this.userid = other.userid == null ? null : other.userid.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.ip = other.ip == null ? null : other.ip.copy();
        this.method = other.method == null ? null : other.method.copy();
        this.requestUrl = other.requestUrl == null ? null : other.requestUrl.copy();
        this.requestType = other.requestType == null ? null : other.requestType.copy();
        this.costTime = other.costTime == null ? null : other.costTime.copy();
        this.removedAt = other.removedAt == null ? null : other.removedAt.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
    }

    @Override
    public SysLogCriteria copy() {
        return new SysLogCriteria(this);
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

    public LogTypeFilter getLogType() {
        return logType;
    }

    public LogTypeFilter logType() {
        if (logType == null) {
            logType = new LogTypeFilter();
        }
        return logType;
    }

    public void setLogType(LogTypeFilter logType) {
        this.logType = logType;
    }

    public StringFilter getLogContent() {
        return logContent;
    }

    public StringFilter logContent() {
        if (logContent == null) {
            logContent = new StringFilter();
        }
        return logContent;
    }

    public void setLogContent(StringFilter logContent) {
        this.logContent = logContent;
    }

    public IntegerFilter getOperateType() {
        return operateType;
    }

    public IntegerFilter operateType() {
        if (operateType == null) {
            operateType = new IntegerFilter();
        }
        return operateType;
    }

    public void setOperateType(IntegerFilter operateType) {
        this.operateType = operateType;
    }

    public StringFilter getUserid() {
        return userid;
    }

    public StringFilter userid() {
        if (userid == null) {
            userid = new StringFilter();
        }
        return userid;
    }

    public void setUserid(StringFilter userid) {
        this.userid = userid;
    }

    public StringFilter getUsername() {
        return username;
    }

    public StringFilter username() {
        if (username == null) {
            username = new StringFilter();
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getIp() {
        return ip;
    }

    public StringFilter ip() {
        if (ip == null) {
            ip = new StringFilter();
        }
        return ip;
    }

    public void setIp(StringFilter ip) {
        this.ip = ip;
    }

    public StringFilter getMethod() {
        return method;
    }

    public StringFilter method() {
        if (method == null) {
            method = new StringFilter();
        }
        return method;
    }

    public void setMethod(StringFilter method) {
        this.method = method;
    }

    public StringFilter getRequestUrl() {
        return requestUrl;
    }

    public StringFilter requestUrl() {
        if (requestUrl == null) {
            requestUrl = new StringFilter();
        }
        return requestUrl;
    }

    public void setRequestUrl(StringFilter requestUrl) {
        this.requestUrl = requestUrl;
    }

    public StringFilter getRequestType() {
        return requestType;
    }

    public StringFilter requestType() {
        if (requestType == null) {
            requestType = new StringFilter();
        }
        return requestType;
    }

    public void setRequestType(StringFilter requestType) {
        this.requestType = requestType;
    }

    public LongFilter getCostTime() {
        return costTime;
    }

    public LongFilter costTime() {
        if (costTime == null) {
            costTime = new LongFilter();
        }
        return costTime;
    }

    public void setCostTime(LongFilter costTime) {
        this.costTime = costTime;
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
        final SysLogCriteria that = (SysLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(logType, that.logType) &&
            Objects.equals(logContent, that.logContent) &&
            Objects.equals(operateType, that.operateType) &&
            Objects.equals(userid, that.userid) &&
            Objects.equals(username, that.username) &&
            Objects.equals(ip, that.ip) &&
            Objects.equals(method, that.method) &&
            Objects.equals(requestUrl, that.requestUrl) &&
            Objects.equals(requestType, that.requestType) &&
            Objects.equals(costTime, that.costTime) &&
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
            logType,
            logContent,
            operateType,
            userid,
            username,
            ip,
            method,
            requestUrl,
            requestType,
            costTime,
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
        return "SysLogCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (logType != null ? "logType=" + logType + ", " : "") +
                (logContent != null ? "logContent=" + logContent + ", " : "") +
                (operateType != null ? "operateType=" + operateType + ", " : "") +
                (userid != null ? "userid=" + userid + ", " : "") +
                (username != null ? "username=" + username + ", " : "") +
                (ip != null ? "ip=" + ip + ", " : "") +
                (method != null ? "method=" + method + ", " : "") +
                (requestUrl != null ? "requestUrl=" + requestUrl + ", " : "") +
                (requestType != null ? "requestType=" + requestType + ", " : "") +
                (costTime != null ? "costTime=" + costTime + ", " : "") +
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
