package com.mycompany.myapp.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.mycompany.myapp.domain.AbstractAuditingEntity;
import com.mycompany.myapp.domain.enumeration.MessageSendType;
import com.mycompany.myapp.domain.enumeration.SendStatus;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * 短信消息
 */

@TableName(value = "sms_message")
public class SmsMessage extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 发送方式
     */
    @TableField(value = "send_type")
    private MessageSendType sendType;

    /**
     * 接收人
     */
    @TableField(value = "receiver")
    private String receiver;

    /**
     * 发送所需参数Json格式
     */
    @TableField(value = "params")
    private String params;

    /**
     * 推送内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 推送时间
     */
    @TableField(value = "send_time")
    private ZonedDateTime sendTime;

    /**
     * 推送状态
     */
    @TableField(value = "send_status")
    private SendStatus sendStatus;

    /**
     * 发送次数 超过5次不再发送
     */
    @TableField(value = "retry_num")
    private Integer retryNum;

    /**
     * 推送失败原因
     */
    @TableField(value = "fail_result")
    private String failResult;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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

    public SmsMessage id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public SmsMessage title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MessageSendType getSendType() {
        return this.sendType;
    }

    public SmsMessage sendType(MessageSendType sendType) {
        this.sendType = sendType;
        return this;
    }

    public void setSendType(MessageSendType sendType) {
        this.sendType = sendType;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public SmsMessage receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getParams() {
        return this.params;
    }

    public SmsMessage params(String params) {
        this.params = params;
        return this;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getContent() {
        return this.content;
    }

    public SmsMessage content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getSendTime() {
        return this.sendTime;
    }

    public SmsMessage sendTime(ZonedDateTime sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public void setSendTime(ZonedDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public SendStatus getSendStatus() {
        return this.sendStatus;
    }

    public SmsMessage sendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
        return this;
    }

    public void setSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Integer getRetryNum() {
        return this.retryNum;
    }

    public SmsMessage retryNum(Integer retryNum) {
        this.retryNum = retryNum;
        return this;
    }

    public void setRetryNum(Integer retryNum) {
        this.retryNum = retryNum;
    }

    public String getFailResult() {
        return this.failResult;
    }

    public SmsMessage failResult(String failResult) {
        this.failResult = failResult;
        return this;
    }

    public void setFailResult(String failResult) {
        this.failResult = failResult;
    }

    public String getRemark() {
        return this.remark;
    }

    public SmsMessage remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDate getRemovedAt() {
        return this.removedAt;
    }

    public SmsMessage removedAt(LocalDate removedAt) {
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
        if (!(o instanceof SmsMessage)) {
            return false;
        }
        return id != null && id.equals(((SmsMessage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsMessage{" +
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
