package com.mycompany.myapp.system.service.dto;

import com.mycompany.myapp.domain.enumeration.MessageSendType;
import com.mycompany.myapp.domain.enumeration.SendStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.mycompany.myapp.system.domain.SmsMessage}的DTO。
 */
@ApiModel(description = "短信消息")
public class SmsMessageDTO implements Serializable {

    /**
     *
     */
    private Long id;

    /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题")
    private String title;

    /**
     * 发送方式
     */
    @ApiModelProperty(value = "发送方式")
    private MessageSendType sendType;

    /**
     * 接收人
     */
    @ApiModelProperty(value = "接收人")
    private String receiver;

    /**
     * 发送所需参数Json格式
     */
    @ApiModelProperty(value = "发送所需参数Json格式")
    private String params;

    /**
     * 推送内容
     */
    @ApiModelProperty(value = "推送内容")
    private String content;

    /**
     * 推送时间
     */
    @ApiModelProperty(value = "推送时间")
    private ZonedDateTime sendTime;

    /**
     * 推送状态
     */
    @ApiModelProperty(value = "推送状态")
    private SendStatus sendStatus;

    /**
     * 发送次数 超过5次不再发送
     */
    @ApiModelProperty(value = "发送次数 超过5次不再发送")
    private Integer retryNum;

    /**
     * 推送失败原因
     */
    @ApiModelProperty(value = "推送失败原因")
    private String failResult;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MessageSendType getSendType() {
        return sendType;
    }

    public void setSendType(MessageSendType sendType) {
        this.sendType = sendType;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(ZonedDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public SendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Integer getRetryNum() {
        return retryNum;
    }

    public void setRetryNum(Integer retryNum) {
        this.retryNum = retryNum;
    }

    public String getFailResult() {
        return failResult;
    }

    public void setFailResult(String failResult) {
        this.failResult = failResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(o instanceof SmsMessageDTO)) {
            return false;
        }

        SmsMessageDTO smsMessageDTO = (SmsMessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, smsMessageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsMessageDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", sendType='" + getSendType() + "'" +
            ", receiver='" + getReceiver() + "'" +
            ", params='" + getParams() + "'" +
            ", content='" + getContent() + "'" +
            ", sendTime='" + getSendTime() + "'" +
            ", sendStatus='" + getSendStatus() + "'" +
            ", retryNum=" + getRetryNum() +
            ", failResult='" + getFailResult() + "'" +
            ", remark='" + getRemark() + "'" +
            ", removedAt='" + getRemovedAt() + "'" +
            "}";
    }
}
