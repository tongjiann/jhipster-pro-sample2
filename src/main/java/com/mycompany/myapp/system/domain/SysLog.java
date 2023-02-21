package com.mycompany.myapp.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.mycompany.myapp.domain.AbstractAuditingEntity;
import com.mycompany.myapp.domain.enumeration.LogType;
import java.time.LocalDate;

/**
 * 系统日志
 */

@TableName(value = "sys_log")
public class SysLog extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 日志类型
     */
    @TableField(value = "log_type")
    private LogType logType;

    /**
     * 日志内容
     */
    @TableField(value = "log_content")
    private String logContent;

    /**
     * 操作类型
     */
    @TableField(value = "operate_type")
    private Integer operateType;

    /**
     * 操作用户账号
     */
    @TableField(value = "userid")
    private String userid;

    /**
     * 操作用户名称
     */
    @TableField(value = "username")
    private String username;

    /**
     * IP
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 请求java方法
     */
    @TableField(value = "method")
    private String method;

    /**
     * 请求路径
     */
    @TableField(value = "request_url")
    private String requestUrl;

    /**
     * 请求参数
     */
    @TableField(value = "request_param")
    private String requestParam;

    /**
     * 请求类型
     */
    @TableField(value = "request_type")
    private String requestType;

    /**
     * 耗时
     */
    @TableField(value = "cost_time")
    private Long costTime;

    /**
     * 软删除时间
     */
    @TableField(value = "removed_at")
    private LocalDate removedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SysLog id(Long id) {
        this.id = id;
        return this;
    }

    public LogType getLogType() {
        return this.logType;
    }

    public SysLog logType(LogType logType) {
        this.logType = logType;
        return this;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public String getLogContent() {
        return this.logContent;
    }

    public SysLog logContent(String logContent) {
        this.logContent = logContent;
        return this;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public Integer getOperateType() {
        return this.operateType;
    }

    public SysLog operateType(Integer operateType) {
        this.operateType = operateType;
        return this;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getUserid() {
        return this.userid;
    }

    public SysLog userid(String userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return this.username;
    }

    public SysLog username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return this.ip;
    }

    public SysLog ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMethod() {
        return this.method;
    }

    public SysLog method(String method) {
        this.method = method;
        return this;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

    public SysLog requestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestParam() {
        return this.requestParam;
    }

    public SysLog requestParam(String requestParam) {
        this.requestParam = requestParam;
        return this;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public SysLog requestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Long getCostTime() {
        return this.costTime;
    }

    public SysLog costTime(Long costTime) {
        this.costTime = costTime;
        return this;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public LocalDate getRemovedAt() {
        return this.removedAt;
    }

    public SysLog removedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
        return this;
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysLog)) {
            return false;
        }
        return id != null && id.equals(((SysLog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysLog{" +
            "id=" + getId() +
            ", logType='" + getLogType() + "'" +
            ", logContent='" + getLogContent() + "'" +
            ", operateType=" + getOperateType() +
            ", userid='" + getUserid() + "'" +
            ", username='" + getUsername() + "'" +
            ", ip='" + getIp() + "'" +
            ", method='" + getMethod() + "'" +
            ", requestUrl='" + getRequestUrl() + "'" +
            ", requestParam='" + getRequestParam() + "'" +
            ", requestType='" + getRequestType() + "'" +
            ", costTime=" + getCostTime() +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
