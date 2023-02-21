package com.mycompany.myapp.system.service.dto;

import com.mycompany.myapp.domain.enumeration.LogType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.system.domain.SysLog}的DTO。
 */
@ApiModel(description = "系统日志")
public class SysLogDTO implements Serializable {

    /**
     *
     */
    private Long id;

    /**
     * 日志类型
     */
    @ApiModelProperty(value = "日志类型")
    private LogType logType;

    /**
     * 日志内容
     */
    @ApiModelProperty(value = "日志内容")
    private String logContent;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型")
    private Integer operateType;

    /**
     * 操作用户账号
     */
    @ApiModelProperty(value = "操作用户账号")
    private String userid;

    /**
     * 操作用户名称
     */
    @ApiModelProperty(value = "操作用户名称")
    private String username;

    /**
     * IP
     */
    @ApiModelProperty(value = "IP")
    private String ip;

    /**
     * 请求java方法
     */
    @ApiModelProperty(value = "请求java方法")
    private String method;

    /**
     * 请求路径
     */
    @ApiModelProperty(value = "请求路径")
    private String requestUrl;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private String requestParam;

    /**
     * 请求类型
     */
    @ApiModelProperty(value = "请求类型")
    private String requestType;

    /**
     * 耗时
     */
    @ApiModelProperty(value = "耗时")
    private Long costTime;

    /**
     * 软删除时间
     */
    @ApiModelProperty(value = "软删除时间")
    private LocalDate removedAt;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public LocalDate getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = removedAt;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysLogDTO)) {
            return false;
        }

        SysLogDTO sysLogDTO = (SysLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sysLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SysLogDTO{" +
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
